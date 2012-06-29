<%@include file="/common/header.jsp" %>

<x:config >
	<page name="vacancy">
		<com.tms.hr.recruit.ui.VacancyList name="list"/>
    </page>
</x:config>

<c:if test="${forward.name=='add'}">
    <script type="text/javascript">
        <c:redirect url="vacancy.jsp"/>
    </script>
</c:if>

<c:if test="${!empty param.vacancyCode}">
    <c:redirect url="vacancyEdit.jsp?vacancyCode=${param.vacancyCode}"/>        
</c:if>

<c:if test="${!empty param.vacancyCodeApply}">
    <c:redirect url="applicantListing.jsp?vacancyCodeApply=${param.vacancyCodeApply}"/>        
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.vacancyListing"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="vacancy.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>