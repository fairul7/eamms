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
    <com.tms.elearning.lesson.ui.LessonFormRead name="lessonFormRead" width="100%">
    <forward name="updated" url="lessons.jsp" redirect="true"/>
        <request_script>

            String id = event.getRequest().getParameter("id");
               String id = event.getRequest().getParameter("id");
            if (id != null) {
                editForm = wm.getWidget("lesson.lessonFormRead");
                editForm.setId(id);
            }
        </request_script>
    </com.tms.elearning.lesson.ui.LessonFormRead>
</page>
</x:config>



<c:if test="${!empty param.id}">

    <x:set name="lesson.lessonFormRead" property="id" value="${param.id}" ></x:set>

</c:if>



<c:if test="${!empty param.moduleId}">

    <x:set name="lesson.lessonFormRead" property="moduleId" value="${param.moduleId}" ></x:set>

</c:if>







<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>

           <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <A HREF="regCourses.jsp"><fmt:message key='eLearning.menu.label.RegisteredCourses'/></A> > <a href="javascript:history.back()"><fmt:message key='eLearning.menu.label.Modules'/></A> > <fmt:message key='eLearning.menu.label.Lesson'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="lesson.lessonFormRead"/>


       </td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
            </table>


<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>
