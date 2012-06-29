package com.tms.hr.competency.ui;

import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;
import com.tms.hr.competency.UserCompetencies;
import kacang.Application;
import kacang.services.security.Profileable;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class UserCompetencyForm extends Form implements Profileable
{
//    public static final String DEFAULT_LABEL = Application.getInstance().getMessage("competencies.title","Competencies");
    public static final String DEFAULT_NAME = "userCompetencyForm";
    public static final String DEFAULT_DELIMITER = ",";
    public static final String DEFAULT_TEMPLATE = "competency/userCompetencyForm";

    public static final String FORWARD_SUCCESSFUL = "forward_competency_success";
    public static final String FORWARD_FAILED = "forward_competency_fail";
    public static final String FORWARD_ADD = "forward_competency_add";
    public static final String FORWARD_DELETE = "forward_competency_delete";

    public static final String PREFIX_SELECTION = "sel";
    public static final String PREFIX_CHECK = "chk";

    protected Button add;
    protected Button delete;

    protected User user;
    protected Map levelMap;
    protected Map competencies;

    public void init()
    {
        setMethod("POST");
        
        if(isModuleConfigured())
        {
            levelMap = new SequencedHashMap();
            String levels = Application.getInstance().getProperty(Competency.PROPERTY_COMPETENCY_LEVELS);
            if(!(levels == null || "".equals(levels)))
            {
                StringTokenizer tokenizer = new StringTokenizer(levels, Competency.DEFAULT_DELIMITER);
                while(tokenizer.hasMoreTokens())
                {
                    String level = tokenizer.nextToken();
                    levelMap.put(level, level);
                }
            }
            String addCompetency = Application.getInstance().getMessage("competencies.label.addCompetency","Add Competency");
            String deleteCompetency = Application.getInstance().getMessage("competencies.label.deleteCompetency","Delete");
            add = new Button("add", addCompetency);
            delete = new Button("delete", deleteCompetency);
            addChild(add);
            addChild(delete);
        }
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = super.onSubmit(event);
        if (isModuleConfigured())
        {
            if(add.getAbsoluteName().equals(findButtonClicked(event)))
                forward =  new Forward(FORWARD_ADD);
            else if(delete.getAbsoluteName().equals(findButtonClicked(event)))
            {
                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                User user = getWidgetManager().getUser();
                for(Iterator i = competencies.keySet().iterator(); i.hasNext();)
                {
                    Competency competency = (Competency) i.next();
                    if(childMap.containsKey(PREFIX_SELECTION + competency.getCompetencyId()))
                    {
                        CheckBox checkBox = (CheckBox) childMap.get(PREFIX_CHECK + competency.getCompetencyId());
                        if(checkBox.isChecked())
                        {
                            try
                            {
                                handler.deleteUserCompetency(user.getId(), competency.getCompetencyId());
                            }
                            catch (CompetencyException e)
                            {
                                Log.getLog(getClass()).error(e.getMessage(), e);
                            }
                        }
                    }
                }
                refresh();
                forward =  new Forward(FORWARD_DELETE);
            }
        }
        return forward;
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public void process(User user)
    {
        if (isModuleConfigured())
        {
            try
            {
                SequencedHashMap newMap = new SequencedHashMap();
                for(Iterator i = competencies.keySet().iterator(); i.hasNext();)
                {
                    Competency competency = (Competency) i.next();
                    if(childMap.containsKey(PREFIX_SELECTION + competency.getCompetencyId()))
                    {
                        SelectBox selectBox = (SelectBox) childMap.get(PREFIX_SELECTION + competency.getCompetencyId());
                        String level = (String) selectBox.getSelectedOptions().keySet().iterator().next();
                        if(!(level == null || "".equals(level)))
                            newMap.put(competency, level);
                    }
                }
                competencies = newMap;
                UserCompetencies userCompetencies = new UserCompetencies();
                userCompetencies.setUser(user);
                userCompetencies.setCompetencies(competencies);
                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                handler.addUserCompetencies(userCompetencies);
            }
            catch(CompetencyException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }

    protected void refresh()
    {
        if (isModuleConfigured())
        {
            if(user != null)
            {
                try
                {
                    removeChildren();
                    CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                    competencies = handler.getUserCompetencies(user.getId()).getCompetencies();
                    for(Iterator i = competencies.keySet().iterator(); i.hasNext();)
                    {
                        Competency competency = (Competency) i.next();
                        SelectBox selectBox = new SelectBox(PREFIX_SELECTION + competency.getCompetencyId());
                        selectBox.setOptionMap(levelMap);
                        selectBox.setSelectedOptions(new String[] {(String) competencies.get(competency)});
                        CheckBox checkBox = new CheckBox(PREFIX_CHECK + competency.getCompetencyId());
                        addChild(selectBox);
                        addChild(checkBox);
                    }
                }
                catch (CompetencyException e)
                {
                    Log.getLog(getClass()).error(e.getMessage(), e);
                }
            }
            addChild(add);
            addChild(delete);
        }
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public Widget getWidget()
    {
        return this;
    }

    public String getProfileableLabel()
    {
        return Application.getInstance().getMessage("competencies.title","Competencies");
    }

    public String getProfileableName()
    {
        return DEFAULT_NAME;
    }

    public void init(User user)
    {
        this.user = user;
    }

    protected boolean isModuleConfigured()
    {
        CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
        return (handler!=null);
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

    public Button getDelete()
    {
        return delete;
    }

    public void setDelete(Button delete)
    {
        this.delete = delete;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Map getCompetencies()
    {
        return competencies;
    }

    public void setCompetencies(Map competencies)
    {
        this.competencies = competencies;
    }
}
