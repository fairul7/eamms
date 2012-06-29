<%@include file="/common/header.jsp"%>


<x:config >
    <page name="formpage">
                 <com.tms.collab.taskmanager.ui.TaskForm name="taskform"/>
    </page>
</x:config>

<c:if test="${forward.name=='conflict exception'}">

<%
    session.setAttribute("edit",Boolean.TRUE);%>
    <c:redirect url="/ekms/calendar/conflicts.jsp" ></c:redirect>



</c:if>

<c:if test="${forward.name=='cancel'||forward.name=='updated'}" >
    <script>
        document.location= "taskview.jsp?id=<c:out value="${param.id}" />";
    </script>

</c:if>
<%  String id = request.getParameter("id");
    if(id!=null&&id.trim().length()>0){
%>
    <x:set name="formpage.taskform" property="taskId" value="<%=id%>" />
<%}%>
<link rel="stylesheet" href="<c:url value="/ekms/images/" />style.css">

<html> <head></head>

<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      <fmt:message key='taskmanager.label.editTask'/>
    </font></b></td>
    </tr>
    <tr><td>
    <x:display name="formpage" />
    </td></tr>
    </table>

</body>


</html>
