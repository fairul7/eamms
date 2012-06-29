package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Collection;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;


public class RecipientTable extends Table
{
	public static final String FORWARD_ADD = "recipient_add";
    public static final String FORWARD_DELETE = "recipient_delete";
    public static final String FORWARD_ERROR = "recipient_error";
    
    public RecipientTable()
    {
        super();
    }

    public RecipientTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();
        setModel(new RecipientTableModel());
        setWidth("100%");
    }

    class RecipientTableModel extends TableModel
    {        
        public RecipientTableModel()
        {
	        Application application = Application.getInstance();
            addAction(new TableAction("add", application.getMessage("digest.label.newRecipients")));
            addAction(new TableAction("delete", application.getMessage("digest.label.delete"), application.getMessage("digest.label.deleteSelectedItems")));

            TableColumn tcName = new TableColumn("recipientName", application.getMessage("digest.label.recipients"));
            tcName.setUrlParam("recipientId");
            addColumn(tcName);
            
            TableFilter tfSearchCriteria = new TableFilter("searchCriteria");
            addFilter(tfSearchCriteria);        
        }

        public String getTableRowKey()
        {
            return "recipientId";
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
                return digestModule.getRecipients(searchCriteria, getSort(), isDesc(), getStart(), getRows());
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
                return digestModule.getNumOfRecipients(searchCriteria);
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
            if("add".equals(action))
            {
            	fwd=new Forward(RecipientTable.FORWARD_ADD);	
            }
            else if("delete".equals(action))
            {
                   DigestModule module = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                   int check=0;
                   for (int i = 0; i < selectedKeys.length; i++)
                   {
                	   DaoQuery query=new DaoQuery();
                	   query.addProperty(new OperatorEquals("recipientId", selectedKeys[i], DaoOperator.OPERATOR_AND));
                       try {
						Collection collect=module.getMailingRecipients(query);
						if(collect.size()>0)
							check=1;
					} catch (DigestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   }
                   if(check==0 && selectedKeys.length>0){
                   for (int i = 0; i < selectedKeys.length; i++)
                   {
                	   try {
						module.deleteRecipients(selectedKeys[i]);
					} catch (DigestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fwd=new Forward(RecipientTable.FORWARD_DELETE);
                   }}else if(check>0 && selectedKeys.length>0){
                	   fwd=new Forward(FORWARD_ERROR);  
                   }
              }
            return fwd;
        }

    }

}

