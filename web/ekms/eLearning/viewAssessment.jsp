<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Dec 8, 2004
  Time: 2:35:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>
<x:config>
<page name="assessment">
    <com.tms.elearning.testware.ui.AssessmentViewTable name="assessmentViewTable" width="100%">
        <forward name="add" url="assessmentAdd.jsp"/>
        <request_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                editForm = wm.getWidget("assessment.assessmentViewTable");
                editForm.setCourseid(id);
            }
        </request_script>
    </com.tms.elearning.testware.ui.AssessmentViewTable>
</page>
</x:config>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="assessment.assessmentViewTable"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>