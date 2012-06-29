 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
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
			<x:display name="${form.status.absoluteName}" />
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

	<c:if test="${form.assignmentStatus == 'C'}" >
	<tr>
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.request.label.completionDate'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.completionDate.absoluteName}" />
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
         <td class="classRowLabel" height="25" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.attachment'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.lbAttachmentList.absoluteName}" />
         </td>
    </tr>
	</c:if>
	<c:if test="${form.assignmentStatus == 'U'}" >
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.reasonUnfulfilled'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.reasonUnfulfilled.absoluteName}" />
         </td>
    </tr>
	</c:if>   
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