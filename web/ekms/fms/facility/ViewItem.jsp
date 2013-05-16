<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.ItemForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<x:config>
	<page name="ViewItem">
		<com.tms.fms.facility.ui.ItemForm name="form"/>
	</page>
</x:config>

<c:choose>
	<c:when test="${not empty(param.fid)}">
		<c:set var="facilityID" value="${param.fid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="facilityID" value="${widgets['ViewItem.form'].facilityId}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty(param.rid)}">
		<c:set var="itemID" value="${param.rid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="itemID" value="${widgets['ViewItem.form'].itemCode}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty(param.mode)}">
		<c:set var="mode" value="${param.mode}"/>
	</c:when>
	<c:otherwise>
		<c:set var="mode" value="${widgets['ViewItem.form'].mode}"/>
	</c:otherwise>
</c:choose>

<x:set name="ViewItem.form" property="mode" value="${mode}"/>
<x:set name="ViewItem.form" property="itemCode" value="${itemID}"/>
<x:set name="ViewItem.form" property="facilityId" value="${facilityID}"/>
<x:set name="ViewItem.form" property="action" value="<%= ItemForm.FORM_ACTION_VIEW %>"/>
<x:set name="ViewItem.form" property="cancelUrl" value="ViewFacility.jsp?fid=${facilityID}"/>
<x:set name="ViewItem.form" property="statusUrl" value="StatusLogs.jsp?fid=${facilityID}&rid=${itemID}"/>
<x:set name="ViewItem.form" property="editUrl" value="EditItem.jsp?fid=${facilityID}&rid=${itemID}"/>
<c:if test="${mode == 'search'}">
	<x:set name="ViewItem.form" property="cancelUrl" value="BarcodeSearching.jsp"/>
	<x:set name="ViewItem.form" property="statusUrl" value="StatusLogs.jsp?fid=${facilityID}&rid=${itemID}&mode=search"/>
	<x:set name="ViewItem.form" property="editUrl" value="EditItem.jsp?fid=${facilityID}&rid=${itemID}&mode=search"/>
</c:if>
<x:set name="ViewItem.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="../../includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.viewItemDetail'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="ViewItem.form" ></x:display>
    </td></tr>
    
    <tr valign="middle">
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.viewItemTrail'/></font></b></td>
	    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
    <tr>
    	<td colspan="2">
    		<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="100%">
				<thead>
					<tr class="tableHeader">
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailRequestTitle'/></td>
						<%-- <td style="padding:5px"><fmt:message key='fms.facility.title.trailAssignmentNo'/></td>--%>
						<td style="padding:5px"><fmt:message key='fms.request.label.requestId'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailDateCheckedIn'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailCheckedInBy'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailDateCheckedOut'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailCheckedOutBy' /></td>
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailTakenBy'/></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${widgets[ 'ViewItem.form' ].allRequestList}" var="req">
					    <tr>
					        <td><c:out value="${req.title}" /></td>
					        <%-- <td><a href="requestDetails.jsp?requestId=${req.requestId}"><c:out value="${req.assignmentCode}" /></a></td>--%>
					        <td><a href="requestDetails.jsp?page=all&requestId=${req.requestId}"><c:out value="${req.requestId}" /></a></td>
					        <td><fmt:formatDate value="${req.checkedInDate}" pattern="d MMM yyyy h:mm a" /></td>
					        <td><c:out value="${req.checkedInBy}" /></td>
					        <td><fmt:formatDate value="${req.checkedOutDate}" pattern="d MMM yyyy h:mm a"/></td>
					        <td><c:out value="${req.checkedOutBy}" /></td>
					        <td><c:out value="${req.takenBy}" /></td>
					    </tr>
				    </c:forEach>
			    </tbody>
			</table>
    	</td>
    </tr>
    <tr valign="middle">
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.internalCheckoutHistory'/></font></b></td>
	    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
    <tr>
    	<td colspan="2">
    		<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="100%">
				<thead>
					<tr class="tableHeader">
						<td style="padding:5px"><fmt:message key='fms.facility.form.requestor'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.form.location'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.form.purpose'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.table.checkInDate'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.table.checkInBy'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.table.checkOutDate' /></td>
						<td style="padding:5px"><fmt:message key='fms.facility.table.checkOutBy'/></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${widgets[ 'ViewItem.form' ].internalCheckoutHistoryList}" var="req1">
					    <tr>
					        <td><c:out value="${req1.requestedByName}" /></td>
					        <td><c:out value="${req1.location_name}"/></td>
					        <td><c:out value="${req1.purpose}" /></td>					        
					        <td><fmt:formatDate pattern="${globalDatetimeLong}" value="${req1.checkin_date}"/></td>
							<td><c:out value="${req1.checkin_by_name}"/></td>
	            			<td><fmt:formatDate pattern="${globalDatetimeLong}" value="${req1.checkout_date}"/></td>
							<td><c:out value="${req1.checkout_by}"/></td>
	            			
					    </tr>
				    </c:forEach>
			    </tbody>
			</table>
    	</td>
    </tr>
    <tr valign="middle">
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">Undo History</font></b></td>
	    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
    <tr>
    	<td colspan="2">
    		<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="100%">
				<thead>
					<tr class="tableHeader">
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailRequestTitle'/></td>
						<td style="padding:5px"><fmt:message key='fms.request.label.requestId'/></td>
						<td style="padding:5px">Undo Date</td>
						<td style="padding:5px">Undo By</td>
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailDateCheckedOut'/></td>
						<td style="padding:5px"><fmt:message key='fms.facility.title.trailCheckedOutBy' /></td>
						<td style="padding:5px">Undo Type</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${widgets[ 'ViewItem.form' ].undoCheckoutListByBarcode}" var="req">
					    <tr>
					        <td><c:out value="${req.title}" /></td>
					        <td><a href="requestDetails.jsp?page=all&requestId=${req.requestId}"><c:out value="${req.requestId}" /></a></td>
					        <td><fmt:formatDate value="${req.checkedInDate}" pattern="d MMM yyyy h:mm a" /></td>
					        <td><c:out value="${req.checkedInBy}" /></td>
					        <td><fmt:formatDate value="${req.checkedOutDate}" pattern="d MMM yyyy h:mm a"/></td>
					        <td><c:out value="${req.checkedOutBy}" /></td>
					        <td><c:out value="${req.propertyMap['undoType']}" /></td>
					    </tr>
				    </c:forEach>
			    </tbody>
			</table>
    	</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<c:if test="${widgets['ViewItem.form'].status == 'W' or widgets['ViewItem.form'].status == 'M'}">
	<script language="JavaScript">
		document.forms['ViewItem.form'].elements['button*ViewItem.form.pnButton.btnEdit'].style.display = 'none';
		document.forms['ViewItem.form'].elements['button*ViewItem.form.pnButton.btnStatus'].style.display = 'none';
	</script>
</c:if>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>