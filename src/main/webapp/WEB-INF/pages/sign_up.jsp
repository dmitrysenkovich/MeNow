<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
      <title>Me Now</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <link href="<c:url value="/resources/css/style.css" />" rel='stylesheet' type='text/css'/>
      <link href='http://fonts.googleapis.com/css?family=Open+Sans:600italic,400,300,600,700' rel='stylesheet' type='text/css'>
      <script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
      <script src="<c:url value="/resources/js/main.js" />"></script>
      <sec:csrfMetaTags/>
    </head>
    <body class="sign-up-page">
        <div class="topFixer">
            <div id="menu">
                <div id="menuCenter">
                </div>
            </div>
        </div>
        <img src="<c:url value="/resources/images/me_now_logo.png" />" class="logo">
        <div class="login-form">
            <form:form action="/menow/sign_up" method="POST" commandName="newUser">
                <div id="error-div" class="msg error sign-up-error" style="display:none">
                </div>
                <table border="0">
                    <tr>
                        <td class="label"><spring:message code="sign_up.nick" /></td>
                        <td><li><form:input type="text" path="nick" autocomplete="off"/></li></td>
                    </tr>
                    <tr>
                        <td class="label"><spring:message code="sign_up.email" /></td>
                        <td><li><form:input type="text" path="email" autocomplete="off"/></li></td>
                    </tr>
                    <tr>
                        <td class="label"><spring:message code="sign_up.phone" /></td>
                        <td><li><form:input type="text" path="phoneNumber" autocomplete="off"/></li></td>
                    </tr>
                    <tr>
                        <td class="label"><spring:message code="sign_up.login" /></td>
                        <td><li><form:input type="text" path="login" autocomplete="off"/></li></td>
                    </tr>
                    <tr>
                        <td class="label"><spring:message code="sign_up.password" /></td>
                        <td><li><form:input type="password" path="password" autocomplete="off"/></li></td>
                    </tr>
                </table>
                <div class="p-container">
                    <a href="/menow/login" class="left-button main-page-button"><spring:message code="sign_up.login.label" /></a>
                    <script>
                        $(document).ready(function(){
                            $('#sign-up-button').on('click', function (){
                                var header = $("meta[name='_csrf_header']").attr("content");
                                var token = $("meta[name='_csrf']").attr("content");
                                var data = {
                                    'nick': document.getElementById('nick').value,
                                    'email': document.getElementById('email').value,
                                    'phoneNumber': document.getElementById('phoneNumber').value,
                                    'login': document.getElementById('login').value,
                                    'password': document.getElementById('password').value,
                                };

                                $.ajax({
                                    type: 'POST',
                                    url: '/menow/sign_up',
                                    contentType: "application/json",
                                    data: JSON.stringify(data),
                                    beforeSend: function(xhr){
                                        xhr.setRequestHeader(header, token);
                                    },
                                    success: function(data){
                                        if (!data) {
                                            window.location.replace('default');
                                            return;
                                        }
                                        $('#error-div').text(data);
                                        $('#error-div').show();
                                        console.log(data);
                                    }
                                });
                            });
                        });
                    </script>
                    <button type="button" id="sign-up-button" class="right-button main-page-button"><spring:message code="sign_up.sign_up"/></button>
                </div>
            </form:form>
        </div>
        <div id="localization">
            <a href="/menow/sign_up/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/sign_up/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
        </div>
    </body>
</html>