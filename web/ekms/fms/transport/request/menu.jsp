<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery,
                 com.tms.fms.register.model.*,
                 com.tms.fms.department.model.*"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
	String department = null;
	boolean transApproval = false;
	
	
	boolean transRequest = service.hasPermission(userId, "com.tms.fms.transport.transportRequest", null, null);
	boolean transAdmin = service.hasPermission(userId, "com.tms.fms.transport.transportAdmin", null, null);
	boolean transAssignment = service.hasPermission(userId, "com.tms.fms.transport.transportAssignment", null, null);
	//boolean transApproval = service.hasPermission(userId, "com.tms.fms.transport.transportRequestApproval", null, null);
	
    if(transRequest){
		items.add(new MenuItem(app.getMessage("com.tms.fms.transport.transportRequest"),null, null, null, null, null));
		items.add(new MenuItem(app.getMessage("com.tms.fms.transport.transportAddNew"), "/ekms/fms/transport/request/addNewRequest.jsp", null, null, null, null));
		items.add(new MenuItem(app.getMessage("com.tms.fms.transport.transportListing"), "/ekms/fms/transport/request/requestListing.jsp", null, null, null, null));
				
	}
    
  	//get Department
	FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
	try{
		department = FRM.getUserDepartment(userId);
	}catch(Exception e){System.out.print("menu.jsp: "+e);}
	
	//get HOD permission
	FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);	
	//String transportdept = Application.getInstance().getProperty("TransportDepartment");
	//if(department.equals(transportdept)){
		
		transApproval = FDM.userIsHOD(userId, department);	    
	    if(transApproval){	    	
	   	    items.add(new MenuItem(app.getMessage("fms.facility.menu.hod"), null, null, null, null, null));
	    	items.add(new MenuItem(app.getMessage("fms.tran.status.pendingHODAprroval"), "/ekms/fms/transport/request/pendingListing.jsp", null, null, null, null));    	
	    	items.add(new MenuItem(app.getMessage("fms.tran.requestListing"), "/ekms/fms/transport/request/incomingHODListing.jsp", null, null, null, null));
	    }
	    
	//}
    if(transAdmin){
    	items.add(new MenuItem(app.getMessage("com.tms.fms.transport.transportAdmin"),null, null, null, null, null));
    	items.add(new MenuItem(app.getMessage("fms.tran.uncomingRequest"), "/ekms/fms/transport/request/incomingListing.jsp", null, null, null, null));
    	items.add(new MenuItem(app.getMessage("fms.tran.requestListing"), "/ekms/fms/transport/request/allRequest.jsp", null, null, null, null));
    	
    }
    
    
    if(transAssignment){
    	items.add(new MenuItem(app.getMessage("fms.tran.assignment"),null, null, null, null, null));
    	items.add(new MenuItem(app.getMessage("fms.tran.assignmentToday"), "/ekms/fms/transport/request/todayListing.jsp", null, null, null, null));
    	items.add(new MenuItem(app.getMessage("fms.tran.assignmentSearch"), "/ekms/fms/transport/request/searchListing.jsp", null, null, null, null));
    	items.add(new MenuItem(app.getMessage("fms.tran.assignmentBulk"), "/ekms/fms/transport/request/bulkAssignment.jsp", null, null, null, null));

    	items.add(new MenuItem(app.getMessage("fms.tran.assignmentDriverSearch"), "/ekms/fms/transport/request/searchDriverAssignmentList.jsp", null, null, null, null));
    }
  
    
%>

<%
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>