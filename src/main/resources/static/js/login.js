$(document ).ready(function() {

    $('#btnSignIn').click(function() {
        var form = document.getElementById('loginFrm');
        form.action = '/performLogin';
        form.submit();
    });

});