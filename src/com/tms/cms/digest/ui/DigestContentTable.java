package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Collection;
import kacang.Application;
import kacang.stdui.Table;
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
import com.tms.cms.digest.model.DigestModule;
import com.tms.util.FormatUtil;

public class DigestContentTable extends Table
{
    protected String digestId;
    
    public DigestContentTable()
    {
        super();
    }

    public DigestContentTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();
        setModel(new DigestContentTableModel());
        setWidth("100%");
    }

    class DigestContentTableModel extends TableModel
    {        
        public DigestContentTableModel()
        {
	        Application application = Application.getInstance();
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
                    if("".equals(summary))
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
                String permission = ContentManager.USE_CASE_VIEW;
                return cm.selectDigestContents(null,searchCriteria,null, null,getSort(), isDesc(), getStart(), getRows(),permission, getWidgetManager().getUser(),digestId);
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
                String permission = ContentManager.USE_CASE_VIEW;
                return cm.selectDigestContentsCount(null,searchCriteria,null, null,permission, getWidgetManager().getUser(),digestId);
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


