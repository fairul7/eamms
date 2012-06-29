<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 kacang.ui.WidgetManager,
                 com.tms.collab.resourcemanager.ui.BookingDetailForm"%>
<%@include file="/common/header.jsp" %>


<x:config >
     <page name="bookingdetailform">
        <com.tms.collab.resourcemanager.ui.BookingDetailForm name="bookingdetail"/>
     </page>
</x:config>

<c:if test="${!empty param.resourceId}" >
<x:set name="bookingdetailform.bookingdetail" property="resourceId" value="${param.resourceId}"/>
</c:if>

<c:if test="${!empty param.eventId}" >
<x:set name="bookingdetailform.bookingdetail" property="eventId" value="${param.eventId}"/>
</c:if>

<c:if test="${!empty param.instanceId}" >
<x:set name="bookingdetailform.bookingdetail" property="instanceId" value="${param.instanceId}"/>
</c:if>

<c:if test="${forward.name =='approved'}">
 <script>
    alert("<fmt:message key='resourcemanager.label.bookingapproved'/>");
    window.opener.location = "<c:url value="/ekms/resourcemanager/resourceview.jsp" ></c:url>";
    window.close();
 </script>
</c:if>

<c:if test="${forward.name =='rejected'}">
<%
    /*WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    BookingDetailForm form = (BookingDetailForm)wm.getWidget("bookingdetailform.bookingdetail");
    pageContext.setAttribute("resourceId ",form.getResourceId());
    pageContext.setAttribute("eventId",form.getEventId());
    pageContext.setAttribute("instanceId", form.getInstanceId());*/

%>
    <c:set var="resourceId" value="${widgets['bookingdetailform.bookingdetail'].resourceId}"/>
    <c:set var="eventId" value="${widgets['bookingdetailform.bookingdetail'].eventId}"/>
    <c:set var="instanceId" value="${widgets['bookingdetailform.bookingdetail'].instanceId}"/>
    <c:redirect url="/ekms/resourcemanager/bookingrejectform.jsp?resourceId=${resourceId}&eventId=${eventId}&instanceId=${instanceId}" ></c:redirect>
</c:if>


<c:if test="${forward.name=='cancel'}" >
 <script>
 window.close();
 </script>
 </c:if>

<html>
<title><fmt:message key='resourcemanager.label.resourceBooking'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

    <x:display name="bookingdetailform.bookingdetail" ></x:display>
</html>

