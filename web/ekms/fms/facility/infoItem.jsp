<%@ page import="kacang.*, kacang.model.*, java.util.*, com.tms.fms.engineering.model.*" %>
<%@ page import="com.tms.fms.facility.model.FacilityObject, com.tms.fms.facility.model.CategoryObject,com.tms.fms.facility.model.*" %>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<%--
	version:
		1.0 used to troubleshoot check in / check out issues (2011-05-13)
		1.1 cosmetic changes (2011-07-01)
		1.2 added category information (2011-07-04)
		1.3 troubleshoot item is still checked out (2011-09-28)
			change default "top" from 5 to -1
		1.4 added "updated by" to facility item (2011-10-04)
			added user to Internal Check Out
			added status to Assignment Check Out & Internal Check Out
		1.5 added Requested By to Internal Check Out
		1.6 added rate card (2013-02-08)
		1.7 added Service Type to Assignment Check Out (2013-04-22)
		    added verbose feature
		    added link to infoRequest.jsp
		    added mini form to prompt for barcode
		1.8 added more barcode in screen (2013-05-03)
		1.9 added Undo Log (2013-05-10)
--%>

<%!
	public static final String CURRENT_VERSION = "1.9";
	public static final String DATE_TIME_PATTERN = "dd-MMM-yyyy hh:mm'&nbsp;'a";
%>

<%
	EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
	EngineeringDao dao = (EngineeringDao) module.getDao();
	
	String barcode = request.getParameter("barcode");
	
	if (barcode == null || barcode.equals("")) {
		out.println("<form>Please enter barcode: <input name=\"barcode\"></form>");
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
	
	// get status
	try {
		String sqlBarcode = 
			"SELECT facility_id AS facilityId, barcode, status, su.username AS updatedby_username, updatedby_date " + 
			"FROM fms_facility_item fi " +
			"LEFT OUTER JOIN security_user su ON (fi.updatedby = su.id) " +
			"WHERE barcode = ? ";
		Collection colBarcode = dao.select(sqlBarcode, EngineeringRequest.class, new String[] {barcode}, 0, -1);
		pageContext.setAttribute("colBarcode", colBarcode);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// get facility
	try {
		String sqlFacility = 
			"SELECT f.id, f.name, f.maketype, f.model_name, f.quantity, f.is_pool, f.status, f.category_id " + 
			"FROM fms_facility_item i " +
			"INNER JOIN fms_facility f ON (i.facility_id = f.id) " +
			"WHERE barcode = ? ";
		Collection colFacility = dao.select(sqlFacility, FacilityObject.class, new String[] {barcode}, 0, -1);
		pageContext.setAttribute("colFacility", colFacility);
		
		if (colFacility.size() == 1) {
			FacilityObject facility = (FacilityObject) colFacility.iterator().next();
			String categoryId = facility.getCategory_id();
			pageContext.setAttribute("categoryId", categoryId);
			String facilityId = facility.getId();
			pageContext.setAttribute("facilityId", facilityId);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// get facility sub category & parent category
	try {
		String categoryId = (String) pageContext.getAttribute("categoryId");
		if (categoryId != null) {
			Collection colCategory = new ArrayList();
			
			String sqlCategory = 
				"SELECT id, name, description, status, parent_cat_id " + 
				"FROM fms_facility_category " +
				"WHERE id = ? ";
			Collection colSubCategory = dao.select(sqlCategory, CategoryObject.class, new String[] {categoryId}, 0, -1);
			colCategory.addAll(colSubCategory);
			
			if (colSubCategory.size() == 1) {
				CategoryObject category = (CategoryObject) colSubCategory.iterator().next();
				String parentCategoryId = category.getParent_cat_id();
				
				Collection colMainCategory = dao.select(sqlCategory, CategoryObject.class, new String[] {parentCategoryId}, 0, -1);
				colCategory.addAll(colMainCategory);
			}
			
			pageContext.setAttribute("colCategory", colCategory);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// get rate card
	try {
		String facilityId = (String) pageContext.getAttribute("facilityId");
		if (facilityId != null) {
			String sqlRateCard = 
				"SELECT cat.id AS idCategory, cat.name AS categoryName, equip.rateCardId, " +
				"       rc.name AS rateCardName, rc.serviceTypeId, rc.status " +
				"FROM fms_eng_rate_card_cat_item item " +
				"INNER JOIN fms_eng_rate_card_category cat ON (cat.id = item.categoryId) " +
				"INNER JOIN fms_rate_card_equipment equip ON (equip.equipment = cat.id) " +
				"LEFT OUTER JOIN fms_rate_card rc ON (rc.id = equip.rateCardId) " +
				"WHERE item.facilityId = ? ";
			Collection colRateCard = dao.select(sqlRateCard, RateCard.class, new String[] {facilityId}, 0, -1);
			pageContext.setAttribute("colRateCard", colRateCard);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// assignment check out
	try {
		String sqlAssignCheckout = 
				"SELECT ae.id, ae.assignmentId, checkedOutBy, checkedOutDate, checkedInBy, checkedInDate, " +
				"       takenBy, createdDate, a.requestId, a.code AS assignmentCode, r.title, ae.status, a.serviceType " +
				"FROM fms_eng_assignment_equipment ae " +
				"INNER JOIN fms_eng_assignment a ON (ae.assignmentId = a.assignmentId) " +
				"INNER JOIN fms_eng_request r ON (a.requestId = r.requestId) " + 
				"WHERE barcode = ? " +
				"UNION " +
				"SELECT DISTINCT ae.id, ae.assignmentId, checkedOutBy, checkedOutDate, checkedInBy, checkedInDate, " +
				"       takenBy, createdDate, a.requestId, '', r.title, ae.status, a.serviceType " +
				"FROM fms_eng_assignment_equipment ae " +
				"INNER JOIN fms_eng_assignment a ON (ae.groupId = a.groupId AND ae.assignmentId = '-') " +
				"INNER JOIN fms_eng_request r ON (a.requestId = r.requestId) " + 
				"WHERE barcode = ? " +
				"ORDER BY checkedOutDate DESC ";
		Collection colAssignCheckout = dao.select(sqlAssignCheckout, EngineeringRequest.class, new String[] {barcode, barcode}, 0, topNum);
		pageContext.setAttribute("colAssignCheckout", colAssignCheckout);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// internal check out
	try {
		String sqlInternalCheckout = 
				"SELECT " + topStr + " checkout_date, suOut.username AS checkout_username, checkin_date, suIn.username AS checkin_username, " + 
				"       suReq.username AS requestedByUsername, " +
				"       status, takenBy, purpose, groupId " + 
				"FROM fms_facility_item_checkout c " + 
				"LEFT OUTER JOIN security_user suOut ON (c.checkout_by = suOut.id) " +
				"LEFT OUTER JOIN security_user suIn ON (c.checkin_by = suIn.id) " +
				"LEFT OUTER JOIN security_user suReq ON (c.requestedBy = suReq.id) " +
				"WHERE barcode = ? " + 
				"ORDER BY checkout_date DESC ";
		Collection colInternalCheckout = dao.select(sqlInternalCheckout, DefaultDataObject.class, new String[] {barcode}, 0, -1);
		pageContext.setAttribute("colInternalCheckout", colInternalCheckout);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// undo log
	try {
		String sqlUndoLog = 
			"SELECT " + topStr + " undoId, undoBy, undoDate, undoType, barcode, ul.requestId, r.title, checkedOutBy, checkedOutDate " + 
			"FROM fms_eng_undo_log ul " +
			"LEFT OUTER JOIN fms_eng_request r ON (ul.requestId = r.requestId) " +
			"WHERE barcode = ? " +
			"ORDER BY undoDate DESC ";
		Collection colUndoLog = dao.select(sqlUndoLog, EngineeringRequest.class, new String[] {barcode}, 0, -1);
		pageContext.setAttribute("colUndoLog", colUndoLog);
	} catch (Exception e) {
		e.printStackTrace();
	}
%>

<html>
	<head>
		<style>
			.niceStyle {
				background-color: #CC9966;
				font-weight: bold;
			}
			
			.checkedOutStyle {
				background-color: #CCCCFF;
				color: #FFFFFF;
			}
		</style>
		
		<title>Barcode: <%= barcode %></title>
	</head>
	<body>
		<b style="font-size:1.5em;">Barcode: <%= barcode %></b><br>
		(v<%= CURRENT_VERSION %>)<br>
		
		<% if (topNum >= 1) { %>
			Showing top <%= topNum %>:
		<% } else { %>
			Showing all:
		<% } %>		
		<% if (topNum == 5) { %>
			[show top 5]
		<% } else { %>
			<a href="?top=5&barcode=<%=barcode%>">show top 5</a>
		<% } %>
		<% if (topNum == 10) { %>
			[show top 10]
		<% } else { %>
			<a href="?top=10&barcode=<%=barcode%>">show top 10</a>
		<% } %>
		<% if (topNum < 1) { %>
			[show all]
		<% } else { %>
			<a href="?barcode=<%=barcode%>">show all</a>
		<% } %>
		<br>
		Verbose mode: 
		<% if (verboseMode) { %>
			ON [ <a href="?barcode=<%=barcode%>">toggle</a> ]
		<% } else { %>
			OFF [ <a href="?verbose=1&barcode=<%=barcode%>">toggle</a> ]
		<% } %>
		<br><br>
		
		<c-rt:set var="checkOutStatus" value="<%= FacilityModule.ITEM_STATUS_CHECKED_OUT %>" />
		
		Facility Item:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">Barcode</td>
					<td rowspan="2">Facility ID</td>
					<td rowspan="2">Status</td>
					<td align="center" colspan="2">Updated By</td>
				</tr>
				<tr>
					<td align="center">User</td>
					<td align="center">Date</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colBarcode}" var="bc" varStatus="status">
					<tr>
						<td><a href="ViewItem.jsp?<c:out value="${bc.facilityId}" />&rid=<c:out value="${bc.barcode}" />"><c:out value="${bc.barcode}" /></a></td>
						<td><c:out value="${bc.facilityId}" /></td>
						<c:choose>
							<c:when test="${bc.status == checkOutStatus}">
								<td class="checkedOutStyle"><c:out value="${bc.status}" /></td>
							</c:when>
							<c:otherwise>
								<td><c:out value="${bc.status}" />&nbsp;</td>
							</c:otherwise>
						</c:choose>
						<td><c:out value="${bc.propertyMap['updatedby_username']}" />&nbsp;</td>
						<td><fmt:formatDate value="${bc.propertyMap['updatedby_date']}" pattern="<%= DATE_TIME_PATTERN %>" />&nbsp;</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Facility:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td>Item Name</td>
					<td>Make Type</td>
					<td>Model Name</td>
					<td>Quantity</td>
					<td>Usage</td>
					<td>Status</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colFacility}" var="fc" varStatus="status">
					<tr>
						<td><a href="ViewFacility.jsp?fid=<c:out value="${fc.id}" />"><c:out value="${fc.name}" /></a></td>
						<td><c:out value="${fc.maketype}" />&nbsp;</td>
						<td><c:out value="${fc.model_name}" />&nbsp;</td>
						<td><c:out value="${fc.quantity}" /></td>
						<td><c:out value="${fc.is_pool}" /></td>
						<td><c:out value="${fc.status}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		<c:if test="${not empty(colCategory)}">
			Facility Category:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Category Name</td>
						<td>Description</td>
						<td>Status</td>
						<td>Category ID</td>
						<td>Parent ID</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${colCategory}" var="sc" varStatus="status">
						<tr>
							<td><c:out value="${sc.name}" />&nbsp;</td>
							<td><c:out value="${sc.description}" />&nbsp;</td>
							<td><c:out value="${sc.status}" /></td>
							<td><c:out value="${sc.id}" /></td>
							<td><c:out value="${sc.parent_cat_id}" />&nbsp;</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
		</c:if>
		
		Rate Card:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td>Rate Card Name</td>
					<td>Item Category Name</td>
					<td>Service Type</td>
					<td>Status</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colRateCard}" var="rc" varStatus="status">
					<tr>
						<td><a href="rateCardView.jsp?id=<c:out value="${rc.propertyMap['rateCardId']}" />"><c:out value="${rc.propertyMap['rateCardName']}" /></a>&nbsp;</td>
						<td><a href="rateCardCategoryEdit.jsp?id=<c:out value="${rc.idCategory}" />"><c:out value="${rc.categoryName}" /></a>&nbsp;</td>
						<td><c:out value="${rc.serviceTypeId}" /></td>
						<td><c:out value="${rc.status}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Assignment Check Out [<%= barcode %>]:<br>
		<table align="center" border="1" cellpadding="3" cellspacing="0" width="100%">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">No.</td>
					<td align="center" colspan="2">Checked Out</td>
					<td align="center" colspan="2">Checked In</td>
					<td rowspan="2">Status</td>
					<% if (verboseMode) { %>
						<td rowspan="2">Taken By</td>
					<% } %>
					<td rowspan="2">Assignment No</td>
					<td rowspan="2">Service Type</td>
					<% if (verboseMode) { %>
						<td rowspan="2">Request ID</td>
					<% } %>
					<td rowspan="2">Request Title</td>
				</tr>
				<tr>
					<td align="center">User</td>
					<td align="center">Date</td>
					<td align="center">User</td>
					<td align="center">Date</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colAssignCheckout}" var="req" varStatus="status">
					<tr>
						<td><c:out value="${status.count}" /></td>
						<c:if test="${not empty(req.checkedOutDate)}">
							<td><c:out value="${req.checkedOutBy}" /></td>
							<td><fmt:formatDate value="${req.checkedOutDate}" pattern="<%= DATE_TIME_PATTERN %>" /></td>
							
							<c:choose>
								<c:when test="${not empty(req.checkedInDate)}">
									<td><c:out value="${req.checkedInBy}" /></td>
									<td><fmt:formatDate value="${req.checkedInDate}" pattern="<%= DATE_TIME_PATTERN %>" /></td>
								</c:when>
								<c:otherwise>
									<td colspan="2" class="checkedOutStyle">&nbsp;</td>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${empty(req.checkedOutDate)}">
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</c:if>
						
						<td><c:out value="${req.status}" /></td>
						
						<% if (verboseMode) { %>
							<td><c:out value="${req.takenBy}" /></td>
						<% } %>
						
						<td style="white-space: nowrap;">
							<c:out value="${req.assignmentCode}" />
							<c:if test="${req.assignmentId == '-'}" >
								<i>-- Extra Check Out --</i>
							</c:if>
						</td>
						<td><c:out value="${req.serviceType}" /></td>
						<% if (verboseMode) { %>
							<td><a href="infoRequest.jsp?requestId=${req.requestId}"><c:out value="${req.requestId}" /></a></td>
						<% } %>
						<td><a href="requestDetails.jsp?page=all&requestId=${req.requestId}"><c:out value="${req.title}" /></a>&nbsp;</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Internal Check Out [<%= barcode %>]:<br>
		<table align="center" border="1" cellpadding="3" cellspacing="0" width="100%">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">No.</td>
					<td align="center" colspan="2">Checked Out</td>
					<td align="center" colspan="2">Checked In</td>
					<td rowspan="2">Status</td>
					<td rowspan="2">Taken By</td>
					<td rowspan="2">Purpose</td>
					<td rowspan="2">Requested By</td>
					<td rowspan="2">Group ID</td>
				</tr>
				<tr>
					<td align="center">User</td>
					<td align="center">Date</td>
					<td align="center">User</td>
					<td align="center">Date</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colInternalCheckout}" var="ic" varStatus="status">
					<tr>
						<td><c:out value="${status.count}" /></td>
						<td><c:out value="${ic.propertyMap['checkout_username']}" /></td>
						<td><fmt:formatDate value="${ic.propertyMap['checkout_date']}" pattern="<%= DATE_TIME_PATTERN %>" /></td>
						
						<c:choose>
							<c:when test="${not empty(ic.propertyMap['checkin_date'])}">
								<td><c:out value="${ic.propertyMap['checkin_username']}" /></td>
								<td><fmt:formatDate value="${ic.propertyMap['checkin_date']}" pattern="<%= DATE_TIME_PATTERN %>" />&nbsp;</td>
							</c:when>
							<c:otherwise>
								<td colspan="2" class="checkedOutStyle">&nbsp;</td>
							</c:otherwise>
						</c:choose>
						
						<td><c:out value="${ic.propertyMap['status']}" /></td>
						<td><c:out value="${ic.propertyMap['takenBy']}" />&nbsp;</td>
						<td><c:out value="${ic.propertyMap['purpose']}" />&nbsp;</td>
						<td><c:out value="${ic.propertyMap['requestedByUsername']}" />&nbsp;</td>
						<td><a href="checkOutDetails.jsp?groupId=<c:out value="${ic.propertyMap['groupId']}" />"><c:out value="${ic.propertyMap['groupId']}" /></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		
		Undo Log [<%= barcode %>]:<br>
		<table border="1" cellpadding="3" cellspacing="0">
			<thead class="niceStyle">
				<tr>
					<td rowspan="2">No.</td>
					<td align="center" colspan="2">Checked Out</td>
					<td align="center" colspan="2">Undo</td>
					<td rowspan="2">Undo Type</td>
					<% if (verboseMode) { %>
						<td rowspan="2">Request ID</td>
					<% } %>
					<td rowspan="2">Request Title</td>
				</tr>
				<tr>
					<td align="center">User</td>
					<td align="center">Date</td>
					<td align="center">User</td>
					<td align="center">Date</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${colUndoLog}" var="req" varStatus="status">
					<tr>
						<td><c:out value="${status.count}" /></td>
						<c:if test="${not empty(req.checkedOutDate)}">
							<td><c:out value="${req.checkedOutBy}" /></td>
							<td><fmt:formatDate value="${req.checkedOutDate}" pattern="<%= DATE_TIME_PATTERN %>" /></td>							
							<td><c:out value="${req.propertyMap['undoBy']}" /></td>
							<td><fmt:formatDate value="${req.propertyMap['undoDate']}" pattern="<%= DATE_TIME_PATTERN %>" /></td>
						</c:if>
						<c:if test="${empty(req.checkedOutDate)}">
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</c:if>
						
						<td><c:out value="${req.propertyMap['undoType']}" /></td>
						
						<% if (verboseMode) { %>
							<td><a href="infoRequest.jsp?requestId=${req.requestId}"><c:out value="${req.requestId}" /></a></td>
						<% } %>
						<td><a href="requestDetails.jsp?page=all&requestId=${req.requestId}"><c:out value="${req.title}" /></a>&nbsp;</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
	</body>
</html>