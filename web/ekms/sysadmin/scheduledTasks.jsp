<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ScheduledTasks" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
    <page name="scheduledTasks">
        <portlet name="jobPortlet" text="<fmt:message key='siteadmin.label.scheduledTasks'/>" width="100%" permanent="true">
            <kacang.services.scheduling.ui.JobScheduleTable name="jobTable" width="100%" />
        </portlet>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="siteadmin.label.scheduledTasks"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

      <x:display name="scheduledTasks.jobPortlet.jobTable" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>

