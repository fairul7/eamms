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

    //Generating HR Menu
//	items.add(new MenuItem(app.getMessage("general.label.humanResource"), null, null, null, null, null));
    if(service.hasPermission(userId, "com.tms.cms.leave.Leaves", null, null)) {
        items.add(new MenuItem(app.getMessage("general.label.leaveApplication"), "/ekms/hrleave", null, null, "/ekms/images/menu/ic_leave.gif", null));
    }

    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
