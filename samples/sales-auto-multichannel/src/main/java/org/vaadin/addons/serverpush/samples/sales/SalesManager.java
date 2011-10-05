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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SalesManager extends AbstractManager<Sale, ManagerListener<Sale>> {

    private static final SalesManager INSTANCE = new SalesManager();

    private final String[] names = new String[] {
      "Bill", "Fred", "Sue", "Joe", "Bob", "Alice"
    };
    private final String[] products = new String[] {
      "Soap", "Laundry Detergent", "Dishwasher Detergent", "Broom", "Mop", "Dustpan"
    };

    private final Set<User> users = new HashSet<User>();

    private SalesManager() {
        startMockSalesThread();
    }

    private void startMockSalesThread() {
        new Thread() {
            public void run() {
                long time = System.currentTimeMillis();
                long max = time + (1000 * 60 * 10);
                while (time < max) {
                    List<User> copy = new ArrayList<User>(users);
                    if (!copy.isEmpty()) {
                        int size = copy.size();
                        User rep = copy.get(size > 1 ? ((int)(Math.random() * 100) % size) : 0);
                        Sale sale = new Sale();
                        sale.setSalesRep(rep);
                        sale.setName(names[(int)(Math.random() * 100) % names.length]);
                        sale.setProduct(products[(int)(Math.random() * 100) % products.length]);
                        String price = DecimalFormat.getCurrencyInstance().format((Math.random() * 100d));
                        sale.setPrice(price);
                        sale.setQuantity(Math.max(1, (int)(Math.random() * 100) % products.length));
                        fireObjectAdded(sale);
                    }

                    try {
                        sleep((long)(Math.random() * 10000));
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    time = System.currentTimeMillis();
                }
            }
        }.start();
    }

    public static SalesManager getInstance() {
        return INSTANCE;
    }

    public void registerUser(User user) {
        this.users.add(user);
    }
}
