<%@ page import="java.util.Collection,
                java.util.ArrayList,
                kacang.Application,
                kacang.ui.menu.MenuItem,
                kacang.ui.menu.ApplicationMenuGenerator,
                kacang.services.security.*,
                kacang.services.security.SecurityException" %>

<%
    Application app = Application.getInstance();
    SecurityService service = (SecurityService)app.getService(SecurityService.class);
    String userId = service.getCurrentUser(request).getId();
    Collection items = new ArrayList();

    boolean quotationView = service.hasPermission( userId, "com.tms.quotation.add", null, null );
    quotationView |= service.hasPermission(userId, "com.tms.quotation.delete", null, null );
    quotationView |= service.hasPermission(userId, "com.tms.quotation.approve", null, null );
	quotationView |= service.hasPermission(userId, "com.tms.quotation.templates", null, null );
    if( quotationView ) {
      items.add(new MenuItem(app.getMessage("com.tms.quotation.mainMenu"),"/ekms/quotation",null,null,"/ekms/images/menu/ic_forms.gif",null));
    }
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>

