$(document ).ready(function() {

    var treatmentTbl = applyTreatmentDataTable();

    $('#btnAdd').click(function () {
        var form = document.getElementById('listTreatmentFrm');
        form.method = 'GET';
        form.action = '/addTreatment/';
        form.submit();
    })

});

function applyTreatmentDataTable() {

    return $('#treatmentTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        ajax: {
            url: "/filterTreatments",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        columns: [
            {"data": "serviceDate","width": "20%"},
            {"data": "customerName","width": "20%"},
            {"data": "item","width": "20%"},
            {"data": "practitionerName","width": "20%"},
            {"data": "paidAmt","width": "10%"},
            {"data": "claimedAmt","width": "10%"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row) {
                    return data.name + ' - ' + data.duration + 'min';
                },
                "targets": 2
            }
        ]
    });
}