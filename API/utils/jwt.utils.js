var jwt = require('jsonwebtoken');

const JWT_SIGN_SECRET = 'uS6eC46eJ4udqD5UgZa4vFpdCgK3r34VvL77NQm6gn535HS92B';

module.exports = {
  generateTokenForUser: function(userData){
      return jwt.sign({
        'userId': userData.id,
        'userType': (userData.age == null ? 'company' : 'person'),
          
      }, JWT_SIGN_SECRET, {
          expiresIn: '1h'
      });
  }
};