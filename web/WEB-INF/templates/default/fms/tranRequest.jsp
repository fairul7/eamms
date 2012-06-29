 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
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
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTitle'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.title.absoluteName}" ></x:display>
            
        </td>
    </tr>
    
	
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestType'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.program.absoluteName}" />            
            <x:display name="${form.nonProgram.absoluteName}" />
            
            <br>
            
            <div id="approvalRow">             
	            <fmt:message key='fms.tran.requestProgramSelect'/><x:display name="${form.programSelect.absoluteName}" />	       
            </div>
        </td>
    </tr>
    
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.blockBooking'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.bbYes.absoluteName}" />            
            <x:display name="${form.bbNo.absoluteName}" />            
        </td>
    </tr>
      
    
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestDateRequiredFrom'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.startDate.absoluteName}" ></x:display>            
        </td>
    </tr>
        
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestDateRequiredTo'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.endDate.absoluteName}" ></x:display>
            
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestStartTime'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.startTime.absoluteName}" ></x:display>
            
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestEndTime'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.endTime.absoluteName}" ></x:display>
            
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestDestination'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.destination.absoluteName}" ></x:display>
            
        </td>
        
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestPurpose'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.purpose.absoluteName}" ></x:display>
            
        </td>
    </tr>
 
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.remarks.absoluteName}" ></x:display>
            
        </td>
    </tr>
           
	<c:if test="${!empty param.id}">    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTypeOfVehicles'/>&nbsp;* <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <x:display name="${form.transportVehicle.absoluteName}" ></x:display>
            
        </td>
    </tr>
    </c:if>
    
     <tr>
        <td class="classRow"></td>
        <td class="classRow">
			
			<c:choose>
				<c:when test="${form.requestStatus == 'S' || form.requestStatus == 'G'}">
					 <x:display name="${form.saveButton.absoluteName}" />
					 <x:display name="${form.backToIncomingListBtn.absoluteName}" />
				</c:when>
				<c:otherwise>
					<c:choose>
			            <c:when test="${!empty param.id}">
							<x:display name="${form.submitButton.absoluteName}" />
						</c:when>
						<c:otherwise>
							<x:display name="${form.continueButton.absoluteName}" />
						</c:otherwise>
		            </c:choose>
		
		            <x:display name="${form.draftButton.absoluteName}" />
		            
		            <c:if test="${!empty param.id}">    
		            	<x:display name="${form.deleteDraftButton.absoluteName}" />
		            </c:if>
		            
		            <x:display name="${form.cancelButton.absoluteName}" />
				</c:otherwise>
            </c:choose>
        </td>
    </tr>
    
    <c:if test="${!empty param.id}">    
    <tr>
        <td  class="classRowLabel" width="23%" height="10" valign="top" align="right"  valign="top"><!--<fmt:message key='fms.tran.setup.status'/>&nbsp;--></td>
        <td class="classRow">
            <!--<x:display name="${form.statusL.absoluteName}" ></x:display> -->   
        </td>
    </tr>
    </c:if>
    
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>

<script>
    hasProgram();                
</script>