package com.tms.collab.messenger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.messenger.jobs.MessengerArchivingRecordsSchedule;
import com.tms.util.MailUtil;

public class MessageModule extends DefaultModule
{
	public static final String SCHEDULER_ARCHIVE = "task_archiving_records";
	
	public void init() {
		// TODO Auto-generated method stub
		super.init();
//		Initializing Archiving Messages Daemon
		Calendar calendar = Calendar.getInstance();
        JobSchedule schedule = new JobSchedule("messengerJobSchedule", JobSchedule.MINUTELY);
        schedule.setGroup(SCHEDULER_ARCHIVE);
        schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule.setRepeatInterval(15);
		schedule.setStartTime(calendar.getTime());

        JobTask task = new MessengerArchivingRecordsSchedule();
        task.setName("messengerTaskSchedule");
        task.setGroup(SCHEDULER_ARCHIVE);
        task.setDescription("Task Archiving Records Daemon");

        SchedulingService service = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        try
        {
            service.deleteJobTask(task);
            service.scheduleJob(task, schedule);
        }
        catch (SchedulingException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
	}
	
	/**
	 * 
	 * @param userId
	 * @return a collection of message
	 */
	public Collection selectGroupId(String userId)
	{
        try
        {
        	MessageDao dao = (MessageDao) getDao();
        	return dao.selectGroupChats(userId);
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while checking for Existing Id", e);            
        }	
        return null;
	}
	
	public String createGroupChat(Message message)
	{
		try
        {
        	MessageDao dao = (MessageDao) getDao();
        	if(message.getChatId() == null || "".equals(message.getChatId()))
        		message.setChatId("group_" + UuidGenerator.getInstance().getUuid());
        	else if (!(message.getChatId().startsWith("group_")))
        		message.setChatId("group_" + message.getChatId());        		
        	if(message.getLastUpdated() == null)
        		message.setLastUpdated(Calendar.getInstance().getTime()); 
        	Collection messages = dao.selectAllMessageAndInfo();
        	Boolean sameChats = containsChat(message, messages);
        	if (sameChats == true)
        	{
        		return null;
        	}
        	else
        	{
        		dao.createChatAccount(message);
        		return message.getChatId();
        	}
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error occured while creating a group chat", e);
        }	
        return null;
	}
	
	public String initChat(String userId, String currentUserId)
	{
		Message msg = checkExistId(userId, currentUserId);
		if(msg != null && !(msg.getChatId().startsWith("group_")))
		{
			return msg.getChatId();
		}
		else
		{
			Message message = new Message();
			message.setMsg("");
			Collection info = new ArrayList();
			MessageInfo myself = constructDefaultInfo(currentUserId);
			MessageInfo chatee = constructDefaultInfo(userId);
			info.add(myself);
			info.add(chatee);
			message.setInfo(info);
			return createChatAccount(message);
		}
	}
	
	public String initGroupChat(String[] userId, String currentUserId)
	{
		Message message = new Message();
		message.setMsg("");
		Collection info = new ArrayList();
		for (int i = 0;i < userId.length ; i++)
		{
			if (!userId[i].equals(null))
			{
				MessageInfo chatee = constructDefaultInfo(userId[i]);
				info.add(chatee);	
			}
			else
			{}
		}
		MessageInfo myself = constructDefaultInfo(currentUserId);
		info.add(myself);
		message.setInfo(info);
		return createGroupChat(message);
	}
	
	private MessageInfo constructDefaultInfo(String userId)
	{
		MessageInfo info = new MessageInfo();
		info.setUserId(userId);
		info.setSave(false);
		info.setUnread(false);
		return info;
	}
	
	public String createChatAccount(Message message)
	{
		try
        {
        	MessageDao dao = (MessageDao) getDao();
        	if(message.getChatId() == null || "".equals(message.getChatId()))
        		message.setChatId(UuidGenerator.getInstance().getUuid());  		
        	if(message.getLastUpdated() == null)
        		message.setLastUpdated(Calendar.getInstance().getTime()); 
        	dao.createChatAccount(message);
    		return message.getChatId();
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error occured while creating chat account for id " + message.getChatId(), e);
        }	
        return null;
	}
	
	public Message checkExistId(String userId, String currentUserId)
	{
        try
        {
        	MessageDao dao = (MessageDao) getDao();
        	Message msg = dao.selectExistMessage(userId,currentUserId);
        	
        	if (msg == null)
        	{
        		return null;
        	}
        	else
        	{
        		return msg;
        	}
			
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while checking for Existing Id", e);
            return null;
        }	
	}
	
	public Collection checkUnreadMessages(String userId)
	{		
		Collection list = checkMessage(userId, true);
		for(Iterator i = list.iterator(); i.hasNext(); )
		{
			Message msg = (Message) i.next();
			setToRead(msg.getChatId(), userId);
		}
		return list;
	}
	
	public void setToSave(String userId, String chatId, String saved)
	{
		try
        {
			MessageDao dao = (MessageDao) getDao();
			dao.setToSave(userId, chatId, saved);
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while setting for save message", e);
        }
	}
	
	public String updateMessage(String messages,String chatId,String userId,Boolean saved)
	{
		try
        {
			MessageDao dao = (MessageDao) getDao();
			String extra = dao.updateMessage(messages,chatId,userId,saved);
			return extra;
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while setting for save message", e);
            return null;
        }
	}
	
	public void setToRead(String chatId, String userId)
	{
		try
        {
			MessageDao dao = (MessageDao) getDao();
			dao.setToRead(chatId, userId);
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while setting message to read", e);
        }
	}
	
	public Collection getExistMessage(String chatId)
	{
		try
        {
			MessageDao dao = (MessageDao) getDao();
			return dao.getExistMessage(chatId);
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while setting message to read", e);
        }
        return null;
	}
	
	public Collection checkMessage(String userId, Boolean unread)
	{
        try
        {
            MessageDao dao = (MessageDao) getDao();
            Collection col= dao.selectMessageByUser(userId, unread);
            
            if (col != null && col.size() > 0) 
				{
            		return col;
				}
				else
				{
					return null;
				}
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while checking message", e);
            return null;
        }	
	}
	
	public Collection initCheckMessage(String userId)
	{
        try
        {
            MessageDao dao = (MessageDao) getDao();
            Collection col= dao.selectInitMessageByUser(userId);
            
            if (col != null && col.size() > 0) 
				{
            		return col;
				}
				else
				{
					return null;
				}
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while checking message", e);
            return null;
        }	
	}
	
	public void addMessage(Message message)
	{
		try
        {
			MessageDao dao = (MessageDao) getDao();
			dao.addMessage(message);
        }
		catch (DaoException e)
	    {
	        Log.getLog(MessageModule.class).error("Error while adding message", e);
	    }
		
	}
	
/**	public void updateMessage(String msg, String chatId, String userId, Boolean saved)
	{
		try
        {
			MessageDao dao = (MessageDao) getDao();
			dao.updateMessage(msg,chatId,userId,saved);
        }
		catch (DaoException e)
	    {
	        Log.getLog(MessageModule.class).error("Error while adding message", e);
	    }
		
	}**/
	
	public Collection selectGroupMessage(String chatId)
	{
		try
        {
			MessageDao dao = (MessageDao) getDao();
			Collection col1 = dao.selectAllMessageInfo(chatId);
			return col1;
        }
		catch (DaoException e)
	    {
	        Log.getLog(MessageModule.class).error("Error while adding message", e);
	        return null;
	    }
		
	}

	public void archiveMessages()
	{
        try
        {
        	MessageDao dao = (MessageDao) getDao();
        	Collection col= dao.selectAllMessage();
        	
			for(Iterator i = col.iterator(); i.hasNext() ; )
			{
				Message msg = (Message) i.next();
				Calendar currentDate = Calendar.getInstance();
				Calendar msgDate = Calendar.getInstance(); 
				msgDate.setTime(msg.getLastUpdated());
				msgDate.add(Calendar.MINUTE, 15);
				if (currentDate.after(msgDate)) 
				{
					Collection col1 = dao.selectAllMessageInfo(msg.getChatId());
					for(Iterator j = col1.iterator(); j.hasNext() ; )
					{
						MessageInfo msgInfo = (MessageInfo) j.next();
						if (msgInfo.isSave()== true)
						{
//							Mailer.sendEmail("mail.tmsasia.com", true, "test2@tmsasia.com", msgInfo.getUser().getProperty("email1").toString(), "",
//								"", "Chat message", msg.getMsg());
							MailUtil.sendEmail(null, true, null, msgInfo.getUser().getProperty("email1").toString(), "",
								"", "Chat message", msg.getMsg());
							dao.deleteMessage(msg.getChatId());
						}
						else
						{
							dao.deleteMessage(msg.getChatId());
						}
					}
				}
			}
        }
        catch (DaoException e)
        {
            Log.getLog(MessageModule.class).error("Error while archiving messages", e);
        }
	}
	
	private boolean containsChat(Message message, Collection messages)
	{
		for (Iterator i = messages.iterator(); i.hasNext() ; )
		{
			Message compare = (Message) i.next();
			if(message.equals(compare))
				return true; 
		}
		return false;
	}
}