<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetViewTaskForm"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 26, 2005
  Time: 4:08:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsViewTaskPage">
        <com.tms.collab.timesheet.ui.TimeSheetViewTaskForm name="tsViewTaskForm"/>
    </page>
</x:config>

<c:if test="${forward.name=='view'}">

    <c:redirect url="TimeSheetViewDet.jsp?taskid=${widgets['tsViewTaskPage.tsViewTaskForm'].taskId}&projectid=${widgets['tsViewTaskPage.tsViewTaskForm'].projectId}"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selecttask"/>');
    </script>
</c:if>

<c:if test="${!empty param.projectid}">
    <x:set name="tsViewTaskPage.tsViewTaskForm" property="projectId" value="${param.projectid}"/>
</c:if>

<!-- display page -->
<%@ include file="/ekms/includes/header.jsp" %>
<x:display name="tsViewTaskPage.tsViewTaskForm"></x:display>
<%@ include file="/ekms/includes/footer.jsp" %>