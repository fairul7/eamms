<%@page import="kacang.Application, 
				kacang.model.DefaultDataObject,
				com.tms.fms.facility.model.*,
				com.tms.fms.engineering.model.*,
				java.util.Collection, 
				java.util.Iterator, 
				java.util.ArrayList,
				kacang.util.Log,
				java.util.Date "
%>

<%
	Application app = Application.getInstance();
	EngineeringModule mod = (EngineeringModule) app.getModule(EngineeringModule.class);
	SetupModule module = (SetupModule) app.getModule(SetupModule.class);
	
	Collection colOfStudio = mod.getBlockBookingServices("studio");
	
	for(Iterator iterate = colOfStudio.iterator(); iterate.hasNext(); ){
		StudioService studio=(StudioService)iterate.next();
		
		Collection colOfNonBlockBooking = mod.getNonBlockBookingInParticularRequest(studio.getRequestId(),studio.getFacilityId(), "studio");
		
		Collection facilities = module.getRateCardEquipment(studio.getFacilityId(), "");
		if(facilities!=null && facilities.size()>0){
			RateCard rateCard = (RateCard) facilities.iterator().next();
			if(mod.countDeletedFacilityBooking(studio.getRequestId(), rateCard.getCategoryId(), studio.getBookingDate(), studio.getBookingDateTo())>0){
				mod.removeFacilityBooking(studio.getRequestId(), rateCard.getCategoryId(), studio.getBookingDate(), studio.getBookingDateTo());
				System.out.println("Deleted booking Studio with requestId: "+studio.getRequestId());
				
				if(studio!=null && studio.getBlockBooking().equals("1")){
					int interval = studio.getBookingDate().getDate() - studio.getBookingDateTo().getDate();
					for (int i = 0; i<=interval; i++){
						Date book = (Date) studio.getBookingDate().clone();
						book.setDate(studio.getBookingDateTo().getDate()+i);
						Collection colOfAssignments = mod.getAssignment(studio.getRequestId(), studio.getId(), book, book, "equipment");
						if(colOfAssignments!=null && colOfAssignments.size()>0){
							mod.bookRateCardFacility(studio.getFacilityId(),studio.getRequestId(),book, book,studio.getRequiredFrom(),studio.getRequiredTo(),1 , studio.getBlockBooking());
						}
					}
				}
				if(colOfNonBlockBooking!=null && colOfNonBlockBooking.size()>0){
					System.out.println("Build new booking Studio for non block booking with requestId:"+studio.getRequestId());
					for(Iterator iter = colOfNonBlockBooking.iterator(); iter.hasNext(); ){
						StudioService studioNonBB=(StudioService)iter.next();
						Collection colOfAssignmentsNonBB = mod.getAssignment(studioNonBB.getRequestId(), studioNonBB.getId(), studio.getBookingDate(), studio.getBookingDateTo(), "equipment");
						if(colOfAssignmentsNonBB!=null && colOfAssignmentsNonBB.size()>0){
							mod.bookRateCardFacility(studioNonBB.getFacilityId(),studioNonBB.getRequestId(),studioNonBB.getBookingDate(),studioNonBB.getBookingDateTo(),studioNonBB.getRequiredFrom(),studioNonBB.getRequiredTo(),1 , studioNonBB.getBlockBooking());
						}
					}
				}
			}
		}
	}
	Log.getLog(getClass()).info("Studio Script Run Successfully");
%>

Script Run Successfully