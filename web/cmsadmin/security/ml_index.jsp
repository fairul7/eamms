<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.maillist.ManageMailList" module="com.tms.cms.maillist.model.MailListModule" url="noPermission.jsp" />

<x:config>
    <page name="page">
        <portlet name="portlet" text="<fmt:message key='maillist.label.mailingLists'/>" width="100%" permanent="true">
            <com.tms.cms.maillist.ui.MailListUi name="mailListUi" showMenu="false" />
        </portlet>
    </page>
</x:config>
<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>
<jsp:include page="/cmsadmin/includes/headerSecurity.jsp" flush="true"/>
<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
    <tbody>
        <tr>
            <td style="vertical-align: top; width: 250px;">
                <jsp:include page="/cmsadmin/includes/sideSecurityMailList.jsp" flush="true"/>
            </td>
            <td style="vertical-align: top;">
            <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
                <tbody>
                    <tr>
                        <td style="vertical-align: top;">
                            <br>
                            <p>
                            <x:display name="page.portlet"/>
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