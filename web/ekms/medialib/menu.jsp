<%@page import="java.util.ArrayList,
				kacang.Application,
				kacang.ui.menu.MenuGenerator,
				kacang.ui.menu.MenuItem,
				kacang.services.security.SecurityService,
				com.tms.cms.medialib.model.AlbumModule,
				com.tms.cms.medialib.model.LibraryModule"
%>

<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
	String userId = securityService.getCurrentUser(request).getId();
	AlbumModule albumModule = (AlbumModule) Application.getInstance().getModule(AlbumModule.class);
	
	// Determine permisson
	boolean createLibrary = securityService.hasPermission(userId, LibraryModule.PERMISSION_CREATE_LIBRARY, null, null);
	boolean isManager = albumModule.isManager();
	boolean isContributor = albumModule.isContributor();
	
	ArrayList items = new ArrayList();
	items.add(new MenuItem(app.getMessage("medialib.label.mediaLibrary"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("medialib.label.featuredAlbum"), "/ekms/medialib/index.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("medialib.label.libraryBrowsing"), "/ekms/medialib/libraryViewOnlyListing.jsp", null, null, null, null));
	
	if(createLibrary || isManager || isContributor) {
		items.add(new MenuItem(app.getMessage("medialib.label.administration"), null, null, null, null, null));
		if(createLibrary) {
			items.add(new MenuItem(app.getMessage("medialib.label.newLibrary"), "/ekms/medialib/newLibrary.jsp", null, null, null, null));
		}
		if(isManager) {
			items.add(new MenuItem(app.getMessage("medialib.label.newAlbum"), "/ekms/medialib/newAlbum.jsp", null, null, null, null));
		}
		if(isContributor) {
			items.add(new MenuItem(app.getMessage("medialib.label.newMedia"), "/ekms/medialib/newMedia.jsp", null, null, null, null));
		}
        if (createLibrary) {
			items.add(new MenuItem(app.getMessage("medialib.label.libraryListing"), "/ekms/medialib/libraryListing.jsp", null, null, null, null));
        }
		if(isManager) {
			items.add(new MenuItem(app.getMessage("medialib.label.albumListing"), "/ekms/medialib/albumListing.jsp", null, null, null, null));
		}
		if(isManager || isContributor) {
			items.add(new MenuItem(app.getMessage("medialib.label.mediaListing"), "/ekms/medialib/mediaListing.jsp", null, null, null, null));
		}
	}
	
	request.setAttribute(MenuGenerator.MENU_FILE, items);
%>