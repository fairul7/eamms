<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Oct 26, 2004
  Time: 7:38:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<jsp:include page="../form_header.jsp"/>

    <table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
        <tr><td colspan="2"><table width="100%" cellpadding="3" cellspacing="1"></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.lesson.label.lessonName"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.name.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.lesson.label.brief"/></td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.brief.absoluteName}"/></td>
        </tr>

        <tr><td class="classRowLabel" valign="top" align="right">&nbsp;</td><td class="classRowLabel" valign="top" align="right"></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
            <td class="classRow"><input type="button" class="button" value="Cancel" onclick="self.location='regCourses.jsp'"></td>
        </tr>
    </table>

<jsp:include page="../form_footer.jsp"/>