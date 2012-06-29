 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>

 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >


	<tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.unitName'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.unitName.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.unitDescription'/></td>
        <td class="classRow">
            <x:display name="${form.description.absoluteName}" ></x:display>
        </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.headOfUnit'/><FONT class="classRowLabel">*</FONT></td>
         <td class="classRow">
            <x:display name="${form.hou.absoluteName}" />
         </td>
    </tr>
    
     <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.department'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.department.absoluteName}" ></x:display>
        </td>
    </tr>
    
    
    <tr>
         <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.altenateApprover'/></td>
         <td class="classRow">
            <x:display name="${form.approver.absoluteName}" />
         </td>
    </tr>
    <tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.status'/></td>
	    <td class=classRow >
	        <x:display name="${form.radioActive.absoluteName}" />
            <x:display name="${form.radioInactive.absoluteName}" />
	    </td>
	</tr>
    
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.submitButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>