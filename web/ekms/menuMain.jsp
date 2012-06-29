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

    //Determine permissions
	boolean userView = service.hasPermission(userId, SecurityService.PERMISSION_USER_VIEW, null, null);
	boolean groupView = service.hasPermission(userId, SecurityService.PERMISSION_GROUP_VIEW, null, null);
    boolean manageContent = service.hasPermission(userId, "com.tms.cms.ManageContent", null, null);
	boolean designView = service.hasPermission(userId, "com.tms.cms.SiteDesign", null, null);
	boolean mobileView = service.hasPermission(userId, "com.tms.cms.Mobile", null, null);
	boolean adView = service.hasPermission(userId, "com.tms.cms.ad.ManageAds", null, null);
    boolean mailListView = service.hasPermission(userId, "com.tms.cms.maillist.ManageMailList", null, null);
    boolean eventView = service.hasPermission(userId, "com.tms.collab.calendar.ManageEvents", null, null);
	boolean quotaView = service.hasPermission(userId, "com.tms.collab.messaging.Quota", null, null);

	//Generating Application Menu
	items.add(new MenuItem(app.getMessage("general.label.knowledgeBase"), "/ekms/content", null, null, "/ekms/images/menu/ic_content.gif", null));
    items.add(new MenuItem(app.getMessage("general.label.messaging"), "/ekms/messaging", null, null, "/ekms/images/menu/ic_messaging.gif", null));
	items.add(new MenuItem(app.getMessage("general.label.scheduler"), "/ekms/calendar", null, null, "/ekms/images/menu/ic_scheduler.gif", null));
	items.add(new MenuItem(app.getMessage("general.label.contacts"), "/ekms/addressbook", null, null, "/ekms/images/menu/ic_contacts.gif", null));
	items.add(new MenuItem(app.getMessage("general.label.forums"), "/ekms/forums", null, null, "/ekms/images/menu/ic_forums.gif", null));
	items.add(new MenuItem(app.getMessage("general.label.formWizard"), "/ekms/formwizard", null, null, "/ekms/images/menu/ic_forms.gif", null));
	items.add(new MenuItem(app.getMessage("general.label.resourceManager"), "/ekms/resourcemanager", null, null, "/ekms/images/menu/ic_resource.gif", null));

	items.add(new MenuItem("----------", "javascript:;", null, null, null, null));

	if (manageContent) {
	    items.add(new MenuItem(app.getMessage("general.label.contentManagement"), "/ekms/cmsadmin", null, null, "/ekms/images/menu/ic_managecontent.gif", null));
    }
    if (designView || mobileView || adView || mailListView || eventView) {
    	items.add(new MenuItem(app.getMessage("general.label.websiteManagement"), "/ekms/siteadmin", null, null, "/ekms/images/menu/ic_webadmin.gif", null));
    }
    if (userView || groupView || quotaView) {
    	items.add(new MenuItem(app.getMessage("general.label.systemAdministration"), "/ekms/sysadmin", null, null, "/ekms/images/menu/ic_sysadmin.gif", null));
    }

	items.add(new MenuItem("----------", "javascript:;", null, null, null, null));
        
    boolean isTransport = service.hasPermission(userId, "com.tms.fms.transport.admin", "com.tms.fms.transport.model.TransportModule", null);
    boolean isTransport2 = service.hasPermission(userId, "com.tms.fms.transport.manager", "com.tms.fms.transport.model.TransportModule", null);
    if(isTransport || isTransport2) {
        items.add(new MenuItem(app.getMessage("fms.label.transport"), "/ekms/fms/transport", null, null, "/ekms/images/menu/ic_resource.gif", null));
    }
    
    boolean isFacility = service.hasPermission(userId, "com.tms.fms.facility.admin", "com.tms.fms.facility.model.FacilityModule", null);
    boolean isFacility2 = service.hasPermission(userId, "com.tms.fms.facility.manager", "com.tms.fms.facility.model.FacilityModule", null);
    if(isFacility || isFacility2) {
        items.add(new MenuItem(app.getMessage("fms.label.facility"), "/ekms/fms/facility", null, null, "/ekms/images/menu/ic_resource.gif", null));
    }

    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>
