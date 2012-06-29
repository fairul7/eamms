package com.tms.collab.messenger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;

public class MessageDWRModule implements Serializable 
{
	public String initChat (String userId, String currentId, String aid)
	{
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
		String initChatId = module.initChat(userId, currentId);
		String chatId = initChatId + "(^_^)" + aid;
		return chatId;
	}
	
	public void savingMessage (String saved, String cui, String chatId)
	{
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
		module.setToSave(cui, chatId, saved);
	}
	
	public Collection checkForGroup (String userId)
	{
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
		Collection groupMessage = module.selectGroupId(userId);
		return groupMessage;
	}
	
	public String initChatting (String messages, String chatId, String userId, Boolean saved, String aid)
	{
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
				String extra =module.updateMessage(messages,chatId,userId,saved);
				chatId = extra + chatId +"(^_^)"+ aid;
		return chatId;
				/**		Collection msg = getExistMessage(chatId);
		for(Iterator i = msg.iterator(); i.hasNext() ; )
		{
			Message msgExist = (Message) i.next();
			String outComeMessage = msgExist.getMsg();
			if (outComeMessage != null)
			{
				String updateMessages = messages + outComeMessage ;
				Message message = new Message();
				message.setChatId(chatId);
				
				message.setMsg(updateMessages);
				message.setLastUpdated(Calendar.getInstance().getTime());
				Collection selectGroupMessage = module.selectGroupMessage(chatId);
				for(Iterator j = selectGroupMessage.iterator(); j.hasNext() ; )
				{
					MessageInfo msgInfo = (MessageInfo) j.next();
					if (msgInfo.getUserId().equals(userId))
					{
						MessageInfo messageInfo = new MessageInfo();
						messageInfo.setUserId(userId);
						messageInfo.setSave(saved);
						messageInfo.setUnread(false);
						message.addInfo(messageInfo);
					}
					else
					{
						MessageInfo messageInfo = new MessageInfo();
						messageInfo.setUserId(msgInfo.getUserId());
						messageInfo.setSave(false);
						messageInfo.setUnread(true);
						message.addInfo(messageInfo);
					}
				}
				module.addMessage(message);
			}
		}**/
	}
	
	public Collection checkMessage (String userId, Boolean unread)
	{
		int len = 500;
		String trunc;
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
		Collection msgCol = module.checkMessage(userId, unread);
		if(msgCol != null && msgCol.size() > 0)
		{
			for(Iterator i = msgCol.iterator(); i.hasNext();)
			{
				Message msg = (Message) i.next();
				trunc = msg.getMsg();
				if (trunc.length() > len)
				{
					trunc = trunc.substring(0, len);
					msg.setMsg(trunc);
				}
			}
			return msgCol;
		}
		else
		{
			return null;
		}
	}
	

	
	public Collection initCheckMessage (String userId)
	{
		int len = 500;
		String trunc;
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
		Collection msgCol = module.initCheckMessage(userId);
		if(msgCol != null && msgCol.size() > 0)
		{
			for(Iterator i = msgCol.iterator(); i.hasNext();)
			{
				Message msg = (Message) i.next();
				trunc = msg.getMsg();
				if (trunc.length() > len)
				{
					trunc = trunc.substring(0, len);
					msg.setMsg(trunc);
				}
			}
			return msgCol ;
		}
		else
		{
			return null;
		}
	}
	
	public void readMessage (String chatId, String userId)
	{
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
		module.setToRead(chatId, userId);
	}
	
	public Collection getExistMessage (String chatId)
	{
		int len = 500;
		String trunc;
		MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
		Collection msgCol = module.getExistMessage(chatId);
		for(Iterator i = msgCol.iterator(); i.hasNext();)
		{
			Message msg = (Message) i.next();
			trunc = msg.getMsg();
			if (trunc.length() > len)
			{
				trunc = trunc.substring(0, len);
				msg.setMsg(trunc);
			}
		}
		return msgCol;
	}
}
