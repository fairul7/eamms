<%@ page import="com.tms.cms.profile.ui.AddContentProfileForm,
                 com.tms.cms.profile.ui.ContentProfileDesignerPanel,
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

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.administration"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                  <%-- Content Profiles --%>
                  <x:display name="profileDesigner.contentProfilePortlet"/>
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


