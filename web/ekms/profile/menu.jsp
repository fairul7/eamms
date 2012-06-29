<%@ page import="kacang.Application,
				 java.util.ArrayList,
				 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.MenuGenerator"%>
<%
	Application app = Application.getInstance();
	ArrayList items = new ArrayList();

	items.add(new MenuItem(app.getMessage("portlet.label.personalize"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("general.label.profile"), "/ekms/profile/profile.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("portlet.label.personalize"), "/ekms/profile/personalize.jsp", null, null, null, null));

	request.setAttribute(MenuGenerator.MENU_FILE, items);
%>