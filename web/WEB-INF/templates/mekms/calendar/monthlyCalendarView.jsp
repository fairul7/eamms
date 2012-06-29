<%@ page import="kacang.runtime.*, kacang.ui.*,
                 com.tms.collab.calendar.ui.CalendarView,
                 kacang.stdui.CalendarBox,
                 java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="isBaseMonth" value="${calendar.month == calendar.baseMonth}"/>
<c:set var="isSelectedMonth" value="${calendar.month == calendar.selectedMonth}"/>
<c:set var="paramDateSelect" value="${calendar.dayOfMonthName}"/>
<c:set var="pDate" value="${calendar.previousDate}"/>
<c:set var="nDate" value="${calendar.nextDate}"/>
<fmt:formatDate var="monthValue" value="${nDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="nextUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_MONTHLY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${nDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${nDate}" pattern="yyyy"/>
</c:set>
<fmt:formatDate var="monthValue" value="${pDate}" pattern="M"/>
<c:set var="monthValue" value="${monthValue-1}" />
<c:set var="previousUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_MONTHLY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${pDate}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${pDate}" pattern="yyyy"/>
</c:set>
<c:set var="dailyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>
</c:set>
<c:set var="weeklyUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_WEEKLY %>
</c:set>

<%--
<script  LANGUAGE="JavaScript">
function MM_openBrWindow(winName,features){
    var selectBox = document.forms['selectform'].elements['select6'];
    //alert(selectBox.options.length);
    for(i=0; i<selectBox.options.length;i++){
        if(selectBox.options[i].selected)  {
            window.open(selectBox.options[i].value,winName,features);
            }
    }
}

function MM_openBrWindow2(winName,features){
    var selectBox = document.forms['selectform2'].elements['select2'];
    //alert(selectBox.options.length);
    for(i=0; i<selectBox.options.length;i++){
        if(selectBox.options[i].selected){
            window.open(selectBox.options[i].value,winName,features);
            }
    }
}
</script>
--%>

<table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%">
<%--<TR VALIGN="MIDDLE">
    <TD HEIGHT="16" BACKGROUND="<c:url value="/ekms/"/>images/monthlystrap.gif" COLSPAN="7"><A HREF="<c:out value="${dailyUrl}" />"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="75" HEIGHT="16" BORDER="0"></A><A HREF="<c:out value="${weeklyUrl}" />"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="84" HEIGHT="16" BORDER="0"></A><A HREF="<c:out value="${monthlyUrl}" />"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="89" HEIGHT="16" BORDER="0"></A></TD>
    </TR>

<tr>--%>
    <tr><td class="calendarHeader" colspan="7"><fmt:message key='calendar.label.monthlyCalendarView'/></td></tr>



<tr> <td VALIGN="BOTTOM" CLASS="calendarSubheader" colspan="7">
 <table width="100%" border="0" cellspacing="0" cellpadding="0"><tr>
 <td align="left">
 <x:display name="${calendar.groupSelectForm.absoluteName}" ></td></x:display>
 <td align="left">
 <x:display name="${calendar.userSelectForm.absoluteName}" body="custom" >
            <x:display name="${calendar.userSelectBox.absoluteName}"/>
            <x:display name="${calendar.userSelectButton.absoluteName}"/>

</td></x:display><td align="right" valign="bottom">
<x:display name="${calendar.jumpForm.absoluteName}" ></td></x:display><td align="right" valign="bottom">
    <%
        Date today = new Date();
        pageContext.setAttribute("today",today);
    %>
    <fmt:formatDate var="monthValue" value="${today}" pattern="M"/>
    <c:set var="monthValue" value="${monthValue-1}" />
    <c:set var="todayUrl">
    <%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${calendar.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>&<%= CalendarView.PARAMETER_KEY_EVENT_VIEW %>=<%= CalendarView.VIEW_DAILY %>&<c:out value="${calendar.dayOfMonthName}"/>=<fmt:formatDate value="${today}" pattern="d"/>&<c:out value="${calendar.monthName}" />=<c:out value="${monthValue}" />&<c:out value="${calendar.yearName}" />=<fmt:formatDate value="${today}" pattern="yyyy"/>
    </c:set>
    <INPUT TYPE="button" class="button" value="<fmt:message key='calendar.label.today'/>" onClick="document.location = '<c:out value="${todayUrl}"/>'">
</td></tr></table></td></tr>

<tr> <td  VALIGN="BOTTOM" CLASS="contentBgColor" colspan="7">
  <table border="0" cellspacing="0" cellpadding="0" width="100%">
   <tr BGCOLOR="#EFEFEF" >
      <td width="20%" VALIGN="TOP" CLASS="contentBgColor">
      <input type="button" value="&nbsp;&nbsp;&lt;&nbsp;&nbsp;" class="button" onClick="document.location='<c:out value="${previousUrl}"/>';">
      </td>
      <td width="60%" colspan="5" align="center" VALIGN="TOP" CLASS="contentBgColor">
        <fmt:parseDate value="${calendar.month+1}" pattern="MM" var="fm"/>
        <B><FONT COLOR="#0065CE" CLASS="textFont">
            <fmt:formatDate pattern="MMMM" value="${fm}"/></font>
        <c:out value="${calendar.year}"/> </b>
      </td>
      <td width="20%" align="right" VALIGN="TOP" CLASS="contentBgColor">
        <input type="button" value="&nbsp;&nbsp;&gt;&nbsp;&nbsp;" class="button" onClick="document.location='<c:out value="${nextUrl}"/>'">
<%--
        <a href="<c:out value="${nextUrl}"/>">&gt;&gt;</a>
--%>
      </td>
    </tr>
    </table>

</td> </tr>
<%--<thead > <tr BGCOLOR="#EFEFEF" >
       <Td VALIGN="TOP" CLASS="contentBgColor" COLSPAN="7">

       </td>
    </tr>
 </table>
 </td></tr><tr><td>
 <table BORDER="0" CELLSPACING="0" CELLPADDING="0" width="100%" >
   <tr><td colspan="7">

   </td></tr>
</thead>--%>
<tr VALIGN="MIDDLE" BGCOLOR="#DBDBDB">
<c:forEach items="${calendar.daysInWeek}" var="day">
  <td class="calendarSubheader" VALIGN="TOP" ALIGN="MIDDLE">
  <B>
    <c:choose>
        <c:when test="${day=='Sun' || day=='Sat'}" >
           <FONT class="calendarMonthWeekendFont">
        </c:when>
        <c:otherwise>
            <FONT class="calendarMonthWeekdayFont">
        </c:otherwise>
        </c:choose>
    <c:out value="${day}"/>
  </FONT></B>
  </td>
</c:forEach>
</tr>


<%--<tbody class="row">--%>
<c:forEach var="week" items="${calendar.monthlyEntries}" begin="0" varStatus="status" >
<c:if test="${week!=null}" >
<tr class="row">
  <c:forEach var="entry" items="${week}" >
<%--<c:if test="${entry!=null}" >--%>

  <td VALIGN="TOP" class="calendarSubheader"> <b>
<%--    <div align="center">--%>

    <c:choose>
      <%--<c:when test="${isSelectedMonth && calendar.selectedDayOfMonth == entry.dayOfMonth}">
       <B CLASS="textFont"><a href="<c:out value="${dailyUrl}"/>&<c:out value="${paramDateSelect}"/>=<c:out value="${entry.dayOfMonth}"/>"><c:out value="${entry.dayOfMonth}"/></a>
       </b>
      </c:when>--%>
      <c:when test="${entry.today}">
       <span style="font-style: italic; font-weight:bold"><%--<c:out value="${entry.dayOfMonth}"/>--%>
        <a href="<c:out value="${dailyUrl}"/>&<c:out value="${paramDateSelect}"/>=<c:out value="${entry.dayOfMonth}"/>"> <c:out value="${entry.dayOfMonth}"/>  </a></span>
      </c:when>
      <c:otherwise>
              <a href="<c:out value="${dailyUrl}"/>&<c:out value="${paramDateSelect}"/>=<c:out value="${entry.dayOfMonth}"/>">
        <c:out value="${entry.dayOfMonth}"/></a>
      </c:otherwise>
    </c:choose>
<%--  </a>  </div>--%>
<%--</c:if>--%>
  </c:forEach> </b></td>
</tr>
<tr  ALIGN="CENTER">
    <c:forEach var="entry" items="${week}" begin="0" varStatus="status"  >
    <%--<c:if test="${entry!=null}" >--%>
    <c:choose>
        <c:when test="${status.index == 0 || status.index==6}" ><%--entry.dayOfWeekLabel == 'Sunday' || entry.dayOfWeekLabel == 'Saturday'--%>
            <td VALIGN="CENTER" class="calendarSubheader">
        </c:when>
        <c:otherwise>
            <td VALIGN="CENTER" CLASS="contentBgColor">

        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${!entry.containingEvents}" >
          &nbsp;<IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="30" HEIGHT="60" BORDER="0" >
        </c:when>
        <c:otherwise>
            <%int num=0;%>
            <c:forEach items="${entry.appointments}"  >
                <%num++;%>
            </c:forEach> <FONT COLOR="#FF0031"><%=num%></FONT><br>
            <%num = 0;%>
            <c:forEach items="${entry.meetings}"  >
                <%num++;%>
            </c:forEach> <FONT COLOR="#F233FF"><%=num%></FONT><br>
            <%num = 0;%>
             <c:forEach items="${entry.events}"  >
                <%num++;%>
            </c:forEach> <FONT COLOR="#33CC00"><%=num%></FONT><br>
            <%num = 0;%>
             <c:forEach items="${entry.tasks}"  >
                <%num++;%>
            </c:forEach> <FONT COLOR="#3130FF"><%=num%></FONT><br>
            <A HREF="<c:out value="${dailyUrl}"/>&<c:out value="${paramDateSelect}"/>=<c:out value="${entry.dayOfMonth}"/>"><IMG SRC="<c:url value="/ekms/"/>images/next.gif" WIDTH="14" HEIGHT="14" ALT="Click here to view this day's schedule" BORDER="0"></A>
        </c:otherwise>

    </c:choose>
  </td>
  </c:forEach>
</tr>
</c:if>
</c:forEach>
<TR BGCOLOR="#EFEFEF">
                      <TD VALIGN="TOP" CLASS="contentBgColor" COLSPAN="7"><IMG SRC="pix/blank.gif" WIDTH="8" HEIGHT="1"><FONT COLOR="#FF0000">&#149;</FONT><fmt:message key='calendar.label.appointment'/><FONT COLOR="#F233FF"> &#149;</FONT><fmt:message key='calendar.label.e-Meeting'/><FONT COLOR="#31CF00"> &#149;</FONT><fmt:message key='calendar.label.events'/><FONT COLOR="#3130FF"> &#149;</FONT><fmt:message key='calendar.label.tasks'/></TD>
                    </TR>


                    <tr>
 <Td BGCOLOR="#003366" CLASS="calendarFooter" align="right" COLSPAN="7">
          &nbsp;
</td></tr>
<%--</tbody>--%>

<%--<tfoot>
<tr>
  <td colspan="7" align="right">
    <a href="<c:out value="${weeklyUrl}"/>"><fmt:message key='calendar.label.weekly'/></a>
  </td>
</tr>
</tfoot>--%>

</table>
<%--
             </td></tr></table>--%>
