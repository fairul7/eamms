<%@ page import="kacang.*, kacang.model.*, kacang.util.*, java.util.*, com.tms.fms.engineering.model.*" %>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<%! // misc. methods
	public static final String DATE_PATTERN = "dd-MMM-yyyy";
	
	private String getServiceCheckSQL(String serviceTable) {
		String sql =
			"SELECT iSrv.requestId, req.title, req.status " +
			"FROM " + serviceTable + " iSrv " +
			"INNER JOIN fms_eng_request req ON (iSrv.requestId = req.requestId) " +
			"LEFT OUTER JOIN fms_eng_request_services serv ON (iSrv.requestId = serv.requestId AND iSrv.serviceId = serv.serviceId) " +
			"WHERE serv.serviceId IS NULL " + 
			"GROUP BY iSrv.requestId, iSrv.serviceId, req.title, req.status ";
		
		return sql;
	}
	
	private String getServiceInfoSQL(String serviceTable) {
		String facilityColumn = "facilityId";
		if (serviceTable.equals("fms_eng_service_manpower")) {
			facilityColumn = "competencyId";
		}
		
		String sql =
			"SELECT iSrv.id, iSrv.requestId, iSrv.serviceId, req.title, req.status, serv.serviceId AS chkServiceId, " + 
			"       rc.name as facility, rc.id as rateCardId, '" + serviceTable + "' as tableName " +
			"FROM " + serviceTable + " iSrv " +
			"INNER JOIN fms_eng_request req ON (iSrv.requestId = req.requestId) " +
			"LEFT OUTER JOIN fms_rate_card rc ON (rc.id = iSrv." + facilityColumn + ") " +
			"LEFT OUTER JOIN fms_eng_request_services serv ON (iSrv.requestId = serv.requestId AND iSrv.serviceId = serv.serviceId) " +
			"WHERE iSrv.requestId = ? ";
		
		return sql;
	}
	
	private void markDuplicateBooking(Collection colBooking, Collection colRecord) {
		HashMap servMap = new HashMap();
		for (Iterator iterator = colRecord.iterator(); iterator.hasNext();) {
			EngineeringRequest request = (EngineeringRequest) iterator.next();
			servMap.put(request.getRateCardId(), "");
		}
		
		for (Iterator iterator = colBooking.iterator(); iterator.hasNext();) {
			EngineeringRequest request = (EngineeringRequest) iterator.next();
			String bookingRateCard = request.getRateCardId();
			if (servMap.containsKey(bookingRateCard)) {
				request.setProperty("dup", "0");
			} else {
				request.setProperty("dup", "1");
			}
		}
	}
	
	private String getDiffTime(long start, long end) {
		long ms = end - start;
		long s = ms / 1000;
		ms = ms % 1000;
		long min = s / 60;
		s = s % 60;
		long h = min / 60;
		min = min % 60;
		
		String result = "";
		if (h != 0) {
			result += h + "h ";
		}
		if (min != 0) {
			result += min + "min ";
		}
		if (s != 0) {
			result += s + "s ";
		}
		if (ms != 0) {
			result += ms + "ms ";
		}
		return result;
	}
%>

<%! // query record
	public void queryInfo(EngineeringDao dao, String showRequestId, PageContext pageContext) {
		// get main
		try {
			String sqlMain = 
				"SELECT req.requestId, req.title, req.status " +
				"FROM fms_eng_request req  " +
				"WHERE req.requestId = ? ";
			
			Collection colMain = dao.select(sqlMain, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colMain", colMain);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// get particular service without service required
		try {
			String sqlRecord = 
				getServiceInfoSQL("fms_eng_service_scp") +
					"UNION ALL " +
				getServiceInfoSQL("fms_eng_service_other") +			
					"UNION ALL " +
				getServiceInfoSQL("fms_eng_service_manpower") +
					"UNION ALL " +
				getServiceInfoSQL("fms_eng_service_vtr") +
					"UNION ALL " +
				getServiceInfoSQL("fms_eng_service_tvro") +
					"UNION ALL " +
				getServiceInfoSQL("fms_eng_service_studio") +
					"UNION ALL " +
				getServiceInfoSQL("fms_eng_service_postproduction") +
				"ORDER BY iSrv.requestId, iSrv.serviceId ";
			
			Collection colRecord = dao.select(sqlRecord, EngineeringRequest.class, new String[] {showRequestId, showRequestId, showRequestId, showRequestId, showRequestId, showRequestId, showRequestId}, 0, -1);
			pageContext.setAttribute("colRecord", colRecord);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// fms_eng_request_unit
		try {
			String sqlUnit = 
				"SELECT unt.id, unt.requestId, unt.serviceId, unt.unitId, unt.rateCardId, req.status, serv.serviceId AS chkServiceId, f.name AS facility " +
				"FROM fms_eng_request_unit unt " +
				"INNER JOIN fms_eng_request req ON (unt.requestId = req.requestId) " +
				"LEFT OUTER JOIN fms_rate_card f ON (f.id = unt.rateCardId) " +
				"LEFT OUTER JOIN fms_eng_request_services serv ON (unt.requestId = serv.requestId AND unt.serviceId = serv.serviceId) " +
				"WHERE unt.requestId = ? " +
				"ORDER BY unt.requestId, unt.serviceId, unt.unitId ";
			
			Collection colUnit = dao.select(sqlUnit, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colUnit", colUnit);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// fms_facility_booking (facility)
		try {
			String sqlFacilityBooking = 
				"SELECT book.id, book.requestId, rc.serviceTypeId AS serviceId, req.status, cat.name as category_name, " + 
				"       rc.id AS rateCardId, rc.name AS rateCardName, serv.serviceId AS chkServiceId, " +
				"       book.bookFrom, book.timeFrom, book.bookTo, book.timeTo " +
				"FROM fms_facility_booking book " +
				"INNER JOIN fms_eng_request req ON (book.requestId = req.requestId) " +
				"INNER JOIN fms_eng_rate_card_category cat ON (book.facilityId = cat.id) " +
				"INNER JOIN fms_rate_card_equipment rce ON (cat.id = rce.equipment) " +
				"INNER JOIN fms_rate_card rc ON (rce.rateCardId = rc.id) " +
				"LEFT OUTER JOIN fms_eng_request_services serv ON (book.requestId = serv.requestId AND rc.serviceTypeId = serv.serviceId) " +
				"WHERE book.requestId = ? " +
				"ORDER BY book.requestId, rc.serviceTypeId, cat.name, rc.name, book.bookFrom ";
			
			Collection colFacilityBooking = dao.select(sqlFacilityBooking, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colFacilityBooking", colFacilityBooking);
			
			Collection colRecord = (Collection) pageContext.getAttribute("colRecord");
			markDuplicateBooking(colFacilityBooking, colRecord);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// fms_facility_booking (manpower)
		try {
			String sqlManpowerBooking = 
				"SELECT book.id, book.requestId, rc.serviceTypeId AS serviceId, req.status, com.competencyName, " +
				"       rc.id AS rateCardId, rc.name AS rateCardName, serv.serviceId AS chkServiceId,  " +
				"       book.bookFrom, book.timeFrom, book.bookTo, book.timeTo " +
				"FROM fms_facility_booking book " +
				"INNER JOIN fms_eng_request req ON (book.requestId = req.requestId) " +
				"INNER JOIN competency com ON (com.competencyId = book.facilityId) " +
				"INNER JOIN fms_rate_card_manpower rcm ON (com.competencyId = rcm.manpower) " +
				"INNER JOIN fms_rate_card rc ON (rcm.rateCardId = rc.id) " +
				"LEFT OUTER JOIN fms_eng_request_services serv ON (book.requestId = serv.requestId AND rc.serviceTypeId = serv.serviceId) " +
				"WHERE book.requestId = ? " +
				"ORDER BY book.requestId, rc.serviceTypeId, com.competencyName, rc.name, book.bookFrom ";
			
			Collection colManpowerBooking = dao.select(sqlManpowerBooking, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colManpowerBooking", colManpowerBooking);
			
			Collection colRecord = (Collection) pageContext.getAttribute("colRecord");
			markDuplicateBooking(colManpowerBooking, colRecord);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// fms_eng_resources_report
		try {
			String sqlResource = 
				"SELECT res.id, res.requestId, res.serviceId, req.status, serv.serviceId AS chkServiceId, f.name AS facility, " +
				"       res.bookDate " +
				"FROM fms_eng_resources_report res " +
				"INNER JOIN fms_eng_request req ON (res.requestId = req.requestId) " +
				"LEFT OUTER JOIN fms_rate_card f ON (f.id = res.facilityId) " +
				"LEFT OUTER JOIN fms_eng_request_services serv ON (res.requestId = serv.requestId AND res.serviceId = serv.serviceId) " +
				"WHERE res.requestId = ? " +
				"ORDER BY res.requestId, res.serviceId, f.name, res.bookDate ";
			
			Collection colResource = dao.select(sqlResource, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colResource", colResource);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// fms_eng_assignment
		try {
			String sqlAssigment = 
				"SELECT a.requestId, a.serviceType AS serviceId, code, groupId, assignmentType, assignmentId, serv.serviceId AS chkServiceId " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_request req ON (a.requestId = req.requestId) " +
				"LEFT OUTER JOIN fms_eng_request_services serv ON (a.requestId = serv.requestId AND a.serviceType = serv.serviceId) " +
				"WHERE a.requestId = ? " +
				"ORDER BY a.requestId, a.serviceType, code ";
			
			Collection colAssignment = dao.select(sqlAssigment, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colAssignment", colAssignment);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// fms_eng_assignment_equipment
		try {
			String sqlAssigmentEquipment = 
				"SELECT a.requestId, a.serviceType AS serviceId, a.groupId, a.assignmentType, a.assignmentId, " +
				"       rc.name AS rateCardName, serv.serviceId AS chkServiceId " +
				"FROM fms_eng_assignment_equipment eq " +
				"INNER JOIN fms_eng_assignment a ON (a.assignmentId = eq.assignmentId) " +
				"INNER JOIN fms_eng_request req ON (a.requestId = req.requestId) " +
				"INNER JOIN fms_rate_card rc ON (eq.rateCardId = rc.id) " +
				"LEFT OUTER JOIN fms_eng_request_services serv ON (a.requestId = serv.requestId AND a.serviceType = serv.serviceId) " +
				"WHERE a.requestId = ? " +
				"ORDER BY a.requestId, a.serviceType, code ";
			
			Collection colAssignmentEquipment = dao.select(sqlAssigmentEquipment, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colAssignmentEquipment", colAssignmentEquipment);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// fms_eng_assignment_manpower
		try {
			String sqlAssigmentManpower = 
				"SELECT a.requestId, a.serviceType AS serviceId, a.groupId, a.assignmentType, a.assignmentId, " +
				"       mp.requiredFrom, mp.requiredTo, mp.fromTime, mp.toTime, " +
				"       rc.name AS rateCardName, serv.serviceId AS chkServiceId " +
				"FROM fms_eng_assignment_manpower mp " +
				"INNER JOIN fms_eng_assignment a ON (a.assignmentId = mp.assignmentId) " +
				"INNER JOIN fms_eng_request req ON (a.requestId = req.requestId) " +
				"INNER JOIN fms_rate_card rc ON (mp.rateCardId = rc.id) " +
				"LEFT OUTER JOIN fms_eng_request_services serv ON (a.requestId = serv.requestId AND a.serviceType = serv.serviceId) " +
				"WHERE a.requestId = ? " +
				"ORDER BY a.requestId, a.serviceType, code ";
			
			Collection colAssignmentManpower = dao.select(sqlAssigmentManpower, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colAssignmentManpower", colAssignmentManpower);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
%>

<%! // delete records
	public void deleteInfo(EngineeringDao dao, String showRequestId, PageContext pageContext) {
		// delete from fms_eng_assignment_manpower
		try {
			Collection colAssignmentManpower = (Collection) pageContext.getAttribute("colAssignmentManpower");
			for (Iterator iterator = colAssignmentManpower.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				if (chkServiceId == null) {
					String assignmentId = request.getAssignmentId();
					String serviceId = request.getServiceId();
					Log.getLog(getClass()).info("DEL fms_eng_assignment_manpower: requestId=" + showRequestId + " serviceId=" + serviceId + " assignmentId=" + assignmentId);
					
					String sqlDel =
							"DELETE FROM fms_eng_assignment_manpower " +
							"WHERE assignmentId = ? ";
					dao.update(sqlDel, new String[] {assignmentId});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete from fms_eng_assignment_equipment
		try {
			Collection colAssignmentEquipment = (Collection) pageContext.getAttribute("colAssignmentEquipment");
			for (Iterator iterator = colAssignmentEquipment.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				if (chkServiceId == null) {
					String assignmentId = request.getAssignmentId();
					String serviceId = request.getServiceId();
					Log.getLog(getClass()).info("DEL fms_eng_assignment_equipment: requestId=" + showRequestId + " serviceId=" + serviceId + " assignmentId=" + assignmentId);
					
					String sqlDel =
							"DELETE FROM fms_eng_assignment_equipment " +
							"WHERE assignmentId = ? ";
					dao.update(sqlDel, new String[] {assignmentId});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete from fms_eng_assignment
		try {
			Collection colAssignment = (Collection) pageContext.getAttribute("colAssignment");
			for (Iterator iterator = colAssignment.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				if (chkServiceId == null) {
					String assignmentId = request.getAssignmentId();
					String serviceId = request.getServiceId();
					Log.getLog(getClass()).info("DEL fms_eng_assignment: requestId=" + showRequestId + " serviceId=" + serviceId + " assignmentId=" + assignmentId);
					
					String sqlDel =
							"DELETE FROM fms_eng_assignment " +
							"WHERE assignmentId = ? ";
					dao.update(sqlDel, new String[] {assignmentId});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete from fms_eng_resources_report
		try {
			Collection colResource = (Collection) pageContext.getAttribute("colResource");
			for (Iterator iterator = colResource.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				if (chkServiceId == null) {
					String id = request.getId();
					String serviceId = request.getServiceId();
					Log.getLog(getClass()).info("DEL fms_eng_resources_report: requestId=" + showRequestId + " serviceId=" + serviceId + " id=" + id);
					
					String sqlDel =
							"DELETE FROM fms_eng_resources_report " +
							"WHERE id = ? ";
					dao.update(sqlDel, new String[] {id});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete from fms_facility_booking (manpower)
		try {
			Collection colManpowerBooking = (Collection) pageContext.getAttribute("colManpowerBooking");
			for (Iterator iterator = colManpowerBooking.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				String dup = (String) request.getProperty("dup");
				if (chkServiceId == null && dup.equals("0")) {
					String id = request.getId();
					String serviceId = request.getServiceId();
					Log.getLog(getClass()).info("DEL fms_facility_booking: requestId=" + showRequestId + " serviceId=" + serviceId + " id=" + id);
					
					String sqlDel =
							"DELETE FROM fms_facility_booking " +
							"WHERE id = ? ";
					dao.update(sqlDel, new String[] {id});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete from fms_facility_booking (facility)
		try {
			Collection colFacilityBooking = (Collection) pageContext.getAttribute("colFacilityBooking");
			for (Iterator iterator = colFacilityBooking.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				String dup = (String) request.getProperty("dup");
				if (chkServiceId == null && dup.equals("0")) {
					String id = request.getId();
					String serviceId = request.getServiceId();
					Log.getLog(getClass()).info("DEL fms_facility_booking: requestId=" + showRequestId + " serviceId=" + serviceId + " id=" + id);
					
					String sqlDel =
							"DELETE FROM fms_facility_booking " +
							"WHERE id = ? ";
					dao.update(sqlDel, new String[] {id});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete from fms_eng_request_unit
		try {
			Collection colUnit = (Collection) pageContext.getAttribute("colUnit");
			for (Iterator iterator = colUnit.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				if (chkServiceId == null) {
					String id = request.getId();
					String serviceId = request.getServiceId();
					Log.getLog(getClass()).info("DEL fms_eng_request_unit: requestId=" + showRequestId + " serviceId=" + serviceId + " id=" + id);
					
					String sqlDel =
							"DELETE FROM fms_eng_request_unit " +
							"WHERE id = ? ";
					dao.update(sqlDel, new String[] {id});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete from services requested
		try {
			Collection colRecord = (Collection) pageContext.getAttribute("colRecord");
			for (Iterator iterator = colRecord.iterator(); iterator.hasNext();) {
				EngineeringRequest request = (EngineeringRequest) iterator.next();
				String chkServiceId = (String) request.getProperty("chkServiceId");
				if (chkServiceId == null) {
					String id = request.getId();
					String serviceId = request.getServiceId();
					String tableName = (String) request.getProperty("tableName");
					Log.getLog(getClass()).info("DEL " + tableName + ": requestId=" + showRequestId + " serviceId=" + serviceId + " id=" + id);
					
					String sqlDel =
							"DELETE FROM " + tableName + " WHERE id = ? ";
					dao.update(sqlDel, new String[] {id});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
%>

<% // starting point
	EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
	EngineeringDao dao = (EngineeringDao) module.getDao();
	
	String showRequestId = request.getParameter("show");
	if (showRequestId != null && showRequestId.trim().equals("")) {
		showRequestId = null;
	}
	
	boolean fixIt = false;
	String fix = request.getParameter("fix");
	if (fix != null) {
		if (showRequestId != null && fix.equals("record")) {
			fixIt = true;
		} else if (showRequestId == null && fix.equals("all")) {
			fixIt = true;
		}
	}
	
	
	Date start = new Date();
	if (showRequestId == null) {
		// get service without service required
		try {
			String sqlExtra = 
					getServiceCheckSQL("fms_eng_service_scp") +
						"UNION " +
					getServiceCheckSQL("fms_eng_service_other") +
						"UNION " +
					getServiceCheckSQL("fms_eng_service_manpower") +
						"UNION " +
					getServiceCheckSQL("fms_eng_service_vtr") +
						"UNION " +
					getServiceCheckSQL("fms_eng_service_tvro") +
						"UNION " +
					getServiceCheckSQL("fms_eng_service_studio") +
						"UNION " +
					getServiceCheckSQL("fms_eng_service_postproduction");
			
			Collection colExtra = dao.select(sqlExtra, EngineeringRequest.class, null, 0, -1);
			pageContext.setAttribute("colExtra", colExtra);
			
			if (fixIt) {
				int i = 1;
				for (Iterator iterator = colExtra.iterator(); iterator.hasNext();) {
					EngineeringRequest req = (EngineeringRequest) iterator.next();
					String requestId = (String) req.getRequestId();
					
					Log.getLog(getClass()).info("FIX " + i + ": requestId=" + requestId);
					queryInfo(dao, requestId, pageContext);
					deleteInfo(dao, requestId, pageContext);
					i++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else {
		queryInfo(dao, showRequestId, pageContext);
		if (fixIt) {
			deleteInfo(dao, showRequestId, pageContext);
		}
	}
	Date end = new Date();
	String elapsed = getDiffTime(start.getTime(), end.getTime());
	
	if (showRequestId == null && fixIt) {
		Log.getLog(getClass()).info("Fix all elapsed: " + elapsed);
	}
%>


<html>
	<head>
		<style>
			.niceStyle {
				background-color: #CC9966;
				font-weight: bold;
			}
			
			.extraStyle {
				background-color: #FFAAAA;
			}
		</style>
		
		<title>Fix Headless</title>
	</head>
	<body>
	
		<% if (showRequestId == null) { %>
			<% if (fixIt) { %>
				<script>
					alert("All items fixed");
					location = "fixHeadless.jsp";
				</script>
				<% if (1 != 2) return; %>
			<% } %>
			
			Fix Headless:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td align="right">No.</td>
						<td>Request ID</td>
						<td>Title</td>
						<td>Status</td>
						<td>Status Label</td>
						<td>&nbsp;</td>
					</tr>
				</thead>
				<tbody>
					<c:set var="recordHasProblems" value="false" />
					<c:forEach items="${colExtra}" var="req" varStatus="status">
						<c:set var="recordHasProblems" value="true" />
						<tr>
							<td align="right"><c:out value="${status.count}" /></td>
							<td><a href="infoRequest.jsp?requestId=<c:out value="${req.requestId}" />"><c:out value="${req.requestId}" /></a></td>
							<td><c:out value="${req.title}" /></td>
							<td><c:out value="${req.status}" /></td>
							<td><c:out value="${req.statusLabel}" /></td>
							<td><a href="fixHeadless.jsp?show=<c:out value="${req.requestId}" />">Show&nbsp;Items</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			<c:if test="${recordHasProblems}">
				<form>
					<input type="hidden" name="fix" value="all">
					<input type="submit" value="Fix All">
				</form>
			</c:if>
			<c:if test="${not recordHasProblems}">
				No invalid records in FMS
			</c:if>
		
		<% } else { %>
			<% if (fixIt) { %>
				<script>
					alert("Item(s) fixed");
					location = "fixHeadless.jsp?show=<%= showRequestId %>";
				</script>
				<% if (1 != 2) return; %>
			<% } %>
		
			<a href="fixHeadless.jsp">&lt;&lt; back to main listing</a><br><br>
			Show <%= showRequestId %>:<br>
						<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Request ID</td>
						<td>Title</td>
						<td>Status</td>
						<td>Status Label</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colMain}" var="req" varStatus="status">
						<tr class="<c:out value="${recStyle}"/>">
							<td><a href="infoRequest.jsp?requestId=<c:out value="${req.requestId}" />"><c:out value="${req.requestId}" /></a></td>
							<td><c:out value="${req.title}" /></td>
							<td><c:out value="${req.status}" /></td>
							<td><c:out value="${req.statusLabel}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Services Requested:
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Service Type</td>
						<td>Table</td>
						<td>Facility</td>
						<td>Action</td>
					</tr>
				</thead>
				<tbody>
					<c:set var="recordHasProblems" value="false" />
					<c:set var="showFixButton" value="false" />
					<c:forEach items="${colRecord}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
							<c:set var="recordHasProblems" value="true" />
							<c:set var="showFixButton" value="true" />
						</c:if>
						
						<tr class="<c:out value="${recStyle}"/>">
							<td><c:out value="${req.serviceId}" /></td>
							<td><c:out value="${req.propertyMap['tableName']}" /></td>
							<td><c:out value="${req.propertyMap['facility']}" /><c:if test="${empty(req.propertyMap['facility'])}"><i>-- missing rate card --</i></c:if></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Unit:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Service Type</td>
						<td>Unit</td>
						<td>Facility</td>
						<td>Action</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colUnit}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
						</c:if>
						
						<tr class="<c:out value="${recStyle}"/>">
							<td><c:out value="${req.serviceId}" /></td>
							<td><c:out value="${req.propertyMap['unitId']}" /></td>
							<td><c:out value="${req.propertyMap['facility']}" /><c:if test="${empty(req.propertyMap['facility'])}"><i>-- missing rate card --</i></c:if></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Facility Booking:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td rowspan="2">Service Type</td>
						<td rowspan="2">Rate Card</td>
						<td rowspan="2">Category Name</td>
						<td align="center" colspan="2">Book From</td>
						<td align="center" colspan="2">Book To</td>
						<td rowspan="2">Action</td>
					</tr>
					<tr>
						<td>Date</td>
						<td>Time</td>
						<td>Date</td>
						<td>Time</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colFacilityBooking}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
						</c:if>
						
						<c:if test="${req.propertyMap['dup'] == 0}">
							<tr class="<c:out value="${recStyle}"/>">
								<td><c:out value="${req.serviceId}" /></td>
								<td><c:out value="${req.propertyMap['rateCardName']}" /></td>
								<td><c:out value="${req.propertyMap['category_name']}" /><c:if test="${empty(req.propertyMap['category_name'])}">&nbsp;</c:if></td>
								<td><fmt:formatDate value="${req.propertyMap['bookFrom']}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${req.propertyMap['timeFrom']}" /></td>
								<td><fmt:formatDate value="${req.propertyMap['bookTo']}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${req.propertyMap['timeTo']}" /></td>
								<td><c:out value="${recComment}" escapeXml="false" /></td>
								<%--
								<td><c:out value="${req.rateCardId}" /></td>
								<td><c:out value="${req.propertyMap['dup']}" /></td>
								--%>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Manpower Booking:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td rowspan="2">Service Type</td>
						<td rowspan="2">Rate Card</td>
						<td rowspan="2">Competency</td>
						<td align="center" colspan="2">Book From</td>
						<td align="center" colspan="2">Book To</td>
						<td rowspan="2">Action</td>
					</tr>
					<tr>
						<td>Date</td>
						<td>Time</td>
						<td>Date</td>
						<td>Time</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colManpowerBooking}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
						</c:if>
						
						<c:if test="${req.propertyMap['dup'] == 0}">
							<tr class="<c:out value="${recStyle}"/>">
								<td><c:out value="${req.serviceId}" /></td>
								<td><c:out value="${req.propertyMap['rateCardName']}" /></td>
								<td><c:out value="${req.competencyName}" /><c:if test="${empty(req.competencyName)}">&nbsp;</c:if></td>
								<td><fmt:formatDate value="${req.propertyMap['bookFrom']}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${req.propertyMap['timeFrom']}" /></td>
								<td><fmt:formatDate value="${req.propertyMap['bookTo']}" pattern="<%= DATE_PATTERN %>"/></td>
								<td><c:out value="${req.propertyMap['timeTo']}" /></td>
								<td><c:out value="${recComment}" escapeXml="false" /></td>
								<%--
								<td><c:out value="${req.rateCardId}" /></td>
								<td><c:out value="${req.propertyMap['dup']}" /></td>
								--%>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Resource Report:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Service Type</td>
						<td>Facility</td>
						<td>Book Date</td>
						<td>Action</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colResource}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
						</c:if>
						
						<tr class="<c:out value="${recStyle}"/>">
							<td><c:out value="${req.serviceId}" /></td>
							<td><c:out value="${req.propertyMap['facility']}" /><c:if test="${empty(req.propertyMap['facility'])}"><i>-- missing rate card --</i></c:if></td>
							<td><fmt:formatDate value="${req.propertyMap['bookDate']}" pattern="<%= DATE_PATTERN %>"/></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Assignment:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Service Type</td>
						<td>Type</td>
						<td>Code</td>
						<td>Group ID</td>
						<td>Assignment ID</td>
						<td>Action</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colAssignment}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
						</c:if>
						
						<tr class="<c:out value="${recStyle}"/>">
							<td><c:out value="${req.serviceId}" /></td>
							<td><c:out value="${req.assignmentType}" /></td>
							<td><c:out value="${req.propertyMap['code']}" /></td>
							<td><c:out value="${req.groupId}" /></td>
							<td><c:out value="${req.assignmentId}" /></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Assignment Equipment:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Service Type</td>
						<td>Type</td>
						<td>Rate Card</td>
						<td>Group ID</td>
						<td>Assignment ID</td>
						<td>Action</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colAssignmentEquipment}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
						</c:if>
						
						<tr class="<c:out value="${recStyle}"/>">
							<td><c:out value="${req.serviceId}" /></td>
							<td><c:out value="${req.assignmentType}" /></td>
							<td><c:out value="${req.propertyMap['rateCardName']}" /></td>
							<td><c:out value="${req.groupId}" /></td>
							<td><c:out value="${req.assignmentId}" /></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			Assignment Manpower:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td rowspan="2">Service Type</td>
						<td rowspan="2">Type</td>
						<td rowspan="2">Rate Card</td>
						<td align="center" colspan="2">Required From</td>
						<td align="center" colspan="2">Required To</td>
						<td rowspan="2">Group ID</td>
						<td rowspan="2">Assignment ID</td>
						<td rowspan="2">Action</td>
					</tr>
					<tr>
						<td>Date</td>
						<td>Time</td>
						<td>Date</td>
						<td>Time</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colAssignmentManpower}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.propertyMap['chkServiceId'])}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
						</c:if>
						
						<tr class="<c:out value="${recStyle}"/>">
							<td><c:out value="${req.serviceId}" /></td>
							<td><c:out value="${req.assignmentType}" /></td>
							<td><c:out value="${req.propertyMap['rateCardName']}" /></td>
							<td><fmt:formatDate value="${req.requiredFrom}" pattern="<%= DATE_PATTERN %>"/></td>
							<td><c:out value="${req.fromTime}" /></td>
							<td><fmt:formatDate value="${req.requiredTo}" pattern="<%= DATE_PATTERN %>"/></td>
							<td><c:out value="${req.toTime}" /></td>
							<td><c:out value="${req.groupId}" /></td>
							<td><c:out value="${req.assignmentId}" /></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			<c:if test="${showFixButton}">
				<form>
					<input type="hidden" name="show" value="<%= showRequestId %>">
					<input type="hidden" name="fix" value="record">
					<input type="submit" value="Fix <%= showRequestId %>">
				</form>
			</c:if>
		<% } %>
		
		[elapsed: <%= elapsed %>]
	</body>
</html>