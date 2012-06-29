/*
 * AlbumForm
 * Date Created: Jun 16, 2005
 * Author: Tien Soon, Law
 * Description: Abstract album form
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.medialib.model.AlbumModule;
import com.tms.cms.medialib.model.SelectListObject;


public abstract class AlbumForm extends Form {
    protected SelectBox libraryName;
    protected TextField albumName;
    protected TextBox description;
    protected Radio featured;
    protected Radio nonfeatured;
    protected DateField eventDate;
    protected TextField eventTimeHour;
    protected TextField eventTimeMin;
    protected Button submit;
    protected Button cancel;
    protected static final String FORWARD_SUCCESS = "success";
    protected static final String FORWARD_FAILURE = "failure";
    
    public void init() {
        setMethod("POST");
        setColumns(2);
        
        Application app = Application.getInstance();
        AlbumModule albumModule = (AlbumModule) app.getModule(AlbumModule.class);
        
        // Library List
        addChild(new Label("l1", app.getMessage("medialib.label.library*", "Library *")));
        libraryName = new SelectBox("libraryName");
        libraryName.setMultiple(false);
        Map mapLibNames = new SequencedHashMap();
        mapLibNames.put("", app.getMessage("medialib.label.pleaseSelect", "---Please Select---"));
        ArrayList alist = albumModule.getLibrarySelectList();
        if(alist.size() > 0) {
	        for(int i=0; i<alist.size(); i++) {
	            SelectListObject item = (SelectListObject) alist.get(i);
	            mapLibNames.put(item.getId(), item.getName());
	        }
        }
        libraryName.setOptionMap(mapLibNames);
        addChild(libraryName);
        
        // Album Name
        addChild(new Label("l2", app.getMessage("medialib.label.name*", "Name *")));
        ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", app.getMessage("medialib.message.notEmpty", "Please fill in this field"));
        albumName = new TextField("albumName");
        albumName.setMaxlength("255");
        albumName.addChild(vne);
        addChild(albumName);
        
        // Descriptions
        addChild(new Label("l3", app.getMessage("medialib.label.description", "Description")));
        description = new TextBox("description");
        addChild(description);
        
        // Fetured Album
        addChild(new Label("l5", app.getMessage("medialib.label.featured*", "Featured *")));
        featured = new Radio("featured", app.getMessage("medialib.label.yes", "Yes"));
        featured.setGroupName("featuredAlbum");
        nonfeatured = new Radio("nonfeatured", app.getMessage("medialib.label.no", "No"));
        nonfeatured.setGroupName("featuredAlbum");
        nonfeatured.setChecked(true);
        
        Panel featuredAlbumPanel = new Panel("featuredAlbumPanel");
        featuredAlbumPanel.setColumns(1);
        featuredAlbumPanel.addChild(featured);
        featuredAlbumPanel.addChild(nonfeatured);
        addChild(featuredAlbumPanel);
        
        // Event Date
        addChild(new Label("l4", app.getMessage("medialib.label.eventDate", "Event Date")));
        eventDate = new DatePopupField("eventDate");
        eventDate.setDate(Calendar.getInstance().getTime());
        addChild(eventDate);
        addChild(new Label("l6", app.getMessage("medialib.label.eventTime", "Event Time")));
        eventTimeHour = new TextField("eventTimeHour", "00");
        eventTimeHour.setMaxlength("2");
        eventTimeHour.setSize("2");
        eventTimeMin = new TextField("eventTimeMin", "00");
        eventTimeMin.setMaxlength("2");
        eventTimeMin.setSize("2");
        Panel eventTime = new Panel("eventTime");
        eventTime.setColumns(4);
        eventTime.addChild(eventTimeHour);
        eventTime.addChild(new Label(":", ":"));
        eventTime.addChild(eventTimeMin);
        eventTime.addChild(new Label("eventTimeDesc", app.getMessage("medialib.label.eventTimeDesc", "(hh:mm 24H)")));
        addChild(eventTime);
        addChild(new Label("lDummy", ""));
        
        // Submit and Cancel Buttons
        submit = new Button("submit");
        submit.setText(app.getMessage("medialib.label.submit", "Submit"));
        
        cancel = new Button(Form.CANCEL_FORM_ACTION, "cancel");
        cancel.setText(app.getMessage("medialib.label.cancel", "Cancel"));
        cancel.setOnClick("return back()");
        
        Panel buttonsPanel = new Panel("buttonsPanel");
        buttonsPanel.addChild(submit);
        buttonsPanel.addChild(cancel);
        addChild(buttonsPanel);        
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public Forward onValidate(Event evt) {
        removeChildren();
        init();

        return new Forward(FORWARD_SUCCESS);
    }
    
    public Forward onSubmit(Event event) {
        Forward forward = super.onSubmit(event);
        
        // Validate if Library Name is selected
        String librarySelectedOption = (String) libraryName.getSelectedOptions().keySet().iterator().next();
        if("".equals(librarySelectedOption)) {
            libraryName.setInvalid(true);
            this.setInvalid(true);
        }
        
        // Validate if Event Date is not later than current date
        Date jEventDate = eventDate.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(jEventDate);
        boolean isDate=false;        
        
        if (calendar.before(Calendar.getInstance())) {
            isDate=true;
        }
        
        if(! isDate) {
            eventDate.setInvalid(true);
            this.setInvalid(true);
        }
        
        // Validate Event Time
        String hour = (String) eventTimeHour.getValue();
        String min = (String) eventTimeMin.getValue();
        if(! "".equals(hour)) {
	        try {
	            int intHour = Integer.parseInt(hour);
	            if(intHour < 0 || intHour > 24) {
	                eventTimeHour.setInvalid(true);
	                this.setInvalid(true);
	            }
	        }
	        catch(NumberFormatException error) {
	            eventTimeHour.setInvalid(true);
	            this.setInvalid(true);
	        }
        }
        else {
            eventTimeHour.setValue("00");
        }
        if(! "".equals(min)) {
	        try {
	            int intMin = Integer.parseInt(min);
	            if(intMin < 0 || intMin > 60) {
	                eventTimeMin.setInvalid(true);
	                this.setInvalid(true);
	            }
	        }
	        catch(NumberFormatException error) {
	            eventTimeMin.setInvalid(true);
	            this.setInvalid(true);
	        }
        }
        else {
            eventTimeHour.setValue("00");
        }
        
        return forward;
    }
}
