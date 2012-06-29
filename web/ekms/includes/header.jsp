<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.ekms.security.ui.LoginForm,
                 kacang.runtime.filter.PresenceFilter"%>


<c-rt:set var="kick" value="<%= PresenceFilter.ATTRIBUTE_KICK %>"/>
<c:if test="${! empty requestScope[kick]}">
    <c:redirect url="/ekms/kick.jsp"/>
</c:if>
<c-rt:set var="anonymous" value="<%= SecurityService.ANONYMOUS_USER_ID %>"/>
<c:set var="validUser" value="true" />
<c:choose>
    <c:when test="${((!validUser) || (sessionScope.currentUser.id == anonymous))}">
        <c:redirect url="/ekms/login.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:include page="/ekms/init.jsp" flush="true"/>
        <c-rt:set var="header_property" value="<%= PortalServerUtil.PROPERTY_HEADER %>"/>
        <modules:portalserverutil name="portal.server" property="${header_property}" var="ekmsHeader"/>
        <c:if test="${!empty ekmsHeader}">
            <c:import url="${ekmsHeader}"/>
        </c:if>
    </c:otherwise>
</c:choose>