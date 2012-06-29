<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 com.tms.collab.directory.model.DirectoryModule"%>
<%@ include file="/common/header.jsp"%>
<x:template name="count" type="com.tms.collab.directory.ui.DirectoryPendingCount" body="custom">
    <c:set var="pendingContactCount" value="${count.pendingContactCount}"/>
    <c:set var="pendingCompanyCount" value="${count.pendingCompanyCount}"/>
</x:template>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determine permissions
	boolean manageContacts = service.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, null, null);
	boolean manageCompanies = service.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, null, null);
	boolean manageFolders = service.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_FOLDERS, null, null);
    //Generating business directory menu
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.businessDirectory"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.contacts"), "/ekms/addressbook/bdContactList.jsp?cn=bdFolderTree.tree&id=&companyId=", null, "/ekms/addressbook/includes/bdFolderTree.jsp", null, null));
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.registeredCompanies"), "/ekms/addressbook/bdCompanyList.jsp", null, null, null, null));
    if (manageContacts) {
        String pendingContactLabel = app.getMessage("addressbook.label.menu.pendingContacts") + " (" + pageContext.getAttribute("pendingContactCount") + ")";
        items.add(new MenuItem(pendingContactLabel, "/ekms/addressbook/bdContactApprovalList.jsp", null, null, null, null));
    }
    if (manageCompanies) {
        String pendingCompanyLabel = app.getMessage("addressbook.label.menu.pendingCompanies") + " (" + pageContext.getAttribute("pendingCompanyCount") + ")";
        items.add(new MenuItem(pendingCompanyLabel, "/ekms/addressbook/bdCompanyApprovalList.jsp", null, null, null, null));
    }
    if (manageFolders) {
        items.add(new MenuItem(app.getMessage("addressbook.label.menu.folders"), "/ekms/addressbook/bdFolderList.jsp", null, null, null, null));
    }
    if (manageContacts) {
        items.add(new MenuItem(app.getMessage("addressbook.label.menu.options"), "/ekms/addressbook/bdOptions.jsp", null, null, null, null));
    }
    //Generating intranet users menu
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.intranetUsers"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.users"), "/ekms/addressbook/udContactList.jsp", null, null, null, null));
    //Generating personal contacts menu
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.personalContacts"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.contacts"), "/ekms/addressbook/abContactList.jsp?cn=abFolderTree.tree&id=", null, "/ekms/addressbook/includes/abFolderTree.jsp", null, null));
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.folders"), "/ekms/addressbook/abFolderList.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("addressbook.label.menu.options"), "/ekms/addressbook/abOptions.jsp", null, null, null, null));
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>