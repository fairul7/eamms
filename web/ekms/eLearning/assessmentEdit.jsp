<%@ page import="kacang.ui.WidgetManager,
                 com.tms.elearning.testware.ui.AssessmentEditForm" %>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="assessment">
        <com.tms.elearning.testware.ui.AssessmentEditForm name="assessmentEditForm" width="100%">


        </com.tms.elearning.testware.ui.AssessmentEditForm>
    </page>
</x:config>

<x:permission permission="com.tms.elearning.testware.Edit" module="com.tms.elearning.course.model.CourseModule" url="noPermission.jsp">

    <c:choose>
        <c:when test="${forward.name == 'updated'}">
            <script>
                alert("<fmt:message key='eLearning.alert.label.assessmentupdated'/>");
                window.location = "assessment.jsp";
            </script>
        </c:when>
        <c:when test="${forward.name == 'questionbank'}">
            <%-- <c:redirect url="questionlistadd.jsp?id=${widgets['assessment.assessmentEditForm'].id}"/>--%>
            <script>

                popUp('<c:out value="questionlistadd.jsp?id=${widgets['assessment.assessmentEditForm'].id}"/>');
                function popUp(URL) {
                    day = new Date();
                    id = day.getTime();
                    eval("page" + id + " = window.open(URL, '" + "popupscreen" + "', 'toolbar=0,scrollbars=1,location=0,statusbar=1,menubar=0,resizable=0,width=700,height=500,left = 226,top = 182');");
                }
            </script>
        </c:when>
        <c:when test="${forward.name=='dateproblem'}" >
            <script>
                alert("<fmt:message key="eLearning.general.date.problem" />   ");
            </script>
        </c:when>
        <c:when test="${ forward.name == 'viewQuestions' }">
            <%--<c:redirect url="questionlist.jsp?id=${widgets['assessment.assessmentEditForm'].id}"/>--%>
            <script>
                popUp('<c:out value="questionlist.jsp?id=${widgets['assessment.assessmentEditForm'].id}"/>');
                function popUp(URL) {
                    day = new Date();
                    id = day.getTime();
                    eval("page" + id + " = window.open(URL, '" + "popupscreen" + "', 'toolbar=0,scrollbars=1,location=0,statusbar=1,menubar=0,resizable=0,width=700,height=500,left = 226,top = 182');");
                }
            </script>
        </c:when>
        <c:when test="${ forward.name == 'cancel' }">
            <script>
                history.back();
            </script>
        </c:when>
    </c:choose>

    <c:if test="${!empty param.id}">
        <x:set name="assessment.assessmentEditForm" property="id" value="${param.id}"></x:set>
    </c:if>

    <%@ include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key='eLearning.menu.label.Instructor_Menu'/> > <fmt:message
                    key='eLearning.menu.label.editcourse'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="assessment.assessmentEditForm"/>
            <jsp:include page="includes/includeAssessmentEdit.jsp" flush="true"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
</x:permission>

<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>