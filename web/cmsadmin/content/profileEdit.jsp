<%@ page import="com.tms.cms.profile.ui.EditContentProfileForm"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageProfile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
  <page name="profileEdit">
    <portlet name="contentProfilePortlet" text="<fmt:message key='cms.label.profileEdit'/>" width="100%" permanent="true">
        <com.tms.cms.profile.ui.EditContentProfileForm name="form" />
        <com.tms.cms.profile.ui.ContentProfilePreviewForm name="preview" />
    </portlet>
  </page>
</x:config>

<c-rt:set var="forwardUpdate" value="<%= EditContentProfileForm.FORWARD_UPDATE %>"/>
<c-rt:set var="forwardEditFields" value="<%= EditContentProfileForm.FORWARD_EDIT_FIELDS %>"/>
<c-rt:set var="forwardCancel" value="<%= EditContentProfileForm.CANCEL_FORM_ACTION %>"/>
<c:choose>
<c:when test="${ !empty param.id }">
    <x:set name="profileEdit.contentProfilePortlet.form" property="profileId" value="${param.id}"/>
    <x:set name="profileEdit.contentProfilePortlet.preview" property="profileId" value="${param.id}"/>
</c:when>
<c:when test="${ forward.name == forwardUpdate }">
    <c:redirect url="profileList.jsp"/>
</c:when>
<c:when test="${ forward.name == forwardEditFields }">
    <c:redirect url="profileDesigner.jsp?id=${widgets['profileEdit.contentProfilePortlet.form'].profileId}"/>
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
                  <x:display name="profileEdit.contentProfilePortlet" body="custom">
                    <x:display name="profileEdit.contentProfilePortlet.form" />
                    <p>
                    <b>Preview</b>
                    <hr size="1">
                    <blockquote>
                    <div style="border:dotted 1px gray">
                        <x:display name="profileEdit.contentProfilePortlet.preview" />
                    </div>
                    </blockquote>
                  </x:display>
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
