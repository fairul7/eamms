<%@ include file="/common/header.jsp" %>

<x:permission var="accessRecycleBin" module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.AccessRecycleBin" />
<c:if test="${!accessRecycleBin}">
    <x:permission var="accessRecycleBin" module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.ManageRecycleBin" />
</c:if>
<c:choose>
<c:when test="${accessRecycleBin}">

<x:config>
<page name="contentDeleted">
    <portlet name="contentDeletedPortlet" text="<fmt:message key='cms.label.deletedContent'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentDeletedTable name="contentDeletedTable" width="100%">
            <forward name="selection" url="contentView.jsp" redirect="false"/>
        </com.tms.cms.core.ui.ContentDeletedTable>
    </portlet>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.administration"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                  <%--Content Deleted Portlet--%>
                  <x:display name="contentDeleted.contentDeletedPortlet"/>
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


</c:when>
<c:otherwise>
    <c:redirect url="noPermission.jsp" />
</c:otherwise>
</c:choose>