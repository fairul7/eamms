<%@ page import="java.util.Calendar,kacang.runtime.*"%>
<%@ include file="/common/header.jsp" %>
<jsp:include page="../form_header.jsp" flush="true"/>
<c:set var="w" value="${widget}" />
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/util.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/interface/TimeSheetModule.js'/>"></script>

<script type="text/javascript">
// Check if the browser is IE4
var ie4 = false;
if(document.all) {
	ie4 = true;
}

function numberonly(e) {
  var key;
  var keychar;

  if (window.event)
    key = window.event.keyCode;
  else if (e)
    key = e.which;
  else
    return true;

  keychar = String.fromCharCode(key);
  keychar = keychar.toLowerCase();

  // control keys
  if ((key==null) || (key==0) || (key==8) ||
      (key==9) || (key==13) || (key==27) )
    return true;

  // alphas and numbers
  else if ((("0123456789").indexOf(keychar) > -1))
    return true;
  else
    return false;
}

function floatonly(obj,e)  {
var index=obj.value.indexOf('.');

var key;
 var keychar;
 if (window.event) 
 {  
 key = window.event.keyCode;
 }
  else if (e){
  key = e.which;
  }
  else
  return true;
  keychar = String.fromCharCode(key);

	if(index>-1 && (key==46)){
	return false;
	}
   if ((key==null) || (key==0) || (key==8) || /**/ (key==9) || (key==13) || (key==27) )
    return true;
    if (((key==46)))  
    {
     
     return true;
    }
    else if ((("0123456789").indexOf(keychar) > -1))  return true;
     else  return false; }
     
// Get an object by ID
function getObject(id) {
	if (ie4) {
		return document.all[id];
	}
	else {
		return document.getElementById(id);
	}
}

function round_decimals(original_number, decimals) {
    var result1 = original_number * Math.pow(10, decimals)
    var result2 = Math.round(result1)
    var result3 = result2 / Math.pow(10, decimals)
    return pad_with_zeros(result3, decimals)
}


function pad_with_zeros(rounded_value, decimal_places) {

    // Convert the number to a string
    var value_string = rounded_value.toString()
    
    // Locate the decimal point
    var decimal_location = value_string.indexOf(".")

    // Is there a decimal point?
    if (decimal_location == -1) {
        
        // If no, then all decimal places will be padded with 0s
        decimal_part_length = 0
        
        // If decimal_places is greater than zero, tack on a decimal point
        value_string += decimal_places > 0 ? "." : ""
    }
    else {

        // If yes, then only the extra decimal places will be padded with 0s
        decimal_part_length = value_string.length - decimal_location - 1
    }
    
    // Calculate the number of decimal places that need to be padded with 0s
    var pad_total = decimal_places - decimal_part_length
    
    if (pad_total > 0) {
        
        // Pad the string with 0s
        for (var counter = 1; counter <= pad_total; counter++) 
            value_string += "0"
        }
    return value_string
}

function insertCost(spanId, divId, varianId, oppId, type, obj) {
var newCost = obj.value;
var spanObj = getObject(spanId);
var spanHiddenObj = getObject(spanId+'_Hidden');
var divObj = getObject(divId);
var oppObj = getObject(oppId);
var varianObj = getObject(varianId);
var varianHiddebObj= getObject(varianId+'_Hidden');
var text;
var text2;
var tot;
var totact;
var totvarian;
var obj;
if(newCost=='')
{
newCost='0';
obj.value='0';
}
newCost=round_decimals(newCost,1);
obj.value=newCost;
if (ie4) {
text=spanObj.innerText;
	}
else {
text=spanObj.textContent;
}

if(type=='1')
{
var varian=oppObj.value-newCost;
varian=round_decimals(varian,1);
if (ie4) {
tot=total_Estimated.innerText;
	}
else {
tot=total_Estimated.textContent;
}
tot=round_decimals(tot,1);
totact=tot*1+newCost*1-text*1;
totact=round_decimals(totact,1);
total_Estimated.innerHTML=totact;
obj=getObject('total_Estimated_Hidden');
obj.value=totact;
}
else if(type=='2'){
if (ie4) {
tot=total_Actual.innerText;
	}
else {
tot=total_Actual.textContent;
}
tot=round_decimals(tot,1);
var varian=newCost-oppObj.value;
varian=round_decimals(varian,1);
totact=tot*1+newCost*1-text*1;
totact=round_decimals(totact,1);
total_Actual.innerHTML=totact;
obj=getObject('total_Actual_Hidden');
obj.value=totact;
}
if (ie4) {
text2=varianObj.innerText;
totvarian=total_variance.innerText;
	}
else {
text2=varianObj.textContent;
totvarian=total_variance.textContent;
}
totvarian=round_decimals(totvarian,1);
var totall=totvarian*1+varian*1-text2*1;
totall=round_decimals(totall,1);
if(totall>0){
total_variance.innerHTML = "<font color=#ff0000>"+totall+"</font>";
}
else if(totall<0){
total_variance.innerHTML = "<font color=#347235>"+totall+"</font>";
}
else{
total_variance.innerHTML = totall;
}
obj=getObject('total_variance_Hidden');
obj.value=totall;
spanObj.innerHTML = newCost;
spanHiddenObj.value = newCost;
if(varian>0){
varianObj.innerHTML = "<font color=#ff0000>"+varian+"</font>";
}
else if(varian<0){
varianObj.innerHTML = "<font color=#347235>"+varian+"</font>";
}
else{
varianObj.innerHTML = varian;
}
varianHiddebObj.value= varian;
divObj.style.display = "none";
}

// When Tab is pressed, move mouse cursor to the next +/- sign
// When Enter is pressed, save the remarks. This is a workaround for IE, as it doesn't treat Enter with onChange event
function specialKeyFunctions(spanId, divId, varianId, oppId, type, obj, evt)  {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if(charCode == 9) {
	}
	if(charCode == 13) {
		insertCost(spanId, divId, varianId, oppId, type, obj);
	}
}

function insertDefects(spanId, divId, defectname, type, obj) {
var newDefects = obj.value;
var spanObj = getObject(spanId);
var spanHiddenObj = getObject(spanId+'_Hidden');
var divObj = getObject(divId);
var defectObj = getObject('total_'+defectname);
var defectHiddenObj = getObject('total_'+defectname+"_Hidden");
var text;
var text2;
var tot;
var totTotal;
var totDefects;
var obj;
if(newDefects=='')
{
newDefects='0';
obj.value='0';
}
text=spanObj.innerHTML;
if(type=='1')
{
tot=resolved_Total.innerHTML;
resolved_Total.innerHTML=tot*1+newDefects*1-text*1;
text2=resolved_Total.innerHTML;
totTotal=total_Total.innerHTML;
total_Total.innerHTML=totTotal*1+text2*1-tot*1;
obj=getObject('resolved_Total_Hidden');
obj.value=tot*1+newDefects*1-text*1;
obj=getObject('total_Total_Hidden');
obj.value=totTotal*1+text2*1-tot*1;
}
else if(type=='2'){
tot=unresolved_Total.innerHTML;
unresolved_Total.innerHTML=tot*1+newDefects*1-text*1;
text2=unresolved_Total.innerHTML;
totTotal=total_Total.innerHTML;
total_Total.innerHTML=totTotal*1+text2*1-tot*1;
obj=getObject('unresolved_Total_Hidden');
obj.value=tot*1+newDefects*1-text*1;
obj=getObject('total_Total_Hidden');
obj.value=totTotal*1+text2*1-tot*1;
}
totDefects=defectObj.innerHTML;
defectObj.innerHTML=totDefects*1+newDefects*1-text*1;
defectHiddenObj.value=totDefects*1+newDefects*1-text*1
spanObj.innerHTML = newDefects;
spanHiddenObj.value = newDefects;
divObj.style.display = "none";
}

// When Tab is pressed, move mouse cursor to the next +/- sign
// When Enter is pressed, save the remarks. This is a workaround for IE, as it doesn't treat Enter with onChange event
function specialKeyFunctionsDefects(spanId, divId, defectName, type, obj, evt)  {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if(charCode == 9) {
	
	}
	if(charCode == 13) {
		insertDefects(spanId, divId, defectName, type, obj);
	}
}

// Find a keyword in string, and replace with another
function findReplace(oriString, find, replace) {
	var regEx = new RegExp(find, 'gi');
	return oriString.replace(regEx, replace);
}

// Toggle the visibility of a specific DIV
function togglePanelVisibility(divId) {
    var divObj = getObject(divId);
    if(divObj.style.display == 'none') {
       divObj.style.display = 'block';       
    }
    else if(divObj.style.display == 'block') {
        divObj.style.display = 'none';       
    }
}

function fieldfocus(objId, divid){

var divObj = getObject(divid);

if(divObj.style.display == 'none') {
   
    }
    else if(divObj.style.display == 'block') {    
    var obj = getObject(objId);
       obj.focus();    
    }

}
</script>
 
<table width="100%" cellpadding="5" cellspacing="0">
<tr class="contentBgColor">
    <td>
    	<strong><fmt:message key="project.label.projectPerformanceMetricReportToday"/></strong>
    </td>
    <td align="right">
    </td>
</tr>

<tr>
    <td class="contentBgColor" colspan="2">

		    <%
		        pageContext.setAttribute("today",Calendar.getInstance().getTime());
		    %>
	    	<fmt:message key="project.label.monthyear"/> : <x:display name="${w.childMap.date.absoluteName}"/><x:display name="${w.childMap.submit.absoluteName}"/>
    </td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2"><fmt:message key="project.label.reportName"/> *: <x:display name="${w.childMap.reportName.absoluteName}"/></td>
</tr>

<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2"><x:display name="${w.childMap.summary.absoluteName}"/></td>
</tr>

<tr class="contentBgColor">
	<td colspan="2">
	<DIV style="display:block" id="summaryA">
		<table width="100%" cellpadding="2" cellspacing="1">
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectName"/>	    
    </td>
    <td class="tableRow" valign="top">
    <c:out value="${w.project.projectName}"/>
    </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.Client"/>	    
    </td>
    <td class="tableRow" valign="top">
    <c:out value="${w.project.clientName}"/></td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectValue"/>	      
    </td>
    <td class="tableRow" valign="top"><c:out value="${w.project.projectCurrencyValue}"/> </td>
</tr>
<c:forEach items="${w.project.roles}" var="roles" varStatus="rolesIndex">
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <c:out value="${roles.roleName}"/>    
    </td>
    <td class="tableRow" valign="top">
    <c:out value="${roles.user}"/></td>
</tr>
</c:forEach>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectSummary"/>	      
    </td>
    <td class="tableRow" valign="top"><c:out value="${w.project.projectSummary}"/> </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectCurrentStatus"/>	      
    </td>
    <td class="tableRow" valign="top"><c:out value="${w.project.projectStatus}"/>	      
    </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectCurrentHighlights"/>	      
    </td>
    <td class="tableRow" valign="top">
    <x:display name="${w.childMap.currentHighlights.absoluteName}"/></td>
</tr>
</table>
<table width="100%" cellpadding="2" cellspacing="1">
<tr>
	<td class="contentBgColor" colspan="3">&nbsp;</td>
</tr>
<tr>
	<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"></td>
	
	
	
	<td bgcolor="#003366" class="contentTitleFont"  align="center" valign="top" style="font-weight:bold">
	<fmt:message key="project.label.schedule"/>
	</td>
	<td bgcolor="#003366" class="contentTitleFont"  align="center" valign="top" style="font-weight:bold">
	 <fmt:message key="project.label.actual"/>	
	</td>
	<td bgcolor="#003366" class="contentTitleFont" align="center" width="20%" valign="top" style="font-weight:bold">
	<fmt:message key="project.label.varianDays"/>	
	</td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    
    <fmt:message key="project.label.projectStartDate"/>	      
    </td>
    <td class="tableRow" align="right" valign="top">
    <fmt:formatDate value="${w.project.projectStartDate}" pattern="ddMMMyyyy"/>
    </td>
    <td class="tableRow" align="right" valign="top">
    <c:out value="${w.project.actualStartDate}"/>   
    </td>   
    <td class="tableRow" align="right" valign="top">
    <c:choose>
  <c:when test="${w.project.startVarians == 'Not Started'}">
  <c:out value="${w.project.startVarians}"/>
  </c:when>
  <c:otherwise>
	<c:if test="${w.project.startVarians >0.0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${w.project.startVarians <0.0}">
        <font color="#347235">
    </c:if>
    <c:out value="${w.project.startVarians}"/>
	<c:if test="${w.project.startVarians >0.0 || w.project.startVarians <0.0}">
        </font>
    </c:if>
  </c:otherwise>
	</c:choose>
    </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    
    <fmt:message key="project.label.projectEndDate"/>	      
    </td>
    <td class="tableRow" align="right" valign="top"><fmt:formatDate value="${w.project.projectEndDate}" pattern="ddMMMyyyy"/></td>
    <td class="tableRow" align="right" valign="top"><c:out value="${w.project.actualEndDate}"/></td>   
    <td class="tableRow" align="right" valign="top">  
    <c:choose>
  <c:when test="${w.project.endVarians == 'Not Started'}">
  <c:out value="${w.project.endVarians}"/>
  </c:when>
  <c:otherwise>
  	<c:if test="${w.project.endVarians >0.0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${w.project.endVarians <0.0}">
        <font color="#347235">
    </c:if>
    <c:if test="${w.project.actualEndDate == 'Ongoing'}">
    *
    </c:if>
    <c:out value="${w.project.endVarians}"/>
    <c:if test="${w.project.endVarians >0.0 || w.project.endVarians <0.0}">
        </font>
    </c:if>
  </c:otherwise>
	</c:choose>    
    </td>   
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    	
    	<fmt:message key="project.label.scheduleDuration"/>          
    </td>
    <td class="tableRow" align="right" valign="top">
    <c:if test="${w.project.estDuration >0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${w.project.estDuration <0}">
        <font color="#347235">
    </c:if>
    <c:out value="${w.project.estDuration}"/>
    <c:if test="${w.project.estDuration >0||w.project.estDuration <0}">
    </font>
    </c:if>
    </td>
    <td class="tableRow" align="right" valign="top">
    <c:choose>
  <c:when test="${w.project.actDuration == 'Not Started'}">
  <c:out value="${w.project.actDuration}"/>
  </c:when>
  <c:otherwise>
    <c:if test="${w.project.actDuration >0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${w.project.actDuration <0}">
        <font color="#347235">
    </c:if>
    <c:if test="${w.project.actualEndDate == 'Ongoing'}">
    *</c:if>    
    <c:out value="${w.project.actDuration}"/>
	<c:if test="${w.project.actDuration >0||w.project.actDuration <0}">
	</font>
	</c:if>
    </c:otherwise>
    </c:choose> 
    
    
    </td>
    <td class="tableRow" align="right" valign="top" style="font-weight:bold">	
    </td>
</tr>
</table>
</DIV>
</td></tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2"><x:display name="${w.childMap.schedule.absoluteName}"/></td>
</tr>
<tr class="contentBgColor">
	<td colspan="2">
	<DIV style="display:block" id="scheduleB">
		<table width="100%" cellpadding="2" cellspacing="1">
		<tr>
				<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"><fmt:message key="project.label.milestone"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estStart"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estEnd" /></td>
				<td bgcolor="#003366" width="8%" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estDuration" /></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actStart" /></td>				
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actEnd" /></td>
				<td bgcolor="#003366" width="8%" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actDuration" /></td>
				<td bgcolor="#003366" width="8%" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.varianceDays" /></td>
			</tr>
<c:forEach items="${w.project.milestones}" var="milestone">
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <c:out value="${milestone.milestoneName}"/>    
    </td>
    <td class="tableRow" align="right" valign="top"><fmt:formatDate value="${milestone.startDate}" pattern="ddMMMyyyy"/>	      
    </td>
    <td class="tableRow" align="right"  valign="top"><fmt:formatDate value="${milestone.endDate}" pattern="ddMMMyyyy"/>   
    </td>
    <td class="tableRow" align="right"  valign="top"><c:out value="${milestone.duration}"/>    
    </td>
    <td class="tableRow" align="right"  valign="top"><c:out value="${milestone.actualStartDate}"/> 
    </td>    
    <td class="tableRow" align="right"  valign="top"><c:out value="${milestone.actualEndDate}"/>
    </td>
    <td class="tableRow" align="right"  valign="top"><c:out value="${milestone.actualDuration}"/>
    </td>
    <td class="tableRow" align="right" valign="top">
    <c:choose>
  <c:when test="${milestone.variance == 'Not Started'}">
  <c:out value="${milestone.variance}"/>
  </c:when>
  <c:otherwise>
    <c:if test="${milestone.variance > 0.0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${milestone.variance <0.0}">
        <font color="#347235">
    </c:if>
    <c:if test="${milestone.actualEndDate == 'Ongoing'}">
    *
    </c:if>
    <c:out value="${milestone.variance}"/>  
    <c:if test="${milestone.variance > 0.0 || milestone.variance <0.0}">
        </font>
    </c:if>  
    </c:otherwise>
    </c:choose> 
    </td>
</tr>
</c:forEach>
</table>
</DIV>
</td></tr>

<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2"><x:display name="${w.childMap.effort.absoluteName}"/></td>
</tr>
<c:set var="totalMandaysEstimatedThisProject" value="0" />
<c:set var="totalMandaysSpentThisProject" value="0" />


<tr class="contentBgColor">
	<td colspan="2">
	<DIV style="display:block" id="effortC">
		<table width="100%" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"><fmt:message key="project.label.activity"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estimatedMandays"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actualMandays" /></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.variance" /></td>
			</tr>


			<!-- Tasks with Timesheet -->
			<c:forEach items="${w.task}" var="task">
				<c:set var="totalMandaysEstimatedThisTask" value="${task.estimationMandays*task.totalAssignee}" />
				<c:set var="var" value="${task.totalMandaysSpent-totalMandaysEstimatedThisTask}" />
				<tr>					
					<!-- Tasks with Timesheet -->
					<td class="tableRow" valign="top" style="font-weight:bold">
						<c:out value="${task.title}"/>
					</td>
					<!-- estimated manday -->
					<td class="tableRow" align="right">
					<fmt:formatNumber value="${totalMandaysEstimatedThisTask}" maxFractionDigits="2" pattern="#0.00" />
					</td>
					<!-- manday spent -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${task.totalMandaysSpent}" maxFractionDigits="2" pattern="#0.00" />

					</td>
					<!-- variance -->
					<td class="tableRow" align="right">
					<c:if test="${var > 0.00}">
                        <font color="#ff0000">
                    </c:if>
                    <c:if test="${var <0.00}">
        				<font color="#347235">
    				</c:if>
						<fmt:formatNumber value="${var}" maxFractionDigits="2" pattern="#0.00" />
					<c:if test="${var > 0.00 || var<0.00}">
                        </font>
                    </c:if>
					</td>

				</tr>

			</c:forEach>
			<c:set var="totvar" value="${w.hourSpent-w.estimatedHourSpent}" />
				<tr>					
					<!-- Tasks with Timesheet -->
					<td class="tableRow" valign="top" style="font-weight:bold">
						<fmt:message key="project.label.total"/>
					</td>
					<!-- estimated manday -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${w.estimatedHourSpent}" maxFractionDigits="2" pattern="#0.00" />
					</td>
					<!-- manday spent -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${w.hourSpent}" maxFractionDigits="2" pattern="#0.00" />
					</td>
					<!-- variance -->
					<td class="tableRow" align="right">
					<c:if test="${totvar > 0.00}">
                        <font color="#ff0000">
                    </c:if>
                    <c:if test="${totvar < 0.00}">
        				<font color="#347235">
    				</c:if>
						<fmt:formatNumber value="${totvar}" maxFractionDigits="2" pattern="#0.00" />
					<c:if test="${totvar > 0.00 || totvar<0.00}">
                        </font>
                    </c:if>	
					</td>
				</tr>
		</table>
		</DIV>
	</td>
</tr>

<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2"><x:display name="${w.childMap.defect.absoluteName}"/></td>
</tr>

<tr class="contentBgColor">
	<td colspan="2">
	<DIV style="display:block" id="defectD">
		<table width="100%" cellpadding="2" cellspacing="1">
		<c:choose>
  		<c:when test="${w.defectList}">
<tr>
			<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"></td>
			<c:forEach items="${w.defectsList}" var="defects">
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold">
			<c:out value="${defects.defectTypeName}"/></td>
			</c:forEach>
			
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.resolved"/></td>
			<c:forEach items="${w.defectsList}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<c:if test="${defects.defectTypeName=='Total'}">
			<span id="resolved_Total">
			<c:out value="${defects.resolved}"/>		
			</span>
			<input type="hidden" name="resolved_Total_Hidden" id="resolved_Total_Hidden" value="<c:out value="${defects.resolved}"/>"/>
			</c:if>
			<c:if test="${defects.defectTypeName!='Total'}">
			
			<a href="javascript:togglePanelVisibility('resolved_<c:out value="${defects.defectTypeName}"/>');fieldfocus('resolved_<c:out value="${defects.defectTypeName}"/>_TextField','resolved_<c:out value="${defects.defectTypeName}"/>');" style="text-decoration:none;">
			<span id="insert_resolved_<c:out value="${defects.defectTypeName}"/>">
			<c:out value="${defects.resolved}"/>
			</span>
			
			<input type="hidden" name="insert_resolved_<c:out value="${defects.defectTypeName}"/>_Hidden" id="insert_resolved_<c:out value="${defects.defectTypeName}"/>_Hidden" value="<c:out value="${defects.resolved}"/>"/>
			</a><br>
			<DIV style="display:none" id="resolved_<c:out value="${defects.defectTypeName}"/>">
			<input type="text" name="resolved_<c:out value="${defects.defectTypeName}"/>_TextField" id="resolved_<c:out value="${defects.defectTypeName}"/>_TextField" size="10" class="textField" 
			onchange="javascript:insertDefects('insert_resolved_<c:out value="${defects.defectTypeName}"/>','resolved_<c:out value="${defects.defectTypeName}"/>','<c:out value="${defects.defectTypeName}"/>', '1',this)"
			onkeydown="javascript:specialKeyFunctionsDefects('insert_resolved_<c:out value="${defects.defectTypeName}"/>','resolved_<c:out value="${defects.defectTypeName}"/>','<c:out value="${defects.defectTypeName}"/>','1',this, event)"
			onkeypress="return numberonly(event)"
			value="<c:out value="${defects.resolved}"/>"/>
			</DIV>
			</c:if>
			</td>
			</c:forEach>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.unresolved"/></td>
			<c:forEach items="${w.defectsList}" var="defects">
			<c:if test="${defects.defectTypeName=='Total'}">
			<td class="tableRow" align="right" valign="top">
			<span id="unresolved_Total">
			<c:out value="${defects.unresolved}"/>				
			</span>
			<input type="hidden" name="unresolved_Total_Hidden" id="unresolved_Total_Hidden" value="<c:out value="${defects.unresolved}"/>"/>
			</td>
			</c:if>
			<c:if test="${defects.defectTypeName!='Total'}">
			<td class="tableRow" align="right"  valign="top">
			<a href="javascript:togglePanelVisibility('unresolved_<c:out value="${defects.defectTypeName}"/>');fieldfocus('unresolved_<c:out value="${defects.defectTypeName}"/>_TextField','unresolved_<c:out value="${defects.defectTypeName}"/>');" style="text-decoration:none;">
			<span id="insert_unresolved_<c:out value="${defects.defectTypeName}"/>">
			<c:out value="${defects.unresolved}"/>		
			</span>
			<input type="hidden" name="insert_unresolved_<c:out value="${defects.defectTypeName}"/>_Hidden" id="insert_unresolved_<c:out value="${defects.defectTypeName}"/>_Hidden" value="<c:out value="${defects.unresolved}"/>"/>
			</a><br>
			<DIV style="display:none" id="unresolved_<c:out value="${defects.defectTypeName}"/>">
			<input type="text" name="unresolved_<c:out value="${defects.defectTypeName}"/>_TextField" id="unresolved_<c:out value="${defects.defectTypeName}"/>_TextField" size="10" class="textField" 
			onchange="javascript:insertDefects('insert_unresolved_<c:out value="${defects.defectTypeName}"/>','unresolved_<c:out value="${defects.defectTypeName}"/>','<c:out value="${defects.defectTypeName}"/>', '2',this)"
			onkeydown="javascript:specialKeyFunctionsDefects('insert_unresolved_<c:out value="${defects.defectTypeName}"/>','unresolved_<c:out value="${defects.defectTypeName}"/>','<c:out value="${defects.defectTypeName}"/>','2',this, event)"
			onkeypress="return numberonly(event)"
			value="<c:out value="${defects.unresolved}"/>"/>
			</DIV>
			</td>
			</c:if>
			</c:forEach>
			
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.total"/></td>
			<c:forEach items="${w.defectsList}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<c:if test="${defects.defectTypeName=='Total'}">
			<span id="total_Total">
			<c:out value="${defects.total}"/>		
			</span>
			<input type="hidden" name="total_Total_Hidden" id="total_Total_Hidden" value="<c:out value="${defects.total}"/>"/>
			</c:if>
			<c:if test="${defects.defectTypeName!='Total'}">
			<span id="total_<c:out value="${defects.defectTypeName}"/>">
			<c:out value="${defects.total}"/>				
			</span>
			<input type="hidden" name="total_<c:out value="${defects.defectTypeName}"/>_Hidden" id="total_<c:out value="${defects.defectTypeName}"/>_Hidden" value="<c:out value="${defects.total}"/>"/>
			</c:if>
			</td>
			</c:forEach>
		</tr>
		</c:when>
		 <c:otherwise>
		<tr>
			<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"></td>
			<c:forEach items="${w.defects}" var="defects">
			<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold">
			<c:out value="${defects.defects_Name}"/></td>
			</c:forEach>
			<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"><fmt:message key="project.label.total" /></td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.resolved"/></td>
			<c:forEach items="${w.defects}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('resolved_<c:out value="${defects.defects_Name}"/>');fieldfocus('resolved_<c:out value="${defects.defects_Name}"/>_TextField','resolved_<c:out value="${defects.defects_Name}"/>');" style="text-decoration:none;">
			<span id="insert_resolved_<c:out value="${defects.defects_Name}"/>">
			0
			</span>
			<input type="hidden" name="insert_resolved_<c:out value="${defects.defects_Name}"/>_Hidden" id="insert_resolved_<c:out value="${defects.defects_Name}"/>_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="resolved_<c:out value="${defects.defects_Name}"/>">
			<input type="text" name="resolved_<c:out value="${defects.defects_Name}"/>_TextField" id="resolved_<c:out value="${defects.defects_Name}"/>_TextField" size="10" class="textField" 
			onchange="javascript:insertDefects('insert_resolved_<c:out value="${defects.defects_Name}"/>','resolved_<c:out value="${defects.defects_Name}"/>','<c:out value="${defects.defects_Name}"/>', '1',this)"
			onkeydown="javascript:specialKeyFunctionsDefects('insert_resolved_<c:out value="${defects.defects_Name}"/>','resolved_<c:out value="${defects.defects_Name}"/>','<c:out value="${defects.defects_Name}"/>','1',this, event)"
			onkeypress="return numberonly(event)"
			value=""/>
			</DIV>
			</td>
			</c:forEach>
			<td class="tableRow" align="right" valign="top">
			<span id="resolved_Total">
			0			
			</span>
			<input type="hidden" name="resolved_Total_Hidden" id="resolved_Total_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.unresolved"/></td>
			<c:forEach items="${w.defects}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('unresolved_<c:out value="${defects.defects_Name}"/>');fieldfocus('unresolved_<c:out value="${defects.defects_Name}"/>_TextField','unresolved_<c:out value="${defects.defects_Name}"/>');" style="text-decoration:none;">
			<span id="insert_unresolved_<c:out value="${defects.defects_Name}"/>">
			0
			</span>
			<input type="hidden" name="insert_unresolved_<c:out value="${defects.defects_Name}"/>_Hidden" id="insert_unresolved_<c:out value="${defects.defects_Name}"/>_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="unresolved_<c:out value="${defects.defects_Name}"/>">
			<input type="text" name="unresolved_<c:out value="${defects.defects_Name}"/>_TextField" id="unresolved_<c:out value="${defects.defects_Name}"/>_TextField" size="10" class="textField" 
			onchange="javascript:insertDefects('insert_unresolved_<c:out value="${defects.defects_Name}"/>','unresolved_<c:out value="${defects.defects_Name}"/>','<c:out value="${defects.defects_Name}"/>', '2',this)"
			onkeydown="javascript:specialKeyFunctionsDefects('insert_unresolved_<c:out value="${defects.defects_Name}"/>','unresolved_<c:out value="${defects.defects_Name}"/>','<c:out value="${defects.defects_Name}"/>','2',this, event)"
			onkeypress="return numberonly(event)"
			value=""/>
			</DIV>
			</td>
			</c:forEach>
			<td class="tableRow" align="right" valign="top">
			<span id="unresolved_Total">
			0			
			</span>
			<input type="hidden" name="unresolved_Total_Hidden" id="unresolved_Total_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.total"/></td>
			<c:forEach items="${w.defects}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<span id="total_<c:out value="${defects.defects_Name}"/>">
			0			
			</span>
			<input type="hidden" name="total_<c:out value="${defects.defects_Name}"/>_Hidden" id="total_<c:out value="${defects.defects_Name}"/>_Hidden" value="0"/>
			</td>
			</c:forEach>
			<td class="tableRow" align="right" valign="top">
			<span id="total_Total">
			0			
			</span>
			<input type="hidden" name="total_Total_Hidden" id="total_Total_Hidden" value="0"/>
			</td>
		</tr>
  	</c:otherwise>
	</c:choose>
		</table>
		</DIV>
	</td>
</tr>

<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2"><x:display name="${w.childMap.cost.absoluteName}"/></td>
</tr>

<tr class="contentBgColor">
	<td colspan="2">
	<DIV style="display:block" id="costE">
		<table width="100%" cellpadding="2" cellspacing="1">
		<tr>
			<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"></td>
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estimated"/></td>
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actualToDate" /></td>
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.variance" /></td>
		</tr>
		<c:choose>
  		<c:when test="${w.costList}">
  		<c:forEach items="${w.costs}" var="costs">
  		<tr>
  		<c:if test="${costs.costName=='Total'}">
  		<td class="tableRow" valign="top" style="font-weight:bold"><c:out value="${costs.costName}"/></td>
			<td class="tableRow" align="right"  valign="top">
			<span id="total_Estimated">			
			<c:out value="${costs.estimated}"/>		
			</span>
			<input type="hidden" name="total_Estimated_Hidden" id="total_Estimated_Hidden" value="<c:out value="${costs.estimated}"/>"/>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="total_Actual">
			<c:set var="var" value="${costs.actual}" />			
			<c:out value="${costs.actual}"/>		
			</span>
			<input type="hidden" name="total_Actual_Hidden" id="total_Actual_Hidden" value="<c:out value="${costs.actual}"/>"/>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="total_variance">
			<c:set var="var" value="${costs.variance}" />
			<c:choose>
			<c:when test="${costs.variance<0.0}">
			<font color=#347235>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:when test="${costs.variance>0.0}">
			<font color=#ff0000>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:otherwise>
			<c:out value="${costs.variance}"/>
			</c:otherwise>
			</c:choose>		
			</span>
			<input type="hidden" name="total_variance_Hidden" id="total_variance_Hidden" value="<c:out value="${costs.variance}"/>"/>
			</td>
  		</c:if> 		
  		<c:if test="${costs.costName=='Project Management'}">
			<td class="tableRow" valign="top" style="font-weight:bold">
			<c:out value="${costs.costName}"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_projectManagement_Text');fieldfocus('insertEstimated_projectManagement_TextField','insertEstimated_projectManagement_Text');" style="text-decoration:none;">
			<span id="insertEstimated_projectManagement">						
			<c:out value="${costs.estimated}"/>
			</span>
			<input type="hidden" name="insertEstimated_projectManagement_Hidden" id="insertEstimated_projectManagement_Hidden" value="<c:out value="${costs.estimated}"/>"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_projectManagement_Text">
			<input type="text" name="insertEstimated_projectManagement_TextField" id="insertEstimated_projectManagement_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_projectManagement','insertEstimated_projectManagement_Text','insertvariance_projectManagement','insertActual_projectManagement_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_projectManagement', 'projectManagement_Text', 'insertvariance_projectManagement','insertActual_projectManagement','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.estimated}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_projectManagement_Text');fieldfocus('insertActual_projectManagement_TextField','insertActual_projectManagement_Text');" style="text-decoration:none;">
			<span id="insertActual_projectManagement">
			<c:out value="${costs.actual}"/>
			</span>	
			<input type="hidden" name="insertActual_projectManagement_Hidden" id="insertActual_projectManagement_Hidden" value="<c:out value="${costs.actual}"/>"/>			
			</a><br>
			<DIV style="display:none" id="insertActual_projectManagement_Text">
			<input type="text" name="insertActual_projectManagement_TextField" id="insertActual_projectManagement_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_projectManagement','insertActual_projectManagement_Text','insertvariance_projectManagement','insertEstimated_projectManagement_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_projectManagement', 'insertActual_projectManagement_Text', 'insertvariance_projectManagement','insertEstimated_projectManagement_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.actual}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:set var="var" value="${costs.variance}" />
			<span id="insertvariance_projectManagement">
			<c:choose>
			<c:when test="${costs.variance<0.0}">
			<font color=#347235>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:when test="${costs.variance>0.0}">
			<font color=#ff0000>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:otherwise>
			<c:out value="${costs.variance}"/>
			</c:otherwise>
			</c:choose>
			</span>
			<input type="hidden" name="insertvariance_projectManagement_Hidden" id="insertvariance_projectManagement_Hidden" value="<c:out value="${costs.variance}"/>"/>
			</td>
			</c:if>		
		<c:if test="${costs.costName=='Hardware Sale'}">
			<td class="tableRow" valign="top" style="font-weight:bold">
			<c:out value="${costs.costName}"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_hardwareSales_Text');fieldfocus('insertEstimated_hardwareSales_TextField','insertEstimated_hardwareSales_Text');" style="text-decoration:none;">
			<span id="insertEstimated_hardwareSales">						
			<c:out value="${costs.estimated}"/>
			</span>
			<input type="hidden" name="insertEstimated_hardwareSales_Hidden" id="insertEstimated_hardwareSales_Hidden" value="<c:out value="${costs.estimated}"/>"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_hardwareSales_Text">
			<input type="text" name="insertEstimated_hardwareSales_TextField" id="insertEstimated_hardwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_hardwareSales','insertEstimated_hardwareSales_Text','insertvariance_hardwareSales','insertActual_hardwareSales_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_hardwareSales', 'hardwareSales_Text', 'insertvariance_hardwareSales','insertActual_hardwareSales','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.estimated}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_hardwareSales_Text');fieldfocus('insertActual_hardwareSales_TextField','insertActual_hardwareSales_Text');" style="text-decoration:none;">
			<span id="insertActual_hardwareSales">
			<c:out value="${costs.actual}"/>
			</span>	
			<input type="hidden" name="insertActual_hardwareSales_Hidden" id="insertActual_hardwareSales_Hidden" value="<c:out value="${costs.actual}"/>"/>			
			</a><br>
			<DIV style="display:none" id="insertActual_hardwareSales_Text">
			<input type="text" name="insertActual_hardwareSales_TextField" id="insertActual_hardwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_hardwareSales','insertActual_hardwareSales_Text','insertvariance_hardwareSales','insertEstimated_hardwareSales_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_hardwareSales', 'insertActual_hardwareSales_Text', 'insertvariance_hardwareSales','insertEstimated_hardwareSales_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.actual}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:set var="var" value="${costs.variance}" />
			<span id="insertvariance_hardwareSales">
			<c:choose>
			<c:when test="${costs.variance<0.0}">
			<font color=#347235>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:when test="${costs.variance>0.0}">
			<font color=#ff0000>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:otherwise>
			<c:out value="${costs.variance}"/>
			</c:otherwise>
			</c:choose>
			</span>
			<input type="hidden" name="insertvariance_hardwareSales_Hidden" id="insertvariance_hardwareSales_Hidden" value="<c:out value="${costs.variance}"/>"/>
			</td>
			</c:if>		
		<c:if test="${costs.costName=='Software Sale'}">
			<td class="tableRow" valign="top" style="font-weight:bold">
			<c:out value="${costs.costName}"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_softwareSales_Text');fieldfocus('insertEstimated_softwareSales_TextField','insertEstimated_softwareSales_Text');" style="text-decoration:none;">
			<span id="insertEstimated_softwareSales">						
			<c:out value="${costs.estimated}"/>
			</span>
			<input type="hidden" name="insertEstimated_softwareSales_Hidden" id="insertEstimated_softwareSales_Hidden" value="<c:out value="${costs.estimated}"/>"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_softwareSales_Text">
			<input type="text" name="insertEstimated_softwareSales_TextField" id="insertEstimated_softwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_softwareSales','insertEstimated_softwareSales_Text','insertvariance_softwareSales','insertActual_softwareSales_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_softwareSales', 'softwareSales_Text', 'insertvariance_softwareSales','insertActual_softwareSales','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.estimated}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_softwareSales_Text');fieldfocus('insertActual_softwareSales_TextField','insertActual_softwareSales_Text');" style="text-decoration:none;">
			<span id="insertActual_softwareSales">
			<c:out value="${costs.actual}"/>
			</span>	
			<input type="hidden" name="insertActual_softwareSales_Hidden" id="insertActual_softwareSales_Hidden" value="<c:out value="${costs.actual}"/>"/>			
			</a><br>
			<DIV style="display:none" id="insertActual_softwareSales_Text">
			<input type="text" name="insertActual_softwareSales_TextField" id="insertActual_softwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_softwareSales','insertActual_softwareSales_Text','insertvariance_softwareSales','insertEstimated_softwareSales_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_softwareSales', 'insertActual_softwareSales_Text', 'insertvariance_softwareSales','insertEstimated_softwareSales_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.actual}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:set var="var" value="${costs.variance}" />
			<span id="insertvariance_softwareSales">
			<c:choose>
			<c:when test="${costs.variance<0.0}">
			<font color=#347235>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:when test="${costs.variance>0.0}">
			<font color=#ff0000>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:otherwise>
			<c:out value="${costs.variance}"/>
			</c:otherwise>
			</c:choose>
			</span>
			<input type="hidden" name="insertvariance_softwareSales_Hidden" id="insertvariance_softwareSales_Hidden" value="<c:out value="${costs.variance}"/>"/>
			</td>
			</c:if>							
  		<c:if test="${costs.costName=='Development'}">
			<td class="tableRow" valign="top" style="font-weight:bold">
			<c:out value="${costs.costName}"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_development_Text');fieldfocus('insertEstimated_development_TextField','insertEstimated_development_Text');" style="text-decoration:none;">
			<span id="insertEstimated_development">						
			<c:out value="${costs.estimated}"/>
			</span>
			<input type="hidden" name="insertEstimated_development_Hidden" id="insertEstimated_development_Hidden" value="<c:out value="${costs.estimated}"/>"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_development_Text">
			<input type="text" name="insertEstimated_development_TextField" id="insertEstimated_development_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_development','insertEstimated_development_Text','insertvariance_development','insertActual_development_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_development', 'development_Text', 'insertvariance_development','insertActual_development','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.estimated}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_development_Text');fieldfocus('insertActual_development_TextField','insertActual_development_Text');" style="text-decoration:none;">
			<span id="insertActual_development">
			<c:out value="${costs.actual}"/>
			</span>	
			<input type="hidden" name="insertActual_development_Hidden" id="insertActual_development_Hidden" value="<c:out value="${costs.actual}"/>"/>			
			</a><br>
			<DIV style="display:none" id="insertActual_development_Text">
			<input type="text" name="insertActual_development_TextField" id="insertActual_development_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_development','insertActual_development_Text','insertvariance_development','insertEstimated_development_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_development', 'insertActual_development_Text', 'insertvariance_development','insertEstimated_development_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.actual}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:set var="var" value="${costs.variance}" />
			<span id="insertvariance_development">
			<c:choose>
			<c:when test="${costs.variance<0.0}">
			<font color=#347235>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:when test="${costs.variance>0.0}">
			<font color=#ff0000>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:otherwise>
			<c:out value="${costs.variance}"/>
			</c:otherwise>
			</c:choose>
			</span>
			<input type="hidden" name="insertvariance_development_Hidden" id="insertvariance_development_Hidden" value="<c:out value="${costs.variance}"/>"/>
			</td>
			</c:if>
		<c:if test="${costs.costName=='Travel'}">
			<td class="tableRow" valign="top" style="font-weight:bold">
			<c:out value="${costs.costName}"/></td>
			<td class="tableRow" align="right"  valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_travel_Text');fieldfocus('insertEstimated_travel_TextField','insertEstimated_travel_Text');" style="text-decoration:none;">
			<span id="insertEstimated_travel">						
			<c:out value="${costs.estimated}"/>
			</span>
			<input type="hidden" name="insertEstimated_travel_Hidden" id="insertEstimated_travel_Hidden" value="<c:out value="${costs.estimated}"/>"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_travel_Text">
			<input type="text" name="insertEstimated_travel_TextField" id="insertEstimated_travel_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_travel','insertEstimated_travel_Text','insertvariance_travel','insertActual_travel_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_travel', 'travel_Text', 'insertvariance_travel','insertActual_travel','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.estimated}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right"  valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_travel_Text');fieldfocus('insertActual_travel_TextField','insertActual_travel_Text');" style="text-decoration:none;">
			<span id="insertActual_travel">
			<c:out value="${costs.actual}"/>
			</span>	
			<input type="hidden" name="insertActual_travel_Hidden" id="insertActual_travel_Hidden" value="<c:out value="${costs.actual}"/>"/>			
			</a><br>
			<DIV style="display:none" id="insertActual_travel_Text">
			<input type="text" name="insertActual_travel_TextField" id="insertActual_travel_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_travel','insertActual_travel_Text','insertvariance_travel','insertEstimated_travel_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_travel', 'insertActual_travel_Text', 'insertvariance_travel','insertEstimated_travel_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.actual}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right"  valign="top">
			<c:set var="var" value="${costs.variance}" />
			<span id="insertvariance_travel">
			<c:choose>
			<c:when test="${costs.variance<0.0}">
			<font color=#347235>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:when test="${costs.variance>0.0}">
			<font color=#ff0000>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:otherwise>
			<c:out value="${costs.variance}"/>
			</c:otherwise>
			</c:choose>
			</span>
			<input type="hidden" name="insertvariance_travel_Hidden" id="insertvariance_travel_Hidden" value="<c:out value="${costs.variance}"/>"/>
			</td>
			</c:if>	
		<c:if test="${costs.costName=='Others'}">
			<td class="tableRow" valign="top" style="font-weight:bold">
			<c:out value="${costs.costName}"/></td>
			<td class="tableRow" align="right"  valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_others_Text');fieldfocus('insertEstimated_others_TextField','insertEstimated_others_Text');" style="text-decoration:none;">
			<span id="insertEstimated_others">						
			<c:out value="${costs.estimated}"/>
			</span>
			<input type="hidden" name="insertEstimated_others_Hidden" id="insertEstimated_others_Hidden" value="<c:out value="${costs.estimated}"/>"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_others_Text">
			<input type="text" name="insertEstimated_others_TextField" id="insertEstimated_others_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_others','insertEstimated_others_Text','insertvariance_others','insertActual_others_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_others', 'others_Text', 'insertvariance_others','insertActual_others','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.estimated}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right"  valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_others_Text');fieldfocus('insertActual_others_TextField','insertActual_others_Text');" style="text-decoration:none;">
			<span id="insertActual_others">
			<c:out value="${costs.actual}"/>
			</span>	
			<input type="hidden" name="insertActual_others_Hidden" id="insertActual_others_Hidden" value="<c:out value="${costs.actual}"/>"/>			
			</a><br>
			<DIV style="display:none" id="insertActual_others_Text">
			<input type="text" name="insertActual_others_TextField" id="insertActual_others_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_others','insertActual_others_Text','insertvariance_others','insertEstimated_others_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_others', 'insertActual_others_Text', 'insertvariance_others','insertEstimated_others_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value="<c:out value="${costs.actual}"/>"/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:set var="var" value="${costs.variance}" />
			<span id="insertvariance_others">
			<c:choose>
			<c:when test="${costs.variance<0.0}">
			<font color=#347235>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:when test="${costs.variance>0.0}">
			<font color=#ff0000>
			<c:out value="${costs.variance}"/></font>
			</c:when>
			<c:otherwise>
			<c:out value="${costs.variance}"/>
			</c:otherwise>
			</c:choose>
			</span>
			<input type="hidden" name="insertvariance_others_Hidden" id="insertvariance_others_Hidden" value="<c:out value="${costs.variance}"/>"/>
			</td>
			</c:if>		
			</tr>
			</c:forEach>
  		</c:when>
  		<c:otherwise>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold">
			<fmt:message key="project.label.projectManagement"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_projectManagement_Text');fieldfocus('insertEstimated_projectManagement_TextField','insertEstimated_projectManagement_Text');" style="text-decoration:none;">
			<span id="insertEstimated_projectManagement">
			0.0
			</span>
			<input type="hidden" name="insertEstimated_projectManagement_Hidden" id="insertEstimated_projectManagement_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_projectManagement_Text">
			<input type="text" name="insertEstimated_projectManagement_TextField" id="insertEstimated_projectManagement_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_projectManagement','insertEstimated_projectManagement_Text','insertvariance_projectManagement','insertActual_projectManagement_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_projectManagement', 'insertEstimated_projectManagement_Text', 'insertvariance_projectManagement','insertActual_projectManagement_TextField','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right"  valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_projectManagement_Text');fieldfocus('insertActual_projectManagement_TextField','insertActual_projectManagement_Text');" style="text-decoration:none;">
			<span id="insertActual_projectManagement">
			0.0
			</span>	
			<input type="hidden" name="insertActual_projectManagement_Hidden" id="insertActual_projectManagement_Hidden" value="0"/>			
			</a><br>
			<DIV style="display:none" id="insertActual_projectManagement_Text">
			<input type="text" name="insertActual_projectManagement_TextField" id="insertActual_projectManagement_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_projectManagement','insertActual_projectManagement_Text','insertvariance_projectManagement','insertEstimated_projectManagement_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_projectManagement', 'insertActual_projectManagement_Text', 'insertvariance_projectManagement','insertEstimated_projectManagement_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="insertvariance_projectManagement">
			0.0
			</span>
			<input type="hidden" name="insertvariance_projectManagement_Hidden" id="insertvariance_projectManagement_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.development"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_development_Text');fieldfocus('insertEstimated_development_TextField','insertEstimated_development_Text');" style="text-decoration:none;">
			<span id="insertEstimated_development">
			0.0
			</span>		
			<input type="hidden" name="insertEstimated_development_Hidden" id="insertEstimated_development_Hidden" value="0"/>		
			</a><br>
			<DIV style="display:none" id="insertEstimated_development_Text">
			<input type="text" name="insertEstimated_development_TextField" id="insertEstimated_development_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_development','insertEstimated_development_Text','insertvariance_development','insertActual_development_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_development', 'insertEstimated_development_Text', 'insertvariance_development','insertActual_development_TextField','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_development_Text');fieldfocus('insertActual_development_TextField','insertActual_development_Text');" style="text-decoration:none;">
			<span id="insertActual_development">
			0.0
			</span>	
			<input type="hidden" name="insertActual_development_Hidden" id="insertActual_development_Hidden" value="0"/>		
			</a><br>
			<DIV style="display:none" id="insertActual_development_Text">
			<input type="text" name="insertActual_development_TextField" id="insertActual_development_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_development','insertActual_development_Text','insertvariance_development','insertEstimated_development_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_development', 'insertActual_development_Text', 'insertvariance_development','insertEstimated_development_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="insertvariance_development">
			0.0
			</span>
			<input type="hidden" name="insertvariance_development_Hidden" id="insertvariance_development_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.travel"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_travel_Text');fieldfocus('insertEstimated_travel_TextField','insertEstimated_travel_Text');" style="text-decoration:none;">
			<span id="insertEstimated_travel">
			0.0
			</span>
			<input type="hidden" name="insertEstimated_travel_Hidden" id="insertEstimated_travel_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_travel_Text">
			<input type="text" name="insertEstimated_travel_TextField" id="insertEstimated_travel_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_travel','insertEstimated_travel_Text','insertvariance_travel','insertActual_travel_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_travel', 'insertEstimated_travel_Text', 'insertvariance_travel','insertActual_travel_TextField','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_travel_Text');fieldfocus('insertActual_travel_TextField','insertActual_travel_Text');" style="text-decoration:none;">
			<span id="insertActual_travel">
			0.0
			</span>
			<input type="hidden" name="insertActual_travel_Hidden" id="insertActual_travel_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertActual_travel_Text">
			<input type="text" name="insertActual_travel_TextField" id="insertActual_travel_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_travel','insertActual_travel_Text','insertvariance_travel','insertEstimated_travel_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_travel', 'insertActual_travel_Text', 'insertvariance_travel','insertEstimated_travel_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="insertvariance_travel">
			0.0
			</span>
			<input type="hidden" name="insertvariance_travel_Hidden" id="insertvariance_travel_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.hardwareSale"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_hardwareSales_Text');fieldfocus('insertEstimated_hardwareSales_TextField','insertEstimated_hardwareSales_Text');" style="text-decoration:none;">
			<span id="insertEstimated_hardwareSales">
			0.0
			</span>
			<input type="hidden" name="insertEstimated_hardwareSales_Hidden" id="insertEstimated_hardwareSales_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_hardwareSales_Text">
			<input type="text" name="insertEstimated_hardwareSales_TextField" id="insertEstimated_hardwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_hardwareSales','insertEstimated_hardwareSales_Text','insertvariance_hardwareSales','insertActual_hardwareSales_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_hardwareSales', 'insertEstimated_hardwareSales_Text', 'insertvariance_hardwareSales','insertActual_hardwareSales_TextField','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_hardwareSales_Text');fieldfocus('insertActual_hardwareSales_TextField','insertActual_hardwareSales_Text');" style="text-decoration:none;">
			<span id="insertActual_hardwareSales">
			0.0
			</span>
			<input type="hidden" name="insertActual_hardwareSales_Hidden" id="insertActual_hardwareSales_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertActual_hardwareSales_Text">
			<input type="text" name="insertActual_hardwareSales_TextField" id="insertActual_hardwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_hardwareSales','insertActual_hardwareSales_Text','insertvariance_hardwareSales','insertEstimated_hardwareSales_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_hardwareSales', 'insertActual_hardwareSales_Text', 'insertvariance_hardwareSales','insertEstimated_hardwareSales_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right"  valign="top">
			<span id="insertvariance_hardwareSales">
			0.0
			</span>
			<input type="hidden" name="insertvariance_hardwareSales_Hidden" id="insertvariance_hardwareSales_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.softwareSale"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_softwareSales_Text');fieldfocus('insertEstimated_softwareSales_TextField','insertEstimated_softwareSales_Text');" style="text-decoration:none;">
			<span id="insertEstimated_softwareSales">
			0.0
			</span>
			<input type="hidden" name="insertEstimated_softwareSales_Hidden" id="insertEstimated_softwareSales_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_softwareSales_Text">
			<input type="text" name="insertEstimated_softwareSales_TextField" id="insertEstimated_softwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_softwareSales','insertEstimated_softwareSales_Text','insertvariance_softwareSales','insertActual_softwareSales_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_softwareSales', 'insertEstimated_softwareSales_Text', 'insertvariance_softwareSales','insertActual_softwareSales_TextField','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_softwareSales_Text');fieldfocus('insertActual_softwareSales_TextField','insertActual_softwareSales_Text');" style="text-decoration:none;">
			<span id="insertActual_softwareSales">
			0.0
			</span>
			<input type="hidden" name="insertActual_softwareSales_Hidden" id="insertActual_softwareSales_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertActual_softwareSales_Text">
			<input type="text" name="insertActual_softwareSales_TextField" id="insertActual_softwareSales_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_softwareSales','insertActual_softwareSales_Text','insertvariance_softwareSales','insertEstimated_softwareSales_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_softwareSales', 'insertActual_softwareSales_Text', 'insertvariance_softwareSales','insertEstimated_softwareSales_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="insertvariance_softwareSales">
			0.0
			</span>
			<input type="hidden" name="insertvariance_softwareSales_Hidden" id="insertvariance_softwareSales_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.others"/></td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertEstimated_others_Text');fieldfocus('insertEstimated_others_TextField','insertEstimated_others_Text');" style="text-decoration:none;">
			<span id="insertEstimated_others">
			0.0
			</span>
			<input type="hidden" name="insertEstimated_others_Hidden" id="insertEstimated_others_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertEstimated_others_Text">
			<input type="text" name="insertEstimated_others_TextField" id="insertEstimated_others_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertEstimated_others','insertEstimated_others_Text','insertvariance_others','insertActual_others_TextField','1',this)"
			onkeydown="javascript:specialKeyFunctions('insertEstimated_others', 'insertEstimated_others_Text', 'insertvariance_others','insertActual_others_TextField','1',this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<a href="javascript:togglePanelVisibility('insertActual_others_Text');fieldfocus('insertActual_others_TextField','insertActual_others_Text');" style="text-decoration:none;">
			<span id="insertActual_others">
			0.0
			</span>
			<input type="hidden" name="insertActual_others_Hidden" id="insertActual_others_Hidden" value="0"/>
			</a><br>
			<DIV style="display:none" id="insertActual_others_Text">
			<input type="text" name="insertActual_others_TextField" id="insertActual_others_TextField" size="10" class="textField" 
			onchange="javascript:insertCost('insertActual_others','insertActual_others_Text','insertvariance_others','insertEstimated_others_TextField','2',this)"
			onkeydown="javascript:specialKeyFunctions('insertActual_others', 'insertActual_others_Text', 'insertvariance_others','insertEstimated_others_TextField','2', this, event)"
			onkeypress="return floatonly(this,event)"
			value=""/>
			</DIV>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="insertvariance_others">
			0.0
			</span>
			<input type="hidden" name="insertvariance_others_Hidden" id="insertvariance_others_Hidden" value="0"/>
			</td>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.total"/></td>
			<td class="tableRow" align="right" valign="top">
			<span id="total_Estimated">
			0.0
			</span>
			<input type="hidden" name="total_Estimated_Hidden" id="total_Estimated_Hidden" value="0"/>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="total_Actual">
			0.0
			</span>
			<input type="hidden" name="total_Actual_Hidden" id="total_Actual_Hidden" value="0"/>
			</td>
			<td class="tableRow" align="right" valign="top">
			<span id="total_variance">
			0.0			
			</span>
			<input type="hidden" name="total_variance_Hidden" id="total_variance_Hidden" value="0"/>
			</td>
		</tr>
		</c:otherwise>
		</c:choose>
</table>
</DIV>
	</td>
</tr>
<tr class="contentBgColor">
    <td colspan="2" style="color : red">
	<fmt:message key="project.label.note"/><br>
	* <fmt:message key="project.label.figures"/>
    </td>
</tr>
<tr class="contentBgColor">
    <td align="center" colspan="2">
    <x:display name="${w.childMap.save.absoluteName}"/>&nbsp;
    <x:display name="${w.childMap.savePrint.absoluteName}"/>
    </td>
</tr>
</table>
<script language="javascript" type="text/JavaScript">
function init(){
var myForm = document.forms['<c:out value="${w.absoluteName}" />'];
var summary = myForm['<c:out value="${w.childMap.summary.absoluteName}" />'];
var schedule = myForm['<c:out value="${w.childMap.schedule.absoluteName}" />'];
var effort = myForm['<c:out value="${w.childMap.effort.absoluteName}" />'];
var defect = myForm['<c:out value="${w.childMap.defect.absoluteName}" />'];
var cost = myForm['<c:out value="${w.childMap.cost.absoluteName}" />'];
summary.checked=true;
schedule.checked=true;
effort.checked=true;
defect.checked=true;
cost.checked=true;
}
window.onload=init;
</script>
<jsp:include page="../form_footer.jsp" flush="true"/>