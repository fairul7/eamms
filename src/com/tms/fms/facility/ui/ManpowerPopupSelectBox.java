package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;

public class ManpowerPopupSelectBox extends PopupSelectBox{

	public ManpowerPopupSelectBox(){}
	
	public ManpowerPopupSelectBox(String s){
		super(s);
	}
	
	protected Map generateOptionMap(String[] ids) {
		Map itemMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return itemMap;
        }
        
        try{
        	Application app = Application.getInstance();
        	CompetencyHandler handler = (CompetencyHandler) app.getModule(CompetencyHandler.class);
        	
        	for (int i=0; i<ids.length; i++) {
        		Competency competency = (Competency) handler.getCompetency(ids[i]);
	    		itemMap.put(ids[i], competency.getCompetencyName());
		    }
        }catch(Exception e){
        	 Log.getLog(getClass()).error("Error retrieving item", e);
        }
		return itemMap;
	}

	protected Table initPopupTable() {
		return new ManpowerPopupSelectBoxTable();
	}

	class ManpowerPopupSelectBoxTable extends PopupSelectBoxTable{
		
		public ManpowerPopupSelectBoxTable(){}
		
		public ManpowerPopupSelectBoxTable(String s){
			super(s);
		}
		 
		public void init() {
	        super.init();
	        setWidth("100%");
	        setModel(new ManpowerPopupSelectBoxTableModel());
        }
		
		public void onRequest(Event evt) {
			init();
        }
		
		class ManpowerPopupSelectBoxTableModel extends PopupSelectBoxTableModel{
			
			public ManpowerPopupSelectBoxTableModel(){
				super();
				Application application = Application.getInstance();
				addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));
				
				TableColumn tcName = new TableColumn("competencyName", application.getMessage("project.label.name", "Name"));			    
				addColumn(tcName);
			    
			    TableColumn tcCompetencyType = new TableColumn("competencyType", application.getMessage("project.label.type", "Type"));
			    addColumn(tcCompetencyType);			 
			    
			    TableFilter tfSearchText = new TableFilter("tfSearchText");
				TextField searchText = new TextField("searchText");
				searchText.setSize("20");
				tfSearchText.setWidget(searchText);
				addFilter(tfSearchText);
				
			}
			
			public Collection getTableRows() {
				Application application = Application.getInstance();
				Collection list = new ArrayList();
				
				CompetencyHandler handler = (CompetencyHandler) application.getModule(CompetencyHandler.class);
                try {
                	if (getSort() == null || "".equals(getSort())) {
                		setSort("competencyName");
                	}
					list = handler.getCompetencies(generateQuery(), getStart(), getRows(), getSort(), isDesc());
				} catch (CompetencyException e) {
					Log.getLog(getClass()).error(e.getMessage(), e);
				}
                
                return list;
			}

			public int getTotalRowCount() {
				Application application = Application.getInstance();
				int count = 0;
	            try
	            {
	                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
	                count = handler.getCompetenciesCount(generateQuery(), getStart(), getRows(), getSort(), isDesc());
	            }
	            catch(CompetencyException e)
	            {
	                Log.getLog(getClass()).error(e.getMessage(), e);
	            }
	            return count;
			}		
			
			public String getTableRowKey() {
			    return "competencyId";
			}
			
			public String getSearchText() {
				return (String)getFilterValue("tfSearchText");
			}
			
	        protected DaoQuery generateQuery()
	        {
	            DaoQuery query = new DaoQuery();
	            if(!(getFilterValue("tfSearchText") == null || "".equals(getFilterValue("tfSearchText"))))
	            {
	                OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
	                parenthesis.addOperator(new OperatorLike("competencyName", getFilterValue("tfSearchText"), null));
	                parenthesis.addOperator(new OperatorLike("competencyType", getFilterValue("tfSearchText"), DaoOperator.OPERATOR_OR));
	                parenthesis.addOperator(new OperatorLike("competencyDescription", getFilterValue("tfSearchText"), DaoOperator.OPERATOR_OR));
	                query.addProperty(parenthesis);
	            }
//	            SelectBox type = (SelectBox) getFilter("type").getWidget();
//	            if(type.getSelectedOptions().size() > 0)
//	            {
//	                String selected = (String) type.getSelectedOptions().keySet().iterator().next();
//	                if(!(selected == null || "-1".equals(selected)))
//	                    query.addProperty(new OperatorEquals("competencyType", selected, DaoOperator.OPERATOR_AND));
//	            }
	            return query;
	        }
		}
	}
}
