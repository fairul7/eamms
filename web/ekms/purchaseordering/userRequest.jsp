<%@ page import="com.tms.sam.po.ui.PrePurchaseApprovalForm,
                kacang.Application,
				kacang.services.security.SecurityService,
				com.tms.sam.po.model.PrePurchaseModule,
				com.tms.sam.po.permission.model.PermissionModel"
%>

<%@include file="/common/header.jsp"%>

<c:if test="${!empty param.ppID}">
	<c:redirect url="checkStatus.jsp?id=${param.ppID}"/>
</c:if>

<c:set var="id" value = "${param.ppID}">
</c:set>
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
	<page name="userRequestPg">
		<com.tms.sam.po.ui.UserRequestListing name="userRequest"/>
	</page>
</x:config>

<c:set var="bodyTitle" scope="request"><fmt:message key="po.label.po"/> > <fmt:message key="userRequest.label.hodApproval"/></c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>


<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="userRequestPg.userRequest"/>
		</td>
	</tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>