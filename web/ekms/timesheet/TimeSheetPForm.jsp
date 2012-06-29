<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetViewForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 28, 2005
  Time: 10:12:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsViewPageProj">
        <com.tms.collab.timesheet.ui.TimeSheetViewForm name="tsViewForm"/>
    </page>
</x:config>

<c:if test="${forward.name=='cont'}">
<c:redirect url="TimeSheetForm.jsp?taskid=${widgets['tsViewPageProj.tsViewForm'].taskId}&projectid=${widgets['tsViewPageProj.tsViewForm'].projectId}&set=true"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectproject"/>');
    </script>
</c:if>
<x:set name="tsViewPageProj.tsViewForm" property="projectId" value=""/>
<x:set name="tsViewPageProj.tsViewForm" property="taskId" value=""/>
<x:set name="tsViewPageProj.tsViewForm" property="set" value="${true}"/>
<x:set name="tsViewPageProj.tsViewForm" property="addTimeSheet" value="${true}"/>

<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
    <td>
<x:display name="tsViewPageProj.tsViewForm"></x:display>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>
