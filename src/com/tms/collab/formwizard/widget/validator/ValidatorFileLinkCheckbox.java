package com.tms.collab.formwizard.widget.validator;

import kacang.stdui.validator.Validator;
import kacang.stdui.FormField;


import com.tms.collab.formwizard.widget.FileLinkCheckbox;

public class ValidatorFileLinkCheckbox extends Validator {
    public ValidatorFileLinkCheckbox() {
        super();
    }

    public ValidatorFileLinkCheckbox(String name) {
        super(name);
    }

    public ValidatorFileLinkCheckbox(String name, String text) {
        super(name);
        setText(text);
    }

    public boolean validate(FormField formField) {
        if (formField instanceof FileLinkCheckbox) {
            FileLinkCheckbox fileLinkCheckbox = (FileLinkCheckbox) formField;
            if (fileLinkCheckbox.isRequired() && fileLinkCheckbox.getCheckBox().isChecked() && fileLinkCheckbox.getFile().getValue() == null)
                return false;
            else return true;
        }
        return false;
    }





}
