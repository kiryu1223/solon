package org.noear.solon.extend.cloud;

import org.noear.solon.SolonApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.Plugin;
import org.noear.solon.extend.cloud.annotation.CloudConfig;
import org.noear.solon.extend.cloud.annotation.CloudDiscovery;
import org.noear.solon.extend.cloud.annotation.CloudEvent;
import org.noear.solon.extend.cloud.impl.CloudBeanInjector;

/**
 * @author noear
 * @since 1.2
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(SolonApp app) {
        Aop.context().beanBuilderAdd(CloudConfig.class, (clz, bw, anno) -> {
            if (bw.raw() instanceof CloudConfigHandler) {
                CloudManager.register(anno, bw.raw());
            }
        });

        Aop.context().beanBuilderAdd(CloudDiscovery.class, (clz, bw, anno) -> {
            if (bw.raw() instanceof CloudDiscoveryHandler) {
                CloudManager.register(anno, bw.raw());
            }
        });

        Aop.context().beanBuilderAdd(CloudEvent.class, (clz, bw, anno) -> {
            if (bw.raw() instanceof CloudEventHandler) {
                CloudManager.register(anno, bw.raw());
            }
        });

        Aop.context().beanInjectorAdd(CloudConfig.class, CloudBeanInjector.instance);

        Aop.context().beanOnloaded(() -> {
            if (CloudManager.configService() != null) {
                CloudManager.configHandlerMap.forEach((anno, handler) -> {
                    String[] ss = anno.value().split("/");
                    if (ss.length > 1) {
                        CloudManager.configService().attention(ss[0], ss[1], (group, cfg) -> {
                            handler.handler(cfg);
                        });
                    }
                });
            }

            if (CloudManager.registerService() != null) {
                CloudManager.discoveryHandlerMap.forEach((anno, handler) -> {
                    CloudManager.registerService().attention(anno.value(), (dis) -> {
                        handler.handler(dis);
                    });
                });
            }
        });
    }
}
