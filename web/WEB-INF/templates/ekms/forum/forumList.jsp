<%@ page import="kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="list" value="${widget}"/>
<c:if test="${!(empty list.message)}">
    <script>alert("<c:out value="${list.message}"/>");</script>
</c:if>
<table border="0" align="center" cellspacing="0" cellpadding="4" class="forumBackground" width="100%">
    <tr>
        <td class="forumHeader">
            <fmt:message key='forum.label.forum'/> > 
            <c:choose>
                <c:when test="${! empty param.category}"><c:out value="${param.category}"/></c:when>
                <c:otherwise><fmt:message key='forum.label.allForums'/></c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td class="forumRow">&nbsp;</td></tr>
    <tr>
        <td class="forumRow">
            <table cellpadding="4" cellspacing="1" width="99%" align="center">
                <tr>
                    <td class="forumHeader" valign="top">&nbsp;</td>
                    <td class="forumHeader" valign="top"><fmt:message key='forum.label.name'/></td>
                    <td class="forumHeader" nowrap><fmt:message key='forum.label.lastPosted'/></td>
                    <td class="forumHeader"><fmt:message key='forum.label.topics'/></td>
                    <td class="forumHeader"><fmt:message key='forum.label.postings'/></td>
                </tr>
                <c:choose>
                    <c:when test="${empty list.forums}">
                        <tr><td colspan="5" class="forumRow"><br><br><fmt:message key='forum.label.noForumsFound'/><br><br></td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:set var="count" value="1"/>
                        <c:forEach items="${list.forums}" var="forum" varStatus="cnt">
                            <tr>
                                <td class="forumFooter" valign="top"><c:out value="${count}"/></td>
                                <td height="20" class="forumFooter" valign="top">
                                    <c:choose>
                                        <c:when test="${forum.active}">
                                            <a href="forumTopicList.jsp?forumId=<c:out value="${forum.forumId}" />"><c:out value="${forum.name}"/></a>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${forum.name}" />
                                        </c:otherwise>
                                    </c:choose>
                                    <br><br>
                                    <c:out value="${forum.description}" escapeXml="false"/>
                                </td>
                                <td class="forumFooter" valign="top">
                                    <c:choose>
                                        <c:when test="${empty forum.lastPostDate}"><fmt:formatDate value="${forum.creationDate}" pattern="${globalDatetimeLong}" /></c:when>
                                        <c:otherwise><fmt:formatDate value="${forum.lastPostDate}" pattern="${globalDatetimeLong}" /></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="forumFooter" valign="top"><c:out value="${forum.numOfThread}"/></td>
                                <td class="forumFooter" valign="top"><c:out value="${forum.numOfMessage}"/></td>
                            </tr>
                            <c:set var="count" value="${count + 1}"/>
                        </c:forEach>
                        <tr><td colspan="5" class="forumRow">&nbsp;</td></tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </td>
    </tr>
    <tr><td class="forumFooter">&nbsp;</td></tr>
</table>
