package com.tms.collab.messaging.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.util.Log;

/**
 * This is invoked from 
 * {@link com.tms.collab.messaging.model.MessagingModule#downloadPop3MessagesNow(String, Pop3Account, ProgressTracker)}
 */
public class DaoMessageFilter extends AbstractDaoMessageFilter {

	private static final Log log = Log.getLog(DaoMessageFilter.class);
	
	
	private MessagingModule _module;
	
	// messages that are not filtered by DaoMessageFilter
	private List _resultantMessages;
	private Collection _filters;
	private Pop3Account _pop3Account;
	private String _filterId;
	
	
	/**
	 * @see com.tms.collab.messaging.model.MessageFilter#canFilter(Message[])
	 */
	public boolean canFilter(Message[] messages) {
		return true;
	}

	/**
	 * param[0] = userId
	 * param[1] = Pop3Account
	 * param[2] = filterId (optional) 
	 * 
	 * If param[2] is supplied, only that filter will be considered.
	 * 
	 * @see com.tms.collab.messaging.model.MessageFilter#init(java.lang.Object[])
	 */
	public void init(Object[] params) {
		// userid
		String userId = (String) params[0];
		_pop3Account = (Pop3Account) params[1];
		
		try {
			_filterId = (String) params[2];
		}
		catch(ArrayIndexOutOfBoundsException e) {
			// ignore
			_filterId = null;
		}
		
		_module = getModule();
		_filters = _module.getFiltersForUser(userId);
		_resultantMessages = new ArrayList();
	}
	

	/**
	 * @see com.tms.collab.messaging.model.MessageFilter#filter(Message)
	 */
	public void filter(Message message) {
		try {
			for (Iterator i = _filters.iterator(); i.hasNext(); ) {
				Filter f = (Filter) i.next();
				try {
				
					// filtering to be done using this filter (_filterId) only
					if(  (_filterId != null)  && 
							(! f.getId().equals(_filterId))  ) {
						log.debug("ignored, filtering to be runned on "+ _filterId+" only");
						continue;
					}
				
					log.debug("filter "+f+" filtering message "+message.getId());
                    boolean isMatched = f.match(message);
					if (isMatched) {
						// match filter's condition and its rule(s)
					
						// what is the action, do it
						FilterActionEnum fa = f.getFilterActionEnum();
						String filterValue = f.getFilterValue();
						fa.performAction(_module, message, _pop3Account, filterValue);
					}
					else {
						// NOTE: dun match filter's condition and its rule(s)
					
						_resultantMessages.add(message);
					}
                    
                    if (isMatched) {
                        // once we found a match filter, we'll use the first one
                        // just in case there are many, only the first one will 
                        // be used instead of propagating down the chain :-)
                        return;
                    }
				}
				catch(Exception e) {
					log.error("filtering for filter "+f.getId()+" failed", e);
				}
			}
		}
		catch(Exception e) {
			log.error(e.toString(), e);
		}
	}

	/**
	 * @see com.tms.collab.messaging.model.MessageFilter#done()
	 */
	public Message[] done() {
		return (Message[]) _resultantMessages.toArray(new Message[0]);
	}

	/**
	 * @see com.tms.collab.messaging.model.MessageFilter#continueWithNextFilter()
	 */
	public boolean continueWithNextFilter() {
		return true;
	}
}




