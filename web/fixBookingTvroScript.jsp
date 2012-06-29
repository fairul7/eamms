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
	
	Collection colOfTvro = mod.getBlockBookingServices("tvro");	
	
	for(Iterator iterate = colOfTvro.iterator(); iterate.hasNext(); ){
		TvroService tvro = (TvroService) iterate.next();
		
		Collection colOfNonBlockBooking = mod.getNonBlockBookingInParticularRequest(tvro.getRequestId(),tvro.getFacilityId(), "tvro");
		
		Collection facilities = module.getRateCardEquipment(tvro.getFacilityId(), "");
		if(facilities!=null && facilities.size()>0){
			RateCard rateCard = (RateCard) facilities.iterator().next();
			if(mod.countDeletedFacilityBooking(tvro.getRequestId(), rateCard.getCategoryId(), tvro.getRequiredDate(), tvro.getRequiredDateTo())>0){
				mod.removeFacilityBooking(tvro.getRequestId(), rateCard.getCategoryId(), tvro.getRequiredDate(), tvro.getRequiredDateTo());
				System.out.println("Deleted booking TVRO with requestId: "+tvro.getRequestId());
				if(tvro!=null && tvro.getBlockBooking().equals("1")){
					int interval = tvro.getRequiredDate().getDate() - tvro.getRequiredDateTo().getDate();
					for (int i = 0; i<=interval; i++){
						Date book = (Date) tvro.getRequiredDate().clone();
						book.setDate(tvro.getRequiredDateTo().getDate()+i);
						Collection colOfAssignments = mod.getAssignment(tvro.getRequestId(), tvro.getId(), book, book, "equipment");
						if(colOfAssignments!=null && colOfAssignments.size()>0){
							mod.bookRateCardFacility(tvro.getFacilityId(),tvro.getRequestId(),book, book,tvro.getFromTime(),tvro.getToTime(), 1, tvro.getBlockBooking());
						}
					}
				}
				
				if(colOfNonBlockBooking!=null && colOfNonBlockBooking.size()>0){
					System.out.println("Build new booking TVRO for non block booking with requestId:"+tvro.getRequestId());
					for(Iterator iter = colOfNonBlockBooking.iterator(); iter.hasNext(); ){
						TvroService tvroNonBB = (TvroService) iter.next();
						Collection colOfAssignmentsNonBB = mod.getAssignment(tvroNonBB.getRequestId(), tvroNonBB.getId(), tvroNonBB.getRequiredDate(), tvroNonBB.getRequiredDateTo(), "equipment");
						if(colOfAssignmentsNonBB!=null && colOfAssignmentsNonBB.size()>0){
							mod.bookRateCardFacility(tvroNonBB.getFacilityId(),tvroNonBB.getRequestId(),tvroNonBB.getRequiredDate(), tvroNonBB.getRequiredDateTo(),tvroNonBB.getFromTime(),tvroNonBB.getToTime(), 1, tvroNonBB.getBlockBooking());
						}
					}
				}
			}
		}
	}
	Log.getLog(getClass()).info("TVRO Studio Script Run Successfully");
%>

Script Run Successfully