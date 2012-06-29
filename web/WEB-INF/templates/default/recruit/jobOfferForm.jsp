<%@ include file="../recruit/fileHeader.jsp" %>
 
<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor" border="0">
<tr>
<td width="100%">
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground"  border="0">
<tr>
	<td>
		
		<table width="100%" cellpadding="3" cellspacing="1" border="0">
		<jsp:include page="../form_header.jsp" flush="true" />		
		
		<tr>
        	<td class="classRowLabel" valign="top" align="right" width="45%"><fmt:message key="recruit.general.label.applicant" /></td>
        	<td class="classRow"><x:display name="${form.lblRecApplicant.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.vacancyCode" /></td>
        	<td class="classRow"><x:display name="${form.lblRecVacancyCode.absoluteName}" /></td>
    	</tr>
		
       <tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.dateOffered" /></td>
        	<td class="classRow"><x:display name="${form.lblRecDateOffered.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.position" /></td>
        	<td class="classRow"><x:display name="${form.lblRecPostion.absoluteName}" /></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.country" /></td>
        	<td class="classRow"><x:display name="${form.lblRecCountry.absoluteName}" /></td>
    	</tr>
		
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.department" /></td>
        	<td class="classRow"><x:display name="${form.lblRecDepartment.absoluteName}" /></td>
    	</tr>
		
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.OfferLetterSent" /></td>
        	<td class="classRow"><x:display name="${form.cbOfferLetterSent.absoluteName}" /></td>
    	</tr>
    	
    	<c:set var="cb" value="${widgets['jobOffer.form'].cbOfferLetterSent}"/>
     	
     	<tr>
     		<td class="classRowLabel" valign="top" align="right">
     		<c:choose>
	    		<c:when test="${cb.checked}" >
	    			<div id="response1" style="display:block"><fmt:message key="recruit.general.label.OfferLetterStatus" /></div>
	    		</c:when>
	    		<c:otherwise>
	    			<div id="response1" style="display:none"><fmt:message key="recruit.general.label.OfferLetterStatus" /></div>
	    		</c:otherwise>
    		</c:choose>
     		</td>
     		<td class="classRow">
     		<c:choose>
	    		<c:when test="${cb.checked}" >
	    		<div id="response1a" style="display:block">
    				<x:display name="${form.radioUnderConsideration.absoluteName}" />
	        		<x:display name="${form.radioAccept.absoluteName}" />
	        		<x:display name="${form.radioReject.absoluteName}" />
	        	</div>	
	        	</c:when>
	        <c:otherwise>
	        	<div id="response1a" style="display:none">
    				<x:display name="${form.radioUnderConsideration.absoluteName}" />
	        		<x:display name="${form.radioAccept.absoluteName}" />
	        		<x:display name="${form.radioReject.absoluteName}" />
	        	</div>	
	        </c:otherwise>		
	        </c:choose>
     		</td>
     	</tr>
     	
     	<tr>
     		<td class="classRowLabel" valign="top" align="right">
     		<c:choose>
	    		<c:when test="${cb.checked}" >
	    			<div id="response2" style="display:block"><fmt:message key="recruit.general.label.remark" /></div>
	    		</c:when>
	    		<c:otherwise>
	    			<div id="response2" style="display:none"><fmt:message key="recruit.general.label.remark" /></div>
	    		</c:otherwise>
    		</c:choose>
     		</td>
     		<td class="classRow">
     		<c:choose>
	    		<c:when test="${cb.checked}" >
	    		<div id="response2a" style="display:block">
    			<x:display name="${form.tbRemark.absoluteName}" />
	        	</div>	
	        	</c:when>
	        <c:otherwise>
	        	<div id="response2a" style="display:none">
    			<x:display name="${form.tbRemark.absoluteName}" />
	        	</div>	
	        </c:otherwise>		
	        </c:choose>
     		</td>
     	</tr>
     	
     	<%--
    	<c:choose>
    		<c:when test="${cb.checked}" >
    			<tr id="response1" style="display:block;">
		        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.OfferLetterStatus" /></td>
		        	<td class="classRow">
		        		<x:display name="${form.radioUnderConsideration.absoluteName}" />
		        		<x:display name="${form.radioAccept.absoluteName}" />
		        		<x:display name="${form.radioReject.absoluteName}" />
		        	</td>
	    		</tr>
	    	
		    	<tr id="response2" style="display:block;">
		        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.remark" /></td>
		        	<td class="classRow"><x:display name="${form.tbRemark.absoluteName}" /></td>
		    	</tr>
    		</c:when>
			<c:otherwise>
				<tr id="response1" style="display:none;">
		        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.OfferLetterStatus" /></td>
		        	<td class="classRow">
		        		<x:display name="${form.radioUnderConsideration.absoluteName}" />
		        		<x:display name="${form.radioAccept.absoluteName}" />
		        		<x:display name="${form.radioReject.absoluteName}" />
		        	</td>
	    		</tr>
	    	
		    	<tr id="response2" style="display:none;">
		        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.remark" /></td>
		        	<td class="classRow"><x:display name="${form.tbRemark.absoluteName}" /></td>
		    	</tr>
			</c:otherwise>
		</c:choose>	    	
    	--%>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right">&nbsp;</td>
        	<td class="classRow"><x:display name="${form.btnSubmit.absoluteName}" /><x:display name="${form.btnCancel.absoluteName}" /></td>
    	</tr>
		
		<jsp:include page="../form_footer.jsp" flush="true"/>	
		</table>
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>

