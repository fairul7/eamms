<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Dec 3, 2004
  Time: 6:55:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
<page name="lesson">
    <com.tms.elearning.lesson.ui.LessonViewTable name="lessonViewTable" width="100%">
        <forward name="add" url="addCourse.jsp"/>
        <request_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                editForm = wm.getWidget("lesson.lessonViewTable");
                editForm.setCourseid(id);
                return new Forward(null, "showLesson.jsp?id=" + id, true);
            }
        </request_script>
    </com.tms.elearning.lesson.ui.LessonViewTable>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="lesson.lessonViewTable"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>