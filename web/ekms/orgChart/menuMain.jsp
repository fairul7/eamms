<%@ page import="java.util.Collection,
				 java.util.ArrayList,
                 kacang.Application,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>
<%
	Application app = Application.getInstance();
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	Collection items = new ArrayList();
	boolean canManageOrgChart = security.hasPermission(userId, "orgChart.permission.manage", null, null);
	
	if(canManageOrgChart) {
		items.add(new MenuItem(app.getMessage("orgChart.menu.label.orgChart", "Organization Chart"), "/ekms/orgChart/", null, null, "/ekms/images/menu/ic_chart.gif", null));
	}
	
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
