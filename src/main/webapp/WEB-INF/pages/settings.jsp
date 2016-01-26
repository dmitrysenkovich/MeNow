<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="settings">
    <head>
        <title>Me Now</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="/resources/css/style.css" />" rel='stylesheet' type='text/css'/>
        <script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
        <script src="<c:url value="/resources/js/main.js" />"></script>
        <sec:csrfMetaTags/>
    </head>
    <body class="body-content profile">
        <jsp:include page="footer.jsp" />
        <form:form id="settings" method="post" action="/menow/settings/save" commandName="userSettingsDto">
            <div class="settings-prompt"><spring:message code="settings.who_can"/></div>
            <div id="radio-buttons">
                <p>
                    <form:radiobutton path="permissionType" id="r1" class="settings radio-button" value="0" />
                    <label for="r1" class="settings"><span class="settings"></span><spring:message code="settings.permit_all"/></label>
                </p>
                <p>
                    <form:radiobutton path="permissionType" id="r2" class="settings radio-button" value="1" />
                    <label for="r2" class="settings"><span class="settings"></span><spring:message code="settings.followers_only"/></label>
                </p>
                <p>
                    <form:radiobutton path="permissionType" id="r3" class="settings radio-button" value="2" />
                    <label for="r3" class="settings"><span class="settings"></span><spring:message code="settings.me_only"/></label>
                </p>
            </div>
            <div class="settings-prompt phone"><spring:message code="settings.phone"/></div>
            <li class="phone"><form:input type="text" path="phoneNumber" id="phone-text" autocomplete="off"/></li>
            <c:choose>
                <c:when test="${userSettingsDto.notification eq 'on'}">
                    <input name="notification" type="checkbox" id="notification" value="${notification}" checked="checked">
                </c:when>
                <c:otherwise>
                    <input name="notification" type="checkbox" id="notification">
                </c:otherwise>
            </c:choose>
            <label for="notification" class="checkbox-label"><span class="settings"></span><spring:message code="settings.notify"/></label>
            <script>
                $(document).ready(function(){
                    $('#save-settings-button').on('click', function (){
                        var header = $("meta[name='_csrf_header']").attr("content");
                        var token = $("meta[name='_csrf']").attr("content");
                        var data = {
                                'permissionType': parseInt(document.querySelector('input[name="permissionType"]:checked').value),
                                'phoneNumber': document.getElementById('phone-text').value,
                                'notification': document.getElementById('notification').checked ? 'on' : null
                        };

                        $.ajax({
                            type: 'POST',
                            url: '/menow/settings/save',
                            contentType: "application/json",
                            data: JSON.stringify(data),
                            beforeSend: function(xhr){
                                xhr.setRequestHeader(header, token);
                            },
                            success: function(data){
                                if (data)
                                    $('#phone-text').addClass('errorTextarea');
                                else
                                    $('#phone-text').removeClass('errorTextarea');
                            }
                        });
                    });
                });
            </script>
            <button id="save-settings-button" class="submitBlue submit-button-active profile settings" type="button">
                <spring:message code="settings.save" />
            </button>
        </form:form>
        <div id="settings-localization">
            <a href="/menow/settings/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/settings/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
        </div>
    </body>
</html>
