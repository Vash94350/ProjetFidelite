const nodemailer = require('nodemailer');
const config = require('../config/mailer');

const transport = nodemailer.createTransport({
    service: 'Gmail',
    auth: {
        user: config.MAIL_USER,
        pass: config.PASS_USER
    }
});

module.exports={
    sendValidationEmail: function(userMail, userId, userType, secretToken, callBack){
        var mail = {
            from: "fidelity.project.no.reply@gmail.com",
            to: userMail,
            subject: "veuillez valider votre email",
            html: "<h1>Valider votre email en cliquant sur le lien ci-dessous</h1>"
            + "<br>"
            + "<form name='form' action='http://localhost:3000/email/verify' method='post'>"
            + "<input type='hidden' id='userType' name='userType' value='" + userType + "'/>"
            + "<input type='hidden' id='userId' name='userId' value='" + userId + "'/>"
            + "<input type='hidden' id='secretToken' name='secretToken' value='" + secretToken + "'/>"
            + "<input type='hidden' id='callBack' name='callBack' value='" + callBack + "'/>"
            + "<button action='submit'>valider votre mail</button>"
            + "</form>"
        };
        
        transport.sendMail(mail, function(error, response){
            if(error){
                console.log(error);
            } else {
                console.log("reussite");
            }
            transport.close();
        })
    }
};