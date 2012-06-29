<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.ClosedPurchaseOrder"%>

<c-rt:set var="forwardBack" value="<%= ClosedPurchaseOrder.FORWARD_BACK %>" />
<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="viewRequestBO.jsp?status=PO&id=${widgets['order.purchaseOrder'].ppID}"/>
</c:if>


<x:config>
	<page name="purchaseOrder">
		<com.tms.sam.po.ui.ClosedPurchaseOrder name="cPO"/>
	</page>
</x:config>

<c:if test="${!empty widgets['order.purchaseOrder'].ppID}">
	<x:set name="purchaseOrder.cPO" property="ppID" value="${widgets['order.purchaseOrder'].ppID}" />
</c:if>

<c:if test="${!empty widgets['order.purchaseOrder'].supplierID}">
	<x:set name="purchaseOrder.cPO" property="supplierID" value="${widgets['order.purchaseOrder'].supplierID}" />
</c:if>

<c:if test="${!empty widgets['order.purchaseOrder'].count}">
	<x:set name="purchaseOrder.cPO" property="count" value="${widgets['order.purchaseOrder'].count}" />
</c:if>
<x:display name="purchaseOrder.cPO"/>
