 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="vehicleReq" value="${widget.request}"/>
 <c:set var="transportRequest" value="${widget.transportRequest}"/>

<script>
function hasProgram(){
        var radio = document.forms['<c:out value="${widget.absoluteName}" />'].elements['<c:out value="${form.programUnit.absoluteName}"/>.programUnit'];
        for(i=0;i<radio.length;i++)
        {
            if(radio[i].checked == true){
                    var row = document.getElementById('approvalRow');
                if(radio[i].value=='<c:out value="${form.program.absoluteName}" />'){
                    row.style.display = 'block';

                }else{
                    row.style.display = 'none';
                }
            }
        }
    }
</script>


  



<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

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
            <table border="0" cellpadding="2" cellspacing="1" width="25%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="10%">Driver</th>
                <th width="10%">Vehicle</th>              
            </tr>                
           
             <tr align="center" class="tableRow">                   
                <td valign="top" class="classRow">
                	<c:if test="${not empty(transportRequest.rate)}">
                		RM <c:out value="${transportRequest.rate}" />
                	</c:if>
                	<br />               
                </td>   
                <td valign="top" class="classRow">
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
    	 <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTypeOfVehicles'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
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
	                <td valign="top" class="classRow">
	                	<c:out value="<%=i%>"/>       
	                	<br />        
	                </td>
                	<td valign="top" class="classRow">
                		<c:out value="${req.name}" /> 
                		<br />              
                	</td>   
                	<td valign="top" class="classRow">
                		<c:out value="${req.quantity}" />
                		<br />               
                	</td>   
                	<td valign="top" class="classRow">
                		<c:out value="${req.driver}" />
                		<br />               
                	</td>    
				</tr> 
	            <% i++;%>
	          	<c:set var="x" value="${x+1}"></c:set>
			</c:forEach>
		</table>
    </td>
</tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.remarksL.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <!-- tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.setup.status'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.statusL.absoluteName}" ></x:display>
        </td>
    </tr-->
    
     <tr>
        <td class="classRow"></td>
        <td class="classRow">
            
             
				<input value="Back to Listing" type="button" class="button" onClick="document.location = 'incomingHODListing.jsp'"/>
           
        </td>
    </tr>
    
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>


<script>
    hasProgram();                
</script>