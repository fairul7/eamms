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
          <page name="assessment">
               <com.tms.elearning.testware.ui.resultAssessmentsForm name="resultAssessmentForm" width="100%" >



                    <listener_script>
                        String id = event.getRequest().getParameter("id");
                        if (id != null) {

                        }
                    </listener_script>
                </com.tms.elearning.testware.ui.resultAssessmentsForm>
         </page>
     </x:config>






<c:choose>

                                            <c:when test="${forward.name == 'back'}" >
                                            <script>


                                            window.location="AssessmentByModule.jsp?id=<c:out value="${widgets['assessment.resultAssessmentForm'].module_id}"/>";
                                            </script>

                                            </c:when>



                                            </c:choose>




<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <fmt:message key='eLearning.menu.label.RegisteredCourses'/> > <fmt:message key='eLearning.menu.label.Assessments'/> > <fmt:message key='eLearning.menu.label.listofquestions'/> > <fmt:message key='eLearning.assessment.assessmentstatistic'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">


<x:display name="assessment.resultAssessmentForm"/>
        </td></tr>
                       <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
                   </table>


<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>


