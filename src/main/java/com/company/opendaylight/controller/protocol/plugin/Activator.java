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

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.dm.Component;
import org.opendaylight.controller.sal.connection.IPluginInConnectionService;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector.NodeConnectorIDType;
import org.opendaylight.controller.sal.inventory.IPluginInInventoryService;
import org.opendaylight.controller.sal.utils.GlobalConstants;
import org.opendaylight.controller.sal.utils.INodeConnectorFactory;
import org.opendaylight.controller.sal.utils.INodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends ComponentActivatorAbstractBase {
    protected static final Logger log = LoggerFactory
            .getLogger(Activator.class);

    public static final String NODE_TYPE = "SPL";
    public static final String NODE_CONNECTOR_TYPE = "SPL";

    private boolean nodeTypeRegistered = false;

    private boolean nodeConnectorTypeRegistered = false;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase#init
     * ()
     */
    @Override
    protected void init() {
        super.init();
        nodeTypeRegistered = Node.NodeIDType.registerIDType(NODE_TYPE,
                String.class);
        nodeConnectorTypeRegistered = NodeConnectorIDType.registerIDType(
                NODE_CONNECTOR_TYPE, String.class, NODE_TYPE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase#destroy
     * ()
     */
    @Override
    public void destroy() {
        if (nodeTypeRegistered) {
            Node.NodeIDType.unRegisterIDType(NODE_TYPE);
        }

        if (nodeConnectorTypeRegistered) {
            NodeConnectorIDType.unRegisterIDType(NODE_CONNECTOR_TYPE);
        }
        super.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase#
     * getGlobalImplementations()
     */
    @Override
    protected Object[] getGlobalImplementations() {
        Object[] resources = { NodeFactory.class, NodeConnectorFactory.class,
                InventoryService.class, ConnectionService.class };
        return resources;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase#
     * configureGlobalInstance(org.apache.felix.dm.Component, java.lang.Object)
     */
    @Override
    protected void configureGlobalInstance(Component c, Object imp) {
        log.debug("GLOBAL CONFIG: {}", imp.toString());
        if (imp.equals(ConnectionService.class)) {
            Dictionary<String, Object> properties = new Hashtable<String, Object>();
            properties.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(),
                    Activator.NODE_TYPE);
            c.setInterface(IPluginInConnectionService.class.getName(),
                    properties);
        } else if (imp.equals(InventoryService.class)) {
            Dictionary<String, Object> properties = new Hashtable<String, Object>();
            properties.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(),
                    Activator.NODE_TYPE);
            c.setInterface(IPluginInInventoryService.class.getName(),
                    properties);
        } else if (imp.equals(NodeFactory.class)) {
            Dictionary<String, Object> properties = new Hashtable<String, Object>();
            properties.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(),
                    Activator.NODE_TYPE);
            properties.put("protocolName", Activator.NODE_TYPE);
            c.setInterface(INodeFactory.class.getName(), properties);
        } else if (imp.equals(NodeConnectorFactory.class)) {
            Dictionary<String, Object> properties = new Hashtable<String, Object>();
            properties.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(),
                    Activator.NODE_TYPE);
            properties.put("protocolName", Activator.NODE_CONNECTOR_TYPE);
            c.setInterface(INodeConnectorFactory.class.getName(), properties);
        }
    }
}