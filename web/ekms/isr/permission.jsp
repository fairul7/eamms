<%@page import="com.tms.collab.isr.permission.model.ISRGroup,
				com.tms.collab.isr.permission.ui.PermissionTable" %>
<%@include file="/common/header.jsp" %>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=ISRGroup.PERM_ACCESS_GROUP_PERMISSION %>" />
<c:set var="aclIsPermissionPage" value="${true}" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forward_add" value="<%=PermissionTable.FORWARD_ADD%>" />
<c-rt:set var="forward_print" value="<%=PermissionTable.FORWARD_PRINT%>" />

<c:if test="${forward.name eq forward_add}">
	<c:redirect url="addGroup.jsp" />
</c:if>
<c:if test="${forward.name eq forward_print}">
<script>

var myWin = window.open('permissionPrint.jsp','print','menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width=600,height=800,left=50,top=50');
  if (myWin != null) {
    myWin.focus();
  }
</script>
</c:if>

<x:config>
	<page name="permission">
		<com.tms.collab.isr.permission.ui.PermissionTable name="permissionTable"/>
    </page>
</x:config>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="5" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont"><fmt:message key="isr.label.groupListing"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="permission.permissionTable" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>