
/*
 * Copyright (C) 2011 Elihu, LLC. All rights reserved.
 *
 * $Id$
 */

package org.vaadin.addons.serverpush;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.atmosphere.gwt.server.AtmosphereGwtHandler;
import org.atmosphere.gwt.server.GwtAtmosphereResource;

public class VaadinServerPushHandler extends AtmosphereGwtHandler {

    @Override
    public int doComet(GwtAtmosphereResource resource) throws ServletException, IOException {
        resource.getBroadcaster().setID(Constants.GWT_COMET_BROADCASTER_ID);
        HttpSession session = resource.getAtmosphereResource().getRequest().getSession(false);
        final ServletContext ctx = this.getServletContext();
        if (session != null) {
            ctx.log("Got session with id: " + session.getId());
            ctx.log("Time attribute: " + session.getAttribute("time"));
        } else {
            ctx.log("No session");
        }
        ctx.log("Url: " + resource.getAtmosphereResource().getRequest().getRequestURL()
          + "?" + resource.getAtmosphereResource().getRequest().getQueryString());
        return NO_TIMEOUT;
    }

    @Override
    public void cometTerminated(GwtAtmosphereResource cometResponse, boolean serverInitiated) {
        super.cometTerminated(cometResponse, serverInitiated);
        logger.debug("Comet disconnected");
    }
}
