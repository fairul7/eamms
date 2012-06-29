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
	
	Collection colOfPostproduction = mod.getBlockBookingServices("postproduction");
	
	for(Iterator iterate = colOfPostproduction.iterator(); iterate.hasNext(); ){
		PostProductionService postProduction = (PostProductionService) iterate.next();
		
		Collection colOfNonBlockBooking = mod.getNonBlockBookingInParticularRequest(postProduction.getRequestId(),postProduction.getFacilityId(), "postproduction");
		
		Collection facilities = module.getRateCardEquipment(postProduction.getFacilityId(), "");
		if(facilities!=null && facilities.size()>0){
			RateCard rateCard = (RateCard) facilities.iterator().next();
			if(mod.countDeletedFacilityBooking(postProduction.getRequestId(), rateCard.getCategoryId(), postProduction.getRequiredDate(), postProduction.getRequiredDateTo())>0){
				mod.removeFacilityBooking(postProduction.getRequestId(), rateCard.getCategoryId(), postProduction.getRequiredDate(), postProduction.getRequiredDateTo());
				System.out.println("Deleted booking PostProduction with requestId: "+postProduction.getRequestId());
				if(postProduction!=null && postProduction.getBlockBooking().equals("1")){
					int interval = postProduction.getRequiredDate().getDate() - postProduction.getRequiredDateTo().getDate();
					for (int i = 0; i<=interval; i++){
						Date book = (Date) postProduction.getRequiredDate().clone();
						book.setDate(postProduction.getRequiredDateTo().getDate()+i);
						Collection colOfAssignments = mod.getAssignment(postProduction.getRequestId(), postProduction.getId(), book, book, "equipment");
						if(colOfAssignments!=null && colOfAssignments.size()>0){
							mod.bookRateCardFacility(postProduction.getFacilityId(),postProduction.getRequestId(),book, book,postProduction.getFromTime(),postProduction.getToTime(),1, postProduction.getBlockBooking());
						}
					}
				}
				
				if(colOfNonBlockBooking!=null && colOfNonBlockBooking.size()>0){
					System.out.println("Build new booking PostProduction for non block booking with requestId:"+postProduction.getRequestId());
					for(Iterator iter = colOfNonBlockBooking.iterator(); iter.hasNext(); ){
						PostProductionService postProductionNonBB = (PostProductionService) iter.next();
						Collection colOfAssignmentsNonBB = mod.getAssignment(postProductionNonBB.getRequestId(), postProductionNonBB.getId(), postProductionNonBB.getRequiredDate(),postProductionNonBB.getRequiredDateTo(), "equipment");
						if(colOfAssignmentsNonBB!=null && colOfAssignmentsNonBB.size()>0){
							mod.bookRateCardFacility(postProductionNonBB.getFacilityId(),postProductionNonBB.getRequestId(),postProductionNonBB.getRequiredDate(),postProductionNonBB.getRequiredDateTo(),postProductionNonBB.getFromTime(),postProductionNonBB.getToTime(),1, postProductionNonBB.getBlockBooking());
						}
					}
				}
			}
		}
	}
	Log.getLog(getClass()).info("Post Script Run Successfully");
%>

Script Run Successfully