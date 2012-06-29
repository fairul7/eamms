
<%@page import="com.tms.fms.engineering.model.FacilitiesCoordinatorModule"%><%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.fms.engineering.model.EngineeringModule"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	boolean isHOD=EngineeringModule.isHOD(userId);
	boolean isFCHead=EngineeringModule.isFCHead(userId);
	boolean isFC=FacilitiesCoordinatorModule.isFC(userId);
	boolean isUnitApprover=EngineeringModule.isUnitApprover(userId);
	ArrayList items = new ArrayList();
	
	items.add(new MenuItem(app.getMessage("fms.facility.menu.facilitiesRequest"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.facility.menu.addRequest"), "/ekms/fms/engineering/submitRequest.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.facility.menu.requestListing"), "/ekms/fms/engineering/requestListing.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.pendingApproval"), "/ekms/fms/engineering/pendingRequestListing.jsp", null, null, null, null));
    
    //HOD
    if(isHOD){
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.hod"), null, null, null, null, null));
		items.add(new MenuItem(app.getMessage("fms.facility.menu.incomingRequest"), "/ekms/fms/engineering/hodPendingRequestListing.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("fms.facility.menu.requestListing"), "/ekms/fms/engineering/requestHODListing.jsp", null, null, null, null));
    }
    
	//  Facilities Coordinator Head
    if(isFCHead){
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.fcHeadListing"), null, null, null, null, null));
		items.add(new MenuItem(app.getMessage("fms.facility.menu.incomingRequest"), "/ekms/fms/engineering/fcHeadRequestListing.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("fms.facility.menu.allRequestListing"), "/ekms/fms/engineering/requestFCHListing.jsp", null, null, null, null));
    }
	//  Facilities Coordinator
    if (isFC){
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.facilitiesCoordinator"), null, null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.incomingRequest"), "/ekms/fms/engineering/incomingRequestFCListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.requestListing"), "/ekms/fms/engineering/requestFCListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.assignmentListing"), "/ekms/fms/engineering/fcViewAllAssignmentListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.batchAssignmentUpdate"), "/ekms/fms/engineering/batchAssignmentUpdate.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.appliedCancellationRequestList"), "/ekms/fms/engineering/appCancelRequestListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.completedDelayRequestList"), "/ekms/fms/engineering/completedDelayRequestListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.request.label.checkAvailability"), "/ekms/fms/engineering/coordCheckAvailability.jsp", null, null, null, null));
    }

    //Unit Head
    if(isUnitApprover){
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.unitHead"), null, null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.incomingRequest"), "/ekms/fms/engineering/houPendingRequestListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.requestListing"), "/ekms/fms/engineering/houAllRequestListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.unitHead.todayAssignment"), "/ekms/fms/engineering/houTodaysAssignment.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.todaysAssignmentVtrTvro"), "/ekms/fms/engineering/todaysAssignment.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("fms.facility.menu.assignmentListingVtrTvro"), "/ekms/fms/engineering/assignmentListing.jsp", null, null, null, null));
	    
    }
    
%>
<%    
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>