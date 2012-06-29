<%@include file="/common/header.jsp" %>

<x:config >
	<page name="carrer">
		<com.tms.hr.recruit.ui.CarrerList name="list"/>
    </page>
</x:config>

<%--<c:if test="${forward.name=='add'}">
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
--%>

<jsp:include page="includes/header.jsp" flush="true" />

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.eOpportunities"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
	<x:display name="carrer.list"/>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<jsp:include page="includes/footer.jsp" flush="true" />