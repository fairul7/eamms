package com.tms.collab.formwizard.model;

import kacang.model.*;
import kacang.util.*;
import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.xmlwidget.TextBlockElement;
import com.tms.collab.formwizard.xmlwidget.XmlWidgetAttributes;
import com.tms.collab.formwizard.engine.TextFieldField;
import java.sql.SQLException;
import java.util.*;
import org.jdom.Element;

public class FormDaoSybase extends FormDaoMsSql {
	// note: Sybase cannot do a multi-statement transaction together with a "CREATE TABLE"
	public void insertFormsConfig(FormConfigDataObject fcDO, String sqlDDL, String createTableDDL) throws DaoException {
		Transaction tx = null;
		Transaction txTable = null;
		String sql = "UPDATE frw_form_config SET previewXml=#previewXml#,formXml=#formXml# WHERE formId=#formId#";
		
		int status = super.update(sql, fcDO);
		if (status != 0 && sqlDDL != null && !sqlDDL.equals("") && !sqlDDL.equals("-") && sqlDDL.length() > 47) {
			try {
				super.update(sqlDDL, null);
			} catch(DaoException e) {
				String warningText = "Warning: no columns to drop, add or modify.";
				if (e.toString().indexOf(warningText) != -1) {
					// warning caught
				} else {
					throw e;
				}
			}
		}
		
		if (status == 0) {
			sql = "INSERT INTO frw_form_config(formConfigId,formId,previewXml,formXml)" +
				" VALUES(#formConfigId#,#formId#,#previewXml#,#formXml#)";
			
			try {
				tx = getTransaction();
				tx.begin();
				tx.update(sql, fcDO);
				
				if (sqlDDL != null && !sqlDDL.equals("") && !sqlDDL.equals("-")) {
					tx.update(sqlDDL, null);
				}
				
				if (createTableDDL != null && !createTableDDL.equals("")) {
					txTable = getTransaction();
					txTable.begin();
					txTable.update(createTableDDL, null);
				}
				
				tx.commit();
				if (txTable != null) {
					txTable.commit();
				}
			}  catch(Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				if (txTable != null) {
					txTable.rollback();
				}
				Log.getLog(getClass()).error(e.toString(), e);
				throw new DaoException(e.toString());
			}
		}
	}
	
	public String generateFormXML(FormElement form, List formChildren) {
		StringBuffer columnxBuf = new StringBuffer();
		
		for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();) {
			Element child = (Element) fromChildrenIter.next();
			if (!(child instanceof XmlWidgetAttributes)) {
				continue;
			}
			
			XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
			columnxBuf.append(child.getAttributeValue("name")).append(" ");
			
			if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
				columnxBuf.append("nvarchar(").append(xmlWidget.getSize()).append(")").append(" NULL,");
			} else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
				columnxBuf.append("int").append(" NULL,");
			} else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
				columnxBuf.append("datetime").append(" NULL,");
			} else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
				columnxBuf.append("text").append(" NULL,");
			} else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
				columnxBuf.append("float").append(" NULL,");
			}
		}
		
		if (columnxBuf.toString().endsWith(",")) {
			columnxBuf.deleteCharAt(columnxBuf.length() - 1);
		}
		
		String columnx = columnxBuf.toString();
		if (!columnx.equals("")) {
			String createDDL = "ALTER TABLE " + FormModule.FORM_PREFIX + form.getAttributeValue("name") + 
					" ADD " + columnx;
			return createDDL;
		} else {
			return "-";
		}
	}
	
	public String generateEditFormFieldXML(FormElement form, String fieldId) {
		StringBuffer columnxBuf = null;
		List formChildren = form.getChildren();
		String childName = null;
		columnxBuf = new StringBuffer();
		
		for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();) {
			Element child = (Element) fromChildrenIter.next();
			
			if (child instanceof TextBlockElement)
				((TextBlockElement)child).removeMetaData();
			
			if (!(child instanceof XmlWidgetAttributes)) {
				continue;
			}
			
			XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
			childName = child.getAttributeValue("name");
			if (childName.equals(fieldId)) {
				columnxBuf.append(childName).append(" ");
				if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
						|| xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
					columnxBuf.append("nvarchar(").append(xmlWidget.getSize()).append(")");
				} else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
					columnxBuf.append("int");
				} else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
					columnxBuf.append("datetime");
				} else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
					columnxBuf.append("text");
				} else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
					columnxBuf.append("float")  ;
				}
			}
			xmlWidget.removeMetaData();
		}
		
		String columnx = columnxBuf.toString();
		if (!columnx.equals("")) {
			String createDDL = "ALTER TABLE " + FormModule.FORM_PREFIX + form.getAttributeValue("name") + 
					" MODIFY " + columnx + " NULL";
			return createDDL;
		} else {
			return "-";
		}
	}
	
	// note: Sybase cannot do a multi-statement transaction together with a "DROP TABLE"
	public void deleteForms(String formsID) throws DaoException{
		Transaction tx = null;
		Transaction txTable = null;
		String formName = "";
		
		String sql_select = "SELECT formName FROM frw_form WHERE formId = '" + formsID + "'";
		Collection row = super.select(sql_select, HashMap.class, null, 0, -1);
		if (row.iterator().hasNext()) {
			Map map = (Map)row.iterator().next();
			formName = map.get("formName").toString();
		}
		
		try {
			tx = getTransaction();
			tx.begin();
			
			String sql = "DELETE FROM frw_form_user_access WHERE formId='"+formsID+"'";
			tx.update(sql,null);
			
			sql= "DELETE FROM frw_form_group_access WHERE formId='"+formsID+"'";
			tx.update(sql,null);
			
			sql = "DELETE FROM frw_form_memo_submission WHERE formId='"+formsID+"'";
			tx.update(sql,null);
			
			sql= "DELETE FROM frw_form_query WHERE formId='"+formsID+"'";
			tx.update(sql,null);
			
			sql = "DELETE FROM frw_form_properties WHERE formId = '" + formsID +"'";
			tx.update(sql,null);
			
			sql = "DELETE FROM frw_form_template_field WHERE formId = '" + formsID +"'";
			tx.update(sql,null);
			
			sql= "DELETE FROM frw_form_draft WHERE formId='"+formsID+"'";
			tx.update(sql,null);
			
			sql= "DELETE FROM frw_form WHERE formId='"+formsID+"'";
			int stat = tx.update(sql,null);
			
			if (stat == 1) {
				sql = "DELETE FROM frw_form_approval WHERE formId='"+formsID+"'";
				tx.update(sql,null);
				
				sql = "DELETE FROM frw_form_workflow WHERE formId='"+formsID+"'";
				tx.update(sql,null);
				
				sql = "DELETE FROM frw_form_config WHERE formId='"+formsID+"'";
				tx.update(sql,null);
				
				try {
					txTable = getTransaction();
					txTable.begin();
					sql = "DROP TABLE " + FormModule.FORM_PREFIX  + formName;
					txTable.update(sql,null);
				} catch (SQLException e) {
				}                                   
			}
			
			tx.commit();
			if (txTable != null) {
				txTable.commit();
			}
		} catch(Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (txTable != null) {
				txTable.rollback();
			}
			Log.getLog(getClass()).error(e.toString());
			throw new DaoException(e.toString());
		}
	}
	
	public String saveForm(FormWorkFlowDataObject wfDO) throws FormDaoException{
		StringBuffer columnBuffer = new StringBuffer();
		StringBuffer holderBuffer = new StringBuffer();
		List valueList = new ArrayList();
		
		// get info for saving TextFeildField
		Map infoMap = FormDao.getTextFieldInfoMap(wfDO.getFormId());
		
		Map valuesMap = wfDO.getValues();
		for (Iterator valuesIterator = valuesMap.keySet().iterator(); valuesIterator.hasNext();) {
			String fieldName = (String) valuesIterator.next();
			
			String sField = fieldName;
			if (fieldName.startsWith("_")) {
				sField = "\"" + fieldName + "\"";
			}
			
			columnBuffer.append(sField + ", ");
			holderBuffer.append("?, ");
			
			if (valuesMap.get(fieldName) instanceof FormData) {
				FormData data = (FormData) valuesMap.get(fieldName);
				Object value = getFormDataValue(fieldName, data, infoMap);
				valueList.add(value);
			} else {
				valueList.add(valuesMap.get(fieldName));
			}
		}
		
		columnBuffer.append("formUid, datePosted, userId");
		holderBuffer.append("?, ?, ?");
		valueList.add(wfDO.getFormUid());
		valueList.add(new Date());
		valueList.add(wfDO.getUserId());
		try {
			String sql =
					"INSERT INTO " + wfDO.getFormName() + 
					"	(" + columnBuffer.toString() + ") " + 
					"VALUES" + 
					"	(" + holderBuffer.toString() + ") ";
			super.update(sql, normalizeListToString(valueList).toArray());
		} catch (DaoException e) {
			throw new FormDaoException("Error inserting data into frw_form_name_" +  wfDO.getFormName() + " table", e);
		}
		
		String formPK = wfDO.getFormUid();
		return formPK;
	}
	
	public void saveEditFormData(FormWorkFlowDataObject wfDO) throws FormDaoException {
		StringBuffer columnBuffer = new StringBuffer();
		StringBuffer holderBuffer = new StringBuffer();
		List valueList = new ArrayList();
		
		// delete old data
		deleteFormData(wfDO.getFormName(), wfDO.getFormUid());
		
		// get info for saving TextFeildField
		Map infoMap = FormDao.getTextFieldInfoMap(wfDO.getFormId());
		
		Map valuesMap = wfDO.getValues();
		for (Iterator valuesIterator = valuesMap.keySet().iterator(); valuesIterator.hasNext();) {
			String fieldName = (String) valuesIterator.next();
			
			columnBuffer.append(fieldName + ", ");
			holderBuffer.append("?, ");
			
			if (valuesMap.get(fieldName) instanceof FormData) {
				FormData data = (FormData) valuesMap.get(fieldName);
				Object value = getFormDataValue(fieldName, data, infoMap);
				valueList.add(value);
			} else {
				valueList.add(valuesMap.get(fieldName));
			}
		}
		
		columnBuffer.append("formUid, datePosted, userId");
		holderBuffer.append("?, ?, ?");
		valueList.add(wfDO.getFormUid());
		valueList.add(new Date());
		valueList.add(wfDO.getUserId());
		
		try {
			String sql =
					"INSERT INTO " + wfDO.getFormName() + 
					"	(" + columnBuffer.toString() + ") " + 
					"VALUES" + 
					"	(" + holderBuffer.toString() + ") ";
			super.update(sql, normalizeListToString(valueList).toArray());
		} catch (DaoException e) {
			throw new FormDaoException("Error inserting data into " + wfDO.getFormName() + " table", e);
		}
	}
	
	private Object getFormDataValue(String fieldName, FormData data, Map infoMap) {
		Object value = data.getValue();
		if (value instanceof String && data.getType() instanceof TextFieldField) {
			if (infoMap.containsKey(fieldName)) {
				TextFieldField textFieldField = (TextFieldField) infoMap.get(fieldName);
				if (textFieldField.getValidatorIsInteger() != null) {
					value = new Integer(value.toString());
				} else if (textFieldField.getValidatorIsNumeric() != null) {
					value = new Float(value.toString());
				}
			}
		}
		return value;
	}
}
