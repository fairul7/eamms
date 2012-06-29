package com.tms.hr.competency.ui;

import com.tms.fms.department.model.FMSDepartmentDao;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.widgets.BoldLabel;
import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class CompetencyForm extends Form
{
    public static final String DEFAULT_SELECT_VALUE = "-1";
    public static final String FORWARD_SUCCESSFUL = "successful";
    public static final String FORWARD_FAILED = "failed";
    public static final String FORWARD_CANCEL = "cancel";

    protected String competencyId;
    protected String permissionId;

    protected TextField competencyName;
    protected SelectBox competencyType;
    protected TextField newCompetencyType;
    protected TextBox competencyDescription;
    protected Button submit;
    protected Button cancel;
    
    protected Label lbName;
    protected Label lbType;
    protected Label lbDescription;
    protected Label lbUnit;
    protected Label lbNewType;
    protected SelectBox unit;

    protected ValidatorNotEmpty validName;

    public CompetencyForm()
    {
    }

    public CompetencyForm(String s)
    {
        super(s);
    }

    public void init()
    {
        setMethod("POST");
        setColumns(2);
        CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
        validName = new ValidatorNotEmpty("validName");
        competencyName = new TextField("competencyName");
        competencyName.setSize("35");
        competencyName.addChild(validName);
        Collection types = new ArrayList();
        try
        {
            types = handler.getCompetencyTypes();
        }
        catch (CompetencyException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        competencyType = new SelectBox("competencyType");
        competencyType.addOption("-1", Application.getInstance().getMessage("project.label.competenciestype","Select A Competency Type"));
        for(Iterator i = types.iterator(); i.hasNext();)
        {
            String type = (String) i.next();
            competencyType.addOption(type, type);
        }
        newCompetencyType = new TextField("newCompetencyType");
        newCompetencyType.setSize("20");
        Panel newCompetencyPanel = new Panel("newCompetencyPanel");
        
        lbNewType = new BoldLabel("newCompetencyLabel");
		lbNewType.setAlign("right");
		lbNewType.setText(Application.getInstance().getMessage("project.label.newtype","New Type")+" * " + ": ");
		//addChild(lbName);
        
        newCompetencyPanel.addChild(lbNewType);
        newCompetencyPanel.addChild(newCompetencyType);
        
        unit = new SelectBox("unit");
        try {
	    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			Collection lstUnit = dao.selectUnit();
			unit.addOption("-1", "-- Please Select --");
		    if (lstUnit.size() > 0) {
		    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
		        	FMSUnit o = (FMSUnit)i.next();
		        	unit.addOption(o.getId(),o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		
        competencyDescription = new TextBox("competencyDescription");
        competencyDescription.setRows("10");
        competencyDescription.setCols("40");

		if (isEditMode()) {
        	submit = new Button("submit", Application.getInstance().getMessage("com.tms.hr.competency.Competency.update","Update"));
		} else {
			submit = new Button("submit", Application.getInstance().getMessage("com.tms.hr.competency.Competency.submit","Submit"));
		}

		cancel = new Button("cancel", Application.getInstance().getMessage("com.tms.hr.competency.Competency.cancel","Cancel"));
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(submit);
        buttonPanel.addChild(cancel);
        //Adding children
        
        
        lbName = new BoldLabel("labelName");
		lbName.setAlign("right");
		lbName.setText(Application.getInstance().getMessage("project.label.name","Name")+" *");
		addChild(lbName);		
        
        addChild(competencyName);
        
        lbType = new BoldLabel("labelType");
		lbType.setAlign("right");
		lbType.setText(Application.getInstance().getMessage("project.label.type","Type")+" *");
		addChild(lbType);
        
        addChild(competencyType);
        
        addChild(new Label("labelNew", ""));
        addChild(newCompetencyPanel);
        
        lbUnit = new BoldLabel("labelUnit");
        lbUnit.setAlign("right");
        lbUnit.setText(Application.getInstance().getMessage("fms.label.unitName","Unit"));
        addChild(lbUnit);
        addChild(unit);
        
        lbDescription = new BoldLabel("labelDescription");
        lbDescription.setAlign("right");
        lbDescription.setText(Application.getInstance().getMessage("project.label.description","Description"));
		addChild(lbDescription);
		
        addChild(competencyDescription);
        addChild(new Label("labelButton", ""));
        addChild(buttonPanel);

        //addChild(new Label("labelName", Application.getInstance().getMessage("project.label.name","Name")+" *"));
        //addChild(new Label("labelType", Application.getInstance().getMessage("project.label.type","Type")+" *"));
        //addChild(new Label("labelDescription", Application.getInstance().getMessage("project.label.description","Description")));
        refresh();
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public Forward actionPerformed(Event event)
    {
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward(FORWARD_CANCEL);
        else
            return super.actionPerformed(event);
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = null;
        String selected = "";
        forward = super.onSubmit(event);
        if(competencyType.getSelectedOptions().size() > 0)
            selected = (String) competencyType.getSelectedOptions().keySet().iterator().next();
        if((DEFAULT_SELECT_VALUE.equals(selected) || "".equals(selected)) && (newCompetencyType.getValue() == null || "".equals(newCompetencyType.getValue())))
        {
            competencyType.setInvalid(true);
            newCompetencyType.setInvalid(true);
            setInvalid(true);
            forward = new Forward(FORWARD_FAILED);
        }
        else
        {
            competencyType.setInvalid(false);
            newCompetencyType.setInvalid(false);
            //Checking for name uniqueness
            CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("competencyName", competencyName.getValue().toString().trim(), DaoOperator.OPERATOR_AND));
            if(!(competencyId == null || "".equals(competencyId)))
                query.addProperty(new OperatorEquals("competencyId", competencyId, DaoOperator.OPERATOR_NAN));
            Collection list = new ArrayList();
            try
            {
                list = handler.getCompetencies(query, 0, 1, null, false);
            }
            catch (CompetencyException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            if(list.size() > 0)
            {
                competencyName.setInvalid(true);
                validName.setInvalid(true);
                validName.setText("Name Already In Use");
                setInvalid(true);
                forward = new Forward(FORWARD_FAILED);
            }
            else
            {
                competencyName.setInvalid(false);
                validName.setInvalid(false);
                setInvalid(false);
            }
        }
        return forward;
    }

    protected void refresh()
    {
        validName.setText("Please Specify A Competency Name");
    }

    protected Competency generateCompetency()
    {
        Competency competency = new Competency();
        competency.setCompetencyName(competencyName.getValue().toString().trim());
        String selected = "";
        if(competencyType.getSelectedOptions().size() > 0)
            selected = ((String) competencyType.getSelectedOptions().keySet().iterator().next()).trim();
        if(!(newCompetencyType.getValue() == null || "".equals(newCompetencyType.getValue())))
            competency.setCompetencyType(newCompetencyType.getValue().toString());
        else
            competency.setCompetencyType(selected);
        competency.setCompetencyDescription(competencyDescription.getValue().toString().trim());
        competency.setUnitId((String)unit.getSelectedOptions().keySet().iterator().next());
        return competency;
    }

    public TextField getCompetencyName()
    {
        return competencyName;
    }

    public void setCompetencyName(TextField competencyName)
    {
        this.competencyName = competencyName;
    }

    public SelectBox getCompetencyType()
    {
        return competencyType;
    }

    public void setCompetencyType(SelectBox competencyType)
    {
        this.competencyType = competencyType;
    }

    public TextField getNewCompetencyType()
    {
        return newCompetencyType;
    }

    public void setNewCompetencyType(TextField newCompetencyType)
    {
        this.newCompetencyType = newCompetencyType;
    }

    public TextBox getCompetencyDescription()
    {
        return competencyDescription;
    }

    public void setCompetencyDescription(TextBox competencyDescription)
    {
        this.competencyDescription = competencyDescription;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public ValidatorNotEmpty getValidName()
    {
        return validName;
    }

    public void setValidName(ValidatorNotEmpty validName)
    {
        this.validName = validName;

    }

    public String getCompetencyId()
    {
        return competencyId;
    }

    public void setCompetencyId(String competencyId)
    {
        this.competencyId = competencyId;
    }

	public boolean isEditMode() {
		return false;
	}

}
