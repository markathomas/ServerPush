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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public final class OnlineUsersManager extends AbstractManager<User, ManagerListener<User>> {

    private static final OnlineUsersManager INSTANCE = new OnlineUsersManager();

    private final Map<User, Application> users = new WeakHashMap<User, Application>();

    private OnlineUsersManager() {
        //
    }

    public static OnlineUsersManager getInstance() {
        return INSTANCE;
    }

    public Collection<User> getOnlineUsers() {
        return Collections.unmodifiableSet(this.users.keySet());
    }

    public Application getApplication(User user) {
        return this.users.get(user);
    }

    public void registerUser(User user, Application app) {
        if (!this.users.containsKey(user)) {
            this.users.put(user, app);
            fireObjectAdded(user);
        }
    }

    public void deregisterUser(User user) {
        if (this.users.containsKey(user)) {
            this.users.remove(user);
            fireObjectRemoved(user);
        }
    }
}
