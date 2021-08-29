var months = [ "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December" ];


$(document ).ready(function() {

    var treatmentTbl = applyTreatmentDataTable();

    $('#dayView').click(function () {
        selectView('day');
        setCurrentDay();
        $('#treatmentTbl').DataTable().ajax.reload();
    })

    $('#monthView').click(function () {
        selectView('month');
        setCurrentYear();
        setCurrentMonth();
        $('#treatmentTbl').DataTable().ajax.reload();
    })

    $('#yearView').click(function () {
        selectView('year');
        setCurrentYear();
        $('#treatmentTbl').DataTable().ajax.reload();
    })

    $('#prevView').click(function () {
        calAndSetCurrentView(false);
        $('#treatmentTbl').DataTable().ajax.reload();
    })

    $('#nextView').click(function () {
        calAndSetCurrentView(true);
        $('#treatmentTbl').DataTable().ajax.reload();
    })

});

function applyTreatmentDataTable() {

    return $('#treatmentTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        searching: false,
        dom: 'Bfrtip',
        order: [[1, 'asc'], [3, 'asc'], [2, 'asc'], [0, 'asc']],
        ajax: {
            url: "/loadCustomerTreatments",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                d.viewMode = $('#viewMode').val();
                d.currentDay = $('#currentDay').val();
                d.currentMonth = $('#currentMonth').val();
                d.currentYear = $('#currentYear').val();
                d.customerId = $('#customerId').val();
                console.log(JSON.stringify(d))
                return JSON.stringify(d);
            }
        },
        buttons: [
            'print'
        ],
        columns: [
            {"data": "serviceDate","width": "15%"},
            {"data": "customerName","width": "15%"},
            {"data": "item","width": "15%"},
            {"data": "type","width": "10%"},
            {"data": "healthFund","width": "25%"},
            {"data": "paidAmt","width": "10%"},
            {"data": "claimedAmt","width": "10%"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row) {
                    if (data) {
                        return data.name + ' - ' + data.duration + 'min';
                    }
                    return '';
                },
                "targets": 2
            },
            {
                "render": function(data, type, row) {
                   if (data && data.indexOf('Total') >= 0) {
                       return '<span style="float: right"><strong>' + data + '</strong></span>';
                   } else if (data && data.indexOf('Total') < 0) {
                       return data;
                   } else {
                       return "";
                   }
                },
                "targets": 4
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

            var idxList = [];

            var hfColData = api.column(4, { page: 'current'} )
                            .data();

            //skip individual insurance total and all insurance total row
            for (var i = 0; i < hfColData.length; i++) {
                var hfText = hfColData[i];
                if (!hfText || (hfText && hfText.indexOf('Total') < 0)) {
                    idxList[idxList.length] = i;
                }
            }
            // Total over this page
            pageTotal_col5 = api
                .column(5, { page: 'current'} )
                .data()
                .reduce( function (a, b, idx) {
                    if (idxList.includes(idx)) {
                        return intVal(a) + intVal(b);
                    } else {
                        return intVal(a);
                    }
                }, 0 );

            pageTotal_col6 = api
                .column(6, { page: 'current'} )
                .data()
                .reduce( function (a, b, idx) {
                    if (idxList.includes(idx)) {
                        return intVal(a) + intVal(b);
                    } else {
                        return intVal(a);
                    }
                }, 0 );

            // Update footer
            $(api.column(5).footer()).html(
                formatCurrencyData(pageTotal_col5)
            );
            $(api.column(6).footer()).html(
                formatCurrencyData(pageTotal_col6)
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

function calAndSetCurrentView(isNext) {

    var viewMode = $('#viewMode').val();
    if (viewMode == 'day') {
        var currentDay = new Date($('#currentDay').val());
        if (isNext) {
            currentDay.setDate(currentDay.getDate() + 1);
        } else {
            currentDay.setDate(currentDay.getDate() - 1);
        }
        var year = currentDay.getFullYear();
        var month = currentDay.getMonth() + 1;
        var date = currentDay.getDate();
        var dateStr = year + '-' + month.toString().padStart(2, '0') + '-' + date.toString().padStart(2, '0');
        $('#currentDay').val(dateStr);
        $('#dmy').text(dateStr);
    } else if (viewMode == 'month') {
        var currentMonth = parseInt($('#currentMonth').val());
        var currentYear = parseInt($('#currentYear').val());
        var month, year;
        if (isNext) {
            if (currentMonth == 12) {
                month = 1;
                year = currentYear + 1;
            } else {
                month = currentMonth + 1;
                year = currentYear;
            }
        } else {
            if (currentMonth == 1) {
                month = 12;
                year = currentYear - 1;
            } else {
                month = currentMonth - 1;
                year = currentYear;
            }
        }
        $('#currentMonth').val(month.toString().padStart(2, '0'));
        $('#currentYear').val(year);
        $('#dmy').text(months[month - 1]);
    } else if (viewMode == 'year') {
        var currentYear = parseInt($('#currentYear').val());
        var year;
        if (isNext) {
            year = currentYear + 1;
        } else {
            year = currentYear - 1;
        }
        $('#currentYear').val(year);
        $('#dmy').text(year);
    }

}

function setCurrentDay() {
    var currentDay = new Date();
    var year = currentDay.getFullYear();
    var month = currentDay.getMonth() + 1;
    var date = currentDay.getDate();
    var dateStr = year + '-' + month.toString().padStart(2, '0') + '-' + date.toString().padStart(2, '0');
    $('#currentDay').val(dateStr);
    $('#dmy').text(dateStr);
}

function setCurrentMonth() {
    var currentDay = new Date();
    var month = currentDay.getMonth() + 1;
    var monthStr = month.toString().padStart(2, '0');
    $('#currentMonth').val(monthStr);
    $('#dmy').text(months[month - 1]);
}

function setCurrentYear() {
    var currentDay = new Date();
    var year = currentDay.getFullYear();
    $('#currentYear').val(year);
    $('#dmy').text(year);
}

function selectView(viewMode) {
    $('#viewMode').val(viewMode);
    $('#dayView').removeClass();
    $('#monthView').removeClass();
    $('#yearView').removeClass();
    if (viewMode == 'day') {
        $('#dayView').addClass('calendar-nav-item-selected');
        $('#monthView').addClass('calendar-nav-item');
        $('#yearView').addClass('calendar-nav-item');
    } else if (viewMode == 'month') {
        $('#dayView').addClass('calendar-nav-item');
        $('#monthView').addClass('calendar-nav-item-selected');
        $('#yearView').addClass('calendar-nav-item');
    } else if (viewMode == 'year') {
        $('#dayView').addClass('calendar-nav-item');
        $('#monthView').addClass('calendar-nav-item');
        $('#yearView').addClass('calendar-nav-item-selected');
    }
}