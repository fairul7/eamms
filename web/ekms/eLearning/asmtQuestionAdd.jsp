<%@ include file="/common/header.jsp" %>
<x:config>
<page name="question">
    <com.tms.elearning.testware.ui.AsmtQuestionAddForm name="questionAddForm" width="100%">
          <request_script>
            String id;
            qForm = wm.getWidget("question.questionAddForm");
            id = qForm.getUid();
            if (id != null) {
                return new Forward(null, "asmtQuestionEdit.jsp?id=" + id, true);
            }
        </request_script>
    </com.tms.elearning.testware.ui.AsmtQuestionAddForm>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="question.questionAddForm"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>