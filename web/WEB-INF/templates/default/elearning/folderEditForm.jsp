<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Oct 25, 2004
  Time: 11:34:16 AM
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
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.folder.label.folderName"/>*</td>
                    <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.name.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.folder.label.course"/></td>
                    <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.courseId.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.folder.label.introduction"/></td>
                    <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.introduction.absoluteName}"/></td>
                </tr>
                <tr>
                <td class="classRowLabel" valign="top" align="right"><fmt:message key='eLearning.element.active'/></td>
                <td class="classRow"><x:display name="${widget.childMap.ispublic.absoluteName}"/></td>
                </tr>
                <tr><td class="classRowLabel" valign="top" align="right">&nbsp;</td><td class="classRowLabel" valign="top" align="right"></td></tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow"><x:display name="${widget.childMap.submit.absoluteName}"/> <input type="button" class="button" value="Cancel" onclick="self.location='folders.jsp'"></td>
                </tr>
            </table>

<jsp:include page="../form_footer.jsp"/>
