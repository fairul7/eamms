<%@ page import="kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.calendar.ui.CalendarEventView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:if test="${forward.name=='deleted'}">
<script>
         window.opener.location.reload();
 window.close();
</script>
</c:if>

<c:set var="calendar" value="${widget}"/>
<c:set var="editUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_EDIT %>
</c:set>
<c:set var="deleteUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %>
</c:set>
<c:set var="deleteAllUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETEALL %>
</c:set>
     <% pageContext.setAttribute("EDIT",String.valueOf(CalendarEventView.EDIT));%>
<table BORDER="0" CELLSPACING="1" CELLPADDING="3" >



<tr><td GCOLOR="#EFEFEF" CLASS="contentBgColor">
    <x:display name="${calendar.child.absoluteName}" />
</td></tr>
<tr><td GCOLOR="#EFEFEF" CLASS="contentBgColor">  <table width="100%"><tr width="100%">
    <td align="left">
    <c:if test="${(calendar.state!=EDIT)}" >

        <c:if test="${calendar.editable}">
            <a href="editappointmentform.jsp?eventId=<c:out value="${calendar.eventId}" />">Edit</a>
        </c:if>
        <c:if test="${calendar.deleteable}">
            <c:if test="${calendar.editable}">
            |
            </c:if>
            <c:if test="${calendar.child.event.recurrence}">
             <a href="<c:out value="${deleteUrl}" />">Delete<a>
            |
            </c:if>
             <a href="<c:out value="${deleteAllUrl}" />">Delete <c:if test="${not empty calendar.child.event.recurrenceRule}" >(whole series)</c:if></a></td>
        </c:if>
   </c:if>
   <c:if test="${calendar.state ==EDIT}">
       <a href="" onClick="javascript:history.back();return false;">Back</a>
   </c:if>
   </td>
   <%--<td  colspan="7" align="right" >
   <a href="<c:out value="${weeklyUrl}"/>">weekly</a></td>--%>
</tr>
</table></td></tr>
</table>