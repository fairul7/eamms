package com.tms.cms.core.ui;

import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentUtil;

public class ContentSelectBox extends SelectBox {

    private String id;

    public ContentSelectBox() {
    	
    	try {
            Class[] classes;
            Map classMap = new SequencedHashMap();
            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);

            // get content object
            if (getId() != null) {
            	//Class clazz = ContentUtil.getModuleClassFromKey(getId());
                String className = ContentUtil.getClassNameFromKey(getId());
                Class clazz = Class.forName(className);
                classes = cm.getAllowedClasses(clazz);
            }
            else {
            	classes = cm.getAllowedClasses(null);
            }
            if (classes != null && classes.length > 0) {
/*
                Collection selected = new ArrayList();
                selected.add(classes[0].getName());
                setValue(selected);
*/
                classMap.put("", "--- "+application.getMessage("general.label.pleaseSelect", "Please Select")+" ---");
            }
            
            // get allowed classes
            for(int i=0; i<classes.length; i++) {
                String label = application.getMessage("cms.label_" + classes[i].getName(), classes[i].getName());
                classMap.put(classes[i].getName(), label);
            }
            
            setOptionMap(classMap);
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public ContentSelectBox(String name) {
        super(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void onRequest(Event evt) {
    	
        try {
            Class[] classes;
            Map classMap = new SequencedHashMap();
            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);

            // get content object
            if (getId() != null) {
            	//Class clazz = ContentUtil.getModuleClassFromKey(getId());
                String className = ContentUtil.getClassNameFromKey(getId());
                Class clazz = Class.forName(className);
                classes = cm.getAllowedClasses(clazz);
            }
            else {
            	classes = cm.getAllowedClasses(null);
            }
            if (classes != null && classes.length > 0) {
/*
                Collection selected = new ArrayList();
                selected.add(classes[0].getName());
                setValue(selected);
*/
                classMap.put("", "--- "+application.getMessage("general.label.pleaseSelect", "Please Select")+" ---");
            }
            
            // get allowed classes
            for(int i=0; i<classes.length; i++) {
                String label = application.getMessage("cms.label_" + classes[i].getName(), classes[i].getName());
                classMap.put(classes[i].getName(), label);
            }
            

            setOptionMap(classMap);
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
