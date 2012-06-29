package com.tms.collab.project.ui;

import java.util.Collection;
import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableAction;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.collab.project.Report;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;

public class ProjectPerformanceMetricReportTable extends Table
{
	public String projectId;
    public ProjectPerformanceMetricReportTable()
    {
    }

    public ProjectPerformanceMetricReportTable(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setModel(new ProjectPerformanceMetricReportTableModel());
        setMultipleSelect(true);
        setNumbering(true);
        setWidth("100%");
    }

    public class ProjectPerformanceMetricReportTableModel extends TableModel{
        public ProjectPerformanceMetricReportTableModel()
        {	
        	TableColumn reportName = new TableColumn("reportId", Application.getInstance().getMessage("project.label.reportName","Report Name"));
        	reportName.setFormat(new TableFormat() {
                public String format(Object arg0) {
                	WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
                    Report report;
					try {
						report = wm.getReport(arg0.toString(), false);
						return ("<a href=\"/ekms/worms/projectPerformaceMetricReport.jsp?reportId="+arg0.toString()+"&projectId="+report.getProjectId()+" \" >"+report.getReportName()+"</a>");
					} catch (WormsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "";
					}                    
                    
                }
            });
            addColumn(reportName);    
            TableColumn dateCreated = new TableColumn("dateCreated", Application.getInstance().getMessage("project.label.dateCreated","Date Created"));
        	dateCreated.setFormat(new TableDateFormat("dd/MM/yyyy")); 
            addColumn(dateCreated);      
            addColumn(new TableColumn("user",Application.getInstance().getMessage("project.label.createdBy","Created By"),true));
            addAction(new TableAction("delete",Application.getInstance().getMessage("project.label.delete","Delete")));
            addFilter(new TableFilter("name"));
        }

        public Collection getTableRows()
        {
        	String filter = (String) getFilterValue("name");
            WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);

            try {
				return wm.getProjectReport(projectId, filter,getSort(),isDesc(),getStart(),getRows());
			} catch (WormsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
               
        }
        
        public int getTotalRowCount()
        {
        	WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        	String filter = (String) getFilterValue("name");
            try {
				return wm.getProjectReport(projectId, filter,getSort(),isDesc(),getStart(),getRows()).size();
			} catch (WormsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
        }
        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {          
        	if("delete".equals(action)){
        		WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
                for(int i=0;i<selectedKeys.length;i++){
                	try {
							wm.deleteReport(selectedKeys[i]);
						} catch (WormsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}                   
                }
                return new Forward("delete");
            }
            return super.processAction(evt,action,selectedKeys);
        }

       

        public String getTableRowKey()
        {
            return "reportId";
        }
    }

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
