<%@ include file="/common/header.jsp" %>

<x:permission permission="kacang.services.log.Entry.view" module="kacang.services.log.LogService" url="noPermission.jsp" />
<x:config>
    <page name="logs">
        <portlet name="portlet" text="Logs" width="100%" permanent="true">
            <kacang.services.log.ui.LogTable name="table" sort="entryDate" desc="true"/>
        </portlet>
    </page>
</x:config>
<c:if test="${!empty param.entryId}">
	<c:redirect url="logsOpen.jsp?entryId=${param.entryId}"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="general.label.systemLogs"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="logs.portlet.table"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
