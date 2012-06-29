package com.tms.fms.facility.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.facility.model.CategoryObject;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.model.LogTable;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;

public class ExcelImportForm extends Form {

	
	protected String id;
	protected String username;
	
	private boolean newExcel;
	public static final int CATEGORY_COLUMN = 7;
	public static final int FACILITY_COLUMN = 18;
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
	
    
    public ExcelImportForm() {
    }
   
    
	public void init(){
		setMethod("POST");
		this.newExcel = true;
		
		fileUpload = new FileUpload("fileUpload");
		fileUpload.setSize("30");		
		
		//Dynamic checkboxes
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		try{
			DaoQuery properties = new DaoQuery();
			collGroup = service.getGroups(properties, 0, -1, "groupName", false);
						
			cbDefaultGroup = new CheckBox[collGroup.size()];
			int x = 0;

			for (Iterator it = collGroup.iterator(); it.hasNext();){
				Group group = (Group) it.next();
				
				String id = group.getId();
				String name = group.getGroupName();
				
				//To void dot in groupId(id.replaceFirst("kacang.services.security.Group_", "")+"="+name);
				cbDefaultGroup[x] = new CheckBox(id.replaceFirst("kacang.services.security.Group_", ""),name);	
				addChild(cbDefaultGroup[x]);
				x++;				
			}
        } catch (Exception e) {
            throw new RuntimeException("Error loading setup properties: " + e.toString());
        }
		
        importButton = new Button("importButton", Application.getInstance().getMessage("mohvl.button.import", "Import"));
        cancelButton = new Button("cancelbutton", Application.getInstance().getMessage("mohvl.button.cancel", "Cancel"));        
        
        addChild(fileUpload);        
        addChild(importButton);
        addChild(cancelButton);

	}	
	
	
	public String getDefaultTemplate() {    
		return "fms/exlImport";	
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
		String errors  = "";
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
		try{
		StorageFile sf = fileUpload.getStorageFile(event.getRequest());
		if(sf == null)
			return new Forward("Empty");
		foldername = sf.getName();		
		storeFile(event);
		root = storage.getRootPath() + FOLDER_EXCEL_UPLOAD;
		File file = new File(root + "/" + foldername);				
		workbook = Workbook.getWorkbook(file);
		
		////
		try{
			upload4(workbook);
		}catch(Exception e){
			
		}
		////
		
		Sheet sheetCategory = workbook.getSheet(CATEGORY_SHEET);
		Sheet sheetFacility = workbook.getSheet(FACILITY_SHEET);
		
		int columnCategory = sheetCategory.getColumns();
		int columnFacility = sheetFacility.getColumns();
		
		int columnCategorySize = CATEGORY_COLUMN;
		int columnFacilitySize = FACILITY_COLUMN;
		
		
		FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		Cell[] cellCategory = new Cell[columnCategorySize];
		String[] contentCategory = new String[columnCategorySize];
		CategoryObject co = new CategoryObject();
		String[] compare2 = new String[columnCategorySize];
				
		if((columnCategory == columnCategorySize) && (columnFacility == columnFacilitySize)){
			
			//Checking duplicate
			String toCompare = "";
			int jq = 0;			
			for (int r=1; r < sheetCategory.getRows(); r++) {		
				int j = 0;
					cellCategory[j] = sheetCategory.getCell(j,r);
					contentCategory[j] = cellCategory[j].getContents().trim();
					toCompare = contentCategory[j];
					
					if(!(toCompare == null || "".equals(toCompare))){
												
						if(!idExist(toCompare)){	//check id in db
							
						for (int c = r; c < sheetCategory.getRows(); c++) {
							//check duplicate id in sheet
							if(c+1 < sheetCategory.getRows()){
								cellCategory[jq] = sheetCategory.getCell(jq,c+1);							
								compare2[jq] =  cellCategory[j].getContents().trim();
								
								if(toCompare.equals(compare2[jq])){
									
									int firstLine = r + 1;
									int secondLine = c + 2;
									logtable = new LogTable();
									errors = "Duplicate id:"+toCompare;
									logtable.setFileName(sheetCategory.getName());
									logtable.setErrors(errors);
									logtable.setErrorsLine(firstLine+","+secondLine);
									errorsInFile.add(logtable);
									//return new Forward(FORWARD_DUPLICATE_ID);
									
								}
							}							
						}					
					}
			}
					
			}					
				//Category					
				//start from column 2 to avoid title and empty space
				for (int i=1; i<sheetCategory.getRows(); i++) {		//template rows
					co = new CategoryObject();
					for (int j=0; j<columnCategorySize; j++) {		//template columns
						cellCategory[j] = sheetCategory.getCell(j,i);
						contentCategory[j] = cellCategory[j].getContents().trim();
							if(contentCategory[0] == null || "".equals(contentCategory[0]))
								break;
							
							co.setId(contentCategory[0]);
							co.setName(contentCategory[1]);
							co.setDescription(contentCategory[2]);
							co.setDepartment_id(contentCategory[3]);
							co.setUnit_id(contentCategory[4]);
							if(!(contentCategory[5] == null)){
								if(contentCategory[5].toUpperCase().equals("N")){
									co.setParent_cat(contentCategory[5]);
									co.setParent_cat_id(contentCategory[6]);
								}else
									co.setParent_cat("Y");
									
							}
														
							co.setCreatedby(getWidgetManager().getUser().getId());
							co.setCreatedby_date(new Date());
							co.setStatus("1");											
				}
					try{
						if(!(co.getId() == null || "".equals(co.getId()))){
							if(!idExist(co.getId()))	//check id in db
								
								totalCategory++;
								mod.insertCategory(co);		
						}
						
					}catch(Exception e){
						Log.getLog(getClass()).error("Error save category from excel: "+e);
					}
			}
		
						
			//Facility
			FacilityObject fo = new FacilityObject();
			FacilityModule fmod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			
			Cell[] cellFacility = new Cell[columnFacilitySize];
			String[] contentFacility = new String[columnFacilitySize];
			String[] bcCompare2 = new String[columnFacilitySize]; 
			
			//start from column 2 to avoid title and empty space			
			for (int i = 1; i<sheetFacility.getRows(); i++) {		//template rows
				fo = new FacilityObject();												
				fo.setId(UuidGenerator.getInstance().getUuid());
				for (int j=0; j<columnFacilitySize; j++) {		//template columns
					cellFacility[j] = sheetFacility.getCell(j,i);
					contentFacility[j] = cellFacility[j].getContents().trim();
					//Log.getLog(getClass()).info(contentFacility[j]);
					
					//if 1st column is empty continue to next
					if(contentFacility[0] == null || "".equals(contentFacility[0])){
						break;
						
					}
						
					if(j == 11){
						int o = 0;
						if(contentFacility[11] == null || "".equals(contentFacility[11])){
							int colm = j + 1;
							int row = i + 1;
							Log.getLog(getClass()).error(sheetFacility.getName()+":null BARCODE at row:"+row+",colm"+colm);
							logtable = new LogTable();
							errors = "BARCODE is empty";
							logtable.setFileName(sheetFacility.getName());
							logtable.setErrors(errors);
							logtable.setErrorsLine(row+"");
							errorsInFile.add(logtable);
				
						}else{			
							String bcCompare = contentFacility[j];
							for (int c = i; c < sheetFacility.getRows(); c++) {
								//check duplicate id in sheet
								if(c+1 < sheetFacility.getRows()){
									cellFacility[j] = sheetFacility.getCell(j,c+1);							
									bcCompare2[o] =  cellFacility[j].getContents().trim();
									
									if(bcCompare.equals(bcCompare2[o])){
										
										int firstLine = i + 1;
										int secondLine = c + 2;
										
										Log.getLog(getClass()).info(sheetFacility.getName()+":Duplicate BARCODE:"+bcCompare+" between line:"+firstLine+", and line:"+secondLine);
										logtable = new LogTable();
										errors = "Duplicate BARCODE found:"+bcCompare;
										logtable.setFileName(sheetFacility.getName());
										logtable.setErrors(errors);
										logtable.setErrorsLine(firstLine+","+secondLine);
										errorsInFile.add(logtable);
										//return new Forward(FORWARD_DUPLICATE_ID);							
									}
								}							
							}		
						}
					}
					
					if(j == 12){
						int o = 0;
						if(contentFacility[j] == null || "".equals(contentFacility[j])){
							int colm = j + 1;
							int row = i + 1;
							Log.getLog(getClass()).error(sheetFacility.getName()+":null SERIAL number at row:"+row+",colm"+colm);
							logtable = new LogTable();
							errors = "SERIAL number is empty";
							logtable.setFileName(sheetFacility.getName());
							logtable.setErrors(errors);
							logtable.setErrorsLine(row+"");
							errorsInFile.add(logtable);
	
						}/*else{						allow duplicate	
							String snCompare = contentFacility[j];
							for (int c = i; c < sheetFacility.getRows(); c++) {
								//check duplicate id in sheet
								if(c+1 < sheetFacility.getRows()){
									cellFacility[j] = sheetFacility.getCell(j,c+1);							
									bcCompare2[o] =  cellFacility[j].getContents().trim();
									
									if(snCompare.equals(bcCompare2[o])){
										
										int firstLine = i + 1;
										int secondLine = c + 2;
										
										Log.getLog(getClass()).info(sheetFacility.getName()+":Duplicate SERIAL NO:"+snCompare+" between line:"+firstLine+", and line:"+secondLine);
										logtable = new LogTable();
										errors = "Duplicate SERIAL number found:"+snCompare;
										logtable.setFileName(sheetFacility.getName());
										logtable.setErrors(errors);
										logtable.setErrorsLine(firstLine+","+secondLine);
										errorsInFile.add(logtable);
										//return new Forward(FORWARD_DUPLICATE_ID);
										
									}
								}							
							}		
						
					}*/
					
					String item = contentFacility[1];
					String barcode = contentFacility[11];			
					if(!(item == null || barcode == null)){
						if(!itemExist(item,barcode)){		//checking at db			
						
						fo.setCategory_id(contentFacility[0]);
						fo.setName(contentFacility[1]);
						fo.setDescription(contentFacility[2]);
						fo.setChannel_id(contentFacility[3]);
						fo.setMaketype(contentFacility[4]);
						fo.setModel_name(contentFacility[5]);
						
						if(!(contentFacility[6] == null)){
							if(contentFacility[6].toString().toUpperCase().equals("Y")){
								fo.setIs_pm("Y");
								if(contentFacility[7].toUpperCase().equals("N"))
									fo.setPm_month(contentFacility[8]);
								else
									fo.setPm_year(contentFacility[8]);						
							}else
								fo.setIs_pm("N");
						}
										
						fo.setPm_type(contentFacility[7]);
						fo.setIs_pool(contentFacility[9]);			
						if(!(contentFacility[10] == null || "".equals(contentFacility[10])))
							fo.setRelated_id(getFacilityObject(contentFacility[10]).getRelated_id());					
						fo.setBarcode(contentFacility[11]);					
						
						fo.setStatus("0");
						if(!(contentFacility[14] == null || "".equals(contentFacility[14]))){
							if(contentFacility[14].toUpperCase().equals("A")){
								fo.setStatus("1");
							}						
						}
						
						if(!(contentFacility[12] == null || "".equals(contentFacility[12])))
							fo.setEasset_num(contentFacility[12]);
						
						if(!(contentFacility[13] == null || "".equals(contentFacility[13]))){
							fo.setLocation_id(contentFacility[13]);
						}else{
							int colm = j + 1;
							int row = i + 1;
							Log.getLog(getClass()).error(sheetFacility.getName()+":null LOCATION_ID at row:"+row+",colm"+colm);
							logtable = new LogTable();
							errors = "LOCATION ID is empty";
							logtable.setFileName(sheetFacility.getName());
							logtable.setErrors(errors);
							logtable.setErrorsLine(row+"");
							errorsInFile.add(logtable);
						}
						
						if(!(contentFacility[15] == null || "".equals(contentFacility[15]))){
							try{
								SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");					
								if(!(contentFacility[15] == null || "".equals(contentFacility[15])))
									fo.setPurchased_date(df.parse(contentFacility[15]));			
								
							}catch(Exception e){
								Log.getLog(getClass()).error("Error SimpleDateFormat:"+e);
							}
						}
												
						
						if(!(contentFacility[16] == null || "".equals(contentFacility[16])))
							fo.setPurchased_cost(contentFacility[16]);
						else
							fo.setPurchased_cost("0");

						if(!(contentFacility[17] == null || "".equals(contentFacility[17])))
							fo.setDo_num(contentFacility[17]);
						
						fo.setCreatedby_date(new Date());
						fo.setCreatedby(getWidgetManager().getUser().getId());
						//fo.setStatus("1");
						fo.setFacility_id(fo.getId());
						
						}
					}
				}										
			}
				//save
				try{
					if(!(fo.getCategory_id() == null || "".equals(fo.getCategory_id()))){						
						if(idExist(fo.getCategory_id())){	//check the category id is exist b4 save
							if(!(barcodeSerialNoExist(fo.getBarcode(),fo.getEasset_num()))){
								String[] ids = {fo.getRelated_id()};
								totalFacility++;														
								fmod.insertRelatedItem(fo.getId(), ids);								
								if(!(itemExist(fo.getName(),null)))
									fmod.insertFacility(fo);			
								
								FacilityObject foitem = fo;
								foitem.setFacility_id(getFacilityObject(fo.getName()).getId());															
								mod.insertItem(foitem);
								
								FacilityObject f = mod.getFacility(foitem.getFacility_id());
								f.setQuantity(f.getQuantity()+1);
								mod.updateFacility(f);
								
							}
						}else{
							int row = i + 1;
							logtable = new LogTable();
							errors = "Category ID is not exist :"+fo.getCategory_id();
							logtable.setFileName(sheetFacility.getName());
							logtable.setErrors(errors);
							logtable.setErrorsLine(row+"");
							errorsInFile.add(logtable);
						}
					}
				}catch(Exception e){
					Log.getLog(getClass()).error("Error adding new Facility item from excel"+e);
				}
		}
		}else{
			if(!(columnCategory == columnCategorySize)){				
				errors = "Incorrect Category column format!";
				logtable.setFileName(sheetCategory.getName());
				logtable.setErrors(errors);
				logtable.setErrorsLine("Category column sizes must be "+columnCategorySize+ " instead of "+columnCategory );
				errorsInFile.add(logtable);
			}
			if(!(columnFacility == columnFacilitySize)){
				errors = "Incorrect Facility column format!";
				logtable.setFileName(sheetFacility.getName());
				logtable.setErrors(errors);
				logtable.setErrorsLine("Facility column sizes must be "+columnFacilitySize+ " instead of "+columnFacility );
				errorsInFile.add(logtable);
			}
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
		
		String newline = System.getProperty("line.separator");
		errorAll = newline + "File Upload Report"+ newline + "==============" + newline + newline + 
		"File name:" + foldername + newline + 
		"Total records added:-" + newline +  
		"Channel:" + totalChannel +newline +
		"Department:" + totalDept +newline +
		"Location:" + totalLocation +newline +
		"Unit:" + totalUnit +newline +
		"Category:" + totalCategory +newline +
		"Facility:" + totalFacility +newline + newline+		
		"Errors:" + newline +"-------" +newline;
		
		int i = 1;
		for(Iterator it = errorsInFile.iterator(); it.hasNext() ; ){
			logtable = (LogTable)it.next();
			filename = i+")Sheet:"+logtable.getFileName()+newline;
			errorDesc = "Errors:"+ logtable.getErrors()+newline;
			errorLine = "Line:"+logtable.getErrorsLine()+newline;
			error = filename + errorDesc + errorLine + newline;
			errorAll += error;
			i++;
		}
	    
		
		Log.getLog(getClass()).error(errorAll);
		
	    File file = new File("c:/LogReport.txt");
	    try{
		    output = new BufferedWriter(new FileWriter(file));		    
		    output.write(errorAll);		    
		    output.close();
	    }catch(IOException e){
	    	Log.getLog(getClass()).error("Error when writing a file:"+e);
	    }
	    ////
	    
	    //call jsp read log
	    /*String viewUrl = "/ekms/fms/read.jsp";
	    try{
	    event.getResponse().sendRedirect(viewUrl);
	    }catch(Exception e){
	    	Log.getLog(getClass()).error("Error redirecting to URL " + viewUrl, e);
	    }*/
		return forward;
	}
	
	
	public boolean itemExist(String itemName,String barcode){
		boolean exist = false;
		
		try{
		FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		FacilityObject fobj = mod.selectFacility(itemName, barcode);
		if(fobj != null)
			exist = true;
		
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	public boolean idExist(String id){
		boolean exist = false;
		Collection colId = new ArrayList();
		try{
		FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		colId = mod.selectCategory(id);
		if(colId.size() > 0)
			exist = true;
		
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	public boolean idExistChannel(String id){
		boolean exist = false;
		Collection colId = new ArrayList();
		try{
		TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		colId = mod.selectChannel(id);
		if(colId.size() > 0)
			exist = true;
		
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	public boolean idExistDept(String id){
		boolean exist = false;
		Collection colId = new ArrayList();
		try{
		FMSDepartmentManager dept = (FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);
		FMSDepartment fmsdept = dept.getselectFMSDepartment(id);
		
		if(!(fmsdept == null || "".equals(fmsdept)))
				exist = true;
				
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	public boolean idExistLocation(String id){
		boolean exist = false;
		Collection colId = new ArrayList();
		try{
		TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		colId = mod.selectLocation(id);
		if(colId.size() > 0)
			exist = true;
		
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	public boolean idExistUnit(String id){
		boolean exist = false;
		Collection colId = new ArrayList();
		try{
		FMSDepartmentManager dept = (FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);
		FMSUnit fmsunit = dept.getselectFMSUnit(id);
		
		if(!(fmsunit == null || "".equals(fmsunit)))
				exist = true;
				
		}catch(Exception e){
			
		}
				
		return exist;
	}
	
	public FacilityObject getFacilityObject(String relatedName){
		
		FacilityObject fobj = new FacilityObject();
		try{
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			if(!(relatedName == null || "".equals(relatedName))){
				fobj = mod.selectFacility(relatedName, null);						
				
			}
				
		}catch(Exception e){
			
		}
		
		return fobj;
	}
	
	public boolean barcodeSerialNoExist(String barcode, String serialno){
		boolean exist = false;
		Collection colId = new ArrayList();
		
		if((barcode == null || "".equals(barcode)) ||
				(serialno == null || "".equals(serialno)))	//if null just return true to prevent save
			exist = true;
		else{
			try{
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			colId = mod.selectBarcodeSerialNo(barcode);
			if(colId.size() > 0)
				exist = true;
			
			}catch(Exception e){
				
			}
		}
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
		
	
	public Forward upload4(Workbook workbook){
		
		LogTable logtable = new LogTable();
		String errors  = "";
		
		Sheet sheetChannel = workbook.getSheet(CHANNEL_SHEET);
		Sheet sheetDept = workbook.getSheet(DEPARTMENT_SHEET);
		Sheet sheetLocation = workbook.getSheet(LOCATION_SHEET);
		Sheet sheetUnit = workbook.getSheet(UNIT_SHEET);
						
		int columnChannel = sheetChannel.getColumns();
		int columnDept = sheetDept.getColumns();
		int columnLocation = sheetLocation.getColumns();
		int columnUnit = sheetUnit.getColumns();
			
		FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		
		FMSDepartmentManager dept = (FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);		
		FMSDepartment fmsdept= new FMSDepartment();
		
		FMSDepartmentManager manager = (FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);		
		FMSUnit fmsunit= new FMSUnit();
		
		Cell[] cellChannel = new Cell[columnChannel];
		String[] contentChannel = new String[columnChannel];
		Cell[] cellDept = new Cell[columnDept];
		String[] contentDept = new String[columnDept];
		Cell[] cellLocation = new Cell[columnLocation];
		String[] contentLocation = new String[columnLocation];
		Cell[] cellUnit = new Cell[columnUnit];
		String[] contentUnit = new String[columnUnit];
		
		SetupObject so = new SetupObject();
		//String[] compare2 = new String[columnCategorySize];
				
		if((columnChannel == 3) && (columnDept == 3) && (columnLocation == 4) && (columnUnit == 4)){
			
		
			int jq = 0;						
				//Channel		
				for (int r = 1; r < sheetChannel.getRows(); r++) {		//template rows
					so = new SetupObject();
					for (int s = 0; s < columnChannel; s++) {		//template columns
						cellChannel[s] = sheetChannel.getCell(s,r);
						contentChannel[s] = cellChannel[s].getContents().trim();
							if(contentChannel[0] == null || "".equals(contentChannel[0]))
								break;		
							
							so.setSetup_id(contentChannel[0]);
							so.setName(contentChannel[1]);
							so.setDescription(contentChannel[2]);
							so.setStatus("1");
							so.setCreatedby(getWidgetManager().getUser().getId());
							so.setCreatedby_date(new Date());
																								
				}
					try{
						if(!(so.getSetup_id() == null || "".equals(so.getId()))){
							if(!idExistChannel(so.getSetup_id())){	//check id in db								
								totalChannel++;
								module.insertSetupObject(SetupObject.SETUP_CHANNEL, so);
								
							}
						}
						
					}catch(Exception e){
						Log.getLog(getClass()).error("Error save CHANNEL from excel: "+e);
					}
				}
										
				//Department
				for (int r = 1; r < sheetDept.getRows(); r++) {		//template rows
					fmsdept= new FMSDepartment();
					
					for (int s = 0; s < columnDept; s++) {		//template columns
						cellDept[s] = sheetDept.getCell(s,r);
						contentDept[s] = cellDept[s].getContents().trim();
							if(contentDept[0] == null || "".equals(contentDept[0]))
								break;							
														
							fmsdept.setId(contentDept[0]);
							fmsdept.setName((contentDept[1]));
							fmsdept.setDescription((contentDept[2]));
							fmsdept.setHOD(FMSDepartmentManager.SYSTEM_ADMIN);
							fmsdept.setStatus("1");																	
				} 
					try{
						if(!(fmsdept.getId() == null || "".equals(fmsdept.getId()))){
							if(!idExistDept(fmsdept.getId())){	//check id in db
								
								totalDept++;
								dept.addDepartment(fmsdept);
							}
						}
						
					}catch(Exception e){
						Log.getLog(getClass()).error("Error save DEPARTMENT from excel: "+e);
					}
				}
				
				
				//Location
				for (int r = 1; r < sheetLocation.getRows(); r++) {		//template rows
					so = new SetupObject();
					
					for (int s = 0; s < columnLocation; s++) {		//template columns
						cellLocation[s] = sheetLocation.getCell(s,r);
						contentLocation[s] = cellLocation[s].getContents().trim();
							if(contentLocation[0] == null || "".equals(contentLocation[0]))
								break;							
										
							so.setSetup_id(contentLocation[0]);							
							so.setName(contentLocation[1]);
							so.setDescription(contentLocation[2]);
							so.setStatus("1");
							so.setCreatedby(getWidgetManager().getUser().getId());
							so.setCreatedby_date(new Date());																
				}
					try{
						if(!(so.getSetup_id() == null || "".equals(so.getId()))){
							if(!idExistLocation(so.getSetup_id())){	//check id in db
							
							totalLocation++;
							module.insertSetupObject(SetupObject.SETUP_LOCATION, so);
							}						
						}
						
					}catch(Exception e){
						Log.getLog(getClass()).error("Error save LOCATION from excel: "+e);
					}
				}
				
				
				//Unit
				for (int r = 1; r < sheetUnit.getRows(); r++) {		//template rows
					fmsunit= new FMSUnit();
					
					for (int s = 0; s < columnUnit; s++) {		//template columns
						cellUnit[s] = sheetUnit.getCell(s,r);
						contentUnit[s] = cellUnit[s].getContents().trim();
							if(contentUnit[0] == null || "".equals(contentUnit[0]))
								break;							
									
							fmsunit.setId(contentUnit[0]);
							fmsunit.setName(contentUnit[1]);
							fmsunit.setDescription(contentUnit[2]);
							fmsunit.setDepartment_id(contentUnit[3]);
							fmsunit.setStatus("1");
							fmsunit.setHOU(FMSDepartmentManager.SYSTEM_ADMIN);																								
				}
					try{
						if(!(fmsunit.getId() == null || "".equals(fmsunit.getId()))){
							if(!idExistUnit(fmsunit.getId())){	//check id in db
											
								totalUnit++;
								manager.addUnit(fmsunit);
							}
						}
						
					}catch(Exception e){
						Log.getLog(getClass()).error("Error save UNIT from excel: "+e);
					}
				}
				
		
			}else{
				errors = "Problem with excel file. Wrong format for Channel,Department,Location and Unit";
				logtable.setFileName("");
				logtable.setErrors(errors);
				logtable.setErrorsLine("");
				errorsInFile.add(logtable);
			}
		return new Forward();
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
}





