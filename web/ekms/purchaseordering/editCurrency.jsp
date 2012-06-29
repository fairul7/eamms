<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.setting.ui.CurrencyAdd,com.tms.sam.po.permission.model.POGroup"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forwardBack" value="<%= CurrencyAdd.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="currencyListing.jsp" />
</c:if>

<c-rt:set var="forwardResult" value="<%= CurrencyAdd.FORWARD_RESULT %>" />
<c:if test="${forward.name== forwardResult}">
    <script type="text/javascript">
		alert('<fmt:message key='po.message.currencyUpdatedExist'/>');
	</script>
</c:if>

<c:if test="${forward.name eq 'cancel_form_action'}">
    <c:redirect url="currencyListing.jsp" />
</c:if>
<x:config>
	<page name="setting">
		<com.tms.sam.po.setting.ui.CurrencyEdit name="EditCurrency"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="setting.EditCurrency" property="currencyID" value="${param.id}" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.label.editCurrency"/></td>
	</tr>
	<tr>
		<td><x:display name="setting.EditCurrency" /></td>
	</tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>