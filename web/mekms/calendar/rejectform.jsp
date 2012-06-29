<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>



<x:config  reloadable="${param.reload}">
        <page name="rejectformpage">
            <com.tms.collab.calendar.ui.RejectForm name="rejectform" />

        </page>
</x:config>

<c:if test="${!empty param.eventId}" >
 <x:set name="rejectformpage.rejectform" property="eventId" value="${param.eventId}" />
 </c:if>
<c:if test="${!empty param.instanceId}" >
 <x:set name="rejectformpage.rejectform" property="instanceId" value="${param.instanceId}" />
 </c:if>

 <c:if test="${!empty param.attendeeId}" >
 <x:set name="rejectformpage.rejectform" property="attendeeId" value="${param.attendeeId}" />
 </c:if>

<c:if test="${forward.name=='rejected'}" >
 <script>
    window.opener.location.reload();
    window.close();
 </script>
 </c:if>

 <c:if test="${forward.name=='cancel'}" >
 <script>
    window.close();
 </script>
 </c:if>

<%--  <jsp:include page="../includes/mheader.jsp" /> --%>
<html>
<head>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <link rel="stylesheet" href="../images/default.css">
    <title><fmt:message key='calendar.label.rejectAppointment'/></title>
</head>
<body>
<%--        <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
          <TBODY>
            <TR>
              <TD><FONT class=f06><strong>MOBILE DESKTOP &gt; <a href="<c:url value="/mekms" />/calendar/calendar2.jsp?defaultDay=today">HOME</a>
                &gt; <c:out value="${theader}" /></strong></font></TD>
            </TR>
            <TR>
              <TD height="3"></TD>
            </TR>
            <TR>
              <TD><TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
                  <TBODY>
                    <TR>
                      <TD height=5 bgColor=#6584CD> </TD>
                    </TR>
                  </TBODY>
                </TABLE></TD>
            </TR>
          </TBODY>
        </TABLE>
<TABLE width="100%" border=0 cellPadding=2 cellSpacing=0>
  <TBODY>
    <TR valign="top" class="f07">
      <TD width="18%">

    <jsp:include page="../includes/menu.jsp" />
    </TD>
      <TD width="82%">            --%>

 <x:display name="rejectformpage.rejectform" >
    </x:display>

<%--
  </TD>
  </TR>
</TBODY>
</TABLE>    --%>
</body>
</html>

<%-- <jsp:include page="../includes/mfooter.jsp" />--%>