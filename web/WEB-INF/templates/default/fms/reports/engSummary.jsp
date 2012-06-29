<%@ page import="java.util.*,                
                 com.tms.ekms.manpowertemp.model.ManpowerLeaveObject,    
                 java.text.DecimalFormat,
                 java.text.NumberFormat,             
                 kacang.Application"%>
<%@ include file="/common/header.jsp" %>



<%@page import="org.apache.commons.lang.ArrayUtils"%><c:set var="form" value="${widget}"/>
 <jsp:include page="../../form_header.jsp" flush="true"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table align="center" cellpadding="2" cellspacing="1" width="100%">    
    <tr>
        <td class="classRowLabel" align="right"><fmt:message key='fms.tran.table.dateFrom'/></td>
        <td class="classRow"><x:display name="${form.startDate.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" align="right"><fmt:message key='fms.tran.table.dateTo'/></td>
        <td class="classRow"><x:display name="${form.endDate.absoluteName}"/></td>
    </tr>
    <tr>
       <td class="classRowLabel" align="right"><fmt:message key='fms.report.message.label.serviceType'/>*</td>
        <td>
            <x:display name="${form.sbServiceType.absoluteName}"/>
            
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" align="right"><fmt:message key='fms.report.message.label.department'/></td>
        <td>
            <x:display name="${form.sbDepartment.absoluteName}"/>
           
        </td>
    </tr>
    <tr>
       <td class="classRowLabel" align="right"><fmt:message key='fms.report.message.label.programmed'/></td>
        <td>
            <x:display name="${form.sbProgram.absoluteName}"/>
            
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <x:display name="${form.submit.absoluteName}"/>
            <x:display name="${form.exportExcel.absoluteName}"/>
        </td>
    </tr>
	
	
	
	<c:if test="${!form.invalid && !empty form.report}">
	 	<tr>
			<td colspan="2" ><hr/></td>
		</tr>
			
 
    		<table cellpadding="0" cellspacing="1" width="95%" align="left" border="0">
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
								<td class="headerBgColor" colspan="5" valign="center" width="20%">
									<b>
										<c:choose>
											<c:when test="${widget.serviceType eq '4'}">
												<fmt:message key='fms.label.manpower'/>
											</c:when>
											<c:otherwise>
												<fmt:message key='fms.facility.label.facility'/>
											</c:otherwise>
										</c:choose>
											
									</b></td>
                    			
                    			<td width="58%" class="headerBgColor" valign="top" colspan="<%= reportDuration %>">
                    				<b><fmt:message key='fms.report.message.label.bookingDate'/></b>
                    			</td>
							</tr>
							
							
                    		<tr>
								<th colspan="5" class="headerBgColor" align="left">&nbsp;</th>													
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
	                            <th class="headerBgColor" align="center" style="cursor:hand" title="<c:out value="${longDate}" />">
	                                <span<c:if test="${fDate == today}"> class="today"</c:if> <% if(sunday) {%> class="highlight"<%} %>><c:out value="${fDate}" /><br><%= strDay %></span>
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
		                    
		                     <c:set var="max" value="${widget.maxfacility}" scope="page"/>
			                   	<%		                   		
			                   		Date[] dateSelect = new Date[reportDuration]; 			                   		
									int max = (Integer)pageContext.getAttribute("max");
									int[][] bookFac = new int[max][reportDuration]; 
									int[][] requestFac = new int[max][reportDuration]; 
									int[][] averageFac = new int[max][reportDuration]; 
			                   		int[] booking = new int[reportDuration];
			                   		int[] requesting = new int[reportDuration];
			                   		int[] totalBooking = new int[reportDuration];
			                   		int[] totalRequest = new int[reportDuration];
			                   		
			                   		tempStart.setTime(reportStarts.getTime());					                        
	                        		for(int i=0; i<reportDuration; i++) {
	                        			if(i != 0)
				                        	tempStart.add(Calendar.DAY_OF_MONTH, 1);
	                        			
				                        dateSelect[i] = tempStart.getTime();
				                        
				                    }
						       %>
						       		                    	
						       		             
						    <tr>
								<th class="headerBgColor" colspan="5" valign="center" width="20%">
									<b><fmt:message key='fms.report.message.label.bookingDuration'/></b></th>
                    			
                    			<th width="58%" class="headerBgColor" valign="top" colspan="<%= reportDuration %>">
                    				
                    			</th>
							</tr>       	
							
							
							
							<%
							int dateArr = 0;
							%>
		                    <c:forEach items="${form.report}" var="record">
		                    	<tr>
		                    	<%String id = ""; %>
		                    	 
		                    	<c:forEach items="${record.key}" var="key">				                    			
		                    			 <c:set var="id" value="${key.key}" scope="page"/>	<%--Show List of Facilities --%>		                    			
		                    			 <td colspan="5" align="left"><c:out value="${key.value}"/></td> 
		                    			 	<%	
		                    			 	int y = 0;	
		                    			 	Date[] dateBooks = new Date[reportDuration];
		                    			 	int[] duration = new int[reportDuration]; 
		                    			 	int[] minutes = new int[reportDuration]; 
		                    			 	String[] strMin = new String[reportDuration];
		                    			 	String facilityId = "";		                    			 		
		                    			 	id = (String)pageContext.getAttribute("id");                   			 		
		                    			 	%>     		
		                    			 	
		                    			 	<c:forEach items="${record.value}" var="facility">       				                    						                    			
				                    			<c:set var="bookDate" value="${facility.bookDate}" scope="page"/>
				                    			<c:set var="hour" value="${facility.hour}" scope="page"/>	
				                    			<c:set var="minutes" value="${facility.minutes}" scope="page"/>	
				                    			<c:set var="facilityId" value="${facility.facilityId}" scope="page"/>			
				                    				
				                    			<%
				                    			if(!(null == pageContext.getAttribute("facilityId"))){
				                    				facilityId = (String)pageContext.getAttribute("facilityId");				                    				
				                    			}
				                    			
				                    			if(facilityId.equals(id)){		//if id same with left then show
					                    			Date dateBook = null;
					                    			if(!(null == pageContext.getAttribute("bookDate"))){
					                    				dateBook = (Date)pageContext.getAttribute("bookDate");
					                    				dateBooks[y] = dateBook;					                   				
					                    			}	
					                    			
					                    			if(!(null == pageContext.getAttribute("hour"))){
					                    				duration[y] = (Integer)pageContext.getAttribute("hour");					                    				
					                    			}
					                    			
					                    			if(!(null == pageContext.getAttribute("minutes"))){
					                    				NumberFormat formatter = new DecimalFormat("#00");
					                    				minutes[y] = (Integer)pageContext.getAttribute("minutes");
					                    				if(minutes[y] >= 60){
					                    					int extraHours = minutes[y] / 60;
					                    					duration[y] = duration[y] + extraHours;
					                    					minutes[y] = minutes[y] %  60;					                    					
					                    				}
					                    				strMin[y] = formatter.format(minutes[y]);							                    				
					                    			}
					                    			
					                    			y++;
				                    			}
				                    			%>				                    							                    			               				              			
				                    		</c:forEach>	
				                    		                 
				                    		   			 	
         			 		                <%
         			 		                int j = 0;
         			 		              	booking = new int[reportDuration];
         			 		             
         			 		                for(int x=0; x<reportDuration; x++) {%>			<%--Show List of Booking Hours --%>             			 	
		                    			 	<td align="center">		                    			 					                    		 
				                    			<%				                    			
				                    			boolean contains = ArrayUtils.contains(dateBooks,dateSelect[x]);				                    			
				                    			if(contains){%>				                    				
				                    				<span><%=duration[j]%>:<%=strMin[j]%></span>
				                    			<%
				                    			booking[x] = duration[j] * 60 + minutes[j];                			
				                    			j++;				                  			
				                    			}else{ %>			
				                    				<span>&nbsp;</span>
				                    			<%} %>    		                    			         			                    			
         			 		                <% 			 		           
         			 		                }    			 		           
         			 		                %>  
         			 		                
         			 		                </td>  			
         			 		                            	                   			 			
		                    			 
		                    			   
		                    			   		
		                    	</c:forEach>	
		                    	
		                    	<%		                    		 		              
			 		               for(int a=0; a<reportDuration; a++) {
			 		            		for(int b=0; b<reportDuration; b++) {			 		            						 		            		
			 		            		if(b == a){           			 		            			
				 		            		bookFac[dateArr][a] += booking[b];					 		            		
			 		            			break;
			 		            		}	          		
			 		            	}			 		            	
			 		              }			 		               
			 		              
		                    	%>	
		                    	</tr>	
		                    	<%dateArr++; %>		
		                    														
							</c:forEach>
							<%							
							totalBooking = new int[reportDuration]; 
							for(int s = 0; s < max; s++){
								for(int q = 0; q < reportDuration; q++){		 		            		
									for(int r = 0; r < reportDuration; r++){
										if(q == r)
											totalBooking[q] += bookFac[s][q];
									}									
		 		            	}
							}
							
							
							%>	
							
							
							<%--No Of Request --%>
							 <tr>
								<th class="headerBgColor" colspan="5" valign="center" width="20%">
									<b><fmt:message key='fms.report.message.label.noOfRequest'/></b></th>
                    			
                    			<th width="58%" class="headerBgColor" valign="top" colspan="<%= reportDuration %>">
                    				
                    			</th>
							</tr>     
							<%
							int reqArr = 0;
							%>  	
							<c:forEach items="${form.report}" var="record">
		                    	<tr>
		                    	<%String id = ""; %>
		                    	<c:forEach items="${record.key}" var="key">				                    			
		                    			 <c:set var="id" value="${key.key}" scope="page"/>	<%--Show List of Facilities --%>
		                    			 
		                    			 <td colspan="5" align="left"><c:out value="${key.value}"/></td> 
		                    			 	<%	int y = 0;	
		                    			 	Date[] dateBooks = new Date[reportDuration];
		                    			 	int[] duration = new int[reportDuration]; 
		                    			 	String[] strMin = new String[reportDuration];
		                    			 	String facilityId = "";		                    			 		
		                    			 	id = (String)pageContext.getAttribute("id");                   			 		
		                    			 	%>     		
		                    			 	
		                    			 	<c:forEach items="${record.value}" var="facility">       				                    						                    			
				                    			<c:set var="bookDate" value="${facility.bookDate}" scope="page"/>
				                    			<c:set var="requestNo" value="${facility.requestNo}" scope="page"/>	
				                    			<c:set var="facilityId" value="${facility.facilityId}" scope="page"/>			
				                    				
				                    			<%
				                    			if(!(null == pageContext.getAttribute("facilityId"))){
				                    				facilityId = (String)pageContext.getAttribute("facilityId");				                    				
				                    			}
				                    			
				                    			if(facilityId.equals(id)){		//if id same with left then show
					                    			Date dateBook = null;
					                    			if(!(null == pageContext.getAttribute("bookDate"))){
					                    				dateBook = (Date)pageContext.getAttribute("bookDate");
					                    				dateBooks[y] = dateBook;					                   				
					                    			}	
					                    			
					                    			if(!(null == pageContext.getAttribute("requestNo"))){
					                    				duration[y] = (Integer)pageContext.getAttribute("requestNo");					                    				
					                    			}					                    			                  			
					                    			
					                    			y++;
				                    			}
				                    			%>				                    							                    			               				              			
				                    		</c:forEach>	
				                    		                 
				                    		   			 	
         			 		                <%
         			 		                int j = 0;
         			 		              	requesting = new int[reportDuration];
         			 		                for(int x=0; x<reportDuration; x++) {%>			<%--Show List of Booking Hours --%>             			 	
		                    			 	<td align="center">		                    			 					                    		 
				                    			<%				                    			
				                    			boolean contains = ArrayUtils.contains(dateBooks,dateSelect[x]);				                    			
				                    			if(contains){%>				                    				
				                    				<span><%=duration[j]%></span>
				                    			<%
				                    			requesting[x] =  duration[j];
				                    			j++;
				                    			}else{ %>			
				                    				<span>&nbsp;</span>
				                    			<%} %>                    			
				                    			
         			 		                <%
         			 		                }%>  
         			 		                
         			 		                </td>  		
		                    			        			
		                    	</c:forEach>	
		                    	<%		                    		 		              
			 		               for(int a=0; a<reportDuration; a++) {
			 		            		for(int b=0; b<reportDuration; b++) {			 		            						 		            		
			 		            		if(b == a){			 		            			
			 		            			//totalRequest[a] += requesting[b];			 		            			
			 		            			requestFac[reqArr][a] += requesting[b];					 		            		
			 		            			break;
			 		            		}	          		
			 		            	}			 		            	
			 		              }		              
		                    	%>		
		                    	</tr>   		
		                    	
		                    	<%reqArr++; %>														
							</c:forEach>
							<%
							totalRequest = new int[reportDuration]; 
							for(int s = 0; s < max; s++){
								for(int q = 0; q < reportDuration; q++){		 		            		
									for(int r = 0; r < reportDuration; r++){
										if(q == r)
											totalRequest[q] += requestFac[s][q];
									}									
		 		            	}
							}
							 %>
							
							
							
							
							
							<%--Average Duration --%>
							<tr>
								<th class="headerBgColor" colspan="5" valign="center" width="20%">
									<b><fmt:message key='fms.report.message.label.averageDuration'/></b></th>
                    			
                    			<th width="58%" class="headerBgColor" valign="top" colspan="<%= reportDuration %>">
                    				
                    			</th>
							</tr>       
							<c:forEach items="${form.report}" var="record">
		                    	<tr>
		                    	<%String id = ""; %>
		                    	<c:forEach items="${record.key}" var="key">				                    			
		                    			 <c:set var="id" value="${key.key}" scope="page"/>	<%--Show List of Facilities --%>
		                    			 
		                    			 <td colspan="5" align="left"><c:out value="${key.value}"/></td> 
		                    			 	<%	int y = 0;	
		                    			 	Date[] dateBooks = new Date[reportDuration];
		                    			 	int[] hour = new int[reportDuration]; 
		                    			 	int[] average = new int[reportDuration]; 
		                    			 	int[] minutes = new int[reportDuration]; 
		                    			 	String[] strMin = new String[reportDuration];
		                    			 	String facilityId = "";		                    			 		
		                    			 	id = (String)pageContext.getAttribute("id");                   			 		
		                    			 	%>     		
		                    			 	
		                    			 	<c:forEach items="${record.value}" var="facility">       				                    						                    			
				                    			<c:set var="bookDate" value="${facility.bookDate}" scope="page"/>
				                    			<c:set var="average" value="${facility.average}" scope="page"/>	
				                    			<c:set var="facilityId" value="${facility.facilityId}" scope="page"/>
				                    			<c:set var="modMinutes" value="${facility.modMinutes}" scope="page"/>			
				                    				
				                    			<%
				                    			if(!(null == pageContext.getAttribute("facilityId"))){
				                    				facilityId = (String)pageContext.getAttribute("facilityId");				                    				
				                    			}
				                    			
				                    			if(facilityId.equals(id)){		//if id same with left then show
					                    			Date dateBook = null;
					                    			if(!(null == pageContext.getAttribute("bookDate"))){
					                    				dateBook = (Date)pageContext.getAttribute("bookDate");
					                    				dateBooks[y] = dateBook;					                   				
					                    			}		
					                    			
					                    			if(!(null == pageContext.getAttribute("average"))){
					                    				NumberFormat formatter = new DecimalFormat("#00");
					                    				average[y] = (Integer)pageContext.getAttribute("average");	
					                    				hour[y] = average[y] / 60;
					                    				minutes[y] = average[y] % 60;					                    				
					                    				strMin[y] = formatter.format(minutes[y]);						                    				
					                    			}						                    			                  			
					                    			y++;
				                    			}
				                    			%>				                    							                    			               				              			
				                    		</c:forEach>	
				                    		                 
				                    		   			 	
         			 		                <%
         			 		                int j = 0;
         			 		                for(int x=0; x<reportDuration; x++) {%>			<%--Show List of Booking Hours --%>             			 	
		                    			 	<td align="center">		                    			 					                    		 
				                    			<%				                    			
				                    			boolean contains = ArrayUtils.contains(dateBooks,dateSelect[x]);				                    			
				                    			if(contains){%>				                    				
				                    				<span><%=hour[j]%>:<%=strMin[j]%></span>
				                    			<%
				                    			j++;
				                    			}else{ %>			
				                    				<span>&nbsp;</span>
				                    			<%} %>                    			
				                    			
         			 		                <%
         			 		                }%>  
         			 		                
         			 		                </td>  				                    	                   			 			
		                    			        			
		                    	</c:forEach>		
		                    	</tr>   														
							</c:forEach>
							
							<tr>
								<td class="headerBgColor" colspan="5" valign="center" width="20%">
									<b><fmt:message key='fms.report.message.label.totalBookingDuration'/></b></td>
                    			
                    			 <%
			 		                int j = 0;
			 		                for(int x=0; x<reportDuration; x++) {			 		                	
			 		                	NumberFormat formatter = new DecimalFormat("#00");
			 		                	int hh = totalBooking[x] / 60;
			 		                	int mm = totalBooking[x] % 60;
			 		                	String formatMM = formatter.format(mm);             	
			 		                %>             			 	
	            			 		<td align="center">	<span><b>
	            			 		<%if(hh > 0){ %>
	            			 			<%=hh %>:<%=formatMM %>
	            			 		<%} %>
	            			 		</b></span> </td>
			 		                <%
			 		     			}%>  
         			 		      			
                    			
							</tr>       
							<tr>
								<td class="headerBgColor" colspan="5" valign="center" width="20%">
									<b><fmt:message key='fms.report.message.label.totalNoOfRequest'/></b></td>
                    			
                    			<%
			 		                j = 0;
			 		                for(int x=0; x<reportDuration; x++) {%>			<%--Show List of Booking Hours --%>             			 	
	            			 		      					
		              					<td align="center">	<span><b>
		            			 		<%if(totalRequest[x] > 0){ %>
		            			 			<%=totalRequest[x] %>
		            			 		<%} %>
		            			 		</b></span> </td>
	            			 		
			 		                <%
			 		     			}%>  
         			 		      			
							</tr>       
							<tr>
								<td class="headerBgColor" colspan="5" valign="center" width="20%">
									<b><fmt:message key='fms.report.message.label.totalAvgDuration'/></b></td>
                    			
                    			 <%
			 		                j = 0;
			 		                for(int x=0; x<reportDuration; x++) {			 		                	
			 		                	NumberFormat formatter = new DecimalFormat("#00");
			 		                	%>
			 		                	<td align="center">	<span><b>
			 		                	<%
			 		                	if(totalRequest[x] > 0){
			 		                	int hr = totalBooking[x] / totalRequest[x];			 		                	
			 		                	int mn = hr % 60;
			 		                	hr = hr / 60;			 		                	
			 		                	String formatMM = formatter.format(mn);             	
			 		                %>             			 	
	            			 		
	            			 		<%if(hr > 0){ %>
	            			 			<%=hr %>:<%=formatMM %>
	            			 		<%}} %>
	            			 		</b></span> </td>
			 		                <%
			 		     			}%>  
         			 		      			
							</tr>       
							
							 
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


