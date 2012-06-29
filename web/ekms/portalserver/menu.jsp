<%@ page import="java.util.ArrayList,
				 kacang.services.security.User,
				 kacang.services.security.SecurityService,
				 kacang.Application,
				 kacang.ui.menu.MenuGenerator,
				 com.tms.portlet.PortletHandler,
				 kacang.ui.menu.MenuItem"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determine permissions
	boolean portletView = service.hasPermission(userId, PortletHandler.PERMISSION_PORTLET_VIEW, null, null);
	boolean themeView = service.hasPermission(userId, PortletHandler.PERMISSION_THEME_VIEW, null, null);
    //Generating menu
	items.add(new MenuItem(app.getMessage("portlet.label.personalize"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("portlet.label.personalize"), "/ekms/portalserver/personalize.jsp", null, null, null, null));
	if(portletView || themeView)
		items.add(new MenuItem(app.getMessage("portlet.label.setup"), null, null, null, null, null));
    if(portletView)
		items.add(new MenuItem(app.getMessage("portlet.message.portletRegistry"), "/ekms/portalserver/portlets.jsp", null, null, null, null));
	if(themeView)
		items.add(new MenuItem(app.getMessage("portlet.label.themesRegistry"), "/ekms/portalserver/themes.jsp", null, null, null, null));

	request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
