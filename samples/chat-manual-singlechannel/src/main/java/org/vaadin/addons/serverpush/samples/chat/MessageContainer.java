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

public class MessageContainer extends BeanItemContainer<Message> implements ManagerListener<Message> {

    private final OnlineUsersManager manager = OnlineUsersManager.getInstance();

    public MessageContainer(final User user, final User from) {
        super(Message.class);
        this.addFilter(new Filter() {
            public boolean passesFilter(Object itemId, Item item) {
                Message msg = (Message)itemId;
                return (from.equals(msg.getFrom()) && user.equals(msg.getTo()))
                  || (from.equals(msg.getTo()) && user.equals(msg.getFrom()));
            }

            public boolean appliesToProperty(Object propertyId) {
                return propertyId.equals("from") || propertyId.equals("to");
            }
        });
        MessageManager manager = MessageManager.getInstance();
        manager.addListener(this);
        for (Message m : manager.getObjects())
            this.addBean(m);
    }

    public void objectAdded(Message message) {
        Application app = this.manager.getApplication(message.getTo());
        if (app != null) {
            synchronized (app) {
                this.addBean(message);
            }
        }
    }

    public void objectRemoved(Message message) {
        Application app = this.manager.getApplication(message.getTo());
        if (app != null) {
            synchronized (app) {
                this.removeItem(message);
            }
        }
    }
}
