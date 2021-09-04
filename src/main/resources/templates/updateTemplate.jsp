<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/addTemplate.js}"></script>
</head>

<body>
<div class="wrapper">
    <div th:insert="fragments/nav.html :: nav" />

    <div class="main">
        <div th:insert="fragments/top.html :: top" xmlns:th="http://www.w3.org/1999/xhtml" />

        <div>
            <main class="content">
                <div class="container-fluid p-0">
                    <div class="row mb-2 mb-xl-3">
                        <div class="col-auto d-none d-sm-block">
                            <h3><strong>Medical Case Template</strong></h3>
                        </div>
                    </div>
                    <div th:insert="fragments/messagediv.html :: messagediv" />
                    <form id="listTemplateFrm" name="listTemplateFrm" method="post" th:action="@{/saveTemplate}" th:object="${template}">
                        <input type="hidden" th:field="*{id}" />
                        <div class="container">
                            <div class="row">
                                <div class="col-md-8 mt-5">
                                        <div class="form-group">
                                            <label for="title">Template Name:</label>
                                            <input type="text" id="title" autocomplete="off" class="form-control"
                                                   th:field="*{name}"/>
                                        </div>

                                        <div class="form-group">
                                            <label for="content">Content:</label>
                                            <textarea type="text" rows="4" id="content" class="form-control"
                                                      th:field="*{content}" autocomplete="off"></textarea>
                                        </div>

                                        <button class="btn btn-info" type="submit">Save</button>
                                        &nbsp;&nbsp;&nbsp;
                                        <button class="btn btn-info" type="button" id="btnCancel">Cancel</button>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </main>
        </div>

        <div th:insert="fragments/footer.html :: footer" xmlns:th="http://www.w3.org/1999/xhtml" />
    </div>
</div>


</body>

</html>