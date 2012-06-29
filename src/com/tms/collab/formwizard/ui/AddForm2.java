package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.services.security.SecurityService;
import kacang.services.security.Group;
import kacang.services.security.ui.UsersSelectBox;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoQuery;



import java.util.*;
import java.io.InputStream;

import org.apache.commons.collections.SequencedHashMap;
import org.jdom.Document;
import org.jdom.Element;

import javax.servlet.http.HttpServletRequest;

import com.tms.collab.formwizard.model.*;



public class AddForm2 extends Form{
    public static final String FORMNAME_EXISTS="Form Exists";
        
    protected TextField formName;
    protected RichTextBox formHeader;
    protected Radio privateFormAccess;
    protected Radio publicFormAccess;
    protected Radio privateFormUserAccess;
    protected Radio publicFormUserAccess;
    protected Radio groupFormUserAccess;
    protected UsersSelectBox sbAccessUsers;
    protected ComboSelectBox cbGroups;
    protected RichTextBox submissionMessage;
    protected Radio yesDB;
    protected Radio noDB;
    protected TextField emailTo;
    protected UsersSelectBox sbMemoTo;
    protected Radio workFlowYes;
    protected Radio workFlowNo;
    protected UsersSelectBox sbApprover;
    protected CheckBox memoApproveNotification;
    protected CheckBox emailApproveNotification;
    protected SelectBox sbForms;
    protected SelectBox sbDuplicateFieldForm;
    protected Button submit;
	protected ResetButton reset;
    protected UsersSelectBox sbQueryBy;
    protected ButtonGroup approveNotification;
    protected TextField tfTableColumn;



    private String formId;
    public AddForm2() {
    }

    public AddForm2(String s) {
        super(s);
    }
    
	

    public void init(){
        String memo = null;

        memo = Application.getInstance().getProperty(FormModule.PROPERTY_MEMO);


        formName = new TextField("formName");
        formName.setMaxlength("50");
        formName.addChild(new ValidatorNotEmpty("notEmpty", Application.getInstance().getMessage("formWizard.label.addFrom.formNameNotEmpty","Form name cannot be empty")));

        
        formHeader = new RichTextBox("formHeader");
        formHeader.addChild(new ValidatorNotEmpty("notEmpty", Application.getInstance().getMessage("formWizard.label.addFrom.formHeaderNotEmpty","Form header cannot be empty")));
        
        
		privateFormAccess = new Radio("privateFormAccess");				
        publicFormAccess = new Radio("publicFormAccess");        
        privateFormAccess.setGroupName("formAccess");
        privateFormAccess.setOnClick("toggleLayers('privateform');");
        publicFormAccess.setGroupName("formAccess");
        publicFormAccess.setOnClick("toggleLayers('publicform');");
		//publicFormAccess.setChecked(true);
         privateFormAccess.setChecked(true);





		publicFormUserAccess = new Radio("publicFormUserAccess");
        publicFormUserAccess.setChecked(true);
        privateFormUserAccess = new Radio("privateFormUserAccess");

        groupFormUserAccess = new Radio("groupFormUserAccess");
        privateFormUserAccess.setGroupName("formGrpAccess");
        privateFormUserAccess.setOnClick("toggleLayers('privateFormAccess');");
        publicFormUserAccess.setGroupName("formGrpAccess");
        publicFormUserAccess.setOnClick("toggleLayers('publicFormAccess');");
        groupFormUserAccess.setGroupName("formGrpAccess");
        groupFormUserAccess.setOnClick("toggleLayers('groupFormAccess');");
      
        sbAccessUsers  = new UsersSelectBox("sbAccessUsers");
        sbAccessUsers.setRows(5);
        

        cbGroups =new ComboSelectBox("cbGroups");
        submissionMessage = new RichTextBox("submissionMessage");
        submissionMessage.addChild(new ValidatorNotEmpty("notEmpty", Application.getInstance().getMessage("formWizard.label.addFrom.submissionNotEmpty","Submission message cannot be empty")));
        yesDB = new Radio("yesDB");
        noDB = new Radio("noDB");
        yesDB.setGroupName("saveDB");
        yesDB.setOnClick("toggleLayers('yesDB');");
        yesDB.setChecked(true);
        noDB.setGroupName("saveDB");
        noDB.setOnClick("toggleLayers('noDB');");
        emailTo = new TextField("emailTo");        
        sbMemoTo = new UsersSelectBox("sbMemoTo");
        sbMemoTo.setRows(5);        
        
        
        sbQueryBy = new UsersSelectBox("sbQueryBy");        
        sbQueryBy.setRows(5);

        
        workFlowYes = new Radio("Yes");
        workFlowNo = new Radio("No");
        workFlowNo.setChecked(true);
        workFlowYes.setGroupName("workFlow");
        workFlowNo.setGroupName("workFlow");
        workFlowYes.setOnClick("toggleLayers('yesApproval')");
        workFlowNo.setOnClick("toggleLayers('noApproval')");
        
        sbApprover = new UsersSelectBox("sbApprover");        
        sbApprover.setRows(5);
        
        sbApprover.setMultiple(true);



        memoApproveNotification = new CheckBox("memoApproveNotification");
        memoApproveNotification.setChecked(true);
        emailApproveNotification = new CheckBox("emailApproveNotification");
        approveNotification = new ButtonGroup("approveNotification",new CheckBox[]{memoApproveNotification,emailApproveNotification});
        if (memo != null && memo.equals("true"))
            approveNotification.addChild(new ValidatorNotEmpty("notEmpty", Application.getInstance().getMessage("formWizard.label.addFrom.oneApprovalNotification","Select at least one approval notification mehtod")));




        sbForms = new SelectBox("sbForms");
        sbDuplicateFieldForm = new SelectBox("duplicateFieldForm");
        submit = new Button("submit");
        reset = new ResetButton("reset");
        submit.setText(Application.getInstance().getMessage("formWizard.label.addFrom.save","Save"));
        reset.setText(Application.getInstance().getMessage("formWizard.label.addFrom.reset","Reset"));
		FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
        Collection groupList;
        Map groupMap =new SequencedHashMap();
        try {
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            groupList = security.getGroups(new DaoQuery(), 0, -1, null, false);
            for (Iterator i = groupList.iterator(); i.hasNext();) {
                Group group = (Group) i.next();
                groupMap.put(group.getId(), group.getName());
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        sbForms.setOptions("-1=Select...");
        sbDuplicateFieldForm.setOptions("-1=Select...");
        try{

            Collection cForms = handler.getViewForms(new DaoQuery(),getWidgetManager().getUser().getId(),"formDisplayName",false, 0, -1);

            for(Iterator i = cForms.iterator();i.hasNext();) {
                FormDataObject formsDO =(FormDataObject)i.next();
                sbForms.setOptions(formsDO.getFormId()+"="+formsDO.getFormDisplayName());
                sbDuplicateFieldForm.setOptions(formsDO.getFormId()+"="+formsDO.getFormDisplayName());
            }
        }    
		catch (FormDaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}

        tfTableColumn = new TextField("tableColumn");
        tfTableColumn.setSize("4");
        tfTableColumn.addChild(new ValidatorIsNumeric("notNumeric", Application.getInstance().getMessage("formWizard.label.addFrom.tableColumnNotNumeric","Table column must be numeric")));
        tfTableColumn.setValue("2");
        addChild(tfTableColumn);




        addChild(formName);
        addChild(formHeader);
//        addChild(privateFormAccess);
//        addChild(publicFormAccess);
        addChild(privateFormUserAccess);
        addChild(publicFormUserAccess);
        addChild(groupFormUserAccess);
        addChild(sbAccessUsers);
        addChild(cbGroups);
        addChild(submissionMessage);
        addChild(yesDB);
        addChild(noDB);
        addChild(emailTo);

        if (memo != null && memo.equals("true"))
            addChild(sbMemoTo);
        
        addChild(sbQueryBy);
        addChild(workFlowYes);
        addChild(workFlowNo);
        addChild(sbApprover);
        addChild(approveNotification);
        addChild(sbForms);
        addChild(sbDuplicateFieldForm);
        addChild(submit);
        addChild(reset);
        setMethod("post");

        if (memo != null && memo.equals("true"))
            sbMemoTo.init();
        sbAccessUsers.init();
        sbQueryBy.init();
        sbApprover.init();
        cbGroups.init();
        cbGroups.setLeftValues(groupMap);
    }
    
    public void onRequest(Event evt) {
        initializeFields();
    }
        
	
    public Forward onSubmit(Event evt) {
        formName.setMessage("");
        sbAccessUsers.setMessage("") ;
        sbQueryBy.setMessage("");
        cbGroups.setMessage("");
        noDB.setMessage("");
        sbApprover.setMessage("");
        tfTableColumn.setMessage("");
		setInvalid(false);


        Forward forward = super.onSubmit(evt);



        if(formName.getValue() != null && !isAlphaNumeric(formName.getValue().toString())){
            formName.setInvalid(true);
            formName.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.alphaNumeric","AlphaNumeric only !"));
            setInvalid(true);
        }
        //check form name duplication
        else if (formName.getValue() != null) {
            FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);


            try {
                boolean exist = module.isFormNameExist(formName.getValue().toString());
                if (exist) {
                    formName.setInvalid(true);
                    formName.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.formNameExist","Form Name already exist !"));
                    setInvalid(true);
                }

            }
            catch (FormException e) {
                formName.setInvalid(true);
                formName.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.error","Error in checking existance of form"));
                setInvalid(true);
                Log.getLog(getClass()).error("Error in checking existance of form named " + formName.getValue(), e);
            }

        }

        if (privateFormUserAccess.isChecked() && sbAccessUsers.getSelectedOptions().size() == 0) {
            sbAccessUsers.setInvalid(true);
            sbAccessUsers.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.selectOneUser","Please select at least one user.")) ;
            setInvalid(true);                                
        }
		if (groupFormUserAccess.isChecked() && cbGroups.getRightValues().size() == 0) {
			cbGroups.setInvalid(true);
            cbGroups.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.selectOneGroup","Please select at least one group."));
			setInvalid(true);			       			
		}
		if (noDB.isChecked() && emailTo.getValue().equals("") && sbMemoTo.getSelectedOptions().size() == 0) {

			noDB.setInvalid(true);            
			emailTo.setInvalid(true);
            sbMemoTo.setInvalid(true);
			setInvalid(true);
            noDB.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.emailRequired","Email address is required. If no email address is available,select 'Yes' to save to the database OR select the users from the message list."));

//            if (memo != null && memo.equals("true") && sbMemoTo.getSelectedOptions().size() == 0) {

//            }
		}
		if (yesDB.isChecked() && sbQueryBy.getSelectedOptions().size() == 0) {
			sbQueryBy.setInvalid(true);
            sbQueryBy.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.selectUserQuery","Please select at least one user to query the form."));
			setInvalid(true);			        			
			
		}
		if (workFlowYes.isChecked() && sbApprover.getSelectedOptions().size() == 0) {
			sbApprover.setInvalid(true);
            sbApprover.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.selectOneApprover","Please select at least one approver."));
			setInvalid(true);			       			
		}

        if (!tfTableColumn.isInvalid() && !tfTableColumn.getValue().toString().equals("")
            &&(Integer.parseInt(tfTableColumn.getValue().toString())) < 1) {
            tfTableColumn.setInvalid(true);
            tfTableColumn.setMessage(Application.getInstance().getMessage("formWizard.label.addForm.tableColumn","Table column value must be greater than 0"));
            setInvalid(true);
        }




        if (isInvalid())
            return null;


                 
        

        
        return forward;
    }
       
    private boolean isAlphaNumeric(String str){
           char[] nameChar = str.toCharArray();
           for (int i = 0; i <nameChar.length; i++) {
               if (nameChar[i] == '_') {    // underscore
                   continue;
               }
               if (nameChar[i] == ' ') {    // space
                    continue;
               }
               if (!Character.isLetterOrDigit(nameChar[i])) {
                   return false;
               }
           }
           return true;
       }
 

    public Forward onValidate(Event evt){

        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && submit.getAbsoluteName().equals(buttonName))
            fwd = addForm(evt.getRequest());

        return fwd;
    }

    public Forward addForm(HttpServletRequest request){
        Forward forward=new Forward("");
        FormDataObject formsDO = null;
        FormConfigDataObject fcdo = null;

        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
        formsDO = getFormDataObject(request);
        fcdo = getCopiedColumns(formsDO);
        try{

            handler.addForm(getWidgetManager().getUser().getId(),formsDO,fcdo);


            forward.setName("formAdded");
        }
        catch(FormNameExistsException e){
            Log.getLog(getClass()).error("Form Name exist,",e);
            forward.setName(FORMNAME_EXISTS);
        }
        catch(FormException e){
            Log.getLog(getClass()).error(e.getMessage(),e);
            forward.setName("fail");
        }
        catch (FormDaoException e) {
           Log.getLog(getClass()).error(e.getMessage(),e);
        }

        return forward;
    }

    public FormDataObject getFormDataObject(HttpServletRequest request) {

        String formId = UuidGenerator.getInstance().getUuid();
        String memo = "false";
        memo = Application.getInstance().getProperty(FormModule.PROPERTY_MEMO);
        setFormId(formId);
        FormDataObject formsDO = new FormDataObject();
        formsDO.setFormId(getFormId());
        formsDO.setFormDisplayName(request.getParameter(formName.getAbsoluteName().trim()));
        formsDO.setFormName(parseFormName(FormTableIdGenerator.getInstance().getUuid()));
        formsDO.setFormHeader(request.getParameter(formHeader.getAbsoluteName()));
        formsDO.setFormDateCreated(new Date());
        formsDO.setFormUpdatedBy(getWidgetManager().getUser().getId());
        formsDO.setFormRetMsg(request.getParameter(submissionMessage.getAbsoluteName()));
        formsDO.setFormEmails(request.getParameter(emailTo.getAbsoluteName()));
        if(publicFormAccess.isChecked()){
            formsDO.setFormInternalFlag("0");
            formsDO.setFormPublicFlag("0");
        } else {
            formsDO.setFormInternalFlag("1");
            if(privateFormUserAccess.isChecked())
                formsDO.setFormPublicFlag("1");
            if(publicFormUserAccess.isChecked())
                formsDO.setFormPublicFlag("0");
            if(groupFormUserAccess.isChecked())
                formsDO.setFormPublicFlag("2");
        }
        formsDO.setIsActive("1");
        if(noDB.isChecked() && !workFlowYes.isChecked() && !workFlowNo.isChecked())
            formsDO.setFormDateUpdated("0");
        else {
            if(workFlowYes.isChecked())
                formsDO.setFormDateUpdated("1");
            else if(workFlowNo.isChecked())
                formsDO.setFormDateUpdated("0");
        }

        if (memo.equals("false"))   {
            emailApproveNotification.setChecked(true);
            memoApproveNotification.setChecked(false);
        }


        if(emailApproveNotification.isChecked())
            formsDO.setFormApprMethod(FormModule.WORKFLOW_APPROVAL_EMAIL);
        if(memoApproveNotification.isChecked())
            formsDO.setFormApprMethod(formsDO.getFormApprMethod() + "," + FormModule.WORKFLOW_APPROVAL_MEMO);



        if(privateFormUserAccess.isChecked() && sbAccessUsers.getOptionMap().size()>0){
            formsDO.setAccessUserIDs(sbAccessUsers.getOptionMap());
        }
        else if(groupFormUserAccess.isChecked() && cbGroups.getRightValues().size()>0){
            formsDO.setAccessGroupIDs(cbGroups.getRightValues());
        }
        if(yesDB.isChecked() && sbQueryBy.getOptionMap().size()>0)
            formsDO.setQueryAccessUserIDs(sbQueryBy.getOptionMap());
        if(sbMemoTo.getOptionMap().size()>0)
            formsDO.setMemoUserIDs(sbMemoTo.getOptionMap());

        Collection cSelectecd = (Collection)sbForms.getValue();
        String selected = (String)cSelectecd.iterator().next();
        if(!selected.equals("-1"))
            formsDO.setFormLink(selected);

        if(workFlowYes.isChecked() && yesDB.isChecked() && sbApprover.getOptionMap().size()>0)
            formsDO.setApproverIDs(sbApprover.getSelectedOptions());

        if (yesDB.isChecked())
            formsDO.setSaveDb(FormConstants.FORM_SAVE_DB);
        else
            formsDO.setSaveDb(FormConstants.FORM_NO_SAVE_DB);

        formsDO.setIsPending("0");
        formsDO.setIsActive("0");

        formsDO.setTableColumn(tfTableColumn.getValue().toString());

        cSelectecd = (Collection)sbForms.getValue();
        selected = (String)cSelectecd.iterator().next();
        if(!selected.equals("-1"))
            formsDO.setFormLink(selected);

        return formsDO;
    }

    public FormConfigDataObject getCopiedColumns(FormDataObject formsDO) {

        FormConfigDataObject fcdo = null;
        Collection cSelectecd = (Collection)sbDuplicateFieldForm.getValue();
        String selected = (String)cSelectecd.iterator().next();
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        if(!selected.equals("-1")){
            try {
                fcdo = new FormConfigDataObject();
                fcdo.setFormId(getFormId());
                fcdo.setFormConfigId(UuidGenerator.getInstance().getUuid());
                fcdo.setPreviewXml(processXml(module.getFormPreviewXML(selected),formsDO.getFormName()));
            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            catch (FormDocumentException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }

        }
        return fcdo;
    }



    public void initializeFields() {
        sbForms.removeAllOptions();
        sbDuplicateFieldForm.removeAllOptions();
        sbForms.setOptions("-1="+Application.getInstance().getMessage("formWizard.label.addForm.select","Select..."));
        sbDuplicateFieldForm.setOptions("-1="+Application.getInstance().getMessage("formWizard.label.addForm.select","Select..."));
        try{
            FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);
			Collection cForms = handler.getViewForms(new DaoQuery(),getWidgetManager().getUser().getId(),"formDisplayName",false, 0, -1);

            for(Iterator i = cForms.iterator();i.hasNext();) {
                FormDataObject formsDO =(FormDataObject)i.next();
                sbForms.setOptions(formsDO.getFormId()+"="+formsDO.getFormDisplayName());
                sbDuplicateFieldForm.setOptions(formsDO.getFormId()+"="+formsDO.getFormDisplayName());
            }
        }
		catch (FormDaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}

        formName.setValue("");
        formHeader.setValue("");
        submissionMessage.setValue("");
        publicFormUserAccess.setChecked(true);
        privateFormUserAccess.setChecked(false);
        groupFormUserAccess.setChecked(false);
        sbAccessUsers.removeAllOptions();
        cbGroups.init();

        Map groupMap = new SequencedHashMap();
        try {
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection groupList = security.getGroups(new DaoQuery(), 0, -1, "groupName", false);
            for (Iterator i = groupList.iterator(); i.hasNext();) {
                Group group = (Group) i.next();
                groupMap.put(group.getId(), group.getName());
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("", e);
        }
        cbGroups.setLeftValues(groupMap);

        yesDB.setChecked(true);
        noDB.setChecked(false);

        emailTo.setValue("");



//        String memo = Application.getInstance().getProperty(FormModule.PROPERTY_MEMO);
//        if (memo != null && memo.equals("true"))
            sbMemoTo.removeAllOptions();

        sbQueryBy.removeAllOptions();

        workFlowNo.setChecked(true);
        workFlowYes.setChecked(false);

        sbApprover.removeAllOptions();
        memoApproveNotification.setChecked(true);
        emailApproveNotification.setChecked(false);

        sbForms.setSelectedOption("-1=Select...");
        sbDuplicateFieldForm.setSelectedOption("-1=Select...");

        tfTableColumn.setValue("2");

        formName.setMessage("");
        sbAccessUsers.setMessage("") ;
        sbQueryBy.setMessage("");
        cbGroups.setMessage("");
        noDB.setMessage("");
        sbApprover.setMessage("");
        tfTableColumn.setMessage("");


        formName.setInvalid(false);
        sbAccessUsers.setInvalid(false);
        sbQueryBy.setInvalid(false);
        cbGroups.setInvalid(false);
        noDB.setInvalid(false);
        sbApprover.setInvalid(false);
        tfTableColumn.setInvalid(false);

		setInvalid(false);



    }

    protected String processXml(InputStream stream, String formName) throws FormDocumentException {
        Document jDomDocument = Util.buildJDomDocument(stream);
        Element formElement = jDomDocument.getRootElement();
        formElement.setAttribute("name",formName);
        return Util.JDomDocumentToString(jDomDocument);

    }

	//Convert the form name white space to underscore
	private String parseFormName(String formName) {
		char[] formNameChar = formName.toCharArray();
		String parsedFormName = "";
		
		for (int i = 0; i < formNameChar.length; i++) {
			if (formNameChar[i] == '-')
				parsedFormName += '_';
			else
				parsedFormName += formNameChar[i];
		}	
		
		return parsedFormName;
		
	}
	
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }


    public TextField getFormName() {
        return formName;
    }

    public RichTextBox getFormHeader() {
        return formHeader;
    }

    public Radio getPrivateFormAccess() {
        return privateFormAccess;
    }

    public Radio getPublicFormAccess() {
        return publicFormAccess;
    }

    public Radio getPrivateFormUserAccess() {
        return privateFormUserAccess;
    }

    public Radio getPublicFormUserAccess() {
        return publicFormUserAccess;
    }

    public Radio getGroupFormUserAccess() {
        return groupFormUserAccess;
    }

     public SelectBox getSbAccessUsers() {
        return sbAccessUsers;
    }

    public ComboSelectBox getCbGroups() {
        return cbGroups;
    }

    public RichTextBox getSubmissionMessage() {
        return submissionMessage;
    }

    public Radio getYesDB() {
        return yesDB;
    }

    public Radio getNoDB() {
        return noDB;
    }

    public TextField getEmailTo() {
        return emailTo;
    }

    public SelectBox getSbMemoTo() {
        return sbMemoTo;
    }

    public UsersSelectBox getSbQueryBy() {
        return sbQueryBy;
    }

    public Radio getWorkFlowYes() {
        return workFlowYes;
    }

    public Radio getWorkFlowNo() {
        return workFlowNo;
    }

    public SelectBox getSbApprover() {
        return sbApprover;
    }

    public CheckBox getMemoApproveNotification() {
        return memoApproveNotification;
    }

    public CheckBox getEmailApproveNotification() {
        return emailApproveNotification;
    }

    public SelectBox getSbForms() {
        return sbForms;
    }

    public Button getSubmit() {
        return submit;
    }


	public ResetButton getReset() {
		return reset;
	}

    public SelectBox getSbDuplicateFieldForm() {
        return sbDuplicateFieldForm;
    }

    public void setSbDuplicateFieldForm(SelectBox sbDuplicateFieldForm) {
        this.sbDuplicateFieldForm = sbDuplicateFieldForm;
    }

    public TextField getTfTableColumn() {
        return tfTableColumn;
    }

    public void setTfTableColumn(TextField tfTableColumn) {
        this.tfTableColumn = tfTableColumn;
    }


    public String getDefaultTemplate() {
        return "formwizard/addForm2";
    }


}

