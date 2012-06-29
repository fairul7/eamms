
<%@page import="kacang.services.security.* " %>
<%@page import="kacang.*" %>
<%@include file="/common/header.jsp" %>
<c:set var="list" value="${widget}"/>
<c:if test="${!(empty list.message)}">
    <script>alert("<c:out value="${list.message}"/>");</script>
</c:if><table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <tr><td colspan="4" class="forumRow"><a class="forumPathLink" href="forums.jsp"><fmt:message key="general.label.forums"/></a> &gt; <span class="forumPathLink"><c:out value="${list.forum.name}" /></span></td></tr>
    <tr><td colspan="4" class="forumRow"><c:out escapeXml="false" value="${list.forum.description}"/></td></tr>
    <tr>
        <td class="forumLabel"><fmt:message key="forum.label.topic"/></td>
        <td class="forumLabel" align="center">Last Posted</td>
        <td class="forumLabel" align="center"><fmt:message key="forum.label.postedBy"/></td>
        <td class="forumLabel" align="center">Replies</td>
    </tr>
    <c:choose>
        <c:when test="${empty list.threads}">
            <tr><td colspan="4" class="forumFooter">No Threads Found</td></tr>
        </c:when>
        <c:otherwise>
            <c:forEach items="${list.threads}" var="thread" varStatus="cnt">
                <tr>
                    <td height="20" class="forumFooter" valign="top">
                        <c:if test="${thread.active}">
                            <a class="forumLink" href="forumMessageList.jsp?threadId=<c:out value="${thread.threadId}" />"><c:out value="${thread.subject}" /></a>
                        </c:if>
                        <c:if test="${!thread.active}">
                            <c:out value="${thread.subject}" />
                        </c:if>
                    </td>
                    <td class="forumFooter" align="center">
                        <c:choose>
                            <c:when test="${! empty thread.lastPostDate}">
                                <fmt:formatDate value="${thread.lastPostDate}" pattern="${globalDatetimeLong}"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${thread.creationDate}" pattern="${globalDatetimeLong}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="forumFooter" align="center"><c:out value="${thread.ownerId}"/></td>
                    <td class="forumFooter" align="center"><c:out value="${thread.numOfMessage + 1}"/></td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose><tr>
        <td colspan="4" class="forumRow">
            <input type="button" class="button" value="<fmt:message key="forum.label.startNewTopic"/>" onClick="document.location='forumTopicForm.jsp?forumId=<c:out value="${list.forum.forumId}"/>';">
        </td>
    </tr><tr><td colspan="4" class="forumFooter">&nbsp;</td></tr>
</table>
