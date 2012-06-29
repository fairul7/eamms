<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
    <tbody>
        <tr>
            <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='security.label.groups'/></span><br>
            </td>
        </tr>
        <tr>
            <td style="background: #DDDDDD; vertical-align: top;">
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/group.jsp?cn=group.groupPortlet.groupForm.viewGroup&et=action&button*group.groupPortlet.groupForm.addGroup.cancelButton=Cancel"><fmt:message key='security.label.groupListing'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/group.jsp?cn=group.groupPortlet.groupForm.viewGroup&et=action&button*group.groupPortlet.groupForm.viewGroup.add=Add"><fmt:message key='security.label.newGroup'/></a>

            <c:set var="groupWidget" value="${widgets['group.groupPortlet.groupForm.editGroup']}"/>
            <c:if test="${!empty groupWidget && !groupWidget.hidden}">
                <script>
                    function contentSecurityPopup() {
                        window.open('<c:url value="/cmsadmin/content/contentSecurityPopup.jsp"/>?principalId=<c:out value="${groupWidget.id}"/>&group=true', 'contentSecurityPopup', 'address=no,scrollbars=yes,status=yes');
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

