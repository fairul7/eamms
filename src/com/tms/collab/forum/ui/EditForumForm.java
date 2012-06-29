package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.Forum;
import com.tms.collab.forum.model.ForumModule;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.ui.UsersSelectBox;
import kacang.services.security.Principal;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

public class EditForumForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "forum/editForumForm";

    private String forumId;
    private String userId;
    private Label forumName;
    private RichTextBox description;
    private CheckBox isPublic;
    private CheckBox isActive;
    private Button updateForum;
    private ResetButton reset;
    private Button cancel;
    private ComboSelectBox userGroup;
    private ComboSelectBox moderatorGroup;
    private UsersSelectBox userSelectBox;
    private UsersSelectBox userModeratorSelectBox;
    private Forum forum;
    private SelectBox categories;
    private TextField newCategory;
    private String message;

    protected UsersSelectBox users;

    public EditForumForm()
    {
    }

    public EditForumForm(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        removeChildren();

        users = new UsersSelectBox("users");

        setMethod("POST");
        userId = getWidgetManager().getUser().getId();
        forum = new Forum();
        try
        {
            if(getForumId() !=null && !getForumId().equals(""))
            {
                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                forum = forumModule.getForum(getForumId(), userId);

                forumName = new Label("forumName");
                forumName.setText(forum.getName());
                description = new RichTextBox("description");
                description.setValue(forum.getDescription());
                //description.setImageUrl(getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH)+"/cmsadmin/interactive/forum/frame.jsp?id="+forum.getForumId());
                isPublic = new CheckBox("isPublic");
                isPublic.setText(Application.getInstance().getMessage("forum.label.public","Public"));
                isPublic.setChecked(forum.getIsPublic());
                isActive = new CheckBox("isActive");
                isActive.setText(Application.getInstance().getMessage("forum.label.active","Active"));
                isActive.setChecked(forum.isActive());
                updateForum = new Button("updateForum");
                updateForum.setText(Application.getInstance().getMessage("forum.label.updateForum","Update Forum"));
                reset = new ResetButton("reset");
                reset.setText(Application.getInstance().getMessage("forum.label.reset","Reset"));
                cancel = new Button("cancel");
                cancel.setText(Application.getInstance().getMessage("forum.label.cancel","Cancel"));
                userGroup = new UserGroupComboSelect("userGroup");
                moderatorGroup = new UserGroupComboSelect("moderatorGroup");
                categories = new SelectBox("categories");
                categories.setSelectedOptions(new String[] {forum.getCategory()});
                newCategory = new TextField("newCategory");
                userSelectBox = new UsersSelectBox("userSelectBox");
                userModeratorSelectBox = new UsersSelectBox("userModeratorSelectBox");
                
                addChild(forumName);
                addChild(description);
                addChild(isPublic);
                addChild(isActive);
                addChild(updateForum);
                addChild(reset);
                addChild(cancel);
                addChild(userGroup);
                addChild(moderatorGroup);
                addChild(categories);
                addChild(newCategory);
                
                //get all available principals
                SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);               
                Collection groupList = security.getGroups(new DaoQuery(), 0, -1, "groupName", false);
                userGroup.init();
                
                Iterator itUserGroup = forum.getUserGroup().keySet().iterator();
                Map allUserGroup = userGroup.getLeftValues();


                while(itUserGroup.hasNext())
                {
                    allUserGroup.remove(itUserGroup.next());
                }
                
                userGroup.setLeftValues(allUserGroup);
                userGroup.setRightValues(forum.getUserGroup());

                moderatorGroup.init();
                Iterator itModeratorGroup = forum.getModeratorGroup().keySet().iterator();
                Map allModeratorGroup = moderatorGroup.getLeftValues();
                while(itModeratorGroup.hasNext())
                {
                    allModeratorGroup.remove(itModeratorGroup.next());
                }
                moderatorGroup.setLeftValues(allModeratorGroup);
                moderatorGroup.setRightValues(forum.getModeratorGroup());
                moderatorGroup.getRightSelect().addChild(new ValidatorNotEmpty("vModeratorGroup", Application.getInstance().getMessage("forum.label.moderatorsMustNotBeEmpty","Moderators must not be empty")));

               Collection usersCollection = forumModule.getSubscribers(forum.getId());
               Collection idList = new ArrayList();
                            
               if(forum.getUsers().size()>0){
               userSelectBox.setIds((String[])forum.getUsers().toArray(new String[0]));}
               if(forum.getModerators().size()>0){
               userModeratorSelectBox.setIds((String[])forum.getModerators().toArray(new String[0]));    
               }
               userSelectBox.init();
               addChild(userSelectBox);
               userModeratorSelectBox.init();
               
               addChild(userModeratorSelectBox);
               int counting= 0;
                for (Iterator iterator = usersCollection.iterator(); iterator.hasNext();)
                {  User user = (User) iterator.next();
                   idList.add(user.getId());

                }
                 String[] ids = (String[])idList.toArray(new String[0]);
                  ((UsersSelectBox)users).setIds(ids);
                 users.init();
               // users.setOptions(usersCollection, "username","userId");

                addChild(users);








                refreshCategories();







            }
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
                fwd = new Forward();
                fwd.setName(Form.CANCEL_FORM_ACTION);
            }
            else
            {
                WidgetManager widgetManager = WidgetManager.getWidgetManager(evt.getRequest());
                //forum.setName((String)forumName.getValue());
                forum.setDescription((String)description.getValue());
                forum.setModificationDate(new Date());
                forum.setActive(isActive.isChecked());
                forum.setIsPublic(isPublic.isChecked());
                forum.setUserGroup(userGroup.getRightValues());
                forum.setModeratorGroup(moderatorGroup.getRightValues());
                forum.setCategory(getCategory());
                String[] userSelectBoxArray = userSelectBox.getIds();
                String[] userModeratorSelectBoxArray = userModeratorSelectBox.getIds();
                Log log = Log.getLog(this.getClass());
                log.debug("~~~ Forum Description = " + forum.getDescription());
                log.debug("~~~ Forum Active = " + forum.isActive());
                log.debug("~~~ Forum Public = " + forum.getIsPublic());

                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                forumModule.editForum(forum, widgetManager.getUser().getId(), userSelectBoxArray, userModeratorSelectBoxArray);
                fwd = super.onValidate(evt);




                if (users.getIds().length > 0) {
                           Map matrix = new SequencedHashMap();
                           String[] ids = users.getIds();


                    forumModule.deleteSubscriptionByForum(forum.getId());

                    for(int count=0; count < ids.length ; count++){

                         forumModule.addSubscription( ids[count], forum.getId());


                    }







                }








            }
         }
        catch(Exception e)
        {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
        return fwd;
    }

    private void refreshCategories()
    {
        if(categories == null)
            categories = new SelectBox("categories");
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
            Log.getLog(EditForumForm.class).error(e);
        }
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
            setMessage(Application.getInstance().getMessage("forum.label.pleaseSpecifyACategory","Please Specify A Category"));
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

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
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

    public void setForumId(String forumId)
    {
        this.forumId = forumId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

   public void setForumName(Label forumName)
    {
        this.forumName = forumName;
    }

    public void setDescription(RichTextBox description)
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

    public void setUpdateForum(Button updateForum)
    {
        this.updateForum = updateForum;
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

    public String getForumId()
    {
        return forumId;
    }

    public String getUserId()
    {
        return userId;
    }

    public Label getForumName()
    {
        return forumName;
    }

    public RichTextBox getDescription()
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

    public Button getUpdateForum()
    {
        return updateForum;
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


    public UsersSelectBox getUsers() {
        return users;
    }

    public void setUsers(UsersSelectBox users) {
        this.users = users;
    }


}
