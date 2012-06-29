 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
    <tr>
        <td class="classRowLabel" width="33%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.request.label.requestId'/>
		</td>
        <td class="classRow" valign="top">
		<a href = "assignItems.jsp" onClick="window.open('requestDetails.jsp?requestId=<c:out value="${form.requestId}"/>', 'check', 'scrollbars=yes,resizable=yes,width=650,height=400'); return false"> 	
			<x:display name="${form.lbl_requestId.absoluteName}" />
		</a>
        </td>
    </tr>
	<tr>
        <td class="classRowLabel" width="33%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.request.label.requestTitle'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.requestTitle.absoluteName}" />
        </td>
    </tr>

	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.request.label.program'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.program.absoluteName}" />
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
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='taskmanager.label.AssignTo'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.assignTo.absoluteName}" />
         </td>
    </tr>
    
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.label.status'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.lblstatus.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.label.scheduleStartDate'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredDateFrom.absoluteName}" />
         </td>
    </tr>  
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.label.scheduleEndDate'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredDateTo.absoluteName}" />
         </td>
    </tr>  
    <tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.label.scheduleStartTime'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredTimeFrom.absoluteName}" />
         </td>
    </tr>

	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.label.scheduleEndTime'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredTimeTo.absoluteName}" />
         </td>
    </tr>

	<tr>
		<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
			<fmt:message key='fms.tran.assignLog'/>
		</td>
		 <td class="classRow">
			<x:display name="${form.lblAssignmentLog.absoluteName}"/>  
        </td>
	</tr>

	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">&nbsp;</td>
         <td class="classRow" valign="top">
			<input value="Back" type="button" class="button" onClick="document.location = 'searchroster.jsp'"/>
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