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
	
	if(!permissionModel.hasPermission(userID, POGroup.PERM_MANAGE_QUOTATION)){
%>

    <c:redirect url="noPermission.jsp"/>
<% } %>

<%@ page import="com.tms.sam.po.ui.SupplierListing, 
                com.tms.sam.po.ui.SupplierListingForm"%>

<c-rt:set var="forwardAdd" value="<%= SupplierListing.FORWARD_ADD %>" />

<c:if test="${forward.name eq forwardAdd}">
    <c:redirect url="addSupplier.jsp?flag=new&index=${forwardAdd}" />
</c:if>

<c-rt:set var="forwardBack" value="<%= SupplierListingForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="purchaseRequestListing.jsp" />
</c:if>

<c-rt:set var="forwardSAdd" value="<%= SupplierListingForm.FORWARD_SUPPLIER_ITEM%>" />
<c:if test="${forward.name eq forwardSAdd}">
    <c:redirect url="addSupplierToItem.jsp?flag=new1&id=${widgets['supplierListPg.supplierListForm'].ppID}" />
</c:if>

<c-rt:set var="forwardGenerate" value="<%= SupplierListingForm.FORWARD_GENERATE%>" />
<c:if test="${forward.name eq forwardGenerate}">
    <c:redirect url="/ekms/po/generateRFQ" />
</c:if>

<c-rt:set var="forwardError" value="<%= SupplierListingForm.FORWARD_ERROR%>" />
<c:if test="${forward.name eq forwardError}">
   <script>
   	alert("Please add at least one supplier before proceed.");
   </script>
</c:if>

<c-rt:set var="forwardDelete" value="<%= SupplierListing.FORWARD_DELETE%>" />
<c:if test="${forward.name eq forwardDelete}">
   	 <c:redirect url="supplierListing.jsp?flag=new1&id=${widgets['supplierListPg.supplierListForm'].ppID}" />
</c:if>
<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="supplierListPg">
	    <com.tms.sam.po.ui.SupplierListingForm name="supplierListForm"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="supplierListPg.supplierListForm" property="ppID" value="${param.id}" />
</c:if>

<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="supplierListPg.supplierListForm"/>
		</td>
	</tr>
</table>


<%@include file="/ekms/includes/footer.jsp" %>