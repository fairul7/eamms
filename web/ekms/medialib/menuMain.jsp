<%@ page import="java.util.Collection,
				 java.util.ArrayList,
                 kacang.Application,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator"%>
<%
	Application app = Application.getInstance();
	Collection items = new ArrayList();

    //Generating Project Menu
    items.add(new MenuItem(app.getMessage("medialib.label.mediaLibrary"), "/ekms/medialib", null, null, "/ekms/medialib/images/icon.gif", null));

    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>