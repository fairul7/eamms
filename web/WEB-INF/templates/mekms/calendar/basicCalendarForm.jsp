<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>




<jsp:include page="../form_header.jsp" flush="true"/>


<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
     <tr><td  colspan="2" class="calendarHeader">Appointment</td></tr>

<c:if test="${!form.newResourceBooking}" >
<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Title'/></td>
    <td class="calendarRow" >
        <x:display name="${form.title.absoluteName}"/>
        <x:display name="${form.validTitle.absoluteName}"/>
    </td>
</tr>
</c:if>
<tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.StartDate'/></td>
<td class="calendarRow" >
<x:display name="${form.startDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.EndDate'/></td>
<td class="calendarRow" >
<x:display name="${form.endDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.StartTime'/></td>
<td class="calendarRow" >
<x:display name="${form.startTime.absoluteName}"/>
</td>
</tr>

<tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.EndTime'/></td>
<td class="calendarRow" >
    <x:display name="${form.endTime.absoluteName}"/>
    <x:display name="${form.allDay.absoluteName}" /><fmt:message key='calendar.label.AllDay'/><%--
    <input type="checkbox" name="allDay" value="true"<c:if test="${calendar_calendar.allDay}"> checked</c:if>>All Day
--%>
</td>
</tr>

<tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Reminder'/></td>
<td class="calendarRow" >
    <%--<select name="reminder">
        <option value=""><fmt:message key='calendar.label.None'/></option>
        <option value="1"<c:if test="${calendar_calendar.reminderDays == 1}"> selected</c:if>><fmt:message key='calendar.label.1DayBefore'/></option>
        <option value="3"<c:if test="${calendar_calendar.reminderDays == 3}"> selected</c:if>><fmt:message key='calendar.label.3DaysBefore'/></option>
        <option value="5"<c:if test="${calendar_calendar.reminderDays == 5}"> selected</c:if>><fmt:message key='calendar.label.5DaysBefore'/></option>
        <option value="7"<c:if test="${calendar_calendar.reminderDays == 7}"> selected</c:if>>1 Week Before</option>
        <option value="14"<c:if test="${calendar_calendar.reminderDays == 14}"> selected</c:if>>2 Weeks Before</option>
    </select>--%>
    <x:display name="${form.reminderRadio.absoluteName}" /> <x:display name="${form.reminderQuantity.absoluteName}" />
    <x:display name="${form.reminderModifier.absoluteName}"/><fmt:message key='calendar.label.before'/></td>
</tr>

<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Description'/></td>
    <td class="calendarRow" >
        <x:display name="${form.description.absoluteName}"/>
    </td>
</tr>


<%--
 <c:if test="${!form.newResourceBooking}" >
<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Agenda'/></td>
    <td class="calendarRow" >
        <x:display name="${form.agenda.absoluteName}"/>
    </td>
</tr>
</c:if>
--%>

<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Location'/></td>
    <td class="calendarRow" >
        <x:display name="${form.location.absoluteName}"/>
    </td>
</tr>

 <c:if test="${!form.newResourceBooking}" >

<tr>
    <td  class="calendarRowLabel" width="23%" height="20" valign="top" align="right"></td>
    <td class="calendarRow" >
        <x:display name="${form.exclude.absoluteName}"/><fmt:message key='calendar.label.Excludeyourself'/></td>
</tr>

<%--<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Universal'/></td>
    <td class="calendarRow" >
        <x:display name="${form.universal.absoluteName}"/>
    </td>
</tr>--%>
 </c:if>
<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Classification'/></td>
    <td class="calendarRow" >
        <x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='calendar.label.Public'/><x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='calendar.label.Private'/><%--        <x:display name="${form.radioConfidential.absoluteName}"/>Confidential--%>
    </td>
</tr>

<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.CompulsoryAttendees'/></td>
    <td class="calendarRow">
        <table>
            <tr>
                <td>
                 <x:display name="${form.nextButton.absoluteName}"/> <x:display name="${form.removeButton.absoluteName}"/>
                   <br>
                 <x:display name="${form.users.absoluteName}"/>
                 </td>
            </tr>
        </table>
    </td>
</tr>

<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
        <fmt:message key='calendar.label.Attendees'/></td>
    <td class="calendarRow" >
        <table>
            <tr>
                <td>
                 <x:display name="${form.nextOptionalButton.absoluteName}"/> <x:display name="${form.removeOptionalButton.absoluteName}"/>
                   <br>
                 <x:display name="${form.optionalUsers.absoluteName}"/>
                 </td>
            </tr>
        </table>
    </td>
</tr>



<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Resources'/></td>
    <td class="calendarRow" >
    <x:display name="${form.resourcesSB.absoluteName}"/>
    </td>
</tr>

<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.NotifyMethod'/></td>
    <td class="calendarRow" >
        <x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='calendar.label.Memo'/><x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='calendar.label.Email'/></td>
</tr>

<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.NotifyNote'/></td>
    <td class="calendarRow" >
        <x:display name="${form.notifyNote.absoluteName}"/>
    </td>
</tr>
<tr><td class="calendarRow" ></td><td class="calendarRow"  >
<x:display name="${form.submitButton.absoluteName}"/>
<x:display name="${form.cancelButton.absoluteName}"/>
</td></tr>
<jsp:include page="../form_footer.jsp" flush="true"/>

</table>



