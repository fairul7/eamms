<%@ page import="kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="list" value="${widget}"/>
<c:if test="${!(empty list.message)}">
    <script>alert("<c:out value="${list.message}"/>");</script>
</c:if>
<table border="0" align="center" cellspacing="1" cellpadding="4" class="forumBackground" width="100%">
    <tr>
        <td class="forumLabel" valign="top">Name</td>
        <td class="forumLabel" align="center">Last Posted</td>
        <td class="forumLabel" align="center">Topics</td>
        <td class="forumLabel" align="center">Postings</td>
    </tr>
    <c:choose>
        <c:when test="${empty list.forums}">
            <tr><td colspan="4" class="forumRow"><br><br>No Forums Found<br><br></td></tr>
        </c:when>
        <c:otherwise>
            <tr><td colspan="4" class="forumRow">&nbsp;</td></tr>
            <c:forEach items="${list.forums}" var="forum" varStatus="cnt">
                <tr>
                    <td height="20" class="forumFooter" valign="top">
                        <c:choose>
                            <c:when test="${forum.active}">
                                <a class="forumHeaderLink" href="forumTopicList.jsp?forumId=<c:out value="${forum.forumId}" />"><b><c:out value="${forum.name}"/></b></a>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${forum.name}" />
                            </c:otherwise>
                        </c:choose>
                        <br>
                        <span style="font-size: -1"><c:out value="${forum.description}" escapeXml="false"/></span>
                    </td>
                    <td class="forumFooter" align="center">
                        <c:choose>
                            <c:when test="${empty forum.lastPostDate}"><fmt:formatDate value="${forum.creationDate}" pattern="${globalDatetimeLong}" /></c:when>
                            <c:otherwise><fmt:formatDate value="${forum.lastPostDate}" pattern="${globalDatetimeLong}" /></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="forumFooter" align="center"><c:out value="${forum.numOfThread}"/></td>
                    <td class="forumFooter" align="center"><c:out value="${forum.numOfMessage}"/></td>
                </tr>
            </c:forEach>
            <tr><td colspan="4" class="forumRow">&nbsp;</td></tr>
        </c:otherwise>
    </c:choose>
    <tr><td colspan="4" class="forumFooter">&nbsp;</td></tr>
</table>
