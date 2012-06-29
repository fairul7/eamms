<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
    <tbody>
        <tr>
            <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='maillist.label.mailingLists'/></span><br>
            </td>
        </tr>
        <tr>
            <td style="background: #DDDDDD; vertical-align: top;">

            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/ml_index.jsp?cn=page.portlet.mailListUi&et=showComposedMailListTable"><fmt:message key='maillist.label.composedLists'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/ml_index.jsp?cn=page.portlet.mailListUi&et=showContentMailListTable"><fmt:message key='maillist.label.contentLists'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/ml_index.jsp?cn=page.portlet.mailListUi&et=showScheduledMailListTable"><fmt:message key='maillist.label.scheduledLists'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/ml_index.jsp?cn=page.portlet.mailListUi&et=showMailTemplateTable"><fmt:message key='maillist.label.mailingListTemplates'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/ml_index.jsp?cn=page.portlet.mailListUi&et=showMailLogTable"><fmt:message key='maillist.label.mailingListLog'/></a>

        <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
        <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
            </td>
        </tr>
    </tbody>
</table>
<br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>

