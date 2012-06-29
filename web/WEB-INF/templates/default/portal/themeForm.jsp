<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="3" cellspacing="1" width="100%" class="portletBackground">
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right"><fmt:message key='portlet.message.name'/>&nbsp; *</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.themeName.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right"><fmt:message key='portlet.message.managerClass'/>&nbsp; *</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.themeManagerClass.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right" valign="top"><fmt:message key='portlet.message.description'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.themeDescription.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right" valign="top"><fmt:message key='portlet.message.defaultPortlets'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.defaultPortlets.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="portletLabel" align="right" valign="top"><fmt:message key='portlet.message.accessibleGroups'/>&nbsp;</td>
        <td width="80%" class="portletLabel"><x:display name="${widget.groups.absoluteName}"/></td>
    </tr>
    <c:if test="${!(empty widget.message)}">
        <tr ><td colspan="2" class="portletLabel"><c:out value="${widget.message}"/></td></tr>
    </c:if>
    <tr>
        <td width="20%" nowrap class="portletRow">&nbsp;</td>
        <td width="80%" class="portletRow">
            <x:display name="${widget.submit.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
        </td>
    </tr>
    <tr><td colspan="2" class="portletFooter">&nbsp;</td></tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
