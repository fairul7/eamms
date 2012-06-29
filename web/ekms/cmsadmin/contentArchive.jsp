<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentArchive">
    <portlet name="archiveContentPortlet" text="<fmt:message key='cms.label.archiveContent'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ArchiveContentObjectPanel name="archiveContentPanel" width="100%">
            <forward name="archive" url="contentView.jsp" redirect="true"/>
            <forward name="unarchive" url="contentView.jsp" redirect="true"/>
            <forward name="cancel_form_action" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.ArchiveContentObjectPanel>
    </portlet>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%-- Archive Content --%>
                  <x:display name="contentArchive.archiveContentPortlet"/>
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

