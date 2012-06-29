<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 12, 2004
  Time: 3:47:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->
<x:config>
    <page name="course">
        <com.tms.elearning.course.ui.RegisteredTable name="regCoursesTable" width="100%" >
                <%--   <forward name="add" url="subjects.jsp"/>
                --%>

                <%-- <listener_script>
                    String id = event.getRequest().getParameter("id");
                    if (id != null) {

                    }
                </listener_script>--%>
        </com.tms.elearning.course.ui.RegisteredTable>
    </page>
</x:config>

<c:if test="${!empty param.id}">
    <script>
        window.location="foldersStud.jsp?cid=<c:out value="${param.id}" />";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.Student_Menu'/> > <A HREF="regCourses.jsp"><fmt:message key='eLearning.menu.label.RegisteredCourses'/></A></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="course.regCoursesTable"/>

    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>




















<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>


