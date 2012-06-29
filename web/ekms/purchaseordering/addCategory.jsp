<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.setting.ui.ItemCategoryAdd,com.tms.sam.po.permission.model.POGroup"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>


<c-rt:set var="forwardBack" value="<%= ItemCategoryAdd.FORWARD_BACK %>" />
<c:if test="${forward.name==forwardBack}">
    <script type="text/javascript">
		alert('<fmt:message key='po.message.categoryUpdated'/>');
		document.location = "categoryListing.jsp";
	</script>
</c:if>
<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="categoryListing.jsp" />
</c:if>

<c-rt:set var="forwardCategory" value="<%= ItemCategoryAdd.FORWARD_CATEGORY %>" />

<c:if test="${forward.name==forwardCategory}">
    <script type="text/javascript">
		alert('<fmt:message key='po.message.categoryUpdatedExist'/>');
	</script>
</c:if>

<x:config>
	<page name="setting">
		<com.tms.sam.po.setting.ui.ItemCategoryAdd name="AddCategory"/>
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.label.addCategory"/></td>
	</tr>
	<tr>
		<td><x:display name="setting.AddCategory" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>