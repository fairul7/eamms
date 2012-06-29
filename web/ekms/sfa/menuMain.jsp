<%@ page import="java.util.Collection,
				 java.util.ArrayList,
                 kacang.Application,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	Collection items = new ArrayList();

    //Generating SFA Menu
    boolean isSalesManager = service.hasPermission(userId, "com.tms.crm.sales.SalesManager", "com.tms.sales.model.AccountManagerModule", null);
    boolean isSalesPerson = service.hasPermission(userId, "com.tms.crm.sales.SalesPerson", "com.tms.sales.model.AccountManagerModule", null);
    boolean isExternalSalesPerson = service.hasPermission(userId, "com.tms.crm.sales.ExternalSalesPerson", "com.tms.sales.model.AccountManagerModule", null);
    boolean isDashboardUser = service.hasPermission(userId, "com.tms.crm.sales.Dashboard", "com.tms.sales.model.AccountManagerModule", null);
    boolean isSalesAdmin = service.hasPermission(userId, "com.tms.crm.sales.SalesAdmin", "com.tms.sales.model.AccountManagerModule", null);

    if(isSalesManager || isSalesPerson || isExternalSalesPerson || isDashboardUser || isSalesAdmin) {
        items.add(new MenuItem(app.getMessage("general.label.sales"), "/ekms/sfa", null, null, "/ekms/images/menu/dp_sale.gif", null));
    }

    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
