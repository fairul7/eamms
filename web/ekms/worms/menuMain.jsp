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

    //Generating Project Menu
    if(service.hasPermission(userId, "com.tms.worms.project.Project.view", "com.tms.worms.WormsHandler", null)) {
        items.add(new MenuItem(app.getMessage("general.label.projects"), "/ekms/worms", null, null, "/ekms/images/menu/ic_project.gif", null));
    }

    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
