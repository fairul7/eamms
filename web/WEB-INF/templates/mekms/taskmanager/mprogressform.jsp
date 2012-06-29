<%@include file="/common/header.jsp" %>

<c:set var="form" value="${widget}" />

<jsp:include page="../mform_header.jsp" flush="true"/>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <Tr>
        <Td class="calendarHeader" align = "center" colspan="2"><fmt:message key='taskmanager.label.TaskProgress'/></td>
    </tr>

    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.Progress'/></td>
        <td class="calendarRow">
            <x:display name="${form.progressSelectBox.absoluteName}" />
        </td>
    </tr>
    <tr>
        <td class="calendarRow">
        &nbsp;
        </td>
        <td class="calendarRow">
            <x:display name="${form.setButton.absoluteName}" /> <x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
    <tr>
        <td class="calendarFooter" colspan="2">
         &nbsp;
        </td>
    </tr>

</table>
<jsp:include page="../mform_footer.jsp" flush="true"/>

