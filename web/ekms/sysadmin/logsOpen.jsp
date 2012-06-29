<%@ page import="kacang.services.log.ui.LogView"%>
<%@ include file="/common/header.jsp" %> 
<x:permission permission="kacang.services.log.Entry.view" module="kacang.services.log.LogService" url="noPermission.jsp" />
<x:config>
    <page name="logsOpen">
        <portlet name="portlet" text="Log Entry" width="100%" permanent="true">
           <!--  kacang.services.log.ui.LogView name="view" / -->
           <com.tms.ekms.setup.ui.LogView name="view" />
        </portlet>
    </page>
</x:config>
<c-rt:set var="forward_done" value="<%= LogView.FORWARD_DONE %>"/>  
<c:if test="${!empty param.entryId}">
	<x:set name="logsOpen.portlet.view" property="entryId" value="${param.entryId}"/>
</c:if>
<c:if test="${forward_done == forward.name}">
	<c:redirect url="logs.jsp"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="general.label.systemLogs"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="logsOpen.portlet.view"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
