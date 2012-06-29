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
	
	Collection colOfVtr = mod.getBlockBookingServices("vtr");

	for(Iterator iterate = colOfVtr.iterator(); iterate.hasNext(); ){
		VtrService vtr = (VtrService) iterate.next();
		
		Collection colOfNonBlockBooking = mod.getNonBlockBookingInParticularRequest(vtr.getRequestId(),vtr.getFacilityId(), "vtr");
		
		Collection facilities = module.getRateCardEquipment( vtr.getFacilityId(), "");
		if(facilities!=null && facilities.size()>0){
			RateCard rateCard = (RateCard) facilities.iterator().next();
			if(mod.countDeletedFacilityBooking(vtr.getRequestId(),rateCard.getCategoryId(), vtr.getRequiredDate(), vtr.getRequiredDateTo())>0){
				mod.removeFacilityBooking(vtr.getRequestId(),rateCard.getCategoryId(), vtr.getRequiredDate(), vtr.getRequiredDateTo());
				System.out.println("Deleted booking VTR with requestId: "+vtr.getRequestId());
				if(vtr!=null && vtr.getBlockBooking().equals("1")){
					int interval = vtr.getRequiredDate().getDate() - vtr.getRequiredDateTo().getDate();
					for (int i = 0; i<=interval; i++){
						Date book = (Date) vtr.getRequiredDate().clone();
						book.setDate(vtr.getRequiredDateTo().getDate()+i);
						Collection colOfAssignments = mod.getAssignment(vtr.getRequestId(), vtr.getId(), book, book, "equipment");
						if(colOfAssignments!=null && colOfAssignments.size()>0){
							mod.bookRateCardFacility(vtr.getFacilityId(),vtr.getRequestId(), book, book,vtr.getRequiredFrom(),vtr.getRequiredTo(),1, vtr.getBlockBooking());
						}
					}
				}
				if(colOfNonBlockBooking!=null && colOfNonBlockBooking.size()>0){
					System.out.println("Build new booking VTR for non block booking with requestId:"+vtr.getRequestId());
					for(Iterator iter = colOfNonBlockBooking.iterator(); iter.hasNext(); ){
						VtrService vtrNonBB = (VtrService) iter.next();
						Collection colOfAssignmentsNonBB = mod.getAssignment(vtrNonBB.getRequestId(), vtrNonBB.getId(), vtrNonBB.getRequiredDate(),vtrNonBB.getRequiredDateTo(), "equipment");
						if(colOfAssignmentsNonBB!=null && colOfAssignmentsNonBB.size()>0){
							mod.bookRateCardFacility(vtrNonBB.getFacilityId(),vtrNonBB.getRequestId(), vtrNonBB.getRequiredDate(),vtrNonBB.getRequiredDateTo(),vtrNonBB.getRequiredFrom(),vtrNonBB.getRequiredTo(),1, vtrNonBB.getBlockBooking());
						}
					}
				}
			}
		}
	}
	Log.getLog(getClass()).info("VTR Studio Script Run Successfully");
%>

Script Run Successfully