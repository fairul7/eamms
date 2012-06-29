package com.tms.cms.document.ui;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.document.Document;
import kacang.services.storage.StorageFile;
import kacang.stdui.CheckBox;
import kacang.stdui.FileUpload;
import kacang.stdui.FormField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;

/**
 * Form for adding/editing a Document.
 */
public class DocumentContentObjectForm extends ContentObjectForm {

    private FileUpload fileUpload;
    private CheckBox deleteCheckBox;

    public DocumentContentObjectForm() {
    }

    public DocumentContentObjectForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/documentContentObjectForm";
    }

    public void init(Event evt) {
        super.init(evt);

        nameField.removeChild(nameFieldValidator);

        fileUpload = new FileUpload("fileUpload");
        fileUpload.addChild(new ValidatorNotEmpty("validFile") {
            public boolean validate(FormField formField) {
                Document doc = (Document)getContentObject();
                boolean newFile = super.validate(formField);
                boolean deleteChecked = deleteCheckBox.isChecked();
                boolean existingFile = (doc != null && doc.getFileName() != null && doc.getFileName().trim().length() > 0);
                return ((!deleteChecked && existingFile) || newFile);
            }
        });
        addChild(fileUpload);

        deleteCheckBox = new CheckBox("deleteCheckBox");
        deleteCheckBox.setValue("true");
        addChild(deleteCheckBox);
    }

    protected void populateFields(Event evt) {
        super.populateFields(evt);
    }

    protected void populateContentObject(Event evt, ContentObject contentObject) {
        super.populateContentObject(evt, contentObject);

        try {
            // set doc properties
            Document doc = (Document)contentObject;
            StorageFile sf = fileUpload.getStorageFile(evt.getRequest());
            if (sf != null) {
                String fileName = sf.getName();
                doc.setStorageFile(sf);
                doc.setFileName(fileName);
                doc.setFilePath(sf.getAbsolutePath());
                doc.setFileSize(sf.getSize());
                doc.setContentType(sf.getContentType());

                // set name to document filename if not specified
              //if no name is given, then put filename as name
				if(doc.getName() ==null || "".equals(doc.getName()))
				{
				    if (fileName != null && fileName.trim().length() > 0) {
						doc.setName(fileName);
					}
				}
            }
            else if (deleteCheckBox.isChecked()) {
                doc.setStorageFile(null);
                doc.setFileName(null);
                doc.setFilePath(null);
                doc.setFileSize(0);
                doc.setContentType(null);
            }

        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
