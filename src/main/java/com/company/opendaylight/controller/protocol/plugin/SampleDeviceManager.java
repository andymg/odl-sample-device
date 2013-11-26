/*
 * Copyright (c) 2013, project authors and/or its affiliates.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of the authors or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.company.opendaylight.controller.protocol.plugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David Bainbridge <davidk.bainbridge@gmail.com>
 * 
 */
public class SampleDeviceManager implements Runnable {
    public enum State {
        INITIALIZED, RUN, ERROR, INTERRUPTED, STOP
    }

    final static private Logger log = LoggerFactory
            .getLogger(SampleDeviceManager.class);
    final private EventListenerList eventListeners;
    final private int port;
    private volatile Selector selector = null;
    private volatile ServerSocketChannel channel = null;
    private volatile SelectionKey serverKey = null;
    private volatile boolean threadStopped = false;
    private volatile State state = State.INITIALIZED;

    public SampleDeviceManager() {
        eventListeners = new EventListenerList();
        port = 9876;
    }

    public void run() {
        try {
            state = State.RUN;
            try {
                selector = Selector.open();
                channel = ServerSocketChannel.open();
                channel.bind(new InetSocketAddress(port));
                channel.configureBlocking(false);
                serverKey = channel.register(selector, SelectionKey.OP_ACCEPT,
                        null);

            } catch (IOException e) {
                log.error(
                        "Failed to open a channel on which to listen for incoming requests from the device.",
                        e);
                state = State.ERROR;
                return;
            }

            SocketChannel client = null;
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int readCount = 0;
            while (!threadStopped) {
                try {
                    if (selector.select() > 0) {
                        for (SelectionKey key : selector.selectedKeys()) {
                            if (key == serverKey) {
                                client = channel.accept();
                                if (client != null) {
                                    client.configureBlocking(false);
                                    client.register(selector,
                                            SelectionKey.OP_READ, null);
                                    log.debug("ACCEPT: {}:{}", client.socket()
                                            .getInetAddress().getHostAddress(),
                                            client.socket().getPort());
                                }
                            } else {
                                // Must be a client
                                client = (SocketChannel) key.channel();
                                buf.clear();
                                if ((readCount = client.read(buf)) > 0) {
                                    StringBuilder build = new StringBuilder();
                                    for (int i = 0; i < readCount; ++i) {
                                        byte b = buf.get(i);
                                        log.debug("GOT: {}", b);
                                        build.append((char) buf.get(i));
                                    }
                                    log.info("READ FROM CLIENT({}): {}",
                                            readCount, build.toString());
                                } else {
                                    log.debug("DISCONNECT: {}:{}", client
                                            .socket().getInetAddress()
                                            .getHostAddress(), client.socket()
                                            .getPort());
                                    key.cancel();
                                    client.close();
                                }
                            }
                        }
                        selector.selectedKeys().clear();
                    }
                } catch (Exception e) {
                    log.error("Unexpected error.", e);
                    state = State.ERROR;
                }
            }
        } finally {
            // Close all connections
            for (SelectionKey key : selector.keys()) {
                try {
                    key.cancel();
                    key.channel().close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                log.debug("CLOSING LISTENER");
                channel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (state == State.RUN) {
                state = State.STOP;
            }
        }
    }

    public void stop() {
        threadStopped = true;
        for (SelectionKey key : selector.keys()) {
            try {
                key.cancel();
                key.channel().close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            log.debug("CLOSING LISTENER");
            channel.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (state == State.RUN) {
            state = State.STOP;
        }
    }

    /**
     * @param connectionService
     */
    public void addNewDeviceListener(NewDeviceListener listener) {
        eventListeners.add(NewDeviceListener.class, listener);

    }

    public void removeNewDeviceListener(NewDeviceListener listener) {
        eventListeners.remove(NewDeviceListener.class, listener);
    }

    public void fireNewDeviceListners(final NewDeviceEvent event) {
        for (NewDeviceListener listener : eventListeners
                .getListeners(NewDeviceListener.class)) {
            listener.newDevice(event);
        }
    }
}
