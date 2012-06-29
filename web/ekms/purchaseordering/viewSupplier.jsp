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

<%@ page import="com.tms.sam.po.ui.ViewSupplierForm"%>

<c-rt:set var="forwardBack" value="<%= ViewSupplierForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="supplierListing.jsp" />
</c:if>



<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewSupplierPg">
		<com.tms.sam.po.ui.ViewSupplierForm name="ViewSupplier"/>
	</page>
</x:config>

<c:if test="${!empty param.supplierID}">
	<x:set name="viewSupplierPg.ViewSupplier" property="supplierID" value="${param.supplierID}" />
</c:if>

<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="viewSupplierPg.ViewSupplier"/>
		</td>
	</tr>
</table>


<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/purchaseordering/supplierListing.jsp";
		return false;
	}
	
	function evaluatefunc() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/purchaseordering/evalutionForm.jsp?supplierID=<c:out value="${widgets['viewSupplierPg.ViewSupplier'].supplierID}"/>&count=<c:out value="${widgets['viewSupplierPg.ViewSupplier'].count}"/>&ppID=<c:out value="${widgets['viewSupplierPg.ViewSupplier'].ppID}"/>";
		return false;
	}
</script>

<%@include file="/ekms/includes/footer.jsp" %>