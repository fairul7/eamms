<%@include file="/common/header.jsp"%>
<%@ page import="com.tms.sam.po.ui.PurchaseOrder"%>
<c-rt:set var="forwardBack" value="<%= PurchaseOrder.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="viewRequestBO.jsp?status=PO&id=${widgets['order.purchaseOrder'].ppID}"/>
</c:if>

<c:if test="${forward.name eq 'cancel_form_action'}">
    <c:redirect url="viewRequestBO.jsp?status=PO&id=${widgets['order.purchaseOrder'].ppID}"/>
</c:if>

<x:config>
	<page name="order">
		<com.tms.sam.po.ui.PurchaseOrder name="purchaseOrder"/>
	</page>
</x:config>

<c:if test="${!empty param.ppID}">
	<x:set name="order.purchaseOrder" property="ppID" value="${param.ppID}" />
</c:if>
<c:if test="${!empty param.supplierID}">
	<x:set name="order.purchaseOrder" property="supplierID" value="${param.supplierID}" />
</c:if>
<c:if test="${!empty param.count}">
	<x:set name="order.purchaseOrder" property="stringCount" value="${param.count}" />
</c:if>
<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  
  <tr>
    <td>
		<x:display name="order.purchaseOrder" />
	</td>
  </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>