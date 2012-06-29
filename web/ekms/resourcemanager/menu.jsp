<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 com.tms.collab.resourcemanager.model.ResourceManager"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determine permissions
	boolean resourceAdd = service.hasPermission(userId, ResourceManager.PERMISSION_ADD_RESOURCE, null, null);
    //Generating resource menu
    items.add(new MenuItem(app.getMessage("general.label.resourceManager"), null, null, null, null, null));
    if (resourceAdd) {
        items.add(new MenuItem(app.getMessage("resourcemanager.label.newResource"), "/ekms/resourcemanager/addresourceform.jsp", null, null, null, null));
    }
    items.add(new MenuItem(app.getMessage("resourcemanager.label.resourceListing"), "/ekms/resourcemanager/resourcemanager.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("resourcemanager.label.resourceUsage"), "/ekms/resourcemanager/resourceUsageReport.jsp", null, null, null, null));
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>