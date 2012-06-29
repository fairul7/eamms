package com.tms.sam.po.setting.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.setting.model.ConfigModule;
import com.tms.sam.po.setting.model.ConfigObject;

public class GlobalSetting extends Form{
	protected SelectBox fileSizeUpload;
	
	protected TextField companyName;
	protected TextField companyTel;
	protected TextField companyFax;
	
	protected TextBox priority;
//	protected TextBox attachmentFileExt;
	protected TextBox companyAddr;
	
	protected Button btnSubmit;
	protected Button btnCancel;
	
	protected Panel btnPanel;
	
	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_INVALID_EXT = "invalid file ext";
    public static final String FORWARD_INVALID_File= "invalid file";
    
    public void init(){
    	initForm();
    }
    
    public void initForm(){
    	setMethod("POST");
		removeChildren();
		setColumns(2);
		
		Application app = Application.getInstance();
		fileSizeUpload = new SelectBox("fileSizeUpload");
    	fileSizeUpload.setMultiple(false);
    	fileSizeUpload.setOptionMap(getRoleOptionList());
    	
    	
		companyName = new TextField("companyName");
		companyName.addChild(new ValidatorNotEmpty("companyNameVNE", ""));
		companyTel = new TextField("companyTel");
		companyTel.addChild(new ValidatorNotEmpty("companyTelVNE", ""));
		companyFax = new TextField("companyFax");
//		companyFax.addChild(new ValidatorNotEmpty("companyFaxVNE", ""));
		
		priority = new TextBox("Priority");
		priority.setCols("50");
    	priority.setRows("5");
    	priority.addChild(new ValidatorNotEmpty("priorityVNE", ""));
    	
//		attachmentFileExt = new TextBox("attachmentFileExt");
//		attachmentFileExt.setCols("50");
//    	attachmentFileExt.setRows("5");
//    	attachmentFileExt.addChild(new ValidatorNotEmpty("attachmentFileExtVNE", ""));
//    	
		companyAddr = new TextBox("companyAddr");
		companyAddr.addChild(new ValidatorNotEmpty("companyAddrVNE", ""));
		
		btnPanel = new Panel("btnPanel");
		btnPanel.setColumns(2);
	
		btnSubmit = new Button("btnSubmit", app.getMessage("po.label.submit"));
	    btnPanel.addChild(btnSubmit);
	        
	    btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("po.label.cancel"));
	    btnPanel.addChild(btnCancel);
	    
		addChild(fileSizeUpload);
		addChild(companyName);
		addChild(companyTel);
		addChild(companyFax);
		addChild(priority);
//		addChild(attachmentFileExt);
		addChild(companyAddr);
		addChild(btnPanel);
    }
    
    private Map getRoleOptionList() {
    	Map optionList = new SequencedHashMap();
    	
    	optionList.put("", Application.getInstance().getMessage("po.label.pleaseSelect","--Please Select--"));
    	optionList.put("256KB", "256KB");
    	optionList.put("512KB", "512KB");
    	optionList.put("1MB", "1MB");
    	optionList.put("2MB", "2MB");
    	optionList.put("3MB", "3MB");
    	optionList.put("4MB", "4MB");
    	optionList.put("5MB", "5MB");
    	optionList.put("Unlimited", "Unlimited");
    	return optionList;
    }
    
//    public Forward onSubmit(Event event) {
//    	Forward forward = super.onSubmit(event);
//        
//    	String fileExtList = attachmentFileExt.getValue().toString();
//    	ArrayList fileExts = getTextBoxValuesByLine(fileExtList, ".");
//    	ArrayList invalidFileExts = null;
//    	
//    	for(int i=0; i<fileExts.size(); i++) {
//    		String extItem = (String) fileExts.get(i);
//    		if(extItem.indexOf(".") != extItem.lastIndexOf(".") || 
//    				!extItem.startsWith(".")) {
//    			attachmentFileExt.setInvalid(true);
//    			this.setInvalid(true);
//    			
//    			if(invalidFileExts == null)
//    				invalidFileExts = new ArrayList();
//    			invalidFileExts.add(extItem);
//    		}
//    	}
//    	
//    	if(invalidFileExts != null) {
//    		event.getRequest().setAttribute("invalidFileExts", invalidFileExts);
//    		return new Forward(FORWARD_INVALID_EXT);
//    	}
//    	else {
//    		return forward;
//    	}
//    	
//    }
	
    public void onRequest(Event ev) {
    	initForm();
    	
    	ConfigModule module = (ConfigModule)Application.getInstance().getModule(ConfigModule.class);
		ConfigObject config = new ConfigObject();
		Collection cols;
		
//		cols = module.getConfigDetailsByType(ConfigObject.FILE_SIZE_UPLOAD, null);
//		if(cols != null) {
//			if(cols.size() > 0) {
//				config = (ConfigObject) cols.iterator().next();
//				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
//					fileSizeUpload.setSelectedOption(config.getConfigDetailName());
//				}
//			}
//		}
		
		cols = module.getConfigDetailsByType(ConfigObject.PRIORITY, null);
		String priorityList = "";
		int j=0;
		for(Iterator i=cols.iterator(); i.hasNext(); j++) {
			config = (ConfigObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				if(j != 0)
					priorityList += "\n";
				priorityList += config.getConfigDetailName();
			}
		}
		if(!"".equals(priorityList)) {
			priority.setValue(priorityList);
		}
		
//		cols = module.getConfigDetailsByType(ConfigObject.ALLOWED_FILE_EXTENSION, null);
//		String fileExtList = "";
//		j=0;
//		for(Iterator i=cols.iterator(); i.hasNext(); j++) {
//			config = (ConfigObject) i.next();
//			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
//				if(j != 0)
//					fileExtList += "\n";
//				fileExtList += config.getConfigDetailName();
//			}
//		}
//		if(!"".equals(fileExtList)) {
//			attachmentFileExt.setValue(fileExtList);
//		}
		
		cols = module.getConfigDetailsByType(ConfigObject.COMPANY_NAME, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					companyName.setValue(config.getConfigDetailName());
				}
			}
		}
		
		cols = module.getConfigDetailsByType(ConfigObject.COMPANY_ADDRESS, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					companyAddr.setValue(config.getConfigDetailName());
				}
			}
		}
		
		cols = module.getConfigDetailsByType(ConfigObject.COMPANY_FAX, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					companyFax.setValue(config.getConfigDetailName());
				}
			}
		}
		
		cols = module.getConfigDetailsByType(ConfigObject.COMPANY_TEL, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					companyTel.setValue(config.getConfigDetailName());
				}
			}
		}
    }
    
	public Forward onValidate(Event event) {
		ConfigModule module = (ConfigModule)Application.getInstance().getModule(ConfigModule.class);
		ConfigObject config = new ConfigObject();
		boolean isSuccess = false;
		
//		if(module.deleteConfigDetailsByType(ConfigObject.FILE_SIZE_UPLOAD)) {
//			String selectedFileSize = (String) fileSizeUpload.getSelectedOptions().keySet().iterator().next();
//			config.setConfigDetailName(selectedFileSize);
//			config.setConfigDetailDescription(selectedFileSize);
//			config.setConfigDetailType(ConfigObject.FILE_SIZE_UPLOAD);
//	    	config.setConfigDetailOrder(new Integer(1));
//	    	isSuccess = module.insertConfigDetail(config);
//		}
		
		if(module.deleteConfigDetailsByType(ConfigObject.PRIORITY)) {
	    	String priorityList = priority.getValue().toString();
	    	ArrayList priorities = getTextBoxValuesByLine(priorityList, null);
	    	for(int i=0; i<priorities.size(); i++) {
	    		String priorityValue = priorities.get(i).toString();
	    		config = new ConfigObject();
	    		config.setConfigDetailName(priorityValue);
	    		config.setConfigDetailDescription(priorityValue);
	    		config.setConfigDetailType(ConfigObject.PRIORITY);
	        	config.setConfigDetailOrder(new Integer(i + 1));
	        	isSuccess = module.insertConfigDetail(config);
	    	}
		}
		
//		if(module.deleteConfigDetailsByType(ConfigObject.ALLOWED_FILE_EXTENSION)) {
//	    	String fileExtList = attachmentFileExt.getValue().toString();
//	    	ArrayList fileExts = getTextBoxValuesByLine(fileExtList, ".");
//	    	for(int i=0; i<fileExts.size(); i++) {
//	    		String fileExtValue = fileExts.get(i).toString();
//	    		
//	    		config = new ConfigObject();
//	    		config.setConfigDetailName(fileExtValue);
//	    		config.setConfigDetailDescription(fileExtValue);
//	    		config.setConfigDetailType(ConfigObject.ALLOWED_FILE_EXTENSION);
//	        	config.setConfigDetailOrder(new Integer(i + 1));
//	        	isSuccess = module.insertConfigDetail(config);
//	    	}
//		}
		
		if(module.deleteConfigDetailsByType(ConfigObject.COMPANY_NAME)) {
			String name = companyName.getValue().toString();
			config = new ConfigObject();
			config.setConfigDetailName(name);
			config.setConfigDetailDescription(name);
			config.setConfigDetailType(ConfigObject.COMPANY_NAME);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = module.insertConfigDetail(config);
		}
		
		if(module.deleteConfigDetailsByType(ConfigObject.COMPANY_TEL)) {
			String tel = companyTel.getValue().toString();
			config = new ConfigObject();
			config.setConfigDetailName(tel);
			config.setConfigDetailDescription(tel);
			config.setConfigDetailType(ConfigObject.COMPANY_TEL);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = module.insertConfigDetail(config);
		}
		
		if(module.deleteConfigDetailsByType(ConfigObject.COMPANY_FAX)) {
			String fax = companyFax.getValue().toString();
			config = new ConfigObject();
			config.setConfigDetailName(fax);
			config.setConfigDetailDescription(fax);
			config.setConfigDetailType(ConfigObject.COMPANY_FAX);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = module.insertConfigDetail(config);
		}
		
		if(module.deleteConfigDetailsByType(ConfigObject.COMPANY_ADDRESS)) {
			String addr = companyAddr.getValue().toString();
			config = new ConfigObject();
			config.setConfigDetailName(addr);
			config.setConfigDetailDescription(addr);
			config.setConfigDetailType(ConfigObject.COMPANY_ADDRESS);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = module.insertConfigDetail(config);
		}
		
		if(isSuccess) {
    		return new Forward(FORWARD_SUCCESS);
    	}
    	else {
    		return new Forward(FORWARD_ERROR);
    	}
	}
	
	 private ArrayList getTextBoxValuesByLine(String value, String mandatoryPrefix) {
	    	ArrayList values = new ArrayList();
	    	char textChar[] = value.toCharArray();
	        String delimitedString = "";
	        boolean isNewString = true;
	        
	        for(int i=0; i<textChar.length; i++) {
	            int ascii = textChar[i];
	            // If textChar[i] not line feed
	            if(ascii != 10) {
		            if(isNewString) {
		            	if(ascii != 13) {
			            	delimitedString = String.valueOf(textChar[i]);
			            	isNewString = false;
		            	}
		            }
		            else {
		            	// If textChar[i] is carriage return, then finalize the current item
		            	if(ascii == 13) {
		            		if(!"".equals(delimitedString.trim())) {
		            			if(mandatoryPrefix != null && !"".equals(mandatoryPrefix)) {
			            			if(delimitedString.indexOf(mandatoryPrefix) == -1) {
			                    		delimitedString = mandatoryPrefix + delimitedString;
			                    	}
		            			}
		            			values.add(delimitedString);
		            		}
		            		isNewString = true;
		                }
		                else {
		                	delimitedString += textChar[i];
		                }
		            }
	            }
	        }
	        if(!"".equals(delimitedString.trim())) {
	        	if(mandatoryPrefix != null && !"".equals(mandatoryPrefix)) {
	    			if(delimitedString.indexOf(mandatoryPrefix) == -1) {
	            		delimitedString = mandatoryPrefix + delimitedString;
	            	}
				}
	        	values.add(delimitedString);
	        }
	        
	        return values;
	    }
	 
	public String getTemplate() {
		return "po/globalSetting";
	}

//	public TextBox getAttachmentFileExt() {
//		return attachmentFileExt;
//	}
//
//	public void setAttachmentFileExt(TextBox attachmentFileExt) {
//		this.attachmentFileExt = attachmentFileExt;
//	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public TextBox getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(TextBox companyAddr) {
		this.companyAddr = companyAddr;
	}

	public TextField getCompanyFax() {
		return companyFax;
	}

	public void setCompanyFax(TextField companyFax) {
		this.companyFax = companyFax;
	}

	public TextField getCompanyName() {
		return companyName;
	}

	public void setCompanyName(TextField companyName) {
		this.companyName = companyName;
	}

	public TextField getCompanyTel() {
		return companyTel;
	}

	public void setCompanyTel(TextField companyTel) {
		this.companyTel = companyTel;
	}

	public SelectBox getFileSizeUpload() {
		return fileSizeUpload;
	}

	public void setFileSizeUpload(SelectBox fileSizeUpload) {
		this.fileSizeUpload = fileSizeUpload;
	}

	public TextBox getPriority() {
		return priority;
	}

	public void setPriority(TextBox priority) {
		this.priority = priority;
	}
	
	
}
