package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.stdui.DateField;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableResourceFormat;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.ui.ContentSelectBox;
import com.tms.cms.digest.model.DigestContentDataObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;
import com.tms.util.FormatUtil;

public class ContentSelectTable extends Table
{
	public static final String FORWARD_SELECT = "content_select";
    protected String digestId;
    private SelectBox contentSelectBox;
    
    public String getContentClass() {
        if (contentSelectBox != null) {
            Map selected = contentSelectBox.getSelectedOptions();
            if (selected != null && selected.size() > 0) {
                return (String)selected.keySet().iterator().next();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public void setContentClass(String contentClass) {
        if (contentSelectBox != null) {
            contentSelectBox.setSelectedOption(contentClass);
        }
    }

	public ContentSelectTable()
    {
        super();
    }

    public ContentSelectTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();
        contentSelectBox = new SelectBox();
        setModel(new ContentSelectTableModel());
        setWidth("100%");
    }

    class ContentSelectTableModel extends TableModel
    {        
        public ContentSelectTableModel()
        {
	        Application application = Application.getInstance();
            addAction(new TableAction("select", application.getMessage("digest.label.select")));

            TableColumn tcSource = new TableColumn("name", application.getMessage("digest.label.contentTitle"));
            tcSource.setSortable(false);
            tcSource.setUrlParam("id");
            addColumn(tcSource);

            TableColumn classColumn = new TableColumn("className", application.getMessage("general.label.type", "Type"));
            classColumn.setFormat(new TableResourceFormat("cms.label.iconLabel_", null));
            addColumn(classColumn);
            
            TableColumn tcSectors = new TableColumn("sector", application.getMessage("digest.label.sectors"));
            tcSectors.setSortable(false);
            addColumn(tcSectors);
            
            TableColumn tcCountries = new TableColumn("country", application.getMessage("digest.label.country"));
            tcCountries.setSortable(false);
            addColumn(tcCountries);
                       
            TableColumn tcCompanies = new TableColumn("company", application.getMessage("digest.label.company"));
            tcCompanies.setSortable(false);
            addColumn(tcCompanies);                        
            
            TableColumn tcDate = new TableColumn("date", Application.getInstance().getMessage("digest.label.dateCreated"));
            tcDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            tcDate.setSortable(false);
            addColumn(tcDate);
            
            
            TableFilter tfSearchCriteria = new TableFilter("searchCriteria");
            addFilter(tfSearchCriteria); 
            TableFilter contentTypeFilter = new TableFilter("contentType", application.getMessage("general.label.contentType", "Content Type"));
            contentSelectBox.addOption("","--- "+Application.getInstance().getMessage("general.label.pleaseSelect", "Please Select")+" ---");
            contentSelectBox.setOptions("com.tms.cms.article.Article="+Application.getInstance().getMessage("cms.label_com.tms.cms.article.Article"));
            contentSelectBox.setOptions("com.tms.cms.document.Document="+Application.getInstance().getMessage("cms.label_com.tms.cms.document.Document"));            
            contentTypeFilter.setWidget(contentSelectBox);
            addFilter(contentTypeFilter);
            Calendar now=Calendar.getInstance();
            DateField startDate = new DateField("startDate");           
            DateField endDate = new DateField("endDate");           
            endDate.setDate(now.getTime());
            now.set(Calendar.MONTH,now.get(Calendar.MONTH)-1);
            startDate.setDate(now.getTime());
            TableFilter startDateFilter = new TableFilter("startDateFilter");
            TableFilter endDateFilter = new TableFilter("endDateFilter");
            startDateFilter.setWidget(startDate);
            endDateFilter.setWidget(endDate);
            addFilter(startDateFilter);
            addFilter(endDateFilter);
                   
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public Collection getTableRows()
        {
            Log log = Log.getLog(this.getClass());
            try
            {
            	String[] classArray = null;

                String contentClass = getContentClass();
                if (contentClass != null && contentClass.trim().length() > 0) {
                    classArray = new String[] {contentClass};
                }else{
                	classArray = new String[] {"com.tms.cms.article.Article","com.tms.cms.document.Document"};
                }
                
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
               
                DateField dfStart = (DateField)getFilter("startDateFilter").getWidget();
                DateField dfEnd = (DateField)getFilter("endDateFilter").getWidget();
                Date dateStart = dfStart.getDate();
                Date dateEnd = dfEnd.getDate();

                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();

                if (dateStart!=null)
                    startCal.setTime(dateStart);
                startCal.set(Calendar.HOUR,0);
                startCal.set(Calendar.MINUTE,0);
                startCal.set(Calendar.SECOND,0);
                if (dateEnd!=null) {
                    endCal.setTime(dateEnd);
                }
                endCal.set(Calendar.HOUR,23);
                endCal.set(Calendar.MINUTE,59);
                endCal.set(Calendar.SECOND,59);
                DigestModule cm = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                String permission = ContentManager.USE_CASE_VIEW;
                return cm.selectContents(classArray,searchCriteria,startCal.getTime(), endCal.getTime(),getSort(), isDesc(), getStart(), getRows(),permission, getWidgetManager().getUser());
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
            	String[] classArray = null;

                String contentClass = getContentClass();
                if (contentClass != null && contentClass.trim().length() > 0) {
                    classArray = new String[] {contentClass};
                }else{
                	classArray = new String[] {"com.tms.cms.article.Article","com.tms.cms.document.Document"};
                }
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
               
                DateField dfStart = (DateField)getFilter("startDateFilter").getWidget();
                DateField dfEnd = (DateField)getFilter("endDateFilter").getWidget();
                Date dateStart = dfStart.getDate();
                Date dateEnd = dfEnd.getDate();

                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();

                if (dateStart!=null)
                    startCal.setTime(dateStart);
                startCal.set(Calendar.HOUR,0);
                startCal.set(Calendar.MINUTE,0);
                startCal.set(Calendar.SECOND,0);
                if (dateEnd!=null) {
                    endCal.setTime(dateEnd);
                }
                endCal.set(Calendar.HOUR,23);
                endCal.set(Calendar.MINUTE,59);
                endCal.set(Calendar.SECOND,59);
                DigestModule cm = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                String permission = ContentManager.USE_CASE_VIEW;
                return cm.selectContentsCount(classArray,searchCriteria,startCal.getTime(), endCal.getTime(),permission, getWidgetManager().getUser());
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
            if("select".equals(action))
            {
            	DigestModule module = (DigestModule) Application.getInstance().getModule(DigestModule.class);
            	ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
                for (int i = 0; i < selectedKeys.length; i++)
                {
             	   try {
             		  DigestContentDataObject dcdo= new DigestContentDataObject();
             		  
             		  Collection duplicate=module.selectDigestContents(digestId, selectedKeys[i]);
             		  if(duplicate.size()<1){
             			  UuidGenerator uuid=UuidGenerator.getInstance();
                		  String digestContentId=uuid.getUuid();
                		  dcdo.setDigestContentId(digestContentId);
                		  dcdo.setDigestId(digestId);
                		  dcdo.setContentId(selectedKeys[i]);
                		  dcdo.setDateCreate(Calendar.getInstance().getTime());
             		  module.insertDigestContent(dcdo);
             		  }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
                }if(selectedKeys.length>0){
                	
                	try {
						module.updateAll(evt.getWidgetManager().getUser().getId(),digestId);
						fwd=new Forward(ContentSelectTable.FORWARD_SELECT,"content.jsp?digestId="+digestId,true);
					} catch (DigestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
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

