<%@ page import="kacang.Application,
                 org.apache.commons.collections.SequencedHashMap,
                 java.util.*,
                 com.tms.hr.recruit.model.RecruitAppDao,
                 com.tms.hr.recruit.model.RecruitAppModule,
                 java.io.PrintWriter"%>
                 
<%@include file="/common/header.jsp" %>

<x:config >
	<page name="jobApplicationFormView">
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
		<com.tms.hr.recruit.ui.JobApplicationFormDelete name="Delete"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:if test="${!empty param.applicantId}">
    <x:set name="jobApplicationFormView.Delete" property="applicantId" value="${param.applicantId}"/>
    <x:set name="jobApplicationFormView.tab1.panel1.view" property="applicantId" value="${param.applicantId}"/>
    <c:set scope="session" value="${param.applicantId}" var="getApplicantIdp"/>
</c:if>

<c:if test="${forward.name == 'cannotDelete'}" >	
	<script type="text/javascript">
       	alert("<fmt:message key="recruit.applicantStatus.label.jafCannotDelete"/>");	
    </script>
</c:if>

<c:if test="${forward.name == 'JAFdeleted'}" >	
	<script type="text/javascript">
       	alert("<fmt:message key="recruit.applicantStatus.label.jafDeleted"/>");
       	window.location="careerList.jsp";	 
    </script>
</c:if>

<%
	String applicantIdSs = Application.getInstance().getThreadRequest().getSession().getAttribute("getApplicantIdp").toString();
	String needRedirect="";

	Application app = Application.getInstance();
	RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
	Collection col = ram.loadApplicantPersonal(applicantIdSs);
	if(col!=null && col.size() > 0)
		needRedirect = "no";
	else
		needRedirect ="yes";
		
	pageContext.setAttribute("needRedirect",needRedirect);
%>

<c:if test="${needRedirect eq 'yes' }" >
	<script>
		alert("<fmt:message key="recruit.applicantStatus.label.jafNotFound"/>");
       	window.location="careerList.jsp";	 
	</script>
</c:if>	

<jsp:include page="includes/header.jsp" flush="true" />

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.applicant.jobAppForm"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
<c:if test="${needRedirect  eq 'no' }" >
	<x:display name="jobApplicationFormView.tab1"/>
	<x:display name="jobApplicationFormView.Delete"/>
</c:if>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<jsp:include page="includes/footer.jsp" flush="true" />