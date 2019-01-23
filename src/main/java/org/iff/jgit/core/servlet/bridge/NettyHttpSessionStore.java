/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NettyHttpSessionStore
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyHttpSessionStore {

    public static ConcurrentHashMap<String, NettyHttpSession> sessions = new ConcurrentHashMap<>();

    public NettyHttpSession createSession() {
        String sessionId = generateNewSessionId();
        Logger.debug(FCS.get("Creating new session with id {0}", sessionId));
        NettyHttpSession session = new NettyHttpSession(sessionId);
        sessions.put(sessionId, session);
        return session;
    }

    public void destroySession(String sessionId) {
        Logger.debug(FCS.get("Destroying session with id {0}", sessionId));
        sessions.remove(sessionId);
    }

    public NettyHttpSession findSession(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        return sessions.get(sessionId);
    }

    protected String generateNewSessionId() {
        return UUID.randomUUID().toString();
    }

    public void destroyInactiveSessions() {
        for (Map.Entry<String, NettyHttpSession> entry : sessions.entrySet()) {
            NettyHttpSession session = entry.getValue();
            if (session.getMaxInactiveInterval() < 0) {
                continue;
            }
            long currentMillis = System.currentTimeMillis();
            if (currentMillis - session.getLastAccessedTime() > session.getMaxInactiveInterval() * 1000) {
                destroySession(entry.getKey());
            }
        }
    }
}
