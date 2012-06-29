<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 kacang.ui.WidgetManager,
                 kacang.ui.Widget"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();

	// Determine permissions
	boolean manageTemplates = service.hasPermission(userId, "com.tms.quotation.templates",null, null);
	
        items.add(new MenuItem(app.getMessage("com.tms.quotation.mainMenu"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("com.tms.quotation.menu.list"), "/ekms/quotation/viewQuotation.jsp", null, null, null, null));
//      items.add(new MenuItem("New Quotation", "/ekms/quotation/newQuotation_customer.jsp", null, null, null, null));
//        items.add(new MenuItem(app.getMessage("com.tms.quotation.menu.customers"), null, null, null, null, null));
//        items.add(new MenuItem(app.getMessage("com.tms.quotation.table.customer"), "/ekms/quotation/viewCustomer.jsp", null, null, null, null));
//      items.add(new MenuItem("New Customer", "/ekms/quotation/addCustomer.jsp", null, null, null, null));
	if( manageTemplates ) {
        items.add(new MenuItem(app.getMessage("com.tms.quotation.menu.templates"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("com.tms.quotation.table.templateText"), "/ekms/quotation/viewQtnTemplateText.jsp", null, null, null, null));
//      items.add(new MenuItem("New Template Text", "/ekms/quotation/addQtnTemplateText.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("com.tms.quotation.table.template"), "/ekms/quotation/viewTemplate.jsp", null, null, null, null));
//      items.add(new MenuItem("New Template Collection", "/ekms/quotation/addTemplate.jsp", null, null, null, null));

        items.add(new MenuItem(app.getMessage("com.tms.quotation.menu.tables"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("com.tms.quotation.table.qtnTable"), "/ekms/quotation/viewQtnTable.jsp", null, null, null, null));
	}
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
