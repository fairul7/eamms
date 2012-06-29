<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.setting.ui.CurrencyAdd,com.tms.sam.po.permission.model.POGroup"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>

<c:if test="${!empty param.currencyID}">
	<c:redirect url="editCurrency.jsp?id=${param.currencyID}"/>
</c:if>

<%@ page import="com.tms.sam.po.setting.ui.CurrencyListing"%>

<x:permission var="isAuthorized" module="com.tms.sam.po.model.PrePurchaseModule" permission="com.tms.sam.po.ManagePermission"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<c:if test="${forward.name eq 'Add'}">
    <c:redirect url="addCurrency.jsp" />
</c:if>

<x:config>
	<page name="setting">
		<com.tms.sam.po.setting.ui.CurrencyListing name="ListCurrency"/>
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  <tr valign="top">
  	<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.menu.setups"/> > <fmt:message key="po.label.listCurrency"/></td>
  </tr>
  <tr>
    <td class="contentBgColor">
		<x:display name="setting.ListCurrency" />
	</td>
  </tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>