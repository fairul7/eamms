<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determine permissions
	boolean manageDigest = service.hasPermission(userId, "com.tms.cms.digest.ManageDigest", null, null);
    if (manageDigest)
    {
        items.add(new MenuItem(app.getMessage("com.tms.cms.digest.model.DigestModule"), null, null, null, null, null));       
		items.add(new MenuItem(app.getMessage("digest.label.digestIssueList"), "/ekms/digest/digestIssue.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("digest.label.mailingList"), "/ekms/digest/mailingList.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("digest.label.reports"), null, null, null, null, null));  
		items.add(new MenuItem(app.getMessage("digest.label.digestFormat"), "/ekms/digest/digestReport.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("digest.label.newsFormat"), "/ekms/digest/newsReport.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("digest.label.setup"), null, null, null, null, null));    
		items.add(new MenuItem(app.getMessage("digest.label.digestIssue"), "/ekms/digest/setupDigestIssue.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("digest.label.digest"), "/ekms/digest/setupDigest.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("digest.label.recipients"), "/ekms/digest/recipients.jsp", null, null, null, null));
    }
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>