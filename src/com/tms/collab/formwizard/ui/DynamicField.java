package com.tms.collab.formwizard.ui;

import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.collab.formwizard.engine.StructureEngine;
import com.tms.collab.formwizard.engine.FormLayout;
import com.tms.collab.formwizard.engine.WidgetEngine;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class DynamicField extends Form {
    private String formId;
    private FormLayout layout;
    private Form widgetForm;


    protected WidgetEngine engine;

    public DynamicField() {
    }

    public void onRequest(Event event) {
        initEngine();
    }

    public void initEngine() {
        InputStream stream = null;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        try {
            stream = module.getFormXML(getFormId());
            StructureEngine engine = new StructureEngine();
            engine.setXml(stream);
            engine.setData(getData());
            layout =  engine.retriveStructure();
            parseLayout(getLayout().getFieldList());
            buildWidget();
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {
            }
        }
    }
    
    public void initEngine(String type) {
        InputStream stream = null;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        try {
            stream = module.getFormXML(getFormId());
            int aBuffSize = 1123123;    // useful int to define a buffer size
            byte buff[] = new byte[aBuffSize]; // Useful array for copying data
            OutputStream xOutputStream = new ByteArrayOutputStream(aBuffSize); 
            int k=0;

				while ( (k=stream.read(buff) ) != -1) 
				 xOutputStream.write(buff,0,k);
				String data=xOutputStream.toString();


            data=data.replaceAll("hidden=\"1\"","hidden=\"3\"");
			//stream = new java.io.ByteArrayInputStream(data.getBytes("UTF-8"));
			stream = new java.io.ByteArrayInputStream(data.getBytes());
			
            StructureEngine engine = new StructureEngine();
            engine.setXml(stream);
            engine.setData(getData());
            layout =  engine.retriveStructure();
            parseLayout(getLayout().getFieldList());
            buildWidget();
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {
            }
        }
    }

    public void parseLayout(List fieldList) {

    }

    public Map getData() throws FormDaoException {
        return null;
    }

    public void buildWidget(){
        removeChildren();
        engine = new WidgetEngine();
        engine.setLayout(layout);        
        addChild(engine.retriveWidget());
        setMethod("POST");
        widgetForm = engine.getForm();
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public FormLayout getLayout() {
        return layout;
    }

    public void setLayout(FormLayout layout) {
        this.layout = layout;
    }

    public Form getWidgetForm() {
        return widgetForm;
    }
}
