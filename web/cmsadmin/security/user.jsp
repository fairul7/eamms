<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="kacang.services.security.User.view" module="kacang.services.security.SecurityService" url="noPermission.jsp" />

<x:config>
    <page name="user">
        <portlet name="userPortlet" text="<fmt:message key='security.label.users'/>" width="100%" permanent="true">
            <kacang.services.security.ui.UserForm name="userForm"/>
        </portlet>
    </page>
</x:config>
<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>
<jsp:include page="/cmsadmin/includes/headerSecurity.jsp" flush="true"/>
<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
    <tbody>
        <tr>
            <td style="vertical-align: top; width: 250px;">
                <jsp:include page="/cmsadmin/includes/sideSecurityUser.jsp" flush="true"/>
            </td>
            <td style="vertical-align: top;">
            <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 98%;">
                <tbody>
                    <tr>
                        <td style="vertical-align: top;">
                            <br>
                            <p>
                            <x:display name="user.userPortlet"/>
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