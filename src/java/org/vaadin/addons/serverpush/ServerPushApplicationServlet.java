
/*
 * Copyright (C) 2011 Elihu, LLC. All rights reserved.
 *
 * $Id$
 */

package org.vaadin.addons.serverpush;

import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import javax.servlet.http.HttpSession;

/**
 * {@link ApplicationServlet} that uses its own {@link WebApplicationContext} to provide {@link ServerPushCommunicationManager}
 * which pushes updates to all client on every repaint request
 */
public class ServerPushApplicationServlet extends ApplicationServlet {

    @Override
    protected WebApplicationContext getApplicationContext(HttpSession session) {
        WebApplicationContext cx = (WebApplicationContext)session.getAttribute(WebApplicationContext.class.getName());
        if (cx == null) {
            cx = new ServerPushWebApplicationContext(session);
            session.setAttribute(WebApplicationContext.class.getName(), cx);
        }
        return cx;
    }
}
