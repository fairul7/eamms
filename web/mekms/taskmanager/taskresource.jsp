<%@ page import="com.tms.collab.taskmanager.ui.TaskResourceForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.ConflictForm"%>
<%@include file="/common/header.jsp"%>


<x:config >
    <page name="taskresourcepage">
        <com.tms.collab.taskmanager.ui.TaskResourceForm name="taskresource" template="taskmanager/mtaskresourceform"/>
    </page>
</x:config>

<c:if test="${!empty param.taskId}" >

<x:set name="taskresourcepage.taskresource" property="taskId" value="${param.taskId}" ></x:set>
</c:if>

<c-rt:set var="cancel" value="<%=TaskResourceForm.FORWARD_CANCEL%>" />
<c:if test="${forward.name==cancel}" >
    <%      WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
             TaskResourceForm form = (TaskResourceForm)wm.getWidget("taskresourcepage.taskresource");
            pageContext.setAttribute("taskId",form.getTaskId());
    %>
    <c:redirect url="/mekms/calendar/edittodotaskform.jsp?id=${taskId}" ></c:redirect>
</c:if>

<c-rt:set var="viewconflict" value="<%=TaskResourceForm.FORWARD_VIEW_CONFLICTS%>" ></c-rt:set>
<c:if test="${forward.name == viewconflict}" >
    <script>
       window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
   </script>
</c:if>

<c-rt:set var="added" value="<%=TaskResourceForm.FORWARD_RESOURCES_BOOKED%>"></c-rt:set>
<c:if test="${forward.name == added}">
    <script>
<%      WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    TaskResourceForm form = (TaskResourceForm)wm.getWidget("taskresourcepage.taskresource");
          %>
        alert("<fmt:message key='taskmanager.label.resourcesbookedsuccessfully'/>");
        document.location = "<c:url value="/mekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(form.getTaskId());%>";
    </script>
</c:if>



<jsp:include page="../includes/mheader.jsp"/>
<jsp:include page="../includes/mheader.jsp"/>
        <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
          <TBODY>
            <TR>
              <TD><FONT class=f06><strong>MOBILE DESKTOP &gt; <a href="<c:url value="/mekms" />/calendar/calendar2.jsp?defaultDay=today" >HOME</a>
                &gt; ADD TO DO TASK</strong></font></TD>
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

    <jsp:include page="../includes/mfooter.jsp"/>
    </TD>
      <TD width="82%">

<x:display name="taskresourcepage" />

</TD>
  </TR>
</TBODY>
</TABLE>

<jsp:include page="../includes/mfooter.jsp" />

