package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
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
import com.tms.cms.digest.ui.DigestIssueTable.DigestIssueTableModel;
import com.tms.util.FormatUtil;

public class DigestTable extends Table
{
	public static final String FORWARD_ADD = "digest_add";
    public static final String FORWARD_DELETE = "digest_delete";
    public static final String FORWARD_REORDER = "content_reorder";
    protected String digestIssueId;
    
    public DigestTable()
    {
        super();
    }

    public DigestTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();
        setModel(new DigestTableModel());
        setWidth("100%");
    }

    class DigestTableModel extends TableModel
    {        
        public DigestTableModel()
        {
	        Application application = Application.getInstance();
            addAction(new TableAction("add", application.getMessage("digest.label.newDigest")));            
            addAction(new TableAction("order", application.getMessage("digest.label.orderDigest")));
            addAction(new TableAction("delete", application.getMessage("digest.label.delete"), application.getMessage("digest.label.deleteSelectedItems")));

            TableColumn tcName = new TableColumn("digestName", application.getMessage("digest.label.digest"));
            tcName.setUrlParam("digestId");
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
            if("add".equals(action))
            {
            	fwd=new Forward(DigestTable.FORWARD_ADD,"/ekms/digest/digestNew.jsp?digestIssueId="+digestIssueId,true);	
            }
            else if("order".equals(action))
            {
            	fwd=new Forward(FORWARD_REORDER,"/ekms/digest/digestReorder.jsp?digestIssueId="+digestIssueId,true);
            }
            else if("delete".equals(action))
            {
                   DigestModule module = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                   for (int i = 0; i < selectedKeys.length; i++)
                   {
                	   try {
						module.deleteDigest(selectedKeys[i]);
					} catch (DigestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fwd=new Forward(DigestTable.FORWARD_DELETE);
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

}


