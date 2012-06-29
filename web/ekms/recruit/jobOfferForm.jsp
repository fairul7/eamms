<%@include file="/common/header.jsp" %>

<%@include file="/ekms/recruit/popupCenterJs.js" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="jobOffer">
	<tabbedpanel name="tab1" width="100%">
			<panel name="panel1" text="Personal Details">
				<com.tms.hr.recruit.ui.JobAppPersonalView name="view"/>
			</panel>
			<panel name="panel2" text="Education Details">
				<com.tms.hr.recruit.ui.JobAppEduListView name="list" width="100%" />
			</panel>
			<panel name="panel3" text="Working Experience">
				<com.tms.hr.recruit.ui.JobAppWorkingExpTypeView name="type" width="100%" />
				<%-- <c:if test="${!widgets['jobApplicationForm.tab1.panel3.type'].hasWorkingExp}"> --%>
				<com.tms.hr.recruit.ui.JobAppWorkingExpListView name="list" width="100%" />
			</panel>
			<panel name="panel4" text="Skill">
				<com.tms.hr.recruit.ui.JobAppSkillListView name="List" width="100%" />
			</panel>
			<panel name="panel5" text="language">
				<com.tms.hr.recruit.ui.JobAppLanguageListView name="List" width="100%" />
			</panel>
			<panel name="panel6" text="Additional Info">
				<com.tms.hr.recruit.ui.JobAppAdditionalView name="Form"/>
			</panel>
		</tabbedpanel>
		<com.tms.hr.recruit.ui.InterviewHistory name="list" />
    	<com.tms.hr.recruit.ui.JobOfferForm name="form"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<c:when test="${forward.name=='cancel_form_action'}">
		<c:redirect url="jobOfferList.jsp" />
	</c:when>
	<c:when test="${forward.name == 'submit'}" >	
		<script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.jobOfferSubmited"/>");
        window.location="jobOfferList.jsp";       
    	</script>
	</c:when>
	<c:when test="${forward.name == 'offerletterNotSent'}" >	
		<script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.offerletterNotSent"/>");
    	</script>
	</c:when>
	<c:when test="${!empty param.applicantId}">
		<x:set name="jobOffer.tab1.panel1.view" property="applicantId" value="${param.applicantId}"/>
		<x:set name="jobOffer.list" property="applicantId" value="${param.applicantId}"/> 
		<x:set name="jobOffer.form" property="applicantId" value="${param.applicantId}"/>
	</c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<%-- 
<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.general.label.applicantDetails"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="jobOffer.tab1"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>	
--%>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.general.label.interviewedHistory"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="jobOffer.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>	

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.jobOfferForm"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="jobOffer.form"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>