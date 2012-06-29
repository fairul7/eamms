<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" scope="request" value="${widget}"/>

<%--Display Form--%>
<x:display name="${panel.childMap.containerForm.absoluteName}" body="custom">
    <table>
    <tr>
      <td valign="top"><fmt:message key='general.label.comments'/><td>
      <td><x:display name="${panel.childMap.containerForm.childMap.commentsBox.absoluteName}" /></td>
    </tr>
    <tr>
      <td valign="top">&nbsp;<td>
      <td>
        <x:display name="${panel.childMap.containerForm.childMap.approveButton.absoluteName}" />
        <x:display name="${panel.childMap.containerForm.childMap.rejectButton.absoluteName}" />
        <x:display name="${panel.childMap.containerForm.childMap.cancel_form_action.absoluteName}" />
      </td>
    </tr>
    </table>
    <hr size="1">
</x:display>

<%--Display Preview--%>
<x:display name="${panel.childMap.contentObjectView.absoluteName}" />
