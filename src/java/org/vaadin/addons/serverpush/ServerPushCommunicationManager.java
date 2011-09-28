
/*
 * Copyright (C) 2011 Elihu, LLC. All rights reserved.
 *
 * $Id$
 */

package org.vaadin.addons.serverpush;

import com.vaadin.Application;
import com.vaadin.terminal.Paintable;
import com.vaadin.terminal.gwt.server.CommunicationManager;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;

public class ServerPushCommunicationManager extends CommunicationManager {

    public ServerPushCommunicationManager(Application application) {
        super(application);
    }

    @Override
    public void repaintRequested(Paintable.RepaintRequestEvent event) {
        super.repaintRequested(event);
        final Broadcaster bc = BroadcasterFactory.getDefault().lookup(DefaultBroadcaster.class,
          Constants.GWT_COMET_BROADCASTER_ID);
        if (bc != null)
            bc.broadcast(Constants.EMPTY_JSON);
    }
}
