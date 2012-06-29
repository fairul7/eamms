<%@ include file="../recruit/fileHeader.jsp" %>

<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor">
<tr>
<td>
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground">
<tr>
	<td>
		
		<table width="100%" cellpadding="3" cellspacing="1" border="0">
		<jsp:include page="../form_header.jsp" flush="true" />		
		
		<tr>
        	<td class="classRowLabel" valign="top"><x:display name="${form.lblRecPosition.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" >&nbsp;</td>
    	</tr>
      	
      	<tr>
        	<td class="classRowLabel" valign="top" ><x:display name="${form.lblRecDepartment.absoluteName}" /> <x:display name="${form.lblRecNoOfPosition.absoluteName}" /></td>
    	</tr>
      	
      	<tr>
        	<td class="classRowLabel" valign="top">(based in <x:display name="${form.lblRecCountry.absoluteName}"/>)</td>
    	</tr>
      	
      	<tr>
        	<td><span class="classRowLabel"><fmt:message key="recruit.general.label.priorty" /></span>
        	<span class="classRow"><x:display name="${form.lblRecPriority.absoluteName}" /></span></td>
    	</tr>
      	
      	<tr>
        	<td><span class="classRowLabel"><fmt:message key="recruit.general.label.vacancyEndDate" /></span>
        	<span class="classRow"><x:display name="${form.lblRecEndDate.absoluteName}" /></span></td>
    	</tr>
      	
      	<tr>
        	<td class="classRowLabel" valign="top">&nbsp;</td>
    	</tr>
      		
      	<tr>
        	<td class="classRowLabel" valign="top"><fmt:message key="recruit.general.label.tbJobRespon" /></td>	
    	</tr>
      
      	<tr>
        	<td class="classRow" ><x:display name="${form.lblRecRespon.absoluteName}" /></td>
    	</tr>
      
      	<tr>
        	<td class="classRowLabel" valign="top"><fmt:message key="recruit.general.label.tbJobRequire" /></td>	
    	</tr>
      
      	<tr>
        	<td class="classRow"><x:display name="${form.lblRecRequire.absoluteName}" /></td>
    	</tr>
      	
    	<tr>
        	<td class="classRowLabel" valign="top" ><x:display name="${form.btnApply.absoluteName}" /></td>
    	</tr>
		
		<jsp:include page="../form_footer.jsp" flush="true"/>	
		</table>
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>

