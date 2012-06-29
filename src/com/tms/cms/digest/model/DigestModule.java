package com.tms.cms.digest.model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import org.apache.commons.lang.StringUtils;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentManagerDao;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.DefaultContentObject;
import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.MailUtil;


public class DigestModule extends DefaultModule {
    private Log log = Log.getLog(getClass());

//-- Initialization
    public void init() {
    	
    }
    
    public DigestIssueDataObject getDigestIssueDo(String digestIssueId) throws DigestException
    {
    	DigestIssueDataObject digest = null;
        DigestDao dao = (DigestDao)getDao();
        try
        {
        	digest = dao.getDigestIssueDo(digestIssueId);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
        return digest;
    }
    
    public DigestDataObject getDigestDo(String digestId) throws DigestException
    {
    	DigestDataObject digest = null;
        DigestDao dao = (DigestDao)getDao();
        try
        {
        	digest = dao.getDigestDo(digestId);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
        return digest;
    }
    
    public Collection getDigestIssue(String searchCriteria, String sort, boolean desc, int start, int rows, boolean includeCount) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigestIssue(searchCriteria, sort, desc, start, rows);    
            if(includeCount){
            	 for (Iterator i=digest.iterator(); i.hasNext();)
                 {
            		 DigestIssueDataObject dido = (DigestIssueDataObject)i.next();
                     dido.setNumOfDigest(getNumOfDigestByDigestIssue(dido.getDigestIssueId()));
                 }
            }
            
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getSetupDigestIssue(String searchCriteria, String sort, boolean desc, int start, int rows) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getSetupDigestIssue(searchCriteria, sort, desc, start, rows);                
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getSetupDigest(String searchCriteria, String sort, boolean desc, int start, int rows) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getSetupDigest(searchCriteria, sort, desc, start, rows);                
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public int getNumOfDigestIssue(String searchCriteria) throws DigestException
    {
        try
        {
           
        	DigestDao dao = (DigestDao)getDao();
            return dao.getNumOfDigestIssue(searchCriteria);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }
    
    public int getNumOfSetupDigestIssue(String searchCriteria) throws DigestException
    {
        try
        {
           
        	DigestDao dao = (DigestDao)getDao();
            return dao.getNumOfSetupDigestIssue(searchCriteria);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }
    
    public int getNumOfSetupDigest(String searchCriteria) throws DigestException
    {
        try
        {
           
        	DigestDao dao = (DigestDao)getDao();
            return dao.getNumOfSetupDigest(searchCriteria);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }
    
    public int getNumOfDigestByDigestIssue(String digestIssueId) throws DigestException
    {
        try
        {
           
        	DigestDao dao = (DigestDao)getDao();
            return dao.getNumOfDigestByDigestIssue(digestIssueId);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }
    
    public int getNumOfContentByDigest(String digestId) throws DigestException
    {
        try
        {
           
        	DigestDao dao = (DigestDao)getDao();
            return dao.getNumOfContentByDigest(digestId);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }    
    
    public void deleteDigestIssue(String digestIssueId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.deleteDigestIssue(digestIssueId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    public void deleteSetupDigestIssue(String digestIssueId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.deleteSetupDigestIssue(digestIssueId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    
    public void deleteSetupDigest(String digestId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.deleteSetupDigest(digestId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    
    public Collection getDigestIssue(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigestIssue(query);                           
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getDigest(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigest(query);                           
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getSetupDigestIssue(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getSetupDigestIssue(query);                           
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getDigestIssueMain(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigestIssueMain(query);                           
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public void insertDigestIssue(DigestIssueDataObject dido, boolean isNew) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
           if(isNew){
        	   dao.insertDigestIssueList(dido); 
           }
           dao.insertDigestIssue(dido);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to insert New Digest Issue: " + e.getMessage());
        }
    }
    
    public void insertSetupDigestIssue(DigestIssueDataObject dido) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	   dao.insertDigestIssueList(dido);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to insert New Digest Issue: " + e.getMessage());
        }
    }
    
    public void insertSetupDigest(DigestDataObject ddo) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	   dao.insertDigestList(ddo);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to insert New Digest: " + e.getMessage());
        }
    }
    
    public void updateSetupDigestIssue(DigestIssueDataObject dido) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	   dao.updateSetupDigestIssue(dido);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update Digest Issue: " + e.getMessage());
        }
    }
    
    public void updateSetupDigest(DigestDataObject ddo) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	   dao.updateSetupDigest(ddo);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update Digest: " + e.getMessage());
        }
    }
       
    public Collection getDigest(String digestIssueId, String searchCriteria, String sort, boolean desc, int start, int rows, boolean includeCount) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigest(digestIssueId, searchCriteria, sort, desc, start, rows);    
            if(includeCount){
            	 for (Iterator i=digest.iterator(); i.hasNext();)
                 {
            		 DigestDataObject ddo = (DigestDataObject)i.next();
                     ddo.setNumOfContents(getNumOfContentByDigest(ddo.getDigestId()));
                 }
            }            
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public int getNumOfDigest(String digestIssueId, String searchCriteria) throws DigestException
    {
        try
        {           
        	DigestDao dao = (DigestDao)getDao();
            return dao.getNumOfDigest(digestIssueId, searchCriteria);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }
    
    public int getNumOfContentsByDigest(String digestId) throws DigestException
    {
        try
        {         
        	DigestDao dao = (DigestDao)getDao();
            return 0;
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }
    
    public void deleteDigest(String digestId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.deleteDigest(digestId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    
    public Collection getSetupDigest(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getSetupDigest(query);                           
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getDigestMain(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigestMain(query);                           
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public void insertDigest(DigestDataObject dido, boolean isNew) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
           if(isNew){
        	   dao.insertDigestList(dido); 
           }
           dao.insertDigest(dido);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to insert New Digest: " + e.getMessage());
        }
    }

    public Collection getRecipients(String searchCriteria, String sort, boolean desc, int start, int rows) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getRecipients(searchCriteria, sort, desc, start, rows);    
            
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getRecipients(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getRecipients(query);    
            
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public int getNumOfRecipients(String searchCriteria) throws DigestException
    {
        try
        {
           
        	DigestDao dao = (DigestDao)getDao();
            return dao.getNumOfRecipients(searchCriteria);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
    }
    
    public void deleteRecipients(String recipientsId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.deleteRecipients(recipientsId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    
    public void insertRecipients(RecipientDataObject rdo, String [] userIds) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.insertRecipients(rdo,userIds);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    
    public void updateRecipients(RecipientDataObject rdo, String [] userIds) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.updateRecipients(rdo,userIds);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    
    public Collection getDigestUser(String recipientId) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigestUser(recipientId);    
            
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getMailingDigestUser(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getMailingDigestUser(query);    
            
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public Collection getDigestRecipients(DaoQuery query) throws DigestException
    {
        Collection digest = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            digest = dao.getDigestRecipients(query);    
            
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return digest;
    }
    
    public RecipientDataObject getDigestRecipientsDo(String recipientId) throws DigestException
    {
    	RecipientDataObject rdo = null;
        DigestDao dao = (DigestDao)getDao();
        try
        {
        	rdo = dao.getDigestRecipientsDo(recipientId);
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }
        return rdo;
    }
    
    public void insertDigestContent(DigestContentDataObject dcdo) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {

           dao.insertDigestContent(dcdo);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to insert New Digest Content: " + e.getMessage());
        }
    }
    
    public void updateAll(String userId, String digestId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {

           dao.updateAll(userId, digestId);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update All: " + e.getMessage());
        }
    }
    public void updateDigestIssue(String userId, String digestIssueId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {

           dao.updateDigestIssue(userId, digestIssueId);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update Digest Issue: " + e.getMessage());
        }
    }
    public void deleteMailingList(String nailingListId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.deleteMailingList(nailingListId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    public void deleteDigestContent(String contentId) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
    	try {
			dao.deleteDigestContent(contentId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
			e.printStackTrace();
		}
    }
    
    public void reorder(String digestId, String[] childKeys, User user) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	if (childKeys == null) {
                return;
            }
        	int order=0;
        	boolean sort=true;
            for (int i = 0; i < childKeys.length; i++) {
            	if("-Order-".equals(childKeys[i])){
            		sort=false;     
            	}else{
                if(sort){
            	order=(childKeys.length-i)*10;
                }else{
                	order=-1;
                }
            	
            	dao.reorderDigestContent(childKeys[i],order);
            	}
            }
           updateAll(user.getId(), digestId);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update All: " + e.getMessage());
        }
    }
    
    public void reorderAlphabetically(String digestId, Collection child, User user) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	if (child.size() == 0) {
                return;
            }
        	int order=0;
        	int count=0;
        	for (Iterator i=child.iterator(); i.hasNext();)
            {
        		DigestContentDataObject dcdo = (DigestContentDataObject)i.next();  
        		count=count+1;
            	order=(count)*10;
               
            	dao.reorderDigestContent(dcdo.getDigestContentId(),order);
                
            }
           updateAll(user.getId(), digestId);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update All: " + e.getMessage());
        }
    }
    
    public void reorderDigest(String digestIssueId, String[] childKeys, User user) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	if (childKeys == null) {
                return;
            }
        	int order=0;
        	boolean sort=true;
            for (int i = 0; i < childKeys.length; i++) {
            	if("-Order-".equals(childKeys[i])){
            		sort=false;     
            	}else{
                if(sort){
            	order=(childKeys.length-i)*10;
                }else{
                	order=-1;
                }
            	
            	dao.reorderDigest(childKeys[i],order);
            	}
            }
            updateDigestIssue(user.getId(), digestIssueId);        
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update All: " + e.getMessage());
        }
    }
    
    public void reorderDigestAlphabetically(String digestIssueId, Collection child, User user) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
        	if (child.size() == 0) {
                return;
            }
        	int order=0;
        	int count=0;
        	for (Iterator i=child.iterator(); i.hasNext();)
            {
        		DigestDataObject ddo = (DigestDataObject)i.next();  
        		count=count+1;
            	order=(count)*10;
               
            	dao.reorderDigest(ddo.getDigestId(),order);
                
            }
        	updateDigestIssue(user.getId(), digestIssueId);         
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to update All: " + e.getMessage());
        }
    }
    
    public void insertMailingList(MailingListDataObject mldo) throws DigestException
    {
    	DigestDao dao = (DigestDao)getDao();
        try
        {
           dao.insertMailingList(mldo);       
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException("Unable to insert New Mailing List: " + e.getMessage());
        }
    }
    
    public Collection getMailingList(String searchCriteria, String sort, boolean desc, int start, int rows, boolean includeCount) throws DigestException
    {
        Collection mail = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            mail = dao.getMailingList(searchCriteria, sort, desc, start, rows);
            for (Iterator i=mail.iterator(); i.hasNext();)
            {
            	MailingListDataObject mldo = (MailingListDataObject)i.next();
            	mldo.setLastSendDate(dao.getMailingListLastEditDate(mldo.getMailingListId()));
            }
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return mail;
    }
    
    public int getMailingListCount(String searchCriteria) throws DigestException
    {   
    	DigestDao dao = (DigestDao)getDao();
        try {
			return dao.getMailingListCount(searchCriteria);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
    }
    
    public Collection getMailingList(DaoQuery query) throws DigestException
    {
        Collection mail = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            mail = dao.getMailingList(query);               
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return mail;
    }
    
    public Collection getMailingRecipients(DaoQuery query) throws DigestException
    {
        Collection mail = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            mail = dao.getMailingRecipients(query);               
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return mail;
    }
    
    public void sendDigestIssueList(MailingListDataObject mldo) throws DigestException
    {
    	ContentObject mailHeaderSpot=null;
    	try {
            String key = Application.getInstance().getProperty("digest.mail.headerSpotId");
    		User user = Application.getInstance().getCurrentUser();
            ContentPublisher cm = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
            mailHeaderSpot = cm.view(key, user);
        }
        catch(Exception e) {
        	//cannot found the spot
        }
        
    	DigestDao dao = (DigestDao)getDao();
    	String serverUrl = "http://";
        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        try
        {
            serverUrl = setup.get("siteUrl");
        }
        catch (SetupException e)
        {
            Log.getLog(DigestModule.class).error(e);
        }
    	Collection userId=new ArrayList();
    	Collection users=new ArrayList();
    	try{
    	StringBuffer mail = new StringBuffer();
    	mail.append("\n");
    	mail.append("<table border=1 cellpadding=0 cellspacing=0 width=\"100%\">");
    	mail.append("<tr>"+
        		"<td >"+((mailHeaderSpot!=null)?mailHeaderSpot.getSummary():"[No content found]")+"</td></tr>");

        mail.append("<tr><td colspan=2><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">");
        mail.append("<tr><td width=\"1%\" align=\"right\" bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">No.</font></td><td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.digest")+"</font></td></tr>");

        int no=1;
        for (Iterator i = mldo.getDigest().iterator(); i.hasNext();)
        {
        	DigestDataObject ddo = (DigestDataObject) i.next();
        	String color="#ffffff";
    		if(no%2==0)
    			color="F3F3F3";
    		else{
    			color="#ffffff";
    		}
        	mail.append("<tr><td width=\"1%\" align=\"right\" bgcolor=\""+color+"\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+no+"</font></td><td bgcolor=\""+color+"\"><a target=\"login\" href=\""+serverUrl+"/ekms/digest/digestContentList.jsp?digestId="+ddo.getDigestId()+"\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+ddo.getDigestName()+"</font></a></td></tr>");
        	no+=1;
        }
        mail.append("</table>");
        mail.append("</td></tr>");
        mail.append("</table>");
        String formattedMail = StringUtils.replace(mail.toString(), "\n", "<br>");
        for (Iterator i = mldo.getRecipients().iterator(); i.hasNext();)
        {
        	RecipientDataObject rdo = (RecipientDataObject) i.next();
        	DaoQuery query=new DaoQuery();
        	query.addProperty(new OperatorEquals("cms_mailing_recipients.recipientId", rdo.getRecipientId(), DaoOperator.OPERATOR_AND));
        	rdo.setUser(getMailingDigestUser(query));
        	for (Iterator j = rdo.getUser().iterator(); j.hasNext();)
            {
        		RecipientDataObject jdo = (RecipientDataObject) j.next();
        	userId.add(jdo.getUserRecipientId());
            }
        }        
        if(userId.size() > 0)
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", userId.toArray(), DaoOperator.OPERATOR_AND));
            users = service.getUsers(query, 0, -1, "username", false);
        }
        for(Iterator i = users.iterator(); i.hasNext();)
        {
            User subscriber = (User) i.next();
            if(!(subscriber.getProperty("email1") == null || "".equals(subscriber.getProperty("email1")))) {
                String email = (String)subscriber.getProperty("email1");
                MailUtil.sendEmail(null, true, null, email, null, null, mldo.getMailingListName()+" - "+mldo.getDigestIssueName()+" ("+Application.getInstance().getMessage("digest.email.issueList")+")", formattedMail);
            }
        }
        dao.insertReport(mldo);
    	}catch(Exception e){
    		throw new DigestException(e.toString(), e);
    	}
    }
    
    public void sendLocalNewsList(MailingListDataObject mldo) throws DigestException
    {
    	ContentObject mailHeaderSpot=null;
    	try {
            String key = Application.getInstance().getProperty("digest.mail.headerSpotId");
    		User user = Application.getInstance().getCurrentUser();
            ContentPublisher cm = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
            mailHeaderSpot = cm.view(key, user);
        }
        catch(Exception e) {
        	//cannot found the spot
        }
    	
    	DigestDao dao = (DigestDao)getDao();
    	String serverUrl = "http://";
        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        try
        {
            serverUrl = setup.get("siteUrl");
        }
        catch (SetupException e)
        {
            Log.getLog(DigestModule.class).error(e);
        }
    	Collection userId=new ArrayList();
    	Collection users=new ArrayList();
    	try{
    	StringBuffer mail = new StringBuffer();
    	mail.append("\n");
    	mail.append("<table border=1 cellpadding=0 cellspacing=0 width=\"100%\">");
    	mail.append("<tr>"+
        		"<td >"+((mailHeaderSpot!=null)?mailHeaderSpot.getSummary():"[No content found]")+"</td></tr>");

        mail.append("<tr><td colspan=2 bgcolor=\"#F3F3F3\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\"><tr><td bgcolor=\"#F3F3F3\">");
             
        for (Iterator i = mldo.getDigest().iterator(); i.hasNext();)
        {
        	DigestDataObject ddo = (DigestDataObject) i.next();
        	ddo.setContents(selectDigestContents(ddo.getDigestId(),null,null,true, 0, -1, false));
        	int no=1;
        	mail.append("<b><font color=\"#0000FF\" size=\"+1\">"+ddo.getDigestName()+"</font></b>\n\n");
        	for (Iterator j = ddo.getContents().iterator(); j.hasNext();)
            {
        		DigestContentDataObject dcdo = (DigestContentDataObject) j.next();
        		
        		if(dcdo.getContentId().startsWith("com.tms.cms.document.Document_")){
        			mail.append(no+". <font size=\"-1\"><b><a target=\"login\" href=\""+serverUrl+"/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\"><strong>"+dcdo.getName()+"</strong></a></b>\n");
        			//mail.append(no+". <font size=\"-1\"><b><a style=\"color:#668F20;\" target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\"><strong>"+dcdo.getName()+"</strong></a></b>\n");
        		}else{
        			mail.append(no+". <font size=\"-1\"><b><a target=\"login\" href=\""+serverUrl+"/ekms/content/content.jsp?id="+dcdo.getContentId()+"\"><strong>"+dcdo.getName()+"</strong></a></b>\n");
        			//mail.append(no+". <font size=\"-1\"><b><a style=\"color:#668F20;\" target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/ekms/content/content.jsp?id="+dcdo.getContentId()+"\"><strong>"+dcdo.getName()+"</strong></a></b>\n");
        		}
        		
        		String summary=dcdo.getSummary();
        		if(summary==null||"".equals(summary)){
        			summary="Abstract N/A.";	
        		}
        		mail.append(summary+"</font>\n\n");
        		no+=1;
            }
        }
        mail.append("</td></tr>");
        mail.append("</table></td></tr></table>");
        String formattedMail = StringUtils.replace(mail.toString(), "\n", "<br>");
        
        for (Iterator i = mldo.getRecipients().iterator(); i.hasNext();)
        {
        	RecipientDataObject rdo = (RecipientDataObject) i.next();
        	DaoQuery query=new DaoQuery();
        	query.addProperty(new OperatorEquals("cms_mailing_recipients.recipientId", rdo.getRecipientId(), DaoOperator.OPERATOR_AND));
        	rdo.setUser(getMailingDigestUser(query));
        	for (Iterator j = rdo.getUser().iterator(); j.hasNext();)
            {
        		RecipientDataObject jdo = (RecipientDataObject) j.next();
        		userId.add(jdo.getUserRecipientId());
            }
        }        
        if(userId.size() > 0)
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", userId.toArray(), DaoOperator.OPERATOR_AND));
            users = service.getUsers(query, 0, -1, "username", false);
        }
        for(Iterator i = users.iterator(); i.hasNext();)
        {
            User subscriber = (User) i.next();
            if(!(subscriber.getProperty("email1") == null || "".equals(subscriber.getProperty("email1")))) {
                String email = (String)subscriber.getProperty("email1");
                MailUtil.sendEmail(null, true, null, email, null, null, mldo.getMailingListName()+" - "+mldo.getDigestIssueName()+" ("+Application.getInstance().getMessage("digest.email.contenList")+")", formattedMail);
            }
        }
        dao.insertReport(mldo); 
    	}catch(Exception e){
    		throw new DigestException(e.toString(), e);
    	}
    }
    
    public Collection selectDigestContents(String[] classes,String name, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows, String permission, User user, String digestId) throws DigestException {
        try {
            DigestDao dao = (DigestDao) getDao();
            Collection results= new ArrayList();
            Collection nodes= new ArrayList();
            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(user.getId());
                String[] principalArray = (String[]) principalList.toArray(new String[0]);

                // retrieve list of objects
                results = dao.selectDigestContentByPermission(classes,principalArray, permission, name, fromDate, toDate, sort, desc, start, rows,digestId);
            }
            else {
                // retrieve list of objects
            	results = dao.selectDigestContents(digestId,name, sort, desc, start, rows);
               
            }
            TaxonomyModule tx = (TaxonomyModule) Application.getInstance().getModule(TaxonomyModule.class);
            
            if(results.size()>0){
            	nodes=tx.formTaxonomyTree(new Integer(1));
            	
            }
            for (Iterator i = results.iterator(); i.hasNext();) {
            	DigestContentDataObject co = (DigestContentDataObject) i.next();
                Map map=tx.getNodesWithMapping(co.getId());
                String nodeName="";
                if(map!=null || map.size() > 0){
                	co.setCompany("N/A");
                	co.setSector("N/A");
                	co.setCountry("N/A");
                for (Iterator j = nodes.iterator(); j.hasNext();) {
                	TaxonomyNode txnode = (TaxonomyNode) j.next();
               
                	if("0".equals(txnode.getParentId())){
                		if("Countries".equals(txnode.getName())||"Companies".equals(txnode.getName())||"Sectors".equals(txnode.getName())){
                			nodeName=txnode.getName();
                			co=recursiveTaxonomy(nodeName,co,map,txnode.getChildNodes());
                		}
                		}
                	}
      
                }else{
                	co.setCompany("N/A");
                	co.setSector("N/A");
                	co.setCountry("N/A");
                }
                
            }
            
            return results;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new DigestException(e.toString());
        }
    }

    
    public void sendDigestList(MailingListDataObject mldo) throws DigestException
    {
    	ContentObject mailHeaderSpot=null;
    	try {
            String key = Application.getInstance().getProperty("digest.mail.headerSpotId");
    		User user = Application.getInstance().getCurrentUser();
            ContentPublisher cm = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
            mailHeaderSpot = cm.view(key, user);
        }
        catch(Exception e) {
        	//cannot found the spot
        }
    	
    	DigestDao dao = (DigestDao)getDao();
    	ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);    	
    	SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy");
    	String serverUrl = "http://";
        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        try
        {
            serverUrl = setup.get("siteUrl");
        }
        catch (SetupException e)
        {
            Log.getLog(DigestModule.class).error(e);
        }
    	Collection userId=new ArrayList();
    	Collection users=new ArrayList();
    	try{
    	StringBuffer mail = new StringBuffer();
    	mail.append("\n");
    	mail.append("<table border=1 cellpadding=0 cellspacing=0 width=\"100%\">");
    	mail.append("<tr>"+
        		"<td >"+((mailHeaderSpot!=null)?mailHeaderSpot.getSummary():"[No content found]")+"</td></tr>");

        mail.append("<tr><td >");
        mail.append("<table cellpadding=5 cellspacing=1 width=\"100%\">");
        mail.append("<tr><td bgcolor=\"BBD5F2\" width=\"1%\" align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">No.</td>");
        mail.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.news")+"</font></td>");
        mail.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.sectors")+"</font></td>");
        mail.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.country")+"</font></td>");
        mail.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.company")+"</font></td>");
        mail.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.sourceByLine")+"</font></td>");
        //mail.append("<td bgcolor=\"F3F3F3\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.sourceDate")+"</font></td>");        
        mail.append("</tr>");
        
        int no=1;
        for (Iterator i = mldo.getDigest().iterator(); i.hasNext();)
        {
        	DigestDataObject ddo = (DigestDataObject) i.next();
        	ddo.setContents(selectDigestContents(ddo.getDigestId(),null,null,true, 0, -1, true));        	
        	for (Iterator j = ddo.getContents().iterator(); j.hasNext();)
            {        		
        		DigestContentDataObject dcdo = (DigestContentDataObject) j.next();
        		String color="#FFFFFF";
        		if(no%2==0)
        			color="F3F3F3";
        		else{
        			color="#FFFFFF";
        		}
        		mail.append("<tr><td bgcolor=\""+color+"\" valign=\"top\" width=\"1%\" align=\"right\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+no+"</font></td>");
        		String summary=dcdo.getSummary();
        		if(summary==null||"".equals(summary)){
        			summary="Abstract N/A.";	
        		}
        		
        		if(dcdo.getContentId().startsWith("com.tms.cms.document.Document_")){
        			mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a  target=\"login\" href=\""+serverUrl+"/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\">"+dcdo.getName()+"</a></b>\n<font size=\"1\">"+summary+"</font></font></td>");
        			//mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a style=\"color:#668F20;\" target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\">"+dcdo.getName()+"</a></b>\n<font size=\"1\">"+summary+"</font></font></td>");
        		}else{
        			mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a  target=\"login\" href=\""+serverUrl+"/ekms/content/content.jsp?id="+dcdo.getContentId()+"\">"+dcdo.getName()+"</a></b>\n<font size=\"1\">"+summary+"</font></font></td>");
        			//mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a style=\"color:#668F20;\" target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/ekms/content/content.jsp?id="+dcdo.getContentId()+"\">"+dcdo.getName()+"</a></b>\n<font size=\"1\">"+summary+"</font></font></td>");
        		}
        		
        		mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+dcdo.getSector()+"</font></td>");
        		mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+dcdo.getCountry()+"</font></td>");
        		mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+dcdo.getCompany()+"</font></td>");
        		String source=dcdo.getAllsource();
        		if(source==null||"".equals(source)){
        			source="N/A";	
        		}
        		mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+source+"</font></td>");
        		//mail.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+sdf.format(dcdo.getSourceDate())+"</font></td></tr>");
        		       		
        		no+=1;
            }
        }
        mail.append("</table>");
        mail.append("</td></tr>");
        mail.append("</table>");
        
        String formattedMail = StringUtils.replace(mail.toString(), "\n", "<br>");
        for (Iterator i = mldo.getRecipients().iterator(); i.hasNext();)
        {
        	RecipientDataObject rdo = (RecipientDataObject) i.next();
        	DaoQuery query=new DaoQuery();
        	query.addProperty(new OperatorEquals("cms_mailing_recipients.recipientId", rdo.getRecipientId(), DaoOperator.OPERATOR_AND));
        	rdo.setUser(getMailingDigestUser(query));
        	for (Iterator j = rdo.getUser().iterator(); j.hasNext();)
            {
        		RecipientDataObject jdo = (RecipientDataObject) j.next();
        		userId.add(jdo.getUserRecipientId());
            }
        }        
        if(userId.size() > 0)
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", userId.toArray(), DaoOperator.OPERATOR_AND));
            users = service.getUsers(query, 0, -1, "username", false);
        }
        for(Iterator i = users.iterator(); i.hasNext();)
        {
            User subscriber = (User) i.next();
            if(!(subscriber.getProperty("email1") == null || "".equals(subscriber.getProperty("email1")))) {
                String email = (String)subscriber.getProperty("email1");
                MailUtil.sendEmail(null, true, null, email, null, null, mldo.getMailingListName()+" - "+mldo.getDigestIssueName()+" ("+Application.getInstance().getMessage("digest.email.topicList")+")", formattedMail);
            }
        }
        dao.insertReport(mldo);
    	}catch(Exception e){
    		throw new DigestException(e.toString(), e);
    	}
    }
    
    public Collection getNewsReport(Date from, Date to, String emailFormat, String emailType) throws DigestException
    {
        Collection mail = new ArrayList();
        try
        {
            DigestDao dao = (DigestDao)getDao();
            mail = dao.getNewsReport(from, to, emailFormat, emailType);               
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            throw new DigestException(e.getMessage());
        }

        return mail;
    }
    
    public int selectDigestContentsCount(String[] classes,String name, Date fromDate, Date toDate, String permission, User user, String digestId) throws DigestException {
        try {
        	DigestDao dao = (DigestDao) getDao();
            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(user.getId());
                String[] principalArray = (String[]) principalList.toArray(new String[0]);

                // retrieve list of objects
                return dao.selectDigestContentByPermissionCount(classes,principalArray, permission, name, fromDate, toDate, digestId);
            }
            else {
                // retrieve list of objects
            	return dao.selectDigestContentsCount(digestId,name);
               
            }           
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new DigestException(e.toString());
        }
    }
    
    public Collection selectDigestContents(String digestId,String name, String sort, boolean desc, int start, int rows, boolean taxonomy) throws DigestException {
        try {
        	DigestDao dao = (DigestDao) getDao();
            Collection results= new ArrayList();
            Collection nodes= new ArrayList();
            
            // retrieve list of objects
            results = dao.selectDigestContents(digestId,name, sort, desc, start, rows);
               
            if(taxonomy){
            TaxonomyModule tx = (TaxonomyModule) Application.getInstance().getModule(TaxonomyModule.class);
            
            if(results.size()>0){
            	nodes=tx.formTaxonomyTree(new Integer(1));
            	
            }
            for (Iterator i = results.iterator(); i.hasNext();) {
            	DigestContentDataObject dcdo = (DigestContentDataObject) i.next();
                Map map=tx.getNodesWithMapping(dcdo.getContentId());
                String nodeName="";
                if(map!=null || map.size() > 0){
                	dcdo.setCompany("N/A");
                	dcdo.setSector("N/A");
                	dcdo.setCountry("N/A");
                for (Iterator j = nodes.iterator(); j.hasNext();) {
                	TaxonomyNode txnode = (TaxonomyNode) j.next();
               
                	if("0".equals(txnode.getParentId())){
                		if("Countries".equals(txnode.getName())||"Companies".equals(txnode.getName())||"Sectors".equals(txnode.getName())){
                			nodeName=txnode.getName();
                			dcdo=recursiveTaxonomy(nodeName,dcdo,map,txnode.getChildNodes());
                		}
                		}
                	}
      
                }else{
                	dcdo.setCompany("N/A");
                	dcdo.setSector("N/A");
                	dcdo.setCountry("N/A");
                }
                
            }
            }
            return results;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new DigestException(e.toString());
        }
    }
    
    public int selectDigestContentsCount(String digestId,String name) throws DigestException {
        try {
        	DigestDao dao = (DigestDao) getDao();
            
            // retrieve list of objects
            return dao.selectDigestContentsCount(digestId,name);                         
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new DigestException(e.toString());
        }
    }
    
    public Collection selectDigestContents(String digestId, String contentId) throws DigestException {
        try {
        	DigestDao dao = (DigestDao) getDao();
 
            
            // retrieve list of objects
            Collection results = dao.selectDigestContents(digestId, contentId);                     
            return results;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new DigestException(e.toString());
        }
    }
    
    public ContentObject selectById(String key) throws DigestException
    {
        try
        {
            ContentManager manager = (ContentManager) Application.getInstance().getModule(ContentManager.class);
        	ContentManagerDao dao = (ContentManagerDao) manager.getDao();
            return dao.selectById(key);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return new DefaultContentObject();
        }
    }
    
    public ContentObject recursiveTaxonomy(String name, ContentObject co, Map map, Collection child) throws DigestException
    {

    	for (Iterator k = child.iterator(); k.hasNext();) {
        	TaxonomyNode childnode = (TaxonomyNode) k.next();
            TaxonomyNode tempNode = (TaxonomyNode)map.get(childnode.getTaxonomyId());
            if(tempNode != null){
            	if("Countries".equals(name))
            	{
            		co.setCountry(childnode.getName());
            		break;
            	}
            	
            	else if("Companies".equals(name)){
            		co.setCompany(childnode.getName());	
            		break;
            	}else if("Sectors".equals(name)){
            		co.setSector(childnode.getName());	
            		break;
            	}
            }
            if(childnode.getChildNodes()!=null){
            	recursiveTaxonomy(name, co, map, childnode.getChildNodes());
            }
        }
            return co;
        
    }
    
    public DigestContentDataObject recursiveTaxonomy(String name, DigestContentDataObject dcdo, Map map, Collection child) throws DigestException
    {

    	for (Iterator k = child.iterator(); k.hasNext();) {
        	TaxonomyNode childnode = (TaxonomyNode) k.next();
            TaxonomyNode tempNode = (TaxonomyNode)map.get(childnode.getTaxonomyId());
            if(tempNode != null){
            	if("Countries".equals(name))
            	{
            		dcdo.setCountry(childnode.getName());
            		break;
            	}
            	
            	else if("Companies".equals(name)){
            		dcdo.setCompany(childnode.getName());	
            		break;
            	}else if("Sectors".equals(name)){
            		dcdo.setSector(childnode.getName());	
            		break;
            	}
            }
            if(childnode.getChildNodes()!=null){
            	recursiveTaxonomy(name, dcdo, map, childnode.getChildNodes());
            }
        }
            return dcdo;
        
    }
    
    public Collection selectContents(String[] classes,String name, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows, String permission, User user) throws DigestException {
        try {
            DigestDao dao = (DigestDao) getDao();
            Collection results= new ArrayList();
            Collection nodes= new ArrayList();
            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(user.getId());
                String[] principalArray = (String[]) principalList.toArray(new String[0]);

                // retrieve list of objects
                results = dao.selectContentByPermission(classes,principalArray, permission, name, fromDate, toDate, sort, desc, start, rows);
            }
            else {
                // retrieve list of objects
            	results = dao.selectContentByCriteria(classes,name, fromDate, toDate, sort, desc, start, rows);
               
            }
            TaxonomyModule tx = (TaxonomyModule) Application.getInstance().getModule(TaxonomyModule.class);
            
            if(results.size()>0){
            	nodes=tx.formTaxonomyTree(new Integer(1));
            	
            }
            for (Iterator i = results.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();
                Map map=tx.getNodesWithMapping(co.getId());
                String nodeName="";
                if(map!=null || map.size() > 0){
                	co.setCompany("N/A");
                	co.setSector("N/A");
                	co.setCountry("N/A");
                for (Iterator j = nodes.iterator(); j.hasNext();) {
                	TaxonomyNode txnode = (TaxonomyNode) j.next();
               
                	if("0".equals(txnode.getParentId())){
                		if("Countries".equals(txnode.getName())||"Companies".equals(txnode.getName())||"Sectors".equals(txnode.getName())){
                			nodeName=txnode.getName();
                			co=recursiveTaxonomy(nodeName,co,map,txnode.getChildNodes());
                		}
                		}
                	}
      
                }else{
                	co.setCompany("N/A");
                	co.setSector("N/A");
                	co.setCountry("N/A");
                }
                
            }
            
            return results;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new DigestException(e.toString());
        }
    }
    
    public int selectContentsCount(String[] classes,String name, Date fromDate, Date toDate, String permission, User user) throws DigestException {
        try {
        	DigestDao dao = (DigestDao) getDao();
            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(user.getId());
                String[] principalArray = (String[]) principalList.toArray(new String[0]);

                // retrieve list of objects
                return dao.selectContentByPermissionCount(classes,principalArray, permission, name, fromDate, toDate);
            }
            else {
                // retrieve list of objects
            	return dao.selectContentByCriteriaCount(classes,name, fromDate, toDate);
               
            }
            
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new DigestException(e.toString());
        }
    }



}
