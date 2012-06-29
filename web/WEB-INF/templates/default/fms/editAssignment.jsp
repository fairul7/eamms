 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
  <c:set var="request" value="${widget.trequest}"/>
 

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

<c:if test="${! empty param.id}">
    <c:set var="id" value="${param.id}"></c:set>
</c:if>


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
             <x:display name="${form.manPowerSelect.absoluteName}"/>     
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
                      -
        </td>
    </tr>
    
    
	
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"></FONT></td>
        <td class="classRow">       	 	
            
            <x:display name="${form.submitButton.absoluteName}" /><x:display name="${form.backButton.absoluteName}" />
            <br>
                        
        </td>
    </tr>


<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>

