package org.noear.solon.boot.jdksocket;

import org.noear.solon.Utils;
import org.noear.solon.core.message.Session;
import org.noear.solon.extend.xsocket.SessionFactory;

import java.net.Socket;

class _SessionFactoryImpl extends SessionFactory {
    @Override
    protected Session getSession(Object conn) {
        if (conn instanceof Socket) {
            return _SocketSession.get((Socket) conn);
        } else {
            throw new IllegalArgumentException("This conn requires a socket type");
        }
    }

    @Override
    protected void removeSession(Object conn) {
        if (conn instanceof Socket) {
            _SocketSession.remove((Socket) conn);
        } else {
            throw new IllegalArgumentException("This conn requires a socket type");
        }
    }

    @Override
    protected Session createSession(String host, int port, boolean autoReconnect) {
        try {
            BioClient bioClient = new BioClient(host, port);

            return new _SocketSession(bioClient, autoReconnect);
        } catch (Exception ex) {
            throw Utils.throwableWrap(ex);
        }
    }
}
