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
<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule">
<%
	items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.transportInventory"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.addNewVehicle"), "/ekms/fms/transport/AddVehicle.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.vehicleListing"), "/ekms/fms/transport/VehicleListing.jsp", null, null, null, null));
%>
</x:permission>
<x:permission permission="com.tms.fms.transport.admin" module="com.tms.fms.transport.model.TransportModule">
<%
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.transportSetup"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.categorySetup"), "/ekms/fms/transport/Category.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.channelSetup"), "/ekms/fms/transport/Channel.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.makeTypeSetup"), "/ekms/fms/transport/MakeType.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.TransportRateCardSetup"), "/ekms/fms/transport/RateCard.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.petrolCardSetup"), "/ekms/fms/transport/PetrolCard.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.inactiveReasonSetup"), "/ekms/fms/transport/InactiveReason.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.workshopSetup"), "/ekms/fms/transport/Workshop.jsp", null, null, null, null));
%>
</x:permission>
<%
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>