<%@include file="/common/header.jsp"%>

<x:config>
    <page name="timesheetMandaysReport">
        <com.tms.collab.timesheet.ui.TimeSheetProjectViewInMandaysReport name="project"/>
    </page>
</x:config>

<c:if test="${!empty param.projectid}">
    <x:set name="timesheetMandaysReport.project" property="projectId" value="${param.projectid}"/>
    <x:set name="timesheetMandaysReport.project" property="print" value="${false}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td>
		<x:display name="timesheetMandaysReport.project"/>
	</td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>