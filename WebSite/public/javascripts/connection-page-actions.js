$(document).ready(function() {
    // initialisation

    $('.modal').modal();

    $('select').material_select();

    $('.datepicker').pickadate({
        selectMonths: true, // Creates a dropdown to control month
        selectYears: 15, // Creates a dropdown of 15 years to control year,
        today: 'Today',
        clear: 'Clear',
        close: 'Ok',
        format: 'yyyy-dd-mm',
        closeOnSelect: false // Close upon selecting a date,
    });


    $("#signin").click(function() {
        $('#connection').fadeOut(100);
        $('#inscription').delay(200).fadeIn(100);
    });

    $("#back-button").click(function() {
        $('#inscription').fadeOut(100);
        $('#connection').delay(200).fadeIn(100);
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
    
    $("#PSIRegister").click(function(){
        const email = $("#PSIEmailUser").val();
        const phone = $("#PSITelephone").val();
        const password = $("#PSIPasswordUser").val();
        const validationPassword = $("#PSIConfPasswordUser").val();
        const lastname = $("#PSILastname").val();
        const firstname = $("#PSIFirstname").val();
        const gender = $("#PSIGender").val();
        const birthDate = $("#PSIBirthDate").val();
        const city = $("#PSICity").val();
        const country = $("#PSICountry").val();
        
        $.ajax({
            type: 'POST',
            dataType: 'json',
            data: {
                'email': email,
                'password': password,
                'firstname': firstname,
                'lastname': lastname,
                'sex': gender,
                'birthDate': birthDate,
                'city': city,
                'country': country,
                'telephone': phone
            },
            url: "http://localhost:3000/persons/register",
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.stringify(jqXHR));
            },
            success: function (msg) {
                alert(JSON.stringify(msg));
            }
        });
    });
});