package com.tms.sam.po.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.setting.model.CategoryObject;
import com.tms.sam.po.setting.model.ItemObject;
import com.tms.sam.po.setting.model.SettingModule;


public class ItemPopupSelectBox extends PopupSelectBox {
	private String ppID="";
    public ItemPopupSelectBox() {
        super();
    }

    public ItemPopupSelectBox(String name) {
        super(name);
    }


    protected Table initPopupTable() {
        return new ItemPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map itemsMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return itemsMap;
        }

        Application app = Application.getInstance();
        SettingModule module = (SettingModule) app.getModule(SettingModule.class);
        Collection userList = module.getItems(ids);

        // build users map
        Map tmpMap = new SequencedHashMap();
        for (Iterator i=userList.iterator(); i.hasNext();) {
             ItemObject obj = (ItemObject)i.next();
             tmpMap.put(obj.getItemCode(), obj.getItemDesc());
        }

        // sort
        for (int j=0; j<ids.length; j++) {
             String name = (String)tmpMap.get(ids[j]);
             if (name == null) {
                 name = "---";
             }
             itemsMap.put(ids[j], name);
        }
       

        return itemsMap;
    }

    public class ItemPopupTable extends PopupSelectBoxTable {

        public ItemPopupTable() {
        }

        public ItemPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new ItemTableModel());
            loadCategories();
        }

        protected void loadCategories() {
            
            SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
            Map catMap = new SequencedHashMap();
            Map searchColMap = new SequencedHashMap();
            
            Collection cat = module.getCategory();
            catMap.put("", Application.getInstance().getMessage("po.label.selectCategory","--Select Category--"));
            for( Iterator i=cat.iterator(); i.hasNext(); ){
               	CategoryObject o = (CategoryObject) i.next();
               	catMap.put(o.getCategoryID(), o.getCategory());
            }
           
            searchColMap.put("", Application.getInstance().getMessage("po.label.selectCriteria","--Select Criteria--"));
            searchColMap.put("itemCode", Application.getInstance().getMessage("po.label.itemCode","Item Code"));
            searchColMap.put("itemDesc", Application.getInstance().getMessage("po.label.itemDesc","Item Description"));
            
            TableFilter catFilter = getModel().getFilter("category");
            SelectBox searchCategory= (SelectBox) catFilter.getWidget();
            searchCategory.setMultiple(false);
            searchCategory.setOptionMap(catMap);
            
            TableFilter colFilter = getModel().getFilter("col");
            SelectBox searchCol= (SelectBox) colFilter.getWidget();
            searchCol.setMultiple(false);
            searchCol.setOptionMap(searchColMap);
        
        }

        public void onRequest(Event evt) {
        	loadCategories();
        }

        public class ItemTableModel extends PopupSelectBoxTableModel {
            public ItemTableModel() {
                super();

                Application application = Application.getInstance();
                //Adding Columns
                TableColumn itemCode = new TableColumn("itemCode", application.getMessage("po.label.itemCode", "Item Code"));
                addColumn(itemCode);
                
            	TableColumn itemDesc = new TableColumn("itemDesc", application.getMessage("po.label.itemDesc", "Item Description"));
                addColumn(itemDesc);
                
            	TableColumn category = new TableColumn("category", application.getMessage("po.menu.itemCategory", "Item Category"));
                addColumn(category);
                
                TableColumn minQty = new TableColumn("minQty", application.getMessage("po.label.minQty", "Min Qty"));
                addColumn(minQty);
                
                TableColumn unitOfMeasure = new TableColumn("unitOfMeasure", application.getMessage("po.label.unitOfMeasure", "Unit Of Measure"));
                addColumn(unitOfMeasure);
               
                //Adding Actions
                //Adding Actions
                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

                //Adding Filters
                TableFilter userNameFilter = new TableFilter("query");
                TableFilter catFilter = new TableFilter("category");
                TableFilter colFilter = new TableFilter("col");
                
                Map catMap = new SequencedHashMap();
                SelectBox catSelect = new SelectBox();
                catSelect.setOptionMap(catMap);
                catSelect.setMultiple(false);
                catFilter.setWidget(catSelect);
                
                Map colMap = new SequencedHashMap();
                SelectBox colSelect = new SelectBox();
                colSelect.setOptionMap(colMap);
                colSelect.setMultiple(false);
                colFilter.setWidget(colSelect);
                
                addFilter(catFilter);
                addFilter(userNameFilter);
                addFilter(colFilter);
            }

            public Collection getTableRows() {
               
                Application application = Application.getInstance();
                ppID = Application.getThreadRequest().getSession().getAttribute("purchaseID").toString();
                String query = (String)getFilterValue("query");
                String catCode = getCatFilter();
                String colCode = getColFilter();
                SettingModule module = (SettingModule) application.getModule(SettingModule.class);
        		return module.getItems(ppID,query, colCode, catCode, getSort(), isDesc(), getStart(), getRows());
            }

            public int getTotalRowCount() {
                Application application = Application.getInstance();
               
                String query = (String)getFilterValue("query");
                String catCode = getCatFilter();
                String colCode = getColFilter();
                SettingModule module = (SettingModule) application.getModule(SettingModule.class);
        		return module.countListingRequest(query, colCode, catCode);
            }

            public String getCatFilter() {
                Collection cat = (Collection) getFilterValue("category");
                if (cat != null)
                    if (!cat.isEmpty()) {
                        String val = (String) cat.iterator().next();
                        return val;
                    }
                return "";
            }
            
            public String getColFilter() {
                Collection col = (Collection) getFilterValue("col");
                if (col != null)
                    if (!col.isEmpty()) {
                        String val = (String) col.iterator().next();
                        return val;
                    }
                return "";
            }
            public String getTableRowKey() {
                return "itemCode";
            }

        }
    }

}
