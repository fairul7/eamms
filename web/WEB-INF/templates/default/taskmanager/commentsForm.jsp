<%@include file="/common/header.jsp" %>

<c:set var="form" value="${widget}" />

<jsp:include page="../form_header.jsp" flush="true"/>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <Tr>
        <Td class="calendarHeader" align = "center" colspan="2">
            <fmt:message key='taskmanager.label.addComments'/>
        </td>
    </tr>

    <Tr>
        <td class="calendarRowLabel" align="right" valign="top"><fmt:message key='taskmanager.label.comments'/></td>
        <td class="calendarRow">
            <x:display name="${form.tfComments.absoluteName}" />
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

