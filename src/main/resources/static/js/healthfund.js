var selectedHealthFund;
$(document ).ready(function() {

    var healthFundTbl = applyHealthFundDataTable();

    $('#healthFundTbl tbody').on( 'click', 'tr', function () {

        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            healthFundTbl.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        selectedHealthFund = healthFundTbl.row('.selected').data();
    });

    $('#btnAdd').click(function () {
        var form = document.getElementById('listHealthFundFrm');
        form.method = 'GET';
        form.action = '/addHealthFund/';
        form.submit();
    })

    $('#btnUpdate').click(function () {
        if (selectedHealthFund) {
            var form = document.getElementById('listHealthFundFrm');
            form.action = '/loadHealthFund/' + selectedHealthFund.id;
            form.submit();
        }
    })

    $('#btnDelete').click(function () {
        if (selectedHealthFund) {
            var form = document.getElementById('listHealthFundFrm');
            form.action = '/deleteHealthFund/' + selectedHealthFund.id;
            form.submit();
        }
    })

});

function applyHealthFundDataTable() {

    return $('#healthFundTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        ajax: {
            url: "/filterHealthFund",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        columns: [
            {"data": "name","width": "40%"},
            {"data": "description","width": "40%"}
        ],
        columnDefs: [

        ]
    });
}