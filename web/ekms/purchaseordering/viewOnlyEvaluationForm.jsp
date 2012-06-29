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
	
	if(!permissionModel.hasPermission(userID, POGroup.PERM_APPROVE_BUDGET)){
%>

    <c:redirect url="noPermission.jsp"/>
<% } %>

<%@ page import="com.tms.sam.po.ui.ViewOnlySupplierEvaluationForm"%>

<c-rt:set var="forwardBack" value="<%= ViewOnlySupplierEvaluationForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="supplierRatingReport.jsp" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewEvaluationPg">
		<com.tms.sam.po.ui.ViewOnlySupplierEvaluationForm name="ViewEvaluationForm"/>
	</page>
</x:config>


<link href="styles/rating.css" rel="stylesheet" type="text/css" media="all"/>

<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="viewEvaluationPg.ViewEvaluationForm"/>
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>