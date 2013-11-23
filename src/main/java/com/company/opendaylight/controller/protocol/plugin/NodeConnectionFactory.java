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

import org.opendaylight.controller.sal.core.ConstructionException;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.utils.INodeConnectorFactory;
import org.opendaylight.controller.sal.utils.INodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David Bainbridge <davidk.bainbridge@gmail.com>
 * 
 */
public class NodeConnectionFactory implements INodeConnectorFactory {
    protected static final Logger log = LoggerFactory
            .getLogger(NodeConnectionFactory.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opendaylight.controller.sal.utils.INodeConnectorFactory#fromStringNoNode
     * (java.lang.String, java.lang.String,
     * org.opendaylight.controller.sal.core.Node)
     */
    @Override
    public NodeConnector fromStringNoNode(String typeStr, String id, Node node) {
        try {
            if (Activator.NODE_CONNECTOR_TYPE.equals(typeStr)) {
                return new NodeConnector(Activator.NODE_CONNECTOR_TYPE, id,
                        node);
            }
        } catch (ConstructionException e) {
            log.error(
                    "Failed to create requested node connection of type {} with id {} with exception {}",
                    typeStr, id, e);
            return null;
        }
        log.error(
                "Request to create node connection type {}, not compatible with factory",
                typeStr);
        return null;
    }

}
