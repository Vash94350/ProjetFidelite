var express = require('express');
var bcrypt = require('bcrypt');
var jwtUtils = require('../utils/jwt.utils');
var models = require('../models');
var router = express.Router();


/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.post('/login', function(req, res, next){
    var email = req.body.email;
    var password = req.body.password;
    
    if(email == null || password == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    models.Persons.findOne({
        where: { email: email }
    }).then(function(personFound){
        if(personFound){
            bcrypt.compare(password, personFound.password, function(errBycrypt, resBycrypt){
               if(resBycrypt){
                   return res.status(200).json({
                      'personId': personFound.id,
                      'token' : jwtUtils.generateTokenForUser(personFound)
                   });
               } else {
                   return res.status(403).json({'error': 'invalid password for this person'});
               }
            });
        } else {
            return res.status(404).json({'error': 'person don\'t exists in database'});
        }
    }).catch(function(error){
        return res.status(500).json({'error': 'unable to verify the person'});
    });
});

router.post('/register', function(req, res){
    var email = req.body.email;
    var password = req.body.password;
    var firstname = req.body.firstname;
    var lastname = req.body.lastname;
    var sex = req.body.sex;
    var age = req.body.age;
    var city = req.body.city;
    var country= req.body.country;
    
    if(email == null || password == null || firstname == null || lastname == null ||
      sex == null || age == null || city == null || country == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    models.Persons.findOne({
        attributes: ['email'],
        where: { email: email }
    }).then(function(personFound){
        if(!personFound){
            bcrypt.hash(password, 5, function(err, bcryptedPassword){
                var newPerson = models.Persons.create({
                    email: email,
                    password: bcryptedPassword,
                    firstname: firstname,
                    lastname: lastname,
                    sex: sex,
                    age: age,
                    city: city,
                    country: country
                }).then(function(newPerson){
                    return res.status(201).json({
                        'personId': newPerson.id
                    });
                }).catch(function(error){
                    return res.status(500).json({'error': 'cannot create a new person'})
                });
            });
        } else {
            return res.status(409).json({'error': 'a person with this email already exists'})
        }
    }).catch(function(error){
        return res.status(500).json({'error': 'unable to verify the person'});
    });
});

module.exports = router;
