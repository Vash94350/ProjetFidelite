$(document).ready(function() {
    // initialisation

    $('.modal').modal();

    $("#signin").click(function() {
        $('#connection').fadeOut(300);
        $('#inscription').delay(400).fadeIn(300);
    });

    $("#back-button").click(function() {
        $('#inscription').fadeOut(300);
        $('#connection').delay(400).fadeIn(300);
    });


    $("#login").click(function() {
        const userType = $(".active").attr('value');
        const mail = $("#emailUser").val();
        const password = $("#passwordUser").val();
        const remember = $("#remember").is(':checked');
        if('' == mail){
            Materialize.toast('Email is required !', 5000);
            $("#emailUser").addClass("invalid");
            return;
        }
        else if('' == password){
            Materialize.toast('Password is required !', 5000);
            $("#passwordUser").addClass("invalid");
            return;
        }
        $.support.cors = true;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            data: {
                'email': mail,
                'password': password
            },
            url: "http://localhost:3000/" + userType + "/login",
            error: function (jqXHR, textStatus, errorThrown) {
                var $toastContent;
                if('mail_not_verified' == jqXHR.responseJSON.errorCode){
                    var $resend = $("<button class='btn-flat toast-action'>resend</button>");
                    $resend.click(function(){
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            data: {
                                'userEmail': mail,
                                'userType': userType
                            },
                            url: "http://localhost:3000/email/resend",
                            error: function(jqXHR, textStatus, errorThrown){
                                Materialize.toast(jqXHR.responseJSON.error, 7000);
                            },
                            success: function(msg){
                                Materialize.toast(msg.success, 7000);
                            }
                        });
                    });
                    var $toastContent = $('<span>' + jqXHR.responseJSON.error + '</span>').add($resend);
                } else {
                    var $toastContent = jqXHR.responseJSON.error;
                }
                Materialize.toast($toastContent, 7000);
                $("#emailUser").addClass("invalid");
                $("#passwordUser").addClass("invalid");
            },
            success: function (msg) {
                Materialize.toast("Your are connected, id : " + ("persons" == userType ? msg.personId : msg.companyId), 7000);
            }
        });
    });
});