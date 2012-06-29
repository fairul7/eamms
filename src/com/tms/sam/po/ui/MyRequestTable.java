package com.tms.sam.po.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.permission.model.PermissionModel;

public class MyRequestTable extends Table {
	private String  status;
	private String purchaseCode;
	public void init(){
		setWidth("100%");
		setModel(new MyRequestTableModel());
	}
	
	public class MyRequestTableModel extends TableModel{
		public MyRequestTableModel(){
			Application app = Application.getInstance();
        	
            TableColumn idCol = new TableColumn("combinedData", app.getMessage("myRequest.label.id","ID"));
            idCol.setFormat(new TableFormat() {
                public String format(Object value) {
                	String data = value.toString();
                	String ppID = data.substring(0,data.indexOf("_"));
                	purchaseCode = data.substring(data.indexOf("_")+1,data.lastIndexOf("_"));
                	status = data.substring(data.lastIndexOf("_")+1);
                	if(status.equals("Draft")){
                		return  "<a href=prepurchaseRequestForm.jsp?ppID="+ppID+"&status="+status+">"+purchaseCode+"</a>";
                	}else{
                		return  "<a href=viewMyRequest.jsp?id="+ppID+"&status="+status+">"+purchaseCode+"</a>";
                	}
                }
            });
       
            addColumn(idCol);
            
            TableColumn items = new TableColumn("delimitedItems", app.getMessage("myRequest.label.itemRequested", "Item Requested"));
            items.setSortable(false);
            addColumn(items);
            
            addColumn(new TableColumn("status", app.getMessage("myRequest.label.status","Status")));
	       
            
            TableColumn dateCreated = new TableColumn("dateCreated", "Date/Time Created");
            TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
            dateCreated.setFormat(dateCreatedFormat);
            addColumn(dateCreated);
            
			TableColumn neededBy = new TableColumn("neededBy", app.getMessage("po.label.needed","Needed By"));
			TableFormat neededByFormat = new TableDateFormat(app.getProperty("globalDateLong"));
			neededBy.setFormat(neededByFormat);
            addColumn(neededBy);
            
   /*         addFilter(new TableFilter("name"));
            
            SelectBox searchCol = new SelectBox("searchCol");
            searchCol.setMultiple(false);
            Map searchColMap = new SequencedHashMap();
            searchColMap.put("purchaseCode", app.getMessage("myRequest.label.id","ID"));
            searchCol.setOptionMap(searchColMap);
            
            TableFilter searchColFilter = new TableFilter("searchColFilter");
            searchColFilter.setWidget(searchCol);
            addFilter(searchColFilter);*/
            
            SelectBox status = new SelectBox("statust");
            status.setOptionMap(getStatusOptionsMap());
			TableFilter statusFilter = new TableFilter("statusFilter");
			statusFilter.setWidget(status);
			addFilter(statusFilter);
			
            addAction(new TableAction("add", "Add", ""));
            addAction(new TableAction("withdraw", "Withdraw", app.getMessage("po.message.confirmWithdraw")));
            
    
		}
		
		public Collection getTableRows() {
			
            String name = (String) getFilterValue("name");
      //      SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
      //      List cols = (List) searchCol.getValue();
            String searchColValue = "ID";
      //      if (cols.size() > 0) {
      //          searchColValue = (String) cols.get(0);
     //       }
            
			String status = new String();
			SelectBox tempStatus = (SelectBox) getFilter("statusFilter").getWidget();
			List statusList = (List) tempStatus.getValue();
            if (statusList.size() > 0) {
            	status = (String) statusList.get(0);
           } else {
            	status = "";
            }
	//		String status = "ID"; 
			
			Application app = Application.getInstance();
			PrePurchaseModule module = (PrePurchaseModule) app.getModule(PrePurchaseModule.class);
	    	
			return module.getRequestItems(name, searchColValue, status, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application app = Application.getInstance();
            String name = (String) getFilterValue("name");
            SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
            List cols = (List) searchCol.getValue();
            String searchColValue = "";
            if (cols.size() > 0) {
                searchColValue = (String) cols.get(0);
            }
           
            PrePurchaseModule module = (PrePurchaseModule) app.getModule(PrePurchaseModule.class);
            
        
            return module.countRequest(name, searchColValue);
            
		}
		
		public String getTableRowKey() {
	            return "ppID";
	    }
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
	            Application application = Application.getInstance();
	            PrePurchaseModule module = (PrePurchaseModule) application.getModule(PrePurchaseModule.class);
	            
	            if ("withdraw".equals(action)) {
	            	getUserID(selectedKeys);
	                for (int i=0; i<selectedKeys.length; i++) {
	                    module.deleteRequest(selectedKeys[i]);
	                }
	               
	            }
	            
	            if ("add".equals(action)) {
	               return new Forward("Add");
	            }
	            return null;
	   }
	}
	
	public void getUserID(String [] selectedKeys) {
    	
    	Collection userID = new ArrayList();
    	Collection id = null;
    	PrePurchaseModule ppModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
    	
    	userID = ppModule.getHOD(Application.getInstance().getCurrentUser().getId());
    	for(int j = 0; j<selectedKeys.length;j++){
        	PurchaseRequestObject oneRequest = ppModule.singleRequest(selectedKeys[j]);
        	String status = oneRequest.getStatus();
        	
        	if(status.equals("Approved by HOD")){
        		id = model.groupOfUser("po.permission.manageQuotation");
        		for(Iterator i = id.iterator();i.hasNext();){
        			HashMap map = (HashMap)i.next();
        			userID.add(map);
        		}
        		
        	}else if(!status.equals("New")){
        		id = model.groupOfUser("po.permission.manageQuotation");
        		for(Iterator i = id.iterator();i.hasNext();){
        			HashMap map = (HashMap)i.next();
        			userID.add(map);
        		}
        		
        		id = model.groupOfUser("po.permission.approveBudget");
        		for(Iterator i = id.iterator();i.hasNext();){
        			HashMap map = (HashMap)i.next();
        			userID.add(map);
        		}
        	}
        }

    	
    	sendNotification(userID, selectedKeys);
    } 
    
	public Map getStatusOptionsMap() {
		
		Map optionsMap = new SequencedHashMap();
		
		optionsMap.put("", "---All Status---");
		optionsMap.put("Draft","Draft");
		optionsMap.put("New","New");
		optionsMap.put("Withdrawn","Withdrawn");
		optionsMap.put("Resubmitted","Resubmitted");
		optionsMap.put("Approved by HOD","Approved by HOD");
		optionsMap.put("Rejected by HOD","Rejected by HOD");
		optionsMap.put("Quoted","Quoted");
		optionsMap.put("Approved by BO","Approved by BO");
		optionsMap.put("Rejected by BO","Rejected by BO");
		optionsMap.put("Re-Quote","Re-Quote");
		optionsMap.put("PO Fulfilled","PO FulFilled");
		optionsMap.put("Closed","Closed");
		return optionsMap;
    }
	
    public void sendNotification(Collection userID, String [] selectedKeys){
    	Log log = Log.getLog(getClass());
    	PrePurchaseModule module = (PrePurchaseModule)Application.getInstance().getModule(PrePurchaseModule.class);
    	try {
             Application app = Application.getInstance();
             MessagingModule mm;
             User user;
             SmtpAccount smtpAccount;
             Message message;

             mm = Util.getMessagingModule();
             user = app.getCurrentUser();
             smtpAccount = mm.getSmtpAccountByUserId(user.getId());

             // construct the message to send
             message = new Message();
             message.setMessageId(UuidGenerator.getInstance().getUuid());
             
             IntranetAccount intranetAccount;

            // intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(getWidgetManager().getUser().getId());
            // message.setFrom(intranetAccount.getFromAddress());
            List HODList;
            HODList = new ArrayList(userID.size());
            for (Iterator i = userID.iterator(); i.hasNext();) {
         		HashMap map = (HashMap) i.next();
                intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId((String) map.get("userId"));
                if(intranetAccount != null) {
		            String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
		            HODList.add(add);
                }
     		}
        
            if (HODList.size() > 0)
                message.setToIntranetList(HODList);

            message.setSubject(app.getMessage("po.label.prePurchase", "Pre-purchase Request") + ": " + 
                    app.getMessage("po.label.withdrawRequest", "Request is being withdrawn"));
            
            String codes="";
            for(int i = 0; i<selectedKeys.length;i++){
            	PurchaseRequestObject oneRequest = module.singleRequest(selectedKeys[i]);
            	if(i>0){
            		codes += ", ";
            	}
            	codes += oneRequest.getPurchaseCode();
            }
           
            String notificationBody = "<p>User <i>" 
            	+ user.getName() 
            	+ "</i> has withdrew request (" 
            	+ codes
            	+ ").</p>" ;
            message.setBody(notificationBody);

            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setDate(new Date());

            mm.sendMessage(smtpAccount, message, user.getId(), false);
        }
        catch (Exception e) {
            log.error("Error sending notification: ", e);
        }
        
        
    }

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

