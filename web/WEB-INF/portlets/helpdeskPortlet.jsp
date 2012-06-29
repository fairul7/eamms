<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="hd">
        <com.tms.crm.helpdesk.ui.HelpdeskPortlet name="portlet"/>
    </page>
</x:config>

<script src="<c:url value="/common/tree/tree.js"/>"></script>

<x:permission var="isHelpdeskUser" module="com.tms.crm.helpdesk.HelpdeskHandler" permission="com.tms.crm.helpdesk.User"/>
<x:permission var="isHelpdeskAdmin" module="com.tms.crm.helpdesk.HelpdeskHandler" permission="com.tms.crm.helpdesk.Admin"/>
<c:choose>
<c:when test="${isHelpdeskUser || isHelpdeskAdmin}">
    <x:display name="hd.portlet"/>
</c:when>
<c:otherwise>
    <c:out value="Sorry, you don't have the permission to view this portlet"/>
</c:otherwise>
</c:choose>