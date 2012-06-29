<%@ include file="/common/header.jsp" %>

<x:permission permission="kacang.services.security.Group.view" module="kacang.services.security.SecurityService" url="noPermission.jsp" />

<x:config>
    <page name="group">
        <portlet name="groupPortlet" text="<fmt:message key='security.label.groups'/>" width="100%" permanent="true">
            <kacang.services.security.ui.GroupForm name="groupForm"/>
        </portlet>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>

<c:choose>
    <c:when test="${!empty param.new && param.new == 1}">
        <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.groups"/> > <fmt:message key="security.label.newGroup"/></c:set>
    </c:when>
    <c:when test="${!empty param.new && param.new == 2}">
        <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.groups"/> > <fmt:message key="security.label.groupListing"/></c:set>
    </c:when>
    <c:when test="${!empty param.id}">
          <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.users"/> > <fmt:message key="security.label.editGroup"/></c:set>

      </c:when>

    <c:otherwise>
        <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.groups"/> > <fmt:message key="security.label.newGroup"/></c:set>
    </c:otherwise>
</c:choose>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="group.groupPortlet.groupForm"/>
<br>
<br>


<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
