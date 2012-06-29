<%@ page import="com.tms.elearning.testware.ui.QuestionTableStud"%>
 <%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 8, 2004
  Time: 5:52:33 PM
  To change this template use File | Settings | File Templates.
--%>
       <%@ include file="/common/header.jsp" %>

                     <!-- Declare Widgets -->
                     <x:config>
                          <page name="course">
                               <com.tms.elearning.testware.ui.QuestionTableStud name="table1" width="100%" />
                         </page>
                     </x:config>


 <c:if test="${!empty param.id}">

    <x:set name="course.table1" property="assessmentId" value="${param.id}" ></x:set>

</c:if>

<c-rt:set var="forwardSuccess" value="<%= QuestionTableStud.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">

    <c:redirect url="resultAssessments.jsp?id=${widgets['course.table1'].assessmentId}"/>
</c:if>




                <%@include file="/ekms/includes/header.jsp" %>
                <jsp:include page="includes/header.jsp" />
            <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <A HREF="regCourses.jsp"><fmt:message key='eLearning.menu.label.RegisteredCourses'/></A> > <fmt:message key='eLearning.menu.label.Modules'/> > <a href="javascript:history.back()"><fmt:message key='eLearning.menu.label.Assessments'/></a> > <fmt:message key='eLearning.menu.label.listofquestions'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

                    <x:display name="course.table1"/>

                </td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
            </table>


                 <p>
       <jsp:include page="includes/footer.jsp" />
       <%@include file="/ekms/includes/footer.jsp" %>


