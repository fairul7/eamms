package com.tms.sam.po.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.setting.model.CategoryObject;
import com.tms.sam.po.setting.model.SettingModule;

public class PopUpItemSelect extends Table{
	private Collection selectedItem=null;
	public void init(){
		setWidth("100%");
		setModel(new PopUpItemSelectModel());
	}
	
	@Override
	public Forward actionPerformed(Event evt) {
		Forward forward = super.actionPerformed(evt);
	
		if(evt.getRequest().getSession().getAttribute("selectedItemCode")!=null)
			setSelectedRowList((List)evt.getRequest().getSession().getAttribute("selectedItemCode"));
		
		
		return forward;  
	}
	public void onRequest(Event evt) {
		init();
		if(evt.getRequest().getSession().getAttribute("selectedItemCode")!=null){
			setSelectedRowList((List)evt.getRequest().getSession().getAttribute("selectedItemCode"));
			selectedItem = null;
		}else{
			clearSelectedRowList();
		}
		super.onRequest(evt);
	}

	public class PopUpItemSelectModel extends TableModel{
		public PopUpItemSelectModel(){
			Application app = Application.getInstance();
			SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
			
        	TableColumn itemCode = new TableColumn("itemCode", app.getMessage("po.label.itemCode", "Item Code"));
            addColumn(itemCode);
            
        	TableColumn itemDesc = new TableColumn("itemDesc", app.getMessage("po.label.itemDesc", "Item Description"));
            addColumn(itemDesc);
            
        	TableColumn category = new TableColumn("category", app.getMessage("po.menu.itemCategory", "Item Category"));
            addColumn(category);
            
            TableColumn minQty = new TableColumn("minQty", app.getMessage("po.label.minQty", "Min Qty"));
            addColumn(minQty);
            
            TableColumn unitOfMeasure = new TableColumn("unitOfMeasure", app.getMessage("po.label.unitOfMeasure", "Unit Of Measure"));
            addColumn(unitOfMeasure);
            
            SelectBox searchCategory= new SelectBox("searchCategory");
            searchCategory.setMultiple(false);
            searchCategory.addOption("", Application.getInstance().getMessage("po.label.selectCategory","--Select Category--"));
            Collection cat = module.getCategory();
            for( Iterator i=cat.iterator(); i.hasNext(); ){
            	CategoryObject o = (CategoryObject) i.next();
            	searchCategory.addOption(o.getCategoryID(), o.getCategory());
            }
            addFilter(new TableFilter("name"));
            
            TableFilter searchCategoryFilter = new TableFilter("searchCategoryFilter");
            searchCategoryFilter.setWidget(searchCategory);
            addFilter(searchCategoryFilter);
            
            SelectBox searchCol = new SelectBox("searchCol");
            searchCol.setMultiple(false);
            Map searchColMap = new SequencedHashMap();
            searchColMap.put("", app.getMessage("po.label.selectCriteria","--Select Criteria--"));
            searchColMap.put("itemCode", app.getMessage("po.label.itemCode","Item Code"));
            searchColMap.put("itemDesc", app.getMessage("po.label.itemDesc","Item Description"));
            searchCol.setOptionMap(searchColMap);
            
            TableFilter searchColFilter = new TableFilter("searchColFilter");
            searchColFilter.setWidget(searchCol);
            addFilter(searchColFilter);
            
            addAction(new TableAction("insert", app.getMessage("department.label.insert", "Insert"), ""));
		}
		
		public Collection getTableRows() {
			String name = (String) getFilterValue("name");
			
            SelectBox searchCriteriaCol = (SelectBox) getFilter("searchColFilter").getWidget();
            List cols = (List) searchCriteriaCol.getValue();
            String searchCriteriaColValue = "";
            if (cols.size() > 0) {
            	searchCriteriaColValue = (String) cols.get(0);
            }
            
            SelectBox searchCategoryCol = (SelectBox) getFilter("searchCategoryFilter").getWidget();
            List categoryCols = (List) searchCategoryCol.getValue();
            String searchCategoryColValue = "";
            if (categoryCols.size() > 0) {
            	searchCategoryColValue = (String) categoryCols.get(0);
            }
            
            
            
			Application app = Application.getInstance();
			SettingModule module = (SettingModule) app.getModule(SettingModule.class);
			return module.getListingItem(name, searchCriteriaColValue, searchCategoryColValue, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			String name = (String) getFilterValue("name");
			
            SelectBox searchCriteriaCol = (SelectBox) getFilter("searchColFilter").getWidget();
            List cols = (List) searchCriteriaCol.getValue();
            String searchCriteriaColValue = "";
            if (cols.size() > 0) {
            	searchCriteriaColValue = (String) cols.get(0);
            }
            
            SelectBox searchCategoryCol = (SelectBox) getFilter("searchCategoryFilter").getWidget();
            List categoryCols = (List) searchCategoryCol.getValue();
            String searchCategoryColValue = "";
            if (categoryCols.size() > 0) {
            	searchCategoryColValue = (String) categoryCols.get(0);
            }
            
			Application app = Application.getInstance();
			SettingModule module = (SettingModule) app.getModule(SettingModule.class);
			return module.countListingRequest(name, searchCriteriaColValue, searchCategoryColValue);
		}
		
		 public Forward processAction(Event evt, String action, String[] selectedKeys) {
			 selectedItem = new ArrayList();
        	 Application app = Application.getInstance();
	         SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	          
	         if ("delete".equals(action)) {
	            for (int i=0; i<selectedKeys.length; i++) {
	               module.deleteItem(selectedKeys[i]);
	            }
	         }
            
            if ("insert".equals(action)) {
            	for (int i=0; i<selectedKeys.length; i++) {
  	               selectedItem.add(selectedKeys[i]);
  	            }
            	
            	evt.getRequest().getSession().setAttribute("selectedItemCode", selectedItem);
               return new Forward("insert");
            }
            return null;
 		}
		
		public String getTableRowKey() {
            return "itemCode";
        }
	}
	
}
