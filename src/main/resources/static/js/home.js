var months = [ "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December" ];


$(document ).ready(function() {

    applyTreatmentDataTable();

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

    setupMedicalCaseRecordDialog();

});

function setupMedicalCaseRecordDialog() {
    $('#mcrDialog').dialog({
        autoOpen: false,
        resizable: false,
        height: 600,
        width: 1000,
        modal: true,
        closeOnEscape: true
    });
}

function applyTreatmentDataTable() {

    return $('#treatmentTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        dom: 'Blfrtip',
        order: [[0, 'asc'], [1, 'asc'], [3, 'asc'], [2, 'asc']],
        ajax: {
            url: "/filterHomeTreatments",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                d.viewMode = $('#viewMode').val();
                d.currentDay = $('#currentDay').val();
                d.currentMonth = $('#currentMonth').val();
                d.currentYear = $('#currentYear').val();
                return JSON.stringify(d);
            }
        },
        buttons: [
            {
                text: 'Export as PDF',
                extend: 'pdfHtml5',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 5, 6 ]
                }
            }
        ],
        columns: [
            {"data": "serviceDate","width": "10%"},
            {"data": "customerName","width": "15%"},
            {"data": "item","width": "15%"},
            {"data": "type","width": "10%"},
            {"data": "duration","width": "5%"},
            {"data": "healthFund","width": "15%"},
            {"data": "paidAmt","width": "10%"},
            {"data": "claimedAmt","width": "10%"},
            {"data": "medicalCaseRecord","width": "10%"}
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
                "render": function(data, type, row) {
                    if (data) {
                        return data.name;
                    }
                    return '';
                },
                "sortable": false,
                "targets": 2
            },
            {
                "sortable": false,
                "targets": 3
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
                "render": function(data, type, row) {
                    if (row.item && row.medicalCaseRecord) {
                        return "<input name='btnMedicalCaseRecord' class=\"btn btn-info\" type=\"button\" value=\"Medical Case Record\" onclick=\"$('#mcrDialog').html(this.title).dialog('open');\" title='" + row.medicalCaseRecord + "'/>";
                    } else {
                        return "";
                    }
                },
                "targets": 8
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

            var hfColData = api.column(5, { page: 'current'} )
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
                .column(6, { page: 'current'} )
                .data()
                .reduce( function (a, b, idx) {
                    if (idxList.includes(idx)) {
                        return intVal(a) + intVal(b);
                    } else {
                        return intVal(a);
                    }
                }, 0 );

            pageTotal_col6 = api
                .column(7, { page: 'current'} )
                .data()
                .reduce( function (a, b, idx) {
                    if (idxList.includes(idx)) {
                        return intVal(a) + intVal(b);
                    } else {
                        return intVal(a);
                    }
                }, 0 );

            // Update footer
            $(api.column(6).footer()).html(
                formatCurrencyData(pageTotal_col5)
            );
            $(api.column(7).footer()).html(
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