/*******************************************************************************
 * Copyright (c) Sep 25, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.netty.server.handlers;

import org.iff.netty.server.ProcessContext;

/**
 * <pre>
 *     Usage:
 *     step 0. call outside: matchUri(), getOrder().
 *     step 1. create(), should return new instance.
 *     step 2. process(), should not throw exception.
 *     step 3. done(), should return true or false or throw exception.
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 25, 2016
 */
public interface ActionHandler {

    /**
     * step 2: create the new instance for process.
     *
     * @return
     */
    ActionHandler create();

    /**
     * step 3: process the request.
     *
     * @param ctx
     * @return
     */
    ActionHandler process(ProcessContext ctx);

    /**
     * step 1: match the request uri.
     *
     * @param uri
     * @return
     */
    boolean matchUri(String uri);

    /**
     * step 0: sort the ActionHandlers.
     *
     * @return order asc
     */
    int getOrder();

    /**
     * step 4: call done when process is completed.
     *
     * @return true: block the after ActionHandler, false: continue the after ActionHandler.
     */
    boolean done();
}