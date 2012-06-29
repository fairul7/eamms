<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.permission.model.PermissionModel,
				kacang.Application" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT %>" />
<%@include file="includes/accessControl.jsp" %>

<% 
Application app = Application.getInstance();
PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
boolean canCreateRequest = permissionModel.hasPermission(app.getCurrentUser().getId(), ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT);
%>

<x:config>
	<page name="isr">
		<com.tms.collab.isr.ui.RequestorClosableRequestListingTable name="closableSubmittedRequest"/>
		<com.tms.collab.isr.ui.AttendantClosableRequestListingTable name="closableAttendingRequest"/>
    </page>
</x:config>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<c-rt:if test="<%= canCreateRequest%>">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.completedSubmittedRequests"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="isr.closableSubmittedRequest" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	</c-rt:if>
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.completedAttendingRequests"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="isr.closableAttendingRequest" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>