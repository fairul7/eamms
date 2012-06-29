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


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.administration"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                  <%-- Content Profiles --%>
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




