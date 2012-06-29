<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%
    pageContext.setAttribute("treeMode", new Boolean(true));
%>

<x:config>
<page name="course">
    <com.tms.elearning.core.ui.LearningTree name="courseTree" width="100%">
        <forward name="add" url="addCourse.jsp"/>
<%--
        <listener_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                return new Forward(null, "editCourse.jsp?id=" + id, true);
            }
        </listener_script>
--%>
    </com.tms.elearning.core.ui.LearningTree>
</page>
</x:config>

      <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td style="background: #003366; vertical-align: top;"><span style="color: white; font-weight: bold;">
              <c:choose>
              <c:when test="${treeMode}">
                    <fmt:message key='courseside.label.courseTree'/>
              </c:when>
              <c:otherwise>
                    <fmt:message key='contentside.label.contentModules'/>
              </c:otherwise>
              </c:choose>
                </span><br>
              </td>
            </tr>
            <tr>
              <td style="background: #DDDDDD; vertical-align: top;">

              <c:choose>
              <c:when test="${treeMode}">
                    <x:display name="course.courseTree"/>
              </c:when>
              <c:otherwise>
                    <x:display name="cms.contentModuleList"/>
              </c:otherwise>
              </c:choose>

                <br> <br> <br> <br> <br>
              </td>
            </tr>
          </tbody>
        </table>


