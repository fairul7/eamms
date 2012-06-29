<%@ page import="com.tms.sam.po.ui.AddSupplierToItemForm"%>
<%@include file="/common/header.jsp"%>

<c-rt:set var="forwardCancel" value="<%= AddSupplierToItemForm.FORWARD_CANCEL%>" />
<c:if test="${forward.name eq forwardCancel}">
     <c:redirect url="supplierListing.jsp" />
</c:if>

<c:if test="${forward.name=='cancel_form_action'}">
     <c:redirect url="supplierListing.jsp" />
</c:if>

<x:config>
	<page name="item">
		<com.tms.sam.po.ui.AddSupplierToItemForm name="addSupplier"/>
	</page>
</x:config>


<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  
  <tr>
    <td class="contentBgColor">
		<x:display name="item.addSupplier" />
	</td>
  </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>