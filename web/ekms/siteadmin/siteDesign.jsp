<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.SiteDesign" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
    <page name="templates">
        <portlet name="contentTemplatePortlet" text="<fmt:message key='siteadmin.label.siteDesign'/>" width="100%" permanent="true">
            <com.tms.cms.core.ui.ContentTemplateForm name="contentTemplateForm" width="100%" />
        </portlet>
    </page>
</x:config>

<c:if test="${forward.name == 'siteTemplate'}">
    <x:flush />
    <c:redirect url=""/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.websiteManagement"/> > <fmt:message key="siteadmin.label.siteDesign"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

      <x:display name="templates.contentTemplatePortlet.contentTemplateForm" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
