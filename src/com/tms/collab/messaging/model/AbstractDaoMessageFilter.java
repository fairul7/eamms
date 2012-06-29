package com.tms.collab.messaging.model;

/**
 * Convenience abstract class with access to {@link MessagingModule}. 
 * MessageFilter that deals with messaging could subclass off here 
 * and have access to {@link MessagingModule}.
 */
public abstract class AbstractDaoMessageFilter implements MessageFilter {

	private MessagingModule _module;
	
	/**
	 * Will be set when this filter's is ready to have its methods invoked
	 * by {@link FilterManager}. Package visibility cause it should only be
	 * called by {@link FilterManager} when appropriate.
	 * 
	 * {@see FilterManager#filter(Message)}.
	 * 
	 * @param messageModule
	 */
	void setModule(MessagingModule messageModule){
		_module = messageModule;
	}
	
	/**
	 * Grab a copy of {@link MessagingModule}.
	 * 
	 * @return MessagingModule
	 */
	public MessagingModule getModule() {
		return _module;
	}
}


