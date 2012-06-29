 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="vehicleReq" value="${widget.request}"/>
  <c:set var="tranreq" value="${widget.tranreq}"/>
 

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >


<c:set var="id" value="${widget.id}"></c:set>

		<tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="classRowLabel">
                   <fmt:message key='fms.tran.requestId'/>: &nbsp;<c:out value="${tranreq.requestId}" />
                   
                </td>
            </tr>
            
            <tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="classRowLabel">
                   <fmt:message key='fms.label.transport.assgId'/>: &nbsp;<c:out value="${form.id}" />
                   
                </td>
            </tr>
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.manpower.form.dateFrom'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.startDateL.absoluteName}" ></x:display>
        </td>
    </tr>
        
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.manpower.form.dateTo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.endDateL.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.time'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.startTimeL.absoluteName}" ></x:display>
        </td>
    </tr>
    
    
    <tr>
    	 <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTypeOfVehicles'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
    <td>
        <table border="0" cellpadding="2" cellspacing="1" width="40%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="5%">No</th>
                <th width="15%">Category</th>
                <th width="5%">Qty</th>
                <th width="15%">Driver Required</th> 
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
					</tr> 				
			        <% i++;%>	              
			        <c:set var="x" value="${x+1}"></c:set>
		        </c:forEach>    
			</table>
	    </td>
	</tr>

	<tr>
        <td  class="classRowLabel">&nbsp;</td>
        <td class="classRow">&nbsp;</td>
    </tr>

    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.selectVehicle'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.vehicleSelect.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.selectManpower'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.manPowerSelect.absoluteName}" ></x:display>
        </td>
    </tr>
    
        
     <tr>
        <td class="classRow"></td>
        <td class="classRow">   			
           	<x:display name="${form.viewAvailabilityButton.absoluteName}" />
           	           																																																	
           	<input value="<fmt:message key='com.tms.fms.transport.backToDetailsButton'/>" type="button" class="button" onClick="document.location='<c:url value="/ekms/fms/transport/request/"/>transportAssignment.jsp?requestId=<c:out value="${tranreq.requestId}';" />"/>
           	
           	<x:display name="${form.backToListButton.absoluteName}" />
			
        </td>
    </tr>
    
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>

