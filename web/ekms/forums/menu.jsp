<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 com.tms.collab.forum.model.ForumModule,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.collab.forum.ui.ForumList"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determine permissions
	boolean manageForums = service.hasPermission(userId, "com.tms.collab.forum.ManageForums", null, null);
    //Generating categories menu
    items.add(new MenuItem(app.getMessage("forum.label.forumCategories"), null, null, null, null, null));
    ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
    //check for moderatoraccess
    int moderatoraccess=module.countNumOfForumsByModeratorGroupAccess(userId,"1");
    Collection categories = module.getCategories();
    for(Iterator i = categories.iterator(); i.hasNext();)
    {
        String category = (String) i.next();
        String url = "/ekms/forums/forums.jsp?" + ForumList.LABEL_CATEGORY + "=" + category;
        items.add(new MenuItem(category, url, null, null, null, null));
    }
    items.add(new MenuItem(app.getMessage("forum.label.allForums"), "/ekms/forums/forums.jsp", null, null, null, null));
    // Generating forum management menu
    if (manageForums||moderatoraccess>0)
    {
        items.add(new MenuItem(app.getMessage("forum.label.options"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("com.tms.collab.forum.ManageForums"), "/ekms/forums/forumAdmin.jsp?cn=forumAdminPage.forumAdminPortlet.adminForumPanel.createForumForm&&button*forumAdminPage.forumAdminPortlet.adminForumPanel.createForumForm.cancel=Cancel", null, null, null, null));
    }
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>