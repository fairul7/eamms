package com.tms.fms.facility.ui;

import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyException;
import com.tms.hr.competency.CompetencyHandler;
import com.tms.hr.competency.UserCompetencies;
import kacang.Application;
import kacang.services.security.Profileable;
import kacang.services.security.SecurityService;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class UserCompetencyForm extends Form implements Profileable
{
//    public static final String DEFAULT_LABEL = Application.getInstance().getMessage("competencies.title","Competencies");
    public static final String DEFAULT_NAME = "userCompetencyForm";
    public static final String DEFAULT_DELIMITER = ",";
    public static final String DEFAULT_TEMPLATE = "/fms/engineering/userCompetencyForm";

    public static final String FORWARD_SUCCESSFUL = "forward_competency_success";
    public static final String FORWARD_FAILED = "forward_competency_fail";
    public static final String FORWARD_ADD = "forward_competency_add";
    public static final String FORWARD_DELETE = "forward_competency_delete";

    public static final String PREFIX_SELECTION = "sel";
    public static final String PREFIX_CHECK = "chk";

    protected Button add;
    protected Button delete;
    protected Button submit;
    protected Button cancel;
    private UnitUserSelectBox userSelectBox;

    protected User user;
    protected Map levelMap;
    protected Map competencies=new HashMap() ;

    public void init(){
    	initForm();
    }
    public void initForm(){
        setMethod("POST");
        competencies=new HashMap();
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
            String addCompetency = Application.getInstance().getMessage("fms.facility.label.selectCompetency","Select Competency");
            String deleteCompetency = Application.getInstance().getMessage("competencies.label.deleteCompetency","Delete");
            userSelectBox=new UnitUserSelectBox("userSelectBox");
            userSelectBox.init();
            addChild(userSelectBox);
            add = new Button("add", addCompetency);
            delete = new Button("delete", deleteCompetency);
            submit = new Button("submit", Application.getInstance().getMessage("fms.facility.submit"));
    		cancel = new Button("cancel", Application.getInstance().getMessage("fms.facility.cancel"));
            addChild(add);
            addChild(delete);
            addChild(submit);
            addChild(cancel);
        }
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = super.onSubmit(event);
        if (findButtonClicked(event) != null && cancel.getAbsoluteName().equals(findButtonClicked(event))) {
			initForm();
			return new Forward(Form.CANCEL_FORM_ACTION, "manpowerListing.jsp", true);
		}else if(add.getAbsoluteName().equals(findButtonClicked(event)))
                forward =  new Forward(FORWARD_ADD);
            else if(delete.getAbsoluteName().equals(findButtonClicked(event)))
            {
                Map oldMap=new HashMap(competencies);
                for(Iterator i = oldMap.keySet().iterator(); i.hasNext();)
                {
                    String competencyId = (String)i.next();
                    if(childMap.containsKey(PREFIX_SELECTION + competencyId))
                    {
                        CheckBox checkBox = (CheckBox) childMap.get(PREFIX_CHECK + competencyId);
                        if(checkBox.isChecked())
                        {
                            try
                            {
                            	competencies.remove(competencyId);
                            }
                            catch (Exception e)
                            {
                                Log.getLog(getClass()).error(e.getMessage(), e);
                            }
                        }
                    }
                }
                refresh();
                forward =  new Forward(FORWARD_DELETE);
            }
        return forward;
    }

    public void onRequest(Event event)
    {
    	//init();
        refresh();
    }
    
    @Override
    public Forward onValidate(Event event) {
    	try{
    		if (findButtonClicked(event) != null && submit.getAbsoluteName().equals(findButtonClicked(event))) {
	    		Map map=(Map)userSelectBox.getSelectedOptions();
	    		if(map.size()==0){
	    			userSelectBox.setInvalid(true);
	    			this.setInvalid(true);
	    		}else{
	    			if(competencies.size()>0){
	    				SecurityService ss=(SecurityService)Application.getInstance().getService(SecurityService.class);
		    			for(Iterator itr=map.keySet().iterator();itr.hasNext();){
		    				String userId=(String)itr.next();
		    				User user=ss.getUser(userId);
		    				process(user);
		    			}
	    			}
	    			initForm();
	    			return new Forward("ADDED");
	    		}
    		}
    	}catch (Exception e) {
		}
    	return new Forward();
    }

    public void process(User user)
    {
        try{
            SequencedHashMap newMap = new SequencedHashMap();
            for(Iterator i = competencies.keySet().iterator(); i.hasNext();)
            {
                String competencyId=(String)i.next();
            	Competency competency = (Competency) competencies.get(competencyId);
                if(childMap.containsKey(PREFIX_SELECTION + competency.getCompetencyId()))
                {
                    SelectBox selectBox = (SelectBox) childMap.get(PREFIX_SELECTION + competency.getCompetencyId());
                    String level = (String) selectBox.getSelectedOptions().keySet().iterator().next();
                    if(!(level == null || "".equals(level)))
                        newMap.put(competency, level);
                }
            }
            //competencies = newMap;
            UserCompetencies userCompetencies = new UserCompetencies();
            userCompetencies.setUser(user);
            userCompetencies.setCompetencies(newMap);
            CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
            handler.addUserCompetencies(userCompetencies);
        }
        catch(CompetencyException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    protected void refresh()
    {
            try
            {
                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                //competencies = handler.getUserCompetencies(user.getId()).getCompetencies();
                for(Iterator i = competencies.keySet().iterator(); i.hasNext();)
                {
                	String key=(String) i.next();
                    Competency competency = (Competency)competencies.get(key);
                    SelectBox selectBox = new SelectBox(PREFIX_SELECTION + competency.getCompetencyId());
                    selectBox.setOptionMap(levelMap);
                    selectBox.setSelectedOption(competency.getCompetencyLevel());
                    CheckBox checkBox = new CheckBox(PREFIX_CHECK + competency.getCompetencyId());
                    addChild(selectBox);
                    addChild(checkBox);
                }
            }
            catch (Exception e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
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

	public UnitUserSelectBox getUserSelectBox() {
		return userSelectBox;
	}

	public void setUserSelectBox(UnitUserSelectBox userSelectBox) {
		this.userSelectBox = userSelectBox;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}
    
}
