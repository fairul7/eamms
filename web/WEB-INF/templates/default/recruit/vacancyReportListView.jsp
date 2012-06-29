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
        	<td class="classRowLabel" valign="top" align="left" colspan="4"><fmt:message key="recruit.menu.label.vacancyDetail" /></td>
    	</tr>
    		
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
    	
    	<!--
    	<tr>
        	<td valign="top" align="left" colspan="2"><span class="classRowLabel"><fmt:message key="recruit.general.label.DownloadVacancyReport" /></span>
        		<span class="classRow"><a href="/recruit/downloadPdfReportFile?vacancyCode=<x:display name="${form.lblRecVacancyCode.absoluteName}" />" >
        		Download</a></span></td>
    	</tr>
    	-->
    	
    		<tr>
        	<td valign="top" align="left" colspan="4">&nbsp;</td>
    	</tr>
    	<% 
    	Application app = Application.getInstance();
		SecurityService service = (SecurityService) app.getService(SecurityService.class);
		String userId = service.getCurrentUser(request).getId();
		
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		boolean recruitHod = rm.validateHod(userId);
    	if (recruitHod){
    	%>
    	<tr>
        	<td valign="top" align="center" colspan="4">
        	<a href="applicantListing.jsp?vacancyCodeApply=<x:display name="${form.lblRecVacancyCode.absoluteName}" />" >
        		<span class="classRow"><fmt:message key="recruit.menu.label.clickToView" /></span> <span class="classRowLabel"><fmt:message key="recruit.general.label.applicantListing" /></span>
        	</a>
        	</td>
    	</tr>
    	<tr>
        	<td valign="top" align="left" colspan="4">&nbsp;</td>
    	</tr>
    	<%
    	}
    	%>
    	
    	
		</table>
		
		<table width="100%" cellpadding="3" cellspacing="1" border="0">
		
		<tr>
        	<td valign="top" align="center" colspan="12"  bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.menu.label.total" /></td>
    	</tr>
    	
    	<tr>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tApplied" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tShortlisted" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tScheduled" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tReScheduled" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tReScheduledRejected" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tInterviewUnsuccessful" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tAnotherInterview" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tJobOffered" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tJobAccepted" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tJobRejected" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tBlackListed" /></td>
        	<td valign="top" align="left" bgcolor="#003366" class="contentTitleFont"><fmt:message key="recruit.general.label.tViewed" /></td>
    	</tr>
    	
    	<tr class="tableRow">
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalApplied.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalShortlisted.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalScheduled.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalReScheduled.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalReScheduledRejected.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalInterviewUnsuccessful.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalAnotherInterview.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalJobOffered.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalJobAccepted.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalJobRejected.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalBlackListed.absoluteName}" /></td>
        	<td valign="top" align="left" ><x:display name="${form.lblRecTotalViewed.absoluteName}" /></td>
        </tr>	
    	
		</table>
		<jsp:include page="../form_footer.jsp" flush="true"/>	
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>

