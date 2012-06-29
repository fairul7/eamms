package com.tms.collab.messaging.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * An enumeration identifying a Filter's Action
 * <ul>
 * 		<li>MOVE_TO_FOLDER</li>
 * 		<li>MARK_AS_READ</li>
 * 		<li>DELETE</li>
 * 		<li>FORWARD</li>
 * </ul>
 * 
 */
public abstract class FilterActionEnum extends ValuedEnum {

	private static final Log log = Log.getLog(FilterActionEnum.class);
	
	private static final String[] NAMES = new String[] {
		"MOVE_TO_FOLDER",
		"MARK_AS_READ",
		"DELETE",
		"FORWARD"
	};
	
	private static final int[] VALUES = new int[] {
		0, 1, 2, 3
	};
	
	private static final String[] I18N_KEYS = new String[] {
			"messaging.filtering.action.label.moveToFolder",
			"messaging.filtering.action.label.markAsRead",
			"messaging.filtering.action.label.delete",
			"messaging.filtering.action.label.forward"
	};
	
	
	/**
	 * FilterActionEnum : MOVE_TO_FOLDER
	 */
	public static final FilterActionEnum MOVE_TO_FOLDER = new FilterActionEnum(NAMES[0], VALUES[0], I18N_KEYS[0]) {

		public void performAction(MessagingModule module, Message message, 
				Pop3Account pop3Account, String filterValue)
		throws IOException, MessagingDaoException, javax.mail.MessagingException, 
		StorageException, MessagingException {
			
			// folder id
			String folderId = filterValue;
			module.moveMessage(message, folderId);
		}
	};
	
	/**
	 * FilterActionEnum : MARK_AS_READ
	 */
	public static final FilterActionEnum MARK_AS_READ = new FilterActionEnum(NAMES[1], VALUES[1], I18N_KEYS[1]) {

		public void performAction(MessagingModule module, Message message, 
				Pop3Account pop3Account, String filterValue)
		throws IOException, MessagingDaoException, MessagingException, 
		StorageException {
			
			message.setRead(true); // mark as read
            module.updateMessage(message);
		}
	};
	
	/**
	 * FilterActionEnum : DELETE
	 */
	public static final FilterActionEnum DELETE = new FilterActionEnum(NAMES[2], VALUES[2], I18N_KEYS[2]) {

		public void performAction(MessagingModule module, Message message, 
				Pop3Account pop3Account, String filterValue) 
		throws IOException, MessagingDaoException, javax.mail.MessagingException, 
		StorageException, MessagingException {
			
            // delete
            module.deleteMessage(message.getMessageId());
		}
		
	};
	
	/**
	 * FilterActionEnum : FORWARD
	 */
	public static final FilterActionEnum FORWARD = new FilterActionEnum(NAMES[3], VALUES[3], I18N_KEYS[3]) {

		public void performAction(MessagingModule module, Message msg, 
				Pop3Account pop3Account, String filterValue) 
		throws IOException, MessagingDaoException, javax.mail.MessagingException, 
		MessagingException {

			// if FORWARD, then filterValue is the email address.
			String forwarderEmail = filterValue;
            
            String userId = pop3Account.getUserId();
            String userName = pop3Account.getUsername();
            SmtpAccount account = module.getSmtpAccountByUserId(userId);
			
            MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
			Message message = mm.getMessageByMessageId(msg.getId(), true);
	        Message forwardMessage = new Message();
            
            
	        // set attachments
            List patheticStorageFiles = new ArrayList();
            for (Iterator i = message.getStorageFileList().iterator(); i.hasNext(); ) {
                InputStream is = null;
                try {
                    StorageFile sf = (StorageFile) i.next();
                    is = sf.getInputStream();
                    patheticStorageFiles.add(new PatheticStorageFile(sf.getName(), is));
                }
                catch(Exception e) {
                    log.error(e.toString(), e);
                }
                finally {
                    if (is != null) { is.close(); }
                }
            }
            forwardMessage.setStorageFileList(patheticStorageFiles);
            
	        // set a new messageId
	        forwardMessage.setMessageId(UuidGenerator.getInstance().getUuid());

	        // set message subject
            forwardMessage.setSubject("FW: " + message.getSubject());
            
	        // set the message body
	        /*if (message.getMessageFormat() == Message.MESSAGE_FORMAT_TEXT) {
                forwardMessage.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                String body = "\n\r\n\r" + userName + " wrote: \n\r" + Util.html2Text(message.getBody());
                forwardMessage.setBody(body);
	        } else {*/
                String body = "<br><BLOCKQUOTE style=\"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #1010ff 2px solid\"><hr>" + mm.getResponseHeader(message) + message.getBody() + "</blockquote>";
                forwardMessage.setBody(body);
	        //}
	        
	        
            List toList = new ArrayList();
            toList.addAll(Util.convertStringToInternetRecipientsList(forwarderEmail));
            toList.addAll(Util.convertStringToIntranetRecipientsList(forwarderEmail));
            forwardMessage.setToList(toList);
			
            
            module.sendMessage(account, forwardMessage, userId);
            //log.info("done forwarding to ["+)
		}
	};
	
	
	private static Map _presentationMap = null;
	
	private String _i18nKey;
	
	/**
	 * Create instance of FilterActionEnum
	 * 
	 * @param name
	 * @param value
     * @param i18nKey
	 */
	protected FilterActionEnum(String name, int value, String i18nKey) {
		super(name, value);
		_i18nKey = i18nKey;
	}

	
	/**
	 * Get a {@link FilterActionEnum} by its name.
	 * 
	 * @param name
	 * @return FilterActionEnum
	 */
	public static FilterActionEnum getEnum(String name) {
		if (name.equals(NAMES[0])) {
			return FilterActionEnum.MOVE_TO_FOLDER;
		}
		if (name.equals(NAMES[1])) {
			return FilterActionEnum.MARK_AS_READ;
		}
		if (name.equals(NAMES[2])) {
			return FilterActionEnum.DELETE;
		}
		if (name.equals(NAMES[3])) {
			return FilterActionEnum.FORWARD;
		}
		throw new IllegalArgumentException(""+name+" is not recognized by "+FilterActionEnum.class);
	}
	
	
	/**
	 * Get a list of {@link FilterActionEnum}s.
	 * 
	 * @return List
	 */
	public static List getEnumList() {	
		List l = new ArrayList();
		l.add(FilterActionEnum.MOVE_TO_FOLDER);
		l.add(FilterActionEnum.MARK_AS_READ);
		l.add(FilterActionEnum.DELETE);
		l.add(FilterActionEnum.FORWARD);
		return l;
	}
	
	
	
	/**
	 * return a map (enum name <-> i18n message) for used by presentation layer eg.
	 * SelectBox
	 * 
	 * @return Map
	 */
	public static Map getPresentationMap() {
		
		// we initialize it lazily upon first request
		if (_presentationMap == null) {
			Application application = Application.getInstance();
			
			Map presentationMap = new HashMap();
			for (Iterator i = getEnumList().iterator(); i.hasNext(); ) {
				FilterActionEnum fce = (FilterActionEnum) i.next();
				presentationMap.put(fce.getName(), application.getMessage(fce.getI18nKey()));
			}
			_presentationMap = presentationMap;
		}
		return _presentationMap;
	}
	
	// instance level ==========================================================
	/**
	 * returned the i18n key
	 */
	public String getI18nKey() {
		return _i18nKey;
	}
	
	/**
	 * returned the i18n message
	 * 
	 * @return String
	 */
	public String getI18nMessage() {
		Application application = Application.getInstance();
		return application.getMessage(getI18nKey());
	}

	// abstract ===============================================================
	
	/**
	 * Perform the action to be done by this filter.
	 */
	public abstract void performAction(MessagingModule module, 
			Message message, Pop3Account pop3Account, String filterValue) 
	throws IOException, MessagingDaoException, javax.mail.MessagingException, 
	StorageException, MessagingException;
	
}
