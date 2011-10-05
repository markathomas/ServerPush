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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class OnlineUsersManager {

    private static final OnlineUsersManager INSTANCE = new OnlineUsersManager();

    private final Map<User, Application> users = new WeakHashMap<User, Application>();
    private final Set<OnlineUserListener> listeners = new HashSet<OnlineUserListener>();

    private OnlineUsersManager() {
        //
    }

    public static OnlineUsersManager getInstance() {
        return INSTANCE;
    }

    public void addListener(OnlineUserListener listener) {
        if (listener != null)
            this.listeners.add(listener);
    }

    public boolean removeListener(OnlineUserListener listener) {
        return this.listeners.remove(listener);
    }

    public Collection<User> getOnlineUsers() {
        return Collections.unmodifiableSet(this.users.keySet());
    }

    public void registerUser(User user, Application app) {
        if (!this.users.containsKey(user)) {
            this.users.put(user, app);
            fireUserOnlineEvent(user);
        }
    }

    public void deregisterUser(User user) {
        if (this.users.containsKey(user)) {
            this.users.remove(user);
            fireUserOfflineEvent(user);
        }
    }

    private void fireUserOnlineEvent(User user) {
        for (OnlineUserListener listener : this.listeners) {
            listener.userOnline(user);
        }
    }

    private void fireUserOfflineEvent(User user) {
        for (OnlineUserListener listener : this.listeners) {
            listener.userOffline(user);
        }
    }

    public static interface OnlineUserListener {
        void userOnline(User user);
        void userOffline(User user);
    }
}
