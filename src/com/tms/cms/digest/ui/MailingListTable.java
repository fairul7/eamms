package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.DefaultContentObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;
import com.tms.cms.digest.model.MailingListDataObject;
import com.tms.cms.digest.model.RecipientDataObject;
import com.tms.cms.digest.ui.DigestIssueTable.DigestIssueTableModel;
import com.tms.util.FormatUtil;

public class MailingListTable extends Table
{
	public static final String FORWARD_ADD = "mailing_list_add";
    public static final String FORWARD_DELETE = "mailing_list_delete";
    
    public MailingListTable()
    {
        super();
    }

    public MailingListTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();
        setModel(new MailingListTableModel());
        setWidth("100%");
    }

    class MailingListTableModel extends TableModel
    {        
        public MailingListTableModel()
        {
	        Application application = Application.getInstance();
            addAction(new TableAction("add", application.getMessage("digest.label.newMailingList")));
            addAction(new TableAction("delete", application.getMessage("digest.label.delete"), application.getMessage("digest.label.deleteSelectedItems")));
            TableColumn tcName = new TableColumn("mailingListId", application.getMessage("digest.label.mailingListName"));
            tcName.setSortable(false);
            tcName.setFormat(new TableFormat() {
                public String format(Object arg0) {
                	DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                    DaoQuery query= new DaoQuery();
                    query.addProperty(new OperatorEquals("mailingListId", arg0.toString(), DaoOperator.OPERATOR_AND));
                    String emailType="";
                    String name="";
					try {						
						Collection co = digestModule.getMailingList(query);
						for (Iterator i = co.iterator(); i.hasNext();)
				        {
							MailingListDataObject mldo = (MailingListDataObject) i.next();
							emailType=mldo.getMailFormat();
							name=mldo.getMailingListName();
				        }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if("newsFormat".equals(emailType)){
						return ("<a href=\"/ekms/digest/mailingListNewsOpen.jsp?mailingListId="+arg0.toString()+"\">"+name+"</a>");	
					}
					else
						return ("<a href=\"/ekms/digest/mailingListOpen.jsp?mailingListId="+arg0.toString()+"\">"+name+"</a>");
                }
            });
            addColumn(tcName);
            
            TableColumn tcDigestIssue = new TableColumn("digestIssueName", application.getMessage("digest.label.digestIssue"));
            tcDigestIssue.setSortable(false);
            addColumn(tcDigestIssue);
                       
            TableColumn tcEmailFormat = new TableColumn("emailFormat", application.getMessage("digest.label.emailFormat"));
            tcEmailFormat.setSortable(false);
            addColumn(tcEmailFormat);            
            
            TableColumn tcRecipients = new TableColumn("mailingListId", application.getMessage("digest.label.recipients"));
            tcRecipients.setSortable(false);
            tcRecipients.setFormat(new TableFormat() {
                public String format(Object arg0) {
                	DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                    DaoQuery query= new DaoQuery();
                    query.addProperty(new OperatorEquals("mailingListId", arg0.toString(), DaoOperator.OPERATOR_AND));
                    String recipients="";
					try {
						Collection co = digestModule.getMailingRecipients(query);
						for (Iterator i = co.iterator(); i.hasNext();)
				        {
				        	RecipientDataObject rdo = (RecipientDataObject) i.next();
				        	recipients+=rdo.getRecipientName()+",";
				        }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(recipients.length()>0)
						recipients=recipients.substring(0,recipients.length()-1);
                    return (recipients);
                }
            });
            addColumn(tcRecipients);
            
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

            TableColumn tcLastSendCreate = new TableColumn("lastSendDate", Application.getInstance().getMessage("digest.label.lastSendDate"));
            tcLastSendCreate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            tcLastSendCreate.setSortable(false);
            addColumn(tcLastSendCreate);
            
            TableFilter tfSearchCriteria = new TableFilter("searchCriteria");
            addFilter(tfSearchCriteria);        
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
                return digestModule.getMailingList(searchCriteria, getSort(), isDesc(), getStart(), getRows(), true);
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
                return digestModule.getMailingListCount(searchCriteria);
            }
            catch (Exception e)
            {
                return 0;
            }
        }
        
        public String getTableRowKey()
        {
            return "mailingListId";
        }
        
        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
        	Forward fwd=super.processAction(evt, action, selectedKeys);
            Log log = Log.getLog(this.getClass());
            log.debug("~~~ action = " + action);
            if("add".equals(action))
            {
            	fwd=new Forward(FORWARD_ADD);	
            }else if("delete".equals(action))
            {
                DigestModule module = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                for (int i = 0; i < selectedKeys.length; i++)
                {
             	   try {
						module.deleteMailingList(selectedKeys[i]);
					} catch (DigestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
           }
            
            return fwd;
        }

    }

}


