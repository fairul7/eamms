package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleRequest;


public class TransportVehicle extends Form
{
    
	
	public static int value = 1;
    
    private Button addButton;
    private Button[] deleteButton;
    private String requestId;
    
    private SelectBox category;
    private TextField qty;
    private TextField driver;
    private CheckBox selectItem;
        
    private Collection vehicles = null;
    private Collection request = null;
    
           
    public TransportVehicle()
    {
    }

    public TransportVehicle(String name)
    {
        super(name);
    }

    public void init()
    {    	
    	    	
    	category = new SelectBox("category");
    	    	
    	qty = new TextField("qty");
    	qty.setSize("5");
    	//qty.addChild(new ValidatorNotEmpty("val"));
    	driver = new TextField("driver");
    	driver.setSize("5");
    	    	
    	////
    	    	
	    	Collection collcat = new ArrayList();
	    	try{
	    		TransportModule tran = (TransportModule) Application.getInstance().getModule(TransportModule.class);        		
	    		collcat = tran.selectSetupObject("fms_tran_category",null,"-1",null,false,0,-1);
	    		category.addOption("-1", "--- NONE ---");
	    		for(Iterator it = collcat.iterator(); it.hasNext(); ){
	    			SetupObject so = (SetupObject) it.next();
	    			String id = so.getSetup_id();
	    			String name = so.getName();
	    			String status = so.getStatus();
	    			if (status.equals("1")) {
	    				category.addOption(id, name);
	    			}
	    		}
	    		
	    		int i = 0;
	    		request = tran.getVehicles(requestId, true);
	    		deleteButton = new Button[request.size()];
	    		for(Iterator it = request.iterator(); it.hasNext(); ){
	    			VehicleRequest vr = (VehicleRequest) it.next();
	    			String id = vr.getId();
	    			
	    			deleteButton[i] = new Button(id,"Delete");
	    			addChild(deleteButton[i]);
	    			i++;
	    		}
	    		
	    	}catch(Exception er){
	    		Log.getLog(getClass()).error(er);
	    	}
    	
    	
    	addButton = new Button("addButton");
    	addButton.setText("Add");    	
    	selectItem = new CheckBox("selectItem");    	
    	addChild(category);
    	addChild(addButton);    	
    	addChild(qty);
    	addChild(driver);
    	addChild(selectItem);    	
    }

    
    public void onRequest(Event evt)
    {
    	requestId = evt.getRequest().getParameter("id");  
    	init();
        
    }

    
    public void refresh(){
        
    }

    public Forward onValidate(Event evt)
    {
        super.onValidate(evt);
        String button = findButtonClicked(evt);
        TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
        
        int d = 0;
        evt.getRequest().setAttribute("id", requestId);
        if(addButton.getAbsoluteName().equals(button)){
        	//insertTransportVehicle
        		    		
        	if(!("".equals(qty.getValue())	|| 
        			"-1".equals(category.getSelectedOptions().keySet().iterator().next()))){
        	
	        	VehicleRequest vr = new VehicleRequest();
	        	
	        	vr.setId(UuidGenerator.getInstance().getUuid());
	        	vr.setCategory_id((String)category.getSelectedOptions().keySet().iterator().next());
	        	vr.setRequestId(requestId);
	        	int q = Integer.parseInt((String) qty.getValue());
	        	
	        	if(!(driver.getValue() == null || "".equals(driver.getValue())))
	        		d = Integer.parseInt((String) driver.getValue());
	        	
	        	vr.setQuantity(q);
	        	vr.setDriver(d);
	        	
	        	try{
	        		tm.insertTransportVehicle(vr);
	        	}catch(Exception e){
	        		
	        	}
	        	Log.getLog(getClass()).info("Save!! :"+category.getSelectedOptions().keySet().iterator().next()+
	        			"| "+qty.getValue()+" | "+driver.getValue()+ " |");
        	}
        	
        }
        else{
        	for(int i = 0; i < request.size(); i++){
        		String id = "";
        		if(deleteButton[i].getAbsoluteName().equals(button))
        			//deleteTransportVehicle
        			id = deleteButton[i].getName();
        			tm.deleteTransportVehicle(id);
        			Log.getLog(getClass()).info("DELETE: "+deleteButton[i].getName());
        	}
        
        }
        try{
        	
		    evt.getResponse().sendRedirect("addNewRequest.jsp?id="+requestId);
		    
		    }catch(Exception e){
		    	Log.getLog(getClass()).error("Error redirecting to URL " + requestId, e);
		    }
        init();
        return null;
    }

    public String getDefaultTemplate()
    {
        return "fms/transport/vehicle";
    }

    
    public Button getAddButton() {
		return addButton;
	}

	public void setAddButton(Button addButton) {
		this.addButton = addButton;
	}

	public CheckBox getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(CheckBox selectItem) {
		this.selectItem = selectItem;
	}

	public static int getValue() {
		return value;
	}

	public static void setValue(int value) {
		TransportVehicle.value = value;
	}

	public SelectBox getCategory() {
		return category;
	}

	public void setCategory(SelectBox category) {
		this.category = category;
	}

	public TextField getQty() {
		return qty;
	}

	public void setQty(TextField qty) {
		this.qty = qty;
	}

	public TextField getDriver() {
		return driver;
	}

	public void setDriver(TextField driver) {
		this.driver = driver;
	}

	public Button[] getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(Button[] deleteButton) {
		this.deleteButton = deleteButton;
	}

	public Collection getVehicles() {
		return vehicles;
	}

	public void setVehicles(Collection vehicles) {
		this.vehicles = vehicles;
	}

	public Collection getRequest() {
		return request;
	}

	public void setRequest(Collection request) {
		this.request = request;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


}
