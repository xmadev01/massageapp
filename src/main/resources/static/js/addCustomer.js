$(document ).ready(function() {

    $('#btnAdd').click(function() {
        var form = document.getElementById('addCustomerFrm');
        form.action = '/createCustomer';
        form.submit();
    });

});