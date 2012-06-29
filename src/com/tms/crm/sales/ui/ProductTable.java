/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.crm.sales.model.OpportunityProductArchiveModule;
import com.tms.crm.sales.model.OpportunityProductModule;
import com.tms.crm.sales.model.ProductModule;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProductTable extends Table {
	private String linkUrl;
	private TableColumn tc_ProductName;
	private TableColumn category;
	
	
	/* Step 1: Initialization */
	public void init() {
		setModel(new ProductTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		tc_ProductName.setUrl(linkUrl); // Set the link
	}
	
	
	/* Step 3: Table display and processing */
	public class ProductTableModel extends TableModel {
	
		public ProductTableModel() {
			Application app = Application.getInstance();
			tc_ProductName = new TableColumn("productName", Application.getInstance().getMessage("sfa.label.product","Product"));
			tc_ProductName.setUrl(linkUrl);
			tc_ProductName.setUrlParam("productID");
			addColumn(tc_ProductName);
			
			category = new TableColumn("categoryName", Application.getInstance().getMessage("sfa.label.category","Product Category"));
			addColumn(category);
			
			TableColumn tc_isArchived = new TableColumn("isArchived", Application.getInstance().getMessage("sfa.label.archived","Archived"));
			tc_isArchived.setFormat(new TableStringFormat(DisplayConstants.getYesNoMap()));
			addColumn(tc_isArchived);

			addFilter(new TableFilter("name"));
			
			SelectBox searchCol = new SelectBox("searchCol");
			searchCol.setMultiple(false);
	        Map searchStatusColMap = new SequencedHashMap();   
	        searchStatusColMap.put("", app.getMessage("sfa.label.pleaseSelect","--Please Select--"));
	        searchStatusColMap.put("productName", app.getMessage("sfa.label.product","Product"));
	        searchStatusColMap.put("categoryName", app.getMessage("sfa.label.category", "Product Category"));
	        searchStatusColMap.put("isArchived", app.getMessage("sfa.label.archived","Archived"));
	        
	        searchCol.setOptionMap(searchStatusColMap);
	        
	        TableFilter searchStatusColFilter = new TableFilter("searchStatusColFilter");
	        searchStatusColFilter.setWidget(searchCol);
	        addFilter(searchStatusColFilter);
	         
            addAction(new TableAction("delete",Application.getInstance().getMessage("sfa.message.delete","Delete"),Application.getInstance().getMessage("sfa.message.deleteMessage","Are you sure you want to delete?")));


		}
		
		public Collection getTableRows() {
			String name = (String) getFilterValue("name");
			
			SelectBox searchCol = (SelectBox) getFilter("searchStatusColFilter").getWidget();
            List cols = (List) searchCol.getValue();
            String searchColValue = "";
            if (cols.size() > 0) {
            	searchColValue = (String) cols.get(0);
            }
            
			Application application = Application.getInstance();
			ProductModule module    = (ProductModule) application.getModule(ProductModule.class);
			return module.listProducts(name, searchColValue, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			String name = (String) getFilterValue("name");
			
			SelectBox searchCol = (SelectBox) getFilter("searchStatusColFilter").getWidget();
            List cols = (List) searchCol.getValue();
            String searchColValue = "";
            if (cols.size() > 0) {
            	searchColValue = (String) cols.get(0);
            }
			Application application = Application.getInstance();
			ProductModule module    = (ProductModule) application.getModule(ProductModule.class);
			return module.countProducts(name, searchColValue);
		}
		
		public String getTableRowKey() {
			return "productID";
		}

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
                ProductModule pm = (ProductModule) Application.getInstance().getModule(ProductModule.class);
                OpportunityProductModule opm = (OpportunityProductModule) Application.getInstance().getModule(OpportunityProductModule.class);
                OpportunityProductArchiveModule opam = (OpportunityProductArchiveModule) Application.getInstance().getModule(OpportunityProductArchiveModule.class);
                boolean type=false;
                for (int i = 0; i < selectedKeys.length; i++) {
                	int count=opm.countOpportunityProductByProduct(selectedKeys[i]);
                	if(count>0){
                		type=true;
                	}
                	int count2=opam.countOpportunityArchiveProductByProduct(selectedKeys[i]);
                	if(count2>0){
                		type=true;
                	}
                }
               if(!type){
                for (int i = 0; i < selectedKeys.length; i++) {
                    String selectedKey = selectedKeys[i];
                    pm.deleleProduct(selectedKey);
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
