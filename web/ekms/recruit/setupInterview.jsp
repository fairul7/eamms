<%@include file="/common/header.jsp" %>

<%@include file="/ekms/recruit/popupCenterJs.js" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="setupInterview">
		<com.tms.hr.recruit.ui.SetupInterviewDateList name="list" />
		<com.tms.hr.recruit.ui.SetupInterviewForm name="form" />
    </page>
</x:config>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="applicantListing.jsp" />
</c:if>

<c:if test="${forward.name=='sendAndSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.mailSend"/>");
        window.location="vacancyList.jsp";       
    </script>
</c:if>

<c:if test="${forward.name=='interviewDateTimeNotEntered'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.applicantStatus.label.interviewDateTimeNotEntered"/>");
    </script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.setupIntervieweeDate"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="setupInterview.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.setupInterviewForm"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="setupInterview.form"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>