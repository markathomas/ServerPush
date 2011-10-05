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

package org.vaadin.addons.serverpush.samples.sales;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SalesApplication extends Application {

    private final Label header = new Label();
    private final Table table = new Table();

    public void init() {
        Window mainWindow = new Window("Sales Application");
        setMainWindow(mainWindow);

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
        final Label label = new Label("<h1>Sales Application</h1><hr/>", Label.CONTENT_XHTML);
        hl.addComponent(label);
        hl.addComponent(this.header);
        hl.setComponentAlignment(this.header, Alignment.MIDDLE_RIGHT);
        hl.setExpandRatio(label, 1);
        vl.addComponent(hl);

        this.table.setSizeFull();
        vl.addComponent(this.table);
        vl.setExpandRatio(this.table, 1);

        mainWindow.setContent(vl);

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
        this.table.setContainerDataSource(new UserSalesContainer(user, this));
        this.header.setValue("Welcome " + username + "!");
    }
}
