<%@ include file="/common/header.jsp" %>

<x:permission permission="kacang.services.security.User.view" module="kacang.services.security.SecurityService" url="noPermission.jsp" />

<x:config>
    <page name="user">
        <portlet name="userPortlet" text="" width="100%" permanent="true">
            <kacang.services.security.ui.UserForm name="userForm"/>
        </portlet>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>

<c:choose>
    <c:when test="${!empty param.new && param.new == 1}">
        <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.users"/> > <fmt:message key="security.label.newUser"/></c:set>
    </c:when>
    <c:when test="${!empty param.new && param.new == 2}">
        <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.users"/> > <fmt:message key="security.label.userListing"/></c:set>
    </c:when>
    <c:when test="${!empty param.id}">
        <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.users"/> > <fmt:message key="security.label.editUser"/></c:set>
    </c:when>
    <c:otherwise>
        <c:set var="bodyTitle" scope="request"><fmt:message key="security.label.users"/> > <fmt:message key="security.label.newUser"/></c:set>
    </c:otherwise>
</c:choose>

<%--<c:set var="bodyTitle" scope="request"><fmt:message key="security.label.users"/></c:set>--%>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="user.userPortlet.userForm"/>
<br>
<br>


<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
