package com.tms.fms.facility.ui;

import java.util.Collection;

import com.tms.fms.facility.model.FacilityModule;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;

public class ItemNotCheckinTable extends Table {
	
	private String requestId;
	private int count;
	public void onRequest(Event evt) {
		setModel(new ItemNotCheckinTableModel());
		//setPageSize(20);
	}
	
	
	

	public String getRequestId() {
		return requestId;
	}




	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}




	private class ItemNotCheckinTableModel extends TableModel{
		
		public ItemNotCheckinTableModel(){
			TableColumn type = new TableColumn("itemName", Application.getInstance().getMessage("fms.facility.label.type", "Type"));			
			type.setSortable(false);
			type.setFormat(new TableFormat() {
				
				public String format(Object arg0) {
					if(arg0 !=null && !"".equals(arg0)){
						return arg0.toString();
					}else{
						return Application.getInstance().getMessage("fms.facility.label.notInRateCard");
					}
				}
			});
			addColumn(type);
			TableColumn itemBarcode = new TableColumn("barcode", Application.getInstance().getMessage("fms.facility.label.itemBarcode", "Item Barcode"));			
			itemBarcode.setSortable(false);
			addColumn(itemBarcode);
		}
		public Collection getTableRows() {
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			Collection col = mod.getItemNotCheckin(requestId);
			count = col.size();
			return col;
		}

		public int getTotalRowCount() {
			return count;
		}
		
	}
}
