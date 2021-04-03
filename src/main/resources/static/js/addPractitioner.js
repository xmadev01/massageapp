$(document ).ready(function() {

    $('#btnAdd').click(function() {
        var form = document.getElementById('addPractitionerFrm');
        form.action = '/createPractitioner';
        form.submit();
    });

});