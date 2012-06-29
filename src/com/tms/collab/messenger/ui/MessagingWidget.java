package com.tms.collab.messenger.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.presence.PresenceService;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.collab.messenger.MessageModule;
import com.tms.ekms.security.ui.LoginForm;

public class MessagingWidget extends Widget{
	
	private Collection onlineUsers;
	private Collection offlineUsers;
	private Collection groupUsers;
	private User currentUser;
//	private Collection checkMessages;

	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}
	
	public void onRequest(Event arg0) {
		PresenceService service = (PresenceService) Application.getInstance().getService(PresenceService.class);
		SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
		try
	    {	
			groupUsers = new ArrayList();
			MessageModule module = (MessageModule)Application.getInstance().getModule(MessageModule.class);
			Collection allOnlineUsers = new ArrayList();
			Collection allOfflineUsers = new ArrayList();
			allOfflineUsers = service.getOfflineUsers();
			User current = getWidgetManager().getUser();
			allOnlineUsers = service.getOnlineUsers();
			groupUsers = module.selectGroupId(current.getId());
			onlineUsers = new ArrayList();
			offlineUsers = new ArrayList();
			currentUser = current;
			for(Iterator i = allOnlineUsers.iterator(); i.hasNext(); )
			{
				User user = (User) i.next();
				if ((!(current.getId().equals(user.getId()))) && (security.hasPermission(user.getId(), LoginForm.PROPERTY_EKMS_PERMISSION, security.getClass().getName(), null)))
				{	
					onlineUsers.add(user);
				}				
			}
			for(Iterator i = allOfflineUsers.iterator(); i.hasNext(); )
			{
				User user = (User) i.next();
				if (security.hasPermission(user.getId(), LoginForm.PROPERTY_EKMS_PERMISSION, security.getClass().getName(), null))
				{	
					offlineUsers.add(user);
				}				
			}
			


	    }
		catch (Exception e)
		{
			Log.getLog(MessageModule.class).error("Error while getting online user", e);
		}
	}
		
	public String getDefaultTemplate() {
		return "messenger/messengerLeftPanel";
	}
	
	public Collection getOfflineUsers() {
		return offlineUsers;
	}

	public void setOfflineUsers(Collection offlineUsers) {
		this.offlineUsers = offlineUsers;
	}

	public Collection getOnlineUsers() {
		return onlineUsers;
	}

	public void setOnlineUsers(Collection onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public Collection getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(Collection groupUsers) {
		this.groupUsers = groupUsers;
	}


}
