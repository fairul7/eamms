package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import com.tms.crm.sales.model.CompanyModule;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 13, 2004
 * Time: 4:49:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportByCompanyForm extends Form {

    private Button view;
    private ComboSelectBox sb_company;
    private Radio viewAll;
    private Radio viewSelected;
    private ButtonGroup viewGroup;
    private DateField from;
    private DateField to;
    private Date toDate,fromDate;
    private Collection companiesIdList;
	private CompaniesSelectBox companiesList;
	protected DateField startFrom;
	protected DateField startTo;
	protected Date startFromDate, startToDate;
	protected CheckBox start;
	protected CheckBox close;
	protected boolean startBool;
	protected boolean closeBool;
	private ValidatorMessage vmsg_start;
	private ValidatorMessage vmsg_close;

    public ReportByCompanyForm() {
    }

    public ReportByCompanyForm(String name) {
        super(name);
    }

    public void init() {

        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setWidth("100%");
        view = new Button("view",Application.getInstance().getMessage("sfa.label.view","View"));
        sb_company = new ComboSelectBox("companies");
        viewAll = new Radio("viewall",Application.getInstance().getMessage("sfa.label.viewAll","View All"));
        viewAll.setChecked(true);
        viewSelected = new Radio("viewSelected",Application.getInstance().getMessage("sfa.label.viewSelected","View Selected"));
        viewGroup = new ButtonGroup("viewGroup",new Radio[]{viewAll,viewSelected});
        from = new DateField("from");
        to = new DateField("to");
		companiesList = new CompaniesSelectBox("companiesList");
		addChild(companiesList);
		companiesList.init();
		startFrom = new DateField("startFrom");
		startTo = new DateField("startTo");
		start = new CheckBox("start");
		close = new CheckBox("close");
		vmsg_start = new ValidatorMessage("vmsg_start");
		start.addChild(vmsg_start);
		vmsg_close = new ValidatorMessage("vmsg_close");
		close.addChild(vmsg_close);
		addChild(view);
        addChild(viewAll);
        addChild(viewSelected);
        addChild(sb_company);
        sb_company.init();
        addChild(from);
        addChild(to);
		addChild(startFrom);
		addChild(startTo);
		addChild(start);
		addChild(close);
        setMethod("POST");
    }



    public void onRequest(Event evt) {
		super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
        CompanyModule cm= (CompanyModule) Application.getInstance().getModule(CompanyModule.class);
        if(sb_company!=null){
            Map rightMap = sb_company.getRightValues();
            Map leftMap = cm.getCompanyMap();
            for (Iterator iterator = rightMap.keySet().iterator(); iterator.hasNext();) {
                String companyId = (String) iterator.next();
                leftMap.remove(companyId);
            }
            sb_company.setLeftValues(leftMap);
        }

    }

	public Forward onSubmit(Event evt) {
		Forward fwd = super.onSubmit(evt);
		if (!start.isChecked() && !close.isChecked()) {
			vmsg_start.showError("");
			vmsg_close.showError("");
			setInvalid(true);
		}
		return fwd;
	}


    public Forward onValidate(Event evt) {

		if(view.getAbsoluteName().equals(findButtonClicked(evt))){
			toDate = to.getDate();
            fromDate = from.getDate();
			startFromDate = startFrom.getDate();
			startToDate = startTo.getDate();
			startBool = start.isChecked();
			closeBool = close.isChecked();
			companiesIdList = new ArrayList();

			if(viewSelected.isChecked()){
                companiesIdList.addAll(Arrays.asList(companiesList.getIds()));
            }else{
                CompanyModule cm = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);
                Map map = cm.getCompanyMap();
                for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
                    String companyId = (String) iterator.next();
                    companiesIdList.add(companyId);
                }

			}
			return new Forward("view");
		}
		 return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public ComboSelectBox getSb_company() {
        return sb_company;
    }

    public void setSb_company(ComboSelectBox sb_company) {
        this.sb_company = sb_company;
    }

    public Radio getViewAll() {
        return viewAll;
    }

    public void setViewAll(Radio viewAll) {
        this.viewAll = viewAll;
    }

    public Radio getViewSelected() {
        return viewSelected;
    }

    public void setViewSelected(Radio viewSelected) {
        this.viewSelected = viewSelected;
    }

    public ButtonGroup getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(ButtonGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public DateField getFrom() {
        return from;
    }

    public void setFrom(DateField from) {
        this.from = from;
    }

    public DateField getTo() {
        return to;
    }

    public void setTo(DateField to) {
        this.to = to;
    }

    public String getDefaultTemplate() {
        return "sfa/reportbycompany";
    }


    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Collection getCompaniesIdList() {
        return companiesIdList;
    }

    public void setCompaniesIdList(Collection companiesIdList) {
        this.companiesIdList = companiesIdList;
    }

	public CompaniesSelectBox getCompaniesList() {
		return companiesList;
	}

	public void setCompaniesList(CompaniesSelectBox companiesList) {
		this.companiesList = companiesList;
	}

	public DateField getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(DateField startFrom) {
		this.startFrom = startFrom;
	}

	public DateField getStartTo() {
		return startTo;
	}

	public void setStartTo(DateField startTo) {
		this.startTo = startTo;
	}

	public Date getStartFromDate() {
		return startFromDate;
	}

	public void setStartFromDate(Date startFromDate) {
		this.startFromDate = startFromDate;
	}

	public Date getStartToDate() {
		return startToDate;
	}

	public void setStartToDate(Date startToDate) {
		this.startToDate = startToDate;
	}

	public CheckBox getStart() {
		return start;
	}

	public void setStart(CheckBox start) {
		this.start = start;
	}

	public CheckBox getClose() {
		return close;
	}

	public void setClose(CheckBox close) {
		this.close = close;
	}

	public boolean isStartBool() {
		return startBool;
	}

	public void setStartBool(boolean startBool) {
		this.startBool = startBool;
	}

	public boolean isCloseBool() {
		return closeBool;
	}

	public void setCloseBool(boolean closeBool) {
		this.closeBool = closeBool;
	}
}




