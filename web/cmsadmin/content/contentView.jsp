<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentView">
    <portlet name="viewContentPortlet" text="<fmt:message key='cms.label.contentDetails'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ViewContentObjectPanel name="viewContentObjectPanel"/>
    </portlet>
    <portlet name="contentChildrenPortlet" text="<fmt:message key='cms.label.contentChildren'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentChildrenTable name="contentChildrenTable" width="100%">
            <forward name="selection" url="contentView.jsp" redirect="false"/>
            <forward name="move" url="contentMove.jsp" redirect="false"/>
        </com.tms.cms.core.ui.ContentChildrenTable>
    </portlet>
    <com.tms.cms.core.ui.ContentOptionsPanel name="contentOptionsPanel">
        <forward name="Create" url="contentAdd.jsp" redirect="true"/>
        <forward name="CheckOut" url="contentEdit.jsp" redirect="true"/>
        <forward name="Preview" url="contentPreview.jsp" redirect="true"/>
        <forward name="UndoCheckOut" url="contentUndoCheckOut.jsp" redirect="true"/>
        <forward name="Move" url="contentMove.jsp" redirect="true"/>
        <forward name="Keywords" url="contentRelated.jsp" redirect="true"/>
        <forward name="Reorder" url="contentReorder.jsp" redirect="true"/>
        <forward name="Subscriptions" url="contentSubscription.jsp" redirect="true"/>
        <forward name="Approve" url="contentApprove.jsp" redirect="true"/>
        <forward name="Publish" url="contentPublish.jsp" redirect="true"/>
        <forward name="Archive" url="contentArchive.jsp" redirect="true"/>
        <forward name="Delete" url="contentDelete.jsp" redirect="true"/>
        <forward name="AclView" url="contentSecurity.jsp" redirect="true"/>
        <forward name="History" url="contentHistory.jsp" redirect="true"/>
        <forward name="AuditView" url="contentAudit.jsp" redirect="true"/>
        <forward name="ReportView" url="contentStatistics.jsp" redirect="true"/>
    </com.tms.cms.core.ui.ContentOptionsPanel>
</page>
</x:config>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0"
 style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideContentTree.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
      <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>
                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%--Content Details--%>
                  <x:display name="contentView.viewContentPortlet"/>
                  <p>
                  <x:display name="contentView.contentChildrenPortlet"/>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
      <td style="vertical-align: top; width: 200px; padding:10px" nowrap>

          <p>
          <br>
          <br>
          <br>
          <%--Content Options--%>
          <x:display name="contentView.contentOptionsPanel"/>

        </td>
    </tr>
  </tbody>
</table>

<jsp:include page="/cmsadmin/includes/footerContent.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>
