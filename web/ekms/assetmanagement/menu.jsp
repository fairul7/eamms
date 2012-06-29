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
	ArrayList items = new ArrayList();
	
	boolean isAdmin = securityService.hasPermission(userId, "com.tms.assetmanagement.manageAssetPermission", null, null);
    boolean isUser = securityService.hasPermission(userId, "com.tms.assetmanagement.accessAssetPermission", null, null);
      	 
    if(isUser || isAdmin) {  
        items.add(new MenuItem(app.getMessage("asset.label.AssetManagement", "Asset Management"), null, null, null, null, null));	      
        }	 
        
    if(isUser ) {	
      items.add(new MenuItem(app.getMessage("asset.label.newAsset", "New Asset"), "/ekms/assetmanagement/assetItemForm.jsp", null, null, null, null));
      
      }
   if(isUser || isAdmin) {
      
      items.add(new MenuItem(app.getMessage("asset.label.assetlisting", "Asset Listing"), "/ekms/assetmanagement/assetItemTableView.jsp", null, null, null, null));
	}      
	
    if(isUser ) {
      
      items.add(new MenuItem(app.getMessage("asset.label.newDisposal", "New Disposal"), "/ekms/assetmanagement/assetDisposalform.jsp", null, null, null, null));
      items.add(new MenuItem(app.getMessage("asset.label.disposalListing", "Disposal Listing"), "/ekms/assetmanagement/assetDisposaltableView.jsp", null, null, null, null));
    }
    
    if(isAdmin) {
      items.add(new MenuItem(app.getMessage("asset.label.administration", "Administration"), null, null, null, null, null));
      items.add(new MenuItem(app.getMessage("asset.label.Category", "Category"), "/ekms/assetmanagement/assetCategory.jsp", null, null, null, null));
      items.add(new MenuItem(app.getMessage("asset.label.generateReport", "Generate Report"), "/ekms/assetmanagement/assetReportType.jsp", null, null, null, null));
      items.add(new MenuItem(app.getMessage("asset.label.financialSetup", "Financial Setup"), "/ekms/assetmanagement/assetfinancialSetup.jsp", null, null, null, null));
	}
	
		request.setAttribute(MenuGenerator.MENU_FILE, items);
	
	
%>