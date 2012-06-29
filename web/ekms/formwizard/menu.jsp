<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery,
                 com.tms.collab.formwizard.model.*,
                 com.tms.collab.formwizard.model.FormConstants"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determining permissions
    boolean canAddForm = service.hasPermission(userId, FormConstants.FORM_ADD_PERMISSION_ID, "com.tms.collab.formwizard.model.FormModule", null);
    boolean canEditForm = service.hasPermission(userId, FormConstants.FORM_EDIT_PERMISSION_ID, "com.tms.collab.formwizard.model.FormModule", null);
    FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
    FormDao dao =(FormDao) handler.getDao();
    DaoQuery daoQuery= new DaoQuery();
    int count=dao.getPendingForms(daoQuery,userId);
    //Generating form wizard menu
    items.add(new MenuItem(app.getMessage("formWizard.label.formWizard"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("formWizard.label.formsListing"), "/ekms/formwizard/frwFormsView.jsp", null, null, null, null));
    if (canAddForm) {
        items.add(new MenuItem(app.getMessage("formWizard.label.newForm"), "/ekms/formwizard/frwAddForm.jsp", null, null, null, null));
    }
    if (canEditForm) {
        items.add(new MenuItem(app.getMessage("formWizard.label.editdelete"), "/ekms/formwizard/frwFormsEdit.jsp", null, null, null, null));
    }
    items.add(new MenuItem(app.getMessage("formWizard.label.queryformdata"), "/ekms/formwizard/frwFormsQuery.jsp", null, null, null, null));

    //Generating personal menu
    items.add(new MenuItem(app.getMessage("formWizard.label.personal"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("formWizard.label.draftSubmission"), "/ekms/formwizard/frwFormsDraft.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("formWizard.label.submittedFromHistory"), "/ekms/formwizard/frwSubmittedFormHistory.jsp", null, null, null, null));

    //Generating workflow menu
    items.add(new MenuItem(app.getMessage("formWizard.label.workflow"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("formWizard.label.submitformstatus"), "/ekms/formwizard/frwStatusReport.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("formWizard.label.submitformapproval")+" ("+count+")", "/ekms/formwizard/frwApproveFormData.jsp", null, null, null, null));

    //Generating form template menu
    if (canAddForm) {
        items.add(new MenuItem(app.getMessage("formWizard.label.formTemplate"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("formWizard.label.newFormTemplate"), "/ekms/formwizard/frwAddFormTemplate.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("formWizard.label.editDeleteFormTemplate"), "/ekms/formwizard/frwTemplatesEdit.jsp", null, null, null, null));
    }

    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>