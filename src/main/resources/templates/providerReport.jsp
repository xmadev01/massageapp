<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/providerreport.js}"></script>
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
                            <h3><strong>Provider Report</strong></h3>
                        </div>
                    </div>
                    <div th:insert="fragments/messagediv.html :: messagediv" />
                    <form id="providerRptFrm" name="providerRptFrm" method="post">
                        <div class="container">
                            <div class="row">
                                <div class="col-sm">
                                    <label class="form-label">From Date:</label>
                                    <input class="" type="text" id="fromDate" name="fromDate" autocomplete="off" />
                                </div>
                                <div class="col-sm">
                                    <label class="form-label">To Date:</label>
                                    <input class="" type="text" id="toDate" name="toDate" autocomplete="off" />
                                </div>
                                <div class="col-sm">
                                    <input type="button" class="btn btn-info" id="btnSearch" value="Search" />
                                </div>
                                <div class="col-sm">

                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="mt-5 mb-5">
                                <table id="providerRptTbl" class="table table-bordered display dataTable" style="width: 100%; ">
                                    <thead>
                                    <tr>
                                        <th rowspan="2">Date</th>
                                        <th rowspan="2">Practitioner</th>
                                        <th rowspan="2">Health Fund</th>
                                        <th colspan="3">Acupuncture</th>
                                        <th colspan="3">Massage</th>
                                    </tr>
                                    <tr>
                                        <th>Number of Client</th>
                                        <th>Total Charged Amount</th>
                                        <th>Total Claimed Amount</th>
                                        <th>Number of Client</th>
                                        <th>Total Charged Amount</th>
                                        <th>Total Claimed Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody></tbody>
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


</body>

</html>