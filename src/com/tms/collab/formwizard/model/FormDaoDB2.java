package com.tms.collab.formwizard.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultDataObject;
import kacang.util.Log;
import kacang.util.Transaction;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.xmlwidget.TextBlockElement;
import com.tms.collab.formwizard.xmlwidget.XmlWidgetAttributes;

public class FormDaoDB2 extends FormDao{
	
	public Collection getDynamicRows(DaoQuery query, String tableName, String columnsStr,String orderStr,
            int start, int rows) throws FormDaoException {

		String sql ;
		
		sql = "SELECT username ," + columnsStr
			+ " FROM " + tableName
			+ " LEFT OUTER JOIN security_user "
			+ " ON id = " + tableName + ".userId"
			+ " LEFT OUTER JOIN frw_form_workflow"
			+ " ON " + tableName + ".formUid = frw_form_workflow.formUid"
			+ " LEFT OUTER JOIN frw_form_draft"
			+ " ON " + tableName + ".formUid = frw_form_draft.formUid"
			+	" WHERE "
			+ " frw_form_workflow.formUid is NULL "
			+ " AND frw_form_draft.formUid is NULL " + query.getStatement();
		
		
		reformOrderStr(orderStr);
		if ( orderStr != null && !orderStr.equals(""))
			sql += reformOrderStr(orderStr);
		
		try {
			return super.select(sql,DefaultDataObject.class,query.getArray(), start,rows);
		}
		catch (DaoException e) {
			throw new FormDaoException("Error selecting data from " + tableName + " table",e);
		}
	}
	
	public String reformOrderStr(String orderStr){
		
		String tempStr = StringUtils.substringAfter(orderStr, "ORDER BY");
		boolean desc = StringUtils.contains(tempStr, "DESC");
		String tempStr2 = StringUtils.substringBefore(tempStr, "DESC");
		
		StringBuffer returnStr = new StringBuffer(" ORDER BY ");
		if(desc){
			returnStr.append(" CAST(");
			returnStr.append(tempStr2);
			returnStr.append(" AS char) ");
			returnStr.append(" DESC ");
		}else{
			returnStr.append(" CAST(");
			returnStr.append(tempStr);
			returnStr.append(" AS char) ");
		}
		
		return returnStr.toString();
	}
	
	public void insertFormsConfig(FormConfigDataObject fcDO,String sqlDDL, String createTableDDL) throws DaoException{
        Transaction tx= null;
        String sql =  "UPDATE frw_form_config SET previewXml=#previewXml#,formXml=#formXml# WHERE formId=#formId#";
        int status = super.update(sql,fcDO);
        
        //the last condition is to the update statement when creating a text block, else it will throws sql error
        if (status != 0 && sqlDDL != null && !sqlDDL.equals("") && StringUtils.contains(sqlDDL, "ADD"))
			super.update(sqlDDL,null);
        if (status == 0) {
			sql = "INSERT INTO frw_form_config(formConfigId,formId,previewXml,formXml)" +
				  " VALUES(#formConfigId#,#formId#,#previewXml#,#formXml#)";
			try {
				tx = getTransaction();
				tx.begin();
				tx.update(sql,fcDO);
				
				if (createTableDDL != null && !createTableDDL.equals(""))
					tx.update(createTableDDL,null);
					
				if (sqlDDL != null && !sqlDDL.equals(""))
					tx.update(sqlDDL,null);
				tx.commit();
			} 
			catch(Exception e) {
				if (tx != null) 
					tx.rollback();						
				Log.getLog(getClass()).error(e);
				throw new DaoException(e.toString());
			}
        }
        
    }
	
	public String changeSql(String sql, String tableName) {
        if (sql.length() > 0)
        sql = alterTableSql(sql, tableName);
        return sql;
    }
	
	public String alterTableSql(String sql, String tableName) {
        if (sql.length() > 0)
            sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + sql;
        return sql;
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
                //columnxBuf.append(childName).append(" ");
                if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
                    || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
                    columnxBuf.append("SET DATA TYPE varchar(").append(xmlWidget.getSize()).append(")");
                }
                else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
                    columnxBuf.append("SET DATA TYPE numeric(").append(xmlWidget.getSize()).append(")");
                }
                else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
                    columnxBuf.append("SET DATA TYPE timestamp");
                }
                else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
                    columnxBuf.append("SET DATA TYPE CLOB(16M)");
                }
                else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
                    columnxBuf.append("SET DATA TYPE float")  ;
                }
            }
            xmlWidget.removeMetaData();

        }

        return changeSql(columnxBuf.toString(),FormModule.FORM_PREFIX  + form.getAttributeValue("name"));
    }
	
	public String generateFormXML(FormElement form,List formChildren) {
        StringBuffer columnxBuf = null;


        columnxBuf = new StringBuffer();
        for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();) {

            Element child = (Element) fromChildrenIter.next();
            if (!(child instanceof XmlWidgetAttributes)) {
                continue;
            }
            XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
            columnxBuf.append(" ADD ").append(child.getAttributeValue("name")).append(" ");

            if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
                || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
                columnxBuf.append("varchar(").append(xmlWidget.getSize()).append(")").append(" ");
            }
            else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
                columnxBuf.append("numeric(").append(xmlWidget.getSize()).append(")").append(" ");
            }
            else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
                columnxBuf.append("timestamp").append(" ");
            }
            else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
                columnxBuf.append("CLOB(16M)").append(" ");
            }
            else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
                columnxBuf.append("float").append(" ");
            }

        }

        if (columnxBuf.toString().endsWith(","))
            columnxBuf.deleteCharAt(columnxBuf.length() - 1);

        String createDDL = "ALTER TABLE " + FormModule.FORM_PREFIX + form.getAttributeValue("name")
                           + columnxBuf.toString();
        return createDDL;
    }
	
	public String createTableSQL(FormElement form) {
        String ddl = "CREATE table " + FormModule.FORM_PREFIX + form.getAttributeValue("name")
                     + "(datePosted TIMESTAMP, userId varchar(255), formUid varchar(255) )";
        return ddl;
    }

}
