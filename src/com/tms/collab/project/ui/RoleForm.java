package com.tms.collab.project.ui;

import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;
import com.tms.hr.competency.ui.CompetencySelectBox;
import com.tms.collab.project.Role;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.Collection;

public abstract class RoleForm extends Form
{
    public static final String FORWARD_SUCCESSFUL = "forward.worms.successful";
    public static final String FORWARD_FAILED = "foward.worms.failed";
    public static final String FORWARD_CANCEL = "forward.worms.cancel";

    protected TextField roleName;
    protected TextBox roleDescription;
    protected CompetencySelectBox roleCompetencies;
    protected UsersSelectBox roleUsers;
    protected Button submit;
    protected Button cancel;

    protected ValidatorNotEmpty validName;

    protected String roleId;
    protected String projectId;
    protected boolean currentlyTemplate = false;

    public RoleForm()
    {
    }

    public RoleForm(String s)
    {
        super(s);
    }

    public void init()
    {
        if(!isKeyEmpty())
        {
            setColumns(2);
            removeChildren();
            setMethod("POST");

            roleName = new TextField("roleName");
            roleName.setSize("35");
            roleName.setMaxlength("100");
            roleDescription = new TextBox("roleDescription");
            roleDescription.setRows("7");
            roleDescription.setCols("35");
            roleCompetencies = new CompetencySelectBox("roleCompetencies");
            roleCompetencies.setSortable(false);
            if(!currentlyTemplate)
            {
                roleUsers = new UsersSelectBox("roleUsers");
                roleUsers.setSortable(false);
            }
            submit = new Button("submit", Application.getInstance().getMessage("com.tms.hr.competency.Competency.submit","Submit"));
            cancel = new Button("cancel", Application.getInstance().getMessage("com.tms.hr.competency.Competency.cancel","Cancel"));
            Panel buttonPanel = new Panel("buttonPanel");
            buttonPanel.addChild(submit);
            buttonPanel.addChild(cancel);

            validName = new ValidatorNotEmpty("validName");
            roleName.addChild(validName);

            addChild(new Label("labelName", Application.getInstance().getResourceBundle().getString("project.label.name")+" *"));
            addChild(roleName);
            addChild(new Label("labelDescription", Application.getInstance().getResourceBundle().getString("project.label.description")));
            addChild(roleDescription);
            addChild(new Label("labelCompetencies", Application.getInstance().getResourceBundle().getString("project.label.competencies")));
            addChild(roleCompetencies);
            if(!currentlyTemplate)
            {
                addChild(new Label("labelUsers", Application.getInstance().getResourceBundle().getString("project.label.designatedPersonnel")));
                addChild(roleUsers);
            }
            addChild(new Label("labelButton", ""));
            addChild(buttonPanel);

            roleCompetencies.init();
            if(!currentlyTemplate)
                roleUsers.init();
        }
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_CANCEL);
            else
                forward = super.actionPerformed(event);
        }
        return forward;
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            forward = super.onSubmit(event);
            if(!isUniqueRole())
            {
                setInvalid(true);
                roleName.setInvalid(true);
                validName.setInvalid(true);
                validName.setText(Application.getInstance().getResourceBundle().getString("project.message.roleNameInUse"));
                forward= new Forward(FORWARD_FAILED);
            }
        }
        return forward;
    }

    protected Role generateRole()
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
        Role role = new Role();
        try
        {
            if(!(roleId == null || "".equals(roleId)))
                role.setRoleId(roleId);
            else
                role.setRoleId(UuidGenerator.getInstance().getUuid());
            role.setRoleName(roleName.getValue().toString());
            role.setRoleDescription(roleDescription.getValue().toString());
            role.setProjectId(projectId);
            //Populating competencies
            if(roleCompetencies.getIds().length > 0)
            {
                DaoQuery competencyQuery = new DaoQuery();
                competencyQuery.addProperty(new OperatorIn("competencyId", roleCompetencies.getIds(), DaoOperator.OPERATOR_AND));
                role.setCompetencies(handler.getCompetencies(competencyQuery, 0, -1, null, false));
            }
            //Populating users
            if(!currentlyTemplate)
            {
                if(roleUsers.getIds().length > 0)
                {
                    DaoQuery userQuery = new DaoQuery();
                    userQuery.addProperty(new OperatorIn("id", roleUsers.getIds(), DaoOperator.OPERATOR_AND));
                    role.setPersonnel(service.getUsers(userQuery, 0, -1, null, false));
                }
            }
        }
        catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        catch(CompetencyException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return role;
    }

    protected boolean isUniqueRole()
    {
        boolean unique = false;
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("roleName", roleName.getValue().toString(), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("projectId", projectId, DaoOperator.OPERATOR_AND));
            if(!(roleId == null || "".equals(roleId)))
                query.addProperty(new OperatorEquals("roleId", roleId, DaoOperator.OPERATOR_NAN));
            Collection list = worms.getRoles(query, 0, 1, null, false, false);
            if(list.size() <= 0)
                unique = true;
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return unique;
    }

    protected void refresh()
    {
        if(!isKeyEmpty())
        {
            validName.setInvalid(false);
            validName.setText("Please Enter The Role Name");
        }
    }

    protected boolean isKeyEmpty()
    {
        return false;
    }

    /* Getters and Setters */
    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public CompetencySelectBox getRoleCompetencies()
    {
        return roleCompetencies;
    }

    public void setRoleCompetencies(CompetencySelectBox roleCompetencies)
    {
        this.roleCompetencies = roleCompetencies;
    }

    public TextBox getRoleDescription()
    {
        return roleDescription;
    }

    public void setRoleDescription(TextBox roleDescription)
    {
        this.roleDescription = roleDescription;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public TextField getRoleName()
    {
        return roleName;
    }

    public void setRoleName(TextField roleName)
    {
        this.roleName = roleName;
    }

    public UsersSelectBox getRoleUsers()
    {
        return roleUsers;
    }

    public void setRoleUsers(UsersSelectBox roleUsers)
    {
        this.roleUsers = roleUsers;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public ValidatorNotEmpty getValidName()
    {
        return validName;
    }

    public void setValidName(ValidatorNotEmpty validName)
    {
        this.validName = validName;
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public boolean isCurrentlyTemplate()
    {
        return currentlyTemplate;
    }

    public void setCurrentlyTemplate(boolean currentlyTemplate)
    {
        this.currentlyTemplate = currentlyTemplate;
        init();
    }

    public void setCurrentTemplate(String currentlyTemplate)
    {
        if("true".equals(currentlyTemplate))
            setCurrentlyTemplate(true);
        else
            setCurrentlyTemplate(false);
    }
}
