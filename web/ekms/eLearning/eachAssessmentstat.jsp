
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
                          <page name="eachAssessmentStat">
                               <com.tms.elearning.testware.ui.eachAssessmentStatTable name="table1" width="100%" />
                         </page>
                     </x:config>


 <c:if test="${!empty param.assessment_id}">

    <x:set name="eachAssessmentStat.table1" property="assessmentId" value="${param.assessment_id}" ></x:set>

</c:if>

                                             <c:choose>
                                             <c:when test="${forward.name == 'back'}" >
                                             <script>
                                             window.location="resultAssessments.jsp";

                                             </script>
                                             </c:when>
                                              </c:choose>






                <%@include file="/ekms/includes/header.jsp" %>
                <jsp:include page="includes/header.jsp" />
            <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <fmt:message key='eLearning.menu.label.RegisteredCourses'/> > <fmt:message key='eLearning.menu.label.Assessments'/> > <fmt:message key='eLearning.menu.label.listofquestions'/> > <fmt:message key='eLearning.assessment.assessmentstatistic'/> > <fmt:message key='eLearning.menu.label.history'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

                    <x:display name="eachAssessmentStat.table1"/>

                </td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
            </table>


                 <p>
       <jsp:include page="includes/footer.jsp" />
       <%@include file="/ekms/includes/footer.jsp" %>


