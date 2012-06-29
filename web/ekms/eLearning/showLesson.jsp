<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Dec 8, 2004
  Time: 10:35:44 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>
<x:config>
<page name="lesson">
    <com.tms.elearning.lesson.ui.LessonFormBrief name="lessonBriefForm" width="100%">
        <forward name="updated" url="viewLessons.jsp" redirect="true"/>
        <request_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                editForm = wm.getWidget("lesson.lessonBriefForm");
                editForm.setId(id);
            }
        </request_script>
    </com.tms.elearning.lesson.ui.LessonFormBrief>
</page>
</x:config>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="lesson.lessonBriefForm"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>