<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="appointmentpoppage">
        <calendarform name="AppointmentForm" prefix="com.tms.collab.calendar.model.Appointment"/>
    </page>
</x:config>

<c:if test="${not empty param.id}" >
    <x:set name="appointmentpoppage" property="eventId" value="${param.id}" ></x:set>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<c:if test="${!empty param.init}">
    <%
        String conflicted = (String) session.getAttribute("conflicted");
        if(conflicted==null||!conflicted.equals("1")){
    %>
    <x:set name="appointmentpoppage.AppointmentForm" property="init" value="1"/>
    <%
        }  else{
            out.print("xxxxx");
        }
    %>
</c:if>

<c:if test="${forward.name=='appointment added'}" >
    <%
        session.setAttribute("conflicted",null);
    %>
    <script>
        alert("<fmt:message key='calendar.message.newAppointmentAdded'/>");
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='event updated'}" >
    <%
        session.setAttribute("conflicted",null);
    %>
    <script>
        alert("<fmt:message key='calendar.message.appointmentUploaded'/>");
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <%    session.setAttribute("edit",Boolean.FALSE);
        session.setAttribute("conflicted","1");
    %>
    <c:redirect url="/ekms/calendar/conflictspop.jsp" ></c:redirect>
</c:if>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.addNewAppointment'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="calendarRow">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <Tr>
        <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key='calendar.label.addNewAppointment'/>
        </font></b></td>

    </tr>     <Tr><td>
    <x:display name="appointmentpoppage.AppointmentForm" >
</td></tr></table></x:display>
</body>
</html>

