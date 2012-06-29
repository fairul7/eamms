<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="setupMessageBody">
		<com.tms.hr.recruit.ui.SetupMessageBody name="Form"/>
    </page>
</x:config>

<c:if test="${forward.name=='add'}">
    <script type="text/javascript">
        <c:redirect url="vacancyTemp.jsp"/>
    </script>
</c:if>
 
<%@ include file="/ekms/includes/header.jsp" %>

<c:set var="bodyTitle" scope="request">
	<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.globalSetup"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
		<x:display name="setupMessageBody.Form"/>	
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<%@ include file="/ekms/includes/footer.jsp" %>