<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<%@include file="/ekms/recruit/popupCenterJs.js" %>

<script type="text/javascript">
<!--
function toggle_visibility(id) {
var e = document.getElementById(id);
if(e.style.display == 'none')
	e.style.display = 'block';
else
	e.style.display = 'none';
}
//-->
</script>

<x:config >
	<page name="vacancy">
		<%--<com.tms.hr.recruit.ui.VacancyView name="View"/>--%>
		<com.tms.hr.recruit.ui.VacancyDetail name="Detail"/>
    	<com.tms.hr.recruit.ui.ApplicantList name="ApplicantList"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<c:when test="${!empty param.applicantId}">
		<c:redirect url="jobApplicationFormView.jsp?applicantId=${param.applicantId}"/>
	</c:when>
	<c:when test="${!empty param.vacancyCodeApply}">
		<%--<x:set name="vacancy.View" property="vacancyCode" value="${param.vacancyCodeApply}"/>--%>
		<x:set name="vacancy.Detail" property="vacancyCode" value="${param.vacancyCodeApply}"/>
		<x:set name="vacancy.ApplicantList" property="sbCode" value="${param.sbCode}"/>
	</c:when>
	<c:when test="${forward.name == 'kiv'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setKIV"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'short-listed'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setShortlisted"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'scheduled'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setScheduled"/>");	
       		 window.location="setupInterviewer.jsp"; 
    	</script>
	</c:when>
	<c:when test="${forward.name == 'black-listed'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setBlackListed"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'setupNewInterview'}" >	
		<c:redirect url="setupInterview.jsp"/>
	</c:when>
	<c:when test="${forward.name == 'selectShortlistedORAnotherInterview'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.selectShortlistedORAnotherInterview"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'NotNewStatus'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.notNewStatus"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'notNewStatusKIV'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.notNewStatusKIV"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'MailNotSend'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.mailNotSend"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'cannotBlack-listed'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.cannotBlack-listed"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'cannotBlack-listedB'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.cannotBlack-listedB"/>");
    	</script>
	</c:when>
	<%--
	<c:when test="${forward.name == 'rejectedApplicant'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setRejectedApplicant"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'cannotRejectedApplicant'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.cannotRejectedApplicant"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'cannotRejectedApplicantB'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.cannotRejectedApplicantB"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'cannotRejectedApplicantAlready'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.cannotRejectedApplicantAlready"/>");
    	</script>
	</c:when>
	--%>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>
<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.vacancyDetail"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<%-- <x:display name="vacancy.View"/> --%>
	<x:display name="vacancy.Detail"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>	

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.totalApplicantListing"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>	
	<x:display name="vacancy.ApplicantList"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>