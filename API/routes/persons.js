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
    var email = req.body.email;
    var password = req.body.password;
    var telephone = req.body.telephone;
    var firstname = req.body.firstname;
    var lastname = req.body.lastname;
    var sex = req.body.sex;
    var age = req.body.age;
    var city = req.body.city;
    var country = req.body.country;
    
    if(email == null || password == null || telephone == null || firstname == null || lastname == null || sex == null || age == null || city == null || country == null){
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
            var newPerson = models.Persons.create({
                email: email,
                password: bcryptedPassword,
                telephone: telephone,
                firstname: firstname,
                lastname: lastname,
                sex: sex,
                age: age,
                city: city,
                country: country
            }).then(function(newPerson){
                done(newPerson);
            }).catch(function(error){
                return res.status(500).json({'error': 'cannot create a new person'})
            });
        }
    ], function(newPerson){
        return res.status(201).json({
            'personId': newPerson.id
        });
    });
});

module.exports = router;
