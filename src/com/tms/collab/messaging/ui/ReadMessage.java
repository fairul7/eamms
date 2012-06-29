package com.tms.collab.messaging.ui;

import kacang.Application;
import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.SelectBox;

import com.tms.collab.messaging.model.*;
import com.tms.collab.messaging.ui.MessageTable.MessageTableModel;

import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReadMessage extends LightWeightWidget {
    
    private static final Log _log = Log.getLog(ReadMessage.class);
    
    
    private Message message;
    private Folder folder;
    private Account account;
    
    private int _index;
    private int _nextIndex;
    private int _previousIndex;
    DaoQuery _query;

    // Index (request parameter) indicating the current row (@see messageListTable.jsp 
    // template to look for where this is being added)
    private static final String INDEX = "index";
    
    private String success;
    
    
	/* Okay to store as class property since message body is not loaded */
	protected Message nextMessage = null;
	protected Message previousMessage = null;
    

    public void onRequest(Event event) {
        String messageId;
        String toggleRead;
        String icsUpdate;
        String et;
        User user = Util.getUser(event);

        messageId = event.getParameter("messageId");
        toggleRead = event.getParameter("toggleRead");
        icsUpdate = event.getParameter("icsupdate");
        et = event.getParameter("at");
        if(messageId==null) {
            Log.getLog(getClass()).error("MessageId not specified");
            return;
        }

        MessagingModule mm = Util.getMessagingModule();
        try {
            // set Message property
            message = mm.getMessageByMessageId(messageId);
            populateNextPrevious(event);

            // set Folder property
            folder = mm.getFolder(message.getFolderId());

            // make sure message belongs to user
            if(!folder.getUserId().equals(user.getId())) {
                // no access
	            message = new Message();
	            message.setBody("Unauthorized access to read message");
                throw new MessagingException("Unauthorized access to read message");
            }

            if(toggleRead!=null) {
                // toggle read flag
                message.setRead(!message.isRead());
                mm.updateMessage(message);

            } else {

                // update read flag
                if(!message.isRead()) {
                    message.setRead(true);
                    mm.updateMessage(message);
                }
            }
            if(icsUpdate!=null && icsUpdate.equals("true")){
            	User currentUser = Application.getInstance().getCurrentUser();
               	try{
            		setSuccess(mm.parseICSFile(messageId,message,currentUser.getId()));
              	}catch(Exception e){
            	}
            }
            // set Account property
            try {
                account = mm.getAccount(message.getAccountId());
            } catch(MessagingException e) {
                // account for message was deleted
                account = null;
            }

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

	protected void populateNextPrevious(Event event) {
		Widget listingWidget = (Widget) event.getRequest().getAttribute("listingWidget");
		String portlet=(String)WidgetManager.getWidgetManager(event.getRequest()).getAttribute("InboxPortlet");
        String indexStr = event.getParameter(INDEX);
        try {
            if (listingWidget != null && listingWidget instanceof MessageTable && portlet==null) {
                MessageTableModel model = (MessageTableModel) ((MessageTable) listingWidget).getModel();
                
				if(indexStr == null || "".equals(indexStr))
					_index = 0;
				else
                	_index = Integer.parseInt(indexStr);
                
                // NOTE: Bug 2721 next and previous function inversed
                //_nextIndex = _index + 1;
                //_previousIndex = _index - 1;

                _nextIndex = _index - 1;
                _previousIndex = _index + 1;

                if (_index <= 0) {
                    _index = 1;
                    _nextIndex = 1;
                }
                if (_index > model.getTotalRowCount()) {
                    _index = model.getTotalRowCount();
                    _previousIndex = model.getTotalRowCount();
                }
                nextMessage = getPrevNextMessage(_nextIndex, model);
                previousMessage = getPrevNextMessage(_previousIndex, model);
            }else{
            	SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    				if(indexStr == null || "".equals(indexStr))
    					_index = 0;
    				else
                    	_index = Integer.parseInt(indexStr);
                    
                    // NOTE: Bug 2721 next and previous function inversed
                    //_nextIndex = _index + 1;
                    //_previousIndex = _index - 1;

                    _nextIndex = _index - 1;
                    _previousIndex = _index + 1;

					try {              
                    
                    MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                    User user = service.getCurrentUser(event.getRequest());
                    String folderId = "";
     
                    Folder f;

					f = mm.getSpecialFolder(user.getId(), Folder.FOLDER_INBOX);
					
                    folderId = f.getId();

                   

                    DaoQuery query = new SerializationFriendlyDaoQuery();
                    query.addProperty(new OperatorEquals("folderId", folderId, DaoOperator.OPERATOR_AND));

                    _query=query;

                   int count= mm.getMessagesCount(query);
                    
                    if (_index <= 0) {
                        _index = 1;
                        _nextIndex = 1;
                    }
                    if (_index > count) {
                        _index = count;
                        _previousIndex = count;
                    }
                    nextMessage = getPrevNextMessage(_nextIndex, count);
                    previousMessage = getPrevNextMessage(_previousIndex, count);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                
            }
        }
        catch (NumberFormatException e) {
            _log.error("error converting "+INDEX+" request parameter ["+indexStr+"] to number", e);
        }
	}
    
	Message getTableRowBasedOnPredefinedParameters(int index) {
        try {
            MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            Collection c =  mm.getMessages(_query, (index - 1), 1, "messageDate", true);
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

	private Message getPrevNextMessage(int indexOfInterest, int count) {
		MessagingModule mm = Util.getMessagingModule();
		
		Message message = null;
        try {
            
           // if we have a valid index
           if (indexOfInterest > 0 && indexOfInterest <= count) {
               message = getTableRowBasedOnPredefinedParameters(indexOfInterest);
           }

           if (message != null) {
               // load message from module to retrieve complete email (including attachments)
               message = mm.getMessageByMessageId(message.getMessageId());
           }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving message " + message.getMessageId(), e);
        }
        return message;
	}
	
	private Message getPrevNextMessage(int indexOfInterest, MessageTableModel model) {
		MessagingModule mm = Util.getMessagingModule();
		
		Message message = null;
        try {
            
           // if we have a valid index
           if (indexOfInterest > 0 && indexOfInterest <= model.getTotalRowCount()) {
               message = model.getTableRowBasedOnPredefinedParameters(indexOfInterest);
           }

           if (message != null) {
               // load message from module to retrieve complete email (including attachments)
               message = mm.getMessageByMessageId(message.getMessageId());
           }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving message " + message.getMessageId(), e);
        }
        return message;
	}

	public String getTemplate() {
        return "messaging/readMessage";
    }


    // === [ getters/setters ] =================================================
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

	public Message getNextMessage()
	{
		return nextMessage;
	}

	public void setNextMessage(Message nextMessage)
	{
		this.nextMessage = nextMessage;
	}

	public Message getPreviousMessage()
	{
		return previousMessage;
	}

	public void setPreviousMessage(Message previousMessage)
	{
		this.previousMessage = previousMessage;
	}
    
    public int getNextMessageIndex() { return _nextIndex; }
    public int getPreviousMessageIndex() { return _previousIndex; }

	public DaoQuery get_query() {
		return _query;
	}

	public void set_query(DaoQuery _query) {
		this._query = _query;
	}
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
    
}
