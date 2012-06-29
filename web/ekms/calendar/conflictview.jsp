<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.ConflictForm,
                 com.tms.collab.calendar.ui.ConflictView,
                 java.util.Collection,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:config >
    <page name="conflictviewpage">
        <com.tms.collab.calendar.ui.ConflictView name="conflictview"/>
    </page>
</x:config>

<c:if test="${forward.name=='select'}" >
    <c:redirect url="/ekms/calendar/eventview.jsp?eventId=${eventId}" ></c:redirect>
</c:if>

<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if(wm!=null){
        ConflictView cview = (ConflictView)wm.getWidget("conflictviewpage.conflictview");
        if(cview!=null){
            cview.setConflicts((Collection)session.getAttribute("conflictList"));
            cview.setUserId((String)session.getAttribute("userId"));
            cview.setResourceId((String)session.getAttribute("resourceId"));
            cview.setResourceConflicts((Collection)session.getAttribute("resourceList"));
        }
    }
%>

<html>
<title><fmt:message key='calendar.label.conflicts'/></title>
<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
<body>
<x:display name="conflictviewpage.conflictview" ></x:display>
</body>
</html>
