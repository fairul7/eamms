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

public class SavedMonthlyDepReportTable extends Table{

	public SavedMonthlyDepReportTable() {
		super();
	}

	public SavedMonthlyDepReportTable(String name) {
		super(name);
	}

	public void init() {
		setModel(new SavedMonthlyDepReportTableModel());
	}

	public void onRequest(Event evt) {
		super.onRequest(evt);
		init();
	}

	public class SavedMonthlyDepReportTableModel extends TableModel {

		public SavedMonthlyDepReportTableModel() {

			TableColumn id = new TableColumn("saveMonthlyId",Application.getInstance().getMessage("asset.label.monthlyDepReport"));		
			id.setFormat(new TableFormat(){
				public String format(Object obj){	

					Application app = Application.getInstance(); 
					AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	 
					//String result = new DecimalFormat("0").format(obj);
					Date createdDate = mod.getCreatedDateSaveMonthlyDep(obj.toString());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm");

					String result = Application.getInstance().getMessage("asset.label.monthlyDepReport")+ "_" + sdf.format(createdDate);
					return result;
				}
			});
			id.setUrlParam("saveMonthlyId");
			addColumn(id);			

			TableColumn colrecordYear = new TableColumn("financialYear",Application.getInstance().getMessage("asset.label.recordYear"));
			addColumn(colrecordYear);	

			TableColumn colCreatedDate = new TableColumn("dateCreated",Application.getInstance().getMessage("asset.label.createddate"));
			TableDateFormat dateFormat = new TableDateFormat("dd MMM yyyy hh:mm");
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


			if (toDate == null || frmDate == null) {
				toDate = null;
				frmDate = null;
			}   

			return mod.listingSaveMonthlyDep(frmDate, toDate ,getSort(), isDesc(), getStart(), getRows());							
		}

		public int getTotalRowCount() {

			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);		    

			Date toDate = ((DateField) getFilter("filterToDate").getWidget()).getDate();
			Date frmDate = ((DateField) getFilter("filterFrmDate").getWidget()).getDate();

			if (toDate == null || frmDate == null) {
				toDate = null;
				frmDate = null;
			}   

			return mod.listingSaveMonthlyDep( frmDate, toDate ,getSort(), isDesc(), 0, -1).size();		
		}

		public Forward processAction(Event evt, String action,String[] selectedKeys) {

			if( "delete".equals(action)){         	

				Application app = Application.getInstance(); 
				AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	    			 

				for (int ii=0; ii<selectedKeys.length; ii++)	    			 			
					mod.deleteSaveMonthlyDep(selectedKeys[ii]);	    		    
			}	        	 
			return null;		
		}		

		public String getTableRowKey() {
			return "saveMonthlyId";
		}

	}
}
