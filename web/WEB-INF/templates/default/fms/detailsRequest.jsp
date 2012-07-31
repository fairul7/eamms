 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="vehicleReq" value="${widget.request}"/>
 <c:set var="vehicle" value="${widget.viewVehicles}"/>
 <c:set var="driver" value="${widget.viewDrivers}"/>
 <c:set var="assignment" value="${widget.assignmentIsCreated}"/>
 <c:set var="transportRequest" value="${widget.transportRequest}"/>
 
 

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

<c:if test="${! empty param.id}">
    <c:set var="id" value="${param.id}"></c:set>
</c:if>

	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestId'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            <c:out value="${id}" />
            
        </td>
    </tr>
    
	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestTitle'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
            <x:display name="${form.titleL.absoluteName}" ></x:display>
        </td>
    </tr>
    
	
	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestType'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            <x:display name="${form.programL.absoluteName}" />
            <br>
                        
        </td>
    </tr>    
	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestDateRequiredFrom'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
            <x:display name="${form.startDateL.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestDateRequiredTo'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
            <x:display name="${form.endDateL.absoluteName}" ></x:display>
        </td>
    </tr>    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestStartTime'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
             <x:display name="${form.startTimeL.absoluteName}" ></x:display>
        </td>
    </tr>    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestEndTime'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
            <x:display name="${form.endTimeL.absoluteName}" ></x:display>
        </td>
    </tr>    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestDestination'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
            <x:display name="${form.destinationL.absoluteName}" ></x:display>
        </td>        
    </tr>    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestPurpose'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">            
            <x:display name="${form.purposeL.absoluteName}" ></x:display>
        </td>
    </tr>    
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.rate'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
           <table border="0" cellpadding="2" cellspacing="1" width="25%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="10%">Driver</th>
                <th width="10%">Vehicle</th>              
            </tr>                
           
             <tr align="center" class="tableRow">                   
                <td valign="top" class="profileRow">
                	<c:if test="${not empty(transportRequest.rate)}">
                		RM <c:out value="${transportRequest.rate}" />
                	</c:if>
                	<br />               
                </td>   
                <td valign="top" class="profileRow">
                	<c:if test="${not empty(transportRequest.rateVehicle)}">
                		RM <c:out value="${transportRequest.rateVehicle}" />
                	</c:if>
                	<br />               
                </td>    
			</tr> 			
			</table>   
        </td>
    </tr>
    
    <tr>
    	 <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.requestTypeOfVehicles'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
    <td>
        <table border="0" cellpadding="2" cellspacing="1" width="50%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="5%">No</th>
                <th width="20%">Category</th>
                <th width="5%">Qty</th>
                <th width="20%">Driver Required</th>       
            </tr>    
            
            <% int i=1;%>      
            <c:set var="x" value="0"></c:set>   
			<c:forEach items="${vehicleReq}" var="req" >
	        	<tr align="center" class="tableRow">
                	<td valign="top" class="profileRow">
                		<c:out value="<%=i%>"/>               
                	</td>
	                <td valign="top" class="profileRow">
                		<c:out value="${req.name}" />               
                	</td>   
	                <td valign="top" class="profileRow">
                		<c:out value="${req.quantity}" />               
                	</td>   
                	<td valign="top" class="profileRow">
                		<c:out value="${req.driver}" />               
                	</td>    
				</tr>
				<% i++;%>
	            <c:set var="x" value="${x+1}"></c:set>
			</c:forEach>
                 
		</table>

    </td>
</tr>
     
	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.Assignment'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">            
 			<c:if test="${!empty vehicle}">                   
	        <b><fmt:message key='fms.tran.AssignedVehicle'/></b><br>
			<table width="35%"  class="borderTable">
				<tr class="tableHeader">
					<th width="5%"><fmt:message key='fms.label.number'/></th>
					<th width="25%"><fmt:message key='fms.tran.vehicles'/></th>
					
				</tr>
		        <c:forEach items="${vehicle}" var="veh" varStatus="stat">	        
					<tr>
						<td align="center" class="profileRow"><c:out value="${stat.index+1}"/></td>
						<td align="center" class="profileRow"><c:out value="${veh.vehicle_num}" /></td>
						
					</tr>           	
		        </c:forEach>
	        </table>
	        <br>
	        </c:if>
			
			<c:if test="${!empty driver}">   
	        <b><fmt:message key='fms.tran.AssignedDriver'/></b><br>
			<table width="35%"  class="borderTable">
				<tr class="tableHeader">
					<th width="5%"><fmt:message key='fms.label.number'/></th>
					<th width="25%"><fmt:message key='fms.tran.drivers'/></th>
				</tr>
				<c:forEach items="${driver}" var="dri" varStatus="statD">
					<tr>
						<td align="center" class="profileRow"><c:out value="${statD.index+1}"/></td>
						<td align="center" class="profileRow"><c:out value="${dri.manpowerName}" /></td>
						
					</tr>	                   	
	        	</c:forEach>        
     		</table>
			</c:if>

			<c:if test="${empty vehicle && empty driver}">
			- 
			</c:if>
        </td>
    </tr>
    
        
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.remarks'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
             <x:display name="${form.remarksL.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.setup.status'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
             <x:display name="${form.statusL.absoluteName}" ></x:display>
        </td>
    </tr>
    
   
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.submittedBy'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">
            
             <x:display name="${form.requestBy.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.tran.approvedBy'/></b>&nbsp; <FONT class="profileRow"></FONT></td>
        <td class="profileRow">            
             <x:display name="${form.approvedBy.absoluteName}" ></x:display>
        </td>
    </tr>
            
    
     <tr>
        <td class="profileRow"></td>			       
        <td class="profileRow">
                
			<c:choose>
					<c:when test="${assignment}">
						<input value="<fmt:message key='fms.label.viewAssignment'/>" type="button" class="button" onClick="document.location='<c:url value="/ekms/fms/transport/request/"/>transportAssignment.jsp?requestId=<c:out value="${id}" />';"/>						      									            													
		            </c:when>
		                  											            										            
	      		<c:otherwise>				      					
						<x:display name="${form.createAssignmentButton.absoluteName}" />&nbsp; 	            		      		
	      		</c:otherwise>										      		
          	</c:choose>	     
          	   	        	
        	        	
        	
            <input value="<fmt:message key='fms.tran.rejectRequest'/>" type="button" class="button" onClick="window.open('/ekms/fms/transport/request/rejectRequest.jsp?id=<c:out value="${form.id}" />','rejectform','scrollbars=yes,resizable=yes,width=400,height=200');return false;"/>
            <x:display name="${form.outsourceButton.absoluteName}" />&nbsp;
            <c:if test="${not assignment}">
            <input value="<fmt:message key='fms.tran.edit'/>" type="button" class="button" onClick="document.location='<c:url value="/ekms/fms/transport/request/"/>editRequest.jsp?id=<c:out value="${id}" />';"/>
            </c:if>
			
			<x:display name="${form.backToListButton.absoluteName}" />
           
        </td>
    </tr>
    
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>

