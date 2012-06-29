<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="setupInterviewer">
	    <com.tms.hr.recruit.ui.InterviewHistory name="list" />
		<com.tms.hr.recruit.ui.SetupInterviewerParticularForm name="form" />
    </page>
</x:config>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="interviewResult.jsp" />
</c:if>

<c:if test="${forward.name=='submit'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.setInterviewer"/>");
        window.location="interviewResult.jsp";       
    </script>
</c:if>

<c:if test="${!empty param.applicantId}">
	<x:set name="setupInterviewer.list" property="applicantId" value="${param.applicantId}"/>
	<x:set name="setupInterviewer.form" property="applicantId" value="${param.applicantId}"/>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.setupInterviewerForm"/> 
</c:set>
 
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	
	<table border="0" width="100%">
	<tr>
	<td>
		<x:display name="setupInterviewer.list"/>
	</td>
	</tr>
	</table>
	
	<table border="0" width="80%">
	<tr>
	<td align="center">
		<x:display name="setupInterviewer.form"/> 
	</td>
	</tr>	
	</table>
	
	<table border="0" width="100%">
	<tr>
	<td>
		*Note: Interviewer Name's are from Recruit Interviewers. HOD can also be one of the interviewer.
	</td>
	</tr>
	</table>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>