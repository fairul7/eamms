package com.tms.fms.engineering.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

//import com.tms.ekms.setup.model.SetupModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupDao;
import com.tms.fms.util.DateDiffUtil;

public class CheckAvailabilityModule extends DefaultModule {
	public static final String ENGINEERING_DEPARTMENT_PROPERTY="fms.facilities.engineering.engineeringDepartmentId";
	public static String ENGINEERING_DEPARTMENT_ID;
	
	
	public void init(){
		super.init();
	}
	
	@SuppressWarnings("unchecked")
	public Collection getFacilityByDate(Date dateChecked, String timeFrom, String timeTo, String bookingType, Collection facilities){
		Collection rows = new ArrayList();
		try{
			// call DAO - selectFacilityByDate()
			CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
			//Collection col = dao.selectFacilityByDate(dateChecked, timeFrom, timeTo, bookingType);
			
			// create new hashMap to collect the data(s)
			// save it into proper variable, so we can call it on JSP Page (checkavailabilityFCfacility.jsp)
			HashMap newMap=new HashMap();
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			newMap.put("dateChecked", sdf.format(dateChecked));
			newMap.put("timeFrom", timeFrom.substring(0, 2) + "." + timeFrom.substring(2));
			newMap.put("timeTo", timeTo.substring(0, 2) + "." + timeTo.substring(2));
			
			if (facilities.size() > 0){
				Object[] arrStr = new String[(facilities.size()*2)];
				
				int i = 0;
				for (Iterator itx=facilities.iterator();itx.hasNext();){
					RateCard rc = (RateCard)itx.next();
					String available = "Available : ";
					String header = "";
					String requestTitle = "";
					
					//int test = countManpowerAvailable(dateChecked, timeFrom.substring(0, 2) + ":" + timeFrom.substring(2), timeTo.substring(0, 2) + ":" + timeTo.substring(2), (String)mpMap.get("competencyId"));
					Collection c = dao.selectFacilityRequest(dateChecked, timeFrom, timeTo, bookingType, rc.getIdCategory());
					
					arrStr[i] = rc.getIdCategory();
					i++;
					int availableFacilities = sumAvailableFacilities(rc.getIdCategory());
					
					if (c.size() > 0){
						int av = 0;
						for(Iterator itr=c.iterator();itr.hasNext();){
							FacilityObject fo = (FacilityObject)itr.next();
							
							av += fo.getQuantityBooked();
							
							requestTitle += "<li><a href='requestDetails.jsp?requestId=" + fo.getRequestId() + "'>";
							requestTitle += fo.getRequestTitle();
							requestTitle += "</a><br />";
							requestTitle += fo.getDisplayTimeFromBooked() + " - " + fo.getDisplayTimeToBooked();
							requestTitle += "</li>";
						}
						
						available += availableFacilities;
						header = "<br /><br />Booked : " + (av) + "<br /><div style='margin-left:-20px'><ul>";
					} else {
						available 	+= availableFacilities;
						header 		= "";
					}
					
					requestTitle += "</ul></div>";
					
					arrStr[i] = available + header + requestTitle;
					i++;					
				}
				
				newMap.put(dateChecked, arrStr);
			}
			
			// save 'newMap' into 'rows' collection
			rows.add(newMap);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getFacilityByDate() ", e);
		}
		return rows;
	}
	
	@SuppressWarnings("unchecked")
	public Collection getManpowerByDate(Date dateChecked, String timeFrom, String timeTo, String bookingType, Collection manpowers){
		Collection rows = new ArrayList();
		Application app = Application.getInstance();
		ENGINEERING_DEPARTMENT_ID = app.getProperty(ENGINEERING_DEPARTMENT_PROPERTY);
		
		try{
			// call DAO - selectFacilityByDate()
			CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
			//Collection col = dao.selectAvailabilityManpowerByDate(dateChecked, timeFrom, timeTo, bookingType);
			
			
			// create new hashMap to collect the data(s)
			// save it into proper variable, so we can call it on JSP Page (checkavailabilityFCfacility.jsp)
			HashMap newMap=new HashMap();
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			newMap.put("dateChecked", sdf.format(dateChecked));
			newMap.put("timeFrom", timeFrom.substring(0, 2) + "." + timeFrom.substring(2));
			newMap.put("timeTo", timeTo.substring(0, 2) + "." + timeTo.substring(2));
			boolean isInNormalWp = dao.isInNormalWorkingProfile(timeFrom.substring(0, 2) + ":" + timeFrom.substring(2), timeTo.substring(0, 2) + ":" + timeTo.substring(2));
			
			if (manpowers.size() > 0){
				String[] arrStr = new String[(manpowers.size()*2)];
				
				int totalAvailable = 0;
				int i = 0;
				for (Iterator itx=manpowers.iterator();itx.hasNext();){
					HashMap mpMap = (HashMap)itx.next();
					String available = "Available : ";
					String header = "<br /><br />Booked : ";
					String requestTitle = "<div style='margin-left:-20px'><ul>";
					
					int availableManpower = dao.countManpowerTotalInPool((String)mpMap.get("competencyId"));
					int test = countManpowerAvailable(dateChecked, timeFrom.substring(0, 2) + ":" + timeFrom.substring(2), timeTo.substring(0, 2) + ":" + timeTo.substring(2), (String)mpMap.get("competencyId"));
					int countMpRest = countManpowerRest(dateChecked, timeFrom.substring(0, 2) + ":" + timeFrom.substring(2), timeTo.substring(0, 2) + ":" + timeTo.substring(2), (String)mpMap.get("competencyId"));
					
					if (isInNormalWp) {
						totalAvailable = availableManpower - countMpRest;
					} else {
						totalAvailable = test;
					}
					
					Collection c = dao.selectAvailabilityManpower(dateChecked, timeFrom, timeTo, bookingType, (String)mpMap.get("competencyId"));
					
					arrStr[i] = (String)mpMap.get("competencyId");
					i++;
					int av = 0;
					if (c.size() > 0){
						
						for(Iterator itr=c.iterator();itr.hasNext();){
							FacilityObject fo = (FacilityObject)itr.next();
							
							av 	+= fo.getQuantityBooked();
							
							requestTitle += "<li><a href='requestDetails.jsp?requestId=" + fo.getRequestId() + "'>";
							requestTitle += fo.getRequestTitle();
							requestTitle += "</a><br />";
							requestTitle += fo.getDisplayTimeFromBooked() + " - " + fo.getDisplayTimeToBooked();
							requestTitle += "</li>";
						}
						
						//available += test - av;
						header += av + "<br />";
					} else {
						//available += String.valueOf(test);
						header = "<br />";
					}
					
					if (totalAvailable <= 0) {
						available += "-";//String.valueOf(totalAvailable);
					} else {
						available += String.valueOf(totalAvailable - av);
					}
					
					requestTitle += "</ul></div>";
					
					arrStr[i] = available + header + requestTitle;
					i++;					
				}
				
				newMap.put(dateChecked, arrStr);
			}
			
			// save 'newMap' into 'rows' collection
			rows.add(newMap);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getFacilityByDate() ", e);
		}
		return rows;
	}
	
	// check availability for manpower
	
	public Map selectManpowerByCompetencyId(String competencyId) throws DaoException {
		HashMap competencyMap = new HashMap();
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		
		try {
			
			competencyMap = (HashMap) dao.selectManpowerByCompetencyId(competencyId);
		} catch (Exception e){
			Log.getLog(getClass()).info(e.getMessage(), e);
		}
		return competencyMap;
	}
	
	public int countManpowerAvailable(Date dateChecked, String timeFrom, String timeTo, String competencyId){
		int countRcm = 0;
		
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		try {
			return dao.countManpowerAvailable(dateChecked, timeFrom, timeTo, competencyId);
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	public int countManpowerRest(Date dateChecked, String timeFrom, String timeTo, String competencyId){
		int countRcm = 0;
		
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		try {
			return dao.countManpowerRest(dateChecked, timeFrom, timeTo, competencyId);
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	public int sumAvailableFacilities(String idCategory){
		int countRateCard = 0;
		
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		try {
			return dao.selectRateCardSumQty(idCategory);
		} catch (DaoException e){
			return countRateCard;
		}
	}
	
	public Collection getAllFacility(String requestId, String service) {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		Collection facilities 	 = new ArrayList();
		Collection scpCol 		 = new ArrayList();
		Collection postCol		 = new ArrayList();
		Collection manpowCol	 = new ArrayList();
		Collection studioCol	 = new ArrayList();
		Collection otherCol		 = new ArrayList();
		Collection tvroCol		 = new ArrayList();
		Collection vtrCol 		 = new ArrayList();
		Collection temp			 = new ArrayList();
		Collection tempF		 = new ArrayList();
		boolean checkFacility	 = false;
		boolean checkManpower	 = false;
		
		
		try {
			if (service != null && !"".equals(service)){
				if (service.equals("SCP")){
					scpCol = dao.selectScpService(requestId);
					
					if (scpCol.size() > 0) {
						for (Iterator iScp = scpCol.iterator();iScp.hasNext();){
							ScpService scp = (ScpService)iScp.next();
							
							//Collection colCatF = dao.selectRateCardCategoryByRCId(scp.getFacilityId());
							Collection colCatF = dao.selectRateCardByRCId(scp.getFacilityId());
							//Collection colCatM = dao.selectRateCardManpowerByRCId(scp.getFacilityId());
							Collection colCatM = dao.selectRateCardMPByRCId(scp.getFacilityId());
							
							temp 	= getFacilityBooked(scp.getRequestId(), colCatF, scp.getRequiredFrom(), scp.getRequiredTo(), scp.getDepartureTime(), scp.getRecordingTo(), "F");
							tempF 	= getFacilityBooked(scp.getRequestId(), colCatM, scp.getRequiredFrom(), scp.getRequiredTo(), scp.getDepartureTime(), scp.getRecordingTo(), "M");
							
							scp.setAdditionalInfo(getAdditionalInfo(temp, tempF));
							
							if ((temp.size() > 0) || (tempF.size() > 0)){
								facilities.add(scp);
							}
						}
					}
				} else if (service.equals("POST")) {
					postCol = dao.selectPostProductionService(requestId);
					temp.clear();
					tempF.clear();
					
					if (postCol.size() > 0) {
						for (Iterator i =postCol.iterator();i.hasNext();){
							PostProductionService post = (PostProductionService)i.next();
							
							Collection colCatF = dao.selectRateCardCategoryByRCId(post.getFacilityId());
							Collection colCatM = dao.selectRateCardManpowerByRCId(post.getFacilityId());
														
							temp 	= getFacilityBooked(post.getRequestId(), colCatF, post.getRequiredDate(), post.getRequiredDateTo(), post.getFromTime(), post.getToTime(), "F");
							tempF 	= getFacilityBooked(post.getRequestId(), colCatM, post.getRequiredDate(), post.getRequiredDateTo(), post.getFromTime(), post.getToTime(), "M");
							
							post.setAdditionalInfo(getAdditionalInfo(temp, tempF));
							
							if ((temp.size() > 0) || (tempF.size() > 0)){
								facilities.add(post);
							}
						}
					}
				} else if (service.equals("MANPOWER")){
					manpowCol	= dao.selectManpowerService(requestId);
					temp.clear();
					tempF.clear();
					
					for (Iterator i = manpowCol.iterator();i.hasNext();){
						ManpowerService mp = (ManpowerService)i.next();
						
						Collection colCatF = dao.selectRateCardCategoryByRCId(mp.getCompetencyId());
						Collection colCatM = dao.selectRateCardManpowerByRCId(mp.getCompetencyId());
						
						temp 	= getFacilityBooked(mp.getRequestId(), colCatF, mp.getRequiredFrom(), mp.getRequiredTo(), mp.getFromTime(), mp.getToTime(), "F", mp.getQuantity());
						tempF 	= getFacilityBooked(mp.getRequestId(), colCatM, mp.getRequiredFrom(), mp.getRequiredTo(), mp.getFromTime(), mp.getToTime(), "M", mp.getQuantity());
						
						mp.setAdditionalInfo(getAdditionalInfo(temp, tempF));
						
						if ((temp.size() > 0) || (tempF.size() > 0)){
							facilities.add(mp);
						}
					}
				} else if (service.equals("STUDIO")){
					studioCol	= dao.selectStudioService(requestId);
					temp.clear();
					tempF.clear();
					
					for (Iterator i =studioCol.iterator();i.hasNext();){
						StudioService studio = (StudioService)i.next();
						
						Collection colCatF = dao.selectRateCardCategoryByRCId(studio.getFacilityId());
						Collection colCatM = dao.selectRateCardManpowerByRCId(studio.getFacilityId());
						
						temp 	= getFacilityBooked(studio.getRequestId(), colCatF, studio.getBookingDate(), studio.getBookingDateTo(), studio.getRequiredFrom(), studio.getRequiredTo(), "F");
						tempF 	= getFacilityBooked(studio.getRequestId(), colCatM, studio.getBookingDate(), studio.getBookingDateTo(), studio.getRequiredFrom(), studio.getRequiredTo(), "M");
						
						studio.setAdditionalInfo(getAdditionalInfo(temp, tempF));
						
						if ((temp.size() > 0) || (tempF.size() > 0)){
							facilities.add(studio);
						}
					}
				} else if (service.equals("OTHER")){
					otherCol	= dao.selectOtherService(requestId);
					temp.clear();
					tempF.clear();
					
					for (Iterator i = otherCol.iterator();i.hasNext();){
						OtherService other = (OtherService)i.next();
						
						Collection colCatF = dao.selectRateCardCategoryByRCId(other.getFacilityId());
						Collection colCatM = dao.selectRateCardManpowerByRCId(other.getFacilityId());
						
						temp 	= getFacilityBooked(other.getRequestId(), colCatF, other.getRequiredFrom(), other.getRequiredTo(), other.getFromTime(), other.getToTime(), "F", other.getQuantity());
						tempF 	= getFacilityBooked(other.getRequestId(), colCatM, other.getRequiredFrom(), other.getRequiredTo(), other.getFromTime(), other.getToTime(), "M", other.getQuantity());						
						
						other.setAdditionalInfo(getAdditionalInfo(temp, tempF));
						
						if ((temp.size() > 0) || (tempF.size() > 0)){
							facilities.add(other);
						}
					}
				} else if (service.equals("TVRO")){
					tvroCol		= dao.selectTvroService(requestId);
					String rateCardId = "";
					
					if (tvroCol.size() > 0) {
						RateCard rc = dao.selectRateCardByService("7");	// '7' is serviceTypeId which is "TVRO/Downlink"
						rateCardId = rc.getId();
						
						temp.clear();
						tempF.clear();
						
						for (Iterator i = tvroCol.iterator();i.hasNext();){
							TvroService tvro = (TvroService)i.next();
							
							Collection colCatF = dao.selectRateCardCategoryByRCId(rateCardId);
							Collection colCatM = dao.selectRateCardManpowerByRCId(rateCardId);
							
							temp 	= getFacilityBooked(tvro.getRequestId(), colCatF, tvro.getRequiredDate(), tvro.getRequiredDateTo(), tvro.getFromTime(), tvro.getToTime(), "F");
							tempF 	= getFacilityBooked(tvro.getRequestId(), colCatM, tvro.getRequiredDate(), tvro.getRequiredDateTo(), tvro.getFromTime(), tvro.getToTime(), "M");
							
							tvro.setAdditionalInfo(getAdditionalInfo(temp, tempF));
							
							if ((temp.size() > 0) || (tempF.size() > 0)){
								facilities.add(tvro);
							}
						}
					}
				} else if (service.equals("VTR")) {
					vtrCol = dao.selectVtrServiceMod(requestId);
					temp.clear();
					tempF.clear();
					
					if (vtrCol.size() > 0) {
						for (Iterator i =vtrCol.iterator();i.hasNext();){
							VtrService vtr = (VtrService)i.next();
							
							Collection colCatF = dao.selectRateCardCategoryByRCId(vtr.getFacilityId());
							Collection colCatM = dao.selectRateCardManpowerByRCId(vtr.getFacilityId());
														
							temp 	= getFacilityBooked(vtr.getRequestId(), colCatF, vtr.getRequiredDate(), vtr.getRequiredDateTo(), vtr.getRequiredFrom(), vtr.getRequiredTo(), "F");
							tempF 	= getFacilityBooked(vtr.getRequestId(), colCatM, vtr.getRequiredDate(), vtr.getRequiredDateTo(), vtr.getRequiredFrom(), vtr.getRequiredTo(), "M");
							
							vtr.setAdditionalInfo(getAdditionalInfo(temp, tempF));
							
							if ((temp.size() > 0) || (tempF.size() > 0)){
								facilities.add(vtr);
							}
						}
					}
				} 
			}			
			//facilities.add(postCol);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		return facilities;
	}
	
	public String getAdditionalInfo(Collection tempColFacility, Collection tempColManpower) throws DaoException{
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String header = "<div style='margin-left:-20px'><ul>";
		String requestTitle = "";
		
		if (tempColFacility!=null && tempColFacility.size() > 0){
//			Collection clf = new ArrayList();
//			for (Iterator i = tempColFacility.iterator(); i.hasNext();){
//				String is = (String)i.next(); 
//				
//				clf.add(is.split("~")[1]);
//				
//			}
//			HashSet clfSet = new HashSet(clf);
			
			HashSet set = new HashSet(tempColFacility);
			Iterator it = set.iterator();
			while (it.hasNext()){
				String its = (String)it.next();
				FacilityObject fo = dao.selectFacilityBookingById(its.split("~")[0]);
				
//				Iterator itClf = clfSet.iterator();
//				while (itClf.hasNext()){
//					String itClfs = (String)itClf.next();
//					
//					
//				}
				requestTitle += "<li>";
				requestTitle += fo.getRequestTitle() + " - " + fo.getCategory_name();
				requestTitle += "<br />";
				if (fo.getBookFrom() == fo.getBookTo()){
					requestTitle += sdf.format(fo.getBookFrom());
				} else {
					requestTitle += sdf.format(fo.getBookFrom()) + " - " + sdf.format(fo.getBookTo());
				}
				requestTitle += "<br />";
				requestTitle += fo.getDisplayTimeFromBooked() + " - " + fo.getDisplayTimeToBooked();
				requestTitle += "</li>";
			}
		}
		
		HashSet setx = new HashSet(tempColManpower);
		Iterator itx = setx.iterator();
		while (itx.hasNext()){
			String its = (String)itx.next();
			FacilityObject fo = dao.selectManpowerBookingById(its.split("~")[0]);
			
			if(fo != null){
				requestTitle += "<li>";
				requestTitle += fo.getRequestTitle();
				if(fo.getManpowerName()!=null) requestTitle += " - " + fo.getManpowerName();
				requestTitle += "<br />";
				if (fo.getBookFrom() == fo.getBookTo()){
					requestTitle += sdf.format(fo.getBookFrom());
				} else {
					requestTitle += sdf.format(fo.getBookFrom()) + " - " + sdf.format(fo.getBookTo());
				}
				requestTitle += "<br />";
				requestTitle += fo.getDisplayTimeFromBooked() + " - " + fo.getDisplayTimeToBooked();
				requestTitle += "</li>";
			}
		}
		
		requestTitle += "</ul></div>";
		
		return header + requestTitle;
	}
	
	public boolean checkAvailability(String requestId) {		
		boolean check = false;
		
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();

		Collection scpCol 		= new ArrayList();
		Collection postCol 		= new ArrayList();
		Collection manpowCol	= new ArrayList();
		Collection studioCol	= new ArrayList();
		Collection otherCol		= new ArrayList();
		Collection tvroCol		= new ArrayList();
		Collection vtrCol 		= new ArrayList();
		
		try {
			scpCol 		= dao.selectScpService(requestId);
			postCol		= dao.selectPostProductionService(requestId);
			manpowCol	= dao.selectManpowerService(requestId);
			studioCol	= dao.selectStudioService(requestId);
			otherCol 	= dao.selectOtherService(requestId);
			tvroCol		= dao.selectTvroService(requestId);
			vtrCol		= dao.selectVtrService(requestId);
			
			check = checkSCP(scpCol) || checkPOST(postCol) || checkManpower(manpowCol) || checkStudio(studioCol) || 
				checkOther(otherCol) || checkTvro(tvroCol) || checkVtr(vtrCol);			

		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		return check;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean checkTvro(Collection tvroCol) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		boolean isTvroBooked = false;
		String rateCardId = "";
		
		if (tvroCol.size() >0) {
			RateCard rc = dao.selectRateCardByService("7");	// '7' is serviceTypeId which is "TVRO/Downlink"
			rateCardId = rc.getId();
			
			Collection tempF = new ArrayList();
			Collection tempM = new ArrayList();
			
			for (Iterator i = tvroCol.iterator();i.hasNext();){
				TvroService tvro = (TvroService)i.next();
													
				Collection colCatF = dao.selectRateCardCategoryByRCId(rateCardId);
				Collection colCatM = dao.selectRateCardManpowerByRCId(rateCardId);
				
				tempF 	= getFacilityBooked(tvro.getRequestId(), colCatF, tvro.getRequiredDate(), tvro.getRequiredDateTo(), tvro.getFromTime(), tvro.getToTime(), "F");
				tempM 	= getFacilityBooked(tvro.getRequestId(),colCatM, tvro.getRequiredDate(), tvro.getRequiredDateTo(), tvro.getFromTime(), tvro.getToTime(), "M");
				
				if ((tempF.size() > 0) || (tempM.size() > 0)){
					return true;
				}
			}
		}
		
		return isTvroBooked;
	}	
	
	public boolean checkVtr(Collection vtrCol) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		boolean isVtrbooked = false;
		
		Collection tempF = new ArrayList();
		Collection tempM = new ArrayList();
		
		if (vtrCol.size() >0) {
			for (Iterator i = vtrCol.iterator();i.hasNext();){
				VtrService vtr = (VtrService)i.next();
													
				Collection colCatF = dao.selectRateCardCategoryByRCId(vtr.getFacilityId());
				Collection colCatM = dao.selectRateCardManpowerByRCId(vtr.getFacilityId());
				
				tempF 	= getFacilityBooked(vtr.getRequestId(), colCatF, vtr.getRequiredDate(), vtr.getRequiredDateTo(), vtr.getRequiredFrom(), vtr.getRequiredTo(), "F");
				tempM 	= getFacilityBooked(vtr.getRequestId(), colCatM, vtr.getRequiredDate(), vtr.getRequiredDateTo(), vtr.getRequiredFrom(), vtr.getRequiredTo(), "M");
				
				if ((tempF.size() > 0) || (tempM.size() > 0)){
					return true;
				}
			}
		}
		
		return isVtrbooked;
	}	
	
	public boolean checkOther(Collection otherCol) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		boolean isOtherbooked = false;
		
		Collection tempF = new ArrayList();
		Collection tempM = new ArrayList();
		
		if (otherCol.size() >0) {
			for (Iterator i = otherCol.iterator();i.hasNext();){
				OtherService oth = (OtherService)i.next();
													
				Collection colCatF = dao.selectRateCardCategoryByRCId(oth.getFacilityId());
				Collection colCatM = dao.selectRateCardManpowerByRCId(oth.getFacilityId());
				
				tempF 	= getFacilityBooked(oth.getRequestId(), colCatF, oth.getRequiredFrom(), oth.getRequiredTo(), oth.getFromTime(), oth.getToTime(), "F", oth.getQuantity());
				tempM 	= getFacilityBooked(oth.getRequestId(), colCatM, oth.getRequiredFrom(), oth.getRequiredTo(), oth.getFromTime(), oth.getToTime(), "M", oth.getQuantity());
				
				if ((tempF.size() > 0) || (tempM.size() > 0)){
					return true;
				}
			}
		}
		
		return isOtherbooked;
	}	
	
	public boolean checkManpower(Collection manpowCol) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		boolean isManBooked = false;
		
		Collection tempF = new ArrayList();
		Collection tempM = new ArrayList();
		
		if (manpowCol.size() >0) {
			for (Iterator i = manpowCol.iterator();i.hasNext();){
				ManpowerService mp = (ManpowerService)i.next();
													
				Collection colCatF = dao.selectRateCardCategoryByRCId(mp.getCompetencyId());
				Collection colCatM = dao.selectRateCardManpowerByRCId(mp.getCompetencyId());
				
				tempF 	= getFacilityBooked(mp.getRequestId(), colCatF, mp.getRequiredFrom(), mp.getRequiredTo(), mp.getFromTime(), mp.getToTime(), "F", mp.getQuantity());
				tempM 	= getFacilityBooked(mp.getRequestId(), colCatM, mp.getRequiredFrom(), mp.getRequiredTo(), mp.getFromTime(), mp.getToTime(), "M", mp.getQuantity());
				
				if ((tempF.size() > 0) || (tempM.size() > 0)){
					return true;
				}
			}
		}
		
		return isManBooked;
	}
	
	public boolean checkStudio(Collection studioCol) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		boolean isStudiobooked = false;
		
		Collection tempF = new ArrayList();
		Collection tempM = new ArrayList();
		
		if (studioCol.size() >0) {
			for (Iterator i = studioCol.iterator();i.hasNext();){
				StudioService std = (StudioService)i.next();
													
				Collection colCatF = dao.selectRateCardCategoryByRCId(std.getFacilityId());
				Collection colCatM = dao.selectRateCardManpowerByRCId(std.getFacilityId());
				
				tempF 	= getFacilityBooked(std.getRequestId(), colCatF, std.getBookingDate(), std.getBookingDateTo(), std.getRequiredFrom(), std.getRequiredTo(), "F");
				tempM 	= getFacilityBooked(std.getRequestId(), colCatM, std.getBookingDate(), std.getBookingDateTo(), std.getRequiredFrom(), std.getRequiredTo(), "M");
				
				if ((tempF.size() > 0) || (tempM.size() > 0)){
					return true;
				}
			}
		}
		
		return isStudiobooked;
	}
	
	public boolean checkSCP(Collection scpCol) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		boolean isSCPbooked = false;
		
		Collection tempF = new ArrayList();
		Collection tempM = new ArrayList();
		
		if (scpCol.size() >0) {
			for (Iterator iScp = scpCol.iterator();iScp.hasNext();){
				ScpService scp = (ScpService)iScp.next();
													
				Collection colCatF = dao.selectRateCardCategoryByRCId(scp.getFacilityId());
				Collection colCatM = dao.selectRateCardManpowerByRCId(scp.getFacilityId());
				
				tempF 	= getFacilityBooked(scp.getRequestId(), colCatF, scp.getRequiredFrom(), scp.getRequiredTo(), scp.getDepartureTime(), scp.getRecordingTo(), "F");
				tempM 	= getFacilityBooked(scp.getRequestId(), colCatM, scp.getRequiredFrom(), scp.getRequiredTo(), scp.getDepartureTime(), scp.getRecordingTo(), "M");
				
				if ((tempF.size() > 0) || (tempM.size() > 0)){
					return true;
				}
			}
		}
		
		return isSCPbooked;
	}	
	
	public boolean checkPOST(Collection postCol) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		boolean isSCPbooked = false;
		
		Collection tempF = new ArrayList();
		Collection tempM = new ArrayList();
		
		if (postCol.size() >0) {
			for (Iterator i = postCol.iterator();i.hasNext();){
				PostProductionService post = (PostProductionService)i.next();
													
				Collection colCatF = dao.selectRateCardCategoryByRCId(post.getFacilityId());
				Collection colCatM = dao.selectRateCardManpowerByRCId(post.getFacilityId());
				
				tempF 	= getFacilityBooked(post.getRequestId(), colCatF, post.getRequiredDate(), post.getRequiredDateTo(), post.getFromTime(), post.getToTime(), "F");
				tempM 	= getFacilityBooked(post.getRequestId(), colCatM, post.getRequiredDate(), post.getRequiredDateTo(), post.getFromTime(), post.getToTime(), "M");
				
				if ((tempF.size() > 0) || (tempM.size() > 0)){
					return true;
				}
			}
		}
		
		return isSCPbooked;
	}	
	
	public Collection getFacilityBooked(String requestId, Collection colCat, Date dateCheckedFrom, Date dateCheckedTo, String fromTime, String fromTo, String bookType) throws DaoException {
		return getFacilityBooked(requestId, colCat, dateCheckedFrom, dateCheckedTo, fromTime, fromTo, bookType, 1);
	}
	
	public Collection getFacilityBooked(String requestId, Collection colCat, Date dateCheckedFrom, Date dateCheckedTo, String fromTime, String fromTo, String bookType, int quantity) throws DaoException {
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();	
		SetupDao sDao = (SetupDao) Application.getInstance().getModule(SetupModule.class).getDao();
		Collection col= new ArrayList();
		
		long dDiff = dateDiff(dateCheckedFrom, dateCheckedTo);
		String timeFrom 	= splitTime(fromTime, ":");
		String timeTo 		= splitTime(fromTo, ":");
		int qty = 0;
		if (quantity > 0) {
			qty = quantity;
		}
		
		if (colCat == null || colCat.size() <= 0) {
			return col;
		}
		
		for (int x = 0; x<= dDiff; x++){
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateCheckedFrom);
			cal.add(Calendar.DATE, x);
			Date dateChecked = cal.getTime();
			
			for (Iterator iCat = colCat.iterator(); iCat.hasNext();){
				RateCard rc = (RateCard)iCat.next();
											
				Collection booked = new ArrayList();
				
				if (bookType.equals("F")){
					booked = dao.selectFacility(requestId, dateChecked, timeFrom, timeTo, bookType, rc.getIdCategory());
					
					int totalAvailable = sDao.selectRateCardSumQty(rc.getIdCategory());
					int quantityBooked = 0;
					if (booked != null){
						if (booked.size() > 0){
							for (Iterator ib = booked.iterator();ib.hasNext();){
								FacilityObject fo = (FacilityObject)ib.next();
								
								quantityBooked += fo.getQuantityBooked();
							}
						}
					}
					
					totalAvailable = totalAvailable - quantityBooked;
					if (totalAvailable > 0 ) {
						booked = null;
					}
					
				} else {
					booked = dao.selectAvailabilityManpowerWithBookingId(requestId, dateChecked, timeFrom, timeTo, bookType, rc.getIdCategory());
					
					int quantityBooked = 0;
					if (booked != null){
						if (booked.size() > 0){
							for (Iterator ib = booked.iterator();ib.hasNext();){
								FacilityObject fo = (FacilityObject)ib.next();
								
								quantityBooked = fo.getQuantityBooked();
							}
						}
					}
					
					int totalAvailable = 0;
					boolean isInNormalWp = dao.isInNormalWorkingProfile(timeFrom.substring(0, 2) + ":" + timeFrom.substring(2), timeTo.substring(0, 2) + ":" + timeTo.substring(2));
					int availableManpower = dao.countManpowerTotalInPool(rc.getIdCategory());
					int availableManpowerByWP = countManpowerAvailable(dateChecked, timeFrom.substring(0, 2) + ":" + timeFrom.substring(2), timeTo.substring(0, 2) + ":" + timeTo.substring(2), rc.getIdCategory());
					int countMpRest = countManpowerRest(dateChecked, timeFrom.substring(0, 2) + ":" + timeFrom.substring(2), timeTo.substring(0, 2) + ":" + timeTo.substring(2), rc.getIdCategory());
					
					if (isInNormalWp) {
						totalAvailable = availableManpower - countMpRest - quantityBooked- qty ;
					} else {
						totalAvailable = availableManpowerByWP - quantityBooked - qty;
					}
					
					if (totalAvailable > 0){
						booked = null;
					} 
				}
				
				if (booked != null){
					if (booked.size() > 0){
						for (Iterator ib = booked.iterator();ib.hasNext();){
							FacilityObject fo = (FacilityObject)ib.next();
							
							col.add(fo.getBookingId()+"~"+fo.getRequestId());
						}
	
					}
				}
			}				
		}
		return col;
	}
	
	public boolean checkAvailabilityFacility(Date dateChecked, String timeFrom, String timeTo, String type, String idCategory) throws DaoException{
		boolean check = false;
		Collection colBookedSCP = new ArrayList();
		CheckAvailabilityDao dao = (CheckAvailabilityDao) getDao();
		
		if (type.equals("F")){
			colBookedSCP = dao.selectFacility("", dateChecked, timeFrom, timeTo, type, idCategory);
		} else {
			colBookedSCP = dao.selectAvailabilityManpower(dateChecked, timeFrom, timeTo, type, idCategory);
		}
		
		if (colBookedSCP.size() > 0) {			
			check = true;
		}
		
		return check;
	}	
	
	public long dateDiff(Date start, Date end){
		
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));
		
		return diff;
	}
	
	public String splitTime(String time, String separator){
		return time.split(separator)[0] + time.split(separator)[1];
	}
}
