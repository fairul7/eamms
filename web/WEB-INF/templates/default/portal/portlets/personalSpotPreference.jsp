<%@ page import="com.tms.portlet.portlets.personal.PersonalSpotPreference"%>
<%@include file="/common/header.jsp" %>

<table cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr><td class="bookmarkHeader" colspan="2"><fmt:message key='portlet.message.personalSpot'/></td></tr>
    <tr><td class="bookmarkRow" colspan="2">&nbsp;</td></tr>
    <tr>
        <td class="bookmarkRow" colspan="2"><x:display name="${widget.childMap.textBox.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="bookmarkRow" align="right">&nbsp;</td>
        <td class="bookmarkRow">
            <x:display name="${widget.childMap.submit.absoluteName}"/>
<%--            <x:display name="${widget.childMap.cancel.absoluteName}"/>--%>
<input
    class="button"
    type="button"
    onclick="window.close()"
    value="Cancel">
        </td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
