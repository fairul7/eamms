package com.tms.util.validator;

import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.FormField;
import kacang.stdui.SelectBox;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 27, 2006
 * Time: 12:33:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectBoxValidatorNotEmpty extends ValidatorNotEmpty {

        public SelectBoxValidatorNotEmpty() {
        }

        public SelectBoxValidatorNotEmpty(String name) {
            super(name);
        }

        public SelectBoxValidatorNotEmpty(String name, String text) {
            super(name, text);
        }

        public boolean validate(FormField formField) {
                    if (formField.isHidden()) {
                        return true;
                    }
                    boolean valid = super.validate(formField);
                    if (valid && formField instanceof SelectBox) {
                        Map map = ((SelectBox)formField).getSelectedOptions();
                        if (map != null && map.size() > 0) {
                            String key = (String)map.keySet().iterator().next();
                        if (key == null || key.trim().length() == 0 || key.equals("-1")) {
                                valid = false;
                            }
                        }
                        else {
                            valid = false;
                        }
                    }
                    return valid;
     }
}
