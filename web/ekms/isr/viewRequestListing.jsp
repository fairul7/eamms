<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.permission.model.PermissionModel,
				com.tms.collab.isr.ui.RequestorRequestListingTable,
				kacang.Application" %>
<%@include file="/common/header.jsp" %>

<% 
Application app = Application.getInstance();
PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
boolean canCreateRequest = permissionModel.hasPermission(app.getCurrentUser().getId(), ISRGroup.PERM_NEW_REQUEST);
%>

<c-rt:set var="forward_withdrawal_failed" value="<%=RequestorRequestListingTable.WITHDRAWAL_FAILED%>" />
<c:choose>
	<c:when test="${forward.name eq forward_withdrawal_failed }">
		<script type="text/javascript">
			alert("<fmt:message key='isr.message.withdrawalFailed'/>");
		</script>
	</c:when>
</c:choose>

<x:config>
	<page name="isrViewListing">
		<com.tms.collab.isr.ui.RequestorRequestListingTable name="requestorRequestListing"/>
		<com.tms.collab.isr.ui.AttendantRequestListingTable name="attendantRequestListing"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${!empty param.status }">
		<x:set name="isrViewListing.requestorRequestListing" property="status" value="${param.status }" />
	</c:when>
	<c:otherwise>
		<x:set name="isrViewListing.requestorRequestListing" property="status" value="0" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${!empty param.attendingStatus }">
		<x:set name="isrViewListing.attendantRequestListing" property="status" value="${param.attendingStatus }" />
	</c:when>
	<c:otherwise>
		<x:set name="isrViewListing.attendantRequestListing" property="status" value="0" />
	</c:otherwise>
</c:choose>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<c-rt:if test="<%= canCreateRequest%>">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.submittedRequests"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="isrViewListing.requestorRequestListing" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	</c-rt:if>
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><a name="attendingRequest"></a><fmt:message key="isr.label.attendingRequests"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="isrViewListing.attendantRequestListing" /></td>
	</tr>
</table>

<script type="text/javascript">
	<c:if test="${!empty param.status}">
		var submittedRequestFormObj = document.forms['isrViewListing.requestorRequestListing'];
		var submittedRequestStatusObj = submittedRequestFormObj.elements['isrViewListing.requestorRequestListing.filterForm.selectStatus'];
		submittedRequestStatusObj.selectedIndex = <c:out value="${widgets['isrViewListing.requestorRequestListing'].selectedStatusIndex}" />;
	</c:if>
	
	<c:if test="${!empty param.attendingStatus}">
		var attendantRequestFormObj = document.forms['isrViewListing.attendantRequestListing'];
		var attendantRequestStatusObj = attendantRequestFormObj.elements['isrViewListing.attendantRequestListing.filterForm.selectStatus'];
		attendantRequestStatusObj.selectedIndex = <c:out value="${widgets['isrViewListing.attendantRequestListing'].selectedStatusIndex}" />;
	</c:if>
</script>

<%@ include file="/ekms/includes/footer.jsp" %>