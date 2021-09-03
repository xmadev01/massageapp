
$(document ).ready(function() {

    applyProviderRptDataTable();

    $("#fromDate").datepicker({
        showButtonPanel: true,
        showAnim: "clip",
        dateFormat: "dd/mm/yy"
    });

    $("#toDate").datepicker({
        showButtonPanel: true,
        showAnim: "clip",
        dateFormat: "dd/mm/yy"
    });

    $("#btnSearch").click(function() {
        $('#providerRptTbl').DataTable().ajax.reload();
    });

});

function applyProviderRptDataTable() {

    return $('#providerRptTbl').DataTable({
        processing: true,
        serverSide: true,
        searching: false,
        pageLength: 50,
        dom: 'Blfrtip',
        order: [[0, 'asc'], [1, 'asc'], [2, 'asc']],
        ajax: {
            url: "/filterProviderReports",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                d.fromDate = $('#fromDate').val();
                d.toDate = $('#toDate').val();
                d.providerName = $('#providerName').val();
                d.healthFund = $('#healthFund').val();
                return JSON.stringify(d);
            }
        },
        buttons: [
            'print'
        ],
        columns: [
            {"data": "serviceDate","width": "15%"},
            {"data": "practitioner","width": "15%"},
            {"data": "healthFund","width": "15%"},
            {"data": "numberOfAcupunctureClient","width": "10%"},
            {"data": "chargedAcupunctureAmt","width": "10%"},
            {"data": "claimedAcupunctureAmt","width": "10%"},
            {"data": "numberOfMassageClient","width": "10%"},
            {"data": "chargedMassageAmt","width": "10%"},
            {"data": "claimedMassageAmt","width": "10%"}
        ],
        columnDefs: [
            {
                "sortable": false,
                "targets": 0
            },
            {
                "sortable": false,
                "targets": 1
            },
            {
                "sortable": false,
                "targets": 2
            },
            {
                "sortable": false,
                "targets": 3
            },
            {
                "sortable": false,
                "targets": 4
            },
            {
                "sortable": false,
                "targets": 5
            },
            {
                "sortable": false,
                "targets": 6
            },
            {
                "sortable": false,
                "targets": 7
            },
            {
                "sortable": false,
                "targets": 8
            }
        ]
    });
}