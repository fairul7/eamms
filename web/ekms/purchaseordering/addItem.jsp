<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.permission.model.POGroup, com.tms.sam.po.setting.ui.Item"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forwardBack" value="<%= Item.FORWARD_BACK %>" />
<c:if test="${forward.name==forwardBack}">
     <c:redirect url="itemListing.jsp" />
</c:if>
<c:if test="${forward.name=='cancel_form_action'}">
     <c:redirect url="itemListing.jsp" />
</c:if>

<c-rt:set var="forwardItem" value="<%= Item.FORWARD_ITEM %>" />
<c:if test="${forward.name==forwardItem}">
     <script>
     	alert('<fmt:message key='po.message.itemAdded'/>');
     	document.location ="itemListing.jsp";
     </script>
</c:if>

<c:if test="${forward.name=='itemCodeDuplicate'}">
     <script>
     alert('<fmt:message key='po.message.itemDuplicated'/>');
     </script>
</c:if>

<x:config>
	<page name="setting">
		<com.tms.sam.po.setting.ui.ItemAdd name="AddItem"/>
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.label.addItem"/></td>
	</tr>
	<tr>
		<td><x:display name="setting.AddItem" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>