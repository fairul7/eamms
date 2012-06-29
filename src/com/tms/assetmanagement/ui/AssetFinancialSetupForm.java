package com.tms.assetmanagement.ui;

import java.text.DateFormatSymbols;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;
import com.tms.assetmanagement.ui.ValidatorIsInteger;
import com.tms.assetmanagement.ui.ValidatorIsNumeric;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.DataFinancialSetup;

public class AssetFinancialSetupForm extends Form {

	protected Label lblFinancial;
	protected Label lblCurrency;
	protected Label lblPattern;
	protected SelectBox sbxMonthly;
	protected TextField txtfdCurrency;
	protected SelectBox sbxCurrencyPattern;
	//protected Button btnSubmit;
	protected Button btnUpdate;
	protected Panel panelBtn;
	private DataFinancialSetup obj;

	public void init(){		
		super.init();	
		setMethod("post");
		setColumns(2);

		lblFinancial = new Label("lblFinancial", "<span class='classRowLabel'>" + Application.getInstance().getMessage("asset.label.financialMonth","Financial Year ends on the month of")+"&nbsp;* </span>");
		lblFinancial.setAlign("right");
		addChild(lblFinancial);

		sbxMonthly = new SelectBox ("sbxMonthly");
		Map mapMonthly = new SequencedHashMap();  
		DateFormatSymbols symbols = new DateFormatSymbols();
		String[] months =  symbols.getMonths();
		for (int i = 0; i< months.length - 1; i++){
			mapMonthly.put(String.valueOf(i), months[i]);
		}
		sbxMonthly.setOptionMap(mapMonthly);
		addChild(sbxMonthly);    

		lblCurrency = new Label("lblCurrency","<span class='classRowLabel'>"+  Application.getInstance().getMessage("asset.label.financialCurrency","Currency Symbol")+"&nbsp;* </span>");
		lblCurrency.setAlign("right");
		addChild(lblCurrency);

		txtfdCurrency = new TextField("txtfdCurrency");
		txtfdCurrency.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
		txtfdCurrency.setSize("5");
		txtfdCurrency.setMaxlength("5");
		addChild(txtfdCurrency);

		lblPattern = new Label("lblPattern","<span class='classRowLabel'>"+  Application.getInstance().getMessage("asset.label.currencyPattern","Currency Pattern")+"&nbsp;* </span>");
		lblPattern.setAlign("right");
		addChild(lblPattern);

		sbxCurrencyPattern = new SelectBox("sbxCurrencyPattern");	
		Map mapPattern = new SequencedHashMap();  
		mapPattern.put("#,###,###,##0.00", "#,###,###,##0.00");
		mapPattern.put("#,###,###,###.##", "#,###,###,###.##");
		mapPattern.put("#,###,###,###.00", "#,###,###,###.00");
		sbxCurrencyPattern.setOptionMap(mapPattern);
		addChild(sbxCurrencyPattern);

		Label lbl = new Label("lbl");
		addChild(lbl);	    
		btnUpdate =  new Button ("btnUpdate", Application.getInstance().getMessage("asset.label.btnUpdate", "Update"));    
		panelBtn = new Panel("panelBtn"); 
	}


	public void onRequest(Event event){

		super.onRequest(event);		 
		removeChildren();
		init();

		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);

		panelBtn.addChild(btnUpdate);
		Collection colFinancialDetail = mod.retrieveFinancialSetup();
		if (colFinancialDetail != null &&colFinancialDetail.size() > 0)
			setFinancialObject(colFinancialDetail);

		addChild(panelBtn);
	}

	public Forward onValidate(Event event) {
		super.onValidate(event);

		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);

		DataFinancialSetup objFinancialSetup = new DataFinancialSetup();
		objFinancialSetup.setCurrencyPattern((String)((List) sbxCurrencyPattern.getValue()).get(0));
		objFinancialSetup.setCurrencySymbol(txtfdCurrency.getValue().toString());
		objFinancialSetup.setFinancialMonth((String)((List) sbxMonthly.getValue()).get(0));

		if (btnUpdate.getAbsoluteName().equals(findButtonClicked(event))){
			objFinancialSetup.setFinancialId(obj.getFinancialId());
			//update record
			mod.updateFinancialSetup(objFinancialSetup);
			return new Forward("update");
		}
		return null;
	}

	public void setFinancialObject(Collection colFinancialDetail){
		for (Iterator iterator = colFinancialDetail.iterator(); iterator.hasNext();) {
			obj = (DataFinancialSetup)iterator.next();
			sbxMonthly.setSelectedOption(obj.getFinancialMonth());
			txtfdCurrency.setValue(obj.getCurrencySymbol());
			sbxCurrencyPattern.setSelectedOption(obj.getCurrencyPattern());
		}
	}
}
