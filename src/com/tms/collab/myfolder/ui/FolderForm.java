/*
 * Created on Jun 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.calendar.ui.CalendarUsersSelectBox;
import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FolderForm extends Form{
	
	public static final String FORWARD_FAILED = "failed";
	
	private TextField txtFolderName;
	private TextBox txtFolderDesc;
	private Radio radPrivateAccess;
	private Radio radPublicAccess;
	private Radio radSelectedAccess;
	private Radio radCmsPublicAccess;
	
	private Label lblFolderName;
	private Label lblFolderDesc;
	private Label lblPrivateAccess;
	private Label lblPublicAccess;
	private Label lblSelectedAccess;
	private Label lblCmsPublicAccess;
	
	private Button submit;
	private Button cancel;
	private FolderSelectBox selBxParentFolder;
	
	private ValidatorMessage valMsg;
	
	private CalendarUsersSelectBox userSelectBox;
	
	private String[] userSelectedId;
	
	private String parentId;
	
	public FolderForm(){
		super();
	}
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		removeChildren();
		setColumns(2);
		
		txtFolderName = new TextField("txtFolderName");
		ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("mf.message.mustNotEmpty","Must not be empty"));
		txtFolderName.addChild(vne);
		txtFolderName.setMaxlength("255");
		
		txtFolderDesc = new TextBox("txtFolderDesc");
		txtFolderDesc.setCols("80");
		txtFolderDesc.setRows("4");
		
		lblFolderName = new Label("lblFolderName", Application.getInstance().getMessage("mf.label.folderName", "Folder Name") + "*");
		lblFolderDesc = new Label("lblFolderDesc", Application.getInstance().getMessage("mf.label.folderDesc", "Folder Description"));
		lblPrivateAccess = new Label("lblPrivateAccess", Application.getInstance().getMessage("mf.label.folderAccess", "Folder Access"));
		lblPublicAccess = new Label("lblPublicAccess", "");
		lblSelectedAccess = new Label("lblSelectedAccess", "");
		lblCmsPublicAccess = new Label("lblCmsPublicAccess", "");
		
		radPrivateAccess = new Radio("radPrivateAccess", Application.getInstance().getMessage("mf.label.privateAccess", "Personal Access"));
		radPrivateAccess.setGroupName("folderAccess");
		radPrivateAccess.setChecked(true);
		radPublicAccess = new Radio("radPublicAccess", Application.getInstance().getMessage("mf.label.publicAccess", "All EKP Users Access"));
		radPublicAccess.setGroupName("folderAccess");
		radSelectedAccess = new Radio("radSelectedAccess", Application.getInstance().getMessage("mf.label.selectedAccess", "Selected User(s) Access"));
		radSelectedAccess.setGroupName("folderAccess");
		radCmsPublicAccess = new Radio("radCmsPublicAccess", Application.getInstance().getMessage("mf.label.cmsPublicAccess", "Public Access"));
		radCmsPublicAccess.setGroupName("folderAccess");
		
		submit = new Button("submit", Application.getInstance().getMessage("mf.label.submit", "Submit"));
		cancel = new Button("calcel", Application.getInstance().getMessage("mf.label.cancel", "Cancel"));
		
		userSelectBox = new CalendarUsersSelectBox("userSelectBox");
        userSelectBox.setMultiple(true);
        userSelectBox.setRows(3);
        userSelectBox.setTemplate("myfolder/popupSelectBox");
        userSelectBox.setMessage("Please select at least one user");
        
        valMsg = new ValidatorMessage("valMsg");
        
        userSelectBox.addChild(valMsg);
        
		addChild(lblFolderName);
		addChild(txtFolderName);
		
		addChild(lblFolderDesc);
		addChild(txtFolderDesc);
		
		addChild(lblPrivateAccess);
		
		Panel accessPanel = new Panel("accessPanel");
		accessPanel.setColumns(1);
		accessPanel.addChild(radPrivateAccess);
		accessPanel.addChild(radCmsPublicAccess);
		accessPanel.addChild(radPublicAccess);
		accessPanel.addChild(radSelectedAccess);
		addChild(accessPanel);
		
		addChild(lblSelectedAccess);
		addChild(userSelectBox);
		
		addChild(new Label("2", Application.getInstance().getMessage("messaging.label.parentFolder","Parent Folder")));
		selBxParentFolder = new FolderSelectBox("parentFolder");
		selBxParentFolder.setParentSelection(false);
        addChild(selBxParentFolder);

        
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.setColumns(2);
        buttonPanel.addChild(submit);
        buttonPanel.addChild(cancel);
        
        addChild(buttonPanel);
		
		userSelectBox.init();
	}
	
	public void onRequest(Event ev) {
		initForm();
	}
	
	public Forward onSubmit(Event event){
		
		String buttonName = findButtonClicked(event);
		Forward result = super.onSubmit(event);
		if(submit.getAbsoluteName().equals(buttonName)){
			
			if(radSelectedAccess.isChecked()){
				
				String[] selectedUsers = userSelectBox.getIds();
				
				if(selectedUsers.length <= 0){
					userSelectBox.setInvalid(true);
					valMsg.setInvalid(true);
					setInvalid(true);
					return new Forward(Form.CANCEL_FORM_ACTION);
				}
			}
		}
		else if(cancel.getAbsoluteName().equals(buttonName)){
			return new Forward("mainPage");
		}
		return result;
	}
	
	public Forward onValidate(Event event) {
		
		String button = findButtonClicked(event);
        button = button == null ? "" : button;

        if (button.endsWith("submit")) {
            // process upload
        	createNewFolder();
            return new Forward("mainPage");
        }else if(button.endsWith("cancel")){
        	return new Forward("mainPage");
        }
		
		return new Forward("mainPage");
	}
	
	public void createNewFolder(){
		//StorageService ss;
		//StorageDirectory dir;
		
		Application application = Application.getInstance();
		FileFolder folder;
		String path = new String();
		
		try{
			MyFolderModule mf = (MyFolderModule)application.getModule(MyFolderModule.class);
			User user = getWidgetManager().getUser();
			folder = new FileFolder();
			UuidGenerator uuidGenerator = UuidGenerator.getInstance();
			
	        //ss = (StorageService)Application.getInstance().getService(StorageService.class);
	        
			folder.setId(uuidGenerator.getUuid());
			folder.setUserId(user.getId());
			folder.setUserName(user.getUsername());
			folder.setFileName((String)txtFolderName.getValue());
			
			//if(parentId == null){
			//	parentId = mf.getRootFolder(user.getId());
			//}else{
				//parentId = parentFolder.getSelectedFolderId();
			//}
			
			//FileFolder parentFolder = mf.loadFileFolder(parentId);
			FileFolder parentFolder = mf.loadFileFolder(selBxParentFolder.getSelectedFolderId());
			path = parentFolder.getFilePath();
			//dir = new StorageDirectory(path, folder.getId());
			
			//orgi
			//folder.setFilePath(path + folder.getId() + "/");
			folder.setFilePath(path);
			
			folder.setFileSize(0);
			folder.setFileDescription((String)txtFolderDesc.getValue());
			folder.setFileType("Folder");
			
			if(radPrivateAccess.isChecked()){
				folder.setFileAccess("1");
			}else if(radPublicAccess.isChecked()){
				folder.setFileAccess("2");
			}else if(radSelectedAccess.isChecked()){
				folder.setFileAccess("3");
			}else if(radCmsPublicAccess.isChecked()){
				folder.setFileAccess("4");
			}else{
				folder.setFileAccess("1");
			}
			
			folder.setParentId(parentFolder.getId());
			folder.setAccessCountPrivate(0);
			folder.setAccessCountPublic(0);
			
			mf.createNewFileFolder(folder);
			
			if("3".equals(folder.getFileAccess())){
				mf.createFolderSharedUser(folder.getId(), userSelectBox.getIds());
			}
			
			//ss.store(dir);
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in creating new folder :" + e.getMessage(), e);
		}
	}
	
	/**
	 * @return Returns the valMsg.
	 */
	public ValidatorMessage getValMsg() {
		return valMsg;
	}
	/**
	 * @param valMsg The valMsg to set.
	 */
	public void setValMsg(ValidatorMessage valMsg) {
		this.valMsg = valMsg;
	}
	/**
	 * @return Returns the parentFolder.
	 */
	public FolderSelectBox getSelBxParentFolder() {
		return selBxParentFolder;
	}
	/**
	 * @param parentFolder The parentFolder to set.
	 */
	public void setSelBxParentFolder(FolderSelectBox selBxParentFolder) {
		this.selBxParentFolder = selBxParentFolder;
	}
	/**
	 * @return Returns the parentId.
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return Returns the userSelectBox.
	 */
	public CalendarUsersSelectBox getUserSelectBox() {
		return userSelectBox;
	}
	/**
	 * @param userSelectBox The userSelectBox to set.
	 */
	public void setUserSelectBox(CalendarUsersSelectBox userSelectBox) {
		this.userSelectBox = userSelectBox;
	}
	/**
	 * @return Returns the userSelectedId.
	 */
	public String[] getUserSelectedId() {
		return userSelectedId;
	}
	/**
	 * @param userSelectedId The userSelectedId to set.
	 */
	public void setUserSelectedId(String[] userSelectedId) {
		this.userSelectedId = userSelectedId;
	}
	/**
	 * @return Returns the lblFolderDesc.
	 */
	public Label getLblFolderDesc() {
		return lblFolderDesc;
	}
	/**
	 * @param lblFolderDesc The lblFolderDesc to set.
	 */
	public void setLblFolderDesc(Label lblFolderDesc) {
		this.lblFolderDesc = lblFolderDesc;
	}
	/**
	 * @return Returns the lblFolderName.
	 */
	public Label getLblFolderName() {
		return lblFolderName;
	}
	/**
	 * @param lblFolderName The lblFolderName to set.
	 */
	public void setLblFolderName(Label lblFolderName) {
		this.lblFolderName = lblFolderName;
	}
	/**
	 * @return Returns the lblPrivateAccess.
	 */
	public Label getLblPrivateAccess() {
		return lblPrivateAccess;
	}
	/**
	 * @param lblPrivateAccess The lblPrivateAccess to set.
	 */
	public void setLblPrivateAccess(Label lblPrivateAccess) {
		this.lblPrivateAccess = lblPrivateAccess;
	}
	/**
	 * @return Returns the lblPublicAccess.
	 */
	public Label getLblPublicAccess() {
		return lblPublicAccess;
	}
	/**
	 * @param lblPublicAccess The lblPublicAccess to set.
	 */
	public void setLblPublicAccess(Label lblPublicAccess) {
		this.lblPublicAccess = lblPublicAccess;
	}
	/**
	 * @return Returns the lblSelectedAccess.
	 */
	public Label getLblSelectedAccess() {
		return lblSelectedAccess;
	}
	/**
	 * @param lblSelectedAccess The lblSelectedAccess to set.
	 */
	public void setLblSelectedAccess(Label lblSelectedAccess) {
		this.lblSelectedAccess = lblSelectedAccess;
	}
	/**
	 * @return Returns the radPrivateAccess.
	 */
	public Radio getRadPrivateAccess() {
		return radPrivateAccess;
	}
	/**
	 * @param radPrivateAccess The radPrivateAccess to set.
	 */
	public void setRadPrivateAccess(Radio radPrivateAccess) {
		this.radPrivateAccess = radPrivateAccess;
	}
	/**
	 * @return Returns the radPublicAccess.
	 */
	public Radio getRadPublicAccess() {
		return radPublicAccess;
	}
	/**
	 * @param radPublicAccess The radPublicAccess to set.
	 */
	public void setRadPublicAccess(Radio radPublicAccess) {
		this.radPublicAccess = radPublicAccess;
	}
	/**
	 * @return Returns the radSelectedAccess.
	 */
	public Radio getRadSelectedAccess() {
		return radSelectedAccess;
	}
	/**
	 * @param radSelectedAccess The radSelectedAccess to set.
	 */
	public void setRadSelectedAccess(Radio radSelectedAccess) {
		this.radSelectedAccess = radSelectedAccess;
	}
	
	/**
	 * @return Returns the submit.
	 */
	public Button getSubmit() {
		return submit;
	}
	/**
	 * @param submit The submit to set.
	 */
	public void setSubmit(Button submit) {
		this.submit = submit;
	}
	/**
	 * @return Returns the txtFolderDesc.
	 */
	public TextBox getTxtFolderDesc() {
		return txtFolderDesc;
	}
	/**
	 * @param txtFolderDesc The txtFolderDesc to set.
	 */
	public void setTxtFolderDesc(TextBox txtFolderDesc) {
		this.txtFolderDesc = txtFolderDesc;
	}
	/**
	 * @return Returns the txtFolderName.
	 */
	public TextField getTxtFolderName() {
		return txtFolderName;
	}
	/**
	 * @param txtFolderName The txtFolderName to set.
	 */
	public void setTxtFolderName(TextField txtFolderName) {
		this.txtFolderName = txtFolderName;
	}
}
