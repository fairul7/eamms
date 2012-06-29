<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="interviewee">
    	<com.tms.hr.recruit.ui.IntervieweeList name="List"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<%--
	<c:when test="${!empty param.vacancyCode}">
		<c:redirect url="vacancyEdit.jsp?vacancyCode=${param.vacancyCode}"/>
	</c:when>
	--%>
	<c:when test="${!empty param.applicantId}">
		<c:redirect url="interviewResult.jsp?applicantId=${param.applicantId}"/> 
	</c:when>
	<c:when test="${forward.name == 're-schedule'}" >	
		<%--<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setReSchedule"/>");
    	</script>
    	--%>
    	<c:redirect url="intervieweeReScheduleList.jsp"/> 
	</c:when>
	<c:when test="${forward.name == 're-scheduleReject'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setReScheduleReject"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'black-listed'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setBlackListed"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'setInterviewer'}" >	
		<c:redirect url="setupInterviewer.jsp"/>
	</c:when>
	<c:when test="${forward.name == 'selectInterviewee'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.selectInterviewee"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'selectScheduled'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.selectScheduled"/>");
    	</script>
	</c:when>
	<c:when test="${forward.name == 'rejectedApplicant'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setRejectedApplicant"/>");
    	</script>
	</c:when>
	
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.intervieweeListing"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="interviewee.List"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>