<%@ include file="/common/header.jsp" %><c:if test="${currentUser.id == 'anonymous'}"><c:redirect url="ekplogin.jsp"/></c:if><%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%><%@ page import="kacang.services.presence.PresenceService,
				 kacang.Application,
				 com.tms.collab.calendar.model.CalendarModule,
				 kacang.model.DaoQuery,
				 kacang.model.operator.OperatorEquals,
				 kacang.model.operator.DaoOperator,
				 kacang.services.security.User,
				 kacang.services.security.SecurityService,
				 java.util.Collection,
				 java.util.Iterator,
				 com.tms.collab.messaging.model.*,
                   java.util.ArrayList,
                   kacang.model.operator.OperatorIn"%>

<%
	int unreadMsg = 0;
	int qMsg = 0;
	int onlineUsers = 0;

	SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
    User user = security.getCurrentUser(request);

    //Presence Service
    PresenceService presence = (PresenceService) Application.getInstance().getService(PresenceService.class);
	onlineUsers = presence.getOnlineUsers().size();
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
        /*query.addProperty(new OperatorEquals("userId", user.getId(), DaoOperator.OPERATOR_AND));*/
        query.addProperty(new OperatorEquals("readFlag", Boolean.FALSE, DaoOperator.OPERATOR_AND));
        /*query.addProperty(new OperatorEquals("folderId", folderQM.getFolderId(), DaoOperator.OPERATOR_NAN));
        query.addProperty(new OperatorEquals("folderId", folderSent.getFolderId(), DaoOperator.OPERATOR_NAN));
        query.addProperty(new OperatorEquals("folderId", folderTrash.getFolderId(), DaoOperator.OPERATOR_NAN));
        query.addProperty(new OperatorEquals("folderId", folderDraft.getFolderId(), DaoOperator.OPERATOR_NAN));
        query.addProperty(new OperatorEquals("folderId", folderOutbox.getFolderId(), DaoOperator.OPERATOR_NAN));*/
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
        unreadMsg = messaging.getMessagesCount(query);
        //Quick messages
        query = new DaoQuery();
        query.addProperty(new OperatorEquals("folderId", folderQM.getFolderId(), DaoOperator.OPERATOR_AND));
        query.addProperty(new OperatorEquals("readFlag", Boolean.FALSE, DaoOperator.OPERATOR_AND));
        qMsg = messaging.getMessagesCount(query);
    }
%>
<x:config>
<page name="statusHeader"><com.tms.collab.messaging.ui.StatusWidget name="status"/></page>
</x:config>
<x:display name="statusHeader.status" body="custom"/>
<a target="ekp" href="ekpcontinue.jsp?continue=/ekms/messaging/"><span class="label">Unread:</span> <%= unreadMsg %></a>
<a target="ekp" href="ekpcontinue.jsp?continue=/ekms/messaging/checkEmail.jsp"><span class="label">POP:</span>
    <c:choose><c:when test="${!(empty widgets['statusHeader.status'])}"><c:out value="${widgets['statusHeader.status'].emailCount}"/></c:when><c:otherwise>-</c:otherwise></c:choose></a>
<a target="ekp" href="ekpcontinue.jsp?continue=/ekms/messaging/messageList.jsp?folder=Quick%20Messages"><span class="label">Quick:</span> <%= qMsg %></a>
<a target="ekp" href="ekpcontinue.jsp?continue=/ekms/"><span class="label">Online:</span> <%= onlineUsers %></a>
&nbsp;<input class="button" type="button" tabindex="3" value="&nbsp;X&nbsp;" onclick="location.href='ekplogout.jsp'">
