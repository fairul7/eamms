<%@ page import="kacang.*, kacang.model.*, kacang.util.*, java.text.*, java.util.*, java.math.*, com.tms.fms.engineering.model.*" %>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<%!
	// please change this if ABW db name is different
	static final String ABW_PREFIX = "fms_abw..";
	
	public static final String DATE_PATTERN = "dd-MMM-yyyy";
	public static final String DATE_TIME_PATTERN = "dd-MMM-yyyy hh:mm'&nbsp;'a";
	
	private String formatNum(BigDecimal num) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return df.format(num.doubleValue());
	}
	
	private BigDecimal sumCost(ArrayList colRecord, int start, int end) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = start; i < end; i++) {
			EngineeringRequest request = (EngineeringRequest) colRecord.get(i);
			BigDecimal cost = (BigDecimal) request.getProperty("cost");
			sum = sum.add(cost);
		}
		return sum;
	}
	
	private void markDuplicate(ArrayList colRecord, int start, int end) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = start; i < end; i++) {
			EngineeringRequest request = (EngineeringRequest) colRecord.get(i);
			request.setProperty("duplicate", "1");
		}
	}
%>

<%! // query record
	public HashMap queryInfo(EngineeringDao dao, String showRequestId, PageContext pageContext) {
		boolean abwPrefixOkay = true;
		boolean internalRequest = true;
		boolean twiceAsBig = false;
		BigDecimal correctAmount = BigDecimal.ZERO;
		
		// get engineering request
		try {
			String sqlRequest = 
				"SELECT requestId, title, requiredFrom, requiredTo, requestType, totalInternalRate, totalExternalRate, status " + 
				"FROM fms_eng_request " +
				"WHERE requestId = ? ";
			Collection colRequest = dao.select(sqlRequest, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colRequest", colRequest);
			
			if (colRequest.size() == 1) {
				EngineeringRequest req = (EngineeringRequest) colRequest.iterator().next();
				Double rate;
				if ("E".equals(req.getRequestType())) {
					internalRequest = false;
					rate = req.getTotalExternalRate();
				} else {
					rate = req.getTotalInternalRate();
				}
				correctAmount = new BigDecimal(rate);
				correctAmount = correctAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// get particular record in ABW
		try {
			String sqlRecord = 
				"SELECT tc.uniqueid AS id, tc.requestId, tc.type, tc.createdDate, tc.status AS abwStatus, " +
					"r.title, r.status, tc.cost, tc.ratecardAbwCode, tc.cancellation_ind, rs.serviceId " +
				"FROM " + ABW_PREFIX + "fms_eng_transfer_cost tc " +
				"INNER JOIN fms_eng_request r ON (tc.requestId = r.requestId) " +
				"LEFT OUTER JOIN fms_eng_request_services rs ON (tc.requestId = rs.requestId AND tc.type = rs.serviceId) " +
				"WHERE tc.requestId = ? " +
				"ORDER BY tc.requestId, tc.createdDate, tc.type, tc.cancellation_ind ";
			Collection colRecord = dao.select(sqlRecord, EngineeringRequest.class, new String[] {showRequestId}, 0, -1);
			pageContext.setAttribute("colRecord", colRecord);
			
			// mark duplicates
			if (colRecord.size() != 0 && colRecord.size() % 2 == 0) {
				int half = colRecord.size() / 2;
				BigDecimal cost1 = sumCost((ArrayList) colRecord, 0, half);
				BigDecimal cost2 = sumCost((ArrayList) colRecord, half, colRecord.size());
				if (cost1.compareTo(cost2) == 0 && cost1.compareTo(correctAmount) == 0) {
					markDuplicate((ArrayList) colRecord, half, colRecord.size());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			abwPrefixOkay = false;
		}
		
		HashMap map = new HashMap();
		map.put("abwPrefixOkay", abwPrefixOkay);
		map.put("internalRequest", internalRequest);
		map.put("correctAmount", correctAmount);
		return map;
	}
%>

<%! // delete records
	public void deleteInfo(EngineeringDao dao, String showRequestId, PageContext pageContext) {
		// delete from fms_eng_transfer_cost
		try {
			Collection colRecord = (Collection) pageContext.getAttribute("colRecord");
			for (Iterator iterator = colRecord.iterator(); iterator.hasNext();) {
				EngineeringRequest req = (EngineeringRequest) iterator.next();
				String cancellation_ind = (String) req.getProperty("cancellation_ind");
				String duplicate = (String) req.getProperty("duplicate");
				
				if ((req.getServiceId() == null && !cancellation_ind.equals("P")) || "1".equals(duplicate)) {
					String id = req.getId();
					String type = (String) req.getProperty("type");
				
					Log.getLog(getClass()).info("DEL fms_eng_transfer_cost: requestId=" + showRequestId + " type=" + type + " id=" + id);
					
					String sqlDel =
							"DELETE FROM " + ABW_PREFIX + "fms_eng_transfer_cost " +
							"WHERE uniqueid = ? ";
					dao.update(sqlDel, new String[] {id});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	boolean internalRequest = true;
	boolean abwPrefixOkay = true;
	BigDecimal correctAmount = BigDecimal.ZERO;
	if (showRequestId == null) {
		// get extra record in ABW
		try {
			String sqlExtra = 
				"SELECT abw.status as abwStatus, fms.status, fms.title, " +
					"abw.requestId, abw.projectcode, SUM(abw.cost) AS totalAbw, fms.totalInternalRate, " + 
					"SUM(abw.cost) - fms.totalInternalRate AS diff " +
				"FROM " + ABW_PREFIX + "fms_eng_transfer_cost abw " +
				"LEFT OUTER JOIN dbo.fms_eng_request fms on abw.requestid=fms.requestid " +
				"WHERE abw.status in ('P','N')   " +
				"AND abw.cancellation_ind='N' " +
				"GROUP BY abw.status, fms.status, fms.title, abw.requestId, abw.projectcode, fms.totalInternalRate " +
				"HAVING SUM(abw.cost) <> fms.totalInternalRate " +
				"AND SUM(abw.cost) - fms.totalInternalRate > 1 " +
				"ORDER BY requestId ";
			Collection colExtra = dao.select(sqlExtra, EngineeringRequest.class, null, 0, -1);
			pageContext.setAttribute("colExtra", colExtra);
			
			//TODO:
			if (fixIt) {
				for (Iterator iterator = colExtra.iterator(); iterator.hasNext();) {
					EngineeringRequest req = (EngineeringRequest) iterator.next();
					String requestId = req.getRequestId();
					
					HashMap map = queryInfo(dao, requestId, pageContext);
					deleteInfo(dao, requestId, pageContext);
				}
			}
		} catch (Exception e) {
			abwPrefixOkay = false;
			e.printStackTrace();
		}
	} else {	
		HashMap map = queryInfo(dao, showRequestId, pageContext);
		abwPrefixOkay = (Boolean) map.get("abwPrefixOkay");
		internalRequest = (Boolean) map.get("internalRequest");
		correctAmount = (BigDecimal) map.get("correctAmount");
		
		if (fixIt) {
			deleteInfo(dao, showRequestId, pageContext);
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
						<td>Title</td>
						<td>Status</td>
						<td>Status Label</td>
						<td>Internal Rate</td>
						<td>ABW Total</td>
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
							<td align="right">&nbsp;<fmt:formatNumber value="${req.totalInternalRate}" minFractionDigits="2" /></td>
							<td align="right">&nbsp;<fmt:formatNumber value="${req.propertyMap['totalAbw']}" minFractionDigits="2" /></td>
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
		
			<%
				BigDecimal total = BigDecimal.ZERO;
				BigDecimal totalWithError = BigDecimal.ZERO;
			%>
		
			<a href="fixAbw.jsp">&lt;&lt; back to main listing</a><br><br>
			Show <%= showRequestId %>:<br>
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
							<td><a href="infoRequest.jsp?requestId=<c:out value="${req.requestId}" />"><c:out value="${req.requestId}" /></a></td>
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
			
			ABW:
			<table border="1" cellpadding="3" cellspacing="0">
				<thead class="niceStyle">
					<tr>
						<td>Service Type</td>
						<td>ABW Code</td>
						<td align="right">Cost</td>
						<td>Cancellation Ind</td>
						<td>ABW Status</td>
						<td>Created Date</td>
						<td>Action</td>
					</tr>
				</thead>
				<tbody>
					<c:set var="recordHasProblems" value="false" />
					<c:forEach items="${colRecord}" var="req" varStatus="status">
						<c:set var="cost" value="${req.propertyMap['cost']}" />
						<%
							BigDecimal cost = (BigDecimal) pageContext.getAttribute("cost");
							total = total.add(cost);
						%>
						<c:set var="recStyle" value=""/>
						<c:set var="recComment" value="&nbsp;"/>
						<c:if test="${(empty(req.serviceId) && req.propertyMap['cancellation_ind'] != 'P') || req.propertyMap['duplicate'] == '1'}">
							<c:set var="recStyle" value="extraStyle"/>
							<c:set var="recComment" value="To be removed"/>
							<c:set var="recordHasProblems" value="true" />
							<%
								totalWithError = totalWithError.add(cost);
							%>
						</c:if>
						<tr class="<c:out value="${recStyle}"/>">
							<td><c:out value="${req.propertyMap['type']}" /></td>
							<td><c:out value="${req.propertyMap['ratecardAbwCode']}" />&nbsp;</td>
							<td align="right"><fmt:formatNumber value="${req.propertyMap['cost']}" minFractionDigits="2" /></td>
							<td><c:out value="${req.propertyMap['cancellation_ind']}" /></td>
							<td><c:out value="${req.propertyMap['abwStatus']}" /></td>
							<td><fmt:formatDate value="${req.propertyMap['createdDate']}" pattern="<%= DATE_TIME_PATTERN %>"/></td>
							<td><c:out value="${recComment}" escapeXml="false" /></td>
						</tr>
					</c:forEach>
					
					<tr>
						<td align="right" colspan="2">Total:</td>
						<% if (correctAmount.compareTo(total) == 0) { %>
							<td align="right" style="background-color: #99CC66"><%= formatNum(total) %></td>
						<% } else { %>
							<td align="right"><%= formatNum(total) %></td>
						<% } %>
						<td colspan="34">&nbsp;</td>
					</tr>
					<% if (!totalWithError.equals(BigDecimal.ZERO)) { %>
					<tr>
						<td align="right" colspan="2">Total with error:</td>
						<td align="right"><%= formatNum(totalWithError) %></td>
						<td colspan="34">&nbsp;</td>
					</tr>
					<tr>
						<td align="right" colspan="2">Total after fix:</td>
						<% if (correctAmount.compareTo(total.subtract(totalWithError)) == 0) { %>
							<td align="right" style="background-color: #99CC66"><%= formatNum(total.subtract(totalWithError)) %></td>
						<% } else { %>
							<td align="right"><%= formatNum(total.subtract(totalWithError)) %></td>
						<% } %>
						<td colspan="34">&nbsp;</td>
					</tr>
					<% } %>
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