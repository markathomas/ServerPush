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

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.atmosphere.gwt.server.AtmosphereGwtHandler;

/**
 * Factory class for obtaining {@link Broadcaster}s and for broadcasting updates
 * @author Mark Thomas
 * @version 1.0.5
 * @since 1.0.5
 */
public final class ServerPushBroadcasterFactory {

    private static final ServerPushBroadcasterFactory INSTANCE = new ServerPushBroadcasterFactory();

    private boolean channelPerApplication;

    private ServerPushBroadcasterFactory() {
    }

    /**
     * Returns singleton instance of factory
     * @return singleton instance of factory
     */
    public static ServerPushBroadcasterFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Retrieves existing {@link Broadcaster} for the specified session id if it exists; otherwise, creates a new instance.  Behavior is further
     * modified by whether or not the factory is configured to return a unique {@link Broadcaster} per session or a global {@link Broadcaster}
     * @param sessionID HttpSession id
     * @return {@link Broadcaster} instance per session or global
     */
    public Broadcaster getBroadcaster(String sessionID) {
        String uid = AtmosphereGwtHandler.GWT_BROADCASTER_ID + (this.channelPerApplication ? "-" + sessionID : "");
        return BroadcasterFactory.getDefault().lookup(DefaultBroadcaster.class, uid, true);
    }

    /**
     * Broadcasts empty array as JSON to trigger client update
     * @param application {@link Application} instance
     */
    public void broadcastForApplication(Application application) {
        this.broadcastForApplication(application, "[]");
    }

    /**
     * Broadcasts specified JSON data to trigger client update
     * @param application {@link Application} instance
     * @param json JSON-encoded String
     */
    public void broadcastForApplication(Application application, String json) {
        if (application == null || !application.isRunning())
            return;
        final String sessionID = ((WebApplicationContext)application.getContext()).getHttpSession().getId();
        final Broadcaster bc = this.getBroadcaster(sessionID);
        if (bc != null)
            bc.broadcast(json);
    }

    /**
     * Sets the factory to retrieve {@link Broadcaster} instances on a global or per session level
     * @param channelPerApplication
     */
    public void setChannelPerApplication(boolean channelPerApplication) {
        this.channelPerApplication = channelPerApplication;
    }
}
