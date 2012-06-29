<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetVTForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 28, 2005
  Time: 2:49:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsVTPage">
        <com.tms.collab.timesheet.ui.TimeSheetVTForm name="tsVT"/>
    </page>
</x:config>

<c:if test="${forward.name=='task'}">

    <c:redirect url="TimeSheetTaskView.jsp?taskid=${widgets['tsVTPage.tsVT'].taskId}&projectid=${widgets['tsVTPage.tsVT'].projectId}"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selecttask"/>');
    </script>
</c:if>

<c:if test="${!empty param.projectid}">
    <x:set name="tsVTPage.tsVT" property="projectId" value="${param.projectid}"/>
</c:if>


  <%@include file="/ekms/includes/header.jsp" %>
    <table width="100%" cellpadding="2" cellspacing="1">
    <tr>
        <td><x:display name="tsVTPage.tsVT"/></td>
    </tr>
    </table>
    <%@include file="/ekms/includes/footer.jsp" %>

