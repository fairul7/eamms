package com.tms.crm.sales.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.TextField;
import kacang.stdui.SelectBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import com.tms.crm.sales.model.OpportunityModule;
import com.tms.crm.sales.model.Opportunity;
import java.text.DateFormatSymbols;
import java.util.Map;

public class FinancialYearSetup extends Form {

	private Button submit;
	private TextField fYear;
	private SelectBox months;
	private TextField currency;

	public FinancialYearSetup()
	{}

	public FinancialYearSetup(String s)	{
		super(s);
	}


	public void initForm()	{

		// Initialization
		submit = new Button("submit");
		submit.setText("Submit");
		fYear = new TextField("fYear");
		fYear.setSize("5");
		months = new SelectBox("months");
		currency = new TextField("currency");
		currency.setSize("10");
		currency.addChild(new ValidatorNotEmpty("vCurrency"));
		addChild(submit);
		addChild(fYear);
		addChild(months);
		addChild(currency);
		fillMonths();
		Opportunity yearEnds = checkMonth();

		if (yearEnds == null)	{
			months.setSelectedOption("11");
		} else if (yearEnds.getFyId() !=null && !"".equals(yearEnds.getFyId())){
			months.setSelectedOption(yearEnds.getYearEnds());
		}
		if (yearEnds.getCurrencySymbol() != null && !"".equals(yearEnds.getCurrencySymbol())) {
			currency.setValue(yearEnds.getCurrencySymbol());
		}
	}

	public void init()	{
		initForm();
	}

	public void onRequest(Event event) {
		initForm();
	}

	public Forward onValidate(Event evt) {

		if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
			Opportunity yearEnds = checkMonth();
			Opportunity setting = new Opportunity();

			if (yearEnds == null ){
				setting.setYearEnds(getSbValue(months));
				setting.setFyId(UuidGenerator.getInstance().getUuid());
				setting.setCurrencySymbol((String) currency.getValue());
				OpportunityModule insertSetting = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
				insertSetting.insertSetting(setting);
				return new Forward("added");
			} else if (yearEnds != null) {
				setting.setYearEnds(getSbValue(months));
				setting.setCurrencySymbol((String) currency.getValue());
				setting.setFyId(yearEnds.getFyId());
				OpportunityModule updateSetting = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
				try {
					updateSetting.updateSetting(setting);
					return new Forward("updated");
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
			}
		}
		return super.onValidate(evt);
	}

	public String getDefaultTemplate() {
		return "sfa/financialYearSetupTemplate";
	}

	public Opportunity checkMonth()	{
		OpportunityModule grabSetting = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		Opportunity setting  = grabSetting.getFinancilSetting();
		return setting;
	}

	public void fillMonths()	{
		for (int i=0 ; i<12 ; i++ )	{
			months.addOption(String.valueOf(i),getMonthsText(i));
		}
	}

	public String getMonthsText(int month)	{
	  DateFormatSymbols symbols = new DateFormatSymbols();
         String[] months =  symbols.getMonths();
         if(month<12)
             return months[month];
         else
             return "";
	}

	public static String getSbValue(SelectBox sb) {
		if (sb != null) {
   			Map selected = sb.getSelectedOptions();
			if (selected.size() == 1) {
    			return (String) selected.keySet().iterator().next();
   			}
  		}
  	return null;
 }

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public TextField getfYear() {
		return fYear;
	}

	public void setfYear(TextField fYear) {
		this.fYear = fYear;
	}

	public SelectBox getMonths() {
		return months;
	}

	public void setMonths(SelectBox months) {
		this.months = months;
	}

	public TextField getCurrency() {
		return currency;
	}

	public void setCurrency(TextField currency) {
		this.currency = currency;
	}
}
