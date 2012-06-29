package com.tms.collab.formwizard.ui.validator;

import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.FormField;
import kacang.stdui.CheckBox;
import kacang.ui.Widget;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;


import com.tms.collab.formwizard.model.FormConstants;

public class ValidatorFileUpload extends ValidatorNotEmpty {
    public boolean validate(FormField formField) {
        boolean fileUploaded = super.validate(formField);
        if (!fileUploaded) {
            fileUploaded = validateFileUpload(formField);
        }

        return fileUploaded;
    }

    public boolean validateFileUpload(FormField formField) {
        String name = "", keyName = "";
        Widget parent = formField.getParent();
        Map childMap = null;
        Set keyset = null;
        boolean fileLinkExist = false;
        boolean checkboxChecked = false;

        name = formField.getName();


        childMap = parent.getChildMap();
        keyset = childMap.keySet();
        for (Iterator iterator = keyset.iterator(); iterator.hasNext();) {
            keyName = String.valueOf(iterator.next());
            if (keyName.startsWith(name + FormConstants.FIELD_LINK_SUFFIX)) {
                fileLinkExist = true;
            }
            if (keyName.startsWith(name + FormConstants.FIELD_CHECK_BOX_SUFFIX)) {
                CheckBox checkbox = (CheckBox) childMap.get(keyName);
                checkboxChecked = checkbox.isChecked();
            }
        }


        if (checkboxChecked)
            return false;
        else if (fileLinkExist)
            return true;
        else
            return false;


    }
}
