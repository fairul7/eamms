<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 kacang.ui.menu.MenuItem,
                 com.tms.hr.recruit.model.*"%>
<%@ include file="../recruit/fileHeader.jsp" %>

<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor">
<tr>
<td>
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground">
<tr>
	<td>
	
		<jsp:include page="../form_header.jsp" flush="true" />		
		<table width="100%" cellpadding="3" cellspacing="1" border="0">

		<tr>
        	<td valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.vacancyCode" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecVacancyCode.absoluteName}" /></span></td>
        	<td valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.createdBy" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecCreatedBy.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td  valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.position" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecPosition.absoluteName}" /></span></td>
        	<td  valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.dateCreated" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecDateCreated.absoluteName}" /></span></td>
    	</tr>
    
    	<tr>
        	<td valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.country" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecCountry.absoluteName}" /></span></td>
        	<td  valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.lastUpdatedBy" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecLastUpdatedBy.absoluteName}" /></span></td>
    	</tr>
    
    	<tr>
        	<td valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.department" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecDepartment.absoluteName}" /></span></td>
        	<td valign="top" align="right"><span class="classRowLabel"><fmt:message key="recruit.general.label.lastDateModified" /></span></td>
        	<td valign="top" align="left"><span class="classRow"><x:display name="${form.lblRecLastDateModified.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="right" colspan="1"><span class="classRowLabel"><fmt:message key="recruit.general.label.noOfPosition" /></span></td>
        	<td valign="top" align="left" colspan="3"><span class="classRow"><x:display name="${form.lblRecNoOfPosition.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="right" colspan="1"><span class="classRowLabel"><fmt:message key="recruit.general.label.priorty" /></span></td>
        	<td valign="top" align="left" colspan="3"><span class="classRow"><x:display name="${form.lblRecPriority.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="right" colspan="1"><span class="classRowLabel"><fmt:message key="recruit.general.label.vacancyStartDate" /></span></td>
        	<td valign="top" align="left" colspan="3"><span class="classRow"><x:display name="${form.lblRecStartDate.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="right" colspan="1"><span class="classRowLabel"><fmt:message key="recruit.general.label.vacancyEndDate" /></span></td>
        	<td valign="top" align="left" colspan="3"><span class="classRow"><x:display name="${form.lblRecEndDate.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="left" colspan="4">&nbsp;</td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="right" colspan="1"><span class="classRowLabel"><fmt:message key="recruit.general.label.tbJobRespon" /></span></td>
        	<td valign="top" align="left" colspan="3"><span class="classRow"><x:display name="${form.lblRecRespon.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="right" colspan="1"><span class="classRowLabel"><fmt:message key="recruit.general.label.tbJobRequire" /></span></td>
        	<td valign="top" align="left" colspan="3"><span class="classRow"><x:display name="${form.lblRecRequire.absoluteName}" /></span></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="left" colspan="4">&nbsp;</td>
    	</tr>
    	
		</table>
		
		<table width="100%" cellpadding="3" cellspacing="1" border="0">
		
		<tr>
        	<td valign="top" align="center" colspan="13"  bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.menu.label.total" /></td>
    	</tr>
    
    	<tr>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=0'>
        			<fmt:message key="recruit.general.label.new" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=1'>
        			<fmt:message key="recruit.general.label.kiv" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=2'>
        			<fmt:message key="recruit.general.label.short-listed" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=3'>
        			<fmt:message key="recruit.general.label.shortlistedAndEmailSend" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=4'>
        			<fmt:message key="recruit.general.label.scheduled" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=5'>
        			<fmt:message key="recruit.general.label.offered" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=6'>
        			<fmt:message key="recruit.general.label.interviewUnsuccessful" />
        		</a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=7'>
        			<fmt:message key="recruit.general.label.anotherInterview" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=8'>
        			<fmt:message key="recruit.general.label.anotherInterviewAndEmailSend" /></a>
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=9'>
        			<fmt:message key="recruit.general.label.jobAccepted" />
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=10'>
        			<fmt:message key="recruit.general.label.jobRejected" />
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=11'>
        			<fmt:message key="recruit.general.label.rejectedApplicant" />
        	</td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont">
        		<a href ='applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />&sbCode=12'>
        			<fmt:message key="recruit.general.label.black-listed" />
        	</td>
    	</tr>
    	
    	<tr class="tableRow">
        	<td valign="top" align="left" ><x:display name="${form.lblRecNew.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecKiv.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecShortListed.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecShortListedAndMailed.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecScheduled.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecOffered.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecInterviewUnSuccessful.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecAnotherInterview.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecAnotherInterviewAndMailed.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecJobAccepted.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecJobRejected.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecRejectedApplicant.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecBlacklisted.absoluteName}" /></td>
        </tr>	
    	
		</table>
		<jsp:include page="../form_footer.jsp" flush="true"/>	
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>

