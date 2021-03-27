<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/updateCustomer.js}"></script>
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
                        <h3><strong>Massage Admin</strong> Dashboard</h3>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
                        <div class="d-table-cell align-middle">

                            <div class="text-center mt-4">
                                <h1 class="h2">Update Customer Profile</h1>
                            </div>
                            <div class="errorMsg">
                                <ul>
                                    <li th:each="err : ${#fields.errors('all')}" th:text="${err}" />
                                </ul>
                            </div>

                            <div class="card">
                                <div class="card-body">
                                    <div class="m-sm-4">
                                        <form id="updateCustomerFrm" name="updateCustomerFrm" method="post" th:action="@{/updateCustomer}" th:object="${customer}">
                                            <input th:type="hidden" th:field="*{id}" />
                                            <div class="mb-3">
                                                <label class="form-label">First Name</label>
                                                <input class="form-control form-control-lg" type="text" th:field="*{firstName}" name="firstName" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('firstName')}" th:errors="*{firstName}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Middle Name</label>
                                                <input class="form-control form-control-lg" type="text" name="middleName" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Last Name</label>
                                                <input class="form-control form-control-lg" type="text" th:field="*{lastName}" name="lastName" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('lastName')}" th:errors="*{lastName}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Email</label>
                                                <input class="form-control form-control-lg" type="email" th:field="*{email}" name="email" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Phone</label>
                                                <input class="form-control form-control-lg" type="text" th:field="*{phone}" name="phone" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('phone')}" th:errors="*{phone}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Mobile</label>
                                                <input class="form-control form-control-lg" type="text" th:field="*{mobile}" name="mobile" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('mobile')}" th:errors="*{mobile}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Address</label>
                                                <input class="form-control form-control-lg" type="text" name="address" />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Health Fund</label>
                                                <select class="form-control form-control-lg" th:field="*{healthFund}">
                                                    <option value="">Please select...</option>
                                                    <option th:each="hf : ${T(com.xms.app.massage.enums.HealthFundEnum).values()}"
                                                            th:value="${hf}" th:text="${hf.displayName}"></option>
                                                </select>
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('healthFund')}" th:errors="*{healthFund}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Membership Number</label>
                                                <input class="form-control form-control-lg" type="text" th:field="*{membershipNum}" name="membershipNum" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('membershipNum')}" th:errors="*{membershipNum}"></div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Rebate Rate (%)</label>
                                                <input class="form-control form-control-lg" type="text" th:field="*{rebateRate}" name="rebateRate" />
                                                <div class="alert alert-warning" th:if="${#fields.hasErrors('rebateRate')}" th:errors="*{rebateRate}"></div>
                                            </div>
                                            <div class="text-center mt-3">
                                                <a id="btnUpdate" href="#" class="btn btn-lg btn-info">Update</a>
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                <a id="btnCancel" href="#" class="btn btn-lg btn-info">Cancel</a>
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