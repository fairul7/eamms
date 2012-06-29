<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<x:config >
     <page name="bookingrejectpage">
        <com.tms.collab.resourcemanager.ui.RejectBookingForm name="bookingrejectform"/>
     </page>
</x:config>

<c:if test="${!empty param.eventId}" >
 <x:set name="bookingrejectpage.bookingrejectform" property="eventId" value="${param.eventId}" />
 </c:if>
<c:if test="${!empty param.instanceId}" >
 <x:set name="bookingrejectpage.bookingrejectform" property="instanceId" value="${param.instanceId}" />
 </c:if>
 <c:if test="${!empty param.resourceId}" >
 <x:set name="bookingrejectpage.bookingrejectform" property="resourceId" value="${param.resourceId}" />
 </c:if>

<c:if test="${forward.name =='rejected'}">
 <script>
    alert("<fmt:message key='resourcemanager.label.bookingrejected'/>");
    window.opener.location = "<c:url value="/ekms/resourcemanager/resourceview.jsp" ></c:url>";
    window.close();
 </script>
</c:if>

<c:if test="${forward.name=='cancel'}">
    <c:redirect url="/ekms/resourcemanager/bookingdetail.jsp" ></c:redirect>
<%--
   <script>
    history.back();
    history.back();
   </script>
--%>
</c:if>


<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
   <title><fmt:message key='resourcemanager.label.rejectResourceBooking'/></title>
       <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

</head>
<body >
<table border="0" cellpadding="0" cellspacing="0">
<tr>
    <td>
     <table cellpadding="4" cellspacing="1" border="0" width="100%">
        <Tr>
            <td class="calendarHeader" width="100%"> <fmt:message key='resourcemanager.label.rejectResourceBooking'/>
            </td>
        </tr></table>
    </td>
</tr>
<tr>
<td>
 <x:display name="bookingrejectpage.bookingrejectform" >
    </x:display>
    </td>
    </tr>
    </table>
</body>
</html>
