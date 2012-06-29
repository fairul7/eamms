package com.tms.crm.sales.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.CheckBox;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

import java.util.Date;
import java.util.Collection;

public class ReportForm extends Form{
    protected Button view;
    protected DateField from;
    protected DateField to;
    protected Date toDate,fromDate;

	protected DateField startFrom;
	protected DateField startTo;
	protected Date startFromDate, startToDate;
	protected CheckBox start;
	protected CheckBox close;
	protected boolean startBool;
	protected boolean closeBool;
	

    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setWidth("100%");
        view = new Button("view",Application.getInstance().getMessage("sfa.label.view","View"));
        from = new DateField("from");
        to = new DateField("to");
		startFrom = new DateField("startFrom");
		startTo = new DateField("startTo");
		start = new CheckBox("start");
		close = new CheckBox("close");
        addChild(view);
        addChild(from);
        addChild(to);
		addChild(startFrom);
		addChild(startTo);
		addChild(start);
		addChild(close);
        setMethod("POST");
    }


    public Forward onValidate(Event evt) {
        if(view.getAbsoluteName().equals(findButtonClicked(evt))){
            fromDate = from.getDate();
            toDate = to.getDate();
			startFromDate = startFrom.getDate();
			startToDate = startTo.getDate();
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


    public String getDefaultTemplate() {
        return "sfa/reportform";
    }

}
