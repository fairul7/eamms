<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="intervieweeReSchedule">
    	<com.tms.hr.recruit.ui.IntervieweeReScheduleList name="list"/>
    </page>
</x:config>

<!-- Handle Events -->
<%--
<c:choose>
	<c:when test="${forward.name == 're-schedule'}" >	
    	<c:redirect url="intervieweeReScheduleList.jsp"/> 
	</c:when>
	<c:when test="${forward.name == 're-scheduleReject'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.setReScheduleReject"/>");
    	</script>
	</c:when>
</c:choose>
--%>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.intervieweeReSchedule"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="intervieweeReSchedule.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>