<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.ViewRequestForm"%>

<c-rt:set var="forwardBack" value="<%= ViewRequestForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="purchaseRequestListing.jsp" />
</c:if>
<c-rt:set var="forwardPO" value="<%= ViewRequestForm.FORWARD_PO %>" />
<c:if test="${forward.name eq forwardPO}">
    <c:redirect url="purchaseOrderListing.jsp" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewURequestPOPg">
		<com.tms.sam.po.ui.ViewRequestForm name="ViewUserRequestPO"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewURequestPOPg.ViewUserRequestPO" property="ppID" value="${param.id}" />
</c:if>

<x:display name="viewURequestPOPg.ViewUserRequestPO"/>


   

<%@include file="/ekms/includes/footer.jsp" %>