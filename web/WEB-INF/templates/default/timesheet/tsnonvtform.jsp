<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="4" cellspacing="0" class="contentBgColor">
    <tr>
        <td class="calendarHeader"><fmt:message key="timesheet.label.view"/>
        &gt; <c:out value="${w.category.name}"/>
        </td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="4" cellspacing="1" class="classBackground">
    <tr>
        <td class="classRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selecttask"/> </td>
        <td class="classRow"><x:display name="${w.sbTask.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel">&nbsp;</td>
        <td class="classRow"><x:display name="${w.btSubmit.absoluteName}"/></td>
    </tr>
    </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>