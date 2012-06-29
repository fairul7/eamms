<%@ include file="../recruit/fileHeader.jsp" %>
<jsp:include page="../form_header.jsp" flush="true" />
<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor">
<tr>
<td>
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground">
<tr>
	<td>
		
		<table width="100%" cellpadding="3" cellspacing="1">
				
		 
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.vacancyCode" /></td>
        	<td class="classRow"><x:display name="${form.lblnVacancyCodeName.absoluteName}" /></td>
    	</tr>
		
       <tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.position" /> *</td>
        	<td class="classRow"><x:display name="${form.sbPosition.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.country" /> *</td>
        	<td class="classRow"><x:display name="${form.sbCountry.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.department" /> *</td>
        	<td class="classRow"><x:display name="${form.sbDepartment.absoluteName}" /></td>
    	</tr>
		
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.tbJobRespon" /> *</td>
        	<td class="classRow"><x:display name="${form.tbJobRespon.absoluteName}" /></td>
    	</tr>
		
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.tbJobRequire" /> *</td>
        	<td class="classRow"><x:display name="${form.tbJobRequire.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.noOfPosition" /> *</td>
        	<td class="classRow"><x:display name="${form.txtNoOfPosition.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.priorty" /></td>
        	<td class="classRow"><x:display name="${form.radioGroup.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.vacancyStartDate" /> *</td>
        	<td class="classRow"><x:display name="${form.startDate.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.vacancyEndDate" /> *</td>
        	<td class="classRow"><x:display name="${form.endDate.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"></td>
        	<td class="classRow"><x:display name="${form.btnSubmit.absoluteName}" /><x:display name="${form.btnCancel.absoluteName}" /></td>
    	</tr>
		
		
		</table>
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>	
