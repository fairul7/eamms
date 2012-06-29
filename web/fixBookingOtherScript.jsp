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
	
	Collection colOfOther = mod.getBlockBookingServices("other");
	
	for(Iterator iterate = colOfOther.iterator(); iterate.hasNext(); ){
		OtherService other=(OtherService)iterate.next();
		
		Collection colOfNonBlockBooking = mod.getNonBlockBookingInParticularRequest(other.getRequestId(),other.getFacilityId(), "other");
		
		Collection facilities = module.getRateCardEquipment(other.getFacilityId(), "");
		if(facilities!=null && facilities.size()>0){
			RateCard rateCard = (RateCard) facilities.iterator().next();
			if(mod.countDeletedFacilityBooking(other.getRequestId(), rateCard.getCategoryId(), other.getRequiredFrom(), other.getRequiredTo())>0){
				mod.removeFacilityBooking(other.getRequestId(), rateCard.getCategoryId(), other.getRequiredFrom(), other.getRequiredTo());
				System.out.println("Deleted booking Other with requestId: "+other.getRequestId());
				
				if(other!=null && other.getBlockBooking().equals("1")){
					int interval = other.getRequiredTo().getDate() - other.getRequiredFrom().getDate();
					for (int i = 0; i<=interval; i++){
						Date book = (Date) other.getRequiredFrom().clone();
						book.setDate(other.getRequiredFrom().getDate()+i);
						Collection colOfAssignments = mod.getAssignment(other.getRequestId(), other.getId(), book, book, "equipment");
						if(colOfAssignments!=null && colOfAssignments.size()>0){
							mod.bookRateCardFacility(other.getFacilityId(),other.getRequestId(),book, book,other.getFromTime(),other.getToTime(),other.getQuantity(), other.getBlockBooking());
						}
					}
				}
				if(colOfNonBlockBooking!=null && colOfNonBlockBooking.size()>0){
					System.out.println("Build new booking Other for non block booking with requestId:"+other.getRequestId());
					for(Iterator iter = colOfNonBlockBooking.iterator(); iter.hasNext(); ){
						OtherService otherNonBB=(OtherService)iter.next();
						Collection colOfAssignmentsNonBB = mod.getAssignment(otherNonBB.getRequestId(), otherNonBB.getId(), otherNonBB.getRequiredFrom(), otherNonBB.getRequiredTo(), "equipment");
						if(colOfAssignmentsNonBB!=null && colOfAssignmentsNonBB.size()>0){
							mod.bookRateCardFacility(otherNonBB.getFacilityId(),otherNonBB.getRequestId(),otherNonBB.getRequiredFrom(),otherNonBB.getRequiredTo(),otherNonBB.getFromTime(),otherNonBB.getToTime(),otherNonBB.getQuantity(), otherNonBB.getBlockBooking());
						}
					}
				}
			}
		}
	}
	Log.getLog(getClass()).info("Others Script Run Successfully");
%>

Script Run Successfully