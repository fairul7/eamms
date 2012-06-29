<%@ include file="/common/header.jsp" %>
<x:config>
<page name="assessment">
    <com.tms.elearning.testware.ui.AssessmentDoForm name="assessmentDoForm" width="100%">
          <forward name="updated" url="assessment.jsp" redirect="true"/>
          <request_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                editForm = wm.getWidget("assessment.assessmentEditForm");
                editForm.setAssessmentId(id);
            }
        </request_script>
    </com.tms.elearning.testware.ui.AssessmentDoForm>
</page>
</x:config>



<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="assessment.assessmentDoForm"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>