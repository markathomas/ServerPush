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

package org.vaadin.addons.serverpush.example;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import org.vaadin.addons.serverpush.ServerPush;

/**
 * Demo class for server push widget
 */
public class ServerPushDemo extends Application {

    private ServerPush push = new ServerPush();

    @Override
    public void init() {
        Window mainWindow = new Window("ServerPush Application");
        setMainWindow(mainWindow);

        // Add the push component
        mainWindow.addComponent(this.push);

        // Add a button for starting background work
        getMainWindow().addComponent(
            new Button("Start background thread", new ClickListener() {
                public void buttonClick(ClickEvent event) {
                    getMainWindow().addComponent(new Label("Waiting for background thread to complete..."));
                    new BackgroundThread().start();
                }
            }));
    }

    public class BackgroundThread extends Thread {

        @Override
        public void run() {
            // Simulate background work
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            // Update UI
            synchronized (ServerPushDemo.this) {
                getMainWindow().addComponent(new Label("This label was pushed to client"));
            }

            // Push the changes
            ServerPushDemo.this.push.push();
        }
    }
}
