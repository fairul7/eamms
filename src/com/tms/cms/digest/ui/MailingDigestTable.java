package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.stdui.Button;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;
import com.tms.cms.digest.model.MailingListDataObject;
import com.tms.cms.digest.model.RecipientDataObject;
import com.tms.cms.digest.ui.DigestTable.DigestTableModel;
import com.tms.util.FormatUtil;

public class MailingDigestTable extends Table
{
    protected String digestIssueId;
    protected String mailingListId;
    
    public MailingDigestTable()
    {
        super();
    }

    public MailingDigestTable(String name)
    {
        this();
        setName(name);
    }

    public void onRequest(Event evt) {
		// TODO Auto-generated method stub
		super.onRequest(evt);
		init();
		DaoQuery query=new DaoQuery();
		query.addProperty(new OperatorEquals("mailingListId", mailingListId, DaoOperator.OPERATOR_AND));
		DigestModule digest = (DigestModule) Application.getInstance().getModule(DigestModule.class);
		Collection mail;
		try {
			mail = digest.getMailingList(query);
			for (Iterator i = mail.iterator(); i.hasNext();)
	        {
	        	MailingListDataObject mldo = (MailingListDataObject) i.next();
	        	digestIssueId=mldo.getDigestIssue();
	        }
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
	}

	public void init()
    {
        super.init();
        setModel(new MailingDigestTableModel());
        setWidth("100%");
    }

    class MailingDigestTableModel extends TableModel
    {        
        public MailingDigestTableModel()
        {
	        Application application = Application.getInstance();
            addAction(new TableAction("send", application.getMessage("digest.label.sendOut"))); 
            addAction(new TableAction("print", application.getMessage("digest.label.printSummary")));

            TableColumn tcName = new TableColumn("digestName", application.getMessage("digest.label.digest"));
            tcName.setSortable(false);
            addColumn(tcName);
            
            TableColumn tcNumOfDigest = new TableColumn("numOfContents", application.getMessage("digest.label.content"));
            tcNumOfDigest.setSortable(false);
            addColumn(tcNumOfDigest);
                       

            TableColumn tcLastEditBy = new TableColumn("lastEditByUser", application.getMessage("digest.label.lastEditedBy"));
            tcLastEditBy.setSortable(false);
            addColumn(tcLastEditBy);
            
            TableColumn tcLastEditDate = new TableColumn("lastEditDate", Application.getInstance().getMessage("digest.label.lastEditedDate"));
            tcLastEditDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            tcLastEditDate.setSortable(false);
            addColumn(tcLastEditDate);
            
            TableColumn tcDateCreate = new TableColumn("dateCreate", Application.getInstance().getMessage("digest.label.dateCreated"));
            tcDateCreate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            tcDateCreate.setSortable(false);
            addColumn(tcDateCreate);
            
            TableFilter tfSearchCriteria = new TableFilter("searchCriteria");
            addFilter(tfSearchCriteria);        
        }

        public String getTableRowKey()
        {
            return "digestId";
        }

        public Collection getTableRows()
        {
            Log log = Log.getLog(this.getClass());
            try
            {
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
               

                DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                return digestModule.getDigest(digestIssueId,searchCriteria, getSort(), isDesc(), getStart(), getRows(), true);
            }
            catch (Exception e)
            {
                // log error and return an empty collection
                log.error(e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount()
        {
            try
            {
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
               

                DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                return digestModule.getNumOfDigest(digestIssueId,searchCriteria);
            }
            catch (Exception e)
            {
                return 0;
            }
        }
        
        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
        	Forward fwd=super.processAction(evt, action, selectedKeys);
            Log log = Log.getLog(this.getClass());
            log.debug("~~~ action = " + action);
            if("send".equals(action))
            {
                DigestModule digest = (DigestModule) Application.getInstance().getModule(DigestModule.class);
               	DaoQuery query=new DaoQuery();
       			query.addProperty(new OperatorEquals("mailingListId", mailingListId, DaoOperator.OPERATOR_AND));
                   
       			Collection mail;
       			MailingListDataObject mldo=new MailingListDataObject();
       			try {
					mail = digest.getMailingList(query);
				
       			if(selectedKeys.length>0){
       	        for (Iterator i = mail.iterator(); i.hasNext();)
       	        {
       	        	mldo = (MailingListDataObject) i.next();
       	        	mldo.setUser(evt.getWidgetManager().getUser());
       	        	DaoQuery query2=new DaoQuery();
       				query2.addProperty(new OperatorEquals("mailingListId", mldo.getMailingListId(), DaoOperator.OPERATOR_AND));
       				mldo.setRecipients(digest.getMailingRecipients(query2));	
       				DaoQuery query3=new DaoQuery();
       				query3.addProperty(new OperatorEquals("cms_digest.digestIssueId", mldo.getDigestIssue(), DaoOperator.OPERATOR_AND));
       				query3.addProperty(new OperatorIn("cms_digest.digestId", selectedKeys, DaoOperator.OPERATOR_AND));
       				mldo.setDigest(digest.getDigestMain(query3));
       				mldo.setEmailFormatType("Summaries List");
       	        }
       	        	digest.sendDigestList(mldo);
       	        	return new Forward("success");
       			}else{
          		  return new Forward("no_select");
       			}
       			} catch (DigestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }else if("print".equals(action))
              {
            	  if(selectedKeys.length>0){
            		getWidgetManager().removeAttribute("summarymailingListId");
                  	getWidgetManager().setAttribute("summarymailingListId",mailingListId);
                  	getWidgetManager().removeAttribute("selectedsummary");
                  	getWidgetManager().setAttribute("selectedsummary",selectedKeys);
                  	return new Forward("print_summary");
            	  }else{
              		  return new Forward("no_select_print");
         			}
              }
            return fwd;
        }

    }

	public String getDigestIssueId() {
		return digestIssueId;
	}

	public void setDigestIssueId(String digestIssueId) {
		this.digestIssueId = digestIssueId;
	}

	public String getMailingListId() {
		return mailingListId;
	}

	public void setMailingListId(String mailingListId) {
		this.mailingListId = mailingListId;
	}
}
