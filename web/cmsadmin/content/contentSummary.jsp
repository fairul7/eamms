<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:config>
<page name="cms">
    <%@ include file="/cmsadmin/content/contentDefinition.jsp" %>
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

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideContentModules.jsp" flush="true" />

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">

                  <%-- Quick Search --%>
                  <jsp:include page="contentSummaryQuickSearch.jsp" flush="true" />

                  <%-- Content Status Summary --%>
                  <x:display name="cms.contentSummaryPortlet"/>
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
              </td>
            </tr>
          </tbody>
        </table></td>
    </tr>
  </tbody>
</table>

<jsp:include page="/cmsadmin/includes/footerContent.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>
