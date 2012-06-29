<%@ page import="com.tms.fms.facility.ui.GlobalAssignmentForm"%>
<%@ include file="/common/header.jsp" %>

<!-- x:permission permission="com.tms.hr.competency.Competency.view" module="com.tms.hr.competency.CompetencyHandler" url="/ekms/index.jsp"/-->

<x:config>
    <page name="globalAssignment">
        <com.tms.fms.facility.ui.GlobalAssignmentForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forward_add_success" value="<%= GlobalAssignmentForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_edit_failed" value="<%= GlobalAssignmentForm.FORWARD_ADD_FAILED %>"/>
<c-rt:set var="forward_edit_success" value="<%= GlobalAssignmentForm.FORWARD_EDIT_SUCCESS %>"/>
<c-rt:set var="forward_setting_failed" value="<%= GlobalAssignmentForm.FORWARD_SETTING_FAILED %>"/>
<c-rt:set var="forward_setting_success" value="<%= GlobalAssignmentForm.FORWARD_SETTING_SUCCESS %>"/>
<c-rt:set var="forward_autosetting_failed" value="<%= GlobalAssignmentForm.FORWARD_AUTOSETTING_FAILED %>"/>
<c-rt:set var="forward_autosetting_success" value="<%= GlobalAssignmentForm.FORWARD_AUTOSETTING_SUCCESS %>"/>
<c-rt:set var="forward_schedule_success" value="<%= GlobalAssignmentForm.FORWARD_SCHEDULE_SUCCESS %>"/>
<c-rt:set var="forward_schedule_abw_success" value="<%= GlobalAssignmentForm.FORWARD_SCHEDULE_ABW_SUCCESS %>"/>

<c:if test="${forward.name == forward_add_success}">
    <script>
        alert("<fmt:message key='fms.facility.msg.globalAssignmentAdded'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>
<c:if test="${forward.name == forward_edit_success}">
    <script>
        alert("<fmt:message key='fms.facility.msg.globalAssignmentUpdated'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>

<c:if test="${forward.name == forward_setting_success}">
    <script>
        alert("<fmt:message key='fms.facility.assignment.successmsg'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>
<c:if test="${forward.name == forward_setting_failed}">
    <script>
        alert("<fmt:message key='fms.facility.assignment.failedmsg'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>

<c:if test="${forward.name == forward_autosetting_success}">
    <script>
        alert("<fmt:message key='fms.facility.autoassignment.successmsg'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>
<c:if test="${forward.name == forward_autosetting_failed}">
    <script>
        alert("<fmt:message key='fms.facility.autoassignment.failedmsg'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>

<c:if test="${forward.name == forward_schedule_success}">
    <script>
        alert("<fmt:message key='fms.facility.schedule.successmsg'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>

<c:if test="${forward.name == forward_schedule_abw_success}">
    <script>
        alert("<fmt:message key='fms.facility.schedule.abw.success'/>");
        document.location="<c:url value="globalAssignment.jsp"/>";
    </script>
</c:if>

<c:if test="${forward.name == 'invalid-field'}">
    <script>
        alert("<fmt:message key='fms.facility.msg.invalidValue'/>");
    </script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
			<b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.label.globalAssignmentListing'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="globalAssignment.form" /></td></tr>
    
	<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>
