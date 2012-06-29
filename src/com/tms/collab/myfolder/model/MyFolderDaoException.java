/*
 * Created on Jun 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.model;

import kacang.util.DefaultException;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyFolderDaoException extends DefaultException{
	
	public MyFolderDaoException(){
		super();
	}
	
	public MyFolderDaoException(String s){
		super(s);
	}
	
	public MyFolderDaoException(Throwable throwable) {
        super(throwable);
    }

    public MyFolderDaoException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
