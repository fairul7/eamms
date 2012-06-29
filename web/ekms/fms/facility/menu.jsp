<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery,
                 com.tms.fms.transport.model.*"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
%>
<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.transport.model.TransportModule">
</x:permission>

<x:permission permission="com.tms.fms.facility.permission.storeManagement" module="com.tms.fms.facility.model.FacilityModule">
<%
	items.add(new MenuItem(app.getMessage("fms.facility.menu.storeAdmin"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.facility.menu.todaysAssignment"), "/ekms/fms/facility/todaysAssignment.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.facility.menu.assignmentListing"), "/ekms/fms/facility/assignmentListing.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.facility.menu.checkInAssignment"), "/ekms/fms/facility/assignmentPreCheckin.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.facility.menu.checkOutAssignmentListing"), "/ekms/fms/facility/assignmentCheckOutListing.jsp", null, null, null, null));
%>
</x:permission>
<x:permission permission="com.tms.fms.facility.permission.newsManagementInternal" module="com.tms.fms.facility.model.FacilityModule">
<%
	items.add(new MenuItem(app.getMessage("fms.facility.menu.internalNews"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.internalCheckOut"), "/ekms/fms/facility/InternalCheckOut.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.internalCheckIn"), "/ekms/fms/facility/InternalCheckIn.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.internalCheckOutList"), "/ekms/fms/facility/checkOutListing.jsp", null, null, null, null));
%>
</x:permission>
<x:permission permission="com.tms.fms.facility.permission.inventoryManagement" module="com.tms.fms.facility.model.FacilityModule">
<%
    items.add(new MenuItem(app.getMessage("fms.facility.menu.inventoryManagement"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.facility.menu.addEquipment"), "/ekms/fms/facility/AddFacility.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.equipmentListing"), "/ekms/fms/facility/FacilityListing.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.checkAvailability"), "/ekms/fms/facility/CheckAvailability.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.barcodeSearching"), "/ekms/fms/facility/BarcodeSearching.jsp", null, null, null, null));
%>
</x:permission>
<x:permission permission="com.tms.fms.facility.admin" module="com.tms.fms.transport.model.TransportModule">
<%
    items.add(new MenuItem(app.getMessage("fms.facility.menu.inventorySetup"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.addCategory"), "/ekms/fms/facility/AddCategory.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.categoryListing"), "/ekms/fms/facility/CategoryListing.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.storeLocationSetup"), "/ekms/fms/facility/Location.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.facility.menu.InactiveReasonSetup"), "/ekms/fms/facility/InactiveReason.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.label.import"), "/ekms/fms/facility/ImportFile.jsp", null, null, null, null));
%>
</x:permission>
<x:permission permission="com.tms.fms.facility.permission.rateCard" module="com.tms.fms.facility.model.FacilityModule">
<%    
	items.add(new MenuItem(app.getMessage("fms.setup.menu.rateCardSetup"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.setup.menu.rateCardListing"), "/ekms/fms/facility/rateCardList.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.setup.menu.itemCategoryListing"), "/ekms/fms/facility/itemCategoryList.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("fms.setup.menu.abwCodeSetup"), "/ekms/fms/facility/abwCodeSetup.jsp", null, null, null, null));
%>
</x:permission>
<%    
	request.setAttribute(MenuGenerator.MENU_FILE, items);
%>