<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.PrePurchaseApprovalForm,
                kacang.Application,
				kacang.services.security.SecurityService,
				com.tms.sam.po.model.PrePurchaseModule,
				com.tms.sam.po.permission.model.PermissionModel"
%>

<c-rt:set var="forwardBack" value="<%= PrePurchaseApprovalForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="userRequest.jsp" />
</c:if>

<%
	Application app = Application.getInstance();
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userID = service.getCurrentUser(request).getId();
	
	if (permissionModel.isHOD(userID)) {
		request.setAttribute("isAccessible", "true");
	}
	else {
		request.setAttribute("isAccessible", "false");
	}
%>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="/cmsadmin/error401.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="preApprovalPg">
		<com.tms.sam.po.ui.PrePurchaseApprovalForm name="preApproval"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="preApprovalPg.preApproval" property="ppID" value="${param.id}" />
</c:if>

<%
	
	PrePurchaseModule pModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
	
	String status = pModule.getStatus(request.getParameter("id"));
	
	if (status.equals("New") || status.equals("Resubmit")) {
%>
	<x:display name="preApprovalPg.preApproval"/>
<%
	}
	else {
%>
	<script>
	window.location="viewUserRequest.jsp?id=${param.id}";
	</script>
	
<%
	}

%>

<%@include file="/ekms/includes/footer.jsp" %>