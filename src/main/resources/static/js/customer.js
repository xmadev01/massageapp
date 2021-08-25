var selectedCustomer;
$(document ).ready(function() {

    $('#btnCreate').click(function() {
        var form = document.getElementById('addCustomerFrm');
        form.action = '/createCustomer';
        form.submit();
    });

    var customerTbl = applyCustomerDataTable();

    $('#customerTbl tbody').on('click', 'tr', function () {

        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            customerTbl.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        selectedCustomer = customerTbl.row('.selected').data();
    });

    $('#btnAdd').click(function () {
        var form = document.getElementById('listCustomerFrm');
        form.method = 'GET'
        form.action = '/addCustomer';
        form.submit();
    })

    $('#btnUpdate').click(function () {
        if (selectedCustomer) {
            var form = document.getElementById('listCustomerFrm');
            form.action = '/loadCustomer/' + selectedCustomer.id;
            form.submit();
        }
    })

    $('#btnDeactivate').click(function () {
        if (selectedCustomer) {
            var form = document.getElementById('listCustomerFrm');
            form.action = '/deactivateCustomer/' + selectedCustomer.id;
            form.submit();
        }
    })

    $('#btnDelete').click(function () {
        if (selectedCustomer) {
            var form = document.getElementById('listCustomerFrm');
            form.action = '/deleteCustomer/' + selectedCustomer.id;
            form.submit();
        }
    })

});

function applyCustomerDataTable() {

    return $('#customerTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        ajax: {
            url: "/filterCustomers",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        columns: [
            {"data": "firstName", "width": "10%"},
            {"data": "middleName","width": "10%"},
            {"data": "lastName","width": "10%"},
            {"data": "email", "width": "20%"},
            {"data": "phone1", "width": "15%"},
            {"data": "phone2", "width": "15%"},
            {"data": "healthFund", "width": "20%"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row) {
                    if (data) {
                        return data.name;
                    } else {
                        return "";
                    }
                },
                "targets": 6
            }

        ]
    });
}