package com.tms.cms.digest.model;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataSourceDao;
import kacang.util.Log;
import kacang.util.Transaction;
import kacang.util.UuidGenerator;

import java.util.*;
import java.util.Date;
import java.sql.*;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.DefaultContentObject;

public class DigestDao extends DataSourceDao {
	public void init() throws DaoException {
		super.init();
		try{
	        super.update("ALTER TABLE cms_digest MODIFY lastEditDate DATETIME", null);
	        super.update("ALTER TABLE cms_digest MODIFY dateCreate DATETIME", null);
	        super.update("ALTER TABLE cms_reports MODIFY dateCreate DATETIME", null);
	        super.update("ALTER TABLE cms_digest_issue MODIFY lastEditDate DATETIME", null);
	        super.update("ALTER TABLE cms_digest_issue MODIFY dateCreate DATETIME", null);
	        super.update("ALTER TABLE cms_mailing MODIFY lastEditDate DATETIME", null);
	        super.update("ALTER TABLE cms_mailing MODIFY dateCreate DATETIME", null);
	        
	    }
	    catch (DaoException e){
	        Log.getLog(getClass()).debug("Error altering column ");
	    }   
		try{
	        super.update("ALTER TABLE cms_digest add(ordering INT DEFAULT -1)", null);
	    }
	    catch (DaoException e){
	        Log.getLog(getClass()).debug("Error adding new column status to content status table");
	    }       
		try{
			super.update("CREATE TABLE cms_report_digest_content (digestContentId varchar(250) NOT NULL,contentName varchar(250),digestId varchar(250)" +
			      " ,ordering INT DEFAULT -1," +
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
	           	" ,mailFormat varchar(250),lastEditBy varchar(250), lastEditDate DATETIME, dateCreate DATETIME," +
	           	" PRIMARY KEY (mailingListId))", null);
	       super.update("CREATE TABLE cms_mailing_recipients (mailingListId varchar(250),recipientId varchar(250), recipientName varchar(250))", null);
	       super.update("CREATE TABLE cms_reports (reportId varchar(250),mailingListId varchar(250), digestIssueId varchar(250), " +
	       		" digestIssueName varchar(250), dateCreate DATETIME,createdBy varchar(250),mailingListName varchar(250), " +
	       		" emailFormat varchar(250),emailType varchar(250), PRIMARY KEY (reportId))", null);
	       super.update("CREATE TABLE cms_report_digest (digestId varchar(250) NOT NULL,digestIssueId varchar(250),digestName varchar(250)" +
	        		" ,PRIMARY KEY (digestId))", null);
	    }catch(Exception e){
	    }    
	    try{
	    super.update("CREATE TABLE cms_digest_issue (digestIssueId varchar(250) NOT NULL,digestIssue varchar(250)" +
	    		" ,lastEditBy varchar(250), lastEditDate DATETIME,dateCreate DATETIME," +
	    		" PRIMARY KEY (digestIssueId))", null);
	    }catch(Exception e){    	
	    }
	    try{
	        super.update("CREATE TABLE cms_digest (digestId varchar(250) NOT NULL,digestIssueId varchar(250),digest varchar(250)" +
	        		" ,lastEditBy varchar(250), lastEditDate DATETIME,dateCreate DATETIME,ordering INT DEFAULT -1," +
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
	           	" ,ordering INT DEFAULT -1, dateCreate DATETIME," +
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
                sql += " AND (digest_issue.digestIssueName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND (digestIssueName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND (digestName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
	            sql += " AND (digestIssueName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
	            sql += " AND (digestIssueName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
	            sql += " AND (digestName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND (digest.digestName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND (digest.digestName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND (recipientName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND (recipientName LIKE ?)";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND ((mailingListName LIKE ?) OR (digestIssueName LIKE ?))";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
                sql += " AND ((mailingListName LIKE ?) OR (digestIssueName LIKE ?))";
                paramList.add((searchCriteria == null) ? "%%" : "%" + searchCriteria + "%");
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
		
		public Collection selectDigestContents(String digestId, String name, String sort, boolean desc, int start, int rows) throws DaoException {
	        try {
	            Collection paramList = new ArrayList();
	            /*String columns = "cds.contentId, cds.digestId, cds.digestContentId, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, cds.ordering, template, publishVersion, status, source, syncDest, syncContent, localSource, foreignSource, sourceDate," +
	                    "className, w.version, w.name, summary, description,  author, dateCreate, related, filename ";*/
                String columns = "cds.contentId, cds.digestId, cds.digestContentId, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, cds.ordering, s.template, publishVersion, className, w.version, w.name, w.summary, w.description,  w.author, dateCreate, w.related, filename ";

	            StringBuffer sql = new StringBuffer(
	                    "SELECT " +
	                    columns +
	                    "FROM cms_digest_content cds " +
	                    "LEFT JOIN cms_content_status s ON cds.contentId=s.id " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "LEFT JOIN cms_content_document d ON d.id = s.id AND d.version = w.version " +
	                    "WHERE cds.digestId=? ");	           
	            paramList.add(digestId);
	            if (name != null && !"".equals(name)) {
	                //sql.append(" AND name LIKE ?");
	                //paramList.add("%" + name + "%");
                    sql.append(" AND (name LIKE ?)");
                    paramList.add("%" + name + "%");
	            }
	            
	            	            
	            if (sort != null && sort.trim().length() > 0) {
	                sql.append(" ORDER BY ");
					if(sort.equals("date"))
						sql.append("date");
					else
	                	sql.append(sort);
	                if (desc)
	                    sql.append(" DESC");
	            }
	            else {
	                sql.append(" ORDER BY cds.ordering DESC, dateCreate DESC");
	            }

	            // custom JDBC for performance
	            long startTime = System.currentTimeMillis();
	            long queryTime = startTime;
	            Collection results = new ArrayList();
	            Connection con = null;
	            PreparedStatement pstmt = null;
	            ResultSet rs = null;
	            int currentRow = 0;

	            try {
	                con = getDataSource().getConnection();
	                pstmt = con.prepareStatement(sql.toString());
	                int c=1;
	                Object o;
	                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
	                    o = i.next();
	                    if (o instanceof Date) {
	                        pstmt.setTimestamp(c, new Timestamp(((Date)o).getTime()));
	                    }
	                    else {
	                        pstmt.setObject(c, o);
	                    }
	                }
	                if (rows > 0) {
	                    pstmt.setMaxRows(start + rows);
	                }
	                rs = pstmt.executeQuery();
	                queryTime = System.currentTimeMillis();
	                while(rs.next()) {
	                    if (currentRow >= start && (rows < 0 || currentRow < (start + rows))) {
	                    	DigestContentDataObject dcdo= new DigestContentDataObject();
	                        dcdo.setDigestContentId(rs.getString("digestContentId"));
	                        dcdo.setDigestId(rs.getString("digestId"));
	                        dcdo.setContentId(rs.getString("contentId"));
	                        dcdo.setName(rs.getString("name"));
	                        dcdo.setDateCreate(rs.getTimestamp("dateCreate"));
	                        dcdo.setOrdering(rs.getString("ordering"));	                        
	                        /*dcdo.setSource(rs.getString("source"));
	                        dcdo.setSourceDate(rs.getTimestamp("sourceDate"));*/
	                        dcdo.setSummary(rs.getString("summary"));
	                        
	                        /*dcdo.setLocalSource(rs.getString("localSource"));
	                        dcdo.setForeignSource(rs.getString("foreignSource"));*/
	                        dcdo.setAuthor(rs.getString("author"));
	                        
	                        dcdo.setFilename(rs.getString("filename"));
	                        
	                        results.add(dcdo);
	                    }
	                    currentRow++;
	                }
	                return results;
	            }
	            catch (SQLException e) {
	                throw e;
	            }
	            finally {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                long endTime = System.currentTimeMillis();
	                Log.getLog(getClass()).debug("selectByCriteria [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + sql);
	            }

	        }
	        catch(Exception e) {
	            throw new DaoException(e.toString());
	        }
	    }
	    
	    public int selectDigestContentsCount(String digestId, String name) throws DaoException {

	            Collection paramList = new ArrayList();
	            String columns = "COUNT(DISTINCT cds.contentId) AS total ";

	            String sql =
	                    "SELECT " +
	                    columns +
	                    "FROM cms_digest_content cds " +
	                    "LEFT JOIN cms_content_status s ON cds.contentId=s.id " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "WHERE cds.digestId=? ";	           
	            paramList.add(digestId);
	            if (name != null && !"".equals(name)) {
                    sql+=" AND (name LIKE ?)";
                    paramList.add("%" + name + "%");
	            }
	            
	            Map row = (Map)super.select(sql, HashMap.class, paramList.toArray(), 0, 1).iterator().next();
	            return Integer.parseInt(row.get("total").toString());
	    }
	    
	    public Collection selectDigestContents(String digestId, String contentId) throws DaoException {
	    	return super.select(
                    "SELECT digestContentId, contentId, digestId, ordering, dateCreate FROM " +
                    "cms_digest_content WHERE digestId=? AND contentId=?", DigestContentDataObject.class, new Object[]{digestId, contentId }, 0, -1);	   
	    }
	    
	    public Collection selectDigestContentByPermission(String[] classes,String[] principalIds, String permissionId, String name, Date fromDate, Date toDate,  String sort, boolean desc, int start, int rows, String digestId) throws DaoException {
	        try {
	            Collection paramList = new ArrayList();
	            /*String columns =
	                    "cds.contentId, cds.digestId, cds.digestContentId, cds.dateCreate, s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, cds.ordering, template, publishVersion, status, source, syncDest, syncContent, localSource, foreignSource, sourceDate," +
	                    "className, w.version, name, description, summary, author, date, related ";*/
                String columns = "cds.contentId, cds.digestId, cds.digestContentId, cds.dateCreate, s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, cds.ordering, template, publishVersion, className, w.version, name, description, summary, author, date, related ";
	            StringBuffer sql = new StringBuffer(
	                    "SELECT " +
	                    columns +
	                    "FROM cms_digest_content cds " +
	                    "LEFT JOIN cms_content_status s ON cds.contentId=s.id " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "WHERE s.id IN (" +
	                    "SELECT DISTINCT s.id " +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "LEFT JOIN cms_content_role_principal rp1 ON rp1.objectId = s.aclId " +
	                    "LEFT JOIN cms_content_role_permission rp2 ON rp1.role = rp2.role " +
	                    "WHERE cds.digestId=?");

	            paramList.add(digestId);

	            if (name != null && !"".equals(name)) {
	                //sql.append(" AND name LIKE ?");
	                //paramList.add("%" + name + "%");

                    sql.append(" AND (name LIKE ?)");
                    paramList.add("%" + name + "%");
	            }
	            if (classes != null && classes.length > 0) {
	                sql.append(" AND className IN (");
	                for (int i=0; i<classes.length; i++) {
	                    if (i > 0)
	                        sql.append(",");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(classes));
	            }
	            if (principalIds != null && principalIds.length > 0) {
	                sql.append(" AND principalId IN (");
	                for (int i=0; i<principalIds.length; i++) {
	                    if (i > 0)
	                        sql.append(", ");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(principalIds));
	            }

	            if (permissionId != null) {
	                sql.append(" AND permissionId=?");
	                paramList.add(permissionId);
	            }	          

	            if (fromDate != null) {
	                sql.append(" AND date>=?");
	                paramList.add(fromDate);
	            }

	            if (toDate != null) {
	                sql.append(" AND date<=?");
	                paramList.add(toDate);
	            }

	           

				sql.append(")");

	            if (sort != null && sort.trim().length() > 0) {
	                sql.append(" ORDER BY ");
					/*if(sort.equals("date"))
						sql.append("\"" + sort.toUpperCase() + "\"");
					else
	                	sql.append(sort);*/
	                if (desc)
	                    sql.append(" DESC");
	            }
	            else {
	                sql.append(" ORDER BY cds.ordering DESC, cds.dateCreate DESC");
	            }

	            // custom JDBC for performance
	            long startTime = System.currentTimeMillis();
	            long queryTime = startTime;
	            Collection results = new ArrayList();
	            Connection con = null;
	            PreparedStatement pstmt = null;
	            ResultSet rs = null;
	            int currentRow = 0;

	            try {
	                con = getDataSource().getConnection();
	                pstmt = con.prepareStatement(sql.toString());
	                int c=1;
	                Object o;
	                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
	                    o = i.next();
	                    if (o instanceof Date) {
	                        pstmt.setTimestamp(c, new Timestamp(((Date)o).getTime()));
	                    }
	                    else {
	                        pstmt.setObject(c, o);
	                    }
	                }
	                if (rows > 0) {
	                    pstmt.setMaxRows(start + rows);
	                }
	                rs = pstmt.executeQuery();
	                queryTime = System.currentTimeMillis();
	                while(rs.next()) {
	                    if (currentRow >= start && (rows < 0 || currentRow < (start + rows))) {
	                    	DigestContentDataObject dcdo= new DigestContentDataObject();
	                        dcdo.setDigestContentId(rs.getString("digestContentId"));
	                        dcdo.setDigestId(rs.getString("digestId"));
	                        dcdo.setContentId(rs.getString("contentId"));
	                        dcdo.setName(rs.getString("name"));
	                        dcdo.setDateCreate(rs.getTimestamp("dateCreate"));
	                        dcdo.setOrdering(rs.getString("ordering"));	                        
	                        /*dcdo.setSource(rs.getString("source"));
	                        dcdo.setSourceDate(rs.getTimestamp("sourceDate"));*/
	                        dcdo.setSummary(rs.getString("summary"));
	                        /*dcdo.setLocalSource(rs.getString("localSource"));
	                        dcdo.setForeignSource(rs.getString("foreignSource"));*/
	                        dcdo.setAuthor(rs.getString("author"));
	                        results.add(dcdo);
	                    }
	                    currentRow++;
	                }
	                return results;
	            }
	            catch (SQLException e) {
	                throw e;
	            }
	            finally {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                long endTime = System.currentTimeMillis();
	                Log.getLog(getClass()).debug("selectByPermission [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + sql);
	            }
	        }
	        catch(Exception e) {
	            throw new DaoException(e.toString());
	        }
	    }
	    
	    public int selectDigestContentByPermissionCount(String[] classes,String[] principalIds, String permissionId, String name, Date fromDate, Date toDate,  String digestId) throws DaoException {
	        try {
	            Collection paramList = new ArrayList();
	            String columns = 
	                    "COUNT(DISTINCT cds.contentId) AS total ";
	            StringBuffer sql = new StringBuffer(
	                    "SELECT " +
	                    columns +
	                    "FROM cms_digest_content cds " +
	                    "LEFT JOIN cms_content_status s ON cds.contentId=s.id " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "WHERE s.id IN (" +
	                    "SELECT DISTINCT s.id " +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "LEFT JOIN cms_content_role_principal rp1 ON rp1.objectId = s.aclId " +
	                    "LEFT JOIN cms_content_role_permission rp2 ON rp1.role = rp2.role " +
	                    "WHERE cds.digestId=?");

	            paramList.add(digestId);

	            if (name != null && !"".equals(name)) {
	                //sql.append(" AND name LIKE ?");
	                //paramList.add("%" + name + "%");
                    sql.append(" AND (name LIKE ?)");
                    paramList.add("%" + name + "%");
	            }
	            if (classes != null && classes.length > 0) {
	                sql.append(" AND className IN (");
	                for (int i=0; i<classes.length; i++) {
	                    if (i > 0)
	                        sql.append(",");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(classes));
	            }
	            if (principalIds != null && principalIds.length > 0) {
	                sql.append(" AND principalId IN (");
	                for (int i=0; i<principalIds.length; i++) {
	                    if (i > 0)
	                        sql.append(", ");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(principalIds));
	            }

	            if (permissionId != null) {
	                sql.append(" AND permissionId=?");
	                paramList.add(permissionId);
	            }	          

	            if (fromDate != null) {
	                sql.append(" AND date>=?");
	                paramList.add(fromDate);
	            }

	            if (toDate != null) {
	                sql.append(" AND date<=?");
	                paramList.add(toDate);
	            }

	           

				sql.append(")");
				Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
		        return Integer.parseInt(row.get("total").toString());
	            
	        }
	        catch(Exception e) {
	            throw new DaoException(e.toString());
	        }
	    }
	    
	    public Collection selectContentByPermission(String[] classes,String[] principalIds, String permissionId, String name, Date fromDate, Date toDate,  String sort, boolean desc, int start, int rows) throws DaoException {
	        try {
	            Collection paramList = new ArrayList();
	            /*String columns =
	                    "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, status, source, syncDest, syncContent, localSource, foreignSource, sourceDate," +
	                    "className, w.version, name, description, summary, author, date, related ";*/
                String columns = "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, className, w.version, name, description, summary, author, date, related ";
	            StringBuffer sql = new StringBuffer(
	                    "SELECT " +
	                    columns +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "WHERE s.id IN (" +
	                    "SELECT DISTINCT s.id " +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "LEFT JOIN cms_content_role_principal rp1 ON rp1.objectId = s.aclId " +
	                    "LEFT JOIN cms_content_role_permission rp2 ON rp1.role = rp2.role " +
	                    "WHERE 1=1");

	            

	            if (name != null && !"".equals(name)) {
	                //sql.append(" AND name LIKE ?");
	                //paramList.add("%" + name + "%");
                    sql.append(" AND (name LIKE ?)");
                    paramList.add("%" + name + "%");
	            }
	            if (classes != null && classes.length > 0) {
	                sql.append(" AND className IN (");
	                for (int i=0; i<classes.length; i++) {
	                    if (i > 0)
	                        sql.append(",");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(classes));
	            }
	            if (principalIds != null && principalIds.length > 0) {
	                sql.append(" AND principalId IN (");
	                for (int i=0; i<principalIds.length; i++) {
	                    if (i > 0)
	                        sql.append(", ");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(principalIds));
	            }

	            if (permissionId != null) {
	                sql.append(" AND permissionId=?");
	                paramList.add(permissionId);
	            }	          

	            if (fromDate != null) {
	                sql.append(" AND date>=?");
	                paramList.add(fromDate);
	            }

	            if (toDate != null) {
	                sql.append(" AND date<=?");
	                paramList.add(toDate);
	            }

	           

				sql.append(")");

	            if (sort != null && sort.trim().length() > 0) {
	                sql.append(" ORDER BY ");
					/*if(sort.equals("date"))
						sql.append("\"" + sort.toUpperCase() + "\"");
					else
	                	sql.append(sort);*/
	                if (desc)
	                    sql.append(" DESC");
	            }
	            else {
	                sql.append(" ORDER BY ordering DESC, date DESC");
	            }

	            // custom JDBC for performance
	            long startTime = System.currentTimeMillis();
	            long queryTime = startTime;
	            Collection results = new ArrayList();
	            Connection con = null;
	            PreparedStatement pstmt = null;
	            ResultSet rs = null;
	            int currentRow = 0;

	            try {
	                con = getDataSource().getConnection();
	                pstmt = con.prepareStatement(sql.toString());
	                int c=1;
	                Object o;
	                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
	                    o = i.next();
	                    if (o instanceof Date) {
	                        pstmt.setTimestamp(c, new Timestamp(((Date)o).getTime()));
	                    }
	                    else {
	                        pstmt.setObject(c, o);
	                    }
	                }
	                if (rows > 0) {
	                    pstmt.setMaxRows(start + rows);
	                }
	                rs = pstmt.executeQuery();
	                queryTime = System.currentTimeMillis();
	                while(rs.next()) {
	                    if (currentRow >= start && (rows < 0 || currentRow < (start + rows))) {
	                        ContentObject co = new DefaultContentObject();
	                        co.setId(rs.getString("id"));
	                        co.setNew(rs.getInt("new") == 1);
	                        co.setModified(rs.getInt("modified") == 1);
	                        co.setDeleted(rs.getInt("deleted") == 1);
	                        co.setArchived(rs.getInt("archived") == 1);
	                        co.setPublished(rs.getInt("published") == 1);
	                        co.setApproved(rs.getInt("approved") == 1);
	                        co.setSubmitted(rs.getInt("submitted") == 1);
	                        co.setCheckedOut(rs.getInt("checkedOut") == 1);
	                        co.setAclId(rs.getString("aclId"));
	                        co.setParentId(rs.getString("parentId"));
	                        co.setOrdering(rs.getString("ordering"));
	                        co.setTemplate(rs.getString("template"));
	                        co.setPublishVersion(rs.getString("publishVersion"));
	                        co.setClassName(rs.getString("className"));
	                        co.setVersion(rs.getString("version"));
	                        co.setName(rs.getString("name"));
	                        co.setDescription(rs.getString("description"));
	                        co.setSummary(rs.getString("summary"));
	                        co.setAuthor(rs.getString("author"));
	                        co.setDate(rs.getTimestamp("date"));
	                        co.setRelated(rs.getString("related"));
	                        results.add(co);
	                    }
	                    currentRow++;
	                }
	                return results;
	            }
	            catch (SQLException e) {
	                throw e;
	            }
	            finally {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                long endTime = System.currentTimeMillis();
	                Log.getLog(getClass()).debug("selectByPermission [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + sql);
	            }
	        }
	        catch(Exception e) {
	            throw new DaoException(e.toString());
	        }
	    }
	    
	    public int selectContentByPermissionCount(String[] classes,String[] principalIds, String permissionId, String name, Date fromDate, Date toDate) throws DaoException {
	        try {
	            Collection paramList = new ArrayList();
	            String columns = 
	                    "COUNT(DISTINCT s.id) AS total ";
	            StringBuffer sql = new StringBuffer(
	                    "SELECT " +
	                    columns +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "WHERE s.id IN (" +
	                    "SELECT DISTINCT s.id " +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "LEFT JOIN cms_content_role_principal rp1 ON rp1.objectId = s.aclId " +
	                    "LEFT JOIN cms_content_role_permission rp2 ON rp1.role = rp2.role " +
	                    "WHERE 1=1");

	            if (name != null && !"".equals(name)) {
	                //sql.append(" AND name LIKE ?");
	                //paramList.add("%" + name + "%");
                    sql.append(" AND (name LIKE ?)");
                    paramList.add("%" + name + "%");
	            }
	            if (classes != null && classes.length > 0) {
	                sql.append(" AND className IN (");
	                for (int i=0; i<classes.length; i++) {
	                    if (i > 0)
	                        sql.append(",");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(classes));
	            }
	            if (principalIds != null && principalIds.length > 0) {
	                sql.append(" AND principalId IN (");
	                for (int i=0; i<principalIds.length; i++) {
	                    if (i > 0)
	                        sql.append(", ");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(principalIds));
	            }

	            if (permissionId != null) {
	                sql.append(" AND permissionId=?");
	                paramList.add(permissionId);
	            }	          

	            if (fromDate != null) {
	                sql.append(" AND date>=?");
	                paramList.add(fromDate);
	            }

	            if (toDate != null) {
	                sql.append(" AND date<=?");
	                paramList.add(toDate);
	            }

	           

				sql.append(")");

				Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
		        return Integer.parseInt(row.get("total").toString());
	        }
	        catch(Exception e) {
	            throw new DaoException(e.toString());
	        }
	    }
	    
	    public Collection selectContentByCriteria(String[] classes, String name, Date fromDate, Date toDate,  String sort, boolean desc, int start, int rows) throws DaoException {
	        try {
	            Collection paramList = new ArrayList();
	            /*String columns = "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, status, source, syncDest, syncContent, localSource, foreignSource, sourceDate," +
	                    "className, w.version, name, description,  author, date, related ";*/
                String columns = "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, status, className, w.version, name, description,  author, date, related ";
	            StringBuffer sql = new StringBuffer(
	                    "SELECT " +
	                    columns +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "WHERE 1=1");	           

	            if (name != null && !"".equals(name)) {
	                //sql.append(" AND name LIKE ?");
	                //paramList.add("%" + name + "%");
                    sql.append(" AND (name LIKE ?)");
                    paramList.add("%" + name + "%");
	            }
	            
	            if (classes != null && classes.length > 0) {
	                sql.append(" AND className IN (");
	                for (int i=0; i<classes.length; i++) {
	                    if (i > 0)
	                        sql.append(",");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(classes));
	            }
	            
	            if (fromDate != null) {
	                sql.append(" AND (date >= ?)");
	                paramList.add(fromDate);
	            }

	            if (toDate != null) {
	                sql.append(" AND (date <= ?)");
	                paramList.add(toDate);
	            }
	            
	            if (sort != null && sort.trim().length() > 0) {
	                sql.append(" ORDER BY ");
					if(sort.equals("date"))
						sql.append("date");
					else
	                	sql.append(sort);
	                if (desc)
	                    sql.append(" DESC");
	            }
	            else {
	                sql.append(" ORDER BY ordering DESC, date DESC");
	            }

	            // custom JDBC for performance
	            long startTime = System.currentTimeMillis();
	            long queryTime = startTime;
	            Collection results = new ArrayList();
	            Connection con = null;
	            PreparedStatement pstmt = null;
	            ResultSet rs = null;
	            int currentRow = 0;

	            try {
	                con = getDataSource().getConnection();
	                pstmt = con.prepareStatement(sql.toString());
	                int c=1;
	                Object o;
	                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
	                    o = i.next();
	                    if (o instanceof Date) {
	                        pstmt.setTimestamp(c, new Timestamp(((Date)o).getTime()));
	                    }
	                    else {
	                        pstmt.setObject(c, o);
	                    }
	                }
	                if (rows > 0) {
	                    pstmt.setMaxRows(start + rows);
	                }
	                rs = pstmt.executeQuery();
	                queryTime = System.currentTimeMillis();
	                while(rs.next()) {
	                    if (currentRow >= start && (rows < 0 || currentRow < (start + rows))) {
	                        ContentObject co = new DefaultContentObject();
	                        co.setId(rs.getString("id"));
	                        co.setNew(rs.getInt("new") == 1);
	                        co.setModified(rs.getInt("modified") == 1);
	                        co.setDeleted(rs.getInt("deleted") == 1);
	                        co.setArchived(rs.getInt("archived") == 1);
	                        co.setPublished(rs.getInt("published") == 1);
	                        co.setApproved(rs.getInt("approved") == 1);
	                        co.setSubmitted(rs.getInt("submitted") == 1);
	                        co.setCheckedOut(rs.getInt("checkedOut") == 1);
	                        co.setAclId(rs.getString("aclId"));
	                        co.setParentId(rs.getString("parentId"));
	                        co.setOrdering(rs.getString("ordering"));
	                        co.setTemplate(rs.getString("template"));
	                        co.setPublishVersion(rs.getString("publishVersion"));
	                        co.setClassName(rs.getString("className"));
	                        co.setVersion(rs.getString("version"));
	                        co.setName(rs.getString("name"));
	                        co.setDescription(rs.getString("description"));

	                        co.setAuthor(rs.getString("author"));
	                        co.setDate(rs.getTimestamp("date"));
	                        co.setRelated(rs.getString("related"));                      
	                        results.add(co);
	                    }
	                    currentRow++;
	                }
	                return results;
	            }
	            catch (SQLException e) {
	                throw e;
	            }
	            finally {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                long endTime = System.currentTimeMillis();
	                Log.getLog(getClass()).debug("selectByCriteria [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + sql);
	            }

	        }
	        catch(Exception e) {
	            throw new DaoException(e.toString());
	        }
	    }
	    
	    public int selectContentByCriteriaCount(String[] classes, String name, Date fromDate, Date toDate) throws DaoException {
	        try {
	            Collection paramList = new ArrayList();
	            String columns = "COUNT(DISTINCT s.id) AS total ";

	            StringBuffer sql = new StringBuffer(
	                    "SELECT " +
	                    columns +
	                    "FROM cms_content_status s " +
	                    "LEFT JOIN cms_content_work w ON s.id=w.id " +
	                    "WHERE 1=1");	           

	            if (name != null && !"".equals(name)) {
	                //sql.append(" AND name LIKE ?");
	                //paramList.add("%" + name + "%");
                    sql.append(" AND (name LIKE ?)");
                    paramList.add("%" + name + "%");
	            }
	            
	            if (classes != null && classes.length > 0) {
	                sql.append(" AND className IN (");
	                for (int i=0; i<classes.length; i++) {
	                    if (i > 0)
	                        sql.append(",");
	                    sql.append("?");
	                }
	                sql.append(")");
	                paramList.addAll(Arrays.asList(classes));
	            }
	            
	            if (fromDate != null) {
	                sql.append(" AND (date >= ?)");
	                paramList.add(fromDate);
	            }

	            if (toDate != null) {
	                sql.append(" AND (date <= ?)");
	                paramList.add(toDate);
	            }
	            

	            Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
		        return Integer.parseInt(row.get("total").toString());	            
	        }
	        catch(Exception e) {
	            throw new DaoException(e.toString());
	        }
	    }



}
