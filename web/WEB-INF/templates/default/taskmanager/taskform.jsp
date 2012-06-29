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
	var projectId='<c:out value="${form.taskProjectId}"/>';
	if(projectId=='')
	{
	var one_day=1000*60*60*24;   
	var estimation=document.forms["<c:out value='${form.absoluteName}'/>"].elements['<c:out value='${form.estimation.absoluteName}'/>'];
	var estimationType=document.forms["<c:out value='${form.absoluteName}'/>"].elements['<c:out value='${form.estimationType.absoluteName}'/>'];
	var text=Math.ceil(((due-start))/one_day);
	if(estimationType.value=='Mandays')
    estimation.value=text*1+1;
    else if(estimationType.value=='Manhours')
    estimation.value=(text*1+1)*8;
    }
	else{
	TimeSheetModule.getWorkingDays(due, start, projectId,getDays);
	}	
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

<table  cellpadding="4" cellspacing="1" class="classBackground" width="100%">

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Title'/>&nbsp;&nbsp;<FONT class="classRowLabel">*</FONT></td>
 <td class="classRow">   <x:display name="${form.title.absoluteName}" ></x:display>
 </td>
</tr>


<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.ScheduleStartDate'/></td>
 <td class="classRow">   <x:display name="${form.startDate.absoluteName}" ></x:display>
 </td>
</tr>

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.ScheduleStartTime'/></td>
 <td class="classRow">  <x:display name="${form.startTime.absoluteName}" />
 </td>
</tr>

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.ScheduleDueDate'/></td>
 <td class="classRow">   <x:display name="${form.dueDate.absoluteName}" ></x:display>
 </td>
</tr>

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.ScheduleDueTime'/></td>
 <td class="classRow">  <x:display name="${form.dueTime.absoluteName}" />
 </td>
</tr>

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.estimation'/></td>
 <td class="classRow">  <x:display name="${form.estimation.absoluteName}" /> <x:display name="${form.estimationType.absoluteName}" /> &nbsp;<fmt:message key='taskmanager.label.perPerson'/>
 </td>
</tr>


<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Category'/></td>
 <td class="classRow">  <x:display name="${form.categories.absoluteName}" />
 </td>
</tr>

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Description'/>&nbsp;&nbsp;<FONT class="classRowLabel">*</FONT></td>
 <td class="classRow">  <x:display name="${form.description.absoluteName}"/>
 </td>
</tr>

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.AssignTo'/></td>
 <td class="classRow">  <x:display name="${form.assignees.absoluteName}" />
 </td>
</tr>
<Tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.NotifyMethod'/></td>
 <td class="classRow">
        <x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='taskmanager.label.Memo'/><x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='taskmanager.label.Email'/></td>
</tr>
</table>

<table  cellpadding="0" cellspacing="1" class="forumBackground" width="100%">
<Tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
</td>
 <td class="classRow">
 <a href="javascript:togglePanelVisibility('optional');">
<fmt:message key='calendar.label.optionalField'/>
</a>
 </td>
</Tr>
<Tr>
<td class="classRowLabel" colspan="2" valign="top">
<div id="optional" style="display:none">
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%"> 

<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Reminder'/></td>
 <td class="classRow">
    <x:display name="${form.reminderRadio.absoluteName}" />
    <x:display name="${form.reminderDate.absoluteName}" />
    <x:display name="${form.reminderTime.absoluteName}" />
    <%--<x:display name="${form.reminderQuantity.absoluteName}" />
    <x:display name="${form.reminderModifier.absoluteName}"/><fmt:message key='taskmanager.label.before'/>--%>
</td>
</tr>
<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.priority'/></td>
 <td class="classRow">  <x:display name="${form.taskPriority.absoluteName}"/>
 </td>
</tr>
<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"> <fmt:message key="taskmanager.label.Allowassigneestoreassignthetask"/>
 </td>
 <td class="classRow">  <x:display name="${form.reassignNo.absoluteName}" /> <x:display name="${form.reassignYes.absoluteName}" />
 </td>
</tr>
<Tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Resources'/></td>
 <td class="classRow">  <x:display name="${form.resources.absoluteName}" />
 </td>
</tr>

<Tr>
   <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.AttachedFiles'/></td>
   <td class="classRow" align="top">
   <x:display name="${form.uploadForm.filesListing.absoluteName}" ></x:display>  <br>
   <x:display name="${form.attachFilesButton.absoluteName}" ></x:display>
   </td>
</tr>
<Tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.NotifyNote'/></td>
 <td class="classRow">    <x:display name="${form.notifyNote.absoluteName}"/>
 </td>
</tr>
<Tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='taskmanager.label.Classification'/></td>
 <td class="classRow">    <x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='taskmanager.label.Public'/><x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='taskmanager.label.Private'/><%--
        <x:display name="${form.radioConfidential.absoluteName}"/>Confidential
--%>
 </td>
</tr>

</table></div>
</td></Tr>
</table>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<Tr>
 <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
 </td>
<td class="classRow"><x:display name="${form.submitButton.absoluteName}"/><x:display name="${form.cancelButton.absoluteName}"/> </td>
 </tr>
    </table>
  <jsp:include page="../form_footer.jsp" flush="true"/>
