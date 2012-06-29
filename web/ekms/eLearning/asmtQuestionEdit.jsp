<%@ include file="/common/header.jsp" %>
<x:config>
<page name="question">
    <com.tms.elearning.testware.ui.AsmtQuestionEditForm name="questionEditForm" width="100%">
        <forward name="updated" url="asmtQuestion.jsp" redirect="true"/>
        <request_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                editForm = wm.getWidget("question.questionEditForm");
                editForm.setQuestionId(id);
            }
        </request_script>
    </com.tms.elearning.testware.ui.AsmtQuestionEditForm>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="question.questionEditForm"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>