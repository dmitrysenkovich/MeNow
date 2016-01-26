<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
		<title>Me Now</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href='http://fonts.googleapis.com/css?family=Open+Sans:600italic,400,300,600,700' rel='stylesheet' type='text/css'>
	</head>
	<body class="login-page">
		<div class="topFixer">
			<div id="menu">
				<div id="menuCenter">
				</div>
			</div>
		</div>
		<img src="<c:url value="/resources/images/me_now_logo.png" />" class="logo">
		<div class="login-form">
            <c:if test="${not empty error}">
                <div class="msg error"><spring:message code="login.error" /></div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="msg info"><spring:message code="login.message" /></div>
            </c:if>
			<form name="loginForm" action="<c:url value='/j_spring_security_check' />" method='POST'>
				<li>
					<input type="text" name="login" class="text" autocomplete="off"><div class="icon user"></div>
				</li>
				<li>
					<input type="password" name="password"><div class="icon lock"></div>
				</li>
				<div class="p-container">
                    <a href="/menow/sign_up" class="left-button main-page-button"><spring:message code="login.sign_up" /></a>
					<spring:message code="login.login" var="login"/>
                    <input type="submit" class="right-button main-page-button" value="${login}">
				</div>
                <input type="hidden" name="${_csrf.parameterName}"
                       value="${_csrf.token}" />
			</form>
		</div>
        <div id="localization">
		<a href="/menow/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
	    </div>
    </body>
</html>