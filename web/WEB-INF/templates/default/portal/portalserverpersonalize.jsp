<%@ page import="kacang.Application,
                 kacang.runtime.filter.RuntimeFilter"%>
<%@include file="/common/header.jsp" %>
<c:set var="widget" value="${widget}"/>
<x:config>
    <page name="portalserverpersonalize">
        <kacang.services.security.ui.UserLocaleForm name="localeForm"/>
    </page>
</x:config>
<c-rt:set var="allowUserLocale" value="<%= Application.getInstance().getProperty(RuntimeFilter.PROPERTY_USER_LOCALE) %>"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="3" cellspacing="0" width="100%" class="portletBackground">
    <tr><td colspan="2" class="portletTableHeader"><fmt:message key='portlet.message.portalServerPersonalization'/></td></tr>
    <tr><td colspan="2" class="portletLabel">&nbsp;</td></tr>
    <c:if test="${allowUserLocale}">
    <tr>
        <td width="20%" class="portletLabel" align="right" nowrap><fmt:message key='general.label.language'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="portalserverpersonalize.localeForm.localeBox"/></td>
    </tr>
    <tr>
        <td width="20%" class="portletLabel" align="right">&nbsp;</td>
        <td width="80%" class="portletLabel">
            <x:display name="portalserverpersonalize.localeForm.buttonPanel.submit"/>
            <x:display name="portalserverpersonalize.localeForm.buttonPanel.cancel_form_action"/>
        </td>
    </tr>
    <tr><td colspan="2" class="portletLabel">&nbsp;</td></tr>
    </c:if>
    <tr>
        <td width="20%" class="portletLabel" align="right" nowrap><fmt:message key='portlet.message.themes'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.themes.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" class="portletLabel" align="right">&nbsp;</td>
        <td width="80%" class="portletLabel">
            <x:display name="${widget.update.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
        </td>
    </tr>
    <tr><td colspan="2" class="portletLabel">&nbsp;</td></tr>
    <tr>
        <td width="20%" class="portletLabel" align="right" nowrap><fmt:message key='portlet.message.portlets'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.portlets.absoluteName}"/></td>
    </tr>
    <c:if test="${!(empty widget.selectedPortlet)}">
        <tr>
            <td width="20%" class="portletLabel" align="right" valign="top" nowrap><fmt:message key='portlet.message.portletInformation'/>&nbsp;</td>
            <td width="80%" class="portletLabel">
                <table cellpadding="3" cellspacing="0" width="100%">
                    <tr>
                        <td class="portletRow" width="10%" nowrap><fmt:message key='portlet.message.name'/>&nbsp;</td>
                        <td class="portletRow" align="left" ><c:out value="${widget.selectedPortlet.portletName}"/></td>
                    </tr>
                    <tr>
                        <td class="portletRow" width="10%" nowrap><fmt:message key='portlet.message.description'/>&nbsp;</td>
                        <td class="portletRow" ><c:out value="${widget.selectedPortlet.portletDescription}"/></td>
                    </tr>
                    <tr>
                        <td class="portletRow" width="10%"><x:display name="${widget.addPortlet.absoluteName}"/></td>
                        <td class="portletRow" width="80%">&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
    </c:if>
    <tr><td class="portletFooter" colspan="2">&nbsp;</td></tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
