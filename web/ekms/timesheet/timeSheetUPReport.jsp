<%@ include file="/common/header.jsp"%>

<x:config>
    <page name="UPMRForm">
        <com.tms.collab.timesheet.ui.TimeSheetUserProjectMR name="upmr" actionType=""/>
    </page>
</x:config>

<c:if test="${!empty param.actionType}">
    <x:set name="UPMRForm.upmr" property="actionType" value="${param.actionType}"/>
</c:if>
<c:if test="${empty param.actionType}">
    <x:set name="UPMRForm.upmr" property="actionType" value=""/>
</c:if>

<c:if test="${forward.name=='print'}">
    <script>
        window.open('/ekms/timesheet/timeSheetUPPrint.jsp');
    </script>
</c:if>
<c:if test="${forward.name=='invalidMonth'}">
    <script>
        alert("Please select month");
    </script>
</c:if>
<c:if test="${forward.name=='invalidUsers'}">
    <script>
        alert("Please select user(s)");
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">

<tr>
    <td>
<x:display name="UPMRForm.upmr"/>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>