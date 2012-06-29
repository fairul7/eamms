<%@ page import="com.tms.collab.forum.ui.EditForumForm"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" class="forumBackground" cellpadding="4" cellspacing="1">
        <tr><td colspan="2" class="forumHeader"><fmt:message key='forum.label.messageDetails'/></td></tr>
    <tr>
        <td class="forumRowLabel"><fmt:message key='forum.label.subject'/></td>
        <td class="forumRowLabel"><x:display name="${form.subject.absoluteName}"/></td>
    </tr>
     <tr>
        <td class="forumRowLabel"><fmt:message key="forum.label.author"/></td>
        <td class="forumRowLabel"><x:display name="${form.author.absoluteName}"/></td>
    </tr>
     <tr>
        <td class="forumRowLabel"><fmt:message key='forum.label.email'/></td>
        <td class="forumRowLabel"><x:display name="${form.email.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="forumRowLabel" valign="top"><fmt:message key="forum.label.messages"/></td>
        <td class="forumRowLabel"><x:display name="${form.content.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="forumRowLabel">&nbsp;</td>
        <td class="forumRowLabel">
            <x:display name="${form.updateMessage.absoluteName}"/>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>