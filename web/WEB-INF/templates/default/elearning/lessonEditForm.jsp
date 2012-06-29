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
    <tr><td colspan="2">
        <table width="100%" cellpadding="3" cellspacing="1">
    </td></tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.lesson.label.lessonName"/>
            *</td>
        <td class="classRowLabel" valign="top" align="left"><x:display
                name="${widget.childMap.name.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.lesson.label.course"/>*</td>
        <td class="classRowLabel" valign="top" align="left"><x:display
                name="${widget.childMap.courseId.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.lesson.label.folder"/>*</td>
        <td class="classRowLabel" valign="top" align="left"><x:display
                name="${widget.childMap.folderId.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.lesson.label.brief"/></td>
        <td class="classRowLabel" valign="top" align="left"><x:display
                name="${widget.childMap.brief.absoluteName}"/></td>
    </tr>

    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key='eLearning.element.active'/></td>
        <td class="classRow"><x:display name="${widget.childMap.ispublic.absoluteName}"/></td>
    </tr>

    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.lesson.label.attachment"/></td>
        <td class="classRowLabel" valign="top" align="left"><a target="_blank"
                                                               href="<%= request.getContextPath() %>/storage<c:out value="${widget.filePath}"/>">
            <c:out value="${widget.fileName}"/></a></td>
        <%--            <td class="classRowLabel" valign="top" align="left"><a target="_blank" href="<%= request.getContextPath() %>/storage<c:out value="${widget.filePath}"/>"><c:out value="${widget.fileName}"/></a></td>--%>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message
                key="eLearning.lesson.lable.attachment.new"/></td>
        <td class="classRow"><x:display name="${widget.childMap.attachment.absoluteName}"/></td>
    </tr>

    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message
                key="eLearning.lesson.label.associatedocuments"/></td>
        <td class="classRow"><x:display name="${widget.childMap.eLearningFiles.absoluteName}"/></td>
    </tr>


    <tr><td class="classRowLabel" valign="top" align="right">&nbsp;</td><td class="classRowLabel" valign="top"
                                                                            align="right"></td></tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
        <td class="classRow"><x:display name="${widget.childMap.submit.absoluteName}"/> <input type="button"
                                                                                               class="button"
                                                                                               value="Cancel"
                                                                                               onclick="self.location='lessons.jsp'">
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp"/>
