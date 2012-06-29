package kacang.stdui.validator;

import kacang.stdui.FormField;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class ValidatorSelectBoxNotEmpty extends Validator {

    public ValidatorSelectBoxNotEmpty() {
        super();
    }

    public ValidatorSelectBoxNotEmpty(String name) {
        super(name);
    }

    public ValidatorSelectBoxNotEmpty(String name, String text) {
        super(name);
        setText(text);
    }

    public boolean validate(FormField formField) {
        Object value = formField.getValue();
        if (value == null) {
            return false;
        } else if (value instanceof Collection || value instanceof Map) {
            return !"-1".equals(((Collection)value).iterator().next().toString());
        } else {
            return value.toString().trim().length() > 0;
        }
    }

}
