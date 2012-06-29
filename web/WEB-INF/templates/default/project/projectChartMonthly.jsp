<%@ page import="org.apache.commons.lang.StringUtils,
                 com.tms.collab.forum.model.Message,
                 com.tms.collab.project.ui.ProjectChart,
				 com.tms.collab.taskmanager.model.TaskManager,
                 kacang.Application,
                 com.tms.collab.project.Project,
                 com.tms.collab.taskmanager.model.Task,
                 com.tms.collab.project.Milestone,
				 com.tms.collab.taskmanager.model.Assignee,
				 com.tms.collab.project.WormsUtil,
				 com.tms.collab.emeeting.Meeting,
				 com.tms.collab.calendar.model.CalendarUtil,
				 com.tms.collab.calendar.model.CalendarEvent,
				 java.util.*"%>
<%@ include file="/common/header.jsp" %>
<script language="javascript">
	//Initializing page constants
	var classIndex1=0;
	var classIndex2=0;
	var classIndex3=0;
	var classIndex4=0;
	var widthMax;
	var heightMax;
	var xCoor;
	var yCoor;
	var sWidth;
	var sHeight;
	var xScroll;
	var widthScroll;
	var ie4 = false;
	
	if(document.all) {
		ie4 = true;
	}
	function displayMenu(strLayerID)
	{
		var divTask = document.getElementById(strLayerID);
        if (divTask.style.visibility != "hidden")
            divTask.style.visibility = "hidden"
        else
        {
            hideMenus();
            if (divTask.style.visibility == "hidden")
            {
                calculatePosition(strLayerID);
                divTask.style.visibility = "visible";
                return false;
            }
        }
	}
	function hideMenus()
	{
        <c:forEach var="milestone" items="${widget.project.milestones}">
            <c:forEach var="task" items="${milestone.tasks}">
                document.getElementById("task_<c:out value="${task.id}"/>").style.visibility = "hidden";
            </c:forEach>
			<c:forEach var="meeting" items="${milestone.meetings}">
                document.getElementById("task_<c:out value="${meeting.eventId}"/>").style.visibility = "hidden";
            </c:forEach>
        </c:forEach>
	}
	function calculatePosition(strLayerID)
	{
		var divTask = document.getElementById(strLayerID);
		widthMax = document.body.clientWidth;
		heightMax = document.body.clientHeight;
		xCoor = window.event.x;
		yCoor = window.event.y;
		sWidth = divTask.style.posWidth;
		sHeight = divTask.style.posHeight;
		xScroll = document.body.scrollTop;
		widthScroll = document.body.offsetWidth - widthMax;
		xWidth = xCoor + sWidth;
		yHeight = yCoor + sHeight;

		if (divTask.style.visibility == "hidden")
		{
			if (yHeight < (heightMax - 1))
				divTask.style.posTop = yCoor + xScroll;
			else
			{
				if (yCoor < sHeight)
					divTask.style.posTop = xScroll + 10;
				else
				{
					divTask.style.posTop = yCoor - sHeight + xScroll - 2;
					if ((divTask.style.posTop + divTask.style.posHeight - xScroll + 2) > heightMax)
						divTask.style.posTop = heightMax - divTask.style.posHeight + xScroll - 2;
				}
			}
			if (xWidth < widthMax - 1)
                divTask.style.posLeft = xCoor + 10;
			else
			{
				if (xCoor == (widthMax + 1))
					divTask.style.posLeft = xCoor - sWidth - 3;
				else
				{
					if (widthMax < xCoor)
						divTask.style.posLeft = xCoor - sWidth - widthScroll - 1;
					else
						divTask.style.posLeft = xCoor - sWidth - 2;
				}
			}
		}
	}
	
function showAll() {
var theRules = new Array();	
if (document.styleSheets[0].cssRules) {		
theRules = document.styleSheets[0].cssRules;} 
else if (document.styleSheets[0].rules) {		
theRules = document.styleSheets[0].rules;	
}

if(theRules[classIndex1].style.display=="none"){
theRules[classIndex1].style.display='';}


if(theRules[classIndex2].style.display=="none"){
theRules[classIndex2].style.display='';}

if(theRules[classIndex3].style.display=="none"){
theRules[classIndex3].style.display='';}

if(theRules[classIndex4].style.display=="none"){
theRules[classIndex4].style.display='';}
}

function showPartGraft() {
var theRules = new Array();	
if (document.styleSheets[0].cssRules) {		
theRules = document.styleSheets[0].cssRules;} 
else if (document.styleSheets[0].rules) {		
theRules = document.styleSheets[0].rules;	
}

if(theRules[classIndex1].style.display==""){
theRules[classIndex1].style.display='none';}

if(theRules[classIndex2].style.display==""){
theRules[classIndex2].style.display='none';}

if(theRules[classIndex3].style.display=="none"){
theRules[classIndex3].style.display='';}

if(theRules[classIndex4].style.display=="none"){
theRules[classIndex4].style.display='';}

}

function showNoGraft() {
var theRules = new Array();	
if (document.styleSheets[0].cssRules) {		
theRules = document.styleSheets[0].cssRules;} 
else if (document.styleSheets[0].rules) {		
theRules = document.styleSheets[0].rules;	
}

if(theRules[classIndex1].style.display=="none"){
theRules[classIndex1].style.display='';}

if(theRules[classIndex2].style.display=="none"){
theRules[classIndex2].style.display='';}

if(theRules[classIndex3].style.display==""){
theRules[classIndex3].style.display='none';}

if(theRules[classIndex4].style.display==""){
theRules[classIndex4].style.display='none';}
}

function init(){
var counter=0;
var theRules = new Array();	
if (document.styleSheets[0].cssRules) {		
theRules = document.styleSheets[0].cssRules;} 
else if (document.styleSheets[0].rules) {		
theRules = document.styleSheets[0].rules;	
}
for(var i=0;i<theRules.length;i++){
if(theRules[i].selectorText==".projectOpenRow"){
counter++;
classIndex1=i;
if(counter==4){
break;
}
}
else if(theRules[i].selectorText==".projectCloseRow"){
counter++;
classIndex2=i;
if(counter==4){
break;
}
}
else if(theRules[i].selectorText==".chartTableRow"){
counter++;
classIndex3=i;
if(counter==4){
break;
}
}
else if(theRules[i].selectorText==".chartClassRow"){
counter++;
classIndex4=i;
if(counter==4){
break;
}
}
}
}

function toggle(strId)
	{
		var parentTask = getObject("Milestone_"+strId);
		for(var i=0;i<i+2;i++){
		
		var divTask = getObject("m"+strId+"t"+i);
		var divTask2 = getObject("m"+strId+"t"+i+"a");
		if(divTask){
        if (divTask.style.display == ""){
        	parentTask.innerHTML = "[+]";
            divTask.style.display = "none"
            divTask2.style.display = "none"
            
         }
        else if (divTask.style.display == "none"){
        	parentTask.innerHTML = "[-]";
            divTask.style.display = ""
            divTask2.style.display = ""
            
         }
         }else{
         break;
         }
         }
         for(var j=0;j<i+2;j++){
		
		var divTask = getObject("m"+strId+"m"+j);
		if(divTask){
        if (divTask.style.display == ""){
        	parentTask.innerHTML = "[+]";
            divTask.style.display = "none"          
         }
        else if (divTask.style.display == "none"){
			parentTask.innerHTML = "[+]";
            divTask.style.display = ""           
         }
         }else{
         break;
         }
         }
	}
function getObject(id) {
	if (ie4) {
		return document.all[id];
	}
	else {
		return document.getElementById(id);
	}
}
<!--window.onload=init;--->
</script>

<c:forEach var="milestone" items="${widget.project.milestones}">
    <c:forEach var="task" varStatus="taskCount" items="${milestone.tasks}">
        <div id="task_<c:out value="${task.id}"/>" style="position:absolute; visibility:hidden">
            <c:if test="${!empty task.attendees}">
                <table cellpadding="1" cellspacing="0" border="0">
                    <tr>
                        <td class="projectTableHeader">
                            <table cellpadding="10" cellspacing="0" border="0">
                                <tr>
                                    <td class="projectClassRow">
                                        <table cellpadding="0" cellspacing="0" border="0">
                                            <tr>
                                                <td class="projectTableRow">
                                                    <table cellpadding="3" cellspacing="1" width="100%">
                                                        <tr>
                                                            <td class="projectTableRow" nowrap><span class="classTextSmall"><b><fmt:message key="project.label.assignedTo"/></b></span></td>
                                                            <td class="projectTableRow" nowrap><span class="classTextSmall"><b><fmt:message key="project.label.progress"/></b></span></td>
                                                            <td class="projectTableRow" nowrap><span class="classTextSmall"><b><fmt:message key="project.label.status"/></b></span></td>
                                                            <td class="projectTableRow" nowrap><span class="classTextSmall"><b><fmt:message key="project.label.started"/></b></span></td>
                                                            <td class="projectTableRow" nowrap><span class="classTextSmall"><b><fmt:message key="project.label.ended"/></b></span></td>
                                                        </tr>
                                                        <c:forEach var="assignee" items="${task.attendees}">
                                                            <tr>
                                                                <td class="projectClassRow" nowrap><span class="classTextSmall"><c:out value="${assignee.name}"/></span></td>
                                                                <td class="projectClassRow" align="center" nowrap><span class="classTextSmall"><fmt:formatNumber value="${assignee.progress}" pattern="0.00"/>%</span></td>
                                                                <td class="projectClassRow" align="center" nowrap><span class="classTextSmall">
                                                                    <c:choose>
                                                                        <c:when test="${assignee.taskStatus == 0}"><fmt:message key="project.label.notStarted"/></c:when>
                                                                        <c:when test="${assignee.taskStatus == 1}"><fmt:message key="project.label.underway"/></c:when>
                                                                        <c:when test="${assignee.taskStatus == 2}"><fmt:message key="project.label.completed"/></c:when>
                                                                    </c:choose>
                                                                </span></td>
                                                                <td class="projectClassRow" align="center" nowrap><span class="classTextSmall">
                                                                    <c:choose>
                                                                        <c:when test="${!empty assignee.startDate}"><fmt:formatDate value="${assignee.startDate}" pattern="dMMMyyyy"/></c:when>
                                                                        <c:otherwise>-</c:otherwise>
                                                                    </c:choose>
                                                                </span></td>
                                                                <td class="projectClassRow" align="center" nowrap><span class="classTextSmall">
                                                                    <c:choose>
                                                                        <c:when test="${!empty assignee.completeDate}"><fmt:formatDate value="${assignee.completeDate}" pattern="dMMMyyyy"/></c:when>
                                                                        <c:otherwise>-</c:otherwise>
                                                                    </c:choose>
                                                                </span></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr><td class="projectClassRow" align="right">
												<%-- TODO: Remove hardcode --%>
                                                <a href="<c:url value='/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${task.id}'/>" target="viewTask"><fmt:message key="project.label.viewTask"/></a>
                                                |
                                                <a href="" onClick="hideMenus(); return false;"><fmt:message key="project.label.close"/></a>
                                            </td></tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </c:if>
        </div>
    </c:forEach>
	<c:forEach items="${milestone.meetings}" var="meeting">
		<div id="task_<c:out value="${meeting.eventId}"/>" style="position:absolute; visibility:hidden">
			<table cellpadding="1" cellspacing="0" border="0">
				<tr>
					<td class="projectTableHeader">
						<table cellpadding="10" cellspacing="0" border="0">
							<tr>
								<td class="projectClassRow">
									<table cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td class="projectTableRow">
												<table cellpadding="3" cellspacing="1" width="100%">
													<tr><td class="projectTableRow" nowrap colspan="2"><span class="classTextSmall"><b><c:out value="${meeting.title}"/></b></span></td></tr>
													<tr>
														<td class="projectClassRowLabel" valign="top" align="right" nowrap><fmt:message key="project.label.starts"/> </td>
														<td class="projectClassRow"><fmt:formatDate value="${meeting.startDate}" pattern="dMMMyyyy"/></td>
													</tr>
													<tr>
														<td class="projectClassRowLabel" valign="top" align="right" nowrap><fmt:message key="project.label.ends"/> </td>
														<td class="projectClassRow"><fmt:formatDate value="${meeting.endDate}" pattern="dMMMyyyy"/></td>
													</tr>
													<tr>
														<td class="projectClassRowLabel" valign="top" align="right" nowrap><fmt:message key="project.label.secretary"/> </td>
														<td class="projectClassRow"><c:if test="${!empty meeting.secretary}"><c:out value="${meeting.secretaryName}"/></c:if></td>
													</tr>
													<tr>
														<td class="projectClassRowLabel" valign="top" align="right" nowrap><fmt:message key="project.label.classification"/>: </td>
														<td class="projectClassRow">
															<c:choose>
																<c:when test="${meeting.category == '-1'}"><fmt:message key="general.label.none"/></c:when>
																<c:otherwise><c:out value="${meeting.category}"/></c:otherwise>
															</c:choose>

														</td>
													</tr>
													<tr>
														<td class="projectClassRowLabel" valign="top" align="right" nowrap><fmt:message key="project.label.attendees"/>: </td>
														<td class="projectClassRow">
															<c:forEach items="${meeting.event.attendees}" var="attendee">
																<li><c:out value="${attendee.name}"/></li>
															</c:forEach>
														</td>
													</tr>
														<td class="projectClassRowLabel" valign="top" align="right" nowrap><fmt:message key="project.label.description"/>: </td>
														<td class="projectClassRow"><c:out value="${meeting.event.description}"/></td>
													</tr>
													<tr>
														<td class="projectClassRowLabel" valign="top" align="right" nowrap><fmt:message key="project.label.agenda"/>: </td>
														<td class="projectClassRow"><c:out value="${meeting.event.agenda}"/></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td class="projectClassRow" align="right">
											<%-- TODO: Remove hardcode --%>
											<a href="<c:url value='/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${meeting.eventId}'/>" target="viewTask"><fmt:message key="project.label.viewMeeting"/></a>
											|
											<a href="" onClick="hideMenus(); return false;"><fmt:message key="project.label.close"/></a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</c:forEach>
</c:forEach>
<c:set var="widget" value="${requestScope.projectChart}"/>
<c:set var="project" value="${widget.project}"/>
<%-- Constants Initialization --%>
<c-rt:set var="property_start_date" value="<%= ProjectChart.PROPERTY_START_DATE %>"/>
<c-rt:set var="property_end_date" value="<%= ProjectChart.PROPERTY_END_DATE %>"/>
<c-rt:set var="property_duration" value="<%= ProjectChart.PROPERTY_DURATION %>"/>
<c-rt:set var="property_progress" value="<%= ProjectChart.PROPERTY_PROJECT_PROGRESS %>"/>
<c:if test="${!(empty widget.project || empty widget.project.milestones)}">
    <table cellpadding="0" cellspacing="0" width="100%" align="center" border="0">
        <tr>
            <td class="projectTableRow">
                <table cellpadding="4" cellspacing="1" width="100%" border="0">
					<c:set var="project" value="${widget.project}" scope="page"/>
                    <c:set var="projectStart" value="${widget.properties[property_start_date]}" scope="page"/>
                    <c:set var="projectEnd" value="${widget.properties[property_end_date]}" scope="page"/>
                    <c:set var="projectDuration" value="${widget.properties[property_duration]}" scope="page"/>
                    <c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
                    <%
                        Project project = (Project) pageContext.getAttribute("project");
						int projectDuration = ((Integer)pageContext.getAttribute("projectDuration")).intValue()+1;
                        Calendar projectStart = Calendar.getInstance();
                        Calendar tempStart = Calendar.getInstance();
                        Calendar projectEnd = Calendar.getInstance();
                        if(pageContext.getAttribute("projectStart") != null)
                            projectStart.setTime((Date)pageContext.getAttribute("projectStart"));
                        if(pageContext.getAttribute("projectEnd") != null)
                            projectEnd.setTime((Date)pageContext.getAttribute("projectEnd"));

                        Calendar taskStart = Calendar.getInstance();
                        Calendar taskEnd = Calendar.getInstance();
                        Calendar milestoneStart = Calendar.getInstance();
                        Calendar milestoneEnd = Calendar.getInstance();
						Calendar meetingStart = Calendar.getInstance();
						Calendar meetingEnd = Calendar.getInstance();

                        int dayOfWeek = 1;
                        String strDay = "";
                        boolean jan = false;
                        int lFillerStart = 0;
                        int rFillerStart = 0;
                        
                        int mlFillerStart = 0;
                        int mrFillerStart = 0;

                        if(project.getProjectSummary()==null){
                            project.setProjectSummary("");
                        }
                    %>
                    <c:if test="${!widget.hideDetails}">
                         <tr>                         
                         <td colspan="8" class="projectTableRow" valign="top"><b><fmt:message key="project.label.projectDetails"/></b></td>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        <td colspan="<%= (projectDuration+2 ) %>" class="chartTableRow" valign="top"></td>
                        </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                        <td colspan="6"class="projectCloseRow"></td>
                        </c:if>
                        </tr>
                        <tr>
                        	
                            <td colspan="1" class="projectClassRowLabel" align="right" nowrap><fmt:message key="project.label.name"/> </td>                           
                            <td colspan="7" class="projectClassRow" nowrap><c:out value="${widget.project.projectName}"/></td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
                            </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td>
                            </c:if>
                        </tr>
                        <tr>                                               
                            <td colspan="1" class="projectClassRowLabel" align="right" nowrap><fmt:message key="project.label.description"/> </td>
                            <td colspan="7" class="projectClassRow" nowrap><c:out value="${widget.project.projectDescription}"/></td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>  
                            </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td>
                            </c:if>
                        </tr>
                        <tr>                     	
                            <td colspan="1" class="projectClassRowLabel" align="right" nowrap><fmt:message key="project.label.category"/> </td>                            
                            <td colspan="7" class="projectClassRow" nowrap><c:out value="${widget.project.projectCategory}"/></td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
                            </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td>
                            </c:if>
                        </tr>
                        <tr>
                            <td colspan="1" class="projectClassRowLabel" align="right" nowrap><fmt:message key="project.label.value"/> </td>                            
                            <td colspan="7" class="projectClassRow" nowrap>
                            <c:choose>
                            <c:when test="${!empty widget.project.projectCurrencyType}">
                            <c:out value="${widget.project.projectCurrencyType}"></c:out>
                            </c:when>
                            <c:otherwise>$</c:otherwise>
                            </c:choose>
                            <fmt:formatNumber value="${widget.project.projectValue}" pattern="#,##0.00"/></td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
                            </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td></c:if>
                        </tr>
                        <%--
                            <td colspan="<%= (projectDuration + 2) %>" class="projectClassRow" nowrap>$ <fmt:formatNumber value="${widget.project.projectValue}" pattern="#,##0.00"/></td>
                        </tr>
                        --%>
                        <tr>                         
                            <td colspan="1" class="projectClassRowLabel" align="right" nowrap><fmt:message key="project.label.starts"/> </td>
                            <td colspan="7" class="projectClassRow" nowrap><fmt:formatDate value="${widget.properties[property_start_date]}" pattern="dMMMyyyy"/></td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
                            </c:if>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td>
                            </c:if>
                        </tr>
                        <tr>							
                            <td colspan="1" class="projectClassRowLabel" align="right" nowrap><fmt:message key="project.label.ends"/> </td>                       
                            <td colspan="7" class="projectClassRow" nowrap><fmt:formatDate value="${widget.properties[property_end_date]}" pattern="dMMMyyyy"/></td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>                        
                            </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td>
                            </c:if>
                        </tr>
                        <tr>                        	
                            <td colspan="1" class="projectClassRowLabel" align="right" nowrap><fmt:message key="project.label.duration"/> </td>                            
                            <td colspan="7" class="projectClassRow" nowrap><c:out value="${widget.properties[property_duration]}"/> Day(s)</td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
                            </c:if>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td>
                            </c:if>
                        </tr>
						<tr>							
                            <td colspan="1" class="projectClassRowLabel" align="right" valign="top" nowrap><fmt:message key='project.label.documents'/> </td>                            
                            <td colspan="7" class="projectClassRow" nowrap>
                            <c:forEach items="${widget.project.files}" var="document">
									<li><a href="<c:out value="${widget.documentLink}?id=${document.id}"/>" target="_blank"><c:out value="${document.name}"/></a></li>
								</c:forEach></td>
							<c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">	
							<td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
							</c:if>
							<c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow">						
							</td></c:if>
                        </tr>
						<tr>							
                            <td colspan="1" class="projectClassRowLabel" align="right" valign="top" nowrap><fmt:message key='project.label.summary'/> </td>                            
                            <td colspan="7" class="projectClassRow" nowrap><%= StringUtils.replace((String)project.getProjectSummary(), "\n", "<br>") %></td>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
                            </c:if>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                            <td colspan="6"class="projectOpenRow"></td>
                            </c:if>
                        </tr>
                    </c:if>
                    <tr>                    
                    <td colspan="8" class="projectTableRow" valign="top">
                    <b><fmt:message key="project.label.milestonesAndTasks"/></b> 
                    <INPUT TYPE=RADIO NAME="view" VALUE="A" onclick="submit()" <c:if test="${widget.viewType=='all'}">checked</c:if>><fmt:message key="project.label.showAll"/>
					<INPUT TYPE=RADIO NAME="view" VALUE="P" onclick="submit()" <c:if test="${widget.viewType=='partial'}">checked</c:if>><fmt:message key="project.label.showPartial"/>
					<INPUT TYPE=RADIO NAME="view" VALUE="H" onclick="submit()" <c:if test="${widget.viewType=='hide'}">checked</c:if>><fmt:message key="project.label.hideChart"/>
                    
                    <!--<input type="button" class="button" name="showall" value="<fmt:message key="project.label.showAll"/>" onclick="showAll()"/>
                    --><!-- <a href="#" onclick="showAll();">[Show All]</a> -->&nbsp;
                    <!--<input type="button" class="button" name="showpartial" value="<fmt:message key="project.label.showPartial"/>" onclick="showPartGraft();"/>
                    --><!-- <a href="#" onclick="showPartGraft();">[Show Partial]</a> -->&nbsp;
                    <!--<input type="button" class="button" name="shownone" value="<fmt:message key="project.label.hideChart"/>" onclick="showNoGraft()"/>
                    --><!-- <a href="#" onclick="showNoGraft();">[Hide Chart]</a> -->
                    </td>
                    <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                    <td colspan="<%= (projectDuration+2 ) %>" class="chartTableRow" valign="top"></td>
                    </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                    <td colspan="6"class="projectCloseRow"></td>
                    </c:if>
                    </tr>  
                    <tr>
                        <td rowspan="2" class="projectClassRow" valign="top" nowrap>
                            <c:if test="${widget.hideDetails}">
                                <b><fmt:message key="project.label.starts"/></b>: <fmt:formatDate value="${widget.properties[property_start_date]}" pattern="dMMMyyyy"/>
                                -<br>
                                <b><fmt:message key="project.label.ends"/></b>: <fmt:formatDate value="${widget.properties[property_end_date]}" pattern="dMMMyyyy"/>
                            </c:if>
                        </td>                                                                    
                        <td rowspan="2" class="projectClassRow"><b><fmt:message key="project.label.estStart"/></td>  
                        <td rowspan="2" class="projectClassRow"><b><fmt:message key="project.label.estEnd"/></td>  
                        <td rowspan="2"class="projectClassRow"><b><fmt:message key="project.label.estDuration"/></td>                        
                        <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                        <td rowspan="2" class="projectOpenRow"><b><fmt:message key="project.label.actStart"/></td>  
                        <td rowspan="2" class="projectOpenRow"><b><fmt:message key="project.label.actEnd"/></td>  
                        <td rowspan="2"class="projectOpenRow"><b><fmt:message key="project.label.actDuration"/></td>                       
                        <td rowspan="2"class="projectOpenRow"><b><fmt:message key="project.label.varianceDays"/></td>                        
                        <td rowspan="2"class="projectOpenRow"><b><fmt:message key="project.label.startvariance"/></td>                       
                        <td rowspan="2"class="projectOpenRow"><b><fmt:message key="project.label.endvariance"/></td> 
                        </c:if><c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        <td width="5%" rowspan="2"class="chartClassRow"><b><fmt:message key="project.label.beforeProjectStart"/></b></td> 
                        <td width="63%" colspan="<%= projectDuration %>" class="chartClassRow" valign="top" nowrap>
                            <table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td class="classTextSmall">&darr;&nbsp;<fmt:formatDate value="${widget.properties[property_start_date]}" pattern="dMMMyyyy"/></td>
                                    <td class="classTextSmall" align="right"><fmt:formatDate value="${widget.properties[property_end_date]}" pattern="dMMMyyyy"/>&nbsp;&darr;</td>
                                </tr>
                            </table>
                        </td>
                        <td width="5%" rowspan="2"class="chartClassRow"><b><fmt:message key="project.label.afterProjectEnd"/></b></td> 
                        </c:if>
                    </tr>
                    <%-- Generating days --%>
                    <tr>
                        <c:set var="today"><fmt:formatDate pattern="MMM yy" value="${currentDate}" /></c:set>
                        <%
                            dayOfWeek = projectStart.get(Calendar.MONTH) + 1;
                            tempStart.setTime(projectStart.getTime());
                            strDay = "";
                            for(int i=0; i<projectDuration; i++)
                            {
                                if(pageContext.getAttribute("projectStart") == null || pageContext.getAttribute("projectEnd") == null)
                                    strDay = "&nbsp;";
                                else
                                {
                                    switch(dayOfWeek)
                                    {
                                    
                                    case 2: strDay = "Feb"; jan = false; break;
                                    case 3: strDay = "Mar"; jan = false; break;
                                    case 4: strDay = "Apr"; jan = false; break;
                                    case 5: strDay = "May"; jan = false; break;
                                    case 6: strDay = "Jun"; jan = false; break;
                                    case 7: strDay = "Jul"; jan = false; break;
                                    case 8: strDay = "Aug"; jan = false; break;
                                    case 9: strDay = "Sep"; jan = false; break;
                                    case 10: strDay = "Oct"; jan = false; break;
                                    case 11: strDay ="Nov"; jan = false; break;
                                    case 12: strDay = "Dec"; jan = false; break;                                      
                                    default: strDay = "Jan"; jan = true; break;
                                    }
                                }
                                pageContext.setAttribute("tempDate", tempStart.getTime());
                        %>
                            <c:set var="fDate"><fmt:formatDate pattern="MMM" value="${tempDate}" /></c:set>
                            <c:set var="fDate2"><fmt:formatDate pattern="MMM yy" value="${tempDate}" /></c:set>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>" align="center" style="cursor:hand" title="<c:out value="${fDate}"/>">
                                <span<c:if test="${fDate2 == today}"> class="highlight"</c:if>><%= strDay %></span>
                            </td>
                            </c:if>
                        <%
                                if(dayOfWeek == 13)
                                    dayOfWeek = 2;
                                else
                                    dayOfWeek++;

                                tempStart.add(Calendar.MONTH, 1);
                            }
                        %>
                    </tr>
                    <%-- END: Generating days --%>
                    <c:forEach var="milestone" items="${widget.project.milestones}" varStatus="milestoneIndex">                                     		
                        <tr>                     
                        <td class="projectClassRow">
                        <b><a href="#@" onclick="toggle('<c:out value="${milestoneIndex.index}"/>');">
                        <span id="Milestone_<c:out value="${milestoneIndex.index}"/>">[-]</span></a>
                        <c:out value="${milestone.milestoneName}"/><b>
                        </td>                       
                        <td valign="top" align="center"class="projectClassRow"><fmt:formatDate value="${milestone.startDate}" pattern="dMMMyyyy"/>
                        </td>
                        <td valign="top" align="center"class="projectClassRow"><fmt:formatDate value="${milestone.endDate}" pattern="dMMMyyyy"/>
                        </td>
                        <td valign="top" align="center"class="projectClassRow">
                        <c:out value="${milestone.duration}"/> 
                        </td>         
                        <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">            
                        <td valign="top" align="center"class="projectOpenRow"><c:out value="${milestone.actualStartDate}"/>
                        </td>
                        <td valign="top" align="center"class="projectOpenRow"><c:out value="${milestone.actualEndDate}"/>
                        </td>
                        <td valign="top" align="center"class="projectOpenRow">
                        <c:if test="${milestone.actualEndDate == 'Ongoing'}"> 
    					*
    					</c:if>
    					<c:out value="${milestone.actualDuration}"/>  
                        </td>
                        <td valign="top" align="center"class="projectOpenRow">
                         <c:choose>
  						<c:when test="${milestone.variance == 'Not Started'}">
  							<c:out value="${milestone.variance}"/>
  						</c:when>
  						<c:otherwise>
						<c:if test="${milestone.variance > 0}">
        					<font color="#ff0000">
    					</c:if>
    					<c:if test="${milestone.variance < 0}">
        					<font color="#347235">
    					</c:if>
    					<c:if test="${milestone.actualEndDate == 'Ongoing'}">
    					*  	
    					</c:if>				
    					<c:out value="${milestone.variance}"/>
    					
						<c:if test="${milestone.variance > 0 || milestone.variance < 0}">
        				</font>
    					</c:if>
  						</c:otherwise>
						</c:choose>
                        </td>                    
                        <td valign="top" align="center"class="projectOpenRow">
                        <c:choose>
  							<c:when test="${milestone.startVariance == 'Not Started'}">
  							<c:out value="${milestone.startVariance}"/>
  							</c:when>
  						<c:otherwise>
						<c:if test="${milestone.startVariance > 0}">
        					<font color="#ff0000">
    					</c:if>
    					<c:if test="${milestone.startVariance < 0}">
        					<font color="#347235">
    					</c:if>
    					<c:if test="${milestone.actualEndDate == 'Ongoing'}"> 
    					*
    					</c:if>
    					<c:out value="${milestone.startVariance}"/>
    					
						<c:if test="${milestone.startVariance > 0 || milestone.startVariance < 0}">
        				</font>
    					</c:if>
  						</c:otherwise>
						</c:choose>
                        </td>
                        <td valign="top" align="center"class="projectOpenRow">
                        <c:choose>
  						<c:when test="${milestone.endVariance == 'Not Started'}">
  							<c:out value="${milestone.endVariance}"/>
  						</c:when>
  						<c:otherwise>
						<c:if test="${milestone.endVariance > 0}">
        					<font color="#ff0000">
    					</c:if>
    					<c:if test="${milestone.endVariance < 0}">
        					<font color="#347235">
    					</c:if>
    					<c:if test="${milestone.actualEndDate == 'Ongoing'}"> 
    					*
    					</c:if>
    					<c:out value="${milestone.endVariance}"/>
    					
						<c:if test="${milestone.endVariance > 0 || milestone.endVariance < 0}">
        				</font>
    					</c:if>
  						</c:otherwise>
						</c:choose>
                        </td>
                        </c:if>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        <td class="chartClassRow">&nbsp;</td> 
                        </c:if>
                        <%
                        Milestone mstone = (Milestone)pageContext.findAttribute("milestone");
                        if(mstone.getTasks().size()>0){
                        %>
							<c:set var="milestoneId" value="${milestone.milestoneId}" scope="page"/>
                            <c:set var="milestoneStart" value="${milestone.startDate}" scope="page"/>
                            <c:set var="milestoneEnd" value="${milestone.endDate}" scope="page"/>
                            <%
                            {
                                Date mstart = (Date) pageContext.getAttribute("milestoneStart");
                                Date mend = (Date) pageContext.getAttribute("milestoneEnd");
                                milestoneStart.setTime(mstart);
                                milestoneEnd.setTime(mend);

                                Calendar mrightFillerStart = Calendar.getInstance();
                                mrightFillerStart.setTime(mend);
                                mrightFillerStart.add(Calendar.MONTH, 1);


                                int milestoneDuration = WormsUtil.getMonthsBetween(milestoneEnd.getTime(), milestoneStart.getTime()) + 1;
                                int mleftOffset = WormsUtil.getMonthsBetween(milestoneStart.getTime(), projectStart.getTime());
                                int mrightFiller = projectDuration-(mleftOffset + milestoneDuration);
                                int mworkingDays = WormsUtil.getWorkingDays(project.getProjectWorking(), milestoneStart.getTime(), milestoneEnd.getTime());

                                mlFillerStart = projectStart.get(Calendar.MONTH) + 1;
                                mrFillerStart = mrightFillerStart.get(Calendar.MONTH) + 1;
      
                                    for(int i=0; i<mleftOffset; i++)
                                    {
                                        if(mlFillerStart == 13 || mlFillerStart == 1)
                                        {
                                            jan = true;
                                            mlFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        mlFillerStart++;

                                %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>">&nbsp;</td>
                                </c:if>
                                <% } %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                <td class="chartClassRow" colspan="<%= milestoneDuration %>">
                                    <table cellpadding="1" cellspacing="0" border="0" width="100%" height="15">
                                        <tr>
                                            <th style="cursor:hand" title="<c:out value="${milestone.milestoneName}"/> (<fmt:formatDate pattern="${globalDateShort}" value="${milestone.startDate}"/> - <fmt:formatDate pattern="${globalDateShort}" value="${milestone.endDate}"/>)" class="projectTableHeader2">
                                                <!--<c:if test="${(task.overallProgress > 0)}">
                                                    <table cellpadding="0" cellspacing="0" height="100%" width="<c:out value="${task.overallProgress}%"/>" align="left" valign="center">
                                                        <tr><td class="projectTableRow">&nbsp;</td></tr>
                                                    </table>
                                                </c:if>
                                            --></th>
                                        </tr>
                                    </table>
                                </td>
                                </c:if>
                                <%
                                    for(int i=0; i<mrightFiller; i++)
                                    {
                                        if(mrFillerStart == 13 || mrFillerStart == 1)
                                        {
                                            jan = true;
                                            mrFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        mrFillerStart++;
                                %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>">&nbsp;</td>
                                </c:if>
                                <% } %>
                                 <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td class="chartClassRow">&nbsp;</td> 
                        		</c:if>
                            </tr>
                            <%
                            }
                            // calculate assignee start and end
                            Milestone milestone = (Milestone)pageContext.findAttribute("milestone");
                            Date mstart = null;
                            Date mend = null;
                            Date mactualStart = null;
                            Date mactualEnd = null;
                            Date startD=milestone.getActStartDate();
                            Date endD=milestone.getActEndDate();

                                if (startD != null && WormsUtil.before(startD, mstart))
                                {
                                    mactualStart = startD;
                                    mstart = startD;
                                }
                                if (mstart != null && WormsUtil.before(mstart, projectStart.getTime()))
                                    mstart = projectStart.getTime();
                                else if (mstart != null && WormsUtil.after(mstart, projectEnd.getTime()))
                                    mstart = projectEnd.getTime();
                               
                                if (endD != null && WormsUtil.after(endD, mend))
                                {
                                    mactualEnd = endD;
                                    mend = endD;
                                }
                                if (mend != null && WormsUtil.before(mend, projectStart.getTime()))
                                    mend = projectStart.getTime();
                                else if (mend != null && WormsUtil.after(mend, projectEnd.getTime()))
                                    mend = projectEnd.getTime();
                                
                            Date milestoneEstEnd=mactualEnd;                         
                            if(milestoneEstEnd==null){milestoneEstEnd=new Date();}     
                            // display progress
                            if (mstart != null)
                            {
                                Calendar mrightFillerStart = Calendar.getInstance();
                                milestoneStart.setTime(mstart);
                                milestoneEnd.setTime((mend != null) ? mend : new Date());
                                mrightFillerStart.setTime(milestoneEnd.getTime());
                                mrightFillerStart.add(Calendar.MONTH, 1);

                                int mleftOffset = WormsUtil.getMonthsBetween(milestoneStart.getTime(), projectStart.getTime());
                                if (mleftOffset < 0)
                                    mleftOffset = 0;
                                int milestoneDuration = WormsUtil.getMonthsBetween(milestoneEnd.getTime(), milestoneStart.getTime()) + 1;
                                int mrightFiller = projectDuration-(mleftOffset + milestoneDuration);

                                mlFillerStart = projectStart.get(Calendar.MONTH) + 1;
                                mrFillerStart = mrightFillerStart.get(Calendar.MONTH) + 1;
                            %>
                            <tr>
                                <td width="2%" class="projectClassRow" valign="top"></td>
                                <td width="25%" class="projectClassRow" valign="top" nowrap></td>
                                <td width="5%" class="projectClassRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                                
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                </c:if>
                                <%
                                    for(int i=0; i<mleftOffset; i++)
                                    {
                                        if(mlFillerStart == 13 || mlFillerStart == 1)
                                        {
                                            jan = true;
                                            mlFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        mlFillerStart++;

                                %><c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>"></td>
                                    </c:if>
                                <% } %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                <%
                                if (!((mactualStart.after(projectEnd.getTime())) || (mactualEnd != null && mactualEnd.before(mstart))) && milestoneEstEnd.after(projectEnd.getTime())) { 
                                	int milestoneDur=projectDuration-mleftOffset+1;
                                    if (mactualStart != null && mactualStart.before(mstart))
                                    	milestoneDur=milestoneDur+1;
                                    else{
                                %><td class="chartClassRow" width="5%" valign="top" align="center" nowrap></td>
                                <%} %>
                                <td class="chartClassRow" colspan="<%= milestoneDur %>">
                                	<% }else{
                                		if (mactualStart != null && mactualStart.before(mstart))
                                			milestoneDuration=milestoneDuration+1;
                                		else{
                                %><td class="chartClassRow" width="5%" valign="top" align="center" nowrap></td>
                                <%} %><td class="chartClassRow" colspan="<%= milestoneDuration %>">
                                	<%
                                	}
									 if (!((mactualStart.after(projectEnd.getTime())) || (mactualEnd != null && mactualEnd.before(mstart)))) { %>
                                    <table cellpadding="0" cellspacing="0" border="0" width="100%" height="3"/>
                                        <tr>
                                            <c-rt:set var="actualStartDate" value="<%= mactualStart %>"/>
                                            <c-rt:set var="actualEndDate" value="<%= mactualEnd %>"/>
                                            <th style="cursor:hand;background:black" title="<fmt:message key="project.label.actual"/>: <c:out value="${milestone.milestoneName}"/> (<fmt:formatDate pattern="${globalDateShort}" value="${milestone.actStartDate}"/> - <fmt:formatDate pattern="${globalDateShort}" value="${milestone.actEndDate}"/>)" class="projectTableHeader2">
                                                <!--<c:if test="${(task.overallProgress > 0)}">
                                                    <table cellpadding="0" cellspacing="0" height="3" width="100%" align="left" valign="center">
                                                        <tr><td style="background: blue" class="projectTableRow"></td></tr>
                                                    </table>
                                                </c:if>
                                            --></th>
                                        </tr>
                                    </table>
                                	<% } %>
                                </td>
                                </c:if>
                                <%
                                if (!(!((mactualStart.after(projectEnd.getTime())) || (mactualEnd != null && mactualEnd.before(mstart))) && milestoneEstEnd.after(projectEnd.getTime()))) { 
                                    
                                    for(int i=0; i<mrightFiller; i++)
                                    {
                                        if(mrFillerStart == 13 || mrFillerStart == 1)
                                        {
                                            jan = true;
                                            mrFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        mrFillerStart++;
                                %><c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>"></td>
                                    </c:if>
                                <% } %>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td width="5%"class="chartClassRow" valign="top" align="center" nowrap>
                                    <% if (mactualStart.after(projectEnd.getTime())) { %>
                                    <table cellpadding="0" cellspacing="0" border="0" width="100%" height="3">
                                        <tr>
                                            <c-rt:set var="actualStartDate" value="<%= mactualStart %>"/>
                                            <c-rt:set var="actualEndDate" value="<%= mactualEnd %>"/>
                                            <th style="cursor:hand;background:black" title="<fmt:message key="project.label.actual"/>: <c:out value="${milestone.milestoneName}"/> (<fmt:formatDate pattern="${globalDateShort}" value="${milestone.actStartDate}"/> - <fmt:formatDate pattern="${globalDateShort}" value="${milestone.actEndDate}"/>)" class="projectTableHeader2">
                                              <!--<c:if test="${(task.overallProgress > 0)}">
                                                    <table cellpadding="0" cellspacing="0" height="3" width="100%" align="left" valign="center">
                                                        <tr><td style="background: blue" class="projectTableRow"></td></tr>
                                                    </table>
                                                </c:if>
                                            --></th>
                                        </tr>
                                    </table>
                                    <% } %>
                                </td> 
                        		</c:if>
                            </tr>
                            <% }} %>
                        
                        <%}else{ %>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow">
                        </td>
                        </c:if>
                        <%} %>
                        
                        </tr>
						<%-- Rendering Tasks --%>
                        <c:forEach var="task" varStatus="taskCount" items="${milestone.tasks}">
                            <c:set var="taskId" value="${task.id}" scope="page"/>
                            <c:set var="taskStart" value="${task.startDate}" scope="page"/>
                            <c:set var="taskEnd" value="${task.dueDate}" scope="page"/>
                            <%
                            {
                                Date start = (Date) pageContext.getAttribute("taskStart");
                                Date end = (Date) pageContext.getAttribute("taskEnd");
                                taskStart.setTime(start);
                                taskEnd.setTime(end);

                                Calendar rightFillerStart = Calendar.getInstance();
                                rightFillerStart.setTime(end);
                                rightFillerStart.add(Calendar.MONTH, 1);


                                int taskDuration = WormsUtil.getMonthsBetween(taskEnd.getTime(), taskStart.getTime()) + 1;
                                int leftOffset = WormsUtil.getMonthsBetween(taskStart.getTime(), projectStart.getTime());
                                int rightFiller = projectDuration-(leftOffset + taskDuration);
                                int workingDays = WormsUtil.getWorkingDays(project.getProjectWorking(), taskStart.getTime(), taskEnd.getTime());

                                lFillerStart = projectStart.get(Calendar.MONTH) + 1;
                                rFillerStart = rightFillerStart.get(Calendar.MONTH) + 1;
                            %>
                            <c:choose>
                                <c:when test="${task.completed}"><c:set var="className" value="projectClassRow"/></c:when>
                                <c:otherwise><c:set var="className" value="projectClassRow"/></c:otherwise>
                            </c:choose>
                            <tr id="m<c:out value="${milestoneIndex.index}"/>t<c:out value="${taskCount.index}"/>">
                                <td width="25%" class="<c:out value="${className}"/>" valign="top" nowrap>
                                    <c:if test="${task.completed}">&radic;&nbsp;</c:if><a href="#" onClick="displayMenu('task_<c:out value="${task.id}"/>'); return false;"><c:out value="${task.title}"/></a>
                                </td>                             
                                <td width="5%" class="<c:out value="${className}"/>" valign="top" align="center" nowrap><fmt:formatDate value="${task.startDate}" pattern="dMMMyyyy"/></td>
                                <td width="5%" class="<c:out value="${className}"/>" valign="top" align="center" nowrap><fmt:formatDate value="${task.dueDate}" pattern="dMMMyyyy"/></td>
                                <td width="5%" class="<c:out value="${className}"/>" valign="top" align="center" nowrap><%= workingDays %> </td>                      
                                <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap><c:out value="${task.actualStartDate}"/></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap><c:out value="${task.actualEndDate}"/></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap>
                                <c:if test="${task.actualEndDate == 'Ongoing'}"> 
    							*
    							</c:if>
    							<c:out value="${task.actualDuration}"/> </td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap>
                                <c:choose>
  									<c:when test="${task.variance == 'Not Started'}">
  									<c:out value="${task.variance}"/>
  									</c:when>
  								<c:otherwise>
								<c:if test="${task.variance >0}">
        							<font color="#ff0000">
    							</c:if>
    							<c:if test="${task.variance <0}">
        							<font color="#347235">
    							</c:if>
    							<c:if test="${task.actualEndDate == 'Ongoing'}"> 
    							*
    							</c:if>
    								<c:out value="${task.variance}"/>
								<c:if test="${task.variance >0 || task.variance <0}">
        						</font>
    							</c:if>
  								</c:otherwise>
								</c:choose>
								</td>
  								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap>
  								<c:choose>
  									<c:when test="${task.startVariance == 'Not Started'}">
  									<c:out value="${task.startVariance}"/>
  									</c:when>
  								<c:otherwise>
								<c:if test="${task.startVariance >0}">
        							<font color="#ff0000">
    							</c:if>
    							<c:if test="${task.startVariance <0}">
        							<font color="#347235">
    							</c:if>
    							<c:if test="${task.actualEndDate == 'Ongoing'}"> 
    							*
    							</c:if>
    								<c:out value="${task.startVariance}"/>
								<c:if test="${task.startVariance >0 || task.startVariance <0}">
        						</font>
    							</c:if>
  								</c:otherwise>
								</c:choose>
  								</td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap>
                                <c:choose>
  									<c:when test="${task.endVariance == 'Not Started'}">
  									<c:out value="${task.endVariance}"/>
  									</c:when>
  								<c:otherwise>
								<c:if test="${task.endVariance >0}">
        							<font color="#ff0000">
    							</c:if>
    							<c:if test="${task.endVariance <0}">
        							<font color="#347235">
    							</c:if>
    							<c:if test="${task.actualEndDate == 'Ongoing'}"> 
    							*
    							</c:if>
    								<c:out value="${task.endVariance}"/>
								<c:if test="${task.endVariance >0 || task.endVariance <0}">
        						</font>
    							</c:if>
  								</c:otherwise>
								</c:choose>
                                </td>
  								</c:if>
  								<c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td class="chartClassRow">&nbsp;</td> 
                        		</c:if>
                                <%
                                    for(int i=0; i<leftOffset; i++)
                                    {
                                        if(lFillerStart == 13 || lFillerStart == 1)
                                        {
                                            jan = true;
                                            lFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        lFillerStart++;

                                %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>">&nbsp;</td>
                                </c:if>
                                <% } %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                <td class="chartClassRow" colspan="<%= taskDuration %>">
                                    <table cellpadding="1" cellspacing="0" border="0" width="100%" height="15">
                                        <tr>
                                            <th style="cursor:hand" title="<c:out value="${task.title}"/> (<fmt:formatDate pattern="${globalDateShort}" value="${task.startDate}"/> - <fmt:formatDate pattern="${globalDateShort}" value="${task.dueDate}"/>) - <fmt:formatNumber value="${task.overallProgress}" pattern="0"/>% <fmt:message key="project.label.completed"/>" class="projectTableHeader">
                                                <c:if test="${(task.overallProgress > 0)}">
                                                    <table cellpadding="0" cellspacing="0" height="100%" width="<c:out value="${task.overallProgress}%"/>" align="left" valign="center">
                                                        <tr><td class="projectTableRow">&nbsp;</td></tr>
                                                    </table>
                                                </c:if>
                                            </th>
                                        </tr>
                                    </table>
                                </td>
                                </c:if>
                                <%
                                    for(int i=0; i<rightFiller; i++)
                                    {
                                        if(rFillerStart == 13 || rFillerStart == 1)
                                        {
                                            jan = true;
                                            rFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        rFillerStart++;
                                %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>">&nbsp;</td>
                                    </c:if>
                                <% } %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td class="chartClassRow">&nbsp;</td> 
                        		</c:if>
                            </tr>
                            <%
                            }
                            // calculate assignee start and end
                            Task task = (Task)pageContext.findAttribute("task");
                            Date start = null;
                            Date end = null;
                            Date actualStart = null;
                            Date actualEnd = null;
                            
                            for (Iterator i=task.getAttendees().iterator(); i.hasNext();)
                            {
                                Assignee assignee = (Assignee)i.next();
                                Date sd = assignee.getStartDate();
                                if (sd != null && WormsUtil.before(sd, start))
                                {
                                    actualStart = sd;
                                    start = sd;
                                }
                                if (start != null && WormsUtil.before(start, projectStart.getTime()))
                                    start = projectStart.getTime();
                                else if (start != null && WormsUtil.after(start, projectEnd.getTime()))
                                    start = projectEnd.getTime();
                                Date cd = assignee.getCompleteDate();
                                if (cd != null && WormsUtil.after(cd, end))
                                {
                                    actualEnd = cd;
                                    end = cd;
                                }
                                if (end != null && WormsUtil.before(end, projectStart.getTime()))
                                    end = projectStart.getTime();
                                else if (end != null && WormsUtil.after(end, projectEnd.getTime()))
                                    end = projectEnd.getTime();
                            }
                            
                            Date taskEstEnd=actualEnd;
                            if(taskEstEnd==null){taskEstEnd=new Date();}
                            
                            // display progress
                            if (start != null)
                            {
                                Calendar rightFillerStart = Calendar.getInstance();
                                taskStart.setTime(start);
                                taskEnd.setTime((end != null) ? end : new Date());
                                rightFillerStart.setTime(taskEnd.getTime());
                                rightFillerStart.add(Calendar.MONTH, 1);

                                int leftOffset = WormsUtil.getMonthsBetween(taskStart.getTime(), projectStart.getTime());
                                if (leftOffset < 0)
                                    leftOffset = 0;
                                int taskDuration = WormsUtil.getMonthsBetween(taskEnd.getTime(), taskStart.getTime()) + 1;
                                int rightFiller = projectDuration-(leftOffset + taskDuration);

                                lFillerStart = projectStart.get(Calendar.MONTH) + 1;
                                rFillerStart = rightFillerStart.get(Calendar.MONTH) + 1;
                            %>
                            <tr id="m<c:out value="${milestoneIndex.index}"/>t<c:out value="${taskCount.index}"/>a">
                                <td width="2%" class="<c:out value="${className}"/>" valign="top"></td>
                                <td width="25%" class="<c:out value="${className}"/>" valign="top" nowrap></td>
								<td width="5%" class="<c:out value="${className}"/>" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
								
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
                                </c:if>
                                <%
                                    for(int i=0; i<leftOffset; i++)
                                    {
                                        if(lFillerStart == 13 || lFillerStart == 1)
                                        {
                                            jan = true;
                                            lFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        lFillerStart++;

                                %><c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>"></td>
                                    </c:if>
                                <% } %>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                
                                
                                <% 
                                if (!((actualStart.after(projectEnd.getTime())) || (actualEnd != null && actualEnd.before(start))) && taskEstEnd.after(projectEnd.getTime())) { 
                                	int taskDur=projectDuration-leftOffset+1;
                                    if (actualStart != null && actualStart.before(start))
                                    	taskDur=taskDur+1;
                                    else{
                                    	%><td width="5%" class="chartClassRow" valign="top" align="center" nowrap></td>
                                    	<%} %>
                                    <td class="chartClassRow" colspan="<%= taskDur %>">
                                    	<% }else{
                                    		if (actualStart != null && actualStart.before(start))
                                    			taskDuration=taskDuration+1;
                                            else{
                                    		%><td width="5%" class="chartClassRow" valign="top" align="center" nowrap></td>
                                    		<%} %><td class="chartClassRow" colspan="<%= taskDuration %>"><%
                                    	}
                                if (!((actualStart.after(projectEnd.getTime())) || (actualEnd != null && actualEnd.before(start)))) { 
                                	%>
                                    <table cellpadding="0" cellspacing="0" border="0" width="100%" height="3"/>
                                        <tr>
                                            <c-rt:set var="actualStartDate" value="<%= actualStart %>"/>
                                            <c-rt:set var="actualEndDate" value="<%= actualEnd %>"/>
                                            <th style="cursor:hand" title="<fmt:message key="project.label.actual"/>: <c:out value="${task.title}"/> (<fmt:formatDate pattern="${globalDateShort}" value="${actualStartDate}"/> - <fmt:formatDate pattern="${globalDateShort}" value="${actualEndDate}"/> - <fmt:formatNumber value="${task.overallProgress}" pattern="0"/>% <fmt:message key="project.label.completed"/>)" class="projectTableHeader">
                                                <c:if test="${(task.overallProgress > 0)}">
                                                    <table cellpadding="0" cellspacing="0" height="3" width="100%" align="left" valign="center">
                                                        <tr><td style="background: blue" class="projectTableRow"></td></tr>
                                                    </table>
                                                </c:if>
                                            </th>
                                        </tr>
                                    </table>
                                	<% } %>
                                </td>
                                </c:if>
                                <%
                                if (!(!((actualStart.after(projectEnd.getTime())) || (actualEnd != null && actualEnd.before(start))) && taskEstEnd.after(projectEnd.getTime()))) { 
                                    for(int i=0; i<rightFiller; i++)
                                    {
                                        if(rFillerStart == 13 || rFillerStart == 1)
                                        {
                                            jan = true;
                                            rFillerStart = 1;
                                        }
                                        else
                                            jan = false;
                                        rFillerStart++;
                                %><c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                                    <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>"></td>
                                    </c:if>
                                <% } %>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td class="chartClassRow" width="5%"valign="top" align="center" nowrap>
                        		<% if (actualStart.after(projectEnd.getTime())) { %>
                                    <table cellpadding="0" cellspacing="0" border="0" width="100%" height="3">
                                        <tr>
                                            <c-rt:set var="actualStartDate" value="<%= actualStart %>"/>
                                            <c-rt:set var="actualEndDate" value="<%= actualEnd %>"/>
                                            <th style="cursor:hand" title="<fmt:message key="project.label.actual"/>: <c:out value="${task.title}"/> (<fmt:formatDate pattern="${globalDateShort}" value="${actualStartDate}"/> - <fmt:formatDate pattern="${globalDateShort}" value="${actualEndDate}"/> - <fmt:formatNumber value="${task.overallProgress}" pattern="0"/>% <fmt:message key="project.label.completed"/>)" class="projectTableHeader">
                                                <c:if test="${(task.overallProgress > 0)}">
                                                    <table cellpadding="0" cellspacing="0" height="3" width="100%" align="left" valign="center">
                                                        <tr><td style="background: blue" class="projectTableRow"></td></tr>
                                                    </table>
                                                </c:if>
                                            </th>
                                        </tr>
                                    </table>
                                    <% } %></td> 
                        		</c:if>
                            <% }} %>
                        </c:forEach>
						<%-- Rendering meetings --%>
						<c:forEach items="${milestone.meetings}" var="meeting" varStatus="meetingIndex">
							<%
                                Meeting meeting = (Meeting) pageContext.getAttribute("meeting");
                                meetingStart.setTime(meeting.getStartDate());
                                meetingEnd.setTime(meeting.getEndDate());

								int workingDays = WormsUtil.getWorkingDays(project.getProjectWorking(), meetingStart.getTime(), meetingEnd.getTime());
								Collection events = new ArrayList();
								events.add(meeting.getEvent());
								Collection list = CalendarUtil.getRecurringEvents(meeting.getEvent(), projectStart.getTime(), projectEnd.getTime());
								events.addAll(list);

								Calendar now  = Calendar.getInstance();
								Calendar currentDate = Calendar.getInstance();
								currentDate.setTime((Date)pageContext.getAttribute("projectStart"));
								int currentDay = currentDate.get(Calendar.MONTH) + 1;
								jan = false;
								int loop = 0;
                            %>
							<tr id="m<c:out value="${milestoneIndex.index}"/>m<c:out value="${meetingIndex.index}"/>">
                                <td width="25%" class="projectClassRow" valign="top" nowrap><a href="#" onClick="displayMenu('task_<c:out value="${meeting.eventId}"/>'); return false;"><c:out value="${meeting.title}"/></a></td>								
                                <td width="5%" class="projectClassRow" valign="top" align="center" nowrap><fmt:formatDate value="${meeting.startDate}" pattern="ddMMMyyyy"/></td>
                                <td width="5%" class="projectClassRow" valign="top" align="center" nowrap><fmt:formatDate value="${meeting.endDate}" pattern="ddMMMyyyy"/></td>
                                <td width="5%" class="projectClassRow" valign="top" align="center" nowrap><%= workingDays %> </td>
                                <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                                <td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								<td width="5%" class="projectOpenRow" valign="top" align="center" nowrap></td>
								</c:if>
								<c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td class="chartClassRow">&nbsp;</td> 
                        		</c:if>  
                                <%
									for (Iterator i = events.iterator(); i.hasNext();)
									{
										/* Looping fillers */
										CalendarEvent event = (CalendarEvent) i.next();
                                        loop = WormsUtil.getMonthsBetween(event.getStartDate(), currentDate.getTime());
                                        for (int it = 0; it < loop; it++)
										{
											%> <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>">&nbsp;</td> <%
											currentDate.add(Calendar.MONTH, 1);
											if(currentDay >= 13 || currentDay == 1)
											{
												jan = true;
												currentDay = 1;
											}
											else
												jan = false;
											currentDay++;
										}
										if(WormsUtil.after(currentDate.getTime(), projectEnd.getTime()))
											break;
										/* Spanning events */
										int duration = WormsUtil.getMonthsBetween(event.getStartDate(), event.getEndDate());
										pageContext.setAttribute("meetingStart", event.getStartDate());
										pageContext.setAttribute("meetingEnd", event.getEndDate());
								%>
								<c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
									<td class="chartClassRow" colspan="<%= duration %>">
										<table cellpadding="1" cellspacing="0" border="0" width="100%" height="3"/>
											<tr>
												<th style="cursor:hand" title="<c:out value="${meeting.title}"/> (<fmt:formatDate pattern="${globalDateShort}" value="${meetingStart}"/> - <fmt:formatDate pattern="d MM" value="${meetingEnd}"/>)" class="projectTableHeader">
													<% if(WormsUtil.before(event.getEndDate(), now.getTime())) { %>
														<table cellpadding="0" cellspacing="0" height="100%" width="100%" align="left" valign="center">
															<tr><td class="projectTableRow">&nbsp;</td></tr>
														</table>
													<%} else {%>
														&nbsp;
													<%}%>
												</th>
											</tr>
										</table>
									</td>
									</c:if>
								<%
										currentDate.setTime(event.getEndDate());
										currentDate.add(Calendar.MONTH, 1);
										currentDay = currentDate.get(Calendar.MONTH) + 1;
										if(WormsUtil.after(currentDate.getTime(), projectEnd.getTime()))
											break;
									}
									if(WormsUtil.before(currentDate.getTime(), projectEnd.getTime()))
									{
                                        loop = WormsUtil.getMonthsBetween(currentDate.getTime(), projectEnd.getTime());
                                        for (int it = 0; it < loop; it++)
										{
											%> <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>">&nbsp;</td> <%
											currentDate.add(Calendar.MONTH, 1);
											if(currentDay >= 13 || currentDay == 1)
											{
												jan = true;
												currentDay = 1;
											}
											else
												jan = false;
											currentDay++;
										}
									}
								%>
								<c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td class="chartClassRow">&nbsp;</td> 
                        		</c:if>
							</td>
						</c:forEach>
						
                    </c:forEach>
                    <%-- Generating days --%>
                    <tr>
                        <td rowspan="2" colspan="4" class="projectClassRow" valign="top" nowrap>&nbsp;</td>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                        <td rowspan="2" colspan="6" class="projectOpenRow" valign="top" nowrap>&nbsp;</td>
                        </c:if>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td rowspan="2" class="chartClassRow">&nbsp;</td> 
                        		</c:if>
                        <%
                            dayOfWeek = projectStart.get(Calendar.MONTH) + 1;
                            tempStart.setTime(projectStart.getTime());
                            strDay = "";
                            for(int i=0; i<projectDuration; i++)
                            {
                                if(pageContext.getAttribute("projectStart") == null || pageContext.getAttribute("projectEnd") == null)
                                    strDay = "&nbsp;";
                                else
                                {
                                    switch(dayOfWeek)
                                    {
                                    case 2: strDay = "Feb"; jan = false; break;
                                    case 3: strDay = "Mar"; jan = false; break;
                                    case 4: strDay = "Apr"; jan = false; break;
                                    case 5: strDay = "May"; jan = false; break;
                                    case 6: strDay = "Jun"; jan = false; break;
                                    case 7: strDay = "Jul"; jan = false; break;
                                    case 8: strDay = "Aug"; jan = false; break;
                                    case 9: strDay = "Sep"; jan = false; break;
                                    case 10: strDay = "Oct"; jan = false; break;
                                    case 11: strDay ="Nov"; jan = false; break;
                                    case 12: strDay = "Dec"; jan = false; break;                                      
                                    default: strDay = "Jan"; jan = true; break;
                                    }
                                }
                                pageContext.setAttribute("tempDate", tempStart.getTime());
                        %>
                            <c:set var="fDate"><fmt:formatDate pattern="MMM" value="${tempDate}" /></c:set>
                            <c:set var="fDate2"><fmt:formatDate pattern="MMM yy" value="${tempDate}" /></c:set>
                            <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                            <td class="<% if(jan) {%>chartTableRow<%}else{%>chartClassRow<%}%>" align="center" style="cursor:hand" title="<c:out value="${fDate}"/>">
                                <span<c:if test="${fDate2 == today}"> class="highlight"</c:if>><%= strDay %></span>
                            </td>
                            </c:if>
                        <%
                                if(dayOfWeek == 13)
                                    dayOfWeek = 2;
                                else
                                    dayOfWeek++;

                                tempStart.add(Calendar.MONTH, 1);
                            }
                        %>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        		<td rowspan="2" class="chartClassRow">&nbsp;</td> 
                        		</c:if>
                    </tr>
                    <%-- END: Generating days --%>
                    <tr>
                    <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        <td width="63%" class="chartClassRow" valign="top" nowrap colspan="<%= projectDuration %>">
                            <table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td class="classTextSmall">&uarr;&nbsp;<fmt:formatDate value="${widget.properties[property_start_date]}" pattern="ddMMMyyyy"/></td>
                                    <td class="classTextSmall" align="right"><fmt:formatDate value="${widget.properties[property_end_date]}" pattern="ddMMMyyyy"/>&nbsp;&uarr;</td>
                                </tr>
                            </table>
                        </td>
                        </c:if>
                    </tr>
                    <tr>                    
                    <td colspan="8"class="projectTableRow">&nbsp;<b><fmt:message key="project.label.projectSummary"/></b></td>
                    <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                    <td colspan="<%= (projectDuration+2 ) %>" class="chartTableRow" valign="top"></td>
                    <td colspan="6"class="projectCloseRow"></td>
                    </c:if>
                    </tr>
                    <tr>
                        <td colspan="1" class="projectClassRowLabel" valign="top" nowrap><fmt:message key="project.label.overallProgress"/></td>                        
                        <td colspan="7" class="projectClassRow" valign="top" nowrap><fmt:formatNumber value="${widget.properties[property_progress]}" pattern="0.00"/>% <fmt:message key="project.label.completed"/></td>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='partial'}">
                        <td colspan="<%= (projectDuration+2 ) %>" class="chartClassRow" valign="top"></td>
                        </c:if>
                        <c:if test="${widget.viewType=='all' || widget.viewType=='hide'}">
                        <td colspan="6"class="projectOpenRow"></td>
                        </c:if>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</c:if>