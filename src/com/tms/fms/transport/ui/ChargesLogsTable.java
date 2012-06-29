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
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class ChargesLogsTable extends Table{
	protected ViewChargesLogsModel model;
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ChargesLogsTable(){}
	
	public ChargesLogsTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		model = new ViewChargesLogsModel();
		model.setId(id);
		model.resetTable();
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewChargesLogsModel extends TableModel {
		
		private String id;
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public ViewChargesLogsModel(){}
		
		public void resetTable(){
			removeChildren();
		    
		    TableColumn tcAmount = new TableColumn("amount", Application.getInstance().getMessage("fms.tran.table.chargesAmount(RM)", "Charges Amount(RM)"));
		    tcAmount.setSortable(false);
		    addColumn(tcAmount);
		    
		    TableColumn tcEffectiveDate = new TableColumn("effective_date", Application.getInstance().getMessage("fms.tran.table.effectiveDate", "Effective Date"));
		    tcEffectiveDate.setSortable(false);
		    tcEffectiveDate.setFormat(new TableDateFormat("dd-MM-yyyy"));
		    addColumn(tcEffectiveDate);
		    
		    TableColumn tcUpdatedBy = new TableColumn("createdby_name", Application.getInstance().getMessage("fms.tran.table.updatedBy", "Updated By"));
		    tcUpdatedBy.setSortable(false);
		    addColumn(tcUpdatedBy);
		}
		
		public Collection getTableRows() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectRateCardLog(id);
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
				return dao.selectRateCardLog(id).size();
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}
	}
}
