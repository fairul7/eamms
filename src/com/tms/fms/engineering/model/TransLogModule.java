package com.tms.fms.engineering.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.Application;
import kacang.model.DefaultModule;
import kacang.services.security.User;
import kacang.util.Log;

public class TransLogModule extends DefaultModule {
	private SimpleDateFormat dateFormat;
	
	static {
		Log.getLog(TransLogModule.class).info("***** START UP *****");
	}
	
	public TransLogModule() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public void info(String requestId, String action, String text) {
		Application application = Application.getInstance();
		
		String message = "";
		
		// requestId
		if (requestId != null && !requestId.equals("")) {
			message += (requestId + " ");
		}
		
		// action type
		if (action != null && !action.equals("")) {
			message += (action + " ");
		}
		
		// text
		if (text != null && !text.equals("")) {
			message += (text + " ");
		}
		
		// get username
		String username = null;
		User user = application.getCurrentUser();
		if (user != null) {
			username = user.getUsername();
		}
		if (username != null) {
			message += "(username=" + username + ")";
		}
		
		Log.getLog(getClass()).info(message);
	}
	
	public void info(EngineeringRequest eRequest, String action, String text) {
		try {
			text = formatText(eRequest, text);
		} catch (Exception e) {
			Log.getLog(getClass()).error("Logging Error", e);
		}
		info(eRequest.getRequestId(), action, text.trim());
	}
	
	public void info(Service service, String action, String text) {
		try {
			text = formatText(service, text);
		} catch (Exception e) {
			Log.getLog(getClass()).error("Logging Error", e);
		}
		info(service.getRequestId(), action, text.trim());
	}
	
	public String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	protected String formatText(EngineeringRequest eRequest, String text) {
		if (text == null) {
			text = "";
		}
		
		// title
		if (eRequest.getTitle() != null) {
			text += " [" + eRequest.getTitle() + "]";
		}
		
		// required from
		if (eRequest.getRequiredFrom() != null) {
			text += " requiredFrom=" + dateFormat.format(eRequest.getRequiredFrom());
		}
		
		// required to
		if (eRequest.getRequiredTo() != null) {
			text += " requiredTo=" + dateFormat.format(eRequest.getRequiredTo());
		}
		
		return text;
	}
	
	protected String formatText(Service service, String text) {
		if (text == null) {
			text = "";
		}
		
		if (service instanceof ScpService) {
			ScpService scp = (ScpService) service;
			
			// facility name
			if (scp.getFacility() != null) {
				text += " [" + scp.getFacility() + "]";
			}
			
			// required from
			if (scp.getRequiredFrom() != null) {
				text += " requiredFrom=" + dateFormat.format(scp.getRequiredFrom());
			}
			
			// required to
			if (scp.getRequiredTo() != null) {
				text += " requiredTo=" + dateFormat.format(scp.getRequiredTo());
			}
			
			text += " blockBooking=" + scp.getBlockBooking();
			text += " facilityId=" + scp.getFacilityId();
			text += " id=" + scp.getId();
		
		} else if (service instanceof PostProductionService) {
			PostProductionService postProduction = (PostProductionService) service;
			
			// facility name
			if (postProduction.getFacility() != null) {
				text += " [" + postProduction.getFacility() + "]";
			}
			
			// required from
			if (postProduction.getRequiredDate() != null) {
				text += " requiredFrom=" + dateFormat.format(postProduction.getRequiredDate());
			}
			
			// required to
			if (postProduction.getRequiredDateTo() != null) {
				text += " requiredTo=" + dateFormat.format(postProduction.getRequiredDateTo());
			}
			
			text += " blockBooking=" + postProduction.getBlockBooking();
			text += " facilityId=" + postProduction.getFacilityId();
			text += " id=" + postProduction.getId();
			
		} else if (service instanceof VtrService) {
			VtrService vtr = (VtrService) service;
			
			// facility name
			if (vtr.getFacility() != null) {
				text += " [" + vtr.getFacility() + "]";
			}
			
			// required from
			if (vtr.getRequiredDate() != null) {
				text += " requiredFrom=" + dateFormat.format(vtr.getRequiredDate());
			}
			
			// required to
			if (vtr.getRequiredDateTo() != null) {
				text += " requiredTo=" + dateFormat.format(vtr.getRequiredDateTo());
			}
			
			text += " blockBooking=" + vtr.getBlockBooking();
			text += " facilityId=" + vtr.getFacilityId();
			text += " id=" + vtr.getId();
			
		} else if (service instanceof ManpowerService) {
			ManpowerService manpower = (ManpowerService) service;
			
			// competency name
			if (manpower.getCompetencyName() != null) {
				text += " [" + manpower.getCompetencyName() + "]";
			}
			
			// required from
			if (manpower.getRequiredFrom() != null) {
				text += " requiredFrom=" + dateFormat.format(manpower.getRequiredFrom());
			}
			
			// required to
			if (manpower.getRequiredTo() != null) {
				text += " requiredTo=" + dateFormat.format(manpower.getRequiredTo());
			}
			
			text += " blockBooking=" + manpower.getBlockBooking();
			text += " quantity=" + manpower.getQuantity();
			text += " competencyId=" + manpower.getCompetencyId();
			text += " id=" + manpower.getId();
			
		} else if (service instanceof StudioService) {
			StudioService studio = (StudioService) service;
			
			// facility name
			if (studio.getFacility() != null) {
				text += " [" + studio.getFacility() + "]";
			}
			
			// required from
			if (studio.getBookingDate() != null) {
				text += " requiredFrom=" + dateFormat.format(studio.getBookingDate());
			}
			
			// required to
			if (studio.getBookingDateTo() != null) {
				text += " requiredTo=" + dateFormat.format(studio.getBookingDateTo());
			}
			
			text += " blockBooking=" + studio.getBlockBooking();
			text += " facilityId=" + studio.getFacilityId();
			text += " id=" + studio.getId();
			
		} else if (service instanceof OtherService) {
			OtherService other = (OtherService) service;
			
			// facility name
			if (other.getFacility() != null) {
				text += " [" + other.getFacility() + "]";
			}
			
			// required from
			if (other.getRequiredFrom() != null) {
				text += " requiredFrom=" + dateFormat.format(other.getRequiredFrom());
			}
			
			// required to
			if (other.getRequiredTo() != null) {
				text += " requiredTo=" + dateFormat.format(other.getRequiredTo());
			}
			
			text += " blockBooking=" + other.getBlockBooking();
			text += " quantity=" + other.getQuantity();
			text += " facilityId=" + other.getFacilityId();
			text += " id=" + other.getId();
			
		} else if (service instanceof TvroService) {
			TvroService tvro = (TvroService) service;
			
			// facility name
			if (tvro.getFacility() != null) {
				text += " [" + tvro.getFacility() + "]";
			}
			
			// required from
			if (tvro.getRequiredDate() != null) {
				text += " requiredFrom=" + dateFormat.format(tvro.getRequiredDate());
			}
			
			// required to
			if (tvro.getRequiredDateTo() != null) {
				text += " requiredTo=" + dateFormat.format(tvro.getRequiredDateTo());
			}
			
			text += " blockBooking=" + tvro.getBlockBooking();
			text += " facilityId=" + tvro.getFacilityId();
			text += " id=" + tvro.getId();
			
		} else {
			text += " -- NOT SUPPORTED YET --";
		}
		
		return text;
	}
}
