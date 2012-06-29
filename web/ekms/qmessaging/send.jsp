<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.qmessaging.ui.QuickMessenger,
                 com.tms.collab.qmessaging.ui.QuickMessengerForm,
                 kacang.services.presence.PresenceService,
                 kacang.Application,
                 java.util.Collection,
                 java.util.Map,
                 kacang.services.presence.Presence,
                 java.util.Iterator,
                 java.util.Date"%>
<%@ include file="/common/header.jsp" %>

<x:permission module="kacang.services.security.SecurityService" permission="kacang.services.security.ekms.ekmsUser">

<x:config>
    <page name="qMessagingSend">
        <tabbedpanel name="tab1" width="100%">
            <panel name="panel1" text="<fmt:message key="qmessaging.message.quickMessage"/>">
                <com.tms.collab.qmessaging.ui.QuickMessengerForm name="form"/>
            </panel>
            <panel name="panel2" text="<fmt:message key="qmessaging.label.netmeeting"/>">
            </panel>
        </tabbedpanel>
    </page>
</x:config>
<%-- Constants Initialization --%>
<c-rt:set var="key_user" value="<%= QuickMessenger.ATTRIBUTE_USER_KEY %>"/>
<c-rt:set var="forward_cancel" value="<%= QuickMessengerForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= QuickMessengerForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_failed" value="<%= QuickMessengerForm.FORWARD_FAILED %>"/>
<%-- Event Handling --%>
<c:if test="${!empty param[key_user]}">
    <x:set name="qMessagingSend.tab1.panel1.form" property="userId" value="${param[key_user]}"/>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='qmessaging.label.messageSent'/>");
        window.close();
    </script>
</c:if>
<html>
<head>
    <title><fmt:message key='qmessaging.label.sendingQuickMessage'/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="qMessagingRow">

<x:display name="qMessagingSend.tab1" body="custom">

<x:display name="qMessagingSend.tab1.panel1" />

<x:display name="qMessagingSend.tab1.panel2" body="custom">

<c:set var="selectedUserId" value="${widgets['qMessagingSend.tab1.panel1.form'].userId}"/>
<%
    // get presence
    String ipAddress = null;
    String userId = (String)pageContext.getAttribute("selectedUserId");
    PresenceService presence = (PresenceService) Application.getInstance().getService(PresenceService.class);
    Map presences = presence.getPresences();
    Presence userPresence = null;
    Date latest = null;
    for (Iterator i=presences.values().iterator(); i.hasNext();) {
        Presence p = (Presence)i.next();
        if (p.getUserId().equals(userId) && (latest == null || latest.before(p.getTimestamp()))) {
            userPresence = p;
            latest = p.getTimestamp();
        }
    }
    if (userPresence != null) {
        ipAddress = userPresence.getIpAddress();
    }
    pageContext.setAttribute("ipAddress", ipAddress);
%>

<table cellspacing="1" cellpadding="3" width="100%">
<form>
    <tr><td class="qMessagingRowHeader" colspan="2"><fmt:message key="qmessaging.label.netmeeting"/></td></tr>
    <tr>
        <td class="qMessagingRowLabel" width="20%" nowrap align="right" valign="right"><fmt:message key='qmessaging.message.recipient'/> </td>
        <td class="qMessagingRow" width="80%">
            <b><c:out value="${widgets['qMessagingSend.tab1.panel1.form'].user.name}"/></b>
            <c:if test="${!empty ipAddress}">
                <br>
                <span style="font-weight:bold; font-size:10pt"><c:out value="${ipAddress}"/></span>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="qMessagingRowLabel" width="20%" nowrap align="right" valign="right">&nbsp;</td>
        <td class="qMessagingRow" width="80%">

            <c:choose>
            <c:when test="${!empty ipAddress}">
                <input type="hidden" id="CallToAddress" value="<c:out value="${ipAddress}"/>">
            </c:when>
            <c:otherwise>
            </c:otherwise>
            </c:choose>
            <script>
            <!--
                function makeCallNow() {
                  try {
                    var NetMeeting = document.getElementById('NetMeeting');
                    var NetDiv = document.getElementById('NetDiv');
                    NetMeeting.CallTo('<c:out value="${ipAddress}"/>');
                  }
                  catch(e) {
                    alert(e);
                  }
                }

                function makeCall() {
                  try {
                    var NetDiv = document.getElementById('NetDiv');
                    NetDiv.style.display="block";
                    setTimeout("makeCallNow()", 2);
                  }
                  catch(e) {
                    alert(e);
                  }
                }
            //-->
            </script>
            <input type="button" value ="<fmt:message key="qmessaging.label.netmeetingCall"/>" id="CallToBtn" onclick="makeCall()">
            <input type="button" value ="<fmt:message key="qmessaging.label.netmeetingUndock"/>"id="UndockBtn" onclick="NetMeeting.UnDock()">
            <p>
            <div id="NetDiv" style="display:block;">
            <object WIDTH="100" HEIGHT="100" ID="NetMeeting" CLASSID="CLSID:3E9BAF2D-7A79-11d2-9334-0000F875AE17">
                <PARAM NAME = "MODE" VALUE = "DataOnly">
            </object>
            </div>
            <p>
            </form>
            <fmt:message key="qmessaging.message.netmeetingNotes"/>
        </td>
    </tr>
</form>
</table>

</x:display>

</x:display>


</body>
</html>

</x:permission>
