var express = require('express');
var bcrypt = require('bcrypt');
var asyncLib = require('async');
var jwtUtils = require('../utils/jwt.utils');
var models = require('../models');
var router = express.Router();

const EMAIL_REGEX = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const PASSWORD_REGEX = /^(?=.*\d).{4,8}$/;


router.post('/login', function(req, res){
    var email = req.body.email;
    var password = req.body.password;
    
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
                return res.status(402).json({'error': 'company found but mail not verified'});
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
    var email = req.body.email;
    var password = req.body.password;
    var telephone = req.body.telephone;
    var companyName = req.body.companyName;
    var description = req.body.description;
    var siret = req.body.siret;
    var creationDate = req.body.creationDate;
    var city = req.body.city;
    var country = req.body.country;
    var companyType = req.body.companyType;
    
    if(email == null || password == null || telephone == null || companyName == null || description == null || siret == null || creationDate == null || city == null || country == null || companyType == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    if(!EMAIL_REGEX.test(email)){
        return res.status(400).json({'error': 'email is invalid'});
    }
    
    if(!PASSWORD_REGEX.test(password)){
        return res.status(400).json({'error': 'password is invalid, password must be between 4 and 8 digits long and include at least one numeric digit'});
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
            var newCompany = models.Companies.create({
                email: email,
                password: bcryptedPassword,
                telephone: telephone,
                companyName: companyName,
                description: description,
                siret: siret,
                creationDate: creationDate,
                city: city,
                country: country,
                CompaniesTypeId: companyType
            }).then(function(newCompany){
                done(newCompany);
            }).catch(function(error){
                return res.status(500).json({'error': 'cannot create a new company'})
            });
        }
    ], function(newCompany){
        return res.status(201).json({
            'companyId': newCompany.id
        });
    });
});

module.exports = router;
