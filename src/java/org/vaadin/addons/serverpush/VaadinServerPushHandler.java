
/*
 * Copyright (C) 2011 Elihu, LLC. All rights reserved.
 *
 * $Id$
 */

package org.vaadin.addons.serverpush;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.atmosphere.gwt.server.AtmosphereGwtHandler;
import org.atmosphere.gwt.server.GwtAtmosphereResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaadinServerPushHandler extends AtmosphereGwtHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int doComet(GwtAtmosphereResource resource) throws ServletException, IOException {
        resource.getBroadcaster().setID(Constants.GWT_COMET_BROADCASTER_ID);
        HttpSession session = resource.getAtmosphereResource().getRequest().getSession(false);
        if (session != null) {
            logger.debug("Got session with id: " + session.getId());
            logger.debug("Time attribute: " + session.getAttribute("time"));
        } else {
            logger.warn("No session");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Url: " + resource.getAtmosphereResource().getRequest().getRequestURL()
              + "?" + resource.getAtmosphereResource().getRequest().getQueryString());
        }
        return NO_TIMEOUT;
    }

    @Override
    public void cometTerminated(GwtAtmosphereResource cometResponse, boolean serverInitiated) {
        super.cometTerminated(cometResponse, serverInitiated);
        logger.debug("Comet disconnected");
    }
}
