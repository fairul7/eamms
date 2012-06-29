<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.setting.ui.GlobalSetting, com.tms.sam.po.permission.model.POGroup"%>

<!-- Check Access Control -->
<c-rt:set var="aclPermissionId" value="<%=POGroup.PERM_ACCESS_SETUP %>" />
<%@include file="includes/accessControl.jsp" %>

<c-rt:set var="forward_add" value="<%=GlobalSetting.FORWARD_SUCCESS%>" />
<c-rt:set var="forward_error" value="<%=GlobalSetting.FORWARD_ERROR%>" />
<c-rt:set var="forward_invalid_ext" value="<%=GlobalSetting.FORWARD_INVALID_EXT%>" />
<c-rt:set var="forward_invalid_file" value="<%=GlobalSetting.FORWARD_INVALID_File%>" />
<c:choose>

<c:when test="${forward.name eq forward_error}">
	<script type="text/javascript">
		alert('<fmt:message key='po.message.updateFailure'/>');
		document.location = "globalSettings.jsp";
	</script>
</c:when>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="globalSetting.jsp"/>
</c:when>
<c:when test="${forward.name eq forward_invalid_ext}">
	<script type="text/javascript">
		var alertMessage = "<fmt:message key='po.message.invalidFileExt'/>";
		<c:forEach var="invalidFileExt" items="${invalidFileExts}">
			alertMessage += "\n- " + "<c:out value='${invalidFileExt}'/>";
		</c:forEach>
		alert(alertMessage);
	</script>
</c:when>
<c:when test="${forward.name eq forward_add}">
	<script type="text/javascript">
		var alertMessage = "<fmt:message key='po.message.successfulUpdate'/>";
		alert(alertMessage);
	</script>
</c:when>
</c:choose>

<x:config>
	<page name="setting">
		<com.tms.sam.po.setting.ui.GlobalSetting name="global"/>
		
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>

		<x:display name="setting.global" />

<%@ include file="/ekms/includes/footer.jsp" %>