<%@ page import="com.tms.cms.core.model.ContentObject"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<p>
<x:template
    name="displayContentChildren"
    type="com.tms.cms.tdk.DisplayContentChildren"
    properties="id=${co.id}&types=com.tms.cms.comment.Commentary&page=1"
    body="custom">

<c:set var="children" value="${displayContentChildren.children}"/>

<c:forEach var="child" items="${children}">
    <c:if test="${child.className == 'com.tms.cms.comment.Commentary'}">

    <x:template
        name="displayCommentary"
        type="com.tms.cms.tdk.DisplayContentObject"
        properties="id=${child.id}"
        body="custom">

        <c:set var="commentary" value="${displayCommentary.contentObject}" scope="request"/>
        <c:set var="commentList" value="${commentary.publishedCommentList}" scope="request"/>

        <p>&nbsp;</p>

        <p>
        <span class="contentName">
            <c:out value="${commentary.name}" escapeXml="false" />
        </span>
        </p>

        <p>
        <span class="contentSubheader">
        <c:if test="${commentary.averageScore > 0}">
            <fmt:message key='comment.label.averageRating'/> [<b><c:forEach begin="0" end="${commentary.averageScore - 1}">*</c:forEach></b>]
        </c:if>
        </span>
        </p>

        <p>
        <span class="contentSummary">
            <c:out value="${commentary.summary}" escapeXml="false" />
        </span>
        </p>

        <div class="contentOptionDiv">
        <c:if test="${!displayCommentary.restricted}">
            <a class="contentOptionLink" href="comment.jsp?id=<c:out value='${commentary.id}'/>"><fmt:message key='comment.label.postYourComment'/></a>
        </c:if>
        </div>

        <br>
        <jsp:include page="displayCommentaryRecursion.jsp" flush="true" />

    </x:template>

    </c:if>
</c:forEach>


</x:template>
