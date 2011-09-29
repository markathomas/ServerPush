
/*
 * Copyright (C) 2011 Elihu, LLC. All rights reserved.
 *
 * $Id$
 */

package org.vaadin.addons.serverpush;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.vaadin.addons.serverpush.client.ui.VServerPush;

@ClientWidget(VServerPush.class)
public class ServerPush extends AbstractComponent {

    public static final String COMET_NAME = "comet-1";

    private final String contextPath;

    public ServerPush() {
        this("");
    }

    public ServerPush(String contextPath) {
        if (contextPath == null)
            contextPath = "";
        this.contextPath = contextPath;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addAttribute(VServerPush.COMET, COMET_NAME);
        target.addAttribute(VServerPush.CONTEXT_PATH, this.contextPath);
    }

    public void push() {
        final Broadcaster bc = BroadcasterFactory.getDefault().lookup(DefaultBroadcaster.class,
          Constants.GWT_COMET_BROADCASTER_ID);
        if (bc != null)
            bc.broadcast(Constants.EMPTY_JSON);
    }
}
