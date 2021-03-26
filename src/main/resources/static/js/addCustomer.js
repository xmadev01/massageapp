$(document ).ready(function() {

    $('#btnCreate').click(function() {
        var form = document.getElementById('addCustomerFrm');
        form.action = '/createCustomer';
        form.submit();
    });

});