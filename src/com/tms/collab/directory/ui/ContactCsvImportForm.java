package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.storage.StorageFile;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.messaging.ui.MailEncoding;

import java.util.List;
import java.util.Map;

public class ContactCsvImportForm extends Form implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_NO_FILE = "nofile";
    public static final String REQUEST_KEY_IMPORT_COUNT = "ContactCsvImportForm.importCount";

    FolderSelectBox folderSelectBox;
    FileUpload csvFile;
    Button importButton;
    Button cancelButton;
    
    
    SelectBox encodingSelect;
    Map encodingMap;

    
    
    public String getDefaultTemplate() {
    	return "addressbook/bdOptions";
    }
    
    
    public ContactCsvImportForm() {
    }

    public ContactCsvImportForm(String name) {
        super(name);
    }

    public void init() {
        removeChildren();
        setColumns(2);

        folderSelectBox = new FolderSelectBox("folder");
        folderSelectBox.setType(getType());
        csvFile = new FileUpload("csvFile");
        importButton = new Button("importButton", Application.getInstance().getMessage("addressbook.label.import","Import"));
        cancelButton = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("addressbook.label.cancel","Cancel"));
        Panel tmpPanel = new Panel("tmpPanel");

        addChild(new Label("l1", Application.getInstance().getMessage("addressbook.label.file", "File")));
        addChild(csvFile);
        addChild(new Label("l2", Application.getInstance().getMessage("addressbook.label.folder", "Folder")));
        addChild(folderSelectBox);
        addChild(new Label("l3", ""));
        tmpPanel.addChild(importButton);
        tmpPanel.addChild(cancelButton);
        addChild(tmpPanel);

        folderSelectBox.init();
        addChild(new Label("selectEncodeText", Application.getInstance().getMessage("addressbook.label.selectencoding", "Select Encoding")));
        encodingSelect = new SelectBox("encodingSelect");
        Map encodingMap = MailEncoding.getInstance().getEncodings();
        encodingSelect.setOptionMap(encodingMap);
        addChild(encodingSelect);
        encodingSelect.setSelectedOption("UTF-8");
        
    }

    public Forward onValidate(Event event) {
        try {
            // get selected folder
            String folderId = null;
            Map selected = folderSelectBox.getSelectedOptions();
            if (selected != null && selected.size() > 0) {
                folderId = selected.keySet().iterator().next().toString();
            }

            // get uploaded CSV String
            StorageFile file = csvFile.getStorageFile(event.getRequest());
            if (file == null) {
                return new Forward(FORWARD_NO_FILE);
            }

            boolean passedEncoding = false;
            String selectedEncoding = (String) ((List)encodingSelect.getValue()).get(0);
            try{
            	
            	Integer.parseInt(selectedEncoding);
            	passedEncoding= false;
            }
            catch(Exception e){
            	
            	passedEncoding = true;
            }
            
            
            String csv = "";
            
            if(passedEncoding && selectedEncoding !=null && !selectedEncoding.equals(""))
            	csv=new String(file.getData(),selectedEncoding);
            else
            	csv=new String(file.getData());

            // invoke module to import
            String userId = getWidgetManager().getUser().getId();
            AddressBookModule module = getModule();

            // store in request
            int contactCount = module.importCsv(folderId, csv, null, userId);
            event.getRequest().setAttribute(REQUEST_KEY_IMPORT_COUNT, new Integer(contactCount).toString());
            return new Forward(FORWARD_SUCCESS);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error importing contacts", e);
            return new Forward(FORWARD_ERROR);
        }
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected AddressBookModule getModule() {
        return Util.getModule(this);
    }

    protected String getUserId() {
        return Util.getUserId(this);
    }

}
