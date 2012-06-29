 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="request" value="${widget.trequest}"/>
  <c:set var="id" value="${widget.id}"/>
<jsp:include page="../form_header_compAssignment.jsp"/>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.assgId'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:out value="${id}" />
            
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
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.assDateTime'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
           <c:set var="sDate"><fmt:formatDate pattern="d MMM yyyy" value="${request.startDate}" /></c:set>	
           <c:set var="eDate"><fmt:formatDate pattern="d MMM yyyy" value="${request.endDate}" /></c:set>
           <c:set var="sTime"><fmt:formatDate pattern="hh:mm:a" value="${request.startDate}" /></c:set>
           <c:set var="eTime"><fmt:formatDate pattern="hh:mm:a" value="${request.endDate}" /></c:set>
           							
           	<c:out value="${sDate}"/> to <c:out value="${eDate}"/> <br>      
           
        	<c:out value="${sTime}"/> - <c:out value="${eTime}"/>     
            
        </td>
    </tr>

    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.completionDate'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
              <x:display name="${form.completionDate.absoluteName}"/>     
        </td>
    </tr>
    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.completionTime'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
           <x:display name="${form.completionTime.absoluteName}"/>     
            
        </td>
    </tr>
    
    	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
           <x:display name="${form.remarks.absoluteName}"></x:display>
            
        </td>
    </tr>
    
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.attachDoc'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.docFileUpload.absoluteName}"></x:display>
        </td>
    </tr>
    
	
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"></FONT></td>
        <td class="classRow">
       	 	
            <x:display name="${form.submitButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
            <br>
                        
        </td>
    </tr>
</table>
</td>
</tr>	
</table>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
