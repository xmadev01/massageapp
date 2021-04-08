$(document ).ready(function() {

    $("#customerName").autocomplete({
        source: function(request, response) {
            $.getJSON( "/getCustomers", request, function(data) {
                response(data);
            });
        }
    });

    $("#serviceDate").datepicker({
        showButtonPanel: true,
        showAnim: "clip",
        dateFormat: "dd/mm/yy"
    });

    $('#btnAssign').click(function() {
        var form = document.getElementById('assignPractitionerFrm');
        form.action = '/assignPractitioner';
        form.submit();
    });

    $('#btnCancel').click(function() {
        var form = document.getElementById('assignPractitionerFrm');
        form.method = 'GET';
        form.action = '/listTreatments';
        form.submit();
    });

});