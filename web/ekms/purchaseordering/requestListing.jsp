<%@include file="/common/header.jsp"%>

<!-- Check Access Control -->


<c:if test="${!empty param.ppID}">
	<c:redirect url="checkStatusBO.jsp?id=${param.ppID}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="listingRequestPg">
		<com.tms.sam.po.ui.RequestListing name="listingRequest"/>
	</page>
</x:config>

<c:set var="bodyTitle" scope="request"><fmt:message key="po.label.po"/> > <fmt:message key="budget.label.title"/></c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>


<table width="100%">
	<tr>
		<td width="100%" style="vertical-align:top">
			<x:display name="listingRequestPg.listingRequest"/>
		</td>
	</tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>