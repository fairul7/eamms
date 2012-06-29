<%@ page import="com.tms.cms.core.model.ContentManager,
                 kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" scope="request" value="${widget}"/>

<%--      <table cellpadding="1" cellspacing="0" border="0" style="text-align: left; width: 100%; border:1px solid gray;">--%>
      <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td class="optionheader" style="background: #003366; vertical-align: top;"><span style="color: white; font-weight: bold;">
                <fmt:message key='eLearning.course.label.courseOptions'/>
                </span><br>
              </td>
            </tr>

            <tr>
              <td class="optioncell"  style="background: #DDDDDD; vertical-align: top;"><img src="../../../ekms/images/showdetailicon.gif">
              <a href="../../../ekms/eLearning/addFolder.jsp?cid=<c:out value='${co.id}'/>" class="option" target="_self"><fmt:message key='eLearning.course.label.addModule'/></a>
              </td>
            </tr>

            <tr>
              <td class="optioncell">
              </td>
            </tr>
            <tr>
              <td class="optioncell"  style="background: #DDDDDD; vertical-align: top;"><img src="../../../ekms/images/showdetailicon.gif">
<%--                <x:event name="${co.absoluteName}" html="class='option'" type="CheckOut"><fmt:message key='eLearning.course.label.addLesson'/></x:event>--%>
                <a href="../../../ekms/eLearning/addLesson.jsp?cid=<c:out value='${co.id}'/>" class="option" target="_self"><fmt:message key='eLearning.course.label.addLesson'/></a>
              </td>
            </tr>

            <tr>
              <td class="optioncell">
              </td>
            </tr>
            <tr>
              <td class="optioncell"  style="background: #DDDDDD; vertical-align: top;"><img src="../../../ekms/images/showdetailicon.gif">
               <a href="../../../ekms/eLearning/assessmentAdd.jsp?cid=<c:out value='${co.id}'/>" class="option" target="_self"><fmt:message key='eLearning.course.label.addExam'/></a>
              </td>
            </tr>

            <tr>
              <td class="optioncell"><br> </td>
            </tr>
          </tbody>
        </table>

