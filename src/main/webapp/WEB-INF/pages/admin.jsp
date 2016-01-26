<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="admin">
	<head>
		<title>Me Now</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />">
		<script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
		<script src="<c:url value="/resources/js/main.js" />"></script>
		<sec:csrfMetaTags/>
	</head>
	<body class="body-content profile">
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
				</div>
			</div>
		</div>
		<div id="wrapper" class="wrapper-top">
			<form:form commandName="search" action="/menow/admin" method='POST'>
				<spring:message code="admin.placeholder" var="placeholder"/>
				<form:input path="part" class="search-form-input" type="text"
							placeholder="${placeholder}" autocomplete="off"/>
				<button class="submitBlue submit-button-active profile search-form-button" type="submit"><spring:message code="search.search" /></button>
				<input type="hidden" name="${_csrf.parameterName}"
					   value="${_csrf.token}" />
			</form:form>
			<c:if test="${not empty foundUsers}">
				<c:forEach var="foundUser" items="${foundUsers}">
                    <script>
                        $(document).ready(function() {
							var header = $("meta[name='_csrf_header']").attr("content");
							var token = $("meta[name='_csrf']").attr("content");

                            $('.delete-user').on('click', '#delete${foundUser.value.login}', function() {
                                $.ajax({
                                    type : 'POST',
                                    url : '/menow/admin/delete/${foundUser.value.login}',
									beforeSend: function(xhr){
										xhr.setRequestHeader(header, token);
									},
                                    success : function() {
                                        $('#user${foundUser.value.login}').toggle(400).show("slide", { direction: "right" }, 1200);
                                    }
                                });
                            });
                        });
                    </script>
					<div id="user${foundUser.value.login}" class="user-admin">
					<div id="search-profile">
						<div id="profileHead_floatHolder">
							<div id="profile-head-avatar-container" class="profile avatar-div" id="profile-picture"
								style="background: url('<c:url value='/resources/images/users/${foundUser.value.login}/${foundUser.value.avatarImageName}' />'); background-size: cover">
							</div>
							<div id="search-bio" method="POST" commandName="foundUser"
								 action="/menow/refresh?${_csrf.parameterName}=${_csrf.token}">
								<div id="profile-name-container">
									<textarea disabled="disabled" cols="30" rows="1" class="profile-textarea" >${foundUser.value.login}</textarea>
								</div>
								<div id="profile-bio">
									<textarea disabled="disabled" cols="30" rows="1" class="profile-textarea profile-status" >${foundUser.value.email}</textarea>
								</div>
								<div id="profile-follow">
									<div id="profile-button" class="delete-user">
										<spring:message code="admin.delete" var="delete"/>
										<input id="delete${foundUser.value.login}" type="submit"  class="submitBlue submit-button-active profile profile-is-allowed-button" value="${delete}"/>
									</div>
									<spring:message code="admin.ban" var="ban"/>
									<spring:message code="admin.unban" var="unban"/>
									<script>
										$(document).ready(function() {
											$('.banned').on('click', '#ban${foundUser.value.login}', function() {
												var header = $("meta[name='_csrf_header']").attr("content");
												var token = $("meta[name='_csrf']").attr("content");

												$.ajax({
													type : 'POST',
													url : '/menow/admin/ban/${foundUser.value.login}',
													beforeSend : function(xhr) {
														$('#ban${foundUser.value.login}')
																.removeClass('profile-is-allowed-button')
																.addClass('profile-not-allowed-button');
														$('#ban${foundUser.value.login}').val('${unban}');
														xhr.setRequestHeader(header, token);
													},
													success : function() {
														$('#ban${foundUser.value.login}').attr('id', 'unban${foundUser.value.login}');
													},
													error : function() {
														$('#ban${foundUser.value.login}')
																.removeClass('profile-not-allowed-button')
																.addClass('profile-is-allowed-button');
														$('#ban${foundUser.value.login}').val('${ban}');
													}
												});
											});

											$('.banned').on('click', '#unban${foundUser.value.login}', function() {
												var header = $("meta[name='_csrf_header']").attr("content");
												var token = $("meta[name='_csrf']").attr("content");

												$.ajax({
													type : 'POST',
													url : '/menow/admin/unban/${foundUser.value.login}',
													beforeSend : function(xhr) {
														$('#unban${foundUser.value.login}')
																.removeClass('profile-not-allowed-button')
																.addClass('profile-is-allowed-button');
														$('#unban${foundUser.value.login}').val('${ban}');
														xhr.setRequestHeader(header, token);
													},
													success : function() {
														$('#unban${foundUser.value.login}').attr('id', 'ban${foundUser.value.login}');
													},
													error : function() {
														$('#unban${foundUser.value.login}')
															.removeClass('profile-is-allowed-button')
															.addClass('profile-not-allowed-button');
														$('#unban${foundUser.value.login}').val('${unban}');
													}
												});
											});
										});
									</script>
									<div id="profile-button" class="banned">
										<c:choose>
											<c:when test="${foundUser.value.access.value}">
												<input id="ban${foundUser.value.login}" type="submit"  class="submitBlue submit-button-active profile profile-is-allowed-button" value="${ban}"/>
											</c:when>
											<c:otherwise>
												<input id="unban${foundUser.value.login}" type="submit"  class="submitBlue submit-button-active profile profile-not-allowed-button" value="${unban}"/>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
					</div>
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${not empty usersNotFound}">
				<div class="users-not-found">
					<spring:message code="admin.not_found" />
				</div>
			</c:if>
		</div>
		<div id="localization">
			<a href="/menow/admin/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/admin/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
		</div>
	</body>
</html>
