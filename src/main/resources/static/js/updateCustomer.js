$(document ).ready(function() {

    $('#btnUpdate').click(function() {
        var form = document.getElementById('updateCustomerFrm');
        form.action = '/updateCustomer/' + $('#id').val();
        form.submit();
    });

    $('#btnCancel').click(function() {
        var form = document.getElementById('updateCustomerFrm');
        form.action = '/listCustomers';
        form.method = 'GET';
        form.submit();
    });

});