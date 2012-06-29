package com.tms.assetmanagement.ui;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
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
import com.tms.assetmanagement.model.DataAddressee;

public class AssetNotificationTable extends Table{
	String strItem;
	String strCategory;
	public AssetNotificationTable() {
		super();
	}

	public AssetNotificationTable(String name) {
		super(name);
	}

	public void init() {
		setModel(new AssetNotificationTableModel());
	}

	public void onRequest(Event event){
		init();
	}

	public class AssetNotificationTableModel extends TableModel {

		public AssetNotificationTableModel() {

			addColumn(new TableColumn("notificationTitle",Application.getInstance().getMessage("asset.label.title","Title")));
			TableColumn coldate = new TableColumn("notificationDate",Application.getInstance().getMessage("asset.label.date","Date"));
			coldate.setFormat( new TableDateFormat("dd MMM yyyy"));
			addColumn(coldate);
			TableColumn coltime = new TableColumn("notificationTime",Application.getInstance().getMessage("asset.label.time","Time"));
			coltime.setFormat( new TableDateFormat("hh:mm"));
			addColumn(coltime);

			TableColumn colSender = new TableColumn("senderID",Application.getInstance().getMessage("asset.label.sendBy", "Send By"));
			colSender.setFormat(new TableFormat() {
				public String format(Object value) {		               	

					Application app = Application.getInstance();
					SecurityService ss=  (SecurityService)app.getService(SecurityService.class);

					try {
						return ss.getUser(value.toString()).getName();
					} catch (SecurityException e) {
						return value.toString();
					}

				}
			});
			addColumn(colSender);

			TableColumn colRepient = new TableColumn("notificationId",Application.getInstance().getMessage("asset.label.recipient", "Recipient"));
			colRepient.setFormat(new TableFormat() {
				public String format(Object value) {

					Application app = Application.getInstance(); 
					AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	
					SecurityService ss = (SecurityService)app.getService(SecurityService.class);

					Collection col = mod.retrieveAddressee(value.toString());
					String result="";
					if(col != null && col.size() > 0){
						for(Iterator iterator = col.iterator(); iterator.hasNext();){							
							DataAddressee objAdd = (DataAddressee)iterator.next();
							if(result.length() != 0)
								result += ", ";
							try {
								result +=  ss.getUser(objAdd.getRecipientId()).getName();
							} catch (SecurityException e) {
								result += objAdd.getRecipientId();
							}
						}
					}
					return result;
				}
			});
			addColumn(colRepient);

			TableColumn colNotify = new TableColumn("notifyMethod",Application.getInstance().getMessage("asset.label.notifyMethod", "Notify Methhod"));
			colNotify.setFormat(new TableFormat(){
				public String format(Object obj){					

					String result;
					String strNotifyMethod = obj.toString();
					if (strNotifyMethod.equals("m"))
						result = "Memo";
					else if(strNotifyMethod.equals("e"))
						result="Email";
					else  if(strNotifyMethod.equals("b"))
						result="Memo & Email";
					else
						result="Memo";

					return result;
				}
			});
			addColumn(colNotify);

			TableColumn colCreatedDate = new TableColumn("dateCreated",Application.getInstance().getMessage("asset.label.createddate", "Created Date"));
			colCreatedDate.setFormat( new TableDateFormat("dd MMM yyyy hh:mm"));
			addColumn(colCreatedDate);
			//addColumn( new TableColumn("dateCreated",Application.getInstance().getMessage("asset.label.createddate", "Created Date")));

			addFilter(new TableFilter("keyword"));

		}

		public Collection getTableRows() {

			String strTitle =(String)getFilterValue("keyword");

			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);		    

			return mod.listingNotification(strTitle, getSort(), isDesc(), getStart(), getRows());

		}

		public int getTotalRowCount() {
			String strTitle =(String)getFilterValue("keyword");

			Application app = Application.getInstance(); 
			AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);		    

			return mod.listingNotification(strTitle, getSort(), isDesc(), 0, -1).size();	
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

	public String getStrCategory() {
		return strCategory;
	}

	public void setStrCategory(String strCategory) {
		this.strCategory = strCategory;
	}

	public String getStrItem() {
		return strItem;
	}

	public void setStrItem(String strItem) {
		this.strItem = strItem;
	}
}
