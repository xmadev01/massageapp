<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/addCustomer.js}"></script>
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
                        <h4><strong>Add Customer</strong></h4>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
                        <div class="d-table-cell align-middle">

                            <div class="card">
                                <div class="card-body">
                                    <div class="m-sm-4">
                                        <form id="addCustomerFrm" name="addCustomerFrm" method="post" th:action="@{/createCustomer}" th:object="${customer}">
                                            <input th:type="hidden" th:field="*{birthday}" />
                                            <div class="mb-3">
                                                <label class="form-label">First Name</label>
                                                <input class="form-control" type="text" th:field="*{firstName}" name="firstName" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('firstName')}" th:errors="*{firstName}"></div>
                                            </div>
                                            <div class="mb-3">a
                                                <label class="form-label">Middle Name</label>
                                                <input class="form-control" type="text" th:field="*{middleName}" name="middleName" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Last Name</label>
                                                <input class="form-control" type="text" th:field="*{lastName}" name="lastName" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('lastName')}" th:errors="*{lastName}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label label-lg">Birthday</label>
                                                <select th:id="days">
                                                    <option th:each="day : ${days}"
                                                            th:value="${day}" th:text="${day}"></option>
                                                </select><span class="ml-1">/</span>
                                                <select th:id="months">
                                                    <option th:each="month : ${months}"
                                                            th:value="${month}" th:text="${month}"></option>
                                                </select><span class="ml-1">/</span>
                                                <select th:id="years">
                                                    <option th:each="year : ${years}"
                                                            th:value="${year}" th:text="${year}"></option>
                                                </select>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Email</label>
                                                <input class="form-control" type="email" th:field="*{email}" name="email" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Phone1</label>
                                                <input class="form-control" type="text" th:field="*{phone1}" name="phone1" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('phone1')}" th:errors="*{phone1}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Phone2</label>
                                                <input class="form-control" type="text" th:field="*{phone2}" name="phone2" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('phone2')}" th:errors="*{phone2}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Address</label>
                                                <input class="form-control" type="text" th:field="*{address}" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Postcode</label>
                                                <input class="form-control" type="text" th:field="*{postcode}" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Health Fund</label>
                                                <input class="form-control" type="text" th:field="*{healthFund}" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('healthFund')}" th:errors="*{healthFund}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Membership Number</label>
                                                <input class="form-control" type="text" th:field="*{membershipNum}" name="membershipNum" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('membershipNum')}" th:errors="*{membershipNum}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Rebate Rate (%)</label>
                                                <input class="form-control" type="text" th:field="*{rebateRate}" name="rebateRate" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('rebateRate')}" th:errors="*{rebateRate}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Current Medication</label>
                                                <textarea class="form-control" type="text" rows="5" th:field="*{medication}" />
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