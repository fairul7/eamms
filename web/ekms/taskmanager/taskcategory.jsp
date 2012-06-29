<%@ page import="java.util.Properties"%>

<%@include file="/common/header.jsp"%>


<x:config >
    <page name="taskcategory">
        <com.tms.collab.taskmanager.ui.TaskCategoryTable name="taskcategorytable" popupViewUrl="taskview.jsp"/>
    </page>

</x:config>
<c:if test="${!empty param.id}" >
   <c:redirect url="/ekms/taskmanager/taskcatform.jsp?id=${param.id}"/>
</c:if>
<c:if test="${forward.name=='add'}" >
  <script>
  document.location = "taskcatform.jsp";
  </script>
</c:if>
<c:if test="${forward.name=='noDelete'}" >
  <script>
  alert('<fmt:message key='taskmanager.label.nopermissiondelete'/>');
  </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table  width="100%" border="0" cellpadding="0" cellspacing="0">
<Tr><Td height="22" class="calendarHeader">&nbsp;&nbsp;<fmt:message key='taskmanager.label.taskManager'/> > <fmt:message key='taskmanager.label.taskCategories'/></td>
</tr>
<tr> <td bgcolor="#CCCCCC" class="contentStrapColor"width="100%">
<x:display name="taskcategory" />
</td></tr>
<tr> <td bgcolor="#CCCCCC" class="contentStrapColor">
 <FONT COLOR="#FF0000"> * <fmt:message key='taskmanager.label.categoryDelete'/>.</font>
</td></tr>
<tr> <td class="calendarFooter">&nbsp;
</td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
