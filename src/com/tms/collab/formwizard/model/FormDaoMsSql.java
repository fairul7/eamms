package com.tms.collab.formwizard.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class FormDaoMsSql extends FormDao {
    
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
	
	public void runDDL(String sql) throws DaoException{
		boolean deleteFlag = true;
		//case in delete text box, sql like "ALTER TABLE frw_form_name_xxxxxxxx" pass to this func which throw error 
		if(sql.startsWith("ALTER TABLE") && sql.length() <= 47)
			deleteFlag = false;
			
        if (sql != null && sql.trim().length() > 0 && deleteFlag)
            super.update(sql,null);
    }
	
	public Collection selectForms(String userID,String action,String sort,boolean desc,int start, int rows)  throws DaoException{
        Collection forms= new ArrayList();

        String sql ="SELECT DISTINCT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated,su.firstName AS formUpdatedByName"+
                    " FROM frw_form f INNER JOIN security_user su ON (su.id = f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_user_access fua ON f.formId = fua.formId "+
                    " LEFT OUTER JOIN frw_form_group_access fga ON f.formId = fga.formId "+
                    " LEFT OUTER JOIN security_user_group sug ON sug.groupId = fga.groupId " +
                    " LEFT OUTER JOIN security_group sg ON sg.id = sug.groupId " +
                    " WHERE 1=1 AND ((f.formPublicFlag='0') OR (f.formPublicFlag='1' AND fua.userId='"+userID+"') OR "+
                    " (f.formPublicFlag='2' AND sug.userId='"+userID+"'  AND sg.active='1'))";
        if(action.equals("view")){
            sql=sql+ " AND f.isActive='1' AND f.isPending='0'";
        }
        if(sort.equals("formDisplayName"))
            sql=sql+" ORDER BY f.formDisplayName";
        else if(sort.equals("formDateCreated"))
            sql=sql+" ORDER BY f.formDateCreated";
        else if(sort.equals("formUpdatedByName"))
            sql = sql+" ORDER BY su.firstName";
        if(desc)
              sql = sql+" DESC";
        forms = super.select(sql,FormDataObject.class,null,start,rows);
        return forms;
    }

    public int selectForms(String userID,String action) throws DaoException{
        String sql ="SELECT count(*) AS total "+
                    " FROM frw_form f INNER JOIN security_user su ON (su.id =f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_user_access fua ON f.formId = fua.formId "+
                    " LEFT OUTER JOIN frw_form_group_access fga ON f.formId = fga.formId "+
                    " LEFT OUTER JOIN security_user_group sug ON sug.groupId = fga.groupId " +
                    " LEFT OUTER JOIN security_group sg ON sg.id = sug.groupId " +
                    " WHERE 1=1 AND ((f.formPublicFlag='0') OR (f.formPublicFlag='1' AND fua.userId='"+userID+"') OR "+
                    " (f.formPublicFlag='2' AND sug.userId='"+userID+"'  AND sg.active='1'))";
       if(action.equals("view")){
            sql=sql+ " AND f.isActive='1' AND f.isPending='0'";
       }

        Map row =  (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    public InputStream selectFormXML(String formID) throws FormDaoException{
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        InputStream xmlReader=null;
        String sql ="SELECT formXml AS formXML FROM frw_form_config WHERE formId='"+formID+"'";
        try{
            conn=  getDataSource().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next())
                xmlReader = new ByteArrayInputStream(rs.getBytes(1));
        } catch (Exception e) {
            throw new FormDaoException(e.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                }
            }
        }
        return xmlReader;
    }

    public InputStream selectFormPreviewXML(String formID) throws FormDaoException{
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs=null;
      InputStream xmlReader=null;
      String sql ="SELECT previewXml AS formXML FROM frw_form_config WHERE formId='"+formID+"'";
      try{
          conn=  getDataSource().getConnection();
          stmt = conn.createStatement();
          rs = stmt.executeQuery(sql);
          if (rs.next())
              xmlReader = new ByteArrayInputStream(rs.getBytes("formXML"));
          
      } catch (Exception e) {
          throw new FormDaoException(e.toString());
      } finally {
          if (conn != null) {
              try {
                  conn.close();
              } catch (SQLException e1) {
              }
          }
      }
      return xmlReader;
  }

    public Collection selectQueryForms(String userID,String sort,boolean desc,int start, int rows) throws DaoException{
        Collection forms = new ArrayList();
        String sql ="SELECT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated,su.firstName AS formsUpdatedByName"+
                    " FROM frw_form f INNER JOIN security_user su ON (su.id =f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_query fq ON fq.formId=f.formId "+
                    " WHERE 1=1 AND fq.userId='"+userID+"'";
        if(sort.equals("formDisplayName"))
             sql=sql+" ORDER BY f.formDisplayName";
        else if(sort.equals("formDateCreated"))
             sql=sql+" ORDER BY f.formDateCreated";
        else if(sort.equals("formsUpdatedByName"))
             sql=sql+" ORDER BY su.firstName";
        if(desc)
             sql = sql+" DESC";
        forms = super.select(sql,FormDataObject.class,null,start,rows);
        return forms;
    }

    public int selectQueryForms(String userID) throws DaoException{
        String sql ="SELECT count(*) AS total"+
                    " FROM  frw_form f INNER JOIN security_user su ON (su.id =f.formUpdatedBy)"+
                    " LEFT OUTER JOIN frw_form_query fq ON fq.formId=f.formId "+
                    " WHERE 1=1  AND fq.userId='"+userID+"'";
        Map row =  (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    public void insertFormsConfig(FormConfigDataObject fcDO,String sqlDDL, String createTableDDL) throws DaoException{
        Transaction tx= null;
        String sql =  "UPDATE frw_form_config SET previewXml=#previewXml#,formXml=#formXml# WHERE formId=#formId#";
        int status = super.update(sql,fcDO);
        if (status != 0 && sqlDDL != null && !sqlDDL.equals("") && sqlDDL.length() > 47)
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
    
	public String generateFormXML(FormElement form,List formChildren) {
        StringBuffer columnxBuf = null;


        columnxBuf = new StringBuffer();
        for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();) {

            Element child = (Element) fromChildrenIter.next();
            if (!(child instanceof XmlWidgetAttributes)) {
                continue;
            }
            XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
            //columnxBuf.append(" ADD ").append(child.getAttributeValue("name")).append(" ");
            columnxBuf.append(child.getAttributeValue("name")).append(" ");

            if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
                || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
                columnxBuf.append("nvarchar(").append(xmlWidget.getSize()).append(")").append(",");
            }
            else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
                columnxBuf.append("int").append(",");
            }
            else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
                columnxBuf.append("datetime").append(",");
            }
            else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
                columnxBuf.append("ntext").append(",");
            }
            else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
                columnxBuf.append("float").append(",");
            }

        }

        if (columnxBuf.toString().endsWith(","))
            columnxBuf.deleteCharAt(columnxBuf.length() - 1);

        String createDDL = "ALTER TABLE " + FormModule.FORM_PREFIX + form.getAttributeValue("name") + " ADD "
        	+ columnxBuf.toString();
        
        return createDDL;
    }

	public String changeSql(String sql, String tableName) {
        if (sql.length() > 0)
        sql = alterTableSql(" ALTER COLUMN " + sql, tableName);
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
                if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
                    || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
                    columnxBuf.append("nvarchar(").append(xmlWidget.getSize()).append(")");
                }
                else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
                    columnxBuf.append("int");
                }
                else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
                    columnxBuf.append("datetime");
                }
                else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
                    columnxBuf.append("ntext");
                }
                else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
                    columnxBuf.append("float")  ;
                }
            }
            xmlWidget.removeMetaData();

        }

        return changeSql(columnxBuf.toString(),FormModule.FORM_PREFIX  + form.getAttributeValue("name"));
    }
}
