package com.tms.collab.messaging.model;

import java.util.Date;

/**
 * This interface is used to tracker the progress of a certain process. Created
 * to determine progress of asynchronous process.
 * <p>
 *
 */
public interface ProgressTracker {
    public static final String STATUS_NOT_STARTED = "notStarted";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_ERROR = "error";

    /**
     * Returns the current status of the progress tracker.
     *
     * @return one of the pre-defined status in ProgressTracker
     * @see ProgressTracker#STATUS_NOT_STARTED
     * @see ProgressTracker#STATUS_PENDING
     * @see ProgressTracker#STATUS_PROCESSING
     * @see ProgressTracker#STATUS_COMPLETED
     * @see ProgressTracker#STATUS_ERROR
     */
    public String getStatus();

    /**
     * Sets the status of the progress tracker to one of the pre-defined status.
     *
     * @param s
     * @see ProgressTracker#STATUS_NOT_STARTED
     * @see ProgressTracker#STATUS_PENDING
     * @see ProgressTracker#STATUS_PROCESSING
     * @see ProgressTracker#STATUS_COMPLETED
     * @see ProgressTracker#STATUS_ERROR
     */
    public void setStatus(String s);

    /**
     * Returns the current percent of completion.
     *
     * @return an integer between 0 to 100
     */
    public int getProgressPercentage();

    /**
     * Sets the current percent of completion from 0 to 100.
     *
     * @param i
     */
    public void setProgressPercentage(int i);

    /**
     * An optional message to indicate current status of progress.
     *
     * @return
     */
    public String getMessage();

    /**
     * Sets the optional message to indicate the current status of progress.
     *
     * @param s
     */
    public void setMessage(String s);

    /**
     * Convenience method to update the progress tracker.
     *
     * @param status
     * @param progressPercentage
     * @param message
     */
    public void update(String status, int progressPercentage, String message);

    /**
     * Returns the time update() was last called.
     * 
     * @return
     */
    public Date getUpdateDate();

    /**
     * Resets the tracker to 'initial' state.
     */
    public void reset();
}
