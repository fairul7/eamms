package com.tms.portlet.portlets.webradio;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.Button;
import kacang.Application;
import kacang.util.Log;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.PortletException;
import com.tms.cms.portlet.ContentPortletPreference;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 19, 2004
 * Time: 11:57:36 AM
 * To change this template use Options | File Templates.
 */
public class WebRadioPreference extends Form
{
    public static final String FORWARD_SUCCESSFUL = "forward_successful";
    public static final String FORWARD_FAILED = "forward_failed";
    public static final String FORWARD_CANCEL = "forward_cancel";
    public static final String EVENT_TYPE_DELETE = "delete";
    public static final String DELETE_KEY = "stationId";
    public static final String DEFAULT_TEMPLATE = "portal/portlets/webradioPreference";

    private Entity entity;
    private String entityId;
    private Map stations;

    private TextField stationLabel;
    private TextField stationIp;
    private Button add;
    private Button cancel;

    public WebRadioPreference()
    {
        stations = new HashMap();
    }

    public WebRadioPreference(String s)
    {
        super(s);
        stations = new HashMap();
    }

    public void init()
    {
        stationLabel = new TextField("stationLabel");
        stationIp = new TextField("stationIp");
        add = new Button("add");
        add.setText(Application.getInstance().getMessage("portlet.label.addChannel","Add Channel"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));

        addChild(stationLabel);
        addChild(stationIp);
        addChild(add);
        addChild(cancel);
    }

    private void refresh()
    {
        if(entity != null)
        {
            PortletPreference preferenceStation = entity.getPreference(WebRadio.PREFERENCE_STATIONS);
            String preferenceValue = "";
            if(preferenceStation != null)
                preferenceValue = preferenceStation.getPreferenceValue();
            stations = WebRadio.getStations(preferenceValue);
        }
    }

    public void onRequest(Event event)
    {
        stationLabel.setInvalid(false);
        stationIp.setInvalid(false);
        refresh();
    }

    public Forward onValidate(Event event)
    {
        Forward forward = null;
        if(entity != null)
        {
            if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_CANCEL);
            else
            {
                if(EVENT_TYPE_DELETE.equals(event.getType()))
                {
                    if(event.getParameter(DELETE_KEY) != null)
                    {
                        stations.remove(event.getParameter(DELETE_KEY));
                        forward = savePreference(stations);
                    }
                }
                else
                {
                    if(!(stationLabel.getValue() == null || "".equals(stationLabel.getValue())))
                    {
                        if(!(stationIp.getValue() == null || "".equals(stationIp.getValue())))
                        {
                            stations.put(stationLabel.getValue(), stationIp.getValue());
                            stationLabel.setValue("");
                            stationIp.setValue("");
                            forward = savePreference(stations);
                        }
                        else
                        {
                            stationIp.setInvalid(true);
                            forward = new Forward(FORWARD_FAILED);
                        }
                    }
                    else
                    {
                        stationLabel.setInvalid(true);
                        forward = new Forward(FORWARD_FAILED);
                    }
                }
            }
        }
        return forward;
    }

    private String constructPreferenceString(Map stations)
    {
        String output = "";
        for(Iterator i = stations.keySet().iterator(); i.hasNext();)
        {
            String key = (String) i.next();
            output += key + WebRadio.WEB_RADIO_DELIMITER + stations.get(key) + ";";
        }
        return output;
    }

    private Forward savePreference(Map stations)
    {
        Forward forward = null;
        PortletPreference preferenceStations = null;
        if(entity.getPreference(WebRadio.PREFERENCE_STATIONS) == null)
        {
            preferenceStations = new PortletPreference();
            preferenceStations.setPreferenceName(WebRadio.PREFERENCE_STATIONS);
            preferenceStations.setPreferenceEditable(true);
            preferenceStations.setPreferenceValue("");
        }
        else
            preferenceStations = entity.getPreference(WebRadio.PREFERENCE_STATIONS);
        preferenceStations.setPreferenceValue(constructPreferenceString(stations));
        entity.getPreferences().put(WebRadio.PREFERENCE_STATIONS, preferenceStations);
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        try
        {
            handler.editEntity(entity);
            forward = new Forward(FORWARD_SUCCESSFUL);
        }
        catch (PortletException e)
        {
            Log.getLog(WebRadioPreference.class).error(e);
            forward = new Forward(FORWARD_FAILED);
        }
        return forward;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    public String getEntityId()
    {
        return entityId;
    }

    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        try
        {
            setEntity(handler.getEntity(entityId));
            init();
        }
        catch(Exception e)
        {
            Log.getLog(ContentPortletPreference.class);
        }
    }

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

    public TextField getStationIp()
    {
        return stationIp;
    }

    public void setStationIp(TextField stationIp)
    {
        this.stationIp = stationIp;
    }

    public TextField getStationLabel()
    {
        return stationLabel;
    }

    public void setStationLabel(TextField stationLabel)
    {
        this.stationLabel = stationLabel;
    }

    public Map getStations()
    {
        return stations;
    }

    public void setStations(Map stations)
    {
        this.stations = stations;
    }
}
