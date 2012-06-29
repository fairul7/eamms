package com.tms.collab.messaging.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.util.Log;

import com.tms.collab.messaging.model.queue.QueueItem;
import com.tms.collab.messaging.model.queue.ServerViewQueueItem;

public class MessagingQueue {
    private static MessagingQueue ourInstance = new MessagingQueue();
    
    public static final String SESSION_KEY_USER_STATUS = "MessagingQueue.userStatus";

    public static MessagingQueue getInstance() {
        return ourInstance;
    }

    public MessagingQueue() {
        log = Log.getLog(MessagingQueue.class);
    }

    protected Log log;
    protected List checkPop3Queue = Collections.synchronizedList(new ArrayList());
    protected List downloadPop3Queue = Collections.synchronizedList(new ArrayList());
    protected List serverViewQueue = Collections.synchronizedList(new ArrayList());
    /**
     * TODO: save this to database to save RAM
     */
    protected List sendSmtpQueue = Collections.synchronizedList(new ArrayList());

    /**
     * Indicates number of tasks currently running. Used to limit number
     * of jobs that processes the specified type of process.
     */
    protected int checkPop3Current = 0;
    protected int downloadPop3Current = 0;
    protected int serverViewCurrent = 0;
    protected int sendSmtpQueueCurrent = 0;

    /**
     * A Map to hold MessagingUserStatus objects.
     */
    private Map userStatusMap = Collections.synchronizedMap(new HashMap());

    /**
     * Get a user's MessagingUserStatus object.
     *
     * @param userId
     * @return specified user's MessagingUserStatus object, null if userId is
     * not a valid messaging user (does not have any messaging account)
     * @throws MessagingException
     * @see MessagingUserStatus
     */
    public MessagingUserStatus getUserStatus(String userId) throws MessagingException {
        MessagingUserStatus us = null;

        us = (MessagingUserStatus) userStatusMap.get(userId);

        if (us == null) {
            synchronized (MessagingQueue.class) {
                // don't have, try to create
                MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                Collection col = mm.getAccounts(userId);
                if (col != null && col.size() > 0) {
                    us = new MessagingUserStatus(userId);
                    userStatusMap.put(userId, us);
                } else {
                    throw new MessagingException("Messaging user status cannot be used by userId: " + userId);
                }
            }
        }
        
        // look in session
        HttpServletRequest request = Application.getThreadRequest();
        if (request != null) {
        	MessagingUserStatus sessionUserStatus = (MessagingUserStatus)request.getSession().getAttribute(SESSION_KEY_USER_STATUS);
        	if (sessionUserStatus != null) {
        		if (us.getTrackerDownload().getUpdateDate().after(sessionUserStatus.getTrackerDownload().getUpdateDate()) || us.getTrackerDownload().getUpdateDate().equals(sessionUserStatus.getTrackerDownload().getUpdateDate())) {
        			request.getSession().setAttribute(SESSION_KEY_USER_STATUS, us);
        		}
        		else {
        			us = sessionUserStatus;
        		}
        	}
        	else {
    			request.getSession().setAttribute(SESSION_KEY_USER_STATUS, us);
        	}
        }

        return us;
    }


    // === [ check POP3 (quick check) ] ========================================
    public void pushCheckPop3(QueueItem qi) {
        if (!isInQueue(qi)) {
            log.debug("Push check POP3 queue");
            checkPop3Queue.add(qi);
        }
    }

    private boolean isInQueue(QueueItem qi) {
        boolean inQueue = false;

        for (int i = 0; i < checkPop3Queue.size(); i++) {
            QueueItem qi2 = (QueueItem) checkPop3Queue.get(i);

            if (qi.getUserId().equals(qi2.getUserId())) {
                // already in queue, ignore
                log.debug("Skipped push queue (in check POP3 queued)");
                inQueue = true;
                break;
            }
        }

        if (!inQueue) {
            for (int i = 0; i < downloadPop3Queue.size(); i++) {
                QueueItem qi2 = (QueueItem) downloadPop3Queue.get(i);

                if (qi2.getUserId().equals(qi.getUserId())) {
                    // already in queue, ignore
                    log.debug("Skipped push queue (in download POP3 queue)");
                    inQueue = true;
                    break;
                }
            }
        }

        if(!inQueue) {
            for (int i = 0; i < serverViewQueue.size(); i++) {
                QueueItem qi2 = (QueueItem) serverViewQueue.get(i);
                if(qi2.getUserId().equals(qi.getUserId())) {
                    // already in queue, ignore
                    log.debug("Skipped push queue (in server view queue)");
                    inQueue = true;
                    break;
                }
            }
        }

        return inQueue;
    }

    private boolean isInQueue(List queue, QueueItem qi) {
        boolean inQueue = false;

        for (int i = 0; i < queue.size(); i++) {
            QueueItem qi2 = (QueueItem) queue.get(i);

            if (qi2.getUserId().equals(qi.getUserId())) {
                inQueue = true;
            }
        }

        return inQueue;
    }

    /**
     *
     * @return userId
     */
    public QueueItem popCheckPop3() {
        QueueItem obj = null;

        if (checkPop3Queue.size() > 0) {
            obj = (QueueItem) checkPop3Queue.remove(0);
            log.debug("Pop check POP3 queue");
        } else {
            log.trace("Pop check POP3 queue (empty)");
        }

        return obj;
    }

    public int getCheckPop3Size() {
        return checkPop3Queue.size();
    }


    // === [ download POP3 ] ===================================================
    public void pushDownloadPop3(QueueItem qi) {
        // if in checkPop3Queue, remove it
        if (isInQueue(checkPop3Queue, qi)) {
            // remove
            log.debug("Override check POP3 queue with download");
            checkPop3Queue.remove(qi);
        }

        if (!isInQueue(qi)) {
            log.debug("Push download POP3 queue");
            downloadPop3Queue.add(qi);
        }
    }

    /**
     *
     * @return userId
     */
    public QueueItem popDownloadPop3() {
        QueueItem obj = null;

        if (downloadPop3Queue.size() > 0) {
            obj = (QueueItem) downloadPop3Queue.remove(0);
            log.debug("Pop download POP3 queue");
        } else {
            log.trace("Pop download POP3 queue (empty)");
        }

        return obj;
    }

    public int getDownloadPop3Size() {
        return downloadPop3Queue.size();
    }


    // === [ serverView ] ======================================================
    public void pushServerView(ServerViewQueueItem qi) {
        if(!isInQueue(qi)) {
            log.debug("Push server view queue");
            serverViewQueue.add(qi);
        }
    }

    /**
     * Format below...
     * <p>
     *
     * <pre>
     * obj[0] = type of server view queue action. SERVER_VIEW_XXX
     * obj[1] = userId
     * obj[2..x] = optional action specific data
     * </pre>
     *
     * @return
     */
    public ServerViewQueueItem popServerView() {
        ServerViewQueueItem item = null;

        if (serverViewQueue.size() > 0) {
            item = (ServerViewQueueItem) serverViewQueue.remove(0);
            log.debug("Pop server view queue");
        } else {
            log.trace("Pop server view queue (empty)");
        }

        return item;
    }

    public int getServerViewSize() {
        return serverViewQueue.size();
    }


    // === [ send SMTP ] =======================================================
    public void pushSendSmtp(SmtpAccount smtpAccount, MimeMessage mimeMessage) {
        Object[] obj = {smtpAccount, mimeMessage};

        log.debug("Push send SMTP queue");
        sendSmtpQueue.add(obj);
    }

    /**
     * This method returns an array of two objects { smtpAccount, mimeMessage }.
     * // TODO: Poor design - Object[] as return value
     *
     * @return
     */
    public Object[] popSendSmtp() {
        Object[] obj = null;

        if (sendSmtpQueue.size() > 0) {
            obj = (Object[]) sendSmtpQueue.remove(0);
            log.debug("Pop send SMTP queue");
        } else {
            log.trace("Pop send SMTP queue (empty)");
        }

        return obj;
    }

    public int getSendSmtpSize() {
        return sendSmtpQueue.size();
    }


    // === [ getters/setters ] =================================================
    public List getCheckPop3Queue() {
        return checkPop3Queue;
    }

    public void setCheckPop3Queue(List checkPop3Queue) {
        this.checkPop3Queue = checkPop3Queue;
    }

    public List getDownloadPop3Queue() {
        return downloadPop3Queue;
    }

    public List getServerViewQueue() {
        return serverViewQueue;
    }

    public void setServerViewQueue(List serverViewQueue) {
        this.serverViewQueue = serverViewQueue;
    }

    public void setDownloadPop3Queue(List downloadPop3Queue) {
        this.downloadPop3Queue = downloadPop3Queue;
    }

    public List getSendSmtpQueue() {
        return sendSmtpQueue;
    }

    public void setSendSmtpQueue(List sendSmtpQueue) {
        this.sendSmtpQueue = sendSmtpQueue;
    }

    public int getCheckPop3Current() {
        return checkPop3Current;
    }

    public void setCheckPop3Current(int checkPop3Current) {
        this.checkPop3Current = checkPop3Current > 0 ? checkPop3Current : 0;
    }

    public int getDownloadPop3Current() {
        return downloadPop3Current;
    }

    public void setDownloadPop3Current(int downloadPop3Current) {
        this.downloadPop3Current = downloadPop3Current > 0 ? downloadPop3Current : 0;
    }

    public int getServerViewCurrent() {
        return serverViewCurrent;
    }

    public void setServerViewCurrent(int serverViewCurrent) {
        this.serverViewCurrent = serverViewCurrent;
    }

    public int getSendSmtpQueueCurrent() {
        return sendSmtpQueueCurrent;
    }

    public void setSendSmtpQueueCurrent(int sendSmtpQueueCurrent) {
        this.sendSmtpQueueCurrent = sendSmtpQueueCurrent > 0 ? sendSmtpQueueCurrent : 0;
    }

}
