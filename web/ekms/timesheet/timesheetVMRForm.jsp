<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetVPForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsVMRPage">
        <com.tms.collab.timesheet.ui.TimesheetVMRForm name="tsVMR"/>
    </page>
</x:config>

<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectproject"/>');
    </script>
</c:if>
<c:if test="${forward.name=='viewMonthlyProject'}">
    <c:redirect url="timeSheetMRForm.jsp?type=project"/>
</c:if>
<c:if test="${forward.name=='viewMonthlyUser'}">
    <c:redirect url="timeSheetMRForm.jsp?type=user"/>
</c:if>
<c:if test="${forward.name=='selectMonthlyReport'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectmonthlyreporttype"/>');
    </script>
</c:if>
<c:if test="${forward.name=='viewUserProject'}">
    <c:redirect url="timeSheetUPReport.jsp?actionType="/>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">

<tr>
    <td>
<x:display name="tsVMRPage.tsVMR"/>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>


