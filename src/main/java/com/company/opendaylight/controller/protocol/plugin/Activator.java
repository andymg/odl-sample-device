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

import org.apache.felix.dm.Component;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector.NodeConnectorIDType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends ComponentActivatorAbstractBase {
    protected static final Logger logger = LoggerFactory
            .getLogger(Activator.class);

    public static final String NODE_TYPE = "SPL";
    public static final String NODE_CONNECTOR_TYPE = "SPL";

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
        Node.NodeIDType.registerIDType(NODE_TYPE, String.class);
        NodeConnectorIDType.registerIDType(NODE_CONNECTOR_TYPE, String.class,
                NODE_TYPE);
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
        Node.NodeIDType.unRegisterIDType(NODE_TYPE);
        NodeConnectorIDType.unRegisterIDType(NODE_CONNECTOR_TYPE);
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
        Object[] resources = {};
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
        super.configureGlobalInstance(c, imp);
    }

}