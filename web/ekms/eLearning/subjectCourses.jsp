<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 9, 2004
  Time: 3:39:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->
<x:config>
    <page name="course">
        <com.tms.elearning.course.ui.ListTable name="listTable" width="100%">

        </com.tms.elearning.course.ui.ListTable>
    </page>
</x:config>

<%
    String categoryid="";
%>

<c:if test="${!empty param.id}">
    <script>
        window.open("displaySynopsis.jsp?id=<c:out value="${param.id}"/>", "_new",
                "height=400,width=400,status=yes,toolbar=no,menubar=no,location=no, scrollbars=1, resizable=1");
    </script>
</c:if>

<c:choose>
    <c:when test="${!empty param.categoryid}">
        <x:set name="course.listTable" property="categoryid" value="${param.categoryid}"></x:set>
        <c:set var="somethingName" value="${param.categoryid}" scope="session"/>
    </c:when>
    <c:otherwise>
        <script>
            window.location="subjectCourses.jsp?categoryid=<c:out value="${somethingName}"/>";
        </script>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${forward.name == 'notSelected'}">
        <script>
            alert("<fmt:message key='eLearning.alert.label.coursenotselected'/>");
        </script>
    </c:when>
    <c:when test="${forward.name == 'register'}">
        <script>
            alert("<fmt:message key='eLearning.alert.label.successregistered'/>");
            window.location = "regCourses.jsp";
        </script>
    </c:when>
</c:choose>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key='eLearning.menu.label.Student_Menu'/> > <fmt:message key='eLearning.menu.label.Courses'/>
        </font>
        </b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="course.listTable"/>

    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<p>

    <jsp:include page="includes/footer.jsp"/>
    <%@ include file="/ekms/includes/footer.jsp" %>


