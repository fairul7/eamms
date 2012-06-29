package com.tms.crm.sales.ui;

import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CompletedSalesReportIndividual extends Form {

	private ReportsUsersSelectBox accMrgs;
	private Button view;
	private TextField financialYear;
	private Collection userIdList;
	private String financialYearStr;


	public CompletedSalesReportIndividual()
	{}

	public CompletedSalesReportIndividual(String s)	{
		super(s);
	}

	public void initForm()	{

		// Initialization
		accMrgs = new ReportsUsersSelectBox("accMrgs");
		addChild(accMrgs);
		accMrgs.addChild(new ValidatorNotEmpty("vAccMrgs"));
		accMrgs.init();
		view = new Button("view");
		view.setText("View");
		addChild(view);
		financialYear = new TextField("financialYear");
		financialYear.setSize("6");
		financialYear.addChild(new ValidatorNotEmpty("vFinancialYear"));
		financialYear.addChild((new ValidatorIsNumeric("vnFinancialYear","Must be a number.")));
		addChild(financialYear);
		setMethod("POST");
	}

	public void init()	{
		initForm();
	}

	public void onRequest(Event event) {
		initForm();
	}

	public Forward onValidate(Event evt) {
			userIdList = new ArrayList();
			financialYearStr = "";
			financialYearStr = (String) financialYear.getValue();
			if (financialYearStr.equals("") || financialYearStr == null)	{
				return new Forward("empty");
			} else {
				userIdList.addAll(Arrays.asList(accMrgs.getIds()));
				if (userIdList.size() == 0 ) {
					return new Forward("emptyAccMrg");
				} else if (userIdList.size() > 0) {
					return new Forward("view");
				}
			}
		return super.onValidate(evt);
	}


	public String getDefaultTemplate() {
		return "sfa/salesReportIndividual";
	}

	public ReportsUsersSelectBox getAccMrgs() {
		return accMrgs;
	}

	public void setAccMrgs(ReportsUsersSelectBox accMrgs) {
		this.accMrgs = accMrgs;
	}

	public Button getView() {
		return view;
	}

	public void setView(Button view) {
		this.view = view;
	}

	public TextField getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(TextField financialYear) {
		this.financialYear = financialYear;
	}

	public Collection getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(Collection userIdList) {
		this.userIdList = userIdList;
	}

	public String getFinancialYearStr() {
		return financialYearStr;
	}

	public void setFinancialYearStr(String financialYearStr) {
		this.financialYearStr = financialYearStr;
	}

}
