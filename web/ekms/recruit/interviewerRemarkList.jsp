<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="interviewerRemark">
    	<com.tms.hr.recruit.ui.InterviewerRemarkList name="list"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<c:when test="${!empty param.vacancyCode}">
		<c:redirect url="vacancyEdit.jsp?vacancyCode=${param.vacancyCode}"/>
	</c:when>
	<c:when test="${!empty param.applicantId}">
		<c:redirect url="interviewerRemarkForm.jsp?applicantId=${param.applicantId}"/> 
	</c:when>
	<c:when test="${forward.name == 'deleted'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.delete"/>");
    	</script>
	</c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.intervieweeRemark"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="interviewerRemark.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>