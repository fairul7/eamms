<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="3" cellspacing="1" width="100%" class="portletBackground">
    <tr><td colspan="2" class="portletTableHeader"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key='portlet.message.portletRegistration'/></td></tr>
    <tr><td colspan="2" class="portletLabel">&nbsp;</td></tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right"><fmt:message key='portlet.message.name'/>&nbsp; *</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.portletName.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right"><fmt:message key='portlet.message.title'/>&nbsp; *</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.portletTitle.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right"><fmt:message key='portlet.message.classContext'/>&nbsp; *</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.portletClass.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap valign="top" class="portletLabel" align="right"><fmt:message key='portlet.message.description'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.portletDescription.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap valign="top" class="portletLabel" align="right"><fmt:message key='portlet.message.themes'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.themes.absoluteName}"/></td>
    </tr>
    <c:if test="${!(empty widget.message)}">
        <tr><td colspan="2" class="portletLabel"><c:out value="${widget.message}"/></td></tr>
    </c:if>

    <tr>
        <td width="20%" nowrap valign="top" class="portletLabel">&nbsp;</td>
        <td width="80%" class="portletLabel">
            <x:display name="${widget.submit.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
        </td>
    </tr>

    <tr><td colspan="2" class="portletFooter">&nbsp;</td></tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
