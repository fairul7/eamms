<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 24, 2005
  Time: 4:50:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="monthlyReport">
        <com.tms.collab.timesheet.ui.TimeSheetMonthlyReport name="pReport" reportType="project"/>
    </page>
</x:config>

<c:if test="${! empty param.month}">
    <x:set name="monthlyReport.pReport" property="month" value="${param.month}"/>
</c:if>
<c:if test="${! empty param.projectId}">
    <x:set name="monthlyReport.pReport" property="projectId" value="${param.projectId}"/>
</c:if>

<c:if test="${forward.name=='print'}">
    <script>
        window.open('timeSheetMRPrint.jsp?projectId=<c:out value="${widgets[monthlyReport.pReport].projectId}"/>&month=<c:out value="${widgets[monthlyReport.pReport].month}"/>');
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">

<tr>
    <td>
<x:display name="monthlyReport.pReport"/>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>