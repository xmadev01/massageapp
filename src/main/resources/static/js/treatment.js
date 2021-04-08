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
            {"data": "practitionerName","width": "10%"},
            {"data": "paidAmt","width": "15%"},
            {"data": "claimedAmt","width": "15%"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row) {
                    return data.name + ' - ' + data.duration + 'min';
                },
                "targets": 2
            }
        ],
        footerCallback: function (row, data, start, end, display) {
            var api = this.api(), data;

            // Remove the formatting to get integer data for summation
            var intVal = function(i) {
                return typeof i === 'string' ?
                    i.replace(/[\$,]/g, '')*1 :
                    typeof i === 'number' ?
                        i : 0;
            };

            /*// Total over all pages
            total_col4 = api
                .column(4)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );

            total_col5 = api
                .column(5)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );*/

            // Total over this page
            pageTotal_col4 = api
                .column(4, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );

            pageTotal_col5 = api
                .column(5, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );

            // Update footer
            $(api.column(4).footer()).html(
                formatCurrencyData(pageTotal_col4)
            );
            $(api.column(5).footer()).html(
                formatCurrencyData(pageTotal_col5)
            );
        }
    });
}

function formatCurrencyData(amountStr) {
    var formatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    });
    return formatter.format(amountStr)
}