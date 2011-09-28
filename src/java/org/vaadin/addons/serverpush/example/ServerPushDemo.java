
/*
 * Copyright (C) 2011 Elihu, LLC. All rights reserved.
 *
 * $Id$
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
