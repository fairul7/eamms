<%@ include file="../recruit/fileHeader.jsp" %>

<jsp:include page="../form_header.jsp" flush="true" />	
<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor" border="0">
<tr>
<td>
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground">
<tr>
	<td>
		
		<table width="100%" cellpadding="3" cellspacing="1" border="0">
	
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.tempName" /></td>
        	<td class="classRow"><x:display name="${form.sbVacancyTempCode.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.vacancyCode" /> *</td>
        	<td class="classRow"><x:display name="${form.txtVacancyCode.absoluteName}" /></td>
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
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.cbVacancyTemp" /></td>
        	<td class="classRow"><x:display name="${form.cbVacancyTemp.absoluteName}" /></td>
    	</tr>
    	
 		<c:set var="cb" value="${widgets['vacancyForm.add'].cbVacancyTemp}"/>
     	<tr>
     		<td class="classRowLabel" valign="top" align="right">
	     		<c:choose>
	    		<c:when test="${cb.checked}" >
	     			<div  id="response1" style="display:inline;">
	     				<fmt:message key="recruit.general.label.tempName" /> *
	     			</div>
	     		</c:when>	
		    	<c:otherwise>
		    		<div  id="response1" style="display:none;">
	     				<fmt:message key="recruit.general.label.tempName" /> *
	     			</div>
		    	</c:otherwise>
	    		</c:choose>		
     		</td>
     		<td class="classRow">
	     		<c:choose>
	    		<c:when test="${cb.checked}" >
	     			<div  id="response1a" style="display:inline;">
	     				<x:display name="${form.txtVacancyTempCode.absoluteName}" />
	     			</div>
	     		</c:when>	
		    	<c:otherwise>
		    		<div  id="response1a" style="display:none;">
	     				<x:display name="${form.txtVacancyTempCode.absoluteName}" />
	     			</div>
		    	</c:otherwise>
	    		</c:choose>		
     		</td>
     	</tr>
     	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right">&nbsp;</td>
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
