<%@ page import="kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 com.tms.sam.po.permission.model.PermissionModel,
                 com.tms.sam.po.permission.model.POGroup,
                 java.util.Collection"%>
<%@include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	
	String userID = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
	
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	// Determine permisson
	
	if(permissionModel.hasPermission(userID, POGroup.PERM_SUBMIT_NEW_REQUEST)){
%>

    <c:redirect url="myRequest.jsp"/>
<% }else if(service.hasPermission(userID, "com.tms.sam.po.ManagePermission", null, null)) {%>
	<c:redirect url="permission.jsp"/>
<% }else if(permissionModel.hasPermission(userID, POGroup.PERM_APPROVE_BUDGET)){%>
	<c:redirect url="requestListing.jsp"/>
<% }else if(permissionModel.hasPermission(userID, POGroup.PERM_MANAGE_QUOTATION)){%>
	<c:redirect url="purchaseRequestListing.jsp"/>
<% }%>
