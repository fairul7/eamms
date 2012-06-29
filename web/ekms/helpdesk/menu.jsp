<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.crm.helpdesk.HelpdeskHandler"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determining permissions
    boolean helpdeskAdmin = service.hasPermission(userId, HelpdeskHandler.PERMISSION_HELPDESK_ADMIN, null, null);

    //Generating helpdesk menu
    items.add(new MenuItem(app.getMessage("helpdesk.label.helpdesk"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("helpdesk.label.listIncidents"), "/ekms/helpdesk/index.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("helpdesk.label.findIncidents"), "/ekms/helpdesk/incidentSearch.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("helpdesk.message.newIncident"), "/ekms/helpdesk/incidentCompany.jsp", null, null, null, null));

    //Generating companies menu
    items.add(new MenuItem(app.getMessage("sfa.message.menuCompanies"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("sfa.message.companiesListing"), "/ekms/helpdesk/companyList.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("sfa.message.newCompany"), "/ekms/helpdesk/companyNew.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("sfa.message.contactListing"), "/ekms/helpdesk/contactList.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("sfa.message.newContact"), "/ekms/helpdesk/contactNew.jsp", null, null, null, null));

    //Generating setup menu
    if (helpdeskAdmin) {
        items.add(new MenuItem(app.getMessage("helpdesk.label.setup"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("helpdesk.label.products"), "/ekms/helpdesk/product.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.companyType"), "/ekms/helpdesk/setupCompanyType.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.salutation"), "/ekms/helpdesk/setupSalutation.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("helpdesk.label.incidentSettings"), "/ekms/helpdesk/incidentSettings.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("helpdesk.label.alert.setup"), "/ekms/helpdesk/alertNotificationSetup.jsp", null, null, null, null));
    }

    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>