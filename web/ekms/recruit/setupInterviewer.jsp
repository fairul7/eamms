<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="setupInterviewer">
		<com.tms.hr.recruit.ui.SetupIntervieweeList name="list" />
		<com.tms.hr.recruit.ui.SetupInterviewerForm name="form" />
    </page>
</x:config>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="intervieweeList.jsp" />
</c:if>

<c:if test="${forward.name=='submit'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.setInterviewer"/>");
        window.location="intervieweeList.jsp";       
    </script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.setupInterviewerForm"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="setupInterviewer.list"/>
	<x:display name="setupInterviewer.form"/>
	<p>
	<fmt:message key="recruit.menu.label.interviewersNote"/>
	</p>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>