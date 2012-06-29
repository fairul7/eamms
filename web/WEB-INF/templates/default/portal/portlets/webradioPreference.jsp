<%@ page import="com.tms.portlet.portlets.webradio.WebRadioPreference"%>
<%@include file="/common/header.jsp" %>
<c-rt:set var="event_delete" value="<%= WebRadioPreference.EVENT_TYPE_DELETE %>"/>
<c-rt:set var="delete_key" value="<%= WebRadioPreference.DELETE_KEY %>"/>
<table cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr><td class="radioHeader" colspan="2"><fmt:message key='portlet.message.configuredChannels'/></td></tr>
    <tr>
        <td class="radioRow" colspan="2">
            <c:choose>
                <c:when test="${empty widget.stations}">
                    <fmt:message key='portlet.message.noChannelsConfigured'/>
                </c:when>
                <c:otherwise>
                    <table cellpadding="1" cellspacing="1" width="100%">
                        <c:forEach var="channel" items="${widget.stations}">
                            <tr>
                                <td class="radioRow">[<x:event name="${widget.absoluteName}" type="${event_delete}" param="${delete_key}=${channel.key}"><fmt:message key='portlet.message.delete'/></x:event>]</td>
                                <td class="radioRow"><c:out value="${channel.key}"/> (<c:out value="${channel.value}"/>)</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td class="radioRow" colspan="2">&nbsp;</td></tr>
    <tr><td class="radioHeader" colspan="2"><fmt:message key='portlet.message.newStation'/></td></tr>
    <tr>
        <td class="radioRow" align="right"><fmt:message key='portlet.message.stationName'/> </td>
        <td class="radioRow"><x:display name="${widget.stationLabel.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="radioRow" align="right"><fmt:message key='portlet.message.stationIp'/> </td>
        <td class="radioRow"><x:display name="${widget.stationIp.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="radioRow" align="right">&nbsp;</td>
        <td class="radioRow">
            <x:display name="${widget.add.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
        </td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
