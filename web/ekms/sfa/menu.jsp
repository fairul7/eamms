<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.crm.sales.misc.AccessUtil"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determining permissions of sales persons
    boolean salesPerson = AccessUtil.isSalesPerson(userId);
    boolean salesManager = AccessUtil.isSalesManager(userId);
    boolean salesAdmin = AccessUtil.isSalesAdmin(userId);
    boolean externalSales = AccessUtil.isExternalSalesPerson(userId);

    //Generating sfa menu
    items.add(new MenuItem(app.getMessage("sfa.message.salesForceAutomation"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("sfa.message.main"), "/ekms/sfa/main.jsp", null, null, null, null));

    if (salesPerson || salesManager || externalSales || salesAdmin) {

		//Generating opportunities menu
        items.add(new MenuItem(app.getMessage("sfa.message.opportunities"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.newOpportunity"), "/ekms/sfa/newopportunity_company_list.jsp", null, null, null, null));
        if (salesManager || salesAdmin) {
            items.add(new MenuItem(app.getMessage("sfa.message.viewAllOpportunites"), "/ekms/sfa/allopportunitytable.jsp", null, null, null, null));
        }
        items.add(new MenuItem(app.getMessage("sfa.message.viewMyOpportunities"), "/ekms/sfa/myopportunitytable.jsp", null, null, null, null));
        if (salesManager || salesAdmin) {
            items.add(new MenuItem(app.getMessage("sfa.message.viewAllSales"), "/ekms/sfa/view_report_individual.jsp?viewFrom=menu", null, null, null, null));
        }
        items.add(new MenuItem(app.getMessage("sfa.message.viewMySales"), "/ekms/sfa/view_my_sales.jsp", null, null, null, null));

        //Generating leads menu
        items.add(new MenuItem(app.getMessage("sfa.message.leads"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.newLead"), "/ekms/sfa/addLead.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.viewAllLeads"), "/ekms/sfa/leadTable.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.viewMyLeads"), "/ekms/sfa/leadTableOwn.jsp", null, null, null, null));

        //Generating companies menu
        items.add(new MenuItem(app.getMessage("sfa.message.menuCompanies"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.companiesListing"), "/ekms/sfa/companies_listing.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.newCompany"), "/ekms/sfa/newcompany.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.contactListing"), "/ekms/sfa/contact_listing.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("sfa.message.newContact"), "/ekms/sfa/newcontact_contact_list.jsp", null, null, null, null));

        if (salesManager || salesAdmin) {
            //Generating report menu
            items.add(new MenuItem(app.getMessage("sfa.message.report"), null, null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.opportuniyReport-ByCompany"), "/ekms/sfa/reportByCompanyForm.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.opportuniyReport-ByIndividuals"), "/ekms/sfa/reportByIndividualsForm.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.opportuniyReport-ByProduct"), "/ekms/sfa/reportByProductForm.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.completedSaleReportIndividual"), "/ekms/sfa/CompletedSalesReportIndividual.jsp", null, null, null, null));
			items.add(new MenuItem(app.getMessage("sfa.message.completedSalesReport"), "/ekms/sfa/reportCompletedSalesForm.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.lostOpportuniyReport"), "/ekms/sfa/reportLostSalesForm.jsp", null, null, null, null));
        }
        
        if (salesAdmin) {
        	 //Generating setup menu
            items.add(new MenuItem(app.getMessage("sfa.message.setup"), null, null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.contactType"), "/ekms/sfa/setup_contacttype.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.companyType"), "/ekms/sfa/setup_companytype.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.group"), "/ekms/sfa/setup_group.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.label.category"), "/ekms/sfa/setup_category.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.product"), "/ekms/sfa/setup_product.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.projection"), "/ekms/sfa/setup_projection.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.salutation"), "/ekms/sfa/setup_salutation.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("sfa.message.source"), "/ekms/sfa/setup_source.jsp", null, null, null, null));
			items.add(new MenuItem(app.getMessage("sfa.message.financialYearSetup"), "/ekms/sfa/financialYearSetup.jsp", null, null, null, null));
        }
    }
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>