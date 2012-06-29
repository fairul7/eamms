<%@ page import="com.tms.cms.profile.ui.AddContentProfileForm"%>

<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageProfile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
  <page name="profileList">
    <portlet name="contentProfilePortlet" text="<fmt:message key='cms.label.profileList'/>" width="100%" permanent="true">
        <com.tms.cms.profile.ui.ContentProfileTable name="table" width="100%"/>
    </portlet>
  </page>
</x:config>

<c-rt:set var="forwardAdd" value="<%= AddContentProfileForm.FORWARD_ADD %>"/>
<c:choose>
<c:when test="${ forward.name == forwardAdd }">
    <c:redirect url="profileAdd.jsp"/>
</c:when>
<c:when test="${ param.et=='sel' && !empty param.id}">
    <c:redirect url="profileEdit.jsp?id=${param.id}"/>
</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.administration"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                  <%-- Content Profiles --%>
                  <x:display name="profileList.contentProfilePortlet"/>
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

