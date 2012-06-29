package com.tms.collab.formwizard.model;

import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.xmlwidget.XmlWidgetAttributes;
import com.tms.collab.formwizard.xmlwidget.TextBlockElement;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultDataObject;
import kacang.util.JdbcUtil;
import kacang.util.Transaction;
import kacang.util.Log;
import org.jdom.Element;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class FormDaoOracle extends FormDao
{
	public String createTableSQL(FormElement form)
	{
        String ddl = "CREATE table " + FormModule.FORM_PREFIX + form.getAttributeValue("name")
                     + "(datePosted DATE, userId VARCHAR(255), formUid VARCHAR(255) PRIMARY KEY)";
        return ddl;
    }

	public String generateFormXML(FormElement form,List formChildren)
	{
		StringBuffer columnxBuf = null;
        columnxBuf = new StringBuffer();
        for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();)
		{
            Element child = (Element) fromChildrenIter.next();
            if (!(child instanceof XmlWidgetAttributes))
                continue;
            XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
            columnxBuf.append(" ADD ");
			columnxBuf.append("\"");
			columnxBuf.append(child.getAttributeValue("name"));
			columnxBuf.append("\" ");
            if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE)
                columnxBuf.append("varchar(").append(xmlWidget.getSize()).append(")").append(",");
            else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE)
                columnxBuf.append("number(").append(xmlWidget.getSize()).append(")").append(",");
            else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE)
                columnxBuf.append("date").append(",");
            else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE)
                columnxBuf.append("clob").append(",");
            else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType())
                columnxBuf.append("number(9,2)").append(",");
        }
        if (columnxBuf.toString().endsWith(","))
            columnxBuf.deleteCharAt(columnxBuf.length() - 1);
        String createDDL = "ALTER TABLE " + FormModule.FORM_PREFIX + form.getAttributeValue("name") + columnxBuf.toString();
        return createDDL;
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
               columnxBuf.append(childName).append(" ");
               if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
                   || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
                   columnxBuf.append("varchar2(").append(xmlWidget.getSize()).append(")");
               }
               else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
                   columnxBuf.append("number(").append(xmlWidget.getSize()).append(")");
               }
               else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
                   columnxBuf.append("date");
               }
               else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
                   columnxBuf.append("clob");
               }
               else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
                   columnxBuf.append("number(9,2)")  ;
               }
           }
           xmlWidget.removeMetaData();

       }

       return changeSql(columnxBuf.toString(),FormModule.FORM_PREFIX  + form.getAttributeValue("name"));
   }


    public Collection selectForms(String userID,String action,String sort,boolean desc,int start, int rows)  throws DaoException
	{
        Collection forms= new ArrayList();
        String sql ="SELECT DISTINCT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated,su.firstname AS formUpdatedByName"+
                    " FROM frw_form f INNER JOIN security_user su ON (su.id = f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_user_access fua ON f.formId = fua.formId "+
                    " LEFT OUTER JOIN frw_form_group_access fga ON f.formId = fga.formId "+
                    " LEFT OUTER JOIN security_user_group sug ON sug.groupid = fga.groupId " +
                    " LEFT OUTER JOIN security_group sg ON sg.id = sug.groupid " +
                    " WHERE 1=1 AND ((f.formPublicFlag='0') OR (f.formPublicFlag='1' AND fua.userId='"+userID+"') OR "+
                    " (f.formPublicFlag='2' AND sug.userid='"+userID+"'  AND sg.active='1'))";
        if(action.equals("view"))
            sql=sql+ " AND f.isActive='1' AND f.isPending='0'";
        if(sort.equals("formDisplayName"))
            sql=sql+" ORDER BY f.formDisplayName";
        else if(sort.equals("formDateCreated"))
            sql=sql+" ORDER BY f.formDateCreated";
        else if(sort.equals("formUpdatedByName"))
            sql = sql+" ORDER BY su.firstname";
        if(desc)
              sql = sql+" DESC";
        forms = super.select(sql,FormDataObject.class,null,start,rows);
        return forms;
    }

    public int selectForms(String userID,String action) throws DaoException
	{
        String sql ="SELECT count(*) AS total "+
                    " FROM frw_form f INNER JOIN security_user su ON (su.id =f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_user_access fua ON f.formId = fua.formId "+
                    " LEFT OUTER JOIN frw_form_group_access fga ON f.formId = fga.formId "+
                    " LEFT OUTER JOIN security_user_group sug ON sug.groupid = fga.groupId " +
                    " LEFT OUTER JOIN security_group sg ON sg.id = sug.groupid " +
                    " WHERE 1=1 AND ((f.formPublicFlag='0') OR (f.formPublicFlag='1' AND fua.userId='"+userID+"') OR "+
                    " (f.formPublicFlag='2' AND sug.userid='"+userID+"'  AND sg.active='1'))";
		if(action.equals("view"))
            sql=sql+ " AND f.isActive='1' AND f.isPending='0'";
		Map row =  (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
		return Integer.parseInt(row.get("total").toString());
    }

    public InputStream selectFormXML(String formID) throws FormDaoException
	{
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        InputStream xmlReader=null;
        String sql ="SELECT formXml FROM frw_form_config WHERE formId='"+formID+"'";
        try
		{
            conn=  getDataSource().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next())
                xmlReader = new ByteArrayInputStream(rs.getString("formXML").getBytes());
        }
		catch (Exception e)
		{
            throw new FormDaoException(e.toString());
        }
		finally
		{
            if (conn != null)
			{
                try
				{
                    conn.close();
                }
				catch (SQLException e1) {}
            }
        }
        return xmlReader;
    }

    public InputStream selectFormPreviewXML(String formID) throws FormDaoException
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs=null;
		InputStream xmlReader=null;
		String sql ="SELECT previewXml AS formXML FROM frw_form_config WHERE formId='"+formID+"'";
		try
		{
			conn=  getDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
				xmlReader = new ByteArrayInputStream(rs.getString("formXML").getBytes());
		}
		catch (Exception e)
		{
			throw new FormDaoException(e.toString());
		}
		finally
		{
			if (conn != null)
			{
				try
				{
					conn.close();
				}
				catch (SQLException e1) {}
			}
		}
		return xmlReader;
	}

    public Collection selectQueryForms(String userID,String sort,boolean desc,int start, int rows) throws DaoException
	{
        Collection forms = new ArrayList();
        String sql ="SELECT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated,su.firstname AS formsUpdatedByName"+
                    " FROM frw_form f INNER JOIN security_user su ON (su.id =f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_query fq ON fq.formId=f.formId "+
                    " WHERE 1=1 AND fq.userId='"+userID+"'";
        if(sort.equals("formDisplayName"))
             sql=sql+" ORDER BY f.formDisplayName";
        else if(sort.equals("formDateCreated"))
             sql=sql+" ORDER BY f.formDateCreated";
        else if(sort.equals("formsUpdatedByName"))
             sql=sql+" ORDER BY su.firstname";
        if(desc)
             sql = sql+" DESC";
        forms = super.select(sql,FormDataObject.class,null,start,rows);
        return forms;
    }

    public int selectQueryForms(String userID) throws DaoException
	{
        String sql ="SELECT count(*) AS total"+
                    " FROM  frw_form f INNER JOIN security_user su ON (su.id =f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_query fq ON fq.formId=f.formId "+
                    " WHERE 1=1  AND fq.userId='"+userID+"'";
        Map row =  (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }


	public Collection getDynamicRows(DaoQuery query, String tableName, String columnsStr,String orderStr, int start, int rows) throws FormDaoException
	{
        String sql ;
		StringBuffer cols = new StringBuffer("");
		StringTokenizer tokenizer = new StringTokenizer(columnsStr, ",");
		while(tokenizer.hasMoreTokens())
		{
			if(!"".equals(cols.toString()))
				cols.append(",");
			String token = tokenizer.nextToken();
			if(token.startsWith("_"))
				cols.append("\"" + token + "\"");
			else
				cols.append(token);
		}
		sql = "SELECT username ," + cols.toString()
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
        if ( orderStr != null && !orderStr.equals(""))
            sql += orderStr;
        try
		{
            return super.select(sql,DefaultDataObject.class,query.getArray(), start,rows);
        }
        catch (DaoException e)
		{
            throw new FormDaoException("Error selecting data from " + tableName + " table",e);
        }
    }

	public Collection getDynamicRows(DaoQuery query, String tableName, String columnsStr,String userId,String orderStr, int start, int rows) throws FormDaoException
	{
        String sql ;
		StringBuffer cols = new StringBuffer("");
		StringTokenizer tokenizer = new StringTokenizer(columnsStr, ",");
		while(tokenizer.hasMoreTokens())
		{
			if(!"".equals(cols.toString()))
				cols.append(",");
			String token = tokenizer.nextToken();
			if(token.startsWith("_"))
				cols.append("\"" + token + "\"");
			else
				cols.append(token);
		}
		sql = "SELECT username ," + cols.toString()
			  + " FROM security_user INNER JOIN " + tableName
              + " ON id = " + tableName + ".userId"
			  + " LEFT OUTER JOIN frw_form_workflow"
			  + " ON " + tableName + ".formUid = frw_form_workflow.formUid"
              + " LEFT OUTER JOIN frw_form_draft"
              + " ON " + tableName + ".formUid = frw_form_draft.formUid"
			  +	" WHERE "
			  + " frw_form_workflow.formUid is NULL "
              + " AND frw_form_draft.formUid is NULL "
              + " AND " + tableName + ".userId = '" + userId + "'" + query.getStatement();
        if ( orderStr != null && !orderStr.equals(""))
            sql += orderStr;
        try
		{
            return super.select(sql,DefaultDataObject.class,query.getArray(), start,rows);
        }
        catch (DaoException e)
		{
            throw new FormDaoException("Error querying " + tableName + " table",e);
        }
    }

	public String saveForm(FormWorkFlowDataObject wfDO)throws FormDaoException{
        String formPK;
        formPK = wfDO.getFormUid();
        StringBuffer columnBuffer = new StringBuffer();
        StringBuffer holderBuffer = new StringBuffer();
        Map valuesMap = wfDO.getValues();
        List valueList = new ArrayList();
        for (Iterator valuesIterator = valuesMap.keySet().iterator(); valuesIterator.hasNext();) {
            String fieldName = (String) valuesIterator.next();

            String sField = fieldName;
            if (fieldName.startsWith("_"))
                sField = "\"" + fieldName + "\"";

            columnBuffer.append(sField + ",");
            holderBuffer.append("?,");

            if (valuesMap.get(fieldName) instanceof FormData)
                valueList.add( ((FormData)valuesMap.get(fieldName)).getValue());
            else
                valueList.add(valuesMap.get(fieldName));


        }
        columnBuffer.append("formUid,datePosted, userId");
        holderBuffer.append("?,?,?");
        valueList.add(wfDO.getFormUid());
        valueList.add(new Date());
        valueList.add(wfDO.getUserId());
        try {
            super.update("insert into " + wfDO.getFormName() + " (" +
                columnBuffer.toString() +
                ") values (" + holderBuffer.toString() +
                ")",normalizeListToString(valueList).toArray());
        }
        catch (DaoException e) {
            throw new FormDaoException("Error inserting data into frw_form_name_" +  wfDO.getFormName() + " table",e);
        }


        return formPK;
    }

	public void saveEditFormData(FormWorkFlowDataObject wfDO) throws FormDaoException {
        StringBuffer columnBuffer = new StringBuffer();
        StringBuffer holderBuffer = new StringBuffer();


        deleteFormData(wfDO.getFormName(),wfDO.getFormUid());


        Map valuesMap = wfDO.getValues();
        List valueList = new ArrayList();
        for (Iterator valuesIterator = valuesMap.keySet().iterator(); valuesIterator.hasNext();) {
            String fieldName = (String) valuesIterator.next();

            if (fieldName.startsWith("_"))
                fieldName = "\"" + fieldName + "\"";

            columnBuffer.append(fieldName + ",");
            holderBuffer.append("?,");

            if (valuesMap.get(fieldName) instanceof FormData)
                valueList.add( ((FormData)valuesMap.get(fieldName)).getValue());
            else
                valueList.add(valuesMap.get(fieldName));
        }
        columnBuffer.append("formUid,userId,datePosted");
        holderBuffer.append("?,?,?");
        valueList.add(wfDO.getFormUid());
        valueList.add(wfDO.getUserId());
        valueList.add(new Date());

        try {
            super.update("insert into " + wfDO.getFormName() + " (" +
                        columnBuffer.toString() +
                        ") values (" + holderBuffer.toString() +
                        ")",normalizeListToString(valueList).toArray());
        }
        catch (DaoException e) {
            throw new FormDaoException("Error inserting data into " + wfDO.getFormName() + " table",e);
        }
	}

	public Collection getSubmittedFormData(String id, String tableName, String columnsStr) throws FormDaoException
	{
		String sql;
		StringBuffer escapedCols = new StringBuffer("");
		StringTokenizer tokenizer = new StringTokenizer(columnsStr, ",");
		while(tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			if(!("".equals(escapedCols.toString())))
				escapedCols.append(",");
			if(token.startsWith("_"))
				escapedCols.append("\"" + token + "\"");
			else
				escapedCols.append(token);
		}
		if (columnsStr != null && !columnsStr.equals(""))
		{
            sql = "select " + escapedCols.toString() + " from " + FormModule.FORM_PREFIX  + tableName + " WHERE formUid = '" + id + "'";
            try
			{
                return super.select(sql,DefaultDataObject.class,null, 0,-1);
            }
            catch (DaoException e)
			{
                throw new FormDaoException("Error selecting from " + FormModule.FORM_PREFIX  + tableName + " table",e);
            }
        }

        return new ArrayList();
    }

	public void insertFormsConfig(FormConfigDataObject fcDO,String sqlDDL, String createTableDDL) throws DaoException
	{
        Transaction tx= null;
        String sql =  "UPDATE frw_form_config SET previewXml=#previewXml#,formXml=#formXml# WHERE formId=#formId#";
        int status = super.update(sql,fcDO);
        if (status != 0 && sqlDDL != null && !sqlDDL.equals(""))
		{
			if(sqlDDL.startsWith("ALTER TABLE "))
				executeAlterStatements(sqlDDL);
				//super.update(sqlDDL,null);
		}
        if (status == 0)
		{
			sql = "INSERT INTO frw_form_config(formConfigId,formId,previewXml,formXml) VALUES(#formConfigId#,#formId#,#previewXml#,#formXml#)";
			try
			{
				tx = getTransaction();
				tx.begin();
				tx.update(sql,fcDO);
				if (createTableDDL != null && !createTableDDL.equals(""))
					tx.update(createTableDDL,null);
				if (sqlDDL != null && !sqlDDL.equals(""))
				    //TODO: No choice but to removing the transaction control here. Consider another approach
					if(sqlDDL.startsWith("ALTER TABLE "))
						executeAlterStatements(sqlDDL);
					//tx.update(sqlDDL,null);
				tx.commit();
			}
			catch(Exception e)
			{
				if (tx != null)
					tx.rollback();
				Log.getLog(getClass()).error(e);
				throw new DaoException(e.toString());
			}
        }
    }

	/**
	 * Default form wizard dao behaviour is to generate all alter statements into a single statment. Unfortunately
	 * such sql blocks does not work in Oracle and PL SQL. This method is written to decode such alter statements
	 * and to break them down to iterative single alter blocks
	 * @param statement Original Alter statement
	 */
	protected void executeAlterStatements(String statement) throws DaoException
	{
        statement = statement.substring(12);
		if(statement.indexOf(" ") != -1)
		{
            // test
            String updateField="",
                    updateFieldType="",
                    renameField="",
                    addNewField="",
                    copyData="",
                    deleteOldField="";
            String tableName = statement.substring(0, statement.indexOf(" "));

            statement = statement.substring(statement.indexOf(" ") + 1);
			StringTokenizer tokenizer = new StringTokenizer(statement, ",");
			Transaction tx = getTransaction();
			try
			{
				while(tokenizer.hasMoreTokens())
				{
					StringBuffer sql = new StringBuffer("");
					String token = tokenizer.nextToken();
                    boolean isNewField=false;

                    int i = token.indexOf("CHANGE");
                    if (i>=0) {
                        i+=7;
                        String fieldOld = token.substring(i,token.indexOf(" ",i));
                        i = token.indexOf(fieldOld,fieldOld.length()+1);
                        String fieldNew = token.substring(i,token.indexOf(" ",i));
                        i = token.indexOf(" ",i+1);
                        String newType =  token.substring(i,token.length());


                            renameField = "ALTER TABLE "+tableName+" RENAME COLUMN \""+fieldOld.trim()+"\" TO FORMTEST";
                            addNewField = "ALTER TABLE "+tableName+" ADD \""+fieldNew+"\" "+newType;
                            copyData = "UPDATE "+tableName+" SET \""+fieldNew+"\" = FORMTEST";
                            deleteOldField = "ALTER TABLE "+tableName+" DROP COLUMN FORMTEST";
                            isNewField=true;
                    }
                    else {
                        i = token.indexOf("ADD");
                        updateFieldType = "ALTER TABLE "+tableName+" "+token;
                    }
                    if (isNewField) {
                        tx.update(renameField,null);
                        tx.update(addNewField,null);
                        tx.update(copyData,null);
                        tx.update(deleteOldField,null);
                    }
                    else {
                        tx.update(updateFieldType,null);
                    }
                }
				tx.commit();
			}
			catch(Exception e)
			{
				if (tx != null)
					tx.rollback();
				Log.getLog(getClass()).error(e);
				throw new DaoException(e.toString());
			}
		}
	}
}
