package com.tms.assetmanagement.ui;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.collections.SequencedHashMap;

import com.tms.assetmanagement.model.AssetModule;

public class AssetDisposalTable extends Table {

	public AssetDisposalTable() {
		super();
	}

	public AssetDisposalTable(String name) {
		super(name);
	}

	public void init() {
		setModel(new AssetDisposalTableModel());
	}
	
	public void onRequest(Event evt) {
		super.onRequest(evt);
		init();
	}
	public class AssetDisposalTableModel extends TableModel {

		public AssetDisposalTableModel() {
			
			TableColumn tblColid = new TableColumn("itemName",Application.getInstance().getMessage("asset.label.assetName"));
				tblColid.setUrlParam("disposalId");
			addColumn(tblColid);
			
			addColumn(new TableColumn("categoryName",Application.getInstance().getMessage("asset.label.Category")));
			
			TableColumn colDate = new TableColumn("dateDisposal",Application.getInstance().getMessage("asset.label.disposaldate"));
	        TableDateFormat dateFormat = new TableDateFormat("dd MMM yyyy");
			colDate.setFormat(dateFormat);
			addColumn(colDate);
			
			TableColumn colQty = new TableColumn("disposalQty",Application.getInstance().getMessage("asset.label.disposalQty"));
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
			
			TableColumn colCost = new TableColumn("disposalCost",Application.getInstance().getMessage("asset.label.disposalcost"));
			colCost.setFormat(new TableFormat(){
				public String format(Object obj2){ 
					
					String result = new DecimalFormat("0.00").format(obj2);
					
					return result;
				}
			});
			addColumn(colCost);
			
			
			TableColumn colCreatedDate = new TableColumn("dateCreated",Application.getInstance().getMessage("asset.label.createddate"));
	        TableDateFormat dateFormat1 = new TableDateFormat("dd MMM yyyy");
	        colCreatedDate.setFormat(dateFormat1);
			addColumn(colCreatedDate);
			
// Set Filter for selectBx and Date
			
			addFilter(new TableFilter("keyword"));
			
			TableFilter filterlblcategory =  new TableFilter("filterlblcategory");
		    Label lblcategory= new Label("lblcategory",Application.getInstance().getMessage("asset.label.Category","Category")+":");
		    filterlblcategory.setWidget(lblcategory);
		    addFilter(filterlblcategory);
		   
			TableFilter filterCategory = new TableFilter("filterCategory");

			SelectBox selbxCategorylist = new SelectBox("selbxCategorylist");
			
			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
			
	        Map mapCategory = new SequencedHashMap();        
	        Collection listCategory = mod.listCategoryType();
			
	        mapCategory.put("-1", "-- " + Application.getInstance().getMessage("asset.label.selectCategory") + " --");
			if (listCategory != null &&listCategory.size() > 0){	
	        	for (Iterator iterator = listCategory.iterator(); iterator.hasNext();) {
			     
	        		Map tempMap = (Map)iterator.next();
	        		if(tempMap.get("categoryName")!=null)
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
		    Label lblpurchasedDate= new Label("lblpurchasedDate", Application.getInstance().getMessage("asset.label.disposaldate","Disposal Date"));
		    filterlblPurchasedDate.setWidget(lblpurchasedDate);
		    addFilter(filterlblPurchasedDate);
		    
	        TableFilter filterlblfrm =  new TableFilter("filterlblfrm");
	        Label lblfrom= new Label("lblfrom",Application.getInstance().getMessage("asset.label.from","From")+":");
	        filterlblfrm.setWidget(lblfrom);
	        addFilter(filterlblfrm);
	   
	        TableFilter filterFrmDate = new TableFilter("filterFrmDate");	
	        DatePopupField dateFrm = new DatePopupField("dateFrm");
	        dateFrm.setOptional(true);
	        filterFrmDate.setWidget(dateFrm);
	        addFilter(filterFrmDate);	  
	        
	        TableFilter filterlbl = new TableFilter("filterlbl");
	        Label lblto = new Label("lblto",Application.getInstance().getMessage("asset.label.to","To")+":");
	        filterlbl.setWidget(lblto);
	    	addFilter(filterlbl);
	    	
	        TableFilter filterToDate = new TableFilter("filterToDate");	  
	        DatePopupField dateTo = new DatePopupField("dateTo");
	        dateTo.setOptional(true);
	        filterToDate.setWidget(dateTo);
	        addFilter(filterToDate);
	     
	       addAction(new TableAction("add", Application.getInstance().getMessage("asset.label.add" ,"Add")));          	       
	       addAction(new TableAction("delete", "Delete", "Delete Selected Items?"));
			 
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

			return mod.listingDisposal(strKeyword, strCategory, frmDate, toDate ,getSort(), isDesc(), getStart(), getRows());
		}

		public int getTotalRowCount() {

			String strKeyword = (String) getFilterValue("keyword");

			List listCategory = (List) getFilterValue("filterCategory");
			String strCategory = "";

			if (listCategory.size() > 0)
				strCategory = (String) listCategory.get(0);
		
			Date toDate = ((DateField) getFilter("filterToDate").getWidget()).getDate();
			Date frmDate = ((DateField) getFilter("filterFrmDate").getWidget()).getDate();
	  
			if (toDate == null || frmDate == null) {
					toDate = null;
					frmDate = null;
			}      
			Application app = Application.getInstance();
			AssetModule mod = (AssetModule) app.getModule(AssetModule.class);

			return mod.listingDisposal(strKeyword, strCategory, frmDate, toDate ,getSort(), isDesc(), 0, -1).size();
		}

		public Forward processAction(Event evt, String action,
				String[] selectedKeys) {
			if( "delete".equals(action)){ 
	      		 
	      		Application app = Application.getInstance(); 
	      		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
	      		
	    		for (int i=0; i<selectedKeys.length; i++){
		 			
		 			mod.deleteDisposal(selectedKeys[i]);
		 		}	
	      	 }

	      	 if("add".equals(action))
	      		return new Forward("Add");	
			return null;
		}
		
		public String getTableRowKey() {
			return "disposalId";
		}
	}
}
