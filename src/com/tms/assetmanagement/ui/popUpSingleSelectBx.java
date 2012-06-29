package com.tms.assetmanagement.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Form;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.DataItems;
import com.tms.assetmanagement.model.DataItemsCategory;

public class popUpSingleSelectBx extends PopupSelectBox{
	public popUpSingleSelectBx(){
		super();
	}
	

	public popUpSingleSelectBx(String name){
		super(name);	
	}


	protected Table initPopupTable() {

		return new popUpSingleSelectBxTable();
	}

	protected Map generateOptionMap(String[] ids) {
	    Map itemsMap = new SequencedHashMap();
	    
        if (ids == null || ids.length == 0) {
            return itemsMap;
        }
        Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		Collection coldata = mod.retrieveItems(ids);
	
		 // build users map
        Map tmpMap = new SequencedHashMap();
        for (Iterator i=coldata.iterator(); i.hasNext();) {
             DataItems objItem = (DataItems)i.next();
             tmpMap.put(objItem.getItemId(), objItem.getItemName());
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
	
	public class popUpSingleSelectBxTable extends PopupSelectBoxTable{
		public popUpSingleSelectBxTable(){
			
		}
		public popUpSingleSelectBxTable (String name){
			super(name);
		}
	     public void init() {
	            super.init();
	            setWidth("100%");
	            setMultipleSelect(false);
	            setModel(new PopupSpopUpSingleSelectTableModel());
	     }	     
	 	
	 	public void onRequest(Event evt) {
	 		super.onRequest(evt);
	 		init();
	 	}
	 	
	     public class PopupSpopUpSingleSelectTableModel extends PopupSelectBoxTableModel {
	    	public PopupSpopUpSingleSelectTableModel(){
	    		addColumn(new TableColumn("itemName",Application.getInstance().getMessage("asset.label.assetName","Asset Name")));
	    		addColumn(new TableColumn("categoryName",Application.getInstance().getMessage("asset.label.Category","Category")));
	    		
	    		TableFilter filterCategory = new TableFilter("filterCategory");

				SelectBox selbxCategorylist = new SelectBox("selbxCategorylist");
				
				//add Table Filter

				addFilter(new TableFilter("keyword"));
				
				Application app = Application.getInstance(); 
				AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
				
		        Map mapCategory = new SequencedHashMap();        
		        Collection listCategory = mod.listCategoryType();				

		        mapCategory.put("-1", "-- " + Application.getInstance().getMessage("asset.label.selectCategory") + " --");
		        if(listCategory != null && listCategory.size() > 0){
		        	for (Iterator iterator = listCategory.iterator(); iterator.hasNext();) {
				     
		        		Map tempMap = (Map)iterator.next();
		        		if(tempMap.get("categoryName")!=null)
						 mapCategory.put(tempMap.get("categoryId"), tempMap.get("categoryName"));				
		        	}
		        }
		        selbxCategorylist.setOptionMap(mapCategory);	        
		        filterCategory.setWidget(selbxCategorylist);
		        
		        addFilter(filterCategory);
		        
		        //button
    			addAction(new TableAction(FORWARD_SELECT,  app.getMessage("general.label.select", "Select")));
	   	    	}
			public Collection getTableRows() {
				
				String strKeyword = (String) getFilterValue("keyword");
				List listCategory = (List) getFilterValue("filterCategory");
				String strCategory = "";
				Collection coldata;
				if (listCategory.size() > 0)
					strCategory = (String) listCategory.get(0);
	
				Application app = Application.getInstance();
				AssetModule mod = (AssetModule) app.getModule(AssetModule.class);
				if ((strKeyword == null || "".equals(strKeyword))  && (strCategory.equals("-1") ||  "".equals(strCategory)))
					 coldata =  mod.retrieveItems(getSort(), isDesc(), getStart(), getRows());
				else
					 coldata =  mod.retrieveCategoryItems(strCategory, strKeyword, getSort(), isDesc(), getStart(), getRows());	
				
				Collection colNewItem = new ArrayList();
			
				if (coldata != null && coldata.size() > 0){					
   					 for(Iterator iterator = coldata.iterator(); iterator.hasNext();){
						 DataItemsCategory obj =(DataItemsCategory)iterator.next();
						 float fTotalQty = obj.getItemQty();						 
						 int iDisposalQty = mod.iGetDisposalUnitByItemId(obj.getItemId());
						 if (fTotalQty - iDisposalQty > 0)
							 colNewItem.add(obj);
					 }
				}				
				return colNewItem;
			}

			public int getTotalRowCount() {
	
				String strKeyword = (String) getFilterValue("keyword");
				List listCategory = (List) getFilterValue("filterCategory");
				String strCategory = "";
				Collection coldata;
				if (listCategory.size() > 0)
					strCategory = (String) listCategory.get(0);
	
				Application app = Application.getInstance();
				AssetModule mod = (AssetModule) app.getModule(AssetModule.class);
				if ((strKeyword == null || "".equals(strKeyword))  && (strCategory.equals("-1") ||  "".equals(strCategory)))
					 coldata =  mod.retrieveItems(getSort(), isDesc(), 0, -1);
				else
					 coldata =  mod.retrieveCategoryItems(strCategory, strKeyword, getSort(), isDesc(), 0, -1);	
				
				Collection colNewItem = new ArrayList();
			
				if (coldata != null && coldata.size() > 0){
					
   					 for(Iterator iterator = coldata.iterator(); iterator.hasNext();){
						 DataItemsCategory obj =(DataItemsCategory)iterator.next();
						 float fTotalQty = obj.getItemQty();						 
						 int iDisposalQty = mod.iGetDisposalUnitByItemId(obj.getItemId());
						 if (fTotalQty - iDisposalQty > 0)
							 colNewItem.add(obj);
					 }
				}					
				return colNewItem.size();
			}			
			
		  public Forward processAction(Event event, String action, String[] selectedKeys) {
            try {
                if (PopupSelectBox.FORWARD_SELECT.equals(action)) {
                    if (getPopupSelectBox() != null) {
                        getPopupSelectBox().setIds(selectedKeys);                  
                 				
                        //add the user into session and pick up by hierarchyform to populate related info to the fields
                        event.getRequest().getSession().setAttribute("item_id", selectedKeys[0]);
                        
                        return new Forward(PopupSelectBox.FORWARD_SELECT);
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return null;
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
                return new Forward(PopupSelectBox.FORWARD_ERROR);
            }
            }

			public String getTableRowKey() {
		            return "itemId";
		       }	     			  	 						 		
	     }	     
	}		
	   public String getDefaultTemplate() {
	        return "assetmanagement/popupSingleSelect";
	    }
	
}
