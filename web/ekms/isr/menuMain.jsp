<%@ page import="java.util.Collection,
				 java.util.ArrayList,
				 com.tms.collab.isr.permission.model.PermissionModel,
                 kacang.Application,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>
<%
	Application app = Application.getInstance();
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	Collection items = new ArrayList();
	boolean isUserInActiveGroup = permissionModel.isUserInActiveGroup(userId);
	boolean systemLevelPermissionAccessibility = security.hasPermission(userId, "isr.permission.managePermission", null, null);
	
	if(isUserInActiveGroup || systemLevelPermissionAccessibility)
		items.add(new MenuItem(app.getMessage("isr.module.name", "Internal Service Request"), "/ekms/isr/", null, null, "/ekms/images/menu/ic_isr.gif", null));
    
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
