package com.tms.hr.leave.ui;

import kacang.stdui.Form;
import kacang.stdui.SelectBox;

import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;


public class LeaveSummaryByYear extends Form {
    protected SelectBox yearSelect;
    protected LeaveSummaryTable lst;
    protected String selected_year;
    private int year;

    public void init() {
        setColumns(1);

        yearSelect = new SelectBox("yearSelect");
        yearSelect.setAlign(Form.ALIGN_RIGHT);

        Map yearMap = new SequencedHashMap();
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);

        yearMap.put((String) Integer.toString(year - 2),
            (String) Integer.toString(year - 2));
        yearMap.put((String) Integer.toString(year - 1),
            (String) Integer.toString(year - 1));
        yearMap.put((String) Integer.toString(year),
            (String) Integer.toString(year));
        yearMap.put((String) Integer.toString(year+1),
                    (String) Integer.toString(year+1));
        yearMap.put((String) Integer.toString(year+2),
                    (String) Integer.toString(year+2));


        yearSelect.setOptionMap(yearMap);

        yearSelect.setColspan(1);
        addChild(yearSelect);

        lst = new LeaveSummaryTable("leavesummarytable");
        lst.setWidth("100%");
        lst.init();

        selected_year = Integer.toString(year);
        yearSelect.setSelectedOption(selected_year);


        yearSelect.setColspan(2);
        addChild(lst);

        yearSelect.setOnChange("document.forms['" + getAbsoluteName() +
            "'].submit()");
    }

    public void updatePage() {
        Collection selected_year_List = (Collection) yearSelect.getValue();

        if (selected_year_List.size() > 0) {
            selected_year = (String) selected_year_List.iterator().next();
            yearSelect.setSelectedOption(selected_year);
            lst.setYear(Integer.parseInt(selected_year));
           
        } else {

            selected_year = Integer.toString(year);

    
            lst.setYear(Integer.parseInt(selected_year));
        }
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        updatePage();

        return null;
    }

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSelected_year() {
		return selected_year;
	}

	public void setSelected_year(String selected_year) {
		this.selected_year = selected_year;
	}
    
	
    
    
}
