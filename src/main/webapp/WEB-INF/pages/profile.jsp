<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="profile">
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
		<div id="wrapper" class="wrapper-top" >
			<div id="profile-head">
				<div id="profileHead_floatHolder">
					<script>
						$(document).ready(function(){
							$('.avatar').hide();
							$('.avatar-div').hover(
									function () {
										$('.avatar').show(100);
									},
									function () {
										$('.avatar').hide(100);
									}
							);
						});
					</script>
					<div id="profile-head-avatar-container" class="profile avatar-div"
						 style="background: url('<c:url value='/resources/images/users/${owner.login}/${owner.avatarImageName}' />'); background-size: cover">
						<c:if test="${owner.login eq viewerLogin}">
							<script>
								$(document).ready(function(){
                                    function getNameFromPath(strFilepath) {
                                        var objRE = new RegExp(/([^\/\\]+)$/);
                                        var strName = objRE.exec(strFilepath);

                                        if (strName == null) {
                                            return null;
                                        }
                                        else {
                                            return strName[0];
                                        }
                                    }

									$('#imageUploadButton').on('click', function (){
                                        var header = $("meta[name='_csrf_header']").attr("content");
                                        var token = $("meta[name='_csrf']").attr("content");
										var image = new FormData($('#imageForm')[0]);

										$.ajax({
											url: '/menow/profile/change_avatar',
											data: image,
                                            dataType: 'text',
                                            contentType: false,
                                            processData: false,
                                            cache: false,
											type: 'POST',
                                            beforeSend: function(xhr){
                                                xhr.setRequestHeader(header, token);
                                            },
											success: function(){
												var filename = getNameFromPath(document.getElementById("changeAvatar").value);
                                                var ownerLogin = '${owner.login}';
												var imageUrl = '/menow/resources/images/users/' + ownerLogin + '/' + filename;
												$('#profile-head-avatar-container').css({'background' : 'url('  + imageUrl + ')',
                                                    'background-size' : 'cover'});
											}
										});
									});
								});
							</script>
						    <form id="imageForm" action="/menow/profile/change_avatar?${_csrf.parameterName}=${_csrf.token}"
                                  method="post" enctype="multipart/form-data">
								<div class="avatar">
									<input style="display: none; visibility: hidden;"
										   name="newAvatar" id="changeAvatar" type="file" />
									<button class="image_upload submitBlue submit-button-active" type="button"
											onclick="$('#changeAvatar').trigger('click');"><spring:message code="profile.change_avatar" /></button>
									<button id="imageUploadButton" class="upload_button
									submitBlue submit-button-active profile" type="button">
                                        <spring:message code="profile.upload" /></button>
								</div>
							</form>
						</c:if>
					</div>
					<form:form id="profile-info" commandName="owner" method="post" action="/menow/profile/refresh">
						<c:choose>
							<c:when test="${owner.login eq viewerLogin}">
								<div path="nick" id="profile-name-container">
									<form:textarea path="nick" cols="30" rows="1" class="profile-textarea" />
								</div>
								<div path="status" id="profile-bio" class="profile-textarea">
                                    <spring:message code="profile.status" var="status"/>
									<form:textarea path="status" cols="30" rows="1" class="profile-textarea profile-status" placeholder="${status}" />
								</div>
							</c:when>
							<c:otherwise>
								<div path="nick" id="profile-name-container">
									<textarea disabled="disabled" cols="30" rows="1" class="profile-textarea" >${owner.nick}</textarea>
								</div>
								<div path="status" id="profile-bio">
									<textarea disabled="disabled" cols="30" rows="1" class="profile-textarea profile-status" >${owner.status}</textarea>
								</div>
							</c:otherwise>
						</c:choose>
                        <div id="profile-follow">
							<c:choose>
								<c:when test="${owner.login eq viewerLogin}">
                                    <script>
                                        $(document).ready(function(){
                                            $('#profile-refresh-button').on('click', function (){
                                                var header = $("meta[name='_csrf_header']").attr("content");
                                                var token = $("meta[name='_csrf']").attr("content");
                                                var data = {
                                                    'nick': document.getElementById('nick').value,
                                                    'status': document.getElementById('status').value
                                                };

                                                $.ajax({
                                                    type: 'POST',
                                                    url: '/menow/profile/refresh',
                                                    contentType: "application/json",
                                                    data: JSON.stringify(data),
                                                    beforeSend: function(xhr){
                                                        xhr.setRequestHeader(header, token);
                                                    },
                                                    success: function(data){
                                                        $('#nick').removeClass('errorTextarea');
                                                        $('#profile-bio').removeClass('errorTextarea');
                                                        if (data === 'nickHasError') {
                                                            $('#nick').addClass('errorTextarea');
                                                        }
                                                        if (data === 'statusHasError') {
                                                            $('#profile-bio').addClass('errorTextarea');
                                                        }
                                                    }
                                                });
                                            });
                                        });
                                    </script>
									<button type="button" id="profile-refresh-button"
										   class="submitBlue submit-button-active profile">
                                        <spring:message code="profile.refresh" />
                                    </button>
								</c:when>
								<c:otherwise>
									<input type="submit" name="submit" id="profile-refresh-button"
										   class="submitBlue submit-button-active profile"
										   style="display: none" value="Refresh">
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${owner.login eq viewerLogin}">
									<div id="profile-button">
										<div class="people-follow submit-button-active profile followers" id="profile-follow-button"><spring:message code="profile.followers" /> ${followersCount}</div>
									</div>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${ownerUserUserRelation.isFollower.value}">
											<div id="profile-button">
												<div class="people-follow"><a href="/menow/profile/unfollow" class="submitBlue submit-button-active profile profile-is-follower-button"><spring:message code="profile.unfollow" /></a></div>
											</div>
										</c:when>
										<c:otherwise>
											<div id="profile-button">
												<div class="people-follow"><a href="/menow/profile/follow" class="submitBlue submit-button-active profile profile-not-follower-button"><spring:message code="profile.follow" /></a></div>
											</div>
										</c:otherwise>
									</c:choose>
                                    <spring:message code="profile.disable" var="disable"/>
                                    <spring:message code="profile.enable" var="enable"/>
                                    <script>
                                        $(document).ready(function() {
                                            $('.people-follow').on('click', '#disable', function() {
                                                var header = $("meta[name='_csrf_header']").attr("content");
                                                var token = $("meta[name='_csrf']").attr("content");

                                                $.ajax({
                                                    type : 'POST',
                                                    url : '/menow/profile/disable',
                                                    beforeSend: function(xhr){
                                                        $('#disable')
                                                                .removeClass('profile-is-allowed-button')
                                                                .addClass('profile-not-allowed-button');
                                                        $('#disable').val('${enable}');
                                                        xhr.setRequestHeader(header, token);
                                                    },
                                                    success : function() {
                                                        $('#disable').attr('id', 'enable');
                                                    },
                                                    error : function() {
                                                        $('#disable')
                                                                .removeClass('profile-not-allowed-button')
                                                                .addClass('profile-is-allowed-button');
                                                        $('#disable').val('${enable}');
                                                    }
                                                });
                                            });

                                            $('.people-follow').on('click', '#enable', function() {
                                                var header = $("meta[name='_csrf_header']").attr("content");
                                                var token = $("meta[name='_csrf']").attr("content");

                                                $.ajax({
                                                    type : 'POST',
                                                    url : '/menow/profile/enable',
                                                    beforeSend: function(xhr){
                                                        $('#enable')
                                                                .removeClass('profile-not-allowed-button')
                                                                .addClass('profile-is-allowed-button');
                                                        $('#enable').val('${disable}');
                                                        xhr.setRequestHeader(header, token);
                                                    },
                                                    success : function() {
                                                        $('#enable').attr('id', 'disable');
                                                    },
                                                    error : function() {
                                                        $('#enable')
                                                                .removeClass('profile-is-allowed-button')
                                                                .addClass('profile-not-allowed-button');
                                                        $('#enable').val('${enable}');
                                                    }
                                                });
                                            });
                                        });
                                    </script>
                                    <div id="profile-button" class="people-follow">
                                    <c:choose>
                                            <c:when test="${userOwnerUserRelation.isAllowed.value}">
                                                <input id="disable" type="button" class="submitBlue submit-button-active profile profile-is-allowed-button" value="${disable}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input id="enable" type="button" class="submitBlue submit-button-active profile profile-not-allowed-button" value="${enable}"/>
                                            </c:otherwise>
                                    </c:choose>
                                    </div>
								</c:otherwise>
							</c:choose>
                        </div>
					</form:form>
				</div>
			</div>
			<div class="container-profile">
				<c:if test="${owner.login eq viewerLogin}">
					<div id="profile_form_container">
						<form:form class="profile" action="/menow/profile/tell" commandName="newPost" autocomplete="off" method="post" style="display:block">
                            <div style="margin:0;padding:0;display:inline"/>
							<div id="postLoaderTerritory">
                                <spring:message code="profile.menow" var="menow"/>
							    <form:textarea path="message" class="composeQuestion-form growable-post profile ${not empty postHasError ? 'errorTextarea' : ''}"
                                          id="profile-input" placeholder="${menow}"
										  style="overflow: hidden; line-height: 18px; height: 36px;"></form:textarea>
							</div>
							<div id="post_options-border">
							  <div id="post_options">
								<div id="generalLevel">
                                    <c:set var="localeCode" value="${pageContext.response.locale}" />
                                    <script>
                                        $(document).ready(function(){
                                            $('#post-button').on('click', function (){
                                                var header = $("meta[name='_csrf_header']").attr("content");
                                                var token = $("meta[name='_csrf']").attr("content");
                                                var data = {
                                                    'message': document.getElementById('profile-input').value
                                                };

                                                $.ajax({
                                                    type: 'POST',
                                                    url: '/menow/profile/tell',
                                                    contentType: "application/json",
                                                    data: JSON.stringify(data),
                                                    beforeSend: function(xhr){
                                                        xhr.setRequestHeader(header, token);
                                                    },
                                                    success: function(data){
                                                        if (data === 'postHasError') {
                                                            $('#profile-input').addClass('errorTextarea');
                                                        }
                                                        else {
                                                            $('#posts-not-found').toggle(400).show('slide', { direction: 'right' }, 1200);
                                                            $('#profile-input').removeClass('errorTextarea');
                                                            var postId = parseInt(data);
                                                            var localeCode = '${localeCode}'.substring(0, 2);
                                                            var creationDate = new Date();
                                                            creationDate = creationDate.toLocaleString(localeCode, {
                                                                year: 'numeric',
                                                                month: 'long',
                                                                day: 'numeric',
                                                                hour: 'numeric',
                                                                minute: 'numeric'
                                                            });
                                                            creationDate = creationDate.replace(/,([^,]*)$/,'$1');
                                                            var deleteScript = document.createElement("script");
                                                            deleteScript.type  = "text/javascript";
                                                            deleteScript.text  = "$(document).ready(function() {\
                                                                $(document).on('click', '#delete"+postId+"', function() {\
                                                                    var header = $(\"meta[name='_csrf_header']\").attr(\"content\");\
                                                                    var token = $(\"meta[name='_csrf']\").attr(\"content\");\
                                                                    $.ajax({\
                                                                        type : 'POST',\
                                                                        url : '/menow/profile/delete/"+postId+"',\
                                                                        beforeSend: function(xhr){\
                                                                            xhr.setRequestHeader(header, token);\
                                                                        },\
                                                                        success : function() {\
                                                                            $('#post"+postId+"').toggle(400).show('slide', { direction: 'right' }, 1200);\
                                                                        }\
                                                                    });\
                                                                });\
                                                            });"
                                                            $('#common_question_container').prepend(deleteScript);
                                                            var likePostScript = document.createElement("script");
                                                            likePostScript.type  = "text/javascript";
                                                            likePostScript.text = "$(document).ready(function() {\
                                                                $(document).on('click', '#like"+postId+"', function() {\
                                                                    var header = $(\"meta[name='_csrf_header']\").attr(\"content\");\
                                                                    var token = $(\"meta[name='_csrf']\").attr(\"content\");\
                                                                    $.ajax({\
                                                                        type : 'POST',\
                                                                        url : '/menow/profile/like/" + postId + "',\
                                                                        beforeSend : function(xhr) {\
                                                                            $('#like" + postId + "')\
                                                                            .removeClass('not-liked');\
                                                                            $('#like" + postId + "').fadeTo(10, 1);\
                                                                            var likesCount = parseInt($('#likesCount" + postId + "').text());\
                                                                            $('#likesCount" + postId + "').text(likesCount + 1);\
                                                                            xhr.setRequestHeader(header, token);\
                                                                        },\
                                                                        success : function() {\
                                                                            $('#like" + postId + "').attr('id', 'dislike" + postId + "');\
                                                                        },\
                                                                        error : function() {\
                                                                            $('#like" + postId + "')\
                                                                            .addClass('not-liked');\
                                                                            $('#like" + postId + "').fadeTo(10, 0.5);\
                                                                            var likesCount = parseInt($('#likesCount" + postId + "').text());\
                                                                            $('#likesCount" + postId + "').text(likesCount - 1);\
                                                                            xhr.setRequestHeader(header, token);\
                                                                        }\
                                                                    });\
                                                                });\
                                                            });";
                                                            $('#common_question_container').prepend(likePostScript);
                                                            var dislikePostScript = document.createElement("script");
                                                            dislikePostScript.type  = "text/javascript";
                                                            dislikePostScript.text = "$(document).ready(function() {\
                                                                $(document).on('click', '#dislike" + postId + "', function() {\
                                                                    var header = $(\"meta[name='_csrf_header']\").attr(\"content\");\
                                                                    var token = $(\"meta[name='_csrf']\").attr(\"content\");\
                                                                    $.ajax({\
                                                                        type : 'POST',\
                                                                        url : '/menow/profile/dislike/" + postId + "',\
                                                                        beforeSend : function(xhr) {\
                                                                            $('#dislike" + postId + "')\
                                                                            .addClass('not-liked');\
                                                                            $('#dislike" + postId + "').fadeTo(10, 0.5);\
                                                                            var likesCount = parseInt($('#likesCount" + postId + "').text());\
                                                                            $('#likesCount" + postId + "').text(likesCount - 1);\
                                                                        },\
                                                                        success : function() {\
                                                                            $('#dislike" + postId + "').attr('id', 'like" + postId + "');\
                                                                        },\
                                                                        error : function() {\
                                                                            $('#dislike" + postId + "')\
                                                                            .removeClass('not-liked');\
                                                                            $('#dislike" + postId + "').fadeTo(10, 1);\
                                                                            var likesCount = parseInt($('#likesCount" + postId + "').text());\
                                                                            $('#likesCount" + postId + "').text(likesCount + 1);\
                                                                        }\
                                                                    });\
                                                                });\
                                                            });";
                                                            $('#common_question_container').prepend(dislikePostScript);
                                                            var newPostDiv = "<div id='post"+postId+"' class='questionBox'>\
                                                                    <div class='question'>\
                                                                        <table>\
                                                                            <td>\
                                                                                <tr><div class='text-bold'>"+document.getElementById('profile-input').value+"</div></tr>\
                                                                                <tr><div class='date'>"+creationDate+"</div></tr>\
                                                                                <tr>\
                                                                                    <div class='delete-div'>\
                                                                                        <div id='delete"+postId+"' class='delete'></div>\
                                                                                    </div>\
                                                                                    <div id='likesCount" + postId + "' class='profile-likes-count'>0</div>\
                                                                                    <div id='profileLikes" + postId + "' class='like-div profile-like-div'>\
                                                                                        <div id='like" + postId + "' class='post-like not-liked'></div>\
                                                                                    </div>\
                                                                                </tr>\
                                                                            </td>\
                                                                        </table>\
                                                                    </div>\
                                                                </div>";
                                                            $(newPostDiv).hide().prependTo('#common_question_container').slideDown();
                                                            $('#profile-input').val('');
                                                            $('.delete').fadeTo(10, 0.5);
                                                            $('.not-liked').fadeTo(10, 0.5);
                                                        }
                                                    }
                                                });
                                            });
                                        });
                                    </script>
									<button id="post-button" class="submitBlue submit-button-active profile" name="commit" type="button">
                                        <spring:message code="profile.tell" />
                                    </button>
								</div>
							  </div>
							</div>
						</form:form>
					</div>
				</c:if>
				<c:if test="${allowed}">
					<div id="common_question_container">
                        <script>
                            $(document).ready(function(){
                                $('.delete').fadeTo(10, 0.5);
                                $(document).on("mouseenter", ".delete", function () {
                                    $('.delete').fadeTo(10, 1);
                                });
                                $(document).on("mouseleave", ".delete", function () {
                                    $('.delete').fadeTo(10, 0.5);
                                });
                            });
                        </script>
                        <script>
                            $(document).ready(function(){
                                $('.not-liked').fadeTo(10, 0.5);
                                $('.like-div').hover(
                                        function () {
                                            $('.not-liked').fadeTo(10, 1);
                                        },
                                        function () {
                                            $('.not-liked').fadeTo(10, 0.5);
                                        }
                                );
                            });
                        </script>
                        <script>
                            $(document).ready(function(){
                                $('.not-liked').fadeTo(10, 0.5);
                                $(document).on("mouseenter", ".not-liked", function () {
                                    $('.not-liked').fadeTo(10, 1);
                                });
                                $(document).on("mouseleave", ".not-liked", function () {
                                    $('.not-liked').fadeTo(10, 0.5);
                                });
                            });
                        </script>
						<c:if test="${not empty posts}">
							<c:forEach var="post" items="${posts}">
                                <script>
                                    $(document).ready(function() {
                                        $(document).on('click', '#delete${post.id}', function() {
                                            var header = $("meta[name='_csrf_header']").attr("content");
                                            var token = $("meta[name='_csrf']").attr("content");

                                            $.ajax({
                                                type : 'POST',
                                                url : '/menow/profile/delete/${post.id}',
                                                beforeSend: function(xhr){
                                                    xhr.setRequestHeader(header, token);
                                                },
                                                success : function() {
                                                    $('#post${post.id}').toggle(400).show("slide", { direction: "right" }, 1200);
                                                }
                                            });
                                        });
                                    });
                                </script>
								<div id="post${post.id}" class="questionBox">
									<div class="question">
										<table>
											<td>
												<tr><div class="text-bold">${post.message}</div></tr>
												<tr><div class="date"><fmt:formatDate value="${post.createdDate}"
                                                    dateStyle="long" timeStyle="short" type="both" /></div></tr>
                                                <tr>
													<c:if test="${owner.login eq viewerLogin}">
														<div class="delete-div">
                                                            <div id="delete${post.id}" class="delete"></div>
														</div>
													</c:if>
                                                    <c:set var="postLikes" value="${likes.get(post.id)}"/>
													<script>
														$(document).ready(function() {
                                                            var liked = ${postLikes.liked};

															$('#profileLikes${post.id}').on('click', '#like${post.id}', function() {
                                                                var header = $("meta[name='_csrf_header']").attr("content");
                                                                var token = $("meta[name='_csrf']").attr("content");

																$.ajax({
																	type : 'POST',
																	url : '/menow/profile/like/${post.id}',
                                                                    beforeSend : function(xhr) {
                                                                        if (liked) {
                                                                            xhr.abort();
                                                                            return;
                                                                        }
                                                                        liked = true;
                                                                        $('#like${post.id}')
                                                                                .removeClass('not-liked');
                                                                        $('#like${post.id}').fadeTo(10, 1);
                                                                        var likesCount = parseInt($('#likesCount${post.id}').text());
                                                                        $('#likesCount${post.id}').text(likesCount + 1);
                                                                        xhr.setRequestHeader(header, token);
                                                                    },
																	success : function() {
                                                                        $('#like${post.id}').attr('id', 'dislike${post.id}');
																	},
                                                                    error : function() {
                                                                        $('#like${post.id}')
                                                                                .addClass('not-liked');
                                                                        $('#like${post.id}').fadeTo(10, 0.5);
                                                                        var likesCount = parseInt($('#likesCount${post.id}').text());
                                                                        $('#likesCount${post.id}').text(likesCount - 1);
                                                                    }
																});
															});

															$('#profileLikes${post.id}').on('click', '#dislike${post.id}', function() {
                                                                var header = $("meta[name='_csrf_header']").attr("content");
                                                                var token = $("meta[name='_csrf']").attr("content");

																$.ajax({
																	type : 'POST',
																	url : '/menow/profile/dislike/${post.id}',
                                                                    beforeSend : function(xhr) {
                                                                        if (!liked) {
                                                                            xhr.abort();
                                                                            return;
                                                                        }
                                                                        liked = false;
                                                                        $('#dislike${post.id}')
                                                                                .addClass('not-liked');
                                                                        $('#dislike${post.id}').fadeTo(10, 0.5);
                                                                        var likesCount = parseInt($('#likesCount${post.id}').text());
                                                                        $('#likesCount${post.id}').text(likesCount - 1);
                                                                        xhr.setRequestHeader(header, token);
                                                                    },
																	success : function() {
                                                                        $('#dislike${post.id}').attr('id', 'like${post.id}');
																	},
                                                                    error : function() {
                                                                        $('#dislike${post.id}')
                                                                                .removeClass('not-liked');
                                                                        $('#dislike${post.id}').fadeTo(10, 1);
                                                                        var likesCount = parseInt($('#likesCount${post.id}').text());
                                                                        $('#likesCount${post.id}').text(likesCount + 1);
                                                                    }
																});
															});
														});
													</script>
													<div id="likesCount${post.id}" class="${owner.login eq viewerLogin ? 'profile-likes-count' : 'viewer-profile-likes-count'}">${postLikes.likesCount}</div>
													<div id="profileLikes${post.id}" class="like-div ${owner.login eq viewerLogin ? 'profile-like-div' : 'viewer-profile-like-div'}">
														<c:choose>
															<c:when test="${postLikes.liked}">
																<div id="dislike${post.id}" class="post-like"></div>
															</c:when>
															<c:otherwise>
																<div id="like${post.id}" class="post-like not-liked"></div>
															</c:otherwise>
														</c:choose>
													</div>
                                                </tr>
											</td>
										</table>
									</div>
								</div>
							</c:forEach>
						</c:if>
						<c:if test="${not empty postsNotFound}">
							<c:choose>
								<c:when test="${owner.login eq viewerLogin}">
									<div id="posts-not-found">
										<spring:message code="profile.my_posts_not_found" />
									</div>
								</c:when>
								<c:otherwise>
									<div id="posts-not-found">
										<spring:message code="profile.users_posts_not_found" />
									</div>
								</c:otherwise>
							</c:choose>
						</c:if>
					</div>
				</c:if>
				<c:if test="${not empty ownerUserUserRelation and not ownerUserUserRelation.isAllowed.value}">
					<div class="users-not-found" style="transform: translateY(10%);">
						<spring:message code="profile.disabled" />
					</div>
				</c:if>
			</div>
		</div>
        <div id="localization">
            <a href="/menow/profile/${owner.login}/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/profile/${owner.login}/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
        </div>
	</body>
</html>
