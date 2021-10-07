<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/home.js}"></script>
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
                    <form id="listTreatmentFrm" name="listTreatmentFrm" method="post">
                        <input th:type="hidden" th:id="viewMode" th:name="viewMode" th:value="${viewMode}" />
                        <input th:type="hidden" th:id="currentDay" th:name="currentDay" th:value="${currentDay}" />
                        <input th:type="hidden" th:id="currentMonth" th:name="currentMonth" th:value="${currentMonth}" />
                        <input th:type="hidden" th:id="currentYear" th:name="currentYear" th:value="${currentYear}" />
                        <div class="container">
                            <div class="row">
                                <div class="col-sm">
                                </div>
                                <div class="col-sm">
                                    <nav>
                                        <ul class="pagination">
                                            <li>
                                                <a th:id="prevView" class="calendar-nav-link" href="#" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>
                                            <li><a th:id="dayView" th:classappend="${viewMode == 'day'} ? calendar-nav-item-selected : calendar-nav-item" href="#">Day</a></li>
                                            <li><a th:id="monthView" th:classappend="${viewMode == 'month'} ? calendar-nav-item-selected : calendar-nav-item" href="#">Month</a></li>
                                            <li><a th:id="yearView" th:classappend="${viewMode == 'year'} ? calendar-nav-item-selected : calendar-nav-item" href="#">Year</a></li>
                                            <li>
                                                <a th:id="nextView" class="calendar-nav-link" href="#" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                            <li th:id="dmy" class="calendar-nav-item" th:text="${currentDay}"></li>
                                        </ul>
                                    </nav>
                                </div>
                                <div class="col-sm">
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
                                        <th>Duration (min)</th>
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