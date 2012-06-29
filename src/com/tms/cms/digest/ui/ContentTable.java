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
import com.tms.cms.digest.model.DigestDataObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;
import com.tms.util.FormatUtil;

public class ContentTable extends Table
{
	public static final String FORWARD_ADD = "content_add";
    public static final String FORWARD_DELETE = "content_delete";
    public static final String FORWARD_REORDER = "content_reorder";
    protected String digestId;
    
    public ContentTable()
    {
        super();
    }

    public ContentTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();
        setModel(new ContentTableModel());
        setWidth("100%");
    }

    class ContentTableModel extends TableModel
    {        
        public ContentTableModel()
        {
	        Application application = Application.getInstance();
	        //addAction(new TableAction("new", application.getMessage("digest.label.newDigest")));
	        addAction(new TableAction("add", application.getMessage("digest.label.newContent")));
            addAction(new TableAction("order", application.getMessage("digest.label.orderContent")));
            addAction(new TableAction("delete", application.getMessage("digest.label.delete"), application.getMessage("digest.label.deleteSelectedItems")));
            
            TableColumn tcContent = new TableColumn("contentId", application.getMessage("digest.label.abstractTitle"));
            tcContent.setSortable(false);
            tcContent.setFormat(new TableFormat() {
                public String format(Object arg0) {
                	DigestModule cm = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                    ContentObject co=new DefaultContentObject();
					try {
						co = cm.selectById(arg0.toString());
					} catch (DigestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    String summary=co.getSummary();
                    if(summary==null||"".equals(summary))
                    	summary="Abstract N/A";
                    return ("<a href=\"/ekms/content/content.jsp?id="+arg0.toString()+"\"\">"+co.getName()+"</a><br>"+summary);
                }
            });
            addColumn(tcContent);
            
            TableColumn tcSectors = new TableColumn("sector", application.getMessage("digest.label.sectors"));
            tcSectors.setSortable(false);
            addColumn(tcSectors);
            
            TableColumn tcCountries = new TableColumn("country", application.getMessage("digest.label.country"));
            tcCountries.setSortable(false);
            addColumn(tcCountries);
                       
            TableColumn tcCompanies = new TableColumn("company", application.getMessage("digest.label.company"));
            tcCompanies.setSortable(false);
            addColumn(tcCompanies);
            
            TableColumn tcSource = new TableColumn("allsource", application.getMessage("digest.label.sourceByLine"));
            tcSource.setSortable(false);
            addColumn(tcSource);
            
            /*TableColumn tcSourceDate = new TableColumn("sourceDate", Application.getInstance().getMessage("digest.label.sourceDate"));
            tcSourceDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            tcSourceDate.setSortable(false);
            addColumn(tcSourceDate);*/
        
            TableFilter tfSearchCriteria = new TableFilter("searchCriteria");
            addFilter(tfSearchCriteria);        
        }

        public String getTableRowKey()
        {
            return "digestContentId";
        }

        public Collection getTableRows()
        {
            Log log = Log.getLog(this.getClass());
            try
            {
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
               

                DigestModule cm = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                return cm.selectDigestContents(digestId,searchCriteria, getSort(), isDesc(), getStart(), getRows(),true);
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
               

                DigestModule cm = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                return cm.selectDigestContentsCount(digestId,searchCriteria);
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
            	fwd=new Forward(FORWARD_ADD,"/ekms/digest/contentSelect.jsp?digestId="+digestId,true);	
            }else if("new".equals(action))
            {
            	DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
        		String digestIssueId="";
        		try {
        			DaoQuery query= new DaoQuery();
        			query.addProperty(new OperatorEquals("cms_digest.digestId", digestId, DaoOperator.OPERATOR_AND));
        			Collection digest = digestModule.getDigestMain(query);        			                   
        	        for (Iterator i = digest.iterator(); i.hasNext();)
        	        {
        	        	DigestDataObject dido = (DigestDataObject) i.next();
        	        	digestIssueId=dido.getDigestIssueId();
        	        }
        		} catch (DigestException e) {
        			// TODO Auto-generated catch block
        			Log.getLog(getClass()).error(e.toString(), e);
        		}      
            	fwd=new Forward(FORWARD_ADD,"/ekms/digest/digestNew.jsp?digestIssueId="+digestIssueId,true);	
            }
            else if("order".equals(action))
            {
            	fwd=new Forward(FORWARD_REORDER,"/ekms/digest/contentDigestReorder.jsp?digestId="+digestId,true);
            }
            else if("delete".equals(action))
            {
                   DigestModule module = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                   for (int i = 0; i < selectedKeys.length; i++)
                   {
                	   try {
						module.deleteDigestContent(selectedKeys[i]);
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

	public String getDigestId() {
		return digestId;
	}

	public void setDigestId(String digestId) {
		this.digestId = digestId;
	}
    

}


