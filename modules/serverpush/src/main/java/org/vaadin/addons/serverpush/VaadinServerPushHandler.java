/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.vaadin.addons.serverpush;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.gwt.server.AtmosphereGwtHandler;
import org.atmosphere.gwt.server.GwtAtmosphereResource;

/**
 * {@link AtmosphereGwtHandler} that configures the {@link ServerPushBroadcasterFactory} to use global or per-session {@link Broadcaster}
 * instances and retrieves a {@link Broadcaster} instances for the current {@link GwtAtmosphereResource}
 */
public class VaadinServerPushHandler extends AtmosphereGwtHandler {

    private static final String CHANNEL_PER_APPLICATION = "channelPerApplication";

    private final ServerPushBroadcasterFactory factory = ServerPushBroadcasterFactory.getInstance();
    private boolean channelPerApplication;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        String param = servletConfig.getInitParameter(CHANNEL_PER_APPLICATION);
        if (param != null)
            setChannelPerApplication(Boolean.parseBoolean(param));
    }

    public boolean isChannelPerApplication() {
        return this.channelPerApplication;
    }
    public void setChannelPerApplication(boolean channelPerApplication) {
        this.channelPerApplication = channelPerApplication;
        this.factory.setChannelPerApplication(this.channelPerApplication);
    }

    @Override
    public int doComet(GwtAtmosphereResource resource) throws ServletException, IOException {
        HttpSession session = resource.getAtmosphereResource().getRequest().getSession(false);
        if (session != null) {
            logger.debug("Got session with id: " + session.getId());
            logger.debug("Time attribute: " + session.getAttribute("time"));
            Broadcaster bc = this.factory.getBroadcaster(session.getId());
            if (bc != null)
                resource.getAtmosphereResource().setBroadcaster(bc);
        } else {
            logger.debug("No session");
        }
        logger.debug("Url: " + resource.getAtmosphereResource().getRequest().getRequestURL()
          + "?" + resource.getAtmosphereResource().getRequest().getQueryString());

        return NO_TIMEOUT;
    }

    @Override
    public void cometTerminated(GwtAtmosphereResource cometResponse, boolean serverInitiated) {
        super.cometTerminated(cometResponse, serverInitiated);
        logger.debug("Comet disconnected");
    }
}
