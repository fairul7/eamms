<%@include file="/common/header.jsp"%>

<x:config>
    <page name="timesheetNonMandaysReport">
        <com.tms.collab.timesheet.ui.TimeSheetNonProjectViewInMandaysReport name="nonproject"/>
    </page>
</x:config>

<c:if test="${!empty param.projectid}">
    <x:set name="timesheetNonMandaysReport.nonproject" property="projectId" value="${param.projectid}"/>
    <x:set name="timesheetNonMandaysReport.nonproject" property="print" value="${false}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td>
		<x:display name="timesheetNonMandaysReport.nonproject"/>
	</td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>