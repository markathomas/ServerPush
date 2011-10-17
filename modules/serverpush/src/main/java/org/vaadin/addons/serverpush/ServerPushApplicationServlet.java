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
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * {@link ApplicationServlet} that uses its own {@link WebApplicationContext} to provide {@link ServerPushCommunicationManager}
 * which pushes updates to all clients on repaint request.  This servlet automatically adds an instance of {@link ServerPush} configured
 * for the current request context path to the {@link Application}'s main window  after the application has been started.
 * @author Mark Thomas
 * @version 1.0.5
 * @since 1.0.0
 */
public class ServerPushApplicationServlet extends ApplicationServlet {

    @Override
    protected Application getNewApplication(final HttpServletRequest request) throws ServletException {
        final Application application = super.getNewApplication(request);
        final String contextPath = request.getContextPath();
        if (application != null) {
            new Thread() {
                public void run() {
                    while (!application.isRunning()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                    synchronized (application) {
                        application.getMainWindow().addComponent(new ServerPush(contextPath));
                    }
                }
            }.start();
        }
        return application;
    }

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
