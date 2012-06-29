<%@ page import="com.tms.collab.forum.ui.EditForumForm"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold">Thread Details</span></td></tr>
    <tr>
        <td><fmt:message key='forum.label.subject'/></td>
        <td><x:display name="${form.subject.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top"><fmt:message key='forum.label.status'/></td>
        <td><!--x:display name="${form.isPublic.absoluteName}"/><br-->
            <x:display name="${form.active.absoluteName}"/></td>
    </tr>
    <tr>
        <td><fmt:message key="forum.label.author"/></td>
        <td><x:display name="${form.author.absoluteName}"/></td>
    </tr>
     <tr>
        <td><fmt:message key='forum.label.email'/></td>
        <td><x:display name="${form.email.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top"><fmt:message key='forum.label.description'/></td>
        <td><x:display name="${form.content.absoluteName}"/></td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <x:display name="${form.updateThread.absoluteName}"/>
            <x:display name="${form.reset.absoluteName}"/>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>