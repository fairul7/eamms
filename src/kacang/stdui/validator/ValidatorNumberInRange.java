package kacang.stdui.validator;

import kacang.stdui.FormField;

public class ValidatorNumberInRange extends Validator {
	private double num1;
	private double num2;

    public ValidatorNumberInRange() {
        super();
    }

    public ValidatorNumberInRange(String name) {
        super(name);
    }

    public ValidatorNumberInRange(String name, String text, double min, double max) {
        super(name);
        setText(text);
        this.num1=min;
        this.num2=max;
    }

    public boolean validate(FormField formField) {
        try {
            Object val = formField.getValue();
            if (val.toString().trim().length() == 0) {
                return true;
            }
            double value = Double.parseDouble(val.toString());
            if(value>=num1 && value<+num2){
            	return true;
            }else{
            	return false;
            }
        } catch (Exception e) {

        }
        return true;
    }
}
