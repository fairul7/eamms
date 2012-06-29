<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>
<x:template type="kacang.services.security.ui.ProfileFormProcess"/>
<x:config>
    <page name="profile">
        <portlet name="profilePortlet" width="100%">
            <panel name="profilePanel"/>
        </portlet>
    </page>
</x:config>
<jsp:include page="/cmsadmin/includes/headerSecurity.jsp" flush="true"/>
<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
    <tbody>
        <tr>
            <td style="vertical-align: top; width: 250px;">
                <jsp:include page="/cmsadmin/includes/sideProfile.jsp" flush="true"/>
            </td>
            <td style="vertical-align: top;">
            <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 98%;">
                <tbody>
                    <tr>
                        <td style="vertical-align: top;">
                            <br>
                            <p>
                            <x:display name="profile.profilePortlet" body="custom">
                                <x:display name="profile.profilePortlet.profilePanel" body="custom">
                                    <x:template type="kacang.services.security.ui.ProfileFormDisplay"/>
                                </x:display>
                            </x:display>
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