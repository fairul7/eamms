package com.tms.cms.tdk;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import com.tms.cms.mobile.model.MobileChannel;
import com.tms.cms.mobile.model.MobileModule;

public class DisplayMobileChannel extends LightWeightWidget {

    private String id;
    private MobileChannel channel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MobileChannel getChannel() {
        return channel;
    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayMobileChannel";
    }

    public void onRequest(Event evt) {

        try {
            // get selected channel
            MobileModule mod = (MobileModule)Application.getInstance().getModule(MobileModule.class);
            channel = mod.getChannel(getId());
        } catch (DataObjectNotFoundException e) {
        } catch (Exception e) {
        }

        // if none, get default channel

    }

}
