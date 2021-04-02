$(document ).ready(function() {

    $('#btnAdd').click(function() {
        var form = document.getElementById('addHealthFundFrm');
        form.action = '/createHealthFund';
        form.submit();
    });

    $('#btnCancel').click(function() {
        var form = document.getElementById('addHealthFundFrm');
        form.method = "GET";
        form.action = '/listHealthFunds';
        form.submit();
    });
});