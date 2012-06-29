<x:permission var="isHelpdeskAdmin" module="com.tms.crm.helpdesk.HelpdeskHandler" permission="com.tms.crm.helpdesk.Admin"/>
<c:if test="${!isHelpdeskAdmin}">
    <c:redirect url="noPermission.jsp"/>
</c:if>