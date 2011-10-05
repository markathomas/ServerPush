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

import com.vaadin.data.Container;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class ChatLayout extends VerticalLayout {

    public ChatLayout(final User user, final User from) {
        setSizeFull();
        Table table = new Table();
        table.setSizeFull();
        table.setContainerDataSource(new MessageContainer(user, from));
        table.addListener(new Container.ItemSetChangeListener() {
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                ((Pushable)getApplication()).push();
            }
        });
        table.setVisibleColumns(new String[] {
          "from", "to", "received", "message"
        });

        addComponent(table);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
        final TextArea textArea = new TextArea();
        textArea.setRows(5);
        textArea.setSizeFull();
        textArea.setWordwrap(true);
        textArea.setValue("");
        hl.addComponent(textArea);

        Button sendButton = new Button("Send");
        sendButton.setWidth("120px");
        sendButton.setImmediate(true);
        sendButton.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Message msg = new Message();
                msg.setFrom(from);
                msg.setTo(user);
                msg.setMessage(textArea.getValue().toString());
                MessageManager.getInstance().addMessage(msg);
                textArea.setValue("");
            }
        });
        hl.addComponent(sendButton);
        hl.setComponentAlignment(sendButton, Alignment.BOTTOM_RIGHT);
        hl.setExpandRatio(textArea, 1);
        addComponent(hl);

        setExpandRatio(table, 1);
    }
}
