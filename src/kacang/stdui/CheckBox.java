package kacang.stdui;

import kacang.ui.Widget;

import java.util.Arrays;
import java.util.List;

/**
 *<p> This class is used to create a HTML form input element - check box -- an item that can be selected or deselected,
 *  and which displays its state to the user. By convention, any number of check boxes in a group can be selected.
 * </p>
 */
public class CheckBox extends FormField {
    private String groupName;
    private boolean checked = false;
    private String onBlur;
    private String onClick;
    private String onFocus;
    private String text;
    private String mouseOverText;

    /**
     * Default constructor of CheckBox. Programmer is required to set the name for the check box
     * before it is added as a child to its parent by calling <code>setName</code> method.
     */
    public CheckBox() {
        super();
    }

    /**
     * Creates an initially unchecked check box with the specifed name.
     * @param name The specified name.
     */
    public CheckBox(String name) {
        super(name);
        setValue(getFieldName());
    }

    /**
     * Creates an initially unchecked check box with the specifed name and text.
     * @param name The name of the check box.
     * @param text The text of the check box.
     */
    public CheckBox(String name, String text) {
        this(name);
        setText(text);
    }

    /**
     * Creates a check box with name and text and specifies whether or not it is initially selected.
     * @param name The name of the check box.
     * @param text The text of the check box.
     * @param checked a boolean value indicating the initial selection state.
     * If true the check box is checked; otherwise, the check box is initially unchecked.
     */
    public CheckBox(String name, String text, boolean checked) {
        this(name, text);
        this.checked = checked;
    }

    /**
     * Return the check box's text.
     * @return The button's text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the check box's text.
     * @param text The check box' text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Return the state of the check box. True if the check box is checked, false if it's not.
     * @return
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Set the state of the check box.
     * @param checked  true if the button is checked, otherwise false
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public kacang.ui.Forward onSubmit(kacang.ui.Event evt) {
        String[] values = evt.getRequest().getParameterValues(getFieldName());
        if (values != null) {
            List valueList = Arrays.asList(values);
            if (valueList.contains(getAbsoluteName()))
                setChecked(true);
            else
                setChecked(false);
        } else {
            setChecked(false);
        }
        // perform validation
        performValidation(evt);
        return null;
    }

    /**
     * Return the name of the button group to which  the check box belongs.
     * @return The name of the button group.
     */
    public java.lang.String getGroupName() {
        return groupName;
    }

    /**
     * Set the name of the button group to which the check box belongs.
     * @param groupName The name of the button group.
     */
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    /**
     * Return the name for HTML form input's name attribute.
     * @return The name.
     */
    public String getFieldName() {
        String cname = (getGroupName() != null) ? getGroupName() : getName();
        if (getParent() != null)
            return getParent().getAbsoluteName() + Widget.SEPARATOR_WIDGET + cname;
        else
            return getAbsoluteName() + SEPARATOR_WIDGET + cname;
    }

    public String getDefaultTemplate() {
        return "checkbox";
    }

    public String getMouseOverText() {
		return mouseOverText;
	}

	public void setMouseOverText(String mouseOverText) {
		this.mouseOverText = mouseOverText;
	}

	/**
     * Return the handle script for OnBlur event of HTML check box
     * @return The handle script
     */
    public String getOnBlur() {
        return onBlur;
    }

    /**
     * Set the handle script for OnBlur event of HTML check box
     * @param onBlur The handle script
     */
    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    /**
     * Return the handle script for OnClick event of HTML check box
     * @return The handle script
     */
    public String getOnClick() {
        return onClick;
    }

    /**
     * Set the handle script for OnClick event of HTML check box
     * @param onClick The handle script
     */
    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    /**
     * Return the handle script for OnFocus event of HTML check box
     * @return The handle script
     */
    public String getOnFocus() {
        return onFocus;
    }

    /**
     * Set the handle script for OnFocus event of HTML check box
     * @param onFocus The handle script
     */
    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }
}