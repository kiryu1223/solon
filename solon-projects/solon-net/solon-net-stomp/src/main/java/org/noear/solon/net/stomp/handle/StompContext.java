package org.noear.solon.net.stomp.handle;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.core.util.KeyValue;
import org.noear.solon.core.util.MultiMap;
import org.noear.solon.net.stomp.Message;
import org.noear.solon.net.stomp.StompBrokerSender;
import org.noear.solon.net.stomp.impl.Headers;
import org.noear.solon.net.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noear
 * @since 3.0
 */
public class StompContext extends ContextEmpty {
    private static final Logger log = LoggerFactory.getLogger(StompContext.class);

    private WebSocket session;
    private Message message;
    private String destination;
    private StompBrokerSender messageSender;

    public StompContext(WebSocket session, Message message, String destination, StompBrokerSender brokerSender) {
        this.session = session;
        this.message = message;
        this.destination = destination;
        this.messageSender = brokerSender;//session.attr("STOMP_MESSAGE_SENDER");

        attrSet(org.noear.solon.core.Constants.ATTR_RETURN_HANDLER, StompReturnHandler.getInstance());
    }

    public WebSocket getSession() {
        return session;
    }

    public Message getMessage() {
        return message;
    }

    public StompBrokerSender getMessageSender() {
        return messageSender;
    }

    @Override
    public String sessionId() {
        return session.id();
    }

    @Override
    public String contentType() {
        return message.getHeader(Headers.CONTENT_TYPE);
    }

    @Override
    public String path() {
        return destination;
    }

    @Override
    public MultiMap<String> headerMap() {
        if (headerMap == null) {
            headerMap = new MultiMap<>();

            for (KeyValue<String> kv : message.getHeaderAll()) {
                headerMap.add(kv.getKey(), kv.getValue());
            }
        }

        return headerMap;
    }

    @Override
    public Object pull(Class<?> clz) {
        if (Message.class.isAssignableFrom(clz)) {
            return message;
        }

        if (WebSocket.class.isAssignableFrom(clz)) {
            return session;
        }

        return null;
    }

    /**
     * <code>
     * new StompContext(...).tryHandle();
     * </code>
     */
    public void tryHandle() {
        try {
            Handler handler = Solon.app().router().matchMain(this);
            if (handler != null) {
                handler.handle(this);
            }
        } catch (Throwable ex) {
            log.warn(ex.getMessage(), ex);
        }
    }
}