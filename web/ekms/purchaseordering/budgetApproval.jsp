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

<%@ page import="com.tms.sam.po.ui.BudgetApprovalForm"%>

<c:if test="${!empty param.supplierID}">
	<c:redirect url="viewQuotation.jsp?id=${param.supplierID}"/>
</c:if>

<c-rt:set var="forwardBack" value="<%= BudgetApprovalForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="requestListing.jsp" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="budgetApprovalPg">
		<com.tms.sam.po.ui.BudgetApprovalItemForm name="budgetItemApproval"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="budgetApprovalPg.budgetItemApproval" property="ppID" value="${param.id}" />
</c:if>

<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
		    <x:display name="budgetApprovalPg.budgetItemApproval"/>
		</td>
	</tr>
</table>


<%@include file="/ekms/includes/footer.jsp" %>