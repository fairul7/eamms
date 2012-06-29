<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
    <tbody>
        <tr>
            <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='security.label.users'/></span><br>
            </td>
        </tr>
        <tr>
            <td style="background: #DDDDDD; vertical-align: top;">
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/user.jsp?cn=user.userPortlet.userForm.viewUser&et=action&button*user.userPortlet.userForm.addUser.cancelButton=Cancel"><fmt:message key='security.label.userListing'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/user.jsp?cn=user.userPortlet.userForm.viewUser&et=action&button*user.userPortlet.userForm.viewUser.add=Add"><fmt:message key='security.label.newUser'/></a>

            <c:set var="userWidget" value="${widgets['user.userPortlet.userForm.editUser']}"/>
            <c:if test="${!empty userWidget && !userWidget.hidden}">
                <script>
                    function contentSecurityPopup() {
                        window.open('<c:url value="/cmsadmin/content/contentSecurityPopup.jsp"/>?principalId=<c:out value="${userWidget.id}"/>', 'contentSecurityPopup', 'address=no,scrollbars=yes,status=yes');
                        return false;
                    }
                </script>
                <li><a class="option" href="#" onclick="return contentSecurityPopup()"><fmt:message key='cms.label.contentPermissions'/></a>
            </c:if>
            <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
            <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
            </td>
        </tr>
    </tbody>
</table>
<br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>

