<%@ include file="/common/header.jsp" %>

<c:set var="tempCommentList" value="${commentList}"/>
<c:forEach var="comment" items="${tempCommentList}">
    <span class="contentChildName"><c:out value="${comment.name}"/>
    <c:if test="${comment.score > 0}">
        [<b><c:forEach begin="0" end="${comment.score - 1}">*</c:forEach></b>]
    </c:if>
    </span>
    <span class="contentOptionLink">
        [<a href="comment.jsp?<c:out value='id=${commentary.id}&commentId=${comment.id}'/>"><fmt:message key='forum.label.reply'/></a>]
    </span>
    <br>
    <span class="contentChildDate"><fmt:formatDate pattern="${globalDateLong}" value="${comment.date}"/></span>
    <br>
    <span class="contentChildAuthor"><fmt:message key="comment.label.author"/>
        <c:out value="${comment.author}"/>
        (<c:out value="${comment.propertyMap.user.propertyMap.lastName}"/> <c:out value="${comment.propertyMap.user.propertyMap.firstName}"/>)
    </span>
    <p>
    <span class="contentChildSummary">
    <c:out value="${comment.summary}" escapeXml="false" />
    </span>
    </p>
    <blockquote>
        <c:set var="commentList" scope="request" value="${comment.children}"/>
        <jsp:include page="displayCommentaryRecursion.jsp" flush="true" />
    </blockquote>
</c:forEach>
