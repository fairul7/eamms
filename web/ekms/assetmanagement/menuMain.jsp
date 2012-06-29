<%@ page import="java.util.Collection,
				 java.util.ArrayList,
                 kacang.Application,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	Collection items = new ArrayList();
 	boolean isAdmin = service.hasPermission(userId, "com.tms.assetmanagement.manageAssetPermission", null, null);
    boolean isUser = service.hasPermission(userId, "com.tms.assetmanagement.accessAssetPermission", null, null);
        
    if(isUser ||isAdmin ) {
        items.add(new MenuItem(app.getMessage("asset.label.AssetManagement"), "/ekms/assetmanagement", null, null, "/ekms/assetmanagement/images/menu/ic_assets.gif", null));
    }
    
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
