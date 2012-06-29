<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ page import="kacang.Application,com.tms.fms.engineering.model.EngineeringModule"%>

<c:set var="widget" value="${widget}"/>
<script type="text/javascript">
	onload = function() {
		<c:choose>
			<c:when test="${widget.closed == 'Y'}">
				populateClientName('closed');
			</c:when>
			<c:when test="${widget.closed == 'C'}">
				populateClientName('complete');
			</c:when>
			<c:when test="${widget.closed == 'U'}">
				populateClientName('hasUnfulfilled');
			</c:when>
			<c:when test="${widget.closed == 'F'}">
				populateClientName('unfulfilled');
			</c:when>
			<c:otherwise>
				populateClientName('submit');
			</c:otherwise>
		</c:choose>
		
	}	
	
</script>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.request.label.requestTitle"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.requestTitle.absoluteName}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.form.requestor"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.requestor.absoluteName}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.label.requestType"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.requestType.absoluteName}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.label.groupAssignmentCode"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.groupAssignmentId.absoluteName}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.requiredDate"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.requiredDate.absoluteName}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.requiredTime"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.requiredTime.absoluteName}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.request.label.requestedItem"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.facility.absoluteName}"/></td>
    </tr>	
	<c:if test="${widget.serviceType == 'vtr'}">
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.conversion"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.conversion.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.duration"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.duration.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.noOfCopies"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.copies.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.request.label.remarks"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.description.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.attachment"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.attachment.absoluteName}"/></td>
	    </tr>
	</c:if>
	<c:if test="${widget.serviceType == 'studio'}">
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.segment"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.segment.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.settingTime"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.settingTime.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.rehearsalTime"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.rehearsalTime.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.VTRTime"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.vtrTime.absoluteName}"/></td>
	    </tr>
	</c:if>
	<c:if test="${widget.serviceType == 'tvro'}">
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.feedTitle"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.tvroFeedTitle.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.form.location"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.tvroLocation.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.timezone"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.tvroTimeZone.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.totalTimeReq"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.tvroTotalTime.absoluteName}"/></td>
	    </tr>
		<tr>
	        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.request.label.remarks"/></b></td>
	        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.tvroRemarks.absoluteName}"/></td>
	    </tr>
	</c:if>
    <tr id="lbCompletionDate" style="display:none">
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.request.label.completionDate"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.lbCompletionDate.absoluteName}"/></td>
    </tr>
   	<tr id="cDate" style="display:none">
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.request.label.completionDate"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.completionDate.absoluteName}"/></td>
    </tr>
	<tr id="cTime" style="display:none">
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.request.label.completionTime"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.completionTime.absoluteName}"/></td>
    </tr>
	<tr id="lbReasonUnfulfilled" style="display:none">
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.reasonUnfulfilled"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.lbReasonUnfulfilled.absoluteName}"/></td>
    </tr>
	<tr id="remarksUnfulfilled" style="display:none">
        <td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.facility.label.reasonUnfulfilled"/></b></td>
        <td width="70%" class="profileRow" valign="top"><x:display name="${widget.remarksUnfulfilled.absoluteName}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow">&nbsp;</td>
        <td width="70%" class="profileRow">
			<table border="0">
			<tr>
			<td>
			<input class="button" type="button" id="completeBtn" value="Complete Assignment" onclick="populateClientName('complete');" />
			</td><td>
			<input class="button" type="button" id="unfulfilledBtn" value="<fmt:message key='fms.label.unfulfilledAssignment'/>" onclick="populateClientName('unfulfilled');" />
			</td>
			</tr>
			</table>
			<span id="submitBtn">
            	<x:display name="${widget.submit.absoluteName}"/>
            	<x:display name="${widget.cancel.absoluteName}"/>
			</span>
			<span id="unfulfilled">
            	<x:display name="${widget.unfulfilled.absoluteName}"/>
            	<x:display name="${widget.cancel.absoluteName}"/>
			</span>
        </td>
    </tr>
    <tr><td class="profileFooter" colspan="2">&nbsp;</td></tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
<script>
	function populateClientName(selValue){
		var cDate=document.getElementById("cDate");
		var cTime=document.getElementById("cTime");
		var complete=document.getElementById("completeBtn");
		var submit=document.getElementById("submitBtn");
		var unfulfilledBtn=document.getElementById("unfulfilledBtn");
		var unfulfilled=document.getElementById("unfulfilled");
		var remarksUnfulfilled=document.getElementById("remarksUnfulfilled");
		var lbCompletion = document.getElementById("lbCompletionDate");
		var lbUnfulfilled = document.getElementById("lbReasonUnfulfilled");
		
		if(selValue=='complete'){
			cDate.style.display='';
			cTime.style.display='';
			complete.style.display='none';
			submit.style.display='';
			unfulfilled.style.display='none';
			unfulfilledBtn.style.display='none';
			remarksUnfulfilled.style.display='none';
		} else if(selValue=='submit'){
			cDate.style.display='none';
			cTime.style.display='none';
			complete.style.display='block';
			submit.style.display='none';
			unfulfilled.style.display='none';
			unfulfilledBtn.style.display='block';
			remarksUnfulfilled.style.display='none';
		} else if(selValue=='closed'){
			cDate.style.display='none';
			cTime.style.display='none';
			complete.style.display='none';
			submit.style.display='none';
			lbCompletion.style.display='';
			unfulfilled.style.display='none';
			unfulfilledBtn.style.display='none';
			remarksUnfulfilled.style.display='none';
		} else if(selValue == 'unfulfilled') {
			cDate.style.display='none';
			cTime.style.display='none';
			complete.style.display='none';
			submit.style.display='none';
			unfulfilled.style.display='';
			unfulfilledBtn.style.display='none';
			remarksUnfulfilled.style.display='';
		} else if (selValue == 'hasUnfulfilled') {
			cDate.style.display='none';
			cTime.style.display='none';
			complete.style.display='none';
			submit.style.display='none';
			unfulfilled.style.display='none';
			unfulfilledBtn.style.display='none';
			remarksUnfulfilled.style.display='none';
			lbUnfulfilled.style.display='';
		}
	}
</script>