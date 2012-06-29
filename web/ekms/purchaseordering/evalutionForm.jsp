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

<%@ page import="com.tms.sam.po.ui.SupplierEvaluationForm"%>

<c-rt:set var="forwardBack" value="<%= SupplierEvaluationForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
	<script>
	document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/purchaseordering/viewSupplier.jsp?supplierID=<c:out value="${widgets['evaluationPg.EvaluationForm'].supplierID}"/>&count=<c:out value="${widgets['evaluationPg.EvaluationForm'].count}"/>&ppID=<c:out value="${widgets['evaluationPg.EvaluationForm'].ppID}"/>";
	</script>
       
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="evaluationPg">
		<com.tms.sam.po.ui.SupplierEvaluationForm name="EvaluationForm"/>
	</page>
</x:config>

<c:if test="${!empty param.supplierID}">
	<x:set name="evaluationPg.EvaluationForm" property="supplierID" value="${param.supplierID}" />
</c:if>
<c:if test="${!empty param.ppID}">
	<x:set name="evaluationPg.EvaluationForm" property="ppID" value="${param.ppID}" />
</c:if>
<link href="styles/rating.css" rel="stylesheet" type="text/css" media="all"/>

<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="evaluationPg.EvaluationForm"/>
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>