<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<x:config >
<page name="taskportletpage">
    <com.tms.collab.taskmanager.ui.TaskListPortlet name="taskportlet"/>
</page>
</x:config>

<c:if test="${forward.name=='all'}" >
    <script>
        document.location = "<c:url value="/ekms/" />taskmanager/taskmanager.jsp?cn=taskmanager.taskview&taskmanager.taskview.viewsb=1";

    </script>
</c:if>



       <x:display name="taskportletpage.taskportlet" />
