
/*
 * Copyright (C) 2011 Elihu, LLC. All rights reserved.
 *
 * $Id$
 */

package org.vaadin.addons.serverpush;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import javax.servlet.http.HttpSession;

public class ServerPushWebApplicationContext extends WebApplicationContext {

    public ServerPushWebApplicationContext(HttpSession session) {
        super();
        this.session = session;
    }

    @Override
    public CommunicationManager getApplicationManager(Application application, AbstractApplicationServlet servlet) {
        CommunicationManager mgr = (CommunicationManager)this.applicationToAjaxAppMgrMap.get(application);

        if (mgr == null) {
            // Creates new manager
            mgr = new ServerPushCommunicationManager(application);
            this.applicationToAjaxAppMgrMap.put(application, mgr);
        }
        return mgr;
    }
}
