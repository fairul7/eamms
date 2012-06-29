 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
		<td colspan="2" height="30">
			<fmt:message key='fms.label.editDetails'/>
		</td>
	</tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.rateCardName'/><FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
			<x:display name="${form.absoluteName}.namelbl" />					
        </td>
    </tr>
   <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.dpfEffectiveDate'/>&nbsp;*<FONT class="classRowLabel"></FONT></td>
         <td class="classRow" valign="top">
            <x:display name="${form.dpfEffectiveDate.absoluteName}" />
         </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.internalRate'/>&nbsp;*</td>
         <td class="classRow" valign="top">
            RM <x:display name="${form.internalRate.absoluteName}" />
         </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.externalRate'/>&nbsp;*</td>
         <td class="classRow" valign="top">
            RM <x:display name="${form.externalRate.absoluteName}" />
         </td>
    </tr>
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
			<x:display name="${form.rateCardId.absoluteName}" />
            <x:display name="${form.submitButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
 
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>