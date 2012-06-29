package com.tms.collab.messaging.model;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

/**
 * Quota update scheduling job.
 */
public class QuotaUpdateJob extends BaseJob {

    private static final Log log = Log.getLog(QuotaUpdateJob.class);
    
    // NOTE:
    // there are two type of queue currently
    // ArrayBasedQueue: which is based on Array and is FIFO
    // StackBasedQueue: which is baed on Stack and is LIFO
    private static Queue _queue = new ArrayBasedQueue(); //new StackBasedQueue();
    
    
    /**
     * @see kacang.services.scheduling.BaseJob#execute(kacang.services.scheduling.JobTaskExecutionContext)
     */
    public void execute(JobTaskExecutionContext context) throws SchedulingException {
        log.debug("execute QuotaUpdateJob");
        
        MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        
        String userId = null;
        
        // when we get from queue, no others should alter it.
        synchronized(_queue) {
            userId = (String) _queue.get();
        }
        
        // null, means no userId is in queue
        if (userId == null) { return; }
        
        try {
            mm.updateUserDiskQuotaNow(userId);
        }
        catch(MessagingException e) {
            log.error(e.toString(), e);
        }
    }
    
    
    

    public static void queueMeUp(String userId) {
        synchronized(_queue) {
            if (_queue.query(userId)) {
                log.debug("user ["+userId+"] already in Quota queue");
            }
            else {
                _queue.put(userId);
                log.debug("user ["+userId+"] added to Quota queue");
            }
        }
    }
    
    
    
    
    // === inner class ===
    /**
     * Contract for QuotaQueueJob's Queue implementation.
     */
    public static interface Queue {
        /** get() should return null if nothing is in queue. */
        String get();
        void put(String id);
        boolean query(String id);
    }
    
    
    // === inner class ===
    /**
     * Stack based implementation of Queue, LIFO
     */
    public static class StackBasedQueue implements Queue {
        private Stack _stack = new Stack();
        
        public String get() {
            try {
                return (String) _stack.pop();
            }
            catch(EmptyStackException e) {
                return null;
            }
        }

        public void put(String id) {
            _stack.push(id);
        }

        public boolean query(String id) {
            return _stack.contains(id);
        }
    }
    
    
    // === inner class ===
    /**
     * Array based implementation of Queue, FIFO.
     */
    public static class ArrayBasedQueue implements Queue {
        private List _list = new ArrayList();
        
        public String get() {
            if (_list.size() <= 0) { return null; }
            return (String) _list.remove(0);
        }

        public void put(String id) {
            _list.add(id);
        }

        public boolean query(String id) {
            if (_list.contains(id)) { return true; }
            return false;
        }
    }
}
