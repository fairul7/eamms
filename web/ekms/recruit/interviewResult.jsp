<%@include file="/common/header.jsp" %>

<%@include file="/ekms/recruit/popupCenterJs.js" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="interviewResult">
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
		<com.tms.hr.recruit.ui.InterviewHistoryList name="list" />
		<com.tms.hr.recruit.ui.InterviewResult name="form" />
    </page>
</x:config>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="intervieweeList.jsp" />
</c:if>

<c:if test="${forward.name=='submit'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.interviewResultAdded"/>");
        window.location="intervieweeList.jsp";       
    </script>
</c:if>

<%--
<c:if test="${forward.name=='noRemarkGiven'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.noRemarkGiven"/>");     
    </script>
</c:if>

<c:if test="${forward.name=='noInterviewerAssigned'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.noInterviewerAssigned"/>");     
    </script>
</c:if>
--%>
<%--"${!widgets['jobApplicationForm.tab1.panel3.type'].hasWorkingExp}" --%>

<c:if test="${forward.name == 'addInterviewer'}" >	
		<c:redirect url="setupInterviewerForm.jsp?applicantId=${widgets['interviewResult.list'].applicantId}" />
</c:if>

<c:if test="${!empty param.applicantId}">
		<x:set name="interviewResult.tab1.panel1.view" property="applicantId" value="${param.applicantId}"/>  
		<x:set name="interviewResult.list" property="applicantId" value="${param.applicantId}"/>
		<x:set name="interviewResult.form" property="applicantId" value="${param.applicantId}"/>
		<x:set name="interviewResult.list" property="interviewDateId" value="${param.interviewDateId}"/>
		<x:set name="interviewResult.form" property="interviewDateId" value="${param.interviewDateId}"/>
		<x:set name="interviewResult.list" property="totalInterview" value="${param.totalInterview}"/>
		<x:set name="interviewResult.form" property="totalInterview" value="${param.totalInterview}"/>
</c:if>

<%--
<c:if test="${!empty param.interviewDateId}">
		<x:set name="interviewResult.list" property="interviewDateId" value="${param.interviewDateId}"/>
		<x:set name="interviewResult.form" property="interviewDateId" value="${param.interviewDateId}"/>
</c:if>

<c:if test="${!empty param.totalInterview}">
		<x:set name="interviewResult.form" property="totalInterview" value="${param.totalInterview}"/>
</c:if>
--%>

<%@ include file="/ekms/includes/header.jsp" %>

<%--<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.general.label.applicantDetails"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="interviewResult.tab1"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>	
--%>
<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.general.label.interviewedHistory"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
<x:display name="interviewResult.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>	

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.general.label.interviewResult"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>	
	<x:display name="interviewResult.form"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>