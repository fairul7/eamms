package com.tms.collab.project.ui;

import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.ui.CalendarUsersSelectBox;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.project.Project;
import com.tms.collab.project.ProjectMember;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsUtil;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.document.Document;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.TreeSet;

public class ProjectForm extends Form
{
    public static final String FORWARD_SUCESSFUL = "forward.worms.project.Successful";
    public static final String FORWARD_FAILED = "forward.worms.project.Failed";
    public static final String FORWARD_CANCEL = "forward.worms.project.Cancel";
    public static final String DEFAULT_CATEGORY = "-1";

    protected TextField projectName;
    protected TextField client;
    protected TextBox projectDescription;
    protected TextField projectValue;
    protected SelectBox projectCurrencyType;
    protected SelectBox projectCategory;
    protected TextField newCategory;
    protected SelectBox owner;
	protected ComboSelectBox projectWorkingDays;
	protected TextBox projectSummary;
	protected CheckBox archived;
	protected DocumentPopupSelectBox projectFiles;
	
	//testing
	protected CalendarUsersSelectBox projectMembers;

    protected Button submit;
    protected Button cancel;

    protected ValidatorNotEmpty validName;
    protected ValidatorIsNumeric validValue;

    public ProjectForm()
    {
    }

    public ProjectForm(String s)
    {
        super(s);
    }

    public void init()
    {
        setColumns(2);
        setMethod("POST");
        // New
        client = new TextField("client");
        client.setSize("35");       
        
        projectName = new TextField("projectName");
        projectName.setSize("35");
        projectDescription = new TextBox("projectDescription");
        projectDescription.setRows("7");
        projectDescription.setCols("35");
        projectCategory = new SelectBox("projectCategory");
        newCategory = new TextField("newCategory");
        newCategory.setSize("35");
        projectValue = new TextField("projectValue");
        projectValue.setSize("15");
        projectCurrencyType=new SelectBox("projectCurrencyType");
        projectCurrencyType.setOptionMap(WormsUtil.getCurrencyType());
        owner = new SelectBox("owner");
		projectFiles = new DocumentPopupSelectBox("projectFiles");
		projectWorkingDays = new ComboSelectBox("projectWorkingDays");
		projectSummary = new TextBox("projectSummary");
		projectSummary.setRows("15");
		projectSummary.setCols("35");
		archived = new CheckBox("archived");
		archived.setText(Application.getInstance().getResourceBundle().getString("project.label.archived"));

		if (isEditMode()) {
			submit = new Button("submit", Application.getInstance().getResourceBundle().getString("general.label.update"));
		} else {
			submit = new Button("submit", Application.getInstance().getResourceBundle().getString("general.label.submit"));
		}
        cancel = new Button("cancel", Application.getInstance().getResourceBundle().getString("general.label.cancel"));
        
        
        
        validName = new ValidatorNotEmpty("validName");
        projectName.addChild(validName);
        validValue = new ValidatorIsNumeric("validValue");
        projectValue.addChild(validValue);
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(submit);
        buttonPanel.addChild(cancel);
        Panel categoryPanel = new Panel("categoryPanel");
        categoryPanel.addChild(new Label("labelNew", Application.getInstance().getResourceBundle().getString("project.label.newCategory")+" * " + ": "));
        categoryPanel.addChild(newCategory);

        projectMembers = new CalendarUsersSelectBox("projectMembers");
        
        
        addChild(new Label("labelName", Application.getInstance().getResourceBundle().getString("project.label.name")+" *"));
        addChild(projectName);
        addChild(new Label("labelClient", Application.getInstance().getResourceBundle().getString("project.label.Client")));
        addChild(client);
        addChild(new Label("labelDescription", Application.getInstance().getResourceBundle().getString("project.label.description")));
        addChild(projectDescription);
        addChild(new Label("labelCategory", Application.getInstance().getResourceBundle().getString("project.label.category")+" *"));
        addChild(projectCategory);
        addChild(new Label("labelNew", ""));
        addChild(categoryPanel);
        addChild(new Label("labelValue", Application.getInstance().getResourceBundle().getString("project.label.projectValue")+" *"));
        addChild(projectValue);
        addChild(new Label("labelCurrencyType",Application.getInstance().getResourceBundle().getString("project.label.projectCurrencyType")));
        addChild(projectCurrencyType);
        addChild(new Label("labelOwner", Application.getInstance().getResourceBundle().getString("project.label.projectOwner")));
        addChild(owner);
        addChild(new Label("labelMembers", Application.getInstance().getResourceBundle().getString("project.label.projectMembers")));
        addChild(projectMembers);
        projectMembers.init();
		addChild(new Label("labelWorkingDays", Application.getInstance().getResourceBundle().getString("project.label.workingDays")));
		addChild(projectWorkingDays);
		addChild(new Label("labelFiles", Application.getInstance().getResourceBundle().getString("project.label.associatedDocuments")));
		addChild(projectFiles);
		addChild(new Label("labelSummary", Application.getInstance().getResourceBundle().getString("project.label.projectSummary")));
		addChild(projectSummary);
		addChild(new Label("labelArchived", ""));
		addChild(archived);
        addChild(new Label("labelButton", ""));
        addChild(buttonPanel);

        
        
		projectWorkingDays.init();
		/* Setting working days */
		Map notWorking = new SequencedHashMap();
		notWorking.put("1", Application.getInstance().getResourceBundle().getString("general.label.sunday"));
		notWorking.put("7", Application.getInstance().getResourceBundle().getString("general.label.saturday"));
		Map working = new SequencedHashMap();
		working.put("2", Application.getInstance().getResourceBundle().getString("general.label.monday"));
		working.put("3", Application.getInstance().getResourceBundle().getString("general.label.tuesday"));
		working.put("4", Application.getInstance().getResourceBundle().getString("general.label.wednesday"));
		working.put("5", Application.getInstance().getResourceBundle().getString("general.label.thursday"));
		working.put("6", Application.getInstance().getResourceBundle().getString("general.label.friday"));
		projectWorkingDays.setLeftValues(notWorking);
		projectWorkingDays.setRightValues(working);
		projectFiles.init();
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        refresh();
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward;
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
            forward = new Forward(FORWARD_CANCEL);
        else
            forward = super.actionPerformed(event);
        return forward;
    }

    public Forward onSubmit(Event event)
    {
        Forward forward =  super.onSubmit(event);
        String selected = (String) projectCategory.getSelectedOptions().keySet().iterator().next();
        if(DEFAULT_CATEGORY.equals(selected) && (newCategory.getValue() == null || "".equals(newCategory.getValue())))
        {
            setInvalid(true);
            projectCategory.setInvalid(true);
            newCategory.setInvalid(true);
        }
        return forward;
    }

    protected void refresh()
    {
        validName.setText(Application.getInstance().getResourceBundle().getString("project.message.enterProjectName"));
        validValue.setText(Application.getInstance().getResourceBundle().getString("project.message.valueNumeric"));
        //Refreshing category options
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Collection list = worms.getProjectCategories();
            SequencedHashMap map = new SequencedHashMap();
            map.put(DEFAULT_CATEGORY, Application.getInstance().getResourceBundle().getString("project.label.selectACategory"));
            for (Iterator i = list.iterator(); i.hasNext();)
            {
                String category = (String) i.next();
                map.put(category, category);
            }
            projectCategory.setOptionMap(map);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        //Populating available users
        try
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection users = service.getUsersByPermission(Application.getInstance().getProperty(WormsHandler.PROPERTY_WORMS_USER), Boolean.TRUE, "firstName", false, 0, -1);
            Map options = new SequencedHashMap();
            for(Iterator i = users.iterator(); i.hasNext();)
            {
                User user = (User) i.next();
                options.put(user.getId(), user.getProperty("firstName").toString() + " " + user.getProperty("lastName").toString());
            }
            owner.setOptionMap(options);
        }
        catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    protected boolean isNameExists(String projectName, String projectId)
    {
        boolean exists = true;
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        DaoQuery query = new DaoQuery();
        query.addProperty(new OperatorEquals("projectName", projectName, DaoOperator.OPERATOR_AND));
        if(!(projectId == null || "".equals(projectId)))
            query.addProperty(new OperatorEquals("worms_project.projectId", projectId, DaoOperator.OPERATOR_NAN));
        try
        {
            Collection list = worms.getProjects(query, 0, 1, null, false);
            if(list.size() <= 0)
                exists = false;
        }
        catch(WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return exists;
    }

    protected Project generateProject(String projectId)
    {
        Project project = new Project();
        if(projectId == null || "".equals(projectId))
            project.setProjectId(UuidGenerator.getInstance().getUuid());
        else
            project.setProjectId(projectId);
        project.setProjectName(projectName.getValue().toString());
        project.setProjectDescription(projectDescription.getValue().toString());
        if(!(newCategory.getValue() == null || "".equals(newCategory.getValue())))
            project.setProjectCategory(newCategory.getValue().toString());
        else
            project.setProjectCategory(projectCategory.getSelectedOptions().keySet().iterator().next().toString());
        project.setProjectValue(Double.parseDouble(projectValue.getValue().toString()));
        project.setProjectCurrencyType(projectCurrencyType.getSelectedOptions().keySet().iterator().next().toString());
        project.setOwnerId(owner.getSelectedOptions().keySet().iterator().next().toString());
        
        //New
        project.setClientName(client.getValue().toString());
		if(archived.isChecked())
			project.setArchived(true);
		else
			project.setArchived(false);
		project.setProjectSummary(projectSummary.getValue().toString());
		Map map = projectWorkingDays.getRightValues();
		String working = "";
		for (Iterator i = map.keySet().iterator(); i.hasNext();)
		{
			String day = (String) i.next();
			if(working.length() != 0)
				working += ",";
			working += day;
		}
		project.setProjectWorkingDays(working);
		//-- Setting Associated Documents
		Collection content = new ArrayList();
		if(projectFiles.getIds().length > 0)
		{
			ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
			try
			{
				content = publisher.viewList(projectFiles.getIds(), new String[] {Document.class.getName()}, null, null, Boolean.FALSE,
					null, false, 0, -1, null, getWidgetManager().getUser().getId());
			}
			catch (ContentException e)
			{
				Log.getLog(getClass()).error("Error while retrieving documents", e);
			}
		}
		project.setFiles(content);
		
		
		Collection projectMemberList = new ArrayList();
        String[] projectMembersIds = projectMembers.getIds();
        if (projectMembersIds != null || projectMembersIds.length>0) {
		for (int i=0; i<projectMembersIds.length; i++) {
            try {
                String uid = projectMembersIds[i];
                // boolean included = false;
                if(map.get(uid)==null){
                    User tmpUser = UserUtil.getUser(uid);
                    ProjectMember pm = new ProjectMember();
                    pm.setProjectId(project.getProjectId());
                    pm.setMemberId(tmpUser.getId());
                    pm.setFirstName((String)tmpUser.getProperty("firstName"));
                    pm.setLastName((String)tmpUser.getProperty("lastName"));
                    
                    projectMemberList.add(pm);
                } else {
                	ProjectMember pm = new ProjectMember();
                	pm.setProjectId(project.getProjectId());
                	pm.setMemberId((String)map.get(uid));
                	pm.setFirstName("");
                    pm.setLastName("");
                    projectMemberList.add(pm);
                }
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
		}		
        }
        project.setMembers(projectMemberList);
        return project;
    }

    /* Getter and Setter */
    public SelectBox getOwner()
    {
        return owner;
    }

    public void setOwner(SelectBox owner)
    {
        this.owner = owner;
    }

    public TextBox getProjectDescription()
    {
        return projectDescription;
    }

    public void setProjectDescription(TextBox projectDescription)
    {
        this.projectDescription = projectDescription;
    }

    public TextField getProjectName()
    {
        return projectName;
    }

    public void setProjectName(TextField projectName)
    {
        this.projectName = projectName;
    }

    public TextField getProjectValue()
    {
        return projectValue;
    }

    public void setProjectValue(TextField projectValue)
    {
        this.projectValue = projectValue;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public TextField getNewCategory()
    {
        return newCategory;
    }

    public void setNewCategory(TextField newCategory)
    {
        this.newCategory = newCategory;
    }

    public SelectBox getProjectCategory()
	{
        return projectCategory;
    }

    public void setProjectCategory(SelectBox projectCategory)
	{
        this.projectCategory = projectCategory;
    }

	public CheckBox getArchived()
	{
		return archived;
	}

	public void setArchived(CheckBox archived)
	{
		this.archived = archived;
	}

	public ComboSelectBox getProjectWorkingDays()
	{
		return projectWorkingDays;
	}

	public void setProjectWorkingDays(ComboSelectBox projectWorkingDays)
	{
		this.projectWorkingDays = projectWorkingDays;
	}

	public TextBox getProjectSummary()
	{
		return projectSummary;
	}

	public void setProjectSummary(TextBox projectSummary)
	{
		this.projectSummary = projectSummary;
	}

	public DocumentPopupSelectBox getProjectFiles()
	{
		return projectFiles;
	}

	public void setProjectFiles(DocumentPopupSelectBox projectFiles)
	{
		this.projectFiles = projectFiles;
	}

    public void setProjectCurrencyType(SelectBox projectCurrencyType) {
        this.projectCurrencyType=projectCurrencyType;
    }

    public SelectBox getProjectCurrencyType() {
        return projectCurrencyType;
    }

	public boolean isEditMode() {
		return false;
	}
	public CalendarUsersSelectBox getProjectMembers() {
		return projectMembers;
	}

	public void setProjectMembers(CalendarUsersSelectBox projectMembers) {
		this.projectMembers = projectMembers;
	}
		
}
