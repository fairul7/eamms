<%@ page import="com.tms.sam.po.ui.PrePurchaseRequestForm, com.tms.sam.po.permission.model.POGroup"%>
<%@include file="/common/header.jsp"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_SUBMIT_NEW_REQUEST %>" />
<%@include file="includes/accessControl.jsp" %>
<c:set var="w" value="${widget}" />



<c-rt:set var="forwardSubmit" value="<%= PrePurchaseRequestForm.FORWARD_SUBMIT %>" />
<c:if test="${forward.name eq forwardSubmit}">
    <c:redirect url="myRequest.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= PrePurchaseRequestForm.FORWARD_ERROR%>" />
<c:if test="${forward.name eq forwardError}">
   <script>
		alert("Please fill in the form correctly.");
	</script>
</c:if>

<c:if test="${forward.name eq 'cancel_form_action'}">
    <c:redirect url="myRequest.jsp" />
</c:if>

<c-rt:set var="forwardDelete" value="<%= PrePurchaseRequestForm.FORWARD_DELETE %>" />
<c:if test="${forward.name eq forwardDelete}">
    <c:redirect url="myRequest.jsp" />
</c:if>

<!--<c-rt:set var="forwardItem" value="<%= PrePurchaseRequestForm.FORWARD_ITEM %>" />
<c:if test="${forward.name eq forwardItem}">
    <script>
		alert();
	</script>
</c:if> 
-->
<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="mainPg">
		<com.tms.sam.po.ui.PrePurchaseRequestForm name="PrePurchase"/>
	</page>
</x:config>

<c:if test="${!empty param.ppID}">
	<x:set name="mainPg.PrePurchase" property="draftPPID" value="${param.ppID}" />
</c:if>

<table width="100%">
	<tr>
		<td width="80%" style="vertical-align:top">
			<x:display name="mainPg.PrePurchase"/>
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>