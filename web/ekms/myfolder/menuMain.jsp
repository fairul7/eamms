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
    //boolean isUser = service.hasPermission(userId, "com.tms.hr.claim.model.Access", null, null);
    //boolean isAdmin = service.hasPermission(userId, "com.tms.hr.claim.model.Admin", null, null);
    //Generating HR Menu
    //if(isUser || isAdmin) {
        items.add(new MenuItem(app.getMessage("mf.label.myFolder"), "/ekms/myfolder", null, null, "/ekms/images/menu/ic_myfolder.gif", null));
    //}


    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
