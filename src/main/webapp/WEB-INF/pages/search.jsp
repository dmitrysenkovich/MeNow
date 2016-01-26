<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="search">
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
        <div id="wrapper" class="wrapper-top">
            <form:form commandName="search" action="/menow/search" method='POST'>
                <spring:message code="search.placeholder" var="placeholder"/>
                <form:input path="part" class="search-form-input" type="text"
                            placeholder="${placeholder}" autocomplete="off"/>
                <button class="submitBlue submit-button-active profile search-form-button" type="submit"><spring:message code="search.search" /></button>
                <input type="hidden" name="${_csrf.parameterName}"
                       value="${_csrf.token}" />
            </form:form>
            <c:if test="${not empty foundUsers}">
                <c:forEach var="foundUser" items="${foundUsers}">
                    <div id="search-profile">
                        <div id="profileHead_floatHolder">
                            <a href="/menow/profile/${foundUser.login}"><div id="profile-head-avatar-container" class="profile avatar-div" id="profile-picture"
                                 style="background: url('<c:url value='/resources/images/users/${foundUser.login}/${foundUser.avatarImageName}' />'); background-size: cover">
                            </div></a>
                            <div id="search-bio" method="POST" commandName="user"
                                       action="/menow/refresh?${_csrf.parameterName}=${_csrf.token}">
                                <div id="profile-name-container">
                                    <a href="/menow/profile/${foundUser.login}"><textarea disabled="disabled" cols="30" rows="1" class="profile-textarea" >${foundUser.nick}</textarea></a>
                                </div>
                                <div id="profile-bio">
                                    <textarea disabled="disabled" cols="30" rows="1" class="profile-textarea profile-status" >${foundUser.status}</textarea>
                                </div>
                                <spring:message code="search.unfollow" var="unfollow"/>
                                <spring:message code="search.follow" var="follow"/>
                                <script>
                                    $(document).ready(function() {

                                        $('.follow').on('click', '#unfollow${foundUser.login}', function() {
                                            var header = $("meta[name='_csrf_header']").attr("content");
                                            var token = $("meta[name='_csrf']").attr("content");

                                            $.ajax({
                                                type : 'POST',
                                                url : '/menow/search/unfollow/${foundUser.login}',
                                                beforeSend : function(xhr) {
                                                    $('#unfollow${foundUser.login}')
                                                            .removeClass('search-is-follower-button')
                                                            .addClass('search-not-follower-button');
                                                    $('#unfollow${foundUser.login}').val('${follow}');
                                                    xhr.setRequestHeader(header, token);
                                                },
                                                success : function() {
                                                    $('#unfollow${foundUser.login}').attr('id', 'follow${foundUser.login}');
                                                },
                                                error : function() {
                                                    $('#unfollow${foundUser.login}')
                                                            .removeClass('search-not-follower-button')
                                                            .addClass('search-is-follower-button');
                                                    $('#unfollow${foundUser.login}').val('${unfollow}');
                                                }
                                            });
                                        });

                                        $('.follow').on('click', '#follow${foundUser.login}', function() {
                                            var header = $("meta[name='_csrf_header']").attr("content");
                                            var token = $("meta[name='_csrf']").attr("content");

                                            $.ajax({
                                                type : 'POST',
                                                url : '/menow/search/follow/${foundUser.login}',
                                                beforeSend : function(xhr) {
                                                    $('#follow${foundUser.login}')
                                                            .removeClass('search-not-follower-button')
                                                            .addClass('search-is-follower-button');
                                                    $('#follow${foundUser.login}').val('${unfollow}');
                                                    xhr.setRequestHeader(header, token);
                                                },
                                                success : function() {
                                                    $('#follow${foundUser.login}').attr('id', 'unfollow${foundUser.login}');
                                                },
                                                error : function () {
                                                    $('#follow${foundUser.login}')
                                                            .removeClass('search-is-follower-button')
                                                            .addClass('search-not-follower-button');
                                                    $('#follow${foundUser.login}').val('${follow}');
                                                }
                                            });
                                        });
                                    });
                                </script>
                                <div id="profile-follow" class="follow">
                                    <input type="submit" name="submit" id="profile-refresh-button"
                                           class="submitBlue submit-button-active profile"
                                           style="display: none" value="Refresh">
                                    <div id="profile-button">
                                        <c:choose>
                                            <c:when test="${foundUsersRelations.get(foundUser.login).getIsFollower().getValue()}">
                                                <input id="unfollow${foundUser.login}" type="submit"  class="submitBlue submit-button-active profile search-is-follower-button" value="${unfollow}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input id="follow${foundUser.login}" type="submit"  class="submitBlue submit-button-active profile search-not-follower-button" value="${follow}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${not empty usersNotFound}">
                <div class="users-not-found">
                    <spring:message code="search.not_found" />
                </div>
            </c:if>
        </div>
        <div id="localization">
            <a href="/menow/search/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/search/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
        </div>
    </body>
</html>
