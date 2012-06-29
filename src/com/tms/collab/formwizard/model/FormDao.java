package com.tms.collab.formwizard.model;

import kacang.Application;
import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DaoQuery;
import kacang.util.Transaction;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.services.security.User;
import kacang.services.security.Group;

import java.util.*;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;


import org.apache.commons.collections.SequencedHashMap;
import org.jdom.Element;
import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.xmlwidget.XmlWidgetAttributes;
import com.tms.collab.formwizard.xmlwidget.TextBlockElement;
import com.tms.collab.formwizard.engine.StructureEngine;
import com.tms.collab.formwizard.engine.FormLayout;
import com.tms.collab.formwizard.engine.TextFieldField;


public class FormDao extends DataSourceDao
{
    public void init() throws DaoException {
        try {
            super.update("ALTER TABLE frw_form ADD saveDb CHAR(1) default '1'", null);
        }
        catch(Exception e) {
        }

        try {
            super.update("UPDATE frw_form_workflow SET status = '" + FormModule.WORKFLOW_PENDING+ "' WHERE status = '1'",null);
        }
        catch(Exception e) {
        }

        super.update("CREATE TABLE frw_form_template ("
            + "formTemplateId varchar(255) NOT NULL default '',"
            + "templateName varchar(255) NOT NULL default '',"
            + "previewXml mediumtext,"
            + "formXml mediumtext,"
            + "PRIMARY KEY (formTemplateId))", null);

        super.update("CREATE TABLE frw_form_template_field ("
            + "formTemplateId varchar(255) NOT NULL default '',"
            + "formId varchar(255) NOT NULL default '',"
            + "formName varchar(255) NOT NULL default '',"
            + "templateNodeName varchar(255) NOT NULL default '',"
            + "KEY formTemplateId (formTemplateId), "
            + "KEY formId (formId), "
            + "KEY templateNodeName (templateNodeName))", null);

       super.update("CREATE TABLE frw_form_draft ("
            + "userId varchar(255) NOT NULL default '',"
            + "formName varchar(255) NOT NULL default '',"
            + "formUid varchar(255) NOT NULL default '',"
            + "datePosted datetime ,"
            + "formId varchar(255) NOT NULL default '',"
            + "KEY userID (userId),"
            + "KEY formId (formId), "
            + "KEY formUid (formUid))",null);

		super.update("CREATE TABLE frw_form ("
			+ "formId varchar(255) NOT NULL default '0',"
		  	+ "formDateCreated datetime NOT NULL default '0000-00-00 00:00:00',"
		  	+ "formDateUpdated varchar(11) default '0000-00-00',"
		  	+ "formEmails varchar(255) NOT NULL default '',"
			+ "formHeader mediumtext NOT NULL,"
		 	+ "formInternalFlag char(1) NOT NULL default '',"
		  	+ "formPublicFlag char(1) NOT NULL default '',"
		  	+ "formRetMsg mediumtext NOT NULL,"
		  	+ "formName varchar(255) NOT NULL default '',"
		  	+ "formUpdatedBy varchar(250) NOT NULL default '',"
		  	+ "isPending char(1) NOT NULL default '',"
		  	+ "isActive char(1) NOT NULL default '',"
		  	+ "formApprMethod varchar(255) NOT NULL default '',"
		  	+ "formDisplayName varchar(255) NOT NULL default '',"
		  	+ "PRIMARY KEY  (formId))" , null);
		  	
		 super.update("CREATE TABLE frw_form_approval ("
		 	+ "formApprovalId varchar(255) NOT NULL default '0',"
			+ "formId varchar(255) NOT NULL default '0',"
			+ "userId varchar(250) NOT NULL default '',"
			+ "priority int(11) NOT NULL default '0',"
			+ "PRIMARY KEY  (formApprovalId),"
			+ "KEY userId (userId),"
			+ "KEY formId (formId))" , null);

		super.update("CREATE TABLE frw_form_config ("
			+ "formConfigId varchar(255) NOT NULL default '',"
			+ "formId varchar(255) NOT NULL default '',"
			+ "previewXml mediumtext NOT NULL,"
			+ "formXml mediumtext NOT NULL,"
			+ "PRIMARY KEY  (formConfigId))",null);

		super.update("CREATE TABLE frw_form_group_access ("
			+ "formGroupAccessId varchar(255) NOT NULL default '0',"
			+ "formId varchar(255) NOT NULL default '0',"
			+ "groupId varchar(250) NOT NULL default '0',"
			+ "PRIMARY KEY  (formGroupAccessId))", null);
	  
	  	super.update("CREATE TABLE frw_form_memo_submission ("
  			+ "formMemoId varchar(250) NOT NULL default '0',"
  			+ "formId varchar(255) NOT NULL default '0',"
  			+ "userId varchar(250) NOT NULL default '',"
  			+ "PRIMARY KEY  (formMemoId))", null);

		super.update("CREATE TABLE frw_form_properties ("
		  	+ "formId varchar(255) NOT NULL default '0',"
  			+ "property varchar(255) NOT NULL default '',"
  			+ "value varchar(255) default NULL,"
  			+ "KEY formId (formId),"
  			+ "KEY property (property))", null);
  			
  		super.update("CREATE TABLE frw_form_query ("
  			+ "formQueryId varchar(255) NOT NULL default '0',"
	  		+ "formId varchar(250) NOT NULL default '',"
  			+ "userId varchar(250) NOT NULL default '',"
  			+ "PRIMARY KEY  (formQueryId),"
  			+ "KEY userId (userId),"
  			+ "KEY formId (formId))", null);
  			
  		super.update("CREATE TABLE frw_form_user_access ("
  			+ "formAccessId varchar(255) NOT NULL default '',"
  			+ "formId varchar(255) NOT NULL default '',"
  			+ "userId varchar(250) NOT NULL default '')", null);

		super.update("CREATE TABLE frw_form_workflow ("
			+ "formUid varchar(255) NOT NULL default '',"
			+ "formId varchar(255) NOT NULL default '',"
			+ "userId varchar(255) NOT NULL default '',"
			+ "formApprovalId varchar(255) default NULL,"
			+ "formApprovalDate datetime default NULL,"
			+ "status varchar(20) default NULL,"
            + "formSubmissionDate datetime NOT NULL default '0000-00-00 00:00:00', "
			+ "PRIMARY KEY  (formUid))",null);
    }

    public Collection getFormDraft(String formUid) throws FormDaoException {
        String sql;
        sql = "SELECT userId, formUid, datePosted AS formSubmissionDate, formId, formName"
              + " FROM frw_form_draft"
              + " WHERE formUid='" + formUid + "'";
        try {
            return super.select(sql,FormWorkFlowDataObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying data from frw_form_draft table - formUid:" + formUid,e);
        }
    }

    public void insertForm(FormDataObject formsDO) throws DaoException{
        Transaction tx = null;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String sql_forms ="INSERT INTO frw_form"+
                    " (formId,formName,formHeader,formDateCreated,formUpdatedBy,"+
                    "formRetMsg,formEmails,formInternalFlag,formPublicFlag," +
                    "isPending,isActive,formDateUpdated,formApprMethod, formDisplayName, saveDb)"+
                    " VALUES(#formId#,#formName#,#formHeader#,#formDateCreated#,#formUpdatedBy#,#formRetMsg#,#formEmails#,"+
                     "#formInternalFlag#,#formPublicFlag#,#isPending#,#isActive#,#formDateUpdated#,#formApprMethod#,#formDisplayName#,#saveDb#)";
        try{
            tx = getTransaction();
            tx.begin();
            tx.update(sql_forms,formsDO);
            if(formsDO.getAccessUserIDs()!=null)
                for(Iterator i=formsDO.getAccessUserIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_user_access ="INSERT INTO frw_form_user_access(formAccessId,formId,userId) VALUES('"+
                    year+"_"+UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_user_access,null);
                }
            if(formsDO.getAccessGroupIDs()!=null)
                for(Iterator i=formsDO.getAccessGroupIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_group_access ="INSERT INTO frw_form_group_access(formGroupAccessId,formId,groupId) VALUES('"+
                    year+"_"+UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_group_access,null);
                }
            if(formsDO.getQueryAccessUserIDs()!=null)
                for(Iterator i=formsDO.getQueryAccessUserIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_query ="INSERT INTO frw_form_query(formQueryId,formId,userId) VALUES('"+
                    year+"_"+UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_query,null);
                }
            if(formsDO.getMemoUserIDs()!=null)
                for(Iterator i=formsDO.getMemoUserIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_memo ="INSERT INTO frw_form_memo_submission(formMemoId,formId,userId) VALUES('"+
                    year+"_"+UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_memo,null);
                }
            
            
            String sql ="INSERT INTO frw_form_properties(formId,property,value) VALUES('"+
                        formsDO.getFormId()+"','formLink','"+formsDO.getFormLink()+"')";
            tx.update(sql,null);
            
            if(formsDO.getApproverIDs()!=null){
                int count=1;
                for(Iterator i=formsDO.getApproverIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_approval ="INSERT INTO frw_form_approval(formApprovalId,formId,userId,priority) VALUES('"+
                    year+"_"+UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"',"+count+")";
                    tx.update(sql_approval,null);
                    count++;
                }
            }
            tx.commit();
        } catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
                throw new DaoException(e.toString());
        }
    }

    public Collection selectAllForms(String sort, boolean desc) throws FormDaoException {
        String sql;
        sql = "SELECT formId, formDisplayName" +
              " FROM frw_form ";
        if (sort != null) {
            if ("formDisplayName".equals(sort))
                sql += " ORDER BY formDisplayName";
        }
        if (desc)
            sql += " DESC";
        try {
            return super.select(sql,FormDataObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException ("Error selecting frw_form table",e);
        }
    }

    public void updateForm(FormDataObject formsDO, boolean approvalCycleChanged) throws DaoException {
        Transaction tx = null;
        try{
             tx = getTransaction();
             tx.begin();
             String sql ="UPDATE frw_form SET formHeader=#formHeader#,formDateCreated=#formDateCreated#,"
                         + "formUpdatedBy=#formUpdatedBy#,formRetMsg=#formRetMsg#,formEmails=#formEmails#,formInternalFlag=#formInternalFlag#,"
                         + "formPublicFlag=#formPublicFlag#,isActive=#isActive#,formDateUpdated=#formDateUpdated#,formApprMethod=#formApprMethod#, "
                         + " saveDb=#saveDb#"
                         + " WHERE formId=#formId#";
             tx.update(sql,formsDO);
             if(formsDO.getAccessUserIDs()!=null){
                 tx.update("DELETE FROM frw_form_user_access WHERE formId='"+formsDO.getFormId()+"'",null);
                 for(Iterator i=formsDO.getAccessUserIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_user_access ="INSERT INTO frw_form_user_access(formAccessId,formId,userId) VALUES('"+
                    UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_user_access,null);
                 }
             }
             if(formsDO.getAccessGroupIDs()!=null){
                 tx.update("DELETE FROM frw_form_group_access WHERE formId='"+formsDO.getFormId()+"'",null);
                 for(Iterator i=formsDO.getAccessGroupIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_group_access ="INSERT INTO frw_form_group_access(formGroupAccessId,formId,groupId) VALUES('"+
                    UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_group_access,null);
                 }
             }
            if(formsDO.getQueryAccessUserIDs()!=null) {
                tx.update("DELETE FROM frw_form_query WHERE formId='"+formsDO.getFormId()+"'",null);
                for(Iterator i=formsDO.getQueryAccessUserIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_query ="INSERT INTO frw_form_query(formQueryId,formId,userId) VALUES('"+
                    UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_query,null);
                }
            }

            tx.update("DELETE FROM frw_form_memo_submission WHERE formId='"+formsDO.getFormId()+"'",null);
            if(formsDO.getMemoUserIDs()!=null) {
                for(Iterator i=formsDO.getMemoUserIDs().keySet().iterator();i.hasNext();){
                    String id = (String) i.next();
                    String sql_memo ="INSERT INTO frw_form_memo_submission(formMemoId,formId,userId) VALUES('"+
                    UuidGenerator.getInstance().getUuid()+"','"+formsDO.getFormId()+"','"+id+"')";
                    tx.update(sql_memo,null);
                }
            }

            tx.update("DELETE FROM frw_form_properties WHERE formId='"+formsDO.getFormId()+"' AND property='formLink'",null);

            sql ="INSERT INTO frw_form_properties(formId,property,value) VALUES('"+
                formsDO.getFormId()+"','formLink','"+formsDO.getFormLink()+"')";
            tx.update(sql,null);


            int count = 1;
            String formApprovalId;



            if(formsDO.getApproverIDs()!= null && approvalCycleChanged ){
                tx.update("DELETE FROM frw_form_approval WHERE formId='"+formsDO.getFormId()+"'",null);
                for(Iterator i=formsDO.getApproverIDs().keySet().iterator();i.hasNext(); count ++){
                    String id = (String) i.next();
                    formApprovalId =  UuidGenerator.getInstance().getUuid();

                    String sql_approval = "INSERT INTO frw_form_approval(formApprovalId,formId,userId,priority) VALUES('"+
                                              formApprovalId +"','"+formsDO.getFormId()+"','"+id+"',"+count+")";
                    tx.update(sql_approval,null);

                    //reset the workflow
                    if (count == 1)
                        tx.update("UPDATE frw_form_workflow SET formApprovalId = '" + formApprovalId + "', "
                                + " status = '"  + FormModule.WORKFLOW_PENDING  + "'"
                                + " WHERE formId = '" + formsDO.getFormId() + "'", null);

                }
            }
            else if (formsDO.getApproverIDs() == null) {
                tx.update("DELETE FROM frw_form_workflow WHERE formId = '" + formsDO.getFormId() + "'", null);
                tx.update("DELETE FROM frw_form_approval WHERE formId='"+formsDO.getFormId()+"'",null);
            }

            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
                throw new DaoException("",e);
        }
    }

    public void updateForm(FormDataObject formsDO)throws DaoException{
        updateForm(formsDO,false);
    }

    protected String getApprovalid(String id,String formID) throws DaoException {
        String sql = "SELECT formApprovalId"
        			 + " FROM frw_form_approval"
        			 + " WHERE formId='"+formID+"' AND userId='"+id+"'";
        String approvalid="";
        Collection count= super.select(sql,HashMap.class,null,0,-1);
        if(count!=null && count.iterator().hasNext()){
            Map row = (Map)count.iterator().next();
            approvalid= row.get("formApprovalId").toString();
        }
        return approvalid;
    }

    public Collection selectForm(String formId) throws FormDaoException {
        String sql ="SELECT formId,formDisplayName,formHeader,formName,"
        			 + " formDateCreated,formUpdatedBy ,"
                     + " formRetMsg,formEmails,"
                     + " formInternalFlag ,"
                     + " formPublicFlag ,isPending,isActive,"
                     + " formDateUpdated,formApprMethod,saveDb"
                     + " FROM frw_form WHERE formId = '"+formId+"'";
        try {
            return super.select(sql,FormDataObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting data from frw_form_table - formId:"+ formId,e);
        }
    }

    public Map selectFormGroup(String formID) throws DaoException{
        Map map = new SequencedHashMap();
        String sql ="SELECT groupId AS id,groupName AS name FROM frw_form_group_access,security_group WHERE formId='"+formID+"'"+
                    " AND groupId=id ";
        Collection groups = super.select(sql,Group.class,null,0,-1);
        for(Iterator i=groups.iterator();i.hasNext();){
            Group group=(Group)i.next();
            map.put(group.getId(),group.getName());
        }
        return map;
    }

    public Map selectFormUsers(String formID,String action) throws DaoException{
        Map map = new SequencedHashMap();
        String sql ="SELECT f.userId AS id ,su.firstName AS firstName,su.lastName AS lastName FROM ";
        if(action.equals("formAccess"))
            sql= sql+" frw_form_user_access f" ;
        else if(action.equals("query"))
            sql = sql+"frw_form_query f";
        else if(action.equals("memo"))
            sql = sql+" frw_form_memo_submission f";
        else if(action.equals("approval"))
            sql = sql+" frw_form_approval f";
        sql= sql +",security_user su WHERE f.formId='"+formID+"' AND f.userId=su.id";
        Collection users = super.select(sql,User.class,null,0,-1);
        for(Iterator i= users.iterator();i.hasNext();){
            User user =(User)i.next();
            map.put(user.getId(),user.getName());
        }
        return map;
    }

    public Object getFormProperty(String formId,String property) throws FormDaoException {
        String sql ="SELECT value AS prop FROM frw_form_properties " +
                    "WHERE formId = '" + formId + "' AND property = '"+property+"'";

        Map row;
        try {
            row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
            return row.get("prop");
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting form frw_form_properties table - formId:" + formId
                                       + " , property:"+property,e);
        }

    }

    public boolean isWorkFlowForm(String formID) throws DaoException{
        String sql ="SELECT count(*) AS total FROM frw_form_approval WHERE formId='"+formID+"'";
        Map row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
        int total=Integer.parseInt(row.get("total").toString());
        if(total>0)
            return true;
        else
            return false;
    }



    public InputStream selectFormXML(String formId) throws FormDaoException {
        Collection collection = null;
        InputStream xmlInput=null;
        Map map = null;
        String sql ="SELECT formXml FROM frw_form_config WHERE formId='"+formId+"'";
        String xml = null;
        try {
            collection = super.select(sql,HashMap.class,null,0,-1);
            if (collection.iterator().hasNext()) {
                map = (Map) collection.iterator().next();
                if (map.get("formXml") != null) {
                    xml = map.get("formXml").toString();
                    xmlInput = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                }
            }
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting formXml FROM frw_form_config table - formId:"+formId,e);
        }
        catch (UnsupportedEncodingException e) {
            Log.getLog(getClass()).error("Unsupported Encoding",e);
        }
        return xmlInput;
    }
    
	
      public InputStream selectFormPreviewXML(String formId) throws FormDaoException{
        InputStream xmlInput=null;
        String sql ="SELECT previewXml FROM frw_form_config WHERE formId='"+formId+"'";
        Collection collection = null;
        Map map = null;
        String xml = null;
        try {
            collection = super.select(sql,HashMap.class,null,0,-1);
            if (collection.iterator().hasNext()) {
                map = (Map) collection.iterator().next();
                if (map.get("previewXml") != null) {
                    xml = map.get("previewXml").toString();
                    xmlInput = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                }
            }
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting formXml FROM frw_form_config table - formId:"+formId,e);
        }
        catch (UnsupportedEncodingException e) {
            Log.getLog(getClass()).error("Unsupported Encoding",e);
        }
        return xmlInput;
    }

    public Collection selectViewForms(DaoQuery query, String userId,String sort,
                                  boolean desc,int start, int rows) throws FormDaoException {

         Collection forms= new ArrayList();
         String sql ="SELECT DISTINCT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated, su.firstName AS formUpdatedByName"+
                     " FROM frw_form f " +
                     " LEFT OUTER JOIN security_user su ON su.id =f.formUpdatedBy"+
                     " LEFT OUTER JOIN frw_form_user_access fua ON f.formId = fua.formId "+
					 " LEFT OUTER JOIN frw_form_group_access fga ON f.formId = fga.formId "+
					 " LEFT OUTER JOIN security_user_group sug ON sug.groupId = fga.groupId " +
					 " LEFT OUTER JOIN security_group sg ON sg.id = sug.groupId " +
                     " WHERE 1=1 AND ((f.formPublicFlag='0') OR (f.formPublicFlag='1' AND fua.userId='"+userId+"') OR "+
                     " (f.formPublicFlag='2' AND sug.userId='"+userId+"'  AND sg.active='1'))" + query.getStatement();

         sql += " AND f.isActive='1' AND f.isPending='0'";

         if(sort == null || sort.equals("formDisplayName"))
             sql += " ORDER BY f.formDisplayName";
         else if(sort.equals("formDateCreated"))
             sql += " ORDER BY f.formDateCreated";
         else if(sort.equals("formUpdatedByName"))
             sql += " ORDER BY su.firstName";

         if(desc)
               sql += " DESC";
        try {
            forms = super.select(sql,FormDataObject.class,query.getArray(),start,rows);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting from frw_form table",e);
        }
        return forms;
    }

    public String FormsEditSQL(String userID, String formsID,String action){
         String sql ="SELECT count(*) AS total "+
                     " FROM frw_form f INNER JOIN security_user su ON su.id =f.formUpdatedBy"+
                     " LEFT OUTER JOIN frw_form_user_access fua ON f.formId = fua.formId "+
					 " LEFT OUTER JOIN frw_form_group_access fga ON f.formId = fga.formId "+
					 " LEFT OUTER JOIN security_user_group sug ON sug.groupId = fga.groupId " +
					 " LEFT OUTER JOIN security_group sg ON sg.id = sug.groupId " +
                     " WHERE 1=1 AND ((f.formPublicFlag='0') OR (f.formPublicFlag='1' AND fua.userId='"+userID+"') OR "+
                     " (f.formPublicFlag='2' AND sug.userId='"+userID+"'  AND sg.active='1'))";
        if(action.equals("view")){
             sql=sql+ " AND f.isActive='1' AND f.isPending='0'";
        }
        if(formsID!=null && action.equals("edit"))
            sql = sql+" AND f.formId='"+formsID+"'";
        return sql;
    }

    public int selectViewFormsCount(DaoQuery query , String userId) throws FormDaoException {



         String sql ="SELECT count(DISTINCT f.formId) AS total"+
                     " FROM frw_form f" +
                     " LEFT OUTER JOIN security_user su ON su.id =f.formUpdatedBy"+
                     " LEFT OUTER JOIN frw_form_user_access fua ON f.formId = fua.formId "+
					 " LEFT OUTER JOIN frw_form_group_access fga ON f.formId = fga.formId "+
					 " LEFT OUTER JOIN security_user_group sug ON sug.groupId = fga.groupId " +
					 " LEFT OUTER JOIN security_group sg ON sg.id = sug.groupId " +
                     " WHERE 1=1 AND ((f.formPublicFlag='0') OR (f.formPublicFlag='1' AND fua.userId='"+userId+"') OR "+
                     " (f.formPublicFlag='2' AND sug.userId='"+userId+"'  AND sg.active='1'))" + query.getStatement();

        sql+= " AND f.isActive='1' AND f.isPending='0'";
        Map row =  null;
        try {
            row = (Map) super.select(sql,HashMap.class,query.getArray(),0,-1).iterator().next();
            return Integer.parseInt(row.get("total").toString());
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting count from frw_form table",e);
        }

    }

    public Collection selectApproveForms(String sort,boolean desc, int start, int rows) throws DaoException{
        Collection forms = new ArrayList();
        String sql ="SELECT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated,su.firstName AS formsUpdatedByName"+
                     " FROM frw_form f,security_user su"+
                     " WHERE su.id =f.formUpdatedBy AND f.isPending='1'";
        if(sort.equals("formDisplayName"))
             sql=sql+" ORDER BY f.formDisplayName";
         else if(sort.equals("formsDateCreated"))
             sql=sql+" ORDER BY f.formDateCreated";
         else if(sort.equals("formsUpdatedByName"))
             sql = sql+" ORDER BY su.firstName";
         if(desc)
               sql = sql+" DESC";
         forms = super.select(sql,FormDataObject.class,null,start,rows);
         return forms;
    }

    public int selectApproveForms()throws DaoException{
        String sql ="SELECT count(*) AS total"+
                     " FROM frw_form f,security_user su"+
                     " WHERE su.id =f.formUpdatedBy AND f.isPending='1'";;
        Map row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }


    public Collection selectEditForms(DaoQuery query, String sort,boolean desc,int start, int rows) throws FormDaoException {
        Collection forms = new ArrayList();
        String sql =   "SELECT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated,"
                    +  "su.firstName AS formUpdatedByName"
        			+  " FROM frw_form f"
                    +  " LEFT OUTER JOIN security_user su ON su.id =f.formUpdatedBy"
                    +  " WHERE f.isPending = '0'" + query.getStatement();


        if(sort == null || sort.equals("formDisplayName"))
             sql += " ORDER BY f.formDisplayName";
         else if(sort.equals("formDateCreated"))
             sql += " ORDER BY f.formDateCreated";
         else if(sort.equals("formUpdatedByName"))
             sql += " ORDER BY su.firstName";
         if(desc)
               sql += " DESC";
        try {
            forms = super.select(sql,FormDataObject.class,query.getArray(),start,rows);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting from frw_form table",e);
        }
        return forms;
    }

    public int selectEditFormsCount(DaoQuery query) throws FormDaoException {
        String sql =   "SELECT count(*) AS total"
                    +  " FROM frw_form "
                    +  " WHERE isPending = '0'" + query.getStatement();
        Map row = null;
        try {
            row = (Map) super.select(sql,HashMap.class,query.getArray(),0,-1).iterator().next();
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting count from frw_form table",e);
        }
        return Integer.parseInt(row.get("total").toString());
    }

    public Collection selectQueryForms(DaoQuery query, String userId,String sort,
                                       boolean desc,int start, int rows) throws FormDaoException {

        Collection forms = new ArrayList();
        String sql = "SELECT f.formId AS formId,f.formDisplayName AS formDisplayName,f.formDateCreated AS formDateCreated," +
                     " su.firstName AS formsUpdatedByName"+
                     " FROM frw_form f " +
                     " LEFT OUTER JOIN security_user su ON su.id =f.formUpdatedBy"+
                     " LEFT OUTER JOIN frw_form_query fq ON fq.formId=f.formId "+
                     " WHERE 1=1 AND fq.userId='"+userId+"'" + query.getStatement();

        if(sort == null || sort.equals("formDisplayName"))
             sql += " ORDER BY f.formDisplayName";
        else if(sort.equals("formDateCreated"))
             sql += " ORDER BY f.formDateCreated";
        else if(sort.equals("formsUpdatedByName"))
             sql += " ORDER BY su.firstName";
        if(desc)
             sql += " DESC";
        try {
            forms = super.select(sql,FormDataObject.class,query.getArray(),start,rows);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting from frw_form table",e);
        }
        return forms;
    }


    public int selectQueryFormsCount(DaoQuery query, String userId) throws FormDaoException {
        String sql ="SELECT count(*) AS total"+
                    " FROM frw_form f " +               
                    " LEFT OUTER JOIN frw_form_query fq ON fq.formId=f.formId "+
                    " WHERE 1=1 AND fq.userId='"+userId+"'" + query.getStatement();

        Map row =  null;
        try {
            row = (Map) super.select(sql,HashMap.class,query.getArray(),0,-1).iterator().next();
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting count from frw_form table",e);
        }
        return Integer.parseInt(row.get("total").toString());
    }

    public void runDDL(String sql) throws DaoException{
        if (sql != null && sql.trim().length() > 0)
            super.update(sql,null);
    }

    public void updateFormConfig(String formId,String previewXml,String formXml) throws FormException {
        FormConfigDataObject fcdo = new FormConfigDataObject();
        fcdo.setPreviewXml(previewXml);
        fcdo.setFormXml(formXml);
        fcdo.setFormId(formId);
        updateFormConfig(fcdo);        
    }

    public void updateFormConfig(FormConfigDataObject fcdo) throws FormException {
        String sql = null;

        sql = "UPDATE frw_form_config"
              + " SET previewXml=#previewXml#,formXml=#formXml# WHERE formId=#formId#";

        try {
            super.update(sql,fcdo);
        }
        catch (DaoException e) {
            throw new FormException("Error updating frw_form_config table - formId:" + fcdo.getFormId(),e);
        }
    }



    public void insertFormsConfig(FormConfigDataObject fcDO,String sqlDDL, String createTableDDL) throws DaoException{
        Transaction tx= null;
        String sql =  "UPDATE frw_form_config SET previewXml=#previewXml#,formXml=#formXml# WHERE formId=#formId#";
        int status = super.update(sql,fcDO);
        if (status != 0 && sqlDDL != null && !sqlDDL.equals(""))
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

    public void insertFormsField(FormFieldDataObject ffDO) throws DaoException{

        String sql ="INSERT INTO formsfield (formsfieldid,fieldtype,fieldsize,datatype,hiddenflag,requiredflag,orderno,formId)"+
                    " VALUES(#formsFieldID#,#fieldType#,#fieldSize#,#dataType#,#hiddenFlag#,#requiredFlag#,#order#,#formId#)";
        super.update(sql,ffDO);
    }
    public void approveForms(String formsID) throws DaoException{
        String sql ="UPDATE frw_form SET isPending=0 , isActive = 1 WHERE formId='"+formsID+"'";
        super.update(sql,null);
    }

    public void deleteForms(String formsID) throws DaoException{
        Transaction tx= null;

        String formName="";

        String sql_select =" SELECT formName FROM frw_form WHERE formId='"+formsID+"'";
        Collection row = super.select(sql_select,HashMap.class,null,0,-1);
        if(row.iterator().hasNext()){
            Map  map = (Map)row.iterator().next();
            formName =map.get("formName").toString();
        }
        
        try{
            tx = getTransaction();
            tx.begin();
            
            String sql ="DELETE FROM frw_form_user_access WHERE formId='"+formsID+"'";
            tx.update(sql,null);
            
            sql="DELETE FROM frw_form_group_access WHERE formId='"+formsID+"'";
            tx.update(sql,null);
            
            sql = "DELETE FROM frw_form_memo_submission WHERE formId='"+formsID+"'";
            tx.update(sql,null);
            
            sql="DELETE FROM frw_form_query WHERE formId='"+formsID+"'";
            tx.update(sql,null);
            
            sql = "DELETE FROM frw_form_properties WHERE formId = '" + formsID +"'";
			tx.update(sql,null);

            sql = "DELETE FROM frw_form_template_field WHERE formId = '" + formsID +"'";
   			tx.update(sql,null);

            sql="DELETE FROM frw_form_draft WHERE formId='"+formsID+"'";
            tx.update(sql,null);

            sql="DELETE FROM frw_form WHERE formId='"+formsID+"'";
            int stat = tx.update(sql,null);



            if(stat ==1){
                sql= "DELETE FROM frw_form_approval WHERE formId='"+formsID+"'";
                tx.update(sql,null);
                
                sql ="DELETE FROM frw_form_workflow WHERE formId='"+formsID+"'";
                tx.update(sql,null);
                
                sql = "DELETE FROM frw_form_config WHERE formId='"+formsID+"'";
                tx.update(sql,null);
                
                
                try {
					sql ="DROP TABLE " +  FormModule.FORM_PREFIX  + formName;
					tx.update(sql,null);
                }
                catch (SQLException e) {
                }                                   
            }
            
            tx.commit();
        } catch(Exception e) {
            if (tx != null) 
                tx.rollback();            
            Log.getLog(getClass()).error(e);
            	throw new DaoException(e.toString());
        }
    }
    
    public void deleteFormData(String tableName, String id) throws FormDaoException {
        String sql;
        
        sql = "DELETE FROM " + tableName
              + " WHERE formUid = '" + id + "'";
        try {
            super.update(sql,null);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error deleting " + tableName + " table - formUid:"+id,e);
        }

    }

    protected String concat(List values) {
        if (values.isEmpty()) {
            return "";
        }
        StringBuffer valueBuffer = new StringBuffer();
        for (Iterator valuesIterator = values.iterator(); valuesIterator.hasNext();) {
              valueBuffer.append(valuesIterator.next().toString() + ",");
        }
        valueBuffer.setLength(valueBuffer.length() - 1);
        return valueBuffer.toString();
    }

    protected List normalizeListToString(List values) {
        int size = values.size();
        for (int i = 0; i < size; i++) {
            Object value = values.get(i);
            if (value instanceof List) {
                values.set(i, concat((List) value));
            }
        }
        return values;
    }

    public int getPriority(String ID,String action) throws DaoException{
        String sql ="SELECT priority FROM frw_form_approval WHERE ";
        if(action.equals("form"))
            sql = sql+"formId='"+ID+"'";
        else
            sql=sql+"formApprovalId='"+ID+"'";
        Map row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
        return Integer.parseInt(row.get("priority").toString());
    }

    public int getHighPriority(String formId) throws FormDaoException {
        String sql="SELECT max(priority) AS highPriority FROM frw_form_approval WHERE formId = '" +formId + "'";
        Map row = null;
        try {
            row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
            return Integer.parseInt(row.get("highPriority").toString());
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting from frw_form_approval table - formId:"+formId,e);
        }

    }

    public Collection selectFormWFDetail(String formUid) throws FormDaoException {
        String sql =" SELECT wf.formId ,wf.formApprovalId ,fa.priority , fa.userId AS approverId, " +
                    " s.username AS name,wf.userId" +
                    " FROM frw_form_workflow wf,frw_form_approval fa,security_user s"+
                    " WHERE wf.formApprovalId = fa.formApprovalId AND wf.formUid = '" + formUid + "' AND s.id = wf.userId";
        try {
            return super.select(sql,FormWorkFlowDataObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying frw_form_workflow table, frw_form_approval table, security_user table",
                                       e);
        }
    }
   
    public boolean isFormNameExist(String formName) throws DaoException{
        String sql ="SELECT count(*) AS total  FROM frw_form WHERE LOWER(formName) = '" + formName.toLowerCase() + "'";
        Map row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
        int flag = Integer.parseInt(row.get("total").toString());
        if(flag==1)
            return true;
        else
            return false;
    }


    public Collection selectFormMemoSubmission(String formId)throws FormDaoException{
        String sql = "SELECT userId from frw_form_memo_submission WHERE formId='" + formId + "'";

        try {
            return  super.select(sql,FormMemoSubmission.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying frw_form_memo_submission table - formId:"+ formId,e);
        }
    }
    


    public Collection getApproverByPriority(String formId,int priority) throws FormDaoException {

        String sql = "SELECT formApprovalId ,userId AS approverId  FROM frw_form_approval" +
                     " WHERE formId = '"+formId+"' AND priority = "+priority;

        try {
            return super.select(sql,FormWorkFlowDataObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying frw_form_approval table - formId:" + formId,e);
        }

    }

    public void saveFormWF(FormWorkFlowDataObject wfDO) throws DaoException{
        if(wfDO.getPriority()>1){
            String sql_update= "UPDATE frw_form_workflow SET formApprovalId=#formApprovalId#," +
                            "formApprovalDate=#formApprovalDate#,status=#status# WHERE formUid=#formUid#";
            super.update(sql_update,wfDO);
        } else {
            String sql_ins="INSERT INTO frw_form_workflow (formUid,priority,formApprovalId,formApprovalDate,status)"+
                        " VALUES(#formUid#,#priority#,#formApprovalId#,#formApprovalDate#,#status#)";
            super.update(sql_ins,wfDO);
        }
    }

    public String saveForm(FormWorkFlowDataObject wfDO) throws FormDaoException {

        String formPK;
        formPK = wfDO.getFormUid();
        StringBuffer columnBuffer = new StringBuffer();
        StringBuffer holderBuffer = new StringBuffer();
        Map valuesMap = wfDO.getValues();
        List valueList = new ArrayList();
        for (Iterator valuesIterator = valuesMap.keySet().iterator(); valuesIterator.hasNext();) {
            String fieldName = (String) valuesIterator.next();
            columnBuffer.append(fieldName + ",");
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



    public Collection selectFormsWorkFlow(DaoQuery query, String userId,String sort,
                                          boolean desc,int start, int rows) throws FormDaoException {

        String sql ="SELECT f.formName AS formName, f.formDisplayName AS formDisplayName,formSubmissionDate,"
                    + " wf.formUid AS formUid,"
                    + "su.username AS approverName ,"
                    + "wf.status AS status " 
                    + "FROM frw_form f, security_user su,frw_form_workflow wf,frw_form_approval fa "
                    + "WHERE wf.formApprovalId=fa.formApprovalId AND wf.formId = f.formId "
                    + "AND wf.userId='"+userId+"' AND fa.userId = su.id" + query.getStatement();

        if(sort == null || sort.equals("formDisplayName"))
            sql += " ORDER BY f.formDisplayName";
        else if(sort.equals("approverName"))
            sql += " ORDER BY su.username";
        else if(sort.equals("status"))
            sql += " ORDER BY wf.status";
        else if(sort.equals("formSubmissionDate"))
            sql += " ORDER BY formSubmissionDate";

        if(desc)
            sql += " DESC";
        try {
            return super.select(sql,FormWorkFlowDataObject.class,query.getArray(),start,rows);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting from frw_form_workflow table",e);
        }
    }

    public int selectFormsWorkFlow(DaoQuery query, String userId) throws FormDaoException {
        String sql ="SELECT count(*) AS total " +
                     "FROM frw_form f, security_user su,frw_form_workflow wf,frw_form_approval fa "+
                     "WHERE wf.formApprovalId=fa.formApprovalId AND wf.formId = f.formId " +
                     "AND wf.userId='"+userId+"' AND fa.userId = su.id" + query.getStatement();

        Map row =  null;
        try {
            row = (Map) super.select(sql,HashMap.class,query.getArray(),0,-1).iterator().next();
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting count from frw_form_workflow table",e);
        }
        return Integer.parseInt(row.get("total").toString());
    }
    
    public Collection selectFormsWorkFlow(String formUid) throws FormDaoException {
    	String sql;
    	
    	sql = "SELECT formId,status, userId FROM frw_form_workflow WHERE formUid = '" + formUid + "'";
        try {
            return super.select(sql,FormWorkFlowDataObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting from frw_form_workflow table - formUid:" + formUid,e);
        }
    }

    public Collection getPendingForms(DaoQuery query, String userId,String sort,boolean desc,int start, int rows)
            throws FormDaoException {
        String sql = "SELECT wf.formUid AS formUid,s.username AS name,"
                    + "f.formId AS formId,f.formName AS formName, "
                    + "f.formDisplayName AS formDisplayName,"
                    + "wf.formSubmissionDate AS formSubmissionDate "
                    + "FROM frw_form_workflow wf,frw_form_approval fa,frw_form f,security_user s "
                    + "WHERE  fa.userId='"+userId+"' AND fa.formApprovalId=wf.formApprovalId "
                    + "AND f.formId= fa.formId AND s.id = wf.userId AND wf.status ='" + FormModule.WORKFLOW_PENDING + "'"
                    + query.getStatement();

        if(sort==null)
            sort ="name";

        if(sort.equals("name"))
            sql += " ORDER BY s.username";
        else if(sort.equals("formId"))
            sql += " ORDER BY f.formId";
        else if(sort.equals("formDisplayName"))
            sql += " ORDER BY f.formDisplayName";
        else if (sort.equals("formSubmissionDate"))
            sql += " ORDER BY wf.formSubmissionDate";

        if(desc)
            sql += " DESC";
        try {
            return super.select(sql,FormWorkFlowDataObject.class,query.getArray(),start,rows);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting pending forms",e);
        }
    }

    public int getPendingForms(DaoQuery query, String userId) throws FormDaoException {
        String sql = "SELECT count(*) AS total " +
                "FROM frw_form_workflow wf,frw_form_approval fa,frw_form f,security_user s " +
                "WHERE  fa.userId='"+userId+"' AND fa.formApprovalId=wf.formApprovalId " +
                "AND f.formId= fa.formId AND s.id = wf.userId AND wf.status ='" + FormModule.WORKFLOW_PENDING+ "'" + query.getStatement();

        Map row =  null;
        try {
            row = (Map) super.select(sql,HashMap.class,query.getArray(),0,-1).iterator().next();
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting count for pending forms",e);
        }
        return Integer.parseInt(row.get("total").toString());
    }

    public Collection getNextPriorityApprover(String formId,int priority) throws FormDaoException {
        String sql ;
        
        sql = "SELECT formApprovalId, "
              + " userId, formId"
              + " FROM frw_form_approval"
              + " WHERE formId = '" + formId + "' AND priority = " + priority;
        try {
            return super.select(sql, FormWorkFlowDataObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting from frw_form_approval table - formId:"+ formId
                                       + " ,priority:" + priority, e);
        }


    }
    
    

    public void updateFormWorkFlow(FormWorkFlowDataObject wfDO) throws FormDaoException {
        String sql = "UPDATE frw_form_workflow SET formApprovalId=#formApprovalId#," +
                     "formApprovalDate=#formApprovalDate#,status=#status# WHERE formUid=#formUid#";
        try {
            super.update(sql,wfDO);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error updating frw_form_workflow table",e);
        }
    }




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
        if ( orderStr != null && !orderStr.equals(""))
            sql += orderStr;

        try {
        	
        	Collection result2 = new ArrayList();
            Collection result = super.select(sql,DefaultDataObject.class,query.getArray(), start,rows);
            
            
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
            	DefaultDataObject temp =  (DefaultDataObject) iterator.next();
            	DefaultDataObject temp2 = new DefaultDataObject();

                //Resetting the id
                temp2.setId(temp.getId());

            	for (Iterator itto = temp.getPropertyMap().entrySet().iterator();
                        itto.hasNext();) {
            		
            		  Map.Entry entry = (Map.Entry) itto.next();
                      String key = (String) entry.getKey();
                
                      if(entry.getValue() instanceof String){
                      String value = (String) entry.getValue();

                      String tempValue ="";
                      try {
						tempValue = new String( value.getBytes(), "UTF-8");
						
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
					
					if((tempValue !=null && (!"".equals(tempValue))) && ((tempValue.length() < 4) || (tempValue.substring(0,4).indexOf("????")>=0)))
				   	  {
						temp2.setProperty(key, value);
				   	  }
					else{
						value= tempValue;
						temp2.setProperty(key, value);
					}

                   }else{
                	   temp2.setProperty( key, entry.getValue());
                   }
            	}
            	  result2.add(temp2);
            }
            
            return result2;
            
            
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting data from " + tableName + " table",e);
        }
    }

    public Collection getDynamicRows(DaoQuery query, String tableName, String columnsStr,String userId,String orderStr,
                                     int start, int rows) throws FormDaoException {


        String sql ;

		sql = "SELECT username ," + columnsStr
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

        try {
        	Collection result2 = new ArrayList();
            Collection result = super.select(sql,DefaultDataObject.class,query.getArray(), start,rows);
            
            
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
            	DefaultDataObject temp =  (DefaultDataObject) iterator.next();
            	DefaultDataObject temp2 = new DefaultDataObject();
            	                   
            	for (Iterator itto = temp.getPropertyMap().entrySet().iterator();
                        itto.hasNext();) {
            		
            		  Map.Entry entry = (Map.Entry) itto.next();
                      String key = (String) entry.getKey();
                
                      if(entry.getValue() instanceof String){
                      String value = (String) entry.getValue();
            		  
                      
                      String tempValue ="";
                      try {
						tempValue = new String( value.getBytes(), "UTF-8");
						
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
					
					if(tempValue !=null && tempValue.substring(0,2).indexOf("??")>=0)
				   	  {
						temp2.setProperty(key, value);
						
				   	  
				   	  
				   	  }
					else{
						
						value= tempValue;
						temp2.setProperty(key, value);
						
					}
            	
                   }else{
                	   
                	   temp2.setProperty( key, entry.getValue()); 
                   }
                      
                      
            	
            	
            	
            	}
            	
            	  result2.add(temp2);
            	
            	
                
            }
            
            return result2;
            
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying " + tableName + " table",e);
        }
    }

    public int getDynamicRowsCount(DaoQuery query, String tableName) throws FormDaoException {
        StringBuffer sqlBuffer = new StringBuffer(
                 "select count(*) as total from " + tableName
              + " LEFT OUTER JOIN frw_form_workflow"
			  + " ON " + tableName + ".formUid = frw_form_workflow.formUid"
              + " LEFT OUTER JOIN frw_form_draft"
              + " ON " + tableName + ".formUid = frw_form_draft.formUid"
              + " WHERE frw_form_workflow.formUid is NULL "
              + " AND frw_form_draft.formUid is NULL" + query.getStatement());
        HashMap count = null;
        try {
            count = (HashMap) super.select(sqlBuffer.toString(),HashMap.class, query.getArray(), 0, -1).iterator().next();
        }
        catch (DaoException e) {
            throw new FormDaoException("Error selecting count from frw_form_workflow table",e);
        }

        return Integer.parseInt(String.valueOf(count.get("total")));
    }

    public int getDynamicRows(DaoQuery query, String tableName, String userId) throws FormDaoException {

        StringBuffer sqlBuffer = new StringBuffer(
                "select count(*) as total from " + tableName
              + " LEFT OUTER JOIN frw_form_workflow"
			  + " ON " + tableName + ".formUid = frw_form_workflow.formUid"
              + " LEFT OUTER JOIN frw_form_draft"
              + " ON " + tableName + ".formUid = frw_form_draft.formUid"
              + " WHERE frw_form_workflow.formUid is NULL "
              + " AND frw_form_workflow.formUid is NULL"
              + " AND frw_form_draft.formUid is NULL"
              + " AND " + tableName + ".userId = '" + userId + "'" + query.getStatement());
        HashMap count = null;
        try {
            count = (HashMap) super.select(sqlBuffer.toString(),HashMap.class, query.getArray(), 0, -1).iterator().next();
            return Integer.parseInt(String.valueOf(count.get("total")));
        }
        catch (DaoException e) {
            throw new FormDaoException("Error calculating total of table's row : tableName:" + tableName,e);
        }
    }

    public Collection selectPublicForms(String userID,String action,String sort,boolean desc,int start, int rows)  throws DaoException{
        Collection forms= new ArrayList();
        String sql;
            
        sql = "SELECT formId,formDisplayName,formDateCreated"+
              " FROM frw_form "+
              " WHERE formInternalFlag='0' AND isActive='1' AND isPending='0'";
            
        if(sort.equals("formDisplayName"))
            sql += " ORDER BY formDisplayName";
            
        if(desc)
            sql += " DESC";
        forms = super.select(sql,FormDataObject.class,null,start,rows);
        return forms;
   }

   

	public Collection getSubmittedFormData(String id, String tableName, String columnsStr) throws FormDaoException {
		String sql;

		if (columnsStr != null && !columnsStr.equals("")) {
            sql = "select " + columnsStr + " from " + FormModule.FORM_PREFIX  + tableName + " WHERE formUid = '" + id + "'";


            try {
                return super.select(sql,DefaultDataObject.class,null, 0,-1);
            }
            catch (DaoException e) {
                throw new FormDaoException("Error selecting from " + FormModule.FORM_PREFIX  + tableName + " table",e);
            }
        }

        return new ArrayList();
    }

    public Collection getSubmittedFormData(String tableName, String columnsStr) throws FormDaoException {
		String sql;

		sql = "select " + columnsStr + " from " + FormModule.FORM_PREFIX  + tableName ;


        try {
            return super.select(sql,DefaultDataObject.class,null, 0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying data from " + FormModule.FORM_PREFIX  + tableName + " table",e);
        }


    }



    public boolean hasPermission(String userID, String formUid) throws FormDaoException, DaoException  {
    	boolean hasPermission = false;
    	String sql, approvalID, formId = null;
		

        Collection data = selectFormsWorkFlow(formUid);
        if (data.iterator().hasNext()) {
            formId = ((FormWorkFlowDataObject) data.iterator().next()).getFormId();
        }


        approvalID = getApprovalid(userID, formId);


        sql = "SELECT formUid FROM frw_form_workflow "
              + " WHERE formUid = '" + formUid + "'"
              +	" AND formApprovalId = '" + approvalID + "'"
              + " AND status = '" + FormModule.WORKFLOW_PENDING + "'";

        Collection collection = super.select(sql,HashMap.class,null,0,-1);
        if (collection.size() > 0)
            hasPermission = true;

    	return hasPermission;
    	
    }
    

    public void deleteFormsWorkFlow(String formUid) throws FormDaoException {
		String sql;

		sql = "DELETE FROM frw_form_workflow WHERE formUid = '" + formUid + "'";
        try {
            super.update(sql,null);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error deleting from frw_form_workflow table : formUid - " + formUid, e);
        }
    }



    public void updateFormDetail(FormDataObject formsDO) throws DaoException {
        String sql ;

         sql = "UPDATE frw_form SET "
               + " isActive=#isActive#, isPending=#isPending#"
               + " WHERE formId=#formId#";
        super.update(sql,formsDO);
    }

    public Collection getFormApproval(String formId) throws DaoException {
        String sql;        
        sql = "SELECT userId , priority "
               + " FROM frw_form_approval "
               + " WHERE formId = '" + formId + "'";

        return super.select(sql,FormWorkFlowDataObject.class,null,0,-1);
    }

    public Collection getFormTemplate(String templateId) throws FormDaoException {
        String sql;

        sql = "SELECT formTemplateId, templateName, previewXml, formXml"
              + " FROM frw_form_template"
              + " WHERE formTemplateId = '"+ templateId + "'";
        try {
            return super.select(sql,FormTemplate.class,null,0,-1);
        }
        catch (DaoException e) {
           throw new FormDaoException("Error querying data from frw_form_template table" + templateId ,e);
        }


    }

    public Collection getFormTemplate(String sort,boolean desc,int start, int rows) throws FormDaoException {
         String sql;

        sql = "SELECT formTemplateId, templateName, previewXml, formXml"
              + " FROM frw_form_template";
        try {

            if (sort==null)
                sort ="templateName";

            if(sort.equals("templateName"))
                sql = sql+" ORDER BY templateName";

            if(desc)
                sql =sql+" DESC";
            return super.select(sql,FormTemplate.class,null,start,rows);
        }
        catch (DaoException e) {
           throw new FormDaoException("Error querying data from frw_form_template table",e);
        }
    }

    public Collection getFormTemplate(DaoQuery query,String sort,boolean desc,int start, int rows) throws FormDaoException {
        String sql;

        sql = "SELECT formTemplateId, templateName, previewXml, formXml"
              + " FROM frw_form_template WHERE" + query.getStatement();
        try {

            if (sort==null)
                sort ="templateName";

            if(sort.equals("templateName"))
                sql = sql+" ORDER BY templateName";

            if(desc)
                sql =sql+" DESC";
            return super.select(sql,FormTemplate.class,query.getArray(),start,rows);
        }
        catch (DaoException e) {
           throw new FormDaoException("Error querying data from frw_form_template table",e);
        }
    }

    public int getFormTemplateCount(DaoQuery query) throws FormDaoException {
        String sql;

        sql = "SELECT count(formTemplateId) as total"
              + " FROM frw_form_template WHERE" + query.getStatement();
        try {

            Map row = (Map) super.select(sql,HashMap.class,query.getArray(),0,-1).iterator().next();
            return Integer.parseInt(row.get("total").toString());
        }
        catch (DaoException e) {
           throw new FormDaoException("Error querying data from frw_form_template table",e);
        }
    }

    public void addFormTemplate(FormTemplate formTemplate) throws FormDaoException {
        String sql;

        sql = "INSERT INTO frw_form_template"
               + " (formTemplateId, templateName, previewXml, formXml)"
               + " VALUES (#formTemplateId#,#templateName#,#previewXml#,#formXml#)";

        try {
            super.update(sql,formTemplate);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error inserting data into frw_form_template table",e);
        }
    }

    public void updateFormTemplate(FormTemplate formTemplate) throws FormDaoException {
        String sql;

        sql = "UPDATE frw_form_template"
              + " SET templateName=#templateName#,previewXml=#previewXml#, formXml=#formXml#"
              + " WHERE formTemplateId = #formTemplateId#";
        try {
            super.update(sql,formTemplate);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error updating data in frw_form_template table:formTemplateId" + formTemplate.getFormTemplateId() ,e);
        }
    }

    public void deleteFormTemplate(FormTemplate formTemplate) throws FormDaoException {
        String sql;

        sql = "DELETE FROM frw_form_template"
              + " WHERE formTemplateId = #formTemplateId#";
        try {
            super.update(sql,formTemplate);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error deleting data from frw_form_template table:formTemplateId" + formTemplate.getFormTemplateId() ,e);
        }

    }

    public boolean isFormTemplateNameExist(String templateName) throws FormDaoException {
        String sql;

        sql = "SELECT count(templateName) AS total"
              + " FROM frw_form_template"
              + " WHERE templateName = '" + templateName + "'";

        Map row = null;
        try {
            row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
            int flag = Integer.parseInt(row.get("total").toString());
            if(flag > 0)
                return true;
            else
                return false;
        }
        catch (DaoException e) {
            throw new FormDaoException("Error checking the duplication of form template named " + templateName ,e);
        }

    }

    public boolean isFormTemplateNameExist(String templateName, String templateId) throws FormDaoException {
        String sql;

        sql = "SELECT count(templateName) AS total"
              + " FROM frw_form_template"
              + " WHERE templateName = '" + templateName + "'"
              + " AND formTemplateId <> '" + templateId + "'";

        Map row = null;
        try {
            row = (Map) super.select(sql,HashMap.class,null,0,-1).iterator().next();
            int flag = Integer.parseInt(row.get("total").toString());
            if(flag > 0)
                return true;
            else
                return false;
        }
        catch (DaoException e) {
            throw new FormDaoException("Error checking the duplication of form template named " + templateName ,e);
        }

    }

    public InputStream getTemplatePreviewXml(String templateId) throws FormDaoException {
        InputStream xmlInput = null;
        String sql ="SELECT previewXml FROM frw_form_template WHERE formTemplateId = '"+templateId+"'";
        Map map ;
        Collection collection;
        try {
            collection = super.select(sql,HashMap.class,null,0,-1);
            if (collection.iterator().hasNext()) {
                map = (Map) collection.iterator().next();
                if (map.get("previewXml") != null)
                    xmlInput = new ByteArrayInputStream(map.get("previewXml").toString().getBytes("UTF-8"));
            }
        }
        catch (DaoException e) {
            throw new FormDaoException("Error getting previewXml:templateId " + templateId ,e);
        }
        catch (UnsupportedEncodingException e) {
            Log.getLog(getClass()).fatal("Unsupported Encoding");
        }
        return xmlInput;
    }

    public void insertFormTemplateField(FormTemplateField formTemplateField) throws FormDaoException {
        String sql;

        sql = "INSERT INTO frw_form_template_field"
              + " (formTemplateId,formId,formName,templateNodeName)"
              + " VALUES(#formTemplateId#,#formId#,#formName#,#templateNodeName#)";

        try {
            super.update(sql,formTemplateField);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error inserting data into frw_form_template_field table",e);
        }
    }

    public Collection getFormTemplateField(String formTemplateId) throws FormDaoException {
        String sql;

        sql = "SELECT formTemplateId, formId, formName, templateNodeName"
              + " FROM frw_form_template_field"
              + " WHERE formTemplateId= '"+ formTemplateId + "'";

        try {
            return super.select(sql,FormTemplateField.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying data from frw_form_template_field table - formTemplateId:" + formTemplateId,e);
        }
    }

    public void deleteFormTemplateField(String formTemplateId) throws FormDaoException {
        String sql;

        sql = "DELETE FROM frw_form_template_field"
              + " WHERE formTemplateId = '" + formTemplateId + "'";

        try {
            super.update(sql,null);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error deleting data from frw_form_template_field - formTemplateId:" + formTemplateId, e);
        }
    }

    public void deleteFormTemplateFieldTemplateNodeName(String templateNodeName) throws FormDaoException {
        String sql;

        sql = "DELETE FROM frw_form_template_field"
              + " WHERE templateNodeName = '" + templateNodeName + "'";

        try {
            super.update(sql,null);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error deleting data from frw_form_template_field - templateNodeName:" + templateNodeName, e);
        }
    }


    public Collection getFormTemplateFieldDistinct(String formTemplateId) throws FormDaoException {
        String sql;

        sql = "SELECT DISTINCT formId, formName"
              + " FROM frw_form_template_field"
              + " WHERE formTemplateId= '"+ formTemplateId + "'";

        try {
            return super.select(sql,FormTemplateField.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying data from frw_form_template_field table - formTemplateId:" + formTemplateId,e);
        }
    }


    public Map getApproverUsers(String formId) throws DaoException{
        Map map = new SequencedHashMap();
        String sql ="SELECT f.userId AS id ,su.firstName AS firstName,su.lastName AS lastName"
                    + " FROM frw_form_approval f,security_user su"
                    + " WHERE f.formId='" + formId +"' AND f.userId=su.id"
                    + " ORDER BY f.priority";        
        Collection users = super.select(sql,User.class,null,0,-1);
        for(Iterator i= users.iterator();i.hasNext();){
            User user =(User)i.next();
            map.put(user.getId(),user.getName());
        }
        return map;
    }

    public void insertFormDraft(FormWorkFlowDataObject wfDO) throws FormDaoException {
        String sql;

        //set form submission date
        wfDO.setFormSubmissionDate(new Date());
        sql = "INSERT INTO frw_form_draft"
              + " (userId, formName, formUid, datePosted, formId)"
              + " VALUES (#userId#,#formName#,#formUid#,#formSubmissionDate#,#formId#)";

        try {
            super.update(sql,wfDO);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error inserting data into frw_form_draft - formUid:" + wfDO.getFormUid(), e);
        }

    }


    public Collection getFormDraft(DaoQuery query, String userId,String sort,boolean desc,int start, int rows) throws FormDaoException {
        String sql;

        sql = "SELECT userId, formUid, datePosted AS formSubmissionDate, formDisplayName"
              + " FROM frw_form_draft"
              + " LEFT JOIN frw_form ON frw_form.formId = frw_form_draft.formId"
              + " WHERE userId='" + userId + "'" + query.getStatement();

        if (sort == null || sort.equals("formDisplayName"))
            sql += " ORDER BY formDisplayName";
        else if (sort.equals("formSubmissionDate"))
            sql += " ORDER BY formSubmissionDate";

        if(desc)
            sql += " DESC";


        try {
            return super.select(sql,FormWorkFlowDataObject.class,query.getArray(),start,rows);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error querying data from frw_form_draft table - userId:" + userId,e);
        }
    }

    public int  getFormDraftCount(DaoQuery query, String userId) throws FormDaoException {
        String sql;

        sql = "SELECT count(userId) AS total"
              + " FROM frw_form_draft"
              + " LEFT JOIN frw_form ON frw_form.formId = frw_form_draft.formId"
              + " WHERE userId='" + userId + "'" + query.getStatement();

        Map row =  null;
        try {
            row = (Map) super.select(sql,HashMap.class,query.getArray(),0,-1).iterator().next();
            return Integer.parseInt(row.get("total").toString());
        }
        catch (DaoException e) {
           throw new FormDaoException("Error querying count from frw_form_draft table - userId:" + userId,e);
        }


    }

    public void deleteFormDraft(String formUid, String formId) throws FormDaoException {
            String sql;
            sql = "DELETE FROM frw_form_draft WHERE formUid='" + formUid + "' AND formId='" + formId + "'";

            try {
                super.update(sql,null);
            }
            catch (DaoException e) {
                throw new FormDaoException("Error deleting data into frw_form_draft - formUid:" + formUid, e);
            }
        }


    public void insertFormWorkFlow(FormWorkFlowDataObject wfDO) throws FormDaoException {
        String sql;

        sql = "INSERT INTO frw_form_workflow (formUid,formId,userId,formApprovalId,status,formSubmissionDate)"+
              " VALUES(#formUid#,#formId#,#userId#,#formApprovalId#,#status#,#formSubmissionDate#)";

        try {
            super.update(sql,wfDO);
        }
        catch (DaoException e) {
            throw new FormDaoException("Error updating frw_form_workflow - formUid:" + wfDO.getFormUid(), e);
        }
    }

     public String createTableSQL(FormElement form) {
        String ddl = "CREATE table " + FormModule.FORM_PREFIX + form.getAttributeValue("name")
                     + "(datePosted datetime, userId varchar(255), formUid varchar(255) primary key)";
        return ddl;
    }

    public String generateTemplateFieldSql(FormElement form, String fieldId, String fieldName, boolean repeat) {
        StringBuffer columnxBuf = null;
        List formChildren = form.getChildren();
        String childName = null;
        columnxBuf = new StringBuffer();
        for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();) {
            Element child = (Element) fromChildrenIter.next();
            if (!(child instanceof XmlWidgetAttributes)) {
                continue;
            }
            XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
            childName = child.getAttributeValue("name");
            if (childName.equals(fieldId)) {
                columnxBuf.append(fieldName).append(" ");
                if (repeat)
                    columnxBuf.append(fieldName).append(" ");
                if (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
                    || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE) {
                    columnxBuf.append("varchar(").append(xmlWidget.getSize()).append(")");
                }
                else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
                    columnxBuf.append("numeric(").append(xmlWidget.getSize()).append(")");
                }
                else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
                    columnxBuf.append("datetime");
                }
                else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
                    columnxBuf.append("text");
                }
            }

        }

        return columnxBuf.toString();

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

            if (xmlWidget!=null && (xmlWidget.getType() == FormElement.FORM_VARCHAR_TYPE
                || xmlWidget.getType() == FormElement.FORM_EMAIL_TYPE)) {
            	int x=xmlWidget.getSize();
                columnxBuf.append("varchar(").append(xmlWidget.getSize()).append(")").append(",");
            }
            else if (xmlWidget!=null && (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE)) {          		
                columnxBuf.append("numeric(").append(xmlWidget.getSize()).append(")").append(",");
            }
            else if (xmlWidget!=null && (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE)) {
                columnxBuf.append("datetime").append(",");
            }
            else if (xmlWidget!=null && (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE)) {
                columnxBuf.append("mediumtext").append(",");
            }
            else if (xmlWidget!=null && (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType())) {
                columnxBuf.append("float").append(",");
            }

        }

        if (columnxBuf.toString().endsWith(","))
            columnxBuf.deleteCharAt(columnxBuf.length() - 1);

        String createDDL = "ALTER TABLE " + FormModule.FORM_PREFIX + form.getAttributeValue("name")
                           + columnxBuf.toString();
        return createDDL;
    }

    public String changeSql(String sql, String tableName) {
        if (sql.length() > 0)
        sql = alterTableSql(" CHANGE " + sql, tableName);
        return sql;
    }

    public String addSql(String sql, String tableName) {
        if (sql.length() > 0)
            sql = alterTableSql(" Add " + sql, tableName);
        return sql;
    }

    public String alterTableSql(String sql, String tableName) {
        if (sql.length() > 0)
            sql = "ALTER TABLE " + tableName + sql;
        return sql;
    }

    public String generateDeleteColumnsSql(String tableName,List columnList) {
        String fieldName = null, sql = null;
        StringBuffer buffer = null;

        buffer = new StringBuffer();
        buffer.append("ALTER TABLE ").append(tableName);

        if (columnList != null)
            for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
                fieldName =  String.valueOf(iterator.next());
                buffer.append(" DROP COLUMN ").append(fieldName).append(",");
            }

        sql = buffer.toString();
        if (sql != null && !sql.trim().equals("") && sql.endsWith(",")) {
            sql = sql.substring(0,sql.length() - 1);
        }

        return sql;
    }

    public String dropColumnSql(FormTemplateField data,String value) {
        return " DROP COLUMN " + data.getTemplateNodeName() + value +",";
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
                    columnxBuf.append("varchar(").append(xmlWidget.getSize()).append(")");
                }
                else if (xmlWidget.getType() == FormElement.FORM_NUMERIC_TYPE) {
                    columnxBuf.append("numeric(").append(xmlWidget.getSize()).append(")");
                }
                else if (xmlWidget.getType() == FormElement.FORM_DATETIME_TYPE) {
                    columnxBuf.append("datetime");
                }
                else if (xmlWidget.getType() == FormElement.FORM_CLOB_TYPE) {
                    columnxBuf.append("text");
                }
                else if (FormElement.FORM_DECIMAL_NUMBER == xmlWidget.getType()) {
                    columnxBuf.append("float")  ;
                }
            }
            xmlWidget.removeMetaData();
        }

        return changeSql(columnxBuf.toString(),FormModule.FORM_PREFIX  + form.getAttributeValue("name"));
    }

	public static Map getTextFieldInfoMap(String formId) {
		FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
		
		HashMap map = new HashMap();
		InputStream stream = null;
		try {
			stream = module.getFormXML(formId);
			StructureEngine engine = new StructureEngine();
			engine.setXml(stream);
			FormLayout layout = engine.retriveStructure();
			
			List fieldList = layout.getFieldList();
			for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				if (object instanceof TextFieldField) {
					TextFieldField textFieldField = (TextFieldField) object;
					String fieldName = textFieldField.getName();
					map.put(fieldName, textFieldField);
				}
			}
		} catch (FormDaoException e) {
			Log.getLog(FormDaoSybase.class).error(e.toString(), e);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
			}
		}
		
		return map;
	}
}



