$(document ).ready(function() {

    $('#btnUpdate').click(function() {
        var form = document.getElementById('updateHealthFundFrm');
        form.action = '/updateHealthFund/' + $('#id').val();
        form.submit();
    });

    $('#btnCancel').click(function() {
        var form = document.getElementById('updateHealthFundFrm');
        form.action = '/listHealthFunds';
        form.method = 'GET';
        form.submit();
    });


});