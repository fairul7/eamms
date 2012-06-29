package com.tms.crm.sales.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.crm.sales.model.CategoryModule;
import com.tms.crm.sales.model.OpportunityProductArchiveModule;
import com.tms.crm.sales.model.OpportunityProductModule;

public class CategoryTable extends Table {
	private String linkUrl;
	private TableColumn categoryName;
	
	
	/* Step 1: Initialization */
	public void init() {
		setModel(new CategoryTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		categoryName.setUrl(linkUrl); // Set the link
	}
	 
	    
	/* Step 3: Table display and processing */
	public class CategoryTableModel extends TableModel {
		public CategoryTableModel() {
			categoryName = new TableColumn("categoryName", Application.getInstance().getMessage("sfa.label.category","Category"));
			categoryName.setUrl(linkUrl);
			categoryName.setUrlParam("categoryID");
			addColumn(categoryName); 
			/*
			TableColumn isArchived = new TableColumn("isArchived", Application.getInstance().getMessage("sfa.label.archived","Archived"));
			isArchived.setFormat(new TableStringFormat(DisplayConstants.getYesNoMap()));
			addColumn(isArchived);*/

            addAction(new TableAction("delete",Application.getInstance().getMessage("sfa.message.delete","Delete"),Application.getInstance().getMessage("sfa.message.deleteMessage","Are you sure you want to delete?")));


		}
		
		public Collection getTableRows() {
			Application application = Application.getInstance();
			CategoryModule module    = (CategoryModule) application.getModule(CategoryModule.class);
			return module.listCategories(getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			CategoryModule module    = (CategoryModule) application.getModule(CategoryModule.class);
			return module.countCategories();
		}
		
		public String getTableRowKey() {
			return "categoryID";
		}

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
            	Application application = Application.getInstance();
            	CategoryModule module    = (CategoryModule) application.getModule(CategoryModule.class);
                OpportunityProductModule opm = (OpportunityProductModule) Application.getInstance().getModule(OpportunityProductModule.class);
                OpportunityProductArchiveModule opam = (OpportunityProductArchiveModule) Application.getInstance().getModule(OpportunityProductArchiveModule.class);
                boolean type=false;
                for (int i = 0; i < selectedKeys.length; i++) {
                	int count=opm.countOpportunityProductByCategory(selectedKeys[i]);
                	if(count>0){
                		type=true;
                	}
                	int count2=opam.countOpportunityArchiveProductByCategory(selectedKeys[i]);
                	if(count2>0){
                		type=true;
                	}
                }
               if(!type){
                for (int i = 0; i < selectedKeys.length; i++) {
                    String selectedKey = selectedKeys[i];
                    module.deleteCategory(selectedKey);
                }if(selectedKeys.length>0)
                return new Forward("delete");
               }
               else{
            	   return new Forward("notdelete");    
               }
            }
            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }

	}
	
	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}
}
