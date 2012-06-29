<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Calendar,
                 com.tms.hr.leave.model.LeaveModule,
                 kacang.services.security.User,
                 com.tms.hr.claim.model.ClaimConfigDao,
                 com.tms.hr.claim.model.ClaimFormIndexModule,
                 com.tms.hr.claim.ui.ClaimConfigAssessor,
                 org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
    User user = service.getCurrentUser(request);
	String userId = user.getId();
	ArrayList items = new ArrayList();
    User currentUser = service.getCurrentUser(request);

	boolean bCanAdmin = false;
	if(service.hasPermission(currentUser.getId(),ClaimConfigDao.PERMISSION_CONFIG_ADMIN,null,null) )
	{
		bCanAdmin= true;
	}

	/// count the ClaimForms in each basket
	/// for owner
	ClaimFormIndexModule ciModule = (ClaimFormIndexModule) app.getModule(
                  ClaimFormIndexModule.class);
	String[] ownerCreated = { " ( userOwner='"+userId+"' OR userOriginator='"+userId+"')",
									" status='act' ", " state='cre' "};
	String[] ownerSubmitted = { " ( userOwner='"+userId+"' OR userOriginator='"+userId+"')",
									" status='act' ", " state='sub' "};
	String[] ownerApproved = { " ( userOwner='"+userId+"' OR userOriginator='"+userId+"')",
									" status='act' ", " state='app' "};
	String[] ownerProcessed = { " ( userOwner='"+userId+"' OR userOriginator='"+userId+"')",
									" status='act' ", " state='ass' "};
	String[] ownerRejected = { " ( userOwner='"+userId+"' OR userOriginator='"+userId+"')",
									" status='act' ", " state='rej' "};
	String[] ownerClosed = { " userOwner='"+userId+"' ",
									" status='act' ", " state='clo' "};
	String[] pendingApproval = { " ( userApprover1='"+userId+"' OR userApprover2='"+userId+"')", " status='act' ", " state='sub' ", " (approvedBy IS NOT NULL AND approvedBy NOT LIKE '%"+StringUtils.replace(user.getName(), "'", "\\'")+"%')"};

    String[] pendingProcess= { " status='act' ", " state='app' "};
	int nCreated = ciModule.countObjects(ownerCreated);
	int nSubmitted = ciModule.countObjects(ownerSubmitted);
	int nApproved = ciModule.countObjects(ownerApproved);
	int nProcessed = ciModule.countObjects(ownerProcessed);
	int nRejected = ciModule.countObjects(ownerRejected);
	int nClosed = ciModule.countObjects(ownerClosed);

    int nPendingApproval = ciModule.countObjects(pendingApproval);
	int nPendingProcess = 0;
	boolean bCanSeePending = ClaimConfigAssessor.isAssessor(userId,app);
	if(bCanSeePending)
	{ nPendingProcess = ciModule.countObjects(pendingProcess);}

    //Generating leave menu
    items.add(new MenuItem(app.getMessage("claims.menu.header"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.submitClaim"), "/ekms/claim/user_createClaim.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.assistants"), "/ekms/claim/user_options.jsp", null, null, null, null));

    items.add(new MenuItem(app.getMessage("claims.label.claimListing"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.draft") + ((nCreated>0)?"("+nCreated+")":""), "/ekms/claim/owner_list_draft.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.submitted") + ((nSubmitted>0)?"("+nSubmitted+")":""), "/ekms/claim/owner_list_submitted.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.approved") + ((nApproved>0)?"("+nApproved+")":""), "/ekms/claim/owner_list_approved.jsp", null, null, null, null));
    //items.add(new MenuItem(app.getMessage("claims.label.processed") + ((nProcessed>0)?"("+nProcessed+")":""), "/ekms/claim/owner_list_processed.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.rejected") + ((nRejected>0)?"("+nRejected+")":""), "/ekms/claim/owner_list_rejected.jsp", null, null, null, null));
    //items.add(new MenuItem(app.getMessage("claims.label.approved") + ((nApproved>0)?"("+nApproved+")":""), "/ekms/claim/owner_list_approved.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.closed") + ((nClosed>0)?"("+nClosed+")":""), "/ekms/claim/owner_list_closed.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("claims.label.summary"),"/ekms/claim/summary.jsp",null,null,null,null));
    if(nPendingApproval>0 || nPendingProcess>0) {
        items.add(new MenuItem(app.getMessage("claims.label.claimApproval"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.label.approve") + ((nPendingApproval>0)?"("+nPendingApproval+")":""), "/ekms/claim/approver_approve.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.label.process") + ((nPendingProcess>0)?"("+nPendingProcess+")":""), "/ekms/claim/approver_process.jsp", null, null, null, null));
    }

    if(bCanAdmin) {
        items.add(new MenuItem(app.getMessage("claims.label.claimAdmin"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.label.setup"), "/ekms/claim/config_setup.jsp", null, null, null, null));
        //items.add(new MenuItem(app.getMessage("claims.label.predefinedClaims"), "/ekms/claim/config_standard_type.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.label.projects"), "/ekms/claim/config_project.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.type.name"), "/ekms/claim/config_type.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.typedept.name"), "/ekms/claim/config_typedept.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.label.category"), "/ekms/claim/config_category.jsp", null, null, null, null));
        //items.add(new MenuItem(app.getMessage("claims.label.generateReport"), "/ekms/claim/generateReport.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("claims.label.generateReport"), "/ekms/claim/reportForm.jsp", null, null, null, null));
       

    }



    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
