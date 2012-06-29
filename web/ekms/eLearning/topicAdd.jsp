<%@ include file="/common/header.jsp" %>
<x:config>
<page name="topic">
    <com.tms.elearning.course.ui.TopicAddForm name="topicAddForm" width="100%">
        <forward name="added" url="topic.jsp" redirect="true"/>
    </com.tms.elearning.course.ui.TopicAddForm>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="topic.topicAddForm"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>