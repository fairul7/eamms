<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetViewTaskForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 28, 2005
  Time: 10:14:06 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsViewTaskPage">
        <com.tms.collab.timesheet.ui.TimeSheetViewTaskForm name="tsViewForm" />
    </page>
</x:config>

<x:set name="tsViewTaskPage.tsViewForm" property="addTimeSheet" value="${true}"/>

<c:if test="${forward.name=='view'}">

    <c:redirect url="TimeSheetForm.jsp?taskid=${widgets['tsViewTaskPage.tsViewForm'].taskId}&projectid=${widgets['tsViewTaskPage.tsViewForm'].projectId}&set=true"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selecttask"/>');
    </script>
</c:if>

<c:if test="${!empty param.projectid}">
    <x:set name="tsViewTaskPage.tsViewForm" property="projectId" value="${param.projectid}"/>
    <x:set name="tsViewTaskPage.tsViewForm" property="set" value="${true}"/>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
    <td>
<x:display name="tsViewTaskPage.tsViewForm"></x:display>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>
