 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
        <td  class="classRowLabel" width="33%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.label.assignmentId'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.assignmentId.absoluteName}" />					
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">
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
         	<fmt:message key='fms.facility.label.requiredDate'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredDate.absoluteName}" />
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
         	<fmt:message key='fms.label.manpowerRequired'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.manpower.absoluteName}" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="<c:out value='assignManpower.jsp?id=${param.id}' />">Assign Manpower</a>
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" height="30" valign="top" align="right"  valign="top">&nbsp;</td>
         <td class="classRow" valign="top">
			<!-- x:display name="${form.submit.absoluteName}" /-->
			<!-- x:display name="${form.cancel.absoluteName}" /-->
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