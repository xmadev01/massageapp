<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/items.js}"></script>
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
                        <h4><strong>Service Items & Pricing</strong></h4>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
                        <div class="d-table-cell align-middle">

                            <div th:insert="fragments/messagediv.html :: messagediv" />

                            <div class="card">
                                <div class="card-body">
                                    <div class="m-sm-4">
                                        <form id="itemsFrm" name="itemsFrm" method="post" th:action="@{/saveItems}" th:object="${itemsForm}">
                                            <table id="customerTbl" class="table table-bordered display" style="width: 100%">
                                                <tbody>
                                                    <tr class="mb-3" style="width:100%" th:each="item, itemStat : *{items}">
                                                        <td width="40%" th:text="*{items[__${itemStat.index}__].name}"></td>
                                                        <input th:type="hidden" th:field="*{items[__${itemStat.index}__].id}" />
                                                        <td width="30%" ><input type="text" th:field="*{items[__${itemStat.index}__].duration}" size="3" /><strong>&nbsp;min</strong></td>
                                                        <td width="30%" ><strong>$</strong><input type="text" th:field="*{items[__${itemStat.index}__].price}" size="5" /></td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                            <div class="text-center mt-3">
                                                <a id="btnSave" href="#" class="btn btn-info">Save</a>
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