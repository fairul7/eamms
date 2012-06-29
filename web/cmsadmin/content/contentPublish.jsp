<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentPublish">
    <portlet name="publishContentPortlet" text="<fmt:message key='general.label.publishWithdrawContent'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.PublishContentObjectPanel name="publishContentPanel" width="100%">
            <forward name="publish" url="contentView.jsp" redirect="true"/>
            <forward name="withdraw" url="contentView.jsp" redirect="true"/>
            <forward name="cancel_form_action" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.PublishContentObjectPanel>
    </portlet>
</page>
</x:config>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
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
                  <x:display name="contentPublish.publishContentPortlet"/>
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
    </tr>
  </tbody>
</table>

<jsp:include page="/cmsadmin/includes/footerContent.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>
