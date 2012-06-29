<%@ page import="com.tms.collab.taskmanager.ui.TaskResourceForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.ConflictForm"%>
<%@include file="/common/header.jsp"%>


<x:config >
    <page name="taskresourcepage">
        <com.tms.collab.taskmanager.ui.TaskResourceForm name="taskresource"/>
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
    <c:redirect url="/ekms/calendar/edittodotaskform.jsp?id=${taskId}" ></c:redirect>
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
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(form.getTaskId());%>";
    </script>
</c:if>



<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<x:display name="taskresourcepage" />
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
