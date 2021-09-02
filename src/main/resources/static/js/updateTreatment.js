$(document ).ready(function() {

    $("#customerName").autocomplete({
        source: function(request, response) {
            $.getJSON( "/getCustomers", request, function(data) {
                response(data);
            });
        }
    });

    $("#serviceDate").datepicker({
        showButtonPanel: true,
        showAnim: "clip",
        dateFormat: "dd/mm/yy"
    });

    $('#btnAssign').click(function() {
        var form = document.getElementById('assignPractitionerFrm');
        form.action = '/assignPractitionerForUpdate';
        form.submit();
    });

    $('#btnCancel').click(function() {
        var form = document.getElementById('assignPractitionerFrm');
        form.method = 'GET';
        form.action = '/listTreatments';
        form.submit();
    });

    $('#medicalCaseTemplate').change(function() {
        $.get("/getTemplateById/" + this.value, function(data) {
            $('#medicalCaseRecord').summernote('reset');
            $('#medicalCaseRecord').summernote('pasteHTML', data.content);
        });
    });

    $('#medicalCaseRecord').summernote({
        toolbar: [
            // [groupName, [list of button]]
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['font', ['strikethrough', 'superscript', 'subscript']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']]
        ],
        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Merriweather'],
        height: 400
    });

});