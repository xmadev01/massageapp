$(document ).ready(function() {

    $("#customerName").autocomplete({
        source: function(request, response) {
            $.getJSON( "/getCustomers", request, function(data) {
                var customerNames = $.map(data, function(element, index) {
                    return element.fullName;
                })
                response(customerNames);
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