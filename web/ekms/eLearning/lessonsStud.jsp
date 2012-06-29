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
    <com.tms.elearning.lesson.ui.LessonTableStud name="lessonTableStud" width="100%">

        <listener_script>
            String id = event.getRequest().getParameter("id");

        </listener_script>
    </com.tms.elearning.lesson.ui.LessonTableStud>
</page>
</x:config>
<c:if test="${!empty param.id}">

    <x:set name="lesson.lessonTableStud" property="moduleId" value="${param.id}" ></x:set>
      
</c:if>



<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <A HREF="regCourses.jsp"><fmt:message key='eLearning.menu.label.RegisteredCourses'/></A> > <a href="javascript:history.back()"><fmt:message key='eLearning.menu.label.Modules'/></A> > <fmt:message key='eLearning.menu.label.Lessons'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:display name="lesson.lessonTableStud"/>
                                  </td></tr>
                                                 <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
                                             </table>


<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>