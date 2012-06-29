package kacang.stdui;

import java.util.Date;
import java.util.Calendar;

import kacang.Application;
import kacang.stdui.validator.ValidatorDateNotNull;

/**
 * Represents an HTML input text field that provides a popup calendar.
 */
public class DatePopupField extends DateField {

    public static final String DEFAULT_FORMAT = "dd-MM-yyyy";

    private boolean optional = false;
    private String onBlur;
    private String onChange;
    private String onFocus;
    private String onSelect;
    private String size;
    private String maxlength;
    private String value;

    /**
     * Creates a new instance of TextField
     */
    public DatePopupField() {
        setDate(null);
        setFormat(DEFAULT_FORMAT);
    }

    /**
     * Creates a new instance of TextField with specific name
     */
    public DatePopupField(String name) {
        setName(name);
        setDate(null);
        setFormat(DEFAULT_FORMAT);
    }

    /**
     * Creates a new instance of TextField with specific name and value
     */
    public DatePopupField(String name, String value) {
        setName(name);
        setValue(value);
        setFormat(DEFAULT_FORMAT);
    }

    public String getDefaultTemplate() {
        return "datePopupField";
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getOnBlur() {
        return onBlur;
    }

    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public String getOnFocus() {
        return onFocus;
    }

    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getOnSelect() {
        return onSelect;
    }

    public void setOnSelect(String onSelect) {
        this.onSelect = onSelect;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    /**
     * Return the selected date.
     * @return The selected date (String), or "" if no date is selected.
     */
    public Object getValue() {
        if (getDate() != null) {
            return getDateFormat().format(getDate());
        }
/*
        else if (!optional && !isInvalid()) {
            // return current date
            Date current = new Date();
            setDate(current);
            return getDateFormat().format(current);
        }
*/
        else {
            return value;
        }
    }

    /**
     * Set the date of the DateField widget.
     * @param value The date.
     */
    public void setValue(Object value) {
        try {
            this.value = (value != null) ? value.toString() : null;
            setDate(getDateFormat().parse(value.toString()));
        } catch (Exception e) {
            setDate(null);
        }
    }

    /**
     * Return the selected day of month in DateField.
     * @return The selected day of month.
     */
    public int getDayOfMonth() {
        if (getDate() == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Return the selected month in DataField.
     * @return The selected month.
     */
    public int getMonth() {
        if (getDate() == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.MONTH);
    }

    /**
     * Return the year entered in the DateField.
     * @return The selected year.
     */
    public int getYear() {
        if (getDate() == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.YEAR);
    }


    /**
     * Returns name refering to the object which to be use in javascript
     * It's the absoluteName but replacing '.' with '_'
     * @return name refering to the object which to be use in javascript
     */
    public String getAbsoluteNameForJavaScript() {
        return getAbsoluteName().replace(SEPARATOR_WIDGET.charAt(0), '_');
    }

    public kacang.ui.Forward onSubmit(kacang.ui.Event evt) {
        try {
        	if(!optional){
        		addChild(new ValidatorDateNotNull("DateNotNull", ""));
        	}
        	
            // get value
            String dateStr = evt.getRequest().getParameter(getAbsoluteName());
            setValue(dateStr);
            if (optional && (dateStr == null || dateStr.trim().length() == 0)) {
                setInvalid(false);
            }
            else if (getDate() == null) {
                setInvalid(true);
                Form form = getParentForm();
                if (form != null)
                    form.setInvalid(true);
            }
            else {
                setInvalid(false);
            }
            performValidation(evt);
            return null;
        } catch (Exception e) {
            setInvalid(true);
            Form form = getParentForm();
            if (form != null)
                form.setInvalid(true);
            return null;
        }

    }

}

