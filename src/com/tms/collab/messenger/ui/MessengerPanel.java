package com.tms.collab.messenger.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.presence.PresenceService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import com.tms.collab.messenger.MessageModule;

public class MessengerPanel extends LightWeightWidget {
	public String getDefaultTemplate() {
		return "messenger/messengerPopOutPanel";
	}
}
