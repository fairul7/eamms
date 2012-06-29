<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%@ include file="/cmsadmin/includes/headerCommon.jsp" %>

<table width="100%" cellpadding="0" height="35" bgcolor="#336699" border="0" cellspacing="0">
    <form>
        <tr>
            <td valign="middle"><img width="5" height="1" src="../images/clear.gif"></td>
            <td valign="middle">
                <table cellpadding="2" bgcolor="#4779AB" border="0" cellspacing="0" width="100%">
                    <tr>
                        <td valign="middle" width="100"> | <a href="user.jsp?cn=user.userPortlet.userForm.viewUser&et=action&button*user.userPortlet.userForm.addUser.cancelButton=Cancel" class="<%= (startsWith(request, "/cmsadmin/security/user")) ? "submenulink2" : "submenulink" %>"><fmt:message key='security.label.users'/></a></td>
                        <td valign="middle" width="100"> | <a href="group.jsp?cn=group.groupPortlet.groupForm.viewGroup&et=action&button*group.groupPortlet.groupForm.addGroup.cancelButton=Cancel" class="<%= (startsWith(request, "/cmsadmin/security/group")) ? "submenulink2" : "submenulink" %>"><fmt:message key='security.label.groups'/></a></td>
<%--                        <td valign="middle" width="100"> | <a href="subscription.jsp" class="<%= (startsWith(request, "/cmsadmin/security/subscription")) ? "submenulink2" : "submenulink" %>"><fmt:message key='security.label.subscriptions'/></a></td>--%>
                        <td valign="middle" width="100"> | <a href="ml_index.jsp" class="<%= (startsWith(request, "/cmsadmin/security/ml")) ? "submenulink2" : "submenulink" %>"><fmt:message key='maillist.label.mailingLists'/></a></td>
                        <td valign="middle" align="right">&nbsp;</a></td>
                    </tr>
                </table>
            </td>
            <td align="right" valign="middle">
                <table cellpadding="2" border="0" cellspacing="0">
                    <tr><td valign="middle" colspan="5">&nbsp;</td></tr>
                </table>
            </td>
        </tr>
    </form>
</table>
