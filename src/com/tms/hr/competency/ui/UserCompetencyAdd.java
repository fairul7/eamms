package com.tms.hr.competency.ui;

import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;
import com.tms.hr.competency.UserCompetencies;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class UserCompetencyAdd extends Form
{
    public static final String FORWARD_SUCCESSFUL = "success";
    public static final String FORWARD_FAILED = "fail";
    public static final String FORWARD_CANCEL = "cancel";

    public static final String DEFAULT_SELECTION_VALUE = "-1";

    protected SelectBox competencyType;
    protected SelectBox competencyName;
    protected SelectBox competencyLevel;
    protected Label competencyDescription;
    protected Button add;
    protected Button cancel;

    protected String selectedType;
    protected String selectedName;

    public UserCompetencyAdd()
    {
    }

    public UserCompetencyAdd(String s)
    {
        super(s);
    }

    public void init()
    {
        CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);

        selectedType = DEFAULT_SELECTION_VALUE;
        selectedName = DEFAULT_SELECTION_VALUE;
        //Initializing Types
        competencyType = new SelectBox("competencyType");
        competencyType.setOnChange("submit();");
        competencyType.addOption(DEFAULT_SELECTION_VALUE, Application.getInstance().getMessage("project.label.competenciestype","Select A Competency Type"));
        try
        {
            Collection types = handler.getCompetencyTypes();
            for(Iterator i = types.iterator(); i.hasNext();)
            {
                String type = (String) i.next();
                competencyType.addOption(type, type);
            }
        }
        catch (CompetencyException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        competencyName = new SelectBox("competencyName");
        competencyName.setOnChange("submit();");
        competencyName.setHidden(true);
        competencyName.addOption(DEFAULT_SELECTION_VALUE,  Application.getInstance().getMessage("project.label.selectcompetencies","Select A Competency"));
        //Initializing Levels
        competencyLevel = new SelectBox("competencyLevel");
        competencyLevel.setHidden(true);
        String levels = Application.getInstance().getProperty(Competency.PROPERTY_COMPETENCY_LEVELS);
        StringTokenizer tokenizer = new StringTokenizer(levels, Competency.DEFAULT_DELIMITER);
        while(tokenizer.hasMoreTokens())
        {
            String level = tokenizer.nextToken();
            competencyLevel.addOption(level, level);
        }
        competencyDescription = new Label("competencyDescription", "");
        add = new Button("add", Application.getInstance().getMessage("com.tms.hr.competency.Competency.add","Add Competency"));
        add.setHidden(true);
        cancel = new Button("cancel", Application.getInstance().getMessage("com.tms.hr.competency.Competency.cancel","Cancel"));
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(add);
        buttonPanel.addChild(cancel);

        setColumns(2);
        addChild(new Label("labelType", Application.getInstance().getMessage("project.label.type","Type")));
        addChild(competencyType);
        addChild(new Label("labelName", Application.getInstance().getMessage("project.label.name","Name")));
        addChild(competencyName);
        addChild(new Label("labelDescription", Application.getInstance().getMessage("project.label.description","Description")));
        addChild(competencyDescription);
        addChild(new Label("labelLevel", Application.getInstance().getMessage("project.label.level","Level")));
        addChild(competencyLevel);
        addChild(new Label("labelButton", ""));
        addChild(buttonPanel);
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
        Forward forward = super.onSubmit(event);
        CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
        //Type onChange()
        String selectedType = (String) competencyType.getSelectedOptions().keySet().iterator().next();
        if(!(this.selectedType.equals(selectedType)))
        {
            this.selectedType = selectedType;
            this.selectedName = DEFAULT_SELECTION_VALUE;
            try
            {
                User user = getWidgetManager().getUser();
                Map competencies = handler.getUserCompetencies(user.getId()).getCompetencies();
                Collection ids = new ArrayList();
                for(Iterator i = competencies.keySet().iterator(); i.hasNext();)
                    ids.add(((Competency) i.next()).getCompetencyId());
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("competencyType", selectedType, DaoOperator.OPERATOR_AND));
                query.addProperty(new OperatorIn("competencyId", ids.toArray(), DaoOperator.OPERATOR_NAN));
                Collection names = new ArrayList();
                names = handler.getCompetencies(query, 0, -1, "competencyName", false);
                Map optionName = new SequencedHashMap();
                optionName.put(DEFAULT_SELECTION_VALUE, "Select A Competency");
                for(Iterator i = names.iterator(); i.hasNext();)
                {
                    Competency competency = (Competency) i.next();
                    optionName.put(competency.getCompetencyId(), competency.getCompetencyName());
                }
                competencyName.setOptionMap(optionName);
                competencyName.setSelectedOptions(new String[] {selectedName});
                competencyName.setHidden(false);
                competencyDescription.setText("");
                competencyLevel.setHidden(true);
                add.setHidden(true);
                if(DEFAULT_SELECTION_VALUE.equals(selectedType))
                    competencyName.setHidden(true);
            }
            catch (CompetencyException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
        else
        {
            String selectedName = (String) competencyName.getSelectedOptions().keySet().iterator().next();
            if(!(this.selectedName.equals(selectedName)))
            {
                this.selectedName = selectedName;
                if(DEFAULT_SELECTION_VALUE.equals(selectedName))
                {
                    competencyDescription.setText("");
                    competencyLevel.setHidden(true);
                    add.setHidden(true);
                }
                else
                {
                    try
                    {
                        Competency competency = handler.getCompetency(selectedName);
                        competencyDescription.setText(competency.getCompetencyDescription());
                        competencyLevel.setHidden(false);
                        add.setHidden(false);
                    }
                    catch (CompetencyException e)
                    {
                        Log.getLog(getClass()).error(e.getMessage(), e);
                    }
                }
            }
        }
        return forward;
    }

    public Forward onValidate(Event event)
    {
        Forward forward = null;
        if(add.getAbsoluteName().equals(findButtonClicked(event)))
        {
            User user = getWidgetManager().getUser();
            CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
            String selectedLevel = (String) competencyLevel.getSelectedOptions().keySet().iterator().next();
            if(!(DEFAULT_SELECTION_VALUE.equals(selectedType) || DEFAULT_SELECTION_VALUE.equals(selectedName) || selectedLevel == null || "".equals(selectedLevel)))
            {
                try
                {
                    Map competencies = handler.getUserCompetencies(user.getId()).getCompetencies();
                    Competency competency = handler.getCompetency(selectedName);
                    competencies.put(competency, selectedLevel);
                    UserCompetencies userCompetencies = new UserCompetencies();
                    userCompetencies.setUser(user);
                    userCompetencies.setCompetencies(competencies);
                    handler.addUserCompetencies(userCompetencies);
                    init();
                    forward = new Forward(FORWARD_SUCCESSFUL);
                }
                catch (CompetencyException e)
                {
                    forward = new Forward(FORWARD_FAILED);
                    Log.getLog(getClass()).error(e.getMessage(), e);
                }
            }
        }
        return forward;
    }

	public void onRequest(Event event)
	{
		CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
		SequencedHashMap types = new SequencedHashMap();
		types.put(DEFAULT_SELECTION_VALUE, Application.getInstance().getMessage("project.label.competenciestype","Select A Competency Type"));
        try
        {
            Collection cTypes = handler.getCompetencyTypes();
            for(Iterator i = cTypes.iterator(); i.hasNext();)
            {
                String type = (String) i.next();
				types.put(type, type);
            }
        }
        catch (CompetencyException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
		competencyType.setOptionMap(types);
		super.onRequest(event);
	}

    /* Getters and Setters */
    public Button getAdd()
    {
        return add;
    }

    public void setAdd(Button add)
    {
        this.add = add;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public Label getCompetencyDescription()
    {
        return competencyDescription;
    }

    public void setCompetencyDescription(Label competencyDescription)
    {
        this.competencyDescription = competencyDescription;
    }

    public SelectBox getCompetencyLevel()
    {
        return competencyLevel;
    }

    public void setCompetencyLevel(SelectBox competencyLevel)
    {
        this.competencyLevel = competencyLevel;
    }

    public SelectBox getCompetencyName()
    {
        return competencyName;
    }

    public void setCompetencyName(SelectBox competencyName)
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
}
