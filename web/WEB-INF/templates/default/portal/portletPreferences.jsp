<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="1" width="100%" class="portletBackground">
    <tr><td colspan="2" class="portletTableHeader"><fmt:message key='portlet.message.portletPreferences'/></td></tr>
    <tr><td colspan="2" class="portletLabel">&nbsp;</td></tr>
    <tr>
        <td class="portletLabel" align="right"><fmt:message key='portlet.message.preferenceName'/>: *</td>
        <td class="portletLabel"><x:display name="${form.preferenceName.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="portletLabel" align="right"><fmt:message key='portlet.message.defaultValue'/>:</td>
        <td class="portletLabel"><x:display name="${form.preferenceValue.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="portletLabel" align="right">&nbsp;</td>
        <td class="portletLabel"><x:display name="${form.preferenceEditable.absoluteName}"/></td>
    </tr>
    <tr><td colspan="2" class="portletRow">&nbsp;</td></tr>
    <tr>
    	<td class="portletLabel">&nbsp;</td>
        <td class="portletFooter">
            <x:display name="${form.update.absoluteName}"/>
            <c:if test="${!(empty form.delete)}">
                <x:display name="${form.delete.absoluteName}"/>
            </c:if>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
