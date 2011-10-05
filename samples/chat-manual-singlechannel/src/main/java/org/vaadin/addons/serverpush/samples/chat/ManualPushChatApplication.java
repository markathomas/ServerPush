/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.vaadin.addons.serverpush.samples.chat;

import com.vaadin.Application;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import org.vaadin.addons.serverpush.ServerPush;

public abstract class ManualPushChatApplication extends Application implements Pushable, ManagerListener<Message> {

    private final ServerPush pusher;
    private final TabSheet tabSheet = new TabSheet();
    private final ComboBox comboBox = new ComboBox("Select buddy:");
    private final Label header = new Label("<h1>Chat Application</h1><hr/>", Label.CONTENT_XHTML);

    protected ManualPushChatApplication(String contextPath) {
        this.pusher = new ServerPush(contextPath);
    }

    public void init() {
        Window mainWindow = new Window("Chat Application");
        setMainWindow(mainWindow);

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();

        vl.addComponent(this.header);

        this.comboBox.setWidth("400px");
        this.comboBox.addListener(new Container.ItemSetChangeListener() {
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                push();
            }
        });
        this.comboBox.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                fireNewTab((User)event.getProperty().getValue(), (User)getUser());
            }
        });
        this.comboBox.setItemCaptionPropertyId("username");
        this.comboBox.setImmediate(true);
        vl.addComponent(this.comboBox);

        this.tabSheet.setSizeFull();
        vl.addComponent(this.tabSheet);
        vl.setExpandRatio(this.tabSheet, 1);

        mainWindow.setContent(vl);
        mainWindow.addComponent(this.pusher);
        mainWindow.addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                OnlineUsersManager.getInstance().deregisterUser((User)getUser());
            }
        });

        fireLoginWindow();
    }

    private void fireLoginWindow() {
        final Window window = new Window("Login");
        window.setModal(true);
        window.setWidth("640px");
        window.setHeight("480px");
        LoginForm loginForm = new LoginForm();
        loginForm.setSizeFull();
        loginForm.addListener(new LoginForm.LoginListener() {
            public void onLogin(LoginForm.LoginEvent event) {
                doLogin(event);
                getMainWindow().removeWindow(window);
            }
        });
        window.setContent(loginForm);
        getMainWindow().addWindow(window);
        window.bringToFront();
    }

    private void doLogin(LoginForm.LoginEvent event) {
        final String username = event.getLoginParameter("username");
        final User user = new User(username);
        setUser(user);
        this.comboBox.setContainerDataSource(new OnlineUsersContainer(user));
        OnlineUsersManager.getInstance().registerUser(user, this);
        this.header.setValue(this.header.getValue() + "<br/><p>Welcome " + username + "!</p><br/>");
        MessageManager.getInstance().addListener(this);
    }

    private void fireNewTab(User user, User from) {
        if (user == null)
            return;

        final String to = user.getUsername();
        boolean exists = false;
        for (int i = 0; i < this.tabSheet.getComponentCount(); i++) {
            TabSheet.Tab tab = this.tabSheet.getTab(i);
            if (to.equals(tab.getCaption())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            TabSheet.Tab tab = this.tabSheet.addTab(new ChatLayout(user, from), to);
            tab.setClosable(true);
            tab.setDescription("Chatting with " + to);
        }
    }

    public synchronized void objectAdded(Message message) {
        if (message.getTo().getUsername().equals(((User)getUser()).getUsername())) {
            fireNewTab(message.getFrom(), (User)getUser());
        }
    }

    public void objectRemoved(Message message) {
    }

    public void push() {
        this.pusher.push();
    }
}
