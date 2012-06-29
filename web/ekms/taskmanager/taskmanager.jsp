<%@ page import="java.util.Properties,
                 java.util.StringTokenizer"%>
<%@include file="/common/header.jsp"%>


<x:config >
    <page name="taskmanager">
    <tabbedpanel name="tab1" width="100%">
    <panel name="panel1" text="<fmt:message key="taskmanager.label.viewMyTasks"/>">
        <com.tms.collab.taskmanager.ui.TaskTable name="taskview" />
    </panel>
    <panel name="panel2" text="<fmt:message key="taskmanager.label.viewPublicTasks"/>">
        <com.tms.collab.taskmanager.ui.PublicTaskTable name="publictaskview" />
    </panel>
    </tabbedpanel>
    </page>

</x:config>

 <c:if test="${!empty param.id}">
    <c:redirect url="/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${param.id}&instanceId=0&from=task" ></c:redirect>
</c:if>

<c:if test="${!empty param.dueDate}">
<%
    String dateStr = request.getParameter("dueDate");
    StringTokenizer tokenizer = new StringTokenizer(dateStr,"-");
    String year = tokenizer.nextToken();
    pageContext.setAttribute("year",year);
    int imonth = Integer.parseInt(tokenizer.nextToken());
    String month = "" + (imonth-1);
    pageContext.setAttribute("month",month);
    // tokenizer.
    String date = tokenizer.nextToken();
    pageContext.setAttribute("day",date.substring(0,2));
%>
<c:redirect url="/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=view&view=daily&calendarPage.calendarView*day=${day}&calendarPage.calendarView*month=${month}&calendarPage.calendarView*year=${year}" ></c:redirect>
</c:if>



<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='taskmanager.label.taskManager'/> > <fmt:message key='calendar.label.viewAllTasks'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:display name="taskmanager.tab1" />

 </td>
  </tr>
  <tr>
       <td cellpadding="5" colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      &nbsp;&nbsp;&nbsp; <FONT COLOR="red">*<fmt:message key='taskmanager.label.MyTasksMessage'/></FONT>
       </td>
  </tr> 
  <tr>
       <td cellpadding="5" colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      &nbsp;&nbsp;&nbsp; <FONT COLOR="red">*<fmt:message key='taskmanager.label.PublicTasksMessage'/></FONT>
       </td>
  </tr> 
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      </td>
  </tr>
  <tr>
       <td cellpadding="5" colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      &nbsp;&nbsp;&nbsp; <FONT COLOR="#3130FF">&#149;</FONT>*<fmt:message key='taskmanager.label.completed'/>&nbsp;<FONT COLOR="#FF0000">&#149;</FONT><fmt:message key='taskmanager.label.incomplete'/>
       </td>
  </tr>
  
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="calendarFooter">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
