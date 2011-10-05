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

public final class ServerPushBroadcasterFactory {

    private static final ServerPushBroadcasterFactory INSTANCE = new ServerPushBroadcasterFactory();

    private boolean channelPerApplication;

    private ServerPushBroadcasterFactory() {
    }

    public static ServerPushBroadcasterFactory getInstance() {
        return INSTANCE;
    }

    public Broadcaster getBroadcaster(String sessionID) {
        String uid = AtmosphereGwtHandler.GWT_BROADCASTER_ID + (this.channelPerApplication ? "-" + sessionID : "");
        return BroadcasterFactory.getDefault().lookup(DefaultBroadcaster.class, uid, true);
    }

    public void broadcastForApplication(Application application) {
        this.broadcastForApplication(application, "[]");
    }
    public void broadcastForApplication(Application application, String json) {
        final String sessionID = ((WebApplicationContext)application.getContext()).getHttpSession().getId();
        final Broadcaster bc = this.getBroadcaster(sessionID);
        if (bc != null)
            bc.broadcast(json);
    }

    public void setChannelPerApplication(boolean channelPerApplication) {
        this.channelPerApplication = channelPerApplication;
    }
}
