package com.tms.elearning.folder.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;

import java.util.Collection;
import java.io.Serializable;

import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.folder.model.FolderModule;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 1:48:45 PM
 * To change this template use File | Settings | File Templates.
 */

public class DisplayFolderList extends LightWeightWidget implements Serializable {

    private Collection folderList;

    public void onRequest(Event evt) {
        Application application = Application.getInstance();
        FolderModule module = (FolderModule)application.getModule(FolderModule.class);
        folderList = module.findFolders(null,null,null, null, false, 0, -1);
    }

    public String getDefaultTemplate() {
        return "folder/folderForm";
    }

    public Collection getFolderList() {
        return folderList;
    }
    
}
