<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 com.tms.hr.recruit.model.*"%>
<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="jobOffer">
    	<com.tms.hr.recruit.ui.JobOfferList name="list"/>
    </page>
</x:config>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	
	RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
	boolean recruitHod = rm.validateHod(userId);
	pageContext.setAttribute("recruitHod", recruitHod);
%>
	<x:set name="jobOffer.list" property="hasRecruitHod" value="${recruitHod}" />  

<!-- Handle Events -->
<c:choose>
	
	<c:when test="${!empty param.vacancyCode}">
		<c:redirect url="vacancyEdit.jsp?vacancyCode=${param.vacancyCode}"/>
	</c:when>
	<c:when test="${!empty param.applicantId}">
	<% 
	if(recruitHod){
	%>
	    <c:redirect url="jobOfferFormView.jsp?applicantId=${param.applicantId}"/>
	<% 
	}else{
	%>
		<c:redirect url="jobOfferForm.jsp?applicantId=${param.applicantId}"/> 
	<%
	}
	%>
	</c:when>
	<c:when test="${forward.name == 'deleted'}" >	
		<script type="text/javascript">
       		 alert("<fmt:message key="recruit.applicantStatus.label.delete"/>");
    	</script>
	</c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.jobOfferListing"/>
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="jobOffer.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>