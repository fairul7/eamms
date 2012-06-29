package com.tms.tmsPIMSync.ui;

import kacang.stdui.*;
import kacang.services.security.Profileable;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.services.storage.StorageFile;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.Application;
import kacang.util.Log;
import com.tms.mugshot.model.MugshotModule;
import com.tms.mugshot.model.Mugshot;
import com.tms.tmsPIMSync.model.PIMSyncModule;
import com.tms.tmsPIMSync.model.PIMSyncPrincipal;
import com.tms.tmsPIMSync.model.PIMSyncDevice;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Manage SyncML configuration within user profile form
 *
 */
public class SyncProfile extends Form implements Profileable {
    public static final String DEFAULT_NAME = "syncProfile";
    public static final String DEFAULT_TEMPLATE = "syncProfile/profile";

    protected User user;
    protected CheckBox chkOutlook;
    protected CheckBox chkWindows;
    protected CheckBox chkSymbian;
    protected TextField winDeviceId;
    protected TextField symDeviceId;


    public String getProfileableLabel() {
        return Application.getInstance().getMessage("tmsPIMSync.defaultLabel");
    }

    public String getProfileableName() {
        return DEFAULT_NAME;
    }

    public Widget getWidget() {
        return this;
    }


    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public void init(User user) {
        this.user = user;
        Application app = Application.getInstance();
        chkOutlook = new CheckBox("chkOutlook", app.getMessage("tmsPIMSync.label.device.outlook", "Outlook"), false);
        chkWindows = new CheckBox("chkWindows", app.getMessage("tmsPIMSync.label.device.windows", "Windows Mobile"), false);
        chkSymbian = new CheckBox("chkSymbian", app.getMessage("tmsPIMSync.label.device.symbian", "Symbian Mobile"), false);
        winDeviceId = new TextField("winDeviceId");
        symDeviceId = new TextField("symDeviceId");

        addChild(chkOutlook);
        addChild(chkWindows);
        addChild(chkSymbian);
        addChild(winDeviceId);
        addChild(symDeviceId);

        PIMSyncModule pm = (PIMSyncModule) app.getModule(PIMSyncModule.class);
        Collection col = pm.getAllPrincipals(user.getUsername());

        for(Iterator itr=col.iterator(); itr.hasNext();){
            PIMSyncPrincipal sp = (PIMSyncPrincipal) itr.next();
            if(sp.getDevice().equals(PIMSyncModule.FUNAMBOL_DEVICE_OUTLOOK)){
                chkOutlook.setChecked(true);
            }else{
                PIMSyncDevice device = pm.getDevice(sp.getDevice());
                if(device != null){
                    if(PIMSyncModule.FUNAMBOL_DEVICE_SYMBIANMOBILE.equals(device.getType())){
                        chkSymbian.setChecked(true);
                        symDeviceId.setValue(device.getId());
                    }
                    else if(PIMSyncModule.FUNAMBOL_DEVICE_WINDOWSMOBILE.equals(device.getType())){
                        chkWindows.setChecked(true);
                        winDeviceId.setValue(device.getId());
                    }
                }
            }
        }
    }

    public void process(User user) {
        PIMSyncModule pm = (PIMSyncModule) Application.getInstance().getModule(PIMSyncModule.class);

        if(chkOutlook.isChecked()){
            if(!pm.principalExist(user.getUsername(), PIMSyncModule.FUNAMBOL_DEVICE_OUTLOOK)){
                pm.addPrincipal(user.getUsername(), PIMSyncModule.FUNAMBOL_DEVICE_OUTLOOK);
            }
        }else pm.deletePrincipal(user.getUsername(), PIMSyncModule.FUNAMBOL_DEVICE_OUTLOOK);

        if(chkSymbian.isChecked() && symDeviceId.getValue().toString().length() > 8){
            String symId = (String) symDeviceId.getValue();
            PIMSyncDevice device = new PIMSyncDevice();
            device.setId(symId);
            device.setDescription("Symbian OS Mobile");
            device.setType(PIMSyncModule.FUNAMBOL_DEVICE_SYMBIANMOBILE);
            pm.saveDevice(device);
            pm.addPrincipal(user.getUsername(), device.getId());
        }

        if(chkWindows.isChecked() && winDeviceId.getValue().toString().length() > 8){
            String winId = (String) winDeviceId.getValue();
            PIMSyncDevice device = new PIMSyncDevice();
            device.setId(winId);
            device.setDescription("Windows Mobile");
            device.setType(PIMSyncModule.FUNAMBOL_DEVICE_WINDOWSMOBILE);
            pm.saveDevice(device);
            pm.addPrincipal(user.getUsername(), device.getId());
        }

        if(!chkWindows.isChecked() || !chkSymbian.isChecked()){
            Collection col = pm.getAllPrincipals(user.getUsername());

            for(Iterator itr=col.iterator(); itr.hasNext();){
                PIMSyncPrincipal sp = (PIMSyncPrincipal) itr.next();
                if(!sp.getDevice().equals(PIMSyncModule.FUNAMBOL_DEVICE_OUTLOOK)){

                    PIMSyncDevice device = pm.getDevice(sp.getDevice());
                    if(device != null){
                        if(!chkSymbian.isChecked() && PIMSyncModule.FUNAMBOL_DEVICE_SYMBIANMOBILE.equals(device.getType())){
                            pm.deletePrincipal(sp.getUsername(), sp.getDevice());
                        }
                        else if(!chkWindows.isChecked() && PIMSyncModule.FUNAMBOL_DEVICE_WINDOWSMOBILE.equals(device.getType())){
                            pm.deletePrincipal(sp.getUsername(), sp.getDevice());
                        }
                    }
                }
            }
        }
    }


    public CheckBox getChkOutlook() {
        return chkOutlook;
    }


    public CheckBox getChkWindows() {
        return chkWindows;
    }

    public CheckBox getChkSymbian() {
        return chkSymbian;
    }

    public TextField getWinDeviceId() {
        return winDeviceId;
    }

    public TextField getSymDeviceId() {
        return symDeviceId;
    }
}
