$(document ).ready(function() {

    $('#btnUpdate').click(function() {
        var form = document.getElementById('updateCustomerFrm');
        form.action = '/updateCustomer/' + $('#id').val();
        form.submit();
    });

});