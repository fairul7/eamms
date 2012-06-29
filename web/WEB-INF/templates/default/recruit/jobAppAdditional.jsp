<%@ include file="../recruit/fileHeader.jsp" %>

<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor">
<tr>
<td>
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground">
<tr>
	<td>
		
		<table width="100%" cellpadding="3" cellspacing="1">
		<jsp:include page="../form_header.jsp" flush="true" />		
		
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.uploadResume" /></td>
        	<td class="classRow"><x:display name="${form.resumePath.absoluteName}" /></td>
    	</tr>
    	
	    <tr>
	        	<td class="classRowLabel" valign="top" align="right"></td>
	        	<td class="classRow"><fmt:message key="recruit.general.label.onlyDocAndPdf" /><br />
	        	<c:if test="${form.removeFileClose}" >
	        		<x:display name="${form.lblRecFileUpload.absoluteName}" />
	        	</c:if>
	        	</td>
	    </tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.expectedSalary" /></td>
        	<td class="classRow"><x:display name="${form.sbCurrency.absoluteName}" /><x:display name="${form.txtSalary.absoluteName}" />
        	<x:display name="${form.cbNego.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.travel" /></td>
        	<td class="classRow"><x:display name="${form.radioTravelNo.absoluteName}" /><x:display name="${form.radioTravelYes.absoluteName}" />
        	<x:display name="${form.radioTravelModerate.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.relocate" /></td>
        	<td class="classRow"><x:display name="${form.radioRelocatelNo.absoluteName}" /><x:display name="${form.radioRelocateWill.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.transport" /></td>
        	<td class="classRow"><x:display name="${form.radioTransportNo.absoluteName}" /><x:display name="${form.radioTransportYes.absoluteName}" /></td>
    	</tr>
   	
   		<tr>
        	<td class="classRowLabel" valign="top" align="right"></td>
        	<td class="classRow"><x:display name="${form.btnSave.absoluteName}" /><x:display name="${form.btnReset.absoluteName}" /></td>
    	</tr>
    	
		<jsp:include page="../form_footer.jsp" flush="true"/>	
		</table>
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>

