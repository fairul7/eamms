package com.tms.portlet.portlets.webradio;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.theme.ThemeManager;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 16, 2004
 * Time: 4:36:41 PM
 * To change this template use Options | File Templates.
 */
public class WebRadio extends Widget
{
    public static final String EVENT_STATION_SELECT = "stationSelect";
    public static final String PROPERTY_STATION = "stationIp";
    public static final String PREFERENCE_STATIONS = "stations";
    public static final String WEB_RADIO_DELIMITER = ":[station-label]:";
    public static final String DEFAULT_TEMPLATE = "portal/portlets/webradio";

    private Map stations;
    private String selectedStation;

    public WebRadio()
    {
    }

    public WebRadio(String s)
    {
        super(s);
    }

    public void init()
    {
        stations = new HashMap();
        selectedStation = "";
    }

    public void onRequest(Event event)
    {
        Entity entity = (Entity) event.getRequest().getAttribute(ThemeManager.LABEL_ENTITY);
        PortletPreference preferenceStation = entity.getPreference(PREFERENCE_STATIONS);
        String preferenceValue = "";
        if(preferenceStation != null)
            preferenceValue = preferenceStation.getPreferenceValue();
        stations = WebRadio.getStations(preferenceValue);
        super.onRequest(event);
    }

    public static Map getStations(String preferenceValue)
    {
        Map stations = new HashMap();
        StringTokenizer tokenizer = new StringTokenizer(preferenceValue, ";");
        while(tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();
            int position = token.indexOf(WEB_RADIO_DELIMITER);
            if(position != 0)
            {
                String stationId = token.substring(0, token.indexOf(WEB_RADIO_DELIMITER));
                String stationLabel = token.substring(token.indexOf(WEB_RADIO_DELIMITER) + WEB_RADIO_DELIMITER.length(), token.length());
                stations.put(stationId, stationLabel);
            }
        }
        return stations;
    }

    public Forward actionPerformed(Event event)
    {
        if(EVENT_STATION_SELECT.equals(event.getType()))
        {
            selectedStation = event.getParameter(PROPERTY_STATION);
        }
        return new Forward();
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public String getSelectedStation()
    {
        return selectedStation;
    }

    public void setSelectedStation(String selectedStation)
    {
        this.selectedStation = selectedStation;
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
