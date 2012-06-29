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

<%@ page import="com.tms.sam.po.ui.ViewOnlySupplierForm"%>

<c-rt:set var="forwardEvaluate" value="<%= ViewOnlySupplierForm.FORWARD_EVALUATE %>" />

<c:if test="${forward.name eq forwardEvaluate}">
     <script>
      <c:redirect url="viewOnlyEvaluationForm.jsp" />
     </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/purchaseordering/viewRequestPO.jsp";
		return false;
	}
</script>

<x:config>
	<page name="viewOnlySupplierPg">
		<com.tms.sam.po.ui.ViewOnlySupplierForm name="ViewOnlySupplier"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewOnlySupplierPg.ViewOnlySupplier" property="supplierID" value="${param.id}" />
</c:if>

<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="viewOnlySupplierPg.ViewOnlySupplier"/>
		</td>
	</tr>
</table>


<%@include file="/ekms/includes/footer.jsp" %>