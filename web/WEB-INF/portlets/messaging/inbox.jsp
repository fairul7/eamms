<%@include file="/common/header.jsp" %>

<x:config>
    <page name="inboxPage">
        <com.tms.collab.messaging.ui.InboxPortlet name="inbox" />
    </page>
</x:config>

<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="forumRow" align="center">

            <table cellpadding="2" cellspacing="1" width="95%">
                <tr>
                  <td class="forumRow">
                    <x:display name="inboxPage.inbox" />
                  </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="forumRow">&nbsp;</td></tr>
    <tr><td class="forumFooter"><img src="images/blank.gif" width="1" height="15">&nbsp;
    <input type="button" class="button" value="<fmt:message key='messaging.label.composeMessage'/>" onclick="document.location='<c:url value="/ekms/" />messaging/composeMessage.jsp'">
    <input type="button" class="button" value="<fmt:message key='messaging.label.downloadPOP3Email'/>" onclick="document.location='<c:url value="/ekms/" />messaging/checkEmail.jsp'">
    </td></tr>
</table>