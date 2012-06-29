<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 24, 2005
  Time: 12:30:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp"%>

<x:config>
    <page name="monthlyReportForm">
        <com.tms.collab.timesheet.ui.TimeSheetMRForm name="mrForm"/>
    </page>
</x:config>


<c:if test="${forward.name=='project'}">
    <c:redirect url="timeSheetPMonthlyReport.jsp?projectId=${widgets['monthlyReportForm.mrForm'].selectedProjectId}&month=${widgets['monthlyReportForm.mrForm'].selectedMonth}"/>
</c:if>
<c:if test="${forward.name=='user'}">
    <c:redirect url="timeSheetUMonthlyReport.jsp?userId=${widgets['monthlyReportForm.mrForm'].selectedUserId}&month=${widgets['monthlyReportForm.mrForm'].selectedMonth}"/>
</c:if>
<c:if test="${forward.name=='selectProject'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectproject"/>');
    </script>
</c:if>
<c:if test="${forward.name=='selectUser'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectuser"/>');
    </script>
</c:if>
<c:if test="${forward.name=='selectMonth'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectmonth"/>');
    </script>
</c:if>

<c:if test="${! empty param.type}">
    <x:set name="monthlyReportForm.mrForm" property="type" value="${param.type}"></x:set>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">

<tr>
    <td>
<x:display name="monthlyReportForm.mrForm"/>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>