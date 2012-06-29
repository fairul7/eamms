<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 com.tms.sam.po.permission.model.PermissionModel,
                 com.tms.sam.po.permission.model.POGroup,
                 com.tms.sam.po.model.PrePurchaseModule,
                 java.util.Collection"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	PrePurchaseModule module = (PrePurchaseModule) app.getModule(PrePurchaseModule.class);
	int newTotal = module.countRequest("New");
	int poTotal = module.countRequest("po");
	int boTotal = module.countRequest("Quoted");
	String userID = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
	
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	// Determine permisson
	
	
	boolean managerRss = service.hasPermission(userID, "com.tms.collab.rss.managerRss", null, null);
    boolean normalAccessRss = service.hasPermission(userID, "com.tms.collab.rss.normalAccessRss", null, null);
	
	items.add(new MenuItem(app.getMessage("rss.channel.rss"), null, null, null, null, null));
	//items.add(new MenuItem(app.getMessage("rss.channel.channelListing"), "/ekms/rss/channelListing.jsp", null, null, null, null));
	//items.add(new MenuItem(app.getMessage("rss.channel.rssListing"), "/ekms/rssListing.jsp", null, null, null, null));
	
    if(managerRss && normalAccessRss) {  
    	items.add(new MenuItem(app.getMessage("rss.channel.channelListing"), "/ekms/rss/channelListing.jsp", null, null, null, null));
    	items.add(new MenuItem(app.getMessage("rss.channel.rssListing"), "/ekms/rss/rssListing.jsp", null, null, null, null));
    } else if (managerRss){
        items.add(new MenuItem(app.getMessage("rss.channel.channelListing", "RSS Listing"), "/ekms/rss/rssListing.jsp", null, null, null, null));	      
    } else if (normalAccessRss){
        items.add(new MenuItem(app.getMessage("rss.channel.rssListing", "RSS Listing"), "/ekms/rss/rssListing.jsp", null, null, null, null));	      
    }
    
    
	
	request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
