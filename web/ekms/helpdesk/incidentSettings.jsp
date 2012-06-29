<%@ page import="com.tms.crm.helpdesk.ui.IncidentSettingsForm"%>
<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskAdminPermission.jsp"%>

<x:config>
	<page name="helpdeskincidentSettings">
		<com.tms.crm.helpdesk.ui.IncidentSettingsForm name="form" width="90%"/>
	</page>
</x:config>

<c-rt:set var="forward_success" value="<%= IncidentSettingsForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_failure" value="<%= IncidentSettingsForm.FORWARD_FAILURE %>"/>
<c-rt:set var="forward_cancel" value="<%= IncidentSettingsForm.CANCEL_FORM_ACTION %>"/>
<c:if test="${!empty forward.name}">
	<script>
		<c:choose>
			<c:when test="${forward_success == forward.name}">
				alert("<fmt:message key='helpdesk.message.incidentSettingsUpdated'/>");
			</c:when>
			<c:when test="${forward_failure == forward.name}">
				alert("<fmt:message key='helpdesk.message.incidentSettingsFailed'/>");
			</c:when>
		</c:choose>
	</script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" class="contentTitleFont"><b><font class="contentTitleFont">&nbsp;&nbsp;&nbsp;
            <fmt:message key="sfa.message.setup"/> > <fmt:message key='helpdesk.label.incidentSettings'/></font></b></td>
    </tr>
    <tr><td  valign="top" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr><td  valign="top" class="contentBgColor">
        &nbsp;&nbsp;<fmt:message key='helpdesk.message.separateOptions'/>
        <x:display name="helpdeskincidentSettings.form" />
    </td></tr>
<%--     <tr><td colspan="2" valign="top" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr> --%>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>