<%@include file="/common/header.jsp"%>
<x:config>
	<page name="purchase">
		<com.tms.sam.po.ui.ViewCompleteOrderRecord name="completeOrder"/>
	</page>
</x:config>

<c:if test="${!empty param.ppID}">
	<x:set name="purchase.completeOrder" property="ppID" value="${param.ppID}" />
</c:if>
<c:if test="${!empty param.supplierID}">
	<x:set name="purchase.completeOrder" property="supplierID" value="${param.supplierID}" />
</c:if>
<c:if test="${!empty param.count}">
	<x:set name="purchase.completeOrder" property="stringCount" value="${param.count}" />
</c:if>
<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  
  <tr>
    <td>
		<x:display name="purchase.completeOrder" />
	</td>
  </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>