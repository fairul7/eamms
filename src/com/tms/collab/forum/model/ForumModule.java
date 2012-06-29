package com.tms.collab.forum.model;

import kacang.Application;
import kacang.model.DefaultModule;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.*;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;

import kacang.util.Log;
import kacang.util.UuidGenerator;
import java.util.*;
import com.tms.util.MailUtil;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.ekms.setup.model.SetupException;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.util.ParserException;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 5, 2003
 * Time: 1:38:28 PM
 * To change this template use Options | File Templates.
 */
public class ForumModule extends DefaultModule implements SearchableModule
{
    public static final String LABEL_FORUM_KEY = "forumId";
    public static final String LABEL_THREAD_KEY = "threadId";
    public static final String LABEL_MESSAGE_KEY = "messageId";
    public static final String LABEL_MESSAGE_REPLY_KEY = "parentMessageId";

    public static final String SETTINGS_LABEL_TYPE = "forumType";

    public static final String FORUM_NAME_EXIST = "Forum Name already exist! Duplicate Forum Name is not allowed";
    public static final String FORUM_TYPE_THREADED = "1";
    public static final String FORUM_TYPE_NON_THREADED = "0";

    public static final String DEFAULT_TOPIC_PATH = "/ekms/forums/forumTopicForm.jsp";
    public static final String DEFAULT_MESSAGE_PATH = "/ekms/forums/forumMessageForm.jsp";
    public static final String PROPERTY_TOPIC_PATH = "com.tms.collab.forum.topicPath";
    public static final String PROPERTY_MESSAGE_PATH = "com.tms.collab.forum.messagePath";

    public void init()
    {

    }

    public Collection getAllForum(String userId) throws ForumException
    {
        return getForums(userId, 0, -1, null, false, null, null, null);
    }

    public Collection getForums(String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria, String isActive) throws ForumException
    {
        Collection forums = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            forums = dao.selectForums(null, userId, start, numOfRows, sortBy, isDescending, searchBy, searchCriteria, isActive);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return forums;
    }

    public Collection getForums(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            return dao.selectForums(query, start, maxResults, sort, descending);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    /**
     * Retrieves forums that groups have access to as users.
     * @param userId
     * @param query
     * @param active
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @param includeCount true to include counts for topics and messages
     * @return
     * @throws ForumException
     */
    public Collection getForumsByUserGroupAccess(String userId, DaoQuery query, String active, String sort, boolean desc, int start, int rows, boolean includeCount) throws ForumException
    {
        Collection forums = new ArrayList();
        try
        {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Iterator itGroups = ss.getUserGroups(userId).iterator();
            List groupIds = new ArrayList();
            while(itGroups.hasNext())
            {
                groupIds.add(((Group)itGroups.next()).getId());
            }
            groupIds.add(userId);
            ForumDao dao = (ForumDao)getDao();
            forums = dao.selectForumsByUserGroupAccess((String[])groupIds.toArray(new String[0]), query, active, sort, desc, start, rows);

            if (includeCount)
            {
                dao.setNumOfMessage(forums);
                dao.setNumOfThread(forums);
            }
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return forums;
    }

    /**
     * Retrieves count of forums that groups have access to as users.
     * @param userId
     * @param query
     * @param active
     * @return
     * @throws ForumException
     */
    public int getNumOfForumsByUserGroupAccess(String userId, DaoQuery query, String active) throws ForumException
    {
        try
        {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Iterator itGroups = ss.getUserGroups(userId).iterator();
            List groupIds = new ArrayList();
            while(itGroups.hasNext())
            {
                groupIds.add(((Group)itGroups.next()).getId());
            }
            groupIds.add(userId);
            ForumDao dao = (ForumDao)getDao();
            return dao.selectForumCountByUserGroupAccess((String[])groupIds.toArray(new String[0]), query, active);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    /**
     * Retrieves forums that groups have access to as moderators.
     * @param userId
     * @param searchBy
     * @param searchCriteria
     * @param active
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @param includeCount true to include counts for topics and messages
     * @return
     * @throws ForumException
     */
    public Collection getForumsByModeratorGroupAccess(String userId, String searchBy, String searchCriteria, String active, String sort, boolean desc, int start, int rows, boolean includeCount) throws ForumException
    {
        Collection forums = new ArrayList();
        try
        {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Iterator itGroups = ss.getUserGroups(userId).iterator();
            List groupIds = new ArrayList();
            while(itGroups.hasNext())
            {
                groupIds.add(((Group)itGroups.next()).getId());
            }
            groupIds.add(userId);
            ForumDao dao = (ForumDao)getDao();
            forums = dao.selectForumsByModeratorGroupAccess((String[])groupIds.toArray(new String[0]), searchBy, searchCriteria, active, sort, desc, start, rows);

            if (includeCount)
            {
                dao.setNumOfMessage(forums);
                dao.setNumOfThread(forums);
            }

        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return forums;
    }
    
    //Retrieve forums for Moderator
    public Collection getForumsByModerator(String userId, String searchBy, String searchCriteria, String active, String sort, boolean desc, int start, int rows, boolean includeCount) throws ForumException
    {
        Collection forums = new ArrayList();
        try
        {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Iterator itGroups = ss.getUserGroups(userId).iterator();
            List groupIds = new ArrayList();
            while(itGroups.hasNext())
            {
                groupIds.add(((Group)itGroups.next()).getId());
            }
            groupIds.add(userId);
            ForumDao dao = (ForumDao)getDao();
            forums = dao.selectForumsByModerator((String[])groupIds.toArray(new String[0]), searchBy, searchCriteria, active, sort, desc, start, rows);

            if (includeCount)
            {
                dao.setNumOfMessage(forums);
                dao.setNumOfThread(forums);
            }

        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return forums;
    }
    
    //Retrieve forums for Forum Manager
    public Collection getForumsByManager(String searchBy, String searchCriteria, String active, String sort, boolean desc, int start, int rows, boolean includeCount) throws ForumException
    {
        Collection forums = new ArrayList();
        try
        {
            
            ForumDao dao = (ForumDao)getDao();
            forums = dao.getForumsByManager(searchBy, searchCriteria, active, sort, desc, start, rows);

            if (includeCount)
            {
                dao.setNumOfMessage(forums);
                dao.setNumOfThread(forums);
            }

        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return forums;
    }

    /**
     * Retrieves count of forums that groups have access to as moderators.
     * @param userId
     * @param searchBy
     * @param searchCriteria
     * @param active
     * @return
     * @throws ForumException
     */
    public int getNumOfForumsByModeratorGroupAccess(String userId, String searchBy, String searchCriteria, String active) throws ForumException
    {
        try
        {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Iterator itGroups = ss.getUserGroups(userId).iterator();
            List groupIds = new ArrayList();
            while(itGroups.hasNext())
            {
                groupIds.add(((Group)itGroups.next()).getId());
            }
            groupIds.add(userId);
            ForumDao dao = (ForumDao)getDao();
            return dao.selectForumCountByModeratorGroupAccess((String[])groupIds.toArray(new String[0]), searchBy, searchCriteria, active);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }
    
    //Retrieve Count forums for moderator
    public int getNumOfForumsByModerator(String userId, String searchBy, String searchCriteria, String active) throws ForumException
    {
        try
        {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Iterator itGroups = ss.getUserGroups(userId).iterator();
            List groupIds = new ArrayList();
            while(itGroups.hasNext())
            {
                groupIds.add(((Group)itGroups.next()).getId());
            }
            groupIds.add(userId);
            ForumDao dao = (ForumDao)getDao();
            return dao.selectForumCountByModerator((String[])groupIds.toArray(new String[0]), searchBy, searchCriteria, active);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }
    
    //Retrieve count forums for Forum Manager
    public int getNumOfForumsByManager(String searchBy, String searchCriteria, String active) throws ForumException
    {
        try
        {         
            ForumDao dao = (ForumDao)getDao();
            return dao.getNumOfForumsByManager(searchBy, searchCriteria, active);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public Forum getForum(String forumId, String userId) throws ForumException
    {
        Forum forum = null;
        ForumDao dao = (ForumDao)getDao();
        try
        {
            forum = dao.selectForum(forumId, userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return forum;
    }

    public Collection getAllThread(String forumId, String userId) throws ForumException
    {
        Collection threads = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            threads = dao.selectFullThreads(forumId, userId, 0, -1, null, false, null, null);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return threads;
    }

    public Collection getThreads(String forumId, String userId, int start, int numOfRows) throws ForumException
    {
        Collection threads = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            threads = dao.selectThreads(forumId, userId, start, numOfRows, null, false, null, null);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return threads;
    }

    public Collection getThreads(String forumId, String userId, int start, int numOfRows, String sortBy, boolean isDescending) throws ForumException
    {
        Collection threads = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            threads = dao.selectThreads(forumId, userId, start, numOfRows, sortBy, isDescending, null, null);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return threads;
    }

    public Collection getThreads(String forumId, String userId, int start, int numOfRows, String searchBy, String searchCriteria) throws ForumException
    {
        Collection threads = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            threads = dao.selectThreads(forumId, userId, start, numOfRows, null, false, searchBy, searchCriteria);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return threads;
    }

    public Collection getThreads(String forumId, String userId, String sortBy, boolean isDescending) throws ForumException
    {
        Collection threads = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            threads = dao.selectThreads(forumId, userId, 0, -1, sortBy, isDescending, null, null);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return threads;
    }

    public Collection getThreads(String forumId, String userId, String searchBy, String searchCriteria) throws ForumException
    {
        Collection threads = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            threads = dao.selectThreads(forumId, userId, 0, -1, null, false, searchBy, searchCriteria);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return threads;
    }

    public Collection getThreads(String forumId, String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws ForumException
    {
        Collection threads = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            threads = dao.selectThreads(forumId, userId, start, numOfRows, sortBy, isDescending, searchBy, searchCriteria);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }

        return threads;
    }

    public Thread getThread(String threadId, String userId) throws ForumException
    {
        Thread thread = null;
        ForumDao dao = (ForumDao)getDao();
        try
        {
            thread = dao.selectThread(threadId, userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return thread;
    }

    /**
     *
     * @param parent
     * @param rows
     * @param userId
     * @return A Collection of HashMaps, each of which may be a forum, thread or message. Returns empty List if parent is not a forum.
     * @throws ForumException
     */
    public Collection getLatestMessages(Object parent, int rows, String userId) throws ForumException
    {
        Collection messages = new ArrayList();
        ForumDao dao = (ForumDao) getDao();
        try
        {
            if(parent instanceof Forum)
                messages = dao.selectLatestMessages(((Forum)parent).getForumId(), userId, rows);
            else
                return new ArrayList();
/*
            else if(parent instanceof Thread)
                messages = dao.selectMessages(((Thread)parent).getThreadId(), userId, 0, rows, "modificationDate", true, null, null);
*/
        }
        catch(DaoException e)
        {
            throw new ForumException(e.getMessage(), e);
        }
        return messages;
    }

    public Collection getAllPostedMessage(String threadId, String userId) throws ForumException
    {
        return getPostedMessages(threadId, userId, 0, -1, null, false, null, null);
    }

    public Collection getPostedMessages(String threadId, String userId, int start, int numOfRows) throws ForumException
    {
        return getPostedMessages(threadId, userId, start, numOfRows, null, false, null, null);
    }

    public Collection getPostedMessages(String threadId, String userId, int start, int numOfRows, String sortBy, boolean isDescending) throws ForumException
    {
        return getPostedMessages(threadId, userId, start, numOfRows, sortBy, isDescending, null, null);
    }

    public Collection getPostedMessages(String threadId, String userId, int start, int numOfRows, String searchBy, String searchCriteria) throws ForumException
    {
        return getPostedMessages(threadId, userId, start, numOfRows, null, false, searchBy, searchCriteria);
    }

    public Collection getPostedMessages(String threadId, String userId, String sortBy, boolean isDescending) throws ForumException
    {
        return getPostedMessages(threadId, userId, 0, -1, sortBy, isDescending, null, null);
    }

    public Collection getPostedMessages(String threadId, String userId, String searchBy, String searchCriteria) throws ForumException
    {
        return getPostedMessages(threadId, userId, 0, -1, null, false, searchBy, searchCriteria);
    }

    public Collection getPostedMessages(String threadId, String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String seachCriteria) throws ForumException
    {
        Collection messages = new ArrayList();
        ForumDao dao = (ForumDao)getDao();
        try
        {
            messages = dao.selectMessages(threadId, userId, start, numOfRows, sortBy, isDescending, searchBy, seachCriteria);
        }
        catch(Exception e)
        {
            Log.getLog(ForumModule.class).error(e.getMessage(), e);
            throw new ForumException(e.getMessage());
        }
        return messages;
    }

    public Message getPostedMessage(String messageId, String userId) throws ForumException
    {
        Message message = null;
        ForumDao dao = (ForumDao)getDao();
        try
        {
            message = dao.selectMessage(messageId, userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return message;
    }

    public int getNumOfForum(String searchBy, String searchCriteria) throws ForumException
    {
        return getNumOfForum(searchBy, searchCriteria, null);
    }


    public int getNumOfForum(String searchBy, String searchCriteria, String isActive) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        int numOfForum = 0;
        try
        {
            numOfForum = dao.selectForumCount(searchBy, searchCriteria, isActive);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return numOfForum;
    }

    public int getNumOfThread(String searchBy, String searchCriteria) throws ForumException
    {
        return getNumOfThread(null, searchBy, searchCriteria);
    }

    public int getNumOfThread(String forumId, String searchBy, String searchCriteria) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        int numOfThread = 0;
        try
        {
            numOfThread = dao.selectThreadCount(forumId, searchBy, searchCriteria);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return numOfThread;
    }

    public int getNumOfMessage(String searchBy, String searchCriteria) throws ForumException
    {
        return getNumOfMessageByForum(null, searchBy, searchCriteria);
    }

    public int getNumOfMessageByForum(String forumId, String searchBy, String searchCriteria) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        int numOfMessage = 0;
        try
        {
            numOfMessage = dao.selectMessageCountPerForum(forumId, searchBy, searchCriteria);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return numOfMessage;
    }

    public int getNumOfMessageByThread(String threadId, String searchBy, String searchCriteria) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        int numOfMessage = 0;
        try
        {
            numOfMessage = dao.selectMessageCountPerThread(threadId, searchBy, searchCriteria);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
        return numOfMessage;
    }

    public String getForumId(String id, String type) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            return dao.selectForumId(id, type);
        }
        catch(Exception e)
        {
            Log.getLog(ForumModule.class).error(e.getMessage(), e);
            throw new ForumException(e.getMessage());
        }
    }

    public String getThreadId(String messageId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            return dao.selectThreadId(messageId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public String getForumName(String forumId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            return dao.selectForumName(forumId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public String getForumDesc(String forumId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            return dao.selectForumDesc(forumId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public String getThreadSubject(String threadId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            return dao.selectThreadSubject(threadId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public String getThreadContent(String threadId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            return dao.selectThreadContent(threadId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public String getMessageSubject(String messageId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            return dao.selectMessageSubject(messageId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public void createForum(Forum forum, String[] userSelectBoxArray, String[] userModeratorSelectBoxArray) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        Log.getLog(ForumModule.class).debug("Creating forum - " + forum.getName() + "with ID = " + forum.getForumId());
        try
        {
//            forum.setThreadId(UuidGenerator.getInstance().getUuid());
//            forum.setOwnerId(userId);
//            forum.setCreationDate(new Date());
//            forum.setModificationDate(forum.getCreationDate());

            forum.setForumId(UuidGenerator.getInstance().getUuid());
            forum.setCreationDate(new Date());
            forum.setModificationDate(forum.getCreationDate());
            String name = forum.getName().trim();
            forum.setName(Profanity.getInstance().filter(name));

            String description = forum.getDescription();
            Log.getLog(ForumModule.class).debug("description b4 filter = " + description);
            forum.setDescription(Profanity.getInstance().filter(description));
            Log.getLog(ForumModule.class).debug("description after filter = " + description);

            dao.insertForum(forum, userSelectBoxArray, userModeratorSelectBoxArray);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public void createThread(Thread thread, String username) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            // check permission
            SecurityService sec = (SecurityService)Application.getInstance().getService(SecurityService.class);
            Collection users = sec.getUsersByUsername(username);
            if (!users.iterator().hasNext())
            {
                throw new SecurityException("User " + username + " not available");
            }
            User user = (User)users.iterator().next();
            if (isForumUser(thread.getForumId(), user.getId()) || isForumModerator(thread.getForumId(), user.getId()))
            {
                // create thread
            	if("anonymous".equals(username)){
            		thread.setOwnerId(thread.getOwnerId()+" (anonymous)");	
            	}else{
            		thread.setOwnerId(username);}
                thread.setThreadId(UuidGenerator.getInstance().getUuid());
                thread.setRootMessageId(UuidGenerator.getInstance().getUuid());
                thread.setCreationDate(new Date());
                thread.setModificationDate(thread.getCreationDate());

                String subject = thread.getSubject();
                thread.setSubject(Profanity.getInstance().filter(subject));
                String content = thread.getContent();
                thread.setContent(Profanity.getInstance().filter(content));

                dao.insertThread(thread);

                sendSubscriptionMail(thread, username);
            }
            else
            {
                throw new SecurityException("User " + username + " has no permission to create thread");
            }
        }
        catch(Exception e)
        {
            throw new ForumException("Unable to create thread: " + e.getMessage());
        }
    }

    public void postMessage(Message message, String username) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            // check permission
            SecurityService sec = (SecurityService)Application.getInstance().getService(SecurityService.class);
            Collection users = sec.getUsersByUsername(username);
            if (!users.iterator().hasNext())
            {
                throw new SecurityException("User " + username + " not available");
            }
            User user = (User)users.iterator().next();
            if (isForumUser(message.getForumId(), user.getId()) || isForumModerator(message.getForumId(), user.getId()))
            {
                // create message
            	if("anonymous".equals(username)){
            		message.setOwnerId(message.getOwnerId()+" (anonymous)");	
            	}else{
                message.setOwnerId(username);}
                message.setMessageId(UuidGenerator.getInstance().getUuid());
                message.setCreationDate(new Date());
                message.setModificationDate(message.getCreationDate());

                String subject = message.getSubject();
                message.setSubject(Profanity.getInstance().filter(subject));
                String content = message.getContent();
                message.setContent(Profanity.getInstance().filter(content));

                dao.insertMessage(message);

                sendSubscriptionMail(message, username);
            }
            else
            {
                throw new SecurityException("User " + username + " has no permission to post message");
            }
        }
        catch(Exception e)
        {
            throw new ForumException("Unable to post message: " + e.getMessage());
        }
    }

    public void editForum(Forum forum, String userId, String[] userSelectBoxArray, String[] userModeratorSelectBoxArray) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            String description = forum.getDescription();
            Log.getLog(ForumModule.class).debug("description b4 filter = " + description);
            forum.setDescription(Profanity.getInstance().filter(description));
            Log.getLog(ForumModule.class).debug("description after filter = " + description);

            dao.updateForum(forum, userId, userSelectBoxArray, userModeratorSelectBoxArray);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }
    
    public void deleteForum(Message message) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {          
            dao.deleteForum(message);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public void editThread(Thread thread, String userId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            dao.updateThread(thread, userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public void editMessage(Message message, String userId) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            dao.updateMessage(message, userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public void deleteForum(String forumId, String userId) throws ForumException
    {
        deleteForum(forumId, userId, true);
    }

    public void deleteForum(String forumId, String userId, boolean force) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            dao.deleteForum(forumId, userId, force);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public void deleteThread(String threadId, String userId) throws ForumException
    {
        deleteThread(threadId, userId, true);
    }

    public void deleteThread(String threadId, String userId, boolean force) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            dao.deleteThread(threadId, userId, force);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    public void deleteMessage(String messageId, String userId) throws ForumException
    {
        deleteMessage(messageId, userId, true);
    }

    public void deleteMessage(String messageId, String userId, boolean force) throws ForumException
    {
        ForumDao dao = (ForumDao)getDao();
        try
        {
            dao.deleteMessage(messageId, userId, force);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }

    /**
     * Retrieves a forum thread that contains all messages in threaded (hierarchical) structure.
     * @param threadId
     * @param userId
     * @return The root object of the thread.
     * @throws ForumException
     */
    public Thread getPostedMessagesInThreadedFormat(String threadId, String userId) throws ForumException
    {
        try {
            // get thread and messages
            Thread thread = getThread(threadId, userId);
            Collection messageList = getAllPostedMessage(threadId, userId);

            // first put all messages in a Map for quick lookup
            Map messageMap = new HashMap();
            for (Iterator i=messageList.iterator(); i.hasNext();)
            {
                Message message = (Message)i.next();
                messageMap.put(message.getMessageId(), message);
            }
            // form the tree
            for (Iterator i=messageList.iterator(); i.hasNext();)
            {
                Message message = (Message)i.next();
                if (message.getParentMessageId() == null || message.getParentMessageId().equals(message.getThreadId()))
                {
                    thread.addMessage(message);
                }
                else
                {
                    Message parent = (Message)messageMap.get(message.getParentMessageId());
                    if (parent != null)
                    {
                        parent.addMessage(message);
                    }
                }
            }
            return thread;
        } catch (Exception e) {
            Log.getLog(getClass()).error("Unable to retrieve messages in threaded format for " + threadId, e);
            throw new ForumException("Unable to retrieve messages in threaded format for " + threadId);
        }
    }

    public void setLastPost(Collection list) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            dao.setLastPost(list);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public Collection getCategories() throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            return dao.selectCategories();
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    /** Forum subscriptions and profile handling */
    public Collection getSubscriptions(String userId) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            return dao.selectSubscriptions(userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public Collection getSubscribers(String forumId) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            return dao.selectSubscribers(forumId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public void addSubscription(String userId, String forumId) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            dao.insertSubscription(userId, forumId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public void deleteSubscriptionByUser(String userId) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            dao.deleteSubscriptionByUser(userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public void deleteSubscriptionByForum(String forumId) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            dao.deleteSubscriptionByForum(forumId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public Map getSettings(String userId) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            return dao.selectUserSettings(userId);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public void saveSettings(String userId, Map settings) throws ForumException
    {
        try
        {
            ForumDao dao = (ForumDao) getDao();
            Map map = dao.selectUserSettings(userId);
            if(map.size() > 0)
                dao.updateSettings(userId, settings);
            else
                dao.insertSettings(userId, settings);
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    /**
     * This method sends subscription mails and is called in the event of a
     * message posting. The mail path is which it includes as part of a anchor
     * link within the mail message itself is extracted from the configurable
     * property: com.tms.collab.forum.MailerPath
     *
     * @param thread Message object in which the event was generated for
     * @throws ForumException if an error occurs
     *
     */
    protected void sendSubscriptionMail(Thread thread, String username) throws ForumException
    {
        try
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = null;
            Collection list = service.getUsersByUsername(username);
            if(list.size() > 0)
            {
                Collection users = getSubscribers(thread.getForumId());
                if(users.size() > 0)
                {
                    user = (User) list.iterator().next();
                    ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                    Forum forum = module.getForum(thread.getForumId(), user.getId());
                    /* Formulating server path */
                    String serverUrl = "http://";
                    SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
                    try
                    {
                        serverUrl = setup.get("siteUrl");
                    }
                    catch (SetupException e)
                    {
                        Log.getLog(ForumModule.class).error(e);
                    }
                    String forumPath = Application.getInstance().getProperty(ForumModule.PROPERTY_MESSAGE_PATH);
                    if (forumPath == null) {
                        forumPath = DEFAULT_MESSAGE_PATH;
                    }
                    serverUrl += forumPath;
                    if (serverUrl.indexOf("?") >= 0) {
                        serverUrl += "&";
                    }
                    else {
                        serverUrl += "?";
                    }
                    serverUrl += LABEL_THREAD_KEY + "=" + thread.getThreadId();
                    /* Composing mail message */
                    StringBuffer mail = new StringBuffer();
                    mail.append("\n");
                    mail.append("<i>" + user.getUsername() + "</i> " + Application.getInstance().getMessage("forum.label.mailNewTopic") + ": <b>" + thread.getSubject() + "</b>\n\n");
                    mail.append("- " + Application.getInstance().getMessage("forum.label.subject") + ": \n");
                    mail.append(thread.getSubject() + "\n\n");
                    mail.append("- " + Application.getInstance().getMessage("forum.label.message") + ":\n");
                    mail.append(thread.getContent() + "\n\n");
                    mail.append(Application.getInstance().getMessage("forum.label.mailPostMsg") + ":\n");
                    mail.append("<a href=\"" + serverUrl);
                    mail.append("\">" + serverUrl + "</a>");
                    String formattedMail = StringUtils.replace(mail.toString(), "\n", "<br>");
                    /* Retrieving email addresses */
                    for(Iterator i = users.iterator(); i.hasNext();)
                    {
                        User subscriber = (User) i.next();
                        if(!(subscriber.getProperty("email1") == null || "".equals(subscriber.getProperty("email1")))) {
                            String email = (String)subscriber.getProperty("email1");
                            MailUtil.sendEmail(null, true, null, email, null, null, forum.getName() + " : " + thread.getSubject(), formattedMail);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    protected void sendSubscriptionMail(Message message, String username) throws ForumException
    {
        try
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = null;
            Collection list = service.getUsersByUsername(username);
            if(list.size() > 0)
            {
                Collection users = getSubscribers(message.getForumId());
                if(users.size() > 0)
                {
                    user = (User) list.iterator().next();
                    ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                    Forum forum = module.getForum(message.getForumId(), user.getId());
                    Thread thread = module.getThread(message.getThreadId(), user.getId());
                    /* Formulating server path */
                    String serverUrl = "http://";
                    SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
                    try
                    {
                        serverUrl = setup.get("siteUrl");
                    }
                    catch (SetupException e)
                    {
                        Log.getLog(ForumModule.class).error(e);
                    }
                    String forumPath = Application.getInstance().getProperty(ForumModule.PROPERTY_MESSAGE_PATH);
                    if (forumPath == null) {
                        forumPath = DEFAULT_MESSAGE_PATH;
                    }
                    serverUrl += forumPath;
                    if (serverUrl.indexOf("?") >= 0) {
                        serverUrl += "&";
                    }
                    else {
                        serverUrl += "?";
                    }
                    serverUrl += LABEL_THREAD_KEY + "=" + thread.getThreadId() + "&" + LABEL_MESSAGE_REPLY_KEY + "=" + message.getMessageId();
                    /* Composing mail message */
                    StringBuffer mail = new StringBuffer();
                    mail.append("\n");
                    mail.append("<i>" + user.getUsername() + "</i> " + Application.getInstance().getMessage("forum.label.mailNewMsg") + ": <b>" + thread.getSubject() + "</b>\n\n");
                    mail.append("- " + Application.getInstance().getMessage("forum.label.subject") + ":\n");
                    mail.append(message.getSubject() + "\n\n");
                    mail.append("- " + Application.getInstance().getMessage("forum.label.message") + ":\n");
                    mail.append(message.getContent() + "\n\n");
                    mail.append(Application.getInstance().getMessage("forum.label.mailReplyMsg") + ":\n");
                    mail.append("<a href=\"" + serverUrl);
                    mail.append("\">" + serverUrl + "</a>");
                    String formattedMail = StringUtils.replace(mail.toString(), "\n", "<br>");
                    /* Retrieving email addresses */
                    for(Iterator i = users.iterator(); i.hasNext();)
                    {
                        User subscriber = (User) i.next();
                        if(!(subscriber.getProperty("email1") == null || "".equals(subscriber.getProperty("email1")))) {
                            String email = (String)subscriber.getProperty("email1");
                            MailUtil.sendEmail(null, true, null, email, null, null, forum.getName() + " : " + thread.getSubject(), formattedMail);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            throw new ForumException(e.toString(), e);
        }
    }

    public boolean isSearchSupported()
    {
        return true;
    }

    public boolean isFullTextSearchSupported()
    {
        return false;
    }

    protected SearchResultItem assembleSearchItem(String object, String key, String title, String description, Date date)
    {
        SearchResultItem item = new SearchResultItem();
        Map map = new HashMap();
        map.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
        map.put(SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS, object);
        map.put(SearchableModule.SEARCH_PROPERTY_KEY, key);
        map.put(SearchableModule.SEARCH_PROPERTY_TITLE, title);
        map.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, description);
		map.put(SearchableModule.SEARCH_PROPERTY_DATE, date);
        item.setValueMap(map);
        item.setScore(null);
        return item;
    }

    /**
     * Check to see if a forum is accessible to a user.
     * @param forumId
     * @param userId
     * @return
     */
    public boolean isForumUser(String forumId, String userId) throws ForumException {
        DaoQuery query = new DaoQuery();
        query.addProperty(new OperatorEquals("ff.forumId", forumId, DaoOperator.OPERATOR_AND));
        Collection forum = getForumsByUserGroupAccess(userId, query, "1", null, false, 0, 1, false);
        return (forum.iterator().hasNext());
    }

    /**
     * Check to see if a forum can be moderated by a user.
     * @param forumId
     * @param userId
     * @return
     */
    public boolean isForumModerator(String forumId, String userId) throws ForumException {
        Collection forum = getForumsByModeratorGroupAccess(userId, "forumId", forumId, null, null, false, 0, 1, false);
        return (forum.iterator().hasNext());
    }

    public boolean isForumModeratorCheck(String forumId, String userId) throws ForumException {
    	SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        Iterator itGroups;
		try {
			itGroups = ss.getUserGroups(userId).iterator();
			List groupIds = new ArrayList();
	        while(itGroups.hasNext())
	        {
	            groupIds.add(((Group)itGroups.next()).getId());
	        }
	        groupIds.add(userId);
	        ForumDao dao = (ForumDao)getDao();

				return dao.isForumModeratorCheck((String[])groupIds.toArray(new String[0]), forumId);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        
    }
    
    //To check whether user is Moderator
    public int countNumOfForumsByModeratorGroupAccess(String userId,String active) throws ForumException
    {
        try
        {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Iterator itGroups = ss.getUserGroups(userId).iterator();
            List groupIds = new ArrayList();
            while(itGroups.hasNext())
            {
                groupIds.add(((Group)itGroups.next()).getId());
            }
            groupIds.add(userId);
            ForumDao dao = (ForumDao)getDao();
            return dao.selectForumCountByModeratorGroupAccess((String[])groupIds.toArray(new String[0]),active);
        }
        catch(Exception e)
        {
            throw new ForumException(e.getMessage());
        }
    }
    
	public SearchResult search(String query, int start, int rows, String userId)  throws QueryException
	{
		return search(query, start, rows, userId, null, null);
	}

	public SearchResult searchFullText(String s, int i, int i1, String s1) throws QueryException
	{
		return new SearchResult();
	}

	public SearchResult search(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
		SearchResult results = new SearchResult();
		int count = 0;
		try
		{
			SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
			ForumDao dao = (ForumDao) getDao();
			/* Formulating Forum Query */
			Collection groups = service.getUserGroups(userId);
			Collection groupId = new ArrayList();
			for(Iterator i = groups.iterator(); i.hasNext();)
			{
				Group group = (Group) i.next();
				groupId.add(group.getId());
			}
			OperatorParenthesis parenthesisForum = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
			parenthesisForum.addOperator(new OperatorLike("name", query, null));
			parenthesisForum.addOperator(new OperatorLike("description", query, DaoOperator.OPERATOR_OR));
			DaoQuery forumQuery = new DaoQuery();
			forumQuery.addProperty(parenthesisForum);
			if(!(startDate == null))
				forumQuery.addProperty(new OperatorGreaterThanEquals("modificationDate", startDate, DaoOperator.OPERATOR_AND));
			if(!(endDate == null))
				forumQuery.addProperty(new OperatorLessThanEquals("modificationDate", endDate, DaoOperator.OPERATOR_AND));
			Collection forums = getForumsByUserGroupAccess(userId, forumQuery, "1", "modificationDate", true, start, rows, false);
			count += getNumOfForumsByUserGroupAccess(userId, forumQuery, "1");
			for(Iterator i = forums.iterator(); i.hasNext();)
			{
				Forum forum = (Forum) i.next();
				String title = Application.getInstance().getMessage("general.label.forum") + ": " + forum.getName();
				String desc = "";
				try
				{
					com.tms.util.StringUtil util = com.tms.util.StringUtil.getInstance();
					desc = util.filterLength(util.filterHtml(forum.getDescription()), 200);
				}
				catch (ParserException e) {}
				results.add(assembleSearchItem(Forum.class.getName(), forum.getForumId(), title, desc, forum.getModificationDate()));
			}
			/* Formulating Thread Query */
			Collection allForums = getForumsByUserGroupAccess(userId, null, "1", null, true, start, rows, false);; //getForums(userId, 0, -1, null, false, null, null, "1");
			if (allForums.size() > 0) {
				Collection forumId = new ArrayList();
				for(Iterator i = allForums.iterator(); i.hasNext();)
				{
					Forum forum = (Forum) i.next();
					forumId.add(forum.getForumId());
				}
				String[] forumIdArray = (String[])forumId.toArray(new String[0]);
				OperatorParenthesis parenthesisThread = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
				parenthesisThread.addOperator(new OperatorLike("frm_thread.subject", query, null));
				parenthesisThread.addOperator(new OperatorLike("frm_thread.content", query, DaoOperator.OPERATOR_OR));
				DaoQuery threadQuery = new DaoQuery();
				threadQuery.addProperty(new OperatorIn("frm_thread.forumId", forumIdArray, DaoOperator.OPERATOR_AND));
				threadQuery.addProperty(new OperatorEquals("active", "1", DaoOperator.OPERATOR_AND));
				if(!(startDate == null))
					threadQuery.addProperty(new OperatorGreaterThanEquals("frm_thread.modificationDate", startDate, DaoOperator.OPERATOR_AND));
				if(!(endDate == null))
					threadQuery.addProperty(new OperatorLessThanEquals("frm_thread.modificationDate", endDate, DaoOperator.OPERATOR_AND));
				Collection allThreads = dao.selectThreads(threadQuery, 0, -1, null, false);
				threadQuery.addProperty(parenthesisThread);
				Collection threads = dao.selectThreads(threadQuery, start, rows, null, false);
				count += dao.selectThreadCount(threadQuery);
				for(Iterator i = threads.iterator(); i.hasNext();)
				{
					Thread thread = (Thread) i.next();
					String title = Application.getInstance().getMessage("forum.label.topic") + ": " + thread.getSubject();
					String desc = "";
					try
					{
						com.tms.util.StringUtil util = com.tms.util.StringUtil.getInstance();
						desc = util.filterLength(util.filterHtml(thread.getContent()), 200);
					}
					catch (ParserException e) {}
					results.add(assembleSearchItem(Thread.class.getName(), thread.getThreadId(), title, desc, thread.getModificationDate()));
				}
				/* Formulating Message Query */
				Collection threadId = new ArrayList();
				for(Iterator i = allThreads.iterator(); i.hasNext();)
				{
					Thread thread = (Thread) i.next();
					threadId.add(thread.getThreadId());
				}
				OperatorParenthesis parenthesisMessage = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
				parenthesisMessage.addOperator(new OperatorLike("subject", query, null));
				parenthesisMessage.addOperator(new OperatorLike("content", query, DaoOperator.OPERATOR_OR));
				DaoQuery messageQuery = new DaoQuery();
				messageQuery.addProperty(new OperatorIn("forumId", forumIdArray, DaoOperator.OPERATOR_AND));
				messageQuery.addProperty(parenthesisMessage);
				if(!(startDate == null))
					messageQuery.addProperty(new OperatorGreaterThanEquals("modificationDate", startDate, DaoOperator.OPERATOR_AND));
				if(!(endDate == null))
					messageQuery.addProperty(new OperatorLessThanEquals("modificationDate", endDate, DaoOperator.OPERATOR_AND));
				Collection messages = dao.selectMessages(messageQuery, start, rows, "modificationDate", true);
				count += dao.selectMessageCount(messageQuery);
				for(Iterator i = messages.iterator(); i.hasNext();)
				{
					Message message = (Message) i.next();
					String title = Application.getInstance().getMessage("forum.label.message") + ": " + message.getSubject();
					String desc = "";
					try
					{
						com.tms.util.StringUtil util = com.tms.util.StringUtil.getInstance();
						desc = util.filterLength(util.filterHtml(message.getContent()), 200);
					}
					catch (ParserException e) {}
					results.add(assembleSearchItem(Message.class.getName(), message.getThreadId() + "," + message.getMessageId(), title, desc, message.getModificationDate()));
				}
			}
		}
		catch(Exception e)
		{
			Log.getLog(ForumModule.class).error(e.getMessage(), e);
		}
		results.setTotalSize(count);
		return results;
	}

	public SearchResult searchFullText(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
		return new SearchResult();
	}


}
