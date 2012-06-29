<%@ include file="/common/header.jsp" %>

<x:config>
<page name="question">
    <com.tms.elearning.testware.ui.QuestionTable name="questionTable" width="100%">

    </com.tms.elearning.testware.ui.QuestionTable>
</page>
</x:config>

<x:permission permission="com.tms.elearning.testware.View" module="com.tms.elearning.course.model.CourseModule" url="noPermission.jsp">

 <c:choose>
     <c:when test="${forward.name =='empty'}">
         <script>
             alert("<fmt:message key='eLearning.questions.empty'/>");
         </script>
     </c:when>
    <c:when test="${forward.name == 'add'}" >
    <script>
    window.location="questionAdd.jsp";
    </script>
    </c:when>
    <c:when test="${forward.name == 'deleted'}" >
    <script>
    alert("<fmt:message key='eLearning.alert.label.questiondeleted'/>");
    window.location="question.jsp";
    </script>
    </c:when>
     <c:when test="${forward.name == 'notSelected'}" >
     <script>
     alert("<fmt:message key='eLearning.alert.label.questionnotselected'/>");
     </script>
     </c:when>
    </c:choose>
<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
                                      <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.Instructor_Menu'/> > <A HREF="question.jsp"><fmt:message key='eLearning.assessment.questionbank'/></A> </font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

                   <x:display name="question.questionTable"/>

                </td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
            </table>
   </x:permission>

<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>