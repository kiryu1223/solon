package org.noear.solon.extend.xsocket;

import org.noear.solon.core.message.Session;

public abstract class SessionFactory {
    private static SessionFactory instance;

    public static void setInstance(SessionFactory factory) {
        instance = factory;
    }

    protected abstract Session getSession(Object conn);
    protected abstract void removeSession(Object conn);

    protected abstract Session createSession(String host, int port, boolean autoReconnect);




    public static Session get(Object conn) {
        if (instance == null) {
            throw new IllegalArgumentException("XSessionFactory uninitialized");
        }

        return instance.getSession(conn);
    }

    public static void remove(Object conn) {
        if (instance == null) {
            throw new IllegalArgumentException("XSessionFactory uninitialized");
        }

        instance.removeSession(conn);
    }

    public static Session create(String host, int port, boolean autoReconnect) {
        if (instance == null) {
            throw new IllegalArgumentException("XSessionFactory uninitialized");
        }

        return instance.createSession(host, port, autoReconnect);
    }
}
