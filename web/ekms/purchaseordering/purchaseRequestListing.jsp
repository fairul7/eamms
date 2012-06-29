<%@ page import="com.tms.sam.po.permission.model.POGroup"%>
<%@include file="/common/header.jsp"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_MANAGE_QUOTATION %>" />
<%@include file="includes/accessControl.jsp" %>

<c:if test="${!empty param.ppID}">
	<c:redirect url="checkStatusPO.jsp?id=${param.ppID}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="purchaseRequestPg">
		<com.tms.sam.po.ui.PurchaseRequestListing name="purchaseRequest"/>
	</page>
</x:config>

<c:set var="bodyTitle" scope="request"><fmt:message key="po.label.po"/> > <fmt:message key="quotation.label.request"/></c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>


<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="purchaseRequestPg.purchaseRequest"/>
		</td>
	</tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>