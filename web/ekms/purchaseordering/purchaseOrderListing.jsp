<%@include file="/common/header.jsp"%>

<x:config>
	<page name="order">
		<com.tms.sam.po.ui.PurchaseOrderListing name="purchaseOrderListing"/>
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  
  <tr>
    <td class="contentBgColor">
		<x:display name="order.purchaseOrderListing" />
	</td>
  </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>