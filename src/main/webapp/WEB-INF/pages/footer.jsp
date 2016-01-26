<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<div class="topFixer">
  <div id="menu">
    <div id="menuCenter">
      <c:url value="/j_spring_security_logout" var="logoutUrl" />
      <form class="profile" action="${logoutUrl}" method="post">
        <div>
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <input class="link-logout profile logout-profile-button" type="submit" value="">
        </div>
      </form>
      <a href="/menow/settings" class="link-menu profile"><spring:message code="top.settings" /></a>
      <a href="/menow/profile/${pageContext.request.userPrincipal.name}" class="link-menu profile"><spring:message code="top.profile" /></a>
      <a href="/menow/search" class="link-menu profile"><spring:message code="top.search" /></a>
      <a href="/menow/my_followings" class="link-menu profile"><spring:message code="top.my_followings" /></a>
      <a href="/menow/feed" class="link-menu profile"><spring:message code="top.feed" /></a>
    </div>
  </div>
</div>

