<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.maillist.ManageMailList" module="com.tms.cms.maillist.model.MailListModule" url="noPermission.jsp" />
<x:config>
    <page name="page">
        <portlet name="portlet" text="<fmt:message key='maillist.label.mailingLists'/>" width="100%" permanent="true">
            <com.tms.cms.maillist.ui.MailListUi name="mailListUi" showMenu="false" />
        </portlet>
    </page>
</x:config>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var = "form">
    <x:display name="page.portlet.mailListUi"/>
</c:set>
<c:set var="bodyTitle" scope="request"><fmt:message key="maillist.label.mailingLists"/> > <c:out value="${widgets['page.portlet.mailListUi'].title}"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
<c:out value="${form}" escapeXml="false" />
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>