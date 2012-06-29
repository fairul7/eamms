<%@include file="/common/header.jsp" %>
<c:set var="display" value="${widget}"/>
<x:config >
	<page name="carrer">
		<com.tms.hr.recruit.ui.CarrerApply name="apply"/>
    </page>
</x:config>

<c:if test="${forward.name eq 'apply'}">
	<c:redirect url="jobApplicationForm.jsp?vacancyCode=${widgets['carrer.apply'].vacancyCode}"/>
</c:if>

<c:if test="${!empty param.vacancyCode}">
    <x:set name="carrer.apply" property="vacancyCode" value="${param.vacancyCode}"/>        
</c:if>

<jsp:include page="includes/header.jsp" flush="true" />

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.eOpportunities"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="carrer.apply"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<jsp:include page="includes/footer.jsp" flush="true" />