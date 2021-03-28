$(document ).ready(function() {

    $("#customerName").autocomplete({
        source: function(request, response) {
            $.getJSON( "/getCustomerNames", request, function(data) {
                response(data);
            });
        }
    });

    $("#serviceDate").datepicker({
        showButtonPanel: true,
        showAnim: "clip",
        dateFormat: "mm/dd/yy"
    });

    $('#btnAssign').click(function() {
        var form = document.getElementById('assignServiceFrm');
        form.action = '/assignService';
        form.submit();
    });

});