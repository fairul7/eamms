package com.tms.fms.engineering.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.stdui.Button;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.core.model.InvalidKeyException;
import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorDao;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.transport.model.SetupObject;

/**
 * @author fahmi
 *
 */
public class OutsourceRequestFCForm extends Form {
    
	protected String requestId;   
	protected Radio rdCompany;
	protected Radio rdIndividual;
	protected Panel pnType;
	protected FileUpload attachment;	
	protected TextField estimatedCost;
	protected TextField actualCost;
	protected TextBox outsourceRemarks;
	protected SelectBox sbOutsourceCompany;
	
	protected Hidden stat;	
	
	protected String outsourceId;
	protected String fileId;
	
	protected Label nameLbl;	
	protected Label programLbl;
	protected Label submittedLbl;
	protected Label totalCostLbl;
	
	protected Button submitButton;
	protected Button cancelButton;
	
	//private String cancelUrl = "outsourceRequestFC.jsp?requestId=";
	private String cancelUrl = "requestDetails.jsp?requestId=";
	
	private String action = "";
	private String outsourceType = "company";
	private String formMode = "add";
	private String acDisplay = "hide";
	
	private Collection files = new ArrayList();
	
	public OutsourceRequestFCForm() {

    }
   
	public void init()
	{
		setColumns(1);
		removeChildren();
		setMethod("POST");
		String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");
		
		nameLbl = new Label("nameLbl");
		addChild(nameLbl);
		
		programLbl = new Label("programLbl");
		addChild(programLbl);
		
		submittedLbl = new Label("submittedLbl");
		addChild(submittedLbl);
		
		totalCostLbl = new Label("totalCostLbl");
		addChild(totalCostLbl);
		
		pnType = new Panel("pnType");
		rdCompany = new Radio("rdCompany", Application.getInstance().getMessage("fms.eng.setup.company", "Engineering"), true);
		rdCompany.setOnClick("populateCompanyName('company')");
		rdIndividual = new Radio("rdIndividual", Application.getInstance().getMessage("fms.eng.setup.individual", "Transport"));
		rdIndividual.setOnClick("populateCompanyName('individual')");
		rdCompany.setGroupName("typeGroup");
		rdIndividual.setGroupName("typeGroup");
		pnType.addChild(rdCompany);
		pnType.addChild(rdIndividual);
		addChild(pnType);
		    
		sbOutsourceCompany = new SelectBox("sbOutsourceCompany");
		
		listOutsourceCompany();
		addChild(sbOutsourceCompany);
		
		attachment = new FileUpload("attachment");
	    addChild(attachment);
		
	    estimatedCost = new TextField("estimatedCost");		
	    estimatedCost.setSize("7");
	    estimatedCost.setValue("0");
	    estimatedCost.addChild(new ValidatorIsNumeric("estimatedCostIsNumberic", msgIsNumberic, false));
		addChild(estimatedCost);
		
		actualCost = new TextField("actualCost");		
	    actualCost.setSize("7");
	    String ac = "0";
	    actualCost.setValue(ac);
	    actualCost.addChild(new ValidatorIsNumeric("actualCostIsNumberic", msgIsNumberic, false));
		addChild(actualCost);
		
		outsourceRemarks = new TextBox("outsourceRemarks");
		outsourceRemarks.setSize("60");
		outsourceRemarks.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));				
		addChild(outsourceRemarks);

		stat = new Hidden("stat");
		addChild(stat);
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.submit", "Submit"));
        addChild(submitButton);
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
        addChild(cancelButton);	
		
	}	 
	
	public void populateForm(String id){
		Application app = Application.getInstance();
		FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)app.getModule(FacilitiesCoordinatorModule.class);
		try{
			EngineeringRequest er = fcm.getRequestById(id);		
			
			nameLbl.setText(er.getTitle());
			
			programLbl.setText((er.getProgramName()!=null?er.getProgramName():"-"));
			
			submittedLbl.setText(er.getCreatedUserName() + " ["+ DateUtil.formatDate(SetupModule.DATE_FORMAT, er.getSubmittedDate())+"] ");
			
			totalCostLbl.setText(fcm.selectOutsourceCost(id) + "");
		} catch(Exception e){
		}
		
	}
	
	public String getDefaultTemplate() {
		return "fms/outsourceFCform";
    }
	
	public void onRequest(Event evt){
		
		//requestId = evt.getRequest().getParameter("requestId");
		outsourceId = evt.getRequest().getParameter("outsourceId");
		fileId = evt.getRequest().getParameter("idfile");
		action = evt.getRequest().getParameter("do");		
		files = null;
		estimatedCost.setValue("");
		actualCost.setValue("");
		outsourceRemarks.setValue("");
		sbOutsourceCompany.addSelectedOption("-1");	
		listOutsourceCompany();
		
		Application application = Application.getInstance();
		FacilitiesCoordinatorModule module = (FacilitiesCoordinatorModule)application.getModule(FacilitiesCoordinatorModule.class);
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao)application.getModule(FacilitiesCoordinatorModule.class).getDao();
		
		if (action != null && !action.equals("")){
			if (action.equals("delete")){
				if (fileId != null && !fileId.equals("")){
					EngineeringRequest erdel = module.getFile(fileId);
					StorageFile file = new StorageFile(module.OUTSOURCE_FILE_PATH + "/" + fileId, erdel.getFileName());
					StorageFile file2 = new StorageFile(module.OUTSOURCE_FILE_PATH, fileId);
					try {
						dao.deleteFile(file);
						dao.deleteFile(file2);
						module.deleteOutsourceFile(erdel.getFileId());
					} catch (DaoException e) {
						Log.getLog(getClass()).error(e.toString(), e);
					} catch (InvalidKeyException e) {
						Log.getLog(getClass()).error(e.toString(), e);
					}
				}
			}
		}
		
		if (getRequestId() != null){
			init();
			if (getOutsourceId() != null && !getOutsourceId().equals("")){
				files = module.getFiles(getOutsourceId());
				
				EngineeringRequest er = module.getRequestById(getRequestId(), getOutsourceId());
				estimatedCost.setValue(er.getEstimatedCost());
				actualCost.setValue(er.getActualCost());
				outsourceRemarks.setValue(er.getDescription());
				
				if (er.getOutsourceType().equals("C")){
					rdCompany.setChecked(true);
					rdIndividual.setChecked(false);
					outsourceType = "company";
				} else {
					rdIndividual.setChecked(true);
					rdCompany.setChecked(false);
					outsourceType = "individual";
				}
				sbOutsourceCompany.addSelectedOption(er.getCompanyId());
				
				formMode = "edit";
				acDisplay = "show";
				if (Float.parseFloat(er.getActualCost()) > 0){
					acDisplay = "show";
				} else {
					acDisplay = "hide";
				}
			} else {
				formMode = "add";
				acDisplay = "add";
			}
			populateForm(getRequestId());
		} else {
			init();
		}
	}

	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);

	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && cancelButton.getAbsoluteName().equals(buttonName)) {
	    	init();
	      	return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl() + getRequestId(), true);
	    } else {
	    	return result;
	    }
	    
	}
	
	public Forward onValidate(Event evt){
		Application application = Application.getInstance();
		FacilitiesCoordinatorModule module = (FacilitiesCoordinatorModule)application.getModule(FacilitiesCoordinatorModule.class);
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao)application.getModule(FacilitiesCoordinatorModule.class).getDao();
		
		EngineeringRequest er = new EngineeringRequest();
		er.setOutsourceId(UuidGenerator.getInstance().getUuid());
		er.setRequestId(getRequestId());
		er.setCompanyId(getSelectBoxValue(sbOutsourceCompany));
		er.setEstimatedCost((String)estimatedCost.getValue());
		er.setActualCost((String)actualCost.getValue());
		er.setDescription((String)outsourceRemarks.getValue());
		er.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
		Date now = new Date();
		er.setCreatedOn(now);
		er.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
		er.setModifiedOn(now);
		if (rdCompany.isChecked()){
			er.setOutsourceType("C");
			
			if ("-1".equals(er.getCompanyId())){
				sbOutsourceCompany.setInvalid(true);
				return new Forward("selectOutsourceCompany");
			}
		} else {
			er.setOutsourceType("I");
		}
		
		if (getOutsourceId() != null && !getOutsourceId().equals("")){
			er.setOutsourceId(getOutsourceId());
			module.updateOutsource(er);
		} else {
			module.insertOutsource(er);
		}
		
		try {
			if (attachment.getValue() != null){
				er.setFileId(UuidGenerator.getInstance().getUuid());
				StorageFile file = new StorageFile(module.OUTSOURCE_FILE_PATH + "/" + er.getFileId() , attachment.getStorageFile(evt.getRequest()));				
				
				er.setFileName(file.getName());
				er.setFilePath(file.getAbsolutePath());
				
				dao.storeFile(file);
				module.insertOutsourceAttachment(er);
			} 
		} catch (IOException ioe){
			Log.getLog(getClass()).error(ioe.toString(), ioe);
		} catch (StorageException se){
			Log.getLog(getClass()).error(se.toString(), se);
		}catch (Exception e) {
		}
		
		Forward fwd = new Forward("continue", "outsourceRequestFC.jsp?requestId=" + getRequestId(), true);
		init();
		return fwd;
	}		
	
	private String getSelectBoxValue(SelectBox sb) {
	    if (sb != null) {
	    	Map selected = sb.getSelectedOptions();
	    	if (selected.size() == 1) {
	    		return (String)selected.keySet().iterator().next();
	    	}
	    }
	    return null;
	}

	public void listOutsourceCompany(){
		try {
			sbOutsourceCompany.removeAllOptions();
			sbOutsourceCompany.addOption("-1", "Select Company");
			FacilitiesCoordinatorModule mod = (FacilitiesCoordinatorModule)Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
			Collection lstCompany = null; 
			
			lstCompany = mod.getOutsourceCompany();
			if (lstCompany.size() > 0 ){
				for (Iterator i=lstCompany.iterator(); i.hasNext();){
					SetupObject o = (SetupObject)i.next();
					sbOutsourceCompany.addOption(o.getSetup_id(), o.getName());
				}
			}
		} catch (Exception e){
			
		}
	}
	
	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public String getCancelUrl(){
		return cancelUrl;
	}	
	public void setCancelUrl(String cancelUrl){
		this.cancelUrl=cancelUrl;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Collection getFiles() {
		return files;
	}

	public void setFiles(Collection files) {
		this.files = files;
	}

	public String getOutsourceId() {
		return outsourceId;
	}

	public void setOutsourceId(String outsourceId) {
		this.outsourceId = outsourceId;
	}

	public Label getNameLbl() {
		return nameLbl;
	}

	public void setNameLbl(Label nameLbl) {
		this.nameLbl = nameLbl;
	}

	public Label getProgramLbl() {
		return programLbl;
	}

	public void setProgramLbl(Label programLbl) {
		this.programLbl = programLbl;
	}

	public Label getSubmittedLbl() {
		return submittedLbl;
	}

	public void setSubmittedLbl(Label submittedLbl) {
		this.submittedLbl = submittedLbl;
	}

	public Label getTotalCostLbl() {
		return totalCostLbl;
	}

	public void setTotalCostLbl(Label totalCostLbl) {
		this.totalCostLbl = totalCostLbl;
	}

	public Radio getRdCompany() {
		return rdCompany;
	}

	public void setRdCompany(Radio rdCompany) {
		this.rdCompany = rdCompany;
	}

	public Radio getRdIndividual() {
		return rdIndividual;
	}

	public void setRdIndividual(Radio rdIndividual) {
		this.rdIndividual = rdIndividual;
	}

	public Panel getPnType() {
		return pnType;
	}

	public void setPnType(Panel pnType) {
		this.pnType = pnType;
	}

	public FileUpload getAttachment() {
		return attachment;
	}

	public void setAttachment(FileUpload attachment) {
		this.attachment = attachment;
	}

	public TextField getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(TextField estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public TextBox getOutsourceRemarks() {
		return outsourceRemarks;
	}

	public void setOutsourceRemarks(TextBox outsourceRemarks) {
		this.outsourceRemarks = outsourceRemarks;
	}

	public SelectBox getSbOutsourceCompany() {
		return sbOutsourceCompany;
	}

	public void setSbOutsourceCompany(SelectBox sbOutsourceCompany) {
		this.sbOutsourceCompany = sbOutsourceCompany;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public TextField getActualCost() {
		return actualCost;
	}

	public void setActualCost(TextField actualCost) {
		this.actualCost = actualCost;
	}

	public String getOutsourceType() {
		return outsourceType;
	}

	public void setOutsourceType(String outsourceType) {
		this.outsourceType = outsourceType;
	}

	public String getFormMode() {
		return formMode;
	}

	public void setFormMode(String formMode) {
		this.formMode = formMode;
	}

	public String getAcDisplay() {
		return acDisplay;
	}

	public void setAcDisplay(String acDisplay) {
		this.acDisplay = acDisplay;
	}
	
}
