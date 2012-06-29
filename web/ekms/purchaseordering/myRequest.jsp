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
	
	if(!permissionModel.hasPermission(userID, POGroup.PERM_SUBMIT_NEW_REQUEST)){
%>

    <c:redirect url="noPermission.jsp"/>
<% } %>


<c:if test="${forward.name eq 'Add'}">
    <c:redirect url="prepurchaseRequestForm.jsp?flag=new" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="requestPg">
		<com.tms.sam.po.ui.MyRequestTable name="MyRequest"/>
	</page>
</x:config>

<c:set var="bodyTitle" scope="request"><fmt:message key="po.label.po"/> > <fmt:message key="myRequest.label.request"/></c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>


<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="requestPg.MyRequest"/>
		</td>
	</tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>