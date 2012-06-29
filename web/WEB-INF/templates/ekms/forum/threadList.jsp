
<%@include file="/common/header.jsp" %>
<c:set var="list" value="${widget}"/>
<c:if test="${!(empty list.message)}">
    <script>alert("<c:out value="${list.message}"/>");</script>
</c:if><table cellpadding="4" cellspacing="0" class="forumBackground" width="100%">
    <tr><td class="forumHeader"><a href="forums.jsp"><font class="forumHeader"><fmt:message key="general.label.forum"/></font></a> &gt; <c:out value="${list.forum.name}" /></td></tr>
    <tr><td class="forumRow">&nbsp;</td></tr>
    <tr><td class="forumRow" align="right"><input type="button" class="button" value="<fmt:message key="forum.label.startNewTopic"/>" onClick="document.location='forumTopicForm.jsp?forumId=<c:out value="${list.forum.forumId}"/>';">&nbsp;</td></tr>
    <tr>
        <td class="forumRow">
            <table cellpadding="4" cellspacing="1" width="99%" align="center">

                <tr>
                    <td class="forumHeader" valign="top">&nbsp;</td>
                    <td class="forumHeader"><fmt:message key="forum.label.topic"/></td>
                    <td class="forumHeader"><fmt:message key='forum.label.lastPosted'/></td>
                    <td class="forumHeader"><fmt:message key='forum.label.createdBy'/></td>
                    <td class="forumHeader"><fmt:message key='forum.label.replies'/></td>
                </tr>
                <c:choose>
                    <c:when test="${empty list.threads}">
                        <tr><td colspan="5" class="forumFooter"><fmt:message key='forum.label.noThreadsFound'/></td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${list.threads}" var="thread" varStatus="cnt">
                            <tr>
                                <td class="forumFooter" valign="top"><c:out value="${cnt.index + 1}"/></td>
                                <td height="20" class="forumFooter" valign="top">
                                    <c:if test="${thread.active}">
                                        <a href="forumMessageList.jsp?threadId=<c:out value="${thread.threadId}" />"><c:out value="${thread.subject}" /></a>
                                    </c:if>
                                    <c:if test="${!thread.active}">
                                        <c:out value="${thread.subject}" />
                                    </c:if>
                                </td>
                                <td class="forumFooter" valign="top">
                                    <c:choose>
                                        <c:when test="${! empty thread.lastPostDate}">
                                            <fmt:formatDate value="${thread.lastPostDate}" pattern="${globalDatetimeLong}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatDate value="${thread.creationDate}" pattern="${globalDatetimeLong}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="forumFooter" valign="top"><c:out value="${thread.ownerId}"/></td>
                                <td class="forumFooter" valign="top"><c:out value="${thread.numOfMessage + 1}"/></td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </table>
        </td>
    </tr><tr><td class="forumRow" align="right"><input type="button" class="button" value="<fmt:message key="forum.label.startNewTopic"/>" onClick="document.location='forumTopicForm.jsp?forumId=<c:out value="${list.forum.forumId}"/>';">&nbsp;</td></tr>
    <tr><td class="forumRow">&nbsp;</td></tr><tr><td class="forumFooter">&nbsp;</td></tr>
    
</table>
