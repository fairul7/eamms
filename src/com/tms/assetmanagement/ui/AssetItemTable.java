package com.tms.assetmanagement.ui;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.assetmanagement.model.AssetModule;

import kacang.Application;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

public class AssetItemTable extends Table {

	protected DatePopupField dateTo;
	protected DatePopupField dateFrm;
	protected SelectBox selbxCategorylist;
	public AssetItemTable() {
		super();
	}

	public AssetItemTable(String name) {
		super(name);
	}

	public void init() {
		setModel(new AssetItemTableModel());
	}

 
	public void onRequest(Event evt) {
		super.onRequest(evt);
		init();
	}
	
	public class AssetItemTableModel extends TableModel {

		public AssetItemTableModel() {

			// put in tableColumn and action
			
			TableColumn tblColid = new TableColumn("itemName",Application.getInstance().getMessage("asset.label.assetName"));
				tblColid.setUrlParam("itemId");
			addColumn(tblColid);
			
			addColumn(new TableColumn("categoryName",Application.getInstance().getMessage("asset.label.Category")));
			
			TableColumn colDate = new TableColumn("datePurchased",Application.getInstance().getMessage("asset.label.date"));
	        TableDateFormat dateFormat = new TableDateFormat("dd MMM yyyy");
			colDate.setFormat(dateFormat);
			addColumn(colDate);
			
			TableColumn colQty = new TableColumn("itemQty",Application.getInstance().getMessage("asset.label.quantity"));
			colQty.setFormat(new TableFormat(){
				public String format(Object obj){					
					
					String result = new DecimalFormat("0").format(obj);
					
					return result;
				}
			});
			
			addColumn(colQty);
			
			TableColumn colUnitPrice = new TableColumn("itemUnitPrice",Application.getInstance().getMessage("asset.label.unitPrice"));
			colUnitPrice.setFormat(new TableFormat(){
				public String format(Object obj1){
					
					String result = new DecimalFormat("0.00").format(obj1);
					
					return result;
				}
			});
			addColumn(colUnitPrice);
			
			TableColumn colCost = new TableColumn("itemCost",Application.getInstance().getMessage("asset.label.totalCost"));
			colCost.setFormat(new TableFormat(){
				public String format(Object obj2){
					
					String result = new DecimalFormat("0.00").format(obj2);
					
					return result;
				}
			});
			addColumn(colCost);
			
// Set Filter for selectBx and Date
			
			addFilter(new TableFilter("keyword"));
			
			TableFilter filterlblcategory =  new TableFilter("filterlblcategory");
		    Label lblcategory= new Label("lblcategory","<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("asset.label.Category","Category")+":&nbsp;</span>");
		    filterlblcategory.setWidget(lblcategory);
		    addFilter(filterlblcategory);
		    
			TableFilter filterCategory = new TableFilter("filterCategory");
			selbxCategorylist = new SelectBox("selbxCategorylist");
			
			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
			
	        Map mapCategory = new SequencedHashMap();        
	        Collection listCategory = mod.listCategoryType();
	        mapCategory.put("-1", "-- " + Application.getInstance().getMessage("asset.label.selectCategory") + " --");
	      
	        	if (listCategory != null && listCategory.size() > 0){
		        	for (Iterator iterator = listCategory.iterator(); iterator.hasNext();) {
				     
		        		Map tempMap = (Map)iterator.next();
		        		if(tempMap.get("categoryId")!=null)
						 mapCategory.put(tempMap.get("categoryId"), tempMap.get("categoryName"));			
		        	}
	        	}
			
	        selbxCategorylist.setOptionMap(mapCategory);	
        
	        filterCategory.setWidget(selbxCategorylist);
	        
	        addFilter(filterCategory);	
	        
	        TableFilter filterlblNewLine = new TableFilter("filterlblNewLine");
	        Label lblnewLine = new Label("lblnewLine","<BR>");
	        filterlblNewLine.setWidget(lblnewLine);
	    	addFilter(filterlblNewLine);
	    	
	    	TableFilter filterlblPurchasedDate = new TableFilter("filterlblPurchasedDate");
		    Label lblpurchasedDate= new Label("lblpurchasedDate",Application.getInstance().getMessage("asset.label.datePurchased","Purchased Date"));
		    filterlblPurchasedDate.setWidget(lblpurchasedDate);
		    addFilter(filterlblPurchasedDate);
		    
	        TableFilter filterlblfrm =  new TableFilter("filterlblfrm");
	        Label lblfrom= new Label("lblfrom","<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("asset.label.from","From")+":&nbsp;</span>");
	        filterlblfrm.setWidget(lblfrom);
	        addFilter(filterlblfrm);
	        
	        TableFilter filterFrmDate = new TableFilter("filterFrmDate");
	        dateFrm = new DatePopupField("dateFrm");
	        dateFrm.setOptional(true);
	        filterFrmDate.setWidget(dateFrm);
	        addFilter(filterFrmDate);
	        
	        TableFilter filterlbl = new TableFilter("filterlbl");
	        Label lblto = new Label("lblto","<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("asset.label.to","To")+":&nbsp;</span>");
	        filterlbl.setWidget(lblto);
	    	addFilter(filterlbl);
	    	
	        TableFilter filterToDate = new TableFilter("filterToDate");
	        dateTo  = new DatePopupField("dateTo");
	        dateTo.setOptional(true);
	      //dateTo.setDate(new java.util.Date());
	        filterToDate.setWidget(dateTo);
	        addFilter(filterToDate);
	     
	        addAction(new TableAction("add", Application.getInstance().getMessage("asset.label.add" ,"Add")));       
	        addAction(new TableAction("delete", Application.getInstance().getMessage("asset.label.delete","Delete"), Application.getInstance().getMessage("asset.message.deleteAsset","Delete Selected Assets ?")));
	    
	      
		}

		public Collection getTableRows() {
			
			String strKeyword = (String) getFilterValue("keyword");

			List listCategory = (List) getFilterValue("filterCategory");
			String strCategory = "";

			if (listCategory.size() > 0)
				strCategory = (String) listCategory.get(0);
		
		Date toDate = ((DateField) getFilter("filterToDate").getWidget()).getDate();
		Date frmDate = ((DateField) getFilter("filterFrmDate").getWidget()).getDate();
  
// either one of date is empty, set both date to null
		if (toDate == null || frmDate == null) {
				toDate = null;
				frmDate = null;
			}
         
			Application app = Application.getInstance();
			AssetModule mod = (AssetModule) app.getModule(AssetModule.class);

			return mod.listingItems(strKeyword, strCategory, frmDate, toDate ,getSort(), isDesc(), getStart(), getRows());
		}

		public int getTotalRowCount() {

			String strKeyword = (String) getFilterValue("keyword");
			
			List listCategory = (List) getFilterValue("filterCategory");
			String strCategory = "";

			if (listCategory.size() > 0)
				strCategory = (String) listCategory.get(0);
			
		
			Date toDate = ((DateField) getFilter("filterToDate").getWidget()).getDate();
			Date frmDate = ((DateField) getFilter("filterFrmDate").getWidget()).getDate();
	
//	       either one of date is empty, set both date to null
			if (toDate == null || frmDate == null) {
					toDate = null;
					frmDate = null;
				}

			Application app = Application.getInstance();
			AssetModule mod = (AssetModule) app.getModule(AssetModule.class);

			return mod.listingItems(strKeyword, strCategory, frmDate, toDate, getSort(), isDesc(), 0, -1).size();

		}

		public Forward processAction(Event evt, String action, String[] selectedKeys) {
		
	      	 if("delete".equals(action)){ 
	      		 
	      		Application app = Application.getInstance(); 
	      		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
	      		
	    		for (int i=0; i<selectedKeys.length; i++){
		 			
		 			mod.deleteItems(selectedKeys[i]);
		 		}	
	      	 }

	      	 if("add".equals(action))
	      		return new Forward("Add");	
	      	 
			return null;
		}

		public String getTableRowKey() {
			return "itemId";
		}
	}
}
