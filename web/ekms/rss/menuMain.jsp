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

 	boolean managerRss = security.hasPermission(userId, "com.tms.collab.rss.managerRss", null, null);
    boolean normalAccessRss = security.hasPermission(userId, "com.tms.collab.rss.normalAccessRss", null, null);
    
    //System.out.println("isAdmin="+isAdmin);
    //System.out.println("isUser="+isUser);
        
	if (managerRss){
        items.add(new MenuItem(app.getMessage("rss.channel.rss"), "/ekms/rss/channelListing.jsp", null, null, "/ekms/images/menu/ic_myfolder.gif", null));
	} else if (normalAccessRss){
        items.add(new MenuItem(app.getMessage("rss.channel.rss"), "/ekms/rss/rssListing.jsp", null, null, "/ekms/images/menu/ic_myfolder.gif", null));
	}

    
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
