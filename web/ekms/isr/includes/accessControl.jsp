<%@ page import="com.tms.collab.isr.permission.model.PermissionModel,
                 kacang.Application,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>
<%@include file="/common/header.jsp" %>

<%
	boolean isPermitted = false;
	if(pageContext.findAttribute("aclPermissionId") != null) {
		if(!"".equals(pageContext.findAttribute("aclPermissionId").toString())) {
			String permission = pageContext.findAttribute("aclPermissionId").toString();
			boolean aclIsPermissionPage = false;
			
			if(pageContext.findAttribute("aclIsPermissionPage") != null) {
				if(Boolean.valueOf(pageContext.findAttribute("aclIsPermissionPage").toString()).booleanValue()) {
					aclIsPermissionPage = true;
				}
			}
			
			Application app = Application.getInstance();
			PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
			SecurityService security = (SecurityService) app.getService(SecurityService.class);
			String userId = security.getCurrentUser(request).getId();
			
			if(permissionModel.hasPermission(userId, permission)) {
				isPermitted = true;
			}
			else {
				if(aclIsPermissionPage) {
					if(security.hasPermission(userId, "isr.permission.managePermission", null, null)) {
						isPermitted = true;
					}
				}
			}
		}
	}
%>

<c-rt:if test="<%=!isPermitted%>">
	<c:redirect url="/cmsadmin/error401.jsp" />
</c-rt:if>