$(document ).ready(function() {

    $('#content').summernote({
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
        height: 600
    });

    $('#btnCancel').click(function () {
        var form = document.getElementById('listTemplateFrm');
        form.method = 'GET';
        form.action = '/listTemplates';
        form.submit();
    })

});
