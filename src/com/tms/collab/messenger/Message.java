package com.tms.collab.messenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.model.DefaultDataObject;


public class Message extends DefaultDataObject 
{
	private String chatId;
	private Date lastUpdated;
	private String msg;
	private Collection info;
	
	public Message() {
		info = new ArrayList();
	}
	
	public String getChatId() 
	{
		return chatId;
	}
	
	public void setChatId(String chatId) 
	{
		this.chatId = chatId;
	}
	
	public Date getLastUpdated() 
	{
		return lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) 
	{
		this.lastUpdated = lastUpdated;
	}
	
	public String getMsg() 
	{
		return msg;
	}
	
	public void setMsg(String msg) 
	{
		this.msg = msg;
	}
	
	public Collection getInfo() 
	{
		return info;
	}
	
	public void setInfo(Collection info) 
	{
		this.info = info;
	}
	
	public void addInfo(MessageInfo messageInfo)
	{
		info.add(messageInfo);
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Message)
		{
			Message compare = (Message) o;
			if(getInfo().size() == compare.getInfo().size())
			{
				for (Iterator i = getInfo().iterator(); i.hasNext() ; )
				{
					MessageInfo info = (MessageInfo) i.next();
					if(!(compare.getInfo().contains(info)))
						return false;
				}
				return true;
			}
		}
		return false;
	}
}