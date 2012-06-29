<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
    <tr>
        <td class="calendarHeader" colspan="2"><fmt:message key='timesheet.label.timesheet'/> > <fmt:message key="timesheet.menu.viewmonthlyreport"/></td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="1" cellspacing="1" class="classBackground">
    <c:choose>
    <c:when test="${w.rights==true}">
    <tr>
        <td colspan="2" class="classRowLabel"><fmt:message key="timesheet.label.viewmonthlyreport"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" align="right"><fmt:message key="timesheet.label.selectreporttype"/></td>
        <td class="classRow"><x:display name="${w.sbMonthlyReport.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel">&nbsp;</td>
        <td class="classRow"><x:display name="${w.btMonthlyReport.absoluteName}"/></td>
    </tr>
    </c:when>
    <c:otherwise>
    <tr>
        <td class="classRowLabel" colspan="2"><fmt:message key="timesheet.error.norightsviewproj"/></td>
    </tr>
    </c:otherwise>
    </c:choose>
    </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>