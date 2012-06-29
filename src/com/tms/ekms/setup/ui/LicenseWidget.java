package com.tms.ekms.setup.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import com.tms.ekms.setup.model.SetupException;
import com.tms.util.license.TmsLicense;

import java.io.IOException;

public class LicenseWidget extends LightWeightWidget {

    private String forward = "../cms/index.jsp";

    private static final String PARAM_LICENSE = "license";
    private static final String PARAM_ACTIVATION = "activation";

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public boolean isValid(Event evt) {
        TmsLicense.flush();

        if(evt.getRequest().getParameter(PARAM_LICENSE)!=null) {
            return true;
        } else {
            return false;
        }
    }

    public void onValidate(Event evt) {
        String license = evt.getRequest().getParameter(PARAM_LICENSE);
        String activationKey = evt.getRequest().getParameter(PARAM_ACTIVATION);

        boolean isValid = false;
        try {
            // update license if need to
            if(license!=null && license.trim().length()>0) {
                TmsLicense.saveLicense(license);
            }

            // update acvtivation key if need to
            if(activationKey!=null && activationKey.length()==32) {
                TmsLicense.saveActivationKey(activationKey);
            }

            isValid = TmsLicense.checkLicense();

        } catch (SetupException e) {
            throw new RuntimeException("Unable to update license " + e.toString());
        }

        // let user redirect manually.
        if (isValid) {
            try {
                String fwd = (getForward() == null) ? evt.getRequest().getContextPath() :  getForward();
                evt.getResponse().sendRedirect(fwd);
            } catch (IOException e) {
            }
        }

    }

}
