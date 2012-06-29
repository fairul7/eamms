<%@ page import="java.util.Collection,
				 java.util.ArrayList,
                 kacang.Application,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator,
				 com.tms.sam.po.permission.model.PermissionModel,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>
                 
<%
	Application app = Application.getInstance();
	Collection items = new ArrayList();
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	boolean isUserInActiveGroup = permissionModel.isUserInActiveGroup(userId);
	boolean isHOD = permissionModel.isHOD(userId);
	boolean permission = security.hasPermission(userId, "com.tms.sam.po.ManagePermission", null, null);
	if(permission || isUserInActiveGroup)
    	items.add(new MenuItem(app.getMessage("po.label.po"), "/ekms/purchaseordering", null, null, "/ekms/images/menu/ic_myfolder.gif", null));
    
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
