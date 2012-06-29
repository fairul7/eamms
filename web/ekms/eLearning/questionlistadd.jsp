<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 8, 2004
  Time: 5:52:33 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<!-- Declare Widgets -->
<x:config>
    <page name="course">
        <com.tms.elearning.testware.ui.QuestionTableNew name="table2" width="100%"/>


    </page>


</x:config>



<x:permission permission="com.tms.elearning.testware.Add" module="com.tms.elearning.course.model.CourseModule">




<c:if test="${!empty param.id}">
    <%--  id for assessment--%>
    <x:set name="course.table2" property="examid" value="${param.id}"></x:set>
    <x:set name="course.table2" property="id" value="${param.id}"></x:set>

</c:if>


<c:choose>
    <c:when test="${forward.name == 'addToExam'}">
        <script>
            alert("<fmt:message key='eLearning.alert.label.assessmentadded'/>");
 /*           window.location = "assessmentEdit.jsp?id=<c:out value="${widgets['course.table2'].id}"/>";*/
        </script>
    </c:when>

    <c:when test="${forward.name == 'cancel'}">
        <script>
            window.location = "assessmentEdit.jsp?id=<c:out value="${widgets['course.table2'].id}"/>";

        </script>
    </c:when>
</c:choose>

<%--

<%@ include file="/ekms/includes/header.jsp" %>

<jsp:include page="includes/header.jsp"/>



--%>

<%--<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><b>
            <font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <A
                    HREF="assessment.jsp"><fmt:message key='eLearning.menu.label.registerAssessment'/></A> > <A
                    HREF="assessmentEdit.jsp?cn=assessment.assessmentTable&et=sel&id=<c:out value="${param.id}" />">
                <fmt:message key='eLearning.menu.label.editcourse'/></A> > <fmt:message
                    key='eLearning.menu.label.addquestionsfromquestionsbank'/></font></b>
        </font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="course.table1"/>

    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>--%>



<table width="100%" border="0" cellspacing="0" cellpadding="5">
 <tr valign="middle">
     <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.Questions'/></font></b></td>
     <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
 </tr>
 <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
 <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

    <x:display name="course.table2"/>



 </td></tr>
 <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
 </table>


      </x:permission>




   <%-- <jsp:include page="includes/footer.jsp"/>
       <%@ include file="/ekms/includes/footer.jsp" %>
   --%>

