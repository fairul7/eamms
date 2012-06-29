<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/util.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/interface/TimeSheetModule.js'/>"></script>

<script>
var ie4 = false;
if(document.all) {
	ie4 = true;
}
function getObject(id) {
	if (ie4) {
		return document.all[id];
	}
	else {
		return document.getElementById(id);
	}
}

function change(obj){
var estimation=document.forms["<c:out value='${form.absoluteName}'/>"].elements['<c:out value='${form.estimation.absoluteName}'/>'];
if(obj.value=='Mandays')
estimation.value=estimation.value/8;
else if(obj.value=='Manhours')
estimation.value=estimation.value*1*8;
}
function getWorkingDays(due, start){
	var projectId='<c:out value="${form.projectId}"/>';
	TimeSheetModule.getWorkingDays(due, start, projectId,getDays);
	
}
function getDays(day){
var estimation=document.forms["<c:out value='${form.absoluteName}'/>"].elements['<c:out value='${form.estimation.absoluteName}'/>'];
	var estimationType=document.forms["<c:out value='${form.absoluteName}'/>"].elements['<c:out value='${form.estimationType.absoluteName}'/>'];
if(estimationType.value=='Mandays')
estimation.value=day;
else if(estimationType.value=='Manhours')
estimation.value=day*1*8;
}

function togglePanelVisibility(divId) {
    var divObj = getObject(divId);
    if(divObj.style.display == 'none') {
       divObj.style.display = 'block';       
    }
    else if(divObj.style.display == 'block') {
        divObj.style.display = 'none';       
    }
}
</script>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.milestone"/> *</td>
        <td class="calendarRow"><x:display name="${form.milestones.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.title"/> *</td>
        <td class="calendarRow"><x:display name="${form.title.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.ScheduleStartDate"/></td>
        <td class="calendarRow"><x:display name="${form.startDate.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.ScheduleStartTime"/></td>
        <td class="calendarRow"><x:display name="${form.startTime.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.ScheduleDueDate"/></td>
        <td class="calendarRow"><x:display name="${form.dueDate.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.ScheduleDueTime"/></td>
        <td class="calendarRow"><x:display name="${form.dueTime.absoluteName}" /></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="taskmanager.label.estimation"/></td>
        <td class="calendarRow"><x:display name="${form.estimation.absoluteName}" /> <x:display name="${form.estimationType.absoluteName}" /> &nbsp;<fmt:message key='taskmanager.label.perPerson'/></td>
    </tr>
    
<%--
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.category"/></td>
        <td class="calendarRow"><x:display name="${form.categories.absoluteName}"/></td>
    </tr>
--%>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.description"/> *</td>
        <td class="calendarRow"><x:display name="${form.description.absoluteName}"/></td>
    </tr>
    
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.assignedTo"/></td>
        <td class="calendarRow"><x:display name="${form.assignees.absoluteName}"/></td>
    </tr>
    
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.notifyMethod"/></td>
        <td class="calendarRow">
            <x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='project.label.memo'/>
            <x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='project.label.email'/>
        </td>
    </tr>
    </table>
    <table  cellpadding="0" cellspacing="1" class="forumBackground" width="100%">
    <Tr>
		<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
		</td>
 		<td class="calendarRow">
 		<a href="javascript:togglePanelVisibility('optional');">
		<fmt:message key='project.label.optionalField'/>
		</a>
 		</td>
	</Tr>
	<Tr>
		<td class="calendarRowLabel" colspan="2" valign="top">
		<div id="optional" style="display:none">
	<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%"> 
	<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.reminder"/></td>
        <td class="calendarRow">
            <x:display name="${form.reminderRadio.absoluteName}" />
            <x:display name="${form.reminderDate.absoluteName}" />
            <x:display name="${form.reminderTime.absoluteName}" />
            <%--<x:display name="${form.reminderQuantity.absoluteName}" />
            <x:display name="${form.reminderModifier.absoluteName}"/> <fmt:message key="project.label.before"/>--%>
        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="taskmanager.label.AllowassigneestoreassignthetaskBR"/> </td>
        <td class="calendarRow">
            <x:display name="${form.reassignNo.absoluteName}" />
            <x:display name="${form.reassignYes.absoluteName}" />
        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.attachedFiles"/></td>
        <td class="calendarRow" align="top">
            <x:display name="${form.fileListing.absoluteName}"/><br>
            <x:display name="${form.attachFilesButton.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.resources"/></td>
        <td class="calendarRow"><x:display name="${form.resources.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.notifyNote"/></td>
        <td class="calendarRow"><x:display name="${form.notifyNote.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key="project.label.classification"/></td>
        <td class="calendarRow">
            <x:display name="${form.radioPublic.absoluteName}"/><fmt:message key="project.label.public"/>
            <x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key="project.label.private"/>
        </td>
    </tr>
    </table></div>
	</td></Tr>
	</table>
    <table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <tr>
        <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">&nbsp;</td>
        <td class="calendarRow"><x:display name="${form.submitButton.absoluteName}"/><x:display name="${form.deleteButton.absoluteName}"/><x:display name="${form.cancelButton.absoluteName}"/></td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>