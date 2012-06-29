package com.tms.assetmanagement.ui;

import java.text.DecimalFormat;
import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.assetmanagement.model.AssetModule;

public class AssetCategoryTable extends Table {

	public AssetCategoryTable() {
		super();
	}

	public AssetCategoryTable(String name) {
		super(name);
	}

	public void init() {
		setModel(new AssetCategoryTableModel());
	}

	public class AssetCategoryTableModel extends TableModel {

		public AssetCategoryTableModel() {

			TableColumn editCategory = new TableColumn("categoryName",Application.getInstance().getMessage("asset.label.categoryName"));
			editCategory.setUrlParam("categoryId");
			addColumn(editCategory);

			TableColumn colDep = new TableColumn("depreciation",Application.getInstance().getMessage("asset.label.depreciation"));
			colDep.setFormat(new TableFormat(){
				public String format(Object obj){					

					String result = new DecimalFormat("0").format(obj);

					return result;
				}
			});
			addColumn(colDep);

			addFilter(new TableFilter("keyword"));

			addAction(new TableAction("delete","Delete","Delete Selected items?"));

		}

		public Collection getTableRows() {

			String strCategory =(String)getFilterValue("keyword");

			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);		    


			return mod.listingCategory(strCategory, getSort(), isDesc(), getStart(), getRows());

		}

		public int getTotalRowCount() {

			String strCategory =(String)getFilterValue("keyword");

			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);		    

			return mod.listingCategory(strCategory, getSort(), isDesc(), 0, -1).size();		
		}

		public Forward processAction(Event evt, String action,String[] selectedKeys) {

			boolean categoryInUsed = false;
			if( "delete".equals(action)){         	

				Application app = Application.getInstance(); 
				AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	

				for (int i=0; i<selectedKeys.length; i++) {             

					//Checking category In Used
					int intCategoryInUsed = mod.countCategoryInUsed(selectedKeys[i]);

					if (intCategoryInUsed > 0){
						categoryInUsed = true; 
						break;  //Exit from the "for" loop 

					}
				}

				if(categoryInUsed)
					return new Forward("categoryInUsed");
				else
					for (int ii=0; ii<selectedKeys.length; ii++){
						mod.deleteCategory(selectedKeys[ii]);
					}	   
			}
			return null;		
		}


		public String getTableRowKey() {
			return "categoryId";
		}

	}
}
