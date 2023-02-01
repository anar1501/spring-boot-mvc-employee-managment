<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="css/header6.css">
    <link rel="stylesheet" href="css/header2.css">
    <link rel="stylesheet" href="css/header5.css">
    <link rel="stylesheet" href="css/header4.css">
    <link rel="stylesheet" href="css/header3.css">
</head>
<body>
<header class="main-header clearfix" role="header">


    <div class="logo">
        <c:choose>
            <c:when test="${homename eq 'Home Page'}">
                <a href="${home_url}"><em>Home</em> Page</a>
            </c:when>
            <c:when test="${emplistname eq 'Employee'}">
                <a href="${employeelist_url}"><em>Employee</em> List</a>
            </c:when>
            <c:when test="${insertname eq 'Insert'}">
                <a href="${insert_url}"><em>Insert</em> Employee</a>
            </c:when>
            <c:when test="${logname eq 'Login'}">
                <a href="/login"><em>Login</em></a>
            </c:when>
            <c:when test="${regname eq 'Register'}">
                <a href="/register"><em>Register</em></a>
            </c:when>
        </c:choose>
    </div>

    <a href="#menu" class="menu-link"><i class="fa fa-bars"></i></a>
    <nav id="menu" class="main-nav" role="navigation">
        <ul class="main-menu">
<%--            <jsp:useBean id="user" type=""  class="com.company.entity.User"/>--%>
            <c:if test="${user ne null and user.role.name ne null and user.role.name eq 'ADMIN'}">
                <li class="${selected_admin}"><a href="${admin_url}">Admin Panel</a></li>
            </c:if>
            <li class="${selected_home}"><a href="${home_url}">Home</a></li>
            <li class="${selected_employeelist}"><a href="${employeelist_url}">Employee List</a>
                <ul class="sub-menu">
                    <li class="${selected_insert}"><a href="${insert_url}">Insert Employee</a></li>
                    <li class="${selected_edit}"><a href="${edit_url}">Edit Employee</a></li>
                    <li class="${selected_delete}"><a href="${delete_url}">Delete Employee</a></li>
                    <li><a href="https://templatemo.com/about" rel="sponsored" class="external">External URL</a></li>
                </ul>
            </li>
            <c:if test="${user eq null}">
                <li class="${selected_login}"><a href="/login">Login</a></li>
                <li class="${selected_register}"><a href="/register">Register</a></li>
            </c:if>
            <c:if test="${user ne null}">
                <li><a href="${pageContext.servletContext.contextPath}/logout" class="external">Logout</a></li>
            </c:if>
        </ul>
    </nav>
</header>
</body>
