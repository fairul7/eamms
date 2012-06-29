<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 kacang.ui.WidgetManager,
                 kacang.ui.Widget,
                 com.tms.cms.core.model.ContentManager"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();

    //Determine permissions
	boolean designView = service.hasPermission(userId, "com.tms.cms.SiteDesign", null, null);
	boolean mobileView = service.hasPermission(userId, "com.tms.cms.Mobile", null, null);
	boolean adView = service.hasPermission(userId, "com.tms.cms.ad.ManageAds", null, null);
    boolean mailListView = service.hasPermission(userId, "com.tms.cms.maillist.ManageMailList", null, null);
    boolean eventView = service.hasPermission(userId, "com.tms.collab.calendar.ManageEvents", null, null);
    boolean pollView = service.hasPermission(userId, "com.tms.collab.vote.ManageVotes", null, null);

    //Generating menu
    items.add(new MenuItem(app.getMessage("general.label.websiteManagement"), null, null, null, null, null));
    if (designView) {
        items.add(new MenuItem(app.getMessage("siteadmin.label.siteDesign"), "/ekms/siteadmin/siteDesign.jsp", null, null, null, null));
    }

    if (adView) {
        items.add(new MenuItem(app.getMessage("general.label.bannerAds"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("ad.label.adLocationListing"), "/ekms/siteadmin/ad_index.jsp?cn=page.portlet.adUi&et=listAdLocations", null, null, null, null));
        items.add(new MenuItem(app.getMessage("ad.label.newAdLocation"), "/ekms/siteadmin/ad_index.jsp?cn=page.portlet.adUi.adLocationTable&et=newAdLocation", null, null, null, null));
        items.add(new MenuItem(app.getMessage("ad.label.adListing"), "/ekms/siteadmin/ad_index.jsp?cn=page.portlet.adUi&et=listAds", null, null, null, null));
        items.add(new MenuItem(app.getMessage("ad.label.newAd"), "/ekms/siteadmin/ad_index.jsp?cn=page.portlet.adUi.adTable&et=newAd", null, null, null, null));
        items.add(new MenuItem(app.getMessage("ad.label.refreshModules"), "/ekms/siteadmin/ad_index.jsp?cn=page.portlet.adUi&et=refreshModule", null, null, null, null));
    }

    if (pollView) {
        items.add(new MenuItem(app.getMessage("vote.label.votes"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("vote.label.questionListing"), "pollAdmin.jsp?event=view", null, null, null, null));
        items.add(new MenuItem(app.getMessage("vote.label.newVote"), "pollAdmin.jsp?event=newVote", null, null, null, null));
    }

    if (eventView) {
        items.add(new MenuItem(app.getMessage("calendar.label.events"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.eventsListing"), "/ekms/siteadmin/eventList.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.monthlyView"), "/ekms/siteadmin/eventMonthlyView.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.newEvent"), "/ekms/siteadmin/eventAdd.jsp", null, null, null, null));
    }

    if (mailListView) {
        items.add(new MenuItem(app.getMessage("maillist.label.mailingLists"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("maillist.label.composedLists"), "/ekms/siteadmin/ml_index.jsp?cn=page.portlet.mailListUi&et=showComposedMailListTable", null, null, null, null));
        items.add(new MenuItem(app.getMessage("maillist.label.contentLists"), "/ekms/siteadmin/ml_index.jsp?cn=page.portlet.mailListUi&et=showContentMailListTable", null, null, null, null));
        items.add(new MenuItem(app.getMessage("maillist.label.scheduledLists"), "/ekms/siteadmin/ml_index.jsp?cn=page.portlet.mailListUi&et=showScheduledMailListTable", null, null, null, null));
        items.add(new MenuItem(app.getMessage("maillist.label.mailingListTemplates"), "/ekms/siteadmin/ml_index.jsp?cn=page.portlet.mailListUi&et=showMailTemplateTable", null, null, null, null));
        items.add(new MenuItem(app.getMessage("maillist.label.mailingListLog"), "/ekms/siteadmin/ml_index.jsp?cn=page.portlet.mailListUi&et=showMailLogTable", null, null, null, null));
    }

    if (mobileView) {
        items.add(new MenuItem(app.getMessage("siteadmin.label.mobile"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("siteadmin.label.channelListing"), "/ekms/siteadmin/mobileChannelList.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("siteadmin.label.newChannel"), "/ekms/siteadmin/mobileChannelAdd.jsp", null, null, null, null));
    }

    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>