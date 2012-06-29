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

    //Generating Helpdesk Menu
    boolean isHelpdeskUser = service.hasPermission(userId, "com.tms.crm.helpdesk.User", "com.tms.helpdesk.HelpdeskHandler", null);
    boolean isHelpdeskAdmin = service.hasPermission(userId, "com.tms.crm.helpdesk.Admin", "com.tms.helpdesk.HelpdeskHandler", null);
    if(isHelpdeskUser || isHelpdeskAdmin) {
        items.add(new MenuItem(app.getMessage("helpdesk.label.helpdesk"), "/ekms/helpdesk", null, null, "/ekms/images/menu/ic_helpdesk.gif", null));
    }

    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
