<%@include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>

<%
	Application app = Application.getInstance();
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	boolean canManageOrgChart = security.hasPermission(userId, "orgChart.permission.manage", null, null);
%>

<c-rt:if test="<%=!canManageOrgChart%>">
	<c:redirect url="/cmsadmin/error401.jsp" />
</c-rt:if>