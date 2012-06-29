<%@include file="/common/header.jsp"%>
<x:permission var="isAuthorized" module="com.tms.sam.po.model.PrePurchaseModule" permission="com.tms.sam.po.PurchaseOfficer"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<x:config>
	<page name="quotationPg">
		<com.tms.sam.po.ui.PrintQuotationForm name="Quotation"/>
	</page>
</x:config>

<table width="100%">
	<tr>
		<td width="80%" style="vertical-align:top">
			<x:display name="quotationPg.Quotation"/>
		</td>
	</tr>
</table>


<jsp:include page="includes/footer.jsp" flush="true"/>
