<%@include file="/common/header.jsp" %>

<%@include file="/ekms/recruit/popupCenterJs.js" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="vacancyReport">
    	<com.tms.hr.recruit.ui.VacancyReportListView name="detail"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:choose>
	<c:when test="${!empty param.vacancyCode}">
		<x:set name="vacancyReport.detail" property="vacancyCode" value="${param.vacancyCode}"/>
	</c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.vacancyReport"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="vacancyReport.detail"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>