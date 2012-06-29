<%@ page import="com.tms.crm.helpdesk.ui.IncidentTable"%>
<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
	<page name="alertNotification">
		<com.tms.crm.helpdesk.ui.AlertNotificationSetup name="setup"/>
	</page>
</x:config>

<c:if test="${forward.name == 'false'}">
	<script>
		alert('You must choose at least one alert method!');
	</script>
</c:if>

<c:if test="${forward.name == 'added'}">
	<script>
		alert('Alert Notification setting has been added!');
	</script>
</c:if>

<c:if test="${forward.name == 'updated'}">
	<script>
		alert('Alert Notification setting has been updated!');
	</script>
</c:if>

<c:if test="${forward.name == 'invalidOcc'}">
	<script>
		alert('Alert occurance cannot be a negative number!');
	</script>
</c:if>

<c:if test="${forward.name == 'invalidFa'}">
	<script>
		alert('First alert value cannot be a negative number');
	</script>
</c:if>

<c:if test="${forward.name == 'invalidSa'}">
	<script>
		alert('Subsequent alert value cannot be a negative number');
	</script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="003366" class="contentTitleFont"><b><font color="FFCF63" class="contentTitleFont">&nbsp;&nbsp;&nbsp;<fmt:message key="helpdesk.label.alert.setup"/></font></b></td>
        <td align="right" bgcolor="003366" class="contentTitleFont"></td>
    </tr>
    <tr><td colspan="2" valign="top" bgcolor="EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="top" bgcolor="CCCCCC" class="contentBgColor"><x:display name="alertNotification.setup"/></td></tr>
    <tr><td colspan="2" valign="top" bgcolor="CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>
