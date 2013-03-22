<%@ page import="kacang.*, kacang.model.*, kacang.util.*, java.util.*, com.tms.fms.engineering.model.*" %>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<%!
	// please change this if ABW db name is different
	static final String ABW_PREFIX = "fms_abw..";

	private void deleteAbw(String requestId, String type) {
		EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
		EngineeringDao dao = (EngineeringDao) module.getDao();
		
		Log.getLog(getClass()).info("Deleting from ABW: requestId=" + requestId + " type=" + type);
		
		try {
			String sqlDel =
					"DELETE FROM " +  ABW_PREFIX + "fms_eng_transfer_cost " +
					"WHERE requestId = ? " + 
					"AND type = ? ";
			dao.update(sqlDel, new String[] {requestId, type});
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error deleting from ABW: requestId=" + requestId + " type=" + type);
		}
	}
%>

<%
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
	
	boolean abwPrefixOkay = true;
	if (showRequestId == null) {
		// get extra record in ABW
		try {
			String sqlExtra = 
				"SELECT tc.requestId, tc.type, r.title, r.status " +
				"FROM " + ABW_PREFIX + "fms_eng_transfer_cost tc " +
				"INNER JOIN fms_eng_request r ON (tc.requestId = r.requestId) " +
				"LEFT OUTER JOIN fms_eng_request_services rs ON (tc.requestId = rs.requestId AND tc.type = rs.serviceId) " +
				"WHERE rs.requestId IS NULL " +
				"AND tc.cancellation_ind != 'P' " +
				"GROUP BY tc.requestId, tc.type, r.title, r.status ";
			Collection colExtra = dao.select(sqlExtra, EngineeringRequest.class, null, 0, -1);
			pageContext.setAttribute("colExtra", colExtra);
			
			if (fixIt) {
				for (Iterator iterator = colExtra.iterator(); iterator.hasNext();) {
					EngineeringRequest req = (EngineeringRequest) iterator.next();
					String requestId = req.getRequestId();
					String type = (String) req.getProperty("type");
					deleteAbw(requestId, type);
				}
			}
		} catch (Exception e) {
			abwPrefixOkay = false;
			e.printStackTrace();
		}
	} else {
		// get particular record in ABW
		try {
			String sqlRecord = 
				"SELECT tc.requestId, tc.type, r.title, r.status, tc.cost, tc.ratecardAbwCode, tc.cancellation_ind, rs.serviceId " +
				"FROM " + ABW_PREFIX + "fms_eng_transfer_cost tc " +
				"INNER JOIN fms_eng_request r ON (tc.requestId = r.requestId) " +
				"LEFT OUTER JOIN fms_eng_request_services rs ON (tc.requestId = rs.requestId AND tc.type = rs.serviceId) " +
				"WHERE tc.requestId = ? " +
				"ORDER BY tc.requestId, tc.type, tc.cancellation_ind ";
			Collection colRecord = dao.select(sqlRecord, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colRecord", colRecord);
			
			if (fixIt) {
				for (Iterator iterator = colRecord.iterator(); iterator.hasNext();) {
					EngineeringRequest req = (EngineeringRequest) iterator.next();
					if (req.getServiceId() == null && !req.getStatus().equals("C")) {
						String requestId = req.getRequestId();
						String type = (String) req.getProperty("type");
						deleteAbw(requestId, type);
					}
				}
			}
		} catch (Exception e) {
			abwPrefixOkay = false;
			e.printStackTrace();
		}
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
		
		<title>Fix ABW</title>
	</head>
	<body>
	
		<% if (!abwPrefixOkay) { %>
			Please check the ABW Prefix<br>
			Current value is '<%= ABW_PREFIX %>'
		<% } else if (showRequestId == null) { %>
			<% if (fixIt) { %>
				<script>
					alert("All items fixed");
					location = "fixAbw.jsp";
				</script>
				<% if (1 != 2) return; %>
			<% } %>
			
			Fix ABW:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td align="right">No.</td>
						<td>Request ID</td>
						<td>Service Type</td>
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
							<td><c:out value="${req.propertyMap['type']}" /></td>
							<td><c:out value="${req.title}" /></td>
							<td><c:out value="${req.status}" /></td>
							<td><c:out value="${req.statusLabel}" /></td>
							<td><a href="fixAbw.jsp?show=<c:out value="${req.requestId}" />">Show&nbsp;Items</a></td>
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
				No invalid records in ABW
			</c:if>
		
		<% } else { %>
			<% if (fixIt) { %>
				<script>
					alert("Item(s) fixed");
					location = "fixAbw.jsp?show=<%= showRequestId %>";
				</script>
				<% if (1 != 2) return; %>
			<% } %>
		
			<a href="fixAbw.jsp">&lt;&lt; back to main listing</a><br><br>
			Show <%= showRequestId %>:<br>
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Request ID</td>
						<td>Service Type</td>
						<td>Title</td>
						<td>ABW Code</td>
						<td align="right">Cost</td>
						<td>Cancellation Ind</td>
						<td>Status</td>
						<td>Status Label</td>
						<td>Action</td>
					</tr>
				</thead>
				<tbody>
					<c:set var="recordHasProblems" value="false" />
					<c:forEach items="${colRecord}" var="req" varStatus="status">
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${empty(req.serviceId) && req.propertyMap['cancellation_ind'] != 'P'}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
							<c:set var="recordHasProblems" value="true" />
						</c:if>
						<tr class="<c:out value="${recStyle}"/>">
							<td><a href="infoRequest.jsp?requestId=<c:out value="${req.requestId}" />"><c:out value="${req.requestId}" /></a></td>
							<td><c:out value="${req.propertyMap['type']}" /></td>
							<td><c:out value="${req.title}" /></td>
							<td><c:out value="${req.propertyMap['ratecardAbwCode']}" />&nbsp;</td>
							<td align="right"><c:out value="${req.propertyMap['cost']}" /></td>
							<td><c:out value="${req.propertyMap['cancellation_ind']}" /></td>
							<td><c:out value="${req.status}" /></td>
							<td><c:out value="${req.statusLabel}" /></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			
			<c:if test="${recordHasProblems}">
				<form>
					<input type="hidden" name="show" value="<%= showRequestId %>">
					<input type="hidden" name="fix" value="record">
					<input type="submit" value="Fix <%= showRequestId %>">
				</form>
			</c:if>
		<% } %>
	</body>
</html>