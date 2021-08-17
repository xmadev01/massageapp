<!DOCTYPE html>
<html lang="en">

<head th:insert="fragments/head.html :: head" />
<head>
    <script type="text/javascript" th:src="@{/js/login.js}"></script>
</head>

<body>
<main class="d-flex w-100 login-bg">
    <div class="container d-flex flex-column">
        <div class="row vh-100">
            <div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
                <div class="d-table-cell align-middle">

                    <div class="text-center mt-4">
                        <p class="lead">
                            <div class="alert alert-danger" role="alert" th:if="${param.error}">
                                Invalid username and password.
                            </div>
                             <div class="alert alert-warning" role="alert" th:if="${param.logout}">
                                You have been logged out.
                            </div>
                        </p>
                    </div>

                    <div class="card">
                        <div class="card-body">
                            <div class="m-sm-4">
                                <div class="text-center">
                                    <img src="/images/logo.png" alt="Charles Hall"  />
                                </div>
                                <form id="loginFrm" name="loginFrm" method="post" th:action="@{/login}">
                                    <div class="mb-3">
                                        <label class="form-label">User Name</label>
                                        <input class="form-control form-control-lg" type="text" name="username" placeholder="Enter your user name" />
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Password</label>
                                        <input class="form-control form-control-lg" type="password" name="password" placeholder="Enter your password" />
                                    </div>
                                    <div>
                                        <label class="form-check">
                                            <input class="form-check-input" type="checkbox" value="remember-me" name="remember-me" checked>
                                            <span class="form-check-label">
                                                    Remember me next time
                                            </span>
                                        </label>
                                    </div>
                                    <div class="text-center mt-3">
                                        <button type="submit" id="btnSignIn" class="btn btn-lg btn-info">Sign in</button>
                                        <!-- <button type="submit" class="btn btn-lg btn-primary">Sign in</button> -->
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

<script src="js/app.js"></script>

</body>

</html>