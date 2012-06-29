<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
    <tbody>
        <tr>
            <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='security.label.subscriptions'/></span><br>
            </td>
        </tr>
        <tr>
            <td style="background: #DDDDDD; vertical-align: top;">
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/subscription.jsp?cn=subscription.subscriptionPortlet.subscriptionForm.viewSubscription&et=action&button*subscription.subscriptionPortlet.subscriptionForm.addSubscription.cancelButton=Cancel"><fmt:message key='security.label.subscriptionListing'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/security/subscription.jsp?cn=subscription.subscriptionPortlet.subscriptionForm.viewSubscription&et=action&button*subscription.subscriptionPortlet.subscriptionForm.viewSubscription.add=Add"><fmt:message key='security.label.newSubscription'/></a>
            <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
            <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
            </td>
        </tr>
    </tbody>
</table>
<br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>

