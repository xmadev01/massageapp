$(document ).ready(function() {

    $('#btnUpdate').click(function() {
        var form = document.getElementById('updateStaffFrm');
        form.action = '/updateStaff/' + $('#id').val();
        form.submit();
    });

    $('#btnCancel').click(function() {
        var form = document.getElementById('updateStaffFrm');
        form.action = '/listStaff';
        form.method = 'GET';
        form.submit();
    });


});