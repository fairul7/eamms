package com.tms.sam.po.report.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;

import com.tms.sam.po.report.model.ReportModule;
import com.tms.sam.po.ui.PopupSupplierSelect;

public class SupplierRatingReportListing extends Table{
	Collection results;
	
	public Collection getResults() {
		return results;
	}

	public void setResults(Collection results) {
		this.results = results;
	}

	public SupplierRatingReportListing(){
		
	}
	
	public SupplierRatingReportListing(String name){
		super(name);
	}
	
	public void init(){
		super.init();
        setModel(new SupplierRatingReportListingModel());
        setWidth("100%");
	}
	
	public class SupplierRatingReportListingModel extends TableModel{
		private PopupSupplierSelect txtSupplier;
		public SupplierRatingReportListingModel() {
			Application app = Application.getInstance();
			
			TableColumn suppName = new TableColumn("firstname", app.getMessage("supplier.label.supp"));
			suppName.setUrl("viewOnlyEvaluationForm.jsp");
			suppName.setUrlParam("id");
			addColumn(suppName);
			
			TableColumn company = new TableColumn("company", app.getMessage("supplier.label.company"));
			addColumn(company);
			
			TableColumn totalRating = new TableColumn("totalRating", app.getMessage("evaluation.label.rating"));
	         totalRating.setFormat(new TableFormat() {
	                public String format(Object value) {
	                   if( value.toString().equals("4"))
	                    {
	                	    return "Excellent";
	                    }else if ( value.toString().equals("3")){
	                    	return "Good";
	                    }else if ( value.toString().equals("2")){
	                    	return "Fair";
	                    }else if ( value.toString().equals("1")){
	                    	return "Poor";
	                    }else{
	                    	return "None";
	                    }
	                    
	                }
	            });
	         
	        totalRating.setSortable(false);
			addColumn(totalRating);
			
			txtSupplier = new PopupSupplierSelect("txtSupplier"); 
			TableFilter supplierFilter = new TableFilter("supplierFilter");
			supplierFilter.setWidget(txtSupplier);
			addFilter(supplierFilter);
			txtSupplier.init();
						
			
		}
		
		@Override
		public Collection getTableRows() {
			Collection supplierIds = (Collection) getFilterValue("supplierFilter");
			
			ReportModule module = (ReportModule) Application.getInstance().getModule(ReportModule.class);
			results =  module.getSuppliersRating(supplierIds, getSort(), isDesc(), getStart(), getRows());
			Application.getThreadRequest().getSession().setAttribute("results", results);
			return results;
//			return null;
		}
		
		@Override
		public int getTotalRowCount() {
			Collection supplierIds = (Collection) getFilterValue("supplierFilter");
			
			ReportModule module = (ReportModule) Application.getInstance().getModule(ReportModule.class);
            return module.countSuppliersRating(supplierIds);
		}
		
		public String getTableRowKey() {
            return "id";
        }
	}
	
	@Override
	public String getDefaultTemplate() {
		return "po/ratingReport";
	}
}
