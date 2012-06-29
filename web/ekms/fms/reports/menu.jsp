<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery,
                 com.tms.fms.setup.model.*"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();

    items.add(new MenuItem(app.getMessage("fms.report.message.label.transportreport"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.report.message.label.transportrequestlisting"), "/ekms/fms/reports/transportReqReport.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.report.message.label.transportrequestlistingTotalCost"), "/ekms/fms/reports/transportReqReportTotal.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.report.message.label.request"), "/ekms/fms/reports/engRequestDet.jsp", null, null, null, null));
    //items.add(new MenuItem(app.getMessage("fms.report.message.label.engineeringresourceutilization"), "/ekms/fms/reports/engResourceUtilReportForm.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.report.message.label.engineeringresourceutilization"), "/ekms/fms/reports/resourceUtil.jsp", null, null, null, null));
  	items.add(new MenuItem(app.getMessage("fms.report.message.label.engSummanyResource"), "/ekms/fms/reports/engSummaryResource.jsp", null, null, null, null));
       
    request.setAttribute(MenuGenerator.MENU_FILE, items);
    
%>