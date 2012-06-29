<%@include file="/common/header.jsp" %>

<x:config >
	<page name="jobApplication">
		<com.tms.hr.recruit.ui.JobApplicationFormGetSession name="ValidateSession"/>	
		<tabbedpanel name="tab1" width="100%">
			<panel name="panel1" text="<fmt:message key="recruit.general.label.tabPersonal"/>" >
				<com.tms.hr.recruit.ui.JobAppPersonal name="Form"/>
			</panel>
			<panel name="panel2" text="<fmt:message key="recruit.general.label.tabEdu"/>" >
				<com.tms.hr.recruit.ui.JobAppEduList name="List" width="100%" />
				<com.tms.hr.recruit.ui.JobAppEdu name="Form"/>
			</panel>
			<panel name="panel3" text="<fmt:message key="recruit.general.label.tabWorkingExp"/>" >
				<com.tms.hr.recruit.ui.JobAppWorkingExpType name="Type" width="100%" />
				<com.tms.hr.recruit.ui.JobAppWorkingExpList name="List" width="100%" />
				<com.tms.hr.recruit.ui.JobAppWorkingExp name="Form"/>
			</panel>
			<panel name="panel4" text="<fmt:message key="recruit.general.label.tabSkill"/>" >
				<com.tms.hr.recruit.ui.JobAppSkillList name="List" width="100%" />
				<com.tms.hr.recruit.ui.JobAppSkill name="Form"/>
			</panel>
			<panel name="panel5" text="<fmt:message key="recruit.general.label.tabLanguage"/>" >
				<com.tms.hr.recruit.ui.JobAppLanguageList name="List" width="100%" />
				<com.tms.hr.recruit.ui.JobAppLanguage name="Form"/>
			</panel>
			<panel name="panel6" text="<fmt:message key="recruit.general.label.tabAdditional"/>" >
				<com.tms.hr.recruit.ui.JobAppAdditional name="Form"/>
			</panel>
		</tabbedpanel>
		<com.tms.hr.recruit.ui.JobApplicationForm name="SubmitForm"/>	
    </page>
</x:config>

<!-- Handle Events -->
<c:if test="${!empty param.vacancyCode}">
	<%--<x:set name="jobApplication.ValidateSession" property="vacancyCodeE" value="${param.vacancyCode}"/>
    <x:set name="jobApplication.ValidateSession" property="applicantIdE" value="${param.applicantIds}"/>--%>
    <x:set name="jobApplication.ValidateSession" property="codeStatusE" value="add"/>  
    <x:set name="jobApplication.tab1.panel1.Form" property="vacancyCode" value="${param.vacancyCode}"/>        
</c:if>

<c:if test="${!empty param.eduId}">
    <x:set name="jobApplication.tab1.panel2.Form" property="eduId" value="${param.eduId}"/>
</c:if>

<c:if test="${!empty param.empId}">
    <x:set name="jobApplication.tab1.panel3.Form" property="empId" value="${param.empId}"/>
</c:if>

<c:if test="${!empty param.skillId}">
    <x:set name="jobApplication.tab1.panel4.Form" property="skillId" value="${param.skillId}"/>
</c:if>

<c:if test="${!empty param.languageId}">
    <x:set name="jobApplication.tab1.panel5.Form" property="languageId" value="${param.languageId}"/>
</c:if>

<c:if test="${!empty param.remove}">
    <x:set name="jobApplication.tab1.panel6.Form" property="removeFile" value="${param.remove}"/>
</c:if>


<c:if test="${forward.name=='personalSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.personalSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='eduSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.eduSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='workingExpTypeNonFreshSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.workingExpTypeNonFreshSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='workingExpTypeFreshSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.workingExpTypeFreshSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='workingExpTypeSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.workingExpTypeSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='workingExpSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.workingExpSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='skillSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.skillSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='languageSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.languageSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='additionalSave'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.personal.alert.additionalSave"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='jFormSubmit'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.jobAppForm.alert.jFormSubmit"/>");
        window.location="careerList.jsp";     
    </script>
</c:if>

<!-- tab error -->
<c:if test="${forward.name=='emptyForm_Personal Details'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.jobAppForm.alert.emptyForm_PersonalDetails"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='emptyForm_Education Details'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.jobAppForm.alert.emptyForm_EducationDetails"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='emptyForm_Working Experience'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.jobAppForm.alert.emptyForm_WorkingExperience"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='emptyForm_Skill'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.jobAppForm.alert.emptyForm_Skill"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='emptyForm_language'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.jobAppForm.alert.emptyForm_language"/>"); 
    </script>
</c:if>

<c:if test="${forward.name=='emptyForm_Additional Info'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.jobAppForm.alert.emptyForm_AdditionalInfo"/>"); 
    </script>
</c:if>
<!-- tab error -->


<jsp:include page="includes/header.jsp" flush="true" />

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.applicant.jobAppForm"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="jobApplication.ValidateSession"/>
	<x:display name="jobApplication.tab1"/>
	<x:display name="jobApplication.SubmitForm"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<jsp:include page="includes/footer.jsp" flush="true" />