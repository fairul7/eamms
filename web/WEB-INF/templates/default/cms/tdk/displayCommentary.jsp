<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}"/>
<c:set var="commentary" value="${co}" scope="request"/>
<c:set var="commentList" value="${co.publishedCommentList}" scope="request"/>

<%-- Display Edit Mode Options --%>
<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />

<p>
<span class="contentName">
    <c:out value="${co.name}" escapeXml="false" />
</span>
</p>

<p>
<span class="contentSubheader">
<c:if test="${co.averageScore > 0}">
    <fmt:message key='comment.label.averageRating'/> [<b><c:forEach begin="0" end="${co.averageScore - 1}">*</c:forEach></b>]
</c:if>
</span>
</p>

<p>
<span class="contentSummary">
    <c:out value="${co.summary}" escapeXml="false" />
</span>
</p>

<p>
<c:if test="${!widget.restricted}">
    <a class="contentOptionLink" href="comment.jsp?id=<c:out value='${co.id}'/>">Post your comment</a>
</c:if>

<p>
<jsp:include page="displayCommentaryRecursion.jsp" flush="true" />
</p>
