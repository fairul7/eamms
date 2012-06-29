<%@ page import="com.tms.collab.taskmanager.ui.ReassignForm"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr>
    <td class="calendarHeader" align="left"><fmt:message key='taskmanager.label.TaskReassignment'/></td>
</tr>
<tr>
  <td class="calendarRow">
    <x:display name="${form.assignees.absoluteName}" />
  </td>
</tr>

<tr>
  <td class="calendarRow"><fmt:message key='taskmanager.label.Allowassigneestoreassignthetask'/><x:display name="${form.reassignYes.absoluteName}" />
    <x:display name="${form.reassignNo.absoluteName}" />
  </td>
</tr>

<tr>
  <td class="calendarRow">
    <x:display name="${form.reassignButton.absoluteName}" />
    <x:display name="${form.cancelButton.absoluteName}" />
  </td>
</tr>

<tr>
  <td>
  </td>
</tr>



</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
