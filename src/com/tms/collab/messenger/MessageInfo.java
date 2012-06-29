package com.tms.collab.messenger;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

public class MessageInfo extends DefaultDataObject 
{
	private String userId;
	private User user;
	private boolean save;
	private boolean unread;
	
	public MessageInfo()
	{
		userId = "";
	}
	
	public boolean isSave() 
	{
		return save;
	}	
	
	public void setSave(boolean save)
	{
		this.save = save;
	}	
	
	public boolean isUnread()
	{
		return unread;
	}	
	
	public void setUnread(boolean unread)
	{
		this.unread = unread;
	}	
	
	public String getUserId()
	{
		return userId;
	}	
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public User getUser() 
	{
		if(user == null && (!(userId == null || "".equals(userId))))
		{
			try
			{
				SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
				user = service.getUser(userId);	
			}
			catch(Exception e) {}			
		}
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof MessageInfo)
		{
			MessageInfo info = (MessageInfo) o;
			if(getUserId().equals(info.getUserId()))
				return true;
		}
		return false;
	}
}
