<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentSecurity">
    <portlet name="contentPermissionsPortlet" text="<fmt:message key='cms.label.contentPermissions'/>" width="100%" permanent="true" columns="2">
        <com.tms.cms.core.ui.ViewContentPermissionsPanel name="viewContentPermissionsPanel" />
        <com.tms.cms.core.ui.EditContentPermissionsPanel name="editContentPermissionsPanel">
            <forward name="cancel_form_action" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.EditContentPermissionsPanel>
    </portlet>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%-- Content Permissions --%>
                  <x:display name="contentSecurity.contentPermissionsPortlet"/>
                <p> <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>


