<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetVPForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsVNPPage">
        <com.tms.collab.timesheet.ui.TimeSheetVNPForm name="tsVNP"/>
    </page>
</x:config>

<c:if test="${forward.name=='task'}">
    <c:redirect url="TimeSheetNonVTForm.jsp?projectid=${widgets['tsVNPPage.tsVNP'].projectId}"/>
</c:if>
<c:if test="${forward.name=='project'}">

    <c:redirect url="TimeSheetNonProjectView.jsp?projectid=${widgets['tsVNPPage.tsVNP'].projectId}"/>
</c:if>
<c:if test="${forward.name=='view mandays report'}">
    <c:redirect url="timeSheetNonMandaysReport.jsp?projectid=${widgets['tsVNPPage.tsVNP'].projectId}"/>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectproject"/>');
    </script>
</c:if>



<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">

<tr>
    <td>
<x:display name="tsVNPPage.tsVNP"/>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>


