<%@ page import="java.util.Calendar,
				kacang.Application"%>
<%@taglib uri='/WEB-INF/cewolf.tld' prefix='cewolf' %>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<jsp:include page="../form_header.jsp" flush="true"/>
<c:set var="report" value="${w.report}" />
<c:set var="project" value="${report.projects}" />
<table width="100%" cellpadding="5" cellspacing="0">
<tr class="contentBgColor">
    <td>
    	<strong><fmt:message key="project.label.projectPerformanceMetricReport"/></strong>
    </td>
    <td align="right">
    </td>
</tr>
<tr>
    <td class="contentBgColor" colspan="2">

		    <%
		        pageContext.setAttribute("today",Calendar.getInstance().getTime());
		    %>
	    	<fmt:message key="project.label.monthyear"/> : <fmt:formatDate value="${report.reportDate}" pattern="dd/MM/yyyy"/>
    </td>
</tr>

<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<c:if test="${w.summary}">
<tr>
	<td class="contentBgColor" colspan="2"><fmt:message key="project.label.projectSummary"/></td>
</tr>
<tr class="contentBgColor">
	<td colspan="2">
		<table width="100%" cellpadding="2" cellspacing="1">
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectName"/>	    
    </td>
    <td class="tableRow" valign="top">
    <c:out value="${project.projectName}"/>
    </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.Client"/>	    
    </td>
    <td class="tableRow" valign="top"><c:out value="${project.clientName}"/>   </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectValue"/>	      
    </td>
    <td class="tableRow" valign="top"><c:out value="${project.projectValue}"/> </td>
</tr>
<c:forEach items="${report.roles}" var="roles" varStatus="rolesIndex">
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
    <td class="tableRow" valign="top"><c:out value="${project.projectSummary}"/> </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectCurrentStatus"/>	      
    </td>
    <td class="tableRow" valign="top"><c:out value="${project.projectStatus}"/>	      
    </td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <fmt:message key="project.label.projectCurrentHighlights"/>	      
    </td>
    <td class="tableRow" valign="top">
    <c:out value="${project.currentHighlights}"/></td>
</tr>
</table>
<table width="100%" cellpadding="2" cellspacing="1">
<tr>
	<td class="contentBgColor" colspan="3">&nbsp;</td>
</tr>
<tr>
	<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"></td>
	<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold">
	<fmt:message key="project.label.schedule"/>
	</td>
	<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold">
	<fmt:message key="project.label.actual"/>	
	</td>
	<td bgcolor="#003366" class="contentTitleFont" width="20%" align="center" valign="top" style="font-weight:bold">
	<fmt:message key="project.label.varianDays"/>	
	</td>
</tr>
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    
    <fmt:message key="project.label.projectStartDate"/>	      
    </td>
    <td class="tableRow" align="right" valign="top"><fmt:formatDate value="${project.projectStartDate}" pattern="ddMMMyyyy"/></td>
    <td class="tableRow" align="right" valign="top">
    <c:out value="${project.actualProjectStartDate}"/>
    </td>
    <td class="tableRow" align="right" valign="top">  
    <c:choose>
  <c:when test="${project.startVariance == 'Not Started'}">
  <c:out value="${project.startVariance}"/>
  </c:when>
  <c:otherwise>
  	<c:if test="${project.startVariance >0.0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${project.startVariance <0.0}">
        <font color="#347235">
    </c:if>
    
    
    <c:out value="${project.startVariance}"/>
    <c:if test="${project.startVariance >0.0||project.startVariance <0.0}">
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
    <td class="tableRow" align="right" valign="top">
    
    <fmt:formatDate value="${project.projectEndDate}" pattern="ddMMMyyyy"/>
    </td>
    <td class="tableRow" align="right" valign="top"><c:out value="${project.actualProjectEndDate}"/></td>  
    <td class="tableRow" align="right" valign="top"> 
    <c:choose>
  <c:when test="${project.endVariance == 'Not Started'}">
  <c:out value="${project.endVariance}"/>
  </c:when>
  <c:otherwise>
  	<c:if test="${project.endVariance >0.0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${project.endVariance <0.0}">
        <font color="#347235">
    </c:if>
    <c:if test="${project.actualProjectEndDate == 'Ongoing'}">
    *</c:if>
    <c:out value="${project.endVariance}"/>
    <c:if test="${project.endVariance >0.0||project.endVariance <0.0}">
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
    <c:if test="${project.estDuration >0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${project.estDuration <0}">
        <font color="#347235">
    </c:if>         
    <c:out value="${project.estDuration}"/>
    <c:if test="${project.estDuration >0||project.estDuration <0}">
        </font>
    </c:if> 
    
    </td>
    <td class="tableRow" align="right" valign="top">
    <c:choose>
  <c:when test="${project.actDuration == 'Not Started'}">
  <c:out value="${project.actDuration}"/>
  </c:when>
  <c:otherwise>
    <c:if test="${project.actDuration >0}">
        <font color="#ff0000">
    </c:if>
    <c:if test="${project.actDuration <0}">
        <font color="#347235">
    </c:if>
    <c:if test="${project.actualProjectEndDate == 'Ongoing'}">
    *</c:if>
    <c:out value="${project.actDuration}"/>
    <c:if test="${project.actDuration >0 ||project.actDuration <0}">
    </font>
    </c:if>
    </c:otherwise>
    </c:choose> 
    
    
    </td>
    <td class="tableRow" align="right" valign="top">
    </td>
</tr>
</table>
</td></tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr><tr>
	<td class="contentBgColor" colspan="2" align="center">
<c:set var="schedule1" value="${project.estDuration}" scope="page"/>
<c:set var="schedule2" value="${project.actDuration}" scope="page"/>
<c:if test="${schedule2 !='Not Started'}">
<jsp:useBean id="schedulepageViews" class="com.tms.collab.project.ui.ProjectReportChart"/>
<cewolf:chart id="schedulechartline" title="Schedule" yaxislabel="days" type="verticalbar" showlegend="true">  
<cewolf:data>       
	<cewolf:producer id="schedulepageViews">  
	<cewolf:param name="value" value="<%= new String(pageContext.findAttribute("schedule1")+",,"+pageContext.findAttribute("schedule2")) %>"/>
	<cewolf:param name="name" value="<%= new String(Application.getInstance().getResourceBundle().getString("project.label.schedule")+",,"+Application.getInstance().getResourceBundle().getString("project.label.actual")) %>"/>		
	</cewolf:producer>  
</cewolf:data>
</cewolf:chart>
<cewolf:img chartid="schedulechartline" renderer="cewolf" width="350" height="250">  
</cewolf:img>
</c:if>
</td></tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
</c:if>
<c:if test="${w.schedule}">
<tr>
	<td class="contentBgColor" colspan="2"><fmt:message key="project.label.scheduleVariance"/>	 </td>
</tr>
<tr class="contentBgColor">
	<td colspan="2">
		<table width="100%" cellpadding="2" cellspacing="1">
		<tr>
				<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"><fmt:message key="project.label.milestone"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estStart"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estEnd" /></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" width="8%" style="font-weight:bold"><fmt:message key="project.label.estDuration" /></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actStart" /></td>				
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actEnd" /></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" width="8%" style="font-weight:bold"><fmt:message key="project.label.actDuration" /></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" width="8%" style="font-weight:bold"><fmt:message key="project.label.varianceDays" /></td>
			</tr>
<c:forEach items="${report.milestones}" var="milestone">
<tr class="contentBgColor">
    <td class="tableRow" valign="top" style="font-weight:bold">	
    <c:out value="${milestone.milestoneName}"/>    
    </td>
    <td class="tableRow" align="right" valign="top"><c:out value="${milestone.estStartDate}"/>
    </td>
    <td class="tableRow" align="right" valign="top"><c:out value="${milestone.estEndDate}"/>
    </td>
    <td class="tableRow" align="right" valign="top"><c:out value="${milestone.estVariance}"/>    
    </td>
    <td class="tableRow" align="right" valign="top"><c:out value="${milestone.actStartDate}"/> 
    </td>    
    <td class="tableRow" align="right" valign="top"><c:out value="${milestone.actEndDate}"/>
    </td>
    <td class="tableRow" align="right" valign="top"><c:out value="${milestone.actVariance}"/>
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
    <c:if test="${milestone.actEndDate == 'Ongoing'}">
    *
    </c:if>
    <c:out value="${milestone.variance}"/>  
    <c:if test="${milestone.variance > 0.0 || milestone.variance<0.0}">
        </font>
    </c:if>  
    </c:otherwise>
    </c:choose> 
    </td>
</tr>
</c:forEach>
</table>
</td></tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
</c:if>
<c:if test="${w.effort}">
<tr>
	<td class="contentBgColor" colspan="2"><fmt:message key="project.label.effortVariance"/></td>
</tr>
<tr class="contentBgColor">
	<td colspan="2">
		<table width="100%" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"><fmt:message key="project.label.activity"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estimatedMandays"/></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actualMandays" /></td>
				<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.variance" /></td>
			</tr>
			<c:forEach items="${report.tasks}" var="task">
			<c:set var="taskindex" value="${task}" />
				<tr>					
					<td class="tableRow" style="font-weight:bold">
						<c:out value="${task.taskName}"/>
					</td>
					<td align="right" class="tableRow">
						<fmt:formatNumber value="${task.estimatedMandays}" maxFractionDigits="2" pattern="#0.00" />
					</td>
					<td align="right" class="tableRow" >
						<c:out value="${task.actualMandays}"/>
					</td>
					<td align="right" class="tableRow">
						<c:if test="${task.variance > 0.00}">
        					<font color="#ff0000">
    					</c:if>
    					<c:if test="${task.variance <0.00}">
        					<font color="#347235">
    					</c:if>
    					<c:out value="${task.variance}"/>  
    					<c:if test="${task.variance > 0.00||task.variance<0.00}">
        					</font>
    					</c:if> 
					</td>
				</tr>
			</c:forEach>		
		</table>
	</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr><tr>
	<td class="contentBgColor" colspan="2" align="center">
<c:set var="valuecollection" value="${taskindex}" scope="page"/>
<c:set var="value1" value="${valuecollection.estimatedMandays}" scope="page"/>
<c:set var="value2" value="${valuecollection.actualMandays}" scope="page"/>
<jsp:useBean id="BeanpageViews" class="com.tms.collab.project.ui.ProjectReportChart"/>
<cewolf:chart id="chartline" title="Effort" yaxislabel="Mandays" type="verticalbar" showlegend="true">  
<cewolf:data>        
	<cewolf:producer id="BeanpageViews">  
	<cewolf:param name="value" value="<%= new String(pageContext.findAttribute("value1")+",,"+pageContext.findAttribute("value2")) %>"/>
	<cewolf:param name="name" value="<%= new String(Application.getInstance().getResourceBundle().getString("project.label.estimatedMandays")+",,"+Application.getInstance().getResourceBundle().getString("project.label.actualMandays")) %>"/>		
	</cewolf:producer>  
</cewolf:data>
</cewolf:chart>
<cewolf:img chartid="chartline" renderer="cewolf" width="350" height="250"/>  
</td>
</tr> 
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
</c:if>

<c:if test="${w.defects}">
<tr>
	<td class="contentBgColor" colspan="2"><fmt:message key="project.label.defectVariance"/></td>
</tr>
<tr class="contentBgColor">
	<td colspan="2">
		<table width="100%" cellpadding="2" cellspacing="1">
		<tr>
			<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"></td>
			<c:forEach items="${report.defects}" var="defects">
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold">
			<c:out value="${defects.defectTypeName}"/></td>
			</c:forEach>
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.resolved"/></td>
			<c:forEach items="${report.defects}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<c:out value="${defects.resolved}"/>
			</td>
			</c:forEach>			
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.unresolved"/></td>
			<c:forEach items="${report.defects}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<c:out value="${defects.unresolved}"/>		
			</td>
			</c:forEach>			
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.total"/></td>
			<c:forEach items="${report.defects}" var="defects">
			<c:if test="${defects.defectTypeName !='Total'}">
			<c:set var="defectsName1" value="${defectsName1}${defects.defectTypeName},," />
			<c:set var="defectsTotal1" value="${defectsTotal1}${defects.total},," />
			</c:if>
			<td class="tableRow" align="right" valign="top">
			<c:out value="${defects.total}"/>	
			</td>
			</c:forEach>			
		</tr>
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold"><fmt:message key="project.label.severity"/></td>
			<c:forEach items="${report.defects}" var="defects">
			<td class="tableRow" align="right" valign="top">
			<c:if test="${defects.defectTypeName !='Total'}">
			<fmt:formatNumber value="${defects.severity}" maxFractionDigits="2" pattern="#0.00" />
			</c:if>
			</td>
			</c:forEach>			
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr><tr>
	<td class="contentBgColor" colspan="2" align="center">
<jsp:useBean id="DefectspageViews" class="com.tms.collab.project.ui.ProjectReportPie"/>
<cewolf:chart id="Defectschartline" title="Defects" type="pie" showlegend="true">  
<cewolf:data>        
	<cewolf:producer id="DefectspageViews">  
	<cewolf:param name="value" value="<%= new String(pageContext.findAttribute("defectsTotal1")+"") %>"/>
	<cewolf:param name="name" value="<%= new String(pageContext.findAttribute("defectsName1")+"") %>"/>		
	</cewolf:producer>  
</cewolf:data>
</cewolf:chart>
<cewolf:img chartid="Defectschartline" renderer="cewolf" width="350" height="250"/>  
</td></tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
</c:if>

<c:if test="${w.cost}">
<tr>
	<td class="contentBgColor" colspan="2"><fmt:message key="project.label.projectCost"/></td>
</tr>
<tr class="contentBgColor">
	<td colspan="2">
		<table width="100%" cellpadding="2" cellspacing="1">
		<tr>
			<td bgcolor="#003366" class="contentTitleFont" valign="top" style="font-weight:bold"></td>
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.estimated"/></td>
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.actualToDate" /></td>
			<td bgcolor="#003366" class="contentTitleFont" align="center" valign="top" style="font-weight:bold"><fmt:message key="project.label.variance" /></td>
		</tr>
		<c:forEach items="${report.cost}" var="cost">
		<c:set var="costindex" value="${cost}" />
		<tr>
			<td class="tableRow" valign="top" style="font-weight:bold">
			<c:out value="${cost.costName}"/>	
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:out value="${cost.estimated}"/>	
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:out value="${cost.actual}"/>
			</td>
			<td class="tableRow" align="right" valign="top">
			<c:if test="${cost.variance > 0.00}">
        		<font color="#ff0000">
    		</c:if>
    		<c:if test="${cost.variance <0.00}">
        		<font color="#347235">
    		</c:if>
    		<c:out value="${cost.variance}"/>  
    		<c:if test="${cost.variance > 0.00||cost.variance<0.00}">
        		</font>
    		</c:if> 
			</td>
		</tr>
		</c:forEach>		
		
		</table>
	</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr><tr>
	<td class="contentBgColor" colspan="2" align="center">
<c:set var="costcollection" value="${costindex}" scope="page"/>
<c:set var="cost1" value="${costcollection.estimated}" scope="page"/>
<c:set var="cost2" value="${costcollection.actual}" scope="page"/>
<jsp:useBean id="CostpageViews" class="com.tms.collab.project.ui.ProjectReportChart"/>
<cewolf:chart id="Costchartline" title="Costs" type="verticalbar" showlegend="true">  
<cewolf:data>        
	<cewolf:producer id="CostpageViews">  
	<cewolf:param name="value" value="<%= new String(pageContext.findAttribute("cost1")+",,"+pageContext.findAttribute("cost2")) %>"/>
	<cewolf:param name="name" value="<%= new String(Application.getInstance().getResourceBundle().getString("project.label.estimated")+",,"+Application.getInstance().getResourceBundle().getString("project.label.actualToDate")) %>"/>		
	</cewolf:producer>  
</cewolf:data>
</cewolf:chart>
<cewolf:img chartid="Costchartline" renderer="cewolf" width="350" height="250">  
</cewolf:img>
</td></tr>
</c:if>
<tr class="contentBgColor">
    <td colspan="2" style="color : red"><br>
	* <fmt:message key="project.label.figures"/>
    </td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>