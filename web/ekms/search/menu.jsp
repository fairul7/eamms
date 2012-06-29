<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	ArrayList items = new ArrayList();
    //Generating search menu
    items.add(new MenuItem(app.getMessage("general.label.search"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("general.label.newSearch"), "/ekms/search/search.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("searchprofile.label.title"), "/ekms/search/searchProfile.jsp", null, null, null, null));
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>