/*
 * LibraryFormEdit
 * Date Created: Jun 27, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.medialib.model.LibraryModule;
import com.tms.cms.medialib.model.LibraryObject;


public class LibraryFormEdit extends LibraryForm {
    private String libraryId = "";
    
    public void init() {
        super.init();
    }
    
    public Forward onValidate(Event event) {
        boolean querySuccess = false;
        Application app = Application.getInstance();
        LibraryModule module = (LibraryModule) app.getModule(LibraryModule.class);
        LibraryObject library = new LibraryObject();
        
        library.setId(libraryId);
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
        
        querySuccess = module.updateLibrary(library);
        
        super.removeChildren();
        super.init();
        
        if(querySuccess) {
            return new Forward(FORWARD_SUCCESS);
        }
        else {
            return new Forward(FORWARD_FAILURE);
        }
    }
    
    public void onRequest(Event evt) {
        init();
        
        if(! "".equals(libraryId)) {
	        LibraryModule module = (LibraryModule) Application.getInstance().getModule(LibraryModule.class);
	        LibraryObject library = module.selectLibrary(libraryId);
	        libraryName.setValue(library.getName());
	        description.setValue(library.getDescription());
	        // description.setRows("3");
	        maxWidth.setValue(String.valueOf(library.getMaxWidth()));
	        if("Y".equals(library.getApprovalNeeded())) {
	            needApproval.setChecked(true);
	        }
	        else {
	            noNeedApproval.setChecked(true);
	        }
	        
	        // Manager Group
	        Iterator itManagerGroup = library.getManagerGroup().keySet().iterator();
            Map allManagerGroup = managerGroup.getLeftValues();
            while(itManagerGroup.hasNext())
            {
                allManagerGroup.remove(itManagerGroup.next());
            }
            managerGroup.setLeftValues(allManagerGroup);
            managerGroup.setRightValues(library.getManagerGroup());
            
            // Contributor Group
	        Iterator itContributorGroup = library.getContributorGroup().keySet().iterator();
            Map allContributorGroup = contributorGroup.getLeftValues();
            while(itContributorGroup.hasNext())
            {
                allContributorGroup.remove(itContributorGroup.next());
            }
            contributorGroup.setLeftValues(allContributorGroup);
            contributorGroup.setRightValues(library.getContributorGroup());
            
            // Viewer Group
	        Iterator itViewerGroup = library.getViewerGroup().keySet().iterator();
            Map allViewerGroup = viewerGroup.getLeftValues();
            while(itViewerGroup.hasNext())
            {
                allViewerGroup.remove(itViewerGroup.next());
            }
            viewerGroup.setLeftValues(allViewerGroup);
            viewerGroup.setRightValues(library.getViewerGroup());
        }
    }
    
    public String getLibraryId() {
        return libraryId;
    }
    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
}
