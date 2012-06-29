<%@ page import="com.tms.collab.qmessaging.ui.QuickMessenger"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="messenger" value="${widget}"/>
<c-rt:set var="event_message" value="<%= QuickMessenger.EVENT_MESSAGE_SELECT %>"/>
<c-rt:set var="key_message" value="<%= QuickMessenger.ATTRIBUTE_MESSAGE_KEY %>"/>
<script language="javascript">
    function qMessengerWindowOpen(url, name)
    {
        window.open(url, name, "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    }
</script>
    <jsp:include page="../form_header.jsp" flush="true"/>

<table cellspacing="0" cellpadding="1" width="100%" align="center">
    <tr>
        <td class="qMessagingOutline" bgcolor="CCCCCC">
            <table cellspacing="1" cellpadding="3" width="100%" class="qMessagingBackground" bgcolor="FFFFFF">
                <tr><td class="qMessagingTextHeader"><fmt:message key='qmessaging.message.systemUser'/>: </td></tr>
                <tr><td class="qMessagingRow"><x:display name="${messenger.onlineUsers.absoluteName}"/></td></tr>
                <tr><td class="qMessagingRow"><x:display name="${messenger.offlineUsers.absoluteName}"/></td></tr>
            </table>
        </td>
    </tr>
    <tr><td class="qMessagingRow">&nbsp;</td></tr>
    <tr><td class="qMessagingTextHeader"><fmt:message key='qmessaging.message.incomingQuickMessages'/></td></tr>
    <c:choose>
        <c:when test="${empty messenger.messages}">
            <tr><td class="qMessagingRow"><li><fmt:message key='qmessaging.message.noIncomingMessages'/></li></td></tr>
        </c:when>
        <c:otherwise>
            <c:forEach var="message" items="${messenger.messages}">
                <tr>
                    <td class="qMessagingRow">
                        <li>
                            <c:choose>
                                <c:when test="${empty messenger.replyUrl}">
                                    <a href="<%= request.getContextPath() %><%= request.getServletPath() %><c:out value="?cn=${messenger.absoluteName}&et=${event_message}&${key_message}=${message.messageId}"/>">
                                </c:when>
                                <c:otherwise>
                                    <a href="" onClick="qMessengerWindowOpen('<c:out value="${messenger.replyUrl}?${key_message}=${message.messageId}"/>', 'replyWindow'); return false;">
                                </c:otherwise>
                            </c:choose>
                            [<fmt:formatDate value="${message.date}" pattern="d/M,h:m a"/>]
                            <c:set var="from" value="${message.from}"/>
                            <%
                                String from = (String) pageContext.getAttribute("from");
                                if(from.indexOf('@') != -1)
                                    from = from.substring(0, from.indexOf('@'));
                                pageContext.setAttribute("from", from);
                            %>
                            <c:out value="${from}" escapeXml="false" />
                            </a>
                        </li>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <tr><td class="qMessagingRow">&nbsp;</td></tr>
    <tr><td class="portletFooter">&nbsp;</td></tr>
</table>
    <jsp:include page="../form_footer.jsp" flush="true"/>
