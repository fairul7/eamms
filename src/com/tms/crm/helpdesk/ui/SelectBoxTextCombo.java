package com.tms.crm.helpdesk.ui;

import kacang.stdui.FormField;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Map;

public class SelectBoxTextCombo extends FormField
{
	public static final String DEFAULT_SELECTION = "-1";
	public static final String DEFAULT_TEMPLATE = "helpdesk/selectboxTextCombo";

	private String label;
	private TextField text;
	private SelectBox select;

	public SelectBoxTextCombo()
	{
		super();
		initField();
	}

	public SelectBoxTextCombo(String s)
	{
		super(s);
		initField();
	}

	private void initField()
	{
		text = new TextField(getName() + "_text");
		text.setSize("20");
		select = new SelectBox(getName() + "_select");

		addChild(select);
		addChild(text);
	}

	public Object getValue()
	{
		String value = "";
		Map map = select.getSelectedOptions();
		if(text.getValue() != null)
			value = (String) text.getValue();
		if("".equals(value))
		{
			if(map != null && map.keySet().size() > 0)
				value = (String) map.keySet().iterator().next();
		}
		return value;
	}

	public void setSelectOptions(Map options)
	{
		select.setOptionMap(options);
	}
	
	public String getDefaultTemplate()
	{
		return DEFAULT_TEMPLATE;
	}

	/* Utility Methods */
	public String getSelectValue()
	{
		String value = "";
		if(select.getSelectedOptions().keySet().size() > 0)
			return (String) select.getSelectedOptions().keySet().iterator().next();
		return value;
	}

	public void setSelectValue(String value)
	{
		select.setSelectedOption(value);
	}

	public String getTextValue()
	{
		String value = "";
		if(text.getValue() != null)
			return (String) text.getValue();
		return value;
	}

	public void setTextValue(String value)
	{
		text.setValue(value);
	}

	public void setTextSize(String size)
	{
		text.setSize(size);
	}

	public void setSelectedOption(String option)
	{
		select.setSelectedOption(option);
	}

	/* Getters and Setters */
	public TextField getText()
	{
		return text;
	}

	public void setText(TextField text)
	{
		this.text = text;
	}

	public SelectBox getSelect()
	{
		return select;
	}

	public void setSelect(SelectBox select)
	{
		this.select = select;
	}

	public void setOptions(Map options)
	{
		select.setOptionMap(options);
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
}
