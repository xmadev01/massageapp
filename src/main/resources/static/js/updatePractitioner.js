$(document ).ready(function() {

    $('#btnUpdate').click(function() {
        var form = document.getElementById('updatePractitionerFrm');
        form.action = '/updatePractitioner/' + $('#id').val();
        form.submit();
    });

    $('#btnCancel').click(function() {
        var form = document.getElementById('updatePractitionerFrm');
        form.action = '/listPractitioner';
        form.method = 'GET';
        form.submit();
    });


});