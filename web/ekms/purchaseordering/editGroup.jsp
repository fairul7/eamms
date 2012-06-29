<%@include file="/common/header.jsp"%>
<%@ page import="com.tms.sam.po.permission.ui.PermissionForm"%>
<x:permission var="isAuthorized" module="com.tms.sam.po.model.PrePurchaseModule" permission="com.tms.sam.po.ManagePermission"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<c-rt:set var="forward_add" value="<%=PermissionForm.FORWARD_SUCCESS%>" />
<c-rt:set var="forward_error" value="<%=PermissionForm.FORWARD_ERROR%>" />

<c:choose>
<c:when test="${forward.name eq forward_add}">
	<script type="text/javascript">
		document.location = "permission.jsp";
	</script>
</c:when>
<c:when test="${forward.name eq forward_error}">
	<script type="text/javascript">
		document.location = "permission.jsp";
	</script>
</c:when>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="permission.jsp"/>
</c:when>
</c:choose>

<x:config>
	<page name="permission">
		<com.tms.sam.po.permission.ui.EditPermission name="edit"/>
	</page>
</x:config>

<c:if test="${!empty param.id }">
	<x:set name="permission.edit" property="groupId" value="${param.id }" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  <tr valign="top">
  	<td class="contentTitleFont" style="padding:5px;"><fmt:message key="po.label.setupGroupPermission"/></td>
  </tr>
  <tr>
    <td class="contentBgColor">
		<x:display name="permission.edit" />
	</td>
  </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>