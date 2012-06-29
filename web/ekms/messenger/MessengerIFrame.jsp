<%@ include file="/common/header.jsp" %>
<%@ include file="/common/nocache.jsp" %>
<x:config>
    <page name="messengerPg">
        <com.tms.collab.messenger.ui.MessagingWidget name="messengerLeftPanel"/>
    </page>
</x:config>
<x:display name="messengerPg.messengerLeftPanel"/>
<%-- 
<%
String pathProperty = Application.getInstance().getProperty("com.tms.collab.messenger.PostLocation");
%>
--%>

	
