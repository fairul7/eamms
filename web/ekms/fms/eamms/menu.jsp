<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery "%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
%>

<%
	items.add(new MenuItem(app.getMessage("com.tms.workflow.myTask"), "index.jsp?addr=myTask", null, null, null, null));
	items.add(new MenuItem(app.getMessage("com.tms.workflow.staffWorkLoad"), "index.jsp?addr=workLoad", null, null, null, null));
%>

<%
	boolean woSubmit = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woSubmit", null, null);
	boolean woAssigned = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAssigned", null, null);
	boolean woAll = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAll", null, null);

	boolean hwViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewHardwareListing", null, null);
	boolean hwManage = service.hasPermission(userId, "com.tms.workflow.permission.manageHardware", null, null);	
	boolean swViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewSoftwareListing", null, null);
	boolean swManage = service.hasPermission(userId, "com.tms.workflow.permission.manageSoftware", null, null);	
	boolean facilityViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewFacilitySetup", null, null);
	boolean facilityManage = service.hasPermission(userId, "com.tms.workflow.permission.manageFacility", null, null);	
	boolean supplierViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewSupplier", null, null);
	boolean supplierManage = service.hasPermission(userId, "com.tms.workflow.permission.manageSupplier", null, null);	
	boolean sparePartViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewSparePart", null, null);
	boolean sparePartManage = service.hasPermission(userId, "com.tms.workflow.permission.manageSparePart", null, null);
	
	boolean rentalSubmit = service.hasPermission(userId, "com.tms.workflow.permission.submitRentalRequest", null, null);
	boolean rentalVerifyApprove = service.hasPermission(userId, "com.tms.workflow.permission.verifyApproveAssign", null, null);
	boolean rentalViewUpdate = service.hasPermission(userId, "com.tms.workflow.permission.viewUpdate", null, null);
	boolean rentalUpdateRental = service.hasPermission(userId, "com.tms.workflow.permission.updateRentalRequest", null, null);
	boolean rentalViewRequest = service.hasPermission(userId, "com.tms.workflow.permission.viewRentalRequest", null, null);
	boolean rentalReassign = service.hasPermission(userId, "com.tms.workflow.permission.reassignEngineer", null, null);
	
	
	
    if(hwViewList || swViewList || sparePartViewList)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.assetManagement"), null, null, null, null, null));    
    
    if(hwViewList)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.hardware.listing"), "index.jsp?addr=hw01", null, null, null, null));
    
    if(hwManage)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.permission.manageHardware"), "index.jsp?addr=hw02", null, null, null, null));    
    
    if(swViewList)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.software.listing"), "index.jsp?addr=sw01", null, null, null, null));
    
    if(swManage)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.permission.manageSoftware"), "index.jsp?addr=sw02", null, null, null, null));    
    
    if(facilityViewList || facilityManage)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.facilitySetup"), null, null, null, null, null));    
    
    if(facilityViewList)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.facility.listing"), "index.jsp?addr=fs01", null, null, null, null));    
    
    if(facilityManage)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.permission.manageFacility"), "index.jsp?addr=fs02", null, null, null, null));
    
    if(sparePartViewList){
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.sparePart.fullListing"), "index.jsp?addr=sp01", null, null, null, null));
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.sparePart.lowListing"), "index.jsp?addr=sp02", null, null, null, null));
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.sparePart.highListing"), "index.jsp?addr=sp03", null, null, null, null));
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.sparePart.historyListing"), "index.jsp?addr=sp04", null, null, null, null));
    }    
    
     if(rentalSubmit || rentalVerifyApprove || rentalViewUpdate || rentalUpdateRental || rentalViewRequest || rentalReassign)
    	 items.add(new MenuItem(app.getMessage("com.tms.workflow.assetRental"), null, null, null, null, null));
     
     if(rentalSubmit)
    	 items.add(new MenuItem(app.getMessage("com.tms.workflow.rental.newRentalRequest"), "index.jsp?addr=rs01", null, null, null, null));
     
     if(rentalVerifyApprove)
    	 items.add(new MenuItem(app.getMessage("com.tms.workflow.rental.rentalListing"), "index.jsp?addr=rs02", null, null, null, null));
     
     
	
    if(supplierViewList || supplierManage)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.supplierSetup"), null, null, null, null, null));
    
    if(supplierViewList)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.supplier.listing"), "index.jsp?addr=ss01", null, null, null, null));
    
    if(supplierManage)
    	items.add(new MenuItem(app.getMessage("com.tms.workflow.permission.manageSupplier"), "index.jsp?addr=ss01", null, null, null, null));
    	
    //-- work Order start
    if(woSubmit || woAssigned || woAll)
	{
	    items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.wo"), null, null, null, null, null));
	}
	
	if(woSubmit)
	{
	    items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woNew"), "index.jsp?addr=wo01", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woMyList"), "index.jsp?addr=wo02", null, null, null, null));
	}
	
    if(woAssigned)
    {
        items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woAssignedToMe"), "index.jsp?addr=wo03", null, null, null, null));
    }
    
    if(woAll)
    {
        items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woAllList"), "index.jsp?addr=wo04", null, null, null, null));
    }
    //-- work Order end
    
    //--dailly feeds start
    items.add(new MenuItem(app.getMessage("eamms.feed.msg.dailyFeeds"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("eamms.feed.msg.dailyFeedsListing"), "#", null, null, null, null));
    items.add(new MenuItem(app.getMessage("eamms.feed.msg.allFeedsListing"), "#", null, null, null, null));
    items.add(new MenuItem(app.getMessage("eamms.feed.msg.feedsHistory"), "#", null, null, null, null));
    items.add(new MenuItem(app.getMessage("eamms.feed.msg.dailyFeedsLog"), "#", null, null, null, null));
    //--dailly feeds end
%>

<%    
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>


