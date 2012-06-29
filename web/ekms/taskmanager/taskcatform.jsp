<%@ include file="/common/header.jsp"%>

<x:config >
    <page name="catformpage">
       <com.tms.collab.taskmanager.ui.TaskCategoryForm name="catform"/>
    </page>
</x:config>

<% String id= request.getParameter("id");
    if(id!=null&&id.trim().length()>0){%>
       <x:set name="catformpage.catform" property="categoryId" value="<%=id%>" />
<%
    } else { %>
       <x:set name="catformpage.catform" property="categoryId" value=" " />
<%  } %>
<c:if test="${forward.name == 'Added'}" >
    <script>
        alert("<fmt:message key='taskmanager.label.newcategoryadded'/>");
        document.location =  "<c:url value="/ekms/taskmanager/taskcategory.jsp" />";
    </script>
 </c:if>


<c:if test="${forward.name == 'cancel'}" >
    <script>
        document.location =  "<c:url value="/ekms/taskmanager/taskcategory.jsp" />";
   </script>
 </c:if>
<c:if test="${forward.name == 'edit successful'}" >
    <script>
        alert("<fmt:message key='taskmanager.label.categoryupdated'/>");
        document.location =  "<c:url value="/ekms/taskmanager/taskcategory.jsp" />";
    </script>
 </c:if>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<x:display name="catformpage.catform" />

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
