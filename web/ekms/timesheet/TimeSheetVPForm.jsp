<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetVPForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 28, 2005
  Time: 2:42:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsVPPage">
        <com.tms.collab.timesheet.ui.TimeSheetVPForm name="tsVP"/>
    </page>
</x:config>

<c:if test="${forward.name=='task'}">
    <c:redirect url="TimeSheetVTForm.jsp?projectid=${widgets['tsVPPage.tsVP'].projectId}"/>
</c:if>
<c:if test="${forward.name=='project'}">

    <c:redirect url="TimeSheetProjectView.jsp?projectid=${widgets['tsVPPage.tsVP'].projectId}"/>
</c:if>
<c:if test="${forward.name=='view mandays report'}">
    <c:redirect url="timeSheetMandaysReport.jsp?projectid=${widgets['tsVPPage.tsVP'].projectId}"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectproject"/>');
    </script>
</c:if>
<c:if test="${forward.name=='viewUserProject'}">
    <c:redirect url="timeSheetUPReport.jsp?actionType="/>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">

<tr>
    <td>
<x:display name="tsVPPage.tsVP"/>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>


