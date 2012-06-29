<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.ConflictForm,
                 com.tms.collab.calendar.model.ConflictException,
                 com.tms.collab.calendar.model.CalendarEvent,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:config >
    <page name="conflictpage">
        <com.tms.collab.calendar.ui.ConflictForm name="conflictform"/>
    </page>
</x:config>

<c:if test="${forward.name == 'cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<c-rt:set var="viewuser" value="<%=ConflictForm.FORWARD_VIEW_USER%>"/>
<c-rt:set var="viewresource" value="<%=ConflictForm.FORWARD_VIEW_RESOURCE%>"/>
<c-rt:set var="viewall" value="<%=ConflictForm.FORWARD_VIEW_ALL%>"/>

<c:choose>
    <c:when test="${forward.name == 'added'}" >
        <script>
            <%      WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
                ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
                String header = cview.getHeader();
                session.setAttribute("conflicted",null);
              %>
            alert("<%=header%> <fmt:message key='calendar.message.addedSuccessfully'/>");
            window.opener.location.reload();
            window.close();
        </script>
    </c:when>
    <c:when test="${forward.name == 'updated'}" >
        <script>
            <%
                WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
                ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
                String header = cview.getHeader();
                session.setAttribute("conflicted",null);

              %>
            alert("<%=header%> <fmt:message key='calendar.message.updatedSuccessfully'/>");
            document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(cview.getEvent().getEventId());%>";
        </script>
    </c:when>
    <c:when test="${forward.name == viewall}" >
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            if(wm!=null){
                ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
                if(cview!=null){
                    session.setAttribute("conflictList",cview.getConflicts());
                    session.setAttribute("userId","");
                    session.setAttribute("resourceId","");
                    session.setAttribute("resourceList",cview.getResourceConflicts());
                }
            }
        %>
        <script>
            window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
        </script>
    </c:when>
    <c:when test="${forward.name==viewuser}" >
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            if(wm!=null){
                ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
                if(cview!=null){
                    session.setAttribute("conflictList",cview.getConflicts());
                    session.setAttribute("userId",cview.getViewUserId());
                    session.setAttribute("resourceId","");
                    session.setAttribute("resourceList",null);
                }
            }
        %>
        <script>
            window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
        </script>
    </c:when>
    <c:when test="${forward.name == viewresource}" >
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            if(wm!=null){
                ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
                if(cview!=null){
                    /*
                                    session.setAttribute("conflictList",cview.getConflicts());
                    */
                    session.setAttribute("userId"," ");
                    session.setAttribute("resourceId",cview.getViewResourceId());
                    session.setAttribute("resourceList",cview.getResourceConflicts());
                }
            }
        %>
        <script>
            window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
        </script>
    </c:when>
    <c:otherwise>
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            if(wm!=null){
                ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
                if(cview!=null){
                    ConflictException exception = (ConflictException)session.getAttribute("conflict");
                    CalendarEvent d= ((CalendarEvent)session.getAttribute("event"));
                    if(d!=null)
                    {
                        cview.setEvent(d);
                    }
                    cview.setConflicts(exception.getConflictList());
                    cview.setResourceConflicts(exception.getResourcesList());
                    cview.setUpdate(((Boolean)session.getAttribute("edit")).booleanValue());
                }
            }
        %>
    </c:otherwise>
</c:choose>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.conflicts'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="calendarRow">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <Tr>
        <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key='calendar.label.conflictsDetected'/>
        </font></b></td>

    </tr>     <Tr><td>

    <x:display name="conflictpage.conflictform" >
</td></tr></table></x:display>
</body>
</html>
