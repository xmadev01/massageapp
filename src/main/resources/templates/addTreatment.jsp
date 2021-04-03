<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/static/js/addTreatment.js}"></script>
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
                                <h1 class="h4">Assign Treatment</h1>
                            </div>
                            <div class="errorMsg">
                                <ul>
                                    <li th:each="err : ${#fields.errors('all')}" th:text="${err}" />
                                </ul>
                            </div>

                            <div class="card">
                                <div class="card-body">
                                    <div class="m-sm-4">
                                        <form id="assignTreatmentFrm" name="assignTreatmentFrm" method="post" th:action="@{/assignTreatment}">
                                            <div class="mb-3">
                                                <label class="form-label">Customer Name</label>
                                                <input class="form-control" type="text" id="customerName" name="customerName" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Treatment Date</label>
                                                <input class="form-control" type="text" id="serviceDate" name="serviceDate" autocomplete="off" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Assignee</label>
                                                <select class="form-control">
                                                    <option value="">Please select...</option>
                                                    <option th:each="practitioner : ${allPractitioner}"
                                                            th:value="${practitioner}" th:text="${practitioner}"></option>
                                                </select>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Services:</label>
                                            </div>
                                            <div>
                                                <label class="form-check form-check-inline" th:each="service : ${T(com.xms.app.massage.enums.ServiceTypeEnum).values()}">
                                                    <input class="form-check-input" type="checkbox" name="serviceType" th:value="${service}">
                                                    <span class="form-check-label" th:text="${service.displayName}"></span>
                                                </label>
                                            </div>
                                            <div class="text-center mt-3">
                                                <a id="btnAssign" href="#" class="btn btn-info">Assign</a>
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