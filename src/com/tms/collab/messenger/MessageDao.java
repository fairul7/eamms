package com.tms.collab.messenger;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Transaction;

public class MessageDao extends DataSourceDao{
		
	    public void init() throws DaoException
	    {
	        super.update("CREATE TABLE chat_content(chatId VARCHAR(255) NOT NULL, msg TEXT NOT NULL, lastUpdated DATETIME, PRIMARY KEY(chatId)) TYPE = InnoDB", null);
	        super.update("CREATE TABLE chat_info(chatId VARCHAR(255) NOT NULL, userId VARCHAR(255) NOT NULL, unread CHAR(1), save CHAR(1), PRIMARY KEY(chatId, userId)) TYPE = InnoDB", null);
	        super.update("CREATE INDEX checkingModule ON chat_info (userId, unread)", null);
	    }

	
		public void addMessage(Message message) throws DaoException 
		{
			super.update("UPDATE chat_content set msg = #msg#, lastUpdated = #lastUpdated# WHERE chatId = #chatId#", message);
			
			for(Iterator i = message.getInfo().iterator(); i.hasNext();)
			{
				MessageInfo info = (MessageInfo) i.next();
				super.update("UPDATE chat_info set save = ?, unread = ? WHERE chatId = ? AND userId = ?", new Object[]{info.isSave(),info.isUnread(), message.getChatId(), info.getUserId()});
			}			
		}
		
		public String updateMessage(String msg, String chatId, String userId, boolean saved) throws DaoException 
		{
			String extra = null;
			Transaction tx = null;
	        try 
	        {
	            tx = getTransaction();
	            tx.begin();
			Collection specificMessage = super.select("SELECT chatId, msg, lastUpdated FROM chat_content WHERE chatId = ?", Message.class, new Object[] {chatId}, 0 , -1);
			if (specificMessage.size() > 0)
			{
				for(Iterator i = specificMessage.iterator(); i.hasNext();)
				{
					Message messages = (Message) i.next();
					messages.setInfo(selectAllMessageInfo(messages.getChatId()));
					String outComeMessage = messages.getMsg();
					if (outComeMessage != null)
					{
						String updateMessages = msg + outComeMessage ;
						tx.update("UPDATE chat_content set msg = ?, lastUpdated = ? WHERE chatId = ?", new Object[]{updateMessages,Calendar.getInstance().getTime(),chatId});
						tx.commit();
	/*					Message message = new Message();
						message.setChatId(chatId);
						
						message.setMsg(updateMessages);
						message.setLastUpdated(Calendar.getInstance().getTime());*/
						Collection selectedMessageInfo = selectAllMessageInfo(chatId);
						for(Iterator j = selectedMessageInfo.iterator(); j.hasNext() ; )
						{
							MessageInfo msgInfo = (MessageInfo) j.next();
							super.update("UPDATE chat_info set save = ?, unread = ? WHERE chatId = ? AND userId = ?", new Object[]{msgInfo.isSave(),(msgInfo.getUserId().equals(userId)) ? false : true, chatId, msgInfo.getUserId()});
							/*						if (msgInfo.getUserId().equals(userId))
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
								messageInfo.setSave(msgInfo.isSave());
								messageInfo.setUnread(true);
								message.addInfo(messageInfo);
							}*/
						}	
	//					tx.update("UPDATE chat_content set msg = #msg#, lastUpdated = #lastUpdated# WHERE chatId = #chatId#", message);
	//					super.update("UPDATE chat_info set save = ?, unread = ? WHERE chatId = ? AND userId = ?", new Object[]{saved,(msgInfo.getUserId().equals(userId)) ? false : true, message.getChatId(), msgInfo.getUserId()});					
	//					for(Iterator k = message.getInfo().iterator(); k.hasNext();)
	//					{
	//						MessageInfo info = (MessageInfo) k.next();
	//						tx.update("UPDATE chat_info set save = ?, unread = ? WHERE chatId = ? AND userId = ?", new Object[]{info.isSave(),info.isUnread(), message.getChatId(), info.getUserId()});
	//					}	
							
					}
				}
				extra="ok";
				return extra;
			}
			else
			{
				extra="ko";
				return extra;
			}
	        }
	        catch(Exception e) 
	        {
	            if (tx != null) 
	            {
	                tx.rollback();
	            }
	            throw new DaoException(e.toString());
	        }
			
		
/**			for(Iterator i = msg.iterator(); i.hasNext() ; )
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
 * 
 * newMessage.setLastUpdated(Calendar.getInstance().getTime());
			newMessage.setMsg(msg);
			newMessage.setChatId(chatId);
			MessageInfo msgInfo = new MessageInfo;
			m
			for(Iterator z = specificMessage.iterator(); z.hasNext() ; )
			{
				MessageInfo msgInfo = (MessageInfo) j.next();
				newMessage.setInfo(selectAllMessageInfo(msgInfo.getChatId()));
			}

			newMessage.addInfo(messageInfo)
			specificMessage.add(newMessage);**/
		}
		
		public Collection selectMessageByUser(String userId, Boolean unread) throws DaoException 
		{
/*			int len = 500;
			String trunc;*/
			Collection messages = super.select("SELECT chatId, msg, lastUpdated FROM chat_content WHERE chatId IN " +
					"(SELECT DISTINCT chatId FROM chat_info WHERE userId = ? AND unread = ?)"
					, Message.class, new Object[] {userId, unread}, 0, -1);
			for(Iterator i = messages.iterator(); i.hasNext();)
			{
				Message msg = (Message) i.next();
			/*	trunc = msg.getMsg();
				if (trunc.length() > len)
				{
					trunc = trunc.substring(0, len);
					trunc = trunc.replace(":", "truncated");
					msg.setMsg(trunc);
				}*/
				msg.setInfo(selectAllMessageInfo(msg.getChatId()));
			}
			return messages;

		}
		
		public Collection selectInitMessageByUser(String userId) throws DaoException 
		{
//			int len = 500;
//			String trunc;
			Collection messages = super.select("SELECT chatId, msg, lastUpdated FROM chat_content WHERE chatId IN " +
					"(SELECT DISTINCT chatId FROM chat_info WHERE userId = ?)"
					, Message.class, new Object[] {userId}, 0, -1);
			for(Iterator i = messages.iterator(); i.hasNext();)
			{
				Message msg = (Message) i.next();
/*				trunc = msg.getMsg();
				if (trunc.length() > len)
				{
					trunc = trunc.substring(0, len);
					trunc = trunc.replace(":", "truncated");
					msg.setMsg(trunc);
				}*/
				msg.setInfo(selectAllMessageInfo(msg.getChatId()));
			}
			return messages;

		}
		
		public Collection selectAllMessageAndInfo() throws DaoException 
		{
			Collection messages = super.select("(SELECT chatId, msg, lastUpdated FROM chat_content)"
					, Message.class, new String[]{}, 0, -1);
			for(Iterator i = messages.iterator(); i.hasNext();)
			{
				Message msg = (Message) i.next();
				msg.setInfo(selectAllMessageInfo(msg.getChatId()));
			}
			return messages;
		}
		
		public Collection selectAllMessage() throws DaoException 
		{
			return super.select("(SELECT chatId, msg, lastUpdated FROM chat_content)"
					, Message.class, new String[]{}, 0, -1);
		}
		
		public Collection getExistMessage(String chatId) throws DaoException 
		{
//			int len = 500;
//			String trunc;
			Collection msgCol = super.select("(SELECT chatId, msg, lastUpdated FROM chat_content WHERE chatId = ?)", Message.class, new Object[]{chatId}, 0, -1);
			for(Iterator i = msgCol.iterator(); i.hasNext();)
			{
				Message msg = (Message) i.next();
//				trunc = msg.getMsg();
//				if (trunc.length() > len)
//				{
//					trunc = trunc.substring(0, len);
//					trunc = trunc.replace(":", "truncated");
//					msg.setMsg(trunc);
//				}
				msg.setInfo(selectAllMessageInfo(msg.getChatId()));
			}
			return msgCol;
		}
		
		public Collection selectAllMessageInfo(String chatId) throws DaoException 
		{
			return super.select("(SELECT chatId, userId, unread, save FROM chat_info WHERE chatId = ?)"
					, MessageInfo.class, new String[]{chatId}, 0, -1);
		}
		
		public Message selectExistMessage(String userId, String currentUserId) throws DaoException 
		{
			String sqlQuery = "SELECT a.chatId as chatId FROM chat_info a INNER JOIN chat_info b ON a.userId=? AND b.userId=? WHERE a.chatId=b.chatId";
            Collection col = super.select(sqlQuery, HashMap.class, new String[]{userId, currentUserId}, 0, -1 );
            if(col.size() > 0)
            {
            HashMap chatMap = (HashMap) col.iterator().next();
            String chatId = (String) chatMap.get("chatId");
            
            Collection msgCol = super.select("(SELECT chatId, msg, lastUpdated FROM chat_content WHERE chatId = ?)", Message.class, new Object[]{chatId}, 0, -1);
            if (msgCol.size() > 0)
            {
            	for(Iterator i = msgCol.iterator(); i.hasNext();)
    			{
    				Message msg = (Message) i.next();
    				msg.setInfo(selectAllMessageInfo(msg.getChatId()));
    			}
            	return (Message) msgCol.iterator().next();
            }
            return null;
            }
            return null;
		}
				
		public void deleteMessage(String chatId) throws DaoException 
		{
			super.update("DELETE FROM chat_content WHERE chat_content.chatId = ? ", new Object[] {chatId});
			super.update("DELETE FROM chat_info WHERE chat_info.chatId = ? ", new Object[] {chatId});
		}
					
		public Collection selectGroupChats(String userId) throws DaoException 
		{
			Collection messages = super.select("SELECT chatId, msg, lastUpdated FROM chat_content WHERE chatId IN (SELECT DISTINCT chatId FROM chat_info WHERE userId=?) AND chatId LIKE 'group_%'", Message.class, new Object[] {userId}, 0, -1);
			for(Iterator i = messages.iterator(); i.hasNext();)
			{
				Message msg = (Message) i.next();
				msg.setInfo(selectAllMessageInfo(msg.getChatId()));
			}
			return messages;
		}
		
		public void createChatAccount(Message message) throws DaoException
		{
			super.update("INSERT INTO chat_content(chatId, msg, lastUpdated) VALUES (#chatId#, #msg#, #lastUpdated#)", message);
			for(Iterator i = message.getInfo().iterator(); i.hasNext();)
			{
				MessageInfo info = (MessageInfo) i.next();
				super.update("INSERT INTO chat_info(chatId, userId, unread, save) VALUES (?, ?, ?, ?)", new Object[] {message.getChatId(), info.getUserId(), info.isUnread(), info.isSave()});
			}
		}
				
		public void setToRead(String chatId, String userId) throws DaoException
		{
			super.update("UPDATE chat_info SET unread='0' WHERE chatId=? AND userId=?", new Object[] {chatId, userId});
		}
		
		public void setToSave(String userId, String chatId, String saved) throws DaoException
		{
			if (saved.equals("true"))
			{
				super.update("UPDATE chat_info SET save='1' WHERE chatId=? AND userId=?", new Object[] {chatId, userId});
			}
			else
			{
				super.update("UPDATE chat_info SET save='0' WHERE chatId=? AND userId=?", new Object[] {chatId, userId});
			}
		}
}		