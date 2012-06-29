package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.*;

public class MessageTable extends Table {
    
    private static final Log _log = Log.getLog(MessageTable.class);
    
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String EVENT_DRAG_DROP = "dragDrop";
    public static final String MESSAGE_PREFIX = "msg_";

    private int currentPercent;
    private long currentUsage;
    private long userQuota;

    SelectBox selectField;


    public void init() {
        super.init();
        setWidth("100%");
    }

    public void onRequest(Event event) {
        String folderId;
        String folderName;
        Folder folder = null;
        MessagingModule mm = Util.getMessagingModule();
        User user = Util.getUser(event);
        getWidgetManager().removeAttribute("InboxPortlet");
        if(getModel()==null) {
            if(Util.hasIntranetAccount(event)) {
                setModel(new MessageTableModel(event));
            } else {
                return;
            }
        }

        // update folder filter
        MessageTableModel model = (MessageTableModel) getModel();
        try {
            TableFilter filter = model.getFilter("folder");
            SelectBox sb = (SelectBox) filter.getWidget();
            sb.removeAllOptions();

            Collection folders = mm.getFolders(Util.getUser(event).getId());

            Iterator iterator = folders.iterator();
            while (iterator.hasNext()) {
                folder = (Folder) iterator.next();
                sb.addOption(folder.getId(), folder.getName());
            }
        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }


        // set folder
        folderName = event.getRequest().getParameter("folder");
        if (folderName != null) {
            try {
                folder = mm.getSpecialFolder(user.getId(), folderName);
                setModel(new MessageTableModel(event));
                SelectBox sb = (SelectBox) getModel().getFilter("folder").getWidget();
                sb.setSelectedOptions(new String[]{folder.getId()});

                // reset paging
                setCurrentPage(1);
                TableModel mod = getModel();
                if (mod != null) {
                    mod.setStart(0);
                }
                
            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }

        // set folder via folderId
        folderId = event.getRequest().getParameter("folderId");
        if (folderId != null) {
            try {
                folder = mm.getFolder(folderId);
                setModel(new MessageTableModel(event));

	            // check user is folder owner
	            if (user.getId().equals(folder.getUserId())) {
		            SelectBox sb = (SelectBox) getModel().getFilter("folder").getWidget();
		            sb.setSelectedOptions(new String[]{folder.getId()});
	            }

                // reset paging
                setCurrentPage(1);
                TableModel mod = getModel();
                if (mod != null) {
                    mod.setStart(0);
                }

            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }


        try {
            currentUsage = mm.getCurrentDiskUsage(user.getId());
            currentPercent = mm.getUserQuotaPercent(user.getId());
            userQuota = mm.getUserQuota(user.getId());
        } catch (MessagingException e) {
            currentUsage = 0;
            currentPercent = 0;
            userQuota = 0;
            Log.getLog(getClass()).error("Error getting user quota information", e);
        }


        super.onRequest(event);
    }

    public Forward actionPerformed(Event evt) {
        // handle drag and drop
        if (EVENT_DRAG_DROP.equals(evt.getType())) {
            return handleDragDrop(evt);
        }
        else {
            return super.actionPerformed(evt);
        }
    }

    protected Forward handleDragDrop(Event evt) {
        String keyStr = evt.getRequest().getParameter("id");
        String folderStr = evt.getRequest().getParameter("targetFolder");
        if (keyStr == null || folderStr == null) {
            return null;
        }
        try {
            MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            String userId = getWidgetManager().getUser().getId();

            // determine folder
            Folder folder;
            String folderId = folderStr.substring("folder_".length());
            if (Folder.FOLDER_INBOX.equals(folderId) || Folder.FOLDER_SENT.equals(folderId) || Folder.FOLDER_DRAFT.equals(folderId)
                    || Folder.FOLDER_QM.equals(folderId) || Folder.FOLDER_TRASH.equals(folderId)) {
                folder = mm.getFolder(userId, folderId);
            }
            else {
                folder = mm.getFolder(folderId);
                if (!userId.equals(folder.getUserId())) {
                    throw new MessagingException("Folder does not belong to user " + userId);
                }
            }

            // move messages
            StringTokenizer st = new StringTokenizer(keyStr, ",");
            String id;
            Message message;
            if(st.hasMoreTokens()) {
                while(st.hasMoreTokens()) {
                    id = st.nextToken().trim();
                    if (id.startsWith(MESSAGE_PREFIX)) {
                        id = id.substring(MESSAGE_PREFIX.length());
                    }
                    message = mm.getMessageByMessageId(id, false);
                    mm.moveMessage(message, folder.getId());
                }

                mm.updateUserDiskQuota(userId);
            }
            return new Forward(FORWARD_SUCCESS);
        }
        catch (MessagingException e) {
            Log.getLog(getClass()).error("Error in moving message(s) using drag and drop: " + e.getMessage(), e);
            return new Forward(FORWARD_ERROR);
        }
    }


    class MessageTableModel extends TableModel {
        
        // index (request parameter) indicating the current row (@see messageListTable.jsp 
        // template to look for where this is being added)
        private static final String INDEX = "index";
        
        private TextField tfSearch;
        
        // parameters extracted upon MessageTableModel.getTableRows();
        private DaoQuery _query;
        private String _sort;
        private boolean _descending;


        public MessageTableModel(Event event) {
            User user;

            user = getWidgetManager().getUser();
            String folderName = event.getRequest().getParameter("folder");
            String deleteMsg = "";

            deleteMsg = ("Trash".equals(folderName))? "messaging.label.permanantlydelete": "messaging.label.moveselectedmailstoTrashfolder";                

            TableAction deleteAction = new TableAction("delete", Application.getInstance().getMessage("messaging.label.delete","Delete"), Application.getInstance().getMessage(deleteMsg,"Move selected mail(s) to Trash folder?"));
            addAction(deleteAction);
            addAction(new TableAction("move", Application.getInstance().getMessage("messaging.label.moveMessages","Move Message(s)"), Application.getInstance().getMessage("messaging.label.moveselectedmessagestoselectedfolder","Move selected message(s) to selected folder?")));
            addAction(new TableAction("markAsRead", Application.getInstance().getMessage("messaging.label.markAsRead","Mark As Read")));
            addAction(new TableAction("markAsUnread", Application.getInstance().getMessage("messaging.label.markAsUnread","Mark As Unread")));

            // add a ClearErrorFlag button (Only available in Outbox to resend error message) 
            if (Folder.FOLDER_OUTBOX.equals(folderName)) {
            	addAction(new TableAction("clearErrorFlag", Application.getInstance().getMessage("messaging.label.clearErrorFlag")));
            }
            
            
            
            if (Folder.FOLDER_SENT.equals(folderName) || Folder.FOLDER_DRAFT.equals(folderName)) {
                TableColumn tcTo = new TableColumn("truncatedRecipients", Application.getInstance().getMessage("messaging.label.to","To"));
                addColumn(tcTo);
            }
            else {
                TableColumn tcFrom = new TableColumn("fromField", Application.getInstance().getMessage("messaging.label.from","From"));
                addColumn(tcFrom);
            }

            TableColumn tcIndicator = new TableColumn("indicator", "&bull;");
            HashMap mapIndicator = new HashMap();
            mapIndicator.put(new Integer(Account.INDICATOR_BLUE), Application.getInstance().getMessage("messaging.label.blue","Blue"));
            mapIndicator.put(new Integer(Account.INDICATOR_CYAN), Application.getInstance().getMessage("messaging.label.cyan","Cyan"));
            mapIndicator.put(new Integer(Account.INDICATOR_GRAY), Application.getInstance().getMessage("messaging.label.gray","Gray"));
            mapIndicator.put(new Integer(Account.INDICATOR_GREEN), Application.getInstance().getMessage("messaging.label.green","Green"));
            mapIndicator.put(new Integer(Account.INDICATOR_PINK), Application.getInstance().getMessage("messaging.label.pink","Pink"));
            mapIndicator.put(new Integer(Account.INDICATOR_PURPLE), Application.getInstance().getMessage("messaging.label.purple","Purple"));
            mapIndicator.put(new Integer(Account.INDICATOR_RED), Application.getInstance().getMessage("messaging.label.red","Red"));
            mapIndicator.put(new Integer(Account.INDICATOR_YELLOW), Application.getInstance().getMessage("messaging.label.yellow","Yellow"));
            //TableStringFormat tsfIndicator = new TableStringFormat(mapIndicator);
            tcIndicator.setFormat(new TableIndicatorColorFormat());
            addColumn(tcIndicator);

            TableColumn tcSubject = new TableColumn("truncatedSubject", Application.getInstance().getMessage("messaging.label.subject","Subject"));
            TableMsgSubjectFormat tfSubject = new TableMsgSubjectFormat("(no subject)");
            tcSubject.setFormat(tfSubject);
            tcSubject.setEscapeXml(true);
            tcSubject.setUrlParam("messageId");
            addColumn(tcSubject);

            TableColumn tcUnread = new TableColumn("readFlag", Application.getInstance().getMessage("messaging.label.unread","Unread"));
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("", "<img src=\"" + contextPath + "/common/table/booleantrue.gif\">");
            tcUnread.setFormat(booleanFormat);
            addColumn(tcUnread);

            TableColumn tcAttachmentCount = new TableColumn("attachmentCount", "@");
            tcAttachmentCount.setFormat(new TableFormat() {
                public String format(Object value) {
                    if (value != null && !"0".equals(value.toString())) {
                        return value.toString();
                    }
                    else {
                        return "";
                    }
                }
            });
            addColumn(tcAttachmentCount);

            String dateHeader;
            if (Folder.FOLDER_SENT.equals(folderName)) {
                dateHeader = Application.getInstance().getMessage("messaging.label.sent","Sent");
            }
            else if (Folder.FOLDER_DRAFT.equals(folderName)) {
                dateHeader = Application.getInstance().getMessage("messaging.label.composed","Composed");
            }
            else if (Folder.FOLDER_OUTBOX.equals(folderName)) {
            	dateHeader = Application.getInstance().getMessage("messaging.label.sent", "Sent");
            }
            else {
                dateHeader = Application.getInstance().getMessage("messaging.label.received","Received");
            }

            TableColumn tcMessageDate = new TableColumn("messageDate", dateHeader);
            tcMessageDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(tcMessageDate);
            
            
            
            // error flag column (only present in Outbox)
            if (Folder.FOLDER_OUTBOX.equals(folderName)) {
                String errorFlagHeader = Application.getInstance().getMessage("messaging.label.outbox.errorFlag");
            	TableColumn tcOutboxErrorFlag = new TableColumn("errorFlag", errorFlagHeader);
                tcOutboxErrorFlag.setFormat(new TableFormat() {
					public String format(Object value) {
                        if(((String)value).equals(Message.ERROR_FLAG_FAIL)) {
                            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
                        	return "<img src="+ contextPath + "/common/table/booleantrue.gif>";
                        }
                        return "";
					}
                });
                addColumn(tcOutboxErrorFlag);
            }
            

            // add search filter/field
            tfSearch = new TextField("tfSearch");
            tfSearch.setSize("20");
            TableFilter searchFilter = new TableFilter("searchFilter");
            searchFilter.setWidget(tfSearch);
            addFilter(searchFilter);

            // add folder filter
            try {
                setFolderFilter(user);
            } catch (MessagingException e) {
                Log.getLog(getClass()).error("Error adding folder filter", e);
            }

            // add fields filter(To, Cc, Bcc, From, Subject, Body)
            TableFilter tfFields = new TableFilter("fields");
            selectField = new SelectBox("selectField");
            selectField.addOption("-1",Application.getInstance().getMessage("messaging.filter.allFields"));

            String fields = Application.getInstance().getMessage("messaging.filter.fields");
            StringTokenizer tokens = new StringTokenizer(fields,",");
            int c=0;
            while(tokens.hasMoreTokens()){
                String str = tokens.nextToken();
                selectField.addOption(String.valueOf(c),str);
                c++;
            }
            selectField.setSelectedOption("-1");
            selectField.setMultiple(false);
            tfFields.setWidget(selectField);
            addFilter(tfFields);

            // add read filter

            TableFilter tfRead = new TableFilter("read");
            SelectBox selectRead = new SelectBox("selectRead");
            selectRead.addOption(null, Application.getInstance().getMessage("messaging.label.AllMessages","- All Messages -"));
            selectRead.addOption("0", Application.getInstance().getMessage("messaging.label.unread","Unread"));
            selectRead.addOption("1", Application.getInstance().getMessage("messaging.label.read","Read"));
            selectRead.setMultiple(false);
            tfRead.setWidget(selectRead);
            addFilter(tfRead);

        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Log log = Log.getLog(this.getClass());
            log.debug("~~~ action = " + action);

            try {
                if (action.equalsIgnoreCase("delete")) {
                    MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                    String userId = evt.getWidgetManager().getUser().getId();
                    Folder trash = mm.getSpecialFolder(userId, Folder.FOLDER_TRASH);

                    long start = System.currentTimeMillis();

                    if (selectedKeys.length > 0) {
                        if (mm.getMessageByMessageId(selectedKeys[0], false).getFolderId().equals(trash.getFolderId())) {
                            // delete from trash if message is already in trash folder
                            for (int i = 0; i < selectedKeys.length; i++) {
                                mm.deleteMessage(selectedKeys[i]);
                            }
                        } else {
                            // move message to trash folder
                            for (int i = 0; i < selectedKeys.length; i++) {
                                mm.moveMessage(mm.getMessageByMessageId(selectedKeys[i], false), trash.getFolderId());
                            }
                        }

                        mm.updateUserDiskQuota(userId);
                    }

                    long end = System.currentTimeMillis();
                    long taken = end-start;
                    if(taken>15000) {
                        log.warn("Delete message(s) took " + taken + "ms");
                    }


                } else if (action.equalsIgnoreCase("move")) {
                    MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

                    List folders = (List) getFilterValue("folder");
                    String folderId = "";
                    if (folders.size() > 0) {
                        folderId = (String) folders.get(0);
                    }

                    Message message;
                    if(selectedKeys.length>0) {
                        for (int i = 0; i < selectedKeys.length; i++) {
                            message = mm.getMessageByMessageId(selectedKeys[i], false);
                            mm.moveMessage(message, folderId);
                        }

                        String userId = evt.getWidgetManager().getUser().getId();
                        mm.updateUserDiskQuota(userId);
                    }
                }
                //Mark as read/unread
                else if(action.equalsIgnoreCase("markAsRead"))
                {
                    MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                    Message message;
                    for (int i = 0; i < selectedKeys.length; i++)
                    {
                        message = mm.getMessageByMessageId(selectedKeys[i], false);
                        message.setReadFlag(true);
                        mm.updateMessage(message);
                    }
                }
                else if(action.equalsIgnoreCase("markAsUnread"))
                {
                    MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                    Message message;
                    for (int i = 0; i < selectedKeys.length; i++)
                    {
                        message = mm.getMessageByMessageId(selectedKeys[i], false);
                        message.setReadFlag(false);
                        mm.updateMessage(message);
                    }
                }
                else if (action.equalsIgnoreCase("clearErrorFlag")) {
                	MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                    for (int i = 0; i < selectedKeys.length; i++) {
                        Message message = mm.getMessageByMessageId(selectedKeys[i], false);
                        message.setErrorFlag(Message.ERROR_FLAG_NORMAL);
                        mm.updateMessage(message);
                    }
                }

            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
                evt.getRequest().getSession().setAttribute("error", e);
                return new Forward(FORWARD_ERROR);
            }

            return new Forward(FORWARD_SUCCESS);
        }

        public String getTableRowKey() {
            return "id";
        }
        
        public Collection getTableRows() {
            try {
                MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

                SelectBox sb = (SelectBox) getFilter("folder").getWidget();
                List folders = (List) sb.getValue();
                String folderId = "";
                if (folders.size() > 0) {
                    folderId = (String) folders.get(0);
                } else {
                    Folder f = mm.getSpecialFolder(getWidgetManager().getUser().getId(), Folder.FOLDER_INBOX);
                    folderId = f.getId();
                }

                List readStatus = (List) getFilterValue("read");
                String read = "";
                if (readStatus.size() > 0)
                    read = (String) readStatus.get(0);
                if (read != null && (read.trim().equalsIgnoreCase("null") || read.trim().equals("")))
                    read = "";

                // getting field filter value

                String fieldFilter = getSelectedValue(selectField);
                String fieldFilterText = getSelectedText(selectField);

                DaoQuery query = new SerializationFriendlyDaoQuery();
                query.addProperty(new OperatorEquals("folderId", folderId, DaoOperator.OPERATOR_AND));
                if(read!=null && read.trim().length()>0) {
                    query.addProperty(new OperatorEquals("readFlag", read, DaoOperator.OPERATOR_AND));
                }
                if(tfSearch.getValue()!=null) {
                    // include 
                    OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                    parenthesis.addOperator(new OperatorLike("subject", tfSearch.getValue().toString(), null));
                    parenthesis.addOperator(new OperatorLike("fromField", tfSearch.getValue().toString(), DaoOperator.OPERATOR_OR));
                    parenthesis.addOperator(new OperatorLike("toField", tfSearch.getValue().toString(), DaoOperator.OPERATOR_OR));
                    parenthesis.addOperator(new OperatorLike("body", tfSearch.getValue().toString(), DaoOperator.OPERATOR_OR));
                    query.addProperty(parenthesis);
                }

                if(fieldFilter != null
                        && !"".equals(fieldFilter)
                        && !"-1".equals(fieldFilter)){
                    String str = tfSearch.getValue().toString();

                    String subject = Application.getInstance().getMessage("messaging.label.subject");
                    String to = Application.getInstance().getMessage("messaging.label.to");
                    String cc = Application.getInstance().getMessage("messaging.label.cc");
                    String body = Application.getInstance().getMessage("messaging.label.body");
                    String from = Application.getInstance().getMessage("messaging.label.from");

                    if(fieldFilterText.equalsIgnoreCase(subject)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("subject", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(to)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("toField", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(cc)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("ccField", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(body)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("body", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(from)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("fromField", str, null));
                        query.addProperty(parenthesis);
                    }
                }

                String s = getSort();
                boolean d = isDesc();
                if(s==null) {
                    s = "messageDate";
                    d = true;
                }
                else if ("truncatedSubject".equals(s)) {
                    s = "subject";
                }
                else if ("truncatedRecipients".equals(s)) {
                    s = "toField";
                }

                _query = query;
                _sort = s;
                _descending = d;
                
                return mm.getMessages(query, getStart(), getRows(), s, d);

            } catch (MessagingException e) {
                // log error and return an empty collection
                Log.getLog(getClass()).error("Error in getTableRows()", e);
                return new ArrayList();
            }
        }
        
        
        Message getTableRowBasedOnPredefinedParameters(int index) {
            try {
                MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                Collection c =  mm.getMessages(_query, (index - 1), 1, _sort, _descending);
                Iterator i = c.iterator();
                if (i.hasNext()) {
                    return (Message) i.next();
                }
            }
            catch(MessagingException e) {
                _log.error(e.toString(), e);
            }
            return null;
        }

        public int getTotalRowCount() {
           try {
                MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

                SelectBox sb = (SelectBox) getFilter("folder").getWidget();
                List folders = (List) sb.getValue();
                String folderId = "";
                if (folders.size() > 0) {
                    folderId = (String) folders.get(0);
                } else {
                    Folder f = mm.getSpecialFolder(getWidgetManager().getUser().getId(), Folder.FOLDER_INBOX);
                    folderId = f.getId();
                }

                List readStatus = (List) getFilterValue("read");
                String read = "";
                if (readStatus.size() > 0)
                    read = (String) readStatus.get(0);
                if (read != null && (read.trim().equalsIgnoreCase("null") || read.trim().equals("")))
                    read = "";

                // getting field filter value

                String fieldFilter = getSelectedValue(selectField);
                String fieldFilterText = getSelectedText(selectField);

                DaoQuery query = new SerializationFriendlyDaoQuery();
                query.addProperty(new OperatorEquals("folderId", folderId, DaoOperator.OPERATOR_AND));
                if(read!=null && read.trim().length()>0) {
                    query.addProperty(new OperatorEquals("readFlag", read, DaoOperator.OPERATOR_AND));
                }
                if(tfSearch.getValue()!=null) {
                    // include
                    OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                    parenthesis.addOperator(new OperatorLike("subject", tfSearch.getValue().toString(), null));
                    parenthesis.addOperator(new OperatorLike("fromField", tfSearch.getValue().toString(), DaoOperator.OPERATOR_OR));
                    parenthesis.addOperator(new OperatorLike("toField", tfSearch.getValue().toString(), DaoOperator.OPERATOR_OR));
                    parenthesis.addOperator(new OperatorLike("body", tfSearch.getValue().toString(), DaoOperator.OPERATOR_OR));
                    query.addProperty(parenthesis);
                }

                if(fieldFilter != null
                        && !"".equals(fieldFilter)
                        && !"-1".equals(fieldFilter)){
                    String str = tfSearch.getValue().toString();

                    String subject = Application.getInstance().getMessage("messaging.label.subject");
                    String to = Application.getInstance().getMessage("messaging.label.to");
                    String cc = Application.getInstance().getMessage("messaging.label.cc");
                    String body = Application.getInstance().getMessage("messaging.label.body");
                    String from = Application.getInstance().getMessage("messaging.label.from");

                    if(fieldFilterText.equalsIgnoreCase(subject)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("subject", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(to)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("toField", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(cc)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("ccField", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(body)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("body", str, null));
                        query.addProperty(parenthesis);
                    }else if(fieldFilterText.equalsIgnoreCase(from)){
                        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                        parenthesis.addOperator(new OperatorLike("fromField", str, null));
                        query.addProperty(parenthesis);
                    }
                }

                String s = getSort();
                boolean d = isDesc();
                if(s==null) {
                    s = "messageDate";
                    d = true;
                }
                else if ("truncatedSubject".equals(s)) {
                    s = "subject";
                }
                else if ("truncatedRecipients".equals(s)) {
                    s = "toField";
                }

                _query = query;
                _sort = s;
                _descending = d;

              return mm.getMessagesCount(query);

            } catch (MessagingException e) {
                // log error and return an empty collection
                Log.getLog(getClass()).error("Error in getTableRows()", e);
                return 0;
            }
        }


      //

        // === [ private methods ] =============================================
        private void setFolderFilter(User user) throws MessagingException {
            TableFilter tfFolder;
/*
            Collection folders;
            Iterator iterator;
            MessagingModule mm;

            mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            folders = mm.getFolders(user.getId());
            SelectBox selectFolder = new SelectBox("selectFolder");
            selectFolder.setMultiple(false);

            iterator = folders.iterator();
            while (iterator.hasNext()) {
                Folder folder = (Folder) iterator.next();
                selectFolder.addOption(folder.getId(), folder.getName());

                // by default, select inbox
                if (Folder.FOLDER_INBOX.equals(folder.getName())) {
                    selectFolder.setSelectedOptions(new String[]{folder.getId()});
                }
            }
*/
            SelectBox selectFolder = new FolderSelectBox("selectFolder");
            tfFolder = new TableFilter("folder");
            tfFolder.setWidget(selectFolder);
            addFilter(tfFolder);
        }

    }


    class TableMsgSubjectFormat implements TableFormat {
        String emptyValue;

        public TableMsgSubjectFormat(String emptyValue) {
            this.emptyValue = emptyValue;
        }

        public String format(Object value) {
            return format((String) value);
        }

        public String format(String value) {
            if (value == null || value.trim().equals(""))
                value = emptyValue;
            return value;
        }
    }

    class TableIndicatorColorFormat implements TableFormat {
        public String format(Object o) {
            String v = o.toString();
            String color;

            color = (String) Account.indicatorColorMap.get(v);
            if (color == null) {
                color = "black";  // default color
            }

            return "<div align=center><font color='" + color + "'>&bull;</font></div>";
        }
    }

    class TableReadFlagFormat implements TableFormat {
        public String format(Object o) {
            String v = o.toString();
            if (v.equalsIgnoreCase("false")) {
                return "<div align=center><font color='red'>"+Application.getInstance().getMessage("messaging.label.unread","Unread")+"</font></div>";
            } else {
                return "<div align=center>&nbsp;</div>";
            }

        }
    }


    public int getCurrentPercent() {
        return currentPercent;
    }

    public void setCurrentPercent(int currentPercent) {
        this.currentPercent = currentPercent;
    }

    public long getCurrentUsage() {
        return currentUsage;
    }

    public void setCurrentUsage(long currentUsage) {
        this.currentUsage = currentUsage;
    }

    public long getUserQuota() {
        return userQuota;
    }

    public void setUserQuota(long userQuota) {
        this.userQuota = userQuota;
    }
    public static String getSelectedValue(SelectBox sb){
        Collection cSelected = (Collection) sb.getValue();
        return cSelected.iterator().next().toString();
    }

    public static String getSelectedText(SelectBox sb) {
        return (String) sb.getOptionMap().get(getSelectedValue(sb));
    }
}
