 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="tr" value="${widget.TR}"/>
 <c:set var="ao" value="${widget.AO}"/>
 <c:set var="checkIn" value="${widget.checkIn}"/>
 <c:set var="checkOutTime" value="${widget.checkOutTime}"/>
 

 

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

<c:if test="${! empty param.id}">
    <c:set var="id" value="${param.id}"></c:set>
</c:if>

	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.assgId'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
            <c:out value="${ao.assgId}" />
        </td>
    </tr>
    
   
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.requestBy'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.requestBy.absoluteName}" />
            
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.department'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
              <c:out value="${tr.department}" />
            
        </td>
    </tr>
    
    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.dateRequired'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.dateRequired.absoluteName}" />
            
        </td>
    </tr>

    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.timeRequired'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
             <x:display name="${form.timeRequired.absoluteName}" />
        </td>
    </tr>
    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.destination'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:out value="${tr.destination}" />
            
        </td>
    </tr>
    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.assignedVehicle'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:out value="${tr.vehicle_num}" />
            
        </td>
    </tr>
    
        	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.driverAssignedToRequest'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
             <x:display name="${form.driver.absoluteName}" />
        </td>
    </tr>
    
    
	
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.startTime'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:out value="${ao.dateCheckOutLabel}" />
           
        </td>
    </tr>
    
	
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.startSpeedoMeter'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
             <c:out value="${ao.meterLabelStart}" />
            <br>
                        
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.petrolCard'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
             <c:out value="${ao.petrolCard}" />
            <br>
                        
        </td>
    </tr>
    

    
     <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.endTime'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:out value="${ao.dateCheckInLabel}" />
            <br>
                        
        </td>
    </tr>
    
     <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.endSpeedo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
             <c:out value="${ao.meterLabelEnd}" />
            <br>
                        
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
             <c:out value="${ao.remarks}" />
            <br>
                        
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.rate'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <table border="0" cellpadding="2" cellspacing="1" width="25%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="10%">Driver</th>
                <th width="10%">Vehicle</th>              
            </tr>                
           
             <tr align="center" class="tableRow">                   
                <td valign="top" class="classRow">
                	<c:if test="${not empty(tr.rate)}">
                		RM <c:out value="${tr.rate}" />
                	</c:if>
                	<br />               
                </td>   
                <td valign="top" class="classRow">
                	<c:if test="${not empty(tr.rateVehicle)}">
                		RM <c:out value="${tr.rateVehicle}" />
                	</c:if>
                	<br />               
                </td>    
			</tr> 			
			</table>   
        </td>
    </tr>
   
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"></FONT></td>
        <td class="classRow">
       	 	
            <x:display name="${form.backButton.absoluteName}" />
            <br>
                        
        </td>
    </tr>


<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>

