<%@include file="/common/header.jsp"%>


<x:config >
    <page name="taskviewpage">
        <com.tms.collab.taskmanager.ui.TaskEventView name="taskviewggg"/>
    </page>
</x:config>

<%
 String id = request.getParameter("id");
 String userId = request.getParameter("userId");
 if(id!=null&&id.trim().length()>0)  {
%>
 <x:set name="taskviewpage.taskviewggg" property="taskId" value="<%=id%>" />

<%}
if (userId!=null&&userId.trim().length()>0){
%>
<x:set name="taskviewpage.taskviewggg" property="userId" value="<%=userId%>" />
<%}%>
          <link rel="stylesheet" href="<c:url value="/ekms/"/>images/style.css">

<html>
    <head>
    </head>
    <body>
    <table class="calendarBackground" cellpadding="4" cellspacing="1"  width="100%">
    <tr align ="center">
        <td class="calendarHeader">
           <b><fmt:message key='taskmanager.label.toDoTask'/></b>
        </td>
    </tr>       </table>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
    <td>
        <x:display name="taskviewpage" />
       </td> </tr>
       </table>

    </body>

</html>
