package com.tms.fms.util;

import kacang.stdui.*;
import kacang.util.Log;
import kacang.model.DefaultDataObject;
import java.text.*;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;

public class WidgetUtil {
	public static String getSbValue(SelectBox sb) {
		if (sb != null) {
			Map selected = sb.getSelectedOptions();
			if (selected.size() == 1) {
				return (String) selected.keySet().iterator().next();
			}
		}
		return null;
	}

	public static String getSbSelectionText(SelectBox sb) {
		return (String) sb.getOptionMap().get(getSbValue(sb));
	}

	public static void populateSelectBox(SelectBox sb, Object[] textArray, Object[] valueArray, String defaultValue) {
		for (int i = 0; i < valueArray.length; i++) {
			Object value = valueArray[i];
			sb.addOption(String.valueOf(value), String.valueOf(textArray[i]));
		}
		sb.setSelectedOption(defaultValue);
	}
	
	public static String getRadioValue(ButtonGroup buttonGroup) {
		if (buttonGroup != null) {
			Collection col = buttonGroup.getChildren();
			
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				Radio radio = (Radio) iterator.next();
				if (radio.isChecked()) {
					return radio.getName();
				}
			}
		}
		return null;
	}
	
	public static String getRadioValue(Radio[] radios) {
		if (radios != null && radios.length>0) {
			for (int i=0; i<radios.length; i++) {
				Radio radio = radios[i];
				if (radio.isChecked()) {
					return radio.getValue()+"";
				}
			}
		}
		return null;
	}
	
	public static Collection getCheckBoxValue(CheckBox[] checkBoxs) {
		ArrayList values=new ArrayList();
		if (checkBoxs != null && checkBoxs.length>0) {
			for (int i=0; i<checkBoxs.length; i++) {
				CheckBox checkBox = checkBoxs[i];
				if (checkBox.isChecked()) {
					values.add((String)checkBox.getValue());
				}
			}
		}
		
		return values;
	}
	
	public static void setRadioValue(ButtonGroup buttonGroup, String selectedValue) {
		if (buttonGroup != null) {
			Collection col = buttonGroup.getChildren();
			
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				Radio radio = (Radio) iterator.next();
				if (radio.getName().equals(selectedValue)) {
					radio.setChecked(true);
				} else {
					radio.setChecked(false);
				}
			}
		}
	}
	
	public static void setRadioValue(Radio[] radios, String selectedValue) {
		if (radios != null && radios.length > 0) {
			
			for (int i=0; i<radios.length; i++) {
				Radio radio = radios[i];
				if (radio.getValue().equals(selectedValue)) {
					radio.setChecked(true);
				} else {
					radio.setChecked(false);
				}
			}
		}
	}
	
	public static void populateRadio(ButtonGroup buttonGroup, String textArray[], String valueArray[], String defaultValue) {
		for (int i = 0; i < valueArray.length; i++) {
			String value = valueArray[i];
			boolean checked = (value.equals(defaultValue)) ? true : false;
			buttonGroup.addButton(new Radio(value, textArray[i], checked));
		}
	}
	
	public static void populateComboSelectBox(ComboSelectBox csb, Map rightIds) {
		// assumes the left side is already populated
		Map leftValues = csb.getLeftValues();
		Map rightValues = csb.getRightValues();
		
		ArrayList toRemove = new ArrayList();
		for (Iterator iterator = leftValues.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (rightIds.containsKey(key)) {
				rightValues.put(key, leftValues.get(key));
				toRemove.add(key);
			}
		}
		for (int i = 0; i < toRemove.size(); i++) {
			String key = (String) toRemove.get(i);
			leftValues.remove(key);
		}
		csb.setLeftValues(leftValues);
		csb.setRightValues(rightValues);
	}
	
	public static Map getStringFormatMap(Collection data, String idCol, String valCol) {
		HashMap map = new HashMap();
		
		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			DefaultDataObject o = (DefaultDataObject) iterator.next();
			String id  = (String) getProperty(o, idCol);
			String val = (String) getProperty(o, valCol);
			map.put(id, val);
		}
		
		return map;
	}
	
	public static Object getProperty(DefaultDataObject dataObject, String propertyName) {
		try {
			return PropertyUtils.getProperty(dataObject, propertyName);
		} catch (Exception e) {
			try {
				return PropertyUtils.getMappedProperty(dataObject, "propertyMap", propertyName);
			} catch (Exception e1) {
				return null;
			}
		}
	}

	public static Map arrayToMap(String textArray[], String valueArray[]) {
		HashMap hm = new HashMap();
		for (int i = 0; i < valueArray.length; i++) {
			String value = valueArray[i];
			String text  = textArray[i];
			hm.put(value, text);
		}
		return hm;
	}
	
	public static Object[] collectionToArray(Collection col, String propertyName) {
		if (col != null) {
			Object[] array = new Object[col.size()];
			Iterator iterator = col.iterator();
			for (int i = 0; i < array.length; i++) {
				DefaultDataObject dataObject = (DefaultDataObject) iterator.next();
				array[i] = getProperty(dataObject, propertyName);
			}
			return array;
		} else {
			return null;
		}
	}
	
	public static String formatDateParam(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(date);
		} else {
			return "";
		}
	}
	
	public static Date parseDateParam(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String getTime(TimeField timeField){
		String time=timeField.getHour()+":"+timeField.getMinute();
		try{
			String[] splitTime=time.split(":");
			if(splitTime[0].length()==1){
				splitTime[0]="0"+splitTime[0];
			}
			if(splitTime[1].length()==1){
				splitTime[1]="0"+splitTime[1];
			}
			return splitTime[0]+":"+splitTime[1];
		}catch(Exception e){
			Log.getLog(WidgetUtil.class).error(e.getMessage(),e);
		}
		return time;
	}
	
	public static String formatTime(String time){
		try{
			String[] splitTime=time.split(":");
			if(splitTime[0].length()==1){
				splitTime[0]="0"+splitTime[0];
			}
			if(splitTime[1].length()==1){
				splitTime[1]="0"+splitTime[1];
			}
			return splitTime[0]+":"+splitTime[1];
		}catch(Exception e){
			Log.getLog(WidgetUtil.class).error(e.getMessage(),e);
		}
		return time;
	}
	
	public static void populateTimeField(TimeField timeField, String value) {
		if (value != null && !value.equals("")) {
			Calendar TodayDate = Calendar.getInstance();
			java.util.Date now=TodayDate.getTime();
			
			String[] splitTime=value.split(":");
			now.setHours(Integer.parseInt(splitTime[0]));
			now.setMinutes(Integer.parseInt(splitTime[1]));
			timeField.setDate(now);
		}
	}
	
	
}