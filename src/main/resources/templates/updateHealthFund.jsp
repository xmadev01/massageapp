<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/updateHealthFund.js}"></script>
</head>

<body>
<div class="wrapper">
    <div th:insert="fragments/nav.html :: nav" />

    <div class="main">
        <div th:insert="fragments/top.html :: top" xmlns:th="http://www.w3.org/1999/xhtml" />

        <main class="content">
            <div class="container-fluid p-0">

                <div class="row mb-2 mb-xl-3">
                    <div class="col-auto d-none d-sm-block">
                        <h4><strong>Update Health Fund</strong></h4>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
                        <div class="d-table-cell align-middle">

                            <div th:insert="fragments/messagediv.html :: messagediv" />

                            <div class="card">
                                <div class="card-body">
                                    <div class="m-sm-4">
                                        <form id="updateHealthFundFrm" name="updateHealthFundFrm" method="post" th:action="@{/updateHealthFund}" th:object="${healthFund}">
                                            <input th:type="hidden" th:field="*{id}" />
                                            <div class="mb-3">
                                                <label class="form-label">Name</label>
                                                <input class="form-control" type="text" th:field="*{name}" name="name" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Description</label>
                                                <input class="form-control" type="text" th:field="*{description}" name="name" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('description')}" th:errors="*{description}"></div>
                                            </div>
                                            <div class="text-center mt-3">
                                                <a id="btnUpdate" href="#" class="btn btn-info">Update</a>
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                <a id="btnCancel" href="#" class="btn btn-info">Cancel</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                        </div>
                </div>
            </div>

            </div>
        </main>

        <div th:insert="fragments/footer.html :: footer" xmlns:th="http://www.w3.org/1999/xhtml" />
    </div>
</div>


</body>

</html>