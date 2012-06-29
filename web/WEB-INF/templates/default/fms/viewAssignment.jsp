 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="request" value="${widget.trequest}"/>
 <c:set var="assigner" value="${widget.ableToAssign}"/>
 <c:set var="id" value="${widget.id}"/>

<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

	
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.assgId'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
		<a href = "detailsRequest.jsp" onClick="window.open('/ekms/fms/dutyroster/tranRequestDetails.jsp?requestId=<c:out value="${request.requestId}"/>&view=view', 'check', 'scrollbars=yes,resizable=yes,width=600,height=300'); return false">		
            <c:out value="${id}" />
		</a>
            
        </td>
    </tr>

	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTitle'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">     
        	<c:out value="${request.requestTitle}"/>                 
        </td>
    </tr>
    
   
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestProgram'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
            <c:out value="${request.program}"/>     
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.descRemarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
           <c:out value="${request.remarks}"/>     
            
        </td>
    </tr>

    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.assgTo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
             <x:display name="${form.manpower.absoluteName}"/>     
        </td>
    </tr>
    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.requiredFac'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
           <c:out value="${request.vehicle_num}"/>     
            
        </td>
    </tr>
    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.scheduleStartD'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
        
        <c:set var="sDate"><fmt:formatDate pattern="d MMM yyyy" value="${request.startDate}" /></c:set>
									
           <c:out value="${sDate}"/>     
            
        </td>
    </tr>
    
        	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.scheduleEndD'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
        
        <c:set var="eDate"><fmt:formatDate pattern="d MMM yyyy" value="${request.endDate}" /></c:set>
             <c:out value="${eDate}"/>     
        </td>
    </tr>
    	
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.scheduleStartT'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
        	<c:set var="sTime"><fmt:formatDate pattern="hh:mm:a" value="${request.startDate}" /></c:set>
        	<c:out value="${sTime}"/>            
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.scheduleEndT'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
        	<c:set var="eTime"><fmt:formatDate pattern="hh:mm:a" value="${request.endDate}" /></c:set>
            <c:out value="${eTime}"/>               
        </td>
    </tr>
    
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
			<c:out value="${request.reason}"/>  
        </td>
    </tr>
    
    
    
    
	
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"></FONT></td>
        <td class="classRow">       
        
			 	
	            <c:if test="${!(form.status == 'T' || form.status == 'E' || form.status == 'U' || form.status == 'M')}" >			
			         <x:display name="${form.completeButton.absoluteName}" />		
	            	<input value="<fmt:message key="fms.label.notFulfilled"/>" type="button" class="button" onClick="window.open('/ekms/fms/dutyroster/unfulfillAssignment.jsp?id=<c:out value="${form.id}" />&userId=<c:out value="${form.userId}"/>','rejectform','scrollbars=yes,resizable=yes,width=600,height=300');return false;"/>
	            		<c:if test="${assigner}">
	           	 			<!--<x:display name="${form.reassignButton.absoluteName}" /> -->
	            		</c:if> 
	            </c:if>
         	
            <x:display name="${form.backButton.absoluteName}" />
            <br>
                        
        </td>
    </tr>
</table>
</td>
</tr>	
</table>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
