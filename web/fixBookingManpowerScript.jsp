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
	
	Collection colOfManpower = mod.getBlockBookingServices("manpower");
	
	
	for(Iterator iterate = colOfManpower.iterator(); iterate.hasNext(); ){
		ManpowerService man=(ManpowerService) iterate.next();
		Collection colOfNonBlockBooking = mod.getNonBlockBookingInParticularRequest(man.getRequestId(),man.getCompetencyId(), "manpower");
		
		Collection manpower = module.getRateCardManpower(man.getCompetencyId(), "");
		if(manpower!=null && manpower.size()>0){
			RateCard rateCard =(RateCard) manpower.iterator().next();
			if(mod.countDeletedFacilityBooking(man.getRequestId(), rateCard.getCompetencyId(), man.getRequiredFrom(), man.getRequiredTo())>0){
				mod.removeFacilityBooking(man.getRequestId(), rateCard.getCompetencyId(), man.getRequiredFrom(), man.getRequiredTo());
				//System.out.println("Deleted booking Manpower with requestId: "+man.getRequestId());
				Log.getLog(getClass()).info("Deleted booking Manpower with requestId: "+man.getRequestId());
				
				if(man!=null && man.getBlockBooking().equals("1")){
					int interval = man.getRequiredTo().getDate() - man.getRequiredFrom().getDate();
					for (int i = 0; i<=interval; i++){
						Date book = (Date) man.getRequiredFrom().clone();
						book.setDate(man.getRequiredFrom().getDate()+i);
						Collection colOfAssignments = mod.getAssignment(man.getRequestId(), man.getId(), book, book, "manpower");
						if(colOfAssignments!=null && colOfAssignments.size()>0){
							mod.bookRateCardFacility(man.getCompetencyId(),man.getRequestId(),book,book,man.getFromTime(),man.getToTime(),man.getQuantity(), man.getBlockBooking());
						}
					}
				}
					
				if(colOfNonBlockBooking!=null && colOfNonBlockBooking.size()>0){
					System.out.println("Build new booking Manpower for non block booking with requestId:"+man.getRequestId());
					for(Iterator iter = colOfNonBlockBooking.iterator(); iter.hasNext(); ){
						ManpowerService manNonBB=(ManpowerService) iter.next();
						Collection colOfAssignmentsNonBB = mod.getAssignment(manNonBB.getRequestId(), manNonBB.getId(), manNonBB.getRequiredFrom(),manNonBB.getRequiredTo(), "manpower");
						if(colOfAssignmentsNonBB!=null && colOfAssignmentsNonBB.size()>0){
							mod.bookRateCardFacility(manNonBB.getCompetencyId(),manNonBB.getRequestId(),manNonBB.getRequiredFrom(),manNonBB.getRequiredTo(),manNonBB.getFromTime(),manNonBB.getToTime(),manNonBB.getQuantity(), manNonBB.getBlockBooking());
						}
					}
				}
			}
		}
	}
	Log.getLog(getClass()).info("Manpower Script Run Successfully");
%>

Script Run Successfully