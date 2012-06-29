<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 com.tms.sam.po.permission.model.PermissionModel,
                 com.tms.sam.po.permission.model.POGroup,
                 com.tms.sam.po.model.PrePurchaseModule,
                 java.util.Collection"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	PrePurchaseModule module = (PrePurchaseModule) app.getModule(PrePurchaseModule.class);
	int newTotal = module.countRequest("New");
	int poTotal = module.countRequest("po");
	int boTotal = module.countRequest("Quoted");
	String userID = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
	
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	// Determine permisson
	
	
	boolean isPO = service.hasPermission(userID, "com.tms.sam.po.PurchaseOfficer", null, null);
	boolean isBO = service.hasPermission(userID, "com.tms.sam.po.BudgetOfficer", null, null);
	boolean permission = service.hasPermission(userID, "com.tms.sam.po.ManagePermission", null, null);
	
	items.add(new MenuItem(app.getMessage("po.label.po"), null, null, null, null, null));
	
	if(permissionModel.hasPermission(userID, POGroup.PERM_SUBMIT_NEW_REQUEST)){
		items.add(new MenuItem(app.getMessage("po.label.prePurchase"), "/ekms/purchaseordering/prepurchaseRequestForm.jsp?flag=new", null, null, null, null));
		items.add(new MenuItem(app.getMessage("myRequest.label.request"), "/ekms/purchaseordering/myRequest.jsp", null, null, null, null));
	}

	if(permissionModel.isHOD(userID)) {
		items.add(new MenuItem(app.getMessage("userRequest.label.hodApproval")+" ("+newTotal+")", "/ekms/purchaseordering/userRequest.jsp", null, null, null, null));
	}
	
	if(permissionModel.hasPermission(userID, POGroup.PERM_MANAGE_QUOTATION)){
		items.add(new MenuItem(app.getMessage("quotation.label.request")+" ("+poTotal+")", "/ekms/purchaseordering/purchaseRequestListing.jsp", null, null, null, null));
		//items.add(new MenuItem(app.getMessage("po.label.purchaseOrder"), "/ekms/purchaseordering/purchaseOrderListing.jsp", null, null, null, null));
	}
	
	if(permissionModel.hasPermission(userID, POGroup.PERM_APPROVE_BUDGET)){
		items.add(new MenuItem(app.getMessage("budget.label.title")+" ("+boTotal+")", "/ekms/purchaseordering/requestListing.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("po.menu.reports"), null, null, null, null, null));
		//items.add(new MenuItem(app.getMessage("report.label.financialReport"), "/ekms/purchaseordering/financialReport.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("report.label.supplierRatingReport"), "/ekms/purchaseordering/supplierRatingReport.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("report.label.statusReport"), "/ekms/purchaseordering/statusReport.jsp", null, null, null, null));
	}
	
	if(permission){
		items.add(new MenuItem(app.getMessage("po.menu.setups"), null, null, null, null, null));
    	items.add(new MenuItem(app.getMessage("po.menu.permission"), "/ekms/purchaseordering/permission.jsp", null, null, null, null));     	
	}
	
	if(permissionModel.hasPermission(userID, POGroup.PERM_ACCESS_SETUP)){
		items.add(new MenuItem(app.getMessage("po.menu.globalSetting"), "/ekms/purchaseordering/globalSetting.jsp", null, null, null, null)); 
    	items.add(new MenuItem(app.getMessage("po.menu.currency"), "/ekms/purchaseordering/currencyListing.jsp", null, null, null, null)); 
    	items.add(new MenuItem(app.getMessage("po.menu.itemCategory"), "/ekms/purchaseordering/categoryListing.jsp", null, null, null, null)); 
    	items.add(new MenuItem(app.getMessage("po.menu.item"), "/ekms/purchaseordering/itemListing.jsp", null, null, null, null)); 
	}
	request.setAttribute(MenuGenerator.MENU_FILE, items);
%>