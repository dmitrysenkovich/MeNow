<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="my-followings">
    <head>
        <title>Me Now</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="/resources/css/style.css" />" rel='stylesheet' type='text/css'/>
        <script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
        <script src="<c:url value="/resources/js/main.js" />"></script>
    </head>
    <body class="body-content profile">
    <jsp:include page="footer.jsp" />
    <div id="wrapper" class="wrapper-top">
            <c:if test="${not empty followedUsers}">
                <c:set var="followedUsersSize" value="${followedUsers.size()}"/>
                <table>
                <c:forEach var="i" begin="0" end="${followedUsersSize-1}" step="4">
                    <td>
                        <c:forEach var="j" begin="0" end="3" step="1">
                            <c:if test="${i+j < followedUsersSize}">
                                <c:set var="followedUser" value="${followedUsers.get(i+j)}"/>
                                <tr>
                                    <div id="following-profile">
                                        <div id="profileHead_floatHolder">
                                            <a href="/menow/profile/${followedUser.login}"><div id="profile-head-avatar-container" class="profile"
                                                style="background:url('<c:url value='/resources/images/users/${followedUser.login}/${followedUser.avatarImageName}' />');
                                                        background-size: cover">
                                            </div></a>
                                            <div id="following-bio">
                                                <a href="/menow/profile/${followedUser.login}" id="following-link">${followedUser.nick}</a>
                                            </div>
                                        </div>
                                    </div>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </td>
                </c:forEach>
                </table>
            </c:if>
            <c:if test="${not empty followingsNotFound}">
                <div id="followings-not-found">
                    <spring:message code="my_followings.not_found" />
                </div>
            </c:if>
        </div>
        <div id="localization">
            <a href="/menow/my_followings/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/my_followings/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
        </div>
    </body>
</html>
