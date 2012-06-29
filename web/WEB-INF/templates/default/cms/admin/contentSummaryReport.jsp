<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="kacang.tld" prefix="x"%>

<c:set var="w" scope="request" value="${widget}" />

<jsp:include page="../../form_header.jsp" flush="true" />

<link rel="stylesheet"
	href="<%= request.getContextPath() %>/common/tree/tree.css">

<script language="javascript"
	src="<%= request.getContextPath() %>/common/tree/tree.js">
</script>

<table border=0 cellspacing=2 cellpadding=1>
	<tr>
		<td>Date</td>
		<td>From <x:display name="${w.startDate.absoluteName}" /> To <x:display
			name="${w.endDate.absoluteName}" /></td>
	</tr>
	<tr>
		<td>Group</td>
		<td><x:display name="${w.selGroup.absoluteName}" /></td>
	</tr>
	<tr>
		<td>&nbsp;
		</td>
		<td>
			<x:display name="${w.submitBtn.absoluteName}" />
			<input type="button" class="button"
				onclick="location.href='systemSettings.jsp'"
				value="<fmt:message key='general.label.cancel'/>">
			<input type="button" class="button" name="exportCsv" value="Export to CSV" onclick="javascript:generateCsv()" />
		</td>
	</tr>
</table>
<br />
<table width="100%" cellpadding="4" cellspacing="1">
	<tr>
		<td class="tableHeader">Content Type</td>
		<td class="tableHeader">Submitted</td>
        <td class="tableHeader">Approved</td>
		<%--<td class="tableHeader">
			Approved By Supervisor
		</td>
		<td class="tableHeader">
			Approved By Editor
		</td>
		<td class="tableHeader">
			Approved By Compliance
		</td>--%>
		<td class="tableHeader">Published</td>
	</tr>
	<c:forEach var="v" items="${w.results}">
	<tr>
		<td><c:out value="${v.contentName}"/></td>
		<td><c:out value="${v.submittedCount}"/></td>
        <td><c:out value="${v.approvedBySupervisorCount}"/></td>
		<%--<td><c:out value="${v.approvedBySupervisorCount}"/></td>
		<td><c:out value="${v.approvedByEditorCount}"/></td>
		<td><c:out value="${v.approvedByComplianceCount}"/></td>--%>
		<td><c:out value="${v.publishCount}"/>
		</td>
	</tr>
	</c:forEach>
</table>

<jsp:include page="../../form_footer.jsp" flush="true" />
