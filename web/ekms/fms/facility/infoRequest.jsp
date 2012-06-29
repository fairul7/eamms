<%@ page import="kacang.*, kacang.model.*, java.util.*, com.tms.fms.engineering.model.*" %>
<%@ page import="com.tms.fms.transport.model.TransportRequest, com.tms.fms.engineering.ui.ServiceDetailsForm, com.tms.fms.facility.model.SetupModule" %>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<%--
	version:
		1.0 used to troubleshoot missing transport request (bug #10473) (2011-06-24)
		1.1 added facility booking (2011-07-13)
		1.2 added manpower (2011-07-22)
		1.3 update facility booking to support manpower (2011-07-25)
			add rate card info
			add block booking info
		1.4 add quantity in request (2011-07-26)
		    format block booking
		    check for invalid rate cards
		1.5 add fromTime, toTime (2011-08-20)
		    add modifiedBy, modifiedDate 
		1.6 change sorting for booking & assignment (2011-08-23)
			url change for "top"
			change default "top" from 5 to -1
			show cancelled assignment items & manpower
		1.7 added status / status label for request & status trail (2011-08-24)
		1.8 added cancel date for Requested Manpower (2011-09-08)
		1.9 added checking for missing rate card at service level (2011-09-23)
			added checking for missing category / competency on booking
			global date/time pattern
		2.0 Bug fix: display duplicate extra item (remove assignment no.) (2011-09-28)
		2.1 added timing to requested items & requested manpower (2011-10-06)
			added created/cancelled date for requested items
			added serviceId to requested services
		2.2 shows missing service request (2011-11-04)
			renamed "Requested Services" to "Services Required"
			renamed columns in "Services Required"
		2.3 verbose feature (2011-11-17)
		2.4 added unit info for Requested Manpower (2012-04-30)
		2.5 added total internal rate / total external rate (2012-06-08)
--%>

<%!
	public static final String CURRENT_VERSION = "2.5";
	public static final String DATE_PATTERN = "dd-MMM-yyyy";
	public static final String DATE_TIME_PATTERN = "dd-MMM-yyyy hh:mm'&nbsp;'a";
	
	public static final HashMap yesNoMap = new HashMap();
	static {
		yesNoMap.put("1", "Yes");
		yesNoMap.put("0", "No");
	}

	public String matchLegend(PageContext pageContext, String var, HashMap map) {
		String legend = (String) pageContext.getAttribute(var);
		if (map.containsKey(legend)) {
			return (String) map.get(legend);
		}
		return legend;
	}
	
	public String formatYesNo(PageContext pageContext, String var) {
		return matchLegend(pageContext, var, yesNoMap);
	}
%>

<%
	EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
	EngineeringDao dao = (EngineeringDao) module.getDao();
	
	String requestId = request.getParameter("requestId");
	
	if (requestId == null || requestId.equals("")) {
		out.println("Please enter requestId");
		return;
	}
	
	boolean verboseMode = false;
	String verbose = request.getParameter("verbose");
	if (verbose != null && verbose.equals("1")) {
		verboseMode = true;
	}
	
	String top = request.getParameter("top");
	int topNum = -1;
	if (top != null && !top.equals("")) {
		topNum = Integer.parseInt(top);
	}
	String topStr = "";
	if (topNum >= 1) {
		topStr = "TOP " + topNum;
	}
	
	// get engineering request
	boolean internalRequest = true;
	try {
		String sqlRequest = 
			"SELECT requestId, title, requiredFrom, requiredTo, requestType, totalInternalRate, totalExternalRate, status " + 
			"FROM fms_eng_request " +
			"WHERE requestId = ? ";
		Collection colRequest = dao.select(sqlRequest, EngineeringRequest.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colRequest", colRequest);
		
		if (colRequest.size() == 1) {
			EngineeringRequest req = (EngineeringRequest) colRequest.iterator().next();
			if ("E".equals(req.getRequestType())) {
				internalRequest = false;
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// get status trail
	try {
		String sqlStatusTrail = 
			"SELECT status, remarks, additionalInfo, createdBy, createdDate " + 
			"FROM fms_eng_request_status " +
			"WHERE requestId = ? " +
			"ORDER BY createdDate DESC ";
		Collection colStatusTrail = dao.select(sqlStatusTrail, Status.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colStatusTrail", colStatusTrail);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// get transport request
	try {
		String sqlTransport = 
			"SELECT r.id, startDate, endDate, status, s.username, requestDate, requestTitle " + 
			"FROM fms_tran_request r " +
			"LEFT OUTER JOIN security_user s ON (r.requestBy = s.id) " +
			"WHERE engineeringRequestId = ? ";
		Collection colTransport = dao.select(sqlTransport, TransportRequest.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colTransport", colTransport);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// services required
	HashMap requestedService = new HashMap();
	try {
		String sqlRequestedServices = 
			"SELECT s.serviceId, s.title, rs.requestId " + 
			"FROM fms_eng_services s " +
			"INNER JOIN fms_eng_request_services rs ON (s.serviceId = rs.serviceId) " +
			"WHERE rs.requestId = ? " +  
			"ORDER BY s.serviceId";
		Collection colRequestedServices = dao.select(sqlRequestedServices, Service.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colRequestedServices", colRequestedServices);
		
		// add requested services to map
		for (Iterator iterator = colRequestedServices.iterator(); iterator.hasNext();) {
			Service service = (Service) iterator.next();
			requestedService.put(service.getServiceId(), service.getTitle());
		}
		
		String sqlAllServices = 
			"SELECT s.serviceId, s.title " + 
			"FROM fms_eng_services s " +
			"ORDER BY s.serviceId ";
		Collection colAllServices = dao.select(sqlAllServices, Service.class, null, 0, -1);
		pageContext.setAttribute("colAllServices", colAllServices);
		
		// individual requested services
		for (Iterator iterator = colAllServices.iterator(); iterator.hasNext();) {
			Service service = (Service) iterator.next();
			String serviceId = (String) service.getServiceId();
			
			if (ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)) {
				//Collection col = module.getScpService(requestId, serviceId);
				String sql =
						"SELECT p.id, p.requestId, p.serviceId, p.facilityId, " +
							"p.requiredFrom, p.requiredTo, p.departureTime, p.location, p.segment, " +
							"p.settingFrom, p.settingTo, p.rehearsalFrom, p.rehearsalTo, p.recordingFrom, p.recordingTo, " +
							"p.createdBy, p.createdDate, p.modifiedBy, p.modifiedDate, f.name as facility, " +
							"p.internalRate, p.externalRate, p.blockBooking, p.submitted " +
						"FROM fms_eng_service_scp p " +
						"LEFT OUTER JOIN fms_rate_card f ON (f.id = p.facilityId) " +
						"WHERE p.requestId = ? " +
						"ORDER BY createdDate DESC ";
				Collection col = dao.select(sql, ScpService.class, new String[] {requestId}, 0, -1);
				service.setProperty("extraScp", col);
			} else if (ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)) {
				//Collection col = module.getOtherService(requestId, serviceId);
				String sql =
						"SELECT p.id, p.requestId, p.serviceId, p.facilityId, p.quantity, " +
							"p.requiredFrom, p.requiredTo, p.remarks, p.fromTime, p.toTime, " +
							"p.createdBy, p.createdDate, p.modifiedBy, p.modifiedDate, f.name as facility, " +
							"p.internalRate, p.externalRate, " +
							"p.blockBooking, p.location, p.submitted " +
						"FROM fms_eng_service_other p " +
						"LEFT OUTER JOIN fms_rate_card f ON (f.id = p.facilityId) " +
						"WHERE p.requestId = ? " + 
						"ORDER BY createdDate DESC ";
				Collection col = dao.select(sql, OtherService.class, new String[] {requestId}, 0, -1);
				service.setProperty("extraOther", col);
			} else if (ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)) {
				//Collection col = module.getManpowerService(requestId, serviceId);
				String sql =
						"SELECT p.id, p.requestId, p.serviceId, p.competencyId, p.quantity," +
							"p.requiredFrom, p.requiredTo, p.remarks, p.fromTime, p.toTime," +
							"p.createdBy, p.createdDate, p.modifiedBy, p.modifiedDate, f.name as competencyName, " +
							"p.internalRate, p.externalRate," +
							"p.blockBooking, p.location, p.submitted " +
						"FROM fms_eng_service_manpower p " +
						"LEFT OUTER JOIN fms_rate_card f ON (f.id = p.competencyId) " +
						"WHERE p.requestId = ? " +
						"ORDER BY createdDate DESC ";
				Collection col = dao.select(sql, ManpowerService.class, new String[] {requestId}, 0, -1);
				service.setProperty("extraManpower", col);
			} else if (ServiceDetailsForm.SERVICE_VTR.equals(serviceId)) {
				//Collection col = module.getVtrService(requestId, serviceId);
				String sql =
						"SELECT p.id, p.requestId, p.serviceId, p.service, " +
							"p.requiredDate, p.requiredDateTo, p.requiredFrom, p.requiredTo, p.formatFrom, p.formatTo, " +
							"p.conversionFrom, p.conversionTo, p.duration, p.noOfCopies, p.remarks, " +
							"p.createdBy, p.createdDate, p.modifiedBy, p.modifiedDate, " +
							"p.facilityId, f.name as facility, " +
							"p.internalRate, p.externalRate, p.blockBooking, p.location, p.submitted " +
						"FROM fms_eng_service_vtr p " +
						"LEFT OUTER JOIN fms_rate_card f ON (f.id = p.facilityId) " +
						"WHERE p.requestId = ? " +
						"ORDER BY createdDate DESC ";
				Collection col = dao.select(sql, VtrService.class, new String[] {requestId}, 0, -1);
				service.setProperty("extraVTR", col);
			} else if (ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)) {
				//Collection col = module.getTvroService(requestId, serviceId);
				String sql =
						"SELECT p.id, p.requestId, p.serviceId, p.feedTitle, p.location, " +
							"p.requiredDate, p.requiredDateTo, p.timezone, p.totalTimeReq, p.timeMeasure, p.remarks, p.fromTime, p.toTime, " +
							"p.createdBy, p.createdDate, p.modifiedBy, p.modifiedDate, p.feedType," +
							"p.internalRate, p.externalRate," +
							"p.facilityId, f.name as facility, p.blockBooking, p.submitted  " +
						"FROM fms_eng_service_tvro p " +
						"LEFT OUTER JOIN fms_rate_card f ON (f.id = p.facilityId) " +
						"WHERE p.requestId = ? " + 
						"ORDER BY createdDate DESC ";
				Collection col = dao.select(sql, TvroService.class, new String[] {requestId}, 0, -1);
				service.setProperty("extraTVRO", col);
			} else if (ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)) {
				//Collection col = module.getStudioService(requestId, serviceId);
				String sql =
						"SELECT p.id, p.requestId, p.serviceId, p.facilityId, p.bookingDate, p.bookingDateTo, " +
							"p.requiredFrom, p.requiredTo, p.segment, " +
							"p.settingFrom, p.settingTo, p.rehearsalFrom, p.rehearsalTo, p.vtrFrom, p.vtrTo, " +
							"p.createdBy, p.createdDate, p.modifiedBy, p.modifiedDate, f.name as facility, " +
							"p.internalRate, p.externalRate, " +
							"p.blockBooking, p.location, p.submitted " +
						"FROM fms_eng_service_studio p " +
						"LEFT OUTER JOIN fms_rate_card f ON (f.id = p.facilityId) " +
						"WHERE p.requestId = ? " + 
						"ORDER BY createdDate desc";
				Collection col = dao.select(sql, StudioService.class, new String[] {requestId}, 0, -1);
				service.setProperty("extraStudio", col);
			} else if (ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)) {
				//Collection col = module.getPostProductionService(requestId, serviceId);
				String sql =
						"SELECT p.id, p.requestId, p.serviceId, p.facilityId, p.requiredDate, p.fromTime, " +
							"p.toTime, p.createdBy, p.createdDate, p.modifiedBy, p.modifiedDate, f.name as facility, " +
							"p.internalRate, p.externalRate, p.blockBooking, p.submitted, p.requiredDateTo, p.location " +
						"FROM fms_eng_service_postproduction p " +
						"LEFT OUTER JOIN fms_rate_card f ON (f.id = p.facilityId) " +
						"WHERE p.requestId = ? " + 
						"ORDER BY createdDate DESC ";
				Collection col = dao.select(sql, PostProductionService.class, new String[] {requestId}, 0, -1);
				service.setProperty("extraPostProduction", col);
			}
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// facility & manpower booking
	try {
		String sqlFacilityBooking = 
				"SELECT " + topStr + " cat.name as category_name, com.competencyName, " +
					"bookFrom, timeFrom, bookTo, timeTo, quantity, " +
					"fb.facilityId, fb.createdBy, fb.createdOn, fb.bookingType " +
				"FROM fms_facility_booking fb " +
				"LEFT OUTER JOIN fms_eng_rate_card_category cat ON (fb.facilityId = cat.id) " +
				"LEFT OUTER JOIN competency com ON (com.competencyId = fb.facilityId) " +
				"WHERE requestId = ? " +
				"ORDER BY bookFrom, timeFrom, bookTo, timeTo, fb.bookingType ";
		Collection colFacilityBooking = dao.select(sqlFacilityBooking, FacilityObject.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colFacilityBooking", colFacilityBooking);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// requested items
	try {
		String sqlRequestedItems = 
				"SELECT " + topStr + " ae.requiredTo, ae.requiredFrom, ae.fromTime, ae.toTime, ae.barcode, ae.assignmentId, c.name AS catName, " +
				"       checkedOutBy, checkedOutDate, checkedInBy, checkedInDate, takenBy, createdDate, a.code AS assignmentCode, a.serviceId " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_equipment ae ON (ae.assignmentId = a.assignmentId) " + 
				"LEFT OUTER JOIN fms_eng_rate_card_category c ON (c.id = ae.rateCardCategoryId) " +
				"WHERE a.requestId = ? " +
				"ORDER BY ae.requiredFrom, ae.requiredTo ";
		Collection colRequestedItems = dao.select(sqlRequestedItems, EngineeringRequest.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colRequestedItems", colRequestedItems);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// cancelled items
	try {
		String sqlCancelledItems = 
				"SELECT " + topStr + " ae.requiredTo, ae.requiredFrom, ae.fromTime, ae.toTime, ae.barcode, ae.assignmentId, c.name AS catName, " +
				"       ae.checkedOutBy, ae.checkedOutDate, ae.checkedInBy, ae.checkedInDate, ae.takenBy, a.cancelDate, a.code AS assignmentCode " +
				"FROM fms_eng_cancel_service_log a " + 
				"INNER JOIN fms_eng_assignment_equipment ae ON (ae.assignmentId = a.assignmentId) " +   
				"LEFT OUTER JOIN fms_eng_rate_card_category c ON (c.id = ae.rateCardCategoryId) " +
				"WHERE a.requestId = ? " +
				"ORDER BY ae.requiredFrom, ae.requiredTo ";
		Collection colCancelledItems = dao.select(sqlCancelledItems, EngineeringRequest.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colCancelledItems", colCancelledItems);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// requested manpower
	try {
		String sqlRequestedManpower = 
				"SELECT " + topStr + " man.requiredTo, man.requiredFrom, man.assignmentId, man.fromTime, man.toTime, " +
				"       c.competencyName, u.firstName + ' ' + u.lastName as assignedFacility, man.status, createdDate, " +
				"       c.unitId, fu.name AS unitName, a.code AS assignmentCode, a.serviceId " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_manpower man ON (man.assignmentId = a.assignmentId) " +
				"LEFT OUTER JOIN competency c ON (c.competencyId = man.competencyId) " +
				"LEFT OUTER JOIN security_user u ON (man.userId = u.id) " +
				"LEFT OUTER JOIN fms_unit fu ON (c.unitId = fu.id) " +
				"WHERE a.requestId = ? " +
				"ORDER BY man.requiredFrom, man.requiredTo ";
		Collection colRequestedManpower = dao.select(sqlRequestedManpower, Assignment.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colRequestedManpower", colRequestedManpower);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// cancelled manpower
	try {
		String sqlCancelledManpower = 
				"SELECT " + topStr + " man.requiredTo, man.requiredFrom, man.assignmentId, man.fromTime, man.toTime, " +
				"       c.competencyName, u.firstName + ' ' + u.lastName as assignedFacility, man.status, cancelDate, " + 
				"       c.unitId, fu.name AS unitName, man.code AS assignmentCode " +
				"FROM fms_eng_cancel_service_log man " +
				"INNER JOIN competency c ON (c.competencyId = man.competencyId) " +
				"LEFT OUTER JOIN security_user u ON (man.userId = u.id) " +
				"LEFT OUTER JOIN fms_unit fu ON (c.unitId = fu.id) " +
				"WHERE man.requestId = ? " +
				"ORDER BY man.requiredFrom, man.requiredTo ";
		Collection colCancelledManpower = dao.select(sqlCancelledManpower, Assignment.class, new String[] {requestId}, 0, -1);
		pageContext.setAttribute("colCancelledManpower", colCancelledManpower);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// extra items
	try {
		String sqlExtraItems = 
				"SELECT " + topStr + " DISTINCT ae.id, ae.requiredTo, ae.requiredFrom, ae.barcode, " +
				"       checkedOutBy, checkedOutDate, checkedInBy, checkedInDate, takenBy, createdDate " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_equipment ae ON (ae.groupId = a.groupId AND ae.assignmentId = '-') " + 
				"WHERE a.requestId = ? " +
				"ORDER BY ae.requiredFrom, ae.requiredTo ";
		Collection colExtraItems = dao.select(sqlExtraItems, EngineeringRequest.class, new String[] {requestId}, 0, -1);
		
		// get category
		UnitHeadModule uhm = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
		for (Iterator iterator = colExtraItems.iterator(); iterator.hasNext();) {
			EngineeringRequest er = (EngineeringRequest) iterator.next();
			String catName = uhm.getRateCardCategoryName(er.getBarcode());
			er.setProperty("catName", catName);
		}
		
		pageContext.setAttribute("colExtraItems", colExtraItems);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// check rate cards
	SetupModule setupModule = (SetupModule) Application.getInstance().getModule(SetupModule.class);
	boolean validRateCards = setupModule.requestHasValidRateCards(requestId);
%>

<%!
	public String showIdTableData(String id) {
		// old: <td bgcolor="#<c:out value="${fn:substring(rs2.id, 29, 35)}"/>"><c:out value="${rs2.id}" /></td>
		String style = "background-color:#" + id.substring(29, 35) + "; color:#ffffff;";
		return "<td style=\"" + style + "\">" + id + "</td>";
	}
%>

<html>
	<head>
		<style>
			.niceStyle {
				background-color: #CC9966;
				font-weight: bold;
			}
			
			.cancelledStyle {
				background-color: #FF3333;
				color: #FFFFFF;
			}
		</style>
		
		<title>Engineering Request: <%= requestId %></title>
	</head>
	<body>
		<b style="font-size:1.5em;">Engineering Request: <%= requestId %></b><br>
		(v<%= CURRENT_VERSION %>)<br>
		
		<% if (topNum >= 1) { %>
			Showing top <%= topNum %>:
		<% } else { %>
			Showing all:
		<% } %>
		<% if (topNum == 5) { %>
			[show top 5]
		<% } else { %>
			<a href="?top=5&requestId=<%=requestId%>">show top 5</a>
		<% } %>
		<% if (topNum == 10) { %>
			[show top 10]
		<% } else { %>
			<a href="?top=10&requestId=<%=requestId%>">show top 10</a>
		<% } %>
		<% if (topNum < 1) { %>
			[show all]
		<% } else { %>
			<a href="?requestId=<%=requestId%>">show all</a>
		<% } %>
		<br>
		Verbose mode: 
		<% if (verboseMode) { %>
			ON [ <a href="?requestId=<%=requestId%>">toggle</a> ]
		<% } else { %>
			OFF [ <a href="?verbose=1&requestId=<%=requestId%>">toggle</a> ]
		<% } %>
		<br><br>
		
		<% if (!validRateCards) { %>
			<h2>*** <font color="red"><fmt:message key="fms.facility.msg.rateCardInvalid"/></font> ***</h2>
		<% } %>
		
		
		Engineering Request:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td>Request ID</td>
					<td>Title</td>
					<td>Required From</td>
					<td>Required To</td>
					<% if (internalRequest) { %>
						<td align="right">Internal Rate</td>
					<% } else { %>
						<td align="right">External Rate</td>
					<% } %>
					<td>Status</td>
					<td>Status Label</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colRequest}" var="req" varStatus="status">
					<tr>
						<td><c:out value="${req.requestId}" /></td>
						<td><c:out value="${req.title}" /></td>
						<td><fmt:formatDate value="${req.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><fmt:formatDate value="${req.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
						<% if (internalRequest) { %>
							<td align="right">&nbsp;<fmt:formatNumber value="${req.totalInternalRate}" minFractionDigits="2" /></td>
						<% } else { %>
							<td align="right">&nbsp;<fmt:formatNumber value="${req.totalExternalRate}" minFractionDigits="2" /></td>
						<% } %>
						<td><c:out value="${req.status}" /></td>
						<td><c:out value="${req.statusLabel}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Status Trail:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td>Date</td>
					<td>Done By</td>
					<td>Status</td>
					<td>Status Label</td>
					<td>Remarks</td>
					<td>Additional Info</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colStatusTrail}" var="st" varStatus="status">
					<tr>
						<td><fmt:formatDate value="${st.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
						<td><c:out value="${st.createdBy}" /></td>
						<td><c:out value="${st.status}" /></td>
						<td><c:out value="${st.statusLabel}" /></td>
						<td><c:out value="${st.remarks}" />&nbsp;</td>
						<td><c:out value="${st.additionalInfo}" escapeXml="false" />&nbsp;</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Services Required:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td>Service Title</td>
					<td>Service Type</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colRequestedServices}" var="rs" varStatus="status">
					<tr>
						<td><c:out value="${rs.displayTitle}" /></td>
						<td><c:out value="${rs.serviceId}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		<c:forEach items="${colAllServices}" var="rs" varStatus="statusMain">
			<c:set var="serviceId" value="${rs.serviceId}"/>
			<c:set var="missingError" value=""/>
			<%
				String serviceId = (String) pageContext.getAttribute("serviceId");
				
				String missingError = "";
				if (!requestedService.containsKey(serviceId)) {
			%>
				<c:set var="missingError"><font color="red"><b>(Services Required is Missing)</b></font></c:set>
			<%
				}
			%>
			
			<c:if test="${not empty(rs.propertyMap['extraScp'])}">
				Service Type: <c:out value="${rs.displayTitle}" />
				<c:out value="${missingError}" escapeXml="false"/><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<thead class="niceStyle">
						<tr>
							<td rowspan="2">No.</td>
							<td rowspan="2">Facility</td>
							<td rowspan="2">Required From</td>
							<td rowspan="2">Required To</td>
							<td rowspan="2">Departure Time</td>
							<td rowspan="2">Block</td>
							<td align="center" colspan="2">Created</td>
							<td align="center" colspan="2">Modified</td>
							<% if (verboseMode) { %>
								<% if (internalRequest) { %>
									<td align="right" rowspan="2">Internal Rate</td>
								<% } else { %>
									<td align="right" rowspan="2">External Rate</td>
								<% } %>
								<td rowspan="2">Service ID</td>
							<% } %>
						</tr>
						<tr>
							<td align="center">User</td>
							<td align="center">Date</td>
							<td align="center">User</td>
							<td align="center">Date</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rs.propertyMap['extraScp']}" var="rs2" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><a href="rateCardView.jsp?id=<c:out value="${rs2.facilityId}" />"><c:out value="${rs2.facility}" /><c:if test="${empty(rs2.facility)}"><i>-- missing rate card --</i></c:if></a></td>
								<td><fmt:formatDate value="${rs2.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><fmt:formatDate value="${rs2.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.departureTime}" /></td>
								<c:set var="blockBooking" value="${rs2.blockBooking}"/>
								<td><%= formatYesNo(pageContext, "blockBooking") %></td>
								<td><c:out value="${rs2.createdBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<td><c:out value="${rs2.modifiedBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.modifiedDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<% if (verboseMode) { %>
									<% if (internalRequest) { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.internalRate}" minFractionDigits="2" /></td>
									<% } else { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.externalRate}" minFractionDigits="2" /></td>
									<% } %>
									<%= showIdTableData(((DefaultDataObject) pageContext.getAttribute("rs2")).getId()) %>
								<% } %>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty(rs.propertyMap['extraOther'])}">
				Service Type: <c:out value="${rs.displayTitle}" />
				<c:out value="${missingError}" escapeXml="false"/><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<thead class="niceStyle">
						<tr>
							<td rowspan="2">No.</td>
							<td rowspan="2">Facility</td>
							<td align="center" colspan="2">Required From</td>
							<td align="center" colspan="2">Required To</td>
							<td rowspan="2">QTY</td>
							<td rowspan="2">Block</td>
							<td align="center" colspan="2">Created</td>
							<td align="center" colspan="2">Modified</td>
							<% if (verboseMode) { %>
								<% if (internalRequest) { %>
									<td align="right" rowspan="2">Internal Rate</td>
								<% } else { %>
									<td align="right" rowspan="2">External Rate</td>
								<% } %>
								<td rowspan="2">Service ID</td>
							<% } %>
						</tr>
						<tr>
							<td>Date</td>
							<td>Time</td>
							<td>Date</td>
							<td>Time</td>
							<td align="center">User</td>
							<td align="center">Date</td>
							<td align="center">User</td>
							<td align="center">Date</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rs.propertyMap['extraOther']}" var="rs2" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><a href="rateCardView.jsp?id=<c:out value="${rs2.facilityId}" />"><c:out value="${rs2.facility}" /><c:if test="${empty(rs2.facility)}"><i>-- missing rate card --</i></c:if></a></td>
								<td><fmt:formatDate value="${rs2.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.fromTime}" /></td>
								<td><fmt:formatDate value="${rs2.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.toTime}" /></td>
								<td><c:out value="${rs2.quantity}" /></td>
								<c:set var="blockBooking" value="${rs2.blockBooking}"/>
								<td><%= formatYesNo(pageContext, "blockBooking") %></td>
								<td><c:out value="${rs2.createdBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<td><c:out value="${rs2.modifiedBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.modifiedDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<% if (verboseMode) { %>
									<% if (internalRequest) { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.internalRate}" minFractionDigits="2" /></td>
									<% } else { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.externalRate}" minFractionDigits="2" /></td>
									<% } %>
									<%= showIdTableData(((DefaultDataObject) pageContext.getAttribute("rs2")).getId()) %>
								<% } %>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty(rs.propertyMap['extraManpower'])}">
				Service Type: <c:out value="${rs.displayTitle}" />
				<c:out value="${missingError}" escapeXml="false"/><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<thead class="niceStyle">
						<tr>
							<td rowspan="2">No.</td>
							<td rowspan="2">Competency Name</td>
							<td align="center" colspan="2">Required From</td>
							<td align="center" colspan="2">Required To</td>
							<td rowspan="2">QTY</td>
							<td rowspan="2">Block</td>
							<td align="center" colspan="2">Created</td>
							<td align="center" colspan="2">Modified</td>
							<% if (verboseMode) { %>
								<% if (internalRequest) { %>
									<td align="right" rowspan="2">Internal Rate</td>
								<% } else { %>
									<td align="right" rowspan="2">External Rate</td>
								<% } %>
								<td rowspan="2">Service ID</td>
							<% } %>
						</tr>
						<tr>
							<td>Date</td>
							<td>Time</td>
							<td>Date</td>
							<td>Time</td>
							<td align="center">User</td>
							<td align="center">Date</td>
							<td align="center">User</td>
							<td align="center">Date</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rs.propertyMap['extraManpower']}" var="rs2" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><a href="rateCardView.jsp?id=<c:out value="${rs2.competencyId}" />"><c:out value="${rs2.competencyName}" /><c:if test="${empty(rs2.competencyName)}"><i>-- missing rate card --</i></c:if></a></td>
								<td><fmt:formatDate value="${rs2.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.fromTime}" /></td>
								<td><fmt:formatDate value="${rs2.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.toTime}" /></td>
								<td><c:out value="${rs2.quantity}" /></td>
								<c:set var="blockBooking" value="${rs2.blockBooking}"/>
								<td><%= formatYesNo(pageContext, "blockBooking") %></td>
								<td><c:out value="${rs2.createdBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<td><c:out value="${rs2.modifiedBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.modifiedDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<% if (verboseMode) { %>
									<% if (internalRequest) { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.internalRate}" minFractionDigits="2" /></td>
									<% } else { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.externalRate}" minFractionDigits="2" /></td>
									<% } %>
									<%= showIdTableData(((DefaultDataObject) pageContext.getAttribute("rs2")).getId()) %>
								<% } %>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty(rs.propertyMap['extraTVRO'])}">
				Service Type: <c:out value="${rs.displayTitle}" />
				<c:out value="${missingError}" escapeXml="false"/><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<thead class="niceStyle">
						<tr>
							<td rowspan="2">No.</td>
							<td rowspan="2">Facility</td>
							<td rowspan="2">Feed Title</td>
							<td align="center" colspan="2">Required From</td>
							<td align="center" colspan="2">Required To</td>
							<td rowspan="2">Block</td>
							<td align="center" colspan="2">Created</td>
							<td align="center" colspan="2">Modified</td>
							<% if (verboseMode) { %>
								<% if (internalRequest) { %>
									<td align="right" rowspan="2">Internal Rate</td>
								<% } else { %>
									<td align="right" rowspan="2">External Rate</td>
								<% } %>
								<td rowspan="2">Service ID</td>
							<% } %>
						</tr>
						<tr>
							<td>Date</td>
							<td>Time</td>
							<td>Date</td>
							<td>Time</td>
							<td align="center">User</td>
							<td align="center">Date</td>
							<td align="center">User</td>
							<td align="center">Date</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rs.propertyMap['extraTVRO']}" var="rs2" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><a href="rateCardView.jsp?id=<c:out value="${rs2.facilityId}" />"><c:out value="${rs2.facility}" /><c:if test="${empty(rs2.facility)}"><i>-- missing rate card --</i></c:if></a></td>
								<td><c:out value="${rs2.feedTitle}" /></td>
								<td><fmt:formatDate value="${rs2.requiredDate}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.fromTime}" /></td>
								<td><fmt:formatDate value="${rs2.requiredDateTo}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.toTime}" /></td>
								<c:set var="blockBooking" value="${rs2.blockBooking}"/>
								<td><%= formatYesNo(pageContext, "blockBooking") %></td>
								<td><c:out value="${rs2.createdBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<td><c:out value="${rs2.modifiedBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.modifiedDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<% if (verboseMode) { %>
									<% if (internalRequest) { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.internalRate}" minFractionDigits="2" /></td>
									<% } else { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.externalRate}" minFractionDigits="2" /></td>
									<% } %>
									<%= showIdTableData(((DefaultDataObject) pageContext.getAttribute("rs2")).getId()) %>
								<% } %>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty(rs.propertyMap['extraStudio'])}">
				Service Type: <c:out value="${rs.displayTitle}" />
				<c:out value="${missingError}" escapeXml="false"/><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<thead class="niceStyle">
						<tr>
							<td rowspan="2">No.</td>
							<td rowspan="2">Facility</td>
							<td align="center" colspan="2">Required From</td>
							<td align="center" colspan="2">Required To</td>
							<td rowspan="2">Block</td>
							<td align="center" colspan="2">Created</td>
							<td align="center" colspan="2">Modified</td>
							<% if (verboseMode) { %>
								<% if (internalRequest) { %>
									<td align="right" rowspan="2">Internal Rate</td>
								<% } else { %>
									<td align="right" rowspan="2">External Rate</td>
								<% } %>
								<td rowspan="2">Service ID</td>
							<% } %>
						</tr>
						<tr>
							<td>Date</td>
							<td>Time</td>
							<td>Date</td>
							<td>Time</td>
							<td align="center">User</td>
							<td align="center">Date</td>
							<td align="center">User</td>
							<td align="center">Date</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rs.propertyMap['extraStudio']}" var="rs2" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><a href="rateCardView.jsp?id=<c:out value="${rs2.facilityId}" />"><c:out value="${rs2.facility}" /><c:if test="${empty(rs2.facility)}"><i>-- missing rate card --</i></c:if></a></td>
								<td><fmt:formatDate value="${rs2.bookingDate}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.requiredFrom}" /></td>
								<td><fmt:formatDate value="${rs2.bookingDateTo}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.requiredTo}" /></td>
								<c:set var="blockBooking" value="${rs2.blockBooking}"/>
								<td><%= formatYesNo(pageContext, "blockBooking") %></td>
								<td><c:out value="${rs2.createdBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<td><c:out value="${rs2.modifiedBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.modifiedDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<% if (verboseMode) { %>
									<% if (internalRequest) { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.internalRate}" minFractionDigits="2" /></td>
									<% } else { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.externalRate}" minFractionDigits="2" /></td>
									<% } %>
									<%= showIdTableData(((DefaultDataObject) pageContext.getAttribute("rs2")).getId()) %>
								<% } %>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty(rs.propertyMap['extraPostProduction'])}">
				Service Type: <c:out value="${rs.displayTitle}" />
				<c:out value="${missingError}" escapeXml="false"/><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<thead class="niceStyle">
						<tr>
							<td rowspan="2">No.</td>
							<td rowspan="2">Facility</td>
							<td align="center" colspan="2">Required From</td>
							<td align="center" colspan="2">Required To</td>
							<td rowspan="2">Block</td>
							<td align="center" colspan="2">Created</td>
							<td align="center" colspan="2">Modified</td>
							<% if (verboseMode) { %>
								<% if (internalRequest) { %>
									<td align="right" rowspan="2">Internal Rate</td>
								<% } else { %>
									<td align="right" rowspan="2">External Rate</td>
								<% } %>
								<td rowspan="2">Service ID</td>
							<% } %>
						</tr>
						<tr>
							<td>Date</td>
							<td>Time</td>
							<td>Date</td>
							<td>Time</td>
							<td align="center">User</td>
							<td align="center">Date</td>
							<td align="center">User</td>
							<td align="center">Date</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rs.propertyMap['extraPostProduction']}" var="rs2" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><a href="rateCardView.jsp?id=<c:out value="${rs2.facilityId}" />"><c:out value="${rs2.facility}" /><c:if test="${empty(rs2.facility)}"><i>-- missing rate card --</i></c:if></a></td>
								<td><fmt:formatDate value="${rs2.requiredDate}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.fromTime}" /></td>
								<td><fmt:formatDate value="${rs2.requiredDateTo}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.toTime}" /></td>
								<c:set var="blockBooking" value="${rs2.blockBooking}"/>
								<td><%= formatYesNo(pageContext, "blockBooking") %></td>
								<td><c:out value="${rs2.createdBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<td><c:out value="${rs2.modifiedBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.modifiedDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<% if (verboseMode) { %>
									<% if (internalRequest) { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.internalRate}" minFractionDigits="2" /></td>
									<% } else { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.externalRate}" minFractionDigits="2" /></td>
									<% } %>
									<%= showIdTableData(((DefaultDataObject) pageContext.getAttribute("rs2")).getId()) %>
								<% } %>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>
			</c:if>
			<c:if test="${not empty(rs.propertyMap['extraVTR'])}">
				Service Type: <c:out value="${rs.displayTitle}" />
				<c:out value="${missingError}" escapeXml="false"/><br>
				<table border="1" cellpadding="3" cellspacing="0">
					<thead class="niceStyle">
						<tr>
							<td rowspan="2">No.</td>
							<td rowspan="2">Facility</td>
							<td align="center" colspan="2">Required From</td>
							<td align="center" colspan="2">Required To</td>
							<td rowspan="2">Block</td>
							<td align="center" colspan="2">Created</td>
							<td align="center" colspan="2">Modified</td>
							<% if (verboseMode) { %>
								<% if (internalRequest) { %>
									<td align="right" rowspan="2">Internal Rate</td>
								<% } else { %>
									<td align="right" rowspan="2">External Rate</td>
								<% } %>
								<td rowspan="2">Service ID</td>
							<% } %>
						</tr>
						<tr>
							<td>Date</td>
							<td>Time</td>
							<td>Date</td>
							<td>Time</td>
							<td align="center">User</td>
							<td align="center">Date</td>
							<td align="center">User</td>
							<td align="center">Date</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rs.propertyMap['extraVTR']}" var="rs2" varStatus="status">
							<tr>
								<td><c:out value="${status.count}" /></td>
								<td><a href="rateCardView.jsp?id=<c:out value="${rs2.facilityId}" />"><c:out value="${rs2.facility}" /><c:if test="${empty(rs2.facility)}"><i>-- missing rate card --</i></c:if></a></td>
								<td><fmt:formatDate value="${rs2.requiredDate}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.requiredFrom}" /></td>
								<td><fmt:formatDate value="${rs2.requiredDateTo}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${rs2.requiredTo}" /></td>
								<c:set var="blockBooking" value="${rs2.blockBooking}"/>
								<td><%= formatYesNo(pageContext, "blockBooking") %></td>
								<td><c:out value="${rs2.createdBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<td><c:out value="${rs2.modifiedBy}" />&nbsp;</td>
								<td><fmt:formatDate value="${rs2.modifiedDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
								<% if (verboseMode) { %>
									<% if (internalRequest) { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.internalRate}" minFractionDigits="2" /></td>
									<% } else { %>
										<td align="right">&nbsp;<fmt:formatNumber value="${rs2.externalRate}" minFractionDigits="2" /></td>
									<% } %>
									<%= showIdTableData(((DefaultDataObject) pageContext.getAttribute("rs2")).getId()) %>
								<% } %>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>
			</c:if>
		</c:forEach>
		
		Facility & Manpower Booking:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">No.</td>
					<td rowspan="2">Category Name</td>
					<td rowspan="2">Competency</td>
					<td align="center" colspan="2">Book From</td>
					<td align="center" colspan="2">Book To</td>
					<td rowspan="2">QTY</td>
					<td rowspan="2">Booking<br> Type</td>
					<td align="center" colspan="2">Created</td>
				</tr>
				<tr>
					<td>Date</td>
					<td>Time</td>
					<td>Date</td>
					<td>Time</td>
					<td align="center">User</td>
					<td align="center">Date</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colFacilityBooking}" var="fb" varStatus="status">
					<tr>
						<td><c:out value="${status.count}" /></td>
						<td><a href="rateCardCategoryEdit.jsp?id=<c:out value="${fb.facilityId}" />"><c:out value="${fb.category_name}" /><c:if test="${fb.bookingType == 'F' && empty(fb.category_name)}"><i>-- missing category --</i></c:if></a>&nbsp;</td>
						<td><a href="/ekms/sysadmin/competencyOpen.jsp?competencyId=<c:out value="${fb.facilityId}" />"><c:out value="${fb.propertyMap['competencyName']}" /><c:if test="${fb.bookingType == 'M' && empty(fb.propertyMap['competencyName'])}"><i>-- missing competency --</i></c:if></a>&nbsp;</td>
						<td><fmt:formatDate value="${fb.bookFrom}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${fb.timeFrom}" /></td>
						<td><fmt:formatDate value="${fb.bookTo}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${fb.timeTo}" /></td>
						<td><c:out value="${fb.quantity}" /></td>
						<td><c:out value="${fb.bookingType}" /></td>
						<td><c:out value="${fb.propertyMap['createdBy']}" /></td>
						<td><fmt:formatDate value="${fb.propertyMap['createdOn']}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Requested Items:<br>
		<table align="center" border="1" cellpadding="3" cellspacing="0" width="100%">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">No.</td>
					<td align="center" colspan="2">Required From</td>
					<td align="center" colspan="2">Required To</td>
					<td rowspan="2">Type</td>
					<td rowspan="2">Barcode</td>
					<td align="center" colspan="2">Checked Out</td>
					<td align="center" colspan="2">Checked In</td>
					<td rowspan="2">Taken By</td>
					<td rowspan="2">Created/Cancel Date</td>
					<td rowspan="2">Assignment No</td>
					<% if (verboseMode) { %>
						<td rowspan="2">Service ID</td>
					<% } %>
				</tr>
				<tr>
					<td>Date</td>
					<td>Time</td>
					<td>Date</td>
					<td>Time</td>
					<td align="center">User</td>
					<td align="center">Date</td>
					<td align="center">User</td>
					<td align="center">Date</td>
				</tr>
			</thead>
			<tbody>
				<c:set var="lastCount" value="0"/>
				<c:forEach items="${colRequestedItems}" var="req" varStatus="status">
					<tr>
						<td><c:out value="${status.count}" /></td>
						<td><fmt:formatDate value="${req.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.fromTime}" /></td>
						<td><fmt:formatDate value="${req.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.toTime}" /></td>
						<td><c:out value="${req.propertyMap['catName']}" />&nbsp;</td>
						<td><c:out value="${req.barcode}" />&nbsp;</td>
						
						<td><c:out value="${req.checkedOutBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.checkedOutDate}" pattern="<%= DATE_TIME_PATTERN %>"/>&nbsp;</td>
						
						<td><c:out value="${req.checkedInBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.checkedInDate}" pattern="<%= DATE_TIME_PATTERN %>" />&nbsp;</td>
						
						<td><c:out value="${req.takenBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.propertyMap['createdDate']}" pattern="<%= DATE_TIME_PATTERN %>"/>&nbsp;</td>
						<td><c:out value="${req.assignmentCode}" />&nbsp;</td>
						<% if (verboseMode) { %>
							<%= showIdTableData(((EngineeringRequest) pageContext.getAttribute("req")).getServiceId()) %>
						<% } %>
					</tr>
					<c:set var="lastCount" value="${status.count}"/>
				</c:forEach>
				<c:forEach items="${colCancelledItems}" var="req" varStatus="status">
					<tr class="cancelledStyle">
						<td><c:out value="${lastCount + status.count}" /></td>
						<td><fmt:formatDate value="${req.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.fromTime}" /></td>
						<td><fmt:formatDate value="${req.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.toTime}" /></td>
						<td><c:out value="${req.propertyMap['catName']}" />&nbsp;</td>
						<td><c:out value="${req.barcode}" />&nbsp;</td>
						
						<td><c:out value="${req.checkedOutBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.checkedOutDate}" pattern="<%= DATE_TIME_PATTERN %>"/>&nbsp;</td>
						
						<td><c:out value="${req.checkedInBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.checkedInDate}" pattern="<%= DATE_TIME_PATTERN %>" />&nbsp;</td>
						
						<td><c:out value="${req.takenBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.cancelDate}" pattern="<%= DATE_TIME_PATTERN %>"/>&nbsp;</td>
						<td><c:out value="${req.assignmentCode}" />&nbsp;</td>
						<% if (verboseMode) { %>
							<td>Not applicable</td>
						<% } %>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Requested Manpower:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">No.</td>
					<td align="center" colspan="2">Required From</td>
					<td align="center" colspan="2">Required To</td>
					<td rowspan="2">Type</td>
					<td rowspan="2">Unit</td>
					<td rowspan="2">Assigned Manpower</td>
					<td rowspan="2">Status</td>
					<td rowspan="2">Created/Cancel Date</td>
					<td rowspan="2">Assignment No</td>
					<% if (verboseMode) { %>
						<td rowspan="2">Service ID</td>
					<% } %>
				</tr>
				<tr>
					<td>Date</td>
					<td>Time</td>
					<td>Date</td>
					<td>Time</td>
				</tr>
			</thead>
			<tbody>
				<c:set var="lastCount" value="0"/>
				<c:forEach items="${colRequestedManpower}" var="req" varStatus="status">
					<tr>
						<td><c:out value="${status.count}" /></td>
						<td><fmt:formatDate value="${req.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.fromTime}" /></td>
						<td><fmt:formatDate value="${req.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.toTime}" /></td>
						<td><c:out value="${req.competencyName}" />&nbsp;</td>
						<td><a href="/ekms/sysadmin/fmsUnitSetup.jsp?id=<c:out value="${req.unitId}" />"><c:out value="${req.propertyMap['unitName']}" /></a>&nbsp;</td>
						<td><c:out value="${req.assignedFacility}" />&nbsp;</td>
						<td><c:out value="${req.status}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.createdDate}" pattern="<%= DATE_TIME_PATTERN %>"/>&nbsp;</td>
						<td><c:out value="${req.propertyMap['assignmentCode']}" />&nbsp;</td>
						<% if (verboseMode) { %>
							<%= showIdTableData(((Assignment) pageContext.getAttribute("req")).getServiceId()) %>
						<% } %>
					</tr>
					<c:set var="lastCount" value="${status.count}"/>
				</c:forEach>
				<c:forEach items="${colCancelledManpower}" var="req" varStatus="status">
					<tr class="cancelledStyle">
						<td><c:out value="${lastCount + status.count}" /></td>
						<td><fmt:formatDate value="${req.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.fromTime}" /></td>
						<td><fmt:formatDate value="${req.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.toTime}" /></td>
						<td><c:out value="${req.competencyName}" />&nbsp;</td>
						<td><a href="/ekms/sysadmin/fmsUnitSetup.jsp?id=<c:out value="${req.unitId}" />"><c:out value="${req.propertyMap['unitName']}" /></a>&nbsp;</td>
						<td><c:out value="${req.assignedFacility}" />&nbsp;</td>
						<td><c:out value="${req.status}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.propertyMap['cancelDate']}" pattern="<%= DATE_TIME_PATTERN %>"/>&nbsp;</td>
						<td><c:out value="${req.propertyMap['assignmentCode']}" />&nbsp;</td>
						<% if (verboseMode) { %>
							<td>Not applicable</td>
						<% } %>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Extra Items:
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">No.</td>
					<td rowspan="2">Required From</td>
					<td rowspan="2">Required To</td>
					<td rowspan="2">Type</td>
					<td rowspan="2">Barcode</td>
					<td align="center" colspan="2">Checked Out</td>
					<td align="center" colspan="2">Checked In</td>
					<td rowspan="2">Taken By</td>
				</tr>
				<tr>
					<td align="center">User</td>
					<td align="center">Date</td>
					<td align="center">User</td>
					<td align="center">Date</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colExtraItems}" var="req" varStatus="status">
					<tr>
						<td><c:out value="${status.count}" /></td>
						<td><fmt:formatDate value="${req.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><fmt:formatDate value="${req.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
						<td><c:out value="${req.propertyMap['catName']}" />&nbsp;</td>
						<td><c:out value="${req.barcode}" />&nbsp;</td>
						
						<td><c:out value="${req.checkedOutBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.checkedOutDate}" pattern="<%= DATE_TIME_PATTERN %>"/>&nbsp;</td>
						
						<td><c:out value="${req.checkedInBy}" />&nbsp;</td>
						<td><fmt:formatDate value="${req.checkedInDate}" pattern="<%= DATE_TIME_PATTERN %>" />&nbsp;</td>
						
						<td><c:out value="${req.takenBy}" />&nbsp;</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Transport Request:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td>ID</td>
					<td>Start Date</td>
					<td>End Date</td>
					<td>Status</td>
					<td>Request By</td>
					<td>Request Date</td>
					<td>Request Title</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colTransport}" var="tr" varStatus="status">
					<tr>
						<td><c:out value="${tr.id}" /></td>
						<td><fmt:formatDate value="${tr.startDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
						<td><fmt:formatDate value="${tr.endDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
						<td><c:out value="${tr.status}" /></td>
						<td><c:out value="${tr.propertyMap['username']}" />&nbsp;</td>
						<td><fmt:formatDate value="${tr.requestDate}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
						<td><c:out value="${tr.requestTitle}" />&nbsp;</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
	</body>
</html>