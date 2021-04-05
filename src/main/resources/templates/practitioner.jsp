<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/practitioner.js}"></script>
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
                            <h3><strong>Practitioners</strong></h3>
                        </div>
                    </div>
                    <div th:insert="fragments/messagediv.html :: messagediv" />
                    <form id="listPractitionerFrm" name="listPractitionerFrm" method="post">
                    <div class="mt-3">
                        <a id="btnAdd" href="#" class="btn btn-info">Add</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="btnUpdate" href="#" class="btn btn-info">Update</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="btnDeactivate" href="#" class="btn btn-info">(De)activate</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="btnDelete" href="#" class="btn btn-info">Delete</a>
                    </div>
                    <div class="row">
                        <div class="mt-5 mb-5">
                            <table id="practitionerTbl" class="table table-bordered table-responsive display dataTable" style="width: 100%">
                                <thead>
                                <tr>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Active</th>
                                </tr>
                                </thead>
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