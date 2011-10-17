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

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

import org.vaadin.addons.serverpush.client.ui.VServerPush;

/**
 * Server push server-side widget
 */
@ClientWidget(VServerPush.class)
public class ServerPush extends AbstractComponent {

    public static final String COMET_NAME = "comet-1";

    private final String contextPath;

    /**
     * Constructor that uses the root context path and expects the server-push URL to be '/server-push'
     */
    public ServerPush() {
        this("");
    }

    /**
     * Constructor that uses the specified context path
     * @param contextPath path to the server-push URL as defined in WEB-INF/web.xml and META-INF/atmosphere.xml minus '/server-push'
     * Examples:
     *   - If path is '/foo/server-push' then comtext path is '/foo'
     *   - If path is '/foo/bar/baz/server-push' then context path is '/foo/bar/baz'
     *   - If path is '/server-push' then context path is ''
     *
     */
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

    /**
     * Pushes updates to client
     */
    public void push() {
        ServerPushBroadcasterFactory.getInstance().broadcastForApplication(getApplication());
    }
}
