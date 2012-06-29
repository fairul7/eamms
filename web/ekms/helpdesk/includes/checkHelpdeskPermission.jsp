<x:permission var="isHelpdeskUser" module="com.tms.crm.helpdesk.HelpdeskHandler" permission="com.tms.crm.helpdesk.User"/>
<x:permission var="isHelpdeskAdmin" module="com.tms.crm.helpdesk.HelpdeskHandler" permission="com.tms.crm.helpdesk.Admin"/>
<c:if test="${!isHelpdeskUser && !isHelpdeskAdmin}">
    <c:redirect url="noPermission.jsp"/>
</c:if>