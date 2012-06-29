package com.tms.fms.facility.ui.validator;

import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;

import kacang.Application;
import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;

public class ValidatorItemBarcode extends Validator {
	public static final String PREPARE_CHECK_OUT = "prepare check out";
	public static final String CHECK_OUT = "check out";
	public static final String CHECK_IN = "check in";
	public static final int ASSIGNMENT_CHECK_IN = 1;
	public static final int INTERNAL_CHECK_IN = 2;
	
	private String status;
	private int checkInType = 0;
	
	public ValidatorItemBarcode(){
		super();
	}
	
	public ValidatorItemBarcode(String name, String status){
		super(name);
		this.status = status;
	}
	
	public ValidatorItemBarcode(String name, int checkInType) {
		super(name);
		this.status = CHECK_IN;
		this.checkInType = checkInType;
	}
	
	public boolean validate(FormField formField) {
		String barcode = formField.getValue().toString();
		if(!"".equals(barcode.trim())){
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			FacilityObject item = mod.getItem(barcode);
			if(item.getBarcode() == null || "".equals(item.getBarcode())){
				setText(Application.getInstance().getMessage("fms.facility.msg.invalidItemBarcode"));
				return false;
			}else{
				if(status.equals(PREPARE_CHECK_OUT)){
					if("0".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailableInactive"));
						return false;
					}else if("W".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailableWriteOff"));
						return false;
					}else if("M".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailableMissing"));
						return false;
					}else if("P".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailablePrepareCheckOut"));
						return false;
					}
				}else if(status.equals(CHECK_OUT)){
					if("0".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailableInactive"));
						return false;
					}else if("W".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailableWriteOff"));
						return false;
					}else if("M".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailableMissing"));
						return false;
					}else if("C".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsUnavailableCheckOut"));
						return false;
					}
				}else if(status.equals(CHECK_IN)){
					if(!"C".equals(item.getStatus())){
						setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsNotCheckOut"));
						return false;
					}
					
					// additional checking
					if (checkInType == ASSIGNMENT_CHECK_IN) {
						// check for assignment check-out
						boolean isAssignment = mod.hasAssignmentCheckOut(barcode);
						if (!isAssignment) {
							// note: for item that is marked for check out but without corresponding record, need to use internal check in 
							setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsInternalCheckOut"));
							return false;
						}
					} else if (checkInType == INTERNAL_CHECK_IN) {
						// check for internal check-out
						boolean isInternal = mod.hasInternalCheckOut(barcode);
						if (!isInternal) {
							boolean isAssignment = mod.hasAssignmentCheckOut(barcode);
							
							if (isAssignment) {
								setText(Application.getInstance().getMessage("fms.facility.msg.itemStatusIsAssignmentCheckOut"));
								return false;
							}
						}
					}
				}
			}
		}
        return true;
    }
	
}
