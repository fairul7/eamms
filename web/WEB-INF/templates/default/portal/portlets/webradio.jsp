<%@ page import="com.tms.portlet.portlets.webradio.WebRadio"%>
<%@ include file="/common/header.jsp" %>
<script language="javascript" src="<c:url value="/common/tree/tree.js"/>"></script>
<c:set var="radio" value="${widget}"/>
<c-rt:set var="event" value="<%= WebRadio.EVENT_STATION_SELECT %>"/>
<c-rt:set var="stationIp" value="<%= WebRadio.PROPERTY_STATION %>"/>
<table cellpadding="3" cellspacing="1" width="100%" class="radioBackground" align="center">
    <tr><td class="radioRow"><a href="" onClick="treeToggle('webRadio'); return false;"><fmt:message key='portlet.message.showHideGraphicsVisualizer'/></a></td></tr>
    <tr>
        <td align="center">
            <div id="webRadio">
                <%-- Embedding Windows Media Player --%>
                <object
                    id="player"
                    width="150"
                    height="100"
                    classID="CLSID:	6BF52A52-394A-11d3-B153-00C04F79FAA6"
                >
                    <c:if test="${! empty radio.selectedStation}">
                        <param name="URL" value="<c:out value="${radio.selectedStation}"/>">
                    </c:if>
                    <param name="autostart" value="true"/>
                    <param name="uiMode" value="none"/>
                </object>
                <%-- END: Embedding Windows Media Player --%>
                <script>treeLoad('webRadio');</script>
            </div>
        </td>
    </tr>
    <tr><td class="radioRow"><b><fmt:message key='portlet.message.availableStations'/>: </b></td></tr>
    <c:choose>
        <c:when test="${! empty radio.stations}">
            <c:forEach var="station" items="${radio.stations}">
                <tr>
                    <td class="radioRow">
                        <x:event name="${radio.absoluteName}" type="${event}" param="${stationIp}=${station.value}">
                            <li><c:if test="${station.value == radio.selectedStation}"><b></c:if><c:out value="${station.key}"/><c:if test="${station.value == radio.selectedStation}"> [<fmt:message key='portlet.message.currentlyPlaying'/>]</b></c:if></li>
                        </x:event>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td class="radioRow">
                    <x:event name="${radio.absoluteName}" type="${event}" param="${stationIp}=">
                        <fmt:message key='portlet.message.stopPlaying'/>
                    </x:event>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr><td class="radioRow"><li><fmt:message key='portlet.message.noStationsConfigured'/></li></td></tr>
        </c:otherwise>
    </c:choose>
    <tr><td class="radioRow">&nbsp;</td></tr>
    <tr><td class="portletFooter">&nbsp;</td></tr>
</table>
