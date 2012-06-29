<%@include file="/common/header.jsp" %>

<c:set var="form" value="${widget}" />

<jsp:include page="../form_header.jsp" flush="true"/>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <Tr>
        <Td class="calendarHeader" align = "center" colspan="2">
            <c:if test="${form.startTask==true}">
                <fmt:message key='taskmanager.label.ActualStartDate'/>
            </c:if>
            <c:if test="${form.startTask!=true}">
                <fmt:message key='taskmanager.label.ActualCompletionDate'/>
            </c:if>

        </td>
    </tr>

    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.Date'/></td>
        <td class="calendarRow">
            <x:display name="${form.datePicker.absoluteName}" />
        </td>
    </tr>
    <Tr>
        <td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.Time'/></td>
        <td class="calendarRow">
            <x:display name="${form.timePicker.absoluteName}" />
        </td>
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

