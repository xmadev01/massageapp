$(document ).ready(function() {

    var templateTbl = applyTemplateDataTable();

    $('#templateTbl tbody').on('click', 'tr', function () {

        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            templateTbl.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        selectedTemplate = templateTbl.row('.selected').data();
    });

    $('#btnAdd').click(function () {
        var form = document.getElementById('listTemplateFrm');
        form.method = 'POST'
        form.action = '/addTemplate';
        form.submit();
    })

    $('#btnUpdate').click(function () {
        if (selectedTemplate) {
            var form = document.getElementById('listTemplateFrm');
            form.action = '/loadTemplate/' + selectedTemplate.id;
            form.submit();
        }
    })

    $('#btnDelete').click(function () {
        if (selectedTemplate) {
            var form = document.getElementById('listTemplateFrm');
            $('#templateId').val(selectedTemplate.id);
            form.method = 'POST';
            form.action = '/deleteTemplate';
            form.submit();
        }
    })

});

function applyTemplateDataTable() {

    return $('#templateTbl').DataTable({
        processing: true,
        serverSide: true,
        pageLength: 50,
        searching: false,
        ajax: {
            url: "/getAllTemplates",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        columns: [
            {"data": "name", "width": "100%"}
        ]
    });
}

