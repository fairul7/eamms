<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="tree" value="${widget}"/>
<c:set var="root" scope="request" value="${tree.thread}"/>
<!--<c:choose>
	<c:when test="${tree.direct}">
		<script>
			document.location="forumMessageList.jsp?threadId=<c:out value="${tree.thread.threadId}"/>";
		</script>
	</c:when>
</c:choose>
--><table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <tr>
        <td class="forumHeader" colspan="2">
            <a href="forums.jsp"><font class="forumHeader"><fmt:message key="general.label.forums"/></font></a>
            &gt; <a href="forumTopicList.jsp?forumId=<c:out value="${tree.thread.forumId}" />"><font class="forumHeader"><c:out value="${tree.forumName}" /></font></a>
            &gt; <a href="forumMessageList.jsp?threadId=<c:out value="${tree.thread.threadId}" />"><font class="forumHeader"><c:out value="${tree.thread.subject}" /></font></a>
        </td>
    </tr>
    <tr><td class="forumFooter" colspan="2">&nbsp;</td></tr>
    <c:choose>
        <c:when test="${empty tree.message}">
            <tr>
                <td class="forumRowLabel" align="right"><fmt:message key="forum.label.topic"/>&nbsp;</td>
                <td class="forumRow"><c:out value="${tree.thread.subject}" /></td>
            </tr>
            <tr>
                <td class="forumRowLabel" align="right">Author&nbsp;</td>
                <td class="forumRow"><c:out value="${tree.thread.ownerId}" /></td>
            </tr>
            <tr>
                <td class="forumRowLabel" align="right">Date&nbsp;</td>
                <td class="forumRow"><fmt:formatDate value="${tree.thread.creationDate}" pattern="${globalDatetimeLong}" /></td>
            </tr>
            <tr><td class="forumFooterLabel" colspan="2">Contents</td></tr>
            <tr><td class="forumRow" colspan="2"><font class="forumRow"><c:out escapeXml="false" value="${tree.thread.content}" /></font></td></tr>
            <tr>
                <td class="forumFooter" colspan="2" align="right"><!--<c:if test="${tree.moderator}">
                	<input type="button" value="<fmt:message key="forum.label.deleteMessage"/>" class="button" onClick="document.location='forumMessageList.jsp?threadId=<c:out value="${tree.thread.threadId}"/>&messageId=<c:out value="${tree.message.messageId}"/>&delete=true';"></c:if>
                    --><input type="button" value="<fmt:message key="forum.label.postNewMessage"/>" class="button" onClick="document.location='forumMessageForm.jsp?threadId=<c:out value="${tree.thread.threadId}"/>';">
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td class="forumRowLabel" align="right"><fmt:message key="forum.label.topic"/> :</td>
                <td class="forumRow"><c:out escapeXml="false" value="${tree.message.subject}" /></td>
            </tr>
            <tr>
                <td class="forumRowLabel" align="right">Author :</td>
                <td class="forumRow"><c:out value="${tree.message.ownerId}" /></td>
            </tr>
            <tr>
                <td class="forumRowLabel" align="right">Date :</td>
                <td class="forumRow"><fmt:formatDate value="${tree.message.creationDate}" pattern="${globalDatetimeLong}" /></td>
            </tr>
            <tr><td class="forumFooterLabel" colspan="2">Contents</td></tr>
            <tr><td class="forumRow" colspan="2"><font class="forumRow"><c:out escapeXml="false" value="${tree.message.content}" /></font></td></tr>
            <tr>
                <td class="forumFooter" colspan="2" align="right">
                <c:if test="${tree.moderator}">
                	                	<input type="button" value="<fmt:message key="forum.label.deleteMessage"/>" class="button" onClick="document.location='forumMessageList.jsp?threadId=<c:out value="${tree.thread.threadId}"/>&messageId=<c:out value="${tree.message.messageId}"/>&delete=true';"></c:if>
                    <input type="button" value="<fmt:message key="forum.label.reply"/>" class="button" onClick="document.location='forumMessageForm.jsp?threadId=<c:out value="${tree.thread.threadId}"/>&parentMessageId=<c:out value="${tree.message.messageId}"/>';">
                </td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr><td class="forumFooterLabel" colspan="2">Forum Threads</td></tr>
    <script language="javascript" src="<%= request.getContextPath() %>/common/tree/forumTree.js"></script>
    <tr>
        <td valign=top class="forumRow" colspan="2">
            <c:choose>
                <c:when test="${!empty root.children && !empty root.children[0]}">
                    <span id="<c:out value="${tree.name}"/><c:out value="${root.threadId}"/>" style="display: block">
                        <script>
                        <!--
                            //treeLoad('<c:out value="${tree.name}"/><c:out value="${root.threadId}"/>');
                        //-->
                        </script>
                        <c:set var="orig" scope="page" value="${root}"/>
                        <jsp:include page="messageTreeRecursion.jsp" flush="true"/>
                        <c:set var="root" scope="request" value="${orig}"/>
                    </span>
                </c:when>
                <c:otherwise>
                    No Messages Found
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td class="forumFooter" colspan="2">&nbsp;</td></tr>
</table>
<p>
