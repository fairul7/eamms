/*
 * Created on Jun 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import java.text.NumberFormat;
import java.util.Properties;

import kacang.Application;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UploadFilesForm extends Form{
	
	private FileUpload fuAttach1;
    private FileUpload fuAttach2;
    private FileUpload fuAttach3;
    
    private TextField txtAttach1;
    private TextField txtAttach2;
    private TextField txtAttach3;
    
    private TextBox txtBxAttach1;
    private TextBox txtBxAttach2;
    private TextBox txtBxAttach3;
    
    private FolderSelectBox selBxParentFolder;
    
    private Button btUpload;
    private Button btDone;
    
    private String parentId;
    
    private String fileUploadMsg1 = "testing testing testing";
    private String fileUploadMsg2;
    private String fileUploadMsg3;
    
    private String fileLimit;
    
    public void init() {
    	setMethod("POST");
    	initForm();
    }
    
    public void initForm(){
    	removeChildren();
    	setColumns(2);
    	
    	addChild(new Label("lblAtt", "Upload file(s) to"));
    	selBxParentFolder = new FolderSelectBox("parentFolder");
		selBxParentFolder.setParentSelection(false);
        addChild(selBxParentFolder);
        
        addChild(new Label("lblAtt10", ""));
        addChild(new Label("lblAtt11", ""));
    	
    	addChild(new Label("lblAtt1", Application.getInstance().getMessage("mf.label.file", "File")+" 1"));
    	txtAttach1 = new TextField("txtAttach1");
    	txtAttach1.setMaxlength("255");
    	addChild(txtAttach1);
    	
    	addChild(new Label("lblDesc1", Application.getInstance().getMessage("mf.label.desc", "Description")));
    	txtBxAttach1 = new TextBox("txtBxAttach1");
    	txtBxAttach1.setCols("80");
    	txtBxAttach1.setRows("2");
    	addChild(txtBxAttach1);
    	
    	addChild(new Label("lblBlank1", ""));
    	fuAttach1 = new FileUpload("attach1");
        addChild(fuAttach1);
        
        addChild(new Label("lblBlank2", ""));
        addChild(new Label("lblBlank3", ""));
        
        addChild(new Label("lblAtt2", Application.getInstance().getMessage("mf.label.file", "File")+" 2"));
    	txtAttach2 = new TextField("txtAttach2");
    	txtAttach2.setMaxlength("255");
    	addChild(txtAttach2);
    	
    	addChild(new Label("lblDesc2", Application.getInstance().getMessage("mf.label.desc", "Description")));
    	txtBxAttach2 = new TextBox("txtBxAttach2");
    	txtBxAttach2.setCols("80");
    	txtBxAttach2.setRows("2");
    	addChild(txtBxAttach2);
    	
    	addChild(new Label("lblBlank4", ""));
    	fuAttach2 = new FileUpload("attach2");
        addChild(fuAttach2);
        
        addChild(new Label("lblBlank5", ""));
        addChild(new Label("lblBlank6", ""));
        
        addChild(new Label("lblAtt3", Application.getInstance().getMessage("mf.label.file", "File")+" 3"));
    	txtAttach3 = new TextField("txtAttach3");
    	txtAttach3.setMaxlength("255");
    	addChild(txtAttach3);
    	
    	addChild(new Label("lblDesc3", Application.getInstance().getMessage("mf.label.desc", "Description")));
    	txtBxAttach3 = new TextBox("txtBxAttach3");
    	txtBxAttach3.setCols("80");
    	txtBxAttach3.setRows("2");
    	addChild(txtBxAttach3);
    	
    	addChild(new Label("lblBlank7", ""));
    	fuAttach3 = new FileUpload("attach3");
        addChild(fuAttach3);
        
        addChild(new Label("lblBlank8", ""));
        addChild(new Label("lblBlank9", ""));
        
        btUpload = new Button("btUpload", Application.getInstance().getMessage("mf.label.upload","upload"));
        btDone = new Button("btDone", Application.getInstance().getMessage("mf.label.done","done"));
        
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.setColumns(2);
        buttonPanel.addChild(btUpload);
        buttonPanel.addChild(btDone);
        
        addChild(buttonPanel);
        
        selBxParentFolder.init();
        
        Properties properties = Application.getInstance().getProperties();
		long upload = Long.parseLong(properties.getProperty("myfolder.upload.quota"));
		fileLimit = String.valueOf(upload);
		
    }
    
    public Forward onValidate(Event evt){
    	String button = findButtonClicked(evt);
        button = button == null ? "" : button;

        if (button.endsWith("btUpload")) {
            doUpload(evt);
            initForm();
            return new Forward("upload");
            
        } else if (button.endsWith("btDone")) {
        	
            return new Forward("done");
        }
        
		return new Forward("mainPage");
    }
    
    public void doUpload(Event evt){
    	Application app = Application.getInstance();
    	MyFolderModule mf = (MyFolderModule)app.getModule(MyFolderModule.class);
    	User user = getWidgetManager().getUser();
    	
    	StorageService ss;
		StorageFile sf;
		
		long quota = 0;
		double usedSpace = 0;
		long uploadQuota = 0;
		
    	try{
    		Properties properties = app.getProperties();
    		uploadQuota = Long.parseLong(properties.getProperty("myfolder.upload.quota"));
    		uploadQuota = uploadQuota * 1024 * 1024;
    		
    		quota = mf.getUserQuota(user.getId());
    		
    		if(quota == 0){
    			quota = Long.parseLong(properties.getProperty("myfolder.space.quota"));
    			quota = quota * 1024;
    		}
    		
    		Log.getLog(getClass());
    		usedSpace = mf.getUserUsedSpace(user.getId());
    		
    		ss = (StorageService) Application.getInstance().getService(StorageService.class);
    		
    		//when myFolder.jsp first load then the user click upload files 
//    		if(parentId == null){
//				parentId = mf.getRootFolder(user.getId());
//			}
    		parentId = selBxParentFolder.getSelectedFolderId();
    		FileFolder parentFolder = mf.loadFileFolder(parentId);
    		
    		//attachment 1
            sf = fuAttach1.getStorageFile(evt.getRequest());
            if(sf!=null) {
            	
            	usedSpace += (double)sf.getSize()/(double)1024;
            	
            	//exceed my folder quota
            	if(usedSpace < quota){
            		
            		//upload file size > upload quota 
	            	if(sf.getSize() < uploadQuota){
	            		
	                    sf.setParentDirectoryPath(parentFolder.getFilePath());
	                    sf = createFileDbEntry(sf, parentFolder, 1);
	                    
	                    ss.store(sf);
	                    evt.getRequest().setAttribute("message1", Application.getInstance().getMessage("mf.message.file1.success", "File 1 successfully uploaded"));
	            	}else{
	            		
	            		//set message
	            		usedSpace -= (double)sf.getSize()/(double)1024;
	            		evt.getRequest().setAttribute("message1", Application.getInstance().getMessage("mf.message.file1.fail", "File 1 was not being uploaded because its size exceed upload limit"));
	            	}
            	}else{
            		usedSpace -= (double)sf.getSize()/(double)1024;
            		
            		evt.getRequest().setAttribute("message", Application.getInstance().getMessage("mf.message.exceedQuota", "Sorry, your uploads has exceed your folder space quota"));
            		//set message
            		return;
            	}
            }
            
            //attachment 2
            sf = fuAttach2.getStorageFile(evt.getRequest());
            if(sf!=null) {
            	
            	usedSpace += (double)sf.getSize()/(double)1024;
            	
            	if(usedSpace < quota){
            		
	            	if(sf.getSize() < uploadQuota){
	            		
		                sf.setParentDirectoryPath(parentFolder.getFilePath());
		                sf = createFileDbEntry(sf, parentFolder, 2);
		                ss.store(sf);
		                evt.getRequest().setAttribute("message2", Application.getInstance().getMessage("mf.message.file2.success", "File 2 successfully uploaded"));
	            	}else{
	            		usedSpace -= (double)sf.getSize()/(double)1024;
	            		
	            		evt.getRequest().setAttribute("message2", Application.getInstance().getMessage("mf.message.file2.fail", "File 2 was not being uploaded because its size exceed upload limit"));
	            		//set message
	            	}
            	}else{
            		usedSpace -= (double)sf.getSize()/(double)1024;
            		
            		evt.getRequest().setAttribute("message", Application.getInstance().getMessage("mf.message.exceedQuota", "Sorry, your uploads has exceed your folder space quota"));
            		//set message
            		return;
            	}
            }
    		
            //attachment 3
            sf = fuAttach3.getStorageFile(evt.getRequest());
            if(sf!=null) {
            	
            	usedSpace += (double)sf.getSize()/(double)1024;
            	
            	if(usedSpace < quota){
            		
	            	if(sf.getSize() < uploadQuota){
	            		
		                sf.setParentDirectoryPath(parentFolder.getFilePath());
		                sf = createFileDbEntry(sf, parentFolder, 3);
		                ss.store(sf);
		                evt.getRequest().setAttribute("message3", Application.getInstance().getMessage("mf.message.file3.success", "File 3 successfully uploaded"));
	            	}else{
	            		usedSpace -= (double)sf.getSize()/(double)1024;
	            		
	            		evt.getRequest().setAttribute("message3", Application.getInstance().getMessage("mf.message.file3.fail", "File 3 was not being uploaded because its size exceed upload limit"));
	            		//set message
	            	}
            	}else{
            		usedSpace -= (double)sf.getSize()/(double)1024;
            		
            		evt.getRequest().setAttribute("message", Application.getInstance().getMessage("mf.message.exceedQuota", "Sorry, your uploads has exceed your folder space quota"));
            		//set message
            		return;
            	}
            }
    		
    	}
    	catch(Exception e){
    		Log.getLog(getClass()).error("error in uploading files: " + e.getMessage(), e);
    	}
    }
    
    
    
    public StorageFile createFileDbEntry(StorageFile fs, FileFolder parentFolder, int attNo){
    	Application app = Application.getInstance();
    	MyFolderModule mf = (MyFolderModule)app.getModule(MyFolderModule.class);
    	User user = getWidgetManager().getUser();
    	String path;
    	FileFolder file;
    	UuidGenerator uuidGenerator = UuidGenerator.getInstance();
    	String tempFileName = new String();
    	
    	try{
    		file = new FileFolder();
    		file.setId(uuidGenerator.getUuid());
    		file.setUserId(user.getId());
    		file.setUserName(user.getUsername());
    		
    		if(attNo == 1){
    			//set file name as the original file name if the file name field is empty
    			tempFileName = (String)txtAttach1.getValue();
    			if(tempFileName == null || "".equals(tempFileName)){
    				tempFileName = fs.getName();
    			}
    			file.setFileDescription((String)txtBxAttach1.getValue());
    		}
    		else if(attNo ==2){
    			tempFileName = (String)txtAttach2.getValue();
    			if(tempFileName == null || "".equals(tempFileName)){
    				tempFileName = fs.getName();
    			}
    			file.setFileDescription((String)txtBxAttach2.getValue());
    		}
    		else{
    			tempFileName = (String)txtAttach3.getValue();
    			if(tempFileName == null || "".equals(tempFileName)){
    				tempFileName = fs.getName();
    			}
    			file.setFileDescription((String)txtBxAttach3.getValue());
    		}
    		
    		file.setFileName(tempFileName);
    		
    		file.setAccessCountPrivate(0);
    		file.setAccessCountPublic(0);
    		
    		file.setFileType(firstLetterCapital(fs.getContentType()));
    		file.setFileAccess(parentFolder.getFileAccess());
    		
    		path = new String();
    		int lastDotIndex = fs.getName().lastIndexOf(".");
    		String extension = fs.getName().substring(lastDotIndex);
    		String realFileName = fs.getName();
    		
    		if(realFileName.length() > 78){
    			realFileName = realFileName.substring(0, 78);
    		}else{
    			realFileName = realFileName.substring(0, lastDotIndex);
    		}
    		
    		tempFileName = realFileName + " " + file.getId() + extension; 
    		
    		path = parentFolder.getFilePath() + tempFileName;
    		
    		file.setFilePath(path);
    		fs.setName(tempFileName);
    		
    		NumberFormat formatter = NumberFormat.getInstance();
    		formatter.setMaximumFractionDigits(2);
    		formatter.setGroupingUsed(false);
    		
    		String sizeStr = formatter.format((double)fs.getSize()/(double)1024);
    		double sizeDou = Double.parseDouble(sizeStr);
    		
    		file.setFileSize(sizeDou);
    		file.setParentId(parentId);
    		
    		mf.createFiles(file);
    		mf.logAction(user.getId(), file.getId(), "upload file");
    		return fs;
    	}
    	catch(Exception e){
    		Log.getLog(getClass()).error("error in create file db entry: " + e.getMessage(), e);
    		return null;
    	}
    }
    
    public String firstLetterCapital(String ori){
    	char[] oriCharAry = ori.toCharArray();
    	String first = String.valueOf(oriCharAry[0]);
    	first = first.toUpperCase();
    	StringBuffer modString = new StringBuffer(first);
    	
		for(int i=1; i<oriCharAry.length; i++){
			modString.append(oriCharAry[i]);
		}
		
    	return modString.toString();
    }
    
    public void onRequest(Event evt) {
    	
		initForm();
	}
    
    public String getDefaultTempalte(){
    	return "myfolder/form";
    }
    
	public String getFileLimit() {
		return fileLimit;
	}

	public void setFileLimit(String fileLimit) {
		this.fileLimit = fileLimit;
	}

	public FolderSelectBox getSelBxParentFolder() {
		return selBxParentFolder;
	}
	public void setSelBxParentFolder(FolderSelectBox selBxParentFolder) {
		this.selBxParentFolder = selBxParentFolder;
	}
	/**
	 * @return Returns the fileUploadMsg1.
	 */
	public String getFileUploadMsg1() {
		return fileUploadMsg1;
	}
	/**
	 * @param fileUploadMsg1 The fileUploadMsg1 to set.
	 */
	public void setFileUploadMsg1(String fileUploadMsg1) {
		this.fileUploadMsg1 = fileUploadMsg1;
	}
	/**
	 * @return Returns the fileUploadMsg2.
	 */
	public String getFileUploadMsg2() {
		return fileUploadMsg2;
	}
	/**
	 * @param fileUploadMsg2 The fileUploadMsg2 to set.
	 */
	public void setFileUploadMsg2(String fileUploadMsg2) {
		this.fileUploadMsg2 = fileUploadMsg2;
	}
	/**
	 * @return Returns the fileUploadMsg3.
	 */
	public String getFileUploadMsg3() {
		return fileUploadMsg3;
	}
	/**
	 * @param fileUploadMsg3 The fileUploadMsg3 to set.
	 */
	public void setFileUploadMsg3(String fileUploadMsg3) {
		this.fileUploadMsg3 = fileUploadMsg3;
	}
	/**
	 * @return Returns the btDone.
	 */
	public Button getBtDone() {
		return btDone;
	}
	/**
	 * @param btDone The btDone to set.
	 */
	public void setBtDone(Button btDone) {
		this.btDone = btDone;
	}
	/**
	 * @return Returns the btUpload.
	 */
	public Button getBtUpload() {
		return btUpload;
	}
	/**
	 * @param btUpload The btUpload to set.
	 */
	public void setBtUpload(Button btUpload) {
		this.btUpload = btUpload;
	}
	/**
	 * @return Returns the fuAttach1.
	 */
	public FileUpload getFuAttach1() {
		return fuAttach1;
	}
	/**
	 * @param fuAttach1 The fuAttach1 to set.
	 */
	public void setFuAttach1(FileUpload fuAttach1) {
		this.fuAttach1 = fuAttach1;
	}
	/**
	 * @return Returns the fuAttach2.
	 */
	public FileUpload getFuAttach2() {
		return fuAttach2;
	}
	/**
	 * @param fuAttach2 The fuAttach2 to set.
	 */
	public void setFuAttach2(FileUpload fuAttach2) {
		this.fuAttach2 = fuAttach2;
	}
	/**
	 * @return Returns the fuAttach3.
	 */
	public FileUpload getFuAttach3() {
		return fuAttach3;
	}
	/**
	 * @param fuAttach3 The fuAttach3 to set.
	 */
	public void setFuAttach3(FileUpload fuAttach3) {
		this.fuAttach3 = fuAttach3;
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
	 * @return Returns the txtAttach1.
	 */
	public TextField getTxtAttach1() {
		return txtAttach1;
	}
	/**
	 * @param txtAttach1 The txtAttach1 to set.
	 */
	public void setTxtAttach1(TextField txtAttach1) {
		this.txtAttach1 = txtAttach1;
	}
	/**
	 * @return Returns the txtAttach2.
	 */
	public TextField getTxtAttach2() {
		return txtAttach2;
	}
	/**
	 * @param txtAttach2 The txtAttach2 to set.
	 */
	public void setTxtAttach2(TextField txtAttach2) {
		this.txtAttach2 = txtAttach2;
	}
	/**
	 * @return Returns the txtAttach3.
	 */
	public TextField getTxtAttach3() {
		return txtAttach3;
	}
	/**
	 * @param txtAttach3 The txtAttach3 to set.
	 */
	public void setTxtAttach3(TextField txtAttach3) {
		this.txtAttach3 = txtAttach3;
	}
	/**
	 * @return Returns the txtBxAttach1.
	 */
	public TextBox getTxtBxAttach1() {
		return txtBxAttach1;
	}
	/**
	 * @param txtBxAttach1 The txtBxAttach1 to set.
	 */
	public void setTxtBxAttach1(TextBox txtBxAttach1) {
		this.txtBxAttach1 = txtBxAttach1;
	}
	/**
	 * @return Returns the txtBxAttach2.
	 */
	public TextBox getTxtBxAttach2() {
		return txtBxAttach2;
	}
	/**
	 * @param txtBxAttach2 The txtBxAttach2 to set.
	 */
	public void setTxtBxAttach2(TextBox txtBxAttach2) {
		this.txtBxAttach2 = txtBxAttach2;
	}
	/**
	 * @return Returns the txtBxAttach3.
	 */
	public TextBox getTxtBxAttach3() {
		return txtBxAttach3;
	}
	/**
	 * @param txtBxAttach3 The txtBxAttach3 to set.
	 */
	public void setTxtBxAttach3(TextBox txtBxAttach3) {
		this.txtBxAttach3 = txtBxAttach3;
	}
}
