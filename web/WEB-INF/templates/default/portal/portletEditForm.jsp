<%@ page import="com.tms.portlet.ui.PortletEdit,
                 com.tms.portlet.ui.PortletForm"%>
<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="3" cellspacing="1" width="100%" class="portletBackground">
    <tr><td class="portletTableHeader" colspan="2"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key='portlet.message.portletEdit'/></td></tr>
    <tr><td class="portletLabel" colspan="2">&nbsp;</td></tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right"><fmt:message key='portlet.message.name'/>&nbsp; *</td>
        <td width="80%" class="portletLabel" class="portletLabel"><x:display name="${form.portletName.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap  class="portletLabel" align="right"><fmt:message key='portlet.message.title'/>&nbsp; *</td>
        <td width="80%" class="portletLabel"><x:display name="${form.portletTitle.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap  class="portletLabel" align="right"><fmt:message key='portlet.message.classContext'/>&nbsp; *</td>
        <td width="80%" class="portletLabel"><x:display name="${form.portletClass.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap  class="portletLabel" align="right" valign="top"><fmt:message key='portlet.message.description'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${form.portletDescription.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right" valign="top"><fmt:message key='portlet.message.themes'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${form.themes.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right" valign="top"><fmt:message key='portlet.message.preferences'/>&nbsp;</td>
        <td width="80%" class="portletLabel">
            <table cellpadding="1" cellspacing="0" class="portletPreferenceBackground">
                <tr>
                    <td>
                        <table cellpadding="5" cellspacing="0" width="100%">
                            <tr>
                                <td class="portletPreferenceHeader"><b><fmt:message key='portlet.message.preferenceName'/></b></td>
                                <td class="portletPreferenceHeader"><b><fmt:message key='portlet.message.defaultValue'/></b></td>
                                <td class="portletPreferenceHeader"><b><fmt:message key='portlet.message.editable'/></b></td>
                            </tr>
                            <c:choose>
                                <c:when test="${!(empty form.portlet.portletPreferences)}" >
                                    <c:forEach items="${form.portlet.portletPreferences}" var="preference">
                                        <tr>
                                            <td class="portletPreferenceRow"><a href="<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="portletId=${form.portletId}&name=${preference.key}"/>"><c:out value="${preference.key}"/></td>
                                            <td class="portletPreferenceRow"><c:out value="${preference.value.preferenceValue}"/></td>
                                            <td class="portletPreferenceRow">
                                                <c:choose>
                                                    <c:when test="${preference.value.preferenceEditable}"><fmt:message key='portlet.message.yes'/></c:when>
                                                    <c:otherwise><fmt:message key='portlet.message.no'/></c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td class="portletPreferenceRow" colspan="3"><fmt:message key='portlet.message.noPreferencesFound'/></td></tr>
                                </c:otherwise>
                            </c:choose>

                            <tr><td class="portletPreferenceRow" colspan="3" align="right"><x:display name="${form.addPreferences.absoluteName}"/></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td width="20%" nowrap  class="portletRow">&nbsp;</td>
        <td width="80%" class="portletRow">
            <x:display name="${form.submit.absoluteName}"/>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
    <c:if test="${!(empty form.message)}">
        <tr><td class="portletLabel" colspan="2"><c:out value="${form.message}"/></td></tr>
    </c:if>
    <tr><td class="portletFooter" colspan="2">&nbsp;</td></tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
