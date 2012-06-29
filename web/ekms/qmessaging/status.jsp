<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 kacang.services.presence.PresenceService,
                 kacang.Application,
                 java.util.Collection,
                 com.tms.collab.messaging.model.MessagingModule,
                 com.tms.collab.messaging.model.Util,
                 com.tms.collab.messaging.model.MessagingException,
                 kacang.util.Log,
                 kacang.services.security.User,
                 kacang.services.security.SecurityService,
                 com.tms.collab.messaging.model.Folder,
                 kacang.model.DaoQuery,
                 kacang.model.operator.OperatorEquals,
                 kacang.model.operator.DaoOperator,
                 kacang.ui.Widget,
                 kacang.model.operator.OperatorParenthesis,
                 java.util.ArrayList,
                 java.util.Iterator,
                 kacang.model.operator.OperatorIn"%>
<%@ include file="/common/header.jsp" %>
<x:permission module="kacang.services.security.SecurityService" permission="kacang.services.security.ekms.ekmsUser">
<html>
<head>
    <meta http-equiv="Refresh" content="60">
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<%
    SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
    User user = security.getCurrentUser(request);
    //Online users
    PresenceService presence = (PresenceService) Application.getInstance().getService(PresenceService.class);
    Collection onlineUsers = presence.getOnlineUsers();
    pageContext.setAttribute("onlineUsers", String.valueOf(onlineUsers.size()));
    //Messaging module
    MessagingModule messaging = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
    if(messaging.getIntranetAccountByUserId(user.getId()) != null)
    {
        Folder folderQM = messaging.getSpecialFolder(user.getId(), Folder.FOLDER_QM);
        Folder folderSent = messaging.getSpecialFolder(user.getId(), Folder.FOLDER_SENT);
        Folder folderTrash = messaging.getSpecialFolder(user.getId(), Folder.FOLDER_TRASH);
        Folder folderDraft = messaging.getSpecialFolder(user.getId(), Folder.FOLDER_DRAFT);
        Folder folderOutbox = messaging.getSpecialFolder(user.getId(), Folder.FOLDER_OUTBOX);
        DaoQuery query;
        //Unread mail
        query = new DaoQuery();
        /*query.addProperty(new OperatorEquals("userId", user.getId(), DaoOperator.OPERATOR_AND));  */
        query.addProperty(new OperatorEquals("readFlag", Boolean.FALSE, DaoOperator.OPERATOR_AND));
        /*query.addProperty(new OperatorEquals("folderId", folderQM.getFolderId(), DaoOperator.OPERATOR_NAN));
        query.addProperty(new OperatorEquals("folderId", folderSent.getFolderId(), DaoOperator.OPERATOR_NAN));
        query.addProperty(new OperatorEquals("folderId", folderTrash.getFolderId(), DaoOperator.OPERATOR_NAN));*/
        Collection list = messaging.getFolders(user.getId());
        if(list.size() > 0)
        {
            Collection folderIds = new ArrayList();
            for (Iterator i = list.iterator(); i.hasNext();)
            {
                Folder folder = (Folder) i.next();
                if(!(folder.getId().equals(folderQM.getId()) || folder.getId().equals(folderSent.getId()) || folder.getId().equals(folderTrash.getId()) || folder.getId().equals(folderDraft.getId()) || folder.getId().equals(folderOutbox.getId())))
                    folderIds.add(folder.getFolderId());
            }
            query.addProperty(new OperatorIn("folderId", folderIds.toArray(new String[] {}), DaoOperator.OPERATOR_AND));
        }
        Integer unread = new Integer(messaging.getMessagesCount(query));
        pageContext.setAttribute("unread", String.valueOf(unread));
        //Quick messages
        query = new DaoQuery();
        query.addProperty(new OperatorEquals("folderId", folderQM.getFolderId(), DaoOperator.OPERATOR_AND));
        query.addProperty(new OperatorEquals("readFlag", Boolean.FALSE, DaoOperator.OPERATOR_AND));
        Integer quickMessages = new Integer(messaging.getMessagesCount(query));
        pageContext.setAttribute("qMessages", String.valueOf(quickMessages));
        //POP

    }
    else
    {
        pageContext.setAttribute("unread", "N/A");
        pageContext.setAttribute("POP", "N/A");
        pageContext.setAttribute("qMessages", "N/A");
    }
%>
<body>
    <table cellpadding="1" cellspacing="0" width="100%">
        <tr>
            <td nowrap width="5%" class="qMessagingRow">&nbsp;</td>
            <td nowrap width="35%" class="qMessagingTextHeader"><fmt:message key='qmessaging.label.usersOnline'/> </td>
            <td nowrap width="5%" class="qMessagingRow" align="right"><c:out value="${onlineUsers}"/></td>
            <td nowrap width="5%" class="qMessagingRow">&nbsp;</td>
            <td nowrap width="40%" class="qMessagingTextHeader"><fmt:message key='qmessaging.label.unreadMsg'/></td>
            <td nowrap width="5%" class="qMessagingRow" align="right"><c:out value="${unread}"/></td>
            <td nowrap width="5%" class="qMessagingRow">&nbsp;</td>
        </tr>
        <tr>
            <td nowrap width="5%" class="qMessagingRow">&nbsp;</td>
            <td nowrap width="35%" class="qMessagingTextHeader"><fmt:message key='qmessaging.label.pOP'/> </td>
            <td nowrap width="5%" class="qMessagingRow" align="right">
                <c:choose>
                    <c:when test="${! empty widgets['statusHeader.status']}">
                        <c:out value="${widgets['statusHeader.status'].emailCount}"/>
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                </c:choose>
            </td>
            <td nowrap width="5%" class="qMessagingRow">&nbsp;</td>
            <td nowrap width="40%" class="qMessagingTextHeader"><fmt:message key='qmessaging.label.quickMsg'/> </td>
            <td nowrap width="5%" class="qMessagingRow" align="right"><c:out value="${qMessages}"/></td>
            <td nowrap width="5%" class="qMessagingRow">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="7" width="100%" class="qMessagingRow">&nbsp;</td>
        </tr>
    </table>
</body>
</html>
</x:permission>
