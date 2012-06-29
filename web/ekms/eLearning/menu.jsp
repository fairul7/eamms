<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.crm.helpdesk.HelpdeskHandler,
                 com.tms.collab.messaging.model.Util,
                 com.tms.collab.messaging.model.Folder"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	ArrayList items = new ArrayList();




%>
     <x:permission permission="com.tms.elearning.course.View" module="com.tms.elearning.course.model.CourseModule">
<%
      //Instructor Menu
      items.add(new MenuItem(app.getMessage("eLearning.menu.label.Instructor_Menu"), null, null, null, null, null));
      items.add(new MenuItem(app.getMessage("eLearning.menu.label.Courses"), "/ekms/eLearning/courses.jsp", null, null, null, null));


%>
     </x:permission>

      <x:permission permission="com.tms.elearning.folder.View" module="com.tms.elearning.course.model.CourseModule">
<%

     items.add(new MenuItem(app.getMessage("eLearning.menu.label.Modules"), " /ekms/eLearning/folders.jsp", null, null, null, null));


%>


      </x:permission>

  <x:permission permission="com.tms.elearning.lesson.View" module="com.tms.elearning.course.model.CourseModule">
  <%
               items.add(new MenuItem(app.getMessage("eLearning.menu.label.Lessons"), "/ekms/eLearning/lessons.jsp", null, null, null, null));



  %>

 </x:permission>
 
 <x:permission permission="com.tms.elearning.testware.View" module="com.tms.elearning.course.model.CourseModule">
  <%


        items.add(new MenuItem(app.getMessage("eLearning.menu.label.Questions"), "/ekms/eLearning/question.jsp", null, null, null, null));



  %>

   </x:permission>


 <x:permission var="inst" permission="com.tms.elearning.testware.View" module="com.tms.elearning.course.model.CourseModule" />

   <c:choose>
   <c:when test="${inst}">
 <%

       items.add(new MenuItem(app.getMessage("eLearning.menu.label.Assessments"), "/ekms/eLearning/assessment.jsp", null, null, null, null));

       //for instructor to become student
       items.add(new MenuItem(app.getMessage("eLearning.menu.label.Student_Menu"), null, null, null, null, null));
       items.add(new MenuItem(app.getMessage("eLearning.menu.label.RegisterCourse"), "/ekms/eLearning/subjects.jsp", null, null, null, null));
       items.add(new MenuItem(app.getMessage("eLearning.menu.label.RegisteredCourses"), "/ekms/eLearning/regCourses.jsp", null, null, null, null));


 %>
   </c:when>
   <c:otherwise>

<%
    //student
    items.add(new MenuItem(app.getMessage("eLearning.menu.label.Student_Menu"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("eLearning.menu.label.RegisterCourse"), "/ekms/eLearning/subjects.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("eLearning.menu.label.RegisteredCourses"), "/ekms/eLearning/regCourses.jsp", null, null, null, null));


%>
</c:otherwise>
</c:choose>

       <x:permission permission="com.tms.elearning.coursecategory.View" module="com.tms.elearning.coursecategory.model.CategoryModule">

  <%

       items.add(new MenuItem(app.getMessage("eLearning.menu.label.Administrator_Menu"), null, null, null, null, null));

           
           items.add(new MenuItem(app.getMessage("eLearning.menu.label.Course_Category_Setup"), "/ekms/eLearning/category.jsp", null, null, null, null));



  %>



       </x:permission>




<%
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>


