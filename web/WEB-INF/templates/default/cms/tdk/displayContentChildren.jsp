<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="children" value="${widget.children}"/>

<c:forEach var="co" items="${children}">
        <a href="content.jsp?id=<c:out value="${co.id}"/>" class="contentChildName" <c:if test="${co.className=='com.tms.cms.bookmark.Bookmark'}"> target="<c:out value='${co.contentObject.target}'/>"</c:if>><c:out value="${co.name}"/></a>
        <br>
        <span class="contentChildDate"><fmt:formatDate pattern="${globalDateLong}" value="${co.date}"/></span>
        <br>
        <span class="contentChildAuthor"><c:out value="${co.author}"/></span>
        <p>
        <span class="contentChildSummary">
        <c:out value="${co.summary}" escapeXml="false" />
        </span>
        </p>
</c:forEach>

<%-- Paging --%>
<c:if test="${widget.pageCount > 1}">
<p class="contentPaging" align="right">
[ <fmt:message key='general.label.page'/>
<c:forEach var="pg" begin="1" end="${widget.pageCount}" varStatus="stat">
    <c:if test="${stat.index > 1}"> | </c:if>
    <c:choose>
        <c:when test="${pg == widget.page}"><span class="contentPageLink"><b><c:out value='${pg}'/></b></span></c:when>
        <c:otherwise><a class="contentPageLink" href="content.jsp?id=<c:out value='${widget.id}'/>&page=<c:out value='${pg}'/>"><c:out value='${pg}'/></a></c:otherwise>
    </c:choose>
</c:forEach>
]
</p>
</c:if>
