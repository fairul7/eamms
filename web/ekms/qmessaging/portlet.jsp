<%@ page import="com.tms.collab.qmessaging.ui.QuickMessenger,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 kacang.ui.WidgetManager,
                 java.util.Collection"%>
<%@ include file="/common/header.jsp" %>

<x:permission module="kacang.services.security.SecurityService" permission="kacang.services.security.ekms.ekmsUser">

<x:config>
    <page name="qMessengerPortlet">
        <com.tms.collab.qmessaging.ui.QuickMessenger name="portlet"/>
    </page>
</x:config>
<%-- Constants Initialization --%>
<c-rt:set var="forward_message" value="<%= QuickMessenger.FORWARD_MESSAGE_SELECTED %>"/>
<c-rt:set var="forward_user" value="<%= QuickMessenger.FORWARD_USER_SELECTED %>"/>
<c-rt:set var="key_message" value="<%= QuickMessenger.ATTRIBUTE_MESSAGE_KEY %>"/>
<c-rt:set var="key_user" value="<%= QuickMessenger.ATTRIBUTE_USER_KEY %>"/>
<c:url var="baseContext" value="/ekms/qmessaging/"/>
<x:set name="qMessengerPortlet.portlet" property="sendUrl" value="${baseContext}send.jsp"/>
<x:set name="qMessengerPortlet.portlet" property="replyUrl" value="${baseContext}reply.jsp"/>
<script language="javascript">
    function qMessengerWindowOpen(url, name)
    {
        window.open(url, name, "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    }
</script>
<%-- Event Handling --%>
<c:if test="${forward.name == forward_message}">
    <c:if test="${! empty widgets['qMessengerPortlet.portlet'].selectedMessage}">
        <script>qMessengerWindowOpen("<c:url value="/ekms/qmessaging/reply.jsp"/>?<c:out value="${key_message}"/>=<c:out value="${widgets['qMessengerPortlet.portlet'].selectedMessage}"/>", "messageWindow");</script>
    </c:if>
</c:if>
<c:if test="${forward.name == forward_user}">
    <c:if test="${! empty widgets['qMessengerPortlet.portlet'].selectedUser}">
        <script>qMessengerWindowOpen("<c:url value="/ekms/qmessaging/send.jsp"/>?<c:out value="${key_user}"/>=<c:out value="${widgets['qMessengerPortlet.portlet'].selectedUser}"/>", "userWindow");</script>
    </c:if>
</c:if>
<html>
<head>
    <meta http-equiv="Refresh" content="30">
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
    <x:display name="qMessengerPortlet.portlet"/>
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        QuickMessenger q = (QuickMessenger)wm.getWidget("qMessengerPortlet.portlet");
        int c = 0;
        if(q!=null) {
            Collection list = q.getMessages();
            if (list != null) {
                c = q.getMessages().size();
            }
        }
        pageContext.setAttribute("count",new Integer(c));
%>
<c:if test="${count>0}" >
<script>
    var height = 218 - (5-<c:out value="${count}" />)*15;
    parent.document.getElementById("qMessengerIF").style.height = height;

</script>
</c:if>
</body>
</html>

</x:permission>

