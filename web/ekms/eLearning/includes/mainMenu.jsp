<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Dec 8, 2004
  Time: 6:11:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="1" class="menuBgColor">
<tr>
   <td>
       <table width="100%" border="0" cellpadding="1" cellspacing="0" class="menuBgColor">
           <tr>
               <td>
                   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="menuBgColor">
                       <tr>
                           <td colspan="3" valign="middle" bgcolor="666666" class="menuBgColor" height="25">
                               <table cellpadding="0" cellspacing="0" width="100%">
                                   <tr>

                                        <td font color="FFFFFF" class="menuFont" ><b>Corporate eLearning</b></td>

                                   </tr>
                               </table>
                           </td>
                       </tr>
                          <tr>
                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="/ekms/eLearning/index.jsp" />"><font color="FFFFFF" class="menuFont"><b>Main</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                       </tr>
                       <%-- Start Instructor Menu--%>
                       <x:permission permission="com.tms.elearning.course.View" module="com.tms.elearning.course.model.CourseModule">
                       <tr>
                           <td colspan="3" valign="middle" bgcolor="666666" class="menuBgColor" height="25">
                               <table cellpadding="0" cellspacing="0" width="100%">
                                   <tr>

                                       <td  class="menuFont" ><b>Instructor Menu</b></td>

                                   </tr>
                               </table>
                           </td>
                       </tr>



                       <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>

                       <tr>
                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="courses.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Courses</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                       </tr>
                       </x:permission>

                       <x:permission permission="com.tms.elearning.folder.View" module="com.tms.elearning.course.model.CourseModule">
                       <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr>
                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="folders.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Modules</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                       </tr>
                       </x:permission>

                       <x:permission permission="com.tms.elearning.lesson.View" module="com.tms.elearning.course.model.CourseModule">
                       <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr>
                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="lessons.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Lessons</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                       </tr>
                       </x:permission>

                       <x:permission permission="com.tms.elearning.testware.View" module="com.tms.elearning.course.model.CourseModule">
                       <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr>
                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="question.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Question Bank</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                       </tr>
                       </x:permission>

                       <x:permission permission="com.tms.elearning.testware.View" module="com.tms.elearning.course.model.CourseModule">
                       <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr>
                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="assessment.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Assessments</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                       </tr>
                       </x:permission>
                       <%-- End Instructor Menu--%>
                       <%-- Start Student Menu --%>
                       <tr>
                           <td colspan="3" valign="middle" bgcolor="666666" class="menuBgColor" height="25">
                               <table cellpadding="0" cellspacing="0" width="100%">
                                   <tr>

                                       <td  class="menuFont" ><b>Student Menu<b></b></td>

                                   </tr>
                               </table>
                           </td>
                       </tr>
                       <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr>

                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="subjects.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Register a Course</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>

                       </tr>
                       <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>
                       <tr>

                           <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                           <td height="28" width="75%"><a href="<c:url value="regCourses.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Registered Courses</b></font></a></td>
                           <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>

                       </tr>

                       <%-- End Student Menu --%>

                        <%-- Start Admin Menu --%>

                        <x:permission permission="com.tms.elearning.course.View" module="com.tms.elearning.course.model.CourseModule">
                              <tr>
                                  <td colspan="3" valign="middle" bgcolor="666666" class="menuBgColor" height="25">
                                      <table cellpadding="0" cellspacing="0" width="100%">
                                          <tr>

                                              <td  class="menuFont" ><b>Administrator Menu</b></td>

                                          </tr>
                                      </table>
                                  </td>
                              </tr>
                              <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="1"></td></tr>
                              <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="5" height="1"></td></tr>
                              <tr>

                                  <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/images/folder.gif"/>" width="15" height="17"></td>
                                  <td height="28" width="75%"><a href="<c:url value="category.jsp"/>"><font color="FFFFFF" class="menuFont"><b>Course Category Setup</b></font></a></td>
                                  <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>

                              </tr>
                               </x:permission>
                             <%-- End Student Menu --%>
                   </table>

               </td>
           </tr>
       </table>
   </td>
</tr>
</table>
