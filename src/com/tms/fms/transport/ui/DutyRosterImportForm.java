package com.tms.fms.transport.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.facility.model.CategoryObject;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.model.LogTable;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.model.WorkingProfile;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.transport.model.TransportModule;

public class DutyRosterImportForm extends Form {

	
	protected String id;
	protected String username;
	
	private boolean newExcel;
	public static final int CATEGORY_COLUMN = 7;
	public static final int FACILITY_COLUMN = 18;
	public static final int DUTY_COLUMN = 8;
	
	public static final String FOLDER_EXCEL_UPLOAD = "/fmsExcel/";
	public static final String TITLE_CELL = "UNIT";
	public static final String DUPLICATE_ID = "UNIT";
	public static final String YES_CHECK = "Y";
	public static final String NO_CHECK = "N";
	public static final String FORWARD_DUPLICATE_ID = "id duplicate";
	public static final String FORWARD_DUPLICATE_ITEM = "item duplicate";
	public static final String FORWARD_NULL_ID = "id null";
	public static final String FORWARD_NULL_VALUE = "value null";
	public static final String CATEGORY_SHEET = "CATEGORY";
	public static final String FACILITY_SHEET = "FACILITY";
	public static final String CHANNEL_SHEET = "CHANNEL";
	public static final String DEPARTMENT_SHEET = "DEPARTMENT";
	public static final String LOCATION_SHEET = "LOCATION";
	public static final String UNIT_SHEET = "UNIT";
	
	public static final String DUTY_ROSTER_SHEET = "DUTY";
	
	
	
	
    private FileUpload fileUpload;    
    private CheckBox[] cbDefaultGroup;
    private Collection collGroup;
    private int errorInt;
         
//    protected boolean editMode = false;
    private Button importButton, cancelButton;
    private int totalAdded;
//    protected boolean editMode = false;
	private int totalRecords;
	private int totalUpdated;
	private int totalError;
	
	private int totalChannel = 0;
	private int totalDept = 0;
	private int totalLocation = 0;
	private int totalUnit = 0;
	private int totalFacility = 0;
	private int totalCategory = 0;
		
	private Collection errorsInFile;
	
	private String editWorkingProfileDurationId;
	

	public DutyRosterImportForm() {
    }
   
    
	public void init(){
		setMethod("POST");
		this.newExcel = true;
		
		fileUpload = new FileUpload("fileUpload");
		fileUpload.setSize("30");		
						
        importButton = new Button("importButton", Application.getInstance().getMessage("mohvl.button.import", "Import"));
        cancelButton = new Button("cancelbutton", Application.getInstance().getMessage("mohvl.button.cancel", "Cancel"));        
        
        addChild(fileUpload);        
        addChild(importButton);
        addChild(cancelButton);

	}	
	
	
	public String getDefaultTemplate() {    
		return "fms/dutyImport";	
    }

	
	public Forward actionPerformed(Event event) {
		Forward forward = new Forward();

		
        String button = findButtonClicked(event);
		//if (!isEditMode()) {
	        
	        if(cancelButton.getAbsoluteName().equals(button)){
	            init();

	            return new Forward();
	        }        
		//}
		forward = super.actionPerformed(event);
        return forward;
    }
	
	
	public Forward onValidate(Event evt) {
		
		Forward fwd = new Forward();
				
		try{
			fwd = readFile(evt);
		}catch(Exception e){
			
		}	
		
		return fwd;
	}
	

	private Forward readFile(Event event){
		
		errorsInFile = new ArrayList();
		LogTable logtable = new LogTable();
		LogTable studioLogTable = new LogTable();
		String errors  = "";
		String houUserId = Application.getInstance().getCurrentUser().getId();
		Forward forward = new Forward("import");
		Workbook workbook;
		StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);		
		String foldername = "";
		String root = "";
		 totalChannel = 0;
		 totalDept = 0;
		 totalLocation = 0;
		 totalUnit = 0;
		 totalFacility = 0;
		 totalCategory = 0;		
		int totalDuty = 0;
		int totalDutyRejected = 0;
		int totalUpdateDuty = 0;
			
		try{
		StorageFile sf = fileUpload.getStorageFile(event.getRequest());
		if(sf == null)
			return new Forward("Empty");
		foldername = sf.getName();		
		storeFile(event);
		root = storage.getRootPath() + FOLDER_EXCEL_UPLOAD;
		File file = new File(root + "/" + foldername);				
		workbook = Workbook.getWorkbook(file);
				
		Sheet sheetDuty = workbook.getSheet(DUTY_ROSTER_SHEET);
						
		int columnDuty = sheetDuty.getColumns();
				
		int columnCategorySize = CATEGORY_COLUMN;
		int columnFacilitySize = FACILITY_COLUMN;
		int columnDutySize = DUTY_COLUMN;
		
		
		
		FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		Cell[] cellCategory = new Cell[columnCategorySize];
		String[] contentCategory = new String[columnCategorySize];
		CategoryObject co = new CategoryObject();
		String[] compare2 = new String[columnCategorySize];
		
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		WorkingProfile wp = new WorkingProfile();
		Cell[] cellDuty = new Cell[columnDutySize];
		String[] contentDuty = new String[columnDutySize];
		FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
				
		boolean dateProblem = false;
		if(columnDutySize == columnDuty){		//check column size must be same
			
			//Checking duplicate
			String username = "";
			String userId = "";		
						
						for (int i=1; i<sheetDuty.getRows(); i++) {		//template rows
							logtable = new LogTable();
							wp = new WorkingProfile();
							for (int j=0; j<columnDutySize; j++) {		//template columns
								cellDuty[j] = sheetDuty.getCell(j,i);
								contentDuty[j] = cellDuty[j].getContents().trim();
									if(contentDuty[0] == null || "".equals(contentDuty[0]))
										break;
									
									if(j == DUTY_COLUMN - 1){
										
										dateProblem = false;
										//1st Column
										if(!(null == contentDuty[0] || "".equals(contentDuty[0]))){
											try{
												SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yy");					
												if(!(contentDuty[0] == null || "".equals(contentDuty[0])))												
													wp.setStartDate(df.parse(contentDuty[0]));
																							
											}catch(Exception e){
												dateProblem = true;
												int errorline = i + 1;
												Log.getLog(getClass()).error("Error SimpleDateFormat:"+e);
												logtable = new LogTable();
												errors = "Wrong Date Format :"+contentDuty[0];														
												logtable.setErrors(errors);
												logtable.setErrorsLine(""+errorline);
												errorsInFile.add(logtable);
											}
										}else{
											dateProblem = true;
											int errorline = i + 1;											
											logtable = new LogTable();
											errors = "Date is empty :"+contentDuty[0];														
											logtable.setErrors(errors);
											logtable.setErrorsLine(""+errorline);
											errorsInFile.add(logtable);
										}
										//2nd Column
										if(!(null == contentDuty[1]|| "".equals(contentDuty[1]))){
											try{
												SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yy");					
												if(!(contentDuty[1] == null || "".equals(contentDuty[1])))												
													wp.setEndDate(df.parse(contentDuty[1]));
																							
											}catch(Exception e){
												dateProblem = true;
												int errorline = i + 1;
												Log.getLog(getClass()).error("Error SimpleDateFormat:"+e);
												logtable = new LogTable();
												errors = "Wrong Date Format :"+contentDuty[1];														
												logtable.setErrors(errors);
												logtable.setErrorsLine(""+errorline);
												errorsInFile.add(logtable);
											}
										}else{
											dateProblem = true;
											int errorline = i + 1;											
											logtable = new LogTable();
											errors = "Date is empty :"+contentDuty[0];														
											logtable.setErrors(errors);
											logtable.setErrorsLine(""+errorline);
											errorsInFile.add(logtable);
										}
										
										//check date
										if(!dateProblem){
											if(wp.getStartDate().after(wp.getEndDate())){
												int errorline = i + 1;
												logtable = new LogTable();
												errors = "Date From:"+contentDuty[0]+"  is greater than Date To:"+contentDuty[1];														
												logtable.setErrors(errors);
												logtable.setErrorsLine(""+errorline);
												errorsInFile.add(logtable);
											}
										}
										
										//3rd Column
										if(!(null == contentDuty[2]|| "".equals(contentDuty[2]))){
											try{
												String wpId = "";
												String wProfileName = contentDuty[2];
												wpId = module.getWorkingProfileCode(wProfileName);
												
												if(!("".equals(wpId) || null == wpId))
													wp.setWorkingProfileId(wpId);
												else{
													int errorline = i + 1;
													logtable = new LogTable();
													errors = "Wrong Working Profile id :"+contentDuty[2];														
													logtable.setErrors(errors);
													logtable.setErrorsLine(""+errorline);
													errorsInFile.add(logtable);
												}
												
											}catch(Exception e){
												Log.getLog(getClass()).error("Error getWorkingProfileCode:"+e);
												int errorline = i + 1;
												logtable = new LogTable();
												errors = "Wrong Working Profile id :"+contentDuty[2];														
												logtable.setErrors(errors);
												logtable.setErrorsLine(""+errorline);
												errorsInFile.add(logtable);
											}
										}else{
											int errorline = i + 1;											
											logtable = new LogTable();
											errors = "Working Profile is empty :"+contentDuty[0];														
											logtable.setErrors(errors);
											logtable.setErrorsLine(""+errorline);
											errorsInFile.add(logtable);
										}
										
										//4th Column
										// get the studio id assign the manpower to the studio which is 
										// should have manpower inside the ratecard
										
										Collection studios = null;
										ArrayList arrStudio = new ArrayList();
										int counter=1;
										
										
										if(!(null == contentDuty[3]|| "".equals(contentDuty[3]))){
											String studio1 = contentDuty[3];
											String xx = "";
											if(studio1 != null && !studio1.equals("")){
												studios = module.getStudiosByCode(studio1);
												if(studios!=null && studios.size()>0){
													for (Iterator iter = studios.iterator(); iter.hasNext();) {
														HashMap map = (HashMap) iter.next();
														arrStudio.add((String) map.get("name"));
														counter++;
												}
											}
											
										}
										}
										
										//5th Column
										if(!(null == contentDuty[4]|| "".equals(contentDuty[4]))){
											String studio2 = contentDuty[4];
											
											if(studio2 != null && !studio2.equals("")){
												studios = module.getStudiosByCode(studio2);
												if(studios!=null && studios.size()>0){
													for (Iterator iter = studios.iterator(); iter.hasNext();) {
														HashMap map = (HashMap) iter.next();
														if(counter<9)
															arrStudio.add((String) map.get("name"));
														counter++;
												}
											}
												
										}
										}
										
										//6th Column
										if(!(null == contentDuty[5]|| "".equals(contentDuty[5]))){
											String studio3 = contentDuty[5];
											
											if(studio3 != null && !studio3.equals("")){
												
													studios = module.getStudiosByCode(studio3);
													if(studios!=null && studios.size()>0){
														for (Iterator iter = studios.iterator(); iter.hasNext();) {
															HashMap map = (HashMap) iter.next();
															if(counter<9)
																arrStudio.add((String) map.get("name"));
															counter++;
												}
											}
												
										}
										}
										
										//7th Column
										if(!(null == contentDuty[6]|| "".equals(contentDuty[6]))){
											String studio4 = contentDuty[6];
											
											if(studio4 != null && !studio4.equals("")){
												studios = module.getStudiosByCode(studio4);
												if(studios!=null && studios.size()>0){
													for (Iterator iter = studios.iterator(); iter.hasNext();) {
														HashMap map = (HashMap) iter.next();
														if(counter<9)
															arrStudio.add((String) map.get("name"));
														counter++;
												}
											}
												
												
										}
										}
										
										//for (Iterator iter = studios.iterator(); iter.hasNext();) {
											
										if(arrStudio.size()>0 && arrStudio.get(0)!=null && !"".equals(arrStudio.get(0)) ){
											if(module.getStudio(houUserId, arrStudio.get(0).toString())!= null && !module.getStudio(houUserId, arrStudio.get(0).toString()).equals("")){
												wp.setStudio1(module.getStudio(houUserId, arrStudio.get(0).toString()));
												}else{
												logError(studioLogTable, i, errors, arrStudio.get(0).toString());
												}
											}
										
										if(arrStudio.size()>1 && arrStudio.get(1)!=null && !"".equals(arrStudio.get(1)) ){
											if(module.getStudio(houUserId, arrStudio.get(1).toString())!= null && !module.getStudio(houUserId, arrStudio.get(1).toString()).equals("")){
												wp.setStudio2(module.getStudio(houUserId, arrStudio.get(1).toString()));
												}else{
												logError(studioLogTable, i, errors, arrStudio.get(1).toString());
												}
											}
										if(arrStudio.size()>2 && arrStudio.get(2)!=null && !"".equals(arrStudio.get(2)) ){
											if(module.getStudio(houUserId, arrStudio.get(2).toString())!= null && !module.getStudio(houUserId, arrStudio.get(2).toString()).equals("")){
												wp.setStudio3(module.getStudio(houUserId, arrStudio.get(2).toString()));
											}else{
												logError(studioLogTable, i, errors, arrStudio.get(2).toString());
										}
										}
										if(arrStudio.size()>3 && null!=arrStudio.get(3)){
											if(module.getStudio(houUserId, arrStudio.get(3).toString())!= null && !module.getStudio(houUserId, arrStudio.get(3).toString()).equals("")){
												wp.setStudio4(module.getStudio(houUserId, arrStudio.get(3).toString()));
												}else{
												logError(studioLogTable, i, errors, arrStudio.get(3).toString());
												}
											}
										if(arrStudio.size()>4 && arrStudio.get(4)!=null && !"".equals(arrStudio.get(4)) ){
											if(module.getStudio(houUserId, arrStudio.get(4).toString())!= null && !module.getStudio(houUserId, arrStudio.get(4).toString()).equals("")){
												wp.setStudio5(module.getStudio(houUserId, arrStudio.get(4).toString()));
											}else{
												logError(studioLogTable, i, errors, arrStudio.get(4).toString());
										}
										}
										if(arrStudio.size()>5 &&arrStudio.get(5)!=null && !"".equals(arrStudio.get(5)) ){	
											if(module.getStudio(houUserId, arrStudio.get(5).toString())!= null && !module.getStudio(houUserId, arrStudio.get(5).toString()).equals("")){
												wp.setStudio6(module.getStudio(houUserId, arrStudio.get(5).toString()));
												}else{
												logError(studioLogTable, i, errors, arrStudio.get(5).toString());
												}
											}
										if(arrStudio.size()>6 &&arrStudio.get(6)!=null && !"".equals(arrStudio.get(6)) ){
											if(module.getStudio(houUserId, arrStudio.get(6).toString())!= null && !module.getStudio(houUserId, arrStudio.get(6).toString()).equals("")){
												wp.setStudio7(module.getStudio(houUserId, arrStudio.get(6).toString()));
											}else{
												logError(studioLogTable, i, errors, arrStudio.get(6).toString());
										}
										}
										if(arrStudio.size()>7 &&arrStudio.get(7)!=null && !"".equals(arrStudio.get(7)) ){
											if(module.getStudio(houUserId, arrStudio.get(7).toString())!= null && !module.getStudio(houUserId, arrStudio.get(7).toString()).equals("")){
												wp.setStudio8(module.getStudio(houUserId, arrStudio.get(7).toString()));
											}else{
												logError(studioLogTable, i, errors, arrStudio.get(7).toString());
											}
										}
										
										//12th Column
										if(!(null == contentDuty[7]|| "".equals(contentDuty[7]))){
											try{
												Map<String, String> userMap = new HashMap<String, String>();
												username = contentDuty[7];		
												
												try{
													userId = manager.selectExistSecurityUser(username);
												}catch(Exception er){}
												userMap.put(userId, username);																						
																					
												
												for(Iterator itr=userMap.keySet().iterator();itr.hasNext();){
													userId=(String)itr.next();
													Log.getLog(getClass()).info(userId);
												}
													if(userIsExist(username)){							
														wp.setManpowerMap(userMap);
														Log.getLog(getClass()).info("Add User"+username);
																				
													}else{				
														int errorline = i + 1;
														logtable = new LogTable();
														errors = "This username is not exist:"+username;														
														logtable.setErrors(errors);
														logtable.setErrorsLine(""+errorline);
														errorsInFile.add(logtable);
													}
													
																		        
							        
											}catch(Exception e){
												Log.getLog(getClass()).error("Error getWorkingProfileCode:"+e);
											}
										}else{
											int errorline = i + 1;											
					 						logtable = new LogTable();
											errors = "Username is empty :"+contentDuty[0];														
											logtable.setErrors(errors);
											logtable.setErrorsLine(""+errorline);
											errorsInFile.add(logtable);
										}
										
										if("".equals(logtable.getErrors()) || null == logtable.getErrors()){	//no error in logtable
											if(!(wp.getWorkingProfileId() == null || "".equals(wp.getWorkingProfileId()))){												
												
												//check duplicate
												if(!isDuplicate(userId, wp.getWorkingProfileId(), wp.getStartDate(), wp.getEndDate() )){
													
													//check if any update for wp
													if(!isUpdateWP(userId, wp.getStartDate(), wp.getEndDate())){
														
														try {													
															module.insertWorkingProfileDuration(wp);
															totalDuty++;														
														}catch (Exception e) {
															Log.getLog(getClass()).error(e.toString()); 
															return new Forward("FAILED");
														} 
														
													}else{
														
														/*try {			No UPDATE
															wp.setWorkingProfileDurationId(editWorkingProfileDurationId);
															module.updateWorkingProfileDuration(wp);
															totalUpdateDuty++;														
														}catch (Exception e) {
															Log.getLog(getClass()).error(e.toString()); 
															return new Forward("FAILED");
														} */
														int errorline = i + 1;
														logtable = new LogTable();
														errors = "Record already exist!";														
														logtable.setErrors(errors);
														logtable.setErrorsLine(""+errorline);
														errorsInFile.add(logtable);
														totalDutyRejected++;
														
													}
												}else{
													int errorline = i + 1;
													logtable = new LogTable();
													errors = "Record already exist!";														
													logtable.setErrors(errors);
													logtable.setErrorsLine(""+errorline);
													errorsInFile.add(logtable);
													totalDutyRejected++;
												}
													
												
											}else{					
												int errorline = i + 1;
												logtable = new LogTable();
												errors = "Working Profile Id is Null";														
												logtable.setErrors(errors);
												logtable.setErrorsLine(""+errorline);
												errorsInFile.add(logtable);
												totalDutyRejected++;
											}
											
										}else{
											totalDutyRejected++;
										}
									}																		
							}	
							
						}		
				
		}else{			
			logtable = new LogTable();
			errors = "Incorrect column size";			
			logtable.setErrors(errors);
			logtable.setErrorsLine("The column sizes must be "+columnDutySize+ " instead of "+columnDuty );
			errorsInFile.add(logtable);
			forward = new Forward("Fail");
		}
			
		}catch(Exception e){
			Log.getLog(getClass()).error("Error save ALL from excel file: "+e);
			forward = new Forward("Fail");
		}
		
		////	Error Report
		Writer output = null;
		String filename = "";
		String errorDesc = "";
		String errorLine = "";
		String error = "";
		String errorAll = "";
		String username = getWidgetManager().getUser().getName();
		
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy"); 
		String dateToday = df.format(new Date());
				
		String newline = System.getProperty("line.separator");
		errorAll = newline + Application.getInstance().getMessage("fms.label.duty.fileUploadReport") + newline + "==============" + newline + newline + 
		Application.getInstance().getMessage("scheduling.label.date")+" :" + dateToday + newline +
		Application.getInstance().getMessage("fms.label.duty.uploaderName")+" :" + username + newline + 
		Application.getInstance().getMessage("fms.label.duty.fileName")+" :"+ foldername + newline + 
		Application.getInstance().getMessage("fms.label.duty.recordAdded")+" :"+ totalDuty +newline +
		Application.getInstance().getMessage("fms.label.duty.recordUpdated")+" :"+ totalUpdateDuty +newline +
		Application.getInstance().getMessage("fms.label.duty.recordsRejected")+" :"+ totalDutyRejected +newline  + newline +
		Application.getInstance().getMessage("fms.label.duty.errors")+" :"+ newline +"-------" +newline;
		
		int i = 1;
		for(Iterator it = errorsInFile.iterator(); it.hasNext() ; ){
			logtable = (LogTable)it.next();			
			errorDesc = i+")Error:"+ logtable.getErrors()+newline;
			errorLine = "Line:"+logtable.getErrorsLine()+newline;
			error = filename + errorDesc + errorLine + newline;
			errorAll += error;
			i++;
		}
	    
		
		Log.getLog(getClass()).error(errorAll);
		
	    File file = new File("c:/DutyLogReport.txt");
	    try{
		    output = new BufferedWriter(new FileWriter(file));		    
		    output.write(errorAll);		    
		    output.close();
	    }catch(IOException e){
	    	Log.getLog(getClass()).error("Error when writing a file:"+e);
	    }
	    
		return forward;
	}
	
	private void logError(LogTable studioLogTable, int i, String errors,String contentDuty){
		int errorline = i + 1;											
		studioLogTable = new LogTable();
		errors = "Import for this studio : "+contentDuty+" error, please check the rate card";														
		studioLogTable.setErrors(errors);
		studioLogTable.setErrorsLine(""+errorline);
		errorsInFile.add(studioLogTable);
	}
	
	public boolean isDuplicate(String userId, String wpName, Date startDate, Date endDate){
		boolean exist = false;
		Collection colId = new ArrayList();
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try{
		
		colId = module.selectWorkingProfile(userId, wpName, startDate, endDate);
				
		if(colId.size() > 0)
			exist = true;
		
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	public boolean isUpdateWP(String userId, Date startDate, Date endDate){
		
		editWorkingProfileDurationId = null;
		WorkingProfile workingProfile = null;
		
		boolean exist = false;
		Collection colId = new ArrayList();
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try{
		
		colId = module.selectWorkingProfileForUpdate(userId, startDate, endDate);
				
		if(colId.size() > 0){
			for(Iterator it = colId.iterator(); it.hasNext(); ){
				workingProfile = (WorkingProfile) it.next();
				editWorkingProfileDurationId = workingProfile.getWorkingProfileDurationId();
			}
			exist = true;
		}
			
		
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	
	
	public boolean userIsExist(String username){
		
		boolean exist = false;
		FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
		
		String existuser = null;
		try{
			existuser = manager.selectExistSecurityUser(username);
		}catch(Exception er){}
		
		if(!(null == existuser))
			exist = true;
		
		return exist;		
		
	}
			
	//---------------------------------------------- upload -------------------------------------------
	private void storeFile(Event evt) {
       
        StorageFile sf;
        StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
        
        try {
            
            sf = fileUpload.getStorageFile(evt.getRequest());
            sf.setParentDirectoryPath(FOLDER_EXCEL_UPLOAD);            
            storage.store(sf);

        } catch(Exception e) {
        	Log.getLog(this.getClass()).error("Error importing batch", e);
        }
    }
		
	public FileUpload getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}
	
	public Button getImportButton() {
		return importButton;
	}
	public void setImportButton(Button importButton) {
		this.importButton = importButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}
	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public CheckBox[] getCbDefaultGroup() {
		return cbDefaultGroup;
	}
	public void setCbDefaultGroup(CheckBox[] cbDefaultGroup) {
		this.cbDefaultGroup = cbDefaultGroup;
	}
	public Collection getCollGroup() {
		return collGroup;
	}
	public void setCollGroup(Collection collGroup) {
		this.collGroup = collGroup;
	}

	public int getErrorInt() {
		return errorInt;
	}
	public void setErrorInt(int errorInt) {
		this.errorInt = errorInt;
	}


	public int getTotalAdded() {
		return totalAdded;
	}


	public void setTotalAdded(int totalAdded) {
		this.totalAdded = totalAdded;
	}

	public int getTotalRecords() {
		return totalRecords;
	}


	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}


	public int getTotalUpdated() {
		return totalUpdated;
	}


	public void setTotalUpdated(int totalUpdated) {
		this.totalUpdated = totalUpdated;
	}


	public int getTotalError() {
		return totalError;
	}


	public void setTotalError(int totalError) {
		this.totalError = totalError;
	}


	public Collection getErrorsInFile() {
		return errorsInFile;
	}


	public void setErrorsInFile(Collection errorsInFile) {
		this.errorsInFile = errorsInFile;
	}
	
	public String getEditWorkingProfileDurationId() {
		return editWorkingProfileDurationId;
	}


	public void setEditWorkingProfileDurationId(String editWorkingProfileDurationId) {
		this.editWorkingProfileDurationId = editWorkingProfileDurationId;
	}
}





