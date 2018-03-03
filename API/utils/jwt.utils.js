var jwt = require('jsonwebtoken');

const JWT_SIGN_SECRET_ID = 'uS6eC46eJ4udqD5UgZa4vFpdCgK3r34VvL77NQm6gn535HS92B';
const JWT_SIGN_SECRET_QR = 'uS6eC46eJ4udqD5UgZa4vFpdCgK3r34VvL77NQm6gn535HS92B';

module.exports = {
    generateTokenForUser: function(userData){
        return jwt.sign({
            'userId': userData.id,
            'userType': (userData.birthDate == null ? 'company' : 'person'),

        }, JWT_SIGN_SECRET_ID, {
            expiresIn: '760h'
        });
    },
    generateQRTokenForUser: function(userData){
        return jwt.sign({
            'userId': userData.id,
            'userType': (userData.birthDate == null ? 'company' : 'person'), 
        }, JWT_SIGN_SECRET_QR, {
            expiresIn: '24h'
        });
    }
};
