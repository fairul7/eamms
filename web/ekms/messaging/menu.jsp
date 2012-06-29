<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.crm.helpdesk.HelpdeskHandler,
                 com.tms.collab.messaging.model.Util,
                 com.tms.collab.messaging.model.Folder"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	ArrayList items = new ArrayList();
    //Determining permissions

    //Generating messaging menu
    items.add(new MenuItem(app.getMessage("messaging.label.messaging"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("messaging.label.composeMessage"), "/ekms/messaging/composeMessage.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("messaging.label.downloadPOP3Email"), "/ekms/messaging/checkEmail.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("messaging.label.options"), "/ekms/messaging/options.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("messaging.label.status"), "javascript:openStatus()", null, null, null, null));

    //Generating folders menu
    items.add(new MenuItem(app.getMessage("messaging.label.folders"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("messaging.label.folders"), null, null, "/ekms/messaging/includes/folderTree.jsp", null, null));

    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>


