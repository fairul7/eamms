<%@include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>
                 
<%@include file="/ekms/includes/header.jsp" %>

<%
	Application app = Application.getInstance();
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	boolean canManageOrgChart = security.hasPermission(userId, "orgChart.permission.manage", null, null);
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.requiredSetupMissing"/></td>
	</tr>
	<tr>
		<td class="contentBgColor" style="padding:5px;">
			Internal Service Request (ISR) module requires proper setup of your account at 
			<c-rt:choose>
			<c-rt:when test="<%=canManageOrgChart%>">
				<a href="/ekms/orgChart/">Hierachy Management of Organization Chart</a>.
			</c-rt:when>
			<c-rt:otherwise>
				Hierachy Management of Organization Chart.
			</c-rt:otherwise>
			</c-rt:choose>
			<br />
			<br />Please consult your System Administrator or relevant authority to perform relevant setup accordingly, before you can be granted access to ISR.
		</td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>