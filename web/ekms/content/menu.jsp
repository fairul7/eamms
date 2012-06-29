<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.cms.core.model.ContentManager"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determining permissions
    boolean manageContent = service.hasPermission(userId, ContentManager.PERMISSION_MANAGE_CONTENT, null, null);
    boolean subscribeContent = service.hasPermission(userId, ContentManager.PERMISSION_SUBSCRIBE_CONTENT, null, null);

    boolean isTaxonomy=false;
    try {
    	String sTaxonomy = Application.getInstance().getProperty("com.tms.cms.taxonomy");
    	if (sTaxonomy!=null && sTaxonomy.equals("true")) {
    		isTaxonomy=true;
    	}
    }catch(Exception e) {
    	
    }
    if (isTaxonomy) {
    // Generate Knowledge Gateway menu
    items.add(new MenuItem("Knowledge Gateway",null,null,null,null,null));
    items.add(new MenuItem(null,null,null,"/ekms/content/includes/taxonomyTree.jsp",null,null));
    }
    
    //Generating knowledge base menu
    items.add(new MenuItem(app.getMessage("cms.label.knowledgeBase"), null, null, null, null, null));
    items.add(new MenuItem(null, null, null, "/ekms/content/includes/contentTree.jsp", null, null));

    //Generating setup menu
    if (manageContent || subscribeContent) {
        items.add(new MenuItem(app.getMessage("cms.label.options"), null, null, null, null, null));
        if (subscribeContent) {
            items.add(new MenuItem(app.getMessage("cms.label.subscriptions"), "/ekms/content/subscription.jsp", null, null, null, null));
        }
        if (manageContent) {
            items.add(new MenuItem(app.getMessage("cms.label.newArticle"), "javascript:newArticle()", null, null, null, null));
            items.add(new MenuItem(app.getMessage("cms.label.newDocument"), "javascript:newDocument()", null, null, null, null));
//            items.add(new MenuItem(app.getMessage("cms.label.manageContent"), "/cmsadmin", "cmsadmin", null, null, null));
        }
    }

    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>