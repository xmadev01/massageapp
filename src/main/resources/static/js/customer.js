var selectedCustomer;
$(document ).ready(function() {

    $('#btnCreate').click(function() {
        var form = document.getElementById('addCustomerFrm');
        form.action = '/createCustomer';
        form.submit();
    });

    var customerTbl = applyCustomerDataTable();

    $('#customerTbl tbody').on( 'click', 'tr', function () {

        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            customerTbl.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        selectedCustomer = customerTbl.row('.selected').data();
    });

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
            {"data": "firstName", "width": "20%"},
            {"data": "lastName","width": "20%"},
            {"data": "email", "width": "10%"},
            {"data": "phone", "width": "10%"},
            {"data": "mobile", "width": "10%"},
            {"data": "healthFund", "width": "10%"},
            {"data": "membershipNum", "width": "10%"},
            {"data": "active", "width": "10%"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row) {
                    return data;
                },
                "targets": 0
            },
            {
                "render": function(data, type, row) {
                    if (data == true) {
                       return '<img src="/images/true.png" />';
                    } else {
                       return '<img src="/images/false.png" />';
                    }
                },
                "targets": 7
            }

        ]
    });
}