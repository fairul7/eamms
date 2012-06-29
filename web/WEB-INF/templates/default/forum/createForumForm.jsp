<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='forum.label.forumDetails'/></span></td></tr>
    <tr>
        <td valign="top" width="150px"><fmt:message key='forum.label.forumName'/></td>
        <td><x:display name="${form.forumName.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top"><fmt:message key='general.label.status'/></td>
        <td><x:display name="${form.isPublic.absoluteName}"/><br>
            <x:display name="${form.active.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top"><fmt:message key='general.label.description'/></td>
        <td><x:display name="${form.description.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top"><fmt:message key="forum.label.category"/></td>
        <td><x:display name="${form.categories.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top">&nbsp;</td>
        <td><fmt:message key="forum.label.newCategory"/> : <x:display name="${form.newCategory.absoluteName}"/></td>
    </tr>
    <c:if test="${! empty form.message}">
        <tr>
            <td valign="top">&nbsp;</td>
            <td><font color="red"><c:out value="${form.message}"/></font></td>
        </tr>
    </c:if>
    <tr><td colspan="2">&nbsp;</td></tr>
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='forum.label.accessPermission'/></span></td></tr>
    <tr>
        <td valign="top"><fmt:message key='forum.label.moderatorGroups'/></td>
        <td><x:display name="${form.moderatorGroup.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top"><fmt:message key='forum.label.userGroups'/></td>
        <td><x:display name="${form.userGroup.absoluteName}"/></td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <x:display name="${form.createForum.absoluteName}"/>
            <x:display name="${form.reset.absoluteName}"/>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>