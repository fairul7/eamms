<%@ page import="com.tms.cms.profile.ui.ContentProfileDesignerPanel,
                 kacang.stdui.Form"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageProfile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
  <page name="profileDesigner">
    <portlet name="contentProfilePortlet" text="<fmt:message key='cms.label.profile'/>" width="100%" permanent="true">
        <com.tms.cms.profile.ui.ContentProfileDesignerPanel name="panel" width="100%" />
    </portlet>
  </page>
</x:config>

<c-rt:set var="forwardSave" value="<%= ContentProfileDesignerPanel.FORWARD_SAVE %>"/>
<c-rt:set var="forwardCancel" value="<%= Form.CANCEL_FORM_ACTION %>"/>
<c:choose>
<c:when test="${ !empty param.id }">
    <x:set name="profileDesigner.contentProfilePortlet.panel" property="profileId" value="${param.id}"/>
</c:when>
<c:when test="${ forward.name == forwardSave }">
    <c:redirect url="profileList.jsp"/>
</c:when>
<c:when test="${ forward.name == forwardCancel }">
    <c:redirect url="profileList.jsp"/>
</c:when>
</c:choose>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideContentProfile.jsp" flush="true" />

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">
                <br>

                  <%-- Content Listing --%>
                  <x:display name="profileDesigner.contentProfilePortlet"/>
                <p> <br>
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
