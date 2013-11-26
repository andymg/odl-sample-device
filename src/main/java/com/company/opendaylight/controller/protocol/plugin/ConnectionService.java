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

import java.util.Map;

import org.opendaylight.controller.sal.connection.ConnectionConstants;
import org.opendaylight.controller.sal.connection.IPluginInConnectionService;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David Bainbridge <davidk.bainbridge@gmail.com>
 * 
 */
public class ConnectionService implements IPluginInConnectionService,
        NewDeviceListener {
    private static final Logger log = LoggerFactory
            .getLogger(ConnectionService.class);
    private Thread deviceManagerThread = null;
    private SampleDeviceManager deviceManager = null;

    /**
     * Called by the OSGi dependency manager once all the components
     * dependencies are met
     */
    public void init() {
        deviceManager = new SampleDeviceManager();
        deviceManager.addNewDeviceListener(this);
    }

    /**
     * Called by the OSGi dependency manager when the component no longer has
     * all available dependencies or it is being shutdown.
     */
    public void destroy() {

    }

    /**
     * Called by the OSGi dependency manager when all the components
     * dependencies are met and the component is being started. This is called
     * after init.
     */
    public void start() {
        /*
         * Create and start the thread that will listen for a new "sample"
         * device to make contact. This could just as easily do a ping sweep or
         * leverage persistence as well.
         */
        synchronized (this) {
            deviceManagerThread = new Thread(deviceManager);
            deviceManagerThread.start();
        }
    }

    /**
     * Called by the OSGi dependency manager before exported services are
     * unregistered and the c
     */
    public void stop() {
        // Stop the device manager. This will be done, by calling the stop
        // method on the device manager.
        deviceManager.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opendaylight.controller.sal.connection.IPluginInConnectionService
     * #connect(java.lang.String, java.util.Map)
     */
    @Override
    public Node connect(String arg0, Map<ConnectionConstants, String> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opendaylight.controller.sal.connection.IPluginInConnectionService
     * #disconnect(org.opendaylight.controller.sal.core.Node)
     */
    @Override
    public Status disconnect(Node arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opendaylight.controller.sal.connection.IPluginInConnectionService
     * #notifyClusterViewChanged()
     */
    @Override
    public void notifyClusterViewChanged() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opendaylight.controller.sal.connection.IPluginInConnectionService
     * #notifyNodeDisconnectFromMaster
     * (org.opendaylight.controller.sal.core.Node)
     */
    @Override
    public void notifyNodeDisconnectFromMaster(Node arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.company.opendaylight.controller.protocol.plugin.NewDeviceListener
     * #newDevice
     * (com.company.opendaylight.controller.protocol.plugin.NewDeviceEvent)
     */
    @Override
    public void newDevice(NewDeviceEvent event) {
        log.debug("NEW SAMPLE DEVICE ADDED");
    }
}
