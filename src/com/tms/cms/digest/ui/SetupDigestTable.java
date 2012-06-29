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

public class SetupDigestTable extends Table
{
	public static final String FORWARD_ADD = "digest_add";
    public static final String FORWARD_DELETE = "digest_delete";
    public static final String FORWARD_ERROR = "digest_error";
    
    public SetupDigestTable()
    {
        super();
    }

    public SetupDigestTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();
        setModel(new SetupDigestTableModel());
        setWidth("100%");
    }

    class SetupDigestTableModel extends TableModel
    {        
        public SetupDigestTableModel()
        {
	        Application application = Application.getInstance();
            addAction(new TableAction("add", application.getMessage("digest.label.newDigest")));
            addAction(new TableAction("delete", application.getMessage("digest.label.delete"), application.getMessage("digest.label.deleteSelectedItems")));

            TableColumn tcName = new TableColumn("digestName", application.getMessage("digest.label.digest"));
            tcName.setUrlParam("digestId");
            addColumn(tcName);
                        
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
                return digestModule.getSetupDigest(searchCriteria, getSort(), isDesc(), getStart(), getRows());
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
                return digestModule.getNumOfSetupDigest(searchCriteria);
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
            	fwd=new Forward(FORWARD_ADD);	
            }
            else if("delete".equals(action))
            {
                   DigestModule module = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                   int check=0;
                   for (int i = 0; i < selectedKeys.length; i++)
                   {
                	   DaoQuery query=new DaoQuery();
                	   query.addProperty(new OperatorEquals("digest", selectedKeys[i], DaoOperator.OPERATOR_AND));
                       try {
						Collection collect=module.getDigest(query);
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
						module.deleteSetupDigest(selectedKeys[i]);
					} catch (DigestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fwd=new Forward(FORWARD_DELETE);
                   }
                   }else if(check>0 && selectedKeys.length>0){
                	   fwd=new Forward(FORWARD_ERROR);  
                   }
              }
            return fwd;
        }

    }

}
