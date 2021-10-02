var selectedStaff;
$(document ).ready(function() {

    var practitionerTbl = applyPractitionerDataTable();

    $('#practitionerTbl tbody').on( 'click', 'tr', function () {

        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            practitionerTbl.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        selectedStaff = practitionerTbl.row('.selected').data();
    });

    $('#btnAdd').click(function () {
        var form = document.getElementById('listPractitionerFrm');
        form.method = 'GET';
        form.action = '/addPractitioner/';
        form.submit();
    })

    $('#btnUpdate').click(function () {
        if (selectedStaff) {
            var form = document.getElementById('listPractitionerFrm');
            form.action = '/loadPractitioner/' + selectedStaff.id;
            form.submit();
        }
    })

    $('#btnDeactivate').click(function () {
        if (selectedStaff) {
            var form = document.getElementById('listPractitionerFrm');
            form.action = '/deactivatePractitioner/' + selectedStaff.id;
            form.submit();
        }
    })

    $('#btnDelete').click(function () {
        if (selectedStaff) {
            var form = document.getElementById('listPractitionerFrm');
            form.action = '/deletePractitioner/' + selectedStaff.id;
            form.submit();
        }
    })

});

function applyPractitionerDataTable() {

    return $('#practitionerTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        ajax: {
            url: "/filterPractitioner",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        columns: [
            {"data": "firstName", "width": "25%"},
            {"data": "lastName","width": "25%"},
            {"data": "associationNum","width": "25%"},
            {"data": "arhgNum","width": "25%"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row) {
                    return data;
                },
                "targets": 0
            }

        ]
    });
}