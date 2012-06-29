<%@ include file="/common/header.jsp" %>
<%--<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />--%>
<c:set var="co" scope="session" value="${widget}"/>
<x:config>
<page name="courseView">
    <portlet name="courseModulesPortlet" text="<fmt:message key='eLearning.course.label.courseModules'/>" width="100%" permanent="true">
        <com.tms.elearning.core.ui.CourseModulesTables name="courseModulesTable" width="100%">
        </com.tms.elearning.core.ui.CourseModulesTables>
    </portlet>
    <portlet name="courseLessonsPortlet" text="<fmt:message key='eLearning.course.label.courseLessons'/>" width="100%" permanent="true">
            <com.tms.elearning.core.ui.CourseLessonsTable name="courseLessonsTable" width="100%">
            </com.tms.elearning.core.ui.CourseLessonsTable>
    </portlet>
    <portlet name="courseExamsPortlet" text="<fmt:message key='eLearning.course.label.courseExams'/>" width="100%" permanent="true">
               <com.tms.elearning.core.ui.CourseExamsTable name="courseExamsTable" width="100%">
               </com.tms.elearning.core.ui.CourseExamsTable>
    </portlet>
    <com.tms.elearning.core.ui.CourseMenuPanel name="courseMenuPanel">
    </com.tms.elearning.core.ui.CourseMenuPanel>
</page>
</x:config>
<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header1.jsp"/>
<table cellpadding="0" cellspacing="0" border="0"
 style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">
        <jsp:include page="../eLearning/includes/sideCourseTree.jsp" flush="true"/>
      </td>
      <td style="vertical-align: top;">
      <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

                  <c:out value='${co.path}'/>
                  <%--Course Details--%>
                  <p>
                  <x:display name="courseView.courseModulesPortlet"/>
                  <br>
                  <br>
                  <x:display name="courseView.courseLessonsPortlet"/>
                  <br>
                  <br>
                  <x:display name="courseView.courseExamsPortlet"/>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
      <td style="vertical-align: top; width: 200px; padding:10px">
      <%--Course Menu Options--%>
          <x:display name="courseView.courseMenuPanel"/>
        </td>
    </tr>
  </tbody>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>

