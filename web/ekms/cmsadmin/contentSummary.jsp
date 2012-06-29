<%@ include file="/common/header.jsp" %>

<x:config>
<page name="cms">
    <%@ include file="/ekms/cmsadmin/contentDefinition.jsp" %>
    <portlet name="contentSummaryPortlet" text="<fmt:message key='cms.label.contentSummary'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentSummaryPanel name="contentSummaryPanel" width="100%" />
    </portlet>
    <portlet name="contentCheckedOutPortlet" text="<fmt:message key='cms.label.contentCheckedOut'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentCheckedOutTable name="contentCheckedOutTable" width="100%" sort="date" desc="true">
            <forward name="selection" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.ContentCheckedOutTable>
    </portlet>
    <portlet name="contentSubmittedPortlet" text="<fmt:message key='cms.label.contentToApprove'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentSubmittedTable name="contentSubmittedTable" width="100%" sort="date" desc="true">
            <forward name="selection" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.ContentSubmittedTable>
    </portlet>
    <portlet name="contentApprovedPortlet" text="<fmt:message key='cms.label.contentToPublish'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentApprovedTable name="contentApprovedTable" width="100%" sort="date" desc="true">
            <forward name="selection" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.ContentApprovedTable>
    </portlet>
</page>
</x:config>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="cms.label.contentSummary"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
                  <%-- Content Status Summary --%>
                  <hr size="1">
                  <x:display name="cms.contentSummaryPortlet.contentSummaryPanel"/>
                  <hr size="1">
                  <p>
                  <%-- Content Checked Out --%>
                  <x:display name="cms.contentCheckedOutPortlet"/>
                  <p>
                  <%-- Content To Approve --%>
                  <x:display name="cms.contentSubmittedPortlet"/>
                  <p>
                  <%-- Content To Publish --%>
                  <x:display name="cms.contentApprovedPortlet"/>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
