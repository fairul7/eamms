
package com.tms.tmsPIMSync.exception;

/**
 */
public class PIMSyncServiceException extends Exception {

    /**
     * Creates a new instance of <code>EKPAccessException</code> without
     * detail message.
     */
    public PIMSyncServiceException() {

    }

    /**
     * Constructs an instance of <code>PIMSyncServiceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PIMSyncServiceException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>PIMSyncServiceException</code> with the
     * specified exception.
     *
     * @param cause the detail message.
     */
    public PIMSyncServiceException(Throwable cause) {
        super(cause);
    }


    /**
     * Constructs an instance of <code>PIMSyncServiceException</code> with the
     * specified detail message and the given cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public PIMSyncServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
