<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/addHealthFund.js}"></script>
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
                        <h4><strong>Massage Admin</strong> Dashboard</h4>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
                        <div class="d-table-cell align-middle">

                            <div class="text-center mt-4">
                                <h1 class="h4">New Health Fund</h1>
                            </div>
                            <div th:insert="fragments/messagediv.html :: messagediv" />

                            <div class="card">
                                <div class="card-body">
                                    <div class="m-sm-4">
                                        <form id="addHealthFundFrm" name="addHealthFundFrm" method="post" th:action="@{/createHealthFund}" th:object="${healthFund}">
                                            <div class="mb-3">
                                                <label class="form-label">Name</label>
                                                <input class="form-control" type="text" th:field="*{name}" name="name" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></div>
                                            </div>
                                            <div class="text-center mt-3">
                                                <a id="btnAdd" href="#" class="btn btn-info">Add</a>
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