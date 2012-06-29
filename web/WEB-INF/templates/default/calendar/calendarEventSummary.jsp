<%@ page import="com.tms.collab.calendar.model.CalendarModule,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.runtime.HttpController,
                 kacang.stdui.CalendarBox,
                 kacang.ui.Event,
                 com.tms.collab.calendar.model.CalendarEvent,
                 kacang.services.security.SecurityService,
                 kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="event" value="${event}"/>
<c:set var="eventUrl" value="${calendar.eventUrl}"/>
<c:set var="hideDetails" value="${param.hideDetails}"/>
<c:set var="deleteUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETE %>
</c:set>
<c:set var="deleteAllUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_DELETEALL %>
</c:set>
<c:if test="${(event.userId == default_LoginUser.userId) && !hideDetails}">
    <input2 type="checkbox" name="eventId" value="<c:out value="${event.eventId}"/>">
</c:if>

<c:if test="${!hideDetails}">

    <c:choose>
        <c:when test="${event.allDay}">
            <FONT COLOR="#FF0000"><fmt:message key='calendar.label.allDay'/></font>
        </c:when>
        <c:when test="${event.moreThanOneDay}" >
            <FONT COLOR="#FF0000"> <fmt:formatDate pattern="${globalDatetimeLong}" value="${event.startDate}" />
            </font><fmt:message key='calendar.label.to'/><FONT COLOR="#FF0000"> <fmt:formatDate pattern="${globalDatetimeLong}" value="${event.endDate}" />
        </c:when>
        <c:otherwise>
            <FONT COLOR="#FF0000"><fmt:formatDate pattern="${globalTimeLong}" value="${event.startDate}" /> </font>
             -
             <FONT COLOR="#FF0000"><fmt:formatDate pattern="${globalTimeLong}" value="${event.endDate}" /> </font>

        </c:otherwise>
    </c:choose>
    </FONT>
<%--
    <c:if test="${event.universal}">(<fmt:message key='calendar.label.public'/>)</c:if>
--%>
    <c:if test="${event.recurrence}">(<fmt:message key='calendar.label.Recurrence'/>)</c:if>

                           <c:set scope="page" value="${widget.widgetManager.user.id}" var="userId"/>

   <%
       String userId = (String)pageContext.getAttribute("userId");
       CalendarEvent event = (CalendarEvent)pageContext.getAttribute("event");
       SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);


       if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null) ||ss.hasPermission(userId,CalendarModule.PERMISSION_EDIT_EVENTS,null,null)){

           pageContext.setAttribute("canEdit", Boolean.TRUE);
       }
       if(event.getUserId().equals(userId)||ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null)
               ||ss.hasPermission(userId,CalendarModule.PERMISSION_DELETE_EVENTS,null,null)){
                   pageContext.setAttribute("canDelete", Boolean.TRUE);
               }
  %>


</c:if>



<c-rt:set var="privateEvent" value="<%= CalendarModule.CLASSIFICATION_PRIVATE %>"/>
<c:choose>
<c:when test="${event.hidden}"><fmt:message key='calendar.label.privateEvent'/></c:when>
<c:otherwise>  <br>
    <c-rt:set var="cn" value="<%= Event.PARAMETER_KEY_WIDGET_NAME %>"/>
    <c-rt:set var="et" value="<%= Event.PARAMETER_KEY_EVENT_TYPE %>"/>
    <c-rt:set var="eid" value="<%= CalendarView.PARAMETER_KEY_EVENTID %>"/>
    <c-rt:set var="iid" value="<%= CalendarView.PARAMETER_KEY_INSTANCEID %>"/>
    <c:url var="eUrl" value="${eventUrl}">
    <c:param name="${eid}" value="${event.eventId}"/>
        <c:param name="${cn}" value="${calendar.absoluteName}"/>
        <c:param name="${et}"><%= CalendarView.PARAMETER_KEY_EVENT_SELECT %></c:param>

        <c:param name="${iid}" value="${event.recurrenceId}"/>
        <c:if test="${event.reminder}">
      <%--  <fmt:formatDate pattern="dd-MM-yyyy" value="${event.startDate}" var="sdate"  />--%>
            <c:param name="remindId" value="${event.id}"/>
        </c:if>
    </c:url>
    <a href="<c:out value="${eUrl}"/>">


    <%--<c:choose>
        <c:when test="${event.eventId == calendar.selectedEventId && event.recurrenceId == calendar.selectedInstanceId}"><c:out value="${event.title}"/>    <c:if test="${event.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if>
</c:when>
        <c:otherwise>--%><c:out value="${event.title}"/>    <c:if test="${event.reminder}">(<fmt:message key='calendar.label.REMINDER'/>)</c:if>
<%--</c:otherwise>
    </c:choose>--%></a>
      <c:if test="${canEdit||canDelete}" >
                    <br>
                    </c:if>
                    <c:if test="${canEdit}" >
                       <input value="<fmt:message key="calendar.label.buttonEdit"/>" type="button" class="button" onClick="document.location = 'editeventform.jsp?<%= CalendarView.PARAMETER_KEY_EVENTID %>=<c:out value="${event.eventId}" />'"/>
                    </c:if>

                    <c:if test="${canDelete && event.recurrence}" >
                    <input value="<fmt:message key="calendar.label.buttonDelete"/>" type="button" class="button" onClick="if(confirm('<fmt:message key="messaging.label.confirmDelete"/>')){document.location='<c:out value="${deleteUrl}"/>&eventId=<c:out value="${event.eventId}"/>';}"/>
                    </c:if>
                    <c:if test="${canDelete}" >
                    <INPUT type="button" class="button" value="<c:if test="${empty event.recurrenceRule}"><fmt:message key="calendar.label.buttonDelete"/></c:if><c:if test="${not empty event.recurrenceRule}"><fmt:message key="calendar.label.buttonDeleteAll"/></c:if>" onClick="if(confirm('<fmt:message key="calendar.message.confirmDelete"/>')){document.location = '<c:out value="${deleteAllUrl}" />&eventId=<c:out value="${event.eventId}"/>'}"/>
					</c:if>
</c:otherwise>
</c:choose>

