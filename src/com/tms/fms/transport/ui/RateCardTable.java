package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tms.fms.transport.model.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class RateCardTable extends Table{
	public static String FORWARD_LISTING_ADD="setup.listing.add";
	
	protected ViewRateCardModel model;
	
	public RateCardTable(){}
	
	public RateCardTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		model = new ViewRateCardModel();
		model.resetTable();
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewRateCardModel extends TableModel {
		
		public ViewRateCardModel(){}
		
		public void resetTable(){
			removeChildren();
			
			addAction(new TableAction("add", Application.getInstance().getMessage("fms.tran.addNewCharges", "Add New Charges")));
			
			TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.tran.table.name", "Name"));
		    addColumn(tcName);
		    
		    TableColumn tcBasedOn = new TableColumn("type", Application.getInstance().getMessage("fms.tran.table.chargesBasedOn", "Charges Based On"));
		    Map mapBasedOn = new HashMap(); 
		    mapBasedOn.put("H", Application.getInstance().getMessage("fms.tran.form.perHour", "Per Hour")); 
		    mapBasedOn.put("M", Application.getInstance().getMessage("fms.tran.form.byMileage", "By Mileage"));
		    tcBasedOn.setFormat(new TableStringFormat(mapBasedOn));
		    addColumn(tcBasedOn);
		    
		    TableColumn tcAmount = new TableColumn("amount", Application.getInstance().getMessage("fms.tran.table.chargesAmount(RM)", "Charges Amount(RM)"));
		    tcAmount.setSortable(false);
		    addColumn(tcAmount);
		    
		    TableColumn tcUpdatedBy = new TableColumn("createdby_name", Application.getInstance().getMessage("fms.tran.table.updatedBy", "Updated By"));
		    tcUpdatedBy.setSortable(false);
		    addColumn(tcUpdatedBy);
		    
		    TableColumn tcAction = new TableColumn("setup_id", Application.getInstance().getMessage("fms.tran.table.action", "Action"));
		    tcAction.setSortable(false);
		    tcAction.setFormat(new TableFormat(){

				public String format(Object arg0) {
					
					String id = (String) arg0;
					String url = " <a href=\"EditTransportCharges.jsp?id="+ arg0.toString()+
					       "\">Edit</a> | <a href=\"ViewChargesLogs.jsp?id="+ arg0.toString()+
					       "\">View Logs</a>";
					if("default".equals(id))
						return "";
					else
						return url;
				}
		    	
		    });
		    addColumn(tcAction);
		}
		
		public Collection getTableRows() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectRateCard(new Date(), getSort(), isDesc());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
				return dao.selectRateCard(new Date(), getSort(), isDesc()).size();
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}
			return super.processAction(event, action, selectedKeys);
		}
	}
}
