<%@ page import="kacang.Application,
                 org.apache.commons.collections.SequencedHashMap,
                 java.util.*,
                 java.text.SimpleDateFormat,
                 kacang.ui.WidgetManager,
                 kacang.services.security.User,
                 com.tms.hr.recruit.model.RecruitDao,
                 com.tms.hr.recruit.model.RecruitModule,
                 java.io.PrintWriter"%>
<%@ include file="/common/header.jsp" %>

<script src="<c:url value="/common/tree/tree.js"/>"></script>

<% 
Application app = Application.getInstance();
RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
Collection vacancyTotalCol = rm.findAllVacancyTotal("a.vacancyCode", false, 0, -1, "", "", "", "", "", "", "");
String amountDate = app.getProperty("com.tms.hr.recruit.ui.closeDate");
int realAmountDate = Integer.parseInt(amountDate);

Date getDate = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

Calendar cal = Calendar.getInstance();
for(Iterator ite=vacancyTotalCol.iterator(); ite.hasNext();){
	HashMap map1= (HashMap)ite.next();
	Date endDate = (Date) map1.get("endDate");
	cal.setTime(endDate);
	//cal.add(Calendar.DATE, realAmountDate);
	cal.add(cal.DATE, realAmountDate);
	if(sdf.format(getDate).equals(sdf.format(cal.getTime())) || getDate.before(cal.getTime()))
	//if(getDate.after(cal.getTime()))
		map1.put("expire","0");
	else
		map1.put("expire","1");

}

pageContext.setAttribute("vacancyTotalCol", vacancyTotalCol);
%>

<!--
<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor">
<tr>
<td>

	<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground">
		<tr>
			<td>
			
					<table width="100%" cellpadding="3" cellspacing="1" border='0'>
					
						<tr>
				        	<td class="classRowLabel" valign="top" align="left" colspan="2"><fmt:message key='recruit.menu.label.vacancySummary' /></td>			        	
				    	</tr>
						
						<c:forEach items="${vacancyTotalCol}" var="coll">
						<tr>
				        	<td class="classRowLabel" valign="top" align="left" colspan="2">	
				        		<a href="recruit/vacancyReportListView.jsp?vacancyCode=<c:out value='${coll.vacancyCode}' />" >
				        			<c:out value='${coll.vacancyCode} (${coll.positionDesc})' />
				        		</a>
				        	</td>
				        </tr>
				        
				        <tr>	
				        	<td>&nbsp;</td>
				        	<td class="classRow">
				        		<c:choose>
					        		<c:when test="${coll.noOfPositionOffered != null}" >
					        			<li><fmt:message key='recruit.general.label.noOfPosition' /> : <c:out value="${coll.noOfPositionOffered} / ${coll.noOfPosition}" /></li>
			                    	</c:when>
			                    	<c:otherwise>
			                    		<li><fmt:message key='recruit.general.label.noOfPosition' /> : <c:out value="0 / ${coll.noOfPosition}" /></li>
			                    	</c:otherwise>
		                    	</c:choose>
			                        <li><fmt:message key='recruit.general.label.totalViewed' /> :  <c:out value='${coll.totalViewed}' /></li>
			                        <li><fmt:message key='recruit.general.label.totalApplied' /> :  <c:out value='${coll.totalApplied}' /></li>
			                        <li><fmt:message key='recruit.general.label.totalShortlisted' /> :  <c:out value='${coll.totalShortlisted}' /></li>
			                        <li><fmt:message key='recruit.general.label.totalScheduled' /> :  <c:out value='${coll.totalScheduled}' /></li>
			                        <li><fmt:message key='recruit.general.label.totalJobOffered' /> :  <c:out value='${coll.totalJobOffered}' /></li>
			                        <li><fmt:message key='recruit.general.label.totalJobAccepted' /> :  <c:out value='${coll.totalJobAccepted}' /></li>
			                        <li><fmt:message key='recruit.general.label.totalJobRejected' /> :  <c:out value='${coll.totalJobRejected}' /></li>
			                        <li><fmt:message key='recruit.general.label.totalBlackListed' /> :  <c:out value='${coll.totalBlackListed}' /></li>
				        	</td>
				    	</tr>
				    	
						</c:forEach>
						
					</table>
			
			</td>
		</tr>
	</table>		

</td>
</tr>
</table>
-->

<!--Start Outer table #1 -->
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="bookmarkRow" align="center">
			
			<!--Start inner table #2 -->
            <table cellpadding="2" cellspacing="1" width="95%">
                <tr>
                  <td class="bookmarkRow">
                  
                    <p>
                    <b><fmt:message key='recruit.menu.label.vacancySummary' /></b>:
                    <hr size="1">
                   
                    <table width="100%">
                    <c:forEach items="${vacancyTotalCol}" var="coll">
                    <c:set var="id" value="vac_${coll.vacancyCode}"/>
                    <c:set var="icon" value="icon_${id}"/>
                    <c:set var="count" value="${1}"/>
                 
                    <c:if test="${coll.expire eq '0'}" > 	 
                        <tr>
	                        <td width="20" nowrap>
	                            [<a href="#" onclick="treeToggle('<c:out value="${id}"/>'); return false"><span id="<c:out value="${icon}"/>"><c:choose><c:when test="${count > 0}">+</c:when><c:otherwise>-</c:otherwise></c:choose></span></a>]
	                        </td>
	                        <td width="100%">
	                      		 <a href="recruit/vacancyReportListView.jsp?vacancyCode=<c:out value='${coll.vacancyCode}' />" >
				        			<c:out value='${coll.vacancyCode} (${coll.positionDesc})' />
				        		</a>
	                        </td>
                        </tr>
                        
                        <tr>
                        	<td></td>
                        	<td>
	                       		<div id="<c:out value="${id}"/>" style="display: none">
		                            <c:if test="${count > 0}">
		                            
		                                <script> 
		                                    treeLoad('<c:out value="${id}"/>');
		                                </script>
		                             
		                                <c:choose>
							        		<c:when test="${coll.noOfPositionOffered != null}" >
							        			<li><fmt:message key='recruit.general.label.noOfPosition' /> : <c:out value="${coll.noOfPositionOffered} / ${coll.noOfPosition}" /></li>
					                    	</c:when>
					                    	<c:otherwise>
					                    		<li><fmt:message key='recruit.general.label.noOfPosition' /> : <c:out value="0 / ${coll.noOfPosition}" /></li>
					                    	</c:otherwise>
				                    	</c:choose>
					                        <li><fmt:message key='recruit.general.label.totalViewed' /> :  <c:out value='${coll.totalViewed}' /></li>
					                        <li><fmt:message key='recruit.general.label.totalApplied' /> :  <c:out value='${coll.totalApplied}' /></li>
					                        <li><fmt:message key='recruit.general.label.totalShortlisted' /> :  <c:out value='${coll.totalShortlisted}' /></li>
					                        <li><fmt:message key='recruit.general.label.totalScheduled' /> :  <c:out value='${coll.totalScheduled}' /></li>
					                        <li><fmt:message key='recruit.general.label.totalJobOffered' /> :  <c:out value='${coll.totalJobOffered}' /></li>
					                        <li><fmt:message key='recruit.general.label.totalJobAccepted' /> :  <c:out value='${coll.totalJobAccepted}' /></li>
					                        <li><fmt:message key='recruit.general.label.totalJobRejected' /> :  <c:out value='${coll.totalJobRejected}' /></li>
					                        <li><fmt:message key='recruit.general.label.totalBlackListed' /> :  <c:out value='${coll.totalBlackListed}' /></li>
		                            </c:if>
	                        	</div>
                       		 </td>
                        </tr>
                    </c:if>    
                    </c:forEach>    
                    </table>
                   
                  </td>
                </tr>
            </table>
            <!--End inner table #2 -->
            
        </td>
    </tr>
</table>
<!--End Outer table #1 -->


	