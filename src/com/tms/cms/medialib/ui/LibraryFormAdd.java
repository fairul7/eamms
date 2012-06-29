/*
 * LibraryFormAdd
 * Date Created: Jun 16, 2005
 * Author: Tien Soon, Law
 * Description: Extends the abstract Library form to create new library
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.cms.medialib.model.LibraryModule;
import com.tms.cms.medialib.model.LibraryObject;


public class LibraryFormAdd extends LibraryForm {
    public void init() {
        super.init();
    }
    
    public String getDefaultTemplate() {
    	return "medialib/newLibrary";
    }
    
    public Forward onValidate(Event event) {
        boolean querySuccess = false;
        Application app = Application.getInstance();
        LibraryModule module = (LibraryModule) app.getModule(LibraryModule.class);
        LibraryObject library = new LibraryObject();
        
        String id = UuidGenerator.getInstance().getUuid();
        library.setId(id);
        library.setCreatedBy(getWidgetManager().getUser().getId());
        library.setName((String) libraryName.getValue());
        library.setDescription((String) description.getValue());
        library.setMaxWidth(Integer.parseInt((String) maxWidth.getValue()));
        if(needApproval.isChecked()) {
            library.setApprovalNeeded("Y");
        }
        else {
            library.setApprovalNeeded("N");
        }
        library.setManagerGroup(managerGroup.getRightValues());
        library.setContributorGroup(contributorGroup.getRightValues());
        library.setViewerGroup(viewerGroup.getRightValues());
        
        querySuccess = module.addLibrary(library);
        
        super.removeChildren();
        super.init();
        
        if(querySuccess) {
            return new Forward(library.getId());
        }
        else {
            return new Forward(FORWARD_FAILURE);
        }
    }
}