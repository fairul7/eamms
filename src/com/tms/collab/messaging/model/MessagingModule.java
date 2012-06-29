package com.tms.collab.messaging.model;

import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarDao;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.CalendaringEventView;
import com.tms.collab.calendar.model.ConflictException;
import com.tms.collab.calendar.model.ICalendarParser;
import com.tms.collab.messaging.model.queue.QueueItem;
import com.tms.collab.messaging.model.queue.ServerViewQueueItem;
import com.tms.util.FormatUtil;
import com.tms.util.MailUtil;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.cms.core.model.ContentPublisher;
import kacang.Application;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.model.operator.*;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.Group;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Encryption;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.NodeReader;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserUtils;
import org.htmlparser.visitors.TextExtractingVisitor;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO: outstanding features for this module
 * <li>Integration with address book ---DONE---
 * <li>Add emails to address book ---DONE---
 * <li>Asynchronous email downloading? ---DONE---
 * <li>Signature ---DONE---
 * <li>Multiple attachments ---DONE---
 * <li>Forwarding messages to include attachments ---DONE---
 * <li>In Draft and Sent message listing, show To instead of From column ---DONE---
 * <li>Subfolders
 * <li>Disk space quota
 * <li>Move message to folders from readMessage.jsp
 * <li>Filtering messages
 * <li>Asynchronous email sending via Outbox
 * <li>Print Email --DONE---
 * <li>Search messages in view
 * <li>choose reply to email when composing messages
 * <li>portlet options for Inbox
 */
public class MessagingModule extends DefaultModule implements SearchableModule, SecurityEventListener {

    public static final String CUSTOM_SEARCH_PROPERTY_MESSAGE = "message";

    public static final String APPLICATION_KEY_CONNECTION_TIMEOUT = "messaging.connection.timeout";
    public static final String APPLICATION_KEY_JOB_COUNT_DOWNLOAD = "messaging.jobCount.download";
    public static final String APPLICATION_KEY_JOB_COUNT_CHECK = "messaging.jobCount.check";
    public static final String APPLICATION_KEY_JOB_COUNT_SEND = "messaging.jobCount.send";
    public static final String APPLICATION_KEY_JOB_COUNT_SERVER_VIEW = "messaging.jobCount.serverView";
    public static final String APPLICATION_KEY_SCHEDULE_INTERVAL = "messaging.schedule.interval";

    public static final String APPLICATION_KEY_AUTOCOMPLETE_MAX_MESSAGES = "messaging.autoComplete.maxMessages";
    public static final String APPLICATION_KEY_AUTOCOMPLETE_MAX_ADDRESSES = "messaging.autoComplete.maxAddresses";
    public static final String APPLICATION_KEY_DRAG_DROP_MESSAGES = "messaging.dragDrop.enabled";

    public static final String TO_ATTRIBUTE = "toAttribute";
    public static final String CC_ATTRIBUTE = "ccAttribute";
    public static final String BCC_ATTRIBUTE = "bccAttribute";

    private static String ENCRYPTION_KEY = "SiRiMoK";

    public static final String APPLICATION_KEY_DEFAULT_QUOTA_KB = "messaging.defaultQuota";

    private Log log = Log.getLog(getClass());

    /**
     * Storage service root path for this module
     */
    public static final String ROOT_PATH = "/messaging";

    /**
     * Intranet email domain (the name after @ in an email).
     */
    public static final String INTRANET_EMAIL_DOMAIN = "intranet";

    /**
     * Contact group business directory email domain (the name after @ in an email).
     */
    public static final String CONTACT_GROUP_DIRECTORY_EMAIL_DOMAIN = "directory";

    /**
     * Contact group personal address book email domain (the name after @ in an email).
     */
    public static final String CONTACT_GROUP_PERSONAL_EMAIL_DOMAIN = "personal";

    /**
     * Session attribute of to store attachment filenames.
     */
    public static final String ATTACHMENT_MAP_SESSION_ATTRIBUTE = "attachmentMap";

    // set default connection timeout to 30 seconds
    private static final String TIMEOUT_PERIOD = "30000";

    public static final String QM_SUBJECT = "--- Quick Message ---";

    public static final int PREVIEW_NO_LINES = 25;

    public static final int POP_ACCOUNT_LOG_SIZE = 10 * 1024;

    public void init() {
        super.init();

        try {
            stopQueueProcessing();
            startQueueProcessing();
            log.info("Messaging queue started");
            Application application = Application.getInstance();
            SecurityService sec = (SecurityService) application.getService(SecurityService.class);
            sec.addEventListener(this);
        } catch (MessagingException e) {
            log.error("Error starting messaging queue", e);
        }
        log.debug("Messaging module initialized");
    }

    // === [Filter] ==========================================================
    private FilterManager _filterManager = null;
    
    public void moveUpFilterOrdering(String userId, String filterId) {
        try {
            MessagingDao dao = (MessagingDao) getDao();

            String previousFid = null;
            String currentFid = null;
        
            for (Iterator i = dao.selectFilters(userId).iterator(); i.hasNext(); ) {
                previousFid = currentFid;
                currentFid = (String) ((Filter)i.next()).getId();
            
                if (currentFid.equals(filterId)) {
                    if (previousFid != null) {
                        swapOrdering(previousFid, currentFid);
                    }
                    break;
                }
            }
        }
        catch(MessagingDaoException e) {
            log.error(e.toString(), e);
        }
    }
    
    public void moveDownFilterOrdering(String userId, String filterId) {
        
        try {
            MessagingDao dao = (MessagingDao) getDao();
        
            String currentFid = null;
            String nextFid = null;
        
            for (Iterator i = dao.selectFilters(userId).iterator(); i.hasNext(); ) {
                currentFid = (String) ((Filter)i.next()).getId();
                if (currentFid.equals(filterId)) {
                    if (i.hasNext()) {
                        nextFid = (String) ((Filter) i.next()).getId();
                        if (nextFid != null) {
                            swapOrdering(nextFid, currentFid);
                        }
                    }
                    break;
                }
            }
        }
        catch(MessagingDaoException e) {
            log.error(e.toString(), e);
        }
    }
    
    protected void swapOrdering(String firstFid, String secondFid) throws MessagingDaoException {
        MessagingDao dao = (MessagingDao) getDao();
        
        Filter previousFilter = dao.selectFilter(firstFid);
        int previousFilterOrdering = previousFilter.getFilterOrder();
        Filter currentFilter = dao.selectFilter(secondFid);
        int currentFilterOrdering = currentFilter.getFilterOrder();
        
        previousFilter.setFilterOrder(currentFilterOrdering);
        currentFilter.setFilterOrder(previousFilterOrdering);
        
        dao.updateFilter(previousFilter);
        dao.updateFilter(currentFilter);
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * Delete a {@link Filter}.
     * 
     * @param filter
     */
    public void addOrUpdateDaoBasedFilter(Filter filter, String userId) {
    	try {
    		MessagingDao dao = (MessagingDao) getDao();
    		
    		if (! filter.isToBePersist()) {
    			// there is a record already exist, we wanna get rid of it first
    			dao.deleteFilter(filter.getId());
    		}
            else {
                // its a new filter, we want to set its ordering to the last
                int numberOfFilters = dao.selectMaxFilterOrder(userId);
                filter.setFilterOrder(numberOfFilters + 1);
            }
            dao.insertFilter(filter);
    	}
    	catch(MessagingDaoException e) {
    		log.error(e.toString(), e);
    		//throw new RuntimeException(e);
    	}
    }
    
    public void removeDaoBasedFilter(String filterId) {
    	try {
    		MessagingDao dao = (MessagingDao) getDao();
    		dao.deleteFilter(filterId);
    	}
    	catch(MessagingDaoException e) {
    		log.error(e.toString(), e);
    		//throw new RuntimeException(e);
    	}
    }
    
    public void activateDaoBasedFitler(String fId) {
    	try {
    		MessagingDao dao = (MessagingDao) getDao();
    		Filter f = dao.selectFilter(fId);
    		
    		Filter filter = new Filter(
    				f.getFilterId(), f.getUserId(), f.getName(), 
					f.getFilterValue(), true, f.getFilterAction(), 
					f.getFilterCondition(), (List) f.getRules());
            filter.setFilterOrder(f.getFilterOrder());
    		dao.updateFilter(filter);
    	}
    	catch(MessagingDaoException e) {
    		log.error(e.toString(), e);
    		//throw new RuntimeException(e);
    	}
    }
    
    public void pasivateDaoBasedFilter(String fId) {
    	try {
    		MessagingDao dao = (MessagingDao) getDao();
    		Filter f = dao.selectFilter(fId);
    		
    		Filter filter = new Filter(
    				f.getFilterId(), f.getUserId(), f.getName(),
					f.getFilterValue(), false, f.getFilterAction(),
					f.getFilterCondition(), (List) f.getRules());
            filter.setFilterOrder(f.getFilterOrder());
    		dao.updateFilter(filter);
    	}
    	catch(MessagingDaoException e) {
    		log.error(e.toString(), e);
    		//throw new RuntimeException(e);
    	}
    }

    /**
     * To obtain a {@link FilterManager}.
     * 
     * @return FilterManager
     */
    public FilterManager getFilterManager() {
    	// we grab a copy lazily
    	if (_filterManager == null) {
    		_filterManager = new FilterManager((MessagingModule) this);
    		
    		// add custom filters here
    		_filterManager.addFilter(new DaoMessageFilter());
    	}
    	return _filterManager;
    }
    
    /**
     * Count the number of {@link Filter}s for this user.
     * 
     * @param userId
     * @return int
     */
    public int getFilterCountForUser(String userId) {
    	try {
    		MessagingDao dao = (MessagingDao) getDao();
    		return dao.selectFiltersCount(userId);
    	}
    	catch(MessagingDaoException e) {
    		log.error(e.toString(), e);
    		throw new RuntimeException(e);
    	}
    }
    
    /**
     * Collection of {@link Filter} objects.
     * 
     * @param userId
     * @return
     */
    public Collection getFiltersForUser(String userId) {
        Collection filters = Collections.EMPTY_LIST;
    	try {
    		MessagingDao dao = (MessagingDao) getDao();
    		filters = dao.selectFilters(userId);
    	}
    	catch(MessagingDaoException e) {
    		log.error(e.toString(), e);
    		//throw new RuntimeException(e);
    	}
        return filters;
    }
	
    
    /**
     * Retrieve a Filter by its id.
     * 
     * @param filterId
     * @return
     */
    public Filter getFilter(String filterId) {
        Filter filter = null;
    	try {
    		MessagingDao dao = (MessagingDao) getDao();
    		filter = dao.selectFilter(filterId);
    	}
    	catch(MessagingDaoException e) {
    		log.error(e.toString(), e);
    		//throw new RuntimeException(e);
    	}
        return filter;
    }
	
    
    
    
    
    
    // === [ messaging schedules ] =============================================
    public static final String SCHEDULE_GROUP = "messaging";
    public static final String SCHEDULE_DOWNLOAD_POP3 = "download";
    public static final String SCHEDULE_CHECK_POP3 = "check";
    public static final String SCHEDULE_SEND = "send";
    public static final String SCHEDULE_SEND_SMTP = "smtp";
    public static final String SCHEDULE_SERVER_VIEW = "serverView";
    public static final String SCHEDULE_QUOTA_UPDATE = "quotaUpdate";
    
    private int downloadPop3JobCount = 5;
    private int checkPop3JobCount = 3;
    private int sendMailJobCount = 1;
    private int serverViewJobCount = 5;
    private int scheduleRepeatInterval = 15;    // in seconds

    public void startQueueProcessing() throws MessagingException {
        SchedulingService ss;
        JobTask job;
        JobSchedule sched;

        // get application properties
        Application app = Application.getInstance();
        try {
            downloadPop3JobCount = Integer.parseInt(app.getProperty(APPLICATION_KEY_JOB_COUNT_DOWNLOAD));
        } catch (Exception e) {
        }
        try {
            checkPop3JobCount = Integer.parseInt(app.getProperty(APPLICATION_KEY_JOB_COUNT_CHECK));
        } catch (Exception e) {
        }
        try {
            sendMailJobCount = Integer.parseInt(app.getProperty(APPLICATION_KEY_JOB_COUNT_SEND));
        } catch (Exception e) {
        }
        try {
            serverViewJobCount = Integer.parseInt(app.getProperty(APPLICATION_KEY_JOB_COUNT_SERVER_VIEW));
        } catch (Exception e) {
        }
        try {
            scheduleRepeatInterval = Integer.parseInt(app.getProperty(APPLICATION_KEY_SCHEDULE_INTERVAL));
        } catch (Exception e) {
        }

        // make sure schedule interval is not zero
        if (scheduleRepeatInterval == 0) {
            scheduleRepeatInterval = 15;
        }

        // stop queue processing first
        stopQueueProcessing();

        ss = (SchedulingService) Application.getInstance().getService(SchedulingService.class);

        sched = new JobSchedule(SCHEDULE_DOWNLOAD_POP3, JobSchedule.SECONDLY);
        sched.setGroup(SCHEDULE_GROUP);
        sched.setRepeatInterval(scheduleRepeatInterval);
        sched.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);

        // changed to only run 1 job.
        final int onlyOne = 1;
        try {
            for (int i = 0; i < onlyOne; i++) {
                sched.setName(SCHEDULE_DOWNLOAD_POP3 + " " + (i + 1));
                job = new DownloadPop3Job();
                job.setName(SCHEDULE_DOWNLOAD_POP3 + " " + (i + 1));
                job.setGroup(SCHEDULE_GROUP);
                ss.scheduleJob(job, sched);
                log.debug("Scheduling job: " + sched.getName());
            }

            for (int i = 0; i < onlyOne; i++) {
                sched.setName(SCHEDULE_CHECK_POP3 + " " + (i + 1));
                job = new CheckPop3Job();
                job.setName(SCHEDULE_CHECK_POP3 + " " + (i + 1));
                job.setGroup(SCHEDULE_GROUP);
                ss.scheduleJob(job, sched);
                log.debug("Scheduling job: " + sched.getName());
            }
            for (int i = 0; i < onlyOne; i++) {
                sched.setName(SCHEDULE_SEND_SMTP + " " + (i + 1));
                job = new SendSmtpJob();
                job.setName(SCHEDULE_SEND_SMTP + " " + (i + 1));
                job.setGroup(SCHEDULE_GROUP);
                ss.scheduleJob(job, sched);
                log.debug("Scheduling job: " + sched.getName());
            }
            for (int i = 0; i < onlyOne; i++) {
            	sched.setName(SCHEDULE_SEND + " " + (i + 1));
                job = new SendIntranetAndSmtpmMessagesJob();
                job.setName(SCHEDULE_SEND+" " + (i + 1));
                job.setGroup(SCHEDULE_GROUP);
                ss.scheduleJob(job, sched);
                log.debug("Scheduling job: " + sched.getName());
            }
            
            for (int i = 0; i < onlyOne; i++) {
                sched.setName(SCHEDULE_QUOTA_UPDATE);
                job = new QuotaUpdateJob();
                job.setName(SCHEDULE_QUOTA_UPDATE+" "+ (i+1));
                job.setGroup(SCHEDULE_GROUP);
                ss.scheduleJob(job, sched);
                log.debug("Scheduling job: " + sched.getName());
            }
            
            for (int i = 0; i < onlyOne; i++) {
                sched.setName(SCHEDULE_SERVER_VIEW + " " + (i + 1));
                job = new ServerViewJob();
                job.setName(SCHEDULE_SERVER_VIEW + " " + (i + 1));
                job.setGroup(SCHEDULE_GROUP);
                ss.scheduleJob(job, sched);
                log.debug("Scheduling job: " + sched.getName());
            }

        } catch (SchedulingException e) {
            throw new MessagingException("Error scheduling messaging queue job", e);
        }
    }

    public void stopQueueProcessing() throws MessagingException {
        SchedulingService ss;
        JobSchedule sched;
        String[] scheduleNames;

        ss = (SchedulingService) Application.getInstance().getService(SchedulingService.class);

        try {
            scheduleNames = ss.getJobScheduleNames(SCHEDULE_GROUP);
            for (int i = 0; i < scheduleNames.length; i++) {
                String scheduleName = scheduleNames[i];
                sched = ss.getJobSchedule(scheduleName, SCHEDULE_GROUP);
                ss.unscheduleJob(sched);
            }
            log.debug("Stopped " + scheduleNames.length + " scheduled jobs");

        } catch (SchedulingException e) {
            throw new MessagingException("Error stopping queue processing", e);
        }
    }

    public int getDownloadPop3JobCount() {
        return downloadPop3JobCount;
    }

    public int getCheckPop3JobCount() {
        return checkPop3JobCount;
    }

    public int getSendMailJobCount() {
        return sendMailJobCount;
    }

    public int getServerViewJobCount() {
        return serverViewJobCount;
    }

    public int getScheduleRepeatInterval() {
        return scheduleRepeatInterval;
    }

    public String[] getQueueProcessingStatus() throws MessagingException {
        SchedulingService ss;
        String[] scheduleNames = null;

        ss = (SchedulingService) Application.getInstance().getService(SchedulingService.class);

        try {
            scheduleNames = ss.getJobScheduleNames(SCHEDULE_GROUP);
            return scheduleNames;

        } catch (SchedulingException e) {
            throw new MessagingException("Error getting queue processing status", e);
        }

    }


    // === [ Searchable interface methods ] ====================================
    public SearchResult search(String s, int startRow, int maxRows, String userId) throws QueryException {
        return search(s, startRow, maxRows, userId, null, null);
    }

    public SearchResult searchFullText(String s, int i, int i1, String s1) throws QueryException {
        return null;
    }

    public boolean isSearchSupported() {
        return true;
    }

    public boolean isFullTextSearchSupported() {
        return false;
    }

    /**
     *
     * @param s
     * @param startRow
     * @param maxRows
     * @param userId
     * @param startDate message date must be greater than or equals startDate
     * @param endDate message date must be less than or equals endDate
     * @return
     * @throws QueryException
     */
    public SearchResult search(String s, int startRow, int maxRows, String userId, Date startDate, Date endDate) throws QueryException {
        MessagingDao dao;
        SearchResult sr;
        SearchResultItem item;
        DaoQuery query;
        Map valueMap;
        Message message;
        String title;
        String body;

        query = new DaoQuery();
        query.addProperty(new OperatorEquals("userId", userId, DaoOperator.OPERATOR_AND));

        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
        parenthesis.addOperator(new OperatorLike("subject", s, null));
        parenthesis.addOperator(new OperatorLike("body", s, DaoOperator.OPERATOR_OR));
        parenthesis.addOperator(new OperatorLike("fromField", s, DaoOperator.OPERATOR_OR));
        parenthesis.addOperator(new OperatorLike("toField", s, DaoOperator.OPERATOR_OR));
        parenthesis.addOperator(new OperatorLike("ccField", s, DaoOperator.OPERATOR_OR));
        query.addProperty(parenthesis);

        if(startDate!=null) {
            OperatorParenthesis p2 = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            p2.addOperator(new OperatorGreaterThanEquals("messageDate", startDate, null));
            query.addProperty(p2);
        }

        if(endDate!=null) {
            OperatorParenthesis p3 = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            p3.addOperator(new OperatorLessThanEquals("messageDate", endDate, null));
            query.addProperty(p3);
        }

        dao = (MessagingDao) getDao();
        sr = new SearchResult();
        try {
            Collection messages = dao.selectMessages(query, startRow, maxRows, "messageDate", true);
            for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
                message = (Message) iterator.next();

                message = getMessageByMessageId(message.getId());
                body = message.getBody();

                // strip html codes, for html messages
                if (message.getMessageFormat() == Message.MESSAGE_FORMAT_HTML) {
                    body = filterHtml(body);
                }

                // make long body shorter
                if (body != null && body.length() > 200) {
                    body = body.substring(0, 200) + "...";
                }

                if (body.trim().length() == 0) {
                    body = "-Message content is empty-";
                }

                // add 'from' and 'date' field to title/subject
                title = message.getSubject();
                if (title.trim().length() == 0) {
                    title = "-No Subject-";
                }
                title = "\"" + title + "\" from " + message.getFrom();

                valueMap = new SequencedHashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS, message.getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, message.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, title);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, body);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DATE, message.getDate());
                valueMap.put(CUSTOM_SEARCH_PROPERTY_MESSAGE, message);

                item = new SearchResultItem();
                item.setValueMap(valueMap);
                if (message.getSubject() != null && message.getSubject().indexOf(s) != -1) {
                    // search string is in subject, so score 100
                    item.setScore(new Float(100));
                } else {
                    // search string is in else where, so score 50
                    item.setScore(new Float(50));
                }
                sr.add(item);
            }

            // get total size
            /* Constructing query manually. selectMessagesCount no longer accepts userId as of 1.5.1 */
            DaoQuery query2 = new DaoQuery();
            OperatorParenthesis parenthesis2 = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            parenthesis2.addOperator(new OperatorLike("subject", s, null));
            parenthesis2.addOperator(new OperatorLike("body", s, DaoOperator.OPERATOR_OR));
            parenthesis2.addOperator(new OperatorLike("fromField", s, DaoOperator.OPERATOR_OR));
            parenthesis2.addOperator(new OperatorLike("toField", s, DaoOperator.OPERATOR_OR));
            parenthesis2.addOperator(new OperatorLike("ccField", s, DaoOperator.OPERATOR_OR));
            query2.addProperty(parenthesis2);
            if(startDate!=null)
            {
                OperatorParenthesis pc2 = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                pc2.addOperator(new OperatorGreaterThanEquals("messageDate", startDate, null));
                query.addProperty(pc2);
            }
            if(endDate!=null)
            {
                OperatorParenthesis pc3 = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                pc3.addOperator(new OperatorLessThanEquals("messageDate", endDate, null));
                query.addProperty(pc3);
            }
            //Retrieving folders
            Collection list = getFolders(userId);
            if(list.size() > 0)
            {
                Collection folderIds = new ArrayList();
                for (Iterator i = list.iterator(); i.hasNext();)
                {
                    Folder folder = (Folder) i.next();
                    folderIds.add(folder.getFolderId());
                }
                query2.addProperty(new OperatorIn("folderId", folderIds.toArray(new String[] {}), DaoOperator.OPERATOR_AND));
            }

            int totalSize = dao.selectMessagesCount(query2);
            sr.setTotalSize(totalSize);

            return sr;

        } catch (MessagingDaoException e) {
            throw new QueryException(e.getMessage(), e);
        } catch (MessagingException e) {
            throw new QueryException(e.getMessage(), e);
        }
    }

    public SearchResult searchFullText(String s, int i, int i1, String s1, Date date, Date date1) throws QueryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    // === [ Account ] =========================================================
    /**
     * Registers a new user into the system. This will create an intranet
     * account with all the special folders: INBOX, DRAFT, SENT and TRASH.
     *
     * @param intranetAccount intranet account to create
     * @param smtpAccount     SMTP account for this intranet account
     * @throws MessagingException if an error occurred while create the new
     *                            user account or the intranet username already taken
     */
    public void addIntranetAccount(IntranetAccount intranetAccount,
                                                SmtpAccount smtpAccount) throws MessagingException {
        MessagingDao dao;
        Folder inbox, draft, sent, trash;
        UuidGenerator uuidGenerator;

        dao = (MessagingDao) getDao();

        // make sure intranet username is not taken
        try {
            IntranetAccount account = dao.selectIntranetAccount(intranetAccount.getIntranetUsername());
            /* Renaming existing account */
            account.setName("arc_" + UuidGenerator.getInstance().getUuid() + "_" + account.getName());
            dao.updateAccount(account);
            /*throw new MessagingException("Intranet username " + intranetAccount.getIntranetUsername() + " already exist");*/
        } catch (MessagingDaoException e) {
            // not found means can proceed to create
        }

        // TODO: should check for one userId, one intranet account?

        try {
            uuidGenerator = UuidGenerator.getInstance();

            inbox = new Folder();
            inbox.setFolderId(uuidGenerator.getUuid());
            inbox.setName(Folder.FOLDER_INBOX);
            inbox.setSpecialFolder(true);
            inbox.setUserId(intranetAccount.getUserId());

            intranetAccount.setDeliveryFolderId(inbox.getFolderId());

            draft = new Folder();
            draft.setFolderId(uuidGenerator.getUuid());
            draft.setName(Folder.FOLDER_DRAFT);
            draft.setSpecialFolder(true);
            draft.setUserId(intranetAccount.getUserId());

            sent = new Folder();
            sent.setFolderId(uuidGenerator.getUuid());
            sent.setName(Folder.FOLDER_SENT);
            sent.setSpecialFolder(true);
            sent.setUserId(intranetAccount.getUserId());
            
            Folder outbox = new Folder();
            outbox.setFolderId(uuidGenerator.getUuid());
            outbox.setName(Folder.FOLDER_OUTBOX);
            outbox.setSpecialFolder(true);
            outbox.setUserId(intranetAccount.getUserId());

            trash = new Folder();
            trash.setFolderId(uuidGenerator.getUuid());
            trash.setName(Folder.FOLDER_TRASH);
            trash.setSpecialFolder(true);
            trash.setUserId(intranetAccount.getUserId());

            Folder qm = new Folder();
            qm.setFolderId(uuidGenerator.getUuid());
            qm.setName(Folder.FOLDER_QM);
            qm.setSpecialFolder(true);
            qm.setUserId(intranetAccount.getUserId());
            

            dao.insertAccount(intranetAccount);
            smtpAccount.setPassword(Encryption.encrypt(smtpAccount.getPassword(), ENCRYPTION_KEY));
            dao.insertSmtpAccount(smtpAccount);
            dao.insertFolder(inbox);
            dao.insertFolder(draft);
            dao.insertFolder(sent);
            dao.insertFolder(qm);
            dao.insertFolder(trash);
            dao.insertFolder(outbox);

        } catch (MessagingDaoException e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * Update details of a messaging account (either intranet or POP3).
     *
     * @param account
     * @throws MessagingException
     */
    public void updateAccount(Account account) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            if (account instanceof Pop3Account) {
                ((Pop3Account) account).setPassword(Encryption.encrypt(((Pop3Account) account).getPassword(), ENCRYPTION_KEY));
            }
            dao.updateAccount(account);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * <b>This method removes ALL data belonging to the speficied
     * <code>userId</code> wihtin the messaging module.</b>
     * <p/>
     * This will clean up all data stored for the particular user. All POP3
     * accounts, folders and messages belonging to this user will also be
     * removed.
     *
     * @param userId
     */
    public void deleteIntranetAccount(String userId)
    throws MessagingException {
        MessagingDao dao;
        StorageService sh;
        StorageDirectory sd;

        dao = (MessagingDao) getDao();

        try {
            // delete storage directory
            sh = (StorageService) Application.getInstance().getService(StorageService.class);
            sd = new StorageDirectory(ROOT_PATH + "/" + userId);

            sh.delete(sd);

            // delete database records
            dao.deleteIntranetAccount(userId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    /**
     * Returns a messaging account object (either intranet or POP3).
     *
     * @param accountId specifies the account ID
     * @return an <code>IntranetAccount</code> or <code>Pop3Account</code>
     *         object
     * @throws MessagingException if an error occurred while retrieving the
     *                            account
     */
    public Account getAccount(String accountId) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            Account account = dao.selectAccount(accountId);
            if (account instanceof Pop3Account) {
                ((Pop3Account) account).setPassword(Encryption.decrypt(((Pop3Account) account).getPassword(), ENCRYPTION_KEY));
            }
            return account;

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves the intranet messaging account for the specified userId.
     *
     * @param userId
     * @return IntranetAccount object for the specified intranet userId, or null
     *         if no IntranetAccount is associated to the userId
     * @throws MessagingException
     */
    public IntranetAccount getIntranetAccountByUserId(String userId) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectIntranetAccountByUserId(userId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * Returns all the intranet accounts within the module.
     *
     * @return Collection of IntranetAccount objects
     * @throws MessagingException if an error occurred
     */
    public Collection getIntranetAccounts() throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectIntranetAccounts();

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    /**
     * Returns the first pop3Account for the user with id <code>userId</code>.
     * @param userId
     * @return
     * @throws MessagingException
     */
    public Pop3Account getPop3Account(String userId) throws MessagingException {
    	for (Iterator i = getAccounts(userId).iterator(); i.hasNext(); ) {
    		Account acc = (Account) i.next();
    		if (acc instanceof Pop3Account) {
    			return (Pop3Account)acc;
    		}
    	}
    	return null;
    }
    
    
    /**
     * Returns all messaging accounts for a user.
     *
     * @param userId specifies the userId
     * @return Collection of Account objects
     * @throws MessagingException if an error occurred
     */
    public Collection getAccounts(String userId) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            Collection accounts = dao.selectAccounts(userId);

            for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = (Account) iterator.next();
                if (account instanceof Pop3Account) {
                    ((Pop3Account) account).setPassword(Encryption.decrypt(((Pop3Account) account).getPassword(), ENCRYPTION_KEY));
                }
            }
            return accounts;

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    public Collection getAccounts(String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectAccounts(userId, start, numOfRows, sortBy, isDescending, searchBy, searchCriteria);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    public int getNumOfAccount(String userId, String searchBy, String searchCriteria) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectAccountCount(userId, searchBy, searchCriteria);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }


    /**
     * Adds a POP3 messaging account.
     *
     * @param pop3Account
     * @throws MessagingException
     */
    public void addPop3Account(Pop3Account pop3Account) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            pop3Account.setPassword(Encryption.encrypt(pop3Account.getPassword(), ENCRYPTION_KEY));
            dao.insertAccount(pop3Account);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    /**
     * Deletes a POP3 messaging account.
     *
     * @param accountId
     * @throws MessagingException
     */
    public void deletePop3Account(String accountId) throws MessagingException {
        MessagingDao dao;
        Account account;

        try {
            dao = (MessagingDao) getDao();
            account = dao.selectAccount(accountId);
            if (account.getAccountType() == Account.ACCOUNT_TYPE_INTRANET) {
                throw new MessagingException("deletePop3Account() cannot delete intranet account");
            } else {
                dao.deleteAccount(accountId);
            }

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }


    // === [ SmtpAccount ] =====================================================
    /**
     * Retrieves the SMTP account of a user. Current version supports only
     * one SMTP account per user. An exception will be thrown if more than
     * one SMTP account is found for a user.
     *
     * @param userId
     * @return
     * @throws MessagingException
     */
    public SmtpAccount getSmtpAccountByUserId(String userId) throws MessagingException {
        MessagingDao dao;
        Collection smtpAccounts;

        try {
            dao = (MessagingDao) getDao();
            smtpAccounts = dao.selectSmtpAccounts(userId);
            if (smtpAccounts.size() == 1) {
                return (SmtpAccount) smtpAccounts.iterator().next();
            } else {
                throw new MessagingException("userId " + userId + " has " + smtpAccounts.size() + " SmtpAccounts");
            }

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public void updateSmtpAccount(SmtpAccount smtpAccount) throws MessagingException {
        MessagingDao dao;
        try {
            dao = (MessagingDao) getDao();
            smtpAccount.setPassword(Encryption.encrypt(smtpAccount.getPassword(), ENCRYPTION_KEY));
            dao.updateSmtpAccount(smtpAccount);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    // === [ Folder ] ==========================================================
    public void addFolder(Folder folder) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            dao.insertFolder(folder);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public void updateFolder(Folder folder) throws MessagingException {
        MessagingDao dao;

        try {
            // ensure special folders do not have parents
            if (folder.isSpecialFolder()) {
                folder.setParentId(null);
            }

            // update folder
            dao = (MessagingDao) getDao();
            dao.updateFolder(folder);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public void deleteFolder(String folderId) throws MessagingException {
        try {
            // retrieve folder subtree
            Folder root = getFolder(folderId);
            String userId = root.getUserId();
            root = getFolderTree(userId);
            Folder folder = findFolderInTree(folderId, root);

            // delete recursively
            deleteFolderRecursively(folder);
            return;

        } catch (MessagingException e) {
            throw e;
        }
    }

    protected Folder findFolderInTree(String folderId, Folder root) {
        Folder folder = root;
        if (!folder.getId().equals(folderId) && folder != null) {
            Collection subfolders = folder.getSubfolders();
            if (subfolders != null && subfolders.size() > 0) {
                Folder found = null;
                for(Iterator i=subfolders.iterator(); i.hasNext();) {
                    Folder subfolder = (Folder)i.next();
                    found = findFolderInTree(folderId, subfolder);
                    if (found != null) {
                        break;
                    }
                }
                return found;
            }
            return null;
        }
        else {
            return folder;
        }
    }

    protected void deleteFolderRecursively(Folder folder) throws MessagingException {
        MessagingDao dao;
        StorageService sh;
        StorageDirectory sd;
        String folderId;

        try {
            dao = (MessagingDao) getDao();
            folderId  = folder.getId();
            
            // === start modification due to added filtering func =============
            // passivate filter if folder is involved in filter's MOVE_TO_FOLDER action
            String[] filterIds = dao.findFilterWithFolderId(folderId);
            for (int a=0; a< filterIds.length; a++) {
            	log.debug("passivating filter "+filterIds[a]+" due to deletion of folder "+folderId);
            	pasivateDaoBasedFilter(filterIds[a]);
            }
            // === end modification due to added filtering func ===============
            

            // disallow deletion of special folders
            if (folder.isSpecialFolder()) {
                return;
            }

            // recurse into subfolders
            Collection subfolders = folder.getSubfolders();
            if (subfolders != null && subfolders.size() > 0) {
                for(Iterator i=subfolders.iterator(); i.hasNext();) {
                    Folder subfolder = (Folder)i.next();
                    deleteFolderRecursively(subfolder);
                }
            }

            // delete storage directory
            sh = (StorageService) Application.getInstance().getService(StorageService.class);
            sd = new StorageDirectory(ROOT_PATH + "/" + folder.getUserId() + "/" + folderId);
            sh.delete(sd);

            // delete folder
            dao.deleteFolder(folderId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public Folder getFolder(String folderId) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectFolder(folderId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public Collection getFolders(String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws MessagingException {
        MessagingDao dao;
        Collection list = new ArrayList();

        try {
            dao = (MessagingDao) getDao();
            list =  dao.selectFolders(userId, start, numOfRows, sortBy, isDescending, searchBy, searchCriteria);
            Application app = Application.getInstance();
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                Folder folder =  (Folder)iterator.next();
                if(folder.isSpecialFolder()){
                    String name = folder.getName();
                    if(name.equalsIgnoreCase(Folder.FOLDER_INBOX)){
                        folder.setName(app.getMessage("messaging.label.inbox", Folder.FOLDER_INBOX));
                    }else if(name.equalsIgnoreCase(Folder.FOLDER_DRAFT)){
                        folder.setName(app.getMessage("messaging.label.draft", Folder.FOLDER_DRAFT));
                    }else if(name.equalsIgnoreCase(Folder.FOLDER_SENT)){
                        folder.setName(app.getMessage("messaging.label.sent", Folder.FOLDER_SENT));
                    }else if(name.equalsIgnoreCase(Folder.FOLDER_OUTBOX)){
                        folder.setName(app.getMessage("messaging.label.outbox", Folder.FOLDER_OUTBOX));
                    }else if(name.equalsIgnoreCase(Folder.FOLDER_TRASH)){
                        folder.setName(app.getMessage("messaging.label.trash", Folder.FOLDER_TRASH));
                    } else if(name.equalsIgnoreCase(Folder.FOLDER_QM)){
                        folder.setName(app.getMessage("messaging.label.quickMessages", Folder.FOLDER_QM));
                    }
                }
            }

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        return list;
    }

    public int getFoldersCount(String userId, String searchBy, String searchCriteria) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectFoldersCount(userId, searchBy, searchCriteria);

        } catch (Exception e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    /**
     * @param userId specifies userId
     * @param name   specifies the special folder's name. Either INBOX, SENT,
     *               DRAFT, TRASH or QUICK MESSAGES
     * @return Folder object
     * @throws MessagingException
     */
    public Folder getSpecialFolder(String userId, String name) throws MessagingException {
        MessagingDao dao;

        if (!(Folder.FOLDER_INBOX.equals(name) || Folder.FOLDER_TRASH.equals(name)
        || Folder.FOLDER_SENT.equals(name) || Folder.FOLDER_DRAFT.equals(name)
        || Folder.FOLDER_QM.equals(name) || Folder.FOLDER_OUTBOX.equals(name))) {
            throw new MessagingException("Invalid special folder name: " + name);
        }

        try {
            dao = (MessagingDao) getDao();
            return dao.selectSpecialFolder(userId, name);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public Collection getFolders(String userId) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectFolders(userId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * @param userId
     * @param name
     * @return returns the named folder, or null if folder name does not exist
     * @throws MessagingException
     */
    public Folder getFolder(String userId, String name) throws MessagingException {
        Collection folders = getFolders(userId);

        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            Folder folder = (Folder) iterator.next();
            if (name != null && name.equals(folder.getName())) {
                return folder;
            }
        }

        return null;
    }

    /**
     * Returns a hierarchical structure for the folders belonging to a user
     *
     * @param userId Compulsory
     * @return A dummy Folder object to represent the root of the tree is returned.
     * @throws MessagingException
     */
    public Folder getFolderTree(String userId) throws MessagingException {

        Collection folders = getFolders(userId);

        Folder rootFolder = new Folder();
        rootFolder.setFolderId("ROOT");

        Collection specialFolders = new ArrayList();

        // place folders into map
        Map specialFolderMap = new HashMap();
        Map customFolderMap = new SequencedHashMap();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            Folder folder = (Folder) iterator.next();
            if (!folder.isSpecialFolder()) {
                customFolderMap.put(folder.getFolderId(), folder);
            } else {
                specialFolderMap.put(folder.getName(), folder);
            }
        }

        // add special folders
        Application app = Application.getInstance();
        Folder folder;
        folder = (Folder) specialFolderMap.get(Folder.FOLDER_INBOX);
        if (folder != null) {
            folder.setName(Folder.FOLDER_INBOX);
            folder.setDisplayName(app.getMessage("messaging.label.inbox", Folder.FOLDER_INBOX));
            specialFolders.add(folder);
        }
        folder = (Folder) specialFolderMap.get(Folder.FOLDER_DRAFT);
        if (folder != null) {
            folder.setName(Folder.FOLDER_DRAFT);
            folder.setDisplayName(app.getMessage("messaging.label.draft", Folder.FOLDER_DRAFT));
            specialFolders.add(folder);
        }
        folder = (Folder) specialFolderMap.get(Folder.FOLDER_SENT);
        if (folder != null) {
            folder.setName(Folder.FOLDER_SENT);
            folder.setDisplayName(app.getMessage("messaging.label.sent", Folder.FOLDER_SENT));
            specialFolders.add(folder);
        }
        folder = (Folder) specialFolderMap.get(Folder.FOLDER_OUTBOX);
        if (folder != null) {
            folder.setName(Folder.FOLDER_OUTBOX);
            folder.setDisplayName(app.getMessage("messaging.label.outbox", Folder.FOLDER_OUTBOX));
            specialFolders.add(folder);
        }
        folder = (Folder) specialFolderMap.get(Folder.FOLDER_TRASH);
        if (folder != null) {
            folder.setName(Folder.FOLDER_TRASH);
            folder.setDisplayName(app.getMessage("messaging.label.trash", Folder.FOLDER_TRASH));
            specialFolders.add(folder);
        }
        folder = (Folder) specialFolderMap.get(Folder.FOLDER_QM);
        if (folder != null) {
            folder.setName(Folder.FOLDER_QM);
            folder.setDisplayName(app.getMessage("messaging.label.quickMessages", Folder.FOLDER_QM));
            specialFolders.add(folder);
        }
        rootFolder.setSubfolders(specialFolders);

        // iterate thru folders to formulate the tree
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            folder = (Folder) iterator.next();
            if (!folder.isSpecialFolder()) {
                Folder parent = (Folder) customFolderMap.get(folder.getParentId());
                if (parent == null) {
                    parent = rootFolder;
                }
                Collection subfolders = parent.getSubfolders();
                if (subfolders == null) {
                    subfolders = new ArrayList();
                    parent.setSubfolders(subfolders);
                }
                subfolders.add(folder);
            }
        }

        return rootFolder;

    }

    // NOTE: no longer used since quota is delegated to a scheduled job
    /**
     * Make this method a 'low priority' method of sort. Might need to
     * optimize/tweak this method in the future.
     */
    /*private static boolean updateUserDiskQuotaBusy = false;
    public static final long QUOTA_UPDATE_DELAY = 1000 * 60;    // 60 seconds
    private static Map quotaExecutionMap;

    static {
        quotaExecutionMap = new HashMap();
    }*/

    /**
     * Update user's disk quota - with cache. This method will only execute once
     * every QUOTA_UPDATE_DELAY (ms).
     *
     * @param userId
     * @throws MessagingException
     */
    public void updateUserDiskQuota(String userId) throws MessagingException {
        /*if (lastCheckDate != null && System.currentTimeMillis() < (lastCheckDate.getTime() + QUOTA_UPDATE_DELAY)) {
            not time to update quota
            return;
        }
        if (updateUserDiskQuotaNow(userId)) {
            quotaExecutionMap.put(userId, new Date());
        }*/
        
        
        // push into Quota Update Queue
        if (userId != null) {
            QuotaUpdateJob.queueMeUp(userId);
        }
    }

    /**
     * Update user's disk quota now. This method will update user's disk quota
     * as long as the method is not 'busy' (busy = already updating quota for
     * another user).
     *
     * @param userId
     * @throws MessagingException
     */
    public void updateUserDiskQuotaNow(String userId) throws MessagingException {
        Collection folders;
        Folder folder;
        StorageService ss;
        StorageDirectory sd;
        Collection listing;
        long size;


/* uncomment this to check only when not busy
        if (updateUserDiskQuotaBusy) {
            // if method is busy, no need to process
            return false;
        }
*/

        //updateUserDiskQuotaBusy = true;
        try {
            ss = (StorageService) Application.getInstance().getService(StorageService.class);
            folders = getFolders(userId);

            for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
                // for each folder, update the quota
                size = 0;
                folder = (Folder) iterator.next();
                sd = new StorageDirectory(ROOT_PATH + "/" + userId + "/" + folder.getFolderId());

                // get size of each file in folder
                try {
                    listing = ss.get(sd);
                    for (Iterator i2 = listing.iterator(); i2.hasNext();) {
                        StorageFile sf = (StorageFile) i2.next();
                        if (!sf.isDirectory()) {
                            size += sf.getSize();
                        }
                    }

                } catch (FileNotFoundException e) {
                    // no disk space used
                }

                // update quota for folder
                folder.setDiskUsage(size);
                updateFolder(folder);
            }

        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            //updateUserDiskQuotaBusy = false;
        }
        //return true;
    }

    /**
     * Check a user's allocated messaging disk quota in kilobytes (KB).
     *
     * @param userId
     * @return quota in KB
     * @throws MessagingException
     */
    public long getUserQuota(String userId) throws MessagingException {
        String quotaStr = Application.getInstance().getProperty(APPLICATION_KEY_DEFAULT_QUOTA_KB);
        boolean useDefault = true;
        long quota = 0;

        try {
            // get user groups
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection principalIds = new ArrayList();
            Collection groups = ss.getUserGroups(userId);
            for (Iterator i = groups.iterator(); i.hasNext();) {
                Group g = (Group) i.next();
                if (g.isActive()) {
                    principalIds.add(g.getId());
                }
            }

            if (principalIds.size() > 0) {
                // get max quota from groups
                MessagingDao dao = (MessagingDao) getDao();
                String[] principalIdArray = (String[])principalIds.toArray(new String[0]);
                Long quotaLong = dao.selectMaxPrincipalQuota(principalIdArray);
                if (quotaLong != null) {
                    quota = quotaLong.longValue();
                    useDefault = false;
                }
            }

            if (useDefault) {
                // get default quota
                if(quotaStr==null) {
                    // default 100MB
                    quota = 102400;
                } else {
                    quota = Long.parseLong(quotaStr);
                }
            }

        } catch(Exception e) {
            quota = 0;
        }

        return quota;
    }

    /**
     *
     * @param name
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return Collection containing Map of groupName (String) -> quota (Long)
     * @throws MessagingException
     */
    public Collection getPrincipalQuotaList(String name, String sort, boolean desc, int start, int rows) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectPrincipalQuotaList(name, sort, desc, start, rows);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public int getPrincipalQuotaCount(String name) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectPrincipalQuotaCount(name);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public int updatePrincipalQuota(String principalId, long quota) throws MessagingException {
        MessagingDao dao;

        if (principalId == null || principalId.trim().length() == 0) {
            return 0;
        }

        try {
            dao = (MessagingDao) getDao();
            return dao.updatePrincipalQuota(principalId, quota);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public int deletePrincipalQuota(String[] principalIdArray) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.deletePrincipalQuota(principalIdArray);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * Get a user's current disk space usage for messaging module in kilobytes (KB).
     *
     * @param userId
     * @return disk space used in KB
     * @throws MessagingException
     */
    public long getCurrentDiskUsage(String userId) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectDiskUsage(userId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    /**
     * Returns user's messaging module disk usage as integer percentage value.
     *
     * @param userId
     * @return
     * @throws MessagingException
     */
    public int getUserQuotaPercent(String userId) throws MessagingException {
        long quota, current;

        quota = getUserQuota(userId);
        current = getCurrentDiskUsage(userId);

        Long percent;
        try {
            percent = new Long(100 * current / quota);
        } catch(Exception e) {
            percent = new Long(101);
        }

        return percent.intValue();
    }

    /**
     * Checks if user's messaging module disk usage has exceeded pre-allocated
     * quota.
     *
     * @param userId
     * @return true if exceeded quota, false otherwise
     * @throws MessagingException
     */
    public boolean isQuotaExceeded(String userId) throws MessagingException {
        if(getUserQuotaPercent(userId)>100) {
            return true;
        } else {
            return false;
        }
    }


    // === [ Message related use cases ] =======================================
    /**
     * Retrieves messages in a folder. Attachments are not retrieved in this
     * method call.
     *
     * @param folderId specifies the folderId
     * @return a Collection of Message objects
     * @throws MessagingException if an error occurred
     */
    public Collection getMessages(String folderId) throws MessagingException {
        MessagingDao dao;

        try {
            dao = (MessagingDao) getDao();
            return dao.selectMessages(folderId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public Message getNextMessage(Date date, String folderId) throws MessagingDaoException {
        MessagingDao dao;
        dao = (MessagingDao) getDao();
        return dao.selectNextMessage(date, folderId);
    }

    public Message getPreviousMessage(Date date, String folderId) throws MessagingDaoException {
        MessagingDao dao;
        dao = (MessagingDao) getDao();
        return dao.selectPreviousMessage(date, folderId);
    }


    public Collection getMessages(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws MessagingException {
        MessagingDao dao;

        dao = (MessagingDao) getDao();
        try {
            return dao.selectMessages(query, start, maxResults, sort, descending);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public int getMessagesCount(DaoQuery query) throws MessagingException {
        MessagingDao dao = (MessagingDao) getDao();
        int numOfMessage = 0;

        try {
            numOfMessage = dao.selectMessagesCount(query);
            return numOfMessage;

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves a message with attachments. This method call will parse the MIME message.
     *
     * @param messageId
     * @return
     * @throws MessagingException
     */
    public Message getMessageByMessageId(String messageId) throws MessagingException {
        return getMessageByMessageId(messageId, true);
    }

    /**
     * Retrieves a message. This method call will parse the MIME message.
     *
     * @param messageId
     * @param loadAttachments true to load attachments
     * @return
     * @throws MessagingException
     */
    public Message getMessageByMessageId(String messageId, boolean loadAttachments) throws MessagingException {
        MessagingDao dao;
        MimeMessage mimeMessage;
        StorageService ss;
        StorageFile sf;
        Message message;
        InputStream in = null;

        try {
            dao = (MessagingDao) getDao();
            message = dao.selectMessage(messageId);

            try {
                ss = (StorageService) Application.getInstance().getService(StorageService.class);
                sf = new StorageFile(message.getStorageFilename());
                sf = ss.get(sf);

                in = sf.getInputStream();
                mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()), in);

                if (loadAttachments) {
                    // load message body from mime message
                    try
                    {
                        populateMessageFromMimeMessage(mimeMessage, message);
                    }
                    catch(Exception e)
                    {
                        message.setBody(e.toString());
                    }
                    // load the attachments (get the filename and inputstream ready)
                    message.setStorageFileList(parseMimeAttachments(mimeMessage));
                }

            } catch (FileNotFoundException e) {
                // just ignore - just return message without its attachments
                log.debug("Storage file (.eml) attached to this message is not found " + message.getId(), e);
            }

            return message;

        } catch (javax.mail.MessagingException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
	 * Created by Arun on july 04, 2006.
	 * 
	 * 
	 * @param messageId
	 * 
	 * @return a String with content type of the message.
	 */
	public String getContentType(String messageId) throws MessagingException {
		MessagingDao dao;
		MimeMessage mimeMessage;
		StorageService ss;
		StorageFile sf;
		Message message;
		InputStream in = null;
		String contentType="";
		try {
			dao = (MessagingDao) getDao();
			message = dao.selectMessage(messageId);

			try {
				ss = (StorageService) Application.getInstance().getService(
						StorageService.class);
				sf = new StorageFile(message.getStorageFilename());
				sf = ss.get(sf);

				in = sf.getInputStream();
				mimeMessage = new MimeMessage(Session
						.getDefaultInstance(new Properties()), in);
				mimeMessage.getContentType();
				contentType=mimeMessage.getContentType();
				contentType=contentType+mimeMessage.getFileName();
				
				if(message.getAttachmentCount()>0){
                    if(mimeMessage.getContent() instanceof Multipart)
                    {
                        Multipart multipart = (Multipart)mimeMessage.getContent();
                        for (int i=0; i<multipart.getCount(); i++) {
                            Part part = multipart.getBodyPart(i);
                            contentType=contentType +"<br>"+ part.getContentType();
                        }
                    }
				}	
			} catch (FileNotFoundException e) {
				// just ignore - just return message without its attachments
				log.debug(
						"Storage file (.eml) attached to this message is not found "
								+ message.getId(), e);
			}
			return contentType;

		} catch (javax.mail.MessagingException e) {
			throw new MessagingException(e.getMessage(), e);
		} catch (MessagingDaoException e) {
			throw new MessagingException(e.getMessage(), e);
		} catch (StorageException e) {
			throw new MessagingException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MessagingException(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	
	public String parseICSFile(String messageId,Message message,String userId) throws MessagingException,IOException,ConflictException,CalendarException, SecurityException  {
			ICalendarParser ics=new ICalendarParser();
		try{	
			CalendarEvent event= ics.getICalendarFile(messageId,userId);
			CalendarModule handler = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
			if(ics.getMethod().trim().equals("REQUEST")){
			try{
				CalendarEvent checkEvent=handler.getCalendarEventByExternalEventId(event.getExternalEventId());
				event.setEventId(checkEvent.getEventId());
				handler.updateCalendarEvent(event, userId, true);
			return "Calendar Updated";
			}catch(Exception e){
				 handler.addCalendarEvent("com.tms.collab.calendar.model.Appointment", event, userId, true);
				 return "Calendar Updated";
			}
		}else if(ics.getMethod().trim().equalsIgnoreCase("REPLY")){
			try{
			event.setEventId(event.getExternalEventId());
			for (Iterator i=event.getAttendees().iterator(); i.hasNext();) {
					try{
						Attendee att = (Attendee)i.next();
                        String status=att.getStatus();
                        if(status==null || status.equals("")){
                            status=CalendarModule.ATTENDEE_STATUS_PENDING;
                        }
                        handler.updateAttendeeStatus(event.getEventId(),(String)att.getProperty("email"),status,"");
					}catch (Exception e){
					}
				}
			return "Attendee status updated";
			}catch (Exception e){
				Log.getLog(getClass()).error(e);
				return "Unable to update the Attendee status";
			}
		}else{
			return "Invalid calendar file";
		}
		}catch(Exception e){
			 Log.getLog(getClass()).error(e);
		return "Error updating calendar";	 
		}
	}
	
	public void checkICSFile(Message message) throws MessagingException  {
		
		String contentType =getContentType(message.getMessageId());
		contentType= contentType.toLowerCase();
		if(contentType.indexOf("text/calendar")>-1||contentType.indexOf("ics")>-1||contentType.indexOf("application/ics")>-1){
			message.setIcsContentType(contentType);
			message.setICSAttachment(true);
		}else{
			message.setICSAttachment(false);
		}
	}
	
    /**
     * Creates a Message object that represents a 'Reply To' message of a
     * given messageId.
     *
     * @param messageId specifies the message to reply to
     * @return a Message object
     */
    public Message getReplyMessage(Event event, String messageId) throws MessagingException {
        Message message, replyMessage;
        InternetAddress address;

        message = getMessageByMessageId(messageId);
        replyMessage = new Message();

        // set attachments
        replyMessage.setStorageFileList(message.getStorageFileList());

        try {
            replyMessage.setMessageId(UuidGenerator.getInstance().getUuid());
            address = new InternetAddress(message.getFrom());

            if (address.getAddress().endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN)) {
                replyMessage.setToIntranetList(Util.convertStringToIntranetRecipientsList(message.getFrom()));
            } else {
                replyMessage.setToList(Util.convertStringToInternetRecipientsList(message.getFrom()));
            }

            setMessageBody(event, message, replyMessage);

            replyMessage.setSubject("RE: " + message.getSubject());

            return replyMessage;
        } catch (AddressException e) {
            throw new MessagingException(e.getMessage(), e);
        }

    }

    protected void setMessageBody(Event event, Message sourceMessage, Message newMessage) {

        // get signature
        String signature = "";
        try {
            IntranetAccount intranetAccount;
            User user;

            user = Util.getUser(event);
            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
            signature = intranetAccount.getSignature() == null ? "" : intranetAccount.getSignature().trim();
        }
        catch (MessagingException e) {
            log.error("Error retrieving signature", e);
        }

        if (sourceMessage.getMessageFormat() == Message.MESSAGE_FORMAT_HTML) {
            if (Util.isRichTextCapable(event.getRequest())) {
                // message is HTML, ok
                newMessage.setMessageFormat(Message.MESSAGE_FORMAT_HTML);

                String body = "<br><BLOCKQUOTE style=\"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #1010ff 2px solid\"><hr>" + getResponseHeader(sourceMessage) + sourceMessage.getBody() + "</blockquote>";
                if (signature.length() > 0) {
                    body = "<br><pre>" + signature + "</pre><br>" + body;
                }
                newMessage.setBody(body);
            } else {
                // convert to text
                newMessage.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                try {
                    String body = "\n\r\n\r" + sourceMessage.getFrom() + " wrote: \n\r" + Util.html2Text(sourceMessage.getBody());
                    if (signature.length() > 0) {
                        body = "\n\n" + signature + body;
                    }
                    newMessage.setBody(body);
                } catch (IOException e) {
                    log.error("Error converting HTML email to text", e);
                    String body = "\n\r\n\r" + sourceMessage.getFrom() + " wrote: \n\r" + sourceMessage.getBody();
                    if (signature.length() > 0) {
                        body = "\n\n" + signature + body;
                    }
                    newMessage.setBody(body);
                }
            }
        } else {
            if (Util.isRichTextCapable(event.getRequest())) {
                // convert text to HTML
                String body = "<br><BLOCKQUOTE style=\"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #1010ff 2px solid\"><hr>" + getResponseHeader(sourceMessage) + Util.plainTextToHtml(sourceMessage.getBody()) + "</blockquote>";
                if (signature.length() > 0) {
                    body = "<br><pre>" + signature + "</pre><br>" + body;
                }
                newMessage.setBody(body);
            } else {
                // text is ok
                newMessage.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                String body = "\n\r\n\r" + sourceMessage.getFrom() + " wrote: \n\r" + sourceMessage.getBody();
                if (signature.length() > 0) {
                    body = "\n\n" + signature + body;
                }
                newMessage.setBody(body);
            }
        }
    }

    protected String getResponseHeader(Message message) {
        SimpleDateFormat sdf = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
        Application app = Application.getInstance();
        StringBuffer sb = new StringBuffer();

        sb.append("<div><b>" + app.getMessage("messaging.message.from") + ":</b> ");




        //do filtering of email to create hyberlink for "from"  ex.   name < email@address >  -->  hyperlink  START
        String email ="";
        String name ="";
        String createHyperLink ="<A href=\"mailto:";
        try{
           int startPoint = message.getFrom().indexOf("<",0);
           int endPoint = message.getFrom().indexOf(">", startPoint+1);

                if(!(startPoint < 0 && endPoint < 0))
                {email =  message.getFrom().substring(startPoint+1,endPoint).trim();
                 name =  message.getFrom().substring(0,startPoint).trim();


                 createHyperLink +=email+"\">"+name+"</A>";


                 sb.append(createHyperLink + "<br>");
                 //sb.append(message.getFrom() + "<br>");           <original>
                }
                else
                 sb.append(message.getFrom() + "<br>");

        }
        catch(Exception e){

              log.error("Filtering of from-email to create hyperlink got problem, invalid input", e);

        }

         //do filtering of email to create hyberlink for "from"  ex.   name < email@address >  -->  hyperlink  END












        sb.append("<b>" + app.getMessage("messaging.label.sent") + ":</b> ");
        sb.append(sdf.format(message.getDate()) + "<br>");

        List list = new ArrayList();
        if(message.getToList().size()>0 || message.getToIntranetList().size() > 0) {
            sb.append("<b>" + app.getMessage("messaging.message.to") + ":</b> ");
            list.addAll(message.getToIntranetList());
            list.addAll(message.getToList());
            sb.append(list.toString() + "<br>");
        }

        list.clear();
        if(message.getCcList().size()>0 || message.getCcIntranetList().size() > 0) {
            sb.append("<b>" + app.getMessage("messaging.message.cc") + ":</b> ");
            list.addAll(message.getCcIntranetList());
            list.addAll(message.getCcList());
            sb.append(list.toString() + "<br>");
        }

        sb.append("<b>" + app.getMessage("messaging.message.subject") + ":</b> ");
        sb.append(message.getTruncatedSubject() + "<br>");

        sb.append("</div>");

        return sb.toString();
    }

    /**
     * Creates a Message object that represents a 'Reply All To' message of a
     * given messageId.
     *
     * @param messageId
     * @return
     */
    public Message getReplyAllMessage(Event event, String messageId, String userId) throws MessagingException {
        Message replyAllMessage, original;
        InternetAddress address;

        original = getMessageByMessageId(messageId);
        replyAllMessage = getReplyMessage(event, messageId);

        // clear TO, CC
        replyAllMessage.setToList(new ArrayList());
        replyAllMessage.setToIntranetList(new ArrayList());
        replyAllMessage.setCcList(new ArrayList());
        replyAllMessage.setCcIntranetList(new ArrayList());

        // reply from email
        try {
            address = new InternetAddress(original.getFrom());

            if (address.getAddress().endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN)) {
                replyAllMessage.setToIntranetList(Util.convertStringToIntranetRecipientsList(original.getFrom()));
            } else {
                replyAllMessage.setToList(Util.convertStringToInternetRecipientsList(original.getFrom()));
            }

        } catch (AddressException e) {
            throw new MessagingException(e.getMessage(), e);
        }

        // reply all intranet emails
        replyAllMessage.getToIntranetList().addAll(original.getToIntranetList());
        replyAllMessage.getCcIntranetList().addAll(original.getCcIntranetList());

        // reply all Internet emails
        replyAllMessage.getToList().addAll(original.getToList());
        replyAllMessage.getCcList().addAll(original.getCcList());

        // remove sender address in To / CC fields
        try {
            IntranetAccount intranetAccount = getIntranetAccountByUserId(userId);
            String intranetAddr = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
            String internetEmail = intranetAccount.getFromAddress();

            replyAllMessage.getToIntranetList().remove(intranetAddr);
            replyAllMessage.getCcIntranetList().remove(intranetAddr);

            for (Iterator i = replyAllMessage.getToList().iterator(); i.hasNext();) {
                String addr = (String) i.next();
                try {
                    InternetAddress ia = new InternetAddress(addr);
                    if (internetEmail.equals(ia.getAddress())) {
                        i.remove();
                    }
                } catch (AddressException e) {
                    // ignore
                }
            }
            for (Iterator i = replyAllMessage.getCcList().iterator(); i.hasNext();) {
                String addr = (String) i.next();
                try {
                    InternetAddress ia = new InternetAddress(addr);
                    if (internetEmail.equals(ia.getAddress())) {
                        i.remove();
                    }
                } catch (AddressException e) {
                    // ignore
                }
            }
        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        return replyAllMessage;
    }

    /**
     * Creates a Message object that represents a 'Forward' message of a
     * given messageId.
     *
     * @param messageId
     * @return
     */
    public Message getForwardMessage(Event event, String messageId) throws MessagingException {
        Message message, forwardMessage;

        message = getMessageByMessageId(messageId);
        forwardMessage = new Message();

        // set attachments
        forwardMessage.setStorageFileList(message.getStorageFileList());

        // set a new messageId
        forwardMessage.setMessageId(UuidGenerator.getInstance().getUuid());

        // set the message body
        setMessageBody(event, message, forwardMessage);

        // set message subject
        forwardMessage.setSubject("FW: " + message.getSubject());

        return forwardMessage;
    }

    /**
     * Saves a message to the special DRAFT folder of a given user.
     *
     * @param message specifies the message to save to draft
     * @param userId  specifies the userId of the draft folder
     * @throws MessagingException if an error occurred
     */
    public void saveDraftMessage(Message message, String userId, HttpSession session) throws MessagingException {
        Folder draftFolder;
        MimeMessage mimeMessage;
        IntranetAccount intranetAccount;
        SmtpAccount smtpAccount;

        try {
            intranetAccount = getIntranetAccountByUserId(userId);
            message.setAccountId(intranetAccount.getAccountId());
            draftFolder = getSpecialFolder(userId, Folder.FOLDER_DRAFT);
            smtpAccount = getSmtpAccountByUserId(userId);
            mimeMessage = makeMimeMessage(message, smtpAccount);
            copyToFolder(message, mimeMessage, userId, draftFolder);

            deleteUserTempFolder(userId, session);

        } catch (IOException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (AddressException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (javax.mail.MessagingException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            updateUserDiskQuota(userId);
        }
    }

    public void downloadPop3Messages(String userId) {
        MessagingQueue queue = MessagingQueue.getInstance();

        QueueItem item = new QueueItem();
        item.setUserId(userId);

        queue.pushDownloadPop3(item);


        try {
            MessagingUserStatus mus = queue.getUserStatus(userId);
            mus.getTrackerDownload().update(ProgressTracker.STATUS_PENDING, 0, Application.getInstance().getMessage("messaging.label.statusWaiting", "Waiting to download emails..."));
        } catch (MessagingException e) {
            // ignore
        }

        return;
    }

    /**
     * Download new emails from POP3 account.
     */
    public int downloadPop3MessagesNow(String userId, Pop3Account pop3Account, final ProgressTracker tracker) throws MessagingException {
        Session session;
        Store store = null;
        javax.mail.Folder folder = null;
        javax.mail.Message[] messages;
        Collection oldMessageIds;
        MessagingDao dao = (MessagingDao) getDao();
        Message myMessage;
        UuidGenerator uuidGenerator;
        int emailCount = 0;
        SmtpAccount smtpAccount;
        int percent;

        try {
            final Application app = Application.getInstance();
            uuidGenerator = UuidGenerator.getInstance();

            log.debug("DownloadPop3MessagesNow(x,x,x): Processing account " + pop3Account.getName() + " for user " + userId);

            smtpAccount = getSmtpAccountByUserId(pop3Account.getUserId());
            tracker.update(ProgressTracker.STATUS_PROCESSING, 1, app.getMessage("messaging.label.statusConnectingTo", "Connecting to {0}", new String[]{pop3Account.getServerName()}));
            session = makeSession(smtpAccount);

            log.debug("DownloadPop3MessagesNow(x,x,x): Connecting to account " + pop3Account.getName() + " for user " + userId);

            store = session.getStore("pop3");
            store.connect(pop3Account.getServerName(), pop3Account.getUsername(), pop3Account.getPassword());
            tracker.update(ProgressTracker.STATUS_PROCESSING, 2, app.getMessage("messaging.label.statusConnectedTo", "Connected to {0}", new String[]{pop3Account.getServerName()}));

            log.debug("DownloadPop3MessagesNow(x,x,x): Connected to account " + pop3Account.getName() + " for user " + userId);

            // get a list of Message-ID in database
            log.debug("DownloadPop3MessagesNow(x,x,x): Selecting to headers for account " + pop3Account.getName() + " for user " + userId);
            //oldMessageIds = dao.selectMessageIdHeaders(pop3Account.getAccountId());

            log.debug("DownloadPop3MessagesNow(x,x,x): Opening inbox for mail write for account " + pop3Account.getName() + " for user " + userId);
            folder = store.getFolder("INBOX");
            folder.open(javax.mail.Folder.READ_WRITE);

            messages = folder.getMessages();
            tracker.update(ProgressTracker.STATUS_PROCESSING, 5, app.getMessage("messaging.label.statusDownloadingMessages", "Downloading {0} messages on {1}", new Object[]{new Integer(messages.length), pop3Account.getServerName()}));
            log.debug("DownloadPop3MessagesNow(x,x,x): Downloading " + messages.length + " messages from server");

            // process each message in the POP3's INBOX (after above filtering)
            log.debug("DownloadPop3MessagesNow(x,x,x): Start filtering pop3mail");
            for (int c = 0; c < messages.length; c++) {
                try {
                    oldMessageIds = dao.selectMessageIdHeaders(pop3Account.getAccountId(), ((MimeMessage) messages[c]).getMessageID());
                    if(oldMessageIds.size() > 0) {
                        percent = 6 + (c * 100 / messages.length);
                        percent = percent > 99 ? 99 : percent;
                        tracker.update(ProgressTracker.STATUS_PROCESSING, percent, app.getMessage("messaging.label.statusDownloadingOldMessage", "Downloading {0} of {1} messages on {2} (old)", new Object[]{new Integer(c + 1), new Integer(messages.length), pop3Account.getServerName()}));
                        log.debug("DownloadPop3MessagesNow(x,x,x): Skipping downloaded message " + (c + 1) + ": " + messages[c].getSubject());
                    } else {
                        percent = 6 + (c * 100 / messages.length);
                        percent = percent > 99 ? 99 : percent;
                        tracker.update(ProgressTracker.STATUS_PROCESSING, percent, app.getMessage("messaging.label.statusDownloadingMessage", "Downloading {0} of {1} messages on {2}", new Object[]{new Integer(c + 1), new Integer(messages.length), pop3Account.getServerName()}));
                        log.debug("DownloadPop3MessagesNow(x,x,x): Processing message " + (c + 1) + ": " + messages[c].getSubject());

                        myMessage = new Message();
                        myMessage.setMessageId(uuidGenerator.getUuid());
                        myMessage.setFolderId(pop3Account.getDeliveryFolderId());
                        myMessage.setRead(false);
                        myMessage.setAccountId(pop3Account.getAccountId());
                        myMessage.setAttachmentCount(0);
                        myMessage.setStorageFilename(ROOT_PATH + "/" + pop3Account.getUserId() + "/" + pop3Account.getDeliveryFolderId() + "/" + myMessage.getMessageId() + ".eml");
                        


                        saveMessageToStorage(messages[c], myMessage);
                        saveMessageToDao(messages[c], myMessage);


                        // start filtering ==================================
                        FilterManager fm = getFilterManager();
                        fm.filter(
                            new Message[] { myMessage },
                            new Object[] { userId, pop3Account }
                        );
                        // end filtering ====================================


                        //msgs.add(myMessage);
                        emailCount++;
                    }

                    // delete message?
                    if (!pop3Account.isLeaveMailOnServer()) {
                        messages[c].setFlag(Flags.Flag.DELETED, true);
                    }
                } catch (javax.mail.MessagingException e) {
                    log.error("Error downloading email message " + ((MimeMessage)messages[c]).getMessageID());
                } catch (UnsupportedEncodingException e) {
                    log.error("Error downloading email message " + ((MimeMessage)messages[c]).getMessageID());
                } catch (UnknownHostException e) {
                    throw e;
                } catch (IOException e) {
                    log.error("Error downloading email message " + ((MimeMessage)messages[c]).getMessageID());
                }
            }
            tracker.update(ProgressTracker.STATUS_PROCESSING, 99, app.getMessage("messaging.label.statusClosingConnection", "Closing connection to {0}", new String[]{pop3Account.getServerName()}));
            log.debug("DownloadPop3MessagesNow(x,x,x): Finish filtering pop3mail");
            
            
            // delete messages?
            if (!pop3Account.isLeaveMailOnServer()) {
                folder.close(true);
            } else {
                folder.close(false);
            }
            return emailCount;

            

        } catch (javax.mail.MessagingException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (UnknownHostException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            // make sure mailbox connection is closed
            if (store != null) {
                try {
                    if (folder != null && folder.isOpen()) {
                        folder.close(true);
                    }
                    store.close();
                } catch (javax.mail.MessagingException e) {
                    throw new MessagingException(e.getMessage(), e);
                }
            }
            log.debug("DownloadPop3MessagesNow(x,x,x): Downloading of " + emailCount + " messages completed");
            tracker.update(ProgressTracker.STATUS_COMPLETED, 100, Application.getInstance().getMessage("messaging.label.statusCompletedDownloading", "Completed downloading {0} messages on {1}", new Object[]{new Integer(emailCount), pop3Account.getServerName()}));
        }

    }

    public void downloadPop3MessagesNow(String userId) throws MessagingException {
        Collection accounts;
        MessagingQueue mq;
        MessagingUserStatus mus;
        int count;

        Log.getLog(MessagingModule.class).debug("=========== DownloadPop3MessagesNow(x): Begin execution for user " + userId + " ===========");

        mq = MessagingQueue.getInstance();
        mus = mq.getUserStatus(userId);

        if (mus.isPop3Busy()) {
            // no need to update, POP3 is busy
            if (!mus.isDownloadBusy()) {
                mus.getTrackerDownload().update(ProgressTracker.STATUS_ERROR, 0, Application.getInstance().getMessage("messaging.label.statusBusy", "Your email account(s) is busy"));
                Log.getLog(MessagingModule.class).debug("DownloadPop3MessagesNow(x): Email account busy for user " + userId);
            }
            return;
        }

        // run through each POP3 account and download emails
        try {
            mus.getTrackerDownload().update(ProgressTracker.STATUS_PROCESSING, 0, Application.getInstance().getMessage("messaging.label.statusPreparingDownload", "Preparing to download emails..."));
            accounts = getAccounts(userId);
            for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = (Account) iterator.next();
                if (account instanceof Pop3Account) {
                    Pop3Account pop3Account = (Pop3Account) account;
                    log.debug("DownloadPop3MessagesNow(x): Processing account " + account.getName());
                    try {
                        count = downloadPop3MessagesNow(userId, pop3Account, mus.getTrackerDownload());

                        // update information to Pop3Account
                        Date now = Calendar.getInstance().getTime();
                        pop3Account = (Pop3Account) getAccount(pop3Account.getId());
                        pop3Account.setDownloadCount(count);
                        pop3Account.setLastDownloadDate(now);
                        pop3Account.setCheckCount(0);
                        pop3Account.setLastCheckDate(now);
                        if (pop3Account.getAccountLog() != null && pop3Account.getAccountLog().length() > (POP_ACCOUNT_LOG_SIZE)) {
                            pop3Account.setAccountLog(now + " : " + " Cleared POP3 account log\n" + now + " : Downloaded POP3 account with " + count + " emails\n");
                        } else {
                            pop3Account.setAccountLog(pop3Account.getAccountLog() + now + " : Downloaded POP3 account with " + count + " emails\n");
                        }
                        updateAccount(pop3Account);

                    } catch (MessagingException e) {
                        Date now = Calendar.getInstance().getTime();
                        StringBuffer sb = new StringBuffer();
                        StringWriter sw = new StringWriter();

                        // error in transport - POP3
                        Log.getLog(DownloadPop3Job.class).error("Error downloading POP3 account for " + pop3Account.getUsername() + " on " + pop3Account.getServerName(), e);

                        if (pop3Account.getAccountLog() != null && pop3Account.getAccountLog().length() > (POP_ACCOUNT_LOG_SIZE)) {
                            // if account log greater than 10K, clear it automatically
                            sb.append(now + " : " + " Cleared POP3 account log\n");
                        }
                        else {
                            sb.append(pop3Account.getAccountLog());
                        }

                        sb.append(now + " : Error downloading POP3 account for " + pop3Account.getUsername() + " on " + pop3Account.getServerName() + "\n");
                        e.printStackTrace(new PrintWriter(sw));
                        sb.append(sw.toString());
                        sb.append("\n==========\n\n");

                        try {
                            pop3Account = (Pop3Account) getAccount(pop3Account.getId());
                            pop3Account.setAccountLog(sb.toString());
                            updateAccount(pop3Account);
                        } catch (MessagingException e1) {
                            Log.getLog(DownloadPop3Job.class).error("Error updating POP3 account", e);
                        }
                    }
                }
            }
        } finally {
            log.debug("DownloadPop3MessagesNow(x): Updating disk quota for user " + userId);
            updateUserDiskQuota(userId);
            log.debug("DownloadPop3MessagesNow(x): Completed updating disk quota for user " + userId);
            if (ProgressTracker.STATUS_PROCESSING.equals(mus.getTrackerDownload().getStatus())) {
                mus.getTrackerDownload().update(ProgressTracker.STATUS_ERROR, 100, Application.getInstance().getMessage("messaging.label.statusUnexpectedTermination", "Unexpected termination of download"));
            }
        }

        Log.getLog(MessagingModule.class).debug("=========== DownloadPop3MessagesNow(x): End execution for user " + userId + " ===========");

    }

    /**
     * Check user's POP3 accounts for new emails. Request will be queued.
     */
    public void checkPop3Messages(String userId) throws MessagingException {
        MessagingQueue queue = MessagingQueue.getInstance();

        QueueItem item;
        item = new QueueItem();
        item.setUserId(userId);

        queue.pushCheckPop3(item);

        return;
    }

    /**
     * Checks POP3 account for new emails. Returns number of emails in the
     * account.
     */
    protected int checkPop3MessagesNow(Pop3Account pop3Account, ProgressTracker tracker) throws MessagingException {
        Session session;
        Store store = null;
        javax.mail.Folder folder = null;
        javax.mail.Message[] messages;
        Collection oldMessageIds;
        MessagingDao dao = (MessagingDao) getDao();
        int emailCount = 0;
        SmtpAccount smtpAccount;
        int percent = 0;

        try {
            Application app = Application.getInstance();
            smtpAccount = getSmtpAccountByUserId(pop3Account.getUserId());
            tracker.update(ProgressTracker.STATUS_PROCESSING, 1, app.getMessage("messaging.label.statusConnectingTo", "Connecting to {0}", new String[]{pop3Account.getServerName()}));
            session = makeSession(smtpAccount);

            store = session.getStore("pop3");
            store.connect(pop3Account.getServerName(), pop3Account.getUsername(), pop3Account.getPassword());
            tracker.update(ProgressTracker.STATUS_PROCESSING, 2, app.getMessage("messaging.label.statusConnectedTo", "Connected to {0}", new String[]{pop3Account.getServerName()}));

            // get a list of Message-ID in database
            //oldMessageIds = dao.selectMessageIdHeaders(pop3Account.getAccountId());

            folder = store.getFolder("INBOX");
            folder.open(javax.mail.Folder.READ_WRITE);

            messages = folder.getMessages();
            tracker.update(ProgressTracker.STATUS_PROCESSING, 5, app.getMessage("messaging.label.statusProcessingMessages", "Processing {0} messages on {1}", new Object[]{new Integer(messages.length), pop3Account.getServerName()}));
            log.debug("Checking " + messages.length + " messages from server");

            // process each message in the POP3's INBOX
            for (int c = 0; c < messages.length; c++) {
                oldMessageIds = dao.selectMessageIdHeaders(pop3Account.getAccountId(), ((MimeMessage) messages[c]).getMessageID());
                if (oldMessageIds.size() > 0) {
                    percent = 6 + (c * 100 / messages.length);
                    percent = percent > 99 ? 99 : percent;
                    tracker.update(ProgressTracker.STATUS_PROCESSING, percent, app.getMessage("messaging.label.statusProcessingOldMessage", "Processing {0} of {1} messages on {2} (old)", new Object[]{new Integer(c + 1), new Integer(messages.length), pop3Account.getServerName()}));
                    log.debug("Skipping downloaded message " + (c + 1) + ": " + messages[c].getSubject());
                } else {
                    // FOUND! but no need to download, just checking.
                    percent = 6 + (c * 100 / messages.length);
                    percent = percent > 99 ? 99 : percent;
                    tracker.update(ProgressTracker.STATUS_PROCESSING, percent, app.getMessage("messaging.label.statusProcessingMessage", "Processing {0} of {1} messages on {2}", new Object[]{new Integer(c + 1), new Integer(messages.length), pop3Account.getServerName()}));
                    log.debug("Processing message " + (c + 1) + ": " + messages[c].getSubject());
                    emailCount++;
                }

                // delete message?
                // just checking, no need to download the messages
            }

            tracker.update(ProgressTracker.STATUS_PROCESSING, 99, app.getMessage("messaging.label.statusClosingConnection", "Closing connection to {0}", new String[]{pop3Account.getServerName()}));

            // do not commit changes?
            folder.close(false);
            store.close();
            log.debug("Checking of messages completed");

            return emailCount;

        } catch (javax.mail.MessagingException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            // make sure mailbox connection is closed
            if (store != null) {
                try {
                    if (folder != null && folder.isOpen()) {
                        folder.close(false);
                    }
                    store.close();
                } catch (javax.mail.MessagingException e) {
                    throw new MessagingException(e.getMessage(), e);
                }
            }
            tracker.update(ProgressTracker.STATUS_COMPLETED, 100, Application.getInstance().getMessage("messaging.label.statusCompletedProcessing", "Completed processing messages on {0}", new Object[]{pop3Account.getServerName()}));
        }

    }

    public void checkPop3MessagesNow(String userId) throws MessagingException {
        Collection accounts;
        MessagingQueue mq;
        MessagingUserStatus mus;
        int count;

        mq = MessagingQueue.getInstance();
        mus = mq.getUserStatus(userId);

        if (mus.isPop3Busy()) {
            // no need to update, POP3 is busy
            if (!mus.isCheckBusy()) {
                mus.getTrackerCheck().update(ProgressTracker.STATUS_ERROR, 0, Application.getInstance().getMessage("messaging.label.statusBusy", "Your email account(s) is busy"));
            }
            return;
        }

        // run through each POP3 account and check no. of mails
        try {
            mus.getTrackerCheck().update(ProgressTracker.STATUS_PROCESSING, 0, Application.getInstance().getMessage("messaging.label.statusPreparingCheck", "Preparing to check for new emails..."));
            accounts = getAccounts(userId);
            for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = (Account) iterator.next();

                if (account instanceof Pop3Account) {
                    Pop3Account pop3Account = (Pop3Account) account;

                    try {
                        count = checkPop3MessagesNow(pop3Account, mus.getTrackerCheck());

                        // update information to Pop3Account
                        Date now = Calendar.getInstance().getTime();
                        pop3Account = (Pop3Account) getAccount(pop3Account.getId());
                        pop3Account.setCheckCount(count);
                        pop3Account.setLastCheckDate(now);
                        if (pop3Account.getAccountLog() != null && pop3Account.getAccountLog().length() > (POP_ACCOUNT_LOG_SIZE)) {
                            pop3Account.setAccountLog(now + " : " + " Cleared POP3 account log\n" + now + " : Checked POP3 account with " + count + " emails\n");
                        } else {
                            pop3Account.setAccountLog(pop3Account.getAccountLog() + now + " : Checked POP3 account with " + count + " emails\n");
                        }
                        updateAccount(pop3Account);

                    } catch (MessagingException e) {
                        Date now = Calendar.getInstance().getTime();
                        StringBuffer sb = new StringBuffer();
                        StringWriter sw = new StringWriter();

                        // error in transport - POP3
                        Log.getLog(MessagingModule.class).debug("Error checking POP3 account for " + pop3Account.getUsername() + " on " + pop3Account.getServerName(), e);

                        if (pop3Account.getAccountLog() != null && pop3Account.getAccountLog().length() > (POP_ACCOUNT_LOG_SIZE)) {
                            // if account log greater than 10K, clear it automatically
                            sb.append(now + " : " + " Cleared POP3 account log\n");
                        }
                        else {
                            sb.append(pop3Account.getAccountLog());
                        }

                        sb.append(now + " : Error checking POP3 account for " + pop3Account.getUsername() + " on " + pop3Account.getServerName() + "\n");
                        e.printStackTrace(new PrintWriter(sw));
                        sb.append(sw.toString());
                        sb.append("\n==========\n\n");

                        try {
                            pop3Account = (Pop3Account) getAccount(pop3Account.getId());
                            pop3Account.setAccountLog(sb.toString());
                            updateAccount(pop3Account);
                        } catch (MessagingException e1) {
                            Log.getLog(MessagingModule.class).error("Error updating POP3 account", e);
                        }

                    }

                }
            }
        } finally {
            if (ProgressTracker.STATUS_PROCESSING.equals(mus.getTrackerCheck().getStatus())) {
                mus.getTrackerCheck().update(ProgressTracker.STATUS_ERROR, 100, Application.getInstance().getMessage("messaging.label.statusUnexpectedTermination", "Unexpected termination of download"));
            }
        }
    }

    /**
     * Sends a message. This will send to both Internet and intranet recipients.
     *
     * @param smtpAccount specifies the SMTP account used to send Internet
     *                    emails
     * @param message     specifies the message to send
     * @throws MessagingException if an exception occurred
     */
    public void sendMessage(SmtpAccount smtpAccount, Message message) throws MessagingException {
        sendMessage(smtpAccount, message, null);
    }

    /**
     * Sends a message. From address will reflect accurately depending on whether
     * the recipient is intranet or Internet user. This method does not copy
     * to SENT folder.
     *
     * @param smtpAccount
     * @param message
     * @param userId
     * @throws MessagingException
     */
    public void sendMessage(SmtpAccount smtpAccount, Message message, String userId) throws MessagingException {
        sendMessage(smtpAccount, message, userId, false, null);
    }

    /**
     * Sends a message. From address will reflect accurately depending on whether
     * the recipient is intranet or Internet user. This method has the option
     * to make a copy to the SENT folder.
     *
     * @param smtpAccount specifies the SMTP account to use when sending
     *                    Internet emails
     * @param message     specifies the message to sent
     * @param userId      specifies the userId that is sending this message. The
     *                    FROM/REPLY TO field will reflect this user's identity.
     * @param copyToSent  true to make a copy to the SENT folder, false otherwise
     * @throws MessagingException if an error occurred
     */
    public void sendMessage(SmtpAccount smtpAccount, Message message,
                            String userId, boolean copyToSent) throws MessagingException {
        sendMessage(smtpAccount, message, userId, false, null);
    }

    /**
     * Sends a message. From address will reflect accurately depending on whether
     * the recipient is intranet or Internet user. This method has the option
     * to make a copy to the SENT folder.
     *
     * @param smtpAccount specifies the SMTP account to use when sending
     *                    Internet emails
     * @param message     specifies the message to sent
     * @param userId      specifies the userId that is sending this message. The
     *                    FROM/REPLY TO field will reflect this user's identity.
     * @param copyToSent  true to make a copy to the SENT folder, false otherwise
     * @param session     HttpSession is required to clear attachment map, specify
     *                    null to ignore this
     * @throws MessagingException if an error occurred
     */
    public void sendMessage(SmtpAccount smtpAccount, Message message,
                            String userId, boolean copyToSent, HttpSession session) throws MessagingException {
        MimeMessage mimeMessage, mimeMessageExploded;
        Message messageExploded;
        String intranetEmail = null;

        try {
            IntranetAccount intranetAccount = null;
            if (userId != null) {
                intranetAccount = getIntranetAccountByUserId(userId);
            }

            // explode recipient list and make MIME message
            messageExploded = com.tms.collab.directory.ui.Util.processContactGroupAddresses(message, userId);
            mimeMessageExploded = makeMimeMessage(messageExploded, smtpAccount);

            // update message FROM field
            if (intranetAccount != null) {
                intranetEmail = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                mimeMessageExploded.setFrom(new InternetAddress(intranetEmail));
                messageExploded.setFrom(intranetEmail);
            }

            // need another instance to save unexploded recipient list
            mimeMessage = makeMimeMessage(message, smtpAccount);
            /*if (userId != null && copyToSent) {
                if (intranetAccount != null) {
                    mimeMessage.setFrom(new InternetAddress(intranetEmail));
                }
                copyToFolder(message, mimeMessage, userId, Folder.FOLDER_SENT);
            }*/

            boolean msgSent = false;
            if (userId != null && !SecurityService.ANONYMOUS_USER_ID.equals(userId)) {
                if (intranetAccount != null) {
                    // save to OUTBOX folder
                    mimeMessage.setFrom(new InternetAddress(intranetEmail));
                    message.setCopyToSent(copyToSent);
                    copyToFolder(message, mimeMessage, userId, Folder.FOLDER_OUTBOX);
                    msgSent = true;
                }
            }

            if (!msgSent) {
                // send memo
                sendIntranetMessageThruFilters(userId, messageExploded, mimeMessageExploded);

                // send email
                if (messageExploded.hasInternetRecipient()) {
                    String sender = null;
                    User currentUser = Application.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        sender = (String)currentUser.getProperty("email1");
                    }

                    if (sender == null || sender.trim().length() == 0) {
                        Map propertyMap = new HashMap();
                        try {
                            SetupModule setup = (SetupModule)Application.getInstance().getModule(SetupModule.class);
                            propertyMap = setup.getAll();
                            sender = (String)propertyMap.get(MailUtil.SETUP_PROPERTY_EMAIL_SENDER);
                        } catch (Exception e) {
                            // ignore
                        }
                        mimeMessageExploded.setFrom(new InternetAddress(sender));
                    }
                    smtpAccount.setAnonymousAccess(true);
                    sendSmtpMessage(smtpAccount, mimeMessageExploded);
                }
            }

            deleteUserTempFolder(userId, session);

        } catch (IOException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (AddressException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (javax.mail.MessagingException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            if (userId != null)
                updateUserDiskQuota(userId);
        }
    }

	/* --Added by Arun to attach iCalendar file --- */

	public void sendMessage(SmtpAccount smtpAccount, Message message,
			String userId, boolean copyToSent, HttpSession session,
			String icsMessage) throws MessagingException {
		MimeMessage mimeMessage, mimeMessageExploded;
		Message messageExploded;
		String intranetEmail = null;

		try {
			IntranetAccount intranetAccount = null;
			if (userId != null) {
				intranetAccount = getIntranetAccountByUserId(userId);
			}

			// explode recipient list and make MIME message
			messageExploded = com.tms.collab.directory.ui.Util
					.processContactGroupAddresses(message, userId);
			mimeMessageExploded = makeMimeMessage(messageExploded, smtpAccount);

			// update message FROM field
			if (intranetAccount != null) {
				intranetEmail = intranetAccount.getIntranetUsername() + "@"
						+ MessagingModule.INTRANET_EMAIL_DOMAIN;
				mimeMessageExploded.setFrom(new InternetAddress(intranetEmail));
				messageExploded.setFrom(intranetEmail);
			}

			// need another instance to save unexploded recipient list
						
			mimeMessage = makeMimeMessage(message, smtpAccount);
			
			
			Multipart multipart = new MimeMultipart();
			// Added by Arun
			//First Bodypart
			mimeMessage.addHeaderLine("method=REQUEST");
			mimeMessage.addHeaderLine("charset=UTF-8");
			mimeMessage.addHeaderLine("component=vevent");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(message.getBody());
			multipart.addBodyPart(messageBodyPart);
			
			messageBodyPart = new MimeBodyPart();
			String filename = "calendar.ics";
			try {  
			     filename = MimeUtility.encodeText(filename, "UTF-8", null);  
			     //DataSource ids = new ICalendarFileDataSource(icsMessage,filename);
			     //messageBodyPart.setDataHandler(new DataHandler(ids));
					
			}catch (Exception e) {  
	                log.warn("Error encoding mail attachment filename", e);  
            }

			messageBodyPart.setFileName(filename);					
			messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
			messageBodyPart.setHeader("Content-ID","calendar_message");
			messageBodyPart.setContent(icsMessage, "text/calendar");
			multipart.addBodyPart(messageBodyPart);	
			mimeMessage.setContent(multipart);
			mimeMessageExploded.setContent(multipart);
		
			
			//-- Added by Arun

			/*
			 * if (userId != null && copyToSent) { if (intranetAccount != null) {
			 * mimeMessage.setFrom(new InternetAddress(intranetEmail)); }
			 * copyToFolder(message, mimeMessage, userId, Folder.FOLDER_SENT); }
			 */

			boolean msgSent = false;
			if (userId != null
					&& !SecurityService.ANONYMOUS_USER_ID.equals(userId)) {
				if (intranetAccount != null) {
					// save to OUTBOX folder
					mimeMessage.setFrom(new InternetAddress(intranetEmail));
					message.setCopyToSent(copyToSent);
					copyToFolder(message, mimeMessage, userId,
							Folder.FOLDER_OUTBOX);
					msgSent = true;
				}
			}

			if (!msgSent) {
				// send memo
				sendIntranetMessageThruFilters(userId, messageExploded,
						mimeMessageExploded);

				// send email
				if (messageExploded.hasInternetRecipient()) {
					String sender = null;
					User currentUser = Application.getInstance()
							.getCurrentUser();
					if (currentUser != null) {
						sender = (String) currentUser.getProperty("email1");
					}

					if (sender == null || sender.trim().length() == 0) {
						Map propertyMap = new HashMap();
						try {
							SetupModule setup = (SetupModule) Application
									.getInstance().getModule(SetupModule.class);
							propertyMap = setup.getAll();
							sender = (String) propertyMap
									.get(MailUtil.SETUP_PROPERTY_EMAIL_SENDER);
						} catch (Exception e) {
							// ignore
						}
						mimeMessageExploded
								.setFrom(new InternetAddress(sender));
					}
					smtpAccount.setAnonymousAccess(true);
					sendSmtpMessage(smtpAccount, mimeMessageExploded);
					
				}
			}

			deleteUserTempFolder(userId, session);

		} catch (IOException e) {
			throw new MessagingException(e.getMessage(), e);
		} catch (StorageException e) {
			throw new MessagingException(e.getMessage(), e);
		} catch (MessagingDaoException e) {
			throw new MessagingException(e.getMessage(), e);
		} catch (AddressException e) {
			throw new MessagingException(e.getMessage(), e);
		} catch (javax.mail.MessagingException e) {
			throw new MessagingException(e.getMessage(), e);
		} finally {
			if (userId != null)
				updateUserDiskQuota(userId);
		}
	}

	/* --Added by Arun--- */


    public void sendStandardHtmlEmail(String userId, String toEmail, String ccEmail, String bccEmail, String subject, String title, String content) throws MessagingException, AddressException {
        Message message;
        StringBuffer sb = new StringBuffer();

        sb.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
        sb.append("  <tr valign=center> \n");
        sb.append("    <td bgcolor=\"#FFFFFF\"><b><font size=\"2\" face=\"Arial, Helvetica, sans-serif\" color=\"#000000\">" + title + "</font></b></td>\n");
        sb.append("  </tr>\n");
        sb.append("  <tr> \n");
        sb.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">" + content + "</font></td>\n");
        sb.append("  </tr>\n");
        sb.append("</table>\n");

        message = new Message();
        message.setId(UuidGenerator.getInstance().getUuid());
        SmtpAccount smtpAccount;

        smtpAccount = Util.getMessagingModule().getSmtpAccountByUserId(userId);

        message.setToList(Util.convertStringToInternetRecipientsList(toEmail));
        message.setCcList(Util.convertStringToInternetRecipientsList(ccEmail));
        message.setBccList(Util.convertStringToInternetRecipientsList(bccEmail));
        message.setToIntranetList(Util.convertStringToIntranetRecipientsList(toEmail));
        message.setCcIntranetList(Util.convertStringToIntranetRecipientsList(ccEmail));
        message.setBccIntranetList(Util.convertStringToIntranetRecipientsList(bccEmail));
        message.setSubject(subject);
        message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
        message.setBody(sb.toString());

        sendMessage(smtpAccount, message, userId);
    }

//	Added by Arun on 14th July,2006
//	 Overrided with icsMessage
	
	public void sendStandardHtmlEmail(String userId, String toEmail,
			String ccEmail, String bccEmail, String subject, String title,
			String content,String icsMessage) throws MessagingException, AddressException {
		Message message;
		StringBuffer sb = new StringBuffer();

		sb
				.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
		sb.append("  <tr valign=center> \n");
		sb
				.append("    <td bgcolor=\"#FFFFFF\"><b><font size=\"2\" face=\"Arial, Helvetica, sans-serif\" color=\"#000000\">"
						+ title + "</font></b></td>\n");
		sb.append("  </tr>\n");
		sb.append("  <tr> \n");
		sb
				.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"
						+ content + "</font></td>\n");
		sb.append("  </tr>\n");
		sb.append("</table>\n");

		message = new Message();
		message.setId(UuidGenerator.getInstance().getUuid());
		SmtpAccount smtpAccount;

		smtpAccount = Util.getMessagingModule().getSmtpAccountByUserId(userId);

		message.setToList(Util.convertStringToInternetRecipientsList(toEmail));
		message.setCcList(Util.convertStringToInternetRecipientsList(ccEmail));
		message
				.setBccList(Util
						.convertStringToInternetRecipientsList(bccEmail));
		message.setToIntranetList(Util
				.convertStringToIntranetRecipientsList(toEmail));
		message.setCcIntranetList(Util
				.convertStringToIntranetRecipientsList(ccEmail));
		message.setBccIntranetList(Util
				.convertStringToIntranetRecipientsList(bccEmail));
		message.setSubject(subject);
		message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
		message.setBody(sb.toString());

		sendMessage(smtpAccount, message, userId,false,null,icsMessage);
	}
	
	
	
	
	
    /**
     * Deletes a message.
     *
     * @param messageId specifies the messageId to delete
     * @throws MessagingException if an error occurred
     */
    public void deleteMessage(String messageId) throws MessagingException {
        MessagingDao dao;
        Message message;
        StorageService ss;
        StorageFile sf;
        Folder folder;
        String userId = null;

        dao = (MessagingDao) getDao();
        try {
            message = dao.selectMessage(messageId);
            folder = dao.selectFolder(message.getFolderId());
            userId = folder.getUserId();

            // delete storage file
            ss = (StorageService) Application.getInstance().getService(StorageService.class);
            sf = new StorageFile(message.getStorageFilename());
            ss.delete(sf);

            // delete message record
            dao.deleteMessage(messageId);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            if (userId != null) {
                updateUserDiskQuota(userId);
            }
        }
    }

    /**
     * Updates a message.
     * <p/>
     * Typical use would be to indicate that a message has been read, but
     * setting the <code>message.setRead(true)</code> followed by calling this
     * method to update the 'read' flag.
     *
     * @param message
     * @throws MessagingException
     */
    public void updateMessage(Message message) throws MessagingException {
        MessagingDao dao;
        Folder folder;

        try {
            dao = (MessagingDao) getDao();
            folder = dao.selectFolder(message.getFolderId());
            dao.updateMessage(message);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * Moves a message to another folder.
     * <p/>
     *
     * @param message             specifies the message to move
     * @param destinationFolderId specifies the destination folder to move to
     * @throws MessagingException if an exception occurred
     */
    public void moveMessage(Message message, String destinationFolderId) throws MessagingException {
        MessagingDao dao;
        StorageService ss;
        StorageFile sf;
        StorageDirectory destinationFolder;
        String destinationFolderName;
        Folder folder;
        String userId = null;

        dao = (MessagingDao) getDao();
        try {
            folder = dao.selectFolder(message.getFolderId());
            userId = folder.getUserId();

            ss = (StorageService) Application.getInstance().getService(StorageService.class);
            sf = new StorageFile(message.getStorageFilename());

            destinationFolderName = message.getStorageFilename().substring(0,
            message.getStorageFilename().lastIndexOf("/", message.getStorageFilename().lastIndexOf("/") - 1))
            + "/" + destinationFolderId;

            destinationFolder = new StorageDirectory(destinationFolderName);

            try {
                ss.move(sf, destinationFolder);
            } catch (Exception e) {
                log.debug("Error moving message " + message.getStorageFilename() + " to " + destinationFolderName, e);
            }

            message.setFolderId(destinationFolderId);
            message.setStorageFilename(destinationFolderName + "/" + message.getMessageId() + ".eml");

            dao.updateMessage(message);

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            if (userId != null) {
                updateUserDiskQuota(userId);
            }
        }

    }

    /**
     * Copies a message to another folder. The new copy of Message object will
     * have its own messageId.
     * <p/>
     *
     * @param message             specifies the message to move
     * @param destinationFolderId specifies the destination folder to move to
     * @throws MessagingException if an exception occurred
     */
    public void copyMessage(Message message, String destinationFolderId) throws MessagingException {
        MessagingDao dao;
        StorageService sh;
        StorageFile sf, destinationStorageFile;
        String destinationFilename;
        Message copyOfMessage;
        Folder folder;
        String userId = null;

        dao = (MessagingDao) getDao();
        try {
            folder = dao.selectFolder(message.getFolderId());
            userId = folder.getUserId();

            sh = (StorageService) Application.getInstance().getService(StorageService.class);

            copyOfMessage = new Message();
            copyOfMessage.setAccountId(message.getAccountId());
            copyOfMessage.setAttachmentCount(message.getAttachmentCount());
            copyOfMessage.setBccIntranetList(message.getBccIntranetList());
            copyOfMessage.setBccList(message.getBccList());
            copyOfMessage.setBody(message.getBody());
            copyOfMessage.setCcIntranetList(message.getCcIntranetList());
            copyOfMessage.setCcList(message.getCcList());
            copyOfMessage.setDate(message.getDate());
            copyOfMessage.setFolderId(message.getFolderId());
            copyOfMessage.setFrom(message.getFrom());
            copyOfMessage.setHeaders(message.getHeaders());
            copyOfMessage.setMessageFormat(message.getMessageFormat());
            copyOfMessage.setRead(message.isRead());
            copyOfMessage.setSubject(message.getSubject());
            copyOfMessage.setToIntranetList(message.getToIntranetList());
            copyOfMessage.setToList(message.getToList());

            // changes
            copyOfMessage.setMessageId(UuidGenerator.getInstance().getUuid());
            copyOfMessage.setFolderId(destinationFolderId);

            destinationFilename = message.getStorageFilename().substring(0,
            message.getStorageFilename().lastIndexOf("/", message.getStorageFilename().lastIndexOf("/") - 1))
            + "/" + destinationFolderId + "/" + copyOfMessage.getMessageId() + ".eml";

            copyOfMessage.setStorageFilename(destinationFilename);

            destinationStorageFile = new StorageFile(destinationFilename);
            sf = new StorageFile(message.getStorageFilename());

            if (sh.copy(sf, destinationStorageFile)) {
                dao.insertMessage(message);
            } else {
                log.error("Failed to copy " + sf.getAbsolutePath() + " to " + destinationStorageFile.getAbsolutePath());
            }

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            if (userId != null) {
                updateUserDiskQuota(userId);
            }
        }

    }

    /**
     * @param folderId
     * @param start
     * @param numOfRows
     * @param sortBy
     * @param isDescending
     * @param searchBy
     * @param searchCriteria
     * @return
     * @throws MessagingException
     * @deprecated don't use this anymore
     */
    public Collection getMessages(String folderId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws MessagingException {
        MessagingDao dao;

        dao = (MessagingDao) getDao();
        try {
            if (folderId == null || folderId.trim().equals("")) {
                throw new MessagingException("folderId not found");
            } else {
                return dao.selectMessages(folderId, start, numOfRows, sortBy, isDescending, searchBy, searchCriteria);
            }

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * @param folderId
     * @param searchBy
     * @param searchCriteria
     * @return
     * @throws MessagingException
     * @deprecated don't use this anymore
     */
    public int getMessagesCount(String folderId, String searchBy, String searchCriteria) throws MessagingException {
        MessagingDao dao = (MessagingDao) getDao();
        int numOfMessage = 0;

        try {
            numOfMessage = dao.selectMessagesCount(folderId, searchBy, searchCriteria);
            return numOfMessage;

        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    // === [ protected methods ] =================================================
    /**
     * Returns a MimeMessage object for storing as intranet message or to be
     * sent via SMTP
     *
     * @param message
     * @return
     * @throws javax.mail.MessagingException
     */
    protected MimeMessage makeMimeMessage(Message message, SmtpAccount smtpAccount) throws javax.mail.MessagingException {
        Session session;
        MimeMessage mimeMessage;
        Iterator iterator;
        Multipart multipart;
        BodyPart messageBodyPart;
        StorageFileDataSource sfds;
        StorageFile sf;

        session = makeSession(smtpAccount);

        // construct the message to send
        mimeMessage = new MimeMessage(session);
        mimeMessage.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
        mimeMessage.setHeader("Content-Transfer-Encoding", "8bit");
        
        // FROM field
        if (message.getFrom() != null && message.getFrom().trim().length() > 0) {
            mimeMessage.setFrom(new InternetAddress(message.getFrom()));
        }

        // TO field
        if (message.getToList() != null) {
            iterator = message.getToList().iterator();
            while (iterator.hasNext()) {
                mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress((String) iterator.next()));
            }
        }

        // CC field
        if (message.getCcList() != null) {
            iterator = message.getCcList().iterator();
            while (iterator.hasNext()) {
                mimeMessage.addRecipient(javax.mail.Message.RecipientType.CC,
                new InternetAddress((String) iterator.next()));
            }
        }

        // BCC field
        if (message.getBccList() != null) {
            iterator = message.getBccList().iterator();
            while (iterator.hasNext()) {
                mimeMessage.addRecipient(javax.mail.Message.RecipientType.BCC,
                new InternetAddress((String) iterator.next()));
            }
        }
        
        mimeMessage.setSubject(message.getSubject(), "utf-8");
        mimeMessage.setSentDate(new Date());

        if (message.getStorageFileList() == null || message.getStorageFileList().size() == 0) {
            // send email with no attachments
            if (message.getMessageFormat() == Message.MESSAGE_FORMAT_HTML) {
                mimeMessage.setContent(message.getBody(), "text/html; charset=\"utf-8\"");
            } else {
                mimeMessage.setContent(message.getBody(), "text/plain; charset=\"utf-8\"");
            }

        } else {
            // send email with attachments
            multipart = new MimeMultipart();

            // add actual message
            messageBodyPart = new MimeBodyPart();
            if (message.getMessageFormat() == Message.MESSAGE_FORMAT_HTML) {
                messageBodyPart.setContent(message.getBody(), "text/html; charset=\"utf-8\"");
            } else {
                messageBodyPart.setContent(message.getBody(), "text/plain; charset=\"utf-8\"");
            }
            multipart.addBodyPart(messageBodyPart);

            // add attachments
            iterator = message.getStorageFileList().iterator();
            while (iterator.hasNext()) {
                sf = (StorageFile) iterator.next();
                sfds = new StorageFileDataSource(sf);

                String filename = sf.getName();
                try {
                    filename = MimeUtility.encodeText(filename, "UTF-8", null);
                }
                catch (Exception e) {
                    log.warn("Error encoding mail attachment filename", e);
                }

                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(sfds));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                
                
                
                
            }
            mimeMessage.setContent(multipart);

        }

        return mimeMessage;
    }

    protected Session makeSession(SmtpAccount smtpAccount) {
        Properties p;
        Session session;
        p = new Properties();

        // set timeouts
        String timeout = Application.getInstance().getProperty(APPLICATION_KEY_CONNECTION_TIMEOUT);
        if (timeout == null || timeout.trim().length() == 0) {
            timeout = TIMEOUT_PERIOD;
        }

        p.put("mail.pop3.connectiontimeout", timeout);
        p.put("mail.pop3.timeout", timeout);

        p.put("mail.smtp.host", smtpAccount.getServerName());
        p.put("mail.smtp.port", Integer.toString(smtpAccount.getServerPort()));

        session = Session.getInstance(p);
        return session;
    }

    protected Session makeSmtpSession(SmtpAccount smtpServer) {
        Properties p;
        Session session;
        p = new Properties();
        p.put("mail.smtp.host", smtpServer.getServerName());
        p.put("mail.smtp.port", Integer.toString(smtpServer.getServerPort()));

        // set timeouts
        String timeout = Application.getInstance().getProperty(APPLICATION_KEY_CONNECTION_TIMEOUT);
        if (timeout == null || timeout.trim().length() == 0) {
            timeout = TIMEOUT_PERIOD;
        }
        p.put("mail.smtp.connectiontimeout", timeout);
        p.put("mail.smtp.timeout", timeout);
        p.put("mail.transport.protocol", "smtp");

        session = Session.getInstance(p);
        return session;
    }
    
    protected void sendIntranetAndSmtpMessagesNow() {
        
        MessagingDao dao = (MessagingDao) getDao();
        try {
            Message[] messages = dao.findMessagesInOutBox();
            Message currentMessage = null;
            String currentUserId = null;
            SmtpAccount currentSmtpAccount = null;
            MimeMessage currentMimeMessage = null;
            IntranetAccount currentIntranetAccount = null;
            
            
            for (int a=0; a< messages.length; a++) {
                try {
                	currentUserId = Util.stripUserIdFromMessageStorageFilename(messages[a].getStorageFilename());
                	currentMessage = messages[a];
                    Message tmpMsg = getMessageByMessageId(messages[a].getId(), true);
                    currentMessage.setStorageFileList(tmpMsg.getStorageFileList());
                    currentMessage.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                	currentSmtpAccount = getSmtpAccountByUserId(currentUserId);
                	currentMimeMessage = makeMimeMessage(currentMessage, currentSmtpAccount);
                    currentIntranetAccount = getIntranetAccountByUserId(currentUserId);
                    
                    currentMimeMessage.setSentDate(currentMessage.getDate());
                    
                    log.debug("send message with id = "+currentMessage.getId());
                    
                	// explode recipient list and make MIME message
                    Message messageExploded = com.tms.collab.directory.ui.Util.processContactGroupAddresses(currentMessage, currentUserId);
                    tmpMsg = getMessageByMessageId(messageExploded.getId(), true);
                    messageExploded.setStorageFileList(tmpMsg.getStorageFileList());
                    MimeMessage mimeMessageExploded = makeMimeMessage(messageExploded, currentSmtpAccount);
                    
                	// send Internet message
                    if (messageExploded.hasInternetRecipient()) {
                        if (currentIntranetAccount != null) {
                            mimeMessageExploded.setFrom(new InternetAddress(currentIntranetAccount.getFromAddress()));
                        }
                        sendSmtpMessageNow(currentSmtpAccount, mimeMessageExploded);
                    }

                    sendIntranetMessageThruFilters(currentUserId, messageExploded, mimeMessageExploded);

                    // make a copy to SENT_BOX
                    Message oMsg = (Message) currentMessage.clone();
                    if (currentMessage.getCopyToSent()) {
                    	copyToFolder(currentMessage, currentMimeMessage, currentUserId, Folder.FOLDER_SENT);
                    }
                    
                    // delete from OUTBOX
                    currentMessage = oMsg;
                    deleteMessage(currentMessage.getMessageId());
                }
                catch(Exception e) {
                    // send memo to notify sender 
                    
                    log.error(e.toString(), e);
                    // smtp sending error
                    dao.markMessageErrorFlag(currentMessage.getId(), Message.ERROR_FLAG_FAIL);
                    
                    String stackTrace = null;
                    
                    StringWriter writer = null;
                    PrintWriter p = null;
                    try {
                    	writer = new StringWriter();
                    	p = new PrintWriter(writer);
                    	stackTrace = writer.toString();
                    }
                    finally {
                    	// always close connections
                        if (p != null) { p.close(); }
                        try {
                        	if (writer != null) { writer.close(); }
                        }
                        catch(IOException ioe) {
                        	log.error("error closing exception print writer", ioe);
                        }
                    }
                    
                    
                    // send memo to sender
                    try {
                        Application app = Application.getInstance();
                        String subject = app.getMessage("messaging.errorEmail.subject");
                        String title = app.getMessage("messaging.errorEmail.title");
                        String content = app.getMessage("messaging.errorEmail.content", 
                                new Object[] {
                                    currentMimeMessage.getSubject(),
                                    e.getMessage(),
                                    stackTrace
                                });
                        
                        IntranetAccount ia = getIntranetAccountByUserId(currentUserId);
                        String toEmail = ia.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                    	sendStandardHtmlEmail(currentUserId, toEmail, "", "", subject, title, content);
                    }
                    catch(AddressException e1) {
                    	// exception when sending error message due to previous message sending, just log
                        log.error("exception when sending error message due to previous message sending", e1);
                    }
                    catch(MessagingException e2) {
                    	log.error("exception when sending error message due to previous message sending", e2);
                    }
                    catch(javax.mail.MessagingException e3) {
                    	log.error("exception when sending error message due to previous message sending", e3);
                    }
                }
            }
        }
        catch(MessagingDaoException e) {
            log.error(e.toString(), e);
        }
    }

    protected void sendIntranetMessageThruFilters(String currentUserId, Message messageExploded, MimeMessage mimeMessageExploded) {
        try {
            // send Intranet message
            if (messageExploded.hasIntranetRecipient()) {
                UserIdAndMessageWrapper[] uidamw = sendIntranetMessage(currentUserId, messageExploded, mimeMessageExploded);

                // === start intranet messages filtering ===
                for (int z=0; z< uidamw.length; z++) {
                    String uid = uidamw[z].getUserId(); // receiver
                    Pop3Account pop3Account = getPop3Account(uid); // receiver p3acount
                    Message msg = uidamw[z].getMessage();

                    getFilterManager().filter(
                            new Message[] { msg },
                            new Object[] { uid, pop3Account});
                }
                // === end intranet messages filtering ===
            }
        }
        catch(Exception e) {
            //  might be incorect memo address, ignore, proceed on
            log.warn("might be incorect memo address, ignore, proceed on", e);
        }
    }

    protected void sendSmtpMessageNow(SmtpAccount smtpAccount, MimeMessage mimeMessage) throws javax.mail.MessagingException {
        Session session;
        Transport transport;

        session = makeSmtpSession(smtpAccount);
        transport = session.getTransport("smtp");

        if (!smtpAccount.isAnonymousAccess()) {
            String decryptedPassword = (smtpAccount.getPassword() != null) ? Encryption.decrypt(smtpAccount.getPassword(), ENCRYPTION_KEY) : "";
            transport.connect(smtpAccount.getServerName(), smtpAccount.getServerPort(), smtpAccount.getUsername(), decryptedPassword);
        }

        Transport.send(mimeMessage);
        log.debug("Email sent (Subject: " + mimeMessage.getSubject() + ")");
    }

    protected void sendSmtpMessage(SmtpAccount smtpAccount, MimeMessage mimeMessage) {
        MessagingQueue queue = MessagingQueue.getInstance();

        queue.pushSendSmtp(smtpAccount, mimeMessage);
    }


    protected UserIdAndMessageWrapper[] sendIntranetMessage(String userId, Message message, MimeMessage mimeMessage) throws javax.mail.MessagingException, IOException, StorageException, MessagingDaoException, MessagingException {
        Iterator iterator;
        InternetAddress address;
        String username;
        IntranetAccount intranetAccount = new IntranetAccount();
        MessagingDao dao;
        List invalidAddressList = new ArrayList();

        SecurityService ss;
        User recipient;
        Folder qmFolder;

        // contain list of <code>UserIdAndMessageWrapper</code>
        List userIdAndMessages = new ArrayList();
        
        dao = (MessagingDao) getDao();

        // TO field
        if (message.getToIntranetList() != null) {
            iterator = message.getToIntranetList().iterator();
            while (iterator.hasNext()) {
                address = new InternetAddress((String) iterator.next());
                int position = address.getAddress().indexOf('@');

                if (position == -1) {
                    log.debug("Invalid intranet email recipient " + address);

                } else {
                    username = address.getAddress().substring(0, position);
                    try {
                        intranetAccount = dao.selectIntranetAccount(username);
                    } catch (MessagingDaoException e) {
                        // skip this guy, does not exist
                        invalidAddressList.add(address);
                        log.debug("Invalid intranet email recipient: " + username);
                        continue;
                    }

                    message.setMessageId(UuidGenerator.getInstance().getUuid());
                    message.setRead(false);

                    // special handling for QM stuff
                    message.setFolderId(intranetAccount.getDeliveryFolderId());
                    try {
                        if (MessagingModule.QM_SUBJECT.equals(message.getSubject())) {
                            ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
                            Collection users = ss.getUsersByUsername(username);
                            if (!users.isEmpty()) {
                                recipient = (User) users.iterator().next();
                                qmFolder = getSpecialFolder(recipient.getId(), Folder.FOLDER_QM);
                                // set to user's QM folder
                                message.setFolderId(qmFolder.getFolderId());
                            }
                        }
                    } catch (SecurityException e) {
                        log.error(e.getMessage(), e);
                    } catch (MessagingException e) {
                        log.error(e.getMessage(), e);
                    }

                    message.setAccountId(intranetAccount.getAccountId());
                    message.setStorageFilename(ROOT_PATH + "/" +
                    intranetAccount.getUserId() + "/" + intranetAccount.getDeliveryFolderId() +
                    "/" + message.getMessageId() + ".eml");

                    saveMessageToStorage(mimeMessage, message);
                    saveMessageToDao(mimeMessage, message);
                    
                    // save a copy of vital info to be returned
                    try {
                    	userIdAndMessages.add(new UserIdAndMessageWrapper(intranetAccount.getUserId(), (Message)message.clone(), mimeMessage));
                    }
                    catch(CloneNotSupportedException e) {
                    	log.error(e.toString(), e);
                    }
                }
            }
        }

        // CC field
        if (message.getCcIntranetList() != null) {
            iterator = message.getCcIntranetList().iterator();
            while (iterator.hasNext()) {
                address = new InternetAddress((String) iterator.next());
                username = address.getAddress().substring(0, address.getAddress().indexOf('@'));
                try {
                    intranetAccount = dao.selectIntranetAccount(username);
                } catch (MessagingDaoException e) {
                    // skip this guy, does not exist
                    invalidAddressList.add(address);
                    log.info("Invalid intranet email recipient: " + username);
                    continue;
                }

                message.setMessageId(UuidGenerator.getInstance().getUuid());
                message.setRead(false);

                // special handling for QM stuff
                message.setFolderId(intranetAccount.getDeliveryFolderId());
                try {
                    if (MessagingModule.QM_SUBJECT.equals(message.getSubject())) {
                        ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
                        Collection users = ss.getUsersByUsername(username);
                        if (!users.isEmpty()) {
                            recipient = (User) users.iterator().next();
                            qmFolder = getSpecialFolder(recipient.getId(), Folder.FOLDER_QM);
                            // set to user's QM folder
                            message.setFolderId(qmFolder.getFolderId());
                        }
                    }
                } catch (SecurityException e) {
                    log.error(e.getMessage(), e);
                } catch (MessagingException e) {
                    log.error(e.getMessage(), e);
                }

                message.setAccountId(intranetAccount.getAccountId());
                message.setStorageFilename(ROOT_PATH + "/" +
                intranetAccount.getUserId() + "/" + intranetAccount.getDeliveryFolderId() +
                "/" + message.getMessageId() + ".eml");

                saveMessageToStorage(mimeMessage, message);
                saveMessageToDao(mimeMessage, message);
                
                // save a copy of vital info to be returned
                try {
                	userIdAndMessages.add(new UserIdAndMessageWrapper(intranetAccount.getUserId(), (Message) message.clone(), mimeMessage));
                }
                catch(CloneNotSupportedException e) {
                	log.error(e.toString(), e);
                }
            }
        }

        // BCC field
        if (message.getBccIntranetList() != null) {
            iterator = message.getBccIntranetList().iterator();
            while (iterator.hasNext()) {
                address = new InternetAddress((String) iterator.next());
                username = address.getAddress().substring(0, address.getAddress().indexOf('@'));
                try {
                    intranetAccount = dao.selectIntranetAccount(username);
                } catch (MessagingDaoException e) {
                    // skip this guy, does not exist
                    invalidAddressList.add(address);
                    log.debug("Invalid intranet email recipient: " + username);
                    continue;
                }

                message.setMessageId(UuidGenerator.getInstance().getUuid());
                message.setRead(false);

                // special handling for QM stuff
                message.setFolderId(intranetAccount.getDeliveryFolderId());
                try {
                    if (MessagingModule.QM_SUBJECT.equals(message.getSubject())) {
                        ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
                        Collection users = ss.getUsersByUsername(username);
                        if (!users.isEmpty()) {
                            recipient = (User) users.iterator().next();
                            qmFolder = getSpecialFolder(recipient.getId(), Folder.FOLDER_QM);
                            // set to user's QM folder
                            message.setFolderId(qmFolder.getFolderId());
                        }
                    }
                } catch (SecurityException e) {
                    log.error(e.getMessage(), e);
                } catch (MessagingException e) {
                    log.error(e.getMessage(), e);
                }

                message.setAccountId(intranetAccount.getAccountId());
                message.setStorageFilename(ROOT_PATH + "/" +
                intranetAccount.getUserId() + "/" + intranetAccount.getDeliveryFolderId() +
                "/" + message.getMessageId() + ".eml");

                saveMessageToStorage(mimeMessage, message);
                saveMessageToDao(mimeMessage, message);
                
                // save a copy of vital info to be returned
                try {
                	userIdAndMessages.add(new UserIdAndMessageWrapper(intranetAccount.getUserId(), (Message) message.clone(), mimeMessage));
                }
                catch(CloneNotSupportedException e) {
                    log.error(e.toString(), e);
                }
            }
        }


        // send message to sender to report if invalidAddressList is not empty
        // Note: potential endless loop here if cannot send to sender
        if (invalidAddressList.size() > 0) {
            Application application = Application.getInstance();
            String subject = application.getMessage("messaging.errorEmail.invalidUser.title");
            String title = subject;
            StringBuffer sb = new StringBuffer();

            //sb.append
            sb.append(application.getMessage("messaging.errorEmail.invalidUser.message.header",
                    new String[] {
                        message.getSubject()
                    }));
            for (int i = 0; i < invalidAddressList.size(); i++) {
                InternetAddress internetAddress = (InternetAddress) invalidAddressList.get(i);
                sb.append(application.getMessage("messaging.errorEmail.invalidUser.message.content",
                        new String[] {
                            internetAddress.getAddress()
                        }));
            }
            sb.append(application.getMessage("messaging.errorEmail.invalidUser.message.tail",
                    new String[] {
                        message.getBody()
                    }));
            
            String content = sb.toString();

            if (userId != null) {
                sendStandardHtmlEmail(userId, message.getFrom(), "", "", subject, title, content);
            }
        }
        return (UserIdAndMessageWrapper[]) userIdAndMessages.toArray(new UserIdAndMessageWrapper[0]);
    }


    protected void copyToFolder(Message message, MimeMessage mimeMessage,
                              String userId, Folder folder)
    throws IOException, javax.mail.MessagingException,
    StorageException, MessagingDaoException {

        message.setMessageId(UuidGenerator.getInstance().getUuid());
        message.setFolderId(folder.getFolderId());
        message.setStorageFilename(ROOT_PATH + "/" +
        userId + "/" + folder.getFolderId() +
        "/" + message.getMessageId() + ".eml");

        saveMessageToStorage(mimeMessage, message);
        saveMessageToDao(mimeMessage, message);
    }

    /**
     * Copy <code>message</code> and <code>mimeMessage</code> to special folder.
     * See {@link Folder} for more info about special folder type.
     * 
     * @param message
     * @param mimeMessage
     * @param userId
     * @param specialFolderType 
     * @throws IOException
     * @throws javax.mail.MessagingException
     * @throws StorageException
     * @throws MessagingDaoException
     * @throws MessagingException
     */
    protected void copyToFolder(Message message, MimeMessage mimeMessage,
                                  String userId, String specialFolderType)
    throws IOException, javax.mail.MessagingException,
    StorageException, MessagingDaoException, MessagingException {

        IntranetAccount intranetAccount;
        Folder specialFolder;

        intranetAccount = getIntranetAccountByUserId(userId);
        specialFolder = getSpecialFolder(userId, specialFolderType);

        message.setAccountId(intranetAccount.getAccountId());
        boolean readFlag = message.isReadFlag();
        message.setReadFlag(false);
        copyToFolder(message, mimeMessage, userId, specialFolder);
        message.setReadFlag(readFlag);
    }
    
    /**
     * Stores raw message into the StorageService.
     *
     * @param message
     * @param myMessage
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     * @throws StorageException
     */
    void saveMessageToStorage(javax.mail.Message message,
                                      Message myMessage)
    throws javax.mail.MessagingException, IOException, StorageException {
        StorageService sh;
        StorageFile sf;
        ByteArrayOutputStream baos;
        ByteArrayInputStream bais;
        javax.mail.internet.MimeMessage mm;

        sh = (StorageService) Application.getInstance().getService(StorageService.class);

		//Checking to avoid NPE from empty message content
		if(message.getContent() == null)
			message.setContent("", "text/html; charset=\"utf-8\"");

        baos = new ByteArrayOutputStream();

        String[] contentTransferEncoding = message.getHeader("Content-Transfer-Encoding");
        if (contentTransferEncoding != null && contentTransferEncoding[0].equalsIgnoreCase("plain")) {
            // handle 'plain'!? emails
            mm = (javax.mail.internet.MimeMessage) message;
            StorageFileDataSource.copy(mm.getRawInputStream(), baos);
        } else {
            // handle the normal way
            message.writeTo(baos);
        }

        bais = new ByteArrayInputStream(baos.toByteArray());

        sf = new StorageFile(myMessage.getStorageFilename());
        sf.setInputStream(bais);
        sh.store(sf);
    }

    /**
     * Parse and store message into database.
     *
     * @param m
     * @param message
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     * @throws MessagingDaoException
     */
    void saveMessageToDao(javax.mail.Message m, Message message) throws javax.mail.MessagingException, IOException, MessagingDaoException {
        MimeMessage mimeMessage;
        MessagingDao dao = (MessagingDao) getDao();

        mimeMessage = (MimeMessage) m;

        // set FROM field
        try {
            if (mimeMessage.getFrom() == null) {
                log.warn("FROM field not specified in POP3 message");
            } else if (mimeMessage.getFrom().length == 0) {
                log.warn("FROM field in POP3 message is empty");
            } else if (mimeMessage.getFrom().length > 1) {
                log.warn("Multiple FROM field entries in POP3 message not supported");
                message.setFrom(mimeMessage.getFrom()[0].toString());
            } else {
                message.setFrom(mimeMessage.getFrom()[0].toString());
            }
        } catch (AddressException e) {
            log.warn(e.getMessage(), e);
        }

        try {
            message.setToList(Util.convertAddressesToRecipientsList(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO)));
        } catch (AddressException e) {
            log.warn(e.getMessage(), e);
        }
        try {
            message.setCcList(Util.convertAddressesToRecipientsList(mimeMessage.getRecipients(javax.mail.Message.RecipientType.CC)));
        } catch (AddressException e) {
            log.warn(e.getMessage(), e);
        }
        try {
            message.setBccList(Util.convertAddressesToRecipientsList(mimeMessage.getRecipients(javax.mail.Message.RecipientType.BCC)));
        } catch (AddressException e) {
            log.warn(e.getMessage(), e);
        }

        message.setSubject(mimeMessage.getSubject());
        if (message.getSubject() == null) {
            message.setSubject("");
        }
        message.setBody("");

        populateMessageFromMimeMessage(mimeMessage, message);

        message.setDate(mimeMessage.getSentDate());
        if (message.getDate() == null) {
            message.setDate(mimeMessage.getReceivedDate());
            if (message.getDate() == null) {
                message.setDate(new Date(System.currentTimeMillis()));
            }
        }

        message.setHeaders(Util.convertToHeaders(mimeMessage.getAllHeaders()));

        dao.insertMessage(message);
    }

    protected void populateMessageFromMimeMessage(MimeMessage mimeMessage,
                                                Message message)
    throws IOException, javax.mail.MessagingException {

        Object o = getContentEnhanced(mimeMessage);

        if (o instanceof String) {
            message.setBody(message.getBody() + (String) o);

            // set message format
            if (mimeMessage.isMimeType("text/plain")) {
                message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
            } else if (mimeMessage.isMimeType("text/html")) {
                message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            } else {
                // BUG 3907:::
                if ((mimeMessage.getContentType() != null) && 
                        (mimeMessage.getContentType().toLowerCase().indexOf("multipart/mixed") >= 0)) {
                    message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                    log.warn("default to text/html content type");
                }
                log.warn("Mime Type not supported: " + mimeMessage.getContentType());
            }

        } else if (o instanceof MimeMultipart) {
            populateMessageFromMimeMultipart((MimeMultipart) o, message);

        } else {
            log.error("MimeMessage content not supported: " + o.getClass().getName());
        }
    }

    /**
     * Sets the body, attachmentCount and messageFormat of a Message object.
     *
     * @param mimeMultipart
     * @param message
     */
    protected void populateMessageFromMimeMultipart(MimeMultipart mimeMultipart,
                                                  Message message) {
        String disposition = null;
        Part part;
        int attachmentCount = 0;
        Map contentIdMap = new HashMap();

        try {
            if (mimeMultipart.getContentType().toLowerCase().startsWith("multipart/alternative")) {
                // handle html and plain text messages
                message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                populateMessageFromMimeMultipartAlternative(mimeMultipart, message);

            } else {
                // handle stuff with attachments

                // go through each part of multipart message
                for (int i = 0; i < mimeMultipart.getCount(); i++) {
                    part = mimeMultipart.getBodyPart(i);

                    if (part.isMimeType("message/rfc822")) {
                        Object o = getContentEnhanced(part);
                        populateMessageFromMimeMessage((MimeMessage) o, message);
                        continue;
                    }

                    try {
                        disposition = part.getDisposition();
                    }
                    catch (ParseException e) {
                        log.warn("Error parsing mail disposition", e);
                    }

                    // populate contentIdMap for second parsing to convert cid: at the end of method
                    String contentId = null;
                    String partFilename;
                    String[] contentIds = part.getHeader("Content-Id");
                    if(contentIds!=null && contentIds.length==1) {
                        contentId = contentIds[0];
                        partFilename = part.getFileName();
                        if(partFilename!=null && partFilename.length()>0 && contentId!=null && contentId.length()>0) {
                            contentId = StringUtils.replaceChars(contentId, "<", "");
                            contentId = StringUtils.replaceChars(contentId, ">", "");
                            contentIdMap.put(contentId, partFilename);
                        }
                    }

                    if (disposition == null || disposition.equalsIgnoreCase(Part.INLINE)) {
                        // handle the types
                        if (part.isMimeType("text/plain")) {
                            // save text/plain content type
                            message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                            message.setBody(message.getBody() + getContentEnhanced(part).toString());

                        } else if (part.isMimeType("text/html")) {
                            // save text/html content type
                            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                            message.setBody(message.getBody() + getContentEnhanced(part).toString());

                        } else if (part.getContentType().toLowerCase().startsWith("multipart/alternative")) {
                            // text and html message with attachments
                            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                            populateMessageFromMimeMultipartAlternative((MimeMultipart) getContentEnhanced(part), message);

                        } else if(part.getContentType().toLowerCase().startsWith("multipart/related")) {
                            // handle multipart/related recursively (inside a multipart/mixed)
                            populateMessageFromMimeMultipart((MimeMultipart) getContentEnhanced(part), message);

                        } else if(part.getContentType().toLowerCase().startsWith("multipart/mixed")) {
                            // handle multipart/mixed
                            populateMessageFromMimeMultipart((MimeMultipart) getContentEnhanced(part), message);

                        } else {
                            // all other types
                            // found attachment
                            attachmentCount++;
                        }
                    } else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                        // found attachment
                        attachmentCount++;
                    }

                }

                // 'reparse' body to convert cid:
                if(Message.MESSAGE_FORMAT_HTML==message.getMessageFormat()) {
                    // for HTML, convert cid:
                    String contentFilename;
                    String body = message.getBody();
                    Set contentIdSet = contentIdMap.keySet();
                    for (Iterator iterator = contentIdSet.iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        // TODO: should be absolute URL?
                        contentFilename = "../../messaging/downloadAttachment?messageId=" + message.getMessageId() + "&name=" + Util.encodeHex((String)contentIdMap.get(key));
                        body = StringUtils.replace(body, "cid:" + key, contentFilename);
                        message.setBody(body);
                    }
                }


            }

        } catch (Exception e) {
            // this SHOULD catch all exceptions and set message body to report
            // the error
            log.error(e.getMessage(), e);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String st = sw.toString();
            String subject = message.getSubject();
            subject = subject == null ? "" : subject;
            message.setSubject("!ERROR! - " + subject);
            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setBody(message.getBody() + "<hr>Exception message: <b>"
            + e.getMessage() + "</b><hr><pre>" + st + "</pre>");

        }

        message.setAttachmentCount(attachmentCount);
    }

    /**
     * Sets the body field of a Message object, for MimeMultipartAlternative
     * (emails sent with both text and html contents). For
     * multipart/alternative, only take the html part.
     */
    protected void populateMessageFromMimeMultipartAlternative(MimeMultipart mimeMultipart, Message message) throws javax.mail.MessagingException, IOException {
        Part part;

        // get text/html part of message
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            part = mimeMultipart.getBodyPart(i);
            if (part.isMimeType("text/html")) {
                message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                message.setBody((String) getContentEnhanced(part));
                break;
            } else if (part.isMimeType("text/plain")) {
                message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                message.setBody((String) getContentEnhanced(part));
                // no need to break, continue to find text/html if any
            } else {
                log.warn("Unsupported mime type found: " + part.getContentType());
            }
        }

    }

    /**
     * Sets the StorageFileList of a Message object from MimeMessage.
     *
     * @param mimeMessage
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     */
    protected List parseMimeAttachments(MimeMessage mimeMessage)
    throws javax.mail.MessagingException, IOException {
        Object o;
        List storageFileList;

        o = getContentEnhanced(mimeMessage);
        if (o instanceof MimeMultipart) {
            storageFileList = handleMimeMultipart((MimeMultipart) o);
        } else {
            storageFileList = new ArrayList();
        }

        return storageFileList;
    }

    /**
     * Sets the StorageFileList of a Message object from MimeMessage. ACTUAL
     * WORK DONE HERE?
     *
     * @param mimeMultipart
     * @return
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     */
    protected List handleMimeMultipart(MimeMultipart mimeMultipart)
    throws javax.mail.MessagingException, IOException {
        String disposition = null;
        Part part;
        List storageFileList = new ArrayList();
        StorageFile storageFile;

        if (mimeMultipart.getContentType().toLowerCase().startsWith("multipart/alternative")) {
            // no attachments

        } else {
            // handle stuff with attachments

            // go through each part of multipart message
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                part = mimeMultipart.getBodyPart(i);

                // if a forwarded message
                if (part.isMimeType("message/rfc822")) {
                    Object o = getContentEnhanced(part);
                    List embeddedMessageList = parseMimeAttachments((MimeMessage) o);
                    storageFileList.addAll(embeddedMessageList);
                    continue;
                }

                try {
                    disposition = part.getDisposition();
                }
                catch (ParseException e) {
                    log.warn("Error parsing mail disposition", e);
                }

                if (disposition == null || disposition.equalsIgnoreCase(Part.INLINE)) {
                    // handle the types
                    if (part.isMimeType("text/plain")) {
                        // do nothing

                    } else if (part.isMimeType("text/html")) {
                        // do nothing

                    } else if (part.getContentType().toLowerCase().startsWith("multipart/alternative")) {
                        // do nothing

                    } else if (part.getContentType().toLowerCase().startsWith("multipart/related")) {
                        // recusive handle this type
                        List list = handleMimeMultipart((MimeMultipart) getContentEnhanced(part));
                        storageFileList.addAll(list);

                    } else {
                        // all other types, just save it!
                        // save attachment
                        storageFile = saveAttachment(part);
                        storageFileList.add(storageFile);
                    }

                } else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                    // save attachment
                    storageFile = saveAttachment(part);
                    storageFileList.add(storageFile);

                }

            }
        }

        return storageFileList;
    }

    protected StorageFile saveAttachment(Part part) throws javax.mail.MessagingException, IOException {
        StorageFile sf;
        String filename = null;

        try {
            filename = part.getFileName();
            if (filename != null) {
                filename = MimeUtility.decodeText(filename);
            }
        }
        catch (ParseException e) {
            log.warn("Error parsing mail part filename", e);
        }
        if (filename == null || filename.length() == 0) {
            try {
                // manually parse?
                String disposition = part.getHeader("Content-Disposition")[0];
                int i=disposition.indexOf("filename=");
                if (i > 0) {
                    String tmp = disposition.substring(i+9);
                    if (tmp.startsWith("\"") && tmp.endsWith("\"")) {
                        tmp = tmp.substring(1, tmp.length()-1);
                        filename = tmp;
                    }
                }
            }
            catch (Exception e) {
                log.warn("Error parsing mail part filename", e);
            }
        }
        if (filename == null || filename.length() == 0) {
            // generate id as filename
            filename = UuidGenerator.getInstance().getUuid();
        }

        sf = new StorageFile(null);
        sf.setName(filename);
        sf.setInputStream(part.getInputStream());

        return sf;
    }

    /**
     * Delete user's temp folder.
     *
     * @param userId
     * @throws StorageException
     */
    public void deleteUserTempFolder(String userId, HttpSession session) throws StorageException {
        // clear user attachments temp folder
        if (userId != null) {
            StorageDirectory sd;
            StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);

            sd = new StorageDirectory(MessagingModule.ROOT_PATH + "/" + userId + "/temp/");
            ss.delete(sd);
        }

        // clear session attribute
        if (session != null) {
            session.removeAttribute(ATTACHMENT_MAP_SESSION_ATTRIBUTE);
        }
    }


    protected String filterHtml(String s) throws MessagingException {
        try {
            Parser parser = new Parser(new NodeReader(new StringReader(s), s.length()));
            TextExtractingVisitor visitor = new TextExtractingVisitor();
            parser.visitAllNodesWith(visitor);
            String cleanText = ParserUtils.removeEscapeCharacters(visitor.getExtractedText());
            return cleanText;
        } catch (ParserException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    protected Object getContentEnhanced(Part part) throws javax.mail.MessagingException, IOException {
        ByteArrayOutputStream baos;

        try {
            return part.getContent();

        } catch (UnsupportedEncodingException uex) {

            log.warn("Unsupported encoding: " + uex.getMessage());
            baos = new ByteArrayOutputStream();
            StorageFileDataSource.copy(part.getInputStream(), baos);
            return baos.toString();

        } catch (IOException e) {
            if (e.getMessage().equalsIgnoreCase("Unknown encoding: plain")) {
                // handle 'plain' emails!
                log.warn("Unknown encoding: plain");

                MimeMessage mm = (MimeMessage) part;
                baos = new ByteArrayOutputStream();
                StorageFileDataSource.copy(mm.getRawInputStream(), baos);
                return baos.toString();
            } else {
                // just throw the IOException out
                throw e;
            }
        }
    }

    /**
     * Returns a Collection of the email/intranet addresses from the most recently sent emails.
     *
     * @param userId
     * @param maxMessages  Number of messages to retrieve
     * @param maxAddresses Number of mail addresses to retrieve
     * @return A Collection of Strings (email addresses).
     * @throws MessagingException
     */
    public Collection getAddressesForAutoComplete(String userId, int maxMessages, int maxAddresses) throws MessagingException {
        MessagingDao dao;
        List addresses = new ArrayList();

        try {
            // retrieve most recent sent messages
            dao = (MessagingDao) getDao();
            Collection recentMessages = dao.selectRecentMessages(Folder.FOLDER_SENT, userId, maxMessages);

            Map bufferMap = new SequencedHashMap(); // email address -> internet address

            for (Iterator i = recentMessages.iterator(); i.hasNext();) {
                Message message = (Message) i.next();

                utilAppendAddressForAutoComplete(bufferMap, message.getToIntranetList());
                utilAppendAddressForAutoComplete(bufferMap, message.getToList());
                utilAppendAddressForAutoComplete(bufferMap, message.getCcIntranetList());
                utilAppendAddressForAutoComplete(bufferMap, message.getCcList());
                utilAppendAddressForAutoComplete(bufferMap, message.getBccIntranetList());
                utilAppendAddressForAutoComplete(bufferMap, message.getBccList());
            }

            // trim to required number of addresses
            addresses = new ArrayList(bufferMap.values());
            if (addresses.size() > maxAddresses) {
                addresses = addresses.subList(0, maxAddresses);
            }

            // sort alphabetically ignoring case and quotes
            Collections.sort(addresses, new Comparator() {
                public int compare(Object o1, Object o2) {
                    String s1 = o1.toString().toLowerCase();
                    String s2 = o2.toString().toLowerCase();
                    try {
                        while(s1.charAt(0) == '"' || s1.charAt(0) == '\'') {
                            s1 = s1.substring(1);
                        }
                    }
                    catch (Exception e) {
                        // ignore
                    }
                    try {
                        while(s2.charAt(0) == '"' || s2.charAt(0) == '\'') {
                            s2 = s2.substring(1);
                        }
                    }
                    catch (Exception e) {
                        // ignore
                    }
                    return s1.compareTo(s2);
                }
            });

        } catch (MessagingDaoException e) {
            log.error("Error retrieving most recent addresses", e);
        }

        return addresses;
    }

    protected void utilAppendAddressForAutoComplete(Map bufferMap, Collection toAddList) {
        if (toAddList != null) {
            for(Iterator i=toAddList.iterator(); i.hasNext();) {
                String address = (String)i.next();
                try {
                    InternetAddress ia = new InternetAddress(address);
                    String email = ia.getAddress().toLowerCase();
                    if (!bufferMap.containsKey(email)) {
                        bufferMap.put(email, address);
                    }
                    else {
                        String val = (String)bufferMap.get(email);
                        if (val.length() < address.length()) {
                            bufferMap.put(email, address);
                        }
                    }
                }
                catch (Exception e) {
                    log.debug("Error parsing email address " + address + ":" + e.toString());
                }
            }
        }
    }

    /**
     * @param userId
     * @param preview
     * @param accountIdList
     */
    public void downloadPop3MessageSummary(String userId, boolean preview, List accountIdList) {
        ServerViewQueueItem item;

        item = new ServerViewQueueItem();
        item.setUserId(userId);
        item.setPreview(preview);
        item.setAccountIdList(accountIdList);

        MessagingQueue.getInstance().pushServerView(item);
    }

    /**
     * Called by check server view job to download email headers for POP3
     * accounts belonging to specified user.
     *
     * @param userId
     * @param preview
     * @param accountIdList a List of POP3 accountId to check. If null, check
     *                      all POP3 accounts for this user
     * @return
     * @throws MessagingException
     */
    public Map downloadPop3MessageSummaryListNow(String userId, boolean preview, List accountIdList) throws MessagingException {
        Map statusMap;
        Collection accounts;
        MessagingQueue mq;
        MessagingUserStatus mus;
        List summaryList;

        statusMap = new SequencedHashMap();

        mq = MessagingQueue.getInstance();
        mus = mq.getUserStatus(userId);

        try {
            if (mus.isPop3Busy()) {
                // no need to update, POP3 is busy
                if (!mus.isServerViewBusy()) {
                    mus.getTrackerServerView().update(ProgressTracker.STATUS_ERROR, 0, Application.getInstance().getMessage("messaging.label.statusBusy", "Your email account(s) is busy"));
                }
                return statusMap;
            }

            // run through each POP3 account and check server view
            mus.getTrackerServerView().update(ProgressTracker.STATUS_PROCESSING, 0, Application.getInstance().getMessage("messaging.label.statusPreparingServerView", "Preparing to update server view..."));
            accounts = getAccounts(userId);
            for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = (Account) iterator.next();

                if (accountIdList == null || accountIdList.contains(account.getAccountId())) {
                    // check only specified POP3 accounts

                    if (account instanceof Pop3Account) {
                        summaryList = downloadPop3MessageSummaryListNow((Pop3Account) account, preview, mus.getTrackerServerView());
                        statusMap.put(account, summaryList);
                    }
                }
            }
        } finally {
            if (ProgressTracker.STATUS_PROCESSING.equals(mus.getTrackerServerView().getStatus())) {
                mus.getTrackerServerView().update(ProgressTracker.STATUS_ERROR, 100, Application.getInstance().getMessage("messaging.label.statusUnexpectedTermination", "Unexpected termination of download"));
            }
        }

        return statusMap;
    }

    /**
     * Check a specific POP3 account.
     *
     * @param pop3Account
     * @param preview
     * @param tracker
     * @return
     * @throws MessagingException
     */
    protected List downloadPop3MessageSummaryListNow(Pop3Account pop3Account, boolean preview, ProgressTracker tracker) throws MessagingException {
        Session session;
        Store store = null;
        javax.mail.Folder folder = null;
        javax.mail.Message[] messages;
        Collection oldMessageIds;
        MessagingDao dao = (MessagingDao) getDao();
        SmtpAccount smtpAccount;
        long startTime, endTime;
        List summaryList;
        MessageSummary summary;
        int percent;

        startTime = System.currentTimeMillis();
        try {
            Application app = Application.getInstance();
            smtpAccount = getSmtpAccountByUserId(pop3Account.getUserId());

            tracker.update(ProgressTracker.STATUS_PROCESSING, 1, app.getMessage("messaging.label.statusConnectingTo", "Connecting to {0}", new String[]{pop3Account.getServerName()}));
            session = makeSession(smtpAccount);

            store = session.getStore("pop3");
            store.connect(pop3Account.getServerName(), pop3Account.getUsername(), pop3Account.getPassword());
            tracker.update(ProgressTracker.STATUS_PROCESSING, 2, app.getMessage("messaging.label.statusConnectedTo", "Connected to {0}", new String[]{pop3Account.getServerName()}));

            // get a list of Message-ID in database
            //oldMessageIds = dao.selectMessageIdHeaders(pop3Account.getAccountId());

            folder = store.getFolder("INBOX");
            folder.open(javax.mail.Folder.READ_ONLY);

            messages = folder.getMessages();
            tracker.update(ProgressTracker.STATUS_PROCESSING, 5, app.getMessage("messaging.label.statusProcessingMessages", "Processing {0} messages on {1}", new Object[]{new Integer(messages.length), pop3Account.getServerName()}));
            log.debug("Processing " + messages.length + " messages on " + pop3Account.getServerName());

            summaryList = new ArrayList();
            // process each message in the POP3's INBOX (peek)
            for (int c = 0; c < messages.length; c++) {
                MimeMessage m = (MimeMessage) messages[c];
                summary = makeMessageSummary(m, preview);

                oldMessageIds = dao.selectMessageIdHeaders(pop3Account.getAccountId(), ((MimeMessage) messages[c]).getMessageID());
                if (oldMessageIds.size() > 0) {
                    percent = 6 + (c * 100 / messages.length);
                    percent = percent > 99 ? 99 : percent;
                    tracker.update(ProgressTracker.STATUS_PROCESSING, percent, app.getMessage("messaging.label.statusProcessingOldMessage", "Processing {0} of {1} messages on {2} (old)", new Object[]{new Integer(c + 1), new Integer(messages.length), pop3Account.getServerName()}));
                    log.debug("Peek - processing old message " + (c + 1) + ": " + messages[c].getSubject());
                    summary.setNewMail(false);
                } else {
                    percent = 6 + (c * 100 / messages.length);
                    percent = percent > 99 ? 99 : percent;
                    tracker.update(ProgressTracker.STATUS_PROCESSING, percent, app.getMessage("messaging.label.statusProcessingMessage", "Processing {0} of {1} messages on {2}", new Object[]{new Integer(c + 1), new Integer(messages.length), pop3Account.getServerName()}));
                    log.debug("Peek - processing new message " + (c + 1) + ": " + messages[c].getSubject());
                    summary.setNewMail(true);
                }

                summaryList.add(summary);
            }

            tracker.update(ProgressTracker.STATUS_PROCESSING, 99, app.getMessage("messaging.label.statusClosingConnection", "Closing connection to {0}", new String[]{pop3Account.getServerName()}));

            // delete messages - false
            folder.close(false);

            return summaryList;

        } catch (javax.mail.MessagingException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (MessagingDaoException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            // make sure mailbox connection is closed
            if (store != null) {
                try {
                    if (folder != null && folder.isOpen()) {
                        folder.close(false);
                    }
                    store.close();
                } catch (javax.mail.MessagingException e) {
                    throw new MessagingException(e.getMessage(), e);
                }
            }
            endTime = System.currentTimeMillis();
            log.debug("Peeking of messages completed in " + (endTime - startTime) + "ms");
            tracker.update(ProgressTracker.STATUS_COMPLETED, 100, Application.getInstance().getMessage("messaging.label.statusCompletedProcessing", "Completed processing messages on {0}", new Object[]{pop3Account.getServerName()}));
        }

    }

    /**
     * This method creates a MessageSummary object from a MimeMessage. This
     * method is used in "server view" mode, to retrieve only partial email
     * contents.
     *
     * @param m
     * @param preview true to include preview of message, false otherwise
     * @return
     */
    protected MessageSummary makeMessageSummary(MimeMessage m, boolean preview) throws javax.mail.MessagingException, IOException {
        MessageSummary summary;

        summary = new MessageSummary();

        // message-ID
        summary.setMessageId(m.getMessageID());

        // set FROM field
        try {
            if (m.getFrom() == null) {
                log.warn("FROM field not specified in POP3 message");
                summary.setFrom("-n/a- *Spam*");
            } else if (m.getFrom().length == 0) {
                log.warn("FROM field in POP3 message is empty");
                summary.setFrom("-n/a- *Spam*");
            } else if (m.getFrom().length > 1) {
                log.warn("Multiple FROM field entries in POP3 message not supported");
                summary.setFrom(m.getFrom()[0].toString());
            } else {
                summary.setFrom(m.getFrom()[0].toString());
            }
        } catch (AddressException e) {
            log.warn(e.getMessage(), e);
        }

        if (preview) {
            summary.setPreview(getMessageLines(m, PREVIEW_NO_LINES));
        } else {
            summary.setPreview(null);
        }
        summary.setReceivedDate(m.getSentDate());
        summary.setSize(m.getSize());
        summary.setSubject(m.getSubject());

        List toList = Util.convertAddressesToRecipientsList(m.getRecipients(MimeMessage.RecipientType.TO));
        String to = Util.convertRecipientsListToString(toList);
        summary.setTo(to);

        return summary;
    }


    /**
     * Retrieve xx number of lines from the MimeMessage object. This method
     * will retrieve based on the message's raw input stream.
     *
     * @param m       message object
     * @param noLines specifies number of lines to retrieve
     * @return String object of the number of lines read
     * @throws javax.mail.MessagingException
     * @throws IOException
     */
    protected String getMessageLines(MimeMessage m, int noLines) throws javax.mail.MessagingException, IOException {
        BufferedReader br;
        StringBuffer sb;
        String line;
        int no;
        Enumeration enumeration;

        sb = new StringBuffer();

        // get all headers
        enumeration = m.getAllHeaderLines();
        while (enumeration.hasMoreElements()) {
            String s = (String) enumeration.nextElement();
            sb.append(s);
            sb.append("\n");
        }

        int headerLength = sb.toString().length();
        log.debug("Size of headers: " + headerLength);

        // get xx number of lines
        br = new BufferedReader(new InputStreamReader(m.getRawInputStream()));
        line = br.readLine();
        no = 1;
        while (line != null && no <= noLines) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
            no++;
        }

        int contentLength = sb.toString().length() - headerLength;
        log.debug("Size of content: " + contentLength);

        return sb.toString();
    }

    public void handleSecurityEvent(SecurityEvent evt){
        String name = evt.getEventName();
        if(name.equals(SecurityService.EVENT_USER_ADDED)){
            createIntranetAccount(evt);
        }
    }

    /**
     * auto creates an intranet account for the just created user
     * @param event
     */
    private void createIntranetAccount(SecurityEvent event){

        Principal principal = event.getPrincipal();
        String userId = principal.getId();
        try{
             IntranetAccount account = getIntranetAccountByUserId(userId);
             boolean hasIntranetAccount;
                if (account == null) {
                    hasIntranetAccount = false;
                } else {
                    hasIntranetAccount = true;
                }
            if (hasIntranetAccount) {
                // account already exist
                String msg = "Cannot create intranet account. Already exist";
                Log.getLog(this.getClass()).error(msg);
            }else{
                // setting intranet account values
                Application application = Application.getInstance();
                SecurityService sec = (SecurityService) application.getService(SecurityService.class);
                User user = sec.getUser(userId);
                IntranetAccount intranetAccount;
                intranetAccount = new IntranetAccount();
                intranetAccount.setId(UuidGenerator.getInstance().getUuid());
                intranetAccount.setUserId(userId);
                intranetAccount.setIntranetUsername(user.getUsername());
                intranetAccount.setName("Intranet Messaging Account");
                intranetAccount.setFilterEnabled(false);
                intranetAccount.setFromAddress((String)user.getProperty("email1"));

                // setting SMTP account values

                SmtpAccount smtpAccount;
                smtpAccount = new SmtpAccount();
                smtpAccount.setSmtpAccountId(UuidGenerator.getInstance().getUuid());
                smtpAccount.setUserId(user.getId());
                smtpAccount.setName("SMTP account");

                SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
                Map propertyMap = new HashMap();
                propertyMap = setup.getAll();
                for (Iterator i=propertyMap.keySet().iterator(); i.hasNext();) {
                    String property = (String)i.next();
                    if(property.equalsIgnoreCase("siteSmtpServer")){
                        String value = (String)propertyMap.get(property);
                        smtpAccount.setServerName(value);
                    }
                }
                // default port number
                smtpAccount.setServerPort(25);
                smtpAccount.setAnonymousAccess(true);
                addIntranetAccount(intranetAccount,smtpAccount);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}