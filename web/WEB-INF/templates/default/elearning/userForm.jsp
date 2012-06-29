<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 24, 2004
  Time: 12:26:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<jsp:include page="../form_header.jsp"/>


    <table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
        <tr><td colspan="2"><table width="100%" cellpadding="3" cellspacing="1"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key="user.label.personalInformation"/></span></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="user.label.usertype"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.usertype.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="user.label.firstname"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.firstName.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="user.label.lastname"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.lastName.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="user.label.username"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.username.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="user.label.password"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.password.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="user.label.confirmPassword"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.confirmPassword.absoluteName}"/></td>
        </tr>
        <tr><td class="classRowLabel" valign="top" align="right">&nbsp;</td><td class="classRowLabel" valign="top" align="right"></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
            <!--<td class="classRow"><x:display name="${widget.childMap.submit.absoluteName}"/> <input type="button" class="button" value="Cancel" onclick="self.location='user.jsp'"></td> -->
            <td class="classRow"><x:display name="${widget.userButton.absoluteName}"/> <input type="button" class="button" value="Cancel" onclick="self.location='user.jsp'"></td>
        </tr>
    </table>

<jsp:include page="../form_footer.jsp"/>