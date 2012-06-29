<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.permission.fmsAdmin" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="abwEmailsSetup">
        <com.tms.fms.transport.ui.AbwEmailsSetup name="form"/>
    </page>
</x:config>

<c:if test="${forward.name == 'updateSuccessfully'}">
    <script>
         alert('<fmt:message key="fms.administration.alert.updateSuccessfully"/>');
         document.location="abwEmailsSetup.jsp";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="fms.administration.msg.abwAdminEmailSetup"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="abwEmailsSetup.form"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
