package com.tms.collab.resourcemanager.ui;

import kacang.stdui.Form;
import kacang.stdui.TimeField;
import kacang.stdui.DateField;
import kacang.stdui.Button;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 15, 2003
 * Time: 4:11:56 PM
 * To change this template use Options | File Templates.
 */
public class ResourceBookingForm extends Form
{

    private DateField startDate;
    private DateField endDate;
    private TimeField startTime;
    private TimeField endTime;
    private Button bookButton;

    public ResourceBookingForm()
    {
    }

    public ResourceBookingForm(String name)
    {
        super(name);
    }

    public Forward onValidate(Event evt)
    {
        String button = findButtonClicked(evt);
        if(bookButton.getAbsoluteName().equals(button)){

        }
        return null;
    }

    public void init()
    {
        startDate = new kacang.stdui.DateField("startDate");
        endDate = new kacang.stdui.DateField("endDate");
        startTime = new TimeField("startTime");
        endTime = new TimeField("endTime");
        bookButton = new Button("bookButton",Application.getInstance().getMessage("resourcemanager.label.Book","Book"));
        addChild(startDate);
        addChild(startTime);
        addChild(endDate);
        addChild(endTime);
        addChild(bookButton);
    }

    public String getDefaultTemplate()
    {
        return "bookingform";
    }

    public DateField getStartDate()
    {
        return startDate;
    }

    public void setStartDate(DateField startDate)
    {
        this.startDate = startDate;
    }

    public DateField getEndDate()
    {
        return endDate;
    }

    public void setEndDate(DateField endDate)
    {
        this.endDate = endDate;
    }

    public TimeField getStartTime()
    {
        return startTime;
    }

    public void setStartTime(TimeField startTime)
    {
        this.startTime = startTime;
    }

    public TimeField getEndTime()
    {
        return endTime;
    }

    public void setEndTime(TimeField endTime)
    {
        this.endTime = endTime;
    }

    public Button getBookButton()
    {
        return bookButton;
    }

    public void setBookButton(Button bookButton)
    {
        this.bookButton = bookButton;
    }
}
