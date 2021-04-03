$(document ).ready(function() {

    $('#btnAdd').click(function() {
        var day = $('#days').val();
        var month = $('#months').val();
        var year = $('#years').val();
        $('#birthday').val(day + '/' + month + '/' + year);
        var form = document.getElementById('addCustomerFrm');
        form.action = '/createCustomer';
        form.submit();
    });

    $("#birthday").datepicker({
        showButtonPanel: true,
        showAnim: "clip",
        dateFormat: "mm/dd/yy"
    });

    $("#healthFund").autocomplete({
        source: function(request, response) {
            $.getJSON( "/getHealthFunds", request, function(data) {
                response(data);
            });
        }
    });

    populateBirthday();

});

function populateBirthday() {
    var birthday = $('#birthday').val();
    if (birthday) {
        $('#days').val(parseInt(birthday.substring(0, 2)));
        $('#months').val(parseInt(birthday.substring(3, 5)));
        $('#years').val(parseInt(birthday.substring(6, 10)));
    }
}