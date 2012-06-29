<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 19, 2004
  Time: 6:44:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="lesson">
        <com.tms.elearning.lesson.ui.LessonFormEdit name="lessonEditForm" width="100%">


        </com.tms.elearning.lesson.ui.LessonFormEdit>
    </page>
</x:config>

<x:permission permission="com.tms.elearning.lesson.Edit" module="com.tms.elearning.course.model.CourseModule" url="noPermission.jsp">

    <c:if test="${!empty param.id}">
        <x:set name="lesson.lessonEditForm" property="id" value="${param.id}" ></x:set>
    </c:if>

    <c:choose>
        <c:when test="${forward.name == 'updated'}" >
            <script>
                alert("<fmt:message key='eLearning.alert.label.lessonupdated'/>");
                window.location="lessons.jsp";
            </script>
        </c:when>
        <c:when test="${forward.name == 'cancel'}" >
            <script>
                history.back();
            </script>
        </c:when>
    </c:choose>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.Instructor_Menu'/> > <fmt:message key='eLearning.menu.label.editlesson'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="lesson.lessonEditForm"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
</x:permission>

<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>