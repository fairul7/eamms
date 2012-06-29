package com.tms.elearning.folder.model;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 1:07:38 PM
 * To change this template use File | Settings | File Templates.
 */

public class FolderModuleException extends RuntimeException {
    public FolderModuleException() {
    }

    public FolderModuleException(String toEmail) {
        super(toEmail);
    }
}