<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetVTForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsNonVTPage">
        <com.tms.collab.timesheet.ui.TimeSheetNonVTForm name="tsNonVT"/>
    </page>
</x:config>

<c:if test="${forward.name=='task'}">

    <c:redirect url="TimeSheetNonTaskView.jsp?taskid=${widgets['tsNonVTPage.tsNonVT'].taskId}&projectid=${widgets['tsNonVTPage.tsNonVT'].projectId}"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selecttask"/>');
    </script>
</c:if>

<c:if test="${!empty param.projectid}">
    <x:set name="tsNonVTPage.tsNonVT" property="projectId" value="${param.projectid}"/>
</c:if>


  <%@include file="/ekms/includes/header.jsp" %>
    <table width="100%" cellpadding="2" cellspacing="1">
    <tr>
        <td><x:display name="tsNonVTPage.tsNonVT"/></td>
    </tr>
    </table>
    <%@include file="/ekms/includes/footer.jsp" %>

