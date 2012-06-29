 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
        <td  class="classRowLabel" width="33%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.label.facility'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.facility.absoluteName}" />					
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.label.serviceParticulars'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.particular.absoluteName}" />
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.requiredDate'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.dateRequired.absoluteName}" />
         </td>
    </tr>   
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.requiredTime'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.timeRequired.absoluteName}" />
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
         	<fmt:message key='fms.facility.label.conversion'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.conversion.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.duration'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.duration.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.noOfCopies'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.copies.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.attachment'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.attachment.absoluteName}" />
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
         	<fmt:message key='fms.request.label.remarks'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.description.absoluteName}" />
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