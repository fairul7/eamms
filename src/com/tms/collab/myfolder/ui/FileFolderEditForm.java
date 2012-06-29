/*
 * Created on Jun 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import java.util.ArrayList;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
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

import com.tms.collab.calendar.ui.CalendarUsersSelectBox;
import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileFolderEditForm extends Form{
	
	public static final String FORWARD_FAILED = "failed";
	
	private TextField txtName;
	private TextBox txtDesc;
	private Radio radPrivateAccess;
	private Radio radPublicAccess;
	private Radio radSelectedAccess;
	private Radio radCmsPublicAccess;
	private Label lblName;
	private Label lblDesc;
	private Label lblPrivateAccess;
	private Label lblPublicAccess;
	private Label lblSelectedAccess;
	private Button submit;
	private Button cancel;
	private Hidden isFolder;
	
	private CalendarUsersSelectBox userSelectBox;
	
	private String[] userSelectedId;
	
	private ValidatorMessage valMsg;
	
	private String mfId;
	
	private boolean authorize;
	
	public FileFolderEditForm(){
		super();
	}
	
	public void init(){
		
		if(checkAuthorization()){
			populateForm();
		}
	}
	
	public boolean checkAuthorization(){
		try{
			Application app = Application.getInstance();
			SecurityService service = (SecurityService) app.getService(SecurityService.class);
			User loginUser = app.getCurrentUser();
			FileFolder fileFolder = new FileFolder();
			boolean isDeletedUserFileFolder = false;
			ArrayList deletedUserList;
			
			boolean delUserFolderView = service.hasPermission(loginUser.getId(), MyFolderModule.PERMISSION_DEL_USER_FOLDER, null, null);
			
			if(getMfId() != null){
				
				MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
				
				fileFolder = mf.loadFileFolder(getMfId());
				
				if(fileFolder != null){
					
					//if the file/folder is not belongs to the login user
					if(!fileFolder.getUserId().equals(loginUser.getId())){
						
						deletedUserList = mf.getDeletedUserList();
						
						if(deletedUserList != null && deletedUserList.size() > 0){
							
							//if the file belongs to a deleted user and the login user have the authority to edit deleted user file/folder
							for(int i=0; i<deletedUserList.size(); i++){
								String delUser = (String)deletedUserList.get(i);
								
								if(delUser.equals(fileFolder.getUserId())){
									isDeletedUserFileFolder = true;
									break;
								}
							}
							
							if(isDeletedUserFileFolder && delUserFolderView){
								
								authorize = true;
							}else{
								authorize = false;
							}
							
						}else{
							authorize = false;
						}
						
					}else{
						authorize = true;
					}
					
				}
			}
			
			return authorize;
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in FileFolderEditForm init: " + e.getMessage(), e);
			return false;
		}
	}
	
	public void onRequest(Event ev) {
		if(checkAuthorization()){
			populateForm();
		}
	}
	
	public Forward onSubmit(Event event){
		
		String buttonName = findButtonClicked(event);
		Forward result = super.onSubmit(event);
		
		if("true".equals((String)isFolder.getValue())){

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
		}
		
		return result;
	}
	
	public Forward onValidate(Event event) {
		
		String button = findButtonClicked(event);
        button = button == null ? "" : button;

        if (button.endsWith("save")) {
        	doEdit(event);
            return new Forward("save");
        }
        else if (button.endsWith("cancel")){
        	return new Forward("cancel");
        }
		
		return new Forward("mainPage");
	}
	
	public void doEdit(Event event){
		Application app = Application.getInstance();
		MyFolderModule mf = (MyFolderModule)app.getModule(MyFolderModule.class);
		
		try{
			FileFolder f = new FileFolder();
			f.setFileName((String)txtName.getValue());
			f.setFileDescription((String)txtDesc.getValue());
			f.setId(mfId);
			
			if("true".equalsIgnoreCase((String)isFolder.getValue())){
				
				if(radPrivateAccess.isChecked()){
					f.setFileAccess("1");
				}else if(radPublicAccess.isChecked()){
					f.setFileAccess("2");
				}else if(radSelectedAccess.isChecked()){
					f.setFileAccess("3");
				}else if(radCmsPublicAccess.isChecked()){
					f.setFileAccess("4");
				}else{
					f.setFileAccess("1");
				}
				mf.editFolder(f, userSelectBox.getIds());
				
			}else{
				mf.editFile(f);
			}
			mf.logAction(getWidgetManager().getUser().getId(), f.getId(), "edit");
		}
		catch(Exception  e){
			Log.getLog(getClass()).error("error in editing file: " + e.getMessage(), e);
		}
	}
	
	public void populateForm(){
		setMethod("POST");
		removeChildren();
		setColumns(2);
		Application app = Application.getInstance();
		FileFolder f = new FileFolder();
		try{
			MyFolderModule mf = (MyFolderModule)app.getModule(MyFolderModule.class);
			
			f = mf.loadFileFolder(getMfId());
			
			lblName = new Label("lblFolderName", Application.getInstance().getMessage("mf.label.fileName", "Name") + "*");
			txtName = new TextField("txtFolderName");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("mf.message.mustNotEmpty","Must not be empty"));
			txtName.addChild(vne);
			txtName.setValue(f.getFileName());
			
			addChild(lblName);
			addChild(txtName);
			
			lblDesc = new Label("lblFolderDesc", Application.getInstance().getMessage("mf.label.desc", "Description") );
			txtDesc = new TextBox("txtFolderDesc");
			txtDesc.setCols("80");
			txtDesc.setRows("4");
			txtDesc.setValue(f.getFileDescription());
			
			addChild(lblDesc);
			addChild(txtDesc);
			
			isFolder = new Hidden("isFolder");
			
			//this is the prevent the root folder being modified in the access
			
			isFolder.setValue(String.valueOf(f.isFolder()));
			
			addChild(isFolder);
			
			if(f.isFolder()){
				
				lblPrivateAccess = new Label("lblPrivateAccess", Application.getInstance().getMessage("mf.label.folderAccess"));
				lblPublicAccess = new Label("lblPublicAccess", "");
				lblSelectedAccess = new Label("lblSelectedAccess", "");
				
				radPrivateAccess = new Radio("radPrivateAccess", Application.getInstance().getMessage("mf.label.privateAccess"));
				radPrivateAccess.setGroupName("folderAccess");
				radPrivateAccess.setChecked(true);
				radPublicAccess = new Radio("radPublicAccess", Application.getInstance().getMessage("mf.label.publicAccess"));
				radPublicAccess.setGroupName("folderAccess");
				radSelectedAccess = new Radio("radSelectedAccess", Application.getInstance().getMessage("mf.label.selectedAccess"));
				radSelectedAccess.setGroupName("folderAccess");
				radCmsPublicAccess = new Radio("radCmsPublicAccess", Application.getInstance().getMessage("mf.label.cmsPublicAccess", "Public Access"));
				radCmsPublicAccess.setGroupName("folderAccess");
				
				userSelectBox = new CalendarUsersSelectBox("userSelectBox");
		        userSelectBox.setMultiple(true);
		        userSelectBox.setRows(3);
		        userSelectBox.setTemplate("myfolder/popupSelectBox");
		        userSelectBox.setMessage(Application.getInstance().getMessage("mf.message.selectUser","Please select at least one user"));
		        
		        valMsg = new ValidatorMessage("valMsg");
		        
		        userSelectBox.addChild(valMsg);
		        
				if("1".equals(f.getFileAccess())){
					radPrivateAccess.setChecked(true);
				}
				else if("2".equals(f.getFileAccess())){
					radPublicAccess.setChecked(true);
				}
				else if("3".equals(f.getFileAccess())){
					radSelectedAccess.setChecked(true);
					ArrayList sharedUserList = new ArrayList();
					sharedUserList = mf.getFolderSharedUsers(mfId);
					
					String[] sharedUserStrList = new String[sharedUserList.size()];
					for(int i=0; i<sharedUserList.size(); i++){
						sharedUserStrList[i] = (String)sharedUserList.get(i);
					}
					
					userSelectBox.setIds(sharedUserStrList);
				}else if("4".equals(f.getFileAccess())){
					radCmsPublicAccess.setChecked(true);
				}
				
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
			
				userSelectBox.init();
			}
			
			submit = new Button("save", Application.getInstance().getMessage("mf.label.update", "Update"));
			cancel = new Button("cancel", Application.getInstance().getMessage("mf.label.cancel", "Cancel"));
			
			Panel buttonPanel = new Panel("buttonPanel");
			buttonPanel.setColumns(2);
			buttonPanel.addChild(submit);
			buttonPanel.addChild(cancel);
			
	        addChild(buttonPanel);
	        
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in populate form: " + e.getMessage(), e);
		}		
	}
	
	public String getDefaultTemplate(){
		return "myfolder/form";
	}
	
	public boolean isAuthorize() {
		return authorize;
	}

	public void setAuthorize(boolean authorize) {
		this.authorize = authorize;
	}

	/**
	 * @return Returns the isFolder.
	 */
	public Hidden getIsFolder() {
		return isFolder;
	}
	/**
	 * @param isFolder The isFolder to set.
	 */
	public void setIsFolder(Hidden isFolder) {
		this.isFolder = isFolder;
	}
	
	/**
	 * @return Returns the lblDesc.
	 */
	public Label getLblDesc() {
		return lblDesc;
	}
	/**
	 * @param lblDesc The lblDesc to set.
	 */
	public void setLblDesc(Label lblDesc) {
		this.lblDesc = lblDesc;
	}
	/**
	 * @return Returns the lblName.
	 */
	public Label getLblName() {
		return lblName;
	}
	/**
	 * @param lblName The lblName to set.
	 */
	public void setLblName(Label lblName) {
		this.lblName = lblName;
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
	 * @return Returns the mfId.
	 */
	public String getMfId() {
		return mfId;
	}
	/**
	 * @param mfId The mfId to set.
	 */
	public void setMfId(String mfId) {
		this.mfId = mfId;
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
	 * @return Returns the txtDesc.
	 */
	public TextBox getTxtDesc() {
		return txtDesc;
	}
	/**
	 * @param txtDesc The txtDesc to set.
	 */
	public void setTxtDesc(TextBox txtDesc) {
		this.txtDesc = txtDesc;
	}
	/**
	 * @return Returns the txtName.
	 */
	public TextField getTxtName() {
		return txtName;
	}
	/**
	 * @param txtName The txtName to set.
	 */
	public void setTxtName(TextField txtName) {
		this.txtName = txtName;
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
}
