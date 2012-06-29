package com.tms.ekmsadmin.ekplog.ui;

import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableDecimalFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.messaging.model.StorageFileDataSource;
import com.tms.ekmsadmin.ekplog.model.EkpLogDao;
import com.tms.ekmsadmin.ekplog.model.EkpLogModule;
import com.tms.ekmsadmin.ekplog.model.EkpLogResult;
import com.tms.ekmsadmin.ekplog.model.EkpLogZipFileDownloadObject;

public class EkpLogTable extends Table {
	private static String DEFAULT_DATETIME_PATTERN = "dd MMM yyyy hh:mm a";
	
	public void init() {
        setWidth("100%");
        setModel(new TomcatLogTableModel());
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public class TomcatLogTableModel extends TableModel {
    	private int rowCount = 0;
    	
        public TomcatLogTableModel() {
            Application application = Application.getInstance();
            
            TableColumn fileNameCol = new TableColumn("singleFileDownloadUrl", application.getMessage("ekplog.label.fileName", "File Name"));
            addColumn(fileNameCol);
            
            String dateTimePattern = application.getProperty("globalDatetimeLong");
            if(dateTimePattern != null && !"".equals(dateTimePattern)) {
            	dateTimePattern = DEFAULT_DATETIME_PATTERN;
            }
            
            TableColumn lastUpdatedDateCol = new TableColumn("lastUpdatedDate", application.getMessage("ekplog.label.lastUpdatedDate", "Last Updated Date"));
            TableFormat dateTimeFormat = new TableDateFormat(dateTimePattern);
            lastUpdatedDateCol.setFormat(dateTimeFormat);
            addColumn(lastUpdatedDateCol);            
            
            TableColumn fileSizeCol = new TableColumn("fileSize", application.getMessage("ekplog.label.fileSize", "File Size") + " (KB)");
            TableDecimalFormat commaSeperatedDecimalFormat = new TableDecimalFormat("#,###,###");
            fileSizeCol.setFormat(commaSeperatedDecimalFormat);
            addColumn(fileSizeCol);
            
            // Filters
            
            Label lblLastUpdatedDateBegin = new Label("lblLastUpdatedDateBegin");
            lblLastUpdatedDateBegin.setText("<strong>" + application.getMessage("ekplog.label.from", "From") + "</strong>");
            TableFilter dummyFilter1 = new TableFilter("dummyFilter1");
            dummyFilter1.setWidget(lblLastUpdatedDateBegin);
            addFilter(dummyFilter1);
            
            DatePopupField lastUpdatedDateBegin = new DatePopupField("lastUpdatedDateBegin");
            lastUpdatedDateBegin.setOptional(true);
            TableFilter lastUpdatedDateBeginFilter = new TableFilter("lastUpdatedDateBeginFilter");
            lastUpdatedDateBeginFilter.setWidget(lastUpdatedDateBegin);
            addFilter(lastUpdatedDateBeginFilter);
            
            Label lblLastUpdatedDateEnd = new Label("lblLastUpdatedDateEnd");
            lblLastUpdatedDateEnd.setText("<strong>" + application.getMessage("ekplog.label.to", "To") + "</strong>");
            TableFilter dummyFilter2 = new TableFilter("lblLastUpdatedDateEnd");
            dummyFilter2.setWidget(lblLastUpdatedDateEnd);
            addFilter(dummyFilter2);
            
            DatePopupField lastUpdatedDateEnd = new DatePopupField("lastUpdatedDateEnd");
            lastUpdatedDateEnd.setOptional(true);
            TableFilter lastUpdatedDateEndFilter = new TableFilter("lastUpdatedDateEndFilter");
            lastUpdatedDateEndFilter.setWidget(lastUpdatedDateEnd);
            addFilter(lastUpdatedDateEndFilter);
            
            addAction(new TableAction("download", application.getMessage("ekplog.label.download", "Download"), null));
        }
        
        public Collection getTableRows() {
        	DateField lastUpdatedDateBeginWidget = (DateField) getFilter("lastUpdatedDateBeginFilter").getWidget();
        	DateField lastUpdatedDateEndWidget = (DateField) getFilter("lastUpdatedDateEndFilter").getWidget();
        	
        	Date lastUpdatedDateBegin = null;
        	Date lastUpdatedDateEnd = null;
        	if(lastUpdatedDateBeginWidget.getDate() != null) {
        		lastUpdatedDateBegin = lastUpdatedDateBeginWidget.getDate();
        	}
        	if(lastUpdatedDateEndWidget.getDate() != null) {
        		lastUpdatedDateEnd = lastUpdatedDateEndWidget.getDate();
        	}
        	
        	Application application = Application.getInstance();
            EkpLogModule module = (EkpLogModule) application.getModule(EkpLogModule.class);
            EkpLogResult logResult =  module.getLogs(lastUpdatedDateBegin, lastUpdatedDateEnd, getSort(), isDesc(), getStart(), getRows());
            if(logResult.getLogObjects() != null)
            	rowCount = logResult.getTotalResult();
            
            return logResult.getLogObjects();
        }

        public int getTotalRowCount() {
            return rowCount;
        }

        public String getTableRowKey() {
            return "fileName";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application application = Application.getInstance();
            EkpLogModule module = (EkpLogModule) application.getModule(EkpLogModule.class);
            
            if ("download".equals(action)) {
            	if(selectedKeys != null) {
            		if(selectedKeys.length > 0) {
            			module.downloadLogs(evt, selectedKeys);
            		}
            	}
            }
            
            return null;
        }
    }
}
