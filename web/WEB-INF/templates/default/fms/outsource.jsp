 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="vehicleReq" value="${widget.request}"/>
 <c:set var="vehicle" value="${widget.viewVehicles}"/>
 <c:set var="driver" value="${widget.viewDrivers}"/>
 
 

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr> 
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >


    <c:set var="id" value="${form.id}"></c:set>
    
    <tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="classRowLabel">
                   <fmt:message key='fms.tran.requestId'/>: &nbsp;<c:out value="${id}" />
                   
                </td>
            </tr>
            
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.manpower.table.dateFromleave'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.startDateL.absoluteName}" ></x:display>
        </td>
    </tr>
        
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.manpower.table.dateToleave'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.endDateL.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.time'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
             <x:display name="${form.timeO.absoluteName}" ></x:display>
        </td>
    </tr>
    
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.itemsRequested'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.allItems.absoluteName}" ></x:display>
        </td>
    </tr>
 
 
  <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.outsourcePanel'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            
            <x:display name="${form.outsourcePanelSelect.absoluteName}" ></x:display>
            
        </td>
    </tr>
    
      <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.quotationNo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.quotationNo.absoluteName}" ></x:display>
        </td>
    </tr>
    
      <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.quotationPrice'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.quotationPrice.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.invoiceNo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.invoiceO.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.invoicePrice'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.invoicePrice.absoluteName}" ></x:display>
        </td>
    </tr>
    
    
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <x:display name="${form.remarksO.absoluteName}" ></x:display>
        </td>
    </tr>
               
            
    
     <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.submitOutsource.absoluteName}" />&nbsp;
            <x:display name="${form.cancelButton.absoluteName}" />&nbsp;
          
        </td>
    </tr>
    
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>

