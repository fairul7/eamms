<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" scope="request" value="${widget}"/>

<%--Display Form--%>
<x:display name="${panel.childMap.containerForm.absoluteName}" body="custom">
    <x:display name="${panel.childMap.containerForm.childMap.scheduleCheckBox.absoluteName}" />
    <br>
  <table>
    <tr>
      <td>&nbsp;&nbsp;&nbsp;</td>
      <td><fmt:message key='general.label.startDate'/></td>
      <td><x:display name="${panel.childMap.containerForm.childMap.startDate.absoluteName}" /></td>
    </tr>
    <tr>
        <td>&nbsp;&nbsp;&nbsp;</td>
        <td><fmt:message key='general.label.startTime'/></td>
        <td><x:display name="${panel.childMap.containerForm.childMap.startTime.absoluteName}"/></td>
    </tr>
    <tr>
      <td>&nbsp;&nbsp;&nbsp;</td>
      <td><fmt:message key='general.label.endDate'/></td>
      <td><x:display name="${panel.childMap.containerForm.childMap.endDate.absoluteName}" /></td>
    </tr>
    <tr>
        <td>&nbsp;&nbsp;&nbsp;</td>
        <td><fmt:message key='general.label.endTime'/></td>
        <td><x:display name="${panel.childMap.containerForm.childMap.endTime.absoluteName}"/></td>
    </tr>
  </table>
    <br>
    <x:display name="${panel.childMap.containerForm.childMap.recursiveBox.absoluteName}" />
    <br>
    <br>
    <x:display name="${panel.childMap.containerForm.childMap.publishButton.absoluteName}" />
    <x:display name="${panel.childMap.containerForm.childMap.withdrawButton.absoluteName}" />
    <x:display name="${panel.childMap.containerForm.childMap.cancel_form_action.absoluteName}" />
    <hr size="1">
</x:display>

<%--Display Preview--%>
<x:display name="${panel.childMap.contentObjectView.absoluteName}" />
