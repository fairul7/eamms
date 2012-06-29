package com.tms.cms.digest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.tms.collab.forum.model.Forum;
import com.tms.collab.forum.model.ForumDao;
import com.tms.collab.forum.model.Message;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.Transaction;
import kacang.util.UuidGenerator;

public class DigestDaoOracle extends DigestDao{
	public void init() throws DaoException {
	super.init();
	try{
        super.update("ALTER TABLE cms_digest add(ordering NUMBER(10,0) DEFAULT -1)", null);
    }
    catch (DaoException e){
        Log.getLog(getClass()).debug("Error adding new column status to content status table");
    }       
	try{
		super.update("CREATE TABLE cms_report_digest_content (digestContentId varchar(250) NOT NULL,contentName varchar(250),digestId varchar(250)" +
		      " ,ordering NUMBER(10,0) DEFAULT -1," +
		      " PRIMARY KEY (digestContentId))", null);
	}catch(Exception e){
	} 
	try{  
		super.update("CREATE TABLE cms_report_recipients (mailingListId varchar(250),orimailingListId varchar(250)," +
			  "recipientId varchar(250), recipientName varchar(250))", null);
	}catch(Exception e){
	}  
    try{
       super.update("CREATE TABLE cms_mailing (mailingListId varchar(250) NOT NULL,mailingListName varchar(250),digestIssue varchar(250),digestIssueName varchar(250)" +
           	" ,mailFormat varchar(250),lastEditBy varchar(250), lastEditDate DATE, dateCreate DATE," +
           	" PRIMARY KEY (mailingListId))", null);
       super.update("CREATE TABLE cms_mailing_recipients (mailingListId varchar(250),recipientId varchar(250), recipientName varchar(250))", null);
       super.update("CREATE TABLE cms_reports (reportId varchar(250),mailingListId varchar(250), digestIssueId varchar(250), " +
       		" digestIssueName varchar(250), dateCreate DATE,createdBy varchar(250),mailingListName varchar(250), " +
       		" emailFormat varchar(250),emailType varchar(250), PRIMARY KEY (reportId))", null);
       super.update("CREATE TABLE cms_report_digest (digestId varchar(250) NOT NULL,digestIssueId varchar(250),digestName varchar(250)" +
        		" ,PRIMARY KEY (digestId))", null);
    }catch(Exception e){
    }    
    try{
    super.update("CREATE TABLE cms_digest_issue (digestIssueId varchar(250) NOT NULL,digestIssue varchar(250)" +
    		" ,lastEditBy varchar(250), lastEditDate DATE,dateCreate DATE," +
    		" PRIMARY KEY (digestIssueId))", null);
    }catch(Exception e){    	
    }
    try{
        super.update("CREATE TABLE cms_digest (digestId varchar(250) NOT NULL,digestIssueId varchar(250),digest varchar(250)" +
        		" ,lastEditBy varchar(250), lastEditDate DATE,dateCreate DATE,ordering NUMBER(10,0) DEFAULT -1," +
        		" PRIMARY KEY (digestId))", null);
    }catch(Exception e){        	
    }
   try{
       super.update("CREATE TABLE digest_issue (digestIssueId varchar(250) NOT NULL,digestIssueName varchar(250)," +
            	" PRIMARY KEY (digestIssueId))", null);
       super.update("CREATE TABLE digest (digestId varchar(250) NOT NULL,digestName varchar(250)," +
           		" PRIMARY KEY (digestId))", null);
       }catch(Exception e){           	
    }  
    try{
       super.update("CREATE TABLE digest_recipients (recipientId varchar(250) NOT NULL,recipientName varchar(250)," +
                " PRIMARY KEY (recipientId))", null);
       super.update("CREATE TABLE digest_user_recipients (recipientId varchar(250) NOT NULL,userRecipientId varchar(250) NOT NULL)", null);
    }catch(Exception e){                	
    }          
    try{
       super.update("CREATE TABLE cms_digest_content (digestContentId varchar(250) NOT NULL,contentId varchar(250),digestId varchar(250)" +
           	" ,ordering NUMBER(10,0) DEFAULT -1, dateCreate DATE," +
           	" PRIMARY KEY (digestContentId))", null);
    }catch(Exception e){
    }  
	}
	
	public DigestIssueDataObject getDigestIssueDo(String digestIssueId) throws DaoException
    {
		String sql = "SELECT cms_digest_issue.digestIssueId, digestIssue, lastEditBy, lastEditDate, dateCreate, " +
		" digest_issue.digestIssueName FROM cms_digest_issue INNER JOIN digest_issue " +
		" ON cms_digest_issue.digestIssue=digest_issue.digestIssueId WHERE cms_digest_issue.digestIssueId=? ";

		Collection collection= super.select(sql, DigestIssueDataObject.class, new Object[]{digestIssueId}, 0, 1);
		return (DigestIssueDataObject)collection.iterator().next();
    }
	
	public Collection getDigestIssue(String searchCriteria, String sort, boolean desc, int start, int rows) throws DaoException
    {
		Collection paramList = new ArrayList();
        String sql = "SELECT cms_digest_issue.digestIssueId, digestIssue, lastEditBy, lastEditDate, dateCreate, " +
        		" digest_issue.digestIssueName,security_user.firstname, security_user.lastname FROM cms_digest_issue INNER JOIN " +
        		" security_user ON cms_digest_issue.lastEditBy=security_user.id INNER JOIN digest_issue " +
        		" ON cms_digest_issue.digestIssue=digest_issue.digestIssueId WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digest_issue.digestIssueName, ?, 'i')";
            paramList.add(searchCriteria);
        }
        if(sort!=null && !sort.trim().equals(""))
            sql += " ORDER BY " + sort;
        else
            sql+=" ORDER BY lastEditDate DESC";
        
        return super.select(sql, DigestIssueDataObject.class, paramList.toArray(), start, rows);
    }
	
	public Collection getSetupDigestIssue(String searchCriteria, String sort, boolean desc, int start, int rows) throws DaoException
    {
		Collection paramList = new ArrayList();
        String sql = "SELECT digestIssueId, digestIssueName FROM digest_issue WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digestIssueName, ?, 'i')";
            paramList.add(searchCriteria);
        }
        if(sort!=null && !sort.trim().equals(""))
            sql += " ORDER BY " + sort;
        else
            sql+=" ORDER BY digestIssueName";
        if(desc)
        {
            sql += " DESC";
        }
        return super.select(sql, DigestIssueDataObject.class, paramList.toArray(), start, rows);
    }
	
	public Collection getSetupDigest(String searchCriteria, String sort, boolean desc, int start, int rows) throws DaoException
    {
		Collection paramList = new ArrayList();
        String sql = "SELECT digestId, digestName FROM digest WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digestName, ?, 'i')";
            paramList.add(searchCriteria);
        }
        if(sort!=null && !sort.trim().equals(""))
            sql += " ORDER BY " + sort;
        else
            sql+=" ORDER BY digestName";
        if(desc)
        {
            sql += " DESC";
        }
        
        return super.select(sql, DigestDataObject.class, paramList.toArray(), start, rows);
    }
	
	public int getNumOfDigestIssue(String searchCriteria) throws DaoException
    {
        Collection paramList = new ArrayList();
        String sql="SELECT COUNT(DISTINCT cms_digest_issue.digestIssueId) AS total " +
                " FROM cms_digest_issue INNER JOIN " +
        		" security_user ON cms_digest_issue.lastEditBy=security_user.id INNER JOIN digest_issue " +
        		" ON cms_digest_issue.digestIssue=digest_issue.digestIssueId WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digestIssueName, ?, 'i')";
            paramList.add(searchCriteria);
        }
       
        Map row = (Map)super.select(sql, HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public int getNumOfSetupDigestIssue(String searchCriteria) throws DaoException
    {
        Collection paramList = new ArrayList();
        String sql="SELECT COUNT(DISTINCT digestIssueId) AS total " +
                " FROM digest_issue WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digestIssueName, ?, 'i')";
            paramList.add(searchCriteria);
        }
       
        Map row = (Map)super.select(sql, HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public int getNumOfSetupDigest(String searchCriteria) throws DaoException
    {
        Collection paramList = new ArrayList();
        String sql="SELECT COUNT(DISTINCT digestId) AS total " +
                " FROM digest WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digestName, ?, 'i')";
            paramList.add(searchCriteria);
        }
       
        Map row = (Map)super.select(sql, HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public int getNumOfDigestByDigestIssue(String digestIssueId) throws DaoException
    {
        String sql="SELECT COUNT(DISTINCT digestId) AS total " +
                " FROM cms_digest WHERE digestIssueId=? ";       
        Map row = (Map)super.select(sql, HashMap.class, new Object[]{digestIssueId}, 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public int getNumOfContentByDigest(String digestId) throws DaoException
    {
        String sql="SELECT COUNT(DISTINCT digestContentId) AS total " +
                " FROM cms_digest_content WHERE digestId=? ";       
        Map row = (Map)super.select(sql, HashMap.class, new Object[]{digestId}, 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public void deleteDigestIssue(String digestIssueId) throws DaoException
    {
        try
        {
                String sql = "DELETE FROM cms_digest_issue WHERE digestIssueId=?";
                super.update(sql, new Object[]{digestIssueId});   
                String sql2 = "DELETE FROM cms_digest WHERE digestIssueId=?";
                super.update(sql2, new Object[]{digestIssueId});  
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public void deleteSetupDigestIssue(String digestIssueId) throws DaoException
    {
        try
        {
                String sql = "DELETE FROM digest_issue WHERE digestIssueId=?";
                super.update(sql, new Object[]{digestIssueId});   
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public void deleteSetupDigest(String digestId) throws DaoException
    {
        try
        {
                String sql = "DELETE FROM digest WHERE digestId=?";
                super.update(sql, new Object[]{digestId});   
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public Collection getDigestIssue(DaoQuery query) throws DaoException
    {
		String sql = "SELECT digestIssueId, digestIssue, lastEditBy, lastEditDate, dateCreate  FROM cms_digest_issue WHERE digestIssueId=digestIssueId "+ query.getStatement() ;
        
        sql+=" ORDER BY dateCreate DESC";
                
        return super.select(sql, DigestIssueDataObject.class, query.getArray(), 0, -1);
    }
	
	public Collection getDigest(DaoQuery query) throws DaoException
    {
		String sql = "SELECT digestId,digestIssueId, digest, lastEditBy, lastEditDate, dateCreate, ordering  FROM cms_digest WHERE digestId=digestId "+ query.getStatement() ;
        
        sql+=" ORDER BY dateCreate DESC";
                
        return super.select(sql, DigestDataObject.class, query.getArray(), 0, -1);
    }
	
	public Collection getSetupDigestIssue(DaoQuery query) throws DaoException
    {
		String sql = "SELECT digestIssueId, digestIssueName  FROM digest_issue WHERE digestIssueId=digestIssueId "+ query.getStatement() ;
        
        sql+=" ORDER BY digestIssueName";
                
        return super.select(sql, DigestIssueDataObject.class, query.getArray(), 0, -1);
    }
	
	public Collection getDigestIssueMain(DaoQuery query) throws DaoException
    {
		String sql = "SELECT cms_digest_issue.digestIssueId, digest_issue.digestIssueName,cms_digest_issue.dateCreate  FROM digest_issue,cms_digest_issue WHERE digest_issue.digestIssueId=cms_digest_issue.digestIssue "+ query.getStatement() ;
        
        sql+=" ORDER BY digest_issue.digestIssueName";
        
        return super.select(sql, DigestIssueDataObject.class, query.getArray(), 0, -1);
    }
	
	public void insertDigestIssue(DigestIssueDataObject dido) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "INSERT INTO cms_digest_issue (digestIssueId, digestIssue, lastEditBy, lastEditDate, dateCreate) " +
                         "VALUES (#digestIssueId#, #digestIssue#, #lastEditBy#, #lastEditDate#, #dateCreate#)";

            tx.update(sql, dido);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public void insertDigestIssueList(DigestIssueDataObject dido) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "INSERT INTO digest_issue (digestIssueId, digestIssueName) " +
                         "VALUES (#digestIssue#, #digestIssueName#)";

            tx.update(sql, dido);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }	
	
	public void updateSetupDigestIssue(DigestIssueDataObject dido) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "UPDATE digest_issue SET digestIssueName=#digestIssueName# WHERE digestIssueId=#digestIssue#";

            tx.update(sql, dido);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public void updateSetupDigest(DigestDataObject ddo) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "UPDATE digest SET digestName=#digestName# WHERE digestId=#digest#";

            tx.update(sql, ddo);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	
	public Collection getDigest(String digestIssueId, String searchCriteria, String sort, boolean desc, int start, int rows) throws DaoException
    {
		Collection paramList = new ArrayList();
        String sql = "SELECT cms_digest.ordering,cms_digest.digestId, digest, lastEditBy, lastEditDate, dateCreate, " +
        		" digest.digestName,security_user.firstname, security_user.lastname FROM cms_digest INNER JOIN " +
        		" security_user ON cms_digest.lastEditBy=security_user.id INNER JOIN digest " +
        		" ON cms_digest.digest=digest.digestId WHERE digestIssueId=? ";
        
        paramList.add(digestIssueId);
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digest.digestName, ?, 'i')";
            paramList.add(searchCriteria);
        }
        if(sort!=null && !sort.trim().equals("")){
            sql += " ORDER BY " + sort;
            if (desc)
                sql+=" DESC";
        }
        else
            sql+=" ORDER BY cms_digest.ordering DESC,lastEditDate DESC";
        
        return super.select(sql, DigestDataObject.class, paramList.toArray(), start, rows);
    }
	
	public int getNumOfDigest(String digestIssueId, String searchCriteria) throws DaoException
    {
        Collection paramList = new ArrayList();
        String sql="SELECT COUNT(DISTINCT cms_digest.digestId) AS total " +
                " FROM cms_digest INNER JOIN " +
        		" security_user ON cms_digest.lastEditBy=security_user.id " +
        		" INNER JOIN digest " +
        		" ON cms_digest.digest=digest.digestId WHERE cms_digest.digestIssueId=? ";
        paramList.add(digestIssueId);
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(digest.digestName, ?, 'i')";
            paramList.add(searchCriteria);
        }
       
        Map row = (Map)super.select(sql, HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public void deleteDigest(String digestId) throws DaoException
    {
        try
        {
                String sql = "DELETE FROM cms_digest WHERE digestId=?";
                super.update(sql, new Object[]{digestId});   
                String sql2 = "DELETE FROM cms_digest_content WHERE digestId=?";
                super.update(sql2, new Object[]{digestId});    
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public Collection getSetupDigest(DaoQuery query) throws DaoException
    {
        String sql = "SELECT digestId, digestName  FROM digest WHERE digestId=digestId"+ query.getStatement() ;
                
            sql+=" ORDER BY digestName";
        
        return super.select(sql, DigestDataObject.class, query.getArray(), 0, -1);
    }
	
	public Collection getDigestMain(DaoQuery query) throws DaoException
    {
        String sql = "SELECT cms_digest.ordering,cms_digest.digestId, cms_digest.digestIssueId, digest.digestName  FROM digest, cms_digest WHERE cms_digest.digest=digest.digestId "+ query.getStatement() ;
                
            sql+=" ORDER BY cms_digest.ordering DESC,cms_digest.lastEditDate DESC";
        
        return super.select(sql, DigestDataObject.class, query.getArray(), 0, -1);
    }
	
	public void insertDigestList(DigestDataObject ddo) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "INSERT INTO digest (digestId, digestName) " +
                         "VALUES (#digest#, #digestName#)";

            tx.update(sql, ddo);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public void insertDigest(DigestDataObject ddo) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "INSERT INTO cms_digest (digestIssueId, digestId, digest, lastEditBy, lastEditDate, dateCreate) " +
                         "VALUES (#digestIssueId#, #digestId#, #digest#, #lastEditBy#, #lastEditDate#, #dateCreate#)";

            tx.update(sql, ddo);
            String sql2 = "UPDATE cms_digest_issue SET lastEditBy=#lastEditBy#, lastEditDate=#lastEditDate# WHERE digestIssueId=#digestIssueId#";

            tx.update(sql2, ddo);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public Collection getRecipients(String searchCriteria, String sort, boolean desc, int start, int rows) throws DaoException
    {
		Collection paramList = new ArrayList();
        String sql = "SELECT recipientId, recipientId AS id, recipientName FROM digest_recipients WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(recipientName, ?, 'i')";
            paramList.add(searchCriteria);
        }
        if(sort!=null && !sort.trim().equals(""))
            sql += " ORDER BY " + sort;
        else
            sql+=" ORDER BY recipientName";
        if(desc)
        {
            sql += " DESC";
        }
        return super.select(sql, RecipientDataObject.class, paramList.toArray(), start, rows);
    }
	
	public Collection getRecipients(DaoQuery query) throws DaoException
    {
        String sql = "SELECT recipientId, recipientName FROM digest_recipients WHERE 1=1 "+ query.getStatement() ;
        
        sql+=" ORDER BY recipientName";
        
        return super.select(sql, RecipientDataObject.class, query.getArray(), 0, -1);
    }
	
	public int getNumOfRecipients(String searchCriteria) throws DaoException
    {
        Collection paramList = new ArrayList();
        String sql="SELECT COUNT(DISTINCT recipientId) AS total " +
                " FROM digest_recipients WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND REGEXP_LIKE(recipientName, ?, 'i')";
            paramList.add(searchCriteria);
        }
       
        Map row = (Map)super.select(sql, HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public void deleteRecipients(String recipientId) throws DaoException
    {
        try
        {
                String sql = "DELETE FROM digest_recipients WHERE recipientId=?";
                super.update(sql, new Object[]{recipientId});   
                String sql2 = "DELETE FROM digest_user_recipients WHERE recipientId=?";
                super.update(sql2, new Object[]{recipientId});  
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public void insertRecipients(RecipientDataObject rdo, String [] userIds) throws DaoException
    {
        try
        {
                String sql = "INSERT INTO digest_recipients (recipientId, recipientName) " +
                "VALUES (#recipientId#, #recipientName#)";
                super.update(sql, rdo);  
                if (userIds == null) {
                    return;
                }
                for (int i=0; i<userIds.length; i++) {
                    String[] params = new String[] { rdo.getRecipientId(), userIds[i] };
                    super.update("INSERT INTO digest_user_recipients (recipientId, userRecipientId) VALUES (?, ?)", params);
                }
                
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public void updateRecipients(RecipientDataObject rdo, String [] userIds) throws DaoException
    {
        try
        {
        		String delete = "DELETE FROM digest_user_recipients WHERE recipientId=?";
        		super.update(delete, new Object[]{rdo.getRecipientId()});
                String sql = "UPDATE digest_recipients SET recipientName=#recipientName# WHERE recipientId=#recipientId#";
                super.update(sql, rdo);  
                if (userIds == null) {
                    return;
                }
                for (int i=0; i<userIds.length; i++) {
                    String[] params = new String[] { rdo.getRecipientId(), userIds[i] };
                    super.update("INSERT INTO digest_user_recipients (recipientId, userRecipientId) VALUES (?, ?)", params);
                }
                
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public Collection getDigestUser(String recipientId) throws DaoException
    {
		Collection paramList = new ArrayList();
        String sql = "SELECT recipientId, userRecipientId FROM digest_user_recipients WHERE recipientId=? ";
        paramList.add(recipientId);            
        return super.select(sql, RecipientDataObject.class, paramList.toArray(), 0, -1);
    }	
	
	public Collection getDigestRecipients(DaoQuery query) throws DaoException
    {
        String sql = "SELECT recipientId, recipientName FROM digest_recipients WHERE 1=1 "+ query.getStatement();            
       return super.select(sql, RecipientDataObject.class, query.getArray(), 0, -1);
    }
	
	public RecipientDataObject getDigestRecipientsDo(String recipientId) throws DaoException
    {
		String sql = "SELECT recipientId, recipientName FROM digest_recipients WHERE recipientId=? ";

		Collection collection= super.select(sql, RecipientDataObject.class, new Object[]{recipientId}, 0, 1);
		return (RecipientDataObject)collection.iterator().next();
    }
	
	public void insertDigestContent(DigestContentDataObject dcdo) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "INSERT INTO cms_digest_content (digestContentId, contentId, digestId, dateCreate) " +
                         "VALUES (#digestContentId#, #contentId#, #digestId#, #dateCreate#)";
            tx.update(sql, dcdo);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public DigestDataObject getDigestDo(String digestId) throws DaoException
    {
		String sql = "SELECT digestId, digestIssueId, digest, lastEditBy, lastEditDate, dateCreate " +
				"FROM cms_digest WHERE digestId=? ";
		Collection collection= super.select(sql, DigestDataObject.class, new Object[]{digestId}, 0, 1);
		return (DigestDataObject)collection.iterator().next();
    }
	
	public void updateAll(String userId, String digestId) throws DaoException
    {
        Transaction tx = null;
        try
        {
        	DigestDataObject ddo=new DigestDataObject();
        		ddo=getDigestDo(digestId);
            tx = getTransaction();
            tx.begin();
            String sql = "UPDATE cms_digest SET lastEditBy=?, lastEditDate=? " +
                         "WHERE digestId=?";
            tx.update(sql, new Object[]{userId,Calendar.getInstance().getTime(),digestId});
            String sql2 = "UPDATE cms_digest_Issue SET lastEditBy=?, lastEditDate=? " +
            "WHERE digestIssueId=?";
            tx.update(sql2,new Object[]{userId,Calendar.getInstance().getTime(),ddo.getDigestIssueId()});
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public void updateDigestIssue(String userId, String digestIssueId) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql1 = "UPDATE cms_digest_Issue SET lastEditBy=?, lastEditDate=? " +
            "WHERE digestIssueId=?";
            tx.update(sql1,new Object[]{userId,Calendar.getInstance().getTime(),digestIssueId});
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public void reorderDigestContent(String digestContentId, int order) throws DaoException
    {
        Transaction tx = null;
        try
        {        	
            tx = getTransaction();
            tx.begin();
            String sql = "UPDATE cms_digest_content SET ordering=? " +
                         "WHERE digestContentId=?";
            tx.update(sql, new Object[]{new Integer(order),digestContentId});          
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public void reorderDigest(String digestId, int order) throws DaoException
    {
        Transaction tx = null;
        try
        {        	
            tx = getTransaction();
            tx.begin();
            String sql = "UPDATE cms_digest SET ordering=? " +
                         "WHERE digestId=?";
            tx.update(sql, new Object[]{new Integer(order),digestId});          
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public void deleteDigestContent(String contentId) throws DaoException
    {
        try
        {
                String sql = "DELETE FROM cms_digest_content WHERE digestContentId=?";
                super.update(sql, new Object[]{contentId});                   
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public void deleteMailingList(String nailingListId) throws DaoException
    {
        try
        {
                String sql = "DELETE FROM cms_mailing WHERE mailingListId=?";
                super.update(sql, new Object[]{nailingListId});                   
        }
        catch(DaoException e)
        {
            Log.getLog(DigestDaoOracle.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
	
	public void insertMailingList(MailingListDataObject mldo) throws DaoException
    {
        try
        {
            String sql = "INSERT INTO cms_mailing (mailingListId, mailingListName, digestIssue, digestIssueName, mailFormat, lastEditBy, lastEditDate, dateCreate) " +
                         "VALUES (#mailingListId#, #mailingListName#, #digestIssue#, #digestIssueName#, #mailFormat#, #lastEditBy#, #lastEditDate#, #dateCreate#)";
            super.update(sql, mldo);
            for (Iterator i = mldo.getRecipients().iterator(); i.hasNext();) {
            	RecipientDataObject recipient = (RecipientDataObject)i.next();
            	String sql2 = "INSERT INTO cms_mailing_recipients (mailingListId, recipientId, recipientName) " +
                "VALUES (?,?,?)";
            	super.update(sql2, new Object[]{mldo.getMailingListId(),recipient.getRecipientId(),recipient.getRecipientName()});
            }
        }
        catch(Exception e)
        {

            throw new DaoException(e.toString());
        }
    }
	
	public Collection getMailingList(String searchCriteria, String sort, boolean desc, int start, int rows) throws DaoException
    {
		Collection paramList = new ArrayList();
        String sql = "SELECT mailingListId, mailingListName, digestIssue, digestIssueName, " +
        		" mailFormat, lastEditBy, lastEditDate, dateCreate, security_user.firstname, " +
        		" security_user.lastname FROM cms_mailing INNER JOIN " +
        		" security_user ON cms_mailing.lastEditBy=security_user.id " +
        		" WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND (REGEXP_LIKE(mailingListName, ?, 'i') OR REGEXP_LIKE(digestIssueName, ?, 'i'))";
            paramList.add(searchCriteria);
            paramList.add(searchCriteria);
        }
        if(sort!=null && !sort.trim().equals(""))
            sql += " ORDER BY " + sort;
        else
            sql+=" ORDER BY lastEditDate DESC";
        
        return super.select(sql, MailingListDataObject.class, paramList.toArray(), start, rows);
    }
	
	public int getMailingListCount(String searchCriteria) throws DaoException
    {
        
        Collection paramList = new ArrayList();
        String sql="SELECT COUNT(DISTINCT mailingListId) AS total " +
                " FROM cms_mailing WHERE 1=1 ";
        
        if (searchCriteria != null && searchCriteria.trim().length() > 0 && searchCriteria != null)
        {
            sql+=" AND (REGEXP_LIKE(mailingListName, ?, 'i') OR REGEXP_LIKE(digestIssueName, ?, 'i'))";
            paramList.add(searchCriteria);
            paramList.add(searchCriteria);
        }
       
        Map row = (Map)super.select(sql, HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
	public Date getMailingListLastEditDate(String mailingListId) throws DaoException
    {
		Collection result= new ArrayList();
		Collection paramList = new ArrayList();
        String sql = " SELECT DISTINCT cms_reports.reportId, cms_reports.mailingListId, cms_reports.digestIssueId AS digestIssue, cms_reports.digestIssueName, " +
            		" cms_reports.dateCreate, cms_reports.createdBy, cms_reports.mailingListName, cms_reports.emailFormat AS mailFormat, " +
            		" cms_reports.emailType FROM cms_reports INNER JOIN cms_report_recipients ON cms_reports.mailingListId=cms_report_recipients.mailingListId " +
            		" WHERE cms_report_recipients.orimailingListId=? ";

        	paramList.add(mailingListId);
       sql+=" ORDER BY cms_reports.dateCreate DESC";
        result= super.select(sql, MailingListDataObject.class, paramList.toArray(), 0, 1);
        for (Iterator i=result.iterator(); i.hasNext();)
        {
        	MailingListDataObject mldo = (MailingListDataObject)i.next();
        	return mldo.getDateCreate();
        }
        return null;
    }
	
	public Collection getMailingList(DaoQuery query) throws DaoException
    {
        String sql = "SELECT mailingListId, mailingListName, digestIssue, digestIssueName, " +
        		" mailFormat, lastEditBy, lastEditDate, dateCreate, security_user.firstname, " +
        		" security_user.lastname FROM cms_mailing INNER JOIN " +
        		" security_user ON cms_mailing.lastEditBy=security_user.id " +
        		" WHERE 1=1 "+ query.getStatement() ;
 
            sql+=" ORDER BY lastEditDate DESC";
        
        return super.select(sql, MailingListDataObject.class, query.getArray(), 0, -1);
    }
	
	public Collection getMailingRecipients(DaoQuery query) throws DaoException
    {
		String sql = "SELECT mailingListId, recipientId, recipientName FROM cms_mailing_recipients WHERE mailingListId=mailingListId "+ query.getStatement() ;
        
        sql+=" ORDER BY recipientName";
                
        return super.select(sql, RecipientDataObject.class, query.getArray(), 0, -1);
    }
	
	public Collection getMailingDigestUser(DaoQuery query) throws DaoException
    {
        String sql = "SELECT cms_mailing_recipients.recipientId,cms_mailing_recipients.recipientName, userRecipientId FROM cms_mailing_recipients, digest_user_recipients WHERE cms_mailing_recipients.recipientId=digest_user_recipients.recipientId "+ query.getStatement();          
        return super.select(sql, RecipientDataObject.class, query.getArray(), 0, -1);
    }
	
	public void insertReport(MailingListDataObject mldo) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            UuidGenerator uuid=UuidGenerator.getInstance();
            String mailingListId=uuid.getUuid();
            String digestIssueId=uuid.getUuid();
            String sql = "INSERT INTO cms_reports (reportId, mailingListId, digestIssueId, digestIssueName, " +
            		" dateCreate, createdBy, mailingListName, emailFormat, emailType) " +
                    " VALUES (?,?,?,?,?,?,?,?,?)";
            tx.update(sql, new Object[]{uuid.getUuid(),mailingListId,digestIssueId,mldo.getDigestIssueName(),Calendar.getInstance().getTime(),mldo.getUser().getName(),mldo.getMailingListName(),mldo.getEmailFormat(),mldo.getEmailFormatType()});
            for (Iterator i = mldo.getDigest().iterator(); i.hasNext();)
            {
            	DigestDataObject ddo = (DigestDataObject) i.next();
            	String digestId=uuid.getUuid();
            	String sql2 = "INSERT INTO cms_report_digest (digestId, digestIssueId, digestName) " +
            	" VALUES (?,?,?)";
            	tx.update(sql2, new Object[]{digestId,digestIssueId,ddo.getDigestName()});
            	if(ddo.getContents()!=null){
            	for (Iterator j = ddo.getContents().iterator(); j.hasNext();)
            	{
            		DigestContentDataObject dcdo = (DigestContentDataObject) j.next();
            		String sql3 = "INSERT INTO cms_report_digest_content (digestContentId, contentName, digestId,ordering) " +
                	" VALUES (?,?,?,?)";
                	tx.update(sql3, new Object[]{uuid.getUuid(),dcdo.getName(),digestId,dcdo.getOrdering()});
            	}}
            }
            for (Iterator i = mldo.getRecipients().iterator(); i.hasNext();)
            {
            	RecipientDataObject rdo = (RecipientDataObject) i.next();
            	String sql4 = "INSERT INTO cms_report_recipients (mailingListId, recipientId, recipientName, orimailingListId) " +
            	" VALUES (?,?,?,?)";
            	tx.update(sql4, new Object[]{mailingListId,uuid.getUuid(),rdo.getRecipientName(),mldo.getMailingListId()});
            } 
            tx.commit();                     
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
	
	public Collection getNewsReport(Date from, Date to, String emailFormat, String emailType) throws DaoException
    {
		Collection result= new ArrayList();
		Collection paramList = new ArrayList();
        String sql = " SELECT reportId, mailingListId, digestIssueId AS digestIssue, digestIssueName, " +
            		" dateCreate, createdBy, mailingListName, emailFormat AS mailFormat, emailType FROM cms_reports WHERE 1=1 ";
        
        if(from != null && to != null){			
        	sql+="AND dateCreate >= ? " +
				"AND dateCreate <= ? ";
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(from);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);			
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(to);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);			
			paramList.add(startCal.getTime());
			paramList.add(endCal.getTime());
		}
        if(emailFormat!=null){
        	sql+=" AND emailFormat=?";
        	paramList.add(emailFormat);
        }if(emailType!=null){
        	sql+=" AND emailType=?";
        	paramList.add(emailType);
        }
        sql+=" ORDER BY dateCreate DESC"; 
        result= super.select(sql, MailingListDataObject.class, paramList.toArray(), 0, -1);
        for (Iterator i = result.iterator(); i.hasNext();)
        {
        	MailingListDataObject mldo = (MailingListDataObject) i.next();
        	paramList = new ArrayList();
        	String sql2 = " SELECT mailingListId, recipientId, recipientName FROM cms_report_recipients WHERE mailingListId=? ";
        	paramList.add(mldo.getMailingListId());
        	sql2+=" ORDER BY recipientName";
        	mldo.setRecipients(super.select(sql2, RecipientDataObject.class, paramList.toArray(), 0, -1));
        	
        	paramList = new ArrayList();
        	String sql3 = " SELECT digestId, digestIssueId, digestName FROM cms_report_digest WHERE digestIssueId=? ";
        	paramList.add(mldo.getDigestIssue());
        	sql3+=" ORDER BY digestName";
        	mldo.setDigest(super.select(sql3, DigestDataObject.class, paramList.toArray(), 0, -1));
        	mldo.setDigestSize(mldo.getDigest().size());
        	for (Iterator j = mldo.getDigest().iterator(); j.hasNext();)
            {
        		DigestDataObject ddo = (DigestDataObject) j.next();
        		
        		paramList = new ArrayList();
            	String sql4 = " SELECT digestContentId, contentName AS name, digestId,ordering FROM cms_report_digest_content WHERE digestId=? ";
            	paramList.add(ddo.getDigestId());
            	sql4+=" ORDER BY contentName";
            	ddo.setContents(super.select(sql4, DigestContentDataObject.class, paramList.toArray(), 0, -1));
        		
            }
        }
        return result;
    }
}
