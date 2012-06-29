package com.tms.sam.po.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.security.User;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;


public class AttachmentForm extends Form{
	public static final String FORWARD_ERROR = "error";
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_DONE = "done";
	
	private FileUpload attach1;
	private FileUpload attach2;
	private FileUpload attach3;
	
	private Button done;
	private Button upload;
	
	private Map attachmentMap;
	
	public void init(){
		setMethod("POST");
		Application app = Application.getInstance();
		attach1 = new FileUpload("attach1");
        addChild(attach1);

        attach2 = new FileUpload("attach2");
        addChild(attach2);

        attach3 = new FileUpload("attach3");
        addChild(attach3);

        upload = new Button("upload", app.getMessage("po.label.upload", "Upload"));
        done = new Button("done", app.getMessage("po.label.done", "Done"));
        addChild(upload);
        addChild(done);
	}
	
	public void onRequest(Event event) {
		  attachmentMap = getAttachmentMapFromSession(event);
	}
	    
	public Forward onValidate(Event event) {
		  
		  Log log;
	      log = Log.getLog(getClass());

	        try {
	            if("remove".equals(event.getType())) {
	                // remove attachment
	                doRemove(event);
	                return new Forward(FORWARD_SUCCESS);

	            } else {
	                String button = findButtonClicked(event);
	                button = button == null ? "" : button;

	                if (button.endsWith("upload")) {
	                    // process upload
	                    doUpload(event);
	                    return new Forward(FORWARD_SUCCESS);

	                } else if (button.endsWith("done")) {
	                    // process done
	                    return new Forward(FORWARD_DONE);

	                } else {
	                    String msg = "Unknown button '" + button + "' pressed!";
	                    log.error(msg);
	                    event.getRequest().getSession().setAttribute("error", msg);
	                    return new Forward(FORWARD_ERROR);
	                }
	            }

	        } catch (IOException e) {
	            Log.getLog(getClass()).error(e.getMessage(), e);
	            event.getRequest().getSession().setAttribute("error", e);
	            return new Forward(FORWARD_ERROR);

	        } catch (StorageException e) {
	            Log.getLog(getClass()).error(e.getMessage(), e);
	            event.getRequest().getSession().setAttribute("error", e);
	            return new Forward(FORWARD_ERROR);
	        }
	 }
	  
	 public void doRemove(Event event) throws StorageException,UnsupportedEncodingException {
		    String encodedKey, key;
	        StorageService ss;
	        StorageFile sf;
	     
	        User currentUser;
    	    Application app = Application.getInstance();
	    	currentUser =app.getCurrentUser();
	    	
	        encodedKey = event.getParameter("key");
	        key = URLDecoder.decode(encodedKey, "UTF-8");
	      
	        ss = (StorageService) Application.getInstance().getService(StorageService.class);
	        attachmentMap = getAttachmentMapFromSession(event);
	        attachmentMap.remove(key);

	        sf = new StorageFile("/purchaseOrdering"+ "/" + currentUser.getId() + "/temp/" + key);
	        ss.delete(sf);
		 
	 }
	 
	 public void doUpload(Event event) throws IOException, StorageException{
		    StorageService ss;
	        StorageFile sf;
	        User currentUser;
    	    Application app = Application.getInstance();
	    	currentUser =app.getCurrentUser();
	    		    	
	        ss = (StorageService) Application.getInstance().getService(StorageService.class);
	        attachmentMap = getAttachmentMapFromSession(event);

	        // attachment 1
	        sf = attach1.getStorageFile(event.getRequest());
	        sf.getSize();
	        if(sf!=null) {
	        	//module.getFileSize();
	            sf.setParentDirectoryPath("/purchaseOrdering"+ "/" + currentUser.getId() + "/temp/");
	            ss.store(sf);
	            attachmentMap.put(sf.getName(), "-data in storage service-");
	        }

	        // attachment 2
	        sf = attach2.getStorageFile(event.getRequest());
	        if(sf!=null) {
	            sf.setParentDirectoryPath("/purchaseOrdering" + "/" + currentUser.getId() + "/temp/");
	            ss.store(sf);
	            attachmentMap.put(sf.getName(), "-data in storage service-");
	        }

	        // attachment 3
	        sf = attach3.getStorageFile(event.getRequest());
	        if(sf!=null) {
	            sf.setParentDirectoryPath("/purchaseOrdering" + "/" + currentUser.getId() + "/temp/");
	            ss.store(sf);
	            attachmentMap.put(sf.getName(), "-data in storage service-");
	        }
	 }
	 
	 public String getTemplate() {
		 return "po/attachmentForm";
	 }
	 
	 public static Map getAttachmentMapFromSession(Event event) {
	        Map attachmentMap;
	        HttpSession session;

	        session = event.getRequest().getSession();
	        attachmentMap = (Map) session.getAttribute("attachmentMap");
	        if(attachmentMap==null) {
	            attachmentMap = new SequencedHashMap();
	            session.setAttribute("attachmentMap", attachmentMap);
	        }
	        return attachmentMap;
	 }

	 // === [ getters/setters] ==================================================
	public FileUpload getAttach1() {
		return attach1;
	}

	public void setAttach1(FileUpload attach1) {
		this.attach1 = attach1;
	}

	public FileUpload getAttach2() {
		return attach2;
	}

	public void setAttach2(FileUpload attach2) {
		this.attach2 = attach2;
	}

	public FileUpload getAttach3() {
		return attach3;
	}

	public void setAttach3(FileUpload attach3) {
		this.attach3 = attach3;
	}

	public Map getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public Button getDone() {
		return done;
	}

	public void setDone(Button done) {
		this.done = done;
	}

	public Button getUpload() {
		return upload;
	}

	public void setUpload(Button upload) {
		this.upload = upload;
	}
}
