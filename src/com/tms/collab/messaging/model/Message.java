package com.tms.collab.messaging.model;

import kacang.model.DefaultDataObject;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import java.util.*;
import java.io.InputStream;
import java.io.IOException;

public class Message extends DefaultDataObject implements Cloneable {
    /**
     * Plain text message format.
     */
    public static final int MESSAGE_FORMAT_TEXT = 1;
    /**
     * HTML message format.
     */
    public static final int MESSAGE_FORMAT_HTML = 2;
    
    
    // error flag constants
    public static final String ERROR_FLAG_FAIL = "FAIL";
    public static final String ERROR_FLAG_NORMAL = "";
    public static final String ERROR_FLAG_PENDING = "PENDING"; // used to indicate a message is being processed or already processed by a background task

    private String messageId;       // required
    private String folderId;        // stored only
    private String accountId;       // stored only

    private String from;            // required
    private List toList;            // toList or toIntranetList required
    private List ccList;            // optional
    private List bccList;           // optional
    private List toIntranetList;    // toList or toIntranetList required
    private List ccIntranetList;    // optional
    private List bccIntranetList;   // optional

    private String subject;         // required
    private String body;            // required
    private int messageFormat;      // required. defaults to MESSAGE_FORMAT_TEXT
    private Date date;              // required

    private boolean read;           // stored only
    private int attachmentCount;    // stored only
    private String storageFilename; // stored only
    private Properties headers;     // stored only
    private String icsContentType;
    private boolean ICSAttachment;
    
    private String errorFlag = ERROR_FLAG_NORMAL;       // for outbox implementation (indicate sending error)
    private boolean copyToSent = true;                  // for outbox implementation (indicate if mesage is to save to sent Folder)
    
    private List storageFileList;   // required?

    private int indicator;          // runtime use only

    /**
     * Creates a new empty Message object.
     */
    public Message() {
        setMessageFormat(MESSAGE_FORMAT_TEXT);
        toList = new ArrayList();
        ccList = new ArrayList();
        bccList = new ArrayList();
        toIntranetList = new ArrayList();
        ccIntranetList = new ArrayList();
        bccIntranetList = new ArrayList();
        headers = new Properties();
    }

    /**
     * Message ID.
     * @return
     */
    public String getId() {
        return getMessageId();
    }

    /**
     * Message ID.
     * @param s
     */
    public void setId(String s) {
        setMessageId(s);
    }

    /**
     * Returns <code>InputStream</code> of the named attachment of this
     * message. If <code>storageFileList</code> is null, this method will throw
     * a <code>NullPointerException/code>.
     *
     * @param filename specifies the attachment filename
     * @return InputStream of the attachment
     * @throws IOException if attachment is not found
     * @throws StorageException if an error occurred in the storage service
     */
    public InputStream getAttachmentInputStream(String filename, int attachmentIndex) throws IOException, StorageException {
        StorageFile sf;

        for(int c=0; c<storageFileList.size(); c++) {
            sf = (StorageFile) storageFileList.get(c);
            if(filename.equals(sf.getName())) {
                return sf.getInputStream();
            }
        }

        // get based on index
        if (storageFileList.size() >= (attachmentIndex+1)) {
            sf = (StorageFile)storageFileList.get(attachmentIndex);
            return sf.getInputStream();
        }

        // attachment not found
        throw new IOException("Attachment not found");
    }

    /**
     * Returns <code>StorageFile</code> of the named attachment of this
     * message. If <code>storageFileList</code> is null, this method will throw
     * a <code>NullPointerException/code>.
     *
     * @param filename specifies the attachment filename
     * @return StorageFile of the attachment
     * @throws IOException if attachment is not found
     * @throws StorageException if an error occurred in the storage service
     */
    public StorageFile getAttachmentStorageFile(String filename, int attachmentIndex) throws IOException, StorageException {
        StorageFile sf;

        for(int c=0; c<storageFileList.size(); c++) {
            sf = (StorageFile) storageFileList.get(c);
            if(filename.equals(sf.getName())) {
                return sf;
            }
        }

        // get based on index
        if (storageFileList.size() >= (attachmentIndex+1)) {
            sf = (StorageFile)storageFileList.get(attachmentIndex);
            return sf;
        }

        // attachment not found
        throw new IOException("Attachment not found");
    }

    /**
     * Checks whether this message has <b>Internet</b> email recipients.
     * @return true if there's at lease one Internet email recipient in this
     * message, false otherwise
     */
    public boolean hasInternetRecipient() {
        if( (getToList()!=null && getToList().size()>0) ||
        (getCcList()!=null && getCcList().size()>0) ||
        (getBccList()!=null && getBccList().size()>0) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether this message has <b>Intranet</b> email recipients.
     * @return true if there's at lease one Intranet email recipient in this
     * message, false otherwise
     */
    public boolean hasIntranetRecipient() {
        if( (getToIntranetList()!=null && getToIntranetList().size()>0) ||
        (getCcIntranetList()!=null && getCcIntranetList().size()>0) ||
        (getBccIntranetList()!=null && getBccIntranetList().size()>0) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method adds attachments to the message. Use this method to attach
     * file attachments to the message before sending it out.
     * @param sf storage file to attach
     */
    public void addStorageFile(StorageFile sf) {
        if(storageFileList==null) {
            storageFileList = new ArrayList();
        }

        storageFileList.add(sf);
    }

    public String getFromField() {
        return getFrom();
    }

    public Date getMessageDate() {
        return getDate();
    }

    public boolean isReadFlag() {
        return isRead();
    }

    public void setReadFlag(boolean b) {
        setRead(b);
    }


    // === [ getters/setters ] =================================================
    /**
     * Message ID.
     * @return
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Message ID.
     * @param messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List getToList() {
        return toList;
    }

    public void setToList(List toList) {
        this.toList = toList;
    }

    public List getCcList() {
        return ccList;
    }

    public void setCcList(List ccList) {
        this.ccList = ccList;
    }

    public List getBccList() {
        return bccList;
    }

    public void setBccList(List bccList) {
        this.bccList = bccList;
    }

    public List getToIntranetList() {
        return toIntranetList;
    }

    public void setToIntranetList(List toIntranetList) {
        this.toIntranetList = toIntranetList;
    }

    public List getCcIntranetList() {
        return ccIntranetList;
    }

    public void setCcIntranetList(List ccIntranetList) {
        this.ccIntranetList = ccIntranetList;
    }

    public List getBccIntranetList() {
        return bccIntranetList;
    }

    public void setBccIntranetList(List bccIntranetList) {
        this.bccIntranetList = bccIntranetList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if(subject != null)
            this.subject = (subject.length() <= 250) ? subject : subject.substring(0, 249);
        else
            this.subject = "";
    }

    public String getBody() {
        return (body==null) ? "" : body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(int messageFormat) {
        this.messageFormat = messageFormat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public String getStorageFilename() {
        return storageFilename;
    }

    public void setStorageFilename(String storageFilename) {
        this.storageFilename = storageFilename;
    }

    public Properties getHeaders() {
        return headers;
    }

    public void setHeaders(Properties headers) {
        this.headers = headers;
    }

    public List getStorageFileList() {
        return storageFileList;
    }

    public void setStorageFileList(List storageFileList) {
        this.storageFileList = storageFileList;
    }

    public int getIndicator() {
        return indicator;
    }

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }
    
    public String getErrorFlag() { 
    	return errorFlag;
    }
    
    public void setErrorFlag(String errorFlag) {
    	this.errorFlag = errorFlag;
    }
    
    public boolean getCopyToSent() {
    	return this.copyToSent;
    }
    
    public void setCopyToSent(boolean copyToSent) {
    	this.copyToSent = copyToSent;
    }

    public String getTruncatedSubject(){
        if(subject == null)
        return "(no subject)";
        if(subject.length()<100)
            return subject;
        else{
            StringBuffer sb = new StringBuffer(110);
            sb.append(subject.substring(0,97));
            sb.append("...");
            return sb.toString();
        }
    }

    public String getTruncatedRecipients() {
        StringBuffer sb = new StringBuffer("");
        if (toIntranetList != null && toIntranetList.size() > 0) {
            sb.append(toIntranetList.iterator().next());
        }
        if (sb.length() == 0 && toList != null && toList.size() > 0) {
            String address = toList.iterator().next().toString();
            try {
                InternetAddress ie = new InternetAddress(address);
                String personal = ie.getPersonal();
                String display = (personal != null && personal.trim().length() > 0) ? personal : ie.getAddress();
                if (display.length() > 50) {
                    display = display.substring(0, 45) + "..";
                }
                sb.append(display);
            }
            catch (AddressException e) {
                sb.append(address);
            }
        }
        if ((toIntranetList.size() + toList.size()) > 1) {
            sb.append(",...");
        }
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        Message clone = (Message)super.clone();
        clone.setId(this.getId());
        clone.setPropertyMap(new HashMap(this.getPropertyMap()));
        if (this.toList != null) {
            clone.toList=new ArrayList(this.toList);
        }
        if (this.ccList != null) {
            clone.ccList=new ArrayList(this.ccList);
        }
        if (this.bccList != null) {
            clone.bccList=new ArrayList(this.bccList);
        }
        if (this.toIntranetList != null) {
            clone.toIntranetList=new ArrayList(this.toIntranetList);
        }
        if (this.ccIntranetList != null) {
            clone.ccIntranetList=new ArrayList(this.ccIntranetList);
        }
        if (this.bccIntranetList != null) {
            clone.bccIntranetList=new ArrayList(this.bccIntranetList);
        }
        if (this.storageFileList != null) {
            clone.storageFileList=new ArrayList(this.storageFileList);
        }
        return clone;
    }

	public String getIcsContentType() {
		return icsContentType;
	}

	public void setIcsContentType(String icsContentType) {
		this.icsContentType = icsContentType;
	}

	public boolean hasICSAttachment() {
		return ICSAttachment;
	}

	public void setICSAttachment(boolean attachment) {
		ICSAttachment = attachment;
    }
}
