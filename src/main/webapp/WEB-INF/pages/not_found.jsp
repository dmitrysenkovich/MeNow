<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html class="error-page">
    <head>
        <title><spring:message code="error_page.oops" /> <spring:message code="error_page.404" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="/resources/css/style.css" />" rel='stylesheet' type='text/css'/>
        <script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
        <script src="<c:url value="/resources/js/main.js" />"></script>
    </head>
    <body class="error not-found">
        <div class="topFixer">
            <div id="menu">
                <div id="menuCenter">
                </div>
            </div>
        </div>
        <div class="error-text-center">
            <h2 class="error-page-text"><spring:message code="error_page.oops" /><spring:message code="error_page.not_found.message" /></h2>
            <h4 class="error-page-text"><spring:message code="error_page.not_found" /></h4>
        </div>
        <spring:message code="error.back" var="back"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input id="back-button" class="main-page-button" onclick="history.go(-1);" type="submit" value="${back}">
        <div id="localization">
            <a href="/menow/404/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/404/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
        </div>
    </body>
</html>
