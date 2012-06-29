<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="w" value="${widget}"/>
<c:if test="${w.showMenu}">
    <table width="100%" border="1" cellspacing="0" cellpadding="5">
        <tr>
            <td><fmt:message key='maillist.label.mailingListModule'/></td>
        </tr>
        <tr>
            <td>
    [
    <x:event name="${w.absoluteName}" type="showComposedMailListTable"><fmt:message key='maillist.label.composedLists'/></x:event> |
    <x:event name="${w.absoluteName}" type="showContentMailListTable"><fmt:message key='maillist.label.contentLists'/></x:event> |
    <x:event name="${w.absoluteName}" type="showScheduledMailListTable"><fmt:message key='maillist.label.scheduledLists'/></x:event> |
    <x:event name="${w.absoluteName}" type="showMailTemplateTable"><fmt:message key='maillist.label.mailingListTemplates'/></x:event> |
    <x:event name="${w.absoluteName}" type="showMailLogTable"><fmt:message key='maillist.label.mailingListLog'/></x:event>
    ]
            </td>
        </tr>
    </table>
    <br>
</c:if>
<x:display name="${w.composedMailListForm.absoluteName}" />
<x:display name="${w.composedMailListTable.absoluteName}" />
<x:display name="${w.contentMailListForm.absoluteName}" />
<x:display name="${w.contentMailListTable.absoluteName}" />
<x:display name="${w.scheduledMailListForm.absoluteName}" />
<x:display name="${w.scheduledMailListTable.absoluteName}" />
<x:display name="${w.mailLogForm.absoluteName}" />
<x:display name="${w.mailLogTable.absoluteName}" />
<x:display name="${w.mailTemplateForm.absoluteName}" />
<x:display name="${w.mailTemplateTable.absoluteName}" />
