<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Oct 26, 2004
  Time: 4:01:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="lesson">
        <com.tms.elearning.lesson.ui.LessonTable name="lessonTable" width="100%">

        </com.tms.elearning.lesson.ui.LessonTable>
    </page>
</x:config>

<x:permission permission="com.tms.elearning.lesson.View" module="com.tms.elearning.course.model.CourseModule">

    <c:if test="${!empty param.id}">
        <x:set name="lesson.lessonTable" property="id" value="${param.id}"></x:set>
    </c:if>

    <c:choose>
        <c:when test="${forward.name == 'add'}">
            <c:redirect url="addLesson.jsp"/>
        </c:when>
        <c:when test="${forward.name == 'deleted'}">
            <script>
                alert("<fmt:message key='eLearning.alert.label.lessondeleted'/>");
                window.location = "lessons.jsp";
            </script>
        </c:when>
        <c:when test="${forward.name == 'notSelected'}">
            <script>
                alert("<fmt:message key='eLearning.alert.label.lessonnotselected'/>");

            </script>
        </c:when>
        <c:when test="${forward.name == 'activate'}">
            <script>
                alert("<fmt:message key='eLearning.general.message.activated'/>");
                /*window.location = "lessons.jsp";*/
            </script>
        </c:when>
        <c:when test="${forward.name == 'deactivate'}">
            <script>
                alert("<fmt:message key='eLearning.general.message.deactivated'/>");
                /* window.location = "lessons.jsp";*/
            </script>
        </c:when>
    </c:choose>

    <%@ include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key='eLearning.menu.label.Instructor_Menu'/> > <A HREF="lessons.jsp"><fmt:message
                    key='eLearning.menu.label.registerlesson'/></A></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="lesson.lessonTable"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>