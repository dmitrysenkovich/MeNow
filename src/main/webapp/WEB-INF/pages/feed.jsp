<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="feed">
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
            <c:if test="${not empty feed}">
                <script>
                    $(document).ready(function(){
                        $('.not-liked').fadeTo(10, 0.5);
                        $('.feed-like-div').hover(
                                function () {
                                    $('.not-liked').fadeTo(10, 1);
                                },
                                function () {
                                    $('.not-liked').fadeTo(10, 0.5);
                                }
                        );
                    });
                </script>
                <c:forEach var="i" begin="0" end="${feed.size()-1}" step="1">
                    <c:set var="postIndex" value="${feed.size()-1-i}"/>
                    <c:set var="post" value="${feed.get(postIndex)}"/>
                    <div id="feed-profile">
                        <div id="profileHead_floatHolder">
                            <a href="/menow/profile/${post.login}"><div id="feed-head-avatar-container"
                                style="background: url('<c:url value='/resources/images/users/${post.login}/${followed.get(post.login).avatarImageName}' />'); background-size: cover">
                            </div></a>
                            <div id="feed-bio">
                                <div id="feed-message">
                                    ${post.message}
                                </div>
                                <div id="feed-date">
                                    <a href="/menow/profile/${post.login}" id="feed-nick">${followed.get(post.login).nick}</a> <spring:message code="feed.told" /> <fmt:formatDate value="${post.createdDate}" dateStyle="long" timeStyle="short" type="both" />
                                </div>
                                <c:set var="postLikes" value="${likes.get(post.id)}"/>
                                <script>
                                    $(document).ready(function() {
                                         var liked = ${postLikes.liked};

                                         $('.feed-like-div').on('click', '#like${post.id}', function() {
                                             var header = $("meta[name='_csrf_header']").attr("content");
                                             var token = $("meta[name='_csrf']").attr("content");

                                            $.ajax({
                                                type : 'POST',
                                                url : '/menow/feed/like/${post.id}',
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

                                         $('.feed-like-div').on('click', '#dislike${post.id}', function() {
                                             var header = $("meta[name='_csrf_header']").attr("content");
                                             var token = $("meta[name='_csrf']").attr("content");

                                            $.ajax({
                                                type : 'POST',
                                                url : '/menow/feed/dislike/${post.id}',
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
                                                            .addClass('not-liked');
                                                    $('#dislike${post.id}').fadeTo(10, 1);
                                                    var likesCount = parseInt($('#likesCount${post.id}').text());
                                                    $('#likesCount${post.id}').text(likesCount + 1);
                                                }
                                            });
                                        });
                                    });
                                </script>
                                <div id="likesCount${post.id}" class="feed-likes-count">${postLikes.likesCount}</div>
                                <div class="feed-like-div">
                                    <c:choose>
                                        <c:when test="${postLikes.liked}">
                                            <div id="dislike${post.id}" class="post-like"></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div id="like${post.id}" class="post-like not-liked"></div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${not empty noFeed}">
                <div class="feed-not-found">
                    <spring:message code="feed.no_feed" />
                </div>
            </c:if>
        </div>
        <div id="localization">
            <a href="/menow/feed/?language=en" class="submitBlue submit-button-active profile language"><spring:message code="english" /></a><a href="/menow/feed/?language=ru_RU" class="submitBlue submit-button-active profile language"><spring:message code="russian" /></a>
        </div>
    </body>
</html>
