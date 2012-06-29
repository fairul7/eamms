<%@ page import="java.util.Collection,
				 java.util.ArrayList,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();

	//Determine permissions
	boolean manageDigest = service.hasPermission(userId, "com.tms.cms.digest.ManageDigest", null, null);
	Collection items = new ArrayList();
	if (manageDigest)
    {
    	//Generating Project Menu
    	items.add(new MenuItem(app.getMessage("com.tms.cms.digest.model.DigestModule"), "/ekms/digest", null, null, "/ekms/images/menu/ic_digest.gif", null));
    }
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>