<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="vacancyTemp">
		<com.tms.hr.recruit.ui.VacancyTempFormEdit name="Edit"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<c:when test="${forward.name == 'updated'}" >	
		<script type="text/javascript">
        alert("<fmt:message key="recruit.vacancyTemplate.alert.updated"/>");
        window.location="vacancyTempList.jsp";       
    	</script>
	</c:when>	
	<c:when test="${!empty param.vacancyTempCode}">
		<x:set name="vacancyTemp.Edit" property="vacancyTempCode" value="${param.vacancyTempCode}"/>
	</c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.editVacancyTemplate"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="vacancyTemp.Edit"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>