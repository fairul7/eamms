package com.tms.elearning.core.ui;

import kacang.stdui.Tree;
import com.tms.cms.core.model.ContentManager;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 8, 2005
 * Time: 10:36:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class CoursePath extends Tree{

    private String rootId = "Course Home";

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public void init() {
        setSelectedId(rootId);
    }
     public String getDefaultTemplate() {
        return "elearning/contentPath";
    }

    

}
