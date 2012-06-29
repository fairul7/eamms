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

function expandSendToOtherDept() {
	var newDiv = getObject("sendToAnotherDeptPanel");
	newDiv.style.display = "block";
}

function exportRequest() {
    document.location = "/ekms/isr/requestorViewRequestPrintable.jsp?requestId=<c:out value='${w.requestId}' />";
    initializeTimer();
}

function actionCancel() {
	if(confirmCancel()) {
		document.location = "/ekms/isr/viewRequestListing.jsp";
	}
}

function confirmSendOtherDept() {
	if(confirm("<fmt:message key='isr.message.confirmSendOtherDept'/>")) {
		return true;
	}
	else {
		return false;
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
        document.location = "/ekms/isr/requestorEditRequest.jsp?requestId=<c:out value='${w.requestId}' />";;
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
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.attachmentByRequestingDept"/> <br />(max <c:out value="${w.maxUploadSize }"/> each)</td>
		<td class="contentBgColor">
			<x:display name="${w.attachmentPanel.absoluteName}"/> 
		</td>
	</tr>	
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.message.dueDate"/></td>
		<td class="contentBgColor">
			<x:display name="${w.dueDate.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.requestedDateTime"/></td>
		<td class="contentBgColor">
			<x:display name="${w.dateCreated.absoluteName}"/> 
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
	<c:if test="${!empty w.checkSendAnotherDept}">
		<tr>
			<td class="contentBgColor">&nbsp;</td>
			<td class="contentBgColor">
			<x:display name="${w.checkSendAnotherDept.absoluteName}"/> Send to Another Department
			<div id="sendToAnotherDeptPanel" style="display:none;">
				<x:display name="${w.multipleAttentionTo.absoluteName}"/>
				<br/><x:display name="${w.btnSend.absoluteName}"/>
			</div>
			</td>
		</tr>
	</c:if>
</table>

<!-- Remarks -->
<c:if test="${w.addRemarks }">
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0;" colspan="2"><fmt:message key="isr.label.remarks"/></td>
	</tr>
	<c:if test="${!empty w.remarksPanel }">
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.remarks"/></td>
		<td class="contentBgColor">
			<div style="text-align:right;">
			<c:choose>
				<c:when test="${w.remarksDesc}">
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorEditRequest.jsp?requestId=<c:out value="${param.requestId }"/>&remarksDesc=false">Ascending</a>
				</c:when>
				<c:otherwise>
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorEditRequest.jsp?requestId=<c:out value="${param.requestId }"/>&remarksDesc=true">Descending</a>
				</c:otherwise>
			</c:choose>
			</div>
			<x:display name="${w.remarksPanel.absoluteName}"/>
		</td>
	</tr>
	</c:if>
	<c:if test="${!empty w.addRemarksTextBox }">
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.addRemarks"/></td>
		<td class="contentBgColor">
			<x:display name="${w.addRemarksTextBox.absoluteName}"/>
		</td>
	</tr>
	</c:if>
</table>
</c:if>

<!-- Clarification -->
<c:if test="${w.addClarification }">
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td class="contentTitleFont" style="padding:5px; background-color:#1863B0;" colspan="2"><fmt:message key="isr.label.clarification"/></td>
	</tr>
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.clarification"/></td>
		<td class="contentBgColor">
			<x:display name="${w.clarification.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" valign="top" class="contentBgColor fieldTitle"><fmt:message key="isr.label.replyToClarification"/> *</td>
		<td class="contentBgColor">
			<x:display name="${w.replyToClarification.absoluteName}"/>
		</td>
	</tr>
</table>
</c:if>

<!-- Submit and Cancel Buttons -->
<c:if test="${!empty w.btnSubmit}">
<table width="100%" border="0" cellspacing="1" cellpadding="4" >
	<tr>
		<td width="20%" valign="top" class="contentBgColor">&nbsp;</td>
		<td class="contentBgColor">
			<x:display name="${w.btnSubmit.absoluteName}"/> 
			<input type="button" class="button" value="<fmt:message key='isr.label.cancel' />" onclick="javascript:actionCancel()" /> 
			<input type="button" class="button" value="<fmt:message key='isr.label.exportRequest' />" onclick="javascript:exportRequest()" />
		</td>
	</tr>
</table>
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
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorEditRequest.jsp?requestId=<c:out value="${param.requestId }"/>&clarificationDesc=false">Ascending</a>
				</c:when>
				<c:otherwise>
					<a href="<c:out value='${pageContext.request.contextPath }' />/ekms/isr/requestorEditRequest.jsp?requestId=<c:out value="${param.requestId }"/>&clarificationDesc=true">Descending</a>
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

<script type="text/javascript">
var form = document.forms['<c:out value="${w.absoluteName}"/>'];
var checkSendAnotherDept = form.elements['<c:out value="${w.absoluteName}"/>.checkSendAnotherDept'];

if(checkSendAnotherDept != null) {
	if(checkSendAnotherDept.checked)
		expandSendToOtherDept();
}
</script>