const express = require('express');
const bcrypt = require('bcrypt');
const asyncLib = require('async');
const randomstring = require('randomstring');
const jwtUtils = require('../utils/jwt.utils');
const mailerUtils = require('../utils/mailer.utils');
const consts = require('../utils/const');
const models = require('../models');
const router = express.Router();


router.post('/login', function(req, res){
    const email = req.body.email;
    const password = req.body.password;
    
    if(email == null || password == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    asyncLib.waterfall([
        function(done){
            models.Companies.findOne({
                where: { email: email }
            }).then(function(companyFound){
                done(null, companyFound);
            }).catch(function(error){
                return res.status(500).json({'error': 'unable to verify the company'});
            });
        },
        function(companyFound, done){
            if(companyFound){
                bcrypt.compare(password, companyFound.password, function(errBycrypt, resBycrypt){
                    done(null, companyFound, resBycrypt);
                });
            } else {
                return res.status(404).json({'error': 'company don\'t exists in database'});
            }
        },
        function(companyFound, resBycrypt, done){
            if(resBycrypt){
                done(null, companyFound);
            } else {
                return res.status(403).json({'error': 'invalid password for this company'});
            }
        },
        function(companyFound){
            if(0 != companyFound.isMailVerified){
                done(companyFound);
            } else {
                return res.status(403).json({'errorCode' : 'mail_not_verified', 'error': 'company found but mail not verified'});
            }
        }
    ], function(companyFound){
        return res.status(200).json({
          'companyId': companyFound.id,
          'token' : jwtUtils.generateTokenForUser(companyFound)
        });
    });
});

router.post('/register', function(req, res){
    const email = req.body.email;
    const password = req.body.password;
    const telephone = req.body.telephone;
    const companyName = req.body.companyName;
    const description = req.body.description;
    const siret = req.body.siret;
    const creationDate = req.body.creationDate;
    const city = req.body.city;
    const country = req.body.country;
    const companyType = req.body.companyType;
    
    if(email == null || password == null || telephone == null || companyName == null || description == null || siret == null || creationDate == null || city == null || country == null || companyType == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    if(!consts.EMAIL_REGEX.test(email)){
        return res.status(400).json({'error': 'email is invalid'});
    }
    
    if(!consts.PASSWORD_REGEX.test(password)){
        return res.status(400).json({'error': 'password is invalid, password must be between 4 and 8 digits long and include at least one numeric digit'});
    }
    
    if(!consts.DATE_REGEX.test(creationDate)){
        return res.status(400).json({'error': 'create date is invalid, it must looks like yyyy-mm-dd'});
    }
    
    asyncLib.waterfall([
        function(done){
            models.Companies.findOne({
                attributes: ['email'],
                where: { email: email }
            }).then(function(companyFound){
                done(null, companyFound);
            }).catch(function(error){
                return res.status(500).json({'error': 'unable to verify the company'});
            });
        },
        function(companyFound, done){
            if(!companyFound){
                bcrypt.hash(password, 5, function(err, bcryptedPassword){
                    done(null, companyFound, bcryptedPassword);
                });
            } else {
                return res.status(409).json({'error': 'a company with this email already exists'});
            }
        },
        function(companyFound, bcryptedPassword, done){
            const secretToken = randomstring.generate();
            bcrypt.hash(secretToken, 5, function(err, bcryptedToken){
                done(null, companyFound, bcryptedPassword, secretToken, bcryptedToken);
            });
        },
        function(companyFound, bcryptedPassword, secretToken, bcryptedToken, done){
            const newCompany = models.Companies.create({
                email: email,
                password: bcryptedPassword,
                telephone: telephone,
                companyName: companyName,
                description: description,
                siret: siret,
                creationDate: creationDate,
                city: city,
                country: country,
                CompaniesTypeId: companyType,
                validationToken: bcryptedToken
            }).then(function(newCompany){
                done(null, newCompany, secretToken);
            }).catch(function(error){
                return res.status(500).json({'error': 'cannot create a new company'})
            });
        },
        function(newCompany, secretToken, done){
            console.log(secretToken);
            mailerUtils.sendValidationEmail(newCompany.email, newCompany.id, 'companies', secretToken);
            done(newCompany);
        }
    ], function(newCompany){
        return res.status(201).json({
            'companyId': newCompany.id
        });
    });
});

module.exports = router;
