package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.Forum;
import com.tms.collab.forum.model.ForumModule;
import com.tms.ekms.security.ui.UsersSelectBox;

import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 8, 2003
 * Time: 12:26:01 PM
 * To change this template use Options | File Templates.
 */
public class CreateForumForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "forum/createForumForm";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";
    public static final String FORWARD_CANCEL = "cancel";

    private TextField forumName;
    private TextBox description;
    private CheckBox isPublic;
    private CheckBox isActive;
    private Button createForum;
    private ResetButton reset;
    private Button cancel;
    private ValidatorNotEmpty validName;
    private ComboSelectBox userGroup;
    private ComboSelectBox moderatorGroup;
    private UsersSelectBox userSelectBox;
    private UsersSelectBox userModeratorSelectBox;
    private Forum forum;
    private SelectBox categories;
    private TextField newCategory;
    private String message;

    public CreateForumForm()
    {
    }

    public CreateForumForm(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        removeChildren();
        setMethod("POST");
        forum = new Forum();
        try
        {
			Application application = Application.getInstance();
            forumName = new TextField("forumName");
            description = new RichTextBox("description");
            isPublic = new CheckBox("isPublic");
            isPublic.setText(application.getMessage("general.label.public", "Public"));
            isPublic.setChecked(true);
            isActive = new CheckBox("isActive");
            isActive.setText(application.getMessage("general.label.active", "Active"));
            isActive.setChecked(true);
            createForum = new Button("createForum");
            createForum.setText(application.getMessage("forum.label.createForum", "Create Forum"));
            reset = new ResetButton("reset");
            reset.setText(application.getMessage("general.label.reset", "Reset"));
            cancel = new Button("cancel");
            cancel.setText(application.getMessage("general.label.cancel", "Cancel"));
            validName = new ValidatorNotEmpty("validName");
            forumName.addChild(validName);
            userGroup = new UserGroupComboSelect("userGroup");
            moderatorGroup = new UserGroupComboSelect("moderatorGroup");
            categories = new SelectBox("categories");
            newCategory = new TextField("newCategory");
            userSelectBox = new UsersSelectBox("userSelectBox");
            userModeratorSelectBox = new UsersSelectBox("userModeratorSelectBox");
            
            userSelectBox.init();
            addChild(userSelectBox);
            userModeratorSelectBox.init();            
            addChild(userModeratorSelectBox);
            addChild(forumName);
            addChild(description);
            addChild(isPublic);
            addChild(isActive);
            addChild(createForum);
            addChild(reset);
            addChild(cancel);
            addChild(userGroup);
            addChild(moderatorGroup);
            addChild(categories);
            addChild(newCategory);

            userGroup.init();
            moderatorGroup.init();
            moderatorGroup.getRightSelect().addChild(new ValidatorNotEmpty("vModeratorGroup", application.getMessage("general.error.moderators", "Moderators must not be empty")));
            refreshCategories();
        }
        catch (Exception e)
        {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
    }

    public Forward onValidate(Event evt)
    {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        try
        {
            if (buttonName != null && (buttonName.equals(reset.getAbsoluteName()) || buttonName.equals(cancel.getAbsoluteName())))
            {
                init();
                fwd = new Forward(Form.CANCEL_FORM_ACTION);
            }
            else
            {
                forum.setName(((String)forumName.getValue()).trim());
                forum.setDescription((String)description.getValue());
                forum.setOwnerId(getWidgetManager().getUser().getId());
                forum.setActive(isActive.isChecked());
                forum.setIsPublic(isPublic.isChecked());
                forum.setUserGroup(userGroup.getRightValues());
                forum.setModeratorGroup(moderatorGroup.getRightValues());
                forum.setCategory(getCategory());               
                String[] userSelectBoxArray = userSelectBox.getIds();
                String[] userModeratorSelectBoxArray = userModeratorSelectBox.getIds();
                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                forumModule.createForum(forum, userSelectBoxArray, userModeratorSelectBoxArray);
                fwd = new Forward(FORWARD_SUCCESS);
            }
         }
        catch(Exception e)
        {
            if(e.getMessage().endsWith(ForumModule.FORUM_NAME_EXIST))
                evt.getRequest().setAttribute("message", ForumModule.FORUM_NAME_EXIST);
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
        return fwd;
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = super.onSubmit(event);
        String category = getCategory();
        if("".equals(category))
        {
            categories.setInvalid(true);
            newCategory.setInvalid(true);
            this.setInvalid(true);
            setMessage("Please Specify A Category");
        }
        if(!isPublic.isChecked())
        {
            Map group = userGroup.getRightValues();
            Map user = userSelectBox.getSelectedOptions();
            if(group.isEmpty() && user.isEmpty())
            {
                userGroup.setInvalid(true);
                userSelectBox.setInvalid(true);
                this.setInvalid(true);
                setMessage("Please Specify User/User Groups for Non Public Forums");
            }
        }
        else
        {
            userGroup.setInvalid(false);
            userSelectBox.setInvalid(false);
        }
        return forward;
    }

    public void onRequest(Event event)
    {
        message = "";
        refreshCategories();
    }

    private void refreshCategories()
    {
        ForumModule module = (ForumModule) Application.getInstance().getModule(ForumModule.class);
        categories.addOption("-1", Application.getInstance().getMessage("forum.label.selectACategory","Select A Category"));
        try
        {
            Collection list = module.getCategories();
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                String category = (String) i.next();
                categories.addOption(category, category);
            }
        }
        catch(Exception e)
        {
            Log.getLog(CreateForumForm.class).error(e);
        }
    }

    private String getCategory()
    {
        String category = null;
        category = (String) newCategory.getValue();
        if(category == null || "".equals(category))
        {
            Map map = categories.getSelectedOptions();
            if(map.size() > 0)
                category = (String) map.keySet().iterator().next();
        }
        if(category == null || "-1".equals(category))
            category = "";
        return category;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public void setForumName(TextField forumName)
    {
        this.forumName = forumName;
    }

    public void setDescription(TextBox description)
    {
        this.description = description;
    }

    public void setIsPublic(CheckBox isPublic)
    {
        this.isPublic = isPublic;
    }

    public void setActive(CheckBox active)
    {
        isActive = active;
    }

    public void setCreateForum(Button createForum)
    {
        this.createForum = createForum;
    }

    public void setReset(ResetButton reset)
    {
        this.reset = reset;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public void setUserGroup(UserGroupComboSelect userGroup)
    {
        this.userGroup = userGroup;
    }

   public TextField getForumName()
    {
        return forumName;
    }

    public TextBox getDescription()
    {
        return description;
    }

    public CheckBox getIsPublic()
    {
        return isPublic;
    }

    public CheckBox getActive()
    {
        return isActive;
    }

    public Button getCreateForum()
    {
        return createForum;
    }

    public ResetButton getReset()
    {
        return reset;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public ComboSelectBox getUserGroup()
    {
        return userGroup;
    }

    public ComboSelectBox getModeratorGroup()
    {
        return moderatorGroup;
    }

    public void setModeratorGroup(ComboSelectBox moderatorGroup)
    {
        this.moderatorGroup = moderatorGroup;
    }

    public SelectBox getCategories()
    {
        return categories;
    }

    public void setCategories(SelectBox categories)
    {
        this.categories = categories;
    }

    public TextField getNewCategory()
    {
        return newCategory;
    }

    public void setNewCategory(TextField newCategory)
    {
        this.newCategory = newCategory;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
