$(document ).ready(function() {

    $('#btnSave').click(function() {
        var form = document.getElementById('itemsFrm');
        form.action = '/saveItems';
        form.submit();
    });
});