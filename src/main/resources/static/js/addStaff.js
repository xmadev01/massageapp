$(document ).ready(function() {

    $('#btnAdd').click(function() {
        var form = document.getElementById('addStaffFrm');
        form.action = '/createStaff';
        form.submit();
    });

});