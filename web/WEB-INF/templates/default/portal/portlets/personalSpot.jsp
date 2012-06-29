<%@ include file="/common/header.jsp" %>
<c:set var="spot" value="${widget}"/>
<table cellpadding="1" cellspacing="1" width="100%" class="bookmarkBackground" align="center">
    <tr><td class="bookmarkRow">
    <table width="100%">
    <tr>
    <td>
    <c:out value="${spot.content}" escapeXml="false" />
    </td>
    </tr>
    </table>
    </td></tr>
    <tr><td class="portletFooter">&nbsp;</td></tr>
</table>