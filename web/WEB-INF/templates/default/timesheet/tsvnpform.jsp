<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="4" cellspacing="0" class="contentBgColor">
    <tr>
        <td class="calendarHeader" colspan="2"><fmt:message key='timesheet.label.timesheet'/> > <fmt:message key="timesheet.menu.nonprojectTask"/></td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="4" cellspacing="1" class="classBackground">
    <c:choose>
    <c:when test="${w.rights==true}">
    <tr>
        <td class="classRowLabel" colspan="2"><fmt:message key="timesheet.label.view"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" width="20%" align="right"><fmt:message key="timesheet.label.selectnonprojectTask"/> </td>
        <td class="classRow" width="80%"><x:display name="${w.sbProject.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel">&nbsp;</td>
        <td class="classRow">
        <x:display name="${w.btViewProject.absoluteName}"/>&nbsp;
        <x:display name="${w.btViewTask.absoluteName}"/>&nbsp;
        <x:display name="${w.btMandaysReport.absoluteName}"/>
        </td>
    </tr>
    </c:when>
    <c:otherwise>
    <tr>
        <td class="classRowLabel" colspan="2"><fmt:message key="timesheet.error.norightsviewnonproj"/></td>
    </tr>
    </c:otherwise>
    </c:choose>
    </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>