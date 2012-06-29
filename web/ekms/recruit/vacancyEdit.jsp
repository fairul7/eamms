<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="vacancy">
		<com.tms.hr.recruit.ui.VacancyFormEdit name="Edit"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<c:when test="${forward.name == 'updated'}" >	
		<script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.updated"/>");
        window.location="vacancyList.jsp";       
    	</script>
	</c:when>	
	<c:when test="${!empty param.vacancyCode}">
		<x:set name="vacancy.Edit" property="vacancyCode" value="${param.vacancyCode}"/>
	</c:when>
	<c:when test="${forward.name=='cancel_form_action'}">
    	<c:redirect url="vacancyList.jsp"/>
	</c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.editVacancy"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="vacancy.Edit"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>