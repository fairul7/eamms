<%@ page import="java.util.Calendar"%>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="maxFractionDigits" value="2" />

<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/util.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/interface/TimeSheetModule.js'/>"></script>

<style type="text/css">
.opacified {
	opacity:.5;
	-moz-opacity:.5;
	filter:alpha(opacity=50);
}

@media print {
	.inviPrint {
		display:none;
	}
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

// When Tab is pressed, move mouse cursor to the next +/- sign
// When Enter is pressed, save the remarks. This is a workaround for IE, as it doesn't treat Enter with onChange event
function specialKeyFunctions(taskId, assigneeUserId, textfieldObj, evt)  {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if(charCode == 9) {
		nextTextField(textfieldObj);
	}
	if(charCode == 13) {
		insertRemarks(taskId, assigneeUserId, textfieldObj);
	}
}

// Insert remarks into database using DWR
function insertRemarks(taskId, assigneeUserId, remarksObj) {
	DWRUtil.setValue("updateRemarksSaveStatus", "<fmt:message key='timesheet.label.savingRemarks'/>");
	TimeSheetModule.insertMandayReportRemarks(taskId, assigneeUserId, remarksObj.value, updateRemarksSaveStatus);

	var newRemarks = remarksObj.value;
	var insertRemarksDiv = getObject(findReplace(remarksObj.name, 'remarks', 'insertRemarks'));
	insertRemarksDiv.style.display = "none";

	var updatingRemarksSpanId = findReplace(remarksObj.name, 'remarks', 'remarksViewText');
	var viewOnlyRemarksSpan = getObject(updatingRemarksSpanId);
	viewOnlyRemarksSpan.innerHTML = newRemarks;
}

// Update the label which says about the status of autosave
function updateRemarksSaveStatus(isSuccess) {
	var dateVar = new Date();
	//Date.prototype.getFullYear = getFullYear;
	//Date.prototype.getActualMonth = getActualMonth;
	//Date.prototype.getActualDay = getActualDay;
	//Date.prototype.getCalendarDay = getCalendarDay;
	//Date.prototype.getCalendarMonth = getCalendarMonth;
	Date.prototype.getFormattedTime = getFormattedTime;

	var successMessage = "<fmt:message key='timesheet.label.remarksAutosavedAt'/> " + dateVar.getFormattedTime();
	var failureMessage = "<font color=#CC0000><strong><fmt:message key='timesheet.label.remarksFailedToBeSave'/> " + dateVar.getFormattedTime() + "</strong></font>";

	if(isSuccess) {
		DWRUtil.setValue("updateRemarksSaveStatus", successMessage);
	}
	else {
		DWRUtil.setValue("updateRemarksSaveStatus", failureMessage);
		document.location = "#status";
	}
}

// Retrive current time formatted as HH:MM:SS am/pm
function getFormattedTime() {
	var hours = this.getHours();
	var ampmSign = "am";
	if(hours > 12) {
		hours -= 12;
		ampmSign = "pm";
	}

	var minutes = new String(this.getMinutes());
	if(minutes.length <= 1) {
		minutes = "0" + minutes;
	}
	var seconds = new String(this.getSeconds());
	if(seconds.length <= 1) {
		seconds = "0" + seconds;
	}
	return formattedTime = hours + ":" + minutes + ":" + seconds + " " + ampmSign;
}

function getFullYear() {
	var n = this.getYear();
	n += 0;
	return n;
}

function getActualMonth() {
	var n = this.getMonth();
	n += 1;
	return n;
}

function getActualDay() {
	var n = this.getDay();
	n += 1;
	return n;
}

function getCalendarDay() {
	var n = this.getDay();
	var dow = new Array(7);
	Dow[0] = "Sun";
	Dow[1] = "Mon";
	Dow[2] = "Tue";
	Dow[3] = "Wed";
	Dow[4] = "Thu";
	Dow[5] = "Fri";
	Dow[6] = "Sat";
	return Dow[n];
}

function getCalendarMonth() {
	var n = this.getMonth();
	var moy = new Array(12);

	moy[0] = "Jan";
	moy[1] = "Feb";
	moy[2] = "Mar";
	moy[3] = "Apr";
	moy[4] = "May";
	moy[5] = "Jun";
	moy[6] = "Jul";
	moy[7] = "Aug";
	moy[8] = "Sep";
	moy[9] = "Oct";
	moy[10] = "Nov";
	moy[11] = "Dec";
	return moy[n];
}

// When Tab is pressed, this function will be called to position the mouse cursor on the next +/- sign
function nextTextField(textfieldObj) {
	var focusNextTextfield = false;

	var theForm = textfieldObj.form;
	for(var z=0; z<theForm.length; z++) {
		if(theForm[z].type == 'text') {
			if(focusNextTextfield) {
				theForm[z-1].focus();
				focusNextTextfield = false;
			}
			else {
				if(theForm[z] == textfieldObj) {
					focusNextTextfield = true;
				}
			}
		}
	}
}

// Alt the opacity of child row based on checkbox state
function altOpacityWithCheckBox(checkbox) {
	var tdName = findReplace(checkbox.name, 'check', 'td');
	var tdObj = document.getElementsByName(tdName);

	if(tdObj != null) {
		// Single td
		if(tdObj.length != null) {
			for(var i=0; i<tdObj.length; i++) {
				if(checkbox.checked) {
					tdObj[i].className = "tableRow";
				}
				else {
					tdObj[i].className = "tableRow opacified";
				}
			}
		}
		// Multiple td
		else {
			if(checkbox.checked) {
				tdObj.className = "tableRow";
			}
			else {
				tdObj.className = "tableRow opacified";
			}
		}
	}
}

// Alt the opacity of child row based on remarks textfield state
// If textfield is empty, then opacified; else otherwise
function altOpacityWithTextField(textfield) {
	var tdName = findReplace(textfield.name, 'remarks', 'td');
	var tdObj = document.getElementsByName(tdName);

	if(tdObj != null) {
		// Single td
		if(tdObj.length != null) {
			for(var i=0; i<tdObj.length; i++) {
				if(trimString(textfield.value) != "") {
					tdObj[i].className = "tableRow";
				}
				else {
					tdObj[i].className = "tableRow opacified";
				}
			}
		}
		// Multiple td
		else {
			if(trimString(textfield.value) != "") {
				tdObj.className = "tableRow";
			}
			else {
				tdObj.className = "tableRow opacified";
			}
		}
	}
}

// Remove opacity of the row; in other words, the row is fully visible as normal
function fullOpacity(textfield) {
	var tdName = findReplace(textfield.name, 'remarks', 'td');
	var tdObj = document.getElementsByName(tdName);

	if(tdObj != null) {
		for(var i=0; i<tdObj.length; i++) {
			tdObj[i].className = "tableRow";
		}
	}
}

// Based on the remarks textfield object given, enable opacity for the row
function enableOpacityWithTextField(textfield) {
	var tdName = findReplace(textfield.name, 'remarks', 'td');
	var tdObj = document.getElementsByName(tdName);

	if(tdObj != null) {
		for(var i=0; i<tdObj.length; i++) {
			tdObj[i].className = "tableRow opacified";
		}
	}
}

// Select all checkboxes
function selectAllCheckboxes(checkboxObj) {
	var theForm = checkboxObj.form;
	for(var z=0; z<theForm.length; z++){
		if(theForm[z].type == 'checkbox') {
			theForm[z].checked = checkboxObj.checked;
			if(theForm[z].name != "selectAll")
				altOpacityWithCheckBox(theForm[z]);
		}
	}
}

// Unselect all checkboxes
function unselectAllSubCheckboxes(parentCheckbox) {
	if(! parentCheckbox.checked) {
		var theForm = parentCheckbox.form;

		for(var z=0; z<theForm.length; z++){
			if(theForm[z].type == 'checkbox') {
				if(theForm[z].name.indexOf(parentCheckbox.name) != -1 && theForm[z].name != parentCheckbox.name) {
					theForm[z].checked = false;
					altOpacityWithCheckBox(theForm[z]);
				}
			}
		}
	}
}

// Find a keyword in string, and replace with another
function findReplace(oriString, find, replace) {
	var regEx = new RegExp(find, 'gi');
	return oriString.replace(regEx, replace);
}

// Trim the given string
function trimString(sInString) {
  sInString = sInString.replace( /^\s+/g, "" );// strip leading
  return sInString.replace( /\s+$/g, "" );// strip trailing
}

// Toggle the visibility of a specific DIV
function togglePanelVisibility(divId, stateId) {
    var divObj = getObject(divId);

    if(divObj.style.display == 'none') {
        divObj.style.display = 'block';
        if(stateId != null) {
            var stateObj = getObject(stateId);
            stateObj.innerHTML = "<img src=<c:out value='${pageContext.request.contextPath}'/>/ekms/images/ekp2005/ic_comment.gif border=0 />";
        }

        var remarksObj = getObject(findReplace(divId, 'insertRemarks', 'remarks'));
        remarksObj.focus();
    }
    else if(divObj.style.display == 'block') {
        divObj.style.display = 'none';
        if(stateId != null) {
            var stateObj = getObject(stateId);
            stateObj.innerHTML = "<img src=<c:out value='${pageContext.request.contextPath}'/>/ekms/images/ekp2005/ic_comment.gif border=0 />";
        }
    }
}

function printableView() {
	var theForm = document.mandaysReportForm;
	var checkedItemsString = "";
	var parentCheckIndex = -1;
	var childCheckIndex = -1;
	var myRE = /kacang.services.security.User/;

	for(var z=0; z<theForm.length; z++) {
		if(theForm[z].type == 'checkbox') {
			var checkboxObj = theForm[z];
			if(checkboxObj.name != "selectAll") {
				if(checkboxObj.name.match(myRE)) {
					childCheckIndex++;
					if(checkboxObj.checked) {
						checkedItemsString += ">" + checkboxObj.name.substring(checkboxObj.name.indexOf('kacang.services.security.User'), checkboxObj.name.length);
						//alert(checkboxObj.name + " -> \n" + checkboxObj.name.substring(checkboxObj.name.indexOf('kacang.services.security.User'), checkboxObj.name.length));
						//checkedItemsString += childCheckIndex + ".";
					}
				}
				else {
					parentCheckIndex++;
					childCheckIndex = -1;
					if(checkboxObj.checked) {
						checkedItemsString += "|" + checkboxObj.name.substring('check_'.length, checkboxObj.name.length);
						//alert(checkboxObj.name + " -> \n" + checkboxObj.name.substring('check_'.length, checkboxObj.name.length));
						//checkedItemsString += "|" + parentCheckIndex + "-";
					}
				}
			}
		}
	}

	/*
	checkedItemsString is composed in the form of
	|task1>user1>user3|task2|task4>user2
	which denotes printing of
		task1
			user1
			user3
		task2
		task4
			user2
	*/
	if(checkedItemsString != "") {
		TimeSheetModule.setSelectedItems(checkedItemsString, openPrintableViewWindow);
	}
	else {
		alert("<fmt:message key='timesheet.message.mandaysReportNoCheck' />");
	}
}

function openPrintableViewWindow(isSuccess) {
	if(isSuccess) {
		var dateTimeForm = document.forms['<c:out value="${widget.absoluteName}"/>'];
		mywindow = window.open('printTimeSheetMandaysReport.jsp?projectid=<c:out value='${w.projectId}'/>&startDate=' + dateTimeForm['<c:out value="${widget.fromDate.absoluteName}"/>'].value + '&endDate=' + dateTimeForm['<c:out value="${widget.toDate.absoluteName}"/>'].value, 'mandaysReportPrintableView', 'resizable=1, scrollbars=yes, statusbar=1');
		mywindow.moveTo(0,0);
	}
}

</script>

<div id="printableRegion">
<table width="100%" cellpadding="5" cellspacing="0">
<tr>
    <td class="contentTitleFont" colspan="2">
	    <c:choose>
	    <c:when test="${w.print==true}">
		    <%
		        pageContext.setAttribute("today",Calendar.getInstance().getTime());
		    %>
	    	<fmt:message key="timesheet.label.mandaysReport"/> - <fmt:formatDate value="${today}" pattern="MM/yyyy"/>
	    </c:when>
	    <c:otherwise>
	    	<fmt:message key="timesheet.label.mandaysReport"/>
	    </c:otherwise>
	    </c:choose>
    </td>
</tr>
<tr class="contentBgColor">
    <td>
    	<strong><fmt:message key="timesheet.label.project"/>:</strong> <c:out value="${w.project.projectName}"/>
    </td>
    <td align="right">
    	<c:if test="${w.print==false}">
    		<input type="button" name="button" class="button" value="<fmt:message key='timesheet.label.printableView'/>" onclick="javascript:printableView()"/>
    	</c:if>
    </td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>

<c:set var="totalMandaysEstimatedThisProject" value="0" />
<c:set var="totalMandaysSpentThisProject" value="0" />

<jsp:include page="../form_header.jsp" flush="true"/>
<tr>
	<td class="contentBgColor" colspan="2" align="right">
		<table cellpadding="2" cellspacing="1" width="100%">
            <tr>
                <td><fmt:message key="project.error.startFrom"/>: </td>
                <td><x:display name="${widget.fromDate.absoluteName}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="project.error.endsAt"/>: </td>
                <td><x:display name="${widget.toDate.absoluteName}"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><x:display name="${widget.submit.absoluteName}"/></td>
            </tr>
		</table>
	</td>
</tr>
<jsp:include page="../form_footer.jsp" flush="true"/>
<tr>
	<td class="contentBgColor" colspan="2" align="right">

      <FONT COLOR="red">*<fmt:message key='timesheet.label.hoursequal'/></FONT>

	</td>
</tr>
<form name="mandaysReportForm">
<tr>
	<td class="contentBgColor" colspan="2" align="right">
		<a name="status"></a><span id='updateRemarksSaveStatus' />
	</td>
</tr>
<tr class="contentBgColor">
	<td colspan="2" class="tableBackground">
		<table width="100%" cellpadding="2" cellspacing="1">
			<tr>
				<td class="tableHeader" valign="top"><input type="checkbox" name="selectAll" onclick="javascript:selectAllCheckboxes(this)" /></td>
				<td class="tableHeader" valign="top"><fmt:message key="timesheet.label.taskName"/></td>
				<td class="tableHeader" valign="top"><fmt:message key="timesheet.label.estimatedMandays"/></td>
				<td class="tableHeader" valign="top"><fmt:message key="timesheet.label.actualMandaysSpent" /></td>
				<td class="tableHeader" valign="top"><fmt:message key="timesheet.label.variance" /></td>
				<!-- <td class="tableHeader" valign="top"><fmt:message key="timesheet.label.balance" /></td>   -->
				<td class="tableHeader" valign="top"><fmt:message key="timesheet.label.remarks" /></td>
			</tr>

			<!-- Tasks with Timesheet -->
			<c:forEach items="${w.task}" var="task">
				<c:set var="totalMandaysEstimatedThisTask" value="${task.estimationMandays*task.totalAssignee}" />
				<c:set var="totalMandaysEstimatedEachUserThisTask" value="${task.estimationMandays}" />
				<c:set var="totalMandaysEstimatedThisProject" value="${totalMandaysEstimatedThisProject + totalMandaysEstimatedThisTask}" />
				<c:set var="totalMandaysSpentThisProject" value="${totalMandaysSpentThisProject + task.totalMandaysSpent}" />

				<tr>
					<td class="tableRow">
						<input type="checkbox"
							name="check_<c:out
							value="${task.id}"/>"
							checked
							onclick="javascript:unselectAllSubCheckboxes(this)" /></td>
					<!-- Tasks with Timesheet -->
					<td class="tableRow" valign="top" style="font-weight:bold">
						<c:out value="${task.title}"/>
					</td>
					<!-- estimated manday -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${totalMandaysEstimatedThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					</td>
					<!-- manday spent -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />

					</td>
					<!-- variance -->
					<td class="tableRow" align="right">
					<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								</span>
							</c:if>
						
					</td>
					<!-- balance -->
					<!--
					<td class="tableRow" align="right">
						<c:choose>
						<c:when test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent <= 0}">
							0.0
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
						</c:otherwise>
						</c:choose>
					</td>
					 -->
					<!-- remarks -->
					<td class="tableRow">
						<a href="javascript:togglePanelVisibility('insertRemarks_<c:out value="${task.id}"/>', 'insertRemarksState_<c:out value="${task.id}"/>')" style="text-decoration:none;">
							<span id="insertRemarksState_<c:out value="${task.id}"/>"><img src="<c:out value='${pageContext.request.contextPath}'/>/ekms/images/ekp2005/ic_comment.gif" border="0" style="background-color:transparent;"/></span>
						</a>
						<span id="remarksViewText_<c:out value="${task.id}"/>"><c:out value="${task.remarks}"/></span>
					</td>
				</tr>
				<tr>
					<td colspan="7" class="tableRow" align="right">
						<div id="insertRemarks_<c:out value="${task.id}"/>" style="display:none">
							<input type="text" name="remarks_<c:out value="${task.id}"/>" class="textField" value="<c:out value="${task.remarks}"/>"
								onchange="javascript:insertRemarks('<c:out value="${task.id}"/>', '', this)"
								onkeydown="javascript:specialKeyFunctions('<c:out value="${task.id}"/>', '', this, event)"
								maxlength="255" size="100" />
						</div>
					</td>
				</tr>
				<c:forEach begin="0" end="${task.totalAssignee - 1}" var="userCount">
					<tr>
						<td class="tableRow opacified" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<input type="checkbox"
								name="check_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"
								id="check_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"
								onclick="javascript:altOpacityWithCheckBox(this)" />
						</td>
						<!-- assignee name -->
						<td class="tableRow opacified" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" style="padding-left:15px;" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:out value="${task.userList[userCount][1]}"/>
						</td>
						<!-- estimated manday -->
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>
						<!-- manday spent -->
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>
						<!-- variance -->
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								</span>
							</c:if>
						</td>
						<!-- balance -->
						<!--
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:choose>
							<c:when test="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount] <= 0}">
								0.0
							</c:when>
							<c:otherwise>
								<fmt:formatNumber value="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
							</c:otherwise>
							</c:choose>
						</td>
						-->
						<!-- remarks -->
						<td class="tableRow opacified" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<a href="javascript:togglePanelVisibility('insertRemarks_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>', 'insertRemarksState_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>')" style="text-decoration:none;">
								<span id="insertRemarksState_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"><img src="<c:out value='${pageContext.request.contextPath}'/>/ekms/images/ekp2005/ic_comment.gif" border="0" /></span>
							</a>
							<span id="remarksViewText_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"><c:out value="${task.userSpecificRemarks[userCount]}"/></span>
						</td>
					</tr>
					<tr>
					<td colspan="7" class="tableRow" align="right">
						<div id="insertRemarks_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" style="display:none">
							<input type="text"
								name="remarks_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"
								class="textField"
								onfocus="javascript:fullOpacity(this)"
								onblur="javascript:enableOpacityWithTextField(this)"
								value="<c:out value="${task.userSpecificRemarks[userCount]}"/>"
								onchange="javascript:insertRemarks('<c:out value="${task.id}"/>', '<c:out value="${task.userList[userCount][0]}"/>', this)"
								onkeydown="javascript:specialKeyFunctions('<c:out value="${task.id}"/>', '<c:out value="${task.userList[userCount][0]}"/>', this, event)"
								maxlength="255" size="100" />
						</div>
					</td>
				</tr>
				</c:forEach>
				<tr>
					<td colspan="7">&nbsp;</td>
				</tr>
			</c:forEach>



			<!-- Tasks without Timesheet -->
			<c:forEach items="${w.noTSTask}" var="task">
				<c:set var="totalMandaysEstimatedThisTask" value="${task.estimationMandays*task.totalAssignee}" />
				<c:set var="totalMandaysEstimatedEachUserThisTask" value="${task.estimationMandays}" />
				<c:set var="totalMandaysEstimatedThisProject" value="${totalMandaysEstimatedThisProject + totalMandaysEstimatedThisTask}" />
				<c:set var="totalMandaysSpentThisProject" value="${totalMandaysSpentThisProject + task.totalMandaysSpent}" />


				<tr>
					<td class="tableRow">
						<input type="checkbox"
							name="check_<c:out
							value="${task.id}"/>"
							checked
							onclick="javascript:unselectAllSubCheckboxes(this)" /></td>
					<!-- task name -->
					<td class="tableRow" valign="top" style="font-weight:bold">
						<c:out value="${task.title}"/>
					</td>
					<!-- estimated manday -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${totalMandaysEstimatedThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					</td>
					<!-- manday spent -->
					<!--
					<td class="tableRow" align="right">
						<fmt:formatNumber value="0" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />

					</td>
					-->
					<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					</td>



							<!-- variance -->
					<td class="tableRow" align="right">
					<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								</span>
							</c:if>						
					</td>
					<!-- balance -->
					<!--
					<td class="tableRow" align="right">
						<c:choose>
						<c:when test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent <= 0}">
							0.0
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
						</c:otherwise>
						</c:choose>
					</td>

					-->
					<!-- remarks -->
					<td class="tableRow">
						<a href="javascript:togglePanelVisibility('insertRemarks_<c:out value="${task.id}"/>', 'insertRemarksState_<c:out value="${task.id}"/>')" style="text-decoration:none;">
							<span id="insertRemarksState_<c:out value="${task.id}"/>"><img src="<c:out value='${pageContext.request.contextPath}'/>/ekms/images/ekp2005/ic_comment.gif" border="0" /></span>
						</a>
						<span id="remarksViewText_<c:out value="${task.id}"/>"><c:out value="${task.remarks}"/></span>
					</td>
				</tr>

				<tr>
					<td colspan="7" class="tableRow" align="right">
						<div id="insertRemarks_<c:out value="${task.id}"/>" style="display:none">
							<input type="text" name="remarks_<c:out value="${task.id}" />" class="textField"
								value="<c:out value="${task.remarks}"/>"
								onchange="javascript:insertRemarks('<c:out value="${task.id}"/>', '', this)"
								onkeydown="javascript:specialKeyFunctions('<c:out value="${task.id}"/>', '', this, event)"
								maxlength="255" size="100" />
						</div>
					</td>
				</tr>

		<c:forEach begin="0" end="${task.totalAssignee - 1}" var="userCount">
					<tr>
						<td class="tableRow opacified" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<input type="checkbox"
								name="check_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"
								id="check_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"
								onclick="javascript:altOpacityWithCheckBox(this)" />
						</td>
						<!-- assignee name -->
						<td class="tableRow opacified" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" style="padding-left:15px;" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:out value="${task.userList[userCount][1]}"/>
						</td>
						<!-- estimated manday -->
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>
						<!-- manday spent -->
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>


						<!-- variance -->
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								</span>
							</c:if>
						</td>
						<!-- balance -->
						<!--
						<td class="tableRow opacified" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:choose>
							<c:when test="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount] <= 0}">
								0.0
							</c:when>
							<c:otherwise>
								<fmt:formatNumber value="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
							</c:otherwise>
							</c:choose>
						</td>
						-->
						<!-- remarks -->
						<td class="tableRow opacified" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<a href="javascript:togglePanelVisibility('insertRemarks_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>', 'insertRemarksState_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>')" style="text-decoration:none;">
								<span id="insertRemarksState_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"><img src="<c:out value='${pageContext.request.contextPath}'/>/ekms/images/ekp2005/ic_comment.gif" border="0" /></span>
							</a>
							<span id="remarksViewText_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"><c:out value="${task.userSpecificRemarks[userCount]}"/></span>
						</td>
					</tr>
					<tr>


					<td colspan="7" class="tableRow" align="right">
						<div id="insertRemarks_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" style="display:none">
							<input type="text"
								name="remarks_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"
								class="textField"
								onfocus="javascript:fullOpacity(this)"
								onblur="javascript:enableOpacityWithTextField(this)"
								value="<c:out value="${task.userSpecificRemarks[userCount]}"/>"
								onchange="javascript:insertRemarks('<c:out value="${task.id}"/>', '<c:out value="${task.userList[userCount][0]}"/>', this)"
								onkeydown="javascript:specialKeyFunctions('<c:out value="${task.id}"/>', '<c:out value="${task.userList[userCount][0]}"/>', this, event)"
								maxlength="255" size="100" />
						</div>
					</td>
				</tr>
			</c:forEach>



				<tr>
					<td colspan="7">&nbsp;</td>
				</tr>


			</c:forEach>


			<!-- Total -->
			<tr>
				<td class="tableRow">&nbsp;</td>
				<td class="tableRow" style="font-weight:bold">
					<fmt:message key="timesheet.label.total"/>
				</td>
				<td class="tableRow" align="right" style="font-weight:bold">
				<fmt:formatNumber value="${totalMandaysEstimatedThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />					
				</td>
				<td class="tableRow" align="right" style="font-weight:bold">
					<fmt:formatNumber value="${totalMandaysSpentThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
				</td>
				<td class="tableRow" align="right" style="font-weight:bold">
					<c:if test="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject < 0}">
						<span style="color:#CC0000">
					</c:if>
					<fmt:formatNumber value="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					<c:if test="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject < 0}">
						</span>
					</c:if>
				</td>
				<!--
				<td class="tableRow" align="right" style="font-weight:bold">
					<c:choose>
						<c:when test="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject <= 0}">
							0.0
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
						</c:otherwise>
					</c:choose>
				</td>
				-->
				<td class="tableRow">&nbsp;</td>
			</tr>
		</table>
	</td>
</tr>
</form>

</table>
</div>