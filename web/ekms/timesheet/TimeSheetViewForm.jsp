<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetViewForm"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 26, 2005
  Time: 4:00:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsViewFormPage">
        <com.tms.collab.timesheet.ui.TimeSheetViewForm name="tsViewForm"/>
    </page>
</x:config>

<c:if test="${forward.name=='cont'}">
<c:redirect url="TimeSheetViewDet.jsp?taskid=${widgets['tsViewFormPage.tsViewForm'].taskId}&projectid=${widgets['tsViewFormPage.tsViewForm'].projectId}"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectproject"/>');
    </script>
</c:if>
<x:set name="tsViewFormPage.tsViewForm" property="projectId" value=""/>
<x:set name="tsViewFormPage.tsViewForm" property="taskId" value=""/>
<x:set name="tsViewFormPage.tsViewForm" property="set" value="${false}"/>

<!-- display page -->
<%@ include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
    <td>
<x:display name="tsViewFormPage.tsViewForm"></x:display>
    </td>
</tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>






