<%@include file="/common/header.jsp"%>
<%@ page import="com.tms.sam.po.permission.ui.PermissionTable"%>
<x:permission var="isAuthorized" module="com.tms.sam.po.model.PrePurchaseModule" permission="com.tms.sam.po.ManagePermission"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<c-rt:set var="forward_add" value="<%=PermissionTable.FORWARD_ADD%>" />
<c-rt:set var="forward_print" value="<%=PermissionTable.FORWARD_PRINT%>" />
<c:if test="${forward.name eq forward_add}">
	<c:redirect url="addGroup.jsp" />
</c:if>

<c:if test="${forward.name eq forward_print}">
<script>

var myWin = window.open('printPermission.jsp','print','menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width=600,height=800,left=50,top=50');
  if (myWin != null) {
    myWin.focus();
  }
</script>
</c:if>

<x:config>
	<page name="permission">
		<com.tms.sam.po.permission.ui.PermissionTable name="Table"/>
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  <tr valign="top">
  	<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.menu.setups"/> > <fmt:message key="po.permission.listing"/></td>
  </tr>
  <tr>
    <td class="contentBgColor">
		<x:display name="permission.Table" />
	</td>
  </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>