package com.tms.collab.timesheet.ui;

import kacang.stdui.*;
import kacang.ui.*;
import com.tms.crm.sales.misc.DateUtil;
import java.util.*;

public class TimeSheetPortlet extends Form {
	protected Date selectedDate;
	protected Button prevButton;
	protected Button nextButton;
	protected Button todayButton;
	
	public void init() {
		selectedDate = DateUtil.getDateOnly(new Date());
		
		prevButton = new Button("prevButton");
		addChild(prevButton);
		
		nextButton = new Button("nextButton");
		addChild(nextButton);
		
		todayButton = new Button("todayButton");
		addChild(todayButton);
	}
	
	public Forward onValidate(Event evt) {
		String btnClicked = findButtonClicked(evt);
		if (btnClicked.endsWith("prevButton")) {
			selectedDate = DateUtil.dateAdd(selectedDate, Calendar.DATE, -7);
		} else if (btnClicked.endsWith("nextButton")) {
			selectedDate = DateUtil.dateAdd(selectedDate, Calendar.DATE, 7);
		} else if (btnClicked.endsWith("todayButton")) {
			selectedDate = DateUtil.getDateOnly(new Date());
		}
		
		return null;
	}
	
	public Date getSelectedDate() {
		return selectedDate;
	}
	
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
}