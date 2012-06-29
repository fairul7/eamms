<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 com.tms.collab.resourcemanager.model.ResourceManager,
                 com.tms.cms.core.model.ContentManager"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();

    //Determine permissions
    boolean manageContent = service.hasPermission(userId, ContentManager.PERMISSION_MANAGE_CONTENT, null, null);
    boolean manageKeywords = service.hasPermission(userId, "com.tms.cms.ManageKeywords", null, null);
    boolean manageProfile = service.hasPermission(userId, ContentManager.PERMISSION_MANAGE_PROFILE, null, null);
    boolean accessRecycleBin= service.hasPermission(userId, ContentManager.PERMISSION_ACCESS_RECYCLE_BIN, null, null);
    boolean manageRecycleBin= service.hasPermission(userId, ContentManager.PERMISSION_MANAGE_RECYCLE_BIN, null, null);
    boolean massTagging = service.hasPermission(userId, "com.tms.cms.MassMapping",null,null);
    boolean manageTaxonomy = service.hasPermission(userId,"com.tms.cms.manageTaxonomy",null,null);
    boolean accessReports = service.hasPermission(userId,"com.tms.cms.AccessReports",null,null);

    boolean isTaxonomy = false;
    try {
    	String taxonomy = Application.getInstance().getProperty("com.tms.cms.taxonomy");
    	if (taxonomy!=null && taxonomy.equals("true")) {
    		isTaxonomy=true;
    	}
    }
    catch(Exception e) {}

    //Generating content creation/editing menu
    if (manageContent) {
        items.add(new MenuItem(app.getMessage("general.label.contentManagement"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("cms.label.contentSummary"), "/ekms/cmsadmin/contentSummary.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("cms.label.explorerView"), "/ekms/cmsadmin/contentExplorer.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("cms.label.listByType"), "/ekms/cmsadmin/contentList.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("cms.label.fullTextSearch"), "/ekms/cmsadmin/contentSearch.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("cms.label.newArticle"), "javascript:newArticle()", null, null, null, null));
        items.add(new MenuItem(app.getMessage("cms.label.newDocument"), "javascript:newDocument()", null, null, null, null));
        if (isTaxonomy && massTagging) {
        	items.add(new MenuItem(app.getMessage("com.tms.cms.MassMapping"),"/ekms/cmsadmin/contentMassMapping.jsp",null,null,null,null));
        			
        }
    }
    if(accessReports) {
        items.add(new MenuItem(app.getMessage("com.tms.report"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("cms.label.contentUsageReport"),"/ekms/cmsadmin/contentUsageReport.jsp",null,null,null,null));
        items.add(new MenuItem(app.getMessage("cms.label.contentSubmittedReport"),"/ekms/cmsadmin/contentSubmittedReport.jsp",null,null,null,null));
        items.add(new MenuItem(app.getMessage("cms.label.contentSummaryReport"),"/ekms/cmsadmin/contentSummaryReport.jsp",null,null,null,null));
    }

    //Generating administration menu
    if (manageKeywords || manageProfile || manageRecycleBin || accessRecycleBin || manageTaxonomy) {
        items.add(new MenuItem(app.getMessage("general.label.administration"), null, null, null, null, null));
        if (manageKeywords) {
            items.add(new MenuItem(app.getMessage("cms.label.keywords"), "/ekms/cmsadmin/contentKeywords.jsp", null, null, null, null));
        }
        if (manageProfile) {
            items.add(new MenuItem(app.getMessage("cms.label.profiles"), "/ekms/cmsadmin/profileList.jsp", null, null, null, null));
        }
        if (manageRecycleBin || accessRecycleBin) {
            items.add(new MenuItem(app.getMessage("cms.label.recycleBin"), "/ekms/cmsadmin/contentDeleted.jsp", null, null, null, null));
        }
        if (isTaxonomy && manageTaxonomy) {
        	items.add(new MenuItem(app.getMessage("com.tms.cms.manageTaxonomy"),"/ekms/cmsadmin/txyForm.jsp",null,null,null,null));
        	items.add(new MenuItem(app.getMessage("taxonomy.label.permission"),"/ekms/cmsadmin/txyPermissionForm.jsp",null,null,null,null));
        }
    }

    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>