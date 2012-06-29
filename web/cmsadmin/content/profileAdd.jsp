<%@ page import="com.tms.cms.profile.ui.AddContentProfileForm"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageProfile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
  <page name="profileAdd">
    <portlet name="contentProfilePortlet" text="<fmt:message key='cms.label.profileAdd'/>" width="100%" permanent="true">
        <com.tms.cms.profile.ui.AddContentProfileForm name="form" />
    </portlet>
  </page>
</x:config>

<c-rt:set var="forwardAdd" value="<%= AddContentProfileForm.FORWARD_ADD %>"/>
<c-rt:set var="forwardCancel" value="<%= AddContentProfileForm.CANCEL_FORM_ACTION %>"/>
<c:choose>
<c:when test="${ forward.name == forwardAdd }">
    <c:redirect url="profileDesigner.jsp?id=${widgets['profileAdd.contentProfilePortlet.form'].profileId}"/>
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
                  <x:display name="profileAdd.contentProfilePortlet"/>
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
