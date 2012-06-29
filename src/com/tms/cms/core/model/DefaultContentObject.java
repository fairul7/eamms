package com.tms.cms.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 20, 2003
 * Time: 2:55:45 PM
 * To change this template use Options | File Templates.
 */
public class DefaultContentObject extends ContentObject {

    public Class getContentModuleClass() {
        return DefaultContentModule.class;
    }

    /**
     *
     * @return A ContentObject of the appropriate class based on the class name.
     * @throws ContentException
     */
    public ContentObject getContentObject() throws ContentException {
        try {
            ContentObject co = (ContentObject)Class.forName(getClassName()).newInstance();
            ContentUtil.copyAttributes(co, this);
            return co;
        }
        catch (Exception e) {
            throw new ContentException("Unable to obtain actual content object: " + e.toString());
        }

    }

}
