<%@include file="/common/header.jsp"%>
 
<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<style type="text/css">
.expandableRowHeader {
	cursor:hand; 
	cursor:pointer;
}
</style>

<script type="text/javascript">
// Check if the browser is IE4
var ie4 = false;
if(document.all) {
	ie4 = true;
}

// Get an object by ID
function getObject(id) {
	if (ie4) {
		return document.all[id];
	}
	else {
		return document.getElementById(id);
	}
}

// Toggle the visibility of a specific DIV
function togglePanelVisibility(divId, stateId) {
    var divObj = getObject(divId);

    if(divObj.style.display == 'none') {
        divObj.style.display = 'block';
        if(stateId != null) {
            var stateObj = getObject(stateId);
            stateObj.innerHTML = "[ - ]";
        }
    }
    else if(divObj.style.display == 'block') {
        divObj.style.display = 'none';
        if(stateId != null) {
            var stateObj = getObject(stateId);
            stateObj.innerHTML = "[ + ]";
        }
    }
}

function exportRequest() {
    document.location = "/ekms/isr/requestorViewRequestPrintable.jsp?requestId=<c:out value='${w.requestId}' />";
    initializeTimer();
}

function backToForceClosure() {
	if(confirmCancel()) {
		document.location = "forceClosure.jsp";
	}
}

function actionCancel() {
	if(confirmCancel()) {
		document.location = "/ekms/isr/viewRequestListing.jsp";
	}
}

var secs;
var timerID = null;
var timerRunning = false;
var delay = 1000;

function initializeTimer()
{
    // Set the length of the timer, in seconds
    secs = 3;
    stopTheClock();
    startTheTimer();
}

function stopTheClock()
{
    if(timerRunning)
        clearTimeout(timerID);
    timerRunning = false;
}

function startTheTimer()
{
    if (secs==0)
    {
        stopTheClock();
        document.location = "/ekms/isr/requestorResolveRequest.jsp?requestId=<c:out value='${w.requestId}' />";;
    }
    else
    {
        self.status = secs;
        secs = secs - 1;
        timerRunning = true;
        timerID = self.setTimeout("startTheTimer()", delay);
    }
}
</script>

<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td valign="top" class="contentBgColor fieldTitle" width="20%"><fmt:message key="isr.label.requestId"/></td>
		<td class="contentBgColor">
			<c:out value="${param.requestId }" />
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle" width="20%"><fmt:message key="isr.label.requestingDept"/></td>
		<td class="contentBgColor">
			<x:display name="${w.requestingDepartment.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.attentionTo"/></td>
		<td class="contentBgColor">
			<x:display name="${w.attentionTo.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.subject"/></td>
		<td class="contentBgColor">
			<x:display name="${w.subject.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.description"/></td>
		<td class="contentBgColor">
			<x:display name="${w.description.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.status"/></td>
		<td class="contentBgColor">
			<x:display name="${w.status.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.priorityByRequester"/></td>
		<td class="contentBgColor">
			<x:display name="${w.priority.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.attachmentByRequestingDept"/></td>
		<td class="contentBgColor">
			<x:display name="${w.attachmentPanel.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.requestedDateTime"/></td>
		<td class="contentBgColor">
			<x:display name="${w.dateCreated.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.message.dueDate"/></td>
		<td class="contentBgColor">
			<x:display name="${w.dueDate.absoluteName}"/> 
		</td>
	</tr>	
	<c:if test="${!empty w.relatedRequests }">
		<tr>
			<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.relatedRequests"/></td>
			<td class="contentBgColor">
				<table border="0" cellpadding="3" cellspacing="0" width="100%">
					<tr>
						<td style="font-weight:bold; background-color:#CCCCCC;"><fmt:message key="isr.label.requestId"/></td>
						<td style="font-weight:bold; background-color:#CCCCCC;"><fmt:message key="isr.label.recvDept"/></td>
						<td style="font-weight:bold; background-color:#CCCCCC;"><fmt:message key="isr.label.date" /></td>
						<td style="font-weight:bold; background-color:#CCCCCC;"><fmt:message key="isr.label.status" /></td>
						<td style="font-weight:bold; background-color:#CCCCCC;"><fmt:message key="isr.label.resolution" /></td>
					</tr>
					<c:forEach var="relatedRequest" items="${w.relatedRequests}">
						<tr>
							<td valign="top"><c:out value="${relatedRequest.requestIdRequestorUrl}" escapeXml="false" /></td>
							<td valign="top"><c:out value="${relatedRequest.requestToDeptName}" /></td>
							<td valign="top"><fmt:formatDate pattern="${globalDatetimeLong}" value="${relatedRequest.dateCreated}" /></td>
							<td valign="top"><c:out value="${relatedRequest.requestStatusName}" escapeXml="false" /></td>
							<td valign="top"><c:out value="${relatedRequest.requestResolution}" />
								<c:if test="${!empty relatedRequest.resolutionAttachments}">
									<c:forEach var="attachment" items="${relatedRequest.resolutionAttachments}">
										<br/><a href="<c:out value='${pageContext.request.contextPath }'/>/isr/downloadResolutionAttachment?resolutionAttachmentId=<c:out value='${attachment.resolutionAttachmentId}' />"><c:out value='${attachment.oriFileName}'/></a>
									</c:forEach>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</c:if>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.resolution"/></td>
		<td class="contentBgColor">
			<x:display name="${w.resolution.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.resolutionAttachment"/></td>
		<td class="contentBgColor">
			<c:if test="${!empty w.resolutionAttachmentPanel}">
				<x:display name="${w.resolutionAttachmentPanel.absoluteName}"/> 
			</c:if>
		</td>
	</tr>
</table>

<!-- Submit and Cancel Buttons -->
<c:if test="${!empty w.btnReopen}">
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td width="20%" valign="top" class="contentBgColor">&nbsp;</td>
		<td class="contentBgColor">
			<span style="color:#999999">(Request will be automatically closed if there is no reply within <c:out value="${w.autoClosureDay}"/> days starting from the resolution date)</span>
			<br /><x:display name="${w.btnReopen.absoluteName}"/> 			
			<c:choose>
				<c:when test="${!empty param.backToForceClosure }">
					<c:if test="${param.backToForceClosure eq 'true'}">
						<input type="button" class="button" value="<fmt:message key='isr.label.cancel'/>" onclick="javascript:backToForceClosure()"> 
					</c:if>
				</c:when>
				<c:otherwise>
					<input type="button" class="button" value="<fmt:message key='isr.label.cancel' />" onclick="javascript:actionCancel()" /> 
				</c:otherwise>
			</c:choose>
			<input type="button" class="button" value="<fmt:message key='isr.label.exportRequest' />" onclick="javascript:exportRequest()" />
		</td>
	</tr>
</table>
</c:if>

<!-- Resolutions -->
<c:if test="${!empty w.suggestedResolutionPanel }">
<table width="100%" border="0" cellspacing="0" cellpadding="4" >
	<tr>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; border-bottom:1px solid #FFFFFF;" colspan="2">
			<div class="expandableRowHeader" onClick="javascript:togglePanelVisibility('suggestedResolutionsDiv', 'suggestedResolutionsDivState')">
			<span id="suggestedResolutionsDivState">[
			<!-- <c:choose><c:when test="${!empty param.suggestedResolutionDesc}"> - </c:when><c:otherwise> + </c:otherwise></c:choose> --> -
			]</span> <fmt:message key="isr.label.resolutions"/>
			</div>
		</td>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; text-align:right; border-bottom:1px solid #FFFFFF;" colspan="2">
			<div class="expandableRowHeader" onClick="javascript:togglePanelVisibility('suggestedResolutionsDiv', 'suggestedResolutionsDivState')">
			<span style="font-weight:normal; color:#FFE900">Click to Show/Hide</span>
			</div>
		</td>
	</tr>
</table>
<div id="suggestedResolutionsDiv" style="display:block;"> <!-- <c:choose><c:when test="${!empty param.suggestedResolutionDesc}"> style="display:block;" </c:when><c:otherwise> style="display:none;" </c:otherwise></c:choose> -->
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.resolutions"/></td>
		<td class="contentBgColor">
			<div style="text-align:right;">
			<c:choose>
				<c:when test="${w.suggestedResolutionDesc}">
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorResolveRequest.jsp?requestId=<c:out value="${param.requestId }"/>&suggestedResolutionDesc=false">Ascending</a>
				</c:when>
				<c:otherwise>
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorResolveRequest.jsp?requestId=<c:out value="${param.requestId }"/>&suggestedResolutionDesc=true">Descending</a>
				</c:otherwise>
			</c:choose>
			</div>
			<x:display name="${w.suggestedResolutionPanel.absoluteName}"/>
		</td>
	</tr>
</table>
</div>
</c:if>

<!-- Remarks -->
<c:if test="${!empty w.remarksPanel }">
<table width="100%" border="0" cellspacing="0" cellpadding="4" >
	<tr>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; border-bottom:1px solid #FFFFFF;" colspan="2">
			<div class="expandableRowHeader" onClick="javascript:togglePanelVisibility('remarksDiv', 'remarksDivState')">
			<span id="remarksDivState">[
			<!-- <c:choose><c:when test="${!empty param.remarksDesc}"> - </c:when><c:otherwise> + </c:otherwise></c:choose> --> -
			]</span> <fmt:message key="isr.label.remarks"/>
			</div>
		</td>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; text-align:right; border-bottom:1px solid #FFFFFF;" colspan="2">
			<div class="expandableRowHeader" onClick="javascript:togglePanelVisibility('remarksDiv', 'remarksDivState')">
			<span style="font-weight:normal; color:#FFE900">Click to Show/Hide</span>
			</div>
		</td>
	</tr>
</table>
<div id="remarksDiv" style="display:block;"> <!-- <c:choose><c:when test="${!empty param.remarksDesc}"> style="display:block;" </c:when><c:otherwise> style="display:none;" </c:otherwise></c:choose> -->
<table width="100%" border="0" cellspacing="1" cellpadding="4" >	
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.remarks"/></td>
		<td class="contentBgColor">
			<div style="text-align:right;">
			<c:choose>
				<c:when test="${w.remarksDesc}">
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorResolveRequest.jsp?requestId=<c:out value="${param.requestId }"/>&remarksDesc=false">Ascending</a>
				</c:when>
				<c:otherwise>
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorResolveRequest.jsp?requestId=<c:out value="${param.requestId }"/>&remarksDesc=true">Descending</a>
				</c:otherwise>
			</c:choose>
			</div>
			<x:display name="${w.remarksPanel.absoluteName}"/>
		</td>
	</tr>
</table>
</div>
</c:if>

<!-- Clarification Messages -->
<c:if test="${!empty w.clarificationMessagesPanel }">
<table width="100%" border="0" cellspacing="0" cellpadding="4" >
	<tr>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; border-bottom:1px solid #FFFFFF;" colspan="2">
			<div class="expandableRowHeader" onClick="javascript:togglePanelVisibility('clarificationMessagesDiv', 'clarificationMessagesDivState')">
			<span id="clarificationMessagesDivState">[
			<!-- <c:choose><c:when test="${!empty param.clarificationDesc}"> - </c:when><c:otherwise> + </c:otherwise></c:choose> --> -
			]</span> <fmt:message key="isr.label.clarificationMessages"/>
			</div>
		</td>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0; text-align:right; border-bottom:1px solid #FFFFFF;" colspan="2">
			<div class="expandableRowHeader" onClick="javascript:togglePanelVisibility('clarificationMessagesDiv', 'clarificationMessagesDivState')">
			<span style="font-weight:normal; color:#FFE900">Click to Show/Hide</span>
			</div>
		</td>
	</tr>
</table>
<div id="clarificationMessagesDiv" style="display:block;"><!-- <c:choose><c:when test="${!empty param.clarificationDesc}"> style="display:block;" </c:when><c:otherwise> style="display:none;" </c:otherwise></c:choose> -->
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.clarificationMessages"/></td>
		<td class="contentBgColor">
			<div style="text-align:right;">
			<c:choose>
				<c:when test="${w.clarificationDesc}">
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorResolveRequest.jsp?requestId=<c:out value="${param.requestId }"/>&clarificationDesc=false">Ascending</a>
				</c:when>
				<c:otherwise>
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorResolveRequest.jsp?requestId=<c:out value="${param.requestId }"/>&clarificationDesc=true">Descending</a>
				</c:otherwise>
			</c:choose>
			</div>
			<x:display name="${w.clarificationMessagesPanel.absoluteName}"/>
		</td>
	</tr>
</table>
</div>
</c:if>

<jsp:include page="../form_footer.jsp" flush="true"/>