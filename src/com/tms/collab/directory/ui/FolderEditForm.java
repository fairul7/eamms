package com.tms.collab.directory.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Folder;

import java.util.Map;

public class FolderEditForm extends FolderNewForm {

    public FolderEditForm() {
    }

    public FolderEditForm(String name) {
        super(name);
    }

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void onRequest(Event evt) {
        setMethod("post");
        populateForm();
    }

    public void populateForm() {
        if (getId() != null) {
            AddressBookModule dm = getModule();

            try {
                // load folder
                Folder folder = dm.getFolder(getId());
                nameField.setValue(folder.getName());
                description.setValue(folder.getDescription());
                folderSelectBox.setSelectedOptions(new String[] {folder.getParentId()});

            } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error("Contact " + getId() + " not found");
                init();
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Contact " + getId() + " error: " + e.toString(), e);
                init();
                throw new RuntimeException("Contact " + getId() + " error: " + e.toString());
            }
        }
    }

    public Forward onValidate(Event evt) {
        try {
            String userId = getWidgetManager().getUser().getId();

            AddressBookModule dm = getModule();

            // populate values
            Folder folder = dm.getFolder(getId());
            folder.setName((String)nameField.getValue());
            folder.setDescription((String)description.getValue());

            Map selectedMap = folderSelectBox.getSelectedOptions();
            if (selectedMap != null && selectedMap.size() > 0) {
                String parentId = (String)selectedMap.keySet().iterator().next();
                if (!getId().equals(parentId)) {
                    folder.setParentId(parentId);
                }
            }

            // store folder
            dm.updateFolder(folder, userId);

            return new Forward(FORWARD_SUCCESS);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error updating contact " + getId() + ": " + e.toString(), e);
            init();
            return new Forward(FORWARD_ERROR);
        }
    }

	public boolean isEditMode() {
		return true;
	}

}
