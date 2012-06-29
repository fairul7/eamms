<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">

<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Title'/></td>
 <td class="calendarRow">   <x:display name="${form.title.absoluteName}" ></x:display>
 </td>
</tr>


<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.StartDate'/></td>
 <td class="calendarRow">   <x:display name="${form.startDate.absoluteName}" ></x:display>
 </td>
</tr>

<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.StartTime'/></td>
 <td class="calendarRow">  <x:display name="${form.startTime.absoluteName}" />
 </td>
</tr>

<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.DueDate'/></td>
 <td class="calendarRow">   <x:display name="${form.dueDate.absoluteName}" ></x:display>
 </td>
</tr>

<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.DueTime'/></td>
 <td class="calendarRow">  <x:display name="${form.dueTime.absoluteName}" />
 </td>
</tr>

<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Reminder'/></td>
 <td class="calendarRow">    <x:display name="${form.reminderRadio.absoluteName}" /> <x:display name="${form.reminderQuantity.absoluteName}" />
    <x:display name="${form.reminderModifier.absoluteName}"/><fmt:message key='taskmanager.label.before'/></td>
</tr>

<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Category'/></td>
 <td class="calendarRow">  <x:display name="${form.categories.absoluteName}" />
 </td>
</tr>


<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Description'/></td>
 <td class="calendarRow">  <x:display name="${form.description.absoluteName}"/>
 </td>
</tr>


<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"> <fmt:message key="taskmanager.label.Allowassigneestoreassignthetask"/>
 </td>
 <td class="calendarRow">  <x:display name="${form.reassignNo.absoluteName}" /> <x:display name="${form.reassignYes.absoluteName}" />
 </td>
</tr>


<Tr>
 <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.AssignTo'/></td>
 <td class="calendarRow">  <x:display name="${form.assignees.absoluteName}" />
 </td>
</tr>

<Tr>
   <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.AttachedFiles'/></td>
   <td class="calendarRow" align="top">
   <x:display name="${form.fileListing.absoluteName}" ></x:display>  <br>
   <x:display name="${form.attachFilesButton.absoluteName}" ></x:display>
   </td>
</tr>


<Tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Resources'/></td>
 <td class="calendarRow">  <x:display name="${form.resources.absoluteName}" />
 </td>
</tr>



<Tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.NotifyMethod'/></td>
 <td class="calendarRow">
        <x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='taskmanager.label.Memo'/><x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='taskmanager.label.Email'/></td>
</tr>


<Tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.NotifyNote'/></td>
 <td class="calendarRow">    <x:display name="${form.notifyNote.absoluteName}"/>
 </td>
</tr>


<Tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Classification'/></td>
 <td class="calendarRow">    <x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='taskmanager.label.Public'/><x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='taskmanager.label.Private'/><%--
        <x:display name="${form.radioConfidential.absoluteName}"/>Confidential
--%>
 </td>
</tr>

<Tr>
 <td class="calendarRow">
 </td>
<td class="calendarRow"><x:display name="${form.submitButton.absoluteName}"/><x:display name="${form.cancelButton.absoluteName}"/> </td>
 </tr>
    </table>

  <jsp:include page="../form_footer.jsp" flush="true"/>
