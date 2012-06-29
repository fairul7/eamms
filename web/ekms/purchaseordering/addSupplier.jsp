<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.AddSupplierForm"%>


<c-rt:set var="forwardBack" value="<%= AddSupplierForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="supplierListing.jsp" />
</c:if>
<c:if test="${forward.name eq 'cancel_form_action'}">
    <c:redirect url="supplierListing.jsp" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="suppPg">
		<com.tms.sam.po.ui.AddSupplierForm name="AddSupplier"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="suppPg.AddSupplier" property="ppID" value="${param.id}" />
</c:if>

<table width="100%">
	<tr>
		<td width="80%" style="vertical-align:top">
			<x:display name="suppPg.AddSupplier"/>
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>