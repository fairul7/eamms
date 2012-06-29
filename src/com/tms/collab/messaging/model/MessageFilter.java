
package com.tms.collab.messaging.model;


/**
 * 
 */
public interface MessageFilter {
	boolean canFilter(Message[] messages);
	
	void init(Object[] params);
	void filter(Message message);
	Message[] done();
	
	boolean continueWithNextFilter();
}

