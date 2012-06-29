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
	boolean isPO = permissionModel.hasPermission(userID, POGroup.PERM_MANAGE_QUOTATION);
	boolean isBO = permissionModel.hasPermission(userID, POGroup.PERM_APPROVE_BUDGET);
	if(!isPO && !isBO){
%>

    <c:redirect url="noPermission.jsp"/>
<% } %>

<%@ page import="com.tms.sam.po.ui.ViewQuotationDetailsForm"%>

<c-rt:set var="forwardBack" value="<%= ViewQuotationDetailsForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="viewRequestBO.jsp?id=${widgets['viewQuotationPg.viewQuotation'].ppID}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewQuotationPg">
		<com.tms.sam.po.ui.ViewQuotationDetailsForm name="viewQuotation"/>
	</page>
</x:config>

<c:if test="${!empty param.supplierID}">
	<x:set name="viewQuotationPg.viewQuotation" property="supplierID" value="${param.supplierID}" />
</c:if>

<c:if test="${!empty param.ppID}">
	<x:set name="viewQuotationPg.viewQuotation" property="ppID" value="${param.ppID}" />
</c:if>

<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
		    <x:display name="viewQuotationPg.viewQuotation"/>
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>