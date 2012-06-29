 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
        <td  class="classRowLabel" width="33%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.label.manpower'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.manpower.absoluteName}" />					
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.table.quantity'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.quantity.absoluteName}" />
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.requiredFrom'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredFrom.absoluteName}" />
         </td>
    </tr>   
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.requiredTo'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredTo.absoluteName}" />
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.requiredTime'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredTime.absoluteName}" />
         </td>
    </tr>  
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key="fms.facility.label.blockBooking"/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.blockBooking.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.table.location'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.location.absoluteName}" />
         </td>
    </tr>
    <tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.remarks'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.remarks.absoluteName}" />
         </td>
    </tr>
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
        </td>
    </tr>
 	<jsp:include page="../form_footer.jsp" flush="true"/>
</table>
</td>
</tr>	
</table>