<%@ include file="/common/header.jsp" %>

<%
    String e = request.getParameter("examid");
    if (e != null) {
        if (session.getAttribute("exam") != null) {
            if (!e.equalsIgnoreCase(session.getAttribute("exam").toString())) {
                session.setAttribute("exam",e);
            }
        } else {
            session.setAttribute("exam",e);
        }
    }
    //out.print(session.getAttribute("exam"));
    /*editForm = wm.getWidget("question.questionTable");
                editForm.setExamid(session.getAttribute("exam").toString());
    */
%>

<x:config>
<page name="question">
    <com.tms.elearning.testware.ui.AsmtQuestionTable name="questionTable" width="100%">
        <forward name="add" url="asmtQuestionAdd.jsp"/>
        <listener_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                return new Forward(null, "asmtQuestionEdit.jsp?id=" + id, true);
            }
        </listener_script>
    </com.tms.elearning.testware.ui.AsmtQuestionTable>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="question.questionTable"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>