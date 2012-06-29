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
</c:if><% 
SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
	User user = ss.getCurrentUser(request);
    String userId = user.getId();%>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <form name="newThreadForm" action="" method="POST">
        <tr><td colspan="2" class="forumFooter">&nbsp;</td></tr>
        <tr>
            <td colspan="2" class="forumRow">
                <a class="forumPathLink" href="forums.jsp"><fmt:message key="general.label.forums"/></a>
                &gt; <a class="forumPathLink" href="forumTopicList.jsp?forumId=<c:out value="${form.forumId}" />"><c:out value="${form.forumName}" /></a>
                &gt; New Topic
            </td>
        </tr>
        <tr><td colspan="2" class="forumRow"><c:out escapeXml="false" value="${form.forumDesc}"/></td></tr>
        <tr>
            <td class="forumRowLabel" width="23%" height="20" valign="top" align="right">
                <input type="hidden" name="form" value="newThreadForm">
                <input type="hidden" name="forumId" value="<c:out value="${form.forumId}" />">
                <fmt:message key="forum.label.subject"/>&nbsp; *
            </td>
            <td class="forumRow" width="77%">
                <c:choose>
            <c:when test="${form.message.invalidSubject=='!'}">
            <c:out value="${form.message.invalidSubject}"/><input style="border:1 solid #de123e" type="text" name="subject" value="<c:out value="${param.subject}"/>" size="30">
            </c:when>
            <c:otherwise><input type="text" name="subject" value="<c:out value="${param.subject}"/>" size="30">
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
            </c:choose></td>
        </tr>
        <%} %>
        <tr>
            <td class="forumRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='forum.label.description'/>&nbsp;</td>
            <td class="forumRowLabel" width="77%"><textarea name="content" rows="10" value="" cols="45"><c:out value="${form.content}"/></textarea></td>
        </tr>
        <tr>
            <td class="forumRowLabel" width="23%">&nbsp;</td>
            <td class="forumRowLabel" width="23%">
                <input type="submit" class="button" value="<fmt:message key="forum.label.createTopic"/>" name="createThread">
                <input type="reset" value="<fmt:message key="general.label.reset"/>" name="reset" class="button">
                <input type="button" class="button" value="<fmt:message key="general.label.cancel"/>" name="cancel" onClick="document.location='forumTopicList.jsp?forumId=<c:out value="${form.forumId}" />'">
            </td>
        </tr>
        <tr><td colspan="2" class="forumFooter">&nbsp;</td></tr>
    </form>
</table>
