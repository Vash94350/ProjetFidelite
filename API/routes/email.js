const express = require('express');
const bcrypt = require('bcrypt');
const asyncLib = require('async');
const randomstring = require('randomstring');
const mailerUtils = require('../utils/mailer.utils');
const models = require('../models');
const router = express.Router();

const USER_REGEX = /^person$|^company$/;

router.post('/resend', function(req, res){
    const userEmail = req.body.userEmail;
    const userType = req.body.userType;
    
    if(userEmail == null || userType == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    if(!USER_REGEX.test(userType)){
        return res.status(400).json({'error': 'user type is invalid, it must be "person" or "company"'});
    }
    
    asyncLib.waterfall([
        function(done){
            if("person" == userType){
                models.Persons.findOne({
                    where: { email: userEmail }
                }).then(function(personFound){
                    done(null, personFound);
                }).catch(function(error){
                    return res.status(500).json({'error': 'unable to verify the person'});
                });
            } else {
                models.Companies.findOne({
                    where: {email: userEmail }
                }).then(function(companyFound){
                    done(null, companyFound);
                }).catch(function(error){
                   return res.status(500).json({'error': 'unable to verify the company'}); 
                });
            }
        },
        function(userFound, done){
            if(userFound){
                const secretToken = randomstring.generate();
                bcrypt.hash(secretToken, 5, function(err, bcryptedToken){
                    done(null, userFound, secretToken, bcryptedToken);
                });
            } else {
                return res.status(404).json({'error': 'no user matches this email and type'});
            }
        },
        function(userFound, secretToken, bCryptedToken, done){
            userFound.validationToken = bCryptedToken;
            userFound.save(function(err){
                if(err){
                    return res.status(400).json({'error': 'unable to save the validation token for this user'});
                }
            }).then(function(){
               done(null, userFound, secretToken); 
            });
        },
        function(userFound, secretToken, done){
            mailerUtils.sendValidationEmail(userFound.email, userFound.id, userType, secretToken);
            console.log(userFound.email + userFound.id + userType + secretToken);
            done(userFound);
        }
    ], function(userFound){
        return res.status(201).json({
            'success': 'a validation email has been sent to ' + userFound.email
        });
    });
});


router.post('/verify', function(req, res){
    const userId = req.body.x_userId;
    const userType = req.body.x_userType;
    const secretToken = req.body.x_secretToken;
    
    if(userId == null || userType == null || secretToken == null){
        return res.status(400).json({'error': 'missing parameter'});
    }
    
    if(!USER_REGEX.test(userType)){
        return res.status(400).json({'error': 'user type is invalid, it must be "person" or "company"'});
    }
    
    asyncLib.waterfall([
        function(done){
            if("person" == userType){
                models.Persons.findOne({
                    where: { id: userId }
                }).then(function(personFound){
                    done(null, personFound);
                }).catch(function(error){
                    return res.status(500).json({'error': 'unable to verify the person'});
                });
            } else {
                models.Companies.findOne({
                    where: {id: userId }
                }).then(function(companyFound){
                    done(null, companyFound);
                }).catch(function(error){
                   return res.status(500).json({'error': 'unable to verify the company'}); 
                });
            }
        },
        function(userFound, done){
            if(userFound){
                bcrypt.compare(secretToken, userFound.validationToken, function(errBycrypt, resBycrypt){
                    done(null, userFound, resBycrypt);
                });
            } else {
                return res.status(404).json({'error': 'no user matches this id'});
            }
        },
        function(userFound, resBycrypt, done){
            if(resBycrypt && '' != userFound.validationToken){
                done(null, userFound);
            } else {
                return res.status(403).json({'error': 'the validation token is invalid or expired'});
            }
        },
        function(userFound, done){
            userFound.isMailVerified = true;
            userFound.validationToken = '';
            userFound.save(function(err){
                if(err){
                    return res.status(400).json({'error': 'unable to save the validation token and changement of mail verified for this user'});
                }
            }).then(function(){
               done(userFound); 
            });
        }
    ], function(userFound){
        return res.status(201).json({
            'success': 'email user of id ' + userFound.id + ' has been verified'
        });
    });
    
});

module.exports = router;
