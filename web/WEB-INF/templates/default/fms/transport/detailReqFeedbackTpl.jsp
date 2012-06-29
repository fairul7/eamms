 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="vehicleReq" value="${widget.request}"/>
 <c:set var="vehicle" value="${widget.viewVehicles}"/>
 <c:set var="driver" value="${widget.viewDrivers}"/>
 <c:set var="assignment" value="${widget.assignmentIsCreated}"/>
 
 
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

<c:if test="${! empty param.requestId}">
    <c:set var="requestId" value="${param.requestId}"></c:set>
</c:if>

	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestId'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:out value="${requestId}" />
            
        </td>
    </tr>
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTitle'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.titleL.absoluteName}" ></x:display>
        </td>
    </tr>
    
	
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestType'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.programL.absoluteName}" />
            <br>
                        
        </td>
    </tr>    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestDateRequiredFrom'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.startDateL.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestDateRequiredTo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.endDateL.absoluteName}" ></x:display>
        </td>
    </tr>    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestStartTime'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.startTimeL.absoluteName}" ></x:display>
        </td>
    </tr>    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestEndTime'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.endTimeL.absoluteName}" ></x:display>
        </td>
    </tr>    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestDestination'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.destinationL.absoluteName}" ></x:display>
        </td>        
    </tr>    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestPurpose'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
            <x:display name="${form.purposeL.absoluteName}" ></x:display>
        </td>
    </tr>    
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.rate'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.rateCard.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
    	 <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTypeOfVehicles'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
    <td>
        <table border="0" cellpadding="2" cellspacing="1" width="50%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="5%">No</th>
                <th width="20%">Category</th>
                <th width="5%">Qty</th>
                <th width="20%">Driver Required</th>                           
                
            	</td> 
            </tr>    
            
            
            <% int i=1;%>      
            <c:set var="x" value="0"></c:set>   
			<c:forEach items="${vehicleReq}" var="req" >
            
             <tr align="center" class="tableRow">
                <td valign="top" class="classRow">
                	<c:out value="<%=i%>"/>               
                </td>
                
                <td valign="top" class="classRow">
                	<c:out value="${req.name}" />               
                </td>   
               
                <td valign="top" class="classRow">
                	<c:out value="${req.quantity}" />               
                </td>   
                
                <td valign="top" class="classRow">
                	<c:out value="${req.driver}" />               
                </td>    

				
	              <% i++;%>
	              
	              <c:set var="x" value="${x+1}"></c:set>
	            </c:forEach>
                
                			 
			</tr>     
		</table>

    </td>
</tr>
     
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.Assignment'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
 			<c:if test="${!empty vehicle}">                   
	        <b><fmt:message key='fms.tran.AssignedVehicle'/></b><br>
			<table width="35%"  class="borderTable">
				<tr class="tableHeader">
					<th width="5%"><fmt:message key='fms.label.number'/></th>
					<th width="25%"><fmt:message key='fms.tran.vehicles'/></th>
				</tr>
		        <c:forEach items="${vehicle}" var="veh" varStatus="stat">	        
					<tr>
						<td align="center" class="tableRow"><c:out value="${stat.index+1}"/></td>
						<td align="center" class="tableRow"><c:out value="${veh.vehicle_num}" /></td>
						
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
						<td align="center" class="tableRow"><c:out value="${statD.index+1}"/></td>
						<td align="center" class="tableRow"><c:out value="${dri.manpowerName}" /></td>
						
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
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.remarksL.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.setup.status'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.statusL.absoluteName}" ></x:display>
        </td>
    </tr>
    
   
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.submittedBy'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.requestBy.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.approvedBy'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
             <x:display name="${form.approvedBy.absoluteName}" ></x:display>
        </td>
    </tr>
            
    
     <tr>
        <td class="classRow"></td>			       
        <td class="classRow">
           
        </td>
    </tr>
    
    
</table>
</td>
</tr>	
</table>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
