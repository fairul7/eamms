<%@ page import="com.tms.collab.forum.ui.NewMessage,
                 org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User"%>
<c:set var="form" value="${widget}"/>
<c:if test="${!(empty form.message.error)}">
    <script>alert("<c:out value="${form.message.error}"/>");</script>
</c:if>
<% 
SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
	User user = ss.getCurrentUser(request);
    String userId = user.getId();%>
<table border="0" align="center" cellspacing="1" cellpadding="4" class="forumBackground" width="100%">
    <form name="newThreadForm" action="" method="POST">
        <tr><td colspan="2" class="forumFooter">&nbsp;</td></tr>
        <tr>
            <td colspan="2" class="forumRow">
                    <a class="forumPathLink" href="forums.jsp"><fmt:message key="general.label.forums"/></a>
                    &gt; <a class="forumPathLink" href="forumTopicList.jsp?forumId=<c:out value="${form.forumId}" />"><c:out value="${form.forumName}" /></a>
                    &gt; <a class="forumPathLink" href="forumMessageList.jsp?threadId=<c:out value="${form.thread.threadId}" />"><c:out value="${form.thread.subject}" /></a>
            </td>
        </tr>
        <c:choose>
            <c:when test="${empty form.parentMessage}">
                <%
                    NewMessage form = (NewMessage) pageContext.getAttribute("form");
                    String content = StringUtils.replace(form.getThread().getContent(), "\n", "<br>");
                    pageContext.setAttribute("content", content);
                %>
                <tr><td colspan="2" class="forumLabel">Original Author: <c:out value="${form.thread.ownerId}" /></td></tr>
                <tr><td colspan="2" class="forumRow"><c:out escapeXml="false" value="${content}"/></td></tr>
            </c:when>
            <c:otherwise>
                <%
                    NewMessage form = (NewMessage) pageContext.getAttribute("form");
                    String content = StringUtils.replace(form.getParentMessage().getContent(), "\n", "<br>");
                    pageContext.setAttribute("content", content);
                %>
                <tr><td colspan="2" class="forumLabel">Original Author: <c:out value="${form.parentMessage.ownerId}" /></td></tr>
                <tr><td colspan="2" class="forumRow"><c:out escapeXml="false" value="${content}"/></td></tr>
            </c:otherwise>
        </c:choose>
        <tr>
            <td class="forumRowLabel" valign="top" align="right">
                <input type="hidden" name="form" value="newMessageForm">
                <input type="hidden" name="forumId" value="<c:out value="${form.forumId}" />">
                <input type="hidden" name="threadId" value="<c:out value="${form.thread.threadId}" />">
                <input type="hidden" name="parentMessageId" value="<c:out value="${param.parentMessageId}" />">
                <fmt:message key="forum.label.subject"/> *&nbsp;
            </td>
            <td class="forumRow"><c:choose>
            <c:when test="${form.message.invalidSubject=='!'}">
            <c:out value="${form.message.invalidSubject}"/><input style="border:1 solid #de123e" type="text" name="msgSubject" value="<c:out value="${param.msgSubject}"/>" size="30">
            </c:when>
            <c:otherwise><input type="text" name="msgSubject" value="<c:out value="${param.msgSubject}"/>" size="30">
            </c:otherwise>
            </c:choose>
            </td>
        </tr>
        <%
        if("anonymous".equals(userId)){ %>
        <tr>
            <td class="forumRowLabel" valign="top" align="right">
                <fmt:message key="forum.label.author"/> *&nbsp;
            </td>
            <td class="forumRow"><c:choose>
            <c:when test="${form.message.invalidAuthor=='!'}">
            <c:out value="${form.message.invalidAuthor}"/><input style="border:1 solid #de123e" type="text" name="msgAuthor" value="<c:out value="${param.msgAuthor}"/>" size="30">
            </c:when>
            <c:otherwise><input type="text" name="msgAuthor" value="<c:out value="${param.msgAuthor}"/>" size="30">
            </c:otherwise>
            </c:choose> 
            </td>
        </tr>
        <tr>
            <td class="forumRowLabel" valign="top" align="right">
                <fmt:message key="forum.label.email"/> *&nbsp;
            </td>
            <td class="forumRow"><c:choose>
            <c:when test="${form.message.invalidEmail=='!'}">
            <c:out value="${form.message.invalidEmail}"/><input style="border:1 solid #de123e" type="text" name="msgEmail" value="<c:out value="${param.msgEmail}"/>" size="30">
            </c:when>
            <c:otherwise><input type="text" name="msgEmail" value="<c:out value="${param.msgEmail}"/>" size="30">
            </c:otherwise>
            </c:choose> 
            </td>
        </tr>
        <%} %>
        <tr>
            <td class="forumRowLabel" valign="top" align="right"><fmt:message key="forum.label.messages"/>&nbsp;</td>
            <td class="forumRowLabel"><textarea name="msgContent" rows="10" cols="45" value="" ><c:out value="${form.content}"/></textarea></td>
        </tr>
        <tr>
            <td class="forumRowLabel">&nbsp;</td>
            <td class="forumRowLabel">
                <input type="submit" class="button" value="<fmt:message key="forum.label.postMessage"/>" name="postMessage">
                <input type="reset" class="button" value="<fmt:message key="general.label.reset"/>" name="reset">
                <input type="button" class="button" value="<fmt:message key="general.label.cancel"/>" name="cancel" onClick="document.location='forumMessageList.jsp?threadId=<c:out value="${param.threadId}" />'">
            </td>
        </tr>
        <tr><td class="forumFooter" colspan="2">&nbsp;</td></tr>
    </form>
</table>
