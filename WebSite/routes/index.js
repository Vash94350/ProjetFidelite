var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {

    var action = req.query.action;
    var email = req.query.email;
    
    if(action == null){
        action = "";
    }
    
    if(email == null){
        email = "";
    }


    res.render("pages/index", {
        action: action,
        email: email
    });
});

module.exports = router;
