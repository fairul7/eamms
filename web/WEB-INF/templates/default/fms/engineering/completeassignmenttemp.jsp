 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
    <tr>
        <td class="classRowLabel" width="33%" height="25" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.request.label.requestTitle'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.requestTitle.absoluteName}" />
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.request.label.program'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.program.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.label.assignmentDateTime'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredDateFrom.absoluteName}" />&nbsp;<fmt:message key='fms.label.separatorDate'/>&nbsp;
			<x:display name="${form.requiredDateTo.absoluteName}" />
			&nbsp;
			<x:display name="${form.requiredTimeFrom.absoluteName}" /> - <x:display name="${form.requiredTimeTo.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.request.label.completionDate'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.completionDate.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.request.label.completionTime'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.completionTime.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.remarks'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.remarks.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.attachment'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.attachmentPanel.absoluteName}" />&nbsp;
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">&nbsp;</td>
         <td class="classRow" valign="top">
			<x:display name="${form.submit.absoluteName}" />
			<x:display name="${form.cancel.absoluteName}" />
         </td>
    </tr>    
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
        </td>
    </tr>
 	<jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
</td>
</tr>	
</table>