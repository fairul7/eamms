<%@ page import="java.util.Calendar,
                 java.util.Date,
                 com.tms.ekms.manpowertemp.model.ManpowerLeaveObject,
                 kacang.Application"%>
<%@ include file="/common/header.jsp" %>
 <c:set var="form" value="${widget}"/>
 <c:set var="defaultprofile" value="${widget.defaultWorkProfile}"/>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table cellpadding="2" cellspacing="1">
        
<c:if test="${!form.invalid && !empty form.report}">
   <tr>
        <td colspan="2"><hr/></td>
        
    </tr>
    <table cellpadding="0" cellspacing="0" width="95%" align="center" border="0">
        <tr>
            <td class="tableRow">
                <c:set var="reportStarts" value="${widget.startDate.date}" scope="page"/>
                <c:set var="reportEnds" value="${widget.endDate.date}" scope="page"/>
                <%
                    Calendar reportStarts = Calendar.getInstance();
                    Calendar reportEnds = Calendar.getInstance();
                    Date dateReportStart = (Date)pageContext.getAttribute("reportStarts");
                    Date dateReportEnd = (Date)pageContext.getAttribute("reportEnds");
                    reportStarts.setTime(dateReportStart);
                    reportEnds.setTime(dateReportEnd);
                    int reportDuration = reportEnds.get(Calendar.DAY_OF_YEAR)-reportStarts.get(Calendar.DAY_OF_YEAR)+1+((reportEnds.get(Calendar.YEAR)-reportStarts.get(Calendar.YEAR))*365);

                    Calendar taskStarts = Calendar.getInstance();
                    Calendar taskEnds = Calendar.getInstance();
                %>
                <table width="100%" border="0" cellspacing="1" cellpadding="0" class="borderTable">
                
                    <tr>
                    <td width="78%" class="tableHeader" valign="top" colspan="<%= reportDuration %>"><b><fmt:message key='fms.label.transport.viewMyDutyRoster'/></b></tr>

                    
					<%-- Generating days --%>
                    <tr>
												
                        <c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
                        <c:set var="today"><fmt:formatDate pattern="d MMM" value="${currentDate}" /></c:set>
                        <%
							Calendar tempStart = Calendar.getInstance();
                            int dayOfWeek = reportStarts.get(Calendar.DAY_OF_WEEK);
                            tempStart.setTime(reportStarts.getTime());
                            String strDay = "&nbsp;";
							boolean sunday = false;
                            for(int i=0; i<reportDuration; i++)
                            {
								switch(dayOfWeek)
								{
									case 2: strDay = Application.getInstance().getMessage("general.label.mon"); sunday = false; break;
                                        case 3: strDay = Application.getInstance().getMessage("general.label.tue"); sunday = false; break;
                                        case 4: strDay = Application.getInstance().getMessage("general.label.wed"); sunday = false; break;
                                        case 5: strDay = Application.getInstance().getMessage("general.label.thu"); sunday = false; break;
                                        case 6: strDay = Application.getInstance().getMessage("general.label.fri"); sunday = false; break;
                                        case 7: strDay = Application.getInstance().getMessage("general.label.sat"); sunday = false; break;
                                        default: strDay = Application.getInstance().getMessage("general.label.sun"); sunday = true; break;
								}
                                pageContext.setAttribute("tempDate", tempStart.getTime());
                        %>
                            <c:set var="fDate"><fmt:formatDate pattern="dd" value="${tempDate}" /></c:set>                            
                            <c:set var="longDate"><fmt:formatDate value="${tempDate}" pattern="EEE, d MMM yyyy" /></c:set>                      
                            
                            <th class="tableHeader" align="center" style="cursor:hand" title="<c:out value="${longDate}"/>">
                                <span<c:if test="${fDate == today}"> class="today"</c:if> <% if(sunday) {%> class="highlight"<%} %>><c:out value="${fDate}"/>&nbsp;<%= strDay %></span>
                            </th>
                        <%
                                if(dayOfWeek == 8)
                                    dayOfWeek = 2;
                                else
                                    dayOfWeek++;

                                tempStart.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        %>
                    </tr>
                    <%-- END: Generating days --%>
                    
                    </tr>
                    
                    	<%String wp = ""; %>
                                            
	                    <c:forEach items="${form.report}" var="record" varStatus="status">	    
	                     						
						  <tr style="<c:out value="${style}"/>">                
	                        
		                             
		                        <%
		                        tempStart.setTime(reportStarts.getTime());
		                        for(int i=0; i<reportDuration; i++)
		                            {								
		                                pageContext.setAttribute("tempDate", tempStart.getTime());
		                        %>
		                        
		                        
		                       <c:set var="fDate"><fmt:formatDate pattern="d MMM" value="${tempDate}" /></c:set>
		                        <td class="classRow" align="center" style="<c:out value="${style}"/>" title="<c:out value="${record.key.name}"/>">
		                        
		                        	<%wp = "No";%>
		                        	
		                            <c:forEach items="${record.value}" var="duty">                           			                            		
										<c:set var="sDate"><fmt:formatDate pattern="d MMM" value="${duty.startDate}" /></c:set>
										<c:set var="eDate"><fmt:formatDate pattern="d MMM" value="${duty.endDate}" /></c:set>
										<c:set var="taskStart"><fmt:formatDate pattern="hh:mm:a" value="${duty.startDate}" /></c:set>
										<c:set var="taskEnd"><fmt:formatDate pattern="hh:mm:a" value="${duty.endDate}" /></c:set>									                                                                    
										<c:set var="title" value="${duty.requestTitle}" />
										<c:set var="destination" value="${duty.destination}" />
										<c:set var="id" value="${duty.id}" />
										<c:set var="workProfile" value="${duty.workingProfileCode}" />
																																								
										<c:set var="dutyStarts" value="${duty.workStart}" scope="page"/>
                                    	<c:set var="dutyEnds" value="${duty.workEnd}" scope="page"/>	
                                    	
                                    	<c:set var="startAssgs" value="${duty.startDate}" scope="page"/>
                                    	<c:set var="endAssgs" value="${duty.endDate}" scope="page"/>		
                                    							
                                        <c:set var="manpowerLeaveObject" value="${duty.manpowerLeaveObject}" />
                                        <c:set var="sLeave"><fmt:formatDate pattern="d MMM" value="${manpowerLeaveObject.startDate}" /></c:set>
                                        <c:set var="eLeave"><fmt:formatDate pattern="d MMM" value="${manpowerLeaveObject.endDate}" /></c:set>
                                                                                                                          
                                        <%                                       
                                        //Working Profile					                    	
					                    Date dutyStart = null;
                                        Date dutyEnd = null;
					                   	try{			                   
											if( !(null == pageContext.getAttribute("dutyStarts") ||
												null == pageContext.getAttribute("dutyEnds")) ){
		                                        dutyStart = (Date)pageContext.getAttribute("dutyStarts");
		                                        dutyEnd = (Date)pageContext.getAttribute("dutyEnds");		                                       
	                                        }
					                   	}catch(Exception er){}
										
										//Assignment
					                    Calendar startAssg = Calendar.getInstance();
					                    Calendar endAssg = Calendar.getInstance();	
					                    Date sDateAssg = null;
                                        Date eDateAssg = null;
					                	try{																			
											if( !(null == pageContext.getAttribute("startAssgs") ||
													null == pageContext.getAttribute("endAssgs")) ){
												sDateAssg = (Date)pageContext.getAttribute("startAssgs");
		                                        eDateAssg = (Date)pageContext.getAttribute("endAssgs");
		                                              
		                                        startAssg.setTime(sDateAssg);
		                                        startAssg.set(Calendar.HOUR_OF_DAY, 00);
		                                        startAssg.set(Calendar.MINUTE, 00);
		                                        startAssg.set(Calendar.SECOND, 00);
		                                        startAssg.set(Calendar.MILLISECOND, 000000);
		                                        
		                                        endAssg.setTime(eDateAssg);		                                       
		                                        endAssg.set(Calendar.HOUR_OF_DAY, 23);
		                                        endAssg.set(Calendar.MINUTE, 59);
		                                        endAssg.set(Calendar.SECOND, 59);
		                                        										
											}
					                	}catch(Exception er){}
										
										//Leave
										Calendar calLeaveS = Calendar.getInstance();
					                    Calendar calLeaveE = Calendar.getInstance();						                    
										ManpowerLeaveObject leaveObj = new ManpowerLeaveObject();	
										leaveObj = null;
										String leave = "";
										Date startLeave = null;
										Date endLeave = null;										
										
										try{										 
										 leaveObj = (ManpowerLeaveObject)pageContext.getAttribute("manpowerLeaveObject");
										 if(null != leaveObj){
											 
											leave = leaveObj.getLeaveType();
											startLeave = leaveObj.getStartDate();
											endLeave = leaveObj.getEndDate();
											
											if(!(startLeave == null || endLeave == null)){
												calLeaveE.setTime(endLeave);
												calLeaveE.add(Calendar.DAY_OF_MONTH, 1);      //+1 day bcoz we use .before ~ <=                                                                  
												calLeaveS.setTime(startLeave);
												calLeaveS.add(Calendar.DAY_OF_MONTH, -1);   //-1 day bcoz we use .after ~ >=	
											}								
										 }										 
										 
										}catch(Exception e){System.out.print("Erores at viewDuty > leaveObj:"+e);}
						                    
                                        %>
                                                               
										<span >											            
									            <c:if test="${not empty(sLeave)}">									            
												 <%if(tempStart.getTime().compareTo(endLeave)<=0 && tempStart.getTime().compareTo(startLeave) >= 0){%>									                        									            											            		                        	
											              	<b><font color='red'><%=leave %></font></b>
											    <%wp="Yes"; } %>      																	           
									            </c:if>													            						              
									    																									            
									            <c:if test="${not empty(dutyStarts)}">				            	
									            <%if(tempStart.getTime().compareTo(dutyEnd)<=0 && tempStart.getTime().compareTo(dutyStart) >= 0){%>								                        									            											            		                        	
											              	<span class="portletBody" style="<c:out value="${style}"/>" ><c:out value="${workProfile}"/></span>
											    <%wp="Yes"; } %>      																	           
									            </c:if>			
									            									            
									            <c:if test="${not empty(startAssgs)}">	 														
												<%if(tempStart.getTime().compareTo(endAssg.getTime())<=0 && tempStart.getTime().compareTo(startAssg.getTime()) >= 0 ){%>     	
											             	<c:out value="${workProfile}"/>	<br>
															<c:out value="${taskStart}"/> - <c:out value="${taskEnd}"/>
															<c:if test="${not empty(destination)}">
																<br/>[<c:out value="${destination}"/>]
															</c:if>
 																							
															<li><a href="viewAssignment.jsp?id=<c:out value="${id}"/> &userId=<c:out value="${record.key.id}"/>" class="calendarWeeklink">										              		
																<c:out value="${title}"/>&nbsp;
															</a></li><br/> 				
											    <%wp="Yes"; } %>  																																					           
												</c:if>																									            					          				
										</span>			
																													
		                           </c:forEach>		 
		                           		          
		                           <%if(!("Yes".equals(wp))){ %>
		                           	<span class="portletBody" style="<c:out value="${style}"/>" ><c:out value="${defaultprofile}"/></span>       
		                           <%} %>   
		                                        
		                        </td>	
		                        
		                        	                            
		                        <%
		                                if(dayOfWeek == 8)
		                                    dayOfWeek = 2;
		                                else
		                                    dayOfWeek++;
		
		                                tempStart.add(Calendar.DAY_OF_MONTH, 1);
		                            }
		                        %>
		                        
		                        </tr>
		                </c:forEach>		                
		                
	                        <tr>
	                        
                    	</tr>
                    
                    
                </table>
            </td>
        </tr>
    </table>
</c:if>

<jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
</td>
</tr>	
</table>


