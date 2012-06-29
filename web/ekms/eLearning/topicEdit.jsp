<%@ include file="/common/header.jsp" %>
<x:config>
<page name="topic">
    <com.tms.elearning.course.ui.TopicEditForm name="topicEditForm" width="100%">
        <forward name="updated" url="topic.jsp" redirect="true"/>
        <request_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                editForm = wm.getWidget("topic.topicEditForm");
                editForm.setTopicId(id);
            }
        </request_script>
    </com.tms.elearning.course.ui.TopicEditForm>
</page>
</x:config>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="topic.topicEditForm"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>