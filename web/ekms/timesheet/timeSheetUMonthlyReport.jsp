<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 24, 2005
  Time: 6:07:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="monthlyReport">
        <com.tms.collab.timesheet.ui.TimeSheetMonthlyReport name="uReport" reportType="user"/>
    </page>
</x:config>

<c:if test="${! empty param.month}">
    <x:set name="monthlyReport.uReport" property="month" value="${param.month}"/>
</c:if>
<c:if test="${! empty param.userId}">
    <x:set name="monthlyReport.uReport" property="userId" value="${param.userId}"/>
</c:if>

<c:if test="${forward.name=='print'}">
    <script>
        window.open('timeSheetMRPrint.jsp?userId=<c:out value="${widgets[monthlyReport.uReport].userId}"/>&month=<c:out value="${widgets[monthlyReport.uReport].month}"/>');
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">

<tr>
    <td>
<x:display name="monthlyReport.uReport"/>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>