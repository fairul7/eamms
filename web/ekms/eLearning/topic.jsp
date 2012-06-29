<%@ include file="/common/header.jsp" %>
<x:config>
<page name="topic">
    <com.tms.elearning.course.ui.TopicTable name="topicTable" width="100%">
        <forward name="add" url="topicAdd.jsp"/>
        <listener_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                return new Forward(null, "topicEdit.jsp?id=" + id, true);
            }
        </listener_script>
    </com.tms.elearning.course.ui.TopicTable>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="topic.topicTable"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>