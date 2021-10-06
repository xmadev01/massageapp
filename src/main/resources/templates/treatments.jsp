<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/treatment.js}"></script>
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
                            <h3><strong>Treatments</strong></h3>
                        </div>
                    </div>
                    <div th:insert="fragments/messagediv.html :: messagediv" />
                    <form id="listTreatmentFrm" name="listTreatmentFrm" method="post" >
                        <input th:type="hidden" th:id="viewMode" th:name="viewMode" th:value="${viewMode}" />
                        <input th:type="hidden" th:id="currentDay" th:name="currentDay" th:value="${currentDay}" />
                        <input th:type="hidden" th:id="currentMonth" th:name="currentMonth" th:value="${currentMonth}" />
                        <input th:type="hidden" th:id="currentYear" th:name="currentYear" th:value="${currentYear}" />
                        <input th:type="hidden" th:id="treatmentId" th:name="treatmentId" />
                        <input th:type="hidden" th:id="treatmentIds" th:name="treatmentIds" />

                        <div class="mt-3">
                            <a id="btnAdd" href="#" class="btn btn-info">Add</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a id="btnUpdate" href="#" class="btn btn-info">Update</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a id="btnDelete" href="#" class="btn btn-info">Delete</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a id="btnInvoice" href="#" class="btn btn-info">Invoice</a>
                        </div>
                        <div class="mt-4 mb-4">
                            <div class="row">
                                <div class="col-3">
                                    <label class="form-label">From Date:</label>
                                    <input class="" type="text" id="fromDate" name="fromDate" autocomplete="off" />
                                </div>
                                <div class="col-2">
                                    <label class="form-label">To Date:</label>
                                    <input class="" type="text" id="toDate" name="toDate" autocomplete="off" />
                                </div>
                                <div class="col-1">
                                    <input type="button" class="btn btn-info" id="btnSearch" value="Search" />
                                </div>
                                <div class="col-1">

                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="mt-5 mb-5">
                                <table id="treatmentTbl" class="table table-bordered display dataTable" style="width: 100%">
                                    <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Name</th>
                                        <th>Item</th>
                                        <th>Type</th>
                                        <th>Duration</th>
                                        <th>Health Fund</th>
                                        <th>Paid Amount</th>
                                        <th>Claimed Amount</th>
                                        <th>Medical Case Record</th>
                                    </tr>
                                    </thead>
                                    <tbody></tbody>
                                    <tfoot>
                                        <tr>
                                            <th colspan="5" style="text-align:right">Total:</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>
                    </form>
                </div>
            </main>
        </div>

        <div th:insert="fragments/footer.html :: footer" xmlns:th="http://www.w3.org/1999/xhtml" />
    </div>
</div>

<div id="mcrDialog" title="Medical Case Record">
</div>

</body>

</html>