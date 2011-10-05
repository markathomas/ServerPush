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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractManager<T, V extends ManagerListener<T>> {

    protected enum Change { ADD, REMOVE }

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Set<V> listeners = new HashSet<V>();
    private final Set<T> objects = new HashSet<T>();

    public Set<T> getObjects() {
        return Collections.unmodifiableSet(this.objects);
    }

    public void addListener(V listener) {
        if (listener != null) {
            synchronized (this.listeners) {
                this.listeners.add(listener);
            }
        }
    }

    public boolean removeListener(V listener) {
        boolean removed;
        synchronized (this.listeners) {
            removed = this.listeners.remove(listener);
        }
        return removed;
    }

    private void notifyListeners(final T t, final Change change) {
        final Set<V> copy = new HashSet<V>();
        synchronized (this.listeners) {
            copy.addAll(this.listeners);
        }
        this.executorService.submit(new Runnable() {
            public void run() {
                for (ManagerListener<T> listener : copy) {
                    if (change == Change.ADD)
                        listener.objectAdded(t);
                    else
                        listener.objectRemoved(t);
                }
            }
        });
    }

    protected void fireObjectAdded(T t) {
        synchronized (this.objects) {
            this.objects.add(t);
        }
        this.notifyListeners(t, Change.ADD);
    }

    protected void fireObjectRemoved(T t) {
        synchronized (this.objects) {
            this.objects.remove(t);
        }
        this.notifyListeners(t, Change.REMOVE);
    }
}
