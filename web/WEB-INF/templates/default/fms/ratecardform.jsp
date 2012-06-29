 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <script type="text/javascript">
	onload = function() {
		showHiddenCategory('<c:out value="${form.needCategory}" />');
	}	
</script>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
		<td colspan="2" height="30">
			<fmt:message key='fms.label.step1'/>
		</td>
	</tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.rateCardName'/>&nbsp;* <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.rateCardName" ></x:display>
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.serviceType'/>&nbsp;*</td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.serviceType" ></x:display>
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
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.facility.rateCard.label.abwCode'/>&nbsp;*</td>
        <td class="classRow" valign="top">
			<x:display name="${form.childMap.abwCode.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.description'/></td>
         <td class="classRow" valign="top">
            <x:display name="${form.description.absoluteName}" />
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.remarksRequestor'/></td>
         <td class="classRow" valign="top">
            <x:display name="${form.remarksRequestor.absoluteName}" />
         </td>
    </tr> 
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.setup.transportRequest'/></td>
         <td class="classRow" valign="top">
            <x:display name="${form.pnType.absoluteName}" />
			<div id="category" style="padding-top:10px;<c:if test="${form.needCategory != 'Y'}">display:none;</c:if>">
				<fmt:message key="fms.tran.requestTypeOfVehicles"/>&nbsp;&nbsp;<x:display name="${form.category.absoluteName}" /><br /><br />
			</div>
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
<script>
	function showHiddenCategory(selValue){
		var tab=document.getElementById("category");

		if(selValue=='Y'){
			tab.style.display='block';
		} else {
			tab.style.display='none';
		}	
	}
</script>