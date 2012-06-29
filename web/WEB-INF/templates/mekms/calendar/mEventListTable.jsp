<%@ page import="kacang.runtime.*,kacang.ui.*,
                 kacang.stdui.Table" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="table" value="${widget}"/>
<c:set var="model" value="${table.model}" scope="page" />
<c:set var="pageCount" value="${table.pageCount}"/>
<c:set var="paging">
    <tr>
        <td align="right">
            Page:
            <c:forEach begin="1" end="${pageCount}" var="pg">
                <c:choose>
                    <c:when test="${pg == table.currentPage}"><b></c:when>
                    <c:otherwise><a href="?cn=<c:out value="${table.absoluteName}"/>&et=page&page=<c:out value="${pg}"/>" class="nav"></c:otherwise>
                </c:choose>
                <span class="nav"><c:out value="${pg}" /></span>
                <c:choose>
                    <c:when test="${pg == table.currentPage}"></b></c:when>
                    <c:otherwise></a></c:otherwise>
                </c:choose>
            </c:forEach>
        </td>
    </tr>
</c:set>
<table cellpadding="2" cellspacing="0" align="center" width="100%" border="0">
    <c:choose>
        <c:when test="${empty model.tableRows}">
            <tr><td align="left" valign="top"><fmt:message key="general.label.noResults"/></td></tr>
        </c:when>
        <c:otherwise>
            <c:out value="${paging}" escapeXml="false" />
            <c:forEach items="${model.tableRows}" var="event">
                <tr>
                    <td align="left" valign="top">
                        &#149;&nbsp;<b><a href="?et=sel&eventId=<c:out value="${event.eventId}"/>" class="title"><c:out value="${event.title}"/></a></b><br>
                        <fmt:formatDate value="${event.startDate}" pattern="${globalDatetimeShort}"/> - <fmt:formatDate value="${event.endDate}" pattern="${globalDatetimeShort}"/><br>
                    </td>
                </tr>
            </c:forEach>
            <c:out value="${paging}" escapeXml="false" />
        </c:otherwise>
    </c:choose>
</table>
