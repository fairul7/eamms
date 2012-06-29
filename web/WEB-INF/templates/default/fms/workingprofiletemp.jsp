 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.label.workingProfileName'/>&nbsp;*
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.tfName.absoluteName}" />					
        </td>
    </tr>
    <tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.label.description'/>&nbsp;
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.description.absoluteName}" />					
        </td>
    </tr>    
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.label.startTime'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.startTime.absoluteName}" />
        </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.endTime'/>
		 </td>
         <td class="classRow" valign="top">
			<x:display name="${form.endTime.absoluteName}" />
         </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.defaultProfile'/>
		 </td>
         <td class="classRow" valign="top">
            <x:display name="${form.defaultProfile.absoluteName}" />
         </td>
    </tr>
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.panel.absoluteName}" />
        </td>
    </tr>
 
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>