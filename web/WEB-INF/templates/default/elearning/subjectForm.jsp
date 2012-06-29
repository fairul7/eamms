<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 9, 2004
  Time: 12:59:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<jsp:include page="../form_header.jsp"/>

<c:if test="${!(empty widget.id)}">
    <c:if test="${!(empty widget.message)}">
        <script>alert("<c:out value="${widget.message}"/>");</script>
    </c:if>
    <table width="100%" cellpadding="2" cellspacing="1">
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key="security.label.personalInformation"/></span></td></tr>
        <tr>
            <td valign="top"><fmt:message key="eLearning.course.label.courseName"/>*</td>
            <td><x:display name="${widget.courseName.absoluteName}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key="eLearning.course.label.instructor"/></td>
            <td><x:display name="${widget.instructor.absoluteName}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key="eLearning.course.label.synopsis"/></td>
            <td><x:display name="${widget.synopsis.absoluteName}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key="eLearning.course.label.category"/></td>
            <td><x:display name="${widget.courseType.absoluteName}"/></td>
        </tr>

        <tr><td colspan="2">&nbsp;</td></tr>
        <tr>
            <td>&nbsp;</td>
            <td><x:display name="${widget.userButton.absoluteName}"/> <x:display name="${widget.cancelButton.absoluteName}"/></td>
        </tr>
    </table>
</c:if>
<jsp:include page="../form_footer.jsp"/>