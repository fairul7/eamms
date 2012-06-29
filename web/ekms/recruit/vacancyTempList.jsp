<%@include file="/common/header.jsp" %>

<x:config >
	<page name="vacancyTemp">
		<com.tms.hr.recruit.ui.VacancyTempList name="list"/>
    </page>
</x:config>

<c:if test="${forward.name=='add'}">
    <script type="text/javascript">
        <c:redirect url="vacancyTemp.jsp"/>
    </script>
</c:if>

<c:if test="${!empty param.vacancyTempCode}">
    <c:redirect url="vacancyTempEdit.jsp?vacancyTempCode=${param.vacancyTempCode}"/>        
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.vacancyTemplateListing"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="vacancyTemp.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>