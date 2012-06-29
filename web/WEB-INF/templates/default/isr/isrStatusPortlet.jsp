<%@page import="com.tms.collab.isr.model.StatusObject" %>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td valign="top" class="contentBgColor fieldTitle" width="50%">
			&nbsp;
		</td>
		<td class="contentBgColor fieldTitle">
			<fmt:message key="isr.label.submittedRequests" />
		</td>
		<td class="contentBgColor fieldTitle">
			<fmt:message key="isr.label.attendingRequests" />
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle" width="50%">
			<fmt:message key="isr.label.statusNew" />
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?status=<c-rt:out value='<%=StatusObject.STATUS_ID_NEW %>'/>"><c:out value="${w.requesterNewRequestCount}"/></a>
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?attendingStatus=<c-rt:out value='<%=StatusObject.STATUS_ID_NEW %>'/>#attendingRequest"><c:out value="${w.attendantNewRequestCount}"/></a>
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle">
			<fmt:message key="isr.label.statusClarification" />
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?status=<c-rt:out value='<%=StatusObject.STATUS_ID_CLARIFICATION %>'/>"><c:out value="${w.requesterClarificationRequestCount}"/></a>
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?attendingStatus=<c-rt:out value='<%=StatusObject.STATUS_ID_CLARIFICATION %>'/>#attendingRequest"><c:out value="${w.attendantClarificationRequestCount}"/></a>
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle">
			<fmt:message key="isr.label.statusInProgress" />
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?status=<c-rt:out value='<%=StatusObject.STATUS_ID_IN_PROGRESS %>'/>"><c:out value="${w.requesterInProgressRequestCount}"/></a>
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?attendingStatus=<c-rt:out value='<%=StatusObject.STATUS_ID_IN_PROGRESS %>'/>#attendingRequest"><c:out value="${w.attendantInProgressRequestCount}"/></a>
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle">
			<fmt:message key="isr.label.statusResolved" />
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?status=<c-rt:out value='<%=StatusObject.STATUS_ID_COMPLETED %>'/>"><c:out value="${w.requesterResolvedRequestCount}"/></a>
		</td>
		<td class="contentBgColor">
			<a onclick="openLink('', 'isr')" target="isr" href="/ekms/isr/viewRequestListing.jsp?attendingStatus=<c-rt:out value='<%=StatusObject.STATUS_ID_COMPLETED %>'/>#attendingRequest"><c:out value="${w.attendantResolvedRequestCount}"/></a>
		</td>
	</tr>
</table>