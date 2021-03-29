var selectedStaff;
$(document ).ready(function() {

    var staffTbl = applyStaffDataTable();

    $('#staffTbl tbody').on( 'click', 'tr', function () {

        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            staffTbl.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        selectedStaff = staffTbl.row('.selected').data();
    });

    $('#btnAdd').click(function () {
        var form = document.getElementById('listStaffFrm');
        form.method = 'GET';
        form.action = '/addStaff/';
        form.submit();
    })

    $('#btnUpdate').click(function () {
        if (selectedStaff) {
            var form = document.getElementById('listStaffFrm');
            form.action = '/loadStaff/' + selectedStaff.id;
            form.submit();
        }
    })

    $('#btnDeactivate').click(function () {
        if (selectedStaff) {
            var form = document.getElementById('listStaffFrm');
            form.action = '/deactivateStaff/' + selectedStaff.id;
            form.submit();
        }
    })

    $('#btnDelete').click(function () {
        if (selectedStaff) {
            var form = document.getElementById('listStaffFrm');
            form.action = '/deleteStaff/' + selectedStaff.id;
            form.submit();
        }
    })

});

function applyStaffDataTable() {

    return $('#staffTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        ajax: {
            url: "/filterStaff",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        columns: [
            {"data": "firstName", "width": "10%"},
            {"data": "lastName","width": "10%"},
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
                "targets": 2
            }

        ]
    });
}