<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/addTreatment.js}"></script>
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
                        <h4><strong>New Treatment</strong></h4>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
                        <div class="d-table-cell align-middle">

                            <div th:insert="fragments/messagediv.html :: messagediv" />

                            <div class="card">
                                <div class="card-body">
                                    <div class="m-sm-4">
                                        <form id="assignPractitionerFrm" name="assignPractitionerFrm" method="post" th:action="@{/assignPractitioner}" th:object="${treatmentVo}">
                                            <input th:type="hidden" th:id="customerId" />
                                            <div class="mb-3">
                                                <label class="form-label">Customer Name</label>
                                                <input class="form-control" type="text" th:field="*{customerName}" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Treatment Date</label>
                                                <input class="form-control" type="text" id="serviceDate" name="serviceDate" th:field="*{serviceDate}" readonly autocomplete="off" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Assignee</label>
                                                <select class="form-control" th:field="*{practitionerId}">
                                                    <option value="">Please select...</option>
                                                    <option th:each="practitioner : ${practitioners}"
                                                            th:value="${practitioner.id}" th:text="${practitioner.fullName}"></option>
                                                </select>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Treatments</label>
                                            </div>
                                            <div>
                                                <label class="form-check form-check-inline" th:each="item  : ${items}">
                                                    <input class="form-check-input" type="checkbox" name="itemIds" th:value="${item.id}" />
                                                    <span class="form-check-label" th:text="${item.name} + ' - ' + ${item.duration} + 'min'"></span>
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