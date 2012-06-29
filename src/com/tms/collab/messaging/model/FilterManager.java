package com.tms.collab.messaging.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kacang.util.Log;

/**
 * Main entry point for performing a filtering. Typical usage would be
 * <pre>
 *	// create a FilterManager first
 * 	FilterManager fm = new FilterManager(myMessagingModule);
 * 
 * 	// add all the MessageFilter needed
 * 	fm.add(new DaoMessageFilter());
 * 	fm.add(new MyOtherMessageFilter());
 * 
 *  // do filtering
 *  fm.filter(...);
 * </pre>
 * 
 */
public class FilterManager {
	
	private static final Log log = Log.getLog(FilterManager.class);
	
	// list of MessageFilter
	private List _filters = new ArrayList(); 
	private MessagingModule _messagingModule;
	private ProgressCallback _progressCallback;
	

	
	/**
	 * Create an instance of FilterManager.
	 * 
	 * @param messagingModule
	 */
	public FilterManager(MessagingModule messagingModule) {
		if (messagingModule == null) { 
			throw new IllegalArgumentException("dao cannot be null");
		}
		_messagingModule = messagingModule;
	}
	
	
	/**
	 * Add a {@link MessageFilter}.
	 * 
	 * @param filter
	 */
	public void addFilter(MessageFilter filter) {
		_filters.add(filter);
	}
	
	
	/**
	 * Filter messages {@link Message} based on <code>param</code>.
	 * Currently the params are :
	 * <ul>
	 * 	<li>String (userId)</li>
	 * 	<li>Pop3Account (pop3Account)</li>
	 * 	<li>String filterId</li>
	 * </ul>
	 * 
	 * @param messages
	 * @param params
	 */
	public void filter(Message[] messages, Object[] params) {
		// total part to complete
		log.debug("start filtering ...");
		int totalPartToComplete = (_filters.size() * messages.length);
		int partCompleted = 0;
		
		
		for (Iterator i = _filters.iterator(); i.hasNext(); ) {

			MessageFilter filter = (MessageFilter) i.next();

			doCallback(partCompleted++);
			
			
			// set dao if it is a AbstractDaoMessageFilter
			if (filter instanceof AbstractDaoMessageFilter) {
				AbstractDaoMessageFilter f = (AbstractDaoMessageFilter) filter;
				f.setModule(_messagingModule);
			}
		
			if (filter.canFilter(messages)) {
				
				// 1. init filter
				log.debug("init "+filter.getClass()+" ... ");
				filter.init(params);
				
				
				for (int a=0; a< messages.length; a++) {
					doCallback(partCompleted++);
					
					// 2. ask to filter individual messages
					log.debug(filter.getClass()+" doing filtering for message "+messages[a].getId());
					filter.filter(messages[a]);
				}
				
				// 3. done 
				filter.done();
				log.debug(filter.getClass()+" done "+filter.getClass());
				
				// see if we wanna continue down the chain
				if (! (filter.continueWithNextFilter())) {
					doCallback(partCompleted += messages.length);
					break;
				}
			}
		}
		log.debug("finish filtering");
	}
	
	
	/**
	 * @param partCompleted
	 */
	private void doCallback(int partCompleted) {
		if (_progressCallback != null) {
			_progressCallback.progressReport(partCompleted);
		}
	}
	
	
	/**
	 * Add a progress callback, that will get called as filter progress.
	 * 
	 * @param progressCallback
	 */
	public void setProgressCallback(ProgressCallback progressCallback) {
		_progressCallback = progressCallback;
	}
	
	
	
	
	// inner interface ========================================================
	
	/**
	 * Interface to be implemented by those interested in the progress of 
	 * this filter.
	 */
	public static interface ProgressCallback {
		
		/**
		 * will be called, to inform the progress.
		 * 
		 * @param precentCompleted
		 */
		void progressReport(int precentCompleted);
	}
	
	
}
