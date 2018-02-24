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
            models.Persons.findOne({
                where: { email: email }
            }).then(function(personFound){
                done(null, personFound);
            }).catch(function(error){
                return res.status(500).json({'error': 'unable to verify the person'});
            });
        },
        function(personFound, done){
            if(personFound){
                bcrypt.compare(password, personFound.password, function(errBycrypt, resBycrypt){
                    done(null, personFound, resBycrypt);
                });
            } else {
                return res.status(404).json({'error': 'person don\'t exists in database'});
            }
        },
        function(personFound, resBycrypt, done){
            if(resBycrypt){
                done(null, personFound);
            } else {
                return res.status(403).json({'error': 'invalid password for this person'});
            }
        },
        function(personFound, done){
            if(0 != personFound.isMailVerified){
                done(personFound);
            } else {
                return res.status(402).json({'error': 'person found but mail not verified'});
            }
        }
    ], function(personFound){
        return res.status(200).json({
          'personId': personFound.id,
          'token' : jwtUtils.generateTokenForUser(personFound)
        });
    });
});

router.post('/register', function(req, res){
    const email = req.body.email;
    const password = req.body.password;
    const telephone = req.body.telephone;
    const firstname = req.body.firstname;
    const lastname = req.body.lastname;
    const sex = req.body.sex;
    const birthDate = req.body.birthDate;
    const city = req.body.city;
    const country = req.body.country;
    
    if(email == null || password == null || telephone == null || firstname == null || lastname == null || sex == null || birthDate == null || city == null || country == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    if(!consts.EMAIL_REGEX.test(email)){
        return res.status(400).json({'error': 'email is invalid'});
    }
    
    if(!consts.PASSWORD_REGEX.test(password)){
        return res.status(400).json({'error': 'password is invalid, password must be between 4 and 8 digits long and include at least one numeric digit'});
    }
    
    if(!consts.DATE_REGEX.test(birthDate)){
        return res.status(400).json({'error': 'birth date is invalid, it must looks like yyyy-mm-dd'});
    }
    
    asyncLib.waterfall([
        function(done){
            models.Persons.findOne({
                attributes: ['email'],
                where: { email: email }
            }).then(function(personFound){
                done(null, personFound);
            }).catch(function(error){
                return res.status(500).json({'error': 'unable to verify the person'});
            });
        },
        function(personFound, done){
            if(!personFound){
                bcrypt.hash(password, 5, function(err, bcryptedPassword){
                    done(null, personFound, bcryptedPassword);
                });
            } else {
                return res.status(409).json({'error': 'a person with this email already exists'});
            }
        },
        function(personFound, bcryptedPassword, done){
            const secretToken = randomstring.generate();
            bcrypt.hash(secretToken, 5, function(err, bcryptedToken){
                done(null, personFound, bcryptedPassword, secretToken, bcryptedToken);
            });
        },
        function(personFound, bcryptedPassword, secretToken, bcryptedToken, done){
            const newPerson = models.Persons.create({
                email: email,
                password: bcryptedPassword,
                telephone: telephone,
                firstname: firstname,
                lastname: lastname,
                sex: sex,
                birthDate: birthDate,
                city: city,
                country: country,
                validationToken: bcryptedToken
            }).then(function(newPerson){
                done(null, newPerson, secretToken);
            }).catch(function(error){
                return res.status(500).json({'error': 'cannot create a new person'})
            });
        },
        function(newPerson, secretToken, done){
            mailerUtils.sendValidationEmail(newPerson.email, newPerson.id, 'person', secretToken);
            done(newPerson);
        }
    ], function(newPerson){
        return res.status(201).json({
            'personId': newPerson.id
        });
    });
});

module.exports = router;
