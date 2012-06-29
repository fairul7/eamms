<%@ page import="com.tms.collab.forum.model.ForumModule,
                 java.util.Map"%>
<%@ include file="/common/header.jsp" %>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<%
    ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    Map settings = module.getSettings(service.getCurrentUser(request).getId());
    if(ForumModule.FORUM_TYPE_NON_THREADED.equals(settings.get(ForumModule.SETTINGS_LABEL_TYPE)))
    {
%>
    <x:template type="TemplateDisplayForumMessageList" />
<%
    }
    else
    {
%>
    <x:template type="TemplateDisplayForumMessageTree" />
<%
    }
%>
<%--<x:template type="TemplateDisplaySetup" name="setup" scope="request"/>
<c:choose>
    <c:when test="${setup.propertyMap.siteThreadedForum}">
        <x:template type="TemplateDisplayForumMessageTree" />
    </c:when>
    <c:otherwise>
        <x:template type="TemplateDisplayForumMessageList" />
    </c:otherwise>
</c:choose>--%>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>