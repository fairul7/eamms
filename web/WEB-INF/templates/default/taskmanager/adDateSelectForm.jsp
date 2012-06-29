<%@include file="/common/header.jsp" %>

<c:set var="form" value="${widget}" />

<jsp:include page="../form_header.jsp" flush="true"/>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <Tr>
        <Td class="calendarHeader" align = "center" colspan="2">
                <fmt:message key='taskmanager.label.ActualCompletionDate'/>
        </td>
    </tr>

    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.user'/></td>
        <td class="calendarRow">
            <c:out value="${form.userName}" />
        </td>
    </tr>

    <tr>
        <td class="calendarRowLabel" colspan="2">
            <x:display name="${form.startCheckBox.absoluteName}"/>&nbsp;
            <fmt:message key="taskmanager.label.setStartDate"/>
        </td>
    </tr>

    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.StartDate'/></td>
        <td class="calendarRow">
            <x:display name="${form.datePicker.absoluteName}" />
        </td>
    </tr>
    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.StartTime'/></td>
        <td class="calendarRow">
            <x:display name="${form.timePicker.absoluteName}" />
        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel">&nbsp;</td>
        <td class="calendarRow">&nbsp;</td>
    </tr>

    <tr>
        <td class="calendarRowLabel" colspan="2">
            <x:display name="${form.completionCheckBox.absoluteName}"/>&nbsp;
            <fmt:message key="taskmanager.label.setCompletionDate"/>
        </td>
    </tr>

    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.CompletionDate'/></td>
        <td class="calendarRow">
            <x:display name="${form.completionDatePicker.absoluteName}" />
        </td>
    </tr>
    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.CompletionTime'/></td>
        <td class="calendarRow">
            <x:display name="${form.completionTimePicker.absoluteName}" />
        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel">&nbsp;</td>
        <td class="calendarRow">&nbsp;</td>
    </tr>

    <tr>
        <td class="calendarRow">
        &nbsp;
        </td>
        <td class="calendarRow">
            <x:display name="${form.btnSubmit.absoluteName}" /> <x:display name="${form.btnCancel.absoluteName}" />
        </td>
    </tr>
    <tr>
        <td class="calendarFooter" colspan="2">
         &nbsp;
        </td>
    </tr>

</table>
<jsp:include page="../form_footer.jsp" flush="true"/>

