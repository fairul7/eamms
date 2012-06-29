package com.tms.cms.core.model;



/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 17, 2003
 * Time: 2:54:20 PM
 * To change this template use Options | File Templates.
 */
public class DefaultContentModule extends ContentModule {

    public Class[] getContentObjectClasses() {
        return new Class[] {DefaultContentObject.class};
    }

}
