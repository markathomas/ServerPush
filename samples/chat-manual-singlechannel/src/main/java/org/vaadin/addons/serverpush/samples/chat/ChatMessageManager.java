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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public final class ChatMessageManager {

    private static final ChatMessageManager INSTANCE = new ChatMessageManager();

    private final Set<ChatMessageListener> listeners = new HashSet<ChatMessageListener>();
    private final Set<Message> memoryLeakingSet = new HashSet<Message>();

    private ChatMessageManager() {
        //
    }

    public static ChatMessageManager getInstance() {
        return INSTANCE;
    }

    public void addListener(ChatMessageListener listener) {
        if (listener != null)
            this.listeners.add(listener);
    }

    public boolean removeListener(ChatMessageListener listener) {
        return this.listeners.remove(listener);
    }

    public Set<Message> getMessages() {
        return Collections.unmodifiableSet(this.memoryLeakingSet);
    }

    public void addMessage(Message message) {
        this.memoryLeakingSet.add(message);
        fireMessageReceived(message);
    }

    private void fireMessageReceived(Message message) {
        for (ChatMessageListener listener : new HashSet<ChatMessageListener>(this.listeners)) {
            listener.messageReceived(message);
        }
    }

    public static interface ChatMessageListener {
        void messageReceived(Message message);
    }
}
