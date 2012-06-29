<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.subscription.Subscription.view" module="com.tms.cms.subscription.SubscriptionHandler" url="noPermission.jsp" />

<x:config>
    <page name="subscription">
        <portlet name="subscriptionPortlet" text="<fmt:message key='security.label.subscriptions'/>" width="100%" permanent="true">
            <com.tms.cms.subscription.ui.SubscriptionForm name="subscriptionForm"/>
        </portlet>
    </page>
</x:config>
<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>
<jsp:include page="/cmsadmin/includes/headerSecurity.jsp" flush="true"/>
<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 95%;">
    <tbody>
        <tr>
            <td style="vertical-align: top; width: 250px;">
                <jsp:include page="/cmsadmin/includes/sideSecuritySubscription.jsp" flush="true"/>
            </td>
            <td style="vertical-align: top;">
            <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
                <tbody>
                    <tr>
                        <td style="vertical-align: top;">
                            <br>
                            <p>
                            <x:display name="subscription.subscriptionPortlet"/>
                            <br>
                            <br>
                            <br>
                            <br>
                            <br>
                            <br>
                            <br>
                        </td>
                    </tr>
                </tbody>
            </table>
            </td>
        </tr>
    </tbody>
</table>
<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>