<%@ include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"/>

<c:set var="view" value="${widget.viewVehicles}"/>
<c:set var="drivers" value="${widget.viewDrivers}"/>


<jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>
<table width ="100%" class="contentBgColor">
<tr width ="100%">
    <td align="center" width ="100%">
        <b></b>
    </td>
</tr>
<tr>
    <td align="right" >    	
       
         
    </td>
</tr>

<c:if test="${!form.invalid && !empty view}">
<tr>
    <td>
        <table border="0" cellpadding="2" cellspacing="1" width="100%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="20%"><div style="text-align:left;padding-left:10px">Vehicles</div></th>
                <th width="15%">Vehicle Registration</th>
                <th width="35%">Assigments</th>
                <th width="20%"></th> 
            </tr>    
            
            
            <% int i=0;%>            
            <c-rt:set var="i" value="<%=""+i%>"/>	      
           
			<c:forEach items="${view}" var="v" >            
	            <c:if test="${not empty(v[i].category_name)}">	
		             <tr align="center" class="tableRow">		                               
		                <td valign="top" class="classRow">
							<div style="text-align:left;padding-left:10px">
		                		<c:out value="${v[i].category_name}" /><br> 
							</div>           
		                </td>   
		                
		                <td valign="top" class="classRow">
		                	<c:out value="${v[i].vehicle_num}" /><br>               
		                </td>                   
		                
		                <td valign="top" class="classRow">
							<div align="left">          	
			                	<c:forEach items="${v[i].vehicleObject}" var="veh" >               		
				               		<fmt:formatDate pattern="${globalDatetimeLong}"value="${veh.startDate}" /> - 
				               		<fmt:formatDate pattern="${globalDatetimeLong}"value="${veh.endDate}" /><br />						
				               			<li><b><c:out value="${veh.requestTitle}" /></b></li>&nbsp;[<c:out value="${veh.id}" />]<br>
			               		</c:forEach><br>     
							</div>
		                </td>                   
		                <td valign="top" class="classRow">           	                	                	                	                	
		                	[<a href = "assignItems.jsp" onClick="window.open('assignItems.jsp?vehicle_num=<c:out value="${v[i].vehicle_num}"/>&id=<c:out value="${form.assgId}"/>', 'check', 'scrollbars=yes,resizable=yes,width=400,height=200'); return false">Assign</a>]            	          	
		               		<br>
		                </td>                                  
			              <% i++;%>	 
			           </tr>   
		          </c:if>            
	          </c:forEach>
	                          			 
			
			
    </table>
	<br /><br />
    </td>
</tr>





<tr>
    <td>
        <table border="0" cellpadding="2" cellspacing="1" width="100%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="20%"><div style="text-align:left;padding-left:10px">Drivers Name</div></th>                
                <th width="15%">Working Profile</th> 
                <th width="35%">Assigments</th> 
                <th width="20%"></th>    
            </tr>    
            
            <% int x=0;%> 
            <c-rt:set var="x" value="<%=""+x%>"/>	
			<c:forEach items="${drivers}" var="d" >
            
             <tr align="center" class="tableRow">
                               
                <td valign="top" class="classRow">
					<div style="text-align:left;padding-left:10px">
                		<c:out value="${d[x].manpowerName}" /><br>         
					</div>      
                </td>   
                
                <td valign="top" class="classRow">
					<div align="center">
                		<c:out value="${d[x].workprofile}" /><br>         
					</div>      
                </td>      
                
                <td valign="top" class="classRow">             
                	<div align="left">
	               		<c:forEach items="${d[x].assignments}" var="ass" >               		
		               		<fmt:formatDate pattern="${globalDatetimeLong}"value="${ass.startDate}" /> - 
		               		<fmt:formatDate pattern="${globalDatetimeLong}"value="${ass.endDate}" /><br>		               		
		               		<li><b><c:out value="${ass.requestTitle}" /></b></li>&nbsp;[<c:out value="${ass.id}" />]<br>
	               		</c:forEach>
	               		
	               		<c:forEach items="${d[x].leaves}" var="l" >
	               		
	               			<font color='red'><fmt:message key='fms.manpower.offDay'/></font>
		               		[<fmt:formatDate pattern="${globalDateShort}"value="${l.startDate}" /> - 
		               		<fmt:formatDate pattern="${globalDateShort}"value="${l.endDate}" />]<br>	               		
	               		</c:forEach>
	               		<br>
                	</div>	                 	
                </td>      
                                           
               
                <td valign="top" class="classRow">            
                	
                	[<a href = "assignItems.jsp" onClick="window.open('assignItems.jsp?userId=<c:out value="${d[x].manpowerId}"/>&id=<c:out value="${form.assgId}"/>', 'check', 'scrollbars=yes,resizable=yes,width=400,height=200'); return false">Assign</a>] 	
                	<br>
                </td>                                   
	             <% x++;%>	      
	            </c:forEach>              			 
			</tr>   
    </table>

    </td>
</tr>



</c:if>
<tr>
    <td>
    </td>
</tr>
</table>
