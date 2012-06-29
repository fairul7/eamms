<%@include file="/common/header.jsp" %>

<x:config >
	<page name="jobApplicationForm">
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
    </page>
</x:config>

<!-- Handle Events -->
<c:if test="${!empty param.applicantId}">
    <x:set name="jobApplicationForm.tab1.panel1.view" property="applicantId" value="${param.applicantId}"/>  
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.recruit"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="jobApplicationForm.tab1"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>