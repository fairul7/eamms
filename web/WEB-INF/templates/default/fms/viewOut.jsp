 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="outsource" value="${widget.outsourceObject}"/>
 <c:set var="outpanel" value="${widget.outspanel}"/>
  <c:set var="items" value="${widget.anItems}"/>
 
 

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr> 
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >


    
    <tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="classRowLabel">
                   <fmt:message key='fms.tran.requestId'/>: &nbsp;<c:out value="${outsource.requestId}" />
                   
                </td>
            </tr>
            
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.manpower.table.dateFromleave'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <c:set var="startDate"><fmt:formatDate pattern="dd-MM-yyyy" value="${outsource.startDate}" /></c:set>
            <c:out value="${startDate}" />
        </td>
    </tr>
        
    
	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.manpower.table.dateToleave'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <c:set var="endDate"><fmt:formatDate pattern="dd-MM-yyyy" value="${outsource.endDate}" /></c:set>
            <c:out value="${endDate}" />
            
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.time'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:set var="startTime"><fmt:formatDate pattern="hh:mm:a" value="${outsource.startDate}" /></c:set>
            <c:set var="endTime"><fmt:formatDate pattern="hh:mm:a" value="${outsource.endDate}" /></c:set>
            <c:out value="${startTime}" /> - <c:out value="${endTime}" />
             
        </td>
    </tr>
    
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.itemsRequested'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <c:out value="${items}" />
        </td>
    </tr>
 
 
  <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.outsourcePanel'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            
            <c:out value="${outpanel}" />
            
        </td>
    </tr>
    
      <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.quotationNo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <c:out value="${outsource.quotationNo}" />
        </td>
    </tr>
    
      <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.quotationPrice'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
           <c:out value="${outsource.quotationPrice}" />
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.invoiceNo'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <c:out value="${outsource.invoiceNo}" />
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.outsource.invoicePrice'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            
            <c:out value="${outsource.invoicePrice}" />
        </td>
    </tr>
    
    
    
    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.remarks'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
            <c:out value="${outsource.remark}" />
           
        </td>
    </tr>
               
            
    
     <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <input value="Back" type="button" class="button" onClick="document.location = 'allRequest.jsp'"/>
                     
        </td>
    </tr>
    
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>

