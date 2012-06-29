package com.tms.assetmanagement.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.assetmanagement.model.AssetModule;

public class SavedFixedAssetReportTable extends Table {

	public SavedFixedAssetReportTable() {
		super();
	}

	public SavedFixedAssetReportTable(String name) {
		super(name);
	}

	public void init() {
		setModel(new SavedFixedAssetReportTableModel());
	}

	public void onRequest(Event evt) {
		super.onRequest(evt);
		init();
	}

	public class SavedFixedAssetReportTableModel extends TableModel {

		public SavedFixedAssetReportTableModel() {

			TableColumn id = new TableColumn("saveAssetId",Application.getInstance().getMessage("asset.label.fixedAssetReport"));		
			id.setFormat(new TableFormat(){
				public String format(Object obj){	

					Application app = Application.getInstance(); 
					AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	 
					Date createdDate = mod.getCreatedDateSaveFixedAsset(obj.toString());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm");

					String result = Application.getInstance().getMessage("asset.label.fixedAssetReport")+ "_" + sdf.format(createdDate);
					return result;
				}
			});
			id.setUrlParam("saveAssetId");
			addColumn(id);			

			TableColumn colrecordYear = new TableColumn("financialYear",Application.getInstance().getMessage("asset.label.recordYear"));
			addColumn(colrecordYear);	

			TableColumn colCreatedDate = new TableColumn("dateCreated",Application.getInstance().getMessage("asset.label.createddate"));
			TableDateFormat dateFormat = new TableDateFormat("dd-MMM-yyyy hh:mm");
			colCreatedDate.setFormat(dateFormat);
			addColumn(colCreatedDate);			

			TableFilter filterlblCreatedDate = new TableFilter("filterlblCreatedDate");
			Label lblCreatedDate= new Label("lblCreatedDate", Application.getInstance().getMessage("asset.label.createddate","Created Date"));
			filterlblCreatedDate.setWidget(lblCreatedDate);
			addFilter(filterlblCreatedDate);			

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

			addAction(new TableAction("delete","Delete","Delete Selected Report(s)?"));				
		}

		public Collection getTableRows() {		

			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
			Date toDate = ((DateField) getFilter("filterToDate").getWidget()).getDate();
			Date frmDate = ((DateField) getFilter("filterFrmDate").getWidget()).getDate();

			// either one of date is empty, set both date to null
			if (toDate == null || frmDate == null) {
				toDate = null;
				frmDate = null;
			}   


			return mod.listingSaveFixedAsset(frmDate, toDate, getSort(), isDesc(), getStart(), getRows());							
		}

		public int getTotalRowCount() {

			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	

			Date toDate = ((DateField) getFilter("filterToDate").getWidget()).getDate();
			Date frmDate = ((DateField) getFilter("filterFrmDate").getWidget()).getDate();

			// either one of date is empty, set both date to null
			if (toDate == null || frmDate == null) {
				toDate = null;
				frmDate = null;
			}   


			return  mod.listingSaveFixedAsset(frmDate, toDate,  getSort(), isDesc(), 0, -1).size();		
		}

		public Forward processAction(Event evt, String action,String[] selectedKeys) {

			if("delete".equals(action)){        	

				Application app = Application.getInstance(); 
				AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	    			 

				for (int ii=0; ii<selectedKeys.length; ii++)	    			 			
					mod.deleteSaveFixedAsset(selectedKeys[ii]);	    		    
			}	
			return null;		
		}

		public String getTableRowKey() {
			return "saveAssetId";
		}		
	}
}
