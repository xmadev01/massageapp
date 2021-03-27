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
        showAnim: "clip"
    });

});