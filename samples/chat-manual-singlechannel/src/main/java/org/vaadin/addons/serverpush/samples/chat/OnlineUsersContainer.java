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
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

public class OnlineUsersContainer extends BeanItemContainer<User> implements ManagerListener<User> {

    private final OnlineUsersManager manager = OnlineUsersManager.getInstance();

    public OnlineUsersContainer(final User user) {
        super(User.class);
        this.addFilter(new Filter() {
            public boolean passesFilter(Object itemId, Item item) {
                return !user.equals(itemId);
            }

            public boolean appliesToProperty(Object propertyId) {
                return "username".equals(propertyId);
            }
        });

        this.manager.addListener(this);
        for (User u : this.manager.getOnlineUsers())
            this.addBean(u);
    }

    public void objectAdded(User user) {
        Application app = this.manager.getApplication(user);
        if (app != null) {
            synchronized (app) {
                this.addBean(user);
            }
        }
    }

    public void objectRemoved(User user) {
        Application app = this.manager.getApplication(user);
        if (app != null) {
            synchronized (app) {
                this.removeItem(user);
            }
        }
    }
}
