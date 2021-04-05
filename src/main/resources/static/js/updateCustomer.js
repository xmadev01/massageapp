$(document ).ready(function() {

    $('#btnUpdate').click(function() {
        setBirthday();
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

    populateBirthday();


});

function setBirthday() {
    var day = $('#days').val();
    var month = $('#months').val();
    var year = $('#years').val();
    $('#birthday').val(day + '/' + month + '/' + year);
}

function populateBirthday() {
    var birthday = $('#birthday').val();
    if (birthday) {
        $('#days').val(parseInt(birthday.substring(0, 2)));
        $('#months').val(parseInt(birthday.substring(3, 5)));
        $('#years').val(parseInt(birthday.substring(6, 10)));
    }
}