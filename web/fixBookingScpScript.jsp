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
	
	/*1. Get collection of the block booking services*/
	Collection colOfScp = mod.getBlockBookingServices("scp");

	for(Iterator iterate = colOfScp.iterator(); iterate.hasNext(); ){
		ScpService scp = (ScpService) iterate.next();
		/*2. Check is it got the same request which is not block booking*/
		Collection colOfNonBlockBooking = mod.getNonBlockBookingInParticularRequest(scp.getRequestId(),scp.getFacilityId(), "scp");
		
		/*3. Delete the current data from fms_booking*/
		Collection facilities = module.getRateCardEquipment(scp.getFacilityId(), "");
		if(facilities!=null && facilities.size()>0){
			RateCard rateCard = (RateCard) facilities.iterator().next();
			if(mod.countDeletedFacilityBooking(scp.getRequestId(), rateCard.getCategoryId(), scp.getRequiredFrom(), scp.getRequiredTo())>0){
				mod.removeFacilityBooking(scp.getRequestId(), rateCard.getCategoryId(), scp.getRequiredFrom(), scp.getRequiredTo());
				System.out.println("Deleted booking SCP with requestId: "+scp.getRequestId());
				
				/*4. Insert new record to facility*/
				if(scp!=null && scp.getBlockBooking().equals("1")){
					int interval = scp.getRequiredTo().getDate() - scp.getRequiredFrom().getDate();
					for (int i = 0; i<=interval; i++){
						Date book = (Date) scp.getRequiredFrom().clone();
						book.setDate(scp.getRequiredFrom().getDate()+i);
						Collection colOfAssignments = mod.getAssignment(scp.getRequestId(), scp.getId(), book, book, "equipment");
						if(colOfAssignments!=null && colOfAssignments.size()>0){
							mod.bookRateCardFacility(scp.getFacilityId(),scp.getRequestId(),book,book,scp.getDepartureTime(),scp.getRecordingTo(),1, scp.getBlockBooking());
						}
					}
				}
				/*5. Insert new record to facility which is not block booking but same requestId, facilityId and date*/
				if(colOfNonBlockBooking!=null && colOfNonBlockBooking.size()>0){
					System.out.println("Build new booking SCP for non block booking with requestId:"+scp.getRequestId());
					for(Iterator iter = colOfNonBlockBooking.iterator(); iter.hasNext(); ){
						ScpService scpNonBB = (ScpService) iter.next();
						Collection colOfAssignmentsNonBB = mod.getAssignment(scpNonBB.getRequestId(), scpNonBB.getId(), scpNonBB.getRequiredFrom(), scpNonBB.getRequiredTo(), "equipment");
						if(colOfAssignmentsNonBB!=null && colOfAssignmentsNonBB.size()>0){
							mod.bookRateCardFacility(scpNonBB.getFacilityId(),scpNonBB.getRequestId(),scpNonBB.getRequiredFrom(),scpNonBB.getRequiredTo(),scpNonBB.getDepartureTime(),scpNonBB.getRecordingTo(),1, scpNonBB.getBlockBooking());
						}
					}
				}
			}
		}
	}
	Log.getLog(getClass()).info("SCP Script Run Successfully");
%>

Script Run Successfully